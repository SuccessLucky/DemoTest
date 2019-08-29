//
//  DeviceAllListVC.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/26.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import "YCTBaseViewController.h"

@interface DeviceAllListVC : YCTBaseViewController

@property (assign, nonatomic) int iType;
@property (strong, nonatomic) NSArray *arrHasAddedList;
@property (copy, nonatomic) void(^BlockGetDeviceList)(int iType, NSArray *arrDeviceListSelected);

@end
