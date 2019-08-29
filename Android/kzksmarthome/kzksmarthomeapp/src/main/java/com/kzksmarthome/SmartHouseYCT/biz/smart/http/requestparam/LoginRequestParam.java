package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

/**
 * @Title: LoginRequestParam
 * @Description: 登陆请求参数
 * @author laixj
 * @date 2016/9/18 12:17
 * @version V1.0
 */
public class LoginRequestParam extends BaseModel{
    String username;
    String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
