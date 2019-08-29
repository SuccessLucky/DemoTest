//
//  DeviceAllListCell.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/26.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DeviceListAllModel.h"

@interface DeviceAllListCell : UITableViewCell


@property (weak, nonatomic) IBOutlet UIImageView *imageViewTopLine;
@property (weak, nonatomic) IBOutlet UIImageView *imageViewBottomLine;

@property (assign, nonatomic) BOOL isChoosed;

@property (copy, nonatomic) void(^blockCellChoosePressed)(DeviceModel *deviceModel,RoomModel *roomModel);

@property (nonatomic,strong)RoomModel *roomModel;

@property (nonatomic,strong)DeviceModel *deviceModel;

@end
