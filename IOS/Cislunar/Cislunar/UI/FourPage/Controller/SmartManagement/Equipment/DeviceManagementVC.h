//
//  DeviceManagementVC.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/27.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "YCTBaseViewController.h"

@interface DeviceManagementVC : YCTBaseViewController

@property (strong, nonatomic) SHModelRoom *room;

- (void)handleAddFailUsedToDo;

@end
