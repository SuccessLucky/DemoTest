//
//  UICommon.m
//  GJJUser
//
//  Created by gjj on 15/1/21.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#import "UICommon.h"

@interface UICommon ()
@end

@implementation UICommon

+ (CGSize)getLabelCGSize:(id)info
{
    NSMutableParagraphStyle * paragraphStyle = [[NSMutableParagraphStyle alloc] init];
    NSDictionary * attributes = @{NSFontAttributeName:[UIFont systemFontOfSize:17],
                                  NSParagraphStyleAttributeName:paragraphStyle};
    
    CGSize contentSize = [info boundingRectWithSize:CGSizeMake(UI_SCREEN_WIDTH, MAXFLOAT)
                                            options:(NSStringDrawingUsesLineFragmentOrigin|NSStringDrawingUsesFontLeading)
                                         attributes:attributes
                                            context:nil].size;
    return contentSize;
}

+ (CGSize)getLabelCGSize:(id)info withFont:(int)font withWidth:(int)width
{
    NSMutableParagraphStyle * paragraphStyle = [[NSMutableParagraphStyle alloc] init];
    NSDictionary * attributes = @{NSFontAttributeName:[UIFont systemFontOfSize:font],
                                  NSParagraphStyleAttributeName:paragraphStyle};
    
    CGSize contentSize = [info boundingRectWithSize:CGSizeMake(width, 9999)
                                            options:(NSStringDrawingUsesLineFragmentOrigin|
                                                     NSStringDrawingUsesFontLeading|
                                                     NSStringDrawingTruncatesLastVisibleLine)
                                         attributes:attributes
                                            context:nil].size;
    return contentSize;
}

+ (NSString *)getContents
{
    NSArray *array = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *path = [array lastObject];
    
    return path;
}

+ (NSString *)getUserContents
{
    NSFileManager *fm = [NSFileManager defaultManager];
    NSString *documentsPath = [UICommon getContents];
    NSArray *arrayPath = [fm contentsOfDirectoryAtPath:documentsPath error:nil];
    
    __block NSString *userContents ;
    [arrayPath enumerateObjectsUsingBlock:^(id obj, NSUInteger idx, BOOL *stop) {
        
        if ([obj isEqualToString:@""]) {
            userContents = obj;
        } else {
            
        }
        
    }];
    
    return userContents;
}

+ (NSString *)getFormatTime:(NSInteger)time formateType:(DateFormatterType)type
{
    NSDate *date = [NSDate dateWithTimeIntervalSince1970:time];
    NSCalendar *calendar = [NSCalendar currentCalendar];
    NSUInteger unitFlags = NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay|NSCalendarUnitHour|NSCalendarUnitMinute;
    NSDateComponents *compsForToday = [calendar components:unitFlags fromDate:[NSDate date]];
    NSDateComponents *compsForDate = [calendar components:unitFlags fromDate:date];
    
    NSString* text = nil;
    
    switch (type) {
        case DateFormatterMDSimpleness:
        {
            NSDateComponents *comps = [calendar components:unitFlags fromDate:date];
            text = [NSString stringWithFormat:@"%01ld.%01ld",(long)comps.month,(long)comps.day];
        }
            break;
        
        case DateFormatterTimeAndMD:
        {
            if (compsForToday.year == compsForDate.year && compsForToday.month == compsForDate.month && compsForToday.day == compsForDate.day)
            {
                NSDateComponents *comps = [calendar components:(NSCalendarUnitHour|NSCalendarUnitMinute) fromDate:date];
                text = [NSString stringWithFormat:@"%02ld:%02ld",(long)comps.hour,(long)comps.minute];
            }
            else if (compsForToday.year == compsForDate.year && compsForToday.month == compsForDate.month && compsForToday.day == compsForDate.day + 1)
            {
                text = @"昨天";
            }
            else {
                NSDateComponents *comps = [calendar components:unitFlags fromDate:date];
                text = [NSString stringWithFormat:@"%02ld月%02ld日",(long)comps.month,(long)comps.day];
            }
        }
            break;
        
        case DateFormatterYMD:
        {
            NSDateComponents *comps = [calendar components:unitFlags fromDate:date];
            text = [NSString stringWithFormat:@"%lu.%lu.%lu",(unsigned long)comps.year,(unsigned long)comps.month,(unsigned long)comps.day];
        }
            break;
        case DateFormatterMD:
        {
            NSDateComponents *comps = [calendar components:unitFlags fromDate:date];
            text = [NSString stringWithFormat:@"%02ld月%02ld日",(long)comps.month,(long)comps.day];
        }
        default:
            break;
    }
    return text;
}

+ (NSString *)getFormatTime:(NSInteger)time
{
    return [UICommon formatDay:[NSDate dateWithTimeIntervalSince1970:time] MD:NO andDateFormatType:DateFormatterMDSimpleness];
}

+ (NSString *)getFormatTimeMD:(NSInteger)time andDateFormatType:(DateFormatterType)type
{
    return [UICommon formatDay:[NSDate dateWithTimeIntervalSince1970:time] MD:YES andDateFormatType:type];
}

+ (NSString*)formatDay:(NSDate*)date MD:(BOOL)onlyMD andDateFormatType:(DateFormatterType)type
{
    NSCalendar *calendar = [NSCalendar currentCalendar];
    NSUInteger unitFlags = NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay|NSCalendarUnitHour|NSCalendarUnitMinute;
    NSDateComponents *compsForToday = [calendar components:unitFlags fromDate:[NSDate date]];
    NSDate *today = [calendar dateFromComponents:compsForToday];
    
    NSTimeInterval interval = [today timeIntervalSinceDate:date];
    
    NSString* text = nil;
    
    if (onlyMD)
    {
        NSDateComponents *comps = [calendar components:unitFlags fromDate:date];
        if (type == DateFormatterMDSimpleness) {
            text = [NSString stringWithFormat:@"%01ld.%01ld",(long)comps.month,(long)comps.day];
        } else if(type == DateFormatterMD) {
            text = [NSString stringWithFormat:@"%01ld月%01ld日",(long)comps.month,(long)comps.day];
        } else if (type == DateFormatterYMD) {
            text = [NSString stringWithFormat:@"%d.%01ld.%01ld",(int)comps.year,(long)comps.month,(long)comps.day];
        } else {
            if (comps.hour > 12) {
               text = [NSString stringWithFormat:@"%01ld月%01ld日下午%01ld:%01ld",(long)comps.month,(long)comps.day,(long)comps.hour,comps.minute];
            } else {
                text = [NSString stringWithFormat:@"%01ld月%01ld日上午%01ld:%01ld",(long)comps.month,(long)comps.day,(long)comps.hour,comps.minute];
            }
            
        }
    }
    else
    {
        if(interval <= 0 && interval > -60*60*24)
        {
            NSDateComponents *comps = [calendar components:(NSCalendarUnitHour|NSCalendarUnitMinute) fromDate:date];
            text = [NSString stringWithFormat:@"%02ld:%02ld",(long)comps.hour,(long)comps.minute];
        }
        else if (interval <= 60*60*24 && interval > 0)
        {
            text = @"昨天";
        }
        else if (interval <= 60*60*24*2 && interval > 60*60*24)
        {
            text = @"前天";
        }
        else {
            
            NSDateComponents *comps = [calendar components:unitFlags fromDate:date];
#if 1
            if (type == DateFormatterMDSimpleness) {
                text = [NSString stringWithFormat:@"%02ld.%02ld",(long)comps.month,(long)comps.day];
            } else {
                text = [NSString stringWithFormat:@"%02ld月%02ld日",(long)comps.month,(long)comps.day];
            }
            
#else
            if(compsForToday.year == comps.year)
            {
                text = [NSString stringWithFormat:@"%02d.%02d日",comps.month,comps.day];
            }
            else {
                text = [NSString stringWithFormat:@"%d年%d月%d日",comps.year,comps.month,comps.day];
            }
#endif
        }
    }
    
    return text;
}



+ (CGRect)getViewFrame:(id)view
{
    UIView *views = (UIView *)view;
    return views.frame;
}

+ (void)showAlertViewWithTilte:(NSString *)title message:(NSString *)message
{
    [UIAlertView bk_showAlertViewWithTitle:title message:message cancelButtonTitle:@"确定" otherButtonTitles:nil handler:nil];
}


+ (NSString *)changeDataToString:(NSData *)data
{
    NSString *strDataTemp = [data debugDescription];
    NSString *str1 = [strDataTemp substringWithRange:NSMakeRange(1, 8)];
    NSString *str2 = [strDataTemp substringWithRange:NSMakeRange(10, 8)];
    NSString *str = [NSString stringWithFormat:@"%@%@",str1,str2];
    return str;
}


@end
