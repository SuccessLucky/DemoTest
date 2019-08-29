//
//  SHAirHandleTypeVC.h
//  SmartHouseNew
//
//  Created by yyh on 15/3/16.
//  Copyright (c) 2015年 yuchangtao. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SHAirHandleTypeVC : UIViewController

@property (strong, nonatomic) SHModelDevice *device;

@property (nonatomic, copy) void (^selectAirHandleType)(NSDictionary *dic);

@end
