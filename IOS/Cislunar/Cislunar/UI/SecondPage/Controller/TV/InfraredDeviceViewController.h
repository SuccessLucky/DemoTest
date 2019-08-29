//
//  InfraredDeviceViewController.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/14.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface InfraredDeviceViewController : UIViewController

@property (strong, nonatomic) NSString *strDeviceMacAddr;
@property (assign, nonatomic) int iDeviceId;
@property (strong, nonatomic) NSMutableArray *mutArrHouseNO;
@property (strong, nonatomic) NSMutableArray *mutArrKeyBtns;
@property (strong, nonatomic) SHModelDevice *device;

@property (assign, nonatomic) BOOL isCouldStudy;

@property (assign, nonatomic) int itype;

@end
