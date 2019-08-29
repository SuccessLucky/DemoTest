package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DoorAccessInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

/**
 * @Title: AddFingerResponse
 * @Description: 添加指纹返回
 * @author laixj
 * @date 2016/9/18 14:50
 * @version V1.0
 */
public class AddFingerResponse extends BaseResponse {
    /**
     * success : true
     * result : {"device_id":1,"lock_id":3,"user_name":"阿毛","unlock_times":0}
     * error : {"code":"","message":""}
     */

    private boolean success;
    /**
     * device_id : 1
     * lock_id : 3
     * user_name : 阿毛
     * unlock_times : 0
     */

    private DoorAccessInfo result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DoorAccessInfo getResult() {
        return result;
    }

    public void setResult(DoorAccessInfo result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "AddFingerResponse{" +
                "success=" + success +
                ", result=" + result +
                '}';
    }
}
