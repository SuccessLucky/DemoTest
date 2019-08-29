//
//  DropDownListView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/13.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^BlockTapOnTheBackgroundView)();

@interface DropDownListView : UIView

@property (copy, nonatomic) void(^BlockGetObjectCompleteHandle)(id object);

@property (copy, nonatomic) void(^BlockTapOnTheBackgroundView)();

- (instancetype)initWithFrame:(CGRect)frame arrList:(NSArray *)arrList;

- (void)dismiss;

- (void)show;

@end
