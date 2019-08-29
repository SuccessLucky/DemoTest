//
//  NetworkEngine+Device0FE6InfoPack.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine.h"
#import "CommonStructDefine.h"

NS_ASSUME_NONNULL_BEGIN

@interface NetworkEngine (Device0FE6InfoPack)

/**
 2.7.1 红外转发器-学习红外信号
 
 @param strTargetAddr 红外转发器的MacAddr
 @param studyCode     学习的编号0~99(即0x00~0x64)
 
 @return NSData
 */
- (NSData *)doGetInfraredStudyDataTargetAddr:(NSString *)strTargetAddr studyCode:(int)studyCode;

/**
 2.7.1 红外转发器-发射已经学习红外信号
 
 @param strTargetAddr 红外转发器的MacAddr
 @param studyCode     已经学习的编号0~99(即0x00~0x64)
 
 @return NSData
 */
- (NSData *)doGetInfraredSendDataTargetAddr:(NSString *)strTargetAddr studyCode:(int)studyCode;


/**
 电动窗帘和平移开窗器
 
 @param actionType            actionType
 @param strHexLocation        strHexLocation 取值范围十六进制字符串：00~10
 
 @return data
 */
- (NSData *)doGetElectricCurtainsOrWindowDataWithModelDevice:(SHModelDevice *)device
                                                  actionType:(ElectricTransimitObjecActionType)actionType
                                                    location:(NSString *)strHexLocation;


/**
 净水器
 
 @param strTargetAddr 设备MacAddr
 @return data
 */
- (NSData *)doGetGetWaterPurifierSendDataWithTargetMacAddr:(NSString *)strTargetAddr
                                 waterPurifierFunctionType:(SHWaterPurifierFunctionType)type;


#pragma mark - 豪力士锁
- (NSData *)doGetLockHLSWithModelDevice:(SHModelDevice *)device strHexHLSCmdID:(NSString *)strHexHLSCmdID;


#pragma mark - 新风系统
- (NSData *)doSendXinFengDataWithModelDevice:(SHModelDevice *)device
                           functionIdentifer:(NSString *)strFuctionIdentifer
                          dataRangeIdentifer:(NSString *)strDataRangeIdentifer;


#pragma mark -  中央空调
- (NSData *)doSendVRVDataWithModelDevice:(SHModelDevice *)device;

@end

NS_ASSUME_NONNULL_END
