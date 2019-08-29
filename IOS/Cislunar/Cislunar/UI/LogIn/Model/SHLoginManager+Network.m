//
//  SHLoginManager+Network.m
//  SmartHouseYCT
//
//  Created by apple on 16/9/22.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHLoginManager+Network.h"

@implementation SHLoginManager (Network)

#pragma mark - 登录 -
- (void)handleTheLoginDataWithUserName:(NSString *)userName
                                 password:(NSString *)psw
                           callBackHandle:(LoginRspCallBack)callBack{
    
    NSString *strPswMd5 = [ToolCommon MD5FromString:psw];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"username"] = userName;
    params[@"password"] = strPswMd5;
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpLogin];
    [[HTTPNetworkEngine sharedInstance] requestWithURLString:strUrl
                                                  parameters:params
                                                        type:HttpRequestTypePost
                                                       token:nil
                                                     gateway:nil
                                                     success:^(id responseObject)
     {
         //登录成功后需要保存token
         NSDictionary *jsonDict = (NSDictionary *)responseObject;
         if (jsonDict) {
             if ([jsonDict[@"success"] intValue]) {
                 NSLog(@"登录-成功 == %@",jsonDict);
                 NSDictionary *dictResult = jsonDict[@"result"];
                 NSString *strToken = dictResult[@"token"];
                 [[SHLoginManager shareInstance] doWriteRememberAccount:userName];
                 [[SHLoginManager shareInstance] doWriteRememberPsw:psw];
                 [[SHLoginManager shareInstance] doWriteCurentSignToken:strToken];
                 
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
                 
                 if (callBack) {
                     callBack(YES,mutArr);
                 }
             }else{
                 NSDictionary *dictError = jsonDict[@"error"];
                 if (dictError) {
                     NSLog(@"登录：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
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
             LLog_Error([NSString stringWithFormat:@"登录-网络请求错误信息: %@",[error localizedDescription]]);
         }
     }];
}


- (void)doLogOutFromNetworkCallBack:(BlockLogOutHandleComplete)complete
{
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpLogOut];
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
                                                                 if (complete) {
                                                                     complete(YES,nil);
                                                                 }
                                                             }else{
                                                                 NSDictionary *dictError = jsonDict[@"error"];
                                                                 if (dictError) {
                                                                     NSLog(@"登出：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                                                                     if (complete) {
                                                                         complete(NO,dictError);
                                                                     }
                                                                 }
                                                             }
                                                         }
                                                         
                                                     } failure:^(NSError *error) {
                                                         if (error) {
                                                             LLog_Error([NSString stringWithFormat:@"登出-网络请求错误信息: %@",[error localizedDescription]]);
                                                             if (complete) {
                                                                 complete(NO,error);
                                                             }
                                                         }
                                                     }];
}


@end
