package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.SceneInfo;
import com.kzksmarthome.common.lib.okhttp.BaseResponse;

import java.util.List;

/**
 * @Title: GetRightsListResponse
 * @Description: 获取子账户权限列表返回
 * @author laixj
 * @date 2016/10/22 8:31
 * @version V1.0
 */
public class GetRightsListResponse extends BaseResponse {

    /**
     * success : true
     * result : {"member_id":null,"scenes":[{"scene_id":2,"name":"回家","image":"0008.jpg"},{"scene_id":3,"name":"上学去","image":"0008.jpg"}],"devices":[{"device_id":1,"name":"测试天天","image":"xxxxx","room_id":1,"floor_id":1,"room_name":"大明湖畔","floor_name":"第三层"},{"device_id":2,"name":"测试天天2","image":"xxxxx","room_id":2,"floor_id":1,"room_name":"大明湖畔","floor_name":"第三层"},{"device_id":3,"name":"测试天天23","image":"xxxxx","room_id":1,"floor_id":1,"room_name":"大明湖畔","floor_name":"第三层"}]}
     * error : {"code":"","message":""}
     */

    private boolean success;
    /**
     * member_id : null
     * scenes : [{"scene_id":2,"name":"回家","image":"0008.jpg"},{"scene_id":3,"name":"上学去","image":"0008.jpg"}]
     * devices : [{"device_id":1,"name":"测试天天","image":"xxxxx","room_id":1,"floor_id":1,"room_name":"大明湖畔","floor_name":"第三层"},{"device_id":2,"name":"测试天天2","image":"xxxxx","room_id":2,"floor_id":1,"room_name":"大明湖畔","floor_name":"第三层"},{"device_id":3,"name":"测试天天23","image":"xxxxx","room_id":1,"floor_id":1,"room_name":"大明湖畔","floor_name":"第三层"}]
     */

    private ResultBean result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private int member_id;
        /**
         * scene_id : 2
         * name : 回家
         * image : 0008.jpg
         */

        private List<SceneInfo> scenes;
        /**
         * device_id : 1
         * name : 测试天天
         * image : xxxxx
         * room_id : 1
         * floor_id : 1
         * room_name : 大明湖畔
         * floor_name : 第三层
         */

        private List<DeviceInfo> devices;

        public int getMember_id() {
            return member_id;
        }

        public void setMember_id(int member_id) {
            this.member_id = member_id;
        }

        public List<SceneInfo> getScenes() {
            return scenes;
        }

        public void setScenes(List<SceneInfo> scenes) {
            this.scenes = scenes;
        }

        public List<DeviceInfo> getDevices() {
            return devices;
        }

        public void setDevices(List<DeviceInfo> devices) {
            this.devices = devices;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "member_id=" + member_id +
                    ", scenes=" + scenes +
                    ", devices=" + devices +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AddUserRightsResponse{" +
                "success=" + success +
                ", result=" + result +
                '}';
    }
}
