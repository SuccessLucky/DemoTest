//
//  SecurityHeaderView.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/20.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "SecurityHeaderView.h"

@interface SecurityHeaderView ()

@property (weak, nonatomic) IBOutlet UIButton *btnArmingOrDisarm;
@property (weak, nonatomic) IBOutlet UIImageView *imageArmingOrDisarm;


@end

@implementation SecurityHeaderView


/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
- (IBAction)btnArmOrDisarm:(UIButton *)sender {
    if (self.BlockCollectionHeaderSelected) {
        self.BlockCollectionHeaderSelected(sender, self.imageArmingOrDisarm,self.isArming);
    }
}

-(void)setIsArming:(BOOL)isArming
{
    _isArming = isArming;
    if (isArming) {
        self.btnArmingOrDisarm.selected = YES;
        self.imageArmingOrDisarm.image = [UIImage imageNamed:@"布防中"];
    }else{
        
        self.btnArmingOrDisarm.selected = NO;
        self.imageArmingOrDisarm.image = [UIImage imageNamed:@"已撤防"];
    }
}
- (IBAction)btnCancellAlarm:(UIButton *)sender {
    
    if (self.BlockBtnCancellAlarmPressed) {
        self.BlockBtnCancellAlarmPressed(sender);
    }
}

@end
