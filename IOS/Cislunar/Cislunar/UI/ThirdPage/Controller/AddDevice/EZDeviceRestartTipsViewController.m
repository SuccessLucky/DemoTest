//
//  EZDeviceRestartTipsViewController.m
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/10/29.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import "EZDeviceRestartTipsViewController.h"
#import "Masonry.h"
#import "DDKit.h"

@interface EZDeviceRestartTipsViewController ()

@property (nonatomic, strong) UIImageView *deviceImageView;
@property (nonatomic, strong) UIButton *nextButton;
@property (nonatomic, strong) UILabel *tipsLabel;

@end

@implementation EZDeviceRestartTipsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.title = NSLocalizedString(@"ad_restart_title", @"重启设备");
    
    [self.view addSubview:self.tipsLabel];
    [self.tipsLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.leading.mas_equalTo(@10);
        make.trailing.mas_equalTo(@-10);
        if([UIScreen mainScreen].bounds.size.height == 480.0){
            make.top.mas_equalTo(@10);
        }else{
            make.top.mas_equalTo(@99);
        }
        make.height.mas_equalTo(@50);
    }];
    
    [self.view addSubview:self.deviceImageView];
    [self.deviceImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.tipsLabel.mas_bottom).offset(19);
        make.width.mas_equalTo(@174);
        make.height.mas_equalTo(@199);
        make.centerX.mas_equalTo(self.view.mas_centerX);
    }];
    
    [self.view addSubview:self.nextButton];
    [self.nextButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.deviceImageView.mas_bottom).offset(31);
        make.height.mas_equalTo(@38);
        make.width.mas_equalTo(@285);
        make.centerX.mas_equalTo(self.view.mas_centerX);
    }];
    
    self.deviceImageView.image = [UIImage imageNamed:@"device_reset"];
    self.tipsLabel.text = NSLocalizedString(@"ad_restart_tip", @"长按设备上的reset键10秒后松开，并等待大约30秒直到设备启动完成");
    [self.nextButton setTitle:NSLocalizedString(@"ad_restart_done", @"我已重启好") forState:UIControlStateNormal];
    
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

- (void)nextAction:(id)sender
{
    [self performSegueWithIdentifier:@"go2WifiInfo2" sender:nil];
}

#pragma mark - Set & Get Methods

- (UILabel *)tipsLabel
{
    if(!_tipsLabel)
    {
        _tipsLabel = [[UILabel alloc] initWithFrame:CGRectZero];
        _tipsLabel.backgroundColor = [UIColor clearColor];
        _tipsLabel.font = [UIFont systemFontOfSize:16.0f];
        _tipsLabel.textColor = [UIColor dd_hexStringToColor:@"0x333333"];
        _tipsLabel.numberOfLines = 2;
        _tipsLabel.textAlignment = NSTextAlignmentCenter;
    }
    return _tipsLabel;
}

- (UIImageView *)deviceImageView
{
    if(!_deviceImageView)
    {
        _deviceImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"device"]];
    }
    return _deviceImageView;
}

- (UIButton *)nextButton
{
    if(!_nextButton)
    {
        _nextButton = [UIButton buttonWithType:UIButtonTypeCustom];
        [_nextButton addTarget:self action:@selector(nextAction:) forControlEvents:UIControlEventTouchUpInside];
        [_nextButton setBackgroundImage:[UIImage imageNamed:@"blue_button"] forState:UIControlStateNormal];
        [_nextButton setBackgroundImage:[UIImage imageNamed:@"blue_button_sel"] forState:UIControlStateHighlighted];
        _nextButton.titleLabel.font = [UIFont systemFontOfSize:15.0f];
    }
    return _nextButton;
}

@end
