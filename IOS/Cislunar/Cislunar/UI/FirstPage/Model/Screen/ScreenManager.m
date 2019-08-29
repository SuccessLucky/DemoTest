//
//  ScreenManager.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/25.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "ScreenManager.h"

@implementation ScreenManager

- (void)handleGetScreenListFromNetworkWithType:(SHScreenType)type
{
    NSString *strUrl;
    if (type == SHScreenType_All) {
        strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpGetScreenList];
    }else if (type == SHScreenType_Common){
        strUrl = [NSString stringWithFormat:@"%@%@?type=%ld",httpCommonHeader,httpGetScreenList,(long)type];
    }else if (type == SHScreenType_Favorite){
        strUrl = [NSString stringWithFormat:@"%@%@?type=%ld",httpCommonHeader,httpGetScreenList,(long)type];
    }
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
                                                                 NSLog(@"获取场景list-成功 == %@",jsonDict);
                                                                 NSArray *arrResult = jsonDict[@"result"];
                                                                 self.arrScreenList = [self doHandleTheScreenData:arrResult];
                                                                 
                                                                 NSString *strJson = [ToolCommon dictionaryToJson:jsonDict];
                                                                 [[SHDataBaseManager sharedInstance] doDeleteDataWithIdentifer:SHIdentifer_Screen];
                                                                 [[SHDataBaseManager sharedInstance] doHandleInsertDataWithIdentifer:SHIdentifer_Screen jsonStr:strJson];
                                                                 
                                                             }else{
                                                                 NSDictionary *dictError = jsonDict[@"error"];
                                                                 if (dictError) {
                                                                     NSLog(@"获取场景list：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                                                                 }
                                                                 self.errorInfo = dictError;
                                                             }
                                                         }
                                                     } failure:^(NSError *error) {
                                                         if (error) {
                                                             self.errorInfo = @{@"code":@"000",@"message":[error localizedDescription]};
                                                             LLog([NSString stringWithFormat:@"获取场景list-网络请求错误信息: %@",[error localizedDescription]]);
                                                         }
                                                     }];
}

- (NSArray *)doHandleTheScreenData:(NSArray *)arrResult
{
    NSMutableArray *mutArrScreen = [NSMutableArray new];
    NSMutableArray *mutArrCommonScreen = [NSMutableArray new];
    for (int i = 0; i < arrResult.count; i ++) {
        
        NSDictionary *dictScreen = arrResult[i];
        ScreenModel *screenModelTmp = [ScreenModel new];
        screenModelTmp.iScreen_scene_id = [dictScreen[@"scene_id"] intValue];
        screenModelTmp.iScreen_scene_type = [dictScreen[@"scene_type"] intValue];
        screenModelTmp.strScreen_name = dictScreen[@"name"];
        screenModelTmp.strScreen_image = dictScreen[@"image"];
        screenModelTmp.str_serial_number = dictScreen[@"serial_number"];
        
        screenModelTmp.str_sub_command_identifer = dictScreen[@"sub_command_identifer"];
        screenModelTmp.iScreen_need_linkage = [dictScreen[@"need_linkage"] intValue];
        screenModelTmp.iScreen_need_time_delay = [dictScreen[@"need_time_delay"] intValue];
        screenModelTmp.iScreen_need_timing = [dictScreen[@"need_timing"] intValue];
        screenModelTmp.iScreen_need_security_on = [dictScreen[@"need_security_on"] intValue];
        
        screenModelTmp.iScreen_need_security_off = [dictScreen[@"need_security_off"] intValue];
        screenModelTmp.str_delay_time = dictScreen[@"delay_time"];
        screenModelTmp.str_timing_time = dictScreen[@"timing_time"];
        screenModelTmp.str_reserved_property = dictScreen[@"reserved_property"];
        screenModelTmp.str_force_linkage = dictScreen[@"force_linkage"];
        
        screenModelTmp.str_enabled_or_disable_identifer = dictScreen[@"enabled_or_disable_identifer"];
        screenModelTmp.str_arming_or_disarming_identifer = dictScreen[@"arming_or_disarming_identifer"];
        screenModelTmp.str_linkage_device_mac_addr = dictScreen[@"linkage_device_mac_addr"];
        screenModelTmp.str_linkage_device_road = dictScreen[@"linkage_device_road"];
        screenModelTmp.str_linkage_device_data_type = dictScreen[@"linkage_device_data_type"];
        
        screenModelTmp.str_linkage_device_data_range = dictScreen[@"linkage_device_data_range"];
        screenModelTmp.str_linkage_time = dictScreen[@"linkage_time"];
        screenModelTmp.str_linkage_delay_time = dictScreen[@"linkage_delay_time"];
        NSArray *arrScreenDetail = dictScreen[@"scene_details"];
        screenModelTmp.arrScreen_scene_details = [self handleGetDeviceModel:arrScreenDetail];
        
        
        [mutArrScreen addObject:screenModelTmp];
        
        if (screenModelTmp.iScreen_scene_type == 1) {
            [mutArrCommonScreen addObject:screenModelTmp];
        }
        
    }
    self.arrCommonScreenList = mutArrCommonScreen;
    
    return mutArrScreen;
}

- (NSMutableArray *)handleGetDeviceModel:(NSArray *)result
{
    NSMutableArray *mutArr = [NSMutableArray new];
    for (int i = 0; i < result.count; i ++) {
        SHModelDevice *model = [SHModelDevice new];
        NSDictionary *dict = result[i];
        model.iScreenDetailId = [dict[@"scene_detail_id"] intValue];
        model.iDevice_device_id = [dict[@"device_id"] intValue];
        model.iDevice_room_id = [dict[@"room_id"] intValue];
        model.strDevice_room_name = dict[@"room_name"];
        model.iDevice_floor_id = [dict[@"floor_id"] intValue];
        
        model.strDevice_floor_name = dict[@"floor_name"];
        model.strDevice_device_name = dict[@"device_name"];
        model.strDevice_image = dict[@"image"];
        model.strDevice_device_OD = dict[@"device_OD"];
        
        model.strDevice_device_type = dict[@"device_type"];
        model.strDevice_category = dict[@"category"];
        model.strDevice_sindex = dict[@"sindex"];
        model.strDevice_sindex_length = dict[@"sindex_length"];
        model.strDevice_cmdId = dict[@"cmdId"];
        
        model.iDevice_device_state1 = [dict[@"device_state1"] intValue];
        model.iDevice_device_state2 = [dict[@"device_state2"] intValue];
        model.iDevice_device_state3 = [dict[@"device_state3"] intValue];
        model.strDevice_alarm_status = dict[@"alarm_status"];
        
        model.strDevice_mac_address = dict[@"mac_address"];
        model.strDevice_other_status = dict[@"other_status"];
        
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
        model.arrBtns = mutArrBtns;
        [mutArr addObject:model];
    }
    return mutArr;
}


- (void)doGetScreenListDataFromDB
{
    NSString *strJson = [[SHDataBaseManager sharedInstance] doQueryDataWithIdentifer:SHIdentifer_Screen];
    if (strJson) {
        NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
        NSArray *arrResult = jsonDict[@"result"];
        self.arrScreenList = [self doHandleTheScreenData:arrResult];
    }
}

- (void)doGetScreenListNameDataFromDBWithCompleteHandle:(GetScreeNameCallBackBlcok)callBack;
{
    NSString *strJson = [[SHDataBaseManager sharedInstance] doQueryDataWithIdentifer:SHIdentifer_Screen];
    if (strJson) {
        NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
        NSArray *arrResult = jsonDict[@"result"];
        NSArray *arrScreenModel = [self doHandleTheScreenData:arrResult];
        if (callBack) {
            callBack(arrScreenModel);
        }
//        NSMutableArray *mutArrName = [NSMutableArray new];
//        for (int i = 0; i <arrScreenModel.count; i ++) {
//            ScreenModel *screenModelTmp =  arrScreenModel[i];
//            [mutArrName addObject:screenModelTmp.strScreen_name];
//        }
//        
//        if (callBack) {
//            callBack(mutArrName);
//        }
    }
}



#pragma mark -
#pragma mark - 添加场景
- (void)handleTheAddScreenDataWithModel:(ScreenModel *)model
                        completeHandle:(BlockHandleScreenCallBack)callBack{
    
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSMutableArray *mutArrScreenDetail = [NSMutableArray new];
    for (int i = 0; i < model.arrScreen_scene_details.count; i ++) {
        SHModelDevice *modelDetail = model.arrScreen_scene_details[i];
        NSMutableDictionary *mutDictDetail = [NSMutableDictionary new];
        mutDictDetail[@"scene_detail_id"] = @(modelDetail.iScreenDetailId);
        mutDictDetail[@"device_id"] =      @(modelDetail.iDevice_device_id);
        mutDictDetail[@"device_state1"] =  @(modelDetail.iDevice_device_state1);
        mutDictDetail[@"device_state2"] =  @(modelDetail.iDevice_device_state2);
        mutDictDetail[@"device_state3"] =  @(modelDetail.iDevice_device_state3);
        mutDictDetail[@"other_status"] =  modelDetail.strDevice_other_status;
        [mutArrScreenDetail addObject:mutDictDetail];
    }
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"scene_id"]             = @(model.iScreen_scene_id);
    params[@"name"]                 = model.strScreen_name;
    params[@"image"]                = model.strScreen_image;
    params[@"scene_type"]           = @(model.iScreen_scene_type);
    params[@"serial_number"]        = model.str_serial_number;
    params[@"scene_details"]        = mutArrScreenDetail;
    
    params[@"need_linkage"]         = @(model.iScreen_need_linkage);
    params[@"need_time_delay"]      = @(model.iScreen_need_time_delay);
    params[@"need_timing"]          = @(model.iScreen_need_timing);
    params[@"need_security_on"]     = @(model.iScreen_need_security_on);
    params[@"need_security_off"]    = @(model.iScreen_need_security_off);
    
    params[@"delay_time"]           = model.str_delay_time;
    params[@"timing_time"]          = model.str_timing_time;
    
    params[@"linkage_time"]         = model.str_linkage_time;
    params[@"linkage_delay_time"]   = model.str_linkage_delay_time;
    
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpAddScreen];
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
                 NSLog(@"添加完场景返回list-成功 == %@",jsonDict);
                 NSDictionary *dictResult = jsonDict[@"result"];
                 
                 NSArray *arrResult = @[dictResult];
                 
                 NSArray*arrScreen = [self doHandleTheScreenData:arrResult];
                 
                 ScreenModel *screenModel = [arrScreen firstObject];
                 
                 if (callBack) {
                     callBack(YES,screenModel);
                 }
             }else{
                 NSDictionary *dictError = jsonDict[@"error"];
                 if (dictError) {
                     NSLog(@"添加完场景返回list失败：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                 }
                 if (callBack) {
                     callBack(YES,dictError[@"message"]);
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             LLog([NSString stringWithFormat:@"添加完场景返回list失败-网络请求错误信息: %@",[error localizedDescription]]);
         }
         if (callBack) {
             callBack(YES,[error localizedDescription]);
         }
     }];
}

#pragma mark -
#pragma mark - 更新场景
- (void)handleTheUpdateScreenDataWithModel:(ScreenModel *)model
                         completeHandle:(BlockHandleScreenCallBack)callBack{
    
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSMutableArray *mutArrScreenDetail = [NSMutableArray new];
    for (int i = 0; i < model.arrScreen_scene_details.count; i ++) {
        SHModelDevice *modelDetail = model.arrScreen_scene_details[i];
        NSMutableDictionary *mutDictDetail = [NSMutableDictionary new];
        mutDictDetail[@"scene_detail_id"] = @(modelDetail.iScreenDetailId);
        mutDictDetail[@"device_id"] =  @(modelDetail.iDevice_device_id);
        mutDictDetail[@"device_state1"] =  @(modelDetail.iDevice_device_state1);
        mutDictDetail[@"device_state2"] =  @(modelDetail.iDevice_device_state2);
        mutDictDetail[@"device_state3"] =  @(modelDetail.iDevice_device_state3);
        mutDictDetail[@"other_status"] =  modelDetail.strDevice_other_status;
        [mutArrScreenDetail addObject:mutDictDetail];
    }
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"scene_id"] = @(model.iScreen_scene_id);
    params[@"name"] = model.strScreen_name;
    params[@"image"] = model.strScreen_image;
    params[@"scene_type"] = @(model.iScreen_scene_type);
    params[@"serial_number"] = model.str_serial_number;
    params[@"scene_details"] = mutArrScreenDetail;
    
    params[@"need_linkage"] = @(model.iScreen_need_linkage);
    params[@"need_time_delay"] = @(model.iScreen_need_time_delay);
    params[@"need_timing"] = @(model.iScreen_need_timing);
    params[@"need_security_on"] = @(model.iScreen_need_security_on);
    params[@"need_security_off"] = @(model.iScreen_need_security_off);
    
    params[@"delay_time"] = model.str_delay_time;
    params[@"timing_time"] = model.str_timing_time;
    
    params[@"linkage_time"] = model.str_linkage_time;
    params[@"linkage_delay_time"] = model.str_linkage_delay_time;
    
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpUpdateScreen];
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
                 NSLog(@"更新场景-成功 == %@",jsonDict);
                 NSDictionary *dictResult = jsonDict[@"result"];
                 
                 NSArray *arrResult = @[dictResult];
                 
                 NSArray*arrScreen = [self doHandleTheScreenData:arrResult];
                 
                 ScreenModel *screenModel = [arrScreen firstObject];
                 
                 if (callBack) {
                     callBack(YES,screenModel);
                 }
             }else{
                 NSDictionary *dictError = jsonDict[@"error"];
                 if (dictError) {
                     NSLog(@"更新场景失败：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                 }
                 if (callBack) {
                     callBack(YES,dictError[@"message"]);
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             LLog([NSString stringWithFormat:@"更新场景失败-网络请求错误信息: %@",[error localizedDescription]]);
         }
         if (callBack) {
             callBack(YES,[error localizedDescription]);
         }
     }];
}





#pragma mark -
#pragma mark - 删除场景
- (void)handleDeleteScreenDataWithScreenID:(int)iScreenID
                        completeHandle:(BlockHandleScreenCallBack)callBack{
    
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"scene_id"] = @(iScreenID);
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpDeleteScreen];
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
                     NSLog(@"添加新的楼层：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             LLog([NSString stringWithFormat:@"添加新的楼层-网络请求错误信息: %@",[error localizedDescription]]);
             if (callBack) {
                 callBack(NO,[error localizedDescription]);
             }
         }
     }];
}


@end
