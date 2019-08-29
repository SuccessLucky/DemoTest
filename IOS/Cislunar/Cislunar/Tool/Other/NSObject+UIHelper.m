//
//  NSObject+UIHelper.m
//  GJJUser
//
//  Created by 杨沛 on 15/5/25.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#import "NSObject+UIHelper.h"

#define NSObjectUIHelperUIHeight        @"uihelper_ui_height"

@implementation NSObject (UIHelper)

- (CGFloat)ui_Height
{
    return [[self bk_associatedValueForKey:NSObjectUIHelperUIHeight] floatValue];
}

- (void)setUi_Height:(CGFloat)ui_Height
{
    [self bk_associateValue:@(ui_Height) withKey:NSObjectUIHelperUIHeight];
}
@end
