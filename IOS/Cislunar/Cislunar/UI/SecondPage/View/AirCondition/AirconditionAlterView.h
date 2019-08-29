//
//  AirconditionAlterView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/16.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AirConditionBtnModel.h"

@interface AirconditionAlterView : UIView

@property (copy, nonatomic) void(^BlockGetTypeCompleteHandle)(AirConditionBtnModel *model);

- (instancetype)initWithFrame:(CGRect)frame arrList:(NSArray *)arrList title:(NSString *)title;

- (void)dismiss;

- (void)show;

@end
