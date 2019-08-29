//
//  CommonDefine.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/13.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#ifndef CommonDefine_h
#define CommonDefine_h

#define kYReadGatewayConfigInfoNotify @"kYReadGatewayConfigInfoNotify"
#define kYGatewayConfigNotify       @"kYGatewayConfigNotify"
#define kGSocketConnectNotify       @"kGSocketConnectNotify"
#define kNotiAppLogout              @"kNotiAppLogout"
#define kGetGatewayMacAddr          @"kGetGatewayMacAddr" //获取网关的MacAddr
#define kRegisterCloudsever         @"kRegisterCloudsever" //成功注册cluodsever

#define kMaxRequestRetryCount       (3)         //请求重试最大次数
#define kMaxRequestRetryTimeout     (60 * 30)   //请求重试最大超时时间

#define GNetReqDefaultTimeOut       (30)        //默认超时时间

#define GNetReqNoTimeOut            (-1)
#define GNetReqDefaultSocketTag     (0)         //默认socketTag

#define kTcpConnectInterval         (0.5)       //tcp重连的间隔

//错误信息自定义
#define G_ERROR_DOMAIN_SERVER        @"domain.server"        //server错误
#define G_ERROR_DOMAIN_CLIENT        @"domain.client"        //客户端错误
#define G_ERROR_DOMAIN_TCP         @"domain.tcp"           //tcpSDK返回错误
#define G_ERROR_DOMAIN_PACKET        @"domain.packet"        //数据包解包出错
#define G_ERROR_DOMAIN_BUSINESS    @"domain.business"        //业务出错

#define cloudIP @"47.105.128.44"
#define cloudPort 7001


//数据包相关东西
#define CMW_FrameStart  0x2A
#define CMW_FrameEnd    0x23

#define CMW_CloudFrameStart 0x55
#define CMW_CloudFrameEnd   0xAA

/* 红外转发器：
 *  学习CMD：0x88
 *  控制CMD：0x86
 */
#define kInfraredStudyCMD   @"88"
#define kInfraredSendCMD    @"86"

#pragma mark -- UnPack
//包头和包尾
#define kStrHexHeader       @"2a"
#define kStrHexEnd          @"23"

//CmdID
#define kCmdID_StrHexRead_OD                @"01"  //Read_OD命令
#define kCmdID_StrHexRead_OD_Error          @"81"  //Read _OD命令执行错误
#define kCmdID_StrHexWrite_OD               @"02"  //Write_OD命令
#define kCmdID_StrHexWrite_OD_Error         @"82"  //Write_OD命令执行错误
#define kCmdID_StrHexAdvertisement          @"05"  //声明集中器命令也就是广告（Advertisement）命令即针对广播命令返回的数据帧
#define kCmdID_StrHexTransmit               @"07"  //转发命令
#define kCmdID_StrHexScreenConfig           @"50"   //场景配置标识符
#define kCmdID_StrHexScreenConfigFail       @"d0"   //场景配置失败

//OD
#define kOD_NetParameter_Samll                          @"03e9" //节点网络参数,网络中的所有节点
#define kOD_NetParameter_Big                            @"03E9"

#define kOD_TimeParameter_Samll                         @"03ea"//当前时间参数,网络中的所有节点
#define kOD_TimeParameter_Big                           @"03EA"

#define kOD_EquipmentNeighbourParameter_Samll           @"03eb"//设备邻居表参数,网络中的所有节点
#define kOD_EquipmentNeighbourParameter_Big             @"03EB"

#define kOD_EquipmentParameter_Samll                    @"03ee"//设备配置参数,网络中的所有节点
#define kOD_EquipmentParameter_Big                      @"03EE"

#define kOD_Socket_Samll                                @"0bbd"//插座表,带计量功能的固定插座、移动插座
#define kOD_Socket_Big                                  @"0BBD"

#define kOD_SwitchActionControl_Samll                   @"0faa"//多用途开关动作控制器
#define kOD_SwitchActionControl_Big                     @"0FAA"

#define kOD_DormancyControl_Samll                       @"0fbe"//多用途休眠设备控制器
#define kOD_DormancyControl_Big                         @"0FBE"

#define kOD_Ammeter_Samll                               @"0fc8"//电表
#define kOD_Ammeter_Big                                 @"0FC8"

#define kOD_ProtocolTransmit_Samll                      @"0fe6"//协议转发
#define kOD_ProtocolTransmit_Big                        @"0FE6"

#define kOD_Heart_Small                                 @"139c"
#define kOD_Heart_Big                                   @"139C"

#define kOD_HeartLoginFrameSmall                        @"1392"
#define kOD_heartLoginFrameBig                          @"1392"

#define kOD_GetGatewayTime_Small                        @"13b0"
#define kOD_GetGatewayTime_Big                          @"13B0"

#define kOD_GetGatewayWifiDetail_small                        @"13c4"
#define kOD_GetGatewayWifiDetail_big                        @"13C4"


//子索引
#define kSonIndex_Base           @"00"
#define kSonIndex_Selected       @"ff"

//指纹锁的CmdID
#define kFingerLockCmdID_RightOrder                     @"00"//确认正确指令
#define kFingerLockCmdID_ErrorOrder                     @"01"//确认错误指令
#define kFingerLockCmdID_Recovery                       @"10"//恢复出厂设置
#define kFingerLockCmdID_RegisterPswSucced              @"11"//注册密码成功
#define kFingerLockCmdID_DeletePswSucced                @"12"//删除密码成功

#define kFingerLockCmdID_RegisterFingerprintSucced      @"13"//注册指纹成功
#define kFingerLockCmdID_DeleteFingerprintSucced        @"14"//删除指纹成功
#define kFingerLockCmdID_RegisterICSucced               @"15"//注册IC卡成功
#define kFingerLockCmdID_DeleteICSucced                 @"16"//删除IC卡成功
#define kFingerLockCmdID_IllegalOperation               @"20"//指纹锁非法操作

#define kFingerLockCmdID_Tamper                         @"21"//指纹锁防撬报警
#define kFingerLockCmdID_Undervoltage                   @"30"//电池欠压状态上报
#define kFingerLockCmdID_NetIn                          @"40"//无线模块入网指令
#define kFingerLockCmdID_NetOut                         @"41"//无线模块退网指令
#define kFingerLockCmdID_OpenReport                     @"50"//指纹开锁情况上报

#define kFingerLockCmdID_PswOpenReport                  @"51"//密码开锁情况上报
#define kFingerLockCmdID_ICOpenReport                   @"52"//C卡开锁情况上报
#define kFingerLockCmdID_SystemOpenReport               @"60"//系统指令开锁
#define kFingerLockCmdID_ResetNull                      @"70"//按键扫描成功：清空键
#define kFingerLockCmdID_PswVerify                      @"90"//密码验证


//电动窗帘和平移开窗器的类型
#define kTransmitElectricCurtains               @"00"
#define kTransmitWindow                         @"01"

#define xf_fuctionCode          @"xf_fuctionCode";
#define xf_switch               @"xf_switch";
#define xf_helpWarm             @"xf_helpWarm";
#define xf_workMode             @"xf_workMode";

#define xf_circulationMode      @"xf_circulationMode";
#define xf_inAndOutWindScale    @"xf_inAndOutWindScale";
#define xf_windSpeedGear        @"xf_windSpeedGear";
#define xf_airVolume            @"xf_airVolume";

#define xf_particulates         @"xf_particulates";
#define xf_CO2ConCtent          @"xf_CO2ConCtent";
#define xf_intakeTemperature    @"xf_intakeTemperature";
#define xf_outflowTemperature   @"xf_outflowTemperature";

#define xf_normalOrMalfunction  @"xf_normalOrMalfunction";
#define xf_chuLvTimePer         @"xf_chuLvTimePer";
#define xf_dustBoxTimePer       @"xf_dustBoxTimePer";
#define xf_gaoLvTimePer         @"xf_gaoLvTimePer";


//新风
#define XFTransimitData @"XFTransimitData"
#define XFTransimitLength @"XFTransimitLength"

//场景tag
#define SwitchLinkageTag    101
#define SwitchDelayedTag    102
#define SwitchTimerTag      103
#define SwitchArmingTag     104
#define SwitchDisarmingTag  105

#define ScreenEditNameTag       106
#define ScreenEditDelayedTag    107
#define ScreenEditTimerTag      108

//是否刷新场景
#define kScreenShouldRefresh        @"kScreenShouldRefresh"
#define kScreenCofigNoti            @"kScreenCofigNoti"
#define kScreenDelteShouldRefresh   @"kScreenDelteShouldRefresh"


#endif /* CommonDefine_h */
