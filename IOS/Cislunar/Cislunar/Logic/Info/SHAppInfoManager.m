//
//  SHAppInfoManager.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/1/13.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHAppInfoManager.h"
@interface SHAppInfoManager ()

@property (nonatomic, strong) NSString *deviceToken;

@property (nonatomic, assign) BOOL  isInLoaclLAN;

@property (nonatomic, assign) BOOL  isChangedFR;    //是否进行了楼层或者房间的修改

@property (nonatomic, assign) BOOL isChangedFavouriteScreen; //是否修改的常用场景模式
@end


@implementation SHAppInfoManager


+ (id)shareInstance
{
    static SHAppInfoManager *appInfoManager;
    static dispatch_once_t oneceToken;
    dispatch_once(&oneceToken, ^{
        appInfoManager = [[SHAppInfoManager alloc] init];
    });
    
    return appInfoManager;
}

#pragma mark - deviceToken
- (NSString *)handleGetDeviceToken
{
    if (!self.deviceToken || [self.deviceToken length] == 0) {
        NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
        self.deviceToken = [userDefaults objectForKey:@"deviceToken"];
    }
    return self.deviceToken;
}

- (void)handleSetDeviceToken:(NSString *)token
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:token forKey:@"deviceToken"];
    [userDefaults synchronize];
}

- (void)doSetStrHardMacAddr:(NSString *)strHardMacAddr
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:strHardMacAddr forKey:@"RouterHardAddress"];
    [userDefaults synchronize];
}

- (NSString *)DoGetstrHardMacAddr
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    return [userDefaults objectForKey:@"RouterHardAddress"];
}

- (void)doSetInLAN:(BOOL)isInLAN
{
    _isInLoaclLAN = isInLAN;
}

- (BOOL)doIsInLAN
{
    return  _isInLoaclLAN;
}

- (void)doSetStrShouldRefreshFavouriteScreen:(NSString *)strRefreshFavouriteScreenIdentifer
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:strRefreshFavouriteScreenIdentifer forKey:@"strRefreshFavouriteScreenIdentifer"];
    [userDefaults synchronize];
}

- (NSString *)doGetRefreshFavouriteScreenIdentifer
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    return [userDefaults objectForKey:@"strRefreshFavouriteScreenIdentifer"];
}


//判断是否进行了智能管理修改
- (void)doChangeTheFloorOrRoom:(BOOL)isChangedFR
{
    _isChangedFR = isChangedFR;
}

- (BOOL)doIsChangedFR
{
    return _isChangedFR;
}

- (void)doChangeTheFavouriteScreen:(BOOL)isChangedFavouriteScreen
{
    _isChangedFavouriteScreen = isChangedFavouriteScreen;
}

- (BOOL)doIsChangedFavouriteScreen
{
    return _isChangedFavouriteScreen;
}



@end
