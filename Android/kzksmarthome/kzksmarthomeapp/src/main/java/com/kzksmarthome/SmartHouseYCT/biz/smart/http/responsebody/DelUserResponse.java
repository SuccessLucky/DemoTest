package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.common.lib.okhttp.BaseResponse;

/**
 * @Title: DelUserResponse
 * @Description: 删除家庭成员返回
 * @author laixj
 * @date 2016/10/10 20:32
 * @version V1.0
 */
public class DelUserResponse extends BaseResponse {
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
        return "AddUserResponse{" +
                "success=" + success +
                ", result=" + (result == null ? "":result.toString()) +
                '}';
    }
}
