//
//  ZJCustomHud.m
//  ZJMsgListViewCtl
//
//  Created by zj on 2016/12/8.
//  Copyright © 2016年 zj. All rights reserved.
//

#import "ZJCustomHud.h"
@interface ZJCustomHud()

@end
@implementation ZJCustomHud
static ZJCustomHud * Hud = nil;

+(void)showWithText:(NSString*)text WithDurations:(CGFloat)duration
{
    //添加背景
    ZJCustomHud * custom = [[ZJCustomHud alloc]initWithFrame:[UIScreen mainScreen].bounds];
    [[UIApplication sharedApplication].keyWindow addSubview:custom];
    
    //添加提示框
    UILabel * label = [[UILabel alloc]initWithFrame:CGRectMake([UIScreen mainScreen].bounds.size.width/2-65, [UIScreen mainScreen].bounds.size.height/2-20, 130, 40)];
    label.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.8];
    label.text = text;
    label.textAlignment = NSTextAlignmentCenter;
    label.font = [UIFont boldSystemFontOfSize:14];
    label.textColor = [UIColor whiteColor];
    [custom addSubview:label];
    label.layer.masksToBounds=YES;
    label.layer.cornerRadius=10;
    
    
    //视图消失
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(duration * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [custom removeFromSuperview];
    });
    
    
}


+(void)showWithStatus:(NSString*)text
{
    ZJCustomHud * custom = [[ZJCustomHud alloc]initWithFrame:[UIScreen mainScreen].bounds];
    Hud=custom;
    [[UIApplication sharedApplication].keyWindow addSubview:custom];
    
    
    UIView * customView = [[UIView alloc]initWithFrame:CGRectMake([UIScreen mainScreen].bounds.size.width/2-75, [UIScreen mainScreen].bounds.size.height/2-50, 150, 120)];
    customView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.5];
    [custom addSubview:customView];
    customView.layer.masksToBounds = YES;
    customView.layer.cornerRadius=10;
    
    
    
    UIImageView *heartImageView = [[UIImageView alloc]initWithFrame:CGRectMake(customView.frame.size.width/2-50, 10,100, 80.0)];
    [customView addSubview:heartImageView];
    NSMutableArray *images = [[NSMutableArray alloc]initWithCapacity:7];
    for (int i=1; i<=5; i++)
    {
        [images addObject:[UIImage imageNamed:[NSString stringWithFormat:@"car%d.png",i]]];
    }
    heartImageView.animationImages = images;
    heartImageView.animationDuration = 0.4 ;
    heartImageView.animationRepeatCount = MAXFLOAT;
    [heartImageView startAnimating];
    
    UILabel * label = [[UILabel alloc]initWithFrame:CGRectMake(customView.frame.size.width/2-50, 80, 100, 40)];
    label.text = text;
    label.textAlignment = NSTextAlignmentCenter;
    label.font = [UIFont boldSystemFontOfSize:16];
    label.textColor = [UIColor whiteColor];
    [customView addSubview:label];
    
}


+(void)showWithSuccess:(NSString*)successString//成功提示
{
    ZJCustomHud * custom = [[ZJCustomHud alloc]initWithFrame:[UIScreen mainScreen].bounds];
    Hud=custom;
    [[UIApplication sharedApplication].keyWindow addSubview:custom];
    
    
    UIView * customView = [[UIView alloc]initWithFrame:CGRectMake([UIScreen mainScreen].bounds.size.width/2-75, [UIScreen mainScreen].bounds.size.height/2-50, 150, 100)];
    customView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.5];
    [custom addSubview:customView];
    customView.layer.masksToBounds = YES;
    customView.layer.cornerRadius=10;
    
    
    
    UIImageView *heartImageView = [[UIImageView alloc]initWithFrame:CGRectMake(customView.frame.size.width/2-20, 15,40, 40.0)];
    heartImageView.contentMode=1;
    [customView addSubview:heartImageView];
    heartImageView.image = [UIImage imageNamed:@"成功图片"];
    
    UILabel * label = [[UILabel alloc]initWithFrame:CGRectMake(customView.frame.size.width/2-50, 55, 100, 40)];
    label.text = successString;
    label.textAlignment = NSTextAlignmentCenter;
    label.font = [UIFont boldSystemFontOfSize:16];
    label.textColor = [UIColor whiteColor];
    [customView addSubview:label];
    
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [custom removeFromSuperview];
    });
}

//失败提示
+(void)showWithError:(NSString *)errorString
{
    ZJCustomHud * custom = [[ZJCustomHud alloc]initWithFrame:[UIScreen mainScreen].bounds];
    Hud=custom;
    [[UIApplication sharedApplication].keyWindow addSubview:custom];
    
    
    UIView * customView = [[UIView alloc]initWithFrame:CGRectMake([UIScreen mainScreen].bounds.size.width/2-75, [UIScreen mainScreen].bounds.size.height/2-50, 150, 100)];
    customView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.5];
    [custom addSubview:customView];
    customView.layer.masksToBounds = YES;
    customView.layer.cornerRadius=10;
    
    
    
    UIImageView *heartImageView = [[UIImageView alloc]initWithFrame:CGRectMake(customView.frame.size.width/2-20, 15,40, 40.0)];
    heartImageView.contentMode=1;
    [customView addSubview:heartImageView];
    heartImageView.image = [UIImage imageNamed:@"失败图片"];
    
    UILabel * label = [[UILabel alloc]initWithFrame:CGRectMake(customView.frame.size.width/2-50, 55, 100, 40)];
    label.text = errorString;
    label.textAlignment = NSTextAlignmentCenter;
    label.font = [UIFont boldSystemFontOfSize:16];
    label.textColor = [UIColor whiteColor];
    [customView addSubview:label];
    
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [custom removeFromSuperview];
    });
}

+(void)dismiss
{
    [Hud removeFromSuperview];
}



-(instancetype)initWithFrame:(CGRect)frame
{
    if (self=[super initWithFrame:frame])
    {
        self.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.2];
    }
    return self;
}


@end
