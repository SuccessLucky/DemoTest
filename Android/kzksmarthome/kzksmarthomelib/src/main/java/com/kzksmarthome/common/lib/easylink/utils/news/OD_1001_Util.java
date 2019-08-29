package com.kzksmarthome.common.lib.easylink.utils.news;

/**
 * Created by Administrator on 2016/11/24 0024.
 */
public class OD_1001_Util {
    //读取网关无线通道
    public static String getCmd_ReadWirelessChannel_For_Gateway(String gateway_mac){
        return CommandUtil.getCmd_ReadWirelessChannel(gateway_mac,"");
    }
    //读取设备无线通道
    public static String getCmd_ReadWirelessChannel_For_Node(String gateway_mac, String mac){
        return CommandUtil.getCmd_ReadWirelessChannel(gateway_mac,mac);
    }
    //写入网关无线通道
    public static String getCmd_WriteWirelessChannel_For_Gateway(String gateway_mac, String data){
        return CommandUtil.getCmd_WriteWirelessChannel(gateway_mac,"",data);
    }
    //写入设备无线通道
    public static String getCmd_WriteWirelessChannel_For_Node(String gateway_mac, String mac, String data){
        return CommandUtil.getCmd_WriteWirelessChannel(gateway_mac,mac,data);
    }
    //获取读取指定mac的无线通道反馈的无线通道值
    public static String getWirelessChannelFromString(String data, String mac){
        return CommandUtil.getWirelessChannelFromString(data,mac);
    }
    //读取网关pan id
    public static String getCmd_ReadPanId_For_Gateway(String gateway_mac){
        return CommandUtil.getCmd_ReadPanId(gateway_mac,"");
    }
    //读取设备pan id
    public static String getCmd_ReadPanId_For_Node(String gateway_mac, String mac){
        return CommandUtil.getCmd_ReadPanId(gateway_mac,mac);
    }
    //写入网关pan id
    public static String getCmd_WritePanId_For_Gateway(String gateway_mac, String data){
        return CommandUtil.getCmd_WritePanId(gateway_mac,"",data);
    }
    //写入设备pan id
    public static String getCmd_WritePanId_For_Node(String gateway_mac, String mac, String data){
        return CommandUtil.getCmd_WritePanId(gateway_mac,mac,data);
    }
    //获取读取指定mac的pan id反馈的pan id值
    public static String getPanIdFromString(String data, String mac){
        return CommandUtil.getPanIdFromString(data,mac);
    }
}
