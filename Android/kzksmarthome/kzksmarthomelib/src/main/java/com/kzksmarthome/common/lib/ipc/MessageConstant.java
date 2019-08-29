package com.kzksmarthome.common.lib.ipc;

/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate Dec 29, 2014
 *
 */
public class MessageConstant {

    /**
     * empty send ,it can be used to judge whether another process is active
     */
    public static final int MESSENGER_EMPTY = -1;
    /**
     * this is the first handshake of three-way handshake
     */
    public static final int MESSENGER_FOREGROUND_CONNECT = 10001;
    /**
     * flag for we complete connecting to background process
     */
    public static final int MESSENGER_BACKGROUND_COMPLETE = 10002;

    public static final int MESSENGER_FOREGROUND_COMPLETE = 10003;
    
    /**
     * 前台发到后台的消息
     */
    public static final int MESSENGER_EVENT_FROM_FOREGROUND = 10004;
    
    /**
     * 后台发到前台的消息
     */
    public static final int MESSENGER_EVENT_FROM_BACKGROUND = 10005;

    /**
     * for foreground state change
     */
    // public static final int FOREGROUND_SHOW_STATE_CHANGE = 60001;
}
