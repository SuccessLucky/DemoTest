//
//  EZAPWiFiConfigViewController.m
//  EZOpenSDKDemo
//
//  Created by linyong on 2018/6/4.
//  Copyright © 2018年 hikvision. All rights reserved.
//

#import "EZAPWiFiConfigViewController.h"
#import "GlobalKit.h"
#import <SystemConfiguration/CaptiveNetwork.h>
//#import "EZHCNetDeviceSDK.h"
#import "EZAPConfigResultViewController.h"
//#import "Toast+UIView.h"

//#define WIFI_PREFROOT_URL @"prefs:root=WIFI"
//#define WIFI_IOS10_WIFI_URL @"App-Prefs:root=WIFI"
#define WIFI_PREFROOT_URL @"[[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString]]=WIFI"
#define WIFI_IOS10_WIFI_URL @"[[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString]]=WIFI"

@interface EZAPWiFiConfigViewController ()

@property (weak, nonatomic) IBOutlet UILabel *wifiNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *wifiPwdLabel;
@property (weak, nonatomic) IBOutlet UILabel *stepTwoLabel;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *processingIndicator;
@property (weak, nonatomic) IBOutlet UIButton *addBtn;

@property (nonatomic,copy) NSString *devicWifiName;
@property (nonatomic,strong) NSTimer *timer;

@end

@implementation EZAPWiFiConfigViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.title = NSLocalizedString(@"wifi_mode_wifi_connect",@"连接到设备Wi-Fi");

    [self initSubviews];
}

- (void) viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    [self startTimer];
    [self addNotifications];
}

- (void) viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
    [self stopTimer];
    [self stopConfigWifi];
    [self removeNotifications];
}

#pragma mark - views

- (void) initSubviews
{
    self.processingIndicator.hidden = YES;
    self.devicWifiName = [NSString stringWithFormat:@"EZVIZ_%@",[GlobalKit shareKit].deviceSerialNo];
    self.wifiNameLabel.text = self.devicWifiName;
    self.wifiPwdLabel.text = [NSString stringWithFormat:@"EZVIZ_%@",[GlobalKit shareKit].deviceVerifyCode];
    NSString *str = [NSString stringWithFormat:NSLocalizedString(@"wifi_step_two_msg",@"进入手机系统Wi-Fi设置界面，选择名称为%@的网络，用提示的密码进行连接"),self.devicWifiName];
    NSMutableAttributedString *aStr = [[NSMutableAttributedString alloc] initWithString:str];
    
    //关键字符颜色调整
    [aStr addAttribute:NSForegroundColorAttributeName
                 value:[UIColor orangeColor]
                 range:[str rangeOfString:self.devicWifiName]];
    
    //行间距调整
    NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
    [paragraphStyle setLineSpacing:5];
    [aStr addAttribute:NSParagraphStyleAttributeName value:paragraphStyle range:NSMakeRange(0, [aStr length])];
    
    self.stepTwoLabel.attributedText = aStr;
}

-(void) startAction
{
    self.processingIndicator.hidden = NO;
    self.addBtn.hidden = YES;
    [self.processingIndicator startAnimating];
}

- (void) stopAction
{
    [self.processingIndicator stopAnimating];
    self.processingIndicator.hidden = YES;
}


#pragma mark - notification

- (void) applicationDidBecomeActive
{
    [self startTimer];
}

- (void) applicationWillResignActive
{
    [self stopTimer];
}

#pragma mark - actions

- (IBAction)enterSettingBtnClick:(id)sender
{
    NSString *urlStr = [[[UIDevice currentDevice] systemVersion] floatValue] >= 10.0?WIFI_IOS10_WIFI_URL:WIFI_PREFROOT_URL;
    NSURL * url = [NSURL URLWithString:urlStr];
    if([[UIApplication sharedApplication] canOpenURL:url])
    {
        [[UIApplication sharedApplication] openURL:url];
    }
}

- (IBAction)copyPwdBtnClick:(id)sender
{
    UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
    pasteboard.string = self.wifiPwdLabel.text;
    
    [self.view makeToast:@"done" duration:1.5 position:@"center"];
}

- (IBAction)addBtnClick:(id)sender
{
    [self performSegueWithIdentifier:@"go2WifiConfigResult" sender:nil];
}

#pragma mark - support

- (void) configWifi
{
    
    [EZOPENSDK startConfigWifi:self.ssid password:self.password deviceSerial:[GlobalKit shareKit].deviceSerialNo mode:1 deviceStatus:^(EZWifiConfigStatus status, NSString *deviceSerial) {
        if (status ==DEVICE_WIFI_CONNECTED )
        {
            [self configSuccess];
        }
        else
        {
            [self configFailed];
            NSLog(@"config failed");
        }
    }];
#warning 11111
//    [EZOPENSDK startConfigWifi:self.ssid password:self.password deviceSerial:[GlobalKit shareKit].deviceSerialNo deviceStatus:^(EZWifiConfigStatus status) {
//
//    }];
    
    /*
    [EZOPENSDK startAPConfigWifiWithSsid:self.ssid
                                password:self.password
                            deviceSerial:[GlobalKit shareKit].deviceSerialNo
                              verifyCode:[GlobalKit shareKit].deviceVerifyCode
                                  result:^(BOOL ret) {
                                      if (ret)
                                      {
                                          [self configSuccess];
                                      }
                                      else
                                      {
                                          [self configFailed];
                                          NSLog(@"config failed");
                                      }
                                  }];
     */
}

- (void) stopConfigWifi
{
#warning 11
    [EZOPENSDK stopConfigWifi];
}

- (void) configSuccess
{
    [self stopAction];
    
    self.addBtn.hidden = NO;
}

- (void) configFailed
{
    [self stopAction];
    
    [self.view makeToast:@"config wifi fail" duration:1.5 position:@"center"];
}

- (void)addNotifications
{
    [self removeNotifications];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(applicationDidBecomeActive)
                                                 name:UIApplicationDidBecomeActiveNotification
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(applicationWillResignActive)
                                                 name:UIApplicationWillResignActiveNotification
                                               object:nil];
}

- (void)removeNotifications
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void) startTimer
{
    [self stopTimer];
    
    self.timer = [NSTimer scheduledTimerWithTimeInterval:5.0 target:self selector:@selector(timerCallback) userInfo:nil repeats:YES];
}

- (void) stopTimer
{
    if (!self.timer)
    {
        return;
    }
    
    if ([self.timer isValid])
    {
        [self.timer invalidate];
    }
    self.timer = nil;
}

- (void) timerCallback
{
    if (![self checkSsid])
    {
        return;
    }
    
    [self stopTimer];
    
    [self startAction];
    
    [self configWifi];
}

- (NSString *) currentSsid
{
    NSString *currentSsid = @"";
    CFArrayRef myArray = CNCopySupportedInterfaces();
    if (myArray != nil) {
        CFDictionaryRef myDict = CNCopyCurrentNetworkInfo(CFArrayGetValueAtIndex(myArray, 0));
        if (myDict != nil) {
            NSDictionary *dict = (__bridge NSDictionary *)(myDict);
            currentSsid = [dict valueForKey:@"SSID"];
        }
    }
    return currentSsid;
}

- (BOOL) checkSsid
{
    NSString *ssid = [self currentSsid];
    
    if (ssid && [ssid isEqualToString:self.devicWifiName])
    {
        return YES;
    }
    
    return NO;
}

#pragma mark - Navigation

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {

}

@end
