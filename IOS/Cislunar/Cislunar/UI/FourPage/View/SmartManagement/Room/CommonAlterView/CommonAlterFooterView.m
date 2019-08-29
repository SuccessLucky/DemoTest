//
//  CommonAlterFooterView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/26.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "CommonAlterFooterView.h"

@implementation CommonAlterFooterView

- (id)init {
    
    NSArray *temp = [[NSBundle  mainBundle] loadNibNamed:@"CommonAlterFooterView" owner:self options:nil];
    
    if (temp) {
        self = temp.firstObject;
        if (self) {
            self.imageViewLine.backgroundColor = kLineColor;
        }
        return self;
    }
    return nil;
}

- (IBAction)btnCancellPressed:(UIButton *)sender {
    if (self.blockBtnPressed) {
        self.blockBtnPressed(CommonAlterBtnType_Cancell);
    }
}

- (IBAction)btnOKPressed:(UIButton *)sender {
    if (self.blockBtnPressed) {
        self.blockBtnPressed(CommonAlterBtnType_OK);
    }
}
@end
