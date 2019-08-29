//
//  SecurityTableSectionView.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/20.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "SecurityTableSectionView.h"

@interface SecurityTableSectionView ()
@property (weak, nonatomic) IBOutlet UIImageView *imageVDeviceIcon;
@property (weak, nonatomic) IBOutlet UILabel *labelDeviceName;
@property (weak, nonatomic) IBOutlet UILabel *labelDeviceTypeNum;
@property (weak, nonatomic) IBOutlet UIImageView *imageVArrow;
@property (weak, nonatomic) IBOutlet UIButton *btnBg;
@property (weak, nonatomic) IBOutlet UIButton *btnRedDotCount;

@end


@implementation SecurityTableSectionView

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
//    self.btnBg.layer.cornerRadius=10;
//    self.btnBg.layer.shadowColor = [UIColor blackColor].CGColor;//shadowColor阴影颜色
//    self.btnBg.layer.shadowOffset = CGSizeMake(0,0);//shadowOffset阴影偏移，默认(0, -3),这个跟shadowRadius配合使用
//    self.btnBg.layer.shadowOpacity = 0.3f;//阴影透明度，默认0
//    self.btnBg.layer.shadowRadius = 3;
//    [self.btnBg addTarget:self action:@selector(touchDown) forControlEvents:UIControlEventTouchDown];
//    [self.btnBg addTarget:self action:@selector(touchUp) forControlEvents:UIControlEventEditingDidEnd];
    
    self.btnRedDotCount.layer.masksToBounds=YES;
    self.btnRedDotCount.layer.cornerRadius= self.btnRedDotCount.frame.size.width/2.0;
    self.btnRedDotCount.clipsToBounds = YES;
    self.btnRedDotCount.backgroundColor = [UIColor redColor];
}

- (void)touchDown
{
    [self.btnBg setBackgroundImage:[UIImage imageNamed:@"bg_anfang"] forState:UIControlStateNormal];
}

- (void)touchUp
{
    [self.btnBg setBackgroundImage:[UIImage imageNamed:@"bg_anfang_pre"] forState:UIControlStateNormal];
}

- (IBAction)btnTapPressed:(UIButton *)sender {
    if (self.BlockBtnPressed) {
        self.BlockBtnPressed(self.iSection);
    }
}

- (IBAction)btnSectionHeaderViewPressed:(UIButton *)sender {
    
}

- (void)setStrIcon:(NSString *)strIcon
{
    self.imageVDeviceIcon.image = [UIImage imageNamed:strIcon];
}

-(void)setStrName:(NSString *)strName
{
    self.labelDeviceName.text = strName;
}

-(void)setStrNum:(NSString *)strNum
{
    if ([strNum intValue] == 0) {
        self.labelDeviceTypeNum.text  = @"";
    }else{
        self.labelDeviceTypeNum.text  = [NSString stringWithFormat:@"%@个",strNum];
    }
    
}

-(void)setStrArrow:(NSString *)strArrow
{
    self.imageVArrow.image = [UIImage imageNamed:strArrow];
}

-(void)setIRedDotCount:(int)iRedDotCount
{
    if (iRedDotCount) {
        self.btnRedDotCount.hidden = NO;
        [self.btnRedDotCount setTitle:[NSString stringWithFormat:@"%d",iRedDotCount] forState:UIControlStateNormal];
    }else{
        self.btnRedDotCount.hidden = YES;
    }
}


/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
