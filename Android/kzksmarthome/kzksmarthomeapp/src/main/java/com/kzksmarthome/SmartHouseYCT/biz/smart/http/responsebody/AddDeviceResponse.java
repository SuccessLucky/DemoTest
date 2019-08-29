package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

import java.util.List;

/**
 * @Title: AddDeviceResponse
 * @Description: 添加设备返回
 * @author laixj
 * @date 2016/9/18 14:50
 * @version V1.0
 */
public class AddDeviceResponse extends BaseResponse {


    /**
     * success : true
     * result : [{"device_id":14,"room_id":1,"floor_id":1,"device_name":"测试天天","room_name":"大明湖畔","floor_name":"第三层","image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","sindex_length":"","cmd_id":"","device_state1":1,"device_state2":0,"device_state3":0,"alarm_status":0,"other_status":null,"mac_address":"192.168.1.8","device_buttons":[]},{"device_id":15,"room_id":1,"floor_id":1,"device_name":"测试天天","room_name":"大明湖畔","floor_name":"第三层","image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","sindex_length":"","cmd_id":"","device_state1":1,"device_state2":0,"device_state3":0,"alarm_status":0,"other_status":null,"mac_address":"192.168.1.9","device_buttons":[]}]
     */

    private boolean success;

    private List<DeviceInfo> result;

    @Override
    public String toString() {
        return "AddDeviceResponse{" +
                "success=" + success +
                ", result=" + (result == null ? "":result.toString()) +
                '}';
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<DeviceInfo> getResult() {
        return result;
    }

    public void setResult(List<DeviceInfo> result) {
        this.result = result;
    }

}
