//
//  GatewayNetManager.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "GatewayNetManager.h"

@implementation GatewayNetManager

- (void)handleTheAddGatewayDataCompleteHandle:(GetAddGatewayCallBack)callBack{
    
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strGatewayName = [[SHLoginManager shareInstance] doGetRememerGatewayName];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    NSString *strWifiMacAddr = [[SHLoginManager shareInstance] doGetWifiMacAddr];
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"gateway_name"] = strGatewayName;
    params[@"mac_address"] = strZigbeeMacAddr;
    params[@"wifi_mac_address"] = strWifiMacAddr;
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpAddGateway];
    [[HTTPNetworkEngine sharedInstance] requestWithURLString:strUrl
                                                  parameters:params
                                                        type:HttpRequestTypePost
                                                       token:strToken
                                                     gateway:nil
                                                     success:^(id responseObject)
     {
         //注册成功后需要保存token
         NSDictionary *jsonDict = (NSDictionary *)responseObject;
         if (jsonDict) {
             if ([jsonDict[@"success"] intValue]) {
                 NSLog(@"添加网关成功 == %@",jsonDict);
                 NSDictionary *dictResult = jsonDict[@"result"];
                 NSArray *arrGateways = dictResult[@"user_gateways"];
                 NSMutableArray *mutArr = [NSMutableArray new];
                 for (int i = 0; i < arrGateways.count; i ++) {
                     NSDictionary *dictTemp = arrGateways[i];
                     SHModelGateway *model = [SHModelGateway new];
                     model.iGateway_gateway_id = [dictTemp[@"gateway_id"] intValue];
                     model.strGateway_mac_address = dictTemp[@"mac_address"];
                     model.strGateway_wifi_mac_address = dictTemp[@"wifi_mac_address"];
                     model.iGateway_member_type = [dictTemp[@"member_type"] intValue];
                     model.strGateway_gateway_name = dictTemp[@"gateway_name"];
                     [mutArr addObject:model];
                 }
                 if (callBack) {
                     callBack(YES,mutArr);
                 }
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"添加网关：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             LLog([NSString stringWithFormat:@"添加网关-网络请求错误信息:%@",[error localizedDescription]]);
             
         }
     }];
}

- (void)handleGetGatewayListFromNetwork
{
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpGatewayList];
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    [[HTTPNetworkEngine sharedInstance] requestWithURLString:strUrl
                                                  parameters:nil
                                                        type:HttpRequestTypeGet
                                                       token:strToken
                                                     gateway:nil
                                                     success:^(id responseObject){
                                                         
                                                         NSDictionary *jsonDict = (NSDictionary *)responseObject;
                                                         if (jsonDict) {
                                                             if ([jsonDict[@"success"] intValue]) {
                                                                 NSLog(@"获取网关列表-成功 == %@",jsonDict);
                                                                 //数据库存储
                                                                 NSString *strJson = [ToolCommon dictionaryToJson:jsonDict];
                                                                 [[SHDataBaseManager sharedInstance] doDeleteDataWithIdentifer:SHIdentifer_GatewayList];
                                                                 [[SHDataBaseManager sharedInstance] doHandleInsertDataWithIdentifer:SHIdentifer_GatewayList jsonStr:strJson];
                                                                 self.arrGatewayList = [self doHandleGetGatewayList:jsonDict];
                                                                 
                                                             }else{
                                                                 NSDictionary *dictError = jsonDict[@"error"];
                                                                 if (dictError) {
                                                                     NSLog(@"获取网关列表：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                                                                     self.errorInfo = dictError;
                                                                 }
                                                             }
                                                         }
                                                         
                                                     } failure:^(NSError *error) {
                                                         if (error) {
                                                             self.errorInfo = @{@"code":@"000",@"message":[error localizedDescription]};
                                                             LLog([NSString stringWithFormat:@"添加网关-网络请求错误信息:%@",[error localizedDescription]]);
                                                         }
                                                     }];
}

- (void)doGetGatewayListDataFromDB
{
    NSString *strJson = [[SHDataBaseManager sharedInstance] doQueryDataWithIdentifer:SHIdentifer_GatewayList];
    if (strJson) {
        NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
        self.arrGatewayList = [self doHandleGetGatewayList:jsonDict];
    }
}


- (void)handleAddGatewayToServer:(NSString *)strGatewayZigbeeMacAddr
                     gatewayName:(NSString *)name
                  callBackHandle:(LoginRspCallBack)callBack
{
    
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpAddGateway];
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"gateway_name"] = name;
    params[@"mac_address"] = strGatewayZigbeeMacAddr;
    [[HTTPNetworkEngine sharedInstance] requestWithURLString:strUrl
                                                  parameters:params
                                                        type:HttpRequestTypePost
                                                       token:strToken
                                                     gateway:nil
                                                     success:^(id responseObject)
     {
         NSDictionary *jsonDict = (NSDictionary *)responseObject;
         if (jsonDict) {
             if ([jsonDict[@"success"] intValue]) {
                 NSLog(@"添加网关-成功 == %@",jsonDict);
                 NSDictionary *dictResult = jsonDict[@"result"];
                 NSArray *arrResult = dictResult[@"user_gateways"];
                 NSMutableArray *mutArr = [NSMutableArray new];
                 for (int i = 0; i < arrResult.count; i ++) {
                     NSDictionary *dict = arrResult[i];
                     SHModelGateway *model = [SHModelGateway new];
                     model.iGateway_gateway_id = [dict[@"gateway_id"] intValue];
                     model.strGateway_mac_address = dict[@"mac_address"];
                     model.strGateway_wifi_mac_address = dict[@"wifi_mac_address"];
                     model.iGateway_member_type = [dict[@"member_type"] intValue];
                     model.strGateway_gateway_name = dict[@"gateway_name"];
                     model.iSecurityStatus = [dict[@"security_status"] intValue];
                     [mutArr addObject:model];
                 }
                 if (callBack) {
                     callBack(YES,mutArr);
                 }
             }else{
                 NSDictionary *dictError = jsonDict[@"error"];
                 if (dictError) {
                     NSLog(@"添加网关：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                     NSString *strMessage = [NSString stringWithFormat:@"%@%@",dictError[@"message"],dictError[@"code"]];
                     if (callBack) {
                         callBack(NO,strMessage);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             if (callBack) {
                 callBack(NO,[error localizedDescription]);
             }
             LLog([NSString stringWithFormat:@"添加网关-网络请求错误信息:%@",[error localizedDescription]]);
         }
     }];
    
}

#pragma mark -
#pragma mark - private
- (NSArray *)doHandleGetGatewayList:(NSDictionary *)jsonDict
{
    NSDictionary *dictResult = jsonDict[@"result"];
    NSArray *arrResult = dictResult[@"user_gateways"];
    NSMutableArray *mutArr = [NSMutableArray new];
    for (int i = 0; i < arrResult.count; i ++) {
        NSDictionary *dict = arrResult[i];
        SHModelGateway *model                   = [SHModelGateway new];
        model.iGateway_gateway_id               = [dict[@"gateway_id"] intValue];
        model.strGateway_mac_address            = dict[@"mac_address"];
        model.iGateway_member_type              = [dict[@"member_type"] intValue];
        model.strGateway_gateway_name           = dict[@"gateway_name"];
        model.strGateway_wifi_mac_address       = dict[@"wifi_mac_address"];
        model.iSecurityStatus                   = [dict[@"security_status"] intValue];
        [mutArr addObject:model];
    }
    return mutArr;
}



- (void)handleDeleteGatewayFromServer:(NSString *)strGatewayZigbeeMacAddr CallBackHandle:(DeleteGatewayCallBack)callBack
{
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpDeleteGateway];
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strGatewayMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"gateway_mac"] = strGatewayZigbeeMacAddr;
    [[HTTPNetworkEngine sharedInstance] requestWithURLString:strUrl
                                                  parameters:params
                                                        type:HttpRequestTypePost
                                                       token:strToken
                                                     gateway:strGatewayMacAddr
                                                     success:^(id responseObject)
     {
         NSDictionary *jsonDict = (NSDictionary *)responseObject;
         if (jsonDict) {
             if ([jsonDict[@"success"] intValue]) {
                 NSLog(@"删除网关-成功 == %@",jsonDict);
                 if (callBack) {
                     callBack(YES,nil);
                 }
             }else{
                 NSDictionary *dictError = jsonDict[@"error"];
                 if (dictError) {
                     NSLog(@"删除网关：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                     NSString *strMessage = [NSString stringWithFormat:@"%@%@",dictError[@"message"],dictError[@"code"]];
                     if (callBack) {
                         callBack(NO,strMessage);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             if (callBack) {
                 callBack(NO,[error localizedDescription]);
             }
             LLog([NSString stringWithFormat:@"添加网关-网络请求错误信息:%@",[error localizedDescription]]);
         }
     }];
    
}


- (void)doGetGatewayMemberDetailInfoWithGatewayMacAddr:(NSString *)strGatewayMacAddr
                                        callBackHandle:(GetGatewayMemInfoDetailCallBack)callBack
{
    
    NSString *strUrl = [NSString stringWithFormat:@"%@%@%@",httpCommonHeader,httpGetGateayMemberInfo,strGatewayMacAddr];
    [[HTTPNetworkEngine sharedInstance] requestWithURLString:strUrl
                                                  parameters:nil
                                                        type:HttpRequestTypeGet
                                                       token:nil
                                                     gateway:nil
                                                     success:^(id responseObject){
                                                         
                                                         NSDictionary *jsonDict = (NSDictionary *)responseObject;
                                                         if (jsonDict) {
                                                             if ([jsonDict[@"success"] intValue]) {
                                                                 NSLog(@"获取网关用户详情信息-成功 == %@",jsonDict);
                                                                 if (callBack) {
                                                                     callBack(YES,jsonDict[@"result"]);
                                                                 }
                                                                 
                                                             }else{
                                                                 NSDictionary *dictError = jsonDict[@"error"];
                                                                 if (dictError) {
                                                                     NSLog(@"获取网关用户详情信息：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
//                                                                     NSString *strMessage = [NSString stringWithFormat:@"%@%@",dictError[@"message"],dictError[@"code"]];
                                                                     if (callBack) {
                                                                         callBack(NO,dictError[@"code"]);
                                                                     }
                                                                 }
                                                             }
                                                         }
                                                         
                                                     } failure:^(NSError *error) {
                                                         if (error) {
                                                             self.errorInfo = @{@"code":@"000",@"message":[error localizedDescription]};
                                                             LLog([NSString stringWithFormat:@"添加网关-网络请求错误信息:%@",[error localizedDescription]]);
                                                         }
                                                     }];
}


@end
