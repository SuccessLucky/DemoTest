//
//  EZDeviceUpgradeViewController.h
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/12/23.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import <UIKit/UIKit.h>
//#import "EZDeviceVersion.h"

@interface EZDeviceUpgradeViewController : UIViewController

@property (nonatomic, strong) EZDeviceVersion *version;
@property (nonatomic, copy) NSString *deviceSerial;

@end
