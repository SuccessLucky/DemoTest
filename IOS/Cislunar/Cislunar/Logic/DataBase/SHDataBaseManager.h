//
//  SHDataBaseManager.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/25.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "FMDB.h"
#import "SHDataBaseDefine.h"
#import "SHCacheModel.h"

typedef void (^BlockDidDeleteTableComplete)(BOOL isSucced);

@interface SHDataBaseManager : NSObject

+ (instancetype)sharedInstance;

#pragma mark - 建表
- (void)createCommonTable;

#pragma mark - 插入操作

//普通表插入数据操作
- (void)doHandleInsertDataWithIdentifer:(NSString *)strIdentifer jsonStr:(NSString *)strJsonStr;

//房间表插入操作
- (void)doHandleInsertRoomListDataWithFloorID:(int)iFloorID jsonStr:(NSString *)strJsonStr;

//根据RoomID存储拉下来的设备列表插入操作
- (void)doHandleInsertDeviceDataWithRoomID:(int)iRoomID jsonStr:(NSString *)strJsonStr;

//根据MemberID存储拉下来的设备列表插入操作
- (void)doHandleInsertMemberControlListDataWithMemberID:(int)iMemberID jsonStr:(NSString *)strJsonStr;

//根据DeviceID存储拉下来的门锁用户列表插入操作
- (void)doHandleInsertLockMemDataWithDeviceID:(int)iDeviceID jsonStr:(NSString *)strJsonStr;

//根据DeviceMacAddr存储拉下来的门锁用户列表插入操作
- (void)doHandleInsertLockMemDataWithDeviceMacAddr:(NSString *)strDeviceMacAddr jsonStr:(NSString *)strJsonStr;

#pragma mark - 查询操作
//普通表查询
- (NSString *)doQueryDataWithIdentifer:(NSString *)identifer;

//根据FloorID查询RoomList操作
- (NSString *)doQueryRoomListDataWithFloorID:(int)iFloorID;

// 根据RoomID查询DeviceList操作
- (NSString *)doQueryDevieListDataWithRoomID:(int)iRoomID;

//根据memberID进行查询
- (NSString *)doQueryMemberControlListDataWithMemberID:(int)iMemberID;

//根据DeviceID查询LockMemberList表操作
- (NSString *)doQueryLockMemberListDataWithDeviceID:(int)iDviceID;

//根据DeviceMacAddr查询LockMemberList表操作
- (NSString *)doQueryLockMemberListDataWithDeviceMacAddr:(NSString *)strDeviceMacAddr;



#pragma mark - 删除操作
//普通表删除
- (void)doDeleteDataWithIdentifer:(NSString *)identifer;

// 根据floor_id删除room表操作
- (void)doDeleteRoomListDataWithFloorID:(int)iFloorID;

//根据floor_id删除room表操作
- (void)doDeleteDeviceListDataWithRoomID:(int)iRoomID;

//根据memberiD删除可控制值列表的操作
- (void)doDeleteMemberControlListDataWithMemberID:(int)iMemberID;

//根据DeviceID删除LockMemberList表操作
- (void)doDeleteLockMemberListDataWithDeviceID:(int)iDeviceID;

//根据strDeviceMacAddr删除LockMemberList表操作
- (void)doDeleteLockMemberListDataWithDeviceMacAddr:(NSString *)strDeviceMacAddr;

#pragma mark - 删除操作
- (void)doDeleteTable:(NSString *)tableName complete:(BlockDidDeleteTableComplete)complete;

@end
