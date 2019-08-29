package com.kzksmarthome.SmartHouseYCT.util;

/**
 * @Title: DeviceCtrTypeEnums
 * @Description: 设备控制类型枚举
 * @author laixj
 * @date 2016/9/17 18:00
 * @version V1.0
 */
public enum DeviceCtrTypeEnums {
    NORMAL("normal", "常用设备"),
    AIRCONDITION("aircondition", "空调"),
    INFRARED("infrared", "其他红外设备"),
    FORWARD("forward","协议转发");

    private String code;
    private String name;

    private DeviceCtrTypeEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }
}
