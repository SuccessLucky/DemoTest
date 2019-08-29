//
//  GatewaSwitchVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "GatewaSwitchVC.h"
#import "GatewaySwitchTableView.h"
#import "LogInNav.h"
#import "GatewayListLocalVC.h"
#import "BonjourManager.h"
#import "NSTimerImprovement.h"
#import "EasyLoadingView.h"
#import "GatewayNetManager.h"

@interface GatewaSwitchVC ()

@property (strong, nonatomic) GatewaySwitchTableView *tableView;

@property (strong, nonatomic) GatewayNetManager *manager;

@property (strong, nonatomic) BonjourManager *bonjourManager;

@property (strong, nonatomic) NSString *strRemWifiMacAddr;

@property (assign, nonatomic) BOOL isSwiching;

@property (nonatomic, strong) NSTimerImprovement *timerImprovement;
@property (strong, nonatomic) EasyLoadingView *LoadingV;
@property (strong, nonatomic) NSArray *arrRemTemp;

@end

@implementation GatewaSwitchVC

//-(void)viewWillAppear:(BOOL)animated
//{
//    [super viewWillAppear:animated];
//    self.isHideNaviBar = YES;
//}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.isSwiching = NO;
    [self doInitSubViews];
    [self doRegisterKVO];
    [self doJudgeNetworkSettingGateway];
    [self doAddNoti];
    
    [self doLoadData];
    [self doAddAction];
}

#pragma mark -
#pragma mark - 初始化UI
-(void)doInitSubViews
{
    [self setTitleViewText:@"账号管理"];
    self.tabBarController.tabBar.hidden = !self.tabBarController.tabBar.hidden;
    [self setNavigationBarLeftBarButtonWithTitle:@"取消" action:@selector(leftAction:)];
    
    [self.view addSubview:self.tableView];
    [self doAddTableViewConstraints];
}

- (void)leftAction:(UIButton *)sender
{
    self.tabBarController.tabBar.hidden = !self.tabBarController.tabBar.hidden;
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark -
#pragma mark - 加载数据
- (void)doLoadData
{
    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
    [self.manager doGetGatewayListDataFromDB];
    [self.manager handleGetGatewayListFromNetwork];
}

#pragma mark -
#pragma mark - 注册kvo
- (void)doRegisterKVO
{
    @weakify(self);
    [self observeKeyPath:@keypath(self.manager.arrGatewayList)
                   block:^(id value) {
                       @strongify(self);
                       [XWHUDManager hideInWindow];
                       NSArray *arrGatewayList = (NSArray *)value;
                       if (arrGatewayList.count) {
                           self.tableView.arrList = arrGatewayList;
                       }
                   }];
    
    [self observeKeyPath:@keypath(self.manager.errorInfo)
                   block:^(id value) {
                       [XWHUDManager hideInWindow];
                       NSDictionary *dict = (NSDictionary *)value;
                       [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",dict[@"message"]]];
                       
                   }];
    
    //搜索到网关
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

#pragma mark -
#pragma mark -搜索网关
- (void)doResearchGatewayLocal
{
    [self.bonjourManager restartSearchService];
}

#pragma mark -
#pragma mark - 本地搜索出来的网关与服务器网关列表作对比
- (void)doUpdateTheTbaleListWithLocalGatewayInfo:(NSArray *)arrLocal
{
    NSMutableArray *mutArr = [NSMutableArray arrayWithArray:self.tableView.arrList];
    
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
                    modelNew.isMatched = YES;
                    [mutArr replaceObjectAtIndex:i withObject:modelNew];
                }
            }
        }
    }
    self.tableView.arrList = mutArr;
}

#pragma mark -
#pragma mark - action
- (void)doAddAction
{
    @weakify(self);
    [self.tableView setBlockDidPressed:^(NSIndexPath *indexPath, GatewaySwitchTableViewCellPressedType type) {
        @strongify(self);
        switch (type) {
            case GatewaySwitchTableViewCellPressedType_Common:
            {
                NSLog(@"选择切换网关");
                self.isSwiching = YES;
                [[SHLoginManager shareInstance] doClearAllShouldData];
                
                SHModelGateway *model = self.tableView.arrList[indexPath.row];
                
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
            }
                break;
            case GatewaySwitchTableViewCellPressedType_Header:
            {
                NSLog(@"添加网关");
                UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"LogIn" bundle:[NSBundle mainBundle]];
                GatewayListLocalVC *VC = (GatewayListLocalVC *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"GatewayListLocalVC"];
//                [self presentViewController:VC animated:YES completion:nil];
                VC.strFromIdentifer = @"fromSwitch";
                [self.navigationController pushViewController:VC animated:YES];
            }
                break;
                
            default:
                break;
        }
    }];
    
    
    [self.tableView setBlockTabelViewCellDelete:^(NSIndexPath *indexPath) {
        @strongify(self);
        [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
        SHModelGateway *gatewaySelected = self.tableView.arrList[indexPath.row];
        [self.manager handleDeleteGatewayFromServer:gatewaySelected.strGateway_mac_address
                                     CallBackHandle:^(BOOL success, id result)
        {
            if (success) {
                [XWHUDManager showSuccessTipHUD:@"删除成功"];
                if ([[[SHLoginManager shareInstance] doGetGatewayMacAddr] isEqualToString:gatewaySelected.strGateway_mac_address]) {
                    [[AppDelegate sharedAppDelegate] doLogoutClearAll];
                }else{
                    [self doLoadData];
                }
            }else{
            
                [XWHUDManager showErrorTipHUD:@"删除失败"];
            }
        }];
    }];
}

#pragma mark -
#pragma mark - init

- (BonjourManager *)bonjourManager
{
    if (!_bonjourManager) {
        _bonjourManager = [[BonjourManager alloc] init];
    }
    return _bonjourManager;
}

- (GatewayNetManager *)manager
{
    if (!_manager) {
        _manager = [GatewayNetManager new];
    }
    return _manager;
}

- (GatewaySwitchTableView *)tableView
{
    if (!_tableView) {
        _tableView = [[GatewaySwitchTableView alloc] initWithFrame:CGRectMake(0,
                                                                              0,
                                                                              UI_SCREEN_WIDTH,
                                                                              UI_SCREEN_HEIGHT)
                                                             style:UITableViewStylePlain];
    }
    return _tableView;
}

- (void)doAddTableViewConstraints
{
    @weakify(self);
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.top.equalTo(self.view.mas_top);
        make.left.equalTo(self.view.mas_left);
        make.right.equalTo(self.view.mas_right);
        make.bottom.equalTo(self.view.mas_bottom);
    }];
}

- (NSArray *)arrRemTemp
{
    if (!_arrRemTemp) {
        _arrRemTemp = [NSArray new];
    }
    return _arrRemTemp;
}

#pragma mark -
#pragma mark - 监听通知

- (void)doAddNoti
{
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




- (void)didCloudserverConnectState:(NSNotification *)notification
{
    if (self.isSwiching) {
        
        NSDictionary *dict = notification.userInfo;
        SHCloudSeverConState state = [dict[@"kRegisterCloudsever"] intValue];
        switch (state) {
            case SHCloudSeverConStateSucc:
            {
                [XWHUDManager showSuccessTipHUD:@"连接成功"];
                [[NSNotificationCenter defaultCenter] removeObserver:self name:kRegisterCloudsever object:nil];
                [[SHLoginManager shareInstance] doWriteLoginState:SHUserLoginStateLocal_LoginSucc];
                [[AppDelegate sharedAppDelegate] didLoginSuccAfterAction];
                
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
}

- (void)didGetGatewayMacAddrSelected:(NSNotification *)notification
{
    if (self.isSwiching) {
        [[NSNotificationCenter defaultCenter] removeObserver:self name:kGetGatewayMacAddr object:nil];
        [[SHLoginManager shareInstance] doWriteLoginState:SHUserLoginStateLocal_LoginSucc];
        [[AppDelegate sharedAppDelegate] didLoginSuccAfterAction];
    }
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





- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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


@end
