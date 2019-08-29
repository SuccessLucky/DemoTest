//
//  SHLockManager.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/25.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHLockManager.h"
#import "SHLockMemModel.h"
#import "SHLockPswModel.h"

@implementation SHLockManager

#pragma mark - 根据DeviceID获取LoackMemberList
- (void)doGetLockMemberListFromNetworkWithDeviceID:(int)iDeviceID
{
    NSString *strUrl = [NSString stringWithFormat:@"%@%@%d",httpCommonHeader,httpGetLockMemberListID,iDeviceID];
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strGatewayMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    [[HTTPNetworkEngine sharedInstance] requestWithURLString:strUrl
                                                  parameters:nil
                                                        type:HttpRequestTypeGet
                                                       token:strToken
                                                     gateway:strGatewayMacAddr
                                                     success:^(id responseObject){
                                                         
                                                         NSDictionary *jsonDict = (NSDictionary *)responseObject;
                                                         if (jsonDict) {
                                                             if ([jsonDict[@"success"] intValue]) {
                                                                 NSLog(@"获取LockMemberlist-成功 == %@",jsonDict);
                                                                 NSArray *arrResult = jsonDict[@"result"];
                                                                 self.arrLockMemberList = [self handleGetModel:arrResult];
                                                                 //数据库存储
                                                                 NSString *strJson = [ToolCommon dictionaryToJson:jsonDict];
                                                                 [[SHDataBaseManager sharedInstance] doDeleteLockMemberListDataWithDeviceID:iDeviceID];
                                                                 [[SHDataBaseManager sharedInstance] doHandleInsertLockMemDataWithDeviceID:iDeviceID jsonStr:strJson];
                                                             }else{
                                                                 NSDictionary *dictError = jsonDict[@"error"];
                                                                 self.errorInfo = dictError;
                                                                 if (dictError) {
                                                                     NSLog(@"获取LockMemberlist：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                                                                 }
                                                             }
                                                         }
                                                         
                                                     } failure:^(NSError *error) {
                                                         self.errorInfo = @{@"code":@"000",@"message":[error localizedDescription]};
                                                         if (error) {
                                                             LLog([NSString stringWithFormat:@"获取LockMemberlist-网络请求错误信息: %@",[error localizedDescription]]);
                                                         }
                                                     }];
}


- (void)doGetLockMemberListDataFromDBWithDeviceID:(int)iDeviceID
{
    NSString *strJson = [[SHDataBaseManager sharedInstance] doQueryLockMemberListDataWithDeviceID:iDeviceID];
    if (strJson) {
        NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
        NSArray *arrResult = jsonDict[@"result"];
        self.arrLockMemberList = [self handleGetModel:arrResult];
    }
}


#pragma mark - 根据DeviceMacAddr获取LoackMemberList
- (void)doGetLockMemberListFromNetworkWithDeviceMacAddr:(NSString *)strDeviceMacAddr
{
    NSString *strUrl = [NSString stringWithFormat:@"%@%@%@",httpCommonHeader,httpGetLockMemberListAddr,strDeviceMacAddr];
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strGatewayMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    [[HTTPNetworkEngine sharedInstance] requestWithURLString:strUrl
                                                  parameters:nil
                                                        type:HttpRequestTypeGet
                                                       token:strToken
                                                     gateway:strGatewayMacAddr
                                                     success:^(id responseObject){
                                                         NSDictionary *jsonDict = (NSDictionary *)responseObject;
                                                         if (jsonDict) {
                                                             if ([jsonDict[@"success"] intValue]) {
                                                                 NSLog(@"获取LockMemberlist-成功 == %@",jsonDict);
                                                                 NSArray *arrResult = jsonDict[@"result"];
                                                                 self.arrLockMemberList = [self handleGetModel:arrResult];
                                                                 //数据库存储
                                                                 NSString *strJson = [ToolCommon dictionaryToJson:jsonDict];
                                                                 [[SHDataBaseManager sharedInstance] doDeleteLockMemberListDataWithDeviceMacAddr:strDeviceMacAddr];
                                                                 [[SHDataBaseManager sharedInstance] doHandleInsertLockMemDataWithDeviceMacAddr:strDeviceMacAddr jsonStr:strJson];
                                                             }else{
                                                                 NSDictionary *dictError = jsonDict[@"error"];
                                                                 self.errorInfo = dictError;
                                                                 if (dictError) {
                                                                     NSLog(@"获取LockMemberlist：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                                                                 }
                                                             }
                                                         }
                                                         
                                                     } failure:^(NSError *error) {
                                                         self.errorInfo = @{@"code":@"000",@"message":[error localizedDescription]};
                                                         if (error) {
                                                             LLog([NSString stringWithFormat:@"获取LockMemberlist-网络请求错误信息: %@",[error localizedDescription]]);
                                                         }
                                                     }];
}


- (void)doGetLockMemberListDataFromDBWithDeviceMacAddr:(NSString *)strDeviceMacAddr
{
    NSString *strJson = [[SHDataBaseManager sharedInstance] doQueryLockMemberListDataWithDeviceMacAddr:strDeviceMacAddr];
    if (strJson) {
        NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
        NSArray *arrResult = jsonDict[@"result"];
        self.arrLockMemberList = [self handleGetModel:arrResult];
    }
}

- (NSMutableArray *)handleGetModel:(NSArray *)result
{
    NSMutableArray *mutArr = [NSMutableArray new];
    
    for (int i = 0; i < result.count; i ++) {
        SHLockMemModel *lockMem = [SHLockMemModel new];
        NSDictionary *dict = result[i];
        lockMem.iDeviceID = [dict[@"device_id"] intValue];
        lockMem.iLockID = [dict[@"lock_id"] intValue];
        lockMem.strLock_fingerprintId = dict[@"fingerprintId"];
        lockMem.strLock_user_name = dict[@"user_name"];
        lockMem.iUnlock_times = [dict[@"unlock_times"] intValue];
        [mutArr addObject:lockMem];
    }
    return mutArr;
}


#pragma mark -
#pragma mark - 添加门锁密码
- (void)handleTheAddLockPswDataWithDeviceID:(int)iDeviceID
                                        psw:(NSString *)strPsw
                             completeHandle:(BlockLockManagerHandleComplete)callBack{
    
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSString *strPswMd5 = [ToolCommon MD5FromString:strPsw];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"device_id"] = @(iDeviceID);
    params[@"unlock_psw"] = strPswMd5;
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpAddLockPsw];
    [[HTTPNetworkEngine sharedInstance] requestWithURLString:strUrl
                                                  parameters:params
                                                        type:HttpRequestTypePost
                                                       token:strToken
                                                     gateway:strZigbeeMacAddr
                                                     success:^(id responseObject)
     {
         //注册成功后需要保存token
         NSDictionary *jsonDict = (NSDictionary *)responseObject;
         if (jsonDict) {
             if ([jsonDict[@"success"] intValue]) {
                 NSLog(@"添加门锁密码成功 == %@",jsonDict);
                 
                 if (callBack) {
                     callBack(YES,nil);
                 }
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"添加门锁密码成功：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             LLog([NSString stringWithFormat:@"添加子账号-网络请求错误信息: %@",[error localizedDescription]]);
             if (callBack) {
                 callBack(NO,[error localizedDescription]);
             }
         }
     }];
}

#pragma mark - 添加指纹用户
- (void)handleTheAddLockMemberDataWithDeviceID:(int)iDeviceID
                                    memberName:(NSString *)strMemberName
                                 fingerprintId:(NSString *)strFingerprintId
                             completeHandle:(BlockLockManagerHandleComplete)callBack{
    
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"device_id"] = @(iDeviceID);
    params[@"user_name"] = strMemberName;
    params[@"fingerprint_id"] = [NSString stringWithFormat:@"%d",[strFingerprintId intValue]];
    
//    params[@"fingerprint_id"] = strFingerprintId;
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpAddLockMember];
    [[HTTPNetworkEngine sharedInstance] requestWithURLString:strUrl
                                                  parameters:params
                                                        type:HttpRequestTypePost
                                                       token:strToken
                                                     gateway:strZigbeeMacAddr
                                                     success:^(id responseObject)
     {
         //注册成功后需要保存token
         NSDictionary *jsonDict = (NSDictionary *)responseObject;
         if (jsonDict) {
             if ([jsonDict[@"success"] intValue]) {
                 NSLog(@"添加锁用户成功 == %@",jsonDict);
                 NSDictionary *dict = jsonDict[@"result"];
                 SHLockMemModel *lockModel = [SHLockMemModel new];
                 lockModel.iDeviceID = [dict[@""] intValue];
                 lockModel.iLockID = [dict[@""] intValue];
                 lockModel.strLock_user_name = dict[@""];
                 lockModel.strLock_fingerprintId = dict[@""];
                 lockModel.iUnlock_times = [dict[@"unlock_times"] intValue];
                 
                 if (callBack) {
                     callBack(YES,lockModel);
                 }
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"添加锁用户失败：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             LLog([NSString stringWithFormat:@"添加锁用户失败-网络请求错误信息: %@",[error localizedDescription]]);
             if (callBack) {
                 callBack(NO,[error localizedDescription]);
             }
         }
     }];
}

#pragma mark -
#pragma mark - 删除锁用户
- (void)doDeleteLockMemberWithLockID:(int)iLockID
                            lockType:(NSString *)strLockType
                      completeHandle:(BlockLockManagerHandleComplete)callBack{
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"lock_id"] = @(iLockID);
    params[@"lock_type"] = strLockType;
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpDeleteLockMember];
    [[HTTPNetworkEngine sharedInstance] requestWithURLString:strUrl
                                                  parameters:params
                                                        type:HttpRequestTypePost
                                                       token:strToken
                                                     gateway:strZigbeeMacAddr
                                                     success:^(id responseObject)
     {
         //注册成功后需要保存token
         NSDictionary *jsonDict = (NSDictionary *)responseObject;
         if (jsonDict) {
             if ([jsonDict[@"success"] intValue]) {
                 NSLog(@"删除门锁用户 == %@",jsonDict);
                 
                 if (callBack) {
                     callBack(YES,nil);
                 }
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"删除门锁用户：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             LLog([NSString stringWithFormat:@"删除房间-网络请求错误信息: %@",[error localizedDescription]]);
             if (callBack) {
                 callBack(NO,[error localizedDescription]);
             }
         }
     }];
}



- (void)doGetLockPswListFromNetworkWithDeviceID:(int)iDeviceID
{
    NSString *strUrl = [NSString stringWithFormat:@"%@%@%d",httpCommonHeader,httpGetLockPswList,iDeviceID];
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strGatewayMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    [[HTTPNetworkEngine sharedInstance] requestWithURLString:strUrl
                                                  parameters:nil
                                                        type:HttpRequestTypeGet
                                                       token:strToken
                                                     gateway:strGatewayMacAddr
                                                     success:^(id responseObject){
                                                         
                                                         NSDictionary *jsonDict = (NSDictionary *)responseObject;
                                                         if (jsonDict) {
                                                             if ([jsonDict[@"success"] intValue]) {
                                                                 NSLog(@"获取LockPswlist-成功 == %@",jsonDict);
                                                                 NSArray *arrResult = jsonDict[@"result"];
                                                                 self.arrLockPswList = [self handleGetLockPswList:arrResult];
                                                             }else{
                                                                 NSDictionary *dictError = jsonDict[@"error"];
                                                                 self.errorInfo = dictError;
                                                                 if (dictError) {
                                                                     NSLog(@"获取LockPswlist：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                                                                 }
                                                             }
                                                         }
                                                         
                                                     } failure:^(NSError *error) {
                                                         self.getLockPswListErrorInfo = @{@"code":@"000",@"message":[error localizedDescription]};
                                                         if (error) {
                                                             LLog([NSString stringWithFormat:@"获取LockPswlist-网络请求错误信息: %@",[error localizedDescription]]);
                                                         }
                                                     }];
}

- (NSArray *)handleGetLockPswList:(NSArray *)arrResult
{
    NSMutableArray *mutArr = [NSMutableArray new];
    
    for (int i = 0; i < arrResult.count; i ++) {
        SHLockPswModel *lockMem = [SHLockPswModel new];
        NSDictionary *dict = arrResult[i];
        lockMem.iDeviceID = [dict[@"device_id"] intValue];
        lockMem.iLockID = [dict[@"lock_id"] intValue];
        lockMem.strUnlockPsw = dict[@"unlock_psw"];
        lockMem.iUnLockTimes = [dict[@"unlock_times"] intValue];
        [mutArr addObject:lockMem];
    }
    return mutArr;
}


- (void)handleToOpenTheLockWithDeviceID:(int)iDeviceID
                              unLockPsw:(NSString *)strUnLockPsw
                         completeHandle:(BlockLockManagerHandleComplete)callBack
{
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSString *strPswMd5 = [ToolCommon MD5FromString:strUnLockPsw];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"device_id"] = @(iDeviceID);
    params[@"unlock_psw"] = strPswMd5;
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpDoOpenLock];
    [[HTTPNetworkEngine sharedInstance] requestWithURLString:strUrl
                                                  parameters:params
                                                        type:HttpRequestTypePost
                                                       token:strToken
                                                     gateway:strZigbeeMacAddr
                                                     success:^(id responseObject)
     {
         NSDictionary *jsonDict = (NSDictionary *)responseObject;
         if (jsonDict) {
             if ([jsonDict[@"success"] intValue]) {
                 NSLog(@"比对锁密码成功 == %@",jsonDict);
                 
                 if (callBack) {
                     callBack(YES,nil);
                 }
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"比对门锁密码成功：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             LLog([NSString stringWithFormat:@"比对子账号-网络请求错误信息: %@",[error localizedDescription]]);
             if (callBack) {
                 callBack(NO,[error localizedDescription]);
             }
         }
     }];
}





@end
