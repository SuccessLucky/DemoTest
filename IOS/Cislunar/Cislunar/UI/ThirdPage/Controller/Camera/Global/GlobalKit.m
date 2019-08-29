//
//  GlobalKit.m
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/10/27.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import "GlobalKit.h"

@implementation GlobalKit

+ (instancetype)shareKit
{
    static GlobalKit *kit = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        kit = [GlobalKit new];
    });
    return kit;
}

- (instancetype)init
{
    self = [super init];
    if (self)
    {
        _deviceVerifyCodeBySerial = [NSMutableDictionary new];
    }
    return self;
}

- (void)clearDeviceInfo
{
    self.deviceVerifyCode = nil;
    self.deviceSerialNo = nil;
    self.deviceModel = nil;
}

@end
