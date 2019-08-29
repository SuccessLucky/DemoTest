//
//  GCommonDefine.h
//  GJJUser
//
//  Created by yuchangtao on 15/1/15.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#define ChinaMobile     @"移动"
#define ChinaUnicom     @"联通"
#define ChinaTelecom    @"电信"

#define WarmPrompt      @"温馨提醒您"


/**
 *  判断版本
 */
#define iOS7 ([[[UIDevice currentDevice] systemVersion] floatValue] >= 7.0)
#define iOS8 ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0)
#define iOS11 ([[[UIDevice currentDevice] systemVersion] floatValue] >= 11.0)


/**
 *  获取当前版本号 和buldNum
 */
#define Major_Version [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"]
#define Build_Version [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"]


/**
 *  判断字符串
 */
#define gjj_string_or_empty(A) \
([A length] ? A : @"")\

#define gjj_string_or_empty1(A) \
([A length] ? A : @"")\

/**
 *  判断data数据
 */
#define gjj_data_or_empty(B) \
([B length] ? B : nil) \




