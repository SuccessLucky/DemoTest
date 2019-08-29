//
//  ColorBulbVCForScreen.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/6/3.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ColorBulbVCForScreen : UIViewController

@property (strong, nonatomic) SHModelDevice *device;

@property (copy, nonatomic) void(^BlockGetNewDevice)(SHModelDevice *modelDevice);

@end
