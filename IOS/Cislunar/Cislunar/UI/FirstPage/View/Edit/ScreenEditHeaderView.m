//
//  ScreenEditHeaderView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/23.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import "ScreenEditHeaderView.h"

@implementation ScreenEditHeaderView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (id)init
{
    NSArray *tempArr = [[NSBundle mainBundle] loadNibNamed:@"ScreenEditHeaderView"
                                                     owner:self
                                                   options:nil];
    if (tempArr) {
        self = tempArr[0];
        if (self) {
//            self.backgroundColor = kBackgroundGrayColor;
        }
        return self;
    }
    return nil;
}

- (IBAction)btnAddPressed:(UIButton *)sender {
    
    if (self.blockScreenEditHeaderViewAddDevicePressed) {
        self.blockScreenEditHeaderViewAddDevicePressed();
    }
}

@end
