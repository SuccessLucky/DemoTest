//
//  SHMemberManager.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/19.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHMemberManager.h"
#import "SHMemberModel.h"

@implementation SHMemberManager

- (void)doGetMemberListFromNetwork
{
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpGetMemberList];
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
                                                                 NSLog(@"获取子账号list-成功 == %@",jsonDict);
                                                                 NSArray *arrResult = jsonDict[@"result"];
                                                                 self.arrMemberList = [self handleGetModel:arrResult];
                                                                 //数据库存储
                                                                 NSString *strJson = [ToolCommon dictionaryToJson:jsonDict];
                                                                 [[SHDataBaseManager sharedInstance] doDeleteDataWithIdentifer:SHIdentifer_MemberList];
                                                                 [[SHDataBaseManager sharedInstance] doHandleInsertDataWithIdentifer:SHIdentifer_MemberList jsonStr:strJson];
                                                             }else{
                                                                 NSDictionary *dictError = jsonDict[@"error"];
                                                                 self.errorInfo = dictError;
                                                                 if (dictError) {
                                                                     NSLog(@"获取子账号list：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                                                                 }
                                                             }
                                                         }
                                                         
                                                     } failure:^(NSError *error) {
                                                         self.errorInfo = @{@"code":@"000",@"message":[error localizedDescription]};
                                                         if (error) {
                                                             NSLog(@"获取子账号-网络请求错误信息: %@",[error localizedDescription]);
                                                         }
                                                     }];
}


- (void)doGetMemberListFromDB
{
    NSString *strJson = [[SHDataBaseManager sharedInstance] doQueryDataWithIdentifer:SHIdentifer_MemberList];
    if (strJson) {
        NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
        NSArray *arrResult = jsonDict[@"result"];
        self.arrMemberList = [self handleGetModel:arrResult];
    }
}

- (NSMutableArray *)handleGetModel:(NSArray *)result
{
    NSMutableArray *mutArr = [NSMutableArray new];
    for (int i = 0; i < result.count; i ++) {
        SHMemberModel *member = [SHMemberModel new];
        NSDictionary *dict = result[i];
        member.iMember_id = [dict[@"member_id"] intValue];
        member.str_member_name = dict[@"member_name"];
        member.iMember_type = [dict[@"member_type"] intValue];
        member.str_image = dict[@"image"];
        member.str_account = dict[@"account"];
        [mutArr addObject:member];
    }
    return mutArr;
}

#pragma mark -
#pragma mark - 添加成员
- (void)handleTheAddMemberListDataWithModel:(NSString *)strAccount
                             completeHandle:(GetMemberListCallBack)callBack{
    
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"phone"] = strAccount;
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpAddMember];
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
                 NSLog(@"添加子账号 == %@",jsonDict);
                 NSDictionary *dictResult = jsonDict[@"result"];
                 SHMemberModel *memberModel = [SHMemberModel new];
                 memberModel.iMember_id = [dictResult[@"member_id"] intValue];
                 memberModel.str_member_name = dictResult[@"member_name"];
                 memberModel.str_image=  dictResult[@"image"];
                 memberModel.iMember_type = [dictResult[@"member_type"] intValue];
                 memberModel.str_account = dictResult[@"account"];
                 if (callBack) {
                     callBack(YES,memberModel);
                 }
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"添加子账号：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             NSLog(@"添加子账号-网络请求错误信息: %@",[error localizedDescription]);
             if (callBack) {
                 callBack(NO,[error localizedDescription]);
             }
         }
     }];
}

#pragma mark - 删除
- (void)handleTheDeleteMemberListDataWithMemberID:(int)iMemberId
                             completeHandle:(GetMemberListCallBack)callBack{
    
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"member_id"] = @(iMemberId);
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpDeleteMember];
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
                 
                 if (callBack) {
                     callBack(YES,nil);
                 }
                 
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"删除子账号：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             NSLog(@"删除子账号-网络请求错误信息: %@",[error localizedDescription]);
         }
     }];
}

- (void)doGetMemberRightListFromNetworkWithMemberID:(int)memberId
{
    NSString *strUrl = [NSString stringWithFormat:@"%@%@%d",httpCommonHeader,httpGetMemberRightList,memberId];
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
                                                                 NSLog(@"获取子账号Devicelist-成功 == %@",jsonDict);
                                                                 NSDictionary *dict = jsonDict[@"result"];
                                                                 self.dictControlList = [self doGetMemberControlList:dict];
                                                                 //数据库存储
                                                                 NSString *strJson = [ToolCommon dictionaryToJson:jsonDict];
                                                                 [[SHDataBaseManager sharedInstance] doDeleteMemberControlListDataWithMemberID:memberId];
                                                                 [[SHDataBaseManager sharedInstance] doHandleInsertMemberControlListDataWithMemberID:memberId jsonStr:strJson];
                                                             }else{
                                                                 NSDictionary *dictError = jsonDict[@"error"];
                                                                 self.controlListErrorInfo = dictError;
                                                                 if (dictError) {
                                                                     NSLog(@"获取子账号Devicelist：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                                                                 }
                                                             }
                                                         }
                                                         
                                                     } failure:^(NSError *error) {
                                                         self.controlListErrorInfo = @{@"code":@"000",@"message":[error localizedDescription]};
                                                         if (error) {
                                                             NSLog(@"获取子账号Devicelist-网络请求错误信息: %@",[error localizedDescription]);
                                                         }
                                                     }];
}

- (void)doGetMemberRightListFromDBWithMemberID:(int)iMemberID
{
    NSString *strJson = [[SHDataBaseManager sharedInstance] doQueryMemberControlListDataWithMemberID:iMemberID];
    if (strJson) {
        NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
        NSDictionary *dictResult = jsonDict[@"result"];
        self.dictControlList = [self doGetMemberControlList:dictResult];
    }
}


- (NSDictionary *)doGetMemberControlList:(NSDictionary *)dict
{
    NSMutableArray *mutArrScreenList = [NSMutableArray new];
    NSMutableArray *mutArrDeviceList = [NSMutableArray new];
    
    NSArray *arrScreenList = dict[@"scenes"];
    NSArray *arrDeviceList = dict[@"devices"];
    
    for (int k = 0; k < arrScreenList.count; k ++) {
        NSDictionary *dictTemp = arrScreenList[k];
        ScreenModel *screenModel = [ScreenModel new];
        screenModel.iScreen_scene_id = [dictTemp[@"scene_id"] intValue];
        screenModel.strScreen_name = dictTemp[@"name"];
        screenModel.strScreen_image = dictTemp[@"image"];
        [mutArrScreenList addObject:screenModel];
    }
    
    for (int i = 0; i < arrDeviceList.count; i ++) {
        NSDictionary *dictTemp = arrDeviceList[i];
        SHModelDevice *device = [SHModelDevice new];
        device.iDevice_device_id = [dictTemp[@"device_id"] intValue];
        device.strDevice_device_name = dictTemp[@"device_name"];
        device.strDevice_image = dictTemp[@"image"];
        device.iDevice_room_id = [dictTemp[@"room_id"] intValue];
        device.iDevice_floor_id = [dictTemp[@"floor_id"] intValue];
        device.strDevice_room_name = dictTemp[@"room_name"];
        device.strDevice_floor_name = dictTemp[@"floor_name"];
        [mutArrDeviceList addObject:device];
    }
    
    NSDictionary *dictNew = @{@"MemberScreenList":mutArrScreenList,
                              @"MemberDeviceList":mutArrDeviceList};
    return dictNew;
}

#pragma mark - 添加子账号权限
- (void)handleTheAddMemberRightListDataWithMemberID:(int)iMemberID
                                          arrScreen:(NSArray *)arrScreen
                                          arrDevice:(NSArray *)arrDeice
                             completeHandle:(GetMemberControlListCallBack)callBack{
    
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"member_id"] = @(iMemberID);
    params[@"scenes"] = arrScreen;
    params[@"devices"] = arrDeice;
    
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpAddMemberRight];
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
                 NSLog(@"添加子账号 == %@",jsonDict);
                 NSDictionary *dictResult = jsonDict[@"result"];
                 NSDictionary *dictTemp = [self doGetMemberControlList:dictResult];
                 if (callBack) {
                     callBack(YES,dictTemp);
                 }
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"添加子账号：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             NSLog(@"添加子账号-网络请求错误信息: %@",[error localizedDescription]);
             if (callBack) {
                 callBack(NO,[error localizedDescription]);
             }
         }
     }];
}





@end
