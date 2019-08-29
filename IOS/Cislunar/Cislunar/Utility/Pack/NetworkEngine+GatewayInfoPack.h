//
//  NetworkEngine+GatewayInfoPack.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine.h"

NS_ASSUME_NONNULL_BEGIN

@interface NetworkEngine (GatewayInfoPack)

//从读取网关的Mac地址
- (NSData *)doGetGatewayZigbeeMacAddressFromGateway;

//3.1、读取全部网关参数（OD：1001）
- (NSData *)doGetGatewayAllParameters;

//3.2、读取网关参数 （OD：1005）
- (NSData *)doGetGatewayPartOfParameters;

//3.3、读取网关登入帧（OD：5010）
- (NSData *)doGetGatewayLoginFrame;

//3.4、读取网关心跳帧（OD：5020）
- (NSData *)doReadGatewayHeart;

//3.5、读取网关基本信息帧（OD：5040）
- (NSData *)doReadGatewayBaseInfoFrame;

//从本地获取网关的MAC地址
- (NSData *)doGetGatewayZigbeeMacAddrFromLocal;

//获取全为“00”的Mac地址
- (NSData *)doGetGatewayZigbeeMacAddrFromLocalWithZero;

// 获取网关时间
- (NSData *)doGetGatewayTimeData;

//设置网关时间
- (NSData *)doSetGatewayTimeDataWith:(NSString *)time;

// 重启wifi模块
- (NSData *)doGetRestarWifiModuleData;

// 重启Zigbee模块
- (NSData *)doGetRestarZigbeeModuleData;

// 重启系统模块
- (NSData *)doGetRestarSystemModuleData;

//心跳归零数据帧
- (NSData *)doGetGatewayHeartClearToZero;

//激活心跳数据帧
- (NSData *)doActivateHeartFrame;


#pragma mark - 读取网关时间
- (NSData *)doReadTheCurrentTimeFromGateway;

#pragma mark - 设置网关时间
- (NSData *)doHandleSetGatewayTimeNew;

//发送入网状态
//strIdentiferModel入网状态开启主动关闭超时时间为240秒，配置参数范围：0x00-0xFF，0x00禁止入网，0xFF永久允许  可以默认为F0入网
- (NSData *)doGatewayAllowAddDevicesWithStrIdentiferModel:(NSString *)strIdentiferModel;

#pragma mark -
#pragma mark - 服务器信息配置
-(NSData*)doSendIpToServerWithIp:(NSString *)strIp
                       localPort:(int)iLocalPort
                         outPort:(int)iOutPort;

//发送配置远程地址、远程端口、本地端口的指令
- (void)sendCMDToChangeRemoteIpLocalPortAndRemotePortWithGatewayZigbeeMacWithGatewayMacAddr:(NSString *)strMacAddr;

// 读取网关wifi信息的命令
- (void)doReadGatewayWifiDetailsWithGatewayMacAddr:(NSString *)strGatewayMacAddr;

#pragma mark -此时再发一个网关复位的指令，这个指令没有回调
- (void)deResetGatewayOrderWithGatewayMacAddr:(NSString *)strGatewayMacAddr;

@end

NS_ASSUME_NONNULL_END
