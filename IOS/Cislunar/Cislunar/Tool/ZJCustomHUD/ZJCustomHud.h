//
//  ZJCustomHud.h
//  ZJMsgListViewCtl
//
//  Created by zj on 2016/12/8.
//  Copyright © 2016年 zj. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ZJCustomHud : UIView
+(void)showWithText:(NSString*)text WithDurations:(CGFloat)duration;//仅限文字
+(void)showWithStatus:(NSString*)text;//加载视图
+(void)dismiss;
+(void)showWithSuccess:(NSString*)successString;//成功提示
+(void)showWithError:(NSString*)errorString;//失败提示


@end
