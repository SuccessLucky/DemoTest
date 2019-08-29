//
//  RightManagementCell.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "RightManagementCell.h"

@implementation RightManagementCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.labelName.textColor = kTitleLabelColor;
    self.labelAcount.textColor = kSubTitleLabelColor;
    
    self.imageViewLongLine.hidden = YES;
    self.imageViewLongLine.backgroundColor= kLineColor;
    
    self.imageViewShortLine.hidden = YES;
    self.imageViewShortLine.backgroundColor= kLineColor;
    
    self.btnIdentifer.layer.cornerRadius = 5;
    self.btnIdentifer.layer.borderColor = kCommonColor.CGColor;
    self.btnIdentifer.layer.borderWidth = 0.5;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
