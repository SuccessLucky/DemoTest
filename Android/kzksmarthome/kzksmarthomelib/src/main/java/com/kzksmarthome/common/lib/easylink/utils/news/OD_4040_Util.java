package com.kzksmarthome.common.lib.easylink.utils.news;

/**
 * Created by Administrator on 2016/11/18 0018.
 */
public class OD_4040_Util {
    private static final String STATE_OPEN = "01"; //开状态
    private static final String STATE_STOP = "02"; //关状态
    //计量设备开关指令-单个
    public static String getCmd_MeteringEquipment(String gateway_mac, String mac, boolean state){
        return CommandUtil.getCmd_MeteringEquipment(gateway_mac,mac,"","",(state?STATE_OPEN:STATE_STOP));
    }
    //计量设备开关指令-区域
    public static String getCmd_MeteringEquipment_Area(String gateway_mac, String type_area, String area, boolean state){
        return CommandUtil.getCmd_MeteringEquipment(gateway_mac,"",type_area,area,(state?STATE_OPEN:STATE_STOP));
    }
    //计量设备电量清零-单个
    public static String getCmd_MeteringEquipmentSetZero(String gateway_mac, String mac) {
        return CommandUtil.getCmd_MeteringEquipmentSetZero(gateway_mac,mac);
    }
}
