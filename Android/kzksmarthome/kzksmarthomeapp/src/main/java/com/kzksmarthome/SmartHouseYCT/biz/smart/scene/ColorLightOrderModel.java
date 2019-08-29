package com.kzksmarthome.SmartHouseYCT.biz.smart.scene;

import com.kzksmarthome.SmartHouseYCT.biz.smart.http.BaseModel;

/**
 * 多彩灯数据模型
 * Created by lizhi on 2017/10/17.
 */

public class ColorLightOrderModel extends BaseModel {
    /**
     * RGB命令
     */
    byte[] orderArray;

    /**
     * 设备mac地址
     */
    private String mac_address;

    public byte[] getOrderArray() {
        return orderArray;
    }

    public void setOrderArray(byte[] orderArray) {
        this.orderArray = orderArray;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }
}
