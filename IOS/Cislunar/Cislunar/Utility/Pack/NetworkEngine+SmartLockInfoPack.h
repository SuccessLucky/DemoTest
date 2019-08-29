//
//  NetworkEngine+SmartLockInfoPack.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine.h"

NS_ASSUME_NONNULL_BEGIN

@interface NetworkEngine (SmartLockInfoPack)

#pragma mark-  指纹密码锁
/**
 设定指纹密码锁
 
 @param strTargetAddr 指纹密码锁的macAddr
 @param strCmdID    0x00 确认正确指令
 0x01 确认错误指令
 0x10 恢复出厂设置
 0x11 注册密码成功
 0x12 删除密码成功
 0x13 注册指纹成功，
 0x14 删除指纹成功
 0x15 注册IC卡成功
 0x16 删除IC卡成功
 0x20 指纹锁非法操作
 0x21 指纹锁防撬报警
 0x30 电池欠压状态上报
 0x40 无线模块入网指令
 0x41 无线模块退网指令
 0x50 指纹开锁情况上报
 0x51 密码开锁情况上报
 0x52 IC卡开锁情况上报
 0x60 系统指令开锁
 0x70 按键扫描成功：清空键
 0x90 密码验证
 @return NSData
 */

//指纹锁开锁

- (NSData *)doGetFingerprintCombinationLockWakeUpTheDoorDataWithTargetMacAddr:(NSString *)strTargetAddr;//唤醒开锁

- (NSData *)doGetFingerprintCombinationLockOpenTheDoorDataWithTargetMacAddr:(NSString *)strTargetAddr;

- (NSData *)doGetFingerprintCombinationLockDataWithTargetMacAddr:(NSString *)strTargetAddr cmdID:(NSString *)strCmdID;



#pragma mark -------------------------- 新指纹锁 -----------------------------------
- (NSData *)sendNewLockControlWithGatewayMacAddr:(NSData *)dataGatewayMacAddr
                              andDestinationAddr:(NSString *)strDestinationAddr
                                withcontrolIndex:(unsigned char)controlIndex;

#pragma mark - new
- (NSData *)doGetRandomNumLockOpenTheDoorDataWithTargetMacAddr:(NSString *)strTargetAddr
                                                  randomNumber:(NSString *)strHEXRandomNum
                                                     validTime:(NSString *)strHEXValidTime
                                                         times:(NSString *)strHEXTimes;

//
//-(void)sendTheSetGatewayTelePhoneWithTheTypeIndentifier:(int)indentifierTelSet andTelNum:(NSString *)strTel WithGatewayMacAddr:(NSData *)dataGatewayMacAddr;

//- (NSData *)doGetRandomNumLockOpenTheDoorDataWithTargetMacAddr:(NSString *)strTargetAddr
//                                                  randomNumber:(NSString *)strRandomNumOrigin
//                                                     validTime:(NSString *)strHEXValidTime
//                                                         times:(NSString *)strHEXTimes;

#pragma mark - 小蛮腰远程开锁
- (NSData *)doSendRemoteOpenXMYLockOrderWithTargetMacAddr:(NSString *)strTargetAddr lockPsw:(NSString *)strPsw;

@end

NS_ASSUME_NONNULL_END
