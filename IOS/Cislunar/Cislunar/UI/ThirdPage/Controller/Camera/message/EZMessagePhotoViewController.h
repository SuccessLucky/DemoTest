//
//  EZMessagePhotoViewController.h
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/11/16.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import "MWPhotoBrowser.h"
//#import "EZAlarmInfo.h"
//#import "EZDeviceInfo.h"

@interface EZMessagePhotoViewController : MWPhotoBrowser

@property (nonatomic, strong) UIImage *image;
@property (nonatomic, strong) EZAlarmInfo *info;
@property (nonatomic, strong) EZDeviceInfo *deviceInfo;

@end
