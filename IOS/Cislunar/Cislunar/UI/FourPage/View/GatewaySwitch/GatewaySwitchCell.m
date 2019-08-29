//
//  GatewaySwitchCell.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "GatewaySwitchCell.h"

@implementation GatewaySwitchCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.labelGatewayName.textColor = kTitleLabelColor;
    self.labelDetail.textColor = kSubTitleLabelColor;
    self.imageVTopLine.backgroundColor = kLineColor;
    self.imageVBottomLine.backgroundColor = kLineColor;
    
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

-(void)UpdateCellWithState:(BOOL)select{
    self.btnSelected.selected = select;
    _isSelected = select;
}

@end
