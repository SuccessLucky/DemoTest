//
//  NetworkEngine.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "GCDAsyncSocket.h"
#import "SHModelDevice.h"
#import "SHModelScreenNew.h"
#import "CommonStructDefine.h"
#import "SHModelGateway.h"

NS_ASSUME_NONNULL_BEGIN

@interface NetworkEngine : NSObject

@property (nonatomic, assign) GSocketConState socketConState;
@property (strong, nonatomic) SHModelScreenNew *screenNew;
@property (strong, nonatomic) SHModelDevice *modelDevice;
@property (strong, nonatomic) NSString* strStudyCode;
@property (strong, nonatomic) NSDictionary *dictXF;

+ (id)shareInstance;

// 设置连接的ip和port
- (void)doSetCurrentIP:(NSString *)strHostIP port:(int)iHostPort;

//初始化socket连接
- (BOOL)doInitSocketConnect;

//强制连接
- (BOOL)doSocketConnectForceWithIp:(NSString *)hostIP andPort:(int)hostPort;

//tcp 断开连接
- (void)doForceDisConnect;

//判断是否连接
- (BOOL)isSocketConnectSucc;

// 发送请求
- (void)sendRequestData:(NSData *)sendData;

//局域网连接
- (void)doConnectLANWithIp:(NSString *)strIp iPort:(int)iport;

//远程连接
- (void)doRemoteConnection;

@end

NS_ASSUME_NONNULL_END
