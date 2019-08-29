//
//  FloorTableViewCell.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "FloorTableViewCell.h"

@implementation FloorTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.btnHeader.layer.cornerRadius = 13.0f;
    self.btnHeader.layer.masksToBounds = YES;
    self.btnHeader.backgroundColor = UIColorFromRGB(0x7ecef4);
    
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
