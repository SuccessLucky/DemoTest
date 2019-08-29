package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.common.lib.okhttp.BaseResponse;

/**
 * @Title: DeleteSceneResponse
 * @Description: 删除场景返回
 * @author laixj
 * @date 2016/10/15 7:18
 * @version V1.0
 */
public class DeleteSceneResponse extends BaseResponse {
    /**
     * success : true
     * result : success
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
        return "DeleteSceneResponse{" +
                "success=" + success +
                ", result='" + result + '\'' +
                '}';
    }
}
