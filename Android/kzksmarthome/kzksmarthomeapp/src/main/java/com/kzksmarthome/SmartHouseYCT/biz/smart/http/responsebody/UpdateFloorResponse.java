package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FloorInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

/**
 * @Title: UpdateFloorResponse
 * @Description: 修改楼层返回
 * @author laixj
 * @date 2016/9/18 14:50
 * @version V1.0
 */
public class UpdateFloorResponse extends BaseResponse {
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
        return "UpdateFloorResponse{" +
                "success=" + success +
                ", result=" + (result == null ? "":result.toString()) +
                '}';
    }
}
