package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

/**
 * @Title: UpdateGwSecurityRequestParam
 * @Description: 修改网关安防状态请求参数
 * @author laixj
 * @date 2016/10/31 22:34
 * @version V1.0
 */
public class UpdateGwSecurityRequestParam {
    int security_status;

    public int getSecurity_status() {
        return security_status;
    }

    public void setSecurity_status(int security_status) {
        this.security_status = security_status;
    }
}
