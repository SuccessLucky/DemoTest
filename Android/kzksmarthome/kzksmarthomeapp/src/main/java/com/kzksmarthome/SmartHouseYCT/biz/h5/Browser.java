package com.kzksmarthome.SmartHouseYCT.biz.h5;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kzksmarthome.common.module.log.L;

public class Browser extends WebView {
    public static final String TAG = "Browser";
    private String JS_PREFIX_EVENT = JSBridge.JS_PREFIX + JSBridgeConstant.isEventExsistString;
    
    private final static OnLongClickListener M_WEB_VIEW_LONG_CLICK_LISTENER = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            return true;
        }
    };
    
    private boolean mIsDestroyed = false;
    private HashMap<String, Object> mEventMap = new HashMap<String, Object>();
    
    public Browser(Context context) {
        super(context);
    }

    public Browser(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Browser(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void init(WebViewClient webViewClient, WebChromeClient webChromeClient) {
        setBackgroundColor(Color.WHITE);
        setWebViewClient(webViewClient);
        setWebChromeClient(webChromeClient);
        setupWebViewAttributes();
        setSoundEffectsEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // fixed the searchBoxJavaBridge_ inject bug
            removeJavascriptInterface("searchBoxJavaBridge_");
        }

        // try to fix PopupWindow crash bug by 1.3.0.3
        setLongClickable(false);
        setOnLongClickListener(M_WEB_VIEW_LONG_CLICK_LISTENER);
        //this.addJavascriptInterface(new JSInterface(), "JSInterface");
    }

    public void destroy() {
        if (mIsDestroyed) {
            return;
        }

        if (Build.VERSION.SDK_INT > 10 && Build.VERSION.SDK_INT < 19) {
            loadUrl("javascript:prompt(\"{'method':'hackDestroyWebView'}\");");
            mIsDestroyed = true;
            return;
        }

        mIsDestroyed = true;
        doDestroy();
    }

    public void doDestroy() {
        try {
            super.loadUrl("about:blank");
            super.stopLoading();
            super.clearCache(true);
            super.destroyDrawingCache();
            super.removeAllViews();
            super.clearHistory();
            super.destroy();
        } catch (Exception e) {
            L.w(e);
        }
     
    }
    
    /**
     * 对webview进行参数设置
     * 
     * @param webView
     *            要设置的webview
     * **/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("SetJavaScriptEnabled")
    public void setupWebViewAttributes() {
        WebSettings webSettings = getSettings();
//        setCustomizedUA(webSettings);

        if (!webSettings.getJavaScriptEnabled()) {

            try {
                webSettings.setJavaScriptEnabled(true);

            } catch (Exception e) {
                L.w(e);
            }
        }
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAppCachePath(getContext().getFilesDir() + "/webcache");
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLightTouchEnabled(false);
        webSettings.setDomStorageEnabled(true); // supports local storage
        webSettings.setDatabaseEnabled(true);   // supports local storage
        webSettings.setDatabasePath(getContext().getFilesDir() + "/localstorage");

        if (Build.VERSION.SDK_INT >= 16) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }

        // we are using ApplicationContext when creaing BrowserTab, without disabling the "Save Password" dialog
        // there will be an exception that would cause crash: "Unable to add window -- token null is not for an application"
        webSettings.setSavePassword(false);
        setHorizontalScrollBarEnabled(false);
//        mWebView.setVerticalScrollBarEnabled(false);
        setScrollbarFadingEnabled(true);
        setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        // 04-30 Api11以上添加软件层渲染属性测试
        // 09-23 Web假死问题，去除webview软件渲染属性
        // 09-26 发现关掉软件加速，所有机型在专区页滑动会出现闪烁现象，先还原，继续找方案
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }
    }
    
    /**
     * 注册webview通知
     * **/
    public void registerEvent(String event, Object obj) {
        mEventMap.put(event, obj);
    }
    /**
     * 注销webview通知
     * **/
    public void unregisterEvent(String event) {
        mEventMap.remove(event);
    }
    
    /**
     * 区分加载是否JSBridgeConstant.isEventExsistString开头
     * @param url 检测的url
     * @return 是否JSBridgeConstant.isEventExsistString开头
     * **/
    public boolean isEventPrefix(String url) {
        boolean isJsPrefix = false;
        if (!TextUtils.isEmpty(url) && url.startsWith(JS_PREFIX_EVENT)) {
            isJsPrefix = true;
        }
        return isJsPrefix;
    }
    /**
     * 区分加载是否javascript开头
     * @param url 检测的url
     * @return 是否javascript开头
     * **/
    public boolean isJsPrefix(String url){
        boolean isJsPrefix = false;
        if (!TextUtils.isEmpty(url) && url.startsWith(JSBridge.JS_PREFIX)) {
            isJsPrefix = true;
        }
        return isJsPrefix;
    }
    /**
     * 是否在注册列表里面
     * @param url 检测的url
     * @return 是否在列表
     * **/
    public boolean isInFilter(String url) {
        boolean isInFilter = false;
        for (String key : mEventMap.keySet()) {
            if (url.contains(key)) {
                isInFilter = true;
                break;
            }
        }
        return isInFilter;
    }
    /**
     * 检查事件Map，返回是否需要loadUrl标示
     * @param url 检测的url
     * @return 是否需要loadUrl
     * **/
    public boolean checkEventMap(String url) {
        boolean needLoadUrl = false;
        if (isEventPrefix(url)) {//JSBridgeConstant.isEventExsistString
            //在Map里面就需要加载url
            needLoadUrl = isInFilter(url);
        } else if (isJsPrefix(url)) { //剩余的javascript开头
            needLoadUrl = true;
        } else {//加载地址的话，直接清空Map
            mEventMap.clear();
            needLoadUrl = true;
        }
        return needLoadUrl;
    }
    /**
     *  获取事件的参数对象
     *  @param event 事件名
     *  @return 事件对应的参数对象，不存在返回null
     * **/
    public Object getEventObj(String event){
       return mEventMap.containsKey(event) ? mEventMap.get(event): null;
    }
    
    /**
     * return unique key for this BrowserTab
     * @return
     */
    public String getKey() {
        return TAG + hashCode();
    }
}
