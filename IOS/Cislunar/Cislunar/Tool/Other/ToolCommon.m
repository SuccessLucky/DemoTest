//
//  GCommon.m
//  GJJUser
//
//  Created by LiuYang on 15/1/9.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#import "ToolCommon.h"
#import <CommonCrypto/CommonDigest.h>
#import<SystemConfiguration/CaptiveNetwork.h>

@implementation ToolCommon


+ (NSString *)getTheVsion
{
    NSDictionary *infoDictionary = [[NSBundle mainBundle] infoDictionary];
    NSString *app_Version = [infoDictionary objectForKey:@"CFBundleShortVersionString"];
    return app_Version;
}

+ (NSString *)getTheAppBuildVsion
{
    NSDictionary *infoDictionary = [[NSBundle mainBundle] infoDictionary];
    NSString *app_Build_Version = [infoDictionary objectForKey:@"CFBundleVersion"];
    return app_Build_Version;
}

//判断邮箱账号是否输入正确
+ (BOOL)isValidateEmail:(NSString *)userId {
    NSString *emailRegex = @"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
    return [emailTest evaluateWithObject:userId];
}

//检测是否是手机号码
+ (BOOL)isMobileNumber:(NSString *)mobileNum
{
    /**
     * 手机号码
     * 移动：134[0-8],135,136,137,138,139,150,151,157,158,159,182,187,188
     * 联通：130,131,132,152,155,156,185,186
     * 电信：133,1349,153,180,189
     */
    NSString * MOBILE = @"^1(3[0-9]|5[0-35-9]|8[025-9])\\d{8}$";
    /**
     10         * 中国移动：China Mobile
     11         * 134[0-8],135,136,137,138,139,150,151,157,158,159,182,187,188
     12         */
    NSString * CM = @"^1(34[0-8]|(3[5-9]|5[017-9]|8[278])\\d)\\d{7}$";
    /**
     15         * 中国联通：China Unicom
     16         * 130,131,132,152,155,156,185,186
     17         */
    NSString * CU = @"^1(3[0-2]|5[256]|8[56])\\d{8}$";
    /**
     20         * 中国电信：China Telecom
     21         * 133,1349,153,180,189
     22         */
    NSString * CT = @"^1((33|53|8[09])[0-9]|349)\\d{7}$";
    NSString * CTO  = @"^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|7[06-8])\\d{8}$";
    /**
     25         * 大陆地区固话及小灵通
     26         * 区号：010,020,021,022,023,024,025,027,028,029
     27         * 号码：七位或八位
     28         */
    // NSString * PHS = @"^0(10|2[0-5789]|\\d{3})\\d{7,8}$";
    
    NSPredicate *regextestmobile = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", MOBILE];
    NSPredicate *regextestcm = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", CM];
    NSPredicate *regextestcu = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", CU];
    NSPredicate *regextestct = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", CT];
    NSPredicate *regextestcto = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", CTO];
    
    if (([regextestmobile evaluateWithObject:mobileNum] == YES)
        || ([regextestcm evaluateWithObject:mobileNum] == YES)
        || ([regextestct evaluateWithObject:mobileNum] == YES)
        || ([regextestcu evaluateWithObject:mobileNum] == YES)
        || ([regextestcto evaluateWithObject:mobileNum] == YES))
    {
        return YES;
    }else{
        
        return NO;
        
    }
}

+ (BOOL)isAllNumber:(NSString *)mobileNum
{
    if ([mobileNum stringByTrimmingCharactersInSet:[NSCharacterSet decimalDigitCharacterSet]].length > 0) {
        return NO;
    }
    return YES;
}

/**
 *  判断网络大致类型
 *
 *  @return 网络类型
 */
+ (NSString *)checkChinaMobile
{
    NSString * ret = nil;
    CTTelephonyNetworkInfo *info = [[CTTelephonyNetworkInfo alloc] init];
    CTCarrier *carrier = [info subscriberCellularProvider];
    if (carrier == nil) {
        return nil;
    }
    
    NSString *code = [carrier mobileNetworkCode];
    if (code == nil) {
        return nil;
    }
    
    if ([code isEqualToString:@"00"] || [code isEqualToString:@"02"] || [code isEqualToString:@"07"]) {
        
        //        NSLog(@"is 移动");
        ret = ChinaMobile;
    }
    else if([code isEqualToString:@"01"] || [code isEqualToString:@"06"] || [code isEqualToString:@"20"]){
        //        NSLog(@"is 联通");
        ret = ChinaUnicom;
    }else if ([code isEqualToString:@"03"] || [code isEqualToString:@"05"])
    {
        //        NSLog(@"is 电信");
        ret = ChinaTelecom;
    }
    else
    {
        //        NSLog(@"is 未知");
        //        ret = code;
    }
    
    return ret;
}

/**
 *  判断运营商
 *
 *  @return 运营商
 */
+ (NSString *)checkNetworkType
{
    NSString *strTypeTemp = nil;
    CTTelephonyNetworkInfo *telephonyInfo = [CTTelephonyNetworkInfo new];
    NSString *strType = telephonyInfo.currentRadioAccessTechnology;
    LLog([NSString stringWithFormat:@"New Radio Access Technology: %@", strType]);
    
    if([strType isEqualToString:CTRadioAccessTechnologyGPRS] || [strType isEqualToString:CTRadioAccessTechnologyGPRS])
    {
        //        NSLog(@"2G网络");
        strTypeTemp = @"2G";
    }
    else if ([strType isEqualToString:CTRadioAccessTechnologyWCDMA]
             || [strType isEqualToString:CTRadioAccessTechnologyHSDPA]
             || [strType isEqualToString:CTRadioAccessTechnologyHSUPA]
             || [strType isEqualToString:CTRadioAccessTechnologyCDMA1x]
             || [strType isEqualToString:CTRadioAccessTechnologyCDMAEVDORev0]
             || [strType isEqualToString:CTRadioAccessTechnologyCDMAEVDORevA]
             || [strType isEqualToString:CTRadioAccessTechnologyCDMAEVDORevB]
             || [strType isEqualToString:CTRadioAccessTechnologyeHRPD])
    {
        //        NSLog(@"3G网络");
        strTypeTemp = @"3G";
    }
    else if([strType isEqualToString:CTRadioAccessTechnologyLTE])
    {
        //        NSLog(@"4G网络");
        strTypeTemp = @"4G";
    } else {
        
        NSError* error = [NSError errorWithDomain:@"无法获取确切数据类型"
                                             code:0
                                         userInfo:@{NSLocalizedDescriptionKey: @"无法获取确切数据类型"}];
        LLog([error localizedDescription]);
    }
    
    return strTypeTemp;
}

+ (NetworkStatus)getReachabilityStatus
{
    return [[Reachability reachabilityForInternetConnection] currentReachabilityStatus];
}

+ (GNetworkStatus)getNetworkStatus
{
    GNetworkStatus netconnType;
    Reachability *reach = [Reachability reachabilityWithHostName:@"www.baidu.com"];
    [reach startNotifier];
    
    switch ([reach currentReachabilityStatus]) {
        case NotReachable:// 没有网络
        {
            
            netconnType =GNetworkStatusNotReachable;
        }
            break;
            
        case ReachableViaWiFi:// Wifi
        {
            netconnType = GNetworkStatusWiFi;
        }
            break;
            
        case ReachableViaWWAN:// 手机自带网络
        {
            netconnType = GNetworkStatusWWAN;
        }
            break;
            
        default:
            break;
    }
    
    return netconnType;
}

+ (BOOL)isNetworkReachable
{
    return [[Reachability reachabilityForInternetConnection] currentReachabilityStatus] != NotReachable;
}

+ (BOOL)isReachableViaWiFi
{
    return [[Reachability reachabilityForInternetConnection] isReachableViaWiFi];
}

+(BOOL)isReachableWWAN
{
    return [[Reachability reachabilityForInternetConnection] isReachableViaWWAN];
}

+ (BOOL)isNotReachable
{
    return [[Reachability reachabilityForInternetConnection] currentReachabilityStatus] == NotReachable;
}

#pragma mark - MD5

+ (NSString *)MD5FromString:(NSString *)str
{
    const char *cStr = [str UTF8String];
    unsigned char result[CC_MD5_DIGEST_LENGTH];
    CC_LONG len = (int)strlen(cStr);
    CC_MD5( cStr, len, result ); // This is the md5 call
    return [NSString stringWithFormat:
            @"%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x",
            result[0], result[1], result[2], result[3],
            result[4], result[5], result[6], result[7],
            result[8], result[9], result[10], result[11],
            result[12], result[13], result[14], result[15]
            ];
}

+ (NSString *)MD5FromData:(NSData *)data
{
    unsigned char result[CC_MD5_DIGEST_LENGTH];
    CC_LONG len = (int)data.length;
    CC_MD5( data.bytes, len, result ); // This is the md5 call
    return [NSString stringWithFormat:
            @"%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x",
            result[0], result[1], result[2], result[3],
            result[4], result[5], result[6], result[7],
            result[8], result[9], result[10], result[11],
            result[12], result[13], result[14], result[15]
            ];
}

#pragma mark -
#pragma mark - 获取UNIX 时间戳

+ (NSString *)getUnixTimesSp{
    NSDate *localDate = [NSDate date];
    NSString *timeSp = [NSString stringWithFormat:@"%ld", (long)[localDate timeIntervalSince1970]];
    //    NSLog(@"timeSp:%@",timeSp);
    return timeSp;
}

#pragma mark -
#pragma mark - UNIX time 转时间
+ (NSString *)exchangeUnixTimeSpToTime:(NSTimeInterval)timeSp
{
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"YYYY-MM-dd HH:mm:ss"];
    NSTimeZone* timeZone = [NSTimeZone timeZoneWithName:@"Asia/BeiJing"];
    [formatter setTimeZone:timeZone];
    
    NSDate *confromTimesp = [NSDate dateWithTimeIntervalSince1970:timeSp];
    NSString *confromTimespStr = [formatter stringFromDate:confromTimesp];
    return confromTimespStr;
}

+ (NSString *)handleGetYearAndMonthAndDay:(NSTimeInterval)timeSp
{
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"YYYY-MM-dd"];
    NSTimeZone* timeZone = [NSTimeZone timeZoneWithName:@"Asia/BeiJing"];
    [formatter setTimeZone:timeZone];
    
    NSDate *confromTimesp = [NSDate dateWithTimeIntervalSince1970:timeSp];
    NSString *confromTimespStr = [formatter stringFromDate:confromTimesp];
    return confromTimespStr;
}

#pragma mark - 获取设备信息
+ (NSString *)handleGetDeviceInfo
{
    NSString *strName = [[UIDevice currentDevice] name];
    //    NSLog(@"设备名称：%@", strName);//e.g. "My iPhone"
    
    NSString *strModel = [[UIDevice currentDevice] model];
    //    NSLog(@"设备模式：%@", strModel);// e.g. @"iPhone", @"iPod touch"
    
    NSString *strSysVersion = [[UIDevice currentDevice] systemVersion];
    //    NSLog(@"系统版本号：%@", strSysVersion);// e.g. @"4.0"
    
    NSString *strIdentifier = [UIDevice currentDevice].identifierForVendor.UUIDString;
    //    NSLog(@"设备UIID：%@",strIdentifier);
    
    CGRect rect_screen = [[UIScreen mainScreen]bounds];
    CGSize size_screen = rect_screen.size;
    CGFloat scale_screen = [UIScreen mainScreen].scale;
    //    NSLog(@"屏幕分辨率：%.f * %.f,%.f * %.f",size_screen.height,scale_screen,size_screen.width,scale_screen);
    
    NSString *deviceInfo = [NSString stringWithFormat:@"%@-%@ %@; %@; %.f*%.f",strName,strModel,strSysVersion,strIdentifier,size_screen.height*scale_screen,size_screen.width*scale_screen];
    
    
    return deviceInfo;
}

+ (NSString *)doGetNDay:(NSInteger)n{
    
    NSDate*nowDate = [NSDate date];
    
    NSDate* theDate;
    
    if(n!=0){
        
        NSTimeInterval  oneDay = 24*60*60*1;  //1天的长度
        theDate = [nowDate initWithTimeIntervalSinceNow: oneDay*n ];//initWithTimeIntervalSinceNow是从现在往前后推的秒数
        
    }else{
        
        theDate = nowDate;
    }
    
    NSDateFormatter *date_formatter = [[NSDateFormatter alloc] init];
    [date_formatter setDateFormat:@"yyyy/MM/dd"];
    NSString *the_date_str = [date_formatter stringFromDate:theDate];
    
    return the_date_str;
}

+ (NSDate *)doGetDateNDay:(NSInteger)n{
    
    NSDate*nowDate = [NSDate date];
    
    NSDate* theDate;
    
    if(n!=0){
        
        NSTimeInterval  oneDay = 24*60*60*1;  //1天的长度
        theDate = [nowDate initWithTimeIntervalSinceNow: oneDay*n ];//initWithTimeIntervalSinceNow是从现在往前后推的秒数
        
    }else{
        
        theDate = nowDate;
    }
    
//    NSDateFormatter *date_formatter = [[NSDateFormatter alloc] init];
//    [date_formatter setDateFormat:@"yyyy/MM/dd"];
//    NSString *the_date_str = [date_formatter stringFromDate:theDate];
    
    return theDate;
}

//字典转字符串
+ (NSString*)dictionaryToJson:(NSDictionary *)dic

{
    NSError *parseError = nil;
    
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dic options:NSJSONWritingPrettyPrinted error:&parseError];
    
    return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    
}

//字符串转字典
+ (NSDictionary *)dictionaryWithJsonString:(NSString *)jsonString
{
    if (jsonString == nil) {
        
        return nil;
        
    }
    NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    
    NSError *err;
    
    NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:jsonData
                         
                                                        options:NSJSONReadingMutableContainers
                         
                                                          error:&err];  
    
    if(err) {  
        
        NSLog(@"json解析失败：%@",err);  
        
        return nil;  
        
    }  
    
    return dic;  
}

+ (NSString *)ssid

{
    NSString *ssid = @"Not Found";
    CFArrayRef myArray = CNCopySupportedInterfaces();
    if (myArray != nil) {
        CFDictionaryRef myDict = CNCopyCurrentNetworkInfo(CFArrayGetValueAtIndex(myArray, 0));
        if (myDict != nil) {
            NSDictionary *dict = (NSDictionary*)CFBridgingRelease(myDict);
            ssid = [dict valueForKey:@"SSID"];
        }
    }
    return ssid;
}

#pragma mark -
#pragma mark UUID
- (NSString *)generateUUID
{
    // Create universally unique identifier (object)
    CFUUIDRef uuidObject = CFUUIDCreate(kCFAllocatorDefault);
    
    // Get the string representation of CFUUID object.
    NSString *uuidStr = (NSString *)CFBridgingRelease(CFUUIDCreateString(kCFAllocatorDefault, uuidObject));
    //    CFUUIDBytes bytes = CFUUIDGetUUIDBytes(uuidObject);
    
    CFRelease(uuidObject);
    
    return uuidStr;
}



@end
