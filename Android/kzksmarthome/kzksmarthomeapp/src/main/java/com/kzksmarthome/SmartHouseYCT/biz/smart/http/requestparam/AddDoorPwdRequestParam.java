package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

/**
 * @Title: AddDoorPwdRequestParam
 * @Description: 增加门锁密码请求参数
 * @author laixj
 * @date 2016/9/18 12:17
 * @version V1.0
 */
public class AddDoorPwdRequestParam {
    /**
     * device_id : 1
     * unlock_psw : 12345678
     */

    int device_id;
    String unlock_psw;

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getUnlock_psw() {
        return unlock_psw;
    }

    public void setUnlock_psw(String unlock_psw) {
        this.unlock_psw = unlock_psw;
    }

    @Override
    public String toString() {
        return "AddDoorPwdRequestParam{" +
                "device_id=" + device_id +
                ", unlock_psw='" + unlock_psw + '\'' +
                '}';
    }
}
