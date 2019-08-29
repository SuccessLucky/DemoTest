//
//  EZDeviceUpgradeViewController.m
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/12/23.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import "EZDeviceUpgradeViewController.h"

//#import "EZDeviceUpgradeStatus.h"
//#import "EZStorageInfo.h"

@interface EZDeviceUpgradeViewController ()

@property (nonatomic, weak) IBOutlet UITextView *upgradeTextView;
@property (nonatomic, weak) IBOutlet UIButton *upgradeButton;
@property (nonatomic, weak) IBOutlet UIProgressView *upgradeProgressView;
@property (nonatomic, weak) IBOutlet UILabel *upgradeLabel;
@property (nonatomic, strong) NSTimer *timer;

@end

@implementation EZDeviceUpgradeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = NSLocalizedString(@"device_upgrade_title", @"设备升级");
    self.upgradeTextView.text = self.version.upgradeDesc;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillDisappear:(BOOL)animated {
    if (_timer)
    {
        [_timer invalidate];
        _timer = nil;
    }
    [super viewWillDisappear:animated];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)upgradeDevice:(id)sender
{
    [EZOPENSDK upgradeDevice:self.deviceSerial completion:^(NSError *error) {
        if (!error)
        {
            //5s获取一次升级状态
            _timer = [NSTimer scheduledTimerWithTimeInterval:5.0 target:self selector:@selector(checkUpgradeStatus:) userInfo:nil repeats:YES];
            self.upgradeButton.hidden = YES;
            self.upgradeLabel.hidden = NO;
            self.upgradeProgressView.hidden = NO;
        }
    }];
}

- (void)checkUpgradeStatus:(NSTimer *)checkTimer
{
    [EZOPENSDK getDeviceUpgradeStatus:self.deviceSerial
                           completion:^(EZDeviceUpgradeStatus *status, NSError *error) {
                               NSLog(@"status = %@",status);
                               if(status.upgradeStatus == 2)
                               {
                                   self.upgradeLabel.text = NSLocalizedString(@"device_upgrade_success", @"升级成功");
                                   [self.upgradeProgressView setProgress:1.0 animated:YES];
                               }
                               else {
                                   self.upgradeLabel.text = NSLocalizedString(@"device_upgrading",@"正在升级中");
                                   [self.upgradeProgressView setProgress:status.upgradeProgress/100.0 animated:YES];
                               }
                           }];
}


@end
