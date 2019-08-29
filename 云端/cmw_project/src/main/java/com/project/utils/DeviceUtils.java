package com.project.utils;

import com.project.bean.DeviceReq;

import java.util.Arrays;

/**
 * Created by xieyanhao on 16/10/30.
 */
public class DeviceUtils {

    final public static String[] DEVICE_OD_INFRARED = {"0F E6"};
    final public static String[] DEVICE_CATEGORY_INFRARED = {"02", "15"};
    final public static String[] DEVICE_TYPE_INFRARED = {"02"};

    final public static String[] DEVICE_OD_LIGHT = {"0F AA"};
    final public static String[] DEVICE_CATEGORY_LIGHT = {"02", "04"};
    final public static String[] DEVICE_TYPE_LIGHT = {"06", "07"};


    public static boolean isGeneralMacDevice(DeviceReq.DeviceItem device) {

        if (Arrays.asList(DEVICE_OD_INFRARED).contains(device.getDevice_OD())
                && Arrays.asList(DEVICE_TYPE_INFRARED).contains(device.getDevice_type())
                && Arrays.asList(DEVICE_CATEGORY_INFRARED).contains(device.getCategory())) {
            // 红外转发设备,od category
            return true;
        }

        if (Arrays.asList(DEVICE_OD_LIGHT).contains(device.getDevice_OD())
                && Arrays.asList(DEVICE_TYPE_LIGHT).contains(device.getDevice_type())
                && Arrays.asList(DEVICE_CATEGORY_LIGHT).contains(device.getCategory())) {
            // 二三路开关,od type category
            return true;
        }

        return false;
    }
}
