package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

/**
 * @Title: DelDeviceRequestParam
 * @Description: 删除设备请求参数
 * @author laixj
 * @date 2016/9/18 12:17
 * @version V1.0
 */
public class DelMacDeviceRequestParam {
    String mac_address;//

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }
}
