//
//  SHModelGateway.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/1/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SHModelGateway : NSObject

@property (assign, nonatomic) int iGateway_gateway_id;
@property (strong, nonatomic) NSString *strGatewayIp;
@property (strong, nonatomic) NSString *strGatewayPort;
@property (strong, nonatomic) NSString *strGateway_gateway_name;
@property (strong, nonatomic) NSString *strGateway_mac_address;
@property (assign, nonatomic) int iGateway_member_type;
@property (strong, nonatomic) NSString *strGateway_wifi_mac_address;
@property (assign, nonatomic) int iSecurityStatus;

@property (assign, nonatomic) BOOL isMatched; //登录拉取服务器绑定网关是否能在本地搜索到 网关的ip和port

@property (strong, nonatomic) NSString *strRemotePort;
@property (strong, nonatomic) NSString *strRemoteIP;
@property (strong, nonatomic) NSString *strOD;
@property (assign, nonatomic) BOOL isWriteWifiConfigSucced;

@property (assign, nonatomic) NSString *strHardware;

@end
