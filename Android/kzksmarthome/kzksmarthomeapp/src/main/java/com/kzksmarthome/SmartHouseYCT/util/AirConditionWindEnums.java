package com.kzksmarthome.SmartHouseYCT.util;

/**
 * @Title: DeviceTypeEnums
 * @Description: 空调风速枚举
 * @author laixj
 * @date 2016/9/8 22:07
 * @version V1.0
 */
public enum AirConditionWindEnums {
    AUTO((short)0, "自动"),
    HIGH((short)3, "高速"),
    MIDDLE((short)2, "中速"),
    LOW((short)1, "低速");

    private short code;
    private String name;

    private AirConditionWindEnums(Short code, String name) {
        this.code = code;
        this.name = name;
    }

    public short getCode() {
        return code;
    }

    public void setCode(short code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }
}
