package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.common.lib.okhttp.BaseResponse;

/**
 * @Title: DelFingerPwdResponse
 * @Description: 删除指纹/密码返回
 * @author laixj
 * @date 2016/9/18 14:50
 * @version V1.0
 */
public class DelFingerPwdResponse extends BaseResponse {
    /**
     * success : true
     * result : 删除成功
     * error : {"code":"","message":""}
     */

    private boolean success;
    private String result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "DelFingerPwdResponse{" +
                "success=" + success +
                ", result='" + result + '\'' +
                '}';
    }
}
