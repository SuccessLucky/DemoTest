//
//  GFirstPageHeaderView.m
//  GJJUser
//
//  Created by 余长涛 on 15/12/16.
//  Copyright © 2015年 过家家. All rights reserved.
//

#import "GFirstPageHeaderView.h"

@implementation GFirstPageHeaderView

- (id)init
{
    NSArray *tempArr = [[NSBundle mainBundle] loadNibNamed:@"GFirstPageHeaderView"
                                                     owner:self
                                                   options:nil];
    if (tempArr) {
        self = tempArr[0];
        if (self) {
            self.backgroundColor = kBackgroundGrayColor;
            self.viewHeader.backgroundColor = [UIColor whiteColor];
        }
        return self;
    }
    return nil;
}

- (IBAction)btnAddPressed:(UIButton *)sender {
    if (self.didPressAddBtn) {
        self.didPressAddBtn();
    }
}

@end
