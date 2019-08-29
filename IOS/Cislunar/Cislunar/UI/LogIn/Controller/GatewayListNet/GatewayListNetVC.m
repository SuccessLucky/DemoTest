//
//  GatewayListNetVC.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/27.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "GatewayListNetVC.h"
#import "BonjourManager.h"
#import "NSTimerImprovement.h"
#import "GatewayListTableView.h"
#import "EasyLoadingView.h"

@interface GatewayListNetVC ()

@property (strong, nonatomic) GatewayListTableView *tableView;
@property (strong, nonatomic) BonjourManager *bonjourManager;
@property (nonatomic, strong) NSTimerImprovement *timerImprovement;
@property (strong, nonatomic) EasyLoadingView *LoadingV;
@property (strong, nonatomic) NSArray *arrRemTemp;

@end

@implementation GatewayListNetVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self doInitSubViews];
    [self doRegisterKVO];
    [self doLoadData];
    [self doAddNoti];
    [self doJudgeNetworkSettingGateway];
    [self doAddAction];
    
    
}

#pragma mark -
#pragma mark - init
- (void)doInitSubViews
{
    [self setTitleViewText: @"服务器网关列表"];
    [self setNavigationBarLeftBarButtonWithTitle:@"取消" action:@selector(leftAction:)];
    //    [self setNavigationBarRightBarButtonWithTitle:@"添加" action:@selector(rightBtnAction)];
    [self.view addSubview:self.tableView];
    [self doBonjourManagerConfig];
    
}

#pragma mark -
#pragma mark - 注册kvo
- (void)doRegisterKVO
{
    @weakify(self);
    [self observeKeyPath:@keypath(self.bonjourManager.arrGatewayInfoGet)
                   block:^(id value) {
                       @strongify(self);
                       NSArray *gatewayList = (NSArray *)value;
                       self.arrRemTemp = gatewayList;
                       if (gatewayList.count) {
                           dispatch_async(dispatch_get_main_queue(), ^{
                               [self doUpdateTheTbaleListWithLocalGatewayInfo:gatewayList];
                           });
                       }else{
                           
                       }
                   }];
}

- (void)doLoadData
{
    self.tableView.arrGatewayList = self.gatewayList;
}


- (void)doRemoveNoti
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:kRegisterCloudsever object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:kGetGatewayMacAddr object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:kGSocketConnectNotify object:nil];
}

- (void)doAddNoti
{
//    [self doRemoveNoti];
//    [[NSNotificationCenter defaultCenter] removeObserver:self];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(didCloudserverConnectState:)
                                                 name:kRegisterCloudsever
                                               object:nil];
    
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(didGetGatewayMacAddrSelected:)
                                                 name:kGetGatewayMacAddr
                                               object:nil];
    
    //检测网络状态的
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(didSocketConnectStateChange:)
                                                 name:kGSocketConnectNotify
                                               object:nil];
    
}

- (void)didSocketConnectStateChange:(NSNotification *)notification
{
    NSDictionary *uInfo = [notification userInfo];
    GSocketConState socketConState = (GSocketConState)[[uInfo objectForKey:@"connectState"] intValue];
    
    if (socketConState == GSocketConStateSucc)
    {
        NSLog(@"连接成功");
    }else if(socketConState == GSocketConStateConnecting){
        NSLog(@"正在连接");
        
    }else{
        [XWHUDManager hideInWindow];
        [XWHUDManager showErrorTipHUD:@"socket连接失败"];
        NSLog(@"连接失败");
    }
}

- (void)didGetGatewayMacAddrSelected:(NSNotification *)notification
{
    [[AppDelegate sharedAppDelegate] didLoginSuccAfterAction];
    [[SHLoginManager shareInstance] doWriteLoginState:SHUserLoginStateLocal_LoginSucc];
    UIViewController *vc = self.navigationController.presentingViewController;
    [self.navigationController dismissViewControllerAnimated:YES completion:^{
        [vc dismissViewControllerAnimated:YES completion:nil];
    }];
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
            [[AppDelegate sharedAppDelegate] didLoginSuccAfterAction];
            [[SHLoginManager shareInstance] doWriteLoginState:SHUserLoginStateLocal_LoginSucc];
            UIViewController *vc = self.navigationController.presentingViewController;
            [self.navigationController dismissViewControllerAnimated:YES completion:^{
                [vc dismissViewControllerAnimated:YES completion:nil];
            }];
        }
            break;
        case SHCloudSeverConState_NORecord:
        {
            [XWHUDManager showWarningTipHUD:@"网关没有注册"];
            [[SHLoginManager shareInstance] doWriteLoginState:SHUserLoginState_NeedLogin];
        }
            break;
        case SHCloudSeverConState_DisCon:
        {
            [XWHUDManager showErrorTipHUD:@"连接失败"];
            [[SHLoginManager shareInstance] doWriteLoginState:SHUserLoginState_NeedLogin];
        }
            break;
            
        default:
        {
            [[SHLoginManager shareInstance] doWriteLoginState:SHUserLoginState_NeedLogin];
        }
            break;
    }
}


#pragma mark - 网关配置模块
#pragma mark - 开启定时搜索网关
- (void)doStartTimer
{
    [self doSettingGateShowJuHuaLoading];
    self.timerImprovement = [NSTimerImprovement ns_timerWithTimeInterval:0.1
                                                                  target:self
                                                                selector:@selector(doResearchGatewayLocal)
                                                                userInfo:nil
                                                                 repeats:YES];
}

#pragma mark -  搜索网关
- (void)doResearchGatewayLocal
{
    [self.bonjourManager restartSearchService];
}

#pragma mark -
#pragma mark - action

- (void)doAddAction
{
    [self.tableView setDidSelectRowBlock:^(NSIndexPath *indexPath, id object) {
        [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
        SHModelGateway *model = (SHModelGateway *)object;
        [[SHLoginManager shareInstance] doWriteRememberGatewayName:model.strGateway_gateway_name];
        [[SHLoginManager shareInstance] doWriteGatewayMemberType:[NSString stringWithFormat:@"%d",model.iGateway_member_type]];
        [[SHLoginManager shareInstance] doWriteSecurityStatus:[NSString stringWithFormat:@"%d",model.iSecurityStatus]];
        [[SHLoginManager shareInstance] doWriteGatewayMacAddr:model.strGateway_mac_address];
        [[SHLoginManager shareInstance] doWriteWifiMacAddr:model.strGateway_wifi_mac_address];
        
        [[SHLoginManager shareInstance] doWriteWifiHardware:model.strHardware];
        
        
        if (model.isMatched) {
            [[SHLoginManager shareInstance] doWriteRememberGatewayIp:model.strGatewayIp];
            [[SHLoginManager shareInstance] doWriteRememberGatewayPort:model.strGatewayPort];
            [[SHAppInfoManager shareInstance] doSetInLAN:YES];;
            [[NetworkEngine shareInstance] doConnectLANWithIp:model.strGatewayIp iPort:[model.strGatewayPort intValue]];
        }else{
            [[SHAppInfoManager shareInstance] doSetInLAN:NO];
            [[NetworkEngine shareInstance] doRemoteConnection];
        }
        
        
    }];
}

- (void)leftAction:(UIButton *)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

/*
#pragma mark -action
- (void)rightBtnAction
{
    [self performSegueWithIdentifier:@"SEG_TO_SettingGWIntroduceVC" sender:nil];
}
 */

#pragma mark -
#pragma mark - 本地搜索出来的网关与服务器网关列表作对比
- (void)doUpdateTheTbaleListWithLocalGatewayInfo:(NSArray *)arrLocal
{
    NSMutableArray *mutArr = [NSMutableArray arrayWithArray:self.gatewayList];
    
    for (int i = 0; i < mutArr.count; i ++) {
        SHModelGateway *netModel = mutArr[i];
        if (arrLocal.count) {
            for (int j = 0; j < arrLocal.count; j++) {
                SHModelGateway *gatewayLAN = arrLocal[j];
                if ([netModel.strGateway_wifi_mac_address isEqualToString:gatewayLAN.strGateway_wifi_mac_address]){
                    SHModelGateway *modelNew = [SHModelGateway new];
                    modelNew.strGateway_gateway_name = netModel.strGateway_gateway_name;
                    modelNew.strGateway_wifi_mac_address = netModel.strGateway_wifi_mac_address;
                    modelNew.iGateway_member_type = netModel.iGateway_member_type;
                    modelNew.iSecurityStatus = netModel.iSecurityStatus;
                    modelNew.strGateway_mac_address = netModel.strGateway_mac_address;
                    modelNew.strGatewayIp = gatewayLAN.strGatewayIp;
                    modelNew.strGatewayPort = gatewayLAN.strGatewayPort;
                    modelNew.strHardware = gatewayLAN.strHardware;
                    modelNew.isMatched = YES;
                    [mutArr replaceObjectAtIndex:i withObject:modelNew];
                }
            }
        }
    }
    self.tableView.arrGatewayList = mutArr;
}


#pragma mark - bonjour 配置
- (void)doBonjourManagerConfig
{
    self.bonjourManager = [[BonjourManager alloc]init];
}

- (NSArray *)arrRemTemp
{
    if (!_arrRemTemp) {
        _arrRemTemp = [NSArray new];
    }
    return _arrRemTemp;
}

#pragma mark - tableView
-(GatewayListTableView *)tableView
{
    if (!_tableView) {
        _tableView = [[GatewayListTableView alloc] initWithFrame:CGRectMake(0,
                                                                            0,
                                                                            UI_SCREEN_WIDTH,
                                                                            UI_SCREEN_HEIGHT - 64 )
                                                           style:UITableViewStylePlain
                                                       tableType:NetListTableType];
    }
    return _tableView;
}

#pragma mark -
#pragma mark -logic1
- (void)doJudgeNetworkSettingGateway
{
    if ([ToolCommon isNotReachable]) {
        [[YCTShowView shareInstance] doShowCustomAlterViewWithTitle:@"温馨提示"
                                                            content:@"目前网络不可用，请连接网络"
                                                   strConfirmButton:@"确定"
                                                   strCancellButton:@""
                                                     callBackHandle:^(long index)
         {
             if (index == 0) {
                 
             }
         }];
    }else if ([ToolCommon isReachableWWAN]){
        
    }else if ([ToolCommon isReachableViaWiFi]){
        [self doStartTimer];
    }
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

#pragma mark -
#pragma mark - private 特制菊花加载
- (void)doSettingGateShowJuHuaLoading
{
    self.LoadingV =  [EasyLoadingView showLoadingText:@"搜索网关中..." config:^EasyLoadingConfig *{
        return [EasyLoadingConfig shared].setLoadingType(LoadingShowTypeIndicatorLeft).setBgColor([UIColor whiteColor]);
    }];
    
    
    dispatch_queue_after_S(1, ^{
        [EasyLoadingView hidenLoading:self.LoadingV];

        if (self.arrRemTemp.count == 0) {
            [[YCTShowView shareInstance] showErrorDefult:@"请进行远程连接"];
        }
        [self.timerImprovement.timer invalidate];
        [self.bonjourManager stopSearchDevice];
    });
}


@end
