//
//  NetworkEngine+AirConditionInfoPack.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine.h"

NS_ASSUME_NONNULL_BEGIN

@interface NetworkEngine (AirConditionInfoPack)

#pragma mark - 空调
/**
 设定空调型号
 
 @param strTargetAddr 红外转发器的MacAddr
 @param modelNo       参照空调型号表
 
 @return NSData
 */
- (NSData *)doGetSetAirConditionModelTargetAddr:(NSString *)strTargetAddr modelNO:(int)modelNo;

/**
 发送空调开关命令
 
 @param strTargetAddr 红外转发器的MacAddr
 @param strOnOrOff    FF： 开，00：关，其余参数无效
 
 @return NSData
 */
- (NSData *)doGetSetAirConditionOnOrOffTargetAddr:(NSString *)strTargetAddr strOnOrOff:(NSString *)strOnOrOff;

/**
 发送模式命令
 
 @param strTargetAddr 红外转发器的MacAddr
 @param mode          00：自动  01：制冷   02：除湿    03：送风   04：制暖
 
 @return NSData
 */
- (NSData *)doGetSetAirConditionModeTargetAddr:(NSString *)strTargetAddr mode:(NSString *)mode;

/**
 发送温度命令
 
 @param strTargetAddr 红外转发器的MacAddr
 @param temperature   温度值 范围：10H - 1EH （16-31度）其余无效
 
 @return NSData
 */
- (NSData *)doGetSetAirConditionTemperatureTargetAddr:(NSString *)strTargetAddr temperature:(int)temperature;

/**
 发送风速命令
 
 @param strTargetAddr 红外转发器的MacAddr
 @param speed   00 = 自动  01=1档   02=2档    03=3档  其余无效
 
 @return NSData
 */
- (NSData *)doGetSetAirConditionSpeedTargetAddr:(NSString *)strTargetAddr speed:(NSString *)speed;

/**
 发送风向命令
 
 @param strTargetAddr  红外转发器的MacAddr
 @param direction     00 = 自动摆风  01手动摆风  其余无效
 
 @return NSData
 */
- (NSData *)doGetSetAirConditionDirectionTargetAddr:(NSString *)strTargetAddr direction:(NSString *)direction;

@end

NS_ASSUME_NONNULL_END
