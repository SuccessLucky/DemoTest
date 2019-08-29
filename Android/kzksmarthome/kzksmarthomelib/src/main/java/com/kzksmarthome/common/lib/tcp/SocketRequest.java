package com.kzksmarthome.common.lib.tcp;


public class SocketRequest {
    //请求数据
    public byte[] requestData;
    
    //请求回调id
    public long callbackId;
    
    public String cmd;

    //请求优先级
    public long priority;
    
    //标识请求是否被取消
    public boolean isCancel;
    
    //标识请求是否完成
    public boolean isFinished;
    
    //请求重试次数
    public int retryCount = 1;
    
    //请求超时时间，单位毫秒
    public int timeoutMillionSec = 30000;
    
    //时间统计临时变量
    public long timeTemp;
    
    public SocketRequest() {
        
    }
    
    /*public SocketRequest(byte[] requestData, int callbackId, String cmd, SocketRequestCallback callback, int priority, int retryCount, int timeoutMillionSec) {
        setRequestData(requestData);
        this.callbackId = callbackId;
        this.cmd = cmd;
        this.callback = callback;
        this.priority = priority;
        setRetryCount(retryCount);
        setTimeoutMillionSec(timeoutMillionSec);
    }*/
    
    public byte[] getRequestData() {
        return requestData;
    }

    public void setRequestData(byte[] requestData) {
        if (requestData == null) {
            throw new RuntimeException("requestData must be not null");
        }
        this.requestData = requestData;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        if (retryCount < 1) {
            throw new RuntimeException("retryCount must be > 0");
        }
        this.retryCount = retryCount;
    }

    public int getTimeoutMillionSec() {
        return timeoutMillionSec;
    }

    public void setTimeoutMillionSec(int timeoutMillionSec) {
        if (timeoutMillionSec <= 0) {
            throw new RuntimeException("timeoutMillionSec must be > 0");
        }
        this.timeoutMillionSec = timeoutMillionSec;
    }


    
}
