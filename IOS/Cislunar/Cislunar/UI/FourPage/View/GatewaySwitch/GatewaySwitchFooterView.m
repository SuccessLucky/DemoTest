//
//  GatewaySwitchHeaderView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "GatewaySwitchFooterView.h"

@implementation GatewaySwitchFooterView

- (void)awakeFromNib
{
    [super awakeFromNib];
    self.imageUpLine.backgroundColor = kLineColor;
    self.iamgeDownLine.backgroundColor = kLineColor;
    //加手势
    
}


- (IBAction)btnAddAcountPressed:(UIButton *)sender {
    
    if (self.BlockAddAcountPressed) {
        self.BlockAddAcountPressed();
    }
}

@end
