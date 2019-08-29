//
//  CommonHeaderCollectionReusableView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/26.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "CommonHeaderCollectionReusableView.h"

@implementation CommonHeaderCollectionReusableView

- (void)awakeFromNib {
    [super awakeFromNib];
    self.textFieldTitle.layer.cornerRadius = 5;
    self.textFieldTitle.layer.borderColor = kLineColor.CGColor;
    self.textFieldTitle.layer.borderWidth = 0.5;
    self.backgroundColor = [UIColor whiteColor];
    // Initialization code
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self.textFieldTitle resignFirstResponder];
    return YES;
}

@end
