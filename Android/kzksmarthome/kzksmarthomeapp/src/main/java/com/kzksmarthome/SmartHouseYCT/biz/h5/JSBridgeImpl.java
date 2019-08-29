package com.kzksmarthome.SmartHouseYCT.biz.h5;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.webkit.WebView;

import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;
import com.kzksmarthome.common.lib.util.AndroidUtil;
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
public class JSBridgeImpl {

    public static final String KEY_STATE = "state";
    public static final String KEY_CODE = "code";
    public static final String KEY_RESULT = "result";
    public static final String KEY_MSG = "msg";
    public static final String KEY_DATA = "data";
    public static final String KEY_VERSIONNAME = "versionName";

    public static final String KEY_IMAGE_DISABLE = "image_disabled";

    public static final String KEY_CALLBACK_ID = "callbackId";

    /**
     * 启动某个应用
     * @param webview
     * @param jsonObject
     */
    public static void startupApp(WebView webview, JSONObject jsonObject) {
        try {
            SmartHomeApp app = SmartHomeApp.getInstance();
            String packageName;
            if (jsonObject != null && !TextUtils.isEmpty(jsonObject.optString("packageName"))) {
                packageName = jsonObject.getString("packageName");
            } else {
                packageName = app.getPackageName();
            }
            if (AndroidUtil.launchApp(app, packageName)) {
            }
        } catch (Exception e) {
            L.w(e);
        }
    }
    
    /**
     * 构造callback json对象
     * @param result
     * @param message
     * @param data
     * @return
     */
    public static JSONObject genCallbackJson(boolean result, String message, Object data) {
        JSONObject resultJson = new JSONObject();

        try {
            resultJson.put(KEY_DATA, data);
            resultJson.put(KEY_RESULT, result);
            resultJson.put(KEY_MSG, message);

        } catch (JSONException e) {
            L.e(e);
        }

        return resultJson;
    }
    
    /**
     * @param webView
     *            destroy webview the hackery way, this method is used to destroy webview.
     *            since on Android 4.1.x, directly calling WebView.destroy() sometimes
     *            causes WebViewCoreThread to wait indefinitely, which stops the WebView from
     *            working as usual.
     */
    public static void hackDestroyWebView(final WebView webView) {
        L.d("%s hackDestroyWebView called.", LogModuleName.JSBRIDGE);

        MainTaskExecutor.scheduleTaskOnUiThread(1000, new Runnable() {
            @Override
            public void run() {
                ((Browser) webView).doDestroy();
            }
        });
    }
}
