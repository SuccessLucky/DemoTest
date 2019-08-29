//
//  CommonDefine.h
//  GJJUser
//
//  Created by LiuYang on 15/1/4.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#ifndef GJJUser_UICommonDefine_h
#define GJJUser_UICommonDefine_h

/**
 *  设备宽度
 */
#define UI_SCREEN_WIDTH ([[UIScreen mainScreen] bounds].size.width)
/**
 *  设备高度
 */
#define UI_SCREEN_HEIGHT  ([[UIScreen mainScreen] bounds].size.height)

/**
 *  设定颜色值
 */
//#define RGBA(r, g, b, a)    [UIColor colorWithRed:(CGFloat)((r)/255.0) green:(CGFloat)((g)/255.0) blue:(CGFloat)((b)/255.0) alpha:(CGFloat)(a)]
//
//#define RGB(r, g, b)        RGBA(r, g, b, 1.0)

#define UIColorFromRGB(rgbValue) [UIColor colorWithRed:((float)((rgbValue & 0xFF0000) >> 16))/255.0 green:((float)((rgbValue & 0xFF00) >> 8))/255.0 blue:((float)(rgbValue & 0xFF))/255.0 alpha:1.0]

//设置大小
#define HELVETICANEUEMEDIUM_FONT(f) [UIFont systemFontOfSize:(f)]

/**
 *  获取tabbarViewController
 */
#define GET_TBARVC [[UIApplication sharedApplication].delegate window].rootViewController;

/**
 *  获取倍率 以5s为标准
 */
#define D_RateX (UI_SCREEN_WIDTH > 320 ? UI_SCREEN_WIDTH/320.0f : 1.0)
#define D_RateY (UI_SCREEN_HEIGHT > 568 ? UI_SCREEN_HEIGHT/568.0f : 1.0)

//获取网关Zigbee的MacAddress
#define kGatewayZigbeeMacAddr 


#endif
