//
//  CommonAlterHeaderView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/26.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "CommonAlterHeaderView.h"

@implementation CommonAlterHeaderView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (id)init
{
    NSArray *libs = [[NSBundle mainBundle] loadNibNamed:@"CommonAlterHeaderView" owner:self options:nil];
    if (libs) {
        self = libs.firstObject;
        if (self) {
            self.textFieldName.delegate =self;
            self.labelTitle.font = [UIFont boldSystemFontOfSize:16];
            self.textFieldName.layer.cornerRadius = 5;
            self.textFieldName.layer.borderColor = kLineColor.CGColor;
            self.textFieldName.layer.borderWidth = 0.5;
            self.backgroundColor = [UIColor whiteColor];
        }
        return self;
    }
    return nil;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self.textFieldName resignFirstResponder];
    return YES;
}


@end
