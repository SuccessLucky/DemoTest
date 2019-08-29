package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.common.lib.okhttp.BaseResponse;

/**
 * @Title: OpenDoorbyFingerResponse
 * @Description: 指纹解锁返回
 * @author laixj
 * @date 2016/9/18 14:50
 * @version V1.0
 */
public class OpenDoorbyFingerResponse extends BaseResponse {
    /**
     * success : false
     * result : null
     * error : {"code":"4005","message":"密码错误,无法打开门锁"}
     */

    private boolean success;
    private Object result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "OpenDoorbyFingerResponse{" +
                "success=" + success +
                ", result=" + result +
                '}';
    }
}
