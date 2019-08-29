package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.common.lib.okhttp.BaseResponse;

/**
 * @Title: AddDoorPwdResponse
 * @Description: 添加门锁密码返回
 * @author laixj
 * @date 2016/9/18 14:50
 * @version V1.0
 */
public class AddDoorPwdResponse extends BaseResponse {
    /**
     * success : true
     * result : null
     * error : {"code":"","message":""}
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
        return "AddDoorPwdResponse{" +
                "success=" + success +
                ", result=" + result +
                '}';
    }
}
