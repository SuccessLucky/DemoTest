//
//  YCTTabBarController.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/14.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "YCTTabBarController.h"
#import "TestTabBar.h"
#import "FirstPageNav.h"
#import "SecondPageNav.h"
#import "ThirdPageNav.h"
#import "FourPageNav.h"
//科大讯飞
#import "FZSpeakClass.h"
#import "FZProgressHudView.h"
#import "ScreenManager.h"
//网关搜搜
#import "BonjourManager.h"
#import "NSTimerImprovement.h"
#import "EasyLoadingView.h"


@interface YCTTabBarController()<AxcAE_TabBarDelegate>

@property (strong, nonatomic) FirstPageNav *firstPageNav;
@property (strong, nonatomic) SecondPageNav *secondPageNav;
@property (strong, nonatomic) ThirdPageNav *thirdPageNav;
@property (strong, nonatomic) FourPageNav *fourPageNav;
@property (nonatomic, strong) FZProgressHudView *hudView;
@property (strong, nonatomic) ScreenManager *manager;

@property (strong, nonatomic) BonjourManager *bonjourManager;
@property (nonatomic, strong) NSTimerImprovement *timerImprovement;
@property (strong, nonatomic) EasyLoadingView *LoadingV;
@property (strong, nonatomic) NSArray *arrRemTemp;


@end

@implementation YCTTabBarController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self doInitSubViews];
    [self doBonjourManagerConfig];
    [self doRegisterKVO];
    if ([[NetworkEngine shareInstance] socketConState] == GSocketConStateSucc ) {
        NSLog(@"socket连接状态连接正常!");
    }else{
        NSLog(@"socket连接状态失败!");
        [self doJudgeNetworkSettingGateway];
    }
    [self doInitIfly];   
}

- (void)doInitSubViews
{
    UIStoryboard *fistPageStoryBoard = [UIStoryboard storyboardWithName:@"FirstPage" bundle:[NSBundle mainBundle]];
    self.firstPageNav = [fistPageStoryBoard instantiateViewControllerWithIdentifier:@"FirstPageNav"];
    
    UIStoryboard *secondPageStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
    self.secondPageNav = [secondPageStoryBoard instantiateViewControllerWithIdentifier:@"SecondPageNav"];
    
    UIStoryboard *thirdPageStoryBoard = [UIStoryboard storyboardWithName:@"ThirdPage" bundle:[NSBundle mainBundle]];
    self.thirdPageNav = [thirdPageStoryBoard instantiateViewControllerWithIdentifier:@"ThirdPageNav"];
    
    UIStoryboard *fourPageStoryBoard = [UIStoryboard storyboardWithName:@"FourPage" bundle:[NSBundle mainBundle]];
    self.fourPageNav = [fourPageStoryBoard instantiateViewControllerWithIdentifier:@"FourPageNav"];
    
    // 添加子VC
    [self addChildViewControllers];
}


- (void)addChildViewControllers{
    // 创建选项卡的数据 想怎么写看自己，这块我就写笨点了
    NSArray <NSDictionary *>*VCArray =
    @[@{@"vc":self.firstPageNav,@"normalImg":@"tab_home_nor",@"selectImg":@"tab_home_pre",@"itemTitle":@"首页"},
      @{@"vc":self.secondPageNav,@"normalImg":@"tab_shebei_nor",@"selectImg":@"tab_shebei_per",@"itemTitle":@"设备"},
      @{@"vc":[UIViewController new],@"normalImg":@"",@"selectImg":@"",@"itemTitle":@"语音"},
      @{@"vc":self.thirdPageNav,@"normalImg":@"tab_click_nor",@"selectImg":@"tab_click_per",@"itemTitle":@"安防"},
      @{@"vc":self.fourPageNav,@"normalImg":@"tab_me_nor",@"selectImg":@"tab_me_pre",@"itemTitle":@"我的"}];
    // 1.遍历这个集合
    // 1.1 设置一个保存构造器的数组
    NSMutableArray *tabBarConfs = @[].mutableCopy;
    // 1.2 设置一个保存VC的数组
    NSMutableArray *tabBarVCs = @[].mutableCopy;
    [VCArray enumerateObjectsUsingBlock:^(NSDictionary * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        // 2.根据集合来创建TabBar构造器
        AxcAE_TabBarConfigModel *model = [AxcAE_TabBarConfigModel new];
        // 3.item基础数据三连
        model.itemTitle = [obj objectForKey:@"itemTitle"];
        model.selectImageName = [obj objectForKey:@"selectImg"];
        model.normalImageName = [obj objectForKey:@"normalImg"];
        // 4.设置单个选中item标题状态下的颜色
//        model.selectColor = [UIColor whiteColor];
//        model.normalColor = [UIColor whiteColor];
//        model.normalBackgroundColor = [UIColor redColor];
//        model.selectBackgroundColor = [UIColor redColor];
//        model.normalTintColor = [UIColor redColor];
//        model.selectTintColor = [UIColor redColor];
//        model.selectColor = [UIColor brownColor];
    
        
        /***********************************/
        if (idx == 2 ) { // 如果是中间的
            // 设置凸出 矩形
            model.bulgeStyle = AxcAE_TabBarConfigBulgeStyleNormal;
            // 设置凸出高度
            model.bulgeHeight = 30;
            // 设置成图片文字展示
            model.itemLayoutStyle = AxcAE_TabBarItemLayoutStyleTopPictureBottomTitle;
            // 设置图片
            model.selectImageName = @"tab_yuyin_per";
            model.normalImageName = @"tab_yuyin_nor";
            model.selectBackgroundColor = model.normalBackgroundColor = [UIColor clearColor];
            model.backgroundImageView.hidden = YES;
            // 设置图片大小c上下左右全边距
            model.componentMargin = UIEdgeInsetsMake(0, 0, 0, 0 );
            // 设置图片的高度为40
            model.icomImgViewSize = CGSizeMake(self.tabBar.frame.size.width / 5, 60);
            model.titleLabelSize = CGSizeMake(self.tabBar.frame.size.width / 5, 20);
            // 图文间距0
            model.pictureWordsMargin = 0;
            // 设置标题文字字号
            model.titleLabel.font = [UIFont systemFontOfSize:11];
            // 设置大小/边长 自动根据最大值进行裁切
            model.itemSize = CGSizeMake(self.tabBar.frame.size.width / 5 - 5.0 ,self.tabBar.frame.size.height + 20);
        }else{  // 其他的按钮来点小动画吧
            // 来点效果好看
            model.interactionEffectStyle = AxcAE_TabBarInteractionEffectStyleSpring;
            // 点击背景稍微明显点吧
            model.selectBackgroundColor = AxcAE_TabBarRGBA(248, 248, 248, 0);
            model.normalBackgroundColor = [UIColor clearColor];
        }
        // 备注 如果一步设置的VC的背景颜色，VC就会提前绘制驻留，优化这方面的话最好不要这么写
        // 示例中为了方便就在这写了
        UIViewController *vc = [obj objectForKey:@"vc"];
        vc.view.backgroundColor = [UIColor whiteColor];
        // 5.将VC添加到系统控制组
        [tabBarVCs addObject:vc];
        // 5.1添加构造Model到集合
        [tabBarConfs addObject:model];
    }];
    // 使用自定义的TabBar来帮助触发凸起按钮点击事件
    TestTabBar *testTabBar = [TestTabBar new];
    [self setValue:testTabBar forKey:@"tabBar"];
    // 5.2 设置VCs -----
    // 一定要先设置这一步，然后再进行后边的顺序，因为系统只有在setViewControllers函数后才不会再次创建UIBarButtonItem，以免造成遮挡
    // 大意就是一定要让自定义TabBar遮挡住系统的TabBar
    self.viewControllers = tabBarVCs;
    //////////////////////////////////////////////////////////////////////////
    // 注：这里方便阅读就将AE_TabBar放在这里实例化了 使用懒加载也行
    // 6.将自定义的覆盖到原来的tabBar上面
    // 这里有两种实例化方案：
    // 6.1 使用重载构造函数方式：
    //    self.axcTabBar = [[AxcAE_TabBar alloc] initWithTabBarConfig:tabBarConfs];
    // 6.2 使用Set方式：
    self.axcTabBar = [AxcAE_TabBar new] ;
    self.axcTabBar.tabBarConfig = tabBarConfs;
    // 7.设置委托
    self.axcTabBar.delegate = self;
    self.axcTabBar.backgroundColor = UIColorFromRGB(0x313c83);
    // 8.添加覆盖到上边
    [self.tabBar addSubview:self.axcTabBar];
    [self addLayoutTabBar]; // 10.添加适配
}


// 9.实现代理，如下：
static NSInteger lastIdx = 0;
- (void)axcAE_TabBar:(AxcAE_TabBar *)tabbar selectIndex:(NSInteger)index{
    if (index != 2) { // 不是中间的就切换
        // 通知 切换视图控制器
        [self setSelectedIndex:index];
        lastIdx = index;
    }else{ // 点击了中间的
        
        [self.axcTabBar setSelectIndex:lastIdx WithAnimation:NO]; // 换回上一个选中状态
        // 或者
        //        self.axcTabBar.selectIndex = lastIdx; // 不去切换TabBar的选中状态
        [self AudioRecognizerResult];
    }
}
- (void)setSelectedIndex:(NSUInteger)selectedIndex{
    [super setSelectedIndex:selectedIndex];
    if(self.axcTabBar){
        self.axcTabBar.selectIndex = selectedIndex;
    }
}

// 10.添加适配
- (void)addLayoutTabBar{
    // 使用重载viewDidLayoutSubviews实时计算坐标 （下边的 -viewDidLayoutSubviews 函数）
    // 能兼容转屏时的自动布局
}
- (void)viewDidLayoutSubviews{
    [super viewDidLayoutSubviews];
    self.axcTabBar.frame = self.tabBar.bounds;
    [self.axcTabBar viewDidLayoutItems];
}

#pragma mark -
#pragma mark - 科大讯飞
- (void)doInitIfly
{
    //配置语音识别
    //Set log level
    [IFlySetting setLogFile:LVL_NONE];
    //Set whether to output log messages in Xcode console
    [IFlySetting showLogcat:NO];
    // 初始化讯飞应用
    NSString *initString = [[NSString alloc] initWithFormat:@"appid=%@",@"56d801df"];
    [IFlySpeechUtility createUtility:initString];

    self.hudView = [[FZProgressHudView alloc] initWithTargetView:self.view];
}

#pragma mark --- 语音听写
-(void)AudioRecognizerResult
{
    //语音听写
    [self.hudView startWork:@"请讲话"];
    [FZSpeechRecognizer xf_AudioRecognizerResult:^(NSString *resText, NSError *error) {
        if (!error) {
            NSString *strResult = [NSString stringWithFormat:@"%@",resText];
            NSLog(@"语音解析后的结果：%@",strResult);
            [self.hudView showHudWithSuccess:resText andDuration:2.0];

            [self.manager doGetScreenListNameDataFromDBWithCompleteHandle:^(NSArray *arrScreenName) {
                for (int i = 0; i < arrScreenName.count; i ++) {
                    ScreenModel *screenModel = arrScreenName[i];
                    if ([strResult containsString:screenModel.strScreen_name]) {
                        NSLog(@"存在%@",screenModel.strScreen_name);
                        NSData *data = [[NetworkEngine shareInstance] doHandleSendScreenOrderToControlWithScreenNO:screenModel.str_serial_number];
                        [[NetworkEngine shareInstance] sendRequestData:data];
                    }else{
                        NSLog(@"不存在");
                    }
                }
            }];



        } else {
            [self.hudView showHudWithFailure:[NSString stringWithFormat:@"eroorCode:%ld eroorMsg:%@",(long)[error code],[error localizedDescription]] andDuration:1.0];
        }
    }];
}

#pragma mark - 拉取数据库中的场景列表
-(ScreenManager *)manager
{
    if (!_manager) {
        _manager = [ScreenManager new];
    }
    return _manager;
}

#pragma mark -
#pragma mark - 搜索网关

#pragma mark - bonjour 配置
- (NSArray *)arrRemTemp
{
    if (!_arrRemTemp) {
        _arrRemTemp = [NSArray new];
    }
    return _arrRemTemp;
}

- (void)doBonjourManagerConfig
{
    self.bonjourManager = [[BonjourManager alloc]init];
}

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
                               SHModelGateway *modelGateway = [self doJudgeCurrentWifiIsOrNotInTheLANWithArrGatewaySearched:gatewayList];
                               if (modelGateway) {
                                   [[SHLoginManager shareInstance] doWriteRememberGatewayIp:modelGateway.strGatewayIp];
                                   [[SHLoginManager shareInstance] doWriteRememberGatewayPort:modelGateway.strGatewayPort];
                                   [[SHAppInfoManager shareInstance] doSetInLAN:YES];;
                                   [[NetworkEngine shareInstance] doConnectLANWithIp:modelGateway.strGatewayIp iPort:[modelGateway.strGatewayPort intValue]];
                               }else{
                                   [[SHAppInfoManager shareInstance] doSetInLAN:NO];
                                   [[NetworkEngine shareInstance] doRemoteConnection];
                               }
                           });
                       }else{
                           
                       }
                   }];
}

- (SHModelGateway *)doJudgeCurrentWifiIsOrNotInTheLANWithArrGatewaySearched:(NSArray *)arrGatewaySearched
{
    NSString *strRemLocalWifiMacAddr = [[SHLoginManager shareInstance] doGetWifiMacAddr];
    for (int i = 0; i < arrGatewaySearched.count; i ++) {
        SHModelGateway *gatewayLAN = arrGatewaySearched[i];
        if ([strRemLocalWifiMacAddr isEqualToString:gatewayLAN.strGateway_wifi_mac_address]) {
            return gatewayLAN;
        }
    }
    return nil;
}

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
        
        [[SHAppInfoManager shareInstance] doSetInLAN:NO];
        [[NetworkEngine shareInstance] doRemoteConnection];
        
    }else if ([ToolCommon isReachableViaWiFi]){
        [self doStartTimer];
    }
}

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
#pragma mark - private 特制菊花加载
- (void)doSettingGateShowJuHuaLoading
{
    self.LoadingV =  [EasyLoadingView showLoadingText:@"搜索网关中..." config:^EasyLoadingConfig *{
        return [EasyLoadingConfig shared].setLoadingType(LoadingShowTypeIndicatorLeft).setBgColor([UIColor whiteColor]);
    }];
    
    
    dispatch_queue_after_S(1, ^{
        [EasyLoadingView hidenLoading:self.LoadingV];
        
        if (self.arrRemTemp.count == 0) {
            [[SHAppInfoManager shareInstance] doSetInLAN:NO];
            [[NetworkEngine shareInstance] doRemoteConnection];
        }
        [self.timerImprovement.timer invalidate];
        [self.bonjourManager stopSearchDevice];
    });
}

@end
