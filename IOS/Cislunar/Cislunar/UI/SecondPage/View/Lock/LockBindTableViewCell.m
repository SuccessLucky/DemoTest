//
//  LockBindTableViewCell.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/16.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "LockBindTableViewCell.h"

@implementation LockBindTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.labelName.textColor = kTitleLabelColor;
    self.labelName.font = kTitleLabelFont;
    self.labelDetail.textColor = kSubTitleLabelColor;
    self.labelDetail.font = kDetailLabelFont;
    
    self.imageVTopLine.backgroundColor = kLineColor;
    self.imageVBottomLine.backgroundColor = kLineColor;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
