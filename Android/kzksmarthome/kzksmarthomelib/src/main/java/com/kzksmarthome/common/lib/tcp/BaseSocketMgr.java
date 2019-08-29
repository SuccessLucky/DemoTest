package com.kzksmarthome.common.lib.tcp;

import android.os.SystemClock;
import android.util.Log;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.event.EventOfRegisterRotme;
import com.kzksmarthome.common.event.EventOfSocket;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.network.NetworkStateMgr;
import com.kzksmarthome.common.lib.task.ForegroundTaskExecutor;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;
import com.kzksmarthome.common.lib.tools.ErrorCode;
import com.kzksmarthome.common.lib.tools.Tools;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.log.LogModuleName;
import com.kzksmarthome.common.module.net.ApiHost;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public abstract class BaseSocketMgr {

    public static final String LOG_CONNECT_ING = "%s tcp connect ing time-consuming [%sms], connection %s";
    public static final String LOG_CONNECT_SUCCEED = "%s tcp connect succeed time-consuming [%sms], retry [%s]";
    public static final String LOG_CONNECT_FAILED = "%s tcp connect failed time-consuming [%sms]";
    public static final String LOG_SEND_SUCCEED = "%s request [cmd:%s,seq:%s] send succeed time-consuming [%sms]";
    public static final String LOG_SEND_FAILED = "%s request [cmd:%s,seq:%s] send failed time-consuming [%sms]";
    public static final String LOG_RECEIVE_SUCCEED = "%s request [cmd:%s,seq:%s] receive succeed time-consuming [%sms]";
    public static final String LOG_RECEIVE_FAILED = "%s request [cmd:%s,seq:%s] receive failed time-consuming [%sms]";
    public static final String LOG_WAIT_RESPONSE = "%s request [cmd:%s,seq:%s] wait response time-consuming [%sms]";

    static final ExecutorService sThreadPoolExecutor = Executors.newFixedThreadPool(3);

    /** 默认最多重试次数 */
    static int MAX_RETRY_CONNECT_COUNT = 3;

    /** 默认最大发送重试次数 */
    static final int MAX_SEND_FAIL_COUNT = 3;

    /** 默认最大接收重试次数 */
    static final int MAX_RECEIVE_FAIL_COUNT = 3;

    /** 默认读超时时间 */
    private static final int DEFAULT_SOCKET_READ_TIME_OUT = 100000;

    /** 默认链接超时时间 */
    static int DEFAULT_SOCKET_CONNECT_TIME_OUT = 5000;

    /** 读取buff的大小 */
    private static final int READ_BUFF_SIZE = 1024;

    /** 消息流的格式 */
    private static final String BUFF_FORMAT = "utf-8";

    /** 心跳周期, 默认两分钟 */
    protected static int DEFAULT_FREQUENCY_CHECK_PUSH_CONNECTION = 1000 * 60 * 2;// 2分钟

    Socket mSocket;

    /** 远端服务器ip地址 */
    // protected String mRemoteIp;

    /** 远端服务器端口 */
    // protected int mPort;

    /** 连接状态 ture:连接成功*/
    boolean mIsConnected = false;
    /**
     * 是否正在连接
     */
    boolean mIsConnecting = false;
    /**
     * 是否主动关闭连接，不执行重连操作
     */
    boolean mIsCloseConnect = false;

    SocketResponseDispatcher mSocketResponseDispatcher = new SocketResponseDispatcher();
    private ConcurrentHashMap<Long, WeakReference<SocketRequest>> mRequestMap = new ConcurrentHashMap<Long, WeakReference<SocketRequest>>();

    ReceiveRunnable mRevMsgRunnable;
    protected long mLastSendSuccessTime;

    /**
     * 获取socket分发
     */
    private SocketRequestDispatcher mSocketRequestDispatcher = null;

    /**
     * 获取socket分发
      * @return
     */
    private SocketRequestDispatcher getSocketRequestDispatcher() {
        if (mSocketRequestDispatcher == null) {
            mSocketRequestDispatcher = new SocketRequestDispatcher() {
                private int sendFailCount = 0;

                @Override
                public synchronized boolean trySendMsg(SocketRequest socketRequest) {
                    if (mIsCloseConnect) {
                        return false;
                    }
                    L.i("Request# SocketRequestDispatcher mIsConnected %s", mIsConnected);
                    long logTime = socketRequest.timeTemp;
                    int tryConnectCount = 0;
                    if (sendFailCount >= MAX_SEND_FAIL_COUNT) {
                        mIsConnected = false;
                    }
                    boolean isLog = !mIsConnected;
                    while (!mIsConnected) {
                        L.i("Request# SocketRequestDispatcher mIsConnecting %s", mIsConnecting);
                        if (!mIsConnecting) {
                            retryConnect();
                            tryConnectCount++;
                        }
                        L.i("Request# SocketRequestDispatcher tryConnectCount %s", tryConnectCount);
                        // 重试3次连接失败，则返回失败
                        if (tryConnectCount >= MAX_RETRY_CONNECT_COUNT) {
                            socketRequest.timeTemp = SystemClock.elapsedRealtime();
                            L.i(LOG_CONNECT_FAILED, LogModuleName.REQUEST_TIME, (socketRequest.timeTemp - logTime));
                            EventOfSocket eventOfSocket = new EventOfSocket();
                            eventOfSocket.setmIsConnectFail(true);
                            GjjEventBus.getInstance().post(eventOfSocket, true);
                            mSocketRequestDispatcher = null;
                            return false;
                        }
                        try {
                            Thread.sleep(500);
                            continue;
                        } catch (InterruptedException e) {
                            L.e(e);
                        }
                    }
                    if (isLog) {
                        socketRequest.timeTemp = SystemClock.elapsedRealtime();
                        L.i(LOG_CONNECT_SUCCEED, LogModuleName.REQUEST_TIME, (socketRequest.timeTemp - logTime), tryConnectCount);
                    }
                    logTime = socketRequest.timeTemp;
                    try {
                        OutputStream os = mSocket.getOutputStream();
//                L.d("Request# SocketRequestDispatcher cmd=%s", socketRequest.cmd);
                        os.write(socketRequest.getRequestData());
                        os.flush();
                        socketRequest.timeTemp = SystemClock.elapsedRealtime();// 保存发送完成时间
                        logTime = socketRequest.timeTemp - logTime;
                        L.i(LOG_SEND_SUCCEED, LogModuleName.REQUEST_TIME, socketRequest.cmd, socketRequest.callbackId, logTime);
//                L.d("Request# SocketRequestDispatcher send success ");
                        sendFailCount = 0;
                        return true;
                    } catch (Exception e) {
                        L.e(e);
                        sendFailCount++;
                    }
                    socketRequest.timeTemp = SystemClock.elapsedRealtime();// 保存发送完成时间
                    logTime = socketRequest.timeTemp - logTime;
                    L.i(LOG_SEND_FAILED, LogModuleName.REQUEST_TIME, socketRequest.cmd, socketRequest.callbackId, logTime);
                    return false;
                }

            };
        }
        return mSocketRequestDispatcher;
    }

    protected BaseSocketMgr() {
        sThreadPoolExecutor.execute(getSocketRequestDispatcher());
        //sThreadPoolExecutor.execute(mSocketResponseDispatcher);
        connect();
    }


    SocketRequest getSocketRequestFromMap(long callbackId) {
        WeakReference<SocketRequest> reference = mRequestMap.get(callbackId);
        if (reference != null) {
            return reference.get();
        }
        return null;
    }

    /**
     * 将请求以callbackId为key，添加到缓存请求map，用于接收到响应时能找回请求
     * @param socketRequest
     */
    void addSocketRequestToMap(SocketRequest socketRequest) {
        mRequestMap.put(socketRequest.callbackId, new WeakReference<SocketRequest>(socketRequest));
    }

    /**
     * 移除缓存请求map中的callbackId为key的请求，用于请求完成或失败时，能移除引用
     * @param callbackId
     */
    void removeSocketRequestFromMap(long callbackId) {
        if (mRequestMap.contains(callbackId)) {
            mLastSendSuccessTime = SystemClock.elapsedRealtime();
            mRequestMap.remove(callbackId);
        }
    }

    /**
     * 往请求队列添加一个请求
     * @param request
     */
    public void sendRequest(final SocketRequest request) {
        try {
            Log.d("AddDevice","发送数据："+ Tools.byte2HexStr(request.requestData));
        }catch (Exception e){
            e.printStackTrace();
        }
        checkAndConnect(false,false);//检测是否断开连接如果断开重连
        getSocketRequestDispatcher().addRequest(request);
    }

    /**
     * 检查如果未连接，则建立连接
     * 如果已连接，则检查是否需要发送心跳和续票
     *
     * @param sendHeadBeat
     * @param checkExpire
     */
    public void checkAndConnect(boolean sendHeadBeat, boolean checkExpire) {
        if (NetworkStateMgr.getInstance().isNetworkAvailable()) {
            if (!isConnect()) {
                retryConnect();
            }
        }else{
            MainTaskExecutor.runTaskOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SmartHomeAppLib.showToast("网络繁忙请稍后重试["+ ErrorCode.ERROR_NET_CODE+"]");
                }
            });
        }
    }

    /**
     * 从请求队列移除一个请求
     * @param callbackId
     * @return
     */
    public boolean cancelRequest(int callbackId) {
        L.i("BaseSocketMgr cancel callbackId:%s", callbackId);
        removeSocketRequestFromMap(callbackId);
        return getSocketRequestDispatcher().cancelRequest(callbackId);
    }

    /**
     * 链接远端地址
     *
     * @return
     */
    private void connect() {
        if (mIsConnecting) {
            return;
        }
        // 需要在子线程下进行链接
        ConnectRunnable connect = new ConnectRunnable();
        ForegroundTaskExecutor.executeTask(connect);

        // if (mSocketResponseDispatcher == null) {
        // mSocketResponseDispatcher = new SocketResponseDispatcher();
        // }
    }

    /**
     * 是否链接着
     *
     * @return
     */
    public boolean isConnect() {
        if (!mIsConnected) {
            return false;
        }

        try {
            // L.d("Request# canConnectServer sendUrgentData ");
            // //发送一个测试数据到服务器,检测服务器是否关闭
            mSocket.sendUrgentData(0xff);
        } catch (IOException e) {
            // L.e(e);
            mIsConnected = false;
            return false;
        }
        return mSocket.isConnected() && !mSocket.isClosed();
    }

    /**
     * 关闭连接，关闭自动重连
     */
    public void closeConnect(){
        mIsCloseConnect = true;
        close();
    }

    /**
     * 重连，开启自动重连
     */
    public void retryConnectOpen(){
        mIsCloseConnect = false;
        connectTime = 0;
        retryConnect();
    }

    /**
     * 关闭链接
     */
    public void close() {
        if (!mIsCloseConnect && mIsConnecting) {
            return;
        }
        mIsConnected = false;
        try {
            if (mSocket != null) {
                L.w("Request# mSocket close ");
                mSocket.close();
            }
        } catch (Exception e) {
            L.e(e);
        }
    }

    /**
     * 重连
     *
     * @return
     */
    public void retryConnect() {
        L.w("Request# SocketRequestDispatcher mIsConnecting " + mIsConnecting);
        L.w("Request# SocketRequestDispatcher mIsCloseConnect " + mIsCloseConnect);
        if(mIsCloseConnect){
            return;
        }
        close();
        connect();
    }

    /**
     * 远程连接时间
     */
    private long remoteTime;
    /**
     * 连接时间
     */
    private long connectTime;
    /**
     * 链接线程
     *
     */
    class ConnectRunnable implements Runnable {

        @Override
        public void run() {
            long logTime = SystemClock.elapsedRealtime();
            mIsConnecting = true;
            try {
                String host = ApiHost.TCP_IOT_HOST;
                int port = ApiHost.TCP_IOT_PORT;
                L.i("Request# connect to [%s:%s], network[%s]", host, port, NetworkStateMgr.getInstance().getNetworkState());
                // 从sharedPreferences中取IP地址
                InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
                Socket socket = new Socket();
                mSocket = socket;
                socket.setTcpNoDelay(false);// 关闭nagle算法
                socket.setKeepAlive(true);
                socket.connect(inetSocketAddress, DEFAULT_SOCKET_CONNECT_TIME_OUT);

                // 设置超时时间
                //mSocket.setSoTimeout(DEFAULT_SOCKET_READ_TIME_OUT);

                mIsConnected = true;

                if (mRevMsgRunnable == null) {
                    // 创建读线程
                    mRevMsgRunnable = new ReceiveRunnable();
                    sThreadPoolExecutor.execute(mRevMsgRunnable);
                }

            } catch (Throwable e) {
                mIsConnected = false;
                L.e(e);
            } finally {
                if (!mIsConnected) {
                    close();
                }
                mIsConnecting = false;
            }
            if (ApiHost.NETWORK_ISREMOTE) {//判断是否连接远程
                if (mIsConnected) {//判断是否连接成功
                    GjjEventBus.getInstance().post(new EventOfRegisterRotme(),true);//通知远端手机信息注册手机
                    L.i("Request# connect to register rotme on tcp mgr");
                }
            }
            doConnectSuccEvnent();
            L.i(LOG_CONNECT_ING, LogModuleName.REQUEST_TIME, (SystemClock.elapsedRealtime() - logTime), mIsConnected);
        }
    }

    /**
     * 发送连接成功通知
     */
    public void doConnectSuccEvnent(){
        long nowTimew = SystemClock.elapsedRealtime();
        if(nowTimew - connectTime > 5000){//5秒发送一次连接成功通知
            if(mIsConnected){//判断是否连接成功
                EventOfSocket eventOfSocket = new EventOfSocket();
                eventOfSocket.setmIsConnectFail(false);
                GjjEventBus.getInstance().post(eventOfSocket,true);//通知网络连接成功
                connectTime = nowTimew;
                L.i("Request# connect to success");
            }
        }
    }



    /**
     *
     * 接收数据线程
     *
     * @author panrq
     * @createDate 2015-4-29
     *
     */
    class ReceiveRunnable implements Runnable {

        private int receiveFailCount = 0;

        @Override
        public void run() {
            int tryConnectCount = 0;
            while (true) {
                //主动关闭连接直接结束线程
                if(mIsCloseConnect){
                    mRevMsgRunnable = null;
                    return;
                }
                L.i("Request# RevMsgRunnable mIsConnected %s", mIsConnected);
                long logTime = SystemClock.elapsedRealtime();
                // 连续接收错误超过3次
                if (receiveFailCount >= MAX_RECEIVE_FAIL_COUNT) {
                    mIsConnected = false;
                }
                if (!mIsConnected) {
                    L.i("Request# RevMsgRunnable mIsConnecting %s", mIsConnecting);
                    if (!mIsConnecting) {
                        retryConnect();
                        tryConnectCount++;
                    }

                    L.i("Request# RevMsgRunnable tryConnectCount %s", tryConnectCount);
                    // 重试3次连接失败，则返回失败
                    if (tryConnectCount >= MAX_RETRY_CONNECT_COUNT) {
                        mRevMsgRunnable = null;
                        L.i(LOG_CONNECT_FAILED, LogModuleName.REQUEST_TIME, (SystemClock.elapsedRealtime() - logTime));
                        EventOfSocket eventOfSocket = new EventOfSocket();
                        eventOfSocket.setmIsConnectFail(true);
                        GjjEventBus.getInstance().post(eventOfSocket,true);
                        return;
                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        L.e(e);
                    }
                    L.i(LOG_CONNECT_SUCCEED, LogModuleName.REQUEST_TIME, (SystemClock.elapsedRealtime() - logTime), tryConnectCount);
                } else {
                    tryConnectCount = 0;
                }

                try {
                    InputStream is = mSocket.getInputStream();
                    // 实际的读取流
                    splitPackage(is);
                } catch (Exception e) {
                    receiveFailCount++;
                    L.e(e);
                }
            }
        }

    }



    protected abstract void splitPackage(InputStream is);
}
