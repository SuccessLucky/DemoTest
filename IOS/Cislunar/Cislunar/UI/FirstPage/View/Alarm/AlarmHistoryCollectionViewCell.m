//
//  AlarmHistoryCollectionViewCell.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/11.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "AlarmHistoryCollectionViewCell.h"

@implementation AlarmHistoryCollectionViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.btnTitle.layer.masksToBounds = YES;
    self.btnTitle.layer.cornerRadius = (UI_SCREEN_WIDTH -15*2 - 15*5)/10.00;
    self.btnTitle.layer.borderColor = [UIColor whiteColor].CGColor;
    self.btnTitle.layer.borderWidth = 1;
    
//    self.backgroundColor = [UIColor clearColor];
//    self.btnTitle.titleLabel.font

    [self.btnTitle setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    // Initialization code
}

@end
