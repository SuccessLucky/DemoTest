//
//  UICommon.h
//  GJJUser
//
//  Created by gjj on 15/1/21.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UICommonDefine.h"

typedef NS_ENUM(NSUInteger, DateFormatterType) {
    DateFormatterMDSimpleness        = 0,      //基本格式     5.10
    DateFormatterTimeAndMD           = 1,      //带时间或者月、日格式  5月10日
    DateFormatterMDTime              = 2,      //带时间格式    5月10日 上午10:00
    DateFormatterYMD                 = 3,      //带年的时间格式
    DateFormatterMD                  = 4,      //只有月、日格式
};


@interface UICommon : NSObject
/**
 *  根据内容得到Label的大小
 *
 *  @param info 内容
 *
 *  @return Label的大小
 */
+ (CGSize)getLabelCGSize:(id)info;
/**
 *  可配置的计算Laber大小
 *
 *  @param info  内容
 *  @param font  字体大小
 *  @param width 字体最大宽度
 *
 *  @return Label大小
 */
+ (CGSize)getLabelCGSize:(id)info withFont:(int)font withWidth:(int)width;

/**
 *  得到应用目录
 */
+ (NSString *)getContents;
/**
 *  将服务器的时间转换成本地时间   MM月dd日
 */
+ (NSString *)getFormatTime:(NSInteger)time formateType:(DateFormatterType)type;

/**
 *  得到视图的 Frame
 */
+ (CGRect)getViewFrame:(id)view;

/**
 *  NSData 转字符串
 */
+ (NSString *)changeDataToString:(NSData *)data;

@end
