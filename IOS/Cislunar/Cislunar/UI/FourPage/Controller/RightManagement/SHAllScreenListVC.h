//
//  SHAllScreenListVC.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/24.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "YCTBaseViewController.h"

@interface SHAllScreenListVC : YCTBaseViewController

@property (strong, nonatomic) NSArray *arrHasAddedList;
@property (copy, nonatomic) void(^BlockGetScreenList)(NSArray *arrSelectedScreenList);

@end
