package com.kzksmarthome.common.lib.easylink.data;

/**
 * Created by Administrator on 2016/10/27 0027.
 */
public abstract class TempData {
    //获取该局域网下的wifi网关设备信息的ip组播地址
    public static final String GatewayBoardcastIp="255.255.255.255";
    //获取该局域网下的wifi网关设备信息的组播端口号
    public static final int GatewayBoardcastPort=8089;
    //获取该局域网下的wifi网关设备信息
    public static final String GetGatewayData="21000A000000D4FFFFFF";

    //当前连接状态
    public static String CONNECTIONSTATE="本地连接";  //当前连接模式
    //当前ssid
    public static String SSID="";
    //当前wifi密码
    public static String SSID_PWD="";
    //当前控制网关ip
    public static String IP="";
    //当前控制网关连接的端口号
    public static int PORT=3000;
    //当前控制网关mac
    public static String MAC="";

    //获取wifi模块下的zb信息--登入帧
    public static final String COMMAND_GET_WIFI_ZB="2A0D01005A8F3804006F0D001392004723";
    //获取该网关下的节点设备信息
    public static String COMMAND_GET_NODE_ZB="2A0D0100"+MAC+"03ED0F"; //缺少校验码跟23

    /**
     *4070  音乐背景器  第三方指令
     */
    //获取房间号
    public static final String COMMAND_MUSICBACKGROUND_GET_ROOMNUM="FA0000010102FE".toLowerCase();
    //音量加
    public static final String COMMAND_MUSICBACKGROUND_SOUND_ADD="FA026B02100786FE".toLowerCase();
    //音量减
    public static final String COMMAND_MUSICBACKGROUND_SOUND_PREVIOUS="FA026B02100685FE".toLowerCase();
    //开机
    public static final String COMMAND_MUSICBACKGROUND_BOOT="FA026B0210007FFE".toLowerCase();
    //关机
    public static final String COMMAND_MUSICBACKGROUND_SHUTDOWN="FA026B02100180FE".toLowerCase();
    //播放
    public static final String COMMAND_MUSICBACKGROUND_PLAY="FA026B02100281FE".toLowerCase();
    //暂停
    public static final String COMMAND_MUSICBACKGROUND_UNPLAY="FA026B02100382FE".toLowerCase();

    //智能窗帘
    //开启
    public static final String COMMAND_CURTAIN_OPEN="FF00003D4F50".toLowerCase();
    //关闭
    public static final String COMMAND_CURTAIN_CLOSE="FF00003D434C".toLowerCase();
    //暂停
    public static final String COMMAND_CURTAIN_PAUSE="FF00003D5354".toLowerCase();

}
