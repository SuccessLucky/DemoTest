package com.kzksmarthome.common.lib.ipc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.module.log.L;

/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate Dec 29, 2014
 *
 */
public class BackProcHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
        Bundle data = msg.getData();
        if (data != null) {
            data.setClassLoader(BackProcHandler.class.getClassLoader());
        }
        L.d("IPC#BackProcHandler what: %s, data: %s", msg.what, data);
        switch (msg.what) {
        case MessageConstant.MESSENGER_EVENT_FROM_FOREGROUND:
            if (data != null && data.containsKey("data")) {
                Object value = data.get("data");
                GjjEventBus.getInstance().post(value);
            }
            break;
        default:
            break;
        }
    }

}
