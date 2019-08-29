//
//  SHDeviceManager.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/27.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void (^GetListCallBack)(BOOL success, id result);
typedef void (^BlockGetDeviceSingleInfoCompleteHandle)(BOOL success, id object);

@interface SHDeviceManager : NSObject

@property (nonatomic, strong) NSArray *arrDeviceList;
@property (nonatomic, strong) NSDictionary *errorInfo;

//根据roomID获取设备
- (void)doGetDeviceListDataFromDBWithRoomID:(int)iRoomID;
- (void)doGetDeviceListFromNetworkWithRoomID:(int)strRoomID;

//添加设备
- (void)handleTheAddDeviceDataWithArrModel:(NSArray *)arrModels
                         completeHandle:(GetListCallBack)callBack;

/**
 删除设备，iD和macAddr仅存一个
 设备ID与MAC地址不能同时传，只能传一个
 删除设备，优先以ID形式删除，当以MAC地址删除的时候，ID必须为空

 @param deviceId 设备的ID
 @param strMacAddr 设备的MacAddr
 @param callBack 返回
 */
- (void)handleDeleteDeviceByDeviceId:(int)deviceId completeHandle:(GetListCallBack)callBack;

- (void)handleDeleteDeviceByMacAddr:(NSString *)strMacAddr completeHandle:(GetListCallBack)callBack;

/**
 更新设备

 @param device device
 */
- (void)handleTheUpdateDeviceDataWithModel:(SHModelDevice *)device
                            completeHandle:(GetListCallBack)callBack;


/**
 获取单个设备的信息

 @param iDeviceID iDeviceID
 @param callBack device
 */
- (void)handleGetDeviceInfoByDeviceId:(int)iDeviceID
                       completeHandle:(BlockGetDeviceSingleInfoCompleteHandle)callBack;


/**
 获取当前网关下所有设备
 */
- (void)doGetAllDeviceListFromNetwork;
- (void)doGetAllDeviceListDataFromDB;

- (void)doGetAllDeviceListFromNetworkV2;
- (void)doGetAllDeviceListDataFromDBV2;


@end
