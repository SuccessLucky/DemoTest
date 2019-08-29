//
//  DeviceCVCell.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/19.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "DeviceCVCell.h"
@interface DeviceCVCell()

@property (weak, nonatomic) IBOutlet UIButton *btnDeviceImage;
@property (weak, nonatomic) IBOutlet UILabel *labelDeviceName;
@property (weak, nonatomic) IBOutlet UILabel *labelRegion;
@property (weak, nonatomic) IBOutlet UILabel *labelState;
@property (weak, nonatomic) IBOutlet UIImageView *imageBg;

@end

@implementation DeviceCVCell

- (void)awakeFromNib {
    [super awakeFromNib];
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

- (void)layoutSubviews {
    [super layoutSubviews];
    
    self.layer.cornerRadius = 5;
    self.layer.masksToBounds = YES;
    self.layer.borderWidth = 1;
    self.layer.borderColor = RGB(36, 68, 117).CGColor;
}

- (void)setStrDeviceNormalImage:(NSString *)strDeviceNormalImage
{
    if (self.isCommonImage) {
        [self.btnDeviceImage setImage:[UIImage imageNamed:strDeviceNormalImage] forState:UIControlStateNormal];

    }else{
        [self.btnDeviceImage sd_setImageWithURL:[NSURL URLWithString:strDeviceNormalImage] forState:UIControlStateNormal];
    }
}

- (void)setStrDeviceSelectedImage:(NSString *)strDeviceSelectedImage
{
    if (self.isCommonImage) {
        [self.btnDeviceImage setImage:[UIImage imageNamed:strDeviceSelectedImage] forState:UIControlStateSelected];
    }else{
        [self.btnDeviceImage sd_setImageWithURL:[NSURL URLWithString:strDeviceSelectedImage] forState:UIControlStateSelected];
    }
    
}

- (void)setStrDeviceName:(NSString *)strDeviceName
{
    self.labelDeviceName.text = strDeviceName;
}

- (void)setStrDeviceState:(NSString *)strDeviceState
{
    self.labelState.text = strDeviceState;
}

-(void)setStrDeviceRegion:(NSString *)strDeviceRegion
{
    self.labelRegion.text = strDeviceRegion;
}

- (void)setStrNormalImageVBg:(NSString *)strNormalImageVBg
{
    self.imageBg.image = [UIImage imageNamed:strNormalImageVBg];
}

-(void)setStrSelctedImageVBg:(NSString *)strSelctedImageVBg
{
    self.imageBg.image = [UIImage imageNamed:strSelctedImageVBg];
}

- (void)setIsSelected:(BOOL)isSelected
{
    self.btnDeviceImage.selected = isSelected;
}

@end
