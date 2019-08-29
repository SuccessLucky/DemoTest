//
//  RoomCollectionViewCell.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "RoomCollectionViewCell.h"

@interface RoomCollectionViewCell ()

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottomLineViewHeightConstraint;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *rightLineViewWidthConstraint;

@end

@implementation RoomCollectionViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.bottomLineViewHeightConstraint.constant = 0.5;
    self.rightLineViewWidthConstraint.constant = 0.5;
    
    self.btnDelete.layer.masksToBounds = YES;
    self.btnDelete.layer.cornerRadius = 8.0f;
    self.btnDelete.backgroundColor = [UIColor redColor];
    [self.btnDelete setTitle:@"－" forState:UIControlStateNormal];
    
    UILongPressGestureRecognizer *longPressGesUp = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(longPressUp:)];
    
    longPressGesUp.minimumPressDuration = 1;
    
    [self addGestureRecognizer:longPressGesUp];
    
}
- (IBAction)btnDeletePressed:(UIButton *)sender {
    
    if (self.BlockBtnDeletePressed) {
        self.BlockBtnDeletePressed();
    }
}

- (IBAction)btnIconPressed:(UIButton *)sender {
    if (self.BlockBtnIconPressed) {
        self.BlockBtnIconPressed();
    }
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
