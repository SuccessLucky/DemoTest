//
//  GCommon.h
//  GJJUser
//
//  Created by LiuYang on 15/1/9.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ToolCommonDefine.h"
#import "Reachability.h"

typedef NS_ENUM(NSUInteger,DeviceInfo) {
    DeviceInfo_Name      = 1,       //设备名称
    DeviceInfo_Model  = 2,          //设备模式
    DeviceInfo_LocalModel   = 3,    //本地设备模式
    DeviceInfo_SysName   = 4,       //系统名称
    DeviceInfo_SysVersion  = 5,     //系统版本号
    DeviceInfo_Language   = 6,      //语言
    DeviceInfo_UIID   = 7,          //设备识别码
    DeviceInfo_Country   = 8,       //国家
};

/**
 *  网络状态
 */
typedef NS_ENUM(NSUInteger, GNetworkStatus){
    /**
     *  无网络
     */
    GNetworkStatusNotReachable = 0,
    /**
     *  Wi-Fi
     */
    GNetworkStatusWiFi = 1,
    /**
     *  Wi-Fi
     */
    GNetworkStatusWWAN = 2,
};

@interface ToolCommon : NSObject


/**
 *  @brief 获取app版本号
 */

+ (NSString *)getTheVsion;

/**
 *  @brief 获取app版本号
 */
+ (NSString *)getTheAppBuildVsion;

/**
 *  检查是否是手机号码
 *
 *  @param mobileNum 号码
 *
 *  @return
 */
+ (BOOL)isMobileNumber:(NSString *)mobileNum;
/**
 *  检查是否都是数字
 *
 *  @param mobileNum mobileNum 号码
 *
 *  @return
 */
+ (BOOL)isAllNumber:(NSString *)mobileNum;
/**
 *  检查是否是邮箱
 *
 *  @param userId 邮箱地址
 *
 *  @return
 */
+ (BOOL)isValidateEmail:(NSString *)userId;

/**
 *  判断运营商
 *
 *  @return 运营商
 */
+ (NSString *)checkNetworkType;

/**
 *  判断网络大致类型
 *
 *  @return 网络类型
 */
+ (NSString *)checkChinaMobile;

/**
 *  获取当前网络状态
 */
+ (NetworkStatus)getReachabilityStatus;

/**
 *  获取当前网络状态
 */
+ (GNetworkStatus)getNetworkStatus;

/**
 *  判断当前网络是否连接
 */
+ (BOOL)isNetworkReachable;

/**
 *  判断当前网络是否是wifi环境
 */
+ (BOOL)isReachableViaWiFi;

/**
 *  判断当前网络是否是手机自带网络环境
 */
+(BOOL)isReachableWWAN;

/**
 *  判断当前网络是否不可连接
 */
+ (BOOL)isNotReachable;

/**
 * MD5
 */
+ (NSString *)MD5FromString:(NSString *)str;


+ (NSString *)MD5FromData:(NSData *)data;

/**
 *  获取当前Unix 时间戳
 */
+ (NSString *)getUnixTimesSp;

/**
 *  unix时间戳转换为时间  年月日时分秒
 */
+ (NSString *)exchangeUnixTimeSpToTime:(NSTimeInterval)timeSp;

/**
 *  unix时间戳转换为时间  年月日格式
 */
+ (NSString *)handleGetYearAndMonthAndDay:(NSTimeInterval)timeSp;


/**
 *  获取设备信息
 */
+ (NSString *)handleGetDeviceInfo;

/**
 .首先获取当前日期 [NSDate date]；
 .调用 initWithTimeIntervalSinceNow方法来推移时间，这个方法是传入推移时间的秒数，
  所以，其实你只需要把所推移的时间换成秒数即可，下面是我封装的方法：

 @param n 获取n天后的日期（n为负数表示往前）

 @return 日期字符串
 */
+ (NSString *)doGetNDay:(NSInteger)n;

+ (NSDate *)doGetDateNDay:(NSInteger)n;



/**
 //字典转字符串

 @param dic json dict

 @return 字符串
 */
+ (NSString*)dictionaryToJson:(NSDictionary *)dic;
/**
 字符串转字典

 @param jsonString jsonString description

 @return dict
 */
+ (NSDictionary *)dictionaryWithJsonString:(NSString *)jsonString;


//获取wifi名字
+ (NSString *)ssid;

//获取设备的UUID
- (NSString *)generateUUID;


@end
