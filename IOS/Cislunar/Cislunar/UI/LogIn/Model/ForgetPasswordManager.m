//
//  ForgetPasswordManager.m
//  SmartHouseYCT
//
//  Created by apple on 16/9/10.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "ForgetPasswordManager.h"

@implementation ForgetPasswordManager

- (instancetype)init {
    self = [super init];
    if (self) {
    }
    return self;
}

- (void)handleRequestGetResetPswCode:(NSString *)telNum complete:(RestPswCodeCallBack)completeHandle
{
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpResetPswCode];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"phone"] = telNum;
    [[HTTPNetworkEngine sharedInstance] requestWithURLString:strUrl
                                                  parameters:params
                                                        type:HttpRequestTypePost
                                                       token:nil
                                                     gateway:nil
                                                     success:^(id responseObject)
     {
         NSDictionary *jsonDict = (NSDictionary *)responseObject;
         if (jsonDict) {
             if ([jsonDict[@"success"] intValue]) {
                 NSLog(@"重置密码-发送验证码 == %@",jsonDict);
                 if (completeHandle) {
                     completeHandle(YES,jsonDict);
                 }
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"重置密码-发送验证码：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (completeHandle) {
                         completeHandle(NO,dict[@"message"]);
                     }
                 }
             }
         }
     }failure:^(NSError *error){
         LLog_Error([NSString stringWithFormat:@"网络请求错误信息: %@",[error localizedDescription]]);
     }];
}



- (void)handleRequestResetPswCodeWithTelNum:(NSString *)strTelNum
                                       code:(NSString *)strCode
                                     newPsw:(NSString *)strNewPsw
                                   complete:(RestPswCodeCallBack)completeHandle
{
    NSString *strPswMd5 = [ToolCommon MD5FromString:strNewPsw];
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpResetPsw];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"code"] = strCode;
    params[@"phone"] = strTelNum;
    params[@"new_password"] = strPswMd5;
    
    [[HTTPNetworkEngine sharedInstance] requestWithURLString:strUrl
                                                  parameters:params
                                                        type:HttpRequestTypePost
                                                       token:nil
                                                     gateway:nil
                                                     success:^(id responseObject)
     {
         NSDictionary *jsonDict = (NSDictionary *)responseObject;
         if (jsonDict) {
             if ([jsonDict[@"success"] intValue]) {
                 NSLog(@"重置成功 == %@",jsonDict);
                 if (completeHandle) {
                     completeHandle(YES,jsonDict);
                 }
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"重置密码失败：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (completeHandle) {
                         completeHandle(NO,dict[@"message"]);
                     }
                 }
             }
         }
     }failure:^(NSError *error){
         LLog_Error([NSString stringWithFormat:@"网络请求错误信息: %@",[error localizedDescription]]);
     }];
}


@end
