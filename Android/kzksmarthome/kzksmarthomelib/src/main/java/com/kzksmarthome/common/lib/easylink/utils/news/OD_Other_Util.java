package com.kzksmarthome.common.lib.easylink.utils.news;

import com.kzksmarthome.common.lib.easylink.modle.Modle_DistanceData;

import java.util.List;



/**
 * Created by Administrator on 2016/11/18 0018.
 */
public class OD_Other_Util {
    //获取设置区域指令
    public static String getSetAreaCommand(String gateway_mac, String mac, String area){
        return CommandUtil.getSetAreaCommand(gateway_mac,mac,area);
    }
    //设置场景
    public static String[] getSetSceneCommand_ForCJ(List<String> cmds, String mac, String num, String stateNum, String time, String startTime){
        return CommandUtil.getSetSceneCommand_ForCJ(cmds,mac,num,stateNum,time,startTime);
    }
    //删除场景
    public static String getDelSceneCommand(String mac, String num){
        return CommandUtil.getDelSceneCommand(mac,num);
    }
    //触发场景
    public static String getStartSceneCommand(String mac, String num){
        return CommandUtil.getStartSceneCommand(mac,num);
    }

    //云端读取远程地址、远程端口、本地端口
    public static String getCmd_ReadDistanceData_All(String gateway_mac){
        return CommandUtil.getCmd_ReadDistanceData(gateway_mac,true,true,true);
    }
    //云端读取远程地址、远程端口、本地端口反馈解析
    public static Modle_DistanceData getDistanceDataFromString(String data, String mac){
        return CommandUtil.getDistanceDataFromString(data,mac);
    }
    //云端配置远程地址、远程端口、本地端口
    public static String getCmd_WriteDistanceData(String gateway_mac, String dip, String dp, String lp){
        return CommandUtil.getCmd_WriteDistanceData(gateway_mac,dip,dp,lp);
    }

    //节点复位
    public static String getCmd_Reset_ForNode(String gateway_mac, String mac){
        return CommandUtil.getCmd_Reset(gateway_mac,mac);
    }
    //网关复位
    public static String getCmd_Reset_ForGateway(String gateway_mac){
        return CommandUtil.getCmd_Reset(gateway_mac,"");
    }
    //心跳置零指令
    public static String getCmd_HeartBeatSetZero(String gateway_mac){
        return CommandUtil.getCmd_HeartBeatSetZero(gateway_mac,"00");
    }
    //判断是否是心跳帧
    public static boolean getBooleanFromHeartBeatString(String data, String mac){
        return CommandUtil.getBooleanFromHeartBeatString(data,mac);
    }
    //开启入网时间窗
    public static String getCmd_openAddModles(String gateway_mac, String time){
        return CommandUtil.getCmd_openAddModles(gateway_mac,time);
    }
}