//
//  AirConditionAlterHeaderView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/15.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AirConditionAlterHeaderView : UIView

@property (copy, nonatomic) void(^BlockCancellPressed)();
@property (weak, nonatomic) IBOutlet UILabel *labelTitle;
@property (weak, nonatomic) IBOutlet UIView *viewLine;

@end
