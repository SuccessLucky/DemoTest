//
//  ScreenIconCollectionViewCell.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/9.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "ScreenIconCollectionViewCell.h"

@interface ScreenIconCollectionViewCell ()



@end

@implementation ScreenIconCollectionViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (IBAction)btnPressed:(UIButton *)sender {
    if (self.blockBtnIconPressed) {
        self.blockBtnIconPressed();
    }
}
@end
