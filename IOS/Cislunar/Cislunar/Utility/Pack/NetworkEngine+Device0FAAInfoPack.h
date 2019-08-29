//
//  NetworkEngine+Device0FAAInfoPack.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine.h"

NS_ASSUME_NONNULL_BEGIN

@interface NetworkEngine (Device0FAAInfoPack)

#pragma mark -2.1(OD索引4010= 0x0FAA )-开、关、停控制

///**
// 此控制器定义主要适用于输出设备（主要是用来控制设备的开、关、停、调光及调色等）和输出设备（主要是用来上报设备的状态）
//
// @param strTargetAddr 目标macAddr
// @param strSonIndexLength sonIndexLength
// @param iWay 第几路
// @param controlMode 控制模式 0-无操作； 1-直接操作； 2-渐渐操作； 3-延时操作
// @param controlState 控制状态 0-无操作； 1-开； 2-关； 3-状态切换； 4-暂停
// @param strDelayTime 延时控制的时间长度（秒） 0-65535
// @return NSData
// */
//- (NSData *)doGetSwitchControlWithTargetAddr:(NSString *)strTargetAddr
//                              sonIndexLength:(NSString *)strSonIndexLength
//                                         way:(int)iWay
//                                 controlMode:(NSString *)controlMode
//                                controlState:(NSString *)controlState
//                                   delayTime:(NSString *)strDelayTime;
- (NSData *)doGetSwitchControlWithTargetAddr:(NSString *)strTargetAddr
                                      device:(SHModelDevice *)device
                                         way:(int)iWay
                                 controlMode:(NSString *)controlMode
                                controlState:(NSString *)controlState;

/**
 2.1.1.1 开、关、停控制 (多路同时控制，针对二路，和三路)
 
 @param device device
 @return data
 */
- (NSData *)doGetSwitchControlAllWithSendDevice:(SHModelDevice *)device;

@end

NS_ASSUME_NONNULL_END
