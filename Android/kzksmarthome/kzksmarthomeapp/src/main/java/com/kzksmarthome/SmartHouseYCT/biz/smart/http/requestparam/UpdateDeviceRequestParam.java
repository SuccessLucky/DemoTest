package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

/**
 * @Title: UpdateDeviceRequestParam
 * @Description: 修改设备请求参数
 * @author laixj
 * @date 2016/9/18 12:17
 * @version V1.0
 */
public class UpdateDeviceRequestParam {
    int device_id;
    String device_name;
    String image;
    String other_state;
    int device_state1;
    int device_state2;
    int device_state3;

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
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

    public String getOther_state() {
        return other_state;
    }

    public void setOther_state(String other_state) {
        this.other_state = other_state;
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
        return "UpdateDeviceRequestParam{" +
                "device_id=" + device_id +
                ", device_name='" + device_name + '\'' +
                ", image='" + image + '\'' +
                ", other_state='" + other_state + '\'' +
                ", device_state1=" + device_state1 +
                ", device_state2=" + device_state2 +
                ", device_state3=" + device_state3 +
                '}';
    }
}
