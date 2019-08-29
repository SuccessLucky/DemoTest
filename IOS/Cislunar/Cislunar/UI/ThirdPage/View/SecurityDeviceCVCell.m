//
//  SecurityDeviceCVCell.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/20.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "SecurityDeviceCVCell.h"

@interface SecurityDeviceCVCell()

@property (weak, nonatomic) IBOutlet UILabel *laebelDeviceName;
@property (weak, nonatomic) IBOutlet UILabel *labelDeviceRegion;
@property (weak, nonatomic) IBOutlet UIImageView *imageVRedDot;

@end

@implementation SecurityDeviceCVCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.backgroundColor=[UIColor lightTextColor];
    self.layer.cornerRadius=10;
    self.layer.shadowColor = [UIColor blackColor].CGColor;//shadowColor阴影颜色
    self.layer.shadowOffset = CGSizeMake(0,0);//shadowOffset阴影偏移，默认(0, -3),这个跟shadowRadius配合使用
    self.layer.shadowOpacity = 0.3f;//阴影透明度，默认0
    self.layer.shadowRadius = 3;
    
    self.imageVRedDot.layer.masksToBounds=YES;
    self.imageVRedDot.layer.cornerRadius= self.imageVRedDot.frame.size.width/2.0;
    self.imageVRedDot.clipsToBounds = YES;
    self.imageVRedDot.backgroundColor = [UIColor redColor];
}

-(void)setStrDeviceName:(NSString *)strDeviceName
{
    self.laebelDeviceName.text = strDeviceName;
}

-(void)setStrDeviceRegion:(NSString *)strDeviceRegion
{
    self.labelDeviceRegion.text = strDeviceRegion;
}

- (void)setIsShowRedDot:(BOOL)isShowRedDot
{
    self.imageVRedDot.hidden = !isShowRedDot;
}

@end
