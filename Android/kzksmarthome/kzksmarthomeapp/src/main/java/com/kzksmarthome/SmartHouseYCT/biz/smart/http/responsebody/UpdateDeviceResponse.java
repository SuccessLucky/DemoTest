package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

/**
 * @Title: UpdateDeviceResponse
 * @Description: 修改设备返回
 * @author laixj
 * @date 2016/9/18 14:50
 * @version V1.0
 */
public class UpdateDeviceResponse extends BaseResponse {
    /**
     * success : true
     * result : null
     * error : {"code":"","message":""}
     */

    private boolean success;
    private DeviceInfo result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DeviceInfo getResult() {
        return result;
    }

    public void setResult(DeviceInfo result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "UpdateDeviceResponse{" +
                "success=" + success +
                ", result=" + (result == null ? "":result.toString()) +
                '}';
    }
}
