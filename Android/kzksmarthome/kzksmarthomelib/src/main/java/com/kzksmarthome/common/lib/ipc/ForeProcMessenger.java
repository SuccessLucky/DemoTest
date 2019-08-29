package com.kzksmarthome.common.lib.ipc;

import java.io.Serializable;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.task.ForegroundTaskExecutor;
import com.kzksmarthome.common.module.log.L;

import kzksmarthome.common.lib.ipc.IBackProcProxy;


/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate Dec 29, 2014
 *
 */
public class ForeProcMessenger {
//    private static final String TAG = "Messenger";
    private static ForeProcMessenger mInstance;
    public synchronized static ForeProcMessenger getInstance() {
        if (mInstance == null) {
            mInstance = new ForeProcMessenger();
            mInstance.send(MessageConstant.MESSENGER_FOREGROUND_CONNECT, null);
        }
        return mInstance;
    }
    
    private IBackProcProxy mBinderProxy;

    // -1:not connect , 0:connecting , 1:connected
    private int mConnState = -1;
    // for service bind state
    private boolean mIsBound = false;
    private Messenger mBackProcMessenger;
    private final Object mConnLock = new Object();
    // receive message from background process
    private Handler mForeProcHandler = new ForeProcHandler() {
        @Override
        public void handleMessage(Message msg) {
            
            switch (msg.what) {
            case MessageConstant.MESSENGER_BACKGROUND_COMPLETE:
                L.d("Ipc# ForeProcMessenger handleMessage MESSENGER_BACKGROUND_COMPLETE");
                if (msg.getData() != null) {
                    Bundle data = msg.getData();
                    if (data != null) {
                        data.setClassLoader(ForeProcMessenger.class.getClassLoader());
                    }
                    // second way handshake
                    mBackProcMessenger = data.getParcelable("back_messenger");
                    L.d("Ipc# ForeProcMessenger handleMessage mBackProcMessenger %s", mBackProcMessenger);
                    mConnState = 1;// now change to connected state
                    L.i("Ipc# ForeProcMessenger finish connect with background process!");

                    synchronized (mConnLock) {
                        mConnLock.notifyAll();
                    }

                    // third handshake,reply to background that we are successed
                    // whether process this message,should be designed by background
                    // now we not process this message in background process
                    send(MessageConstant.MESSENGER_FOREGROUND_COMPLETE, null);
                }
                break;

            default:
                // handle all background message and delivery them to main process
                super.handleMessage(msg);
                break;
            }
        }
    };
    
    private ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderProxy = IBackProcProxy.Stub.asInterface(service);
            SmartHomeAppLib.getInstance().resetPreference();
            L.i("Ipc# ForeProcMessenger onServiceConnected!");
            mIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mConnState = -1;
            mBackProcMessenger = null;
            mBinderProxy = null;
            mIsBound = false;
        }
    };

    /**
     * disconnect with background process
     */
    public void disConnectToBackProc() {
        if (mIsBound) {
            SmartHomeAppLib.getInstance().getContext().unbindService(mServiceConn);
        }
    }

    /**
     * get binder for background process
     * 
     * @return
     */
    public IBackProcProxy getBackProxy() {
        return mBinderProxy;
    }

    /**
     * send bundle data
     * 
     * @param what
     * @param data
     */
    public void send(int what, Bundle data) {
        Message msg = Message.obtain(null, what);
        msg.setData(data);
        sendForState(msg);
    }

    public void sendSerializable(int what, Serializable p) {
        Message msg = Message.obtain(null, what);

        if (p != null) {
            Bundle data = new Bundle();
            data.putSerializable("data", p);
            msg.setData(data);
        }

        sendForState(msg);
    }
    
    /**
     * send parcelable data to background process
     *
     * @param what: the message id from MessageConstant
     * @param p: parcelable data transfer to background process
     */
    public void sendParcel(int what, Parcelable p) {
        Message msg = Message.obtain(null, what);

        if (p != null) {
            Bundle data = new Bundle();
            data.putParcelable("data", p);
            msg.setData(data);
        }

        sendForState(msg);
    }

    /**
     * before we send message ,we should check if background messenger is ready if background process is not ready ,wait for it, and send
     * message after it finished connecting.
     * 
     * @param msg
     */
    private void sendForState(Message msg) {
        if (mConnState == 1) {
            doSend(msg);

        } else {
            ForegroundTaskExecutor.executeTask(new ConnTask(msg));
        }
    }

    /**
     * call background process messenger to send message
     * 
     * @param m
     */
    private void doSend(Message m) {
        if (mBackProcMessenger != null) {
            try {
                mBackProcMessenger.send(m);

            } catch (RemoteException e) {
                mConnState = -1;
                mBackProcMessenger = null;
                L.w("Ipc# ForeProcMessenger foreground send message error# exception：" + e);
            }
        }
    }

    private class ConnTask implements Runnable {

        private Message mMsg;

        public ConnTask(Message m) {
            mMsg = m;
        }

        @Override
        public void run() {
            ForegroundTaskExecutor.ensurePriority();
            if (mConnState == -1) {
                mConnState = 0;// now change to connecting state
                L.d("Ipc# ForeProcMessenger start connectToBackProc#");
                Intent intent = new Intent(SmartHomeAppLib.getInstance().getContext(), BackProcService.class);
                // first handshake
                intent.putExtra("fore_messenger", new Messenger(mForeProcHandler));
                SmartHomeAppLib.getInstance().getContext().bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
            }

            // if now connection state to background change to 0 , it means it
            // is still connecting,just wait for it
            synchronized (mConnLock) {
                if (mConnState == 0) {
                    try {
                        L.d("Ipc# ForeProcMessenger wait for connect background success#");
                        mConnLock.wait();

                    } catch (InterruptedException e) {
                        L.w("Ipc# ForeProcMessenger run conn task error# exception:" + e);
                    }
                }
            }

            // now it's ready for us to send message
            doSend(mMsg);
        }

    }

    /**
     * 前台进程是否存活
     * 
     * @return boolean
     */
    public boolean isForegroundProcAlive() {
        return mIsBound;
    }
}
