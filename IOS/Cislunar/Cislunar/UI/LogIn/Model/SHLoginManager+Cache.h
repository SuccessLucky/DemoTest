//
//  SHLoginManager+Cache.h
//  SmartHouseYCT
//
//  Created by apple on 16/9/22.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHLoginManager.h"

@interface SHLoginManager (Cache)
/**
 *  写入和读取，删除wifi模块的mac地址
 *
 *  @return void
 */
- (void)doWriteWifiMacAddr:(NSString *)strWifiMacAddr;

- (NSString *)doGetWifiMacAddr;

- (void)doClearWifiMacAddr;

#pragma mark - rememberIp
/**
 *  写入、读取、删除rememberIp
 *
 *  @return void
 */
- (void)doWriteRememberGatewayIp:(NSString *)gatewayIP;

- (NSString *)doGetRememerGatewayIP;

- (void)doClearRememerGatewayIP;

#pragma mark - gataWay 端口的存入，读取，和删除
/**
 *  写入、读取、删除rememberPort
 *
 *  @return void
 */
- (void)doWriteRememberGatewayPort:(NSString *)port;
- (NSString *)doGetRememerGatewayPort;
- (void)doClearRememerGatewayPort;

#pragma mark - gataWay 名字的存入，读取，和删除
/**
 *  写入、读取、删除rememberPort
 *
 *  @return void
 */
- (void)doWriteRememberGatewayName:(NSString *)gatewayName;
- (NSString *)doGetRememerGatewayName;

- (void)doClearRememerGatewayName;


#pragma mark - rememberAccount
/**
 *  写入、读取、删除rememberAccount
 *
 *  @return void
 */
- (void)doWriteRememberAccount:(NSString *)strRememberAccount;

- (NSString *)doGetRememberAccount;

- (void)doClearRememberAccount;


#pragma mark - rememberPsw
/**
 *  写入、读取、删除rememberPsw
 *
 *  @return void
 */
- (void)doWriteRememberPsw:(NSString *)strRememberPsw;

- (NSString *)doGetRememberPsw;


- (void)doClearRememberPsw;

#pragma mark - SignToken
/**
 *  写入、读取、删除curentSignToken
 *
 *  @return void
 */
- (void)doWriteCurentSignToken:(NSString *)strCurentSignToken;

- (NSString *)doGetCurentSignToken;

- (void)doClearCurentSignToken;

/**
 *  写入、读取、删除网关Zigbee的macAddr
 *
 *  @return void
 */
-(void)doWriteGatewayMacAddr:(NSString *)dataGatewayMacAddr;

-(NSString *)doGetGatewayMacAddr;

- (void)doClearGatewayMacAddr;

/**
 *  写入、读取、删除网关的memberType
 *
 *  @return void
 */
-(void)doWriteGatewayMemberType:(NSString *)memberType;

-(NSString *)doGetGatewayMemberType;

- (void)doClearGatewayMemberType;


/**
 写入、读取、删除设防状态的

 @param securityStatus 设防状态 安防状态，0：撤防，1布防
 */
-(void)doWriteSecurityStatus:(NSString *)securityStatus;

-(NSString *)doGetSecurityStatus;

- (void)doClearSecurityStatus;

#pragma mark - 写入、读取、删除GeTuiClientID
-(void)doWriteGeTuiClientID:(NSString *)geTuiClientID;
-(NSString *)doGetGeTuiClientID;
- (void)doClearGeTuiClientID;

#pragma mark - 写入读取删除wifiHardware
- (void)doWriteWifiHardware:(NSString *)strWifiHardware;
- (NSString *)doGetWifiHardware;
- (void)doClearWifiHardware;

#pragma mark -写入、读取、删除登录状态
- (void)doWriteLoginState:(NSInteger)iLoginState;
- (NSInteger)doGetLoginState;
- (void)doClearLoginState;


/**
 *  清除当前用户信息
 */
- (void)doSetUserInfoNull;

/**
 *  清除除记录的用户名外的其他数据
 */
- (void)doClearAllUserInfoExceptRememberAccount;


/**
 切换用户或者退出登录的时候要清理的数据
 */
- (void)doClearAllShouldData;


@end
