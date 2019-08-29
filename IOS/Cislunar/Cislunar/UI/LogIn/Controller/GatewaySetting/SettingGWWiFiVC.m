//
//  SettingGWWiFiVC.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/15.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "SettingGWWiFiVC.h"
#import "BonjourManager.h"
#import "EasyLinkManager.h"
#import "NSTimerImprovement.h"
#import "HUDManager.h"

@interface SettingGWWiFiVC ()

@property(nonatomic,strong)BonjourManager *bonjourManager;
@property(nonatomic,strong)EasyLinkManager *easyLinkManager;
@property (nonatomic, strong) NSTimerImprovement *timerImprovement;
@property (weak, nonatomic) IBOutlet UIButton *btnStartConfig;
@property (weak, nonatomic) IBOutlet UIView *viewInfoBg;
@property (weak, nonatomic) IBOutlet UITextField *tfWiFiName;
@property (weak, nonatomic) IBOutlet UITextField *tfWiFiPsw;
@property (weak, nonatomic) IBOutlet UITextField *tfGWName;

@end

@implementation SettingGWWiFiVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self doEasylinkConfig];
    [self doBonjourManagerConfig];
    [self doInitSubViews];
    [self doRegisterKVO];
    [self doStartTimer];
    // Do any additional setup after loading the view.
}

- (void)doInitSubViews
{
    [self setTitleViewText:@"配置网关"];
    NSString *ssid = [EASYLINK ssidForConnectedNetwork];
    self.tfWiFiName.text = ssid;
    
    self.btnStartConfig.layer.cornerRadius = 20;
    self.btnStartConfig.layer.masksToBounds = YES;
    
    self.viewInfoBg.layer.cornerRadius = 8;
    self.viewInfoBg.layer.masksToBounds = YES;
    
    [self setNavigationBarBackButtonWithTitle:@"" action:@selector(leftAction:)];
}

#pragma mark -
#pragma mark -kvo
- (void)doRegisterKVO
{
    @weakify(self);
    [self observeKeyPath:@keypath(self.bonjourManager.gwModel)
                   block:^(id value) {
                       @strongify(self);
//                       [self clickSure];
                       SHModelGateway *gwModel = (SHModelGateway *)value;
                       
                       __block BOOL isExist = NO;
                       [self.arrGWLists enumerateObjectsUsingBlock:^(SHModelGateway * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                           if ([obj.strGateway_mac_address isEqualToString:gwModel.strGateway_mac_address] || [obj.strGatewayIp isEqualToString:gwModel.strGatewayIp]) {//数组中已经存在该对象
                               *stop = YES;
                               isExist = YES;
                           }
                       }];
                       if (!isExist) {//如果不存在就添加进去
                           
                           if ([self.strFromIdentifer isEqualToString:@"fromSwitch"]) {
                               [self.navigationController popToViewController:self.navigationController.viewControllers[2] animated:YES];
                           }else{
                               [self.navigationController popToViewController:self.navigationController.viewControllers[1] animated:YES];
                           }
                       }
                   }];
}

#pragma mark -网关搜索配置模块
#pragma mark - 开启定时搜索网关
- (void)doStartTimer
{
    self.timerImprovement = [NSTimerImprovement ns_timerWithTimeInterval:1.0
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

#pragma mark - 开始配置网关
- (void)doStartConfigGateway
{
    self.easyLinkManager.wifiPSD = self.tfWiFiPsw.text;
    [self.easyLinkManager prepareEasyLinker];
    [self.easyLinkManager configureEasyLinker];
    [self.easyLinkManager runningEasyLinker];
}

#pragma mark - Easylink 配置
-(void)doEasylinkConfig
{
    self.easyLinkManager = [[EasyLinkManager alloc]init];
    self.easyLinkManager.easyLinker = [[EASYLINK alloc]init];
    //    self.easyLinkManager.wifiPSD = self.password?self.password:@"";
}

#pragma mark - bonjour 配置
- (void)doBonjourManagerConfig
{
    self.bonjourManager = [[BonjourManager alloc]init];
}


#pragma mark -
#pragma mark - action
- (void)leftAction:(UIButton *)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)btnConfigPressed:(UIButton *)sender {
    
    if ([self shouStartConfig]) {
        
        [HUDManager showHud:@"网关配置中.." customImgView:nil btnTitle:@"取消" btnFont:[UIFont systemFontOfSize:17] btnTitleColor:nil btnBackColor:nil Target:self action:@selector(clickSure) isShowMaskView:YES afterDelay:20 isHide:YES onView:self.view completionBlock:^{
            NSLog(@"自定义带按钮的视图设置显示时间");
        }];
        [self doStartConfigGateway];
    }
}

-(void)clickSure{
    [HUDManager hidenHudFromView:self.view];
}

#pragma mark -
#pragma mark - condition
#pragma mark - 是否满足登录的条件
- (BOOL)shouStartConfig
{
    if (!self.tfWiFiName.text.length){
        [XWHUDManager showWarningTipHUD:@"请输入你的WIFI名字"];
        return NO;
    }else if (!self.tfWiFiPsw.text.length){
         [XWHUDManager showWarningTipHUD:@"请输入你WIFI的密码"];
        return NO;
    }else{
        return YES;
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

@end
