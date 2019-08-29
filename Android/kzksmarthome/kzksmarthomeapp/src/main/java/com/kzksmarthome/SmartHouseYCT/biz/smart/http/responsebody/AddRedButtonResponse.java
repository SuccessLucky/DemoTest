package com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody;

import com.kzksmarthome.common.lib.okhttp.BaseResponse;

import java.util.List;

/**
 * 添加红外按钮response
 * @author lizhid
 * @version V1.0
 * @description:
 * @date 2016/10/20
 */
public class AddRedButtonResponse extends BaseResponse {

    /**
     * success : true
     * result : {"device_id":1,"room_id":1,"name":"测试","image":"xxxxx","device_OD":"0x001","device_type":"0x01a","category":"awc","sindex":"0001","device_state1":1,"device_state2":1,"device_state3":1,"alarm_status":0,"other_status":"75","mac_address":"192.168.1.1","device_buttons":[{"button_id":1,"name":"音量加","instruction_code":"0001x11"},{"button_id":2,"name":"音量减","instruction_code":"0001x12"}]}
     * error : {"code":"","message":""}
     */

    private boolean success;
    /**
     * device_id : 1
     * room_id : 1
     * name : 测试
     * image : xxxxx
     * device_OD : 0x001
     * device_type : 0x01a
     * category : awc
     * sindex : 0001
     * device_state1 : 1
     * device_state2 : 1
     * device_state3 : 1
     * alarm_status : 0
     * other_status : 75
     * mac_address : 192.168.1.1
     * device_buttons : [{"button_id":1,"name":"音量加","instruction_code":"0001x11"},{"button_id":2,"name":"音量减","instruction_code":"0001x12"}]
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
        private int device_id;
        private int room_id;
        private String name;
        private String image;
        private String device_OD;
        private String device_type;
        private String category;
        private String sindex;
        private int device_state1;
        private int device_state2;
        private int device_state3;
        private int alarm_status;
        private String other_status;
        private String mac_address;
        /**
         * button_id : 1
         * name : 音量加
         * instruction_code : 0001x11
         */

        private List<DeviceButtonsBean> device_buttons;

        public int getDevice_id() {
            return device_id;
        }

        public void setDevice_id(int device_id) {
            this.device_id = device_id;
        }

        public int getRoom_id() {
            return room_id;
        }

        public void setRoom_id(int room_id) {
            this.room_id = room_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDevice_OD() {
            return device_OD;
        }

        public void setDevice_OD(String device_OD) {
            this.device_OD = device_OD;
        }

        public String getDevice_type() {
            return device_type;
        }

        public void setDevice_type(String device_type) {
            this.device_type = device_type;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSindex() {
            return sindex;
        }

        public void setSindex(String sindex) {
            this.sindex = sindex;
        }

        public int getDevice_state1() {
            return device_state1;
        }

        public void setDevice_state1(int device_state1) {
            this.device_state1 = device_state1;
        }

        public int getDevice_state2() {
            return device_state2;
        }

        public void setDevice_state2(int device_state2) {
            this.device_state2 = device_state2;
        }

        public int getDevice_state3() {
            return device_state3;
        }

        public void setDevice_state3(int device_state3) {
            this.device_state3 = device_state3;
        }

        public int getAlarm_status() {
            return alarm_status;
        }

        public void setAlarm_status(int alarm_status) {
            this.alarm_status = alarm_status;
        }

        public String getOther_status() {
            return other_status;
        }

        public void setOther_status(String other_status) {
            this.other_status = other_status;
        }

        public String getMac_address() {
            return mac_address;
        }

        public void setMac_address(String mac_address) {
            this.mac_address = mac_address;
        }

        public List<DeviceButtonsBean> getDevice_buttons() {
            return device_buttons;
        }

        public void setDevice_buttons(List<DeviceButtonsBean> device_buttons) {
            this.device_buttons = device_buttons;
        }

        public static class DeviceButtonsBean {
            private int button_id;
            private String name;
            private String instruction_code;

            public int getButton_id() {
                return button_id;
            }

            public void setButton_id(int button_id) {
                this.button_id = button_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getInstruction_code() {
                return instruction_code;
            }

            public void setInstruction_code(String instruction_code) {
                this.instruction_code = instruction_code;
            }
        }
    }
}
