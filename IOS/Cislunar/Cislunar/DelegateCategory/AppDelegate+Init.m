//
//  AppDelegate+Init.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/14.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "AppDelegate+Init.h"
#import "AppDelegate+GeTui.h"
#import "AppDelegate+Camera.h"
#import "AppDelegate+Other.h"
#import "AppDelegate+LogIn.h"
//#import "BonjourManager.h"

@implementation AppDelegate (Init)

#pragma mark -
#pragma mark - 初始化
- (void)doAppInitLogic
{
    
    //设置状态栏为白色
     [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleLightContent;
    
//    [self doGetVisonUpdate];
    [self IterationVersion];
    
    //注册个推的
    [self doStartGT];
    
    //注册摄像头的
    [self doInitEZOpenSDK];
    
    //控制台打印log
//    [self doStartLogDetection];
    
    //创建数据库表
    [[SHDataBaseManager sharedInstance] createCommonTable];
    
    //获取网络状态
    [ToolCommon getNetworkStatus];
    
    //监听通知
    [self addNotification];
    
    [self startNetworkMonitor];
}

#pragma mark -
#pragma mark - private 注册必要的通知 和相关方法
- (void)addNotification
{
    //socket连接状态
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(didSocketConnectStateChange:)
                                                 name:kGSocketConnectNotify
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(didCloudserverConnectState:)
                                                 name:kRegisterCloudsever
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(didGetGatewayMacAddr:)
                                                 name:kGetGatewayMacAddr
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(didReadGatewayConfigInfo:)
                                                 name:kYReadGatewayConfigInfoNotify
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(didGetGatewayConfigResult:)
                                                 name:kYGatewayConfigNotify
                                               object:nil];
}

#pragma mark -
#pragma mark - 网关wifi配置成功或者失败
- (void)didGetGatewayConfigResult:(NSNotification *)noti
{
    SHModelGateway *modelGateway = (SHModelGateway *)noti.object;
    if (modelGateway.isWriteWifiConfigSucced) {
        NSLog(@"网关wifi参数配置成功！");
        [[NetworkEngine shareInstance] deResetGatewayOrderWithGatewayMacAddr:modelGateway.strGateway_mac_address];
        
    }else{
        NSLog(@"网关wifi参数配置失败！");
    }
}

#pragma mark -
#pragma mark -读取到网关wifi的配置信息
- (void)didReadGatewayConfigInfo:(NSNotification *)noti
{
    SHModelGateway *modelGateway = (SHModelGateway *)noti.object;
    NSString *strGatewayMacAddr = modelGateway.strGateway_mac_address;
    NSString *strLocalPort = modelGateway.strGatewayPort;
    NSString *strRemotePort = modelGateway.strRemotePort;
    NSString *strRemoteIp = modelGateway.strRemoteIP;
    
    NSString *strRemberCloudIp = [NSString stringWithFormat:@"%@",cloudIP];
    NSString *strRemberCloudPort =@"7002";
    
    if ([strRemoteIp isEqualToString:strRemberCloudIp] && [strRemotePort isEqualToString:strRemberCloudPort]) {
        //如果远程端口和ip 没变化，则不处理
        NSLog(@"************服务器端口和ip没变化详细：网关的mac地址：%@;本地端口:%@;远程端口:%@;远程oIP:%@！",strGatewayMacAddr,strLocalPort,strRemotePort,strRemoteIp);
        
//        [[NetworkEngine shareInstance] sendCMDToChangeRemoteIpLocalPortAndRemotePortWithGatewayZigbeeMacWithGatewayMacAddr:strGatewayMacAddr];
        
    }else{
        NSLog(@"************远程ip 和端口有变化需要重新配置详细：网关的mac地址：%@;本地端口:%@;远程端口:%@;远程oIP:%@！",strGatewayMacAddr,strLocalPort,strRemotePort,strRemoteIp);
        
        NSString *strHardware = [[SHLoginManager shareInstance] doGetWifiHardware];
        
        [[NetworkEngine shareInstance] sendCMDToChangeRemoteIpLocalPortAndRemotePortWithGatewayZigbeeMacWithGatewayMacAddr:strGatewayMacAddr];
//        if ([strHardware isEqualToString:@"EMW3162"]) {
//            NSLog(@"电脑收懂配置ip 和 port")
//        }else
//            [[NetworkEngine shareInstance] sendCMDToChangeRemoteIpLocalPortAndRemotePortWithGatewayZigbeeMacWithGatewayMacAddr:strGatewayMacAddr];
        
    }
    
}

#pragma mark -
#pragma mark - 获取到网关的mac地址
- (void)didGetGatewayMacAddr:(NSNotification *)notification
{
    NSString *strGatewayMacAddr = notification.object;
    NSLog(@"***********3局域网：读取网关配置详细信息*************");
    if ([[SHAppInfoManager shareInstance] doIsInLAN]){
//        [self bk_performBlock:^(id obj) {
//            [[NetworkEngine shareInstance] doReadGatewayWifiDetailsWithGatewayMacAddr:strGatewayMacAddr];
//        } afterDelay:0.25];
        [[NetworkEngine shareInstance] doReadGatewayWifiDetailsWithGatewayMacAddr:strGatewayMacAddr];
    }
    
    
}

- (void)didCloudserverConnectState:(NSNotification *)notification
{
    NSDictionary *dict = notification.userInfo;
    SHCloudSeverConState state = [dict[@"kRegisterCloudsever"] intValue];
    [XWHUDManager hideInWindow];
    switch (state) {
        case SHCloudSeverConStateSucc:
        {
            [XWHUDManager showSuccessTipHUD:@"连接成功"];
            NSString *strClientID = [[SHLoginManager shareInstance] doGetGeTuiClientID];
            [[SHAppCommonRequest shareInstance] handlePostPushWithUUID:strClientID
                                                  pushRegisterCallBack:^(BOOL success, id result)
             {}];
        }
            break;
        case SHCloudSeverConState_NORecord:
        {
            [XWHUDManager showWarningTipHUD:@"网关没有注册"];
        }
            break;
        case SHCloudSeverConState_DisCon:
        {
            [XWHUDManager showErrorTipHUD:@"连接失败"];
        }
            break;
            
        default:
            break;
    }
}



#pragma mark - socket连接改变后操作
- (void)didSocketConnectStateChange:(NSNotification *)notification
{
    NSDictionary *uInfo = [notification userInfo];
    GSocketConState socketConState = (GSocketConState)[[uInfo objectForKey:@"connectState"] intValue];
    
    if (socketConState == GSocketConStateSucc)
    {
        LLog([NSString stringWithFormat:@"连接成功socketConState = %ld", (long)socketConState]);
        [self didCheckForSendNecessaryReqs];
        
    }else if(socketConState == GSocketConStateConnecting){
        
        LLog([NSString stringWithFormat:@"正在连接socketConState = %ld", (long)socketConState]);
    }else{
        
        LLog([NSString stringWithFormat:@"连接失败socketConState = %ld", (long)socketConState]);
    }
}

#pragma mark - private 检查并发送必要的请求 主要获取网关mac地址
- (void)didCheckForSendNecessaryReqs
{
    if ([[SHAppInfoManager shareInstance] doIsInLAN]) {
        
        NSLog(@"***********1局域网：发送激活心跳帧*************");
        NSData *dataTemp = [[NetworkEngine shareInstance] doActivateHeartFrame];
        [[NetworkEngine shareInstance] sendRequestData:dataTemp];
        
        [self bk_performBlock:^(id obj) {
            NSLog(@"***********2局域网：获取网关的Mac地址*************");
            NSData *dataGatewayMac = [[NetworkEngine shareInstance] doGetGatewayZigbeeMacAddressFromGateway];
            [[NetworkEngine shareInstance] sendRequestData:dataGatewayMac];
        } afterDelay:0.25];
        
    }else{
        //发送手机注册消息
        NSString *strMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
        NSString *strDeviceToken = [[SHAppInfoManager shareInstance] handleGetDeviceToken];
        NSLog(@"*********************\n网关mac地址:%@,\ntoken:%@\n**************",strMacAddr,strDeviceToken);
        
        NSData *data = [[NetworkEngine shareInstance] doGetSendTelPhoneInfoToCloudSeverDataWithGatewayAddr];
        [[NetworkEngine shareInstance] sendRequestData:data];
    }
    
    [[SHAppCommonRequest shareInstance] handleGetCurrentGatewayInfo:^(BOOL success, id result) {
        
    }];
}


#pragma mark - 判断登录状态 判断是否第一次登陆
- (SHUserLoginState)doGetLoginedState
{
    NSString *strWifiMacAddr = [[SHLoginManager shareInstance] doGetWifiMacAddr];
    NSString *strToken = [[SHLoginManager shareInstance] doGetCurentSignToken];
    
    if (strWifiMacAddr.length > 0 && strToken.length > 0) {
        return SHUserLoginStateLocal_LoginSucc;
    }else if(strWifiMacAddr.length == 0 && strToken.length > 0){
        //登录后台成功，但是没有获取到网关的wifiMacAddr
        return SHUserLoginState_NeedLogin;
    }else{
        return SHUserLoginState_NeedLogin;
    }
}

#pragma mark - 主要判断是否需要重新登录
- (void)didUserLoginAction
{
    SHUserLoginState loginState = [self doGetLoginedState];
    
    switch (loginState) {
        case SHUserLoginState_NeedLogin:
        {
             [[SHLoginManager shareInstance] doWriteLoginState:SHUserLoginState_NeedLogin];
            [self didEnterLoginViewController];
        }
            break;
        case SHUserLoginStateLocal_LoginSucc:
        {
             [[SHLoginManager shareInstance] doWriteLoginState:SHUserLoginStateLocal_LoginSucc];
            [self didEnterMainTabBarController];
            
        }
            break;
        case SHUserLoginStateNone:
        {
            [[SHLoginManager shareInstance] doWriteLoginState:SHUserLoginStateNone];
            [self didEnterMainTabBarController];
            
        }
            break;
            
        default:
            break;
    }
}


#pragma mark -
#pragma mark - 监听网络变化
- (void)startNetworkMonitor
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:kReachabilityChangedNotification object:nil];
    Reachability* reach = [Reachability reachabilityWithHostname:@"www.baidu.com"];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(reachabilityChanged:)
                                                 name:kReachabilityChangedNotification
                                               object:nil];
    
    [reach startNotifier];
}

- (void)reachabilityChanged:(NSNotification*)note {
    
    Reachability * reach = [note object];
    if ([[AppDelegate sharedAppDelegate] doGetLoginedState] == SHUserLoginStateLocal_LoginSucc)
    {
        [[NetworkEngine shareInstance] setSocketConState:GSocketConStateFail];
        if([reach isReachable])
        {
#warning 需要区分wifi或者Lan
            [[SHAppInfoManager shareInstance] doSetInLAN:NO];
            [[NetworkEngine shareInstance] doRemoteConnection];
        }else{
            [XWHUDManager showErrorTipHUD:@"网络失败"];
        }
    } else
        NSLog(@"需要登录");
}




@end
