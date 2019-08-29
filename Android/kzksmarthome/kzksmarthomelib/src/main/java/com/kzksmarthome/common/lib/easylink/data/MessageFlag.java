package com.kzksmarthome.common.lib.easylink.data;

/**
 * Created by Administrator on 2016/10/27 0027.
 */
public interface MessageFlag {
    public static int FAILED=96;   //失败
    public static int WAIT=97;   //缓冲等待
    public static int WAITOVER=98;   //等待结束状态
    public static int UPDATEUI=99;   //更新UI
    public static int CLEARALL=44;   //清空

    public static int ADD_GATEWAY=0;   //网关添加

    public static int ADD_UNKNOW=1404;   //未知设备添加

//    public static int ADD_ONELIGHT=1101;   //一路灯添加
//    public static int ADD_TWOLIGHT=1102;   //二路灯添加
//
//    public static int ADD_THREELIGHT=1103;   //三路灯添加
//
//    public static int ADD_COLORLIGHT=1201;   //多彩灯添加
//
//    public static int ADD_CURTAIN=1301;   //幕布或窗帘添加
//
//    public static int ADD_DOOR=1501;   //门磁添加
//
//    public static int ADD_FINGERPRINTLOCK=1601;   //指纹锁添加
//
//    public static int ADD_STATE_CONTROL=1701;   //场景控制器添加
//
//    public static int ADD_SOUND_LIGHT_ALARM=1801;   //声光报警器添加
//
//    public static int ADD_CO_SENSOR=1901;   //一氧化碳传感器添加
//    public static int ADD_INFRAREDSENSOR=1902;   //红外传感器传感器添加
//    public static int ADD_HUMANHEATSENSOR=1903;  //人体热释传感器添加
//    public static int ADD_WATERSENSOR=1904;  //水浸传感器添加
//    public static int ADD_SMOKE_SENSOR=1905;   //烟雾传感器添加
//
//    public static int ADD_METERINGSOCKET=2001; //计量插座添加
//    public static int ADD_CONTROLBOX=2002; //计量控制盒添加

    public static int TYPE_CHANGE=1; //普通模式、超级模式
}
