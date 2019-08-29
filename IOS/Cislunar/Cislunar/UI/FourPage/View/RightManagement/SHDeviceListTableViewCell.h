//
//  SHDeviceListTableViewCell.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/9.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SHDeviceListTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UIButton *btnIcon;
@property (weak, nonatomic) IBOutlet UILabel *labelName;
@property (weak, nonatomic) IBOutlet UILabel *labelAddr;
@property (weak, nonatomic) IBOutlet UIButton *btnSelected;
@property (weak, nonatomic) IBOutlet UIImageView *imageVTopLine;
@property (weak, nonatomic) IBOutlet UIImageView *imageVBottomLine;

@end
