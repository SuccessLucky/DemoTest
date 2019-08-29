//
//  ScreenCVCell.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/18.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "ScreenCVCell.h"
@interface ScreenCVCell()

@property (weak, nonatomic) IBOutlet UIButton *btnItem;
@property (weak, nonatomic) IBOutlet UILabel *labelName;

@end

@implementation ScreenCVCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    self.btnItem.titleLabel.font = [UIFont systemFontOfSize:14];
    
    self.layer.cornerRadius = 5;
    self.layer.masksToBounds = YES;
    self.layer.borderWidth = 1;
    self.layer.borderColor = RGB(36, 68, 117).CGColor;
    
    UILongPressGestureRecognizer *longPressGesUp = [[UILongPressGestureRecognizer alloc] initWithTarget:self
                                                                                                 action:@selector(longPressUp:)];
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

- (void)setStrTitle:(NSString *)strTitle
{
    _strTitle = strTitle;
    self.labelName.text = strTitle;
}

- (void)setStrNormalImageName:(NSString *)strNormalImageName
{
    _strNormalImageName = strNormalImageName;
    
//    if (self.isCommonImage) {
//        [self.btnItem setImage:[UIImage imageNamed:strNormalImageName] forState:UIControlStateNormal];
//    }else{
////        [self.btnItem sd_setImageWithURL:[NSURL URLWithString:strNormalImageName] forState:UIControlStateNormal];
//
//        [self.btnItem sd_setImageWithURL:[NSURL URLWithString:strNormalImageName] forState:UIControlStateNormal placeholderImage:[UIImage imageNamed:@"更多"]];
//    }
    [self.btnItem sd_setImageWithURL:[NSURL URLWithString:strNormalImageName] forState:UIControlStateNormal placeholderImage:[UIImage imageNamed:@"更多"]];
    
    
}

- (void)setStrHighLightImageName:(NSString *)strHighLightImageName
{
    _strHighLightImageName = strHighLightImageName;
//    if (self.isCommonImage) {
//        [self.btnItem setImage:[UIImage imageNamed:strHighLightImageName] forState:UIControlStateHighlighted];
//    }else{
//        [self.btnItem sd_setImageWithURL:[NSURL URLWithString:strHighLightImageName] forState:UIControlStateHighlighted];
//    }
    [self.btnItem sd_setImageWithURL:[NSURL URLWithString:strHighLightImageName] forState:UIControlStateHighlighted placeholderImage:[UIImage imageNamed:@"更多"]];
    
}

////  button1普通状态下的背景色
//- (void)button1BackGroundNormal:(UIButton *)sender
//{
//    sender.backgroundColor = [UIColor clearColor];
//}
//
////  button1高亮状态下的背景色
//- (void)button1BackGroundHighlighted:(UIButton *)sender
//{
//    sender.backgroundColor = RGB(79, 200, 249);
//}


@end
