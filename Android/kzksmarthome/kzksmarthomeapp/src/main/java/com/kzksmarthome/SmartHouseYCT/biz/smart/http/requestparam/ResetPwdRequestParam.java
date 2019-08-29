package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

/**
 * @Title: ResetPwdRequestParam
 * @Description: 重置密码请求参数
 * @author laixj
 * @date 2016/9/18 12:17
 * @version V1.0
 */
public class ResetPwdRequestParam {
    String code;
    String phone;
    String new_password;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}
