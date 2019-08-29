package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

/**
 * @Title: GetDeviceByIdResponse
 * @Description: 根据id获取设备信息返回
 * @author laixj
 * @date 2016/9/18 14:50
 * @version V1.0
 */
public class GetDeviceByIdResponse extends BaseResponse {
    /**
     * success : true
     * result : {"device_id":1,"room_id":1,"name":"测试","image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","device_state1":1,"device_state2":1,"device_state3":1,"alarm_status":0,"other_status":"75","mac_address":"192.168.1.1","device_buttons":[{"button_id":1,"name":"温度加","instruction_code":"0001x11"},{"button_id":2,"name":"温度减","instruction_code":"0001x12"}]}
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
}
