//
//  SHAppCommonRequest.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/26.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHAppCommonRequest.h"
#import "SHUIPicModel.h"

@implementation SHAppCommonRequest

+ (id)shareInstance
{
    static SHAppCommonRequest *appCommonManager;
    static dispatch_once_t oneceToken;
    dispatch_once(&oneceToken, ^{
        appCommonManager = [[SHAppCommonRequest alloc] init];
    });
    return appCommonManager;
}


- (void)doGetPicListFromNetworkWithTypeID:(NSString *)strCategory completeHandle:(BlockGetPicCompleteHandle)complete
{
    NSString *strUrl = [NSString stringWithFormat:@"%@%@%@",httpCommonHeader,httpGetPic,strCategory];
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
                                                                 //数据库存储
                                                                 NSString *strJson = [ToolCommon dictionaryToJson:jsonDict];
                                                                 switch ([strCategory intValue]) {
                                                                     case 0:
                                                                     {
                                                                         NSLog(@"获取设备公共-成功 == %@",jsonDict);
                                                                         [[SHDataBaseManager sharedInstance] doDeleteDataWithIdentifer:SHIdentifer_DevicePublicPic];
                                                                         [[SHDataBaseManager sharedInstance] doHandleInsertDataWithIdentifer:SHIdentifer_DevicePublicPic jsonStr:strJson];
                                                                     }
                                                                         break;
                                                                     case 1:
                                                                     {
                                                                         NSLog(@"获取设备Piclist-成功 == %@",jsonDict);
                                                                         [[SHDataBaseManager sharedInstance] doDeleteDataWithIdentifer:SHIdentifer_DevicePic];
                                                                          [[SHDataBaseManager sharedInstance] doHandleInsertDataWithIdentifer:SHIdentifer_DevicePic jsonStr:strJson];
                                                                     }
                                                                         break;
                                                                     case 2:
                                                                     {
                                                                         [[SHDataBaseManager sharedInstance] doDeleteDataWithIdentifer:SHIdentifer_RoomPic];
                                                                          [[SHDataBaseManager sharedInstance] doHandleInsertDataWithIdentifer:SHIdentifer_RoomPic jsonStr:strJson];
                                                                         NSLog(@"获取房间Piclist-成功 == %@",jsonDict);
                                                                     }
                                                                         break;
                                                                     case 3:
                                                                     {
                                                                         [[SHDataBaseManager sharedInstance] doDeleteDataWithIdentifer:SHIdentifer_ScreenPic];
                                                                          [[SHDataBaseManager sharedInstance] doHandleInsertDataWithIdentifer:SHIdentifer_ScreenPic jsonStr:strJson];
                                                                         NSLog(@"获取场景Piclist-成功 == %@",jsonDict);
                                                                     }
                                                                         break;
                                                                         
                                                                     default:
                                                                         break;
                                                                 }
                                                                 
                                                                  NSArray *arr = [self handleTheDevicePic:jsonDict];
                                                                 if (complete) {
                                                                     complete(YES,arr);
                                                                 }
                                                                 
                                                             }else{
                                                                 NSDictionary *dictError = jsonDict[@"error"];
                                                                 if (dictError) {
                                                                     NSLog(@"获取Piclist：后台返回错误信息jsonerror == %@ code == %@,类型：%@",dictError[@"message"],dictError[@"code"],strCategory);
                                                                     if (complete) {
                                                                         complete(NO,dictError);
                                                                     }
                                                                 }
                                                             }
                                                         }
                                                     } failure:^(NSError *error) {
                                                         NSDictionary *dictError = @{@"code":@"000",@"message":[error localizedDescription]};
                                                         if (error) {
                                                             if (complete) {
                                                                 complete(NO,dictError);
                                                             }
                                                             LLog([NSString stringWithFormat:@"获取Piclist-网络请求错误信息: %@",[error localizedDescription]]);
                                                         }
                                                     }];
}

- (NSMutableArray *)handleTheDevicePic:(NSDictionary *)jsonDict
{
    NSDictionary *dictResult = jsonDict[@"result"];
    NSMutableArray *mutArrPic = [NSMutableArray new];
    NSArray *arrImage = dictResult[@"images"];
    for (int i = 0; i < arrImage.count; i ++) {
        NSDictionary *dictTemp      = arrImage[i];
        SHUIPicModel *model         = [SHUIPicModel new];
        model.iUIPic_id             = [dictTemp[@"id"] intValue];
        model.strUIPic_base_url     = dictTemp[@"base_url"];
        model.strUIPic_name         = dictTemp[@"name"];
        model.strUIPic_image_type   = dictTemp[@"image_type"];
        [mutArrPic addObject:model];
    }
    return mutArrPic;
}


#pragma mark -
#pragma mark - 获取图片

//获取房间UI图片和公共图片的集合
- (NSArray *)doGetRoomUIPicAll
{
    NSMutableArray *mutArr = [[NSMutableArray alloc] init];
    NSArray *arrRoom = [self doGetRoomPic];
    for (int i = 0; i < arrRoom.count; i ++) {
        SHUIPicModel *model = arrRoom[i];
        [mutArr addObject:model];
    }
    
    return mutArr;
}

//获取设备UI图片和公共图片的集合
- (NSArray *)doGetDeviceUIPicAll
{
    NSMutableArray *mutArr = [NSMutableArray new];
    NSArray *arrDevice = [self doGetDevicePic];
    for (int i = 0; i < arrDevice.count; i ++) {
        SHUIPicModel *model = arrDevice[i];
        [mutArr addObject:model];
    }
    
    NSArray *arrDeviceAll = [self doGetPublicPic];
    for (int i = 0; i < arrDeviceAll.count; i ++) {
        SHUIPicModel *model = arrDeviceAll[i];
        [mutArr addObject:model];
    }
    return mutArr;
}

//获取场景UI图片和公共图片的集合
- (NSArray *)doGetScreenUIPicAll
{
    NSMutableArray *mutArr = [NSMutableArray new];
    NSArray *arrScreen = [self doGetScreenPic];
    for (int i = 0; i < arrScreen.count; i ++) {
        SHUIPicModel *model = arrScreen[i];
        [mutArr addObject:model];
    }
    return mutArr;
}


#pragma mark -
#pragma mark - 获取场景三种图片
- (NSString *)doGetScreenCommonUIPicWithPicName:(NSString *)strPicName
{
    NSArray *arr = [self doGetScreenUIPicAll];
    NSString *strUrl = [self doGetCommonPicWithArrPic:arr picName:strPicName];
    return strUrl;
}

- (NSString *)doGetScreenHighLightUIPicWithPicName:(NSString *)strPicName
{
    NSArray *arr = [self doGetScreenUIPicAll];
    NSString *strUrl = [self doGetHighlightPicWithArrPic:arr picName:strPicName];
    return strUrl;
}

- (NSString *)doGetScreenGrayUIPicWithPicName:(NSString *)strPicName
{
    NSArray *arr = [self doGetScreenUIPicAll];
    NSString *strUrl = [self doGetGrayPicWithArrPic:arr picName:strPicName];
    return strUrl;
}


#pragma mark -
#pragma mark - 获取设备三种图片
- (NSString *)doGetDeviceCommonUIPicWithPicName:(NSString *)strPicName
{
    NSArray *arr = [self doGetDeviceUIPicAll];
    NSString *strUrl = [self doGetCommonPicWithArrPic:arr picName:strPicName];
    return strUrl;
}

- (NSString *)doGetDeviceHighLightUIPicWithPicName:(NSString *)strPicName
{
    NSArray *arr = [self doGetDeviceUIPicAll];
    NSString *strUrl = [self doGetHighlightPicWithArrPic:arr picName:strPicName];
    return strUrl;
}

- (NSString *)doGetDeviceGrayUIPicWithPicName:(NSString *)strPicName
{
    NSArray *arr = [self doGetDeviceUIPicAll];
    NSString *strUrl = [self doGetGrayPicWithArrPic:arr picName:strPicName];
    return strUrl;
}


#pragma mark -
#pragma mark - 获取房间三种图片
- (NSString *)doGetRoomCommonUIPicWithPicName:(NSString *)strPicName
{
    NSArray *arr = [self doGetRoomUIPicAll];
    NSString *strUrl = [self doGetCommonPicWithArrPic:arr picName:strPicName];
    return strUrl;
}

- (NSString *)doGetRoomHighLightUIPicWithPicName:(NSString *)strPicName
{
    NSArray *arr = [self doGetRoomUIPicAll];
    NSString *strUrl = [self doGetHighlightPicWithArrPic:arr picName:strPicName];
    return strUrl;

}

- (NSString *)doGetRoomGrayUIPicWithPicName:(NSString *)strPicName
{
    NSArray *arr = [self doGetRoomUIPicAll];
    NSString *strUrl = [self doGetGrayPicWithArrPic:arr picName:strPicName];
    return strUrl;
}



#pragma mark -
#pragma mark - private

//获取普通Url
- (NSString *)doGetCommonPicWithArrPic:(NSArray *)arrPic  picName:(NSString *)strPicName
{
    NSString *strUrl = nil;
    for (int i = 0; i < arrPic.count; i ++) {
        SHUIPicModel *modelUI = arrPic[i];
        if ([modelUI.strUIPic_name isEqualToString:strPicName]) {
            strUrl = [NSString stringWithFormat:@"%@%@@2x.%@",modelUI.strUIPic_base_url,modelUI.strUIPic_name,modelUI.strUIPic_image_type];
        }
    }
    return strUrl;
}

//获取高亮Url
- (NSString *)doGetHighlightPicWithArrPic:(NSArray *)arrPic  picName:(NSString *)strPicName
{
    NSString *strUrl = nil;
    for (int i = 0; i < arrPic.count; i ++) {
        SHUIPicModel *modelUI = arrPic[i];
        if ([modelUI.strUIPic_name isEqualToString:strPicName]) {
            strUrl = [NSString stringWithFormat:@"%@Pr_%@@2x.%@",modelUI.strUIPic_base_url,modelUI.strUIPic_name,modelUI.strUIPic_image_type];
        }
    }
    return strUrl;
}

//获取普通Url
- (NSString *)doGetGrayPicWithArrPic:(NSArray *)arrPic  picName:(NSString *)strPicName
{
    NSString *strUrl = nil;
    for (int i = 0; i < arrPic.count; i ++) {
        SHUIPicModel *modelUI = arrPic[i];
        if ([modelUI.strUIPic_name isEqualToString:strPicName]) {
            strUrl = [NSString stringWithFormat:@"%@Un_%@@2x.%@",modelUI.strUIPic_base_url,modelUI.strUIPic_name,modelUI.strUIPic_image_type];
        }
    }
    return strUrl;
}


- (NSArray *)doGetPublicPic
{
    NSString *strJson = [[SHDataBaseManager sharedInstance] doQueryDataWithIdentifer:SHIdentifer_DevicePublicPic];
    if (strJson) {
        NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
        NSArray *arr = [self handleTheDevicePic:jsonDict];
        return arr;
    }else{
        return nil;
    }
}

- (NSArray *)doGetRoomPic
{
    NSString *strJson = [[SHDataBaseManager sharedInstance] doQueryDataWithIdentifer:SHIdentifer_RoomPic];
    if (strJson) {
        NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
        NSArray *arr = [self handleTheDevicePic:jsonDict];
        return arr;
    }else{
    
        return nil;
    }
}

- (NSArray *)doGetDevicePic
{
    NSString *strJson = [[SHDataBaseManager sharedInstance] doQueryDataWithIdentifer:SHIdentifer_DevicePic];
    if (strJson) {
        NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
        NSArray *arr = [self handleTheDevicePic:jsonDict];
        return arr;
    }else{
        
        return nil;
    }
}

- (NSArray *)doGetScreenPic
{
    NSString *strJson = [[SHDataBaseManager sharedInstance] doQueryDataWithIdentifer:SHIdentifer_ScreenPic];
    if (strJson) {
        NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
        NSArray *arr = [self handleTheDevicePic:jsonDict];
        return arr;
    }else{
        
        return nil;
    }
}



//
//
//- (NSString *)handleGetRoomImg:(NSString *)strRoomImgName
//{
//    NSString *strUrl = nil;
//    NSArray *arrUIPic =  [self doGetRoomPic];
//    for (int i = 0; i < arrUIPic.count; i ++) {
//        SHUIPicModel *modelUI = arrUIPic[i];
//        if ([modelUI.strUIPic_name isEqualToString:strRoomImgName]) {
//            strUrl = [NSString stringWithFormat:@"%@%@@2x.%@",modelUI.strUIPic_base_url,modelUI.strUIPic_name,modelUI.strUIPic_image_type];
//        }
//    }
//    return strUrl;
//}
//
//- (NSString *)handleGetDeviceUnPressImg:(NSString *)strDeviceImgName
//{
//    NSString *strUrl = nil;
//    NSArray *arrUIPic =  [self doGetDevicePic];
//    for (int i = 0; i < arrUIPic.count; i ++) {
//        SHUIPicModel *modelUI = arrUIPic[i];
//        if ([modelUI.strUIPic_name isEqualToString:strDeviceImgName]) {
//            strUrl = [NSString stringWithFormat:@"%@Un_%@@2x.%@",modelUI.strUIPic_base_url,modelUI.strUIPic_name,modelUI.strUIPic_image_type];
//        }
//    }
//    return strUrl;
//}
//
//- (NSString *)handleGetDevicePressImg:(NSString *)strDeviceImgName
//{
//    NSString *strUrl = nil;
//    NSArray *arrUIPic =  [self doGetDevicePic];
//    for (int i = 0; i < arrUIPic.count; i ++) {
//        SHUIPicModel *modelUI = arrUIPic[i];
//        if ([modelUI.strUIPic_name isEqualToString:strDeviceImgName]) {
//            strUrl = [NSString stringWithFormat:@"%@Pr_%@@2x.%@",modelUI.strUIPic_base_url,modelUI.strUIPic_name,modelUI.strUIPic_image_type];
//        }
//    }
//    return strUrl;
//}
//
//
//- (NSString *)handleGetScreenUnPressImg:(NSString *)strScreenImgName
//{
//    NSString *strUrl = nil;
//    NSArray *arrUIPic =  [self doGetScreenPic];
//    for (int i = 0; i < arrUIPic.count; i ++) {
//        SHUIPicModel *modelUI = arrUIPic[i];
//        if ([modelUI.strUIPic_name isEqualToString:strScreenImgName]) {
//            strUrl = [NSString stringWithFormat:@"%@Un_%@@2x.%@",modelUI.strUIPic_base_url,modelUI.strUIPic_name,modelUI.strUIPic_image_type];
//        }
//    }
//    return strUrl;
//}
//
//- (NSString *)handleGetScreenPressImg:(NSString *)strScreenImgName
//{
//    NSString *strUrl = nil;
//    NSArray *arrUIPic =  [self doGetScreenPic];
//    for (int i = 0; i < arrUIPic.count; i ++) {
//        SHUIPicModel *modelUI = arrUIPic[i];
//        if ([modelUI.strUIPic_name isEqualToString:strScreenImgName]) {
//            strUrl = [NSString stringWithFormat:@"%@Pr_%@@2x.%@",modelUI.strUIPic_base_url,modelUI.strUIPic_name,modelUI.strUIPic_image_type];
//            //            NSLog(@"strul == %@",strUrl);
//        }
//    }
//    return strUrl;
//}

- (void)handlePostPushWithUUID:(NSString *)strUUID pushRegisterCallBack:(BlockPushRegisterCallBack)callBack
{
    NSMutableDictionary *params = [NSMutableDictionary dictionary];
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    params[@"uuid"] = strUUID;
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpPush];
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
                 NSLog(@"注册push信息 == %@",jsonDict);
                 if (callBack) {
                     callBack(YES,jsonDict);
                 }
             }else{
                 NSDictionary *dict = jsonDict[@"error"];
                 if (dict) {
                     NSLog(@"注册push信息：后台返回错误信息jsonerror == %@ code == %@",dict[@"message"],dict[@"code"]);
                     if (callBack) {
                         callBack(NO,dict[@"message"]);
                     }
                 }
             }
         }
     } failure:^(NSError *error) {
         if (error) {
             LLog([NSString stringWithFormat:@"push信息网络请求错误信息: %@",[error localizedDescription]]);
         }
     }];
}


- (void)handleGetCurrentGatewayInfo:(GetCurrentGatewayInfoCallBack)callBack
{
    NSString *strUrl = [NSString stringWithFormat:@"%@%@",httpCommonHeader,httpGetCurrentGatewayInfo];
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
                                                                 NSLog(@"获取当前网关-成功 == %@",jsonDict);
                                                                 SHModelGateway *model                   = [SHModelGateway new];
                                                                 model.iGateway_gateway_id               = [jsonDict[@"gateway_id"] intValue];
                                                                 model.strGateway_mac_address            = jsonDict[@"mac_address"];
                                                                 model.iGateway_member_type              = [jsonDict[@"member_type"] intValue];
                                                                 model.strGateway_gateway_name           = jsonDict[@"gateway_name"];
                                                                 model.strGateway_wifi_mac_address       = jsonDict[@"wifi_mac_address"];
                                                                 model.iSecurityStatus                   = [jsonDict[@"security_status"] intValue];
                                                                 if (callBack) {
                                                                     callBack(YES,model);
                                                                 }
                                                             }else{
                                                                 NSDictionary *dictError = jsonDict[@"error"];
                                                                 if (dictError) {
                                                                     if (callBack) {
                                                                         callBack(NO,dictError);
                                                                     }
                                                                     NSLog(@"获取当前网关：后台返回错误信息jsonerror == %@ code == %@",dictError[@"message"],dictError[@"code"]);
                                                                 }
                                                             }
                                                         }
                                                     } failure:^(NSError *error) {
                                                         if (error) {
                                                             NSDictionary *dictError = @{@"code":@"000",@"message":[error localizedDescription]};
                                                             if (callBack) {
                                                                 callBack(NO,dictError);
                                                             }
                                                             LLog([NSString stringWithFormat:@"添加网关-网络请求错误信息:%@",[error localizedDescription]]);
                                                         }
                                                     }];
}




@end
