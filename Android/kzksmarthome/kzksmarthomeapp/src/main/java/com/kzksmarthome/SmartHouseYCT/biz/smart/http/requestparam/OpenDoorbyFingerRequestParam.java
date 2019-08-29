package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

/**
 * @Title: OpenDoorbyFingerRequestParam
 * @Description: 指纹解锁请求参数
 * @author laixj
 * @date 2016/9/18 12:17
 * @version V1.0
 */
public class OpenDoorbyFingerRequestParam {
    /**
     * device_id : 1
     * fingerprint_id : 12345678
     */

    int device_id;
    String fingerprint_id;

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getFingerprint_id() {
        return fingerprint_id;
    }

    public void setFingerprint_id(String fingerprint_id) {
        this.fingerprint_id = fingerprint_id;
    }

    @Override
    public String toString() {
        return "OpenDoorbyFingerRequestParam{" +
                "device_id=" + device_id +
                ", fingerprint_id='" + fingerprint_id + '\'' +
                '}';
    }
}
