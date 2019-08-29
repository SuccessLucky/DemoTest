//
//  DropDownTableViewCell.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/13.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "DropDownTableViewCell.h"

@implementation DropDownTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
}

- (void)setIsSelected:(BOOL)isSelected
{
    if (isSelected) {
        self.labelName.textColor = UIColorFromRGB(0xff5500);
        self.imageViewSelected.hidden = NO;
    }else{
        self.labelName.textColor = UIColorFromRGB(0x202026);
        self.imageViewSelected.hidden = YES;
    }
}

- (void)setIsCouldSelected:(BOOL)isCouldSelected
{
    self.btnTap.userInteractionEnabled = isCouldSelected;
    if (isCouldSelected) {
        self.btnTap.userInteractionEnabled = YES;
        self.labelName.textColor = UIColorFromRGB(0x202026);
    }else{
        
        self.btnTap.userInteractionEnabled = NO;
        self.labelName.textColor = [UIColor grayColor];
    }
    
}


@end
