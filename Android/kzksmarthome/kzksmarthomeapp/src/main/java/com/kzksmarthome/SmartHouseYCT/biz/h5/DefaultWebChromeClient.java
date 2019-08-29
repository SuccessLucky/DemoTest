package com.kzksmarthome.SmartHouseYCT.biz.h5;

import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.common.module.log.L;

/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate 2015-3-9
 *
 */
public class DefaultWebChromeClient extends WebChromeClient {

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        // we MUST return true we are done with debugging
        L.handleConsoleMessage(consoleMessage);
        return true;  // return false to enable console.log
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        result.confirm(JSBridge.callNative(view, message));
        return true;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

        SmartHomeApp.showToast(message);
        result.confirm();
        return true;
    }

}
