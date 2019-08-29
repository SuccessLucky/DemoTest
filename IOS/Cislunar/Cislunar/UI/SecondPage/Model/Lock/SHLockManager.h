//
//  SHLockManager.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/25.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void (^BlockLockManagerHandleComplete)(BOOL success, id result);

@interface SHLockManager : NSObject

@property (nonatomic, strong) NSArray *arrLockMemberList;
@property (nonatomic, strong) NSDictionary *errorInfo;

@property (nonatomic, strong) NSArray *arrLockPswList;
@property (nonatomic, strong) NSDictionary *getLockPswListErrorInfo;

//获取指纹锁用户列表
- (void)doGetLockMemberListFromNetworkWithDeviceID:(int)iDeviceID;
- (void)doGetLockMemberListDataFromDBWithDeviceID:(int)iDeviceID;

// 根据DeviceMacAddr获取LoackMemberList
- (void)doGetLockMemberListFromNetworkWithDeviceMacAddr:(NSString *)strDeviceMacAddr;
- (void)doGetLockMemberListDataFromDBWithDeviceMacAddr:(NSString *)strDeviceMacAddr;

//添加门锁密码
- (void)handleTheAddLockPswDataWithDeviceID:(int)iDeviceID
                                        psw:(NSString *)strPsw
                             completeHandle:(BlockLockManagerHandleComplete)callBack;

// 添加指纹用户
- (void)handleTheAddLockMemberDataWithDeviceID:(int)iDeviceID
                                    memberName:(NSString *)strMemberName
                                 fingerprintId:(NSString *)strFingerprintId
                                completeHandle:(BlockLockManagerHandleComplete)callBack;

//删除锁用户
- (void)doDeleteLockMemberWithLockID:(int)iLockID
                            lockType:(NSString *)strLockType
                      completeHandle:(BlockLockManagerHandleComplete)callBack;

//获取指纹锁密码列表
- (void)doGetLockPswListFromNetworkWithDeviceID:(int)iDeviceID;


//密码解锁
- (void)handleToOpenTheLockWithDeviceID:(int)iDeviceID
                              unLockPsw:(NSString *)strUnLockPsw
                         completeHandle:(BlockLockManagerHandleComplete)callBack;


@end
