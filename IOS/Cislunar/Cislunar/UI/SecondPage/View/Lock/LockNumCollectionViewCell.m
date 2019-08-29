//
//  LockNumCollectionViewCell.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/16.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "LockNumCollectionViewCell.h"

#define kItemWidth (UI_SCREEN_WIDTH - 41 * 2 - 23 * 2)/3.00f

@implementation LockNumCollectionViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    [self.btnNum setBackgroundImage:[UIImage imageWithColor:[UIColor clearColor]] forState:UIControlStateNormal];
    [self.btnNum setBackgroundImage:[UIImage imageWithColor:UIColorFromRGB(0x8bbfe)] forState:UIControlStateHighlighted];
    self.btnNum.layer.cornerRadius = kItemWidth/2.0f;
    self.btnNum.layer.borderWidth = 1.0f;
    self.btnNum.layer.masksToBounds = YES;
    self.btnNum.layer.borderColor = [[UIColor whiteColor]CGColor];
    
}

- (IBAction)btnNumPressed:(UIButton *)sender {
    
    if (self.BlockBtnNumPressed) {
        self.BlockBtnNumPressed();
    }
}

@end
