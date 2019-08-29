package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

/**
 * @Title: RegisterRequestParam
 * @Description: 注册请求参数
 * @author laixj
 * @date 2016/9/18 12:17
 * @version V1.0
 */
public class RegisterRequestParam {
    String code;
    String phone;
    String password;
    String nickname;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
