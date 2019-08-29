package com.kzksmarthome.common.lib.easylink.utils.news;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public class OD_4010_Util {
    /**
     * 多路控制相关参数
     */
    public static final String STATE_OPEN = "01"; //多路灯开状态
    public static final String STATE_STOP = "02"; //多路灯关状态
    public static final String STATE_PAUSE = "04"; //多路灯暂停状态

    /**
     * 单控多路
     * 注意：此处的延时时间均为16进制
     *
     * @param gateway_mac 网关mac
     * @param mac         设备mac
     * @param A           A开关状态（THREE_STATE_OPEN、THREE_STATE_STOP、THREE_STATE_PAUSE）   01:开   02:关
     * @param timeA       A开关延时时间（此处若为null，则A开关设定没有意义，即没有开关A；此处若为"",则A开关的开启模式为直接开启） 2个字节
     * @param B           B开关状态（THREE_STATE_OPEN、THREE_STATE_STOP、THREE_STATE_PAUSE）
     * @param timeB       B开关延时时间（此处若为null，则B开关设定没有意义，即没有开关B；此处若为"",则B开关的开启模式为直接开启） 2个字节
     * @param C           C开关状态（THREE_STATE_OPEN、THREE_STATE_STOP、THREE_STATE_PAUSE）
     * @param timeC       C开关延时时间（此处若为null，则C开关设定没有意义，即没有开关C；此处若为"",则C开关的开启模式为直接开启） 2个字节
     * @return
     */
    public static String getCmd_Three_Only(String gateway_mac, String mac, String A, String timeA, String B, String timeB, String C, String timeC) {
        return CommandUtil.getCmd_ThreeLight(gateway_mac, mac,"" ,"", A, timeA, B, timeB, C, timeC);
    }

    /**
     * 区域控多路
     * 注意：此处的延时时间均为16进制
     *
     * @param gateway_mac 网关mac
     * @param type_area   设备类别+产品类型
     * @param area        区域信息
     * @param A           A开关状态（THREE_STATE_OPEN、THREE_STATE_STOP、THREE_STATE_PAUSE）
     * @param timeA       A开关延时时间（此处若为null，则A开关设定没有意义，即没有开关A；此处若为"",则A开关的开启模式为直接开启） 2个字节
     * @param B           B开关状态（THREE_STATE_OPEN、THREE_STATE_STOP、THREE_STATE_PAUSE）
     * @param timeB       B开关延时时间（此处若为null，则B开关设定没有意义，即没有开关B；此处若为"",则B开关的开启模式为直接开启） 2个字节
     * @param C           C开关状态（THREE_STATE_OPEN、THREE_STATE_STOP、THREE_STATE_PAUSE）
     * @param timeC       C开关延时时间（此处若为null，则C开关设定没有意义，即没有开关C；此处若为"",则C开关的开启模式为直接开启） 2个字节
     * @return
     */
    public static String getCmd_Three_Area(String gateway_mac, String type_area, String area, String A, String timeA, String B, String timeB, String C, String timeC) {
        return CommandUtil.getCmd_ThreeLight(gateway_mac, "",type_area, area, A, timeA, B, timeB, C, timeC);
    }

    /**
     * 多彩灯相关参数
     */
    public static final String COLOR_MODE_STRAIGHT="01"; //4010设备  参数-立即开启
    public static final String COLOR_MODE_SLOWLY="02"; //4010设备  参数-渐渐开启
    public static final String COLOR_MODE_DELAY="03"; //4010设备  参数-延时开启
    public static final String COLOR_MODE_COLORFULJUMP="0a"; //模式-七彩跳变
    public static final String COLOR_MODE_COLORFULSLOWLY="09"; //模式-七彩渐变
    public static final String COLOR_MODE_BREATH="0b"; //模式-呼吸灯

    public static final String COLOR_MODE_MONOTONE="01";    //调光模式
    public static final String COLOR_MODE_COLORFUL="05";    //调色模式

    public static final String COLOR_TYPE_SPECIAL="01";  //类型-特殊
    public static final String COLOR_TYPE_COLORFUL="03"; //类型-调色
    public static final String COLOR_TYPE_MONOTONE_WARM="02"; //类型-暖光
    public static final String COLOR_TYPE_MONOTONE_COLD="04"; //类型-冷光

    /**
     * 单控多彩灯
     * @param gateway_mac   网关mac
     * @param mac   设备mac
     * @param startType 模式选择(COLOR_MODE_STRAIGHT、COLOR_MODE_SLOWLY、COLOR_MODE_DELAY、COLOR_MODE_COLORFULJUMP、COLOR_MODE_COLORFULSLOWLY、COLOR_MODE_BREATH)
     * @param colorType 类型选择（COLOR_TYPE_SPECIAL、COLOR_TYPE_COLORFUL、COLOR_TYPE_MONOTONE_WARM、COLOR_TYPE_MONOTONE_COLD）
     * @param time 控制效果的时间  16进制    2个字节
     * @param state 开关状态
     * @param r_w   16进制 0~255
     * @param g     16进制 0~255
     * @param b     16进制 0~255
     * @return
     */
    public static String getCmd_ColorLight_Only(String gateway_mac, String mac, String startType, String colorType, String time, boolean state, String r_w, String g, String b) {
        return CommandUtil.getCmd_ColorLight(gateway_mac, mac, "","", startType, colorType, time, state, r_w, g, b);
    }
    //简单多彩灯关闭指令
    public static String getCmd_ColorLight_Only_EasyClose(String gateway_mac, String mac){
        return CommandUtil.getCmd_ColorLight(gateway_mac, mac,"" ,"", COLOR_MODE_STRAIGHT, COLOR_TYPE_MONOTONE_WARM,"0000",false,"ff","00","00");
    }

    /**
     * 区域控多彩灯
     * @param gateway_mac   网关mac
     * @param type_area   设备类别+产品类型
     * @param area   区域信息
     * @param startType 模式选择(COLOR_MODE_STRAIGHT、COLOR_MODE_SLOWLY、COLOR_MODE_DELAY、COLOR_MODE_COLORFULJUMP、COLOR_MODE_COLORFULSLOWLY、COLOR_MODE_BREATH)
     * @param colorType 类型选择（COLOR_TYPE_SPECIAL、COLOR_TYPE_COLORFUL、COLOR_TYPE_MONOTONE_WARM、COLOR_TYPE_MONOTONE_COLD）
     * @param time 控制效果的时间  16进制    2个字节
     * @param state 开关状态
     * @param r_w   16进制 0~255
     * @param g     16进制 0~255
     * @param b     16进制 0~255
     * @return
     */
    public static String getCmd_ColorLight_Area(String gateway_mac, String type_area, String area, String startType, String colorType, String time, boolean state, String r_w, String g, String b) {
        return CommandUtil.getCmd_ColorLight(gateway_mac, "",type_area, area, startType, colorType, time, state, r_w, g, b);
    }
    //简单多彩灯关闭指令-区域
    public static String getCmd_ColorLight_Area_EasyClose(String gateway_mac, String type_area, String area){
        return CommandUtil.getCmd_ColorLight(gateway_mac, "",type_area,area, COLOR_MODE_STRAIGHT, COLOR_TYPE_MONOTONE_WARM,"0000",false,"ff","00","00");
    }

    /**
     * 设置多彩灯机械开关-单
     * @param gateway_mac  网关mac
     * @param node_mac      设备mac
     * @param lightModeA    模式选择（COLOR_MODE_MONOTONE、COLOR_MODE_COLORFUL、COLOR_MODE_COLORFULJUMP、COLOR_MODE_COLORFULSLOWLY、COLOR_MODE_BREATH）
     * @param r_w_A          A组  16进制 00~ff
     * @param gA             A组  16进制 00~ff
     * @param bA             A组  16进制 00~ff
     * @param lightModeB   模式选择（COLOR_MODE_MONOTONE、COLOR_MODE_COLORFUL、COLOR_MODE_COLORFULJUMP、COLOR_MODE_COLORFULSLOWLY、COLOR_MODE_BREATH）
     * @param r_w_B         B组  16进制 00~ff
     * @param gB            B组  16进制 0~255
     * @param bB            B组  16进制 0~255
     * @return
     */
    public static String getCmd_ColorLight_Set_Only(String gateway_mac, String node_mac, String lightModeA, String r_w_A, String gA, String bA, String lightModeB, String r_w_B, String gB, String bB) {
        return CommandUtil.getCmd_ColorLight_Set(gateway_mac, node_mac, lightModeA, r_w_A, gA, bA, lightModeB, r_w_B, gB, bB);
    }

    /**
     * 设置多彩灯机械开关-全部
     * @param gateway_mac  网关mac
     * @param lightModeA    模式选择（COLOR_MODE_MONOTONE、COLOR_MODE_COLORFUL、COLOR_MODE_COLORFULJUMP、COLOR_MODE_COLORFULSLOWLY、COLOR_MODE_BREATH）
     * @param r_w_A          A组  16进制 0~255
     * @param gA             A组  16进制 0~255
     * @param bA             A组  16进制 0~255
     * @param lightModeB   模式选择（COLOR_MODE_MONOTONE、COLOR_MODE_COLORFUL、COLOR_MODE_COLORFULJUMP、COLOR_MODE_COLORFULSLOWLY、COLOR_MODE_BREATH）
     * @param r_w_B         B组  16进制 0~255
     * @param gB            B组  16进制 0~255
     * @param bB            B组  16进制 0~255
     * @return
     */
    public static String getCmd_ColorLight_Set_All(String gateway_mac, String lightModeA, String r_w_A, String gA, String bA, String lightModeB, String r_w_B, String gB, String bB) {
        return CommandUtil.getCmd_ColorLight_Set(gateway_mac, "", lightModeA, r_w_A, gA, bA, lightModeB, r_w_B, gB, bB);
    }

    /**
     *获取场景控制器场景设置指令
     * @param mac1  网关mac
     * @param mac2  设备mac
     * @param A   第一个按钮对应的场景编号
     * @param B   第二个按钮对应的场景编号
     * @param C   第三个按钮对应的场景编号
     * @return
     */
    public static String getStateControlCommand(String mac1, String mac2, String A, String B, String C){
        return CommandUtil.getStateControlCommand(mac1,mac2,"".equals(A)?A:(A==null?"":A),"".equals(B)?B:(B==null?"":B),"".equals(C)?C:(C==null?"":C));
    }
}
