package com.kzksmarthome.SmartHouseYCT.util;

/**
 * @Title: ImageTypeEnums
 * @Description: 图片类型枚举
 * @author laixj
 * @date 2016/10/11 14:06
 * @version V1.0
 */
public enum ImageTypeEnums {
    DEVICE(1, "设备"),
    ROOM(2, "房间"),
    SCENE(3, "场景");

    private int code;
    private String name;

    private ImageTypeEnums(int code, String name) {
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
