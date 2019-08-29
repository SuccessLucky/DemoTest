package com.project.bean;

/**
 * Created by xieyanhao on 16/9/28.
 */
public class UserGatewayBean {

    private int gateway_id;
    private String mac_address; // 网关mac地址
    private String wifi_mac_address; // WiFi mac地址
    private int member_type; // 1 网关管理员, 2 网关成员
    private int security_status; // 安防状态
    private String gateway_name;

    public int getGateway_id() {
        return gateway_id;
    }

    public void setGateway_id(int gateway_id) {
        this.gateway_id = gateway_id;
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

    public int getMember_type() {
        return member_type;
    }

    public void setMember_type(int member_type) {
        this.member_type = member_type;
    }

    public int getSecurity_status() {
        return security_status;
    }

    public void setSecurity_status(int security_status) {
        this.security_status = security_status;
    }

    public String getGateway_name() {
        return gateway_name;
    }

    public void setGateway_name(String gateway_name) {
        this.gateway_name = gateway_name;
    }
}
