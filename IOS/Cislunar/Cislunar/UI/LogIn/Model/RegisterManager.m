//
//  RegisterManager.m
//  SmartHouseYCT
//
//  Created by apple on 16/9/10.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "RegisterManager.h"

@implementation RegisterManager

#pragma mark - 注册 -
+ (void)handleTheRegisterDataWithUserName:(NSString *)userName
                                 password:(NSString *)psw
                                   mobile:(NSString *)nickName
                         verificationCode:(NSString *)code
                           callBackHandle:(RegisterCallBack)callBack{
    
    NSString *strPswMd5 = [ToolCommon MD5FromString:psw];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"code"] = code;
    params[@"phone"] = userName;
    params[@"password"] = strPswMd5;
    params[@"nickname"] = nickName;
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpRegister];
    [[HTTPNetworkEngine sharedInstance] requestWithURLString:strUrl
                                                  parameters:params
                                                        type:HttpRequestTypePost
                                                       token:nil
                                                     gateway:nil
                                                     success:^(id responseObject)
    {
        //注册成功后需要保存token
        NSDictionary *jsonDict = (NSDictionary *)responseObject;
        if (jsonDict) {
            if ([jsonDict[@"success"] intValue]) {
                NSLog(@"注册 == %@",jsonDict);
                [[SHLoginManager shareInstance] doWriteRememberAccount:userName];
                if (callBack) {
                    callBack(YES,jsonDict);
                }
            }else{
                NSDictionary *dict = jsonDict[@"error"];
                if (dict) {
                    NSLog(@"注册：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                    if (callBack) {
                        callBack(NO,dict[@"message"]);
                    }
                }
            }
        }
    } failure:^(NSError *error) {
        if (error) {
            LLog_Error([NSString stringWithFormat:@"网络请求错误信息: %@",[error localizedDescription]]);
        }
    }];
}

#pragma mark - 获取注册验证码 -
+ (void)handleTheGetMobileRegisterTokenReqWithTelNum:(NSString *)telNum
                      mobileRegisterTokenReqCallBack:(MobileRegisterTokenReqCallBack)callBack
{
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpGetRegisterCode];
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
                NSLog(@"发送验证码 == %@",jsonDict);
                if (callBack) {
                    callBack(YES,jsonDict);
                }
            }else{
                NSDictionary *dict = jsonDict[@"error"];
                if (dict) {
                    NSLog(@"发送验证码：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                    NSString *strMessage = [NSString stringWithFormat:@"%@%@",dict[@"message"],dict[@"code"]];
                    if (callBack) {
                        callBack(NO,strMessage);
                    }
                }
            }
        }
    }failure:^(NSError *error){
       LLog_Error([NSString stringWithFormat:@"网络请求错误信息: %@",[error localizedDescription]]);
    }];
}

@end
