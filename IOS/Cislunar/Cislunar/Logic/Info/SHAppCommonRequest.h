//
//  SHAppCommonRequest.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/26.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void (^BlockGetPicCompleteHandle)(BOOL isSucceed, id response);
typedef void (^BlockPushRegisterCallBack)(BOOL success, id result);
typedef void (^GetCurrentGatewayInfoCallBack)(BOOL success, id result);


@interface SHAppCommonRequest : NSObject

+ (id)shareInstance;

- (void)doGetPicListFromNetworkWithTypeID:(NSString *)strCategory completeHandle:(BlockGetPicCompleteHandle)complete;

#pragma mark - 获取房间UI图片和公共图片的集合
- (NSArray *)doGetRoomUIPicAll;

#pragma mark - 获取设备UI图片和公共图片的集合
- (NSArray *)doGetDeviceUIPicAll;

#pragma mark - 获取场景UI图片和公共图片的集合
- (NSArray *)doGetScreenUIPicAll;

#pragma mark - 获取场景三种图片
- (NSString *)doGetScreenCommonUIPicWithPicName:(NSString *)strPicName;

- (NSString *)doGetScreenHighLightUIPicWithPicName:(NSString *)strPicName;

- (NSString *)doGetScreenGrayUIPicWithPicName:(NSString *)strPicName;

#pragma mark - 获取设备三种图片
- (NSString *)doGetDeviceCommonUIPicWithPicName:(NSString *)strPicName;

- (NSString *)doGetDeviceHighLightUIPicWithPicName:(NSString *)strPicName;

- (NSString *)doGetDeviceGrayUIPicWithPicName:(NSString *)strPicName;

#pragma mark - 获取房间三种图片
- (NSString *)doGetRoomCommonUIPicWithPicName:(NSString *)strPicName;

- (NSString *)doGetRoomHighLightUIPicWithPicName:(NSString *)strPicName;

- (NSString *)doGetRoomGrayUIPicWithPicName:(NSString *)strPicName;


//注册push信息
- (void)handlePostPushWithUUID:(NSString *)strUUID pushRegisterCallBack:(BlockPushRegisterCallBack)callBack;

//获取当前网关信息
- (void)handleGetCurrentGatewayInfo:(GetCurrentGatewayInfoCallBack)callBack;

@end
