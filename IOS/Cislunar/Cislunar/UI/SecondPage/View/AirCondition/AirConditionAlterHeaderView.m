//
//  AirConditionAlterHeaderView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/15.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "AirConditionAlterHeaderView.h"

@implementation AirConditionAlterHeaderView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
- (IBAction)btnCancellPressed:(UIButton *)sender {
    if (self.BlockCancellPressed) {
        self.BlockCancellPressed();
    }
    
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    self.labelTitle.textColor = kTitleLabelColor;
    self.viewLine.backgroundColor = kLineColor;
}

@end
