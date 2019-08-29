package com.kzksmarthome.common.lib.tcp;


import android.os.SystemClock;


import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.log.LogModuleName;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 
 * 响应分发
 * 
 * @author panrq
 * @createDate 2015-4-29
 *
 */
public class SocketResponseDispatcher implements Runnable {

    public static final String LOG_DISPATCHER = "%s response [seq:%s] dispatch time-consuming [%sms]";

    private boolean mIsDestroy = false;

    private PriorityBlockingQueue<SocketResponse> mResponseQueue = new PriorityBlockingQueue<SocketResponse>(10, new Comparator<SocketResponse>() {
        public int compare(SocketResponse o1, SocketResponse o2) {
            return o1.priority < o2.priority ? -1 : 1;
        }
    });

    @Override
    public void run() {
        PriorityBlockingQueue<SocketResponse> requestQueue = mResponseQueue;
        while (!mIsDestroy) {
            try {
                SocketResponse socketResponse = requestQueue.take();
//                L.d("Request# SocketResponseDispatcher %s", socketResponse);
                L.i(LOG_DISPATCHER, LogModuleName.REQUEST_TIME, socketResponse.callbackId, (SystemClock.elapsedRealtime() - socketResponse.priority));
                dispatch(socketResponse);
            } catch (InterruptedException e) {
                L.e(e);
            }

        }

    }

    private void dispatch(SocketResponse socketResponse) {
        TCPMgr.getInstance().removeSocketRequestFromMap(socketResponse.callbackId);
    }

    public void addResponse(SocketResponse socketResponse) {
        mResponseQueue.add(socketResponse);
    }

    public void destroy() {
        mIsDestroy = true;
    }
}
