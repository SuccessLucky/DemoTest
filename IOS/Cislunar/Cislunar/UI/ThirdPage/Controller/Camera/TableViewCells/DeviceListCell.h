//
//  CameraListCell.h
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/10/27.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import <UIKit/UIKit.h>
//#import "EZDeviceInfo.h"

@interface DeviceListCell : UITableViewCell
{
    EZDeviceInfo *_deviceInfo;
}

@property (nonatomic, weak) IBOutlet UIImageView *cameraImageView;
@property (nonatomic, weak) IBOutlet UIButton *playButton;
@property (nonatomic, weak) IBOutlet UIButton *recordButton;
@property (nonatomic, weak) IBOutlet UIButton *messageButton;
@property (nonatomic, weak) IBOutlet UIButton *settingButton;
@property (nonatomic, weak) IBOutlet UILabel *nameLabel;
@property (nonatomic, weak) IBOutlet UIImageView *offlineIcon;
@property (nonatomic, weak) UIViewController *parentViewController;
@property (nonatomic) BOOL isShared;

- (void)setDeviceInfo:(EZDeviceInfo *)deviceInfo;

@end
