//
//  SmartEquipmentBannerView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/12.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SmartEquipmentBannerView.h"
#import "UIButton+ImageTitleStyle.h"

@interface SmartEquipmentBannerView ()


@end

@implementation SmartEquipmentBannerView

- (void)awakeFromNib
{
    [super awakeFromNib];
    [self.btnBack setEnlargeEdgeWithTop:20 right:20 bottom:20 left:20];
    [self.btnTitle setButtonImageTitleStyle:ButtonImageTitleStyleRight padding:10.0f];
}

- (IBAction)btnTitlePressed:(UIButton *)sender {
    sender.selected = !sender.selected;
    if (self.BlockBtnTitlePressed) {
        self.BlockBtnTitlePressed(self.btnTitle.titleLabel.text,sender);
    }
}

- (IBAction)btnAddPressed:(UIButton *)sender {
    if (self.BlockBtnAddPressed) {
        self.BlockBtnAddPressed(nil,sender);
    }
}

- (IBAction)btnBackPressed:(UIButton *)sender {
    if (self.BlockBtnBackPressed) {
        self.BlockBtnBackPressed(nil,sender);
    }
}
@end
