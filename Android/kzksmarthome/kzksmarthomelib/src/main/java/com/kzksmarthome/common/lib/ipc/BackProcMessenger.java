package com.kzksmarthome.common.lib.ipc;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;

import com.kzksmarthome.common.module.log.L;

public class BackProcMessenger {
    private static BackProcMessenger mInstance;
    public synchronized static BackProcMessenger getInstance() {
        if (mInstance == null) {
            mInstance = new BackProcMessenger();
        }
        return mInstance;
    }
    
    private Messenger mForeProcMessenger;
    
    public static void setMessenger(Messenger messenger) {
        getInstance().mForeProcMessenger = messenger;
    }
    
    public boolean send(int what, Serializable p) {

        Message m = Message.obtain(null, what);
        Bundle data = new Bundle();
        data.putSerializable("data", p);
        m.setData(data);
        return doSend(m);
    }
    
    /**
     * send message to foreground process
     * @param what
     * @param p
     */
    public boolean send(int what, Parcelable p) {

        Message m = Message.obtain(null, what);
        Bundle data = new Bundle();
        data.putParcelable("data", p);
        m.setData(data);
        return doSend(m);
    }

    public boolean sendParcelArrayList(int what, ArrayList<? extends Parcelable> list) {

        Message m = Message.obtain(null, what);
        Bundle data = new Bundle();
        data.putParcelableArrayList("data", list);
        m.setData(data);
        return doSend(m);
    }

    public boolean sendInt(int what, int v) {

        Message m = Message.obtain(null, what);
        Bundle data = new Bundle();
        data.putInt("data", v);
        m.setData(data);
        return doSend(m);
    }

    public boolean sendBoolean(int what, boolean b) {

        Message m = Message.obtain(null, what);
        Bundle data = new Bundle();
        data.putBoolean("data", b);
        m.setData(data);
        return doSend(m);
    }

    public boolean sendString(int what, String str) {

        Message m = Message.obtain(null, what);
        Bundle data = new Bundle();
        data.putString("data", str);
        m.setData(data);
        return doSend(m);
    }

    /**
     * send this message to foreground process
     * @param m
     * @return true:send sucess,false:send failed,may be foreground not existed
     */
    private boolean doSend(Message m) {
        Messenger msger = mForeProcMessenger;
        if (msger == null) {
            return false;
        }
        try {
            msger.send(m);
            return true;

        } catch (RemoteException e) {
            L.w(e);
        }

        return false;
    }

    /**
     * 前台进程是否存在
     * 
     * @return
     */
    public boolean isForegroundAlive() {
        return mForeProcMessenger != null;
    }
}
