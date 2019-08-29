package com.kzksmarthome.SmartHouseYCT.util;

/**
 * @Title: UserRoleEnums
 * @Description: 用户角色枚举
 * @author laixj
 * @date 2016/9/14 7:13
 * @version V1.0
 */
public enum UserRoleEnums {
    ADMIN(1, "管理员"),
    USER(2, "用户");

    private int code;
    private String name;

    private UserRoleEnums(int code, String name) {
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
