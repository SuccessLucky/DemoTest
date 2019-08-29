//
//  AILampItemButton.h
//  SmartHome
//
//  Created by 艾泽鑫 on 16/3/14.
//  Copyright © 2016年 艾泽鑫. All rights reserved.
//  弹簧按钮

#import <UIKit/UIKit.h>

typedef enum{    //title的方向
    TitleDirection_right,
    TitleDirection_Top,
    TitleDirection_bottom,
    TitleDirection_left
}TitleDirections;
@interface AISpringButton : UIButton
/** 是否要最小动画*/
@property (nonatomic,assign,getter=isMinAnimation) BOOL minAnimation;
/** 弹簧动画*/
@property (nonatomic,assign,getter=isSpringAnimation) BOOL springAnimation;
/** 动画时间*/
@property (nonatomic,assign) CGFloat animationTime;
/** title相对于Image的方向*/
@property (nonatomic,assign) TitleDirections titleDirection;

/**
 *  设置正常图片和被选中图片
 *
 *  @param normalStr 正常图片名字
 *  @param selectStr 被选中图片名字
 */
-(void)setNormalImageName:(NSString*)normalStr andSelectedImageName:(NSString*)selectStr;
/**
 *  初始化的时候添加方法
 *
 *  @param action 方法
 *
 *  @return 按钮
 */
-(instancetype)initWithAction:(SEL)action;
/**
 *  设置正常和选中的title
 */
-(void)setTitle:(NSString*)title;
@end
