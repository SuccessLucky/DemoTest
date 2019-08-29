//
//  FirstPageManager.m
//  Cislunar
//
//  Created by 余长涛 on 2018/10/1.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "FirstPageManager.h"

@implementation FirstPageManager

- (void)doGetAlarmInfoFromNetworkWithTime:(NSString *)strTime
{
    NSString *strUrl = [NSString stringWithFormat:@"%@%@%@",httpCommonHeader,httpGetAlarmInfo,strTime];
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
                                                                 NSLog(@"获取报警记录-成功 == %@",jsonDict);
                                                                 NSArray *arrResult = jsonDict[@"result"];
                                                                 self.arrAlarmInfo = [self handleGetAlarmModel:arrResult];
                                                                 //数据库存储
                                                                 NSString *strJson = [ToolCommon dictionaryToJson:jsonDict];
                                                                 [[SHDataBaseManager sharedInstance] doDeleteDataWithIdentifer:SHIdentifer_AlarmInfo];
                                                                 [[SHDataBaseManager sharedInstance] doHandleInsertDataWithIdentifer:SHIdentifer_AlarmInfo jsonStr:strJson];
                                                             }else{
                                                                 NSDictionary *dictError = jsonDict[@"error"];
                                                                 self.errorInfo = dictError;
                                                                 if (dictError) {
                                                                     NSLog(@"获取报警记录：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                                                                 }
                                                             }
                                                         }
                                                         
                                                     } failure:^(NSError *error) {
                                                         self.errorInfo = @{@"code":@"000",@"message":[error localizedDescription]};
                                                         if (error) {
                                                             NSLog(@"获取报警记录-网络请求错误信息: %@",[error localizedDescription]);
                                                         }
                                                     }];
}

- (void)doGetAlarmInfoDataFromDB
{
    NSString *strJson = [[SHDataBaseManager sharedInstance] doQueryDataWithIdentifer:SHIdentifer_AlarmInfo];
    if (strJson) {
        NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
        NSArray *arrResult = jsonDict[@"result"];
        self.arrAlarmInfo = [self handleGetAlarmModel:arrResult];
    }
}

- (NSArray *)handleGetAlarmModel:(NSArray *)result
{
    NSMutableArray *mutArr = [NSMutableArray new];
    
    for (int i = 0; i < result.count; i ++) {
        SHAlarmModel *alarm = [SHAlarmModel new];
        NSDictionary *dict = result[i];
        alarm.iAlarm_alarm_id = [dict[@"alarm_id"] intValue];
        alarm.strAlarm_create_date = dict[@"create_date"];
        alarm.strAlarm_alarm_msg = dict[@"alarm_msg"];
        [mutArr addObject:alarm];
    }
    return mutArr;
}

- (void)handleSetTheSecurityStatus:(int)iSecurityStatus
                    callBackHandle:(HandleSetSecurityStatueCompelte)callBack{
    
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strGatewayMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"security_status"] = @(iSecurityStatus);
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpModiftySecurity];
    [[HTTPNetworkEngine sharedInstance] requestWithURLString:strUrl
                                                  parameters:params
                                                        type:HttpRequestTypePost
                                                       token:strToken
                                                     gateway:strGatewayMacAddr
                                                     success:^(id responseObject)
     {
         //登录成功后需要保存token
         NSDictionary *jsonDict = (NSDictionary *)responseObject;
         if (jsonDict) {
             if ([jsonDict[@"success"] intValue]) {
                 NSLog(@"修改安防状态-成功 == %@",jsonDict);
                 
                 if (callBack) {
                     callBack(YES,nil);
                 }
             }else{
                 NSDictionary *dictError = jsonDict[@"error"];
                 if (dictError) {
                     NSLog(@"修改安防状态：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
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
             NSLog(@"修改安防状态-网络请求错误信息: %@",[error localizedDescription]);
         }
     }];
}

@end
