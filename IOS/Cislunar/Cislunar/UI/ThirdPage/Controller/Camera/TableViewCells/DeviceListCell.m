//
//  CameraListCell.m
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/10/27.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import "DeviceListCell.h"
#import "DDKit.h"

#import "UIImageView+WebCache.h"

@implementation DeviceListCell

- (void)setDeviceInfo:(EZDeviceInfo *)deviceInfo
{
    NSLog(@"deviceInfo is %@", deviceInfo);
    _deviceInfo = deviceInfo;
    
    if (_deviceInfo.status == 1)
    {
        self.offlineIcon.hidden = YES;
    }
    else
    {
        self.offlineIcon.hidden = NO;
    }
    self.nameLabel.text = [NSString stringWithFormat:@"%@",deviceInfo.deviceName];
    [self.cameraImageView sd_setImageWithURL:[NSURL URLWithString:deviceInfo.deviceCover] placeholderImageScale:nil];
//    [EZOPENSDK capturePicture:cameraInfo.deviceSerial channelNo:cameraInfo.channelNo completion:^(NSString *url, NSError *error) {
//        if(!error){
//            [self.cameraImageView sd_setImageWithURL:[NSURL URLWithString:url]];
//        }
//    }];
    
    self.messageButton.hidden = NO;
    self.settingButton.hidden = NO;
    if (self.isShared)
    {
        self.messageButton.hidden = YES;
        self.settingButton.hidden = YES;
    }
    [self.contentView dd_addSeparatorWithType:ViewSeparatorTypeBottom];
}

- (IBAction)go2Play:(id)sender
{
    [self.parentViewController performSelector:@selector(go2Play:) withObject:_deviceInfo];
}

- (IBAction)go2Record:(id)sender
{
    [self.parentViewController performSelector:@selector(go2Record:) withObject:_deviceInfo];
}

- (IBAction)go2Setting:(id)sender
{
    [self.parentViewController performSelector:@selector(go2Setting:) withObject:_deviceInfo];
}

- (IBAction)go2Message:(id)sender
{
    [self.parentViewController performSelector:@selector(go2Message:) withObject:_deviceInfo];
}

//- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
//    NSLog(@"touches = %@, event = %@", touches, event);
//}

@end
