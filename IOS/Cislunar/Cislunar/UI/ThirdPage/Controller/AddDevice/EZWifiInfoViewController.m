//
//  EZWifiInfoViewController.m
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/10/29.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import "EZWifiInfoViewController.h"
#import <SystemConfiguration/CaptiveNetwork.h>
#import "DDKit.h"
#import "EZWifiConfigViewController.h"
#import "EZAPWiFiConfigViewController.h"

//#define WIFI_PREFROOT_URL @"prefs:root=WIFI"
//#define WIFI_IOS10_WIFI_URL @"App-Prefs:root=WIFI"
#define WIFI_PREFROOT_URL @"[[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString]]=WIFI"
#define WIFI_IOS10_WIFI_URL @"[[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString]]=WIFI"

@interface EZWifiInfoViewController ()

@property (nonatomic, weak) IBOutlet UIButton *nextButton;
@property (nonatomic, weak) IBOutlet UILabel *tipsLabel;
@property (nonatomic, weak) IBOutlet UITextField *nameTextField;
@property (nonatomic, weak) IBOutlet UITextField *passwordTextField;
@property (nonatomic, weak) IBOutlet UILabel *nameLabel;
@property (nonatomic, weak) IBOutlet UILabel *passwordLabel;

@end

@implementation EZWifiInfoViewController

- (void)dealloc
{
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = NSLocalizedString(@"wifi_connect_wifi_title", @"第二步，连接WiFi");
    
    self.nameTextField.leftView = self.nameLabel;
    self.nameTextField.leftViewMode = UITextFieldViewModeAlways;
    self.nameTextField.enabled = NO;
    
    self.passwordTextField.leftView = self.passwordLabel;
    self.passwordTextField.leftViewMode = UITextFieldViewModeAlways;

    [self.nameTextField dd_addCornerRadius:4.0f lineColor:[UIColor lightGrayColor]];
    [self.passwordTextField dd_addCornerRadius:4.0f lineColor:[UIColor lightGrayColor]];
    [self.nextButton dd_addCornerRadius:19.0 lineColor:[UIColor dd_hexStringToColor:@"0x1b9ee2"]];
}

- (void) viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self addNotifications];
}

- (void) viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [self removeNotifications];
}

- (void) viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    [self getWiFiName];
    
    [self checkWiFi];
}

- (void)addNotifications
{
    [self removeNotifications];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(applicationDidBecomeActive)
                                                 name:UIApplicationDidBecomeActiveNotification
                                               object:nil];
}

- (void)removeNotifications
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void) getWiFiName
{
    NSArray *interfaces = CFBridgingRelease(CNCopySupportedInterfaces());
    for (NSString *ifnam in interfaces)
    {
        NSDictionary *info = CFBridgingRelease(CNCopyCurrentNetworkInfo((__bridge CFStringRef)ifnam));
        self.nameTextField.text = info[@"SSID"];
        break;
    }
}

- (void) checkWiFi
{
    if (!self.nameTextField.text || self.nameTextField.text.length <= 0)
    {
        self.nextButton.enabled = NO;
        [self showJumpSettingTip];
    }
    else
    {
        self.nextButton.enabled = YES;
    }
}

- (void) showJumpSettingTip
{
    UIAlertController *alertController =
        [UIAlertController alertControllerWithTitle:NSLocalizedString(@"alert_title",@"提示")
                                            message:NSLocalizedString(@"wifi_info_no_wifi_msg", @"未连接Wi-Fi,请设置合适的Wi-Fi")
                                     preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"cancel",@"取消")
                                                           style:UIAlertActionStyleCancel
                                                         handler:nil];
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"wifi_info_setting",@"设置")
                                                       style:UIAlertActionStyleDefault
                                                     handler:^(UIAlertAction * _Nonnull action) {
                                                         NSString *urlStr = [[[UIDevice currentDevice] systemVersion] floatValue] >= 10.0?WIFI_IOS10_WIFI_URL:WIFI_PREFROOT_URL;
        
                                                         NSURL * url = [NSURL URLWithString:urlStr];
                                                         if([[UIApplication sharedApplication] canOpenURL:url])
                                                         {
                                                             [[UIApplication sharedApplication] openURL:url];
                                                         }
                                                     }];
    [alertController addAction:cancelAction];
    [alertController addAction:okAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

- (void) showModeSelect
{
    UIAlertController *alertController =
    [UIAlertController alertControllerWithTitle:nil
                                        message:NSLocalizedString(@"wifi_mode_select_msg", @"请选择配网模式")
                                 preferredStyle:UIAlertControllerStyleActionSheet];
    
    UIAlertAction *normalAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"wifi_mode_normal",@"一般配网")
                                                           style:UIAlertActionStyleDefault
                                                         handler:^(UIAlertAction * _Nonnull action) {
                                                             [self performSegueWithIdentifier:@"go2WifiConfig" sender:nil];
                                                         }];
    
    UIAlertAction *soundAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"wifi_mode_sound",@"声波配网")
                                                           style:UIAlertActionStyleDefault
                                                         handler:^(UIAlertAction * _Nonnull action) {
                                                             [self performSegueWithIdentifier:@"go2WifiConfig" sender:nil];
                                                         }];
    
    UIAlertAction *apAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"wifi_mode_ap",@"热点配网")
                                                       style:UIAlertActionStyleDefault
                                                     handler:^(UIAlertAction * _Nonnull action) {
                                                         [self performSegueWithIdentifier:@"go2APWifiConfig" sender:nil];
                                                     }];
    
    UIAlertAction *lineAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"wifi_mode_line",@"有线连接")
                                                         style:UIAlertActionStyleDefault
                                                       handler:^(UIAlertAction * _Nonnull action) {
                                                           [self.navigationController dismissViewControllerAnimated:YES completion:nil];
                                                     }];
    
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"cancel",@"取消")
                                                           style:UIAlertActionStyleCancel
                                                         handler:^(UIAlertAction * _Nonnull action) {
                                                         }];
    
    if (self.supportSmartMode)
    {
        [alertController addAction:normalAction];
    }
    
    if (self.supportSoundMode)
    {
        [alertController addAction:soundAction];
    }
    
    if (self.supportApMode)
    {
        [alertController addAction:apAction];
    }
    
    [alertController addAction:lineAction];
    
    [alertController addAction:cancelAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

#pragma mark - notification

- (void) applicationDidBecomeActive
{
    [self getWiFiName];
    
    [self checkWiFi];
}


#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    if ([[segue destinationViewController] isKindOfClass:[EZWifiConfigViewController class]]) {
        EZWifiConfigViewController *vc = (EZWifiConfigViewController *)[segue destinationViewController];
        vc.ssid = self.nameTextField.text;
        vc.password = self.passwordTextField.text;
        vc.supportSmartMode = self.supportSmartMode;
        vc.supportSoundMode = self.supportSoundMode;
    }
    
    if ([[segue destinationViewController] isKindOfClass:[EZAPWiFiConfigViewController class]]) {
        EZAPWiFiConfigViewController *vc = (EZAPWiFiConfigViewController *)[segue destinationViewController];
        vc.ssid = self.nameTextField.text;
        vc.password = self.passwordTextField.text;
    } 
}

- (IBAction)nextAction:(id)sender
{
    [self showModeSelect];
}

@end
