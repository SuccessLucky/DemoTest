//
//  DeviceListFooterView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/27.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import "DeviceListFooterView.h"

@implementation DeviceListFooterView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (id)init
{
    NSArray *tempArr = [[NSBundle mainBundle] loadNibNamed:@"DeviceListFooterView"
                                                     owner:self
                                                   options:nil];
    if (tempArr) {
        self = tempArr[0];
        if (self) {
//         self.backgroundColor = [UIColor brownColor];
        }
        return self;
    }
    return nil;
}

//- (instancetype)initWithFrame:(CGRect)frame
//{
//    self =[super initWithFrame:frame];
//    NSArray *tempArr = [[NSBundle mainBundle] loadNibNamed:@"DeviceListFooterView"
//                                                     owner:self
//                                                   options:nil];
//    if (tempArr) {
//        self = tempArr[0];
//        if (self) {
//            self.frame = frame;
//        }
//        return self;
//    }
//    return nil;
//
//}


- (IBAction)btnAllSelected:(UIButton *)sender {
    
    if (self.blockAllPressed) {
        self.blockAllPressed(sender);
    }
}

@end
