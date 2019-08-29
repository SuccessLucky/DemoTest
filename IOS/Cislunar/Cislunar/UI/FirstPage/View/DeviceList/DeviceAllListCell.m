//
//  DeviceAllListCell.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/26.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import "DeviceAllListCell.h"

@interface DeviceAllListCell ()

@property (weak, nonatomic) IBOutlet UIImageView *imageViewIcon;
@property (weak, nonatomic) IBOutlet UILabel *labelDeviceName;
@property (weak, nonatomic) IBOutlet UILabel *labelDeviceLocation;
@property (weak, nonatomic) IBOutlet UIButton *btnChoose;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *topLineConstraint;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottomLineConstraint;

/** 记录最终选中的个数*/
@property (nonatomic,assign)NSInteger  addCount;
@end

@implementation DeviceAllListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.imageViewTopLine.backgroundColor = kLineColor;
    self.topLineConstraint.constant = 0.5;
    
    self.imageViewBottomLine.backgroundColor = kLineColor;
    self.bottomLineConstraint.constant = 0.5;
    
    self.addCount = 1;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)btnChoosePressed:(UIButton *)sender {
    
    if (self.blockCellChoosePressed) {
        self.blockCellChoosePressed(self.deviceModel,self.roomModel);
    }
}

- (void)setIsChoosed:(BOOL)isChoosed
{
    _isChoosed = isChoosed;
    
}

-(void)setDeviceModel:(DeviceModel *)deviceModel
{
    _deviceModel = deviceModel;
    
    NSString *strUnUrl = [[SHAppCommonRequest shareInstance] doGetDeviceGrayUIPicWithPicName:deviceModel.strDevice_image];
//    NSString *strPrUrl = [[SHAppCommonRequest shareInstance] doGetDeviceHighLightUIPicWithPicName:deviceModel.strDevice_image];
    [self.imageViewIcon sd_setImageWithURL:[NSURL URLWithString:strUnUrl] placeholderImage:nil];
    self.labelDeviceName.text = deviceModel.strDevice_device_name;
    self.labelDeviceLocation.text = [NSString stringWithFormat:@"%@%@",deviceModel.strDevice_floor_name,deviceModel.strDevice_room_name];
    if (deviceModel.isChoosed) {
        [self.btnChoose setImage:[UIImage imageNamed:@"color_choose"] forState:UIControlStateNormal];
        
    }else{
        [self.btnChoose setImage:[UIImage imageNamed:@"color_no_choose"] forState:UIControlStateNormal];
    }
}


@end
