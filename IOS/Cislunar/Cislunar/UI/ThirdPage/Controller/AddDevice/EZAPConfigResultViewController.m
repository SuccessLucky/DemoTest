//
//  EZAPConfigResultViewController.m
//  EZOpenSDKDemo
//
//  Created by linyong on 2018/6/5.
//  Copyright © 2018年 hikvision. All rights reserved.
//

#import "EZAPConfigResultViewController.h"
#import "GlobalKit.h"
//#import "EZOpenSDK.h"
//#import "EZProbeDeviceInfo.h"
//#import "Toast+UIView.h"
#import "EZDeviceTableViewController.h"


#define MAX_COUNT (20)

@interface EZAPConfigResultViewController ()
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *loadingIndicator;
@property (weak, nonatomic) IBOutlet UIImageView *successImageView;
@property (weak, nonatomic) IBOutlet UIButton *doneBtn;
@property (weak, nonatomic) IBOutlet UIButton *retryBtn;
@property (weak, nonatomic) IBOutlet UILabel *msgLabel;
@property (nonatomic,strong) NSTimer *timer;
@property (nonatomic,assign) NSInteger addCount;
@end

@implementation EZAPConfigResultViewController

- (void)viewDidLoad
{
    [super viewDidLoad];

    self.title = NSLocalizedString(@"wifi_ap_add_device_title", @"添加设备");
    
    [self initSubviews];
    
    [self startTimer];
}

- (void) viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
    [self stopTimer];
}


#pragma mark - views

- (void) initSubviews
{
    [self.loadingIndicator startAnimating];
}

#pragma mark - actions

- (IBAction)doneBtnClick:(id)sender
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
    }
}

- (IBAction)retryBtnClick:(id)sender
{
    self.msgLabel.hidden = NO;
    self.loadingIndicator.hidden = NO;
    [self.loadingIndicator startAnimating];
    self.retryBtn.hidden = YES;
    
    [self startTimer];
}

#pragma mark - callback

- (void) timerCallback
{
    self.addCount ++;
    
    if (self.addCount > MAX_COUNT)
    {
        [self timeoutProcess];
        return;
    }
    
    [self addProcess];
}

#pragma mark - supoort

- (void) startTimer
{
    [self stopTimer];
    
    self.addCount = 0;

    self.timer = [NSTimer scheduledTimerWithTimeInterval:5.0
                                                  target:self
                                                selector:@selector(timerCallback)
                                                userInfo:nil
                                                 repeats:YES];
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

- (void) addProcess
{
    
    [EZOPENSDK probeDeviceInfo:[GlobalKit shareKit].deviceSerialNo completion:^(EZProbeDeviceInfo *deviceInfo, NSError *error) {
        if (error)
        {
            [self probeErrorProcessWithError:error];
        }
        else
        {
            [EZOpenSDK addDevice:[GlobalKit shareKit].deviceSerialNo
                      verifyCode:[GlobalKit shareKit].deviceVerifyCode
                      completion:^(NSError *error) {
                          if (error)
                          {
                              [self addErrorProcessWithError:error];
                          }
                          else
                          {
                              [self successProcess];
                          }
                      }];
        }

    }];
    
#warning 1
    /*
    [EZOPENSDK probeDeviceInfo:[GlobalKit shareKit].deviceSerialNo
                    deviceType:[GlobalKit shareKit].deviceModel
                    completion:^(EZProbeDeviceInfo *deviceInfo, NSError *error) {
                        if (error)
                        {
                            [self probeErrorProcessWithError:error];
                        }
                        else
                        {
                            [EZOpenSDK addDevice:[GlobalKit shareKit].deviceSerialNo
                                      verifyCode:[GlobalKit shareKit].deviceVerifyCode
                                      completion:^(NSError *error) {
                                          if (error)
                                          {
                                              [self addErrorProcessWithError:error];
                                          }
                                          else
                                          {
                                              [self successProcess];
                                          }
                                      }];
                        }
                    }];
     */
}

- (void) probeErrorProcessWithError:(NSError *) error
{
    NSString *msg = nil;
    if (error.code == EZ_HTTPS_DEVICE_ADDED_MYSELF)
    {
        msg = NSLocalizedString(@"ad_already_added",@"您已添加过此设备");
    }
    else if (error.code == EZ_HTTPS_DEVICE_ONLINE_IS_ADDED ||
             error.code == EZ_HTTPS_DEVICE_OFFLINE_IS_ADDED)
    {
        msg = NSLocalizedString(@"ad_added_by_others",@"此设备已被别人添加");
    }
    else
    {
        //continue
        return;
    }
    
    self.msgLabel.hidden = YES;
    [self stopTimer];
    [self.view makeToast:msg duration:2.0 position:@"center"];
}

- (void) addErrorProcessWithError:(NSError *) error
{
    NSString *msg = nil;
    if (error.code == 120010)
    {
        msg = NSLocalizedString(@"device_verify_code_wrong", @"验证码错误");
    }
    else if (error.code == 120020)
    {
        msg = NSLocalizedString(@"ad_already_added", @"您已添加过此设备");
    }
    else if (error.code == 120022)
    {
        msg = NSLocalizedString(@"ad_added_by_others", @"此设备已被别人添加");
    }
    else
    {
        msg = NSLocalizedString(@"wifi_add_fail", @"添加失败");
    }
    
    [self.view makeToast:msg duration:2.0 position:@"center"];
    self.msgLabel.hidden = YES;
}

- (void) timeoutProcess
{
    [self stopTimer];
    
    [self.view makeToast:@"timeout,add device fail." duration:2.0 position:@"center"];
    self.retryBtn.hidden = NO;
    self.msgLabel.hidden = YES;
}

- (void) successProcess
{
    [self stopTimer];
    self.loadingIndicator.hidden = YES;
    [self.loadingIndicator stopAnimating];
    
    self.successImageView.hidden = NO;
    self.doneBtn.hidden = NO;
    self.msgLabel.hidden = YES;
}


@end
