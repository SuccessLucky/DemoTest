//
//  ScreenEditHeaderView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/23.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ScreenEditHeaderView : UIView

@property (weak, nonatomic) IBOutlet UIImageView *imageViewIcon;
@property (weak, nonatomic) IBOutlet UILabel *labelName;
@property (weak, nonatomic) IBOutlet UIButton *btnAdd;
@property (weak, nonatomic) IBOutlet UIImageView *imageViewHeaderBg;

@property (copy, nonatomic) void(^blockScreenEditHeaderViewAddDevicePressed)();

@end
