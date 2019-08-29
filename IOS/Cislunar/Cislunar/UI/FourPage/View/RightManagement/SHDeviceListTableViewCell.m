//
//  SHDeviceListTableViewCell.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/9.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHDeviceListTableViewCell.h"

@implementation SHDeviceListTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    self.imageVTopLine.backgroundColor = kLineColor;
    self.imageVBottomLine.backgroundColor = kLineColor;
    
    self.labelName.textColor = kTitleLabelColor;
    self.labelAddr.textColor = kSubTitleLabelColor;
    self.btnSelected.hidden = NO;
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
