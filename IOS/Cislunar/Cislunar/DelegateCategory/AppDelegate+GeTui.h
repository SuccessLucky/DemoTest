//
//  AppDelegate+GeTui.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/14.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "AppDelegate.h"
#import <GTSDK/GeTuiSdk.h>
#import <PushKit/PushKit.h> //VOIP支持需要导入PushKit库,实现 PKPushRegistryDelegate
#if __IPHONE_OS_VERSION_MAX_ALLOWED >= __IPHONE_10_0
#import <UserNotifications/UserNotifications.h>
#endif

@interface AppDelegate (GeTui)<UNUserNotificationCenterDelegate,PKPushRegistryDelegate, GeTuiSdkDelegate>

//推送---k启动推送
- (void)doStartGT;

//停止推送
- (void)doStopGT;

//获取状态
- (SdkStatus)doGetGTStatus;

//获取CID
- (NSString *)doGetGTCID;

//获取个推SDK版本
- (NSString *)doGetGTVison;

//注册DeviceToken
- (void)doRegisterDeviceToken:(NSString *)deviceToken;

//设置标签
- (void)doSetGTTag;

//绑定别名
- (void)doBindingAlias;

// 取消绑定别名
- (void)doCancellBindingAlias;

//发送消息
- (void)doSendMsg;

//设置App 红点计数
- (void)doSetBadgeValue:(int)iBadgeValue;

// 重置app 红点计数
- (void)doResetBadgeValue;

@end
