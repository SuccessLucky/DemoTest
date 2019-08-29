//
//  ScreenDeviceListTableViewCell.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/29.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "ScreenDeviceListTableViewCell.h"

@implementation ScreenDeviceListTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    self.imageTopLine.backgroundColor = kLineColor;
    self.imageBottomLine.backgroundColor = kLineColor;
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)switchPressed:(UISwitch *)sender {
    if (sender.isOn) {
        if (self.didGetStatueOn) {
            self.didGetStatueOn(YES);
        }
    }else{
        if (self.didGetStatueOn) {
            self.didGetStatueOn(NO);
        }
    }
}
@end
