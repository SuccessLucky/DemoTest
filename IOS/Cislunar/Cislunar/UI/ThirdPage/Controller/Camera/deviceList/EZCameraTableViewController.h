//
//  EZCameraTableViewController.h
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 16/9/20.
//  Copyright © 2016年 hikvision. All rights reserved.
//

#import <UIKit/UIKit.h>
//#import "EZDeviceInfo.h"

@interface EZCameraTableViewController : UITableViewController

@property (nonatomic, strong) EZDeviceInfo *deviceInfo;
@property (nonatomic) NSInteger go2Type; //0直播，1回放

@end
