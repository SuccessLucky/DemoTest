package com.kzksmarthome.common.lib.ipc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.kzksmarthome.common.lib.eventbus.GjjEventBus;


/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate Dec 29, 2014
 *
 */
public class ForeProcHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
        Bundle data = msg.getData();
        if (data != null) {
            data.setClassLoader(ForeProcHandler.class.getClassLoader());
        }
        switch (msg.what) {
        case MessageConstant.MESSENGER_EVENT_FROM_BACKGROUND:
            if (data != null && data.containsKey("data")) {
                Object value = data.get("data");
                GjjEventBus.getInstance().post(value);
            }
            break;
        default:
            break;
        }
//        if (data != null) {
//            if (what >= 0 && what < Message.Type.values().length) {
//                Message.Type msgType = Message.Type.values()[what];
//                Object value = data.get("data");
//                int priority = Message.PRIORITY_HIGH;
//
//                GjjApp.getInstance().getMessagePump().broadcastMessage(msgType, value, priority);
//            }
//        }
    }

}
