//
//  TVCollectionViewCell.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/14.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "InfraredCollectionViewCell.h"

#define kItemWidth (UI_SCREEN_WIDTH - 24 * 2 - 51 * 2)/3.00f

@implementation InfraredCollectionViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.btnItem.layer.masksToBounds = YES;
    self.btnItem.layer.cornerRadius = kItemWidth/2.0f;
    self.btnItem.layer.borderWidth = 2;
    self.btnItem.layer.borderColor = [UIColor whiteColor].CGColor;
    
    UILongPressGestureRecognizer *longPressGesUp = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(longPressUp:)];
    
    longPressGesUp.minimumPressDuration = 1;
    
    [self addGestureRecognizer:longPressGesUp];
}

- (void)longPressUp:(UILongPressGestureRecognizer *)gestrue
{
    if (gestrue.state != UIGestureRecognizerStateBegan)
    {
        return;
    }
    if (self.BlockBtnLongPressed) {
        self.BlockBtnLongPressed();
    }
}

@end
