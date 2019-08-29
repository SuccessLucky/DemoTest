package com.kzksmarthome.SmartHouseYCT.util;

/**
 * @Title: DeviceTypeEnums
 * @Description: 空调模式枚举
 * @author laixj
 * @date 2016/9/8 22:07
 * @version V1.0
 */
public enum AirConditionModeEnums {
    AUTO((short)0, "自动"),
    COLD((short)1, "制冷"),
    HOT((short)4, "制热"),
    WIND((short)3, "送风"),
    XERANSIS((short)2, "除湿");

    private short code;
    private String name;

    private AirConditionModeEnums(Short code, String name) {
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
