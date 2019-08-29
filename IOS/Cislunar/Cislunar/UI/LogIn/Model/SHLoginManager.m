//
//  SHLoginManager.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/1/13.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHLoginManager.h"
@interface SHLoginManager()

@property (nonatomic, assign) BOOL  isLogined;    //登录标志

@end

@implementation SHLoginManager

+ (id)shareInstance
{
    static SHLoginManager *loginManager;
    static dispatch_once_t oneceToken;
    dispatch_once(&oneceToken, ^{
        loginManager = [[SHLoginManager alloc] init];
    });
    
    return loginManager;
}

@end
