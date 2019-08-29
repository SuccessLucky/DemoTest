package com.kzksmarthome.SmartHouseYCT.biz.h5;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Looper;
import android.text.TextUtils;
import android.webkit.WebView;

import com.kzksmarthome.common.lib.task.MainTaskExecutor;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.log.LogModuleName;


/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate 2015-3-9
 *
 */
public class JSBridge {
    private final static Class<?> sJSBridgeImplClass = JSBridgeImpl.class;
    private final static HashMap<String, Method> mBridgedMethodCache = new HashMap<String, Method>();
    public final static String JS_PREFIX = "javascript:";

    public static String callNative(WebView webView, String jsonStr) {
        if (!TextUtils.isEmpty(jsonStr)) {
            try {
                String result = "";
                Object json = new JSONTokener(jsonStr).nextValue();
                if (json instanceof JSONObject) {//旧接口
                    ParameterObject po = new ParameterObject((JSONObject)json);
                    result = callNative(webView, po);
                } else if (json instanceof JSONArray) {//新批量接口
                    JSONArray jsonArray = (JSONArray)json;
                    int size = jsonArray.length();
                    JSONArray resultArray = new JSONArray();
                    for (int i = 0; i < size; i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        ParameterObject po = new ParameterObject(jObj);
                        resultArray.put(i, callNative(webView, po));
                    }
                    result = resultArray.toString();
                }
                return result;
            } catch (JSONException e1) {
                L.e("%s call native error, jsonStr: %s, error:%s", LogModuleName.JSBRIDGE, jsonStr, e1);
            }
        }

        return "";
    }
    
    private static String callNative(WebView webView, ParameterObject po) {
        try {
//            String className = po.getClassName();
            Class<?> clz = sJSBridgeImplClass;

            String methodName = po.getMethodName();
            Method method = mBridgedMethodCache.get(methodName);

            L.d("%s callNative: method: %s, Args: %s", LogModuleName.JSBRIDGE, methodName, po.getArgs());

            if (method == null) {
                if (po.getArgs() != null) {
                    method = clz.getMethod(methodName, WebView.class, JSONObject.class);
                } else {
                    method = clz.getMethod(methodName, WebView.class);
                }

                mBridgedMethodCache.put(methodName, method);
            }

            String result;

            if (method.getParameterTypes().length == 2) {
                result = (String) method.invoke(null, webView, po.getArgs());
            } else {
                result = (String) method.invoke(null, webView);
            }

            return result == null ? "" : result;

        } catch (Exception e) {
            L.e("%s JS call %s, error:%s", LogModuleName.JSBRIDGE, po.getMethodName(), e);
            if (po.getArgs() != null) {
                String callbackId =  Util.getJSONString(po.getArgs(), "callbackId", "");
                if (!TextUtils.isEmpty(callbackId)) {
                    JSONObject resultJson = JSBridgeImpl.genCallbackJson(false, "NotFoundException", "{}");
                    L.d("%s callbackJS callbackId %s %s ", LogModuleName.JSBRIDGE, callbackId, resultJson);
                    JSBridge.callbackJS((Browser)webView, callbackId, resultJson);
                }
                return callbackId;
            }
        }
        return "";
    }

    public static void callJSFunction(final Browser webview, String funName, String args) {
        StringBuilder sb = Util.getThreadSafeStringBuilder().append(funName).append('(');

        if (args != null)
            sb.append(args);

        sb.append(')');
        callJS(webview, sb.toString());
    }

    public static void callJS(final Browser webview, final String js) {
        if (webview == null)
            return;

        final String url = JS_PREFIX + js;
        
        L.d("%s callNative callback callJS %s", LogModuleName.JSBRIDGE, url);

        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            webview.loadUrl(url);

        } else {
            MainTaskExecutor.runTaskOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webview.loadUrl(url);
                }
            });
        }
    }

    public static void callbackJS(Browser webview, String callbackId, JSONObject jsonObject) {
        StringBuilder sb = Util.getThreadSafeStringBuilder().append(JSBridgeConstant.isCallbackExsistString);
        sb.append(callbackId).append(',').append(jsonObject.toString()).append(')');
        callJS(webview, sb.toString());
    }

    public static void callbackJS(Browser webview, String callbackId, String str) {
        StringBuilder sb = Util.getThreadSafeStringBuilder().append(JSBridgeConstant.isCallbackExsistString);
        sb.append(callbackId).append(',').append(str).append(')');
        callJS(webview, sb.toString());
    }

    public static void callbackJS(Browser webview, String callbackId) {
        StringBuilder sb = Util.getThreadSafeStringBuilder().append(JSBridgeConstant.isCallbackExsistString).append(callbackId).append(')');
        callJS(webview, sb.toString());
    }

    public static void callbackEvent(Browser webview, final String event, final String json) {
        if (webview == null) {
            return;
        }
        L.d("%s webview: %s, callbackEvent: %s, json: %s", LogModuleName.JSBRIDGE, webview.hashCode(), event, json);
        //注册事件时传过来的Object，执行的时候需要传回去
        Object eventObj = ((Browser) webview).getEventObj(event);
        if (TextUtils.isEmpty(json)) {
            callJS(webview, JSBridgeConstant.isEventExsistString + event + "', {}, " + eventObj + ")");
        } else {
            callJS(webview, JSBridgeConstant.isEventExsistString + event + "'," + json + "," + eventObj + ")");
        }
    }

    public static void callbackJSOnHitPageBottom(Browser webview) {
        //callJS(webview, "if(NineGameClient && NineGameClient.onHitPageBottom) NineGameClient.onHitPageBottom()", true);
        callbackEvent(webview, JSBridgeConstant.EVENT_PAGE_SCROLL_BOTTOM , null);
    }

    public static void callbackJSOnSettingsChanged(Browser webview, String json) {
        callbackEvent(webview, JSBridgeConstant.EVENT_CONFIG_CHANGED, json);
    }

    public static void callbackJSOnLayoutInitSuccess(Browser webview) {
        callbackEvent(webview, JSBridgeConstant.EVENT_WEBVIEW_INIT , null);
    }
    
    public static JSONObject genCallbackJson(boolean result, String message, Object data) {
        JSONObject resultJson = new JSONObject();

        try {
            resultJson.put(JSBridgeImpl.KEY_DATA, data);
            resultJson.put(JSBridgeImpl.KEY_RESULT, result);
            resultJson.put(JSBridgeImpl.KEY_MSG, message);

        } catch (JSONException e) {
            L.e(e);
        }

        return resultJson;
    }
}
