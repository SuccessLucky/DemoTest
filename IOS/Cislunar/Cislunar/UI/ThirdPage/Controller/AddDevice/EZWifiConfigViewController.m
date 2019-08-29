//
//  EZWifiConfigViewController.m
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/10/29.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import "EZWifiConfigViewController.h"
#import "Masonry.h"
#import "DDKit.h"
#import "EZDeviceTableViewController.h"
#import "EZDdnsDeviceTableViewController.h"

// 设备bonjour搜索到的状态标示
typedef enum _DEVICE_STATE
{
    STATE_NONE = 0,          // 设备状态-无
    STATE_WIFI,              // wifi已连接
    STATE_LINE,              // 有线已连接
    STATE_PLAT,              // 平台已注册
    STATE_SUCC,              // 已添加成功
} DEVICE_STATE;

@interface EZWifiConfigViewController ()
{
    NSTimer *_countTimer;
    
    NSTimeInterval _interval;
}

@property (nonatomic, strong) UIImageView *timerImageView; //添加设备操作有效时间视图
@property (nonatomic, strong) UIView *failedTipsView; //添加失败提示视图
@property (nonatomic, strong) UIImageView *successWifiImageView;  //Wi-Fi成功标签图
@property (nonatomic, strong) UIImageView *successRegisterImageView; //注册成功标签图
@property (nonatomic, strong) UIImageView *animationImageView; //添加设备动画视图
@property (nonatomic, strong) UIView *addTipsView; //添加设备阶段提示视图
@property (nonatomic, strong) UIButton *helpButton; //帮助链接按钮

@property (nonatomic, strong) UILabel *wifiLabel;
@property (nonatomic, strong) UILabel *registerLabel;
@property (nonatomic, strong) UILabel *bindLabel;

@property (nonatomic, strong) UILabel *completionLabel;
@property (nonatomic, strong) UIButton *completionButton; //设备添加完成按钮

@property (nonatomic) DEVICE_STATE enState;

@end

@implementation EZWifiConfigViewController

- (void)dealloc
{
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = NSLocalizedString(@"wifi_add_device_title", @"第三步，添加设备");
    
    // key为空可以传个任意字符过去
    if ([self.password length] == 0)
    {
        self.password = nil;
    }
    
    [self wifiConfigStart];
}

- (void)viewWillDisappear:(BOOL)animated
{
    if (!_isAddDeviceSuccessed)
    {
        [EZOPENSDK stopConfigWifi];
    }
    if (_countTimer)
    {
        [_countTimer invalidate];
        _countTimer = nil;
    }
    [super viewWillDisappear:animated];
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

#pragma mark - UIAlertViewDelgate Methods

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (alertView.tag == 0xaa && buttonIndex == 1)
    {
        self.enState = STATE_PLAT;
        [self createTimerWithTimeOut:30];
        [GlobalKit shareKit].deviceVerifyCode = [alertView textFieldAtIndex:0].text;
        [EZOPENSDK addDevice:[GlobalKit shareKit].deviceSerialNo
                  verifyCode:[GlobalKit shareKit].deviceVerifyCode
                  completion:^(NSError *error) {
                      [self handleTheError:error];
                  }];
    }
    else if (alertView.tag == 0xbb && buttonIndex == 1)
    {
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"device_input_vierify_code", @"请输入设备验证码")  message:@"" delegate:self cancelButtonTitle:NSLocalizedString(@"cancel",@"取消") otherButtonTitles:NSLocalizedString(@"done",@"确定"), nil];
        alertView.alertViewStyle = UIAlertViewStyleSecureTextInput;
        alertView.tag = 0xaa;
        [alertView show];
    }
}


#pragma mark - Custom UI Methods

- (void)wifiConfigStart
{
    if(!_isAddDeviceSuccessed)
    {
        [self createAddDeviceInitView];
        NSInteger mode = 0;
        mode |= self.supportSmartMode?EZWiFiConfigSmart:0;
        mode |= self.supportSoundMode?EZWiFiConfigWave:0;

        __weak typeof(self) weakSelf = self;
        [EZOPENSDK startConfigWifi:weakSelf.ssid
                          password:weakSelf.password
                      deviceSerial:[GlobalKit shareKit].deviceSerialNo
                              mode:mode
                      deviceStatus:^(EZWifiConfigStatus status, NSString *deviceSerial) {
                          if (status == DEVICE_WIFI_CONNECTING)
                          {
                              weakSelf.enState = STATE_NONE;
                              [weakSelf createTimerWithTimeOut:60];
                          }
                          else if (status == DEVICE_WIFI_CONNECTED)
                          {
                              if(weakSelf.enState != STATE_WIFI){
                                  weakSelf.enState = STATE_WIFI;
                                  [weakSelf createTimerWithTimeOut:60];
                              }
                          }
                          else if (status == DEVICE_PLATFORM_REGISTED)
                          {
                              weakSelf.enState = STATE_PLAT;
                              [weakSelf createTimerWithTimeOut:30];
                              if([GlobalKit shareKit].deviceVerifyCode != nil)
                              {
                                  [EZOPENSDK addDevice:[GlobalKit shareKit].deviceSerialNo
                                            verifyCode:[GlobalKit shareKit].deviceVerifyCode
                                            completion:^(NSError *error) {
                                                [weakSelf handleTheError:error];
                                            }];
                              }
                              else
                              {
                                  UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"device_input_vierify_code", @"请输入设备验证码") message:@"" delegate:weakSelf cancelButtonTitle:NSLocalizedString(@"cancel",@"取消") otherButtonTitles:NSLocalizedString(@"done",@"确定"), nil];
                                  alertView.alertViewStyle = UIAlertViewStyleSecureTextInput;
                                  alertView.tag = 0xaa;
                                  [alertView show];
                                  dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2.0f * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                                      if ([_countTimer isValid])
                                      {
                                          [_countTimer invalidate];
                                          _countTimer = nil;
                                      }
                                  });
                              }
                          }
                      }];
    }
    else
    {
        [self createAddDeviceInitView];

        _enState = STATE_SUCC;
        [self showTipsView];
    }
}

- (void)createTimerWithTimeOut:(NSInteger)timeout
{
    if ([_countTimer isValid])
    {
        [_countTimer invalidate];
        _countTimer = nil;
    }
    
    _interval = timeout; //各阶段UI显示时间
    UILabel *timeLabel = (UILabel *)[self.timerImageView viewWithTag:0x11c];
    timeLabel.text = [NSString stringWithFormat:@"%d",(int)timeout];
    
    [self showTipsView];
    
    _countTimer = [NSTimer scheduledTimerWithTimeInterval:1.0f
                                                   target:self
                                                 selector:@selector(countDownBindDevice)
                                                 userInfo:nil
                                                  repeats:YES];
}

/**
 * 各阶段倒计时控制
 **/
- (void)countDownBindDevice
{
    _interval--;
    
    if (_interval < 0)
    {
        _interval = 0;
        
        if ([_countTimer isValid])
        {
            [_countTimer invalidate];
            _countTimer = nil;
        }
        
        //超时以后查询一次设备信息
        
        [EZOpenSDK probeDeviceInfo:[GlobalKit shareKit].deviceSerialNo
                        completion:^(EZProbeDeviceInfo *deviceInfo, NSError *error) {
            
                            if (error)
                            {
                                //有错误直接显示错误的UI
                                [self showFailedView];
                            }
                            else
                            {
                                if ([GlobalKit shareKit].deviceVerifyCode != nil)
                                {
                                    [EZOPENSDK addDevice:[GlobalKit shareKit].deviceSerialNo
                                              verifyCode:[GlobalKit shareKit].deviceVerifyCode
                                              completion:^(NSError *error) {
                                                  if (!error)
                                                  {
                                                      [self handleTheError:error];
                                                  }
                                                  else
                                                  {
                                                      [self showFailedView];
                                                  }
                                              }];
                                }
                                else
                                {
                                    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"device_input_vierify_code", @"请输入设备验证码") message:@"" delegate:self cancelButtonTitle:NSLocalizedString(@"cancel",@"取消") otherButtonTitles:NSLocalizedString(@"done",@"确定"), nil];
                                    alertView.alertViewStyle = UIAlertViewStyleSecureTextInput;
                                    alertView.tag = 0xaa;
                                    [alertView show];
                                }
                            }
                            
        }];
        
        /*
        [EZOPENSDK probeDeviceInfo:[GlobalKit shareKit].deviceSerialNo
                        deviceType:[GlobalKit shareKit].deviceModel
                        completion:^(EZProbeDeviceInfo *deviceInfo, NSError *error) {
                            if (error)
                            {
                                //有错误直接显示错误的UI
                                [self showFailedView];
                            }
                            else
                            {
                                if ([GlobalKit shareKit].deviceVerifyCode != nil)
                                {
                                    [EZOPENSDK addDevice:[GlobalKit shareKit].deviceSerialNo
                                              verifyCode:[GlobalKit shareKit].deviceVerifyCode
                                              completion:^(NSError *error) {
                                                  if (!error)
                                                  {
                                                      [self handleTheError:error];
                                                  }
                                                  else
                                                  {
                                                      [self showFailedView];
                                                  }
                                              }];
                                }
                                else
                                {
                                    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"device_input_vierify_code", @"请输入设备验证码") message:@"" delegate:self cancelButtonTitle:NSLocalizedString(@"cancel",@"取消") otherButtonTitles:NSLocalizedString(@"done",@"确定"), nil];
                                    alertView.alertViewStyle = UIAlertViewStyleSecureTextInput;
                                    alertView.tag = 0xaa;
                                    [alertView show];
                                }
                            }
                        }];
         */
    }
    
    UILabel *timeLabel = (UILabel *)[self.timerImageView viewWithTag:0x11c];
    timeLabel.text = [NSString stringWithFormat:@"%d",(int)_interval];
}

/*
 * 设备添加初始界面构造
 */
- (void)createAddDeviceInitView
{
    [self.view addSubview:self.animationImageView];
    [self.animationImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(@64);
        make.leading.trailing.mas_equalTo(@0);
        make.height.mas_equalTo(@(185 * [UIScreen mainScreen].bounds.size.width/320.0f));
    }];
    
    if (STATE_LINE == _enState)
    {
        // 动画image
        self.animationImageView.animationImages = [NSArray arrayWithObjects:
                                                   [UIImage imageNamed:@"link_account1"],
                                                   [UIImage imageNamed:@"link_account2"],
                                                   [UIImage imageNamed:@"link_account3"],
                                                   [UIImage imageNamed:@"link_account4"],
                                                   nil];
    }
    else
    {
        
        
        // 动画image
        self.animationImageView.animationImages = [NSArray arrayWithObjects:
                                                   [UIImage imageNamed:@"connect_wifi1"],
                                                   [UIImage imageNamed:@"connect_wifi2"],
                                                   [UIImage imageNamed:@"connect_wifi3"],
                                                   [UIImage imageNamed:@"connect_wifi4"],
                                                   nil];
    }
    
    self.animationImageView.animationDuration = 1.5f;
    [self.animationImageView startAnimating];
    
    //配置添加阶段提示界面构造
    [self createWifiConfigAddTipsView];
}

/*
 * “设备wifi配置、添加”阶段提示界面构造
 *
 */
- (void)createWifiConfigAddTipsView
{
    [self.view addSubview:self.addTipsView];
    
    [self.addTipsView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.animationImageView.mas_bottom).offset(20);
        make.leading.trailing.bottom.mas_equalTo(@0);
    }];
    
    //wifi配置阶段提示label
    [self.addTipsView addSubview:self.wifiLabel];
    
    [self.wifiLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.addTipsView.mas_top).offset(30);
        make.leading.trailing.mas_equalTo(@0);
        make.height.mas_equalTo(@35);
    }];
    
    //平台注册阶段提示label
    [self.addTipsView addSubview:self.registerLabel];
    
    [self.registerLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.wifiLabel.mas_bottom).offset(5);
        make.leading.trailing.mas_equalTo(@0);
        make.height.mas_equalTo(@35);
    }];
    
    //绑定账号阶段提示label
    [self.addTipsView addSubview:self.bindLabel];
    
    [self.bindLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.registerLabel.mas_bottom).offset(5);
        make.leading.trailing.mas_equalTo(@0);
        make.height.mas_equalTo(@35);
    }];
    
    //倒计时显示label
    [self.addTipsView addSubview:self.timerImageView];
    
    [self.timerImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.width.height.mas_equalTo(@25);
        make.trailing.mas_equalTo(@-15);
        make.centerY.mas_equalTo(self.wifiLabel.mas_centerY);
    }];
    
    if ([self.timerImageView viewWithTag:0x11c])
    {
        [[self.timerImageView viewWithTag:0x11c] removeFromSuperview];
    }
    
    UILabel *operationTime = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 25, 25)];
    operationTime.font = [UIFont systemFontOfSize:14.0f];
    operationTime.textColor = [UIColor dd_hexStringToColor:@"0xffffff"];
    operationTime.backgroundColor = [UIColor clearColor];
    operationTime.textAlignment = NSTextAlignmentCenter;
    operationTime.tag = 0x11c;
    operationTime.text = @"60";//默认60秒为添加流程总时耗
    [self.timerImageView addSubview:operationTime];
    
    //wifi成功标签图
    [self.addTipsView addSubview:self.successWifiImageView];
    [self.successWifiImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.width.height.mas_equalTo(@25);
        make.trailing.mas_equalTo(@-15);
        make.centerY.mas_equalTo(self.wifiLabel.mas_centerY);
    }];
    
    //注册成功标签图
    [self.addTipsView addSubview:self.successRegisterImageView];
    [self.successRegisterImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.width.height.mas_equalTo(@25);
        make.trailing.mas_equalTo(@-15);
        make.centerY.mas_equalTo(self.registerLabel.mas_centerY);
    }];
    
    //阶段成功标示图标,默认隐藏
    self.successRegisterImageView.hidden = YES;
    self.successWifiImageView.hidden     = YES;
}

/**
 *  显示提示界面，根据state可以标示不同的提示语
 */
- (void)showTipsView
{
    self.failedTipsView.hidden = YES;
    self.addTipsView.hidden    = NO;
    self.timerImageView.hidden = NO;
    
    switch(_enState)
    {
        case STATE_NONE:
        {
            self.successWifiImageView.hidden = YES;
            self.successRegisterImageView.hidden = YES;
            
            //字体颜色设置
            self.wifiLabel.font = [UIFont systemFontOfSize:17.0];
            self.wifiLabel.textColor = [UIColor dd_hexStringToColor:@"0x333333"];
            
            self.registerLabel.font = [UIFont systemFontOfSize:15.0f];
            self.registerLabel.textColor = [UIColor dd_hexStringToColor:@"0x666666"];

            self.bindLabel.font = [UIFont systemFontOfSize:15.0f];
            self.bindLabel.textColor = [UIColor dd_hexStringToColor:@"0x666666"];

            self.wifiLabel.text = NSLocalizedString(@"wifi_connecting_net", @"萤小石正在努力连接Wi-Fi网络");
            self.registerLabel.text = NSLocalizedString(@"wifi_register_server", @"注册平台服务器");
            self.bindLabel.text = NSLocalizedString(@"wifi_bind_account", @"绑定你的账号");
            
            //计算文字的长度
            CGSize labelSize = [self.wifiLabel.text sizeWithFont:self.wifiLabel.font];
            //计算padding
            CGFloat padding = (self.view.bounds.size.width - labelSize.width)/2.0f - 27.0f;
            
            [self.timerImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.width.height.mas_equalTo(@25);
                make.trailing.mas_equalTo(@(-padding));
                make.centerY.mas_equalTo(self.wifiLabel.mas_centerY);
            }];
        }
            break;
        case STATE_WIFI:
        {
            self.successWifiImageView.hidden = NO;
            self.successRegisterImageView.hidden = YES;
            
            self.wifiLabel.font = [UIFont systemFontOfSize:15.0];
            self.wifiLabel.textColor = [UIColor dd_hexStringToColor:@"0x666666"];
            
            self.registerLabel.font = [UIFont systemFontOfSize:17.0f];
            self.registerLabel.textColor = [UIColor dd_hexStringToColor:@"0x333333"];
            
            self.bindLabel.font = [UIFont systemFontOfSize:15.0f];
            self.bindLabel.textColor = [UIColor dd_hexStringToColor:@"0x666666"];
            
            self.wifiLabel.text = NSLocalizedString(@"wifi_already_configed", @"已配置好Wi-Fi网络");
            
            //计算文字的长度
            CGSize labelSize = [self.wifiLabel.text sizeWithFont:self.wifiLabel.font];
            //计算padding
            CGFloat padding = (self.view.bounds.size.width - labelSize.width)/2.0f - 27.0f;
            
            [self.successWifiImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.width.height.mas_equalTo(@25);
                make.trailing.mas_equalTo(@(-padding));
                make.centerY.mas_equalTo(self.wifiLabel.mas_centerY);
            }];
            
            self.registerLabel.text = NSLocalizedString(@"wifi_registering_server", @"快了快了,正在注册平台服务器");
            self.bindLabel.text = NSLocalizedString(@"wifi_bind_account", @"绑定你的账号");
            
            //计算文字的长度
            labelSize = [self.registerLabel.text sizeWithFont:self.registerLabel.font];
            //计算padding
            padding = (self.view.bounds.size.width - labelSize.width)/2.0f - 27.0f;
            
            [self.timerImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.width.height.mas_equalTo(@25);
                make.trailing.mas_equalTo(@(-padding));
                make.centerY.mas_equalTo(self.registerLabel.mas_centerY);
            }];
            
            break;
        }
        case STATE_LINE:      //有线连接添加逻辑
        {
            self.successWifiImageView.hidden = YES;
            self.successRegisterImageView.hidden = YES;
            
            self.wifiLabel.hidden = YES;
            self.registerLabel.hidden = YES;
            self.timerImageView.hidden = NO;
            
            self.bindLabel.font = [UIFont systemFontOfSize:17.0];
            self.bindLabel.textColor = [UIColor dd_hexStringToColor:@"0x333333"];
            self.bindLabel.text = NSLocalizedString(@"wifi_bind_account_result", @"绑到你的账号下就大功告成了哦");
            
            //计算文字的长度
            CGSize labelSize = [self.bindLabel.text sizeWithFont:self.bindLabel.font];
            //计算padding
            CGFloat padding = (self.view.bounds.size.width - labelSize.width)/2.0f - 27.0f;
            
            [self.timerImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.width.height.mas_equalTo(@25);
                make.trailing.mas_equalTo(@(-padding));
                make.centerY.mas_equalTo(self.bindLabel.mas_centerY);
            }];
            
            break;
        }
        case STATE_PLAT:
        {
            self.successWifiImageView.hidden = NO;
            self.successRegisterImageView.hidden = NO;
            
            self.wifiLabel.font = [UIFont systemFontOfSize:15.0];
            self.wifiLabel.textColor = [UIColor dd_hexStringToColor:@"0x666666"];
            
            self.registerLabel.font = [UIFont systemFontOfSize:15.0f];
            self.registerLabel.textColor = [UIColor dd_hexStringToColor:@"0x666666"];
            
            self.bindLabel.font = [UIFont systemFontOfSize:17.0f];
            self.bindLabel.textColor = [UIColor dd_hexStringToColor:@"0x333333"];
            
            self.wifiLabel.text = NSLocalizedString(@"wifi_already_configed",@"已配置好Wi-Fi网络");
            self.registerLabel.text = NSLocalizedString(@"wifi_already_registered_server", @"已注册到萤石平台");
            //计算文字的长度
            CGSize labelSize = [self.registerLabel.text sizeWithFont:self.registerLabel.font];
            //计算padding
            CGFloat padding = (self.view.bounds.size.width - labelSize.width)/2.0f - 27.0f;
            
            [self.successWifiImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.width.height.mas_equalTo(@25);
                make.trailing.mas_equalTo(@(-padding));
                make.centerY.mas_equalTo(self.wifiLabel.mas_centerY);
            }];
            
            [self.successRegisterImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.width.height.mas_equalTo(@25);
                make.trailing.mas_equalTo(@(-padding));
                make.centerY.mas_equalTo(self.registerLabel.mas_centerY);
            }];
            
            self.bindLabel.text = NSLocalizedString(@"wifi_bind_account_result",@"绑到你的账号下就大功告成了哦");
            
            //计算文字的长度
            labelSize = [self.bindLabel.text sizeWithFont:self.bindLabel.font];
            //计算padding
            padding = (self.view.bounds.size.width - labelSize.width)/2.0f - 27.0f;
            
            [self.timerImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.width.height.mas_equalTo(@25);
                make.trailing.mas_equalTo(@(-padding));
                make.centerY.mas_equalTo(self.bindLabel.mas_centerY);
            }];
            
            break;
        }
        case STATE_SUCC:
        {
            //隐藏阶段提示界面，展示成功界面
            self.addTipsView.hidden = YES;
            
            [self.animationImageView stopAnimating];
            self.animationImageView.animationImages = nil;
            //调整图片高度
            
            self.animationImageView.image = [UIImage imageNamed:@"addDevice_success"];//成功标示图片
            
            [self.animationImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.top.mas_equalTo(@90);
                make.leading.trailing.mas_equalTo(@0);
                make.height.mas_equalTo(@(185 * [UIScreen mainScreen].bounds.size.width/320.0f));
            }];
            
            [self.view addSubview:self.completionLabel];
            
            [self.completionLabel mas_makeConstraints:^(MASConstraintMaker *make) {
                make.top.mas_equalTo(self.animationImageView.mas_bottom).offset(10);
                make.leading.trailing.mas_equalTo(@0);
                make.height.mas_equalTo(@20);
            }];
            
            [self.view addSubview:self.completionButton];
            
            [self.completionButton mas_makeConstraints:^(MASConstraintMaker *make) {
                make.top.mas_equalTo(self.completionLabel.mas_bottom).offset(15);
                make.width.mas_equalTo(@285);
                make.height.mas_equalTo(@38);
                make.centerX.mas_equalTo(self.view.mas_centerX);
            }];
            
            break;
        }
        default:
            break;
    }
}

/**
 *  显示失败界面，根据state可以标示不同的失败类型
 */
- (void)showFailedView
{
    [EZOPENSDK stopConfigWifi];
    
    //失败-移除阶段提示视图
    self.addTipsView.hidden = YES;
    
    //失败-配置特效动画停止
    [self.animationImageView stopAnimating];
    
    [self.view addSubview:self.failedTipsView];
    
    [self.failedTipsView.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    
    [self.failedTipsView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.animationImageView.mas_bottom).offset(20);
        make.leading.trailing.bottom.mas_equalTo(@0);
    }];
    
    UILabel *failedLabel = [[UILabel alloc] initWithFrame:CGRectZero];
    failedLabel.numberOfLines = 0;
    failedLabel.lineBreakMode = NSLineBreakByWordWrapping;
    failedLabel.font = [UIFont systemFontOfSize:15.0f];
    failedLabel.textColor = [UIColor dd_hexStringToColor:@"0x666666"];
    failedLabel.textAlignment = NSTextAlignmentCenter;
    failedLabel.backgroundColor = [UIColor clearColor];
    failedLabel.tag = 0x22a;
    [self.failedTipsView addSubview:failedLabel];
    
    [failedLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(@0);
        make.leading.mas_equalTo(@15);
        make.trailing.mas_equalTo(@-15);
        make.height.mas_equalTo(@45);
    }];
    
    UIButton *retryButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [retryButton setBackgroundImage:[UIImage imageNamed:@"blue_button"] forState:UIControlStateNormal];
    [retryButton setBackgroundImage:[UIImage imageNamed:@"blue_button_sel"] forState:UIControlStateHighlighted];
    [retryButton setTitle:NSLocalizedString(@"retry", @"重试") forState:UIControlStateNormal];
    [retryButton addTarget:self action:@selector(retryButtonClicked:) forControlEvents:UIControlEventTouchUpInside];
    
    retryButton.tag = 0x22c;
    [self.failedTipsView addSubview:retryButton];
    
    [retryButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(failedLabel.mas_bottom).offset(10);
        make.width.mas_equalTo(@285);
        make.height.mas_equalTo(@38);
        make.centerX.mas_equalTo(self.view.mas_centerX);
    }];
    
    UILabel *failedLab = (UILabel *)[self.failedTipsView viewWithTag:0x22a];
    
    switch (_enState)
    {
        case STATE_NONE:
        {
            failedLab.text = NSLocalizedString(@"wifi_config_fail", @"配置Wi-Fi失败，请重试或返回检查Wi-Fi密码是否输入正确");
            
            self.animationImageView.image = [UIImage imageNamed:@"failure_wifi"]; //wifi连接失败
        }
            break;
        case STATE_WIFI:
        case STATE_LINE:
        {
            failedLab.text = NSLocalizedString(@"wifi_register_fail", @"Wi-Fi配置成功,注册平台失败,请检查设备网络后重试");
            
            self.animationImageView.image = [UIImage imageNamed:@"failure_server"];//设备注册平台失败
        }
            break;
        case STATE_PLAT:
        {
            failedLab.text = NSLocalizedString(@"wifi_bind_fail", @"Wi-Fi配置成功,绑定账号失败,请重试");

            self.animationImageView.image = [UIImage imageNamed:@"failure_account"];//设备绑定失败
        }
            break;
        default:
            break;
    }
    
    self.failedTipsView.hidden = NO;
}

- (void)handleTheError:(NSError *)error
{
    [EZOPENSDK stopConfigWifi];
    
    if (!error)
    {
        if ([_countTimer isValid])
        {
            [_countTimer invalidate];
            _countTimer = nil;
        }
        _enState = STATE_SUCC;
        [self showTipsView];
        return;
    }
    
    if (error.code == 120010)
    {
        UIAlertView *retryAlertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"device_verify_code_wrong", @"验证码错误")
                                                                 message:nil delegate:self
                                                       cancelButtonTitle:NSLocalizedString(@"cancel", @"取消")
                                                       otherButtonTitles:NSLocalizedString(@"retry", @"重试"), nil];
        retryAlertView.tag = 0xbb;
        [retryAlertView show];
    }
    else if (error.code == 120020)
    {
        [UIView dd_showMessage:NSLocalizedString(@"ad_already_added", @"您已添加过此设备")];
    }
    else if (error.code == 120022)
    {
        [UIView dd_showMessage:NSLocalizedString(@"ad_added_by_others", @"此设备已被别人添加")];
    }
    else
    {
        [UIView dd_showMessage:NSLocalizedString(@"wifi_add_fail", @"添加失败")];
    }
}

#pragma mark - Action Methods

- (void)completionButtonClicked:(id)sender
{
    NSArray *viewControllers = self.navigationController.viewControllers;
    for (UIViewController *vc in viewControllers)
    {
        if ([vc isKindOfClass:[EZDeviceTableViewController class]])
        {
            ((EZDeviceTableViewController *)vc).needRefresh = YES;
            [self.navigationController popToViewController:vc animated:YES];
            break;
        }
        
        if ([vc isKindOfClass:[EZDdnsDeviceTableViewController class]])
        {
            ((EZDdnsDeviceTableViewController *)vc).needRefresh = YES;
            [self.navigationController popToViewController:vc animated:YES];
            break;
        }
    }
}

- (void)retryButtonClicked:(id)sender
{
    [self wifiConfigStart];
}


#pragma mark - Get & Set Methods

- (UIImageView *)animationImageView
{
    if (!_animationImageView)
    {
        _animationImageView = [[UIImageView alloc] initWithFrame:CGRectZero];
    }
    return _animationImageView;
}

- (UIView *)addTipsView
{
    if (!_addTipsView)
    {
        _addTipsView = [[UIView alloc] initWithFrame:CGRectZero];
        _addTipsView.backgroundColor = [UIColor clearColor];
    }
    return _addTipsView;
}

- (UIImageView *)timerImageView
{
    if (!_timerImageView)
    {
        
        UIImage *image = [UIImage dd_createImageWithCGSize:CGSizeMake(25.0, 25.0) color:[UIColor dd_hexStringToColor:@"0x1b9ee2"]];
        
        _timerImageView = [[UIImageView alloc] initWithImage:image];
        _timerImageView.clipsToBounds = YES;
        _timerImageView.layer.cornerRadius = 25/2.0;
    }
    return _timerImageView;
}

- (UIImageView *)successRegisterImageView{
    if (!_successRegisterImageView)
    {
        _successRegisterImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"add_success_icon"]];
    }
    return _successRegisterImageView;
}

- (UIImageView *)successWifiImageView
{
    if (!_successWifiImageView)
    {
        _successWifiImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"add_success_icon"]];
    }
    return _successWifiImageView;
}

- (UILabel *)wifiLabel
{
    if (!_wifiLabel)
    {
        _wifiLabel = [[UILabel alloc] initWithFrame:CGRectZero];
        _wifiLabel.font = [UIFont systemFontOfSize:15.0f];
        _wifiLabel.textColor = [UIColor dd_hexStringToColor:@"0x333333"];
        _wifiLabel.backgroundColor = [UIColor clearColor];
        _wifiLabel.textAlignment = NSTextAlignmentCenter;
        _wifiLabel.text = NSLocalizedString(@"wifi_connecting_net", @"正在连接Wi-Fi网络");
    }
    return _wifiLabel;
}

- (UILabel *)registerLabel
{
    if (!_registerLabel)
    {
        _registerLabel = [[UILabel alloc] initWithFrame:CGRectZero];
        _registerLabel.font = [UIFont systemFontOfSize:15.0f];
        _registerLabel.textColor = [UIColor dd_hexStringToColor:@"0x333333"];
        _registerLabel.backgroundColor = [UIColor clearColor];
        _registerLabel.textAlignment = NSTextAlignmentCenter;
        _registerLabel.text = NSLocalizedString(@"wifi_register_server", @"注册平台服务器");
    }
    return _registerLabel;
}

- (UILabel *)bindLabel
{
    if (!_bindLabel)
    {
        _bindLabel = [[UILabel alloc] initWithFrame:CGRectZero];
        _bindLabel.font = [UIFont systemFontOfSize:15.0f];
        _bindLabel.textColor = [UIColor dd_hexStringToColor:@"0x333333"];
        _bindLabel.backgroundColor = [UIColor clearColor];
        _bindLabel.textAlignment = NSTextAlignmentCenter;
        _bindLabel.text = NSLocalizedString(@"wifi_bind_account", @"绑定你的账号");
    }
    return _bindLabel;
}

- (UIView *)failedTipsView
{
    if(!_failedTipsView)
    {
        _failedTipsView = [[UIView alloc] initWithFrame:CGRectZero];
    }
    return _failedTipsView;
}

- (UILabel *)completionLabel
{
    if (!_completionLabel)
    {
        _completionLabel = [[UILabel alloc] initWithFrame:CGRectZero];
        _completionLabel.text = NSLocalizedString(@"wifi_add_success", @"添加成功");
        _completionLabel.textAlignment = NSTextAlignmentCenter;
        _completionLabel.font = [UIFont systemFontOfSize:15.0f];
        _completionLabel.backgroundColor = [UIColor clearColor];
    }
    return _completionLabel;
}

- (UIButton *)completionButton
{
    if (!_completionButton)
    {
        _completionButton = [UIButton buttonWithType:UIButtonTypeCustom];
        [_completionButton setBackgroundImage:[UIImage imageNamed:@"blue_button"] forState:UIControlStateNormal];
        [_completionButton setBackgroundImage:[UIImage imageNamed:@"blue_button_sel"] forState:UIControlStateHighlighted];
        [_completionButton setTitle:NSLocalizedString(@"complete", @"完成") forState:UIControlStateNormal];
        [_completionButton addTarget:self action:@selector(completionButtonClicked:) forControlEvents:UIControlEventTouchUpInside];
    }
    return _completionButton;
}

@end
