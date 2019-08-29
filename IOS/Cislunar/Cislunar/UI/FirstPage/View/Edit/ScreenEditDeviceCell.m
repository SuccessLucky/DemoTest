//
//  ScreenEditDeviceCell.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/24.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import "ScreenEditDeviceCell.h"

@interface ScreenEditDeviceCell ()

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *topLineConstraintHeight;

@end

@implementation ScreenEditDeviceCell

//-(instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
//{
//    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
//    if (self) {
//        [self handleInitView];
//        self.selectionStyle = UITableViewCellSelectionStyleNone;
//        
//    }
//    return self;
//}

- (void)awakeFromNib {
    [super awakeFromNib];
    self.imageViewTopLine.backgroundColor = kLineColor;
    self.topLineConstraintHeight.constant = 0.5;
    
    UIColor *color = [UIColor whiteColor];
    self.backgroundColor = [color colorWithAlphaComponent:0.0];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    // Configure the view for the selected state
}


- (IBAction)switchDevicePressed:(UISwitch *)sender {
    
    if (self.blockDeviceSwitchPressed) {
        self.blockDeviceSwitchPressed(sender);
    }
}


//- (void)layoutSubviews {
//    [super layoutSubviews];
////    [self setCornerRadius:8 withShadow:YES withOpacity:0.6];
//}
//
//- (void)setCornerRadius:(CGFloat)radius withShadow:(BOOL)shadow withOpacity:(CGFloat)opacity {
//    self.layer.cornerRadius = radius;
//    if (shadow) {
//        self.layer.shadowColor = [UIColor lightGrayColor].CGColor;
//        self.layer.shadowOpacity = opacity;
//        self.layer.shadowOffset = CGSizeMake(-4, 4);
//        self.layer.shadowRadius = 4;
//        self.layer.shouldRasterize = NO;
//        self.layer.shadowPath = [[UIBezierPath bezierPathWithRoundedRect:[self bounds] cornerRadius:radius] CGPath];
//    }
//    self.layer.masksToBounds = !shadow;
//}


@end
