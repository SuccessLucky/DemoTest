package com.kzksmarthome.common.lib.easylink.utils.news;

/**
 * Created by Administrator on 2016/11/24 0024.
 */
public class OD_1007_Util {
    //单址读取设备波特率
    public static String getCmd_ReadBaudRate(String gateway_mac, String mac){
        return CommandUtil.getCmd_ReadBaudRate(gateway_mac,mac);
    }
    //单址写入设备波特率
    public static String getCmd_WriteBaudRate(String gateway_mac, String mac, String data){
        return CommandUtil.getCmd_WriteBaudRate(gateway_mac,mac,data);
    }
    //单址读取设备波特率反馈解析，获取设备波特率字段   返回null代表解析失败
    public static String getBaudRateFromString(String data, String mac){
        return CommandUtil.getBaudRateFromString(data,mac);
    }
    //单址读取设备检验方式
    public static String getCmd_ReadCheckType(String gateway_mac, String mac){
        return CommandUtil.getCmd_ReadCheckType(gateway_mac,mac);
    }
    //单址写入设备检验方式
    public static String getCmd_WriteCheckType(String gateway_mac, String mac, String data){
        return CommandUtil.getCmd_WriteCheckType(gateway_mac,mac,data);
    }
    //单址读取设备检验方式反馈解析，获取设备检验方式字段   返回null代表解析失败
    public static String getCheckTypeFromString(String data, String mac){
        return CommandUtil.getCheckTypeFromString(data,mac);
    }
    //单址读取设备类别
    public static String getCmd_ReadEquipmentCategory(String gateway_mac, String mac){
        return CommandUtil.getCmd_ReadEquipmentCategory(gateway_mac,mac);
    }
    //单址写入设备类别
    public static String getCmd_WriteEquipmentCategory(String gateway_mac, String mac, String data){
        return CommandUtil.getCmd_WriteEquipmentCategory(gateway_mac,mac,data);
    }
    //单址读取设备类别反馈解析，获取设备类别字段   返回null代表解析失败
    public static String getEquipmentCategoryFromString(String data, String mac){
        return CommandUtil.getEquipmentCategoryFromString(data,mac);
    }
    //单址读取产品类型
    public static String getCmd_ReadProductType(String gateway_mac, String mac){
        return CommandUtil.getCmd_ReadProductType(gateway_mac,mac);
    }
    //单址写入产品类型
    public static String getCmd_WriteProductType(String gateway_mac, String mac, String data){
        return CommandUtil.getCmd_WriteProductType(gateway_mac,mac,data);
    }
    //单址读取产品类型反馈解析，获取产品类型字段   返回null代表解析失败
    public static String getProductTypeFromString(String data, String mac){
        return CommandUtil.getProductTypeFromString(data,mac);
    }
}
