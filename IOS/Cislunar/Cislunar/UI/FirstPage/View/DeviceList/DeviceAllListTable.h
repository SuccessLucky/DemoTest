//
//  DeviceAllListTable.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/26.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DeviceListAllModel.h"

@interface DeviceAllListTable : UITableView

@property (strong, nonatomic) UIButton *btnSelectedAllTr;

@property (strong, nonatomic) DeviceListAllModel *deviceListAllModel;

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style;

@end
