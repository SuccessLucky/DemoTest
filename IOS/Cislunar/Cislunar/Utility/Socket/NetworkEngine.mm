//
//  NetworkEngine.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine.h"

@interface NetworkEngine ()<GCDAsyncSocketDelegate>

@property (nonatomic, strong) GCDAsyncSocket    *socket;
@property (nonatomic, assign) int               reConnectIndex;     // socket重连次数
@property (nonatomic, strong) NSString *strHostIP;
@property (nonatomic, assign) int iHostPort;


@end

@implementation NetworkEngine

+ (id)shareInstance
{
    static NetworkEngine *networkEngine;
    static dispatch_once_t oneceToken;
    dispatch_once(&oneceToken, ^{
        networkEngine = [[NetworkEngine alloc] init];
    });
    
    return networkEngine;
}

- (id)init
{
    self = [super init];
    if (self)
    {
        self.modelDevice = [SHModelDevice new];
        _socketConState = GSocketConStateFail;
        _reConnectIndex = 0;
        _socket = [[GCDAsyncSocket alloc] initWithDelegate:self delegateQueue:dispatch_get_main_queue()];
    }
    return self;
}

#pragma mark -
#pragma mark - 设置连接的ip和port
- (void)doSetCurrentIP:(NSString *)strHostIP port:(int)iHostPort
{
    self.strHostIP = strHostIP;
    self.iHostPort = iHostPort;
}

#pragma mark -
#pragma mark - tcp 断开连接
- (void)doForceDisConnect
{
    [_socket disconnect];
}

#pragma mark -
#pragma mark Tcp连接
- (BOOL)doInitSocketConnect
{
    NSLog(@"%@", [NSString stringWithFormat:@"ip == %@,port == %d",self.strHostIP,self.iHostPort]);
    NSLog(@"%@", [NSString stringWithFormat:@"current socket conState = %ld", (long)self.socketConState]);
    //连接检测
    if (self.socketConState != GSocketConStateFail)  return NO;
    
    return [self doSocketConnectForceWithIp:self.strHostIP andPort:self.iHostPort];
}

- (BOOL)doSocketConnectForceWithIp:(NSString *)hostIP andPort:(int)hostPort
{
    NSLog(@"%@", [NSString stringWithFormat:@"当前服务器ip:%@, 端口:%d", hostIP, hostPort]);
    NSLog(@"socket连接时，先断掉原来连接");
    [_socket disconnect];
    NSError *error = nil;
    //正在连接
    self.socketConState = GSocketConStateConnecting;
    [self postSocketConnectNotification:self.socketConState];
    
    if ([_socket connectToHost:hostIP onPort:hostPort withTimeout:[self handleGetAccessTimeOut] error:&error])
    {
        LLog(@"svr处于监听状态");
        return YES;
    }
    else
    {
        LLog(@"svr未处于监听状态");
        return NO;
    }
}

#pragma mark - GCDAsyncSocketDelegate
/**
 *  连接到服务器
 *
 *  @param sock GCDAsyncSocket
 *  @param host host
 *  @param port port
 */
- (void)socket:(GCDAsyncSocket *)sock didConnectToHost:(NSString *)host port:(uint16_t)port
{
    LLog(@"socket连接成功，开始接收数据");
    [self doSocketConnectSuccAction];
    //接收数据
    [sock readDataWithTimeout:GNetReqNoTimeOut tag:GNetReqDefaultSocketTag];
}

/**
 *  断开连接
 *
 *  @param sock GCDAsyncSocket
 *  @param err  错误信息
 */
- (void)socketDidDisconnect:(GCDAsyncSocket *)sock withError:(NSError *)err
{
    LLog([NSString stringWithFormat:@"socket连接失败了，errCode = %ld, errDesc = %@, reCommectIndex = %d", (long)[err code], [err description], self.reConnectIndex]);
    
    self.socketConState = GSocketConStateFail;
    [self doSocketConnectFailAction];
//    if (err)
//    {
//        if (self.reConnectIndex < [self handleGetAccessRetryTimes])
//        {
//            [self performSelector:@selector(doSocketReconnectAction) withObject:nil afterDelay:kTcpConnectInterval];
//        }else{
//            LLog(@"tcp重连结束。");
//            [self doSocketConnectFailAction];
//        }
//    }
}

/**
 *  收到数据信息
 *
 *  @param sock GCDAsyncSocket
 *  @param data sever return data
 *  @param tag  tag
 */
- (void)socket:(GCDAsyncSocket *)sock didReadData:(NSData *)data withTag:(long)tag
{
        NSString *strTemp = [[ToolHexManager sharedManager] convertDataToHexStr:data];
        NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strTemp];
        NSLog(@"\n收到数据了:%@",strBigTemp);
    @synchronized(self)
    {
        @try {
            char *pData = (char *)[data bytes];
            UInt8 ppstart = *pData;
            @synchronized(self)
            {
                if (ppstart == 0x2a) {
                    
                    NSString *strHexWholeData = [[ToolHexManager sharedManager] convertDataToHexStr:data];
                    NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexWholeData];
                    LLog([NSString stringWithFormat:@"上报origin==\n%@",strBigTemp]);
                    
                    [self handleLocalRspData:data];
                }else if (ppstart == 0x55){
                    LLog(@"处理云端的");
                    [self handleCloudSeverData:data];
                }else{
                }
            }
        }
        @catch (NSException *exception) {
            LLog([NSString stringWithFormat:@"处理粘包异常One :%@", [exception reason]]);
            NSError* error = [NSError errorWithDomain:G_ERROR_DOMAIN_CLIENT
                                                 code:GNetErrorCodeClient
                                             userInfo:@{NSLocalizedDescriptionKey: @"处理粘包失败"}];
            NSLog(@"error == %@",[error description]);
            return;
        }
        @finally {
            
        }
    }
    [sock readDataWithTimeout:GNetReqNoTimeOut tag:GNetReqDefaultSocketTag];
}

#pragma mark -
#pragma mark 发送请求
- (void)sendRequestData:(NSData *)sendData
{
    NSString *strTemp = [[ToolHexManager sharedManager] convertDataToHexStr:sendData];
    NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strTemp];
    LLog([NSString stringWithFormat:@"\n发送的数据:%@",strBigTemp]);
    
    if (![self isSocketConnectSucc]){
        LLog(@"tcp连接异常，需要重连");
        
        NSError* error = [NSError errorWithDomain:G_ERROR_DOMAIN_CLIENT
                                             code:GNetErrorCodeNetUnusual
                                         userInfo:@{NSLocalizedDescriptionKey: @"网络异常"}];
        
        [self didNotifyConnectFail:error];
        //发送失败进行tcp重连
        [self doInitSocketConnect];
        
    }else{
        
        [_socket writeData:sendData withTimeout:GNetReqNoTimeOut tag:GNetReqDefaultSocketTag];
    }
}

- (BOOL)isSocketConnectSucc
{
    LLog([NSString stringWithFormat:@"socketConState = %ld,  socket.isConnected = %d", (long)self.socketConState, _socket.isConnected]);
    return (self.socketConState == GSocketConStateSucc && _socket.isConnected);
}




#pragma mark -
#pragma mark - 添加通知获取socket连接状态
-(void)postSocketConnectNotification:(GSocketConState)conState
{
    LLog([NSString stringWithFormat:@"socket连接状态改变，抛通知。 socketConState = %ld", (long)conState]);
    NSMutableDictionary*dict =[NSMutableDictionary dictionary];
    [dict setObject:[NSNumber numberWithInt:conState] forKey:@"connectState"];
    dispatch_async(dispatch_get_main_queue(), ^{
        [[NSNotificationCenter defaultCenter] postNotificationName:kGSocketConnectNotify
                                                            object:nil
                                                          userInfo:dict];
    });
}

#pragma mark - 计数
#pragma mark - 接入超时间
- (int)handleGetAccessTimeOut
{
    return -1;
}

#pragma mark - 接入重试次数
- (int)handleGetAccessRetryTimes
{
    return 3;
}

#pragma mark -private
#pragma mark -socket 连接成功 ，失败 操作
/**
 *  socket连接成功后操作
 */
- (void)doSocketConnectSuccAction
{
    self.socketConState = GSocketConStateSucc;
    self.reConnectIndex = 0;
    [self postSocketConnectNotification:self.socketConState];
}

/**
 *  socket连接失败后操作
 */
- (void)doSocketConnectFailAction
{
    self.socketConState = GSocketConStateFail;
    self.reConnectIndex = 0;
    
    [self postSocketConnectNotification:self.socketConState];
    
    NSError* error = [NSError errorWithDomain:G_ERROR_DOMAIN_CLIENT
                                         code:GNetErrorCodeDisconnect
                                     userInfo:@{NSLocalizedDescriptionKey: @"网络错误"}];
    LLog([NSString stringWithFormat:@"%@",error]);
}

#pragma mark -
#pragma mark - socket 重连
- (void)doSocketReconnectAction
{
    self.reConnectIndex ++;
    [self doInitSocketConnect];
}


#pragma mark - 的点点滴滴多多多多
- (void)didNotifyConnectFail:(NSError *)error
{
    
}

#pragma mark -
#pragma mark - 局域网连接  和 远程连接
//局域网连接
- (void)doConnectLANWithIp:(NSString *)strIp iPort:(int)iport
{
    self.socketConState = GSocketConStateFail;
    [self doSetCurrentIP:strIp port:iport];
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [self doInitSocketConnect];
    });
}

//远程连接
- (void)doRemoteConnection
{
    self.socketConState = GSocketConStateFail;
    [self doSetCurrentIP:cloudIP port:cloudPort];
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [self doInitSocketConnect];
    });
}

@end
