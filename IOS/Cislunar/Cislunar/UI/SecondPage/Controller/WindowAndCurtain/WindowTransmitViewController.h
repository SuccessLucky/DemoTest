//
//  WindowTransmitViewController.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/14.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface WindowTransmitViewController : UIViewController

@property (strong, nonatomic) SHModelDevice *device;

@property (copy, nonatomic) void(^blockGetStatueComplete)(NSString *strStatueHex);

@property (assign, nonatomic) int itype;


@end
