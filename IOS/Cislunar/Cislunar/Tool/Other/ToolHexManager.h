//
//  ToolHexManager.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/9/27.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ToolHexManager : NSObject

+ (id)sharedManager;

//不含“0x”的字符串转char
-(NSData*)stringToByte:(NSString*)string;

//空调的运行模式
- (NSArray *)arrAirConditonModel;
//风速
- (NSArray *)arrAirConditionWindSpeed;
//风向
- (NSArray *)arrAirConditionWindDirection;



#pragma mark - 1 二进制转十进制 -
- (NSInteger)toDecimalSystemWithBinarySystem:(NSString *)binary;

#pragma mark - 将16进制转化为二进制 -
-(NSString *)getBinaryByhex:(NSString *)hex;

#pragma mark - 1 将十进制转化为十六进制 -
- (NSString *)ToHex:(uint16_t)tmpid;


#pragma mark - 16进制字符串转 char数组 -
- (NSData *)temp16ToCharWithHEX:(NSString *)hexString;

#pragma mark - 二进制转16进制 -
- (NSString*)convertBin:(NSString *)bin;

#pragma mark - 将十进制转化为二进制,设置返回NSString 长度  -
- (NSString *)decimalTOBinary:(uint16_t)tmpid backLength:(int)length;


#pragma mark - btn转圆形
- (void)hanleBtnRound:(UIButton *)btn;

#pragma mark - 获取网管的macAddr
- (NSString *)fetchRouterSSIDInfo;

#pragma mark -  十六制字符串与NSData的相互转化
- (NSData *)convertHexStrToData:(NSString *)str;
- (NSString *)convertDataToHexStr:(NSData *)data;

#pragma mark - 十进制转16进制 后为两个字节
- (NSString *)converIntToHex:(int)iTemp;

#pragma mark - 16进制字符串转10进制
//-(int)doHandleGetDecimalByHexadecimal:(NSString*)tmpid;
- (NSInteger)numberWithHexString:(NSString *)hexString;

#pragma mark - 遍历字符串统一大写并每两个字符间加入一个空格

- (NSString *)doMakeUpperCaseAndAddSpace:(NSString*)originalString;

#pragma mark - 遍历字符串统一小写并且去掉之间的空格
- (NSString *)doMakeLowerCaseAndRemoveSpace:(NSString *)strOriginal;


#pragma mark - 普通字符串转16进制字符串和普通十六进制字符串转普通字符串
//将十六进制的字符串转换成NSString则可使用如下方式:
- (NSString *)convertHexStrToString:(NSString *)str;
//将NSString转换成十六进制的字符串则可使用如下方式:
- (NSString *)convertStringToHexStr:(NSString *)str;


#pragma mark - 普通字符串转16进制
- (NSString *)hexStringFromString:(NSString *)string;



@end
