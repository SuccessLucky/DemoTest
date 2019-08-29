//
//  LockViewController.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/16.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LockViewController : UIViewController

@property (strong, nonatomic) NSString *strPsw;
@property (assign, nonatomic) int iDeviceID;
@property (strong, nonatomic) NSString *strMacAddr;
@property (assign, nonatomic) int itype;  //区分是来自添加设备还是控制设备 1.表示添加设备push；0.表示控制页面push
@property (strong, nonatomic) SHModelDevice *deviceTransmit;

@end
