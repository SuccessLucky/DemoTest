package com.kzksmarthome.SmartHouseYCT.biz.h5;

public class JSBridgeConstant {
    public final static String isCallbackExsistString = "if(window.JSBridge && JSBridge.onCallback) JSBridge.onCallback(";
    public final static String isEventExsistString = "if(window.JSBridge && JSBridge.onEvent) JSBridge.onEvent('";
    public final static String EVENT_ERROR =  "error";
    public final static String EVENT_MESSAGE_RECEIVED =  "message_received";
    public final static String EVENT_CONFIG_CHANGED =  "config_changed";


    public final static String EVENT_WEBVIEW_INIT =  "webview_init";
    public final static String EVENT_PAGE_SCROLL_BOTTOM =  "page_scroll_bottom";
    public final static String EVENT_TAB_SWITCH =  "tab_switch";
    public final static String EVENT_WINDOW_CLOSED =  "window_closed";
    public final static String EVENT_WINDOW_OPEN =  "window_open";
}
