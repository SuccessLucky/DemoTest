package com.project.entity;

import com.project.common.entity.DomainObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "gateway_tbl")
public class GatewayEntity extends DomainObject {

    private String macAddress; // 网关mac地址
    private String gatewayName;
    private String wifiMacAddress; // WiFi模块,MAC地址
    private int securityStatus; // 0 撤防, 1布防

    @Column(name = "mac_address")
    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Column(name = "gateway_name")
    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public String getWifiMacAddress() {
        return wifiMacAddress;
    }

    @Column(name = "wifi_mac_address")
    public void setWifiMacAddress(String wifiMacAddress) {
        this.wifiMacAddress = wifiMacAddress;
    }

    @Column(name = "security_status", columnDefinition = "tinyint default 0")
    public int getSecurityStatus() {
        return securityStatus;
    }

    public void setSecurityStatus(int securityStatus) {
        this.securityStatus = securityStatus;
    }
}
