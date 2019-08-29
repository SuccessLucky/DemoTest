//
//  HIKLoadPercentView.h
//  VideoGo
//  带有百分比的加载动画视图
//  Created by zhil.shi on 15/3/9.
//  Copyright (c) 2015年 HIKVison. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HIKLoadPercentView : UIView

@property (strong, nonatomic, readonly) NSString *percentStr;
/**
 *  初始化函数
 *
 *  @param frame      视图大小
 *  @param percentStr 初始值，可为nil
 *
 *  @return self
 */
- (instancetype)initWithFrame:(CGRect)frame percentStr:(NSString *)percentStr;

/**
 *  初始化函数
 *
 *  @param frame      视图大小
 *  @param percentStr 初始值，可为nil
 *  @param color      字体颜色
 *
 *  @return self
 */
- (instancetype)initWithFrame:(CGRect)frame percentStr:(NSString *)percentStr percentColor:(UIColor *)color;

/**
 *  显示提示
 *
 *  @param content 提示
 */
- (void)setShowContent:(NSString *)content;

/**
 *  开始百分比动画
 */
- (void)showPercentAnimation;

/**
 *  隐藏并移除动画
 */
- (void)hideRemovePercentAnimation;

@end
