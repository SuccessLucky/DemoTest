//
//  EZWifiConfigViewController.h
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/10/29.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface EZWifiConfigViewController : UIViewController

@property (nonatomic, copy) NSString *ssid;
@property (nonatomic, copy) NSString *password;
@property (nonatomic) BOOL isAddDeviceSuccessed;
@property (nonatomic,assign) BOOL supportSmartMode;
@property (nonatomic,assign) BOOL supportSoundMode;

@end
