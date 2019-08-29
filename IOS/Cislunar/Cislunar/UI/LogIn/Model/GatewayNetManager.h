//
//  GatewayNetManager.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void (^GetAddGatewayCallBack)(BOOL success, id result);

typedef void (^GetGatewayListCallBack)(BOOL success, id result);
typedef void (^DeleteGatewayCallBack)(BOOL success, id result);

typedef void (^GetGatewayMemInfoDetailCallBack)(BOOL success, id result);

NS_ASSUME_NONNULL_BEGIN

@interface GatewayNetManager : NSObject

@property (nonatomic, strong) NSArray *arrGatewayList;
@property (nonatomic, strong) NSDictionary *errorInfo;

//添加网关到服务器
- (void)handleTheAddGatewayDataCompleteHandle:(GetAddGatewayCallBack)callBack;

//获取网关列表
- (void)handleGetGatewayListFromNetwork;
- (void)doGetGatewayListDataFromDB;

//添加网关到服务器
- (void)handleAddGatewayToServer:(NSString *)strGatewayZigbeeMacAddr
                     gatewayName:(NSString *)name
                  callBackHandle:(LoginRspCallBack)callBack;


//删除网关
- (void)handleDeleteGatewayFromServer:(NSString *)strGatewayZigbeeMacAddr
                       CallBackHandle:(DeleteGatewayCallBack)callBack;


//获取网关用户详情
- (void)doGetGatewayMemberDetailInfoWithGatewayMacAddr:(NSString *)strGatewayMacAddr
                                        callBackHandle:(GetGatewayMemInfoDetailCallBack)callBack;

@end

NS_ASSUME_NONNULL_END
