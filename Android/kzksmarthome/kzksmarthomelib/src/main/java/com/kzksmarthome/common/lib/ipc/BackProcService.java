package com.kzksmarthome.common.lib.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.kzksmarthome.common.module.log.L;

/**
 * 
 * for communicate with foreground process
 * 
 * @author panrq
 * @createDate Dec 29, 2014
 *
 */

public class BackProcService extends Service {
//    private static final String TAG = "Messenger";

    private Handler mBackProcHandler = new BackProcHandler();
//    private Messenger mBackProcMessenger;
//    private IBinder mBinderProxy;

//    private static Messenger sForeProcMessenger;

//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            Bundle data = msg.getData();
//
//            if (data != null) {
//                data.setClassLoader(BackProcService.class.getClassLoader());
//            }
//
//            if (mBackProcHandler == null) {
//                mBackProcHandler = new BackProcHandler();
//            }
//
//            mBackProcHandler.handleMessage(msg.what, data);
//        }
//    };

    @Override
    public void onCreate() {
        L.i("Ipc#BackProcService onCreate");
        super.onCreate();
        
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        L.i("Ipc#BackProcService onBind");
        IBinder iBinder = new BackBinderProxy();
        Messenger messenger = intent.getParcelableExtra("fore_messenger");
        BackProcMessenger.setMessenger(messenger);
        //try connect with foreground process
        connectToForeProc(messenger);
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
//        mBinderProxy = null;
        BackProcMessenger.setMessenger(null);
        L.d("IPC#BackProcMessenger onUnbind");
        return super.onUnbind(intent);
    }

    /**
     * send background messenger to foreground process
     */
    private void connectToForeProc(Messenger messenger) {
        L.d("IPC#connectToForeProc messenger: %s", messenger);
        if (messenger != null) {
            Message connMsg = Message.obtain(null, MessageConstant.MESSENGER_BACKGROUND_COMPLETE);
            Bundle data = new Bundle();
            data.putParcelable("back_messenger", new Messenger(mBackProcHandler));
            connMsg.setData(data);

            try {
                messenger.send(connMsg);

            } catch (RemoteException e) {
                L.w(e);
            }
        }
    }
}
