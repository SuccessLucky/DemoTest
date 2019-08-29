//
//  DeviceListHeaderView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/26.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import "DeviceListHeaderView.h"

@interface DeviceListHeaderView ()

@property (weak, nonatomic) IBOutlet UIButton *btnChoose;

@property (weak, nonatomic) IBOutlet UILabel *labelName;

@end

@implementation DeviceListHeaderView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (id)init
{
    NSArray *tempArr = [[NSBundle mainBundle] loadNibNamed:@"DeviceListHeaderView"
                                                     owner:self
                                                   options:nil];
    if (tempArr) {
        self = tempArr[0];
        if (self) {
            //            self.backgroundColor = kBackgroundGrayColor;
        }
        return self;
    }
    return nil;
}


-(void)awakeFromNib{
    [super awakeFromNib];
    
}

-(void)setName:(NSString *)name{
    _name = name;
    self.labelName.text = name;
}

- (IBAction)clickAllSelected:(UIButton *)sender {
    NSLog(@"区头全选");
    if ([self.delegate respondsToSelector:@selector(DeviceListHeaderViewDelegateMethodClickSection:)]) {
        [self.delegate DeviceListHeaderViewDelegateMethodClickSection:self.section];
    }
}

-(void)setDetailModel:(RoomModel *)detailModel
{
    _detailModel = detailModel;
    self.labelName.text = detailModel.strRoom_name;
    
    if (detailModel.isChoose) {
        [self.btnChoose setImage:[UIImage imageNamed:@"color_choose"] forState:UIControlStateNormal];
    }else{
        [self.btnChoose setImage:[UIImage imageNamed:@"color_no_choose"] forState:UIControlStateNormal];
    }
}

@end
