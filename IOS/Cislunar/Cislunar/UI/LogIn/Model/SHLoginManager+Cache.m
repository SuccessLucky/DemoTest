//
//  SHLoginManager+Cache.m
//  SmartHouseYCT
//
//  Created by apple on 16/9/22.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHLoginManager+Cache.h"

@implementation SHLoginManager (Cache)

#pragma mark - private

- (void)doWriteWifiMacAddr:(NSString *)strWifiMacAddr
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:strWifiMacAddr forKey:@"WifiMacAddr"];
    [userDefaults synchronize];
}

- (NSString *)doGetWifiMacAddr
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    return [userDefaults objectForKey:@"WifiMacAddr"];
}

- (void)doClearWifiMacAddr
{
    NSUserDefaults *defaults=[NSUserDefaults standardUserDefaults];
    [defaults removeObjectForKey:@"WifiMacAddr"];
    [defaults synchronize];
}

#pragma mark - gataWayName 名字的存入，读取，和删除
- (void)doWriteRememberGatewayName:(NSString *)gatewayName
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:gatewayName forKey:@"gatewayName"];
    [userDefaults synchronize];
}

- (NSString *)doGetRememerGatewayName
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    return [userDefaults objectForKey:@"gatewayName"];
}

- (void)doClearRememerGatewayName
{
    NSUserDefaults *defaults=[NSUserDefaults standardUserDefaults];
    [defaults removeObjectForKey:@"gatewayName"];
    [defaults synchronize];
}

#pragma mark - gataWayIp的存入，读取，和删除
- (void)doWriteRememberGatewayIp:(NSString *)gatewayIP
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:gatewayIP forKey:@"gatewayIP"];
    [userDefaults synchronize];
}

- (NSString *)doGetRememerGatewayIP
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    return [userDefaults objectForKey:@"gatewayIP"];
}

- (void)doClearRememerGatewayIP
{
    NSUserDefaults *defaults=[NSUserDefaults standardUserDefaults];
    [defaults removeObjectForKey:@"gatewayIP"];
    [defaults synchronize];
}

#pragma mark - gataWayPort 端口的存入，读取，和删除
- (void)doWriteRememberGatewayPort:(NSString *)port
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:port forKey:@"port"];
    [userDefaults synchronize];
}

- (NSString *)doGetRememerGatewayPort
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    return [userDefaults objectForKey:@"port"];
}

- (void)doClearRememerGatewayPort
{
    NSUserDefaults *defaults=[NSUserDefaults standardUserDefaults];
    [defaults removeObjectForKey:@"port"];
    [defaults synchronize];
}

#pragma mark - rememberAccount的存入，读取，和删除
- (void)doWriteRememberAccount:(NSString *)strRememberAccount
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:strRememberAccount forKey:@"strRememberAccount"];
    [userDefaults synchronize];
}

- (NSString *)doGetRememberAccount
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    return [userDefaults objectForKey:@"strRememberAccount"];
}

- (void)doClearRememberAccount
{
    NSUserDefaults *defaults=[NSUserDefaults standardUserDefaults];
    [defaults removeObjectForKey:@"strRememberAccount"];
    [defaults synchronize];
}

#pragma mark - 过CurentSignToken的存入，读取，和删除
- (void)doWriteCurentSignToken:(NSData *)strCurentSignToken
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:strCurentSignToken forKey:@"usrToken"];
    [userDefaults synchronize];
}

- (NSString *)doGetCurentSignToken
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    return [userDefaults objectForKey:@"usrToken"];
}

- (void)doClearCurentSignToken
{
    NSUserDefaults *defaults=[NSUserDefaults standardUserDefaults];
    [defaults removeObjectForKey:@"usrToken"];
    [defaults synchronize];
}

#pragma mark - 登录密码的存入，读取，和删除
- (void)doWriteRememberPsw:(NSString *)strRememberPsw
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:strRememberPsw forKey:@"RememberPsw"];
    [userDefaults synchronize];
}

- (NSString *)doGetRememberPsw
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    NSString *str = (NSString *)[userDefaults objectForKey:@"RememberPsw"];
    return str;
}

- (void)doClearRememberPsw
{
    NSUserDefaults *defaults=[NSUserDefaults standardUserDefaults];
    [defaults removeObjectForKey:@"RememberPsw"];
    [defaults synchronize];
}

#pragma mark - 网关的macAddr的存入，读取，和删除
-(void)doWriteGatewayMacAddr:(NSString *)dataGatewayMacAddr
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:dataGatewayMacAddr forKey:@"gatewayMacAddr"];
    [userDefaults synchronize];
}

-(NSString *)doGetGatewayMacAddr
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    return [userDefaults objectForKey:@"gatewayMacAddr"];
}

- (void)doClearGatewayMacAddr
{
    NSUserDefaults *defaults=[NSUserDefaults standardUserDefaults];
    [defaults removeObjectForKey:@"gatewayMacAddr"];
    [defaults synchronize];
}


#pragma mark - 写入、读取、删除网关的memberType
-(void)doWriteGatewayMemberType:(NSString *)memberType
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:memberType forKey:@"MemberType"];
    [userDefaults synchronize];
}

-(NSString *)doGetGatewayMemberType
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    return [userDefaults objectForKey:@"MemberType"];
}

- (void)doClearGatewayMemberType
{
    NSUserDefaults *defaults=[NSUserDefaults standardUserDefaults];
    [defaults removeObjectForKey:@"MemberType"];
    [defaults synchronize];
}

#pragma mark - 写入、读取、删除设防状态的
-(void)doWriteSecurityStatus:(NSString *)securityStatus
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:securityStatus forKey:@"securityStatus"];
    [userDefaults synchronize];
}

-(NSString *)doGetSecurityStatus
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    return [userDefaults objectForKey:@"securityStatus"];
}

- (void)doClearSecurityStatus
{
    NSUserDefaults *defaults=[NSUserDefaults standardUserDefaults];
    [defaults removeObjectForKey:@"securityStatus"];
    [defaults synchronize];
}


#pragma mark - 写入、读取、删除GeTuiClientID
-(void)doWriteGeTuiClientID:(NSString *)geTuiClientID
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:geTuiClientID forKey:@"GeTuiClientID"];
    [userDefaults synchronize];
}

-(NSString *)doGetGeTuiClientID
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    return [userDefaults objectForKey:@"GeTuiClientID"];
}

- (void)doClearGeTuiClientID
{
    NSUserDefaults *defaults=[NSUserDefaults standardUserDefaults];
    [defaults removeObjectForKey:@"GeTuiClientID"];
    [defaults synchronize];
}


- (void)doWriteWifiHardware:(NSString *)strWifiHardware
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:strWifiHardware forKey:@"WifiHardware"];
    [userDefaults synchronize];
}

- (NSString *)doGetWifiHardware
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    return [userDefaults objectForKey:@"WifiHardware"];
}

- (void)doClearWifiHardware
{
    NSUserDefaults *defaults=[NSUserDefaults standardUserDefaults];
    [defaults removeObjectForKey:@"WifiHardware"];
    [defaults synchronize];
}

#pragma mark -写入、读取、删除登录状态
- (void)doWriteLoginState:(NSInteger)iLoginState
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setInteger:iLoginState forKey:@"iLoginState"];
    [userDefaults synchronize];
}

- (NSInteger)doGetLoginState
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    return [userDefaults integerForKey:@"iLoginState"];
}

- (void)doClearLoginState
{
    NSUserDefaults *defaults=[NSUserDefaults standardUserDefaults];
    [defaults removeObjectForKey:@"iLoginState"];
    [defaults synchronize];
}


- (void)doSetUserInfoNull
{
    NSString *appDomain = [[NSBundle mainBundle] bundleIdentifier];
    [[NSUserDefaults standardUserDefaults] removePersistentDomainForName:appDomain];
    
}

- (void)doClearAllUserInfoExceptRememberAccount
{
    [self doClearCurentSignToken];
    [self doClearRememberPsw];
}

- (void)doClearAllShouldData
{
    [[SHDataBaseManager sharedInstance] doDeleteTable:Table_Cache complete:nil];
    [[SHDataBaseManager sharedInstance] doDeleteTable:Table_Cache_Room complete:nil];
    [[SHDataBaseManager sharedInstance] doDeleteTable:Table_Cache_Device complete:nil];
    [[SHDataBaseManager sharedInstance] doDeleteTable:Table_Cache_MemberControlList complete:nil];
    [[SHDataBaseManager sharedInstance] doDeleteTable:Table_Cache_Lock complete:nil];
    [[SHDataBaseManager sharedInstance] doDeleteTable:Table_Cache_LockMemberList complete:nil];
    
    [[SHLoginManager shareInstance] doClearRememerGatewayIP];
    [[SHLoginManager shareInstance] doClearRememerGatewayPort];
    [[SHLoginManager shareInstance] doClearRememerGatewayName];
    [[SHLoginManager shareInstance] doClearGatewayMemberType];
    
    [[SHLoginManager shareInstance] doClearSecurityStatus];
    [[SHLoginManager shareInstance] doClearGatewayMacAddr];
    [[SHLoginManager shareInstance] doClearWifiMacAddr];
    
    [[SHLoginManager shareInstance] doClearWifiHardware];
    [[SHLoginManager shareInstance] doClearCurentSignToken];
}


@end
