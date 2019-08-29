//
//  HLDataUtil.h
//  JiZhiSDK
//
//  Created by 王振 DemoKing on 2016/11/29.
//  Copyright © 2016年 DemoKing. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NSData+HLData.h"
@interface HLDataUtil : NSObject

// 十六进制字符串转换为普通字符串
+ (NSString *)stringFromHexString:(NSString *)hexString;


/**
 Int转16进制字符串

 @param tmpid int值
 @return 16进制字符串
 */
//+ (NSString *)ToHex:(long long int)tmpid;
+  (NSString *)getHexByDecimal:(NSDecimalNumber *)decimal;

/**
 Data 转 16进制字符串
 
 @param data data数据
 @return 翻译成的字符串
 */
+ (NSString *)stringWithData:(NSData *)data;

/**
 data 转 byte数组
 
 @param data data
 @return byte数组
 */
+ (Byte *)byteWithData:(NSData *)data;

/**
 data截取
 
 @param data data
 @param location 截取位置
 @param length 截取长度
 @return data
 */
+ (NSData *)dataWithData:(NSData *)data location:(NSInteger)location length:(NSInteger)length;

/**
 data 通过 UTF_8 转字符串
 
 @param data data
 @return string
 */
+ (NSString *)UTF8StringWithData:(NSData *)data;

/**
 16进制字符串 字节 转 data

 @param hexString hexString
 @return data
 */
+ (NSData *)dataWithHexString:(NSString *)hexString;
+ (int)valueWithSingleHexString:(NSString *)string;
/**
 16进制字符串 转 int 值

 @param hexString 0faa 等
 @return int
 */
+ (int)valueWithHexString:(NSString *)hexString;

/**
 data 转 int

 @param data data
 @return int
 */
+ (int)valueWithData:(NSData *)data;

+(NSString *)getBinaryByhex:(NSString *)hex;

//二进制转10进制
+ (NSInteger)getDecimalByBinary:(NSString *)binary;

//将十六进制的字符串转换成NSString则可使用如下方式:
+ (NSString *)convertHexToString:(NSString *)str;

//+(NSString *)stringFromHexString:(NSString *)hexString ;
@end
