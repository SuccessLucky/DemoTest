//
//  ScreenCollectionViewCell.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/10.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "ScreenCollectionViewCell.h"

@implementation ScreenCollectionViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    
    //    self.userInteractionEnabled = YES;
    //    self.btnImage.userInteractionEnabled = YES;
    //    self.labelTitle.userInteractionEnabled = YES;
    // Initialization code
    
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
