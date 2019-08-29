package com.kzksmarthome.SmartHouseYCT.util;

/**
 * @Title: DeviceTypeEnums
 * @Description: 设备状态枚举
 * @author laixj
 * @date 2016/9/8 22:07
 * @version V1.0
 */
public enum DeviceStatusEnums {
    OFF(2, "关"),
    ON(1, "开"),
    STOP(3,"停");

    private int code;
    private String name;

    private DeviceStatusEnums(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }
}
