//
//  EZLocalCameraListViewController.h
//  EZOpenSDKDemo
//
//  Created by linyong on 2017/8/16.
//  Copyright © 2017年 hikvision. All rights reserved.
//

#import <UIKit/UIKit.h>

@class EZHCNetDeviceInfo;

@interface EZLocalCameraListViewController : UITableViewController

@property (nonatomic,strong) EZHCNetDeviceInfo *deviceInfo;


@end
