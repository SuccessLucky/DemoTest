package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.SceneInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

/**
 * @Title: UpdateSceneResponse
 * @Description: 修改场景返回
 * @author laixj
 * @date 2016/10/15 7:14
 * @version V1.0
 */
public class UpdateSceneResponse extends BaseResponse {
    /**
     * success : true
     * result : {"scene_id":3,"name":"上学去","image":"0008.jpg","scene_details":[{"scene_detail_id":4,"device_id":1,"room_id":1,"device_name":"测试天天","device_image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","device_state1":1,"device_state2":0,"device_state3":0,"alarm_status":0,"other_status":"30","mac_address":"192.168.1.1"},{"scene_detail_id":6,"device_id":3,"room_id":1,"device_name":"测试天天23","device_image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","device_state1":1,"device_state2":0,"device_state3":0,"alarm_status":0,"other_status":"50","mac_address":"192.168.1.3"},{"scene_detail_id":7,"device_id":3,"room_id":1,"device_name":"测试天天23","device_image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","device_state1":1,"device_state2":0,"device_state3":0,"alarm_status":0,"other_status":"50","mac_address":"192.168.1.3"}]}
     * error : {"code":"","message":""}
     */

    private boolean success;
    /**
     * scene_id : 3
     * name : 上学去
     * image : 0008.jpg
     * scene_details : [{"scene_detail_id":4,"device_id":1,"room_id":1,"device_name":"测试天天","device_image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","device_state1":1,"device_state2":0,"device_state3":0,"alarm_status":0,"other_status":"30","mac_address":"192.168.1.1"},{"scene_detail_id":6,"device_id":3,"room_id":1,"device_name":"测试天天23","device_image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","device_state1":1,"device_state2":0,"device_state3":0,"alarm_status":0,"other_status":"50","mac_address":"192.168.1.3"},{"scene_detail_id":7,"device_id":3,"room_id":1,"device_name":"测试天天23","device_image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","device_state1":1,"device_state2":0,"device_state3":0,"alarm_status":0,"other_status":"50","mac_address":"192.168.1.3"}]
     */

    private SceneInfo result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public SceneInfo getResult() {
        return result;
    }

    public void setResult(SceneInfo result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "UpdateSceneResponse{" +
                "success=" + success +
                ", result=" + result +
                '}';
    }
}
