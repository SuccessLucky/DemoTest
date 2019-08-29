//
//  AILampItemButton.m
//  SmartHome
//
//  Created by 艾泽鑫 on 16/3/14.
//  Copyright © 2016年 艾泽鑫. All rights reserved.
//

#import "AISpringButton.h"
#import <objc/runtime.h>

////label和image的距离
//#define AITitleAndImageMargin 10

@interface AISpringButton ()

/** 原有的高亮 */
@property (assign,nonatomic)SEL normalHighlighted;
/** 动画高亮 */
@property (assign,nonatomic)SEL animationHighlighted;
/** 动画被选中 */
@property (assign,nonatomic)SEL normalSelected;
/** 动画被选中 */
@property (assign,nonatomic)SEL animationSelected;
/** 是否已经交换被选中方法*/
@property (assign,nonatomic,getter=isChangeSel)BOOL changeSel;
/** 是否已经交换高两方法*/
@property (assign,nonatomic,getter=isChangeHigh)BOOL changeHigh;

@end

@implementation AISpringButton

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        //动画高亮
        _animationHighlighted = @selector(AI_setAnimationHighlighted:);
        //正常高亮
        _normalHighlighted = @selector(setHighlighted:);
        //动画被选中
        _animationSelected = @selector(AI_setAnimationSelected:);
        //正常被选中
        _normalSelected = @selector(setSelected:);
        
        [self.titleLabel setTextAlignment:(NSTextAlignmentCenter)];
        [self setTitleColor:[UIColor blackColor] forState:(UIControlStateNormal)];
        [self setTitleColor:[UIColor blackColor] forState:(UIControlStateSelected)];
        self.animationTime = 0.5;
    }
    return self;
}

#pragma mark -----调节title和图片的方向
- (CGRect)titleRectForContentRect:(CGRect)contentRect{
    CGRect rect = contentRect;
    switch (self.titleDirection) {
        case TitleDirection_right:{
            CGFloat titleX = self.currentImage.size.width;
            CGFloat titleY = 0;
            CGFloat titleW = self.frame.size.width - self.currentImage.size.width;
            CGFloat titleH = self.frame.size.height;
            rect = CGRectMake(titleX, titleY, titleW, titleH);
            break;
        }
            
        case TitleDirection_left:{
            CGFloat titleX = 0;
            CGFloat titleY = 0;
            CGFloat titleW = self.frame.size.width - self.currentImage.size.width;
            CGFloat titleH = self.frame.size.height;
            rect = CGRectMake(titleX, titleY, titleW, titleH);
            break;
        }
        case TitleDirection_Top:{
            CGFloat titleX = 0;
            CGFloat titleY = 0;
            CGFloat titleW = self.frame.size.width;
            CGFloat titleH = self.frame.size.height - self.currentImage.size.height;
            rect = CGRectMake(titleX, titleY, titleW, titleH);
            break;
        }
        case TitleDirection_bottom:{
            CGFloat titleX = 0;
            CGFloat titleY = self.currentImage.size.height;
            CGFloat titleW = self.frame.size.width;
            CGFloat titleH = self.frame.size.height - self.currentImage.size.height;;
            rect = CGRectMake(titleX, titleY, titleW, titleH);
            break;
        }
        default:
            break;
    }
    return rect;
}
- (CGRect)imageRectForContentRect:(CGRect)contentRect{
    CGRect rect = contentRect;
    switch (self.titleDirection) {
        case TitleDirection_right:{
            CGFloat imageX = 0;
            CGFloat imageY = 0;
            CGFloat imageW = self.currentImage.size.width;
            CGFloat imageH = self.currentImage.size.height;
            rect = CGRectMake(imageX, imageY, imageW, imageH);
            break;
        }
            
        case TitleDirection_left:{
            CGFloat imageX = self.frame.size.width - self.currentImage.size.width;
            CGFloat imageY = 0;
            CGFloat imageW = self.currentImage.size.width;
            CGFloat imageH = self.currentImage.size.height;
            rect = CGRectMake(imageX, imageY, imageW, imageH);
            break;
        }
        case TitleDirection_Top:{
            CGFloat imageX = 0;
            CGFloat imageY = self.frame.size.width - self.currentImage.size.height;
            CGFloat imageW = self.currentImage.size.width;
            CGFloat imageH = self.currentImage.size.height;
            rect = CGRectMake(imageX, imageY, imageW, imageH);
            break;
        }
        case TitleDirection_bottom:{
            CGFloat imageX = 0;
            CGFloat imageY = 0;
            CGFloat imageW = self.currentImage.size.width;
            CGFloat imageH = self.currentImage.size.height;
            rect = CGRectMake(imageX, imageY, imageW, imageH);
            break;
        }
        default:
            break;
    }
    return rect;
    
}

#pragma mark ----公开方法
-(instancetype)initWithAction:(SEL)action{
    self = [super init];
    if (self) {
        [self addTarget:nil action:action forControlEvents:(UIControlEventTouchUpInside)];
    }
    return self;
}
-(void)setNormalImageName:(NSString*)normalStr andSelectedImageName:(NSString*)selectStr{
    [self setImage:[UIImage imageNamed:normalStr] forState:(UIControlStateNormal)];
    [self setImage:[UIImage imageNamed:selectStr] forState:(UIControlStateSelected)];
}

-(void)setTitle:(NSString*)title{
    [self setTitle:title forState:(UIControlStateNormal)];
    [self setTitle:title forState:(UIControlStateSelected)];
}
/**
 *  这只最小动画
 *
 *  @param minAnimation 是否有最小动画
 */
-(void)setMinAnimation:(BOOL)minAnimation{
    _minAnimation = minAnimation;
    _springAnimation = NO;
    if (minAnimation) {
        if (self.isChangeHigh) {
            return;
        }
        //切换为动画的高亮
        [self changeMethod:_normalHighlighted toMethod:_animationHighlighted];
        self.changeHigh = YES;
    }else{
        if (!self.isChangeHigh) {
            return;
        }
        //切换为正常高亮
        [self changeMethod:_animationHighlighted toMethod:_normalHighlighted];
        self.changeHigh = NO;
    }
}
/**
 *  设置只弹簧一次没有最小动画
 *
 *  @param springAnimation 是否只弹簧一次
 */
-(void)setSpringAnimation:(BOOL)springAnimation{
    _springAnimation = springAnimation;
    _minAnimation = NO;
    if (springAnimation) {
        if (self.isChangeSel) {
            return;
        }
        //切换为动画的被选中
        [self changeMethod:_normalSelected toMethod:_animationSelected];
        self.changeSel = YES;
    }else{
        if (!self.isChangeSel) {
            return;
        }
        [self changeMethod:_animationSelected toMethod:_normalSelected];
        self.changeSel = NO;
    }
}
#pragma mark  -----设置私有方法
/**
 *  带动画的高亮方法
 */
-(void)AI_setAnimationHighlighted:(BOOL)animationHighlighted{
    [self spring];
}

/**
 *  弹簧动画
 */
-(void)spring{
    [UIView animateWithDuration:self.animationTime animations:^{
        self.transform = CGAffineTransformMakeScale(1.1, 1.1);
        self.alpha = 0.3;
    } completion:^(BOOL finished) {
        self.alpha = 1;
    }];
    self.transform = CGAffineTransformIdentity;
}
/**
 *  带动画的被选中
 */
-(void)AI_setAnimationSelected:(BOOL)selected{
    [self spring];
}

#pragma mark   -----交换系统方法
/**
 *  切换方法从A切换到B
 */
-(void)changeMethod:(SEL)actionA toMethod:(SEL)actionB{

    //原有的高亮方法
    Method aMetod =   class_getInstanceMethod([AISpringButton class],actionA);
    //动画高亮方法
    Method bMetod = class_getInstanceMethod([AISpringButton class], actionB);
    BOOL isAdd = class_addMethod([AISpringButton class], actionA, method_getImplementation(bMetod), method_getTypeEncoding(bMetod));
    if (isAdd) {
        class_replaceMethod([AISpringButton class], actionB, method_getImplementation(aMetod), method_getTypeEncoding(aMetod));
    }else{
        method_exchangeImplementations(aMetod, bMetod);
    }
}

/********************************************************************/
/*                      title相对于image的方向                        */
/********************************************************************/
-(void)setTitleDirection:(TitleDirections)direction{
    _titleDirection = direction;
    switch (direction) {
        case TitleDirection_Top:
            
            break;
            
        default:
            break;
    }
}

@end
