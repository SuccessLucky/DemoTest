//
//  AirConditionAlterCellTableViewCell.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/15.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "AirConditionAlterCellTableViewCell.h"

@implementation AirConditionAlterCellTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.LabelName.textColor = kTitleLabelColor;
    self.viewLine.backgroundColor = kLineColor;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
