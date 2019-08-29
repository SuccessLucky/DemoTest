//
//  UIButton+GButtonImageWithLable.m
//  GJJUser
//
//  Created by LiuYang on 15/1/9.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#import "UIButton+GButtonImageWithLable.h"

@implementation UIButton (GButtonImageWithLable)

- (void) setImage:(UIImage *)image withTitle:(NSString *)title forState:(UIControlState)stateType {
    //UIEdgeInsetsMake(CGFloat top, CGFloat left, CGFloat bottom, CGFloat right)
    UIFont *font = [UIFont systemFontOfSize:16];
    [self.imageView setContentMode:UIViewContentModeScaleToFill];
    [self setImageEdgeInsets:UIEdgeInsetsMake(0.0,
                                              -25.0,
                                              0.0,
                                              0.0)];
    [self setImage:image forState:stateType];
    
    [self.titleLabel setContentMode:UIViewContentModeCenter];
    [self.titleLabel setBackgroundColor:[UIColor clearColor]];
    [self.titleLabel setFont:font];
    [self.titleLabel setTextColor:[UIColor whiteColor]];
    [self setTitleEdgeInsets:UIEdgeInsetsMake(0.0,
                                              0.0,
                                              0.0,
                                              10.0)];
    [self setTitle:title forState:stateType];
}

- (void) setImage:(UIImage *)image withTitleBottom:(NSString *)title forState:(UIControlState)stateType {
    //UIEdgeInsetsMake(CGFloat top, CGFloat left, CGFloat bottom, CGFloat right)
    CGSize titleSize = [UICommon getLabelCGSize:title];
    [self.imageView setContentMode:UIViewContentModeCenter];
    [self setImageEdgeInsets:UIEdgeInsetsMake(-8.0,
                                              0.0,
                                              0.0,
                                              -titleSize.width)];
    [self setImage:image forState:stateType];
    
    [self.titleLabel setContentMode:UIViewContentModeCenter];
    [self.titleLabel setBackgroundColor:[UIColor clearColor]];
    [self.titleLabel setFont:HELVETICANEUEMEDIUM_FONT(12)];
    [self.titleLabel setTextColor:[UIColor whiteColor]];
    [self setTitleEdgeInsets:UIEdgeInsetsMake(30.0,
                                              -image.size.width,
                                              0.0,
                                              0.0)];
    [self setTitle:title forState:stateType];
}
@end
