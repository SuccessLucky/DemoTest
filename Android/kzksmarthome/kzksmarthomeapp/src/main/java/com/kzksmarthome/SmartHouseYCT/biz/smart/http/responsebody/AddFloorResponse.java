package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FloorInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

/**
 * @Title: AddFloorResponse
 * @Description: 添加楼层返回
 * @author laixj
 * @date 2016/9/18 14:50
 * @version V1.0
 */
public class AddFloorResponse extends BaseResponse {
    /**
     * success : true
     * result : null
     * error : {"code":"","message":""}
     */

    private boolean success;
    private FloorInfo result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public FloorInfo getResult() {
        return result;
    }

    public void setResult(FloorInfo result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "AddFloorResponse{" +
                "success=" + success +
                ", result=" + (result == null ? "":result.toString()) +
                '}';
    }
}
