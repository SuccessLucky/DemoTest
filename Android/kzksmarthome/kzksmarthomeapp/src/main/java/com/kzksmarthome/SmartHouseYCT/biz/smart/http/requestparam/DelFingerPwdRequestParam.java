package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

/**
 * @Title: DelFingerPwdRequestParam
 * @Description: 删除门锁指纹/密码请求参数
 * @author laixj
 * @date 2016/9/18 12:17
 * @version V1.0
 */
public class DelFingerPwdRequestParam {
    /**
     * lock_id : 1
     * lock_type : psw
     */

    int lock_id;
    String lock_type;

    public int getLock_id() {
        return lock_id;
    }

    public void setLock_id(int lock_id) {
        this.lock_id = lock_id;
    }

    public String getLock_type() {
        return lock_type;
    }

    public void setLock_type(String lock_type) {
        this.lock_type = lock_type;
    }

    @Override
    public String toString() {
        return "DelFingerPwdRequestParam{" +
                "lock_id=" + lock_id +
                ", lock_type='" + lock_type + '\'' +
                '}';
    }
}
