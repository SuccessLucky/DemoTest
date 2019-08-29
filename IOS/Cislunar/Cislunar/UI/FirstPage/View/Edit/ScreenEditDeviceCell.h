//
//  ScreenEditDeviceCell.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/24.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ScreenEditDeviceCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *imageViewBg;
@property (weak, nonatomic) IBOutlet UIImageView *imageViewIcon;
@property (weak, nonatomic) IBOutlet UILabel *labelDeviceName;
@property (weak, nonatomic) IBOutlet UILabel *labelLocation;
@property (weak, nonatomic) IBOutlet UILabel *labelDetail;
@property (weak, nonatomic) IBOutlet UISwitch *switchDevice;
@property (weak, nonatomic) IBOutlet UIImageView *imageViewTopLine;
@property (copy, nonatomic) void(^blockDeviceSwitchPressed)(UISwitch *switchTr);

//- (void)setCornerRadius:(CGFloat)radius withShadow:(BOOL)shadow withOpacity:(CGFloat)opacity;

@end
