 //
//  SHInfraredHoueNOManager.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/4.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHInfraredHoueNOManager.h"

@implementation SHInfraredHoueNOManager

- (void)handleTheAddInfraredBtnsDataWithDeviceId:(int)deviceID
                                        arrModel:(NSArray *)arrModels
                                  completeHandle:(GetListCallBack)callBack{
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSMutableArray *mutArr = [NSMutableArray new];
    for (int i = 0; i < arrModels.count; i++) {
        SHInfraredKeyModel *model = arrModels[i];
        NSMutableDictionary *paramsSingle = [NSMutableDictionary dictionary];
        paramsSingle[@"button_id"] = @(model.iButton_id);
        paramsSingle[@"name"] = model.strName;
        paramsSingle[@"instruction_code"] = model.strWarehouseNO;
        [mutArr addObject:paramsSingle];
    }
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"device_id"] = @(deviceID);
    params[@"device_buttons"] = mutArr;
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpAddInfraredBtn];
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
                 NSLog(@"添加红外按钮 == %@",jsonDict);
                 NSDictionary *dict = jsonDict[@"result"];
                 SHModelDevice *modelNew = [SHModelDevice new];
                 modelNew.iDevice_device_id = [dict[@"device_id"] intValue];
                 modelNew.iDevice_room_id = [dict[@"room_id"] intValue];
                 modelNew.strDevice_room_name = dict[@"room_name"];
                 modelNew.strDevice_device_name = dict[@"device_name"];
                 modelNew.strDevice_image = dict[@"image"];
                 modelNew.strDevice_device_OD = dict[@"device_OD"];
                 modelNew.strDevice_device_type = dict[@"device_type"];
                 modelNew.strDevice_category = dict[@"category"];
                 modelNew.strDevice_sindex = dict[@"sindex"];
                 modelNew.strDevice_mac_address = dict[@"mac_address"];
                 modelNew.strDevice_other_status = dict[@"other_status"];
                 modelNew.iDevice_device_state1 = [dict[@"device_state1"] intValue];
                 modelNew.iDevice_device_state2 = [dict[@"device_state2"] intValue];
                 modelNew.iDevice_device_state3 = [dict[@"device_state3"] intValue];
                 modelNew.iDevice_floor_id = [dict[@"floor_id"] intValue];
                 modelNew.strDevice_floor_name = dict[@"floor_name"];
                 modelNew.strDevice_alarm_status = dict[@"alarm_status"];
                 
                 NSArray *arrDeivceBtns = dict[@"device_buttons"];
                 NSMutableArray *mutArrBtns = [NSMutableArray new];
                 for (int i = 0; i < arrDeivceBtns.count; i ++) {
                     SHInfraredKeyModel *btn = [SHInfraredKeyModel new];
                     NSDictionary *dict = arrDeivceBtns[i];
                     btn.iButton_id = [dict[@"button_id"] intValue];
                     btn.strName = dict[@"name"];
                     btn.strWarehouseNO = dict[@"instruction_code"];
                     [mutArrBtns addObject:btn];
                 }
                 modelNew.arrBtns = mutArrBtns;
                 
                 if (callBack) {
                     callBack(YES,modelNew);
                 }
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"添加红外按钮：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             if (callBack) {
                 callBack(NO,[error localizedDescription]);
             }
             NSLog(@"添加红外按钮-网络请求错误信息: %@",[error localizedDescription]);
         }
     }];
}


@end
