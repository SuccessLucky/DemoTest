package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.common.lib.okhttp.BaseResponse;

/**
 * @Title: DeleteFloorResponse
 * @Description: 删除楼层返回
 * @author laixj
 * @date 2016/9/18 14:50
 * @version V1.0
 */
public class DeleteFloorResponse extends BaseResponse {
    /**
     * success : false
     * result : null
     * error : {"code":"","message":"暂时无法删除楼层,楼层内有房间信息"}
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
        return "DeleteFloorResponse{" +
                "success=" + success +
                ", result=" + (result == null ? "":result.toString()) +
                '}';
    }
}
