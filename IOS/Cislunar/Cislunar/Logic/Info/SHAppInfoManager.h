//
//  SHAppInfoManager.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/1/13.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SHAppInfoManager : NSObject

+ (id)shareInstance;

/**
 路由器
 @param strHardMacAddr 路由器的硬件mac地址
 */
- (void)doSetStrHardMacAddr:(NSString *)strHardMacAddr;
- (NSString *)DoGetstrHardMacAddr;

/**
 deviceToken

 @return deviceToken
 */
- (NSString *)handleGetDeviceToken;

- (void)handleSetDeviceToken:(NSString *)token;

/**
 判断局域网标志

 @param isInLAN 是否在局域网内
 */
- (void)doSetInLAN:(BOOL)isInLAN;

- (BOOL)doIsInLAN;

/**
 判断是否需要刷新首页常用场景

 @param strIdentifer 取值为 @“0”不用刷新 ； @“1”需要刷新
 */
- (void)doSetStrShouldRefreshFavouriteScreen:(NSString *)strRefreshFavouriteScreenIdentifer;

- (NSString *)doGetRefreshFavouriteScreenIdentifer;


//判断是否进行了智能管理修改
- (void)doChangeTheFloorOrRoom:(BOOL)isChangedFR;

- (BOOL)doIsChangedFR;

//判断是否进行了常用情景模式的修改
- (void)doChangeTheFavouriteScreen:(BOOL)isChangedFavouriteScreen;

- (BOOL)doIsChangedFavouriteScreen;

@end
