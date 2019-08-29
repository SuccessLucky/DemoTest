//
//  SHRoomManager.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/26.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHRoomManager.h"

@implementation SHRoomManager

- (void)doGetRoomListFromNetworkWithFloorID:(int)iFloorID
{
    NSString *strUrl = [NSString stringWithFormat:@"%@%@?floor_id=%d",httpCommonHeader,httpGetRoomList,iFloorID];
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
                                                                 NSLog(@"获取房间list-成功 == %@",jsonDict);
                                                                 NSArray *arrResult = jsonDict[@"result"];
                                                                 self.arrRoom = [self handleGetModel:arrResult];
                                                                 //数据库存储
                                                                 NSString *strJson = [ToolCommon dictionaryToJson:jsonDict];
                                                                 
                                                                 [[SHDataBaseManager sharedInstance] doDeleteRoomListDataWithFloorID:iFloorID];
                                                                 [[SHDataBaseManager sharedInstance] doHandleInsertRoomListDataWithFloorID:iFloorID jsonStr:strJson];
                                                             }else{
                                                                 NSDictionary *dictError = jsonDict[@"error"];
                                                                 self.errorInfo = dictError;
                                                                 if (dictError) {
                                                                     NSLog(@"获取房间list：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                                                                 }
                                                             }
                                                         }
                                                         
                                                     } failure:^(NSError *error) {
                                                         self.errorInfo = @{@"code":@"000",@"message":[error localizedDescription]};
                                                         if (error) {
                                                             NSLog(@"获取房间list-网络请求错误信息: %@",[error localizedDescription]);
                                                         }
                                                     }];
}


- (void)doGetRoomListDataFromDBWithFloorID:(int)iFloorID
{
    NSString *strJson = [[SHDataBaseManager sharedInstance] doQueryRoomListDataWithFloorID:iFloorID];
    if (strJson) {
        NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
        NSArray *arrResult = jsonDict[@"result"];
        self.arrRoom = [self handleGetModel:arrResult];
    }
}

- (NSMutableArray *)handleGetModel:(NSArray *)result
{
    NSMutableArray *mutArr = [NSMutableArray new];
    
    for (int i = 0; i < result.count; i ++) {
        SHModelRoom *floor = [SHModelRoom new];
        NSDictionary *dict = result[i];
        floor.iRoom_id = [dict[@"id"] intValue];
        floor.strRoom_name = dict[@"name"];
        NSString *strImage = dict[@"image"];
        floor.strRoom_image = strImage;
        floor.iRoom_floor_id = [dict[@"floor_id"] intValue];
        [mutArr addObject:floor];
    }
    return mutArr;
}


- (void)handleTheAddRoomDataWithModel:(SHModelRoom *)model
                       completeHandle:(GetRoomListCallBack)callBack{
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"floor_id"] = @(model.iRoom_floor_id);
    params[@"image"] = model.strRoom_image;
    params[@"room_name"] = model.strRoom_name;
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpAddRoom];
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
                 NSLog(@"添加新的房间 == %@",jsonDict);
                 NSDictionary *dictResult = jsonDict[@"result"];
                 SHModelRoom *room = [SHModelRoom new];
                 room.iRoom_id = [dictResult[@"id"] intValue];
                 room.strRoom_name = dictResult[@"name"];
                 room.strRoom_image = dictResult[@"image"];
                 room.iRoom_floor_id = [dictResult[@"floor_id"] intValue];
                 
                 if (callBack) {
                     callBack(YES,room);
                 }
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"添加新的房间：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             NSLog(@"添加新的房间-网络请求错误信息: %@",[error localizedDescription]);
         }
     }];
}

- (void)handleTheDeleteRoomDataWithRoomId:(int)roomID
                          completeHandle:(GetRoomListCallBack)callBack{
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"room_id"] = @(roomID);
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpDeleteRoom];
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
                 NSLog(@"删除房间 == %@",jsonDict);
                 
                 if (callBack) {
                     callBack(YES,nil);
                 }
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"删除房间：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             NSLog(@"删除房间-网络请求错误信息: %@",[error localizedDescription]);
         }
     }];
}

- (void)handleTheUpdateRoomDataWithModel:(SHModelRoom *)model
                       completeHandle:(GetRoomListCallBack)callBack{
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"room_id"] = @(model.iRoom_id);
    params[@"image"] = model.strRoom_image;
    params[@"room_name"] = model.strRoom_name;
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpUpdateRoom];
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
                 NSLog(@"更新房间 == %@",jsonDict);
                 NSDictionary *dictResult = jsonDict[@"result"];
                 SHModelRoom *room = [SHModelRoom new];
                 room.iRoom_id = [dictResult[@"id"] intValue];
                 room.strRoom_name = dictResult[@"name"];
                 room.strRoom_image = dictResult[@"image"];
                 room.iRoom_floor_id = [dictResult[@"floor_id"] intValue];
                 
                 if (callBack) {
                     callBack(YES,room);
                 }
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"更新房间：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             NSLog(@"更新房间-网络请求错误信息: %@",[error localizedDescription]);
         }
     }];
}





@end
