//
//  EZPlaybackViewController.h
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/11/3.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import <UIKit/UIKit.h>
//#import "EZDeviceInfo.h"

@interface EZPlaybackViewController : UIViewController

@property (nonatomic, strong) EZDeviceInfo *deviceInfo;
@property (nonatomic) NSInteger cameraIndex;

@end
