package com.kzksmarthome.common.lib.easylink.utils.news;

import com.kzksmarthome.common.lib.easylink.modle.Modle_DistanceData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * 指令工具类
 * Created by Administrator on 2016/11/11 0011.
 */
public class CommandUtil {
    /*
     *  公共参数
     */
    /*命令标识*/
    private static final String PUBLIC_FOR_WRITE = "02"; //命令标识写 OD
    private static final String PUBLIC_FOR_READ = "01";  //命令标识读 OD
    private static final String PUBLIC_FOR_SCENE = "50";  //命令标识 场景联动设置

    /*指令作用对象*/
    private static final String PARAM_FOR_ALL_NODE = "ff";  //广播所有设备指令
    private static final String PARAM_FOR_NODE = "01";  //针对单个设备指令
    private static final String PARAM_FOR_GATEWAY = "00";  //针对网关指令
    /*可变索引*/
    private static final String PARAM_FOR_CHANGE_INDEX = "ff"; //可变索引
    private static final String PARAM_FOR_READALL_INDEX = "00"; //不可变索引 - 读取全部参数(没有子索引)

    /**
     * 4010参数
     */
    private static final String OD_4010 = "0faa";  //OD类别（4010）
    private static final String OD_All_Area = "00000043"; //所有类型的区域的有效位
    private static final String PARAM_OD_4010_STRAIGHT = "01"; //4010设备  参数-立即开启
    private static final String PARAM_OD_4010_SLOWLY = "02"; //4010设备  参数-渐渐开启
    private static final String PARAM_OD_4010_DELAY = "03"; //4010设备  参数-延时开启

    /**
     * 4030参数
     */
    private static final String OD_4030 = "0fbe";  //OD类别（4030）

    /**
     * 4040参数
     */
    private static final String OD_4040 = "0fc8";  //OD类别（4040）

    /**
     * 4070参数
     */
    private static final String OD_4070 = "0fe6";  //OD类别（4070）

    /**
     * 1001参数
     */
    private static final String OD_1001 = "03e9";  //OD类别（1001）

    /**
     * 1007参数
     */
    private static final String OD_1007 = "03ef";  //OD类别（1007）

    /**
     * 5020参数
     */
    private static final String OD_5020 = "139c";  //OD类别（5020）

    /**
     * 6001参数
     */
    private static final String OD_6001 = "1771";  //OD类别（6001）


    //**-----------------------------------------多路灯相关-------------------------------------------**//


    private static final String THREE_LIGHT_A_MOMENT = "00018000";  //A开关瞬间有效位
    private static final String THREE_LIGHT_A_DELAY = "00038000";  //A开关延时有效位
    private static final String THREE_LIGHT_B_MOMENT = "000c0000";  //B开关瞬间有效位
    private static final String THREE_LIGHT_B_DELAY = "001c0000";  //B开关延时有效位
    private static final String THREE_LIGHT_C_MOMENT = "00600000";  //C开关瞬间有效位
    private static final String THREE_LIGHT_C_DELAY = "00e00000";  //C开关延时有效位

    /**
     * 获取多路灯指令
     * 1.指令类型(全广播、区域广播、单发):
     * 当node_mac为""，则该指令为广播指令(若area也为""，则为全广播，反之为区域广播),反之为单个设备控制指令
     * 2.开关类型（瞬间、延时）
     * 当开关延时参数为""时就为瞬间，反之为延时
     * <p/>
     * 注意：延时为null，则没有这个开关参数   为""，则这个开关为瞬间   其他，则这个开关的延时时间
     *
     * @param gateway_mac 网关mac
     * @param node_mac    设备mac
     * @param area        区域
     * @param A           A开关状态
     * @param A_delay     A开关延时
     * @param B           B开关状态
     * @param B_delay     B开关延时
     * @param C           C开关状态
     * @param C_delay     C开关延时
     * @return
     */
    public static String getCmd_ThreeLight(String gateway_mac, String node_mac, String type_area, String area, String A, String A_delay, String B, String B_delay, String C, String C_delay) {
        String index = getCmd_ThreeLight_Index(area, A_delay, B_delay, C_delay);
        String cmd = index +type_area+ area;

        if (A_delay == null && B_delay == null && C_delay == null)
            return null;
        if (A_delay != null) {
            cmd += ("".equals(A_delay) ? PARAM_OD_4010_STRAIGHT : PARAM_OD_4010_DELAY) + A + setBytesLen(A_delay, 2);
        }
        if (B_delay != null) {
            cmd += ("".equals(B_delay) ? PARAM_OD_4010_STRAIGHT : PARAM_OD_4010_DELAY) + B + setBytesLen(B_delay, 2);
        }
        if (C_delay != null) {
            cmd += ("".equals(C_delay) ? PARAM_OD_4010_STRAIGHT : PARAM_OD_4010_DELAY) + C + setBytesLen(C_delay, 2);
        }

        cmd = PUBLIC_FOR_WRITE + ("".equals(area) ? PARAM_FOR_NODE : PARAM_FOR_ALL_NODE) + gateway_mac + ("".equals(area) ? node_mac : "") + OD_4010 + PARAM_FOR_CHANGE_INDEX + DataTypeUtil.decimalToHex(cmd.length() / 2) + cmd;
        return addShellForCmd(cmd);
    }

    //获取多路灯子索引
    private static String getCmd_ThreeLight_Index(String area, String A_delay, String B_delay, String C_delay) {
        String index = "00000000";
        if (!"".equals(area))
            index = getSumOfHex(index, OD_All_Area);

        if (A_delay == null && B_delay == null && C_delay == null) {
            return null;
        }
        if (A_delay != null) {
            if ("".equals(A_delay)) {
                index = getSumOfHex(index, THREE_LIGHT_A_MOMENT);
            } else {
                index = getSumOfHex(index, THREE_LIGHT_A_DELAY);
            }
        }
        if (B_delay != null) {
            if ("".equals(B_delay)) {
                index = getSumOfHex(index, THREE_LIGHT_B_MOMENT);
            } else {
                index = getSumOfHex(index, THREE_LIGHT_B_DELAY);
            }
        }
        if (C_delay != null) {
            if ("".equals(C_delay)) {
                index = getSumOfHex(index, THREE_LIGHT_C_MOMENT);
            } else {
                index = getSumOfHex(index, THREE_LIGHT_C_DELAY);
            }
        }
        return index;
    }


    //**------------------------------------------多彩灯相关-----------------------------------------**//


    private static final String COLOR_LIGHT_NEEDTIME = "04000000";  //时间有效位
    private static final String COLOR_LIGHT_MONOTONE = "1b000000";  //调光有效位
    private static final String COLOR_LIGHT_COLORFUL = "7b000000";  //调色有效位

    private static final String PARAM_COLOR_LIGHT_SPECIAL = "01";  //类型-特殊
    private static final String PARAM_COLOR_LIGHT_MONOTONE_WARM = "02"; //类型-暖光
    private static final String PARAM_COLOR_LIGHT_COLORFUL = "03"; //类型-调色
    private static final String PARAM_COLOR_LIGHT_MONOTONE_COLD = "04"; //类型-冷光

    private static final String PARAM_COLOR_LIGHT_MODE_COLORFUL_JUMP = "0a"; //模式-七彩跳变
    private static final String PARAM_COLOR_LIGHT_MODE_COLORFUL_SLOWLY = "09"; //模式-七彩渐变
    private static final String PARAM_COLOR_LIGHT_MODE_BREATH = "0b"; //模式-呼吸灯
    private static final String PARAM_COLOR_LIGHT_MODE_MONOTONE = "01"; //模式-调光模式
    private static final String PARAM_COLOR_LIGHT_MODE_COLORFUL = "05"; //模式-调色模式

    /**
     * 获取多彩灯控制指令
     * 控制的模式有直接、渐渐、延时、七彩跳变、七彩渐变（不带时间参数）、呼吸灯（不带时间参数）
     * 控制的类型有调暖光、调冷光、调色、特殊（针对七彩跳变、七彩渐变、呼吸灯）
     *
     * @param gateway_mac
     * @param node_mac
     * @param area
     * @param startType
     * @param colorType
     * @param time
     * @param state
     * @param r_or_w
     * @param g
     * @param b
     * @return
     */
    public static String getCmd_ColorLight(String gateway_mac, String node_mac, String type_area, String area, String startType, String colorType, String time, boolean state, String r_or_w, String g, String b) {
        boolean needTime = !(PARAM_COLOR_LIGHT_MODE_COLORFUL_SLOWLY.equals(startType) || PARAM_COLOR_LIGHT_MODE_BREATH.equals(startType));
        String index = getCmd_ColorLight_Index(area, colorType, needTime);
        String cmd = index +type_area+ area;
        if (!needTime) {
            time = "";
        }
        if (PARAM_OD_4010_STRAIGHT.equals(startType)) {
            time = "0000";
        }
        if (PARAM_COLOR_LIGHT_MODE_COLORFUL_JUMP.equals(startType) || PARAM_COLOR_LIGHT_MODE_COLORFUL_SLOWLY.equals(startType)) {
            colorType = PARAM_COLOR_LIGHT_SPECIAL;
        }
        cmd += startType + colorType + setBytesLen(time, 2) + (state ? "01" : "02");
        switch (colorType) {
            case PARAM_COLOR_LIGHT_MONOTONE_WARM:
                cmd += r_or_w;
                break;
            case PARAM_COLOR_LIGHT_MONOTONE_COLD:
                cmd += r_or_w;
                break;
            case PARAM_COLOR_LIGHT_COLORFUL:
                cmd += r_or_w + g + b;
                break;
            case PARAM_COLOR_LIGHT_SPECIAL:
                cmd += "000000";
                break;
        }
        cmd = PUBLIC_FOR_WRITE + ("".equals(area) ? PARAM_FOR_NODE : PARAM_FOR_ALL_NODE) + gateway_mac + ("".equals(area) ? node_mac : "") + OD_4010 + PARAM_FOR_CHANGE_INDEX + DataTypeUtil.decimalToHex(cmd.length() / 2) + cmd;
        return addShellForCmd(cmd);
    }

    //获取多彩灯子索引
    private static String getCmd_ColorLight_Index(String area, String colorType, boolean needTime) {
        String index = "00000000";
        if (!"".equals(area))
            index = getSumOfHex(index, OD_All_Area);
        if (needTime)
            index = getSumOfHex(index, COLOR_LIGHT_NEEDTIME);
        if (PARAM_COLOR_LIGHT_MONOTONE_COLD.equals(colorType) || PARAM_COLOR_LIGHT_MONOTONE_WARM.equals(colorType)) {
            index = getSumOfHex(index, COLOR_LIGHT_MONOTONE);
        } else {
            index = getSumOfHex(index, COLOR_LIGHT_COLORFUL);
        }
        return index;
    }


    //**-----------------------------------------多彩灯机械开关设置----------------------------------------**//


    private static final String COLOR_LIGHT_SET_A_MONOTONE = "00000003";  //机械开关A设置调光有效位
    private static final String COLOR_LIGHT_SET_A_COLORFUL = "0000000f";  //机械开关A设置调色有效位
    private static final String COLOR_LIGHT_SET_A_NULL = "00000001";  //机械开关A设置暂无有效位
    private static final String COLOR_LIGHT_SET_B_MONOTONE = "00000030";  //机械开关B设置调光有效位
    private static final String COLOR_LIGHT_SET_B_COLORFUL = "000000f0";  //机械开关B设置调色有效位
    private static final String COLOR_LIGHT_SET_B_null = "00000010";//机械开关B设置暂无有效位
    private static final String COLOR_LIGHT_SET_C_null = "00000100"; //机械开关C设置暂无有效位
    private static final String COLOR_LIGHT_SET_D_null = "00001000"; //机械开关D设置暂无有效位

    /**
     * 获取机械开关指令  注意：设置机械开关是OD(6001)
     * 设置的模式有七彩跳变、七彩渐变、呼吸灯、调光、调色
     *
     * @param gateway_mac
     * @param node_mac
     * @param lightModeA
     * @param r_w_A
     * @param gA
     * @param bA
     * @param lightModeB
     * @param r_w_B
     * @param gB
     * @param bB
     * @return
     */
    public static String getCmd_ColorLight_Set(String gateway_mac, String node_mac, String lightModeA, String r_w_A, String gA, String bA, String lightModeB, String r_w_B, String gB, String bB) {
        String index = getCmd_ColorLight_Set_Index(lightModeA, lightModeB);
        if (lightModeA == null && lightModeB == null) return null;
        String cmd = index;
        if (lightModeA != null) {
            cmd += PARAM_COLOR_LIGHT_MODE_MONOTONE.equals(lightModeA) ? lightModeA + r_w_A : lightModeA + r_w_A + gA + bA;
        } else {
            cmd += "00";
        }
        if (lightModeB != null) {
            cmd += PARAM_COLOR_LIGHT_MODE_MONOTONE.equals(lightModeB) ? lightModeB + r_w_B : lightModeB + r_w_B + gB + bB;
        } else {
            cmd += "00";
        }
        cmd += "0000";
        cmd = PUBLIC_FOR_WRITE + ("".equals(node_mac) ? PARAM_FOR_ALL_NODE : PARAM_FOR_NODE) + gateway_mac + ("".equals(node_mac) ? node_mac : "") + OD_6001 + PARAM_FOR_CHANGE_INDEX + DataTypeUtil.decimalToHex(cmd.length() / 2) + cmd;
        return addShellForCmd(cmd);
    }

    //获取机械开关子索引
    private static String getCmd_ColorLight_Set_Index(String lightModeA, String lightModeB) {
        String index = "00000000";
        if (lightModeA != null) {
            if (PARAM_COLOR_LIGHT_MODE_MONOTONE.equals(lightModeA)) {
                index = getSumOfHex(index, COLOR_LIGHT_SET_A_MONOTONE);
            } else {
                index = getSumOfHex(index, COLOR_LIGHT_SET_A_COLORFUL);
            }
        } else {
            index = getSumOfHex(index, COLOR_LIGHT_SET_A_NULL);
        }
        if (lightModeB != null) {
            if (PARAM_COLOR_LIGHT_MODE_MONOTONE.equals(lightModeB)) {
                index = getSumOfHex(index, COLOR_LIGHT_SET_B_MONOTONE);
            } else {
                index = getSumOfHex(index, COLOR_LIGHT_SET_B_COLORFUL);
            }
        } else {
            index = getSumOfHex(index, COLOR_LIGHT_SET_B_null);
        }
        index = getSumOfHex(index, COLOR_LIGHT_SET_C_null);
        index = getSumOfHex(index, COLOR_LIGHT_SET_D_null);
        return index;
    }

    //**------------------------------------------场景控制器相关--------------------------------------------**//
    private static final String STATECONTROL_PARAM_TYPE = "8a";  //设备类别代表场景控制器
    private static final String STATECONTROL_A = "00000800";  //A路输入信号有效位
    private static final String STATECONTROL_B = "00001000";  //B路输入信号有效位
    private static final String STATECONTROL_C = "00002000";  //C路输入信号有效位

    /**
     * 获取场景控制器场景设置指令
     *
     * @param mac1 网关mac
     * @param mac2 设备mac
     * @param A    第一个按钮对应的场景编号
     * @param B    第二个按钮对应的场景编号
     * @param C    第三个按钮对应的场景编号
     * @return
     */
    public static String getStateControlCommand(String mac1, String mac2, String A, String B, String C) {
        String temp = getCmd_StateControl_Index(A, B, C) + STATECONTROL_PARAM_TYPE + A + B + C;
        String s = PUBLIC_FOR_WRITE + PARAM_FOR_NODE + mac1 + mac2 + OD_4010 + PARAM_FOR_CHANGE_INDEX + DataTypeUtil.decimalToHex(temp.length() / 2) + temp;
        return addShellForCmd(s);
    }

    //获取场景控制器子索引
    private static String getCmd_StateControl_Index(String A, String B, String C) {
        String index = "00000001";
        if (!"".equals(A)) {
            index = getSumOfHex(index, STATECONTROL_A);
        }
        if (!"".equals(B)) {
            index = getSumOfHex(index, STATECONTROL_B);
        }
        if (!"".equals(C)) {
            index = getSumOfHex(index, STATECONTROL_C);
        }
        return index;
    }

    //**------------------------------------------计量设备相关--------------------------------------------**//
    private static final String MeteringEquipment_EffectivePosition = "00020000";  //计量设备开关有效位
    private static final String MeteringEquipment_Electricity = "10";  //计量设备有功电量单址读取位

    //计量设备开关控制
    public static String getCmd_MeteringEquipment(String gateway_mac, String mac, String type_area, String area, String state) {
        String index = getCmd_MeteringEquipment_Index(area,MeteringEquipment_EffectivePosition);
        String cmd = index+type_area + area + state;
        cmd = PUBLIC_FOR_WRITE + ("".equals(area) ? PARAM_FOR_NODE : PARAM_FOR_ALL_NODE) + gateway_mac + ("".equals(area) ? mac : "") + OD_4040 + PARAM_FOR_CHANGE_INDEX + DataTypeUtil.decimalToHex(cmd.length() / 2)+cmd;
        return addShellForCmd(cmd);
    }
    //计量设备电量清零
    public static String getCmd_MeteringEquipmentSetZero(String gateway_mac, String mac) {
        String cmd =  "00000000";
        cmd = PUBLIC_FOR_WRITE + PARAM_FOR_NODE  + gateway_mac + mac+ OD_4040 +MeteringEquipment_Electricity+DataTypeUtil.decimalToHex(cmd.length()/2)+cmd;
        return addShellForCmd(cmd);
    }
    public static String getCmd_MeteringEquipment_Index(String area, String indexOfStart) {
        String index = indexOfStart;
        if (!"".equals(area)) {
            index = getSumOfHex(index, OD_All_Area);
        }
        return index;
    }
    //**------------------------------------------透传相关--------------------------------------------**//

    //获取透传指令
    public static String getCmd_TransparentTransmission(String mac1, String mac2, String cmd) {
        String s = "07" + "01" + mac1 + mac2 + DataTypeUtil.decimalToHex(cmd.length() / 2) + cmd;
        return addShellForCmd(s);
    }

    //**------------------------------------------OD1001相关指令--------------------------------------------**//
    private static final String PARAM_WirelessChannel = "01";  //无线通道
    private static final String PARAM_PAN_ID = "06";  //PAN ID
    private static final String PARAM_BAUDRATE="0d"; //设备波特率
    private static final String PARAM_CHECKTYPE="0e"; //设备检验方式
    private static final String PARAM_EquipmentCategory="02"; //设备类别
    private static final String PARAM_ProductType="03"; //产品类型
    //单址读取无线通道
    public static String getCmd_ReadWirelessChannel(String gateway_mac, String mac){
        String s=PUBLIC_FOR_READ+("".equals(mac)?PARAM_FOR_GATEWAY:PARAM_FOR_NODE)+gateway_mac+mac+OD_1001+PARAM_WirelessChannel;
        return addShellForCmd(s);
    }
    //单址写入无线通道
    public static String getCmd_WriteWirelessChannel(String gateway_mac, String mac, String data){
        String s=PUBLIC_FOR_WRITE+("".equals(mac)?PARAM_FOR_GATEWAY:PARAM_FOR_NODE)+gateway_mac+mac+OD_1001+PARAM_WirelessChannel+DataTypeUtil.decimalToHex(data.length()/2)+data;
        return addShellForCmd(s);
    }
    //单址读取无线通道反馈解析，获取无线通道字段   返回null代表解析失败
    public static String getWirelessChannelFromString(String data, String mac){
        if(data.length()==36){
            if(data.substring(6,22).equals(mac)&&PUBLIC_FOR_READ.equals(data.substring(4,6))&&PARAM_WirelessChannel.equals(data.substring(26,28))){
                return data.substring(30,32);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    //单址读取PAN ID
    public static String getCmd_ReadPanId(String gateway_mac, String mac){
        String s=PUBLIC_FOR_READ+("".equals(mac)?PARAM_FOR_GATEWAY:PARAM_FOR_NODE)+gateway_mac+mac+OD_1001+PARAM_PAN_ID;
        return addShellForCmd(s);
    }
    //单址写入PAN ID
    public static String getCmd_WritePanId(String gateway_mac, String mac, String data){
        String s=PUBLIC_FOR_WRITE+("".equals(mac)?PARAM_FOR_GATEWAY:PARAM_FOR_NODE)+gateway_mac+mac+OD_1001+PARAM_PAN_ID+DataTypeUtil.decimalToHex(data.length()/2)+data;
        return addShellForCmd(s);
    }
    //单址读取无线通道反馈解析，获取无线通道字段   返回null代表解析失败
    public static String getPanIdFromString(String data, String mac){
        if(data.length()==38){
            if(data.substring(6,22).equals(mac)&&PUBLIC_FOR_READ.equals(data.substring(4,6))&&PARAM_PAN_ID.equals(data.substring(26,28))){
                return data.substring(30,34);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    //单址读取设备波特率
    public static String getCmd_ReadBaudRate(String gateway_mac, String mac){
        String s=PUBLIC_FOR_READ+PARAM_FOR_NODE+gateway_mac+mac+OD_1007+PARAM_BAUDRATE;
        return addShellForCmd(s);
    }
    //单址写入设备波特率
    public static String getCmd_WriteBaudRate(String gateway_mac, String mac, String data){
        String s=PUBLIC_FOR_WRITE+PARAM_FOR_NODE+gateway_mac+mac+OD_1007+PARAM_BAUDRATE+DataTypeUtil.decimalToHex(data.length()/2)+data;
        return addShellForCmd(s);
    }
    //单址读取设备波特率反馈解析，获取设备波特率字段   返回null代表解析失败
    public static String getBaudRateFromString(String data, String mac){
        if(data.length()==36){
            if(data.substring(6,22).equals(mac)&&PUBLIC_FOR_READ.equals(data.substring(4,6))&&PARAM_BAUDRATE.equals(data.substring(26,28))){
                return data.substring(30,32);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    //单址读取设备检验方式
    public static String getCmd_ReadCheckType(String gateway_mac, String mac){
        String s=PUBLIC_FOR_READ+PARAM_FOR_NODE+gateway_mac+mac+OD_1007+PARAM_CHECKTYPE;
        return addShellForCmd(s);
    }
    //单址写入设备检验方式
    public static String getCmd_WriteCheckType(String gateway_mac, String mac, String data){
        String s=PUBLIC_FOR_WRITE+PARAM_FOR_NODE+gateway_mac+mac+OD_1007+PARAM_CHECKTYPE+DataTypeUtil.decimalToHex(data.length()/2)+data;
        return addShellForCmd(s);
    }
    //单址读取设备检验方式反馈解析，获取设备检验方式字段   返回null代表解析失败
    public static String getCheckTypeFromString(String data, String mac){
        if(data.length()==36){
            if(data.substring(6,22).equals(mac)&&PUBLIC_FOR_READ.equals(data.substring(4,6))&&PARAM_CHECKTYPE.equals(data.substring(26,28))){
                return data.substring(30,32);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    //单址读取设备类别
    public static String getCmd_ReadEquipmentCategory(String gateway_mac, String mac){
        String s=PUBLIC_FOR_READ+PARAM_FOR_NODE+gateway_mac+mac+OD_1007+PARAM_EquipmentCategory;
        return addShellForCmd(s);
    }
    //单址写入设备类别
    public static String getCmd_WriteEquipmentCategory(String gateway_mac, String mac, String data){
        String s=PUBLIC_FOR_WRITE+PARAM_FOR_NODE+gateway_mac+mac+OD_1007+PARAM_EquipmentCategory+DataTypeUtil.decimalToHex(data.length()/2)+data;
        return addShellForCmd(s);
    }
    //单址读取设备类别反馈解析，获取设备类别字段   返回null代表解析失败
    public static String getEquipmentCategoryFromString(String data, String mac){
        if(data.length()==36){
            if(data.substring(6,22).equals(mac)&&PUBLIC_FOR_READ.equals(data.substring(4,6))&&PARAM_EquipmentCategory.equals(data.substring(26,28))){
                return data.substring(30,32);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    //单址读取产品类型
    public static String getCmd_ReadProductType(String gateway_mac, String mac){
        String s=PUBLIC_FOR_READ+PARAM_FOR_NODE+gateway_mac+mac+OD_1007+PARAM_ProductType;
        return addShellForCmd(s);
    }
    //单址写入产品类型
    public static String getCmd_WriteProductType(String gateway_mac, String mac, String data){
        String s=PUBLIC_FOR_WRITE+PARAM_FOR_NODE+gateway_mac+mac+OD_1007+PARAM_ProductType+DataTypeUtil.decimalToHex(data.length()/2)+data;
        return addShellForCmd(s);
    }
    //单址读取产品类型反馈解析，获取产品类型字段   返回null代表解析失败
    public static String getProductTypeFromString(String data, String mac){
        if(data.length()==36){
            if(data.substring(6,22).equals(mac)&&PUBLIC_FOR_READ.equals(data.substring(4,6))&&PARAM_ProductType.equals(data.substring(26,28))){
                return data.substring(30,32);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    //**------------------------------------------公共属性相关--------------------------------------------**//
    private static final String No_CHANGE_IINDEX_FOR_AREA = "08"; //不可变索引  1007区域有效位
    private static final String PARAM_CHILD_CMDFLAG = "01"; //子命令标识，表示参数配置命令
    private static final String DEFAULT_SCENE_NAME = "0000000000000000000000000000000000000000";  //存至网关的默认场景名称
    private static final String TYPE_SCENE_CJ = "01";  //要设置的场景类型为场景
    private static final String TYPE_SCENE_LD = "02";  //要设置的场景类型为联动
    private static final String TYPE_SCENE_DEL = "07";  //场景特性为删除
    private static final String TYPE_SCENE_START = "09";  //场景特性为触发
    private static final String FEATURES_SCENE_SET = "00";  //场景特性为设置

    //设置设备区域
    public static String getSetAreaCommand(String gateway_mac, String mac, String area) {
        String cmd = PUBLIC_FOR_WRITE + PARAM_FOR_NODE + gateway_mac + mac + OD_1007 + No_CHANGE_IINDEX_FOR_AREA + "01" + area;  //01是长度  这里就只有一个区域，所以就不按照字符串长度除以2的解析了
        return addShellForCmd(cmd);
    }

    /**
     * 场景设置
     *
     * @param cmds      要配置的指令
     * @param mac       网关地址
     * @param num       编号
     * @param stateNum  代号
     * @param time      延时时间  2个字节
     * @param startTime 定时时间  2个字节
     * @return
     */
    public static String[] getSetSceneCommand_ForCJ(List<String> cmds, String mac, String num, String stateNum, String time, String startTime) {
        String[] s = new String[cmds.size() + 1];
        String temp = PARAM_CHILD_CMDFLAG + DEFAULT_SCENE_NAME + TYPE_SCENE_CJ + num + FEATURES_SCENE_SET + DataTypeUtil.decimalToHex(cmds.size()) + stateNum + "ffff" + "ffffff" + "ffffffffffffffff" + "ffffff" + "ffffffffffffffff" + startTime + time;
        s[0] = PUBLIC_FOR_SCENE + PARAM_FOR_GATEWAY + mac + DataTypeUtil.decimalToHex(temp.length() / 2) + temp;
        s[0] = addShellForCmd(s[0]);
        for (int i = 1; i < s.length; i++) {
            s[i] = PUBLIC_FOR_WRITE + num + DataTypeUtil.decimalToHex(cmds.size()) + DataTypeUtil.decimalToHex(i) + cmds.get(i - 1);
            s[i] = PUBLIC_FOR_SCENE + PARAM_FOR_GATEWAY + mac + DataTypeUtil.decimalToHex(s[i].length() / 2) + s[i];
            s[i] = addShellForCmd(s[i]);
        }
        return s;
    }

    /**
     * 设置联动指令
     *
     * @param cmds
     * @param mac
     * @param num
     * @param force     01为强制联动；02为非强制联动
     * @param bcf
     * @param mac2
     * @param channel   数据通道
     * @param dataType  数据类型
     * @param compType  比较类型
     * @param dataDown
     * @param dataUp
     * @param time
     * @return
     */
    private static String PARAM_SCENE_LD_OPEN = "01";  //设置的联动开启
    private static String PARAM_SCENE_LD_CLOSE = "02";  //设置的联动关闭
    private static String PARAM_FORCE_TRUE = "01"; //设置强制联动
    private static String PARAM_FORCE_FLASE = "02"; //设置普通联动

    public String[] getSetSceneCommand_ForLD(List<String> cmds, String mac, String num, String force, String bcf, String mac2, String channel, String dataType, String compType, String dataDown, String dataUp, String time) {
        String[] s = new String[cmds.size() + 1];
        String temp = PARAM_CHILD_CMDFLAG + DEFAULT_SCENE_NAME + TYPE_SCENE_LD + num + FEATURES_SCENE_SET + DataTypeUtil.decimalToHex(cmds.size()) + "ffffff" + force + PARAM_SCENE_LD_OPEN + bcf + mac2 + channel + dataType + compType + "0018" + dataDown + dataUp + time;
        s[0] = "5000" + mac + DataTypeUtil.decimalToHex(temp.length() / 2) + temp;
        s[0] = "2a" + DataTypeUtil.decimalToHex(s[0].length() / 2) + s[0] + DataTypeUtil.getAddCheck(s[0]) + "23";
        for (int i = 1; i < s.length; i++) {
            s[i] = "02" + num + DataTypeUtil.decimalToHex(cmds.size()) + DataTypeUtil.decimalToHex(i) + cmds.get(i - 1);
            s[i] = "5000" + mac + DataTypeUtil.decimalToHex(s[i].length() / 2) + s[i];
            s[i] = "2a" + DataTypeUtil.decimalToHex(s[i].length() / 2) + s[i] + DataTypeUtil.getAddCheck(s[i]) + "23";
        }
        return s;
    }

    /**
     * 删除场景指令
     *
     * @param mac 网关
     * @param num 编号
     * @return
     */
    public static String getDelSceneCommand(String mac, String num) {
        String temp = TYPE_SCENE_DEL + num;
        String s = PUBLIC_FOR_SCENE + PARAM_FOR_GATEWAY + mac + DataTypeUtil.decimalToHex(temp.length() / 2) + temp;
        return addShellForCmd(s);
    }

    /**
     * 触发场景或联动指令
     *
     * @param mac 网关
     * @param num 编号
     * @return
     */
    public static String getStartSceneCommand(String mac, String num) {
        String temp = TYPE_SCENE_START + num;
        String s = PUBLIC_FOR_SCENE + PARAM_FOR_GATEWAY + mac + DataTypeUtil.decimalToHex(temp.length() / 2) + temp;
        return addShellForCmd(s);
    }

    //**-----------------------------------------配置云端ip及端口，还有本地端口------------------------------------------------**//
    private static String DISTANCEDATA_IP="00000040";//远程地址有效位
    private static String DISTANCEDATA_IP__="07";//远程地址单址读取位
    private static String DISTANCEDATA_PORT="00000008";//远程端口有效位
    private static String DISTANCEDATA_PORT__="04";//远程端口单址读取位
    private static String DISTANCEDATA_LOCAL_PORT="00000004";//本地端口有效位
    private static String DISTANCEDATA_LOCAL_PORT__="03";//本地端口单址读取位
    private static String OD_5060="13c4";  //OD5060

    //云端读取远程地址、远程端口、本地端口
    public static String getCmd_ReadDistanceData(String gateway_mac, boolean dip, boolean dp, boolean lp){
        String index=getDistanceData_Index(dip,dp,lp);
        String s=PUBLIC_FOR_READ+PARAM_FOR_GATEWAY+gateway_mac+OD_5060+PARAM_FOR_CHANGE_INDEX+index;
        return addShellForCmd(s);
    }
    //云端读取远程地址、远程端口、本地端口反馈解析
    public static Modle_DistanceData getDistanceDataFromString(String data, String mac){
        Modle_DistanceData s=null;
        if(PUBLIC_FOR_READ.equals(data.substring(4,6))&&mac.equals(data.substring(6,22))&&OD_5060.equals(data.substring(22,26))){
            String index=data.substring(30,38);
            if(DISTANCEDATA_IP.equals(index)){
                //单单远程地址
                s=new Modle_DistanceData(data.substring(38,data.length()-4),"","");
            }else if(DISTANCEDATA_PORT.equals(index)){
                //单单远程端口
                s=new Modle_DistanceData("",data.substring(38,42),"");
            }else if(DISTANCEDATA_LOCAL_PORT.equals(index)){
                //单单本地端口
                s=new Modle_DistanceData("","",data.substring(38,42));
            }else if(getDistanceData_Index(true,true,true).equals(index)){
                //三者都有
                s=new Modle_DistanceData(data.substring(46,data.length()-4),data.substring(42,46),data.substring(38,42));
            }
        }
        return s;
    }
    //云端配置远程地址、远程端口、本地端口
    public static String getCmd_WriteDistanceData(String gateway_mac, String dip, String dp, String lp){
        String index=getDistanceData_Index(!"".equals(dip),!"".equals(dp),!"".equals(lp));
        String temp=index+lp+dp+dip;
        String s=PUBLIC_FOR_WRITE+PARAM_FOR_GATEWAY+gateway_mac+OD_5060+PARAM_FOR_CHANGE_INDEX+DataTypeUtil.decimalToHex(temp.length()/2)+temp;
        return addShellForCmd(s);
    }
    //获取配置云端的索引
    public static String getDistanceData_Index(boolean dip, boolean dp, boolean lp){
        String index = "00000000";
        if(dip){
            index = getSumOfHex(index, DISTANCEDATA_IP);
        }
        if(dp){
            index = getSumOfHex(index, DISTANCEDATA_PORT);
        }
        if(lp){
            index = getSumOfHex(index, DISTANCEDATA_LOCAL_PORT);
        }
        return index;
    }

    //**-----------------------------------------网关相关-----------------------------------------------**//
    private static String HEARTBEAT_NUM="04";//心跳计数单址有效位
    /**
     * 复位指令
     * @param gateway_mac  网关mac
     * @param mac   节点mac   当节点mac为""时，代表网关复位指令
     */
    public static String getCmd_Reset(String gateway_mac, String mac){
        String s="22";
        if("".equals(mac)){
            //网关复位
            s+=PARAM_FOR_GATEWAY+gateway_mac+gateway_mac;
        }else{
            //节点复位
            s+=PARAM_FOR_NODE+gateway_mac+mac;
        }
        return addShellForCmd(s);
    }
    /**
     * 修改网关心跳值
     * @param gateway_mac  网关
     * @param num   16进制具体值
     * @return
     */
    public static String getCmd_HeartBeatSetZero(String gateway_mac, String num){
        String s=PUBLIC_FOR_WRITE+PARAM_FOR_GATEWAY+gateway_mac+OD_5020+HEARTBEAT_NUM+DataTypeUtil.decimalToHex(num.length()/2)+num;
        return addShellForCmd(s);
    }
    //是否是指定网关的心跳上报帧
    public static boolean getBooleanFromHeartBeatString(String data, String mac){
        if(data.length()==106&&PUBLIC_FOR_READ.equals(data.substring(4,6))&&data.substring(6,22).equals(mac)&&data.substring(22,26).equals(OD_5020)&&CommandUtil.getCheckBoolean(data)){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 开启入网时间窗
     * @param gateway_mac  网关
     * @param time  配置参数范围：0x00-0xFF，0x00 禁止入网，0xFF 永久允许    单位秒
     * @return
     */
    public static String getCmd_openAddModles(String gateway_mac, String time){
        String s="010101"+time+"0000";
        s="60"+PARAM_FOR_GATEWAY+gateway_mac+DataTypeUtil.decimalToHex(s.length()/2)+s;
        return addShellForCmd(s);
    }

    //**-----------------------------------------其他------------------------------------------------**//

    /**
     * 设备命名
     *
     * @param flag 设备OD
     * @param udt  设备类别
     * @param ust  设备类型
     * @return
     */
    public static String getModleName(String flag, String udt, String ust) {
        String name = null;
        switch (flag) {
            case OD_4010:
                name = "多用开关";
                switch (udt) {
                    case "01":
                        switch (ust) {
                            case "02":
                                name = "电动窗帘";
                                break;
                        }
                        break;
                    case "02":
                        switch (ust) {
                            case "02":
                                name = "电动幕布";
                                break;
                            case "10":
                                name = "投影架";
                                break;
                            case "11":
                                name = "推拉开窗器";
                                break;
                            case "12":
                                name = "平推开窗器";
                                break;
                            case "13":
                                name = "机械手控制器";
                                break;
                        }
                        break;
                    case "05":
                        switch (ust) {
                            case "02":
                                name = "一路灯";
                                break;
                            case "03":
                                name = "电动玻璃";
                                break;
                            case "10":
                                name = "86插座";
                                break;
                            case "11":
                                name = "移动插座";
                                break;
                        }
                        break;
                    case "06":
                        switch (ust) {
                            case "02":
                                name = "二路灯";
                                break;
                            case "03":
                                name = "中继器";
                                break;
                        }
                        break;
                    case "07":
                        switch (ust) {
                            case "02":
                                name = "三路灯";
                                break;
                        }
                        break;
                    case "09":
                        switch (ust) {
                            case "02":
                                name = "声光报警器";
                                break;
                        }
                        break;
                    case "81":
                        switch (ust) {
                            case "02":
                                name = "人体热释";
                                break;
                            case "03":
                                name = "CO传感器";
                                break;
                            case "04":
                                name = "烟雾传感器";
                                break;
                        }
                        break;
                    case "8a":
                        switch (ust) {
                            case "02":
                                name = "场景控制器";
                                break;
                        }
                        break;
                    case "0b":
                        switch (ust) {
                            case "02":
                                name = "多彩球泡灯";
                                break;
                        }
                        break;
                    case "0e":
                        switch (ust) {
                            case "02":
                                name = "多彩冷暖灯";
                                break;
                        }
                        break;
                    case "c1":
                        switch (ust) {
                            case "02":
                                name = "六路面板";
                                break;
                        }
                        break;
                }
                break;
            case OD_4030:
                name = "休眠设备";
                switch (udt) {
                    case "01":
                        switch (ust) {
                            case "02":
                                name = "门磁";
                                break;
                        }
                        break;
                    case "02":
                        switch (ust) {
                            case "02":
                                name = "指纹锁";
                                break;
                        }
                        break;
                    case "07":
                        switch (ust) {
                            case "02":
                                name = "烟雾传感器";
                                break;
                        }
                        break;
                    case "81":
                        switch (ust) {
                            case "02":
                                name = "门磁";
                                break;
                            case "03":
                                name = "窗磁";
                                break;
                        }
                        break;
                    case "83":
                        switch (ust) {
                            case "02":
                                name = "水浸传感器";
                                break;
                        }
                        break;
                    case "86":
                        switch (ust) {
                            case "02":
                                name = "红外传感器";
                                break;
                        }
                        break;
                }
                break;
            case OD_4040:
                name = "计量设备";
                switch (udt) {
                    case "01":
                        switch (ust) {
                            case "02":
                                name = "单相电表";
                                break;
                        }
                        break;
                    case "02":
                        switch (ust) {
                            case "02":
                                name = "计量控制盒";
                                break;
                        }
                        break;
                    case "03":
                        switch (ust) {
                            case "02":
                                name = "三相电表";
                                break;
                        }
                        break;
                    case "04":
                        switch (ust) {
                            case "02":
                                name = "10A插座";
                                break;
                            case "03":
                                name = "16A插座";
                                break;
                        }
                        break;
                }
                break;
            case OD_4070:
                name = "透传设备";
                switch (udt) {
                    case "01":
                        switch (ust) {
                            case "02":
                                name = "纯透传模块";
                                break;
                        }
                        break;
                    case "02":
                        switch (ust) {
                            case "02":
                                name = "红外学习仪";
                                break;
                            case "03":
                                name = "音乐背景器";
                                break;
                            case "04":
                                name = "日立投影仪";
                                break;
                            case "05":
                                name = "风机盘管";
                                break;
                            case "10":
                                name = "智能窗帘";
                                break;
                            case "11":
                                name = "平移开窗器";
                                break;
                            case "12":
                                name = "电动床";
                                break;
                            case "13":
                                name = "新风系统";
                                break;
                            case "20":
                                name = "浴霸";
                                break;
                        }
                        break;
                }
                break;
        }
        return name;
    }

    //指令校验->该接受的指令是否符合加分校验和的标准
    public static boolean getCheckBoolean(String buffer) {
        return buffer.substring(buffer.length() - 4, buffer.length() - 2).equals(DataTypeUtil.getAddCheck(buffer.substring(4, buffer.length() - 4)));
    }

    /**
     * 2字节长度补全  一般用于2个字节的时间参数
     */
    public static String setBytesLen(String s, int bytesNum) {
        int length = bytesNum * 2;
        if ("".equals(s))
            return "";
        int len = s.length();
        if (len < length) {
            int temp = length - len;
            for (int i = 0; i < temp; i++)
                s = "0" + s;
        } else if (len > length) {
            s = s.substring(len - length, len);
        }
        return s;
    }
    public static String setBytesLenAfter(String s, int bytesNum) {
        int length = bytesNum * 2;
        if ("".equals(s))
            return "";
        int len = s.length();
        if (len < length) {
            int temp = length - len;
            for (int i = 0; i < temp; i++)
                s =s+"0";
        } else if (len > length) {
            s = s.substring(len - length, len);
        }
        return s;
    }

    /**
     * 给指定字符串加壳
     *
     * @param cmd
     * @return
     */
    private static final String CMD_HEAD = "2a";
    private static final String CMD_END = "23";

    public static String addShellForCmd(String cmd) {
        return CMD_HEAD + DataTypeUtil.decimalToHex(cmd.length() / 2) + cmd + DataTypeUtil.getAddCheck(cmd) + CMD_END;
    }

    /**
     * 获取当前时间(yyyy-MM-dd-HH-mm-ss-SS)
     *
     * @return
     */
    public static String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SS");
        String time = df.format(new Date());// new Date()为获取当前系统时间
        return time;
    }

    /**
     * 极智有效位相加方法
     * 4个字节的16进制字符串相加，获取4个字节的16进制结果
     *
     * @param a 4字节参数
     * @param b 4字节参数
     * @return
     */
    public static String getSumOfHex(String a, String b) {
        int len = a.length();
        if (len != b.length()) {
            return null;
        }

        String ba = "";
        for (int i = 0; i < len; i++) {
            ba += DataTypeUtil.hexToBinary(a.substring(i, i + 1));
        }

        String bb = "";
        for (int i = 0; i < len; i++) {
            bb += DataTypeUtil.hexToBinary(b.substring(i, i + 1));
        }

        String index = "";
        for (int i = 0; i < len * 4; i++) {
            if ("1".equals(ba.substring(i, i + 1)) || "1".equals(bb.substring(i, i + 1))) {
                index += "1";
            } else {
                index += "0";
            }
        }

        index = DataTypeUtil.binaryToHex(index);
        if (index.length() == len) {
            return index;
        } else {
            int temp = len - index.length();
            for (int i = 0; i < temp; i++) {
                index = "0" + index;
            }
            return index;
        }
    }

    /**
     * 子字符串在父字符串中出现的次数
     */
    public static int stringSub(String str, String substr) {
        int index = 0;
        int count = 0;
        int fromindex = 0;
        while ((index = str.indexOf(substr, fromindex)) != -1) {
            fromindex = index + substr.length();
            count++;
        }
        return count;
    }

    /**
     * 字符串处理->4位长度字符串，若长度小于4，则前补0；反之长度大于4，则截取后4位
     */
    public static String getTimeString(int time) {
        //时间2字节补齐
        String time_str = DataTypeUtil.decimalToHex(time);
        if (time_str.length() < 4) {
            int num = 4 - time_str.length();
            for (int i = 0; i < num; i++) {
                time_str = "0" + time_str;
            }
        } else if (time_str.length() > 4) {
            time_str = time_str.substring(0, 4);
        }
        return time_str;
    }

    /**
     * 字符串处理->结尾去00
     */
    public static String getDataEndWithoutZero(String s) {
        StringBuffer S = new StringBuffer(s);
        while (true) {
            if (S.length() <= 2 || !S.substring(S.length() - 2, S.length()).equals("00")) {
                return S.toString();
            }
            S.delete(S.length() - 2, S.length());
        }
    }

    /**
     * 字符串处理->获取偶数位字符串
     **/
    public static String getIpNum(String s) {
        StringBuffer S = new StringBuffer("");
        StringBuffer S_ = new StringBuffer(s);
        for (int i = 0; i < s.length(); i++) {
            if ((i + 1) % 2 == 0) {
                S.append(S_.substring(i, i + 1));
            }
        }
        return S.toString();
    }

    /**
     * 将一个数字转换成一个字节长度的16进制(用于场景控制器的场景编号字段)
     **/
    public String getStateNum(int i) {
        String s = "";
        if (i <= 16 && i > 0) {
            s = "0" + DataTypeUtil.decimalToHex(i);
        } else if (i > 16) {
            s = DataTypeUtil.decimalToHex(i);
            s = s.substring(s.length() - 2, s.length());
        }
        return s;
    }

}
