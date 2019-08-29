package com.project.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xieyanhao on 16/10/30.
 */
public class DeviceReq {

    List<DeviceItem> devices = new ArrayList<>();

    public List<DeviceItem> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceItem> devices) {
        this.devices = devices;
    }

    public static class DeviceItem {

        private Integer room_id;
        private String device_name;
        private String image;
        private String device_OD; // Object Dictionary
        private String device_type; // 设备类型
        private String category; // 设备类别
        private String sindex; // 子索引
        private String sindex_length; // 子索引长度
        private String cmd_id;
        private String mac_address;
        private String other_status;
        private String other_info;
        private String regional; // 位置信息
        private int control_type; // 控制类型 单控:0,群控:1
        private int device_state1; // 状态 默认0:关闭,1:打开
        private int device_state2; // 状态 默认0:关闭,1:打开
        private int device_state3; // 状态 默认0:关闭,1:打开

        public Integer getRoom_id() {
            return room_id;
        }

        public void setRoom_id(Integer room_id) {
            this.room_id = room_id;
        }

        public String getDevice_name() {
            return device_name;
        }

        public void setDevice_name(String device_name) {
            this.device_name = device_name;
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

        public String getSindex_length() {
            return sindex_length;
        }

        public void setSindex_length(String sindex_length) {
            this.sindex_length = sindex_length;
        }

        public String getCmd_id() {
            return cmd_id;
        }

        public void setCmd_id(String cmd_id) {
            this.cmd_id = cmd_id;
        }

        public String getMac_address() {
            return mac_address;
        }

        public void setMac_address(String mac_address) {
            this.mac_address = mac_address;
        }

        public String getOther_status() {
            return other_status;
        }

        public void setOther_status(String other_status) {
            this.other_status = other_status;
        }

        public String getOther_info() {
            return other_info;
        }

        public void setOther_info(String other_info) {
            this.other_info = other_info;
        }

        public String getRegional() {
            return regional;
        }

        public void setRegional(String regional) {
            this.regional = regional;
        }

        public int getControl_type() {
            return control_type;
        }

        public void setControl_type(int control_type) {
            this.control_type = control_type;
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
    }
}
