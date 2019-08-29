//
//  GBorderView.h
//  GJJUser
//
//  Created by yuchangtao on 15/1/22.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GBorderView : UIView

@property (assign) BOOL drawsTopLine;
@property (assign) BOOL drawsBottomLine;
@property (assign) BOOL drawsRightLine;
@property (assign) BOOL drawsLeftLine;
@property (strong, nonatomic) UIColor * lineColor;
@property (assign) CGFloat lineWith;

@end
