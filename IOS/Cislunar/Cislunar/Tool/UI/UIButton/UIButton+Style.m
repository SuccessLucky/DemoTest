//
//  UIButton+Style.m
//  GJJManager
//
//  Created by yangpei on 15/5/7.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#import "UIButton+Style.h"

@implementation UIButton (Style)

#pragma mark - buttonWithToolStyle

+ (UIButton *)buttonWithToolStyle
{
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    [button setBackgroundColor:kCommonColor];
    [button initToolStyle];
    return button;
}

- (void)initToolStyle
{
    UIImage *normalBgImage = [UIImage imageWithColor:kCommonColor];
    [self setBackgroundImage:normalBgImage forState:UIControlStateNormal];
    
    UIImage *highlightBhImage = [UIImage imageWithColor:kCommonColor];
    [self setBackgroundImage:highlightBhImage forState:UIControlStateHighlighted];
    
    UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, 0.5)];
    line.backgroundColor = kLineColor;
    [self addSubview:line];
    
    [self setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    self.titleLabel.font = [UIFont systemFontOfSize:18];
}
@end
