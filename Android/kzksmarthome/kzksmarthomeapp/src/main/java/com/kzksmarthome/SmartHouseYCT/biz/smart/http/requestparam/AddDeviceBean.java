package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

/**
 * @Title: AddDeviceBean
 * @Description: 增加设备请求参数
 * @author laixj
 * @date 2016/9/18 12:17
 * @version V1.0
 */
public class AddDeviceBean extends BaseModel{
    private int room_id;
    private String device_name;
    private String image;
    private String device_OD;
    private String device_type;
    private String category;
    private String cmd_id;
    private String sindex_length;
    private String sindex;
    private String mac_address;
    private String other_status;
    private int device_state1;
    private int device_state2;
    private int device_state3;

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
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

    public String getCmd_id() {
        return cmd_id;
    }

    public void setCmd_id(String cmd_id) {
        this.cmd_id = cmd_id;
    }

    public String getSindex_length() {
        return sindex_length;
    }

    public void setSindex_length(String sindex_length) {
        this.sindex_length = sindex_length;
    }

    public String getSindex() {
        return sindex;
    }

    public void setSindex(String sindex) {
        this.sindex = sindex;
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

    @Override
    public String toString() {
        return "AddDeviceBean{" +
                "room_id=" + room_id +
                ", device_name='" + device_name + '\'' +
                ", image='" + image + '\'' +
                ", device_OD='" + device_OD + '\'' +
                ", device_type='" + device_type + '\'' +
                ", category='" + category + '\'' +
                ", sindex='" + sindex + '\'' +
                ", sindex_length='" + sindex_length + '\'' +
                ", cmdId='" + cmd_id + '\'' +
                ", mac_address='" + mac_address + '\'' +
                ", other_status='" + other_status + '\'' +
                ", device_state1=" + device_state1 +
                ", device_state2=" + device_state2 +
                ", device_state3=" + device_state3 +
                '}';
    }
}
