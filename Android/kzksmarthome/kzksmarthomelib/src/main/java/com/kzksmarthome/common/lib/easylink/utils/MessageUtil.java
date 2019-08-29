package com.kzksmarthome.common.lib.easylink.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Administrator on 2016/11/16 0016.
 */
public class MessageUtil {
    public static void sendMessage(Handler handler, int what, Object obj){
        Message msg=handler.obtainMessage();
        msg.what=what;
        msg.obj=obj;
        handler.sendMessage(msg);
    }
}
