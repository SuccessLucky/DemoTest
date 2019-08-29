//
//  ScreenDeviceListTableViewCell.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/29.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ScreenDeviceListTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UIButton *btnIcon;
@property (weak, nonatomic) IBOutlet UILabel *labelName;
@property (weak, nonatomic) IBOutlet UILabel *labelAddr;
@property (weak, nonatomic) IBOutlet UILabel *labelState;
@property (weak, nonatomic) IBOutlet UIImageView *imageTopLine;
@property (weak, nonatomic) IBOutlet UIImageView *imageBottomLine;

@property (weak, nonatomic) IBOutlet UIImageView *imageArrow;
@property (weak, nonatomic) IBOutlet UISwitch *switchOn;

@property (copy, nonatomic) void(^didGetStatueOn)(BOOL isON);

@end
