//
//  AirConditionVC.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/15.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AirConditionVC : UIViewController

@property (strong, nonatomic) SHModelDevice *device;

@property (assign, nonatomic) int itype;  //区分是来自添加设备还是控制设备 1.表示添加设备push；0.表示控制页面push

@property (assign, nonatomic) int iCode;


@end
