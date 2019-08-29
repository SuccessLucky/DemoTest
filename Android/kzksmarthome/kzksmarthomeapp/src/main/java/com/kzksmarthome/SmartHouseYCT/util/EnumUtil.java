package com.kzksmarthome.SmartHouseYCT.util;

import java.util.Arrays;
import java.util.List;

/**
 * @Title: EnumUtil
 * @Description: 枚举相关工具
 * @author laixj
 * @date 2016/9/17 16:58
 * @version V1.0
 */
public class EnumUtil {
    public static <T extends Enum> List<T> toList(Class<T> clazz) {
        return Arrays.asList(clazz.getEnumConstants());
    }
}
