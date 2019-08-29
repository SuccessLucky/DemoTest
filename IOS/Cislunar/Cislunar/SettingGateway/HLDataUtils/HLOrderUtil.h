//
//  HLOrderUtil.h
//  JiZhiSDK
//
//  Created by 王振 DemoKing on 2016/11/30.
//  Copyright © 2016年 DemoKing. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SendOrderModel.h"
@interface HLOrderUtil : NSObject

/**
 4010 获取子索引选项

 @param array 置位数组
 @return string
 */
+ (NSString *)subIndex4010DataWithArray:(NSArray <NSNumber *>*)array;

/**
 重新构建需要发送的数据
 
 @param model model对象
 @return 新数据
 */
+ (NSData *)sendDataWithSendModel:(SendOrderModel *)model;

/**
 校验码
 
 @param data data
 @return cs
 */
+ (NSData *)cs:(NSData *)data;

/*
 * bref 求异或和
 */
+ (NSData *)xorWithData:(NSData *)data;

/*
 * bref 求空调编码 两个字节的值
 */
+ (NSString *)ariConditionerCodeWithCodeInteger:(NSInteger)codeInteger;

+(NSString *)silentCurtainCs:(NSString *)dataString;

@end
