package com.kzksmarthome.common.lib.tcp;


import android.os.SystemClock;
import android.widget.Toast;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.network.NetworkStateMgr;
import com.kzksmarthome.common.lib.task.BackgroundTaskExecutor;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.log.LogModuleName;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public abstract class SocketRequestDispatcher implements Runnable {

    public static final String LOG_DISPATCHER = "%s request [cmd:%s,seq:%s] dispatch time-consuming [%sms]";

    private boolean mIsDestroy = false;
    private PriorityBlockingQueue<SocketRequest> mRequestQueue = new PriorityBlockingQueue<SocketRequest>(10,
            new Comparator<SocketRequest>() {
                public int compare(SocketRequest o1, SocketRequest o2) {
                    return o1.priority < o2.priority ? -1 : 1;
                }
            });

    @Override
    public void run() {
        PriorityBlockingQueue<SocketRequest> requestQueue = mRequestQueue;
        while (!mIsDestroy) {
            try {
                final SocketRequest socketRequest = requestQueue.take();
                socketRequest.timeTemp = SystemClock.elapsedRealtime();
                L.i(LOG_DISPATCHER, LogModuleName.REQUEST_TIME, socketRequest.cmd,
                        socketRequest.callbackId, socketRequest.timeTemp - socketRequest.priority);
                boolean success = false;
                while (!success && !socketRequest.isCancel && socketRequest.retryCount-- > 0) {
//                    L.d("SocketRequestDispatcher trySendMsg callbackId:%s", socketRequest.callbackId);
                    success = trySendMsg(socketRequest);
                }
                L.i("Request# SocketRequestDispatcher success:%s", success);
                if (!success) {
                    socketRequest.isFinished = true;
                    if (!NetworkStateMgr.getInstance().isNetworkAvailable()) {
                            //提示发送失败
                        MainTaskExecutor.runTaskOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SmartHomeAppLib.getInstance().getContext(),"请链接网络！",Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        //提示发送失败
                        MainTaskExecutor.runTaskOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SmartHomeAppLib.getInstance().getContext(),"数据发送失败！",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    TCPMgr.getInstance().removeSocketRequestFromMap(socketRequest.callbackId);
                } else {
                    BackgroundTaskExecutor.scheduleTask(socketRequest.timeoutMillionSec, new Runnable() {

                        @Override
                        public void run() {
                                TCPMgr.getInstance().removeSocketRequestFromMap(socketRequest.callbackId);
                        }

                    });
                }

            } catch (InterruptedException e) {
                L.e(e);
            }
        }
    }

    public void destroy() {
        mIsDestroy = true;
    }

    /**
     * 发送数据,此函数需要在独立的子线程中完成,可以考虑做一个发送队列 自己开一个子线程对该队列进行处理,就好像connect一样
     * 
     * @param socketRequest
     * @return
     */
    public abstract boolean trySendMsg(SocketRequest socketRequest);

    public void addRequest(SocketRequest socketRequest) {
        TCPMgr.getInstance().addSocketRequestToMap(socketRequest);
        mRequestQueue.add(socketRequest);
    }

    public boolean cancelRequest(int callback) {
        PriorityBlockingQueue<SocketRequest> requestQueue = mRequestQueue;
        for (SocketRequest mRequest : requestQueue) {
            if (mRequest.callbackId == callback) {
                mRequest.isCancel = true;
                requestQueue.remove(mRequest);
                L.i("Request# SocketRequestDispatcher cancel callback:%s", callback);
                return true;
            }
        }
        return false;
    }
}
