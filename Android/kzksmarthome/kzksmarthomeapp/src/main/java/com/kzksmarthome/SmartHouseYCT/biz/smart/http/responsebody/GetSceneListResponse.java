package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.SceneInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

import java.util.List;

/**
 * @Title: GetSceneListResponse
 * @Description: 获取场景列表返回
 * @author laixj
 * @date 2016/10/15 7:21
 * @version V1.0
 */
public class GetSceneListResponse extends BaseResponse {
    /**
     * success : true
     * result : [{"scene_id":2,"name":"回家","image":"0002.jpg","scene_details":[{"scene_detail_id":1,"device_id":1,"room_id":1,"device_name":"测试天天","device_image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","device_state1":1,"device_state2":0,"device_state3":0,"alarm_status":0,"other_status":"30","mac_address":"192.168.1.1"},{"scene_detail_id":2,"device_id":2,"room_id":1,"device_name":"测试天天2","device_image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","device_state1":1,"device_state2":0,"device_state3":0,"alarm_status":0,"other_status":"70","mac_address":"192.168.1.2"}]},{"scene_id":3,"name":"上学去","image":"0008.jpg","scene_details":[{"scene_detail_id":4,"device_id":1,"room_id":1,"device_name":"测试天天","device_image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","device_state1":1,"device_state2":0,"device_state3":0,"alarm_status":0,"other_status":"30","mac_address":"192.168.1.1"},{"scene_detail_id":6,"device_id":3,"room_id":1,"device_name":"测试天天23","device_image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","device_state1":1,"device_state2":0,"device_state3":0,"alarm_status":0,"other_status":"50","mac_address":"192.168.1.3"},{"scene_detail_id":7,"device_id":3,"room_id":1,"device_name":"测试天天23","device_image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","device_state1":1,"device_state2":0,"device_state3":0,"alarm_status":0,"other_status":"50","mac_address":"192.168.1.3"}]}]
     * error : {"code":"","message":""}
     */

    private boolean success;
    /**
     * scene_id : 2
     * name : 回家
     * image : 0002.jpg
     * scene_details : [{"scene_detail_id":1,"device_id":1,"room_id":1,"device_name":"测试天天","device_image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","device_state1":1,"device_state2":0,"device_state3":0,"alarm_status":0,"other_status":"30","mac_address":"192.168.1.1"},{"scene_detail_id":2,"device_id":2,"room_id":1,"device_name":"测试天天2","device_image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","device_state1":1,"device_state2":0,"device_state3":0,"alarm_status":0,"other_status":"70","mac_address":"192.168.1.2"}]
     */

    private List<SceneInfo> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<SceneInfo> getResult() {
        return result;
    }

    public void setResult(List<SceneInfo> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "GetSceneListResponse{" +
                "success=" + success +
                ", result=" + result +
                '}';
    }
}
