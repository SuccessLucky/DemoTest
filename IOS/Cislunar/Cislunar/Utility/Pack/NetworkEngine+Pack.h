//
//  NetworkEngine+Pack.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine.h"


NS_ASSUME_NONNULL_BEGIN

@interface NetworkEngine (Pack)

#pragma mark - private
- (NSData *)dataWithHexString:(NSString *)str;

/**
 刷新所有设备的状态
 
 @return NSData
 */
- (NSData *)doGetRefreshDeviceStatue;


#pragma mark - 心跳
/**
 网关维持和服务器通信的心跳帧
 
 @return NSData
 */
- (NSData *)doGetGatewayHeart;


/**
 返回所有OD=4010设备状态命令
 
 @return NSData
 */
- (NSData *)doAskAll0FAADeviceState;


/**
 2.5.1 设定网关电话号码  2.5.2 设定报警电话号码 2.5.3 设定报警短信号码
 
 @param strTel      电话号码
 @param strSonIndex 设定网关电话号码子索引:00 00 00 10；
 设定报警电话号码:00 00 00 20；
 短信号码1:00 00 00 40；
 短信号码2:00 00 00 80；
 短信号码3:00 00 01 00；
 短信号码4:00 00 02 00；
 短信号码5:00 00 04 00
 
 @return NSData
 */
- (NSData *)doGetSetGatewayPhoneNum:(NSString *)strTel sonIndex:(NSString *)strSonIndex;


///**
// 撤防
//
// @return NSData
// */
//- (NSData *)doGetDismissAlertData;
//
//
///**
// 布防
//
// @return NSData
// */
//- (NSData *)doGetSetAlertData;


/**
 连接cloudserver
 
 @return data
 */
- (NSData *)doGetSendTelPhoneInfoToCloudSeverDataWithGatewayAddr;

@end

NS_ASSUME_NONNULL_END
