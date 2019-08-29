package com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam;

/**
 * @Title: AddGwRequestParam
 * @Description: 增加网关请求参数
 * @author laixj
 * @date 2016/9/18 12:17
 * @version V1.0
 */
public class AddGwRequestParam {
    /**
     * gateway_name : 我家
     * mac_address : f4:5c:89:c4:12:9d
     */

    String gateway_name;
    String mac_address;
    String wifi_mac_address;

    public String getGateway_name() {
        return gateway_name;
    }

    public void setGateway_name(String gateway_name) {
        this.gateway_name = gateway_name;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

    public String getWifi_mac_address() {
        return wifi_mac_address;
    }

    public void setWifi_mac_address(String wifi_mac_address) {
        this.wifi_mac_address = wifi_mac_address;
    }

    @Override
    public String toString() {
        return "AddGwRequestParam{" +
                "gateway_name='" + gateway_name + '\'' +
                ", mac_address='" + mac_address + '\'' +
                ", wifi_mac_address='" + wifi_mac_address + '\'' +
                '}';
    }
}
