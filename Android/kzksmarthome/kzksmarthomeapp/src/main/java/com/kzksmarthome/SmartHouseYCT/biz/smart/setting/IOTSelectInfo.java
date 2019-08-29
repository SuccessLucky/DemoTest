package com.kzksmarthome.SmartHouseYCT.biz.smart.setting;

/**
 * 网关配置信息
 * Created by jack on 2016/10/27.
 */

public class IOTSelectInfo {
    private String iot_wifi_mac;
    private String hostIp;
    private String hostPort;

    public String getIot_wifi_mac() {
        return iot_wifi_mac;
    }

    public void setIot_wifi_mac(String iot_wifi_mac) {
        this.iot_wifi_mac = iot_wifi_mac;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getHostPort() {
        return hostPort;
    }

    public void setHostPort(String hostPort) {
        this.hostPort = hostPort;
    }
}
