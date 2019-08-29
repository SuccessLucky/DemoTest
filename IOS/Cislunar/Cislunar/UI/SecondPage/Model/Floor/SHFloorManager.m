//
//  SHFloorManager.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/25.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHFloorManager.h"

@implementation SHFloorManager
- (void)doGetFloorListFromNetwork
{
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpGetFloorList];
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
                                                                 NSLog(@"获取楼层list-成功 == %@",jsonDict);
                                                                 NSArray *arrResult = jsonDict[@"result"];
                                                                 self.arrFloor = [self handleGetModel:arrResult];
                                                                 //数据库存储
                                                                 NSString *strJson = [ToolCommon dictionaryToJson:jsonDict];
                                                                 [[SHDataBaseManager sharedInstance] doDeleteDataWithIdentifer:SHIdentifer_Floor];
                                                                 [[SHDataBaseManager sharedInstance] doHandleInsertDataWithIdentifer:SHIdentifer_Floor jsonStr:strJson];
                                                             }else{
                                                                 NSDictionary *dictError = jsonDict[@"error"];
                                                                 self.errorInfo = dictError;
                                                                 if (dictError) {
                                                                     NSLog(@"获取楼层list：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                                                                 }
                                                             }
                                                         }
                                                         
                                                     } failure:^(NSError *error) {
                                                         self.errorInfo = @{@"code":@"000",@"message":[error localizedDescription]};
                                                         if (error) {
                                                             NSLog(@"获取楼层list-网络请求错误信息: %@",[error localizedDescription]);
                                                         }
                                                     }];
}


- (void)doGetFloorListDataFromDB
{
    NSString *strJson = [[SHDataBaseManager sharedInstance] doQueryDataWithIdentifer:SHIdentifer_Floor];
    if (strJson) {
        NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
        NSArray *arrResult = jsonDict[@"result"];
        self.arrFloor = [self handleGetModel:arrResult];
    }
}

- (NSMutableArray *)handleGetModel:(NSArray *)result
{
    NSMutableArray *mutArr = [NSMutableArray new];
    
    for (int i = 0; i < result.count; i ++) {
        SHModelFloor *floor = [SHModelFloor new];
        NSDictionary *dict = result[i];
        floor.iFloor_id = [dict[@"id"] intValue];
        floor.strFloor_name = dict[@"name"];
        floor.strFloor_image = dict[@"image"];
        [mutArr addObject:floor];
    }
    return mutArr;
}


- (void)handleTheAddFloorDataWithModel:(SHModelFloor *)model
                           completeHandle:(GetFloorListCallBack)callBack{
    
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"floor_name"] = model.strFloor_name;
    params[@"image"] = model.strFloor_image;
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpAddFloor];
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
                 NSLog(@"添加新的楼层 == %@",jsonDict);
                 NSDictionary *dictResult = jsonDict[@"result"];
                 SHModelFloor *floor = [SHModelFloor new];
                 floor.strFloor_name = dictResult[@"name"];
                 floor.strFloor_image = dictResult[@"image"];
                 floor.iFloor_id = [dictResult[@"id"] intValue];
                 if (callBack) {
                     callBack(YES,floor);
                 }
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"添加新的楼层：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             NSLog(@"添加新的楼层-网络请求错误信息: %@",[error localizedDescription]);
         }
     }];
}

- (void)handleTheDelteFloorDataWithModel:(SHModelFloor *)model
                        completeHandle:(GetFloorListCallBack)callBack{
    
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"floor_id"] = @(model.iFloor_id);
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpDeleteFloor];
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
                     NSLog(@"删除楼层：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             NSLog(@"删除楼层-网络请求错误信息: %@",[error localizedDescription]);
         }
     }];
}

- (void)handleTheUpdateFloorDataWithModel:(SHModelFloor *)model
                             floorNewName:(NSString *)floorNewName
                          completeHandle:(GetFloorListCallBack)callBack{
    
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"floor_name"] = floorNewName;
    params[@"image"] = model.strFloor_image;
    params[@"floor_id"] = @(model.iFloor_id);
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpModifyFloor];
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
                     NSLog(@"删除楼层：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             NSLog(@"删除楼层-网络请求错误信息: %@",[error localizedDescription]);
         }
     }];
}




@end
