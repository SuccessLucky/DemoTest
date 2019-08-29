//
//  UIView+WZLBadge.h
//  WZLBadgeDemo
//
//  Created by zilin_weng on 15/6/24.
//  Copyright (c) 2015年 Weng-Zilin. All rights reserved.
//  Project description: this is a solution to enable any UIView to display badge

#import <UIKit/UIKit.h>
#import "WZLBadgeProtocol.h"

#pragma mark -- badge apis

@interface UIView (WZLBadge)<WZLBadgeProtocol>


/**
 *  show badge with red dot style and WBadgeAnimTypeNone by default.
 */
- (void)showBadge;

/**
 *  showBadge
 *
 *  @param style WBadgeStyle type
 *  @param value (if 'style' is WBadgeStyleRedDot or WBadgeStyleNew,
        this value will be ignored. In this case, any value will be ok.)
*   @param aniType
 */
- (void)showBadgeWithStyle:(WBadgeStyle)style
                     value:(NSInteger)value
             animationType:(WBadgeAnimType)aniType;

/*
 wBadgeStyle default is WBadgeStyleNumber
 */
- (void)showNumberBadgeWithValue:(NSInteger)value
              animationType:(WBadgeAnimType)aniType;

// wBadgeStyle default is WBadgeStyleNumber ;
// WBadgeAnimType defualt is  WBadgeAnimTypeNone
- (void)showNumberBadgeWithValue:(NSInteger)value;

/**
 *  clear badge(hide badge)
 */
- (void)clearBadge;

/**
 *  make bage(if existing) not hiden
 */
- (void)resumeBadge;



//新增加
- (void)showBadgeWithStyleText:(WBadgeStyle)style
                     text:(NSString *)text
             animationType:(WBadgeAnimType)aniType;

@end
