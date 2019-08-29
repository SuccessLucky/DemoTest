//
//  GatewayListCell.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/9/25.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "GatewayListCell.h"

@implementation GatewayListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    self.title.textColor = kTitleLabelColor;
    self.title.font = kTitleLabelFont;
    
    self.subTitle.textColor = kSubTitleLabelColor;
    self.subTitle.font = kDescriptionLabelFont;
    
    self.port.textColor = kSubTitleLabelColor;
    self.port.font = kDescriptionLabelFont;
    
    self.rightLabel.textColor = kCommonColor;
    self.rightLabel.font = kDetailLabelFont;
    
    self.bottomLine = [[UIView alloc]initWithFrame:CGRectMake(15, self.frame.size.height - 0.5, UI_SCREEN_WIDTH, 0.5)];
    self.bottomLine.backgroundColor = kCellSeparatorColorH;
    [self addSubview:self.bottomLine];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
