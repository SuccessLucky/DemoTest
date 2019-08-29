package com.kzksmarthome.SmartHouseYCT.util;

/**
 * @Title: RoomTypeEnums
 * @Description: 房间类型枚举
 * @author laixj
 * @date 2016/9/10 23:11
 * @version V1.0
 */
public enum RoomTypeEnums {
    BALCONY((short)0, "阳台"),
    BEDROOM((short)1, "卧室"),
    CHILDRENROOM((short)2, "儿童房"),
    DININGROOM((short)3, "餐厅"),
    DRAWINGROOM((short)4, "客厅"),
    KITCHEN((short)5, "厨房"),
    RESTROOM((short)6, "卫生间"),
    SCHOOLROOM((short)7, "书房");

    private short code;
    private String name;

    private RoomTypeEnums(Short code, String name) {
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
