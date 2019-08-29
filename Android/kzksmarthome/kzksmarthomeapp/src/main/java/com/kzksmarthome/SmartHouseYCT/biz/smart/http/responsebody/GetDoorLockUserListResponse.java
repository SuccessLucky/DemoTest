package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DoorAccessInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

import java.util.List;

/**
 * @Title: GetDoorLockUserListResponse
 * @Description: 获取门锁用户列表返回
 * @author laixj
 * @date 2016/10/31 21:20
 * @version V1.0
 */
public class GetDoorLockUserListResponse extends BaseResponse {
    /**
     * success : true
     * result : [{"device_id":1,"lock_id":1,"user_name":"阿毛","unlock_times":0},{"device_id":1,"lock_id":2,"user_name":"阿毛","unlock_times":0},{"device_id":1,"lock_id":3,"user_name":"阿毛","unlock_times":0}]
     */

    private boolean success;
    /**
     * device_id : 1
     * lock_id : 1
     * user_name : 阿毛
     * unlock_times : 0
     */

    private List<DoorAccessInfo> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<DoorAccessInfo> getResult() {
        return result;
    }

    public void setResult(List<DoorAccessInfo> result) {
        this.result = result;
    }
}
