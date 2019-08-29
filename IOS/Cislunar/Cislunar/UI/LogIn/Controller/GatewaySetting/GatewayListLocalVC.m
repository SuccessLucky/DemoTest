//
//  GatewayListLocalVC.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/15.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "GatewayListLocalVC.h"
#import "BonjourManager.h"
#import "NSTimerImprovement.h"
#import "GatewayListTableView.h"
#import "SettingGWIntroduceVC.h"

#import "EasyLoadingView.h"
#import "GatewayNetManager.h"

@interface GatewayListLocalVC ()

@property (strong, nonatomic) GatewayListTableView *tableView;
@property (nonatomic, strong) BonjourManager *bonjourManager;
@property (nonatomic, strong) NSTimerImprovement *timerImprovement;
@property (strong, nonatomic) EasyLoadingView *LoadingV;
@property (strong, nonatomic) GatewayNetManager *gatewayManager;

@end

@implementation GatewayListLocalVC

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self doJudgeNetworkSettingGateway];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [self.bonjourManager stopSearchDevice];
    [self.timerImprovement.timer invalidate];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self doInitSubViews];
    [self doBonjourManagerConfig];
    [self doRegisterKVO];
    [self doAddNoti];
    
}

#pragma mark -
#pragma mark - 获取网关的macAddr
- (void)doAddNoti
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:kGetGatewayMacAddr object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(didGetGatewayMacAddrSelected:)
                                                 name:kGetGatewayMacAddr
                                               object:nil];
}

#pragma mark -
#pragma mark - init
- (void)doInitSubViews
{
    [self setTitleViewText: @"网关列表"];
    [self setNavigationBarLeftBarButtonWithTitle:@"取消" action:@selector(leftAction:)];
    [self setNavigationBarRightBarButtonWithTitle:@"添加" action:@selector(rightBtnAction)];
    [self.view addSubview:self.tableView];
    [self doAddAction];
}

#pragma mark - bonjour 配置
- (void)doBonjourManagerConfig
{
    self.bonjourManager = [[BonjourManager alloc]init];
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
                                                       tableType:LoacalListTableType];
    }
    return _tableView;
}

- (GatewayNetManager *)gatewayManager
{
    if (!_gatewayManager) {
        _gatewayManager = [GatewayNetManager new];
    }
    return _gatewayManager;
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
                       if (gatewayList.count) {
                           [[YCTShowView shareInstance] doHideLoading];
                           self.tableView.arrGatewayList = gatewayList;
                       }else{
                           
                       }
                   }];
}

#pragma mark - 网关配置模块
#pragma mark - 开启定时搜索网关
- (void)doStartTimer
{
    [self doSettingGateShowJuHuaLoading];
    self.timerImprovement = [NSTimerImprovement ns_timerWithTimeInterval:2
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
#pragma mark -action
- (void)doAddAction
{
    @weakify(self);
    [self.tableView setDidSelectRowBlock:^(NSIndexPath *indexPath, id object) {
        @strongify(self);
        [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
        
        [self.bonjourManager stopSearchDevice];
        [self.timerImprovement.timer invalidate];
        
        SHModelGateway *model = (SHModelGateway *)object;
        [[SHLoginManager shareInstance] doWriteRememberGatewayName:model.strGateway_gateway_name];
        [[SHLoginManager shareInstance] doWriteRememberGatewayIp:model.strGatewayIp];
        [[SHLoginManager shareInstance] doWriteRememberGatewayPort:model.strGatewayPort];
        [[SHLoginManager shareInstance] doWriteWifiMacAddr:model.strGateway_wifi_mac_address];
        [[SHLoginManager shareInstance] doWriteWifiHardware:model.strHardware];
        
        [[SHAppInfoManager shareInstance] doSetInLAN:YES];
        [[NetworkEngine shareInstance] doConnectLANWithIp:model.strGatewayIp iPort:[model.strGatewayPort intValue]];
    }];
}

#pragma mark -action
- (void)rightBtnAction
{
    [self performSegueWithIdentifier:@"SEG_TO_SettingGWIntroduceVC" sender:nil];
}

#pragma mark -重写父类方法
- (void)leftAction:(UIButton *)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark -
#pragma mark - private 特制菊花加载
- (void)doSettingGateShowJuHuaLoading
{
    self.LoadingV =  [EasyLoadingView showLoadingText:@"搜索网关中..." config:^EasyLoadingConfig *{
        return [EasyLoadingConfig shared].setLoadingType(LoadingShowTypeIndicatorLeft).setBgColor([UIColor whiteColor]);
    }];
    

    dispatch_queue_after_S(5, ^{
        [EasyLoadingView hidenLoading:self.LoadingV];
        
        if (self.tableView.arrGatewayList.count == 0) {
            [[YCTShowView shareInstance] doShowCustomAlterViewWithTitle:@"没有搜索到网关"
                                                                content:@"是否重新搜索？"
                                                       strConfirmButton:@"再来一次"
                                                       strCancellButton:@"取消"
                                                         callBackHandle:^(long index)
             {
                 if (index == 0) {
                     [self.timerImprovement.timer invalidate];
                     [self doStartTimer];
                 }else{
                     [self.timerImprovement.timer invalidate];
                 }
             }];
        }
    });
    
    
    
}

- (void)didGetGatewayMacAddrSelected:(NSNotification *)notification
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:kGetGatewayMacAddr object:nil];
    NSString *strGatewayMacAddr = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:notification.object];
    [[SHLoginManager shareInstance] doWriteGatewayMacAddr:strGatewayMacAddr];
    
    NSLog(@"在第一次登陆绑定网关的时候走了== %@",strGatewayMacAddr);
    //第一步，当获取到网关zigbeeMacAddr后，先判断是否被绑定
    @weakify(self);
    [self.gatewayManager doGetGatewayMemberDetailInfoWithGatewayMacAddr:strGatewayMacAddr
                                                         callBackHandle:^(BOOL success, id result)
     {
         [XWHUDManager hideInWindow];
         @strongify(self);
         if (success) {
             NSArray *arr = (NSArray *)result;
             if (arr.count == 0) {
                 [self handleAddTheGatewayToSever];
             }else{
                 NSDictionary *dict = arr[0];
                 NSString *strMember_type = dict[@"member_type"];
                 NSString *strAccount     = dict[@"account"];
                 NSString *strTypeInfo;
                 if ([strMember_type intValue] == 1) {
                     strTypeInfo = @"管理员";
                 }else{
                     strTypeInfo = @"网关成员";
                 }
                 NSString *strInfoAll = [NSString stringWithFormat:@"被%@账号:%@绑定了，请联系其管理员进行解绑",strTypeInfo,strAccount];
                 NSString *strAccountLocal =  [[SHLoginManager shareInstance] doGetRememberAccount];
                 if ([strAccount isEqualToString:strAccountLocal]) {
                     [self handleAddTheGatewayToSever];
                 }else{
                     [[YCTShowView shareInstance] doShowCustomAlterViewWithTitle:@"温馨提示"
                                                                         content:strInfoAll
                                                                strConfirmButton:@"确定"
                                                                strCancellButton:@"取消"
                                                                  callBackHandle:^(long index) {}];
                 }
             }
         }else{
             NSString *strCode = (NSString *)result;
             if ([strCode intValue] == 1000) {
                 [self handleAddTheGatewayToSever];
             }else{}
         }
     }];
}

#pragma mark -
#pragma mark -  添加网关到服务器操作
- (void)handleAddTheGatewayToSever
{
    @weakify(self);
    [self.gatewayManager handleTheAddGatewayDataCompleteHandle:^(BOOL success, id result) {
        @strongify(self);
        
        [XWHUDManager hideInWindow];
        if (success) {
            NSArray *arrGatewayList = (NSArray *)result;
            for (SHModelGateway *model in arrGatewayList) {
                NSString *strRemWifiMacAddr = [[SHLoginManager shareInstance] doGetWifiMacAddr];
                if ([model.strGateway_wifi_mac_address isEqualToString:strRemWifiMacAddr]) {
                    [self doClearDataBeforeAddingGateway];
                    [[SHLoginManager shareInstance] doWriteGatewayMemberType:[NSString stringWithFormat:@"%d",model.iGateway_member_type]];
                    [[SHLoginManager shareInstance] doWriteSecurityStatus:[NSString stringWithFormat:@"%d",model.iSecurityStatus]];
                    [[SHLoginManager shareInstance] doWriteLoginState:SHUserLoginStateLocal_LoginSucc];
                    break;
                }
            }
            [[AppDelegate sharedAppDelegate] didLoginSuccAfterAction];
            UIViewController *vc = self.navigationController.presentingViewController;
            [self.navigationController dismissViewControllerAnimated:YES completion:^{
                [vc dismissViewControllerAnimated:YES completion:nil];
            }];
            
        }else{
            NSDictionary *dict = (NSDictionary *)result;
            [[SHLoginManager shareInstance] doWriteLoginState:SHUserLoginState_NeedLogin];
            [XWHUDManager showErrorTipHUD:dict[@"message"]];
        }
    }];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    
    if ([segue.identifier isEqualToString:@"SEG_TO_SettingGWIntroduceVC"]) {
        SettingGWIntroduceVC *intrdVC = (SettingGWIntroduceVC *)segue.destinationViewController;
        intrdVC.arrGWLists = self.tableView.arrGatewayList;
        intrdVC.strFromIdentifer = @"fromSwitch";
    }
}

#pragma mark -
#pragma mark - 添加网关需要清理的一些数据
- (void)doClearDataBeforeAddingGateway
{
    [[SHDataBaseManager sharedInstance] doDeleteTable:Table_Cache complete:nil];
    [[SHDataBaseManager sharedInstance] doDeleteTable:Table_Cache_Room complete:nil];
    [[SHDataBaseManager sharedInstance] doDeleteTable:Table_Cache_Device complete:nil];
    [[SHDataBaseManager sharedInstance] doDeleteTable:Table_Cache_MemberControlList complete:nil];
    [[SHDataBaseManager sharedInstance] doDeleteTable:Table_Cache_Lock complete:nil];
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
        [[YCTShowView shareInstance] doShowCustomAlterViewWithTitle:@"温馨提示"
                                                            content:@"当前网络为手机自带网络，请连接到网关配置网络"
                                                   strConfirmButton:@"确定"
                                                   strCancellButton:@""
                                                     callBackHandle:^(long index)
         {
             if (index == 0) {
                 
             }
         }];
    }else if ([ToolCommon isReachableViaWiFi]){
        NSString *strSSID = [ToolCommon ssid];
        [self setTitleViewText: [NSString stringWithFormat:@"当前网络%@网关列表",strSSID]];
         [self doStartTimer];
    }
}

@end
