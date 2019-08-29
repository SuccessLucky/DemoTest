package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

/**
 * @Title: AddFingerRequestParam
 * @Description: 增加指纹请求参数
 * @author laixj
 * @date 2016/9/18 12:17
 * @version V1.0
 */
public class AddFingerRequestParam extends BaseModel{
    /**
     * device_id : 1
     * user_name : 阿毛
     * fingerprint_id : 6876e876r8we
     */

    int device_id;
    String user_name;
    String fingerprint_id;

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFingerprint_id() {
        return fingerprint_id;
    }

    public void setFingerprint_id(String fingerprint_id) {
        this.fingerprint_id = fingerprint_id;
    }

    @Override
    public String toString() {
        return "AddFingerRequestParam{" +
                "device_id=" + device_id +
                ", user_name='" + user_name + '\'' +
                ", fingerprint_id='" + fingerprint_id + '\'' +
                '}';
    }
}
