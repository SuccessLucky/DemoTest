package com.kzksmarthome.common.lib.okhttp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.event.EventOfNeedLogin;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.network.NetworkStateMgr;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;
import com.kzksmarthome.common.lib.util.AndroidUtil;
import com.kzksmarthome.common.lib.util.GsonHelper;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.net.ApiHost;
import com.kzksmarthome.common.module.net.HttpCache;
import com.kzksmarthome.common.module.user.UserInfo;
import com.kzksmarthome.lib.R;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kop on 2015/7/21.
 */
public class RequestMgr {

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public final static int CACHE_SIZE = 1024 * 100;

    private static RequestMgr sRequestMgr;
    private static ExecutorService sThreadPoolExecutor = Executors.newFixedThreadPool(3);

    private final OkHttpClient mOkHttpClient;
    private final RequestHeader mRequestHeader;

    private static synchronized RequestMgr getInstance() {
        if (sRequestMgr == null) {
            sRequestMgr = new RequestMgr();
        }
        return sRequestMgr;
    }

    private RequestMgr() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        mOkHttpClient = okHttpClient;
        RequestHeader requestHeader = new RequestHeader();
        Context context = SmartHomeAppLib.getInstance().getContext();
        requestHeader.ch = context.getString(R.string.ch);
        requestHeader.model = android.os.Build.MODEL;
        requestHeader.os_vc = android.os.Build.VERSION.SDK_INT;
        requestHeader.vc = AndroidUtil.getVersionCode(context);
        requestHeader.vn = AndroidUtil.getVersionName(context);
        requestHeader.platform = 1;
        mRequestHeader = requestHeader;
    }

    private void doExecute(final Method method, final String url, final CacheControlMode mode, Object requestBody, Class requestBodyClass, final Class responseBodyClass, final RequestCallback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(ApiHost.DEFAULT_SERVER_HOST + url);
        String reqBodyStr = null;
        RequestParam<Object> param = new RequestParam<Object>();
        //param.body = requestBody;
        if (method == Method.POST) {
            //param.header = genRequestHeader();
            //reqBodyStr = param.toJson(requestBodyClass);
            reqBodyStr = GsonHelper.getGson().toJson(requestBody, requestBodyClass);
            Log.d("doExecute", "POST：" + reqBodyStr);
            RequestBody reqBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, reqBodyStr);
            builder.post(reqBody);
        } else if (method == Method.GET) {
            builder.get();
            reqBodyStr = url;
        } else {
            throw new RuntimeException("Method not support yet!");
        }

        UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        if (userInfo != null && userInfo.token != null) {
            builder.addHeader("Token", userInfo.token);
        } else {
            builder.addHeader("Token", "");
        }
        if (userInfo != null && userInfo.gateway != null) {
            builder.addHeader("Gateway", userInfo.gateway);
        } else {
            builder.addHeader("Gateway", "");
        }
        if (userInfo != null && userInfo.uuid != null) {
            builder.addHeader("_pt", userInfo.uuid);
        } else {
            builder.addHeader("_pt", "");
        }
        builder.addHeader("Content-Type", ApiHost.CONTENTTYPE);

        final Request request = builder.build();
        Log.d("doExecute", "request：" + request.toString());
        final String cacheKey = HttpCache.getCacheKey(url, reqBodyStr);
        switch (mode.mCacheMode) {
            case CacheControlMode.CACHE_LOCAL_ONLY:
                boolean result = tryGetFromCache(cacheKey, url, responseBodyClass, callback);
                if (!result && null != callback) {
                    MainTaskExecutor.runTaskOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onBizFail(null, url, ResponseParam.FAILED_FROM_CACHE_CODE);
                        }
                    });
                }
                return;

            case CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_CACHE:
                boolean result1 = tryGetFromCache(cacheKey, url, responseBodyClass, callback);
                break;

            case CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_IF_EXPIRE_CACHE:
                boolean result2 = tryGetFromCache(cacheKey, url, responseBodyClass, callback);
                if (result2) {
                    return;
                }
                break;
            case CacheControlMode.CACHE_REMOTE_NO_CACHE:
            case CacheControlMode.CACHE_REMOTE_AND_CACHE:
            default: {
                break;
            }
        }
        Call call = mOkHttpClient.newCall(request);
        try {
            final Response response = call.execute();
            if (!response.isSuccessful() && null != callback) {
                MainTaskExecutor.runTaskOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(request, url, new IOException("Unexpected code " + response));
                    }
                });
            } else {
                ResponseParam temp = new ResponseParam();
                final String responseBodyStr = response.body().string();
                try {
                    Log.d("doExecute", url + ";response：" + responseBodyStr);
                    temp.body = GsonHelper.getGson().fromJson(responseBodyStr, responseBodyClass);
                } catch (Exception e) {
                    L.e(e);
                }

                final ResponseParam rspParam = temp;
                if (rspParam != null) {
                    if (CacheControlMode.CACHE_REMOTE_NO_CACHE != mode.mCacheMode && mode.mCacheDuration > 0) {
                        sThreadPoolExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                if (null != responseBodyStr) {
                                    HttpCache.saveCache(cacheKey, responseBodyStr);
                                } else {
                                    HttpCache.removeCache(cacheKey);
                                }
                            }
                        });
                    }
                    BaseResponse baseResponse = (BaseResponse) rspParam.body;
                    if (null != baseResponse && null != baseResponse.getError()
                            && ("1001".equals(baseResponse.getError().getCode()) || "1002".equals(baseResponse.getError().getCode()))) {
                        EventOfNeedLogin event = new EventOfNeedLogin();
                        GjjEventBus.getInstance().post(event);
                    }
                    if (null != callback) {
                        MainTaskExecutor.runTaskOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onBizSuccess(rspParam, url, ResponseParam.SUCCESS_FROM_REMOTE_CODE);
                            }
                        });
                    }

                } else {
                    if (null != callback) {
                        MainTaskExecutor.runTaskOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onBizFail(rspParam, url, ResponseParam.FAILED_FROM_REMOTE_CODE);
                            }
                        });
                    }
                }
            }
        } catch (final Exception e) {
            L.e(e);
            if (null != callback) {
                MainTaskExecutor.runTaskOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(request, url, e);
                    }
                });
            }
        }

    }

    public static void execute(final Method method, final String url, final CacheControlMode mode, final Object param, final Class requestBodyClass, final Class responseBodyClass, final RequestCallback callback) {
        sThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                getInstance().doExecute(method, url, mode, param, requestBodyClass, responseBodyClass, callback);
            }
        });
    }

    private RequestHeader genRequestHeader() {
        RequestHeader requestHeader = mRequestHeader;
        requestHeader.net = NetworkStateMgr.getInstance().getNetworkState().getName();
        UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        if (userInfo != null) {
            requestHeader.token = userInfo.token;
            requestHeader.uuid = userInfo.uuid;
        } else {
            requestHeader.token = null;
            requestHeader.uuid = null;
        }
        return requestHeader;
    }


    /**
     * 缓存处理
     * @param cacheKey 请求key
     * @param url 请求连接
     * @param responseBody 响应实体Class
     * @param callback 回调
     * @return
     */
    private boolean tryGetFromCache(final String cacheKey, final String url, final Class responseBody, final RequestCallback callback) {
        if (!TextUtils.isEmpty(cacheKey)) {
            L.i("RequestMgr tryGetFromCache: cacheKey[%s]", cacheKey);
            String cacheRsp = HttpCache.getCache(cacheKey);
            if(TextUtils.isEmpty(cacheRsp)){
                return false;
            }
            try {
                final ResponseParam rspParam = new ResponseParam();
                rspParam.body  = GsonHelper.getGson().fromJson(cacheRsp, responseBody);
                if (null != callback) {
                    MainTaskExecutor.runTaskOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onBizSuccess(rspParam, url, ResponseParam.SUCCESS_FROM_CACHE_CODE);
                        }
                    });

                }
                return true;
            } catch (Exception e) {
                L.e(e);
            }
        }
        return false;
    }


    public static enum Method {
        GET, POST, PUT, DELETE
    }

}
