//
//  SHDeviceManager.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/27.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHDeviceManager.h"

@implementation SHDeviceManager

- (void)doGetDeviceListFromNetworkWithRoomID:(int)iRoomID
{
    NSString *strUrl = [NSString stringWithFormat:@"%@%@%d",httpCommonHeader,httpGetDeviceList,iRoomID];
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
//                                                                 NSLog(@"获取设备list-成功 == %@",jsonDict);
                                                                 NSArray *arrResult = jsonDict[@"result"];
                                                                 self.arrDeviceList = [self handleGetDeviceModel:arrResult];
                                                                 //数据库存储
                                                                 NSString *strJson = [ToolCommon dictionaryToJson:jsonDict];
                                                                 [[SHDataBaseManager sharedInstance] doDeleteDeviceListDataWithRoomID:iRoomID];
                                                                 [[SHDataBaseManager sharedInstance] doHandleInsertDeviceDataWithRoomID:iRoomID jsonStr:strJson];
                                                             }else{
                                                                 NSDictionary *dictError = jsonDict[@"error"];
                                                                 self.errorInfo = dictError;
                                                                 if (dictError) {
                                                                     NSLog(@"获取设备list：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                                                                 }
                                                             }
                                                         }
                                                         
                                                     } failure:^(NSError *error) {
                                                         self.errorInfo = @{@"code":@"000",@"message":[error localizedDescription]};
                                                         if (error) {
                                                             NSLog(@"获取设备list-网络请求错误信息: %@",[error localizedDescription]);
                                                         }
                                                     }];
}


- (void)doGetDeviceListDataFromDBWithRoomID:(int)iRoomID
{
    NSString *strJson = [[SHDataBaseManager sharedInstance] doQueryDevieListDataWithRoomID:iRoomID];
    if (strJson) {
        NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
        NSArray *arrResult = jsonDict[@"result"];
        self.arrDeviceList = [self handleGetDeviceModel:arrResult];
    }
}

- (NSMutableArray *)handleGetDeviceModel:(NSArray *)result
{
    NSMutableArray *mutArr = [NSMutableArray new];
    for (int i = 0; i < result.count; i ++) {
        SHModelDevice *model = [SHModelDevice new];
        NSDictionary *dict = result[i];
        model.iDevice_device_id             = [dict[@"device_id"] intValue];
        model.iDevice_room_id               = [dict[@"room_id"] intValue];
        model.strDevice_room_name           = dict[@"room_name"];
        model.strDevice_device_name         = dict[@"device_name"];
        
        model.strDevice_image               = dict[@"image"];
        model.strDevice_device_OD           = dict[@"device_OD"];
        model.strDevice_device_type         = dict[@"device_type"];
        model.strDevice_category            = dict[@"category"];
        
        model.strDevice_sindex              = dict[@"sindex"];
        model.strDevice_sindex_length       = dict[@"sindex_length"];
        model.strDevice_cmdId               = dict[@"cmdId"];
        model.strDevice_mac_address         = dict[@"mac_address"];
        
        model.strDevice_other_status        = dict[@"other_status"];
        model.iDevice_device_state1         = [dict[@"device_state1"] intValue];
        model.iDevice_device_state2         = [dict[@"device_state2"] intValue];
        model.iDevice_device_state3         = [dict[@"device_state3"] intValue];
        
        model.iDevice_floor_id              = [dict[@"floor_id"] intValue];
        model.strDevice_floor_name          = dict[@"floor_name"];
        model.strDevice_alarm_status        = dict[@"alarm_status"];
        
        NSArray *arrDeivceBtns = dict[@"device_buttons"];
        NSMutableArray *mutArrBtns = [NSMutableArray new];
        for (int i = 0; i < arrDeivceBtns.count; i ++) {
            SHInfraredKeyModel *btn = [SHInfraredKeyModel new];
            NSDictionary *dict  = arrDeivceBtns[i];
            btn.iButton_id      = [dict[@"button_id"] intValue];
            btn.strName         = dict[@"name"];
            btn.strWarehouseNO  = dict[@"instruction_code"];
            [mutArrBtns addObject:btn];
        }
        model.arrBtns = mutArrBtns;
        [mutArr addObject:model];
    }
    return mutArr;
}

- (void)handleTheAddDeviceDataWithArrModel:(NSArray *)arrModels
                       completeHandle:(GetListCallBack)callBack{
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSMutableArray *mutArr = [NSMutableArray new];
    for (int i = 0; i < arrModels.count; i++) {
        SHModelDevice *model = arrModels[i];
        NSMutableDictionary *paramsSingle = [NSMutableDictionary dictionary];
        paramsSingle[@"room_id"]            = @(model.iDevice_room_id);
        paramsSingle[@"device_name"]        = model.strDevice_device_name;
        paramsSingle[@"image"]              = model.strDevice_image;
        
        paramsSingle[@"device_OD"]          = model.strDevice_device_OD;
        paramsSingle[@"device_type"]        = model.strDevice_device_type;
        paramsSingle[@"category"]           = model.strDevice_category;
        
        paramsSingle[@"sindex_length"]      = model.strDevice_sindex_length;
        paramsSingle[@"sindex"]             = model.strDevice_sindex;
        paramsSingle[@"cmdId"]              = model.strDevice_cmdId;
        paramsSingle[@"mac_address"]        = model.strDevice_mac_address;
        
        paramsSingle[@"other_status"]       = model.strDevice_other_status;
        paramsSingle[@"device_state1"]      = @(model.iDevice_device_state1);
        paramsSingle[@"device_state2"]      = @(model.iDevice_device_state2);
        paramsSingle[@"device_state3"]      = @(model.iDevice_device_state3);
        [mutArr addObject:paramsSingle];
    }
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"devices"] = mutArr;
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpGetDeviceNew];
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
                 NSLog(@"添加设备 == %@",jsonDict);
                 
                 NSMutableArray *mutArrModels = [NSMutableArray new];
                 NSArray *arrDict = jsonDict[@"result"];
                 for (int i = 0; i < arrDict.count; i ++) {
                     SHModelDevice *modelNew = [SHModelDevice new];
                     NSDictionary *dict = arrDict[i];
                     modelNew.iDevice_device_id         = [dict[@"device_id"] intValue];
                     modelNew.iDevice_room_id           = [dict[@"room_id"] intValue];
                     modelNew.strDevice_room_name       = dict[@"room_name"];
                     modelNew.strDevice_device_name     = dict[@"device_name"];
                     
                     modelNew.strDevice_image           = dict[@"image"];
                     modelNew.strDevice_device_OD       = dict[@"device_OD"];
                     modelNew.strDevice_device_type     = dict[@"device_type"];
                     modelNew.strDevice_category        = dict[@"category"];
                     
                     modelNew.strDevice_sindex          = dict[@"sindex"];
                     modelNew.strDevice_sindex_length   = dict[@"sindex_length"];
                     modelNew.strDevice_mac_address     = dict[@"mac_address"];
                     modelNew.strDevice_other_status    = dict[@"other_status"];
                     
                     modelNew.iDevice_device_state1     = [dict[@"device_state1"] intValue];
                     modelNew.iDevice_device_state2     = [dict[@"device_state2"] intValue];
                     modelNew.iDevice_device_state3     = [dict[@"device_state3"] intValue];
                     modelNew.iDevice_floor_id          = [dict[@"floor_id"] intValue];
                     
                     modelNew.strDevice_floor_name      = dict[@"floor_name"];
                     modelNew.strDevice_alarm_status    = dict[@"alarm_status"];
                     
                     NSArray *arrDeivceBtns = dict[@"device_buttons"];
                     NSMutableArray *mutArrBtns = [NSMutableArray new];
                     for (int i = 0; i < arrDeivceBtns.count; i ++) {
                         SHInfraredKeyModel *btn = [SHInfraredKeyModel new];
                         NSDictionary *dict = arrDeivceBtns[i];
                         btn.iButton_id = [dict[@"button_id"] intValue];
                         btn.strName = dict[@"name"];
                         btn.strWarehouseNO = dict[@"instruction_code"];
                         [mutArrBtns addObject:btn];
                         [mutArrBtns addObject:btn];
                     }
                     modelNew.arrBtns = mutArrBtns;
                     [mutArrModels addObject:modelNew];
                 }
                
                 if (callBack) {
                     callBack(YES,mutArrModels);
                 }
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"添加设备：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
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
             NSLog(@"添加设备-网络请求错误信息: %@",[error localizedDescription]);
         }
     }];
}


- (void)handleDeleteDeviceByDeviceId:(int)deviceId completeHandle:(GetListCallBack)callBack
{
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"device_id"] = @(deviceId);
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpDeleteDevice];
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
                 NSLog(@"删除设备成功 == %@",jsonDict);
                 if (callBack) {
                     callBack(YES,nil);
                 }
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"删除设备失败：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             NSLog(@"删除设备失败-网络请求错误信息: %@",[error localizedDescription]);
         }
     }];
}

- (void)handleDeleteDeviceByMacAddr:(NSString *)strMacAddr completeHandle:(GetListCallBack)callBack
{
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"mac_address"] = strMacAddr;
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpDeleteDevice];
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
                 NSLog(@"删除设备成功 == %@",jsonDict);
                 if (callBack) {
                     callBack(YES,nil);
                 }
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"删除设备失败：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             NSLog(@"删除设备失败-网络请求错误信息: %@",[error localizedDescription]);
         }
     }];
}

- (void)handleTheUpdateDeviceDataWithModel:(SHModelDevice *)device
                          completeHandle:(GetListCallBack)callBack{
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    params[@"device_id"] = @(device.iDevice_device_id);
    params[@"device_name"] = device.strDevice_device_name;
    params[@"image"] = device.strDevice_image;
    params[@"other_status"] = device.strDevice_other_status;
    params[@"device_state1"] = @(device.iDevice_device_state1);
    params[@"device_state2"] = @(device.iDevice_device_state2);
    params[@"device_state3"] = @(device.iDevice_device_state3);
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpUpdateDevice];
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
                 NSLog(@"更新设备 == %@",jsonDict);
                 NSDictionary *dict = jsonDict[@"result"];
                 SHModelDevice *modelNew = [SHModelDevice new];
                 modelNew.iDevice_device_id             = [dict[@"device_id"] intValue];
                 modelNew.iDevice_room_id               = [dict[@"room_id"] intValue];
                 modelNew.strDevice_room_name           = dict[@"room_name"];
                 modelNew.strDevice_device_name         = dict[@"device_name"];
                 modelNew.strDevice_image               = dict[@"image"];
                 modelNew.strDevice_device_OD           = dict[@"device_OD"];
                 modelNew.strDevice_device_type         = dict[@"device_type"];
                 modelNew.strDevice_category            = dict[@"category"];
                 modelNew.strDevice_sindex              = dict[@"sindex"];
                 modelNew.strDevice_sindex_length       = dict[@"sindex_length"];
                 modelNew.strDevice_mac_address         = dict[@"mac_address"];
                 modelNew.strDevice_other_status        = dict[@"other_status"];
                 modelNew.iDevice_device_state1         = [dict[@"device_state1"] intValue];
                 modelNew.iDevice_device_state2         = [dict[@"device_state2"] intValue];
                 modelNew.iDevice_device_state3         = [dict[@"device_state3"] intValue];
                 modelNew.iDevice_floor_id              = [dict[@"floor_id"] intValue];
                 modelNew.strDevice_floor_name          = dict[@"floor_name"];
                 modelNew.strDevice_alarm_status        = dict[@"alarm_status"];
                 
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
                     NSLog(@"更新设备：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             NSLog(@"更新设备-网络请求错误信息: %@",[error localizedDescription]);
         }
     }];
}


#pragma mark - 获取单个设备的信息
- (void)handleGetDeviceInfoByDeviceId:(int)iDeviceID
                       completeHandle:(BlockGetDeviceSingleInfoCompleteHandle)callBack
{
    NSString *strUrl = [NSString stringWithFormat:@"%@/rest/v1/device/%d.json",httpCommonHeader,iDeviceID];
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
                                                                 NSLog(@"获取单个设备-成功 == %@",jsonDict);
                                                                 NSDictionary *dictResult = jsonDict[@"result"];
                                                                 NSArray *arrDevice = [self handleGetDeviceModel:@[dictResult]];
                                                                 SHModelDevice *device = arrDevice[0];
                                                                 if (callBack) {
                                                                     callBack(YES,device);
                                                                 }
                                                                 
                                                             }else{
                                                                 NSDictionary *dictError = jsonDict[@"error"];
                                                                 if (dictError) {
                                                                     NSLog(@"获取单个设备：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                                                                 }
                                                                 
                                                                 if (callBack) {
                                                                     callBack(NO,dictError[@"message"]);
                                                                 }
                                                             }
                                                         }
                                                     } failure:^(NSError *error) {
                                                         if (error) {
                                                             NSLog(@"获取单个设备-网络请求错误信息: %@",[error localizedDescription]);
                                                         }
                                                         if (callBack) {
                                                             callBack(NO,[error localizedDescription]);
                                                         }
                                                         
                                                     }];
}


- (void)doGetAllDeviceListFromNetwork
{
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpGetAllDeviceList];
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
                                                                 //                                                                 NSLog(@"获取网关下所有设备-成功 == %@",jsonDict);
                                                                 NSArray *arrResult = jsonDict[@"result"];
                                                                 self.arrDeviceList = [self handleGetDeviceModel:arrResult];
                                                                 //数据库存储
                                                                 NSString *strJson = [ToolCommon dictionaryToJson:jsonDict];
                                                                 
                                                                 [[SHDataBaseManager sharedInstance] doDeleteDataWithIdentifer:SHIdentifer_AllDevice];
                                                                 [[SHDataBaseManager sharedInstance] doHandleInsertDataWithIdentifer:SHIdentifer_AllDevice jsonStr:strJson];
                                                                 
                                                             }else{
                                                                 NSDictionary *dictError = jsonDict[@"error"];
                                                                 self.errorInfo = dictError;
                                                                 if (dictError) {
                                                                     NSLog(@"获取网关下所有设备：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                                                                 }
                                                             }
                                                         }
                                                         
                                                     } failure:^(NSError *error) {
                                                         self.errorInfo = @{@"code":@"000",@"message":[error localizedDescription]};
                                                         if (error) {
                                                             NSLog(@"获取网关下所有设备-网络请求错误信息: %@",[error localizedDescription]);
                                                         }
                                                     }];
}


- (void)doGetAllDeviceListDataFromDB
{
    NSString *strJson = [[SHDataBaseManager sharedInstance] doQueryDataWithIdentifer:SHIdentifer_AllDevice];
    if (strJson) {
        NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
        NSArray *arrResult = jsonDict[@"result"];
        self.arrDeviceList = [self handleGetDeviceModel:arrResult];
    }
}


#pragma mark -
#pragma mark - 新的带房间的设备列表V2
- (void)doGetAllDeviceListFromNetworkV2
{
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpGetAllDeviceListV2];
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
                                                                 //                                                                 NSLog(@"获取网关下所有设备-成功 == %@",jsonDict);
                                                                 NSArray *arrResult = jsonDict[@"result"];
                                                                 self.arrDeviceList = [self doHandleTheResult:arrResult];
                                                                 //数据库存储
                                                                 NSString *strJson = [ToolCommon dictionaryToJson:jsonDict];
                                                                 
                                                                 [[SHDataBaseManager sharedInstance] doDeleteDataWithIdentifer:SHIdentifer_AllDeviceV2];
                                                                 [[SHDataBaseManager sharedInstance] doHandleInsertDataWithIdentifer:SHIdentifer_AllDeviceV2 jsonStr:strJson];
                                                                 
                                                             }else{
                                                                 NSDictionary *dictError = jsonDict[@"error"];
                                                                 self.errorInfo = dictError;
                                                                 if (dictError) {
                                                                     NSLog(@"获取网关下所有设备V2：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                                                                 }
                                                             }
                                                         }
                                                         
                                                     } failure:^(NSError *error) {
                                                         self.errorInfo = @{@"code":@"000",@"message":[error localizedDescription]};
                                                         if (error) {
                                                             NSLog(@"获取网关下所有设备V2-网络请求错误信息: %@",[error localizedDescription]);
                                                         }
                                                     }];
}



- (void)doGetAllDeviceListDataFromDBV2
{
    NSString *strJson = [[SHDataBaseManager sharedInstance] doQueryDataWithIdentifer:SHIdentifer_AllDeviceV2];
    if (strJson) {
        NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
        NSArray *arrResult = jsonDict[@"result"];
        self.arrDeviceList = [self doHandleTheResult:arrResult];
    }
}

- (NSArray *)doHandleTheResult:(NSArray *)arrResult
{
    NSMutableArray *mutArrAllDeviceList = [NSMutableArray new];
    for (int i = 0; i < arrResult.count; i ++) {
        
        SHModelRoom *room = [SHModelRoom new];
        NSDictionary *dict = arrResult[i];
        NSArray *arrDevices = dict[@"devices"];
        NSArray *arrDeivceModels = [self handleGetDeviceModel:arrDevices];
        
        NSString *strFloorName = dict[@"floor_name"];
        NSString *strRoomName = dict[@"room_name"];
        NSString *strRoomId = dict[@"room_id"];
        NSString *strRoomImage = dict[@"room_image"];
        
        room.iRoom_id = [strRoomId intValue];
        room.strRoom_name = strRoomName;
        room.strRoom_image = strRoomImage;
        room.strFloorName = strFloorName;
        room.arrDeviceList = arrDeivceModels;
        [mutArrAllDeviceList addObject:room];
    }
    return mutArrAllDeviceList;
}



@end
