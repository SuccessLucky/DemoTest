
//
//  YSWifiTipsViewController.m
//  VideoGo
//
//  Created by DeJohn Dong on 15/7/30.
//  Copyright (c) 2015年 hikvision. All rights reserved.
//

#import "EZWifiTipsViewController.h"
#import "Masonry.h"
#import "DDKit.h"
#import "EZWifiInfoViewController.h"

@interface EZWifiTipsViewController ()
{
    BOOL _isRestart;
}

@property (nonatomic, strong) UIImageView *deviceImageView;
@property (nonatomic, strong) UIButton *nextButton;
@property (nonatomic, strong) UIButton *exceptionButton;
@property (nonatomic, strong) UILabel *tipsLabel;

@end

@implementation EZWifiTipsViewController

- (void)dealloc
{
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.title = NSLocalizedString(@"wifi_prepare_title", @"第一步，准备好设备");
    
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
    
    [self.view addSubview:self.exceptionButton];
    [self.exceptionButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.nextButton.mas_bottom).offset(15);
        make.height.mas_equalTo(@38);
        make.width.mas_equalTo(@285);
        make.centerX.mas_equalTo(self.view.mas_centerX);
    }];
    
    [self checkDevice];
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

#pragma mark - Custom Methods

- (void)checkDevice
{
    self.tipsLabel.text = NSLocalizedString(@"wifi_device_power_tip", @"请将设备插上电后等待大约30秒，直到设备启动完成");
    [self.nextButton setTitle:NSLocalizedString(@"wifi_device_start_ready", @"设备已启动好，且是第一次配置网络") forState:UIControlStateNormal];
    [self.exceptionButton setTitle:NSLocalizedString(@"wifi_config_wifi_ago", @"这台设备以前配过网络") forState:UIControlStateNormal];
    
    NSMutableAttributedString *attributeStr = [[NSMutableAttributedString alloc] initWithString:self.tipsLabel.text];
    [attributeStr addAttributes:@{NSForegroundColorAttributeName:[UIColor dd_hexStringToColor:@"0xff4000"],NSFontAttributeName:[UIFont boldSystemFontOfSize:16.0f]} range:[self.tipsLabel.text rangeOfString:@"30"]];
    self.tipsLabel.attributedText = attributeStr;
}

- (void)nextButtonClicked:(id)sender
{
    [self performSegueWithIdentifier:@"go2WifiInfo" sender:nil];
}

- (void)exceptionButtonClicked:(id)sender
{
    [self performSegueWithIdentifier:@"go2DeviceRestart" sender:nil];
}

#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([[segue destinationViewController] isKindOfClass:[EZWifiInfoViewController class]]) {
        EZWifiInfoViewController *vc = (EZWifiInfoViewController *)[segue destinationViewController];
        vc.supportApMode = self.supportApMode;
        vc.supportSmartMode = self.supportSmartMode;
        vc.supportSoundMode = self.supportSoundMode;
    }
}

#pragma mark - Super Methods

- (void)popBack:(id)sender {
    if(_isRestart){
        _isRestart = NO;
        
        self.exceptionButton.hidden = NO;
        
        self.title = NSLocalizedString(@"wifi_prepare_title", @"第一步，准备好设备");
        
        [self checkDevice];

        return;
    }
    [self.navigationController popViewControllerAnimated:YES];
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
        [_nextButton addTarget:self action:@selector(nextButtonClicked:) forControlEvents:UIControlEventTouchUpInside];
        [_nextButton setBackgroundImage:[UIImage imageNamed:@"blue_button"] forState:UIControlStateNormal];
        [_nextButton setBackgroundImage:[UIImage imageNamed:@"blue_button_sel"] forState:UIControlStateHighlighted];
        _nextButton.titleLabel.font = [UIFont systemFontOfSize:15.0f];
    }
    return _nextButton;
}

- (UIButton *)exceptionButton
{
    if(!_exceptionButton)
    {
        _exceptionButton = [UIButton buttonWithType:UIButtonTypeCustom];
        [_exceptionButton addTarget:self action:@selector(exceptionButtonClicked:) forControlEvents:UIControlEventTouchUpInside];
        [_exceptionButton setBackgroundImage:[UIImage imageNamed:@"white_button"] forState:UIControlStateNormal];
        [_exceptionButton setBackgroundImage:[UIImage imageNamed:@"white_button_sel"] forState:UIControlStateHighlighted];
        [_exceptionButton setTitleColor:[UIColor dd_hexStringToColor:@"0x1b9ee2"] forState:UIControlStateNormal];
        _exceptionButton.titleLabel.font = [UIFont systemFontOfSize:15.0f];
    }
    return _exceptionButton;
}

@end
