//
//  EZLivePlayViewController.m
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/10/28.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import <sys/sysctl.h>
#import <mach/mach.h>
#import <Photos/Photos.h>
#import "EZLivePlayViewController.h"
#import "UIViewController+EZBackPop.h"
//#import "EZDeviceInfo.h"
//#import "EZPlayer.h"
#import "DDKit.h"
#import "Masonry.h"
#import "HIKLoadView.h"
#import "MBProgressHUD.h"
//#import "EZCameraInfo.h"
#import <AVFoundation/AVFoundation.h>


@interface EZLivePlayViewController ()<EZPlayerDelegate, UIAlertViewDelegate>
{
    NSOperation *op;
    BOOL _isPressed;
}

@property (nonatomic) BOOL isOpenSound;
@property (nonatomic) BOOL isPlaying;
@property (nonatomic, strong) NSTimer *recordTimer;
@property (nonatomic) NSTimeInterval seconds;
@property (nonatomic, strong) CALayer *orangeLayer;
@property (nonatomic, copy) NSString *filePath;
@property (nonatomic, strong) EZPlayer *player;
@property (nonatomic, strong) EZPlayer *talkPlayer;
@property (nonatomic) BOOL isStartingTalk;
@property (nonatomic, strong) HIKLoadView *loadingView;
@property (nonatomic, weak) IBOutlet UIButton *playerPlayButton;
@property (nonatomic, weak) IBOutlet UIView *playerView;
@property (nonatomic, weak) IBOutlet UIView *toolBar;
@property (nonatomic, weak) IBOutlet UIView *bottomView;
@property (nonatomic, weak) IBOutlet UIButton *controlButton;
@property (nonatomic, weak) IBOutlet UIButton *talkButton;
@property (nonatomic, weak) IBOutlet UIButton *captureButton;
@property (nonatomic, weak) IBOutlet UIButton *localRecordButton;
@property (nonatomic, weak) IBOutlet UIButton *playButton;
@property (nonatomic, weak) IBOutlet UIButton *voiceButton;
@property (nonatomic, weak) IBOutlet UIButton *qualityButton;
@property (nonatomic, weak) IBOutlet UIButton *emptyButton;
@property (nonatomic, weak) IBOutlet UIButton *largeButton;
@property (nonatomic, weak) IBOutlet UIButton *largeBackButton;
@property (nonatomic, weak) IBOutlet UIView *ptzView;
@property (nonatomic, weak) IBOutlet UIButton *ptzCloseButton;
@property (nonatomic, weak) IBOutlet UIButton *ptzControlButton;
@property (nonatomic, weak) IBOutlet UIView *qualityView;
@property (nonatomic, weak) IBOutlet UIButton *highButton;
@property (nonatomic, weak) IBOutlet UIButton *middleButton;
@property (nonatomic, weak) IBOutlet UIButton *lowButton;
@property (nonatomic, weak) IBOutlet UIButton *ptzUpButton;
@property (nonatomic, weak) IBOutlet UIButton *ptzLeftButton;
@property (nonatomic, weak) IBOutlet UIButton *ptzDownButton;
@property (nonatomic, weak) IBOutlet UIButton *ptzRightButton;
@property (nonatomic, weak) IBOutlet NSLayoutConstraint *ptzViewContraint;
@property (nonatomic, weak) IBOutlet UILabel *localRecordLabel;
@property (nonatomic, weak) IBOutlet UIView *talkView;
@property (nonatomic, weak) IBOutlet UIButton *talkCloseButton;
@property (nonatomic, weak) IBOutlet NSLayoutConstraint *talkViewContraint;
@property (nonatomic, weak) IBOutlet UIImageView *speakImageView;
@property (nonatomic, weak) IBOutlet UILabel *largeTitleLabel;
@property (nonatomic, weak) IBOutlet NSLayoutConstraint *localRecrodContraint;
@property (nonatomic, weak) IBOutlet UILabel *messageLabel;
@property (nonatomic, strong) NSMutableData *fileData;
@property (nonatomic, weak) MBProgressHUD *voiceHud;
@property (nonatomic, strong) EZCameraInfo *cameraInfo;



@end

@implementation EZLivePlayViewController

- (void)dealloc
{
    NSLog(@"%@ dealloc", self.class);
    [EZOPENSDK releasePlayer:_player];
    [EZOPENSDK releasePlayer:_talkPlayer];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = _deviceInfo.deviceName;
    self.largeTitleLabel.text = self.title;
    
    self.isAutorotate = YES;
    self.isStartingTalk = NO;
    self.ptzView.hidden = YES;
    self.talkView.hidden = YES;
    
    self.talkButton.enabled = self.deviceInfo.isSupportTalk;
    self.controlButton.enabled = self.deviceInfo.isSupportPTZ;
    self.captureButton.enabled = NO;
    self.localRecordButton.enabled = NO;
    
//    _url = @"rtsp://183.136.184.33:8554/demo://544542032:1:1:1:0:183.136.184.7:6500";
    
//    _url = @"ysproto://122.225.228.217:8554/live?dev=501694318&chn=1&stream=2&cln=1&isp=0&biz=3";
    
    if (_url)
    {
        _player = [EZOPENSDK createPlayerWithUrl:_url];
    }
    else
    {
        _cameraInfo = [self.deviceInfo.cameraInfo dd_objectAtIndex:_cameraIndex];
        _player = [EZOPENSDK createPlayerWithDeviceSerial:_cameraInfo.deviceSerial cameraNo:_cameraInfo.cameraNo];
//        _player.backgroundModeByPlayer = NO;
        _talkPlayer = [EZOPENSDK createPlayerWithDeviceSerial:_cameraInfo.deviceSerial cameraNo:_cameraInfo.cameraNo];
//        _player = [EZOPENSDK createPlayerWithDeviceSerial:info.deviceSerial cameraNo:info.cameraNo streamType:1];
        if (_cameraInfo.videoLevel == 2)
        {
            [self.qualityButton setTitle:NSLocalizedString(@"device_quality_high", @"高清") forState:UIControlStateNormal];
        }
        else if (_cameraInfo.videoLevel == 1)
        {
            [self.qualityButton setTitle:NSLocalizedString(@"device_quality_median", @"均衡") forState:UIControlStateNormal];
        }
        else
        {
            [self.qualityButton setTitle:NSLocalizedString(@"device_quality_low",@"流畅") forState:UIControlStateNormal];
        }
    }
    
#if DEBUG
    if (!_url)
    {
        //抓图接口演示代码
        [EZOPENSDK captureCamera:_cameraInfo.deviceSerial cameraNo:_cameraInfo.cameraNo completion:^(NSString *url, NSError *error) {
            NSLog(@"[%@] capture cameraNo is [%d] url is %@, error is %@", _cameraInfo.deviceSerial, (int)_cameraInfo.cameraNo, url, error);
        }];
    }
#endif
    
    _player.delegate = self;
    _talkPlayer.delegate = self;
    //判断设备是否加密，加密就从demo的内存中获取设备验证码填入到播放器的验证码接口里，本demo只处理内存存储，本地持久化存储用户自行完成
    if (self.deviceInfo.isEncrypt)
    {
        NSString *verifyCode = [[GlobalKit shareKit].deviceVerifyCodeBySerial objectForKey:self.deviceInfo.deviceSerial];
        [_player setPlayVerifyCode:verifyCode];
        [_talkPlayer setPlayVerifyCode:verifyCode];
    }
    [_player setPlayerView:_playerView];
    [_player startRealPlay];
    
    if(!_loadingView)
        _loadingView = [[HIKLoadView alloc] initWithHIKLoadViewStyle:HIKLoadViewStyleSqureClockWise];
    [self.view insertSubview:_loadingView aboveSubview:self.playerView];
    [_loadingView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.width.height.mas_equalTo(@14);
        make.centerX.mas_equalTo(self.playerView.mas_centerX);
        make.centerY.mas_equalTo(self.playerView.mas_centerY);
    }];
    [self.loadingView startSquareClcokwiseAnimation];
    
    self.largeBackButton.hidden = YES;
    _isOpenSound = YES;
    
    [self.controlButton dd_centerImageAndTitle];
    [self.talkButton dd_centerImageAndTitle];
    [self.captureButton dd_centerImageAndTitle];
    [self.localRecordButton dd_centerImageAndTitle];
    
    [self.voiceButton setImage:[UIImage imageNamed:@"preview_unvoice_btn_sel"] forState:UIControlStateHighlighted];
    [self addLine];
    
    self.localRecordLabel.layer.borderColor = [UIColor whiteColor].CGColor;
    self.localRecordLabel.layer.cornerRadius = 12.0f;
    self.localRecordLabel.layer.borderWidth = 1.0f;
    self.localRecordLabel.clipsToBounds = YES;
    self.playButton.enabled = NO;
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    self.ptzViewContraint.constant = self.bottomView.frame.size.height;
    self.talkViewContraint.constant = self.ptzViewContraint.constant;
}

- (void)viewWillDisappear:(BOOL)animated {
    [NSObject cancelPreviousPerformRequestsWithTarget:self selector:@selector(hideQualityView) object:nil];
    //结束本地录像
    if(self.localRecordButton.selected)
    {
        [_player stopLocalRecord];
        [self.fileData writeToFile:_filePath atomically:YES];
        self.localRecordLabel.hidden = YES;
        [_recordTimer invalidate];
        _recordTimer = nil;
        [self saveRecordToPhotosAlbum:_filePath];
    }
    

    NSLog(@"viewWillDisappear");
    [super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
    NSLog(@"viewDidDisappear");
    [super viewDidDisappear:animated];
    [_player stopRealPlay];
    if (_talkPlayer)
    {
        [_talkPlayer stopVoiceTalk];
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

- (UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskAllButUpsideDown;
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation
                                duration:(NSTimeInterval)duration
{
    self.navigationController.navigationBarHidden = NO;
    self.toolBar.hidden = NO;
    self.largeBackButton.hidden = YES;
    self.bottomView.hidden = NO;
    self.largeTitleLabel.hidden = YES;
    self.localRecrodContraint.constant = 10;
    if(toInterfaceOrientation == UIInterfaceOrientationLandscapeLeft ||
       toInterfaceOrientation == UIInterfaceOrientationLandscapeRight)
    {
        self.navigationController.navigationBarHidden = YES;
        self.localRecrodContraint.constant = 50;
        self.toolBar.hidden = YES;
        self.largeTitleLabel.hidden = NO;
        self.largeBackButton.hidden = NO;
        self.bottomView.hidden = YES;
    }
}

- (IBAction)pressed:(id)sender {
    
}

#pragma mark - UIAlertViewDelegate Methods

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (alertView.alertViewStyle == UIAlertViewStyleSecureTextInput)
    {
        if (buttonIndex == 1)
        {
            NSString *checkCode = [alertView textFieldAtIndex:0].text;
            [[GlobalKit shareKit].deviceVerifyCodeBySerial setValue:checkCode forKey:self.deviceInfo.deviceSerial];
            if (!self.isStartingTalk)
            {
                [self.player setPlayVerifyCode:checkCode];
                [self.player startRealPlay];
            }
            else
            {
                [self.talkPlayer setPlayVerifyCode:checkCode];
                [self.talkPlayer startVoiceTalk];
            }
        }
    }
    else
    {
        if (buttonIndex == 1)
        {
            [self showSetPassword];
            return;
        }
    }
}

#pragma mark - PlayerDelegate Methods

- (void)player:(EZPlayer *)player didReceivedDataLength:(NSInteger)dataLength
{
    CGFloat value = dataLength/1024.0;
    NSString *fromatStr = @"%.1f KB/s";
    if (value > 1024)
    {
        value = value/1024;
        fromatStr = @"%.1f MB/s";
    }

    [_emptyButton setTitle:[NSString stringWithFormat:fromatStr,value] forState:UIControlStateNormal];
}


- (void)player:(EZPlayer *)player didPlayFailed:(NSError *)error
{
    NSLog(@"player: %@, didPlayFailed: %@", player, error);
    //如果是需要验证码或者是验证码错误
    if (error.code == EZ_SDK_NEED_VALIDATECODE) {
        [self showSetPassword];
        return;
    } else if (error.code == EZ_SDK_VALIDATECODE_NOT_MATCH) {
        [self showRetry];
        return;
    } else if (error.code == EZ_SDK_NOT_SUPPORT_TALK) {
        [UIView dd_showDetailMessage:[NSString stringWithFormat:@"%d", (int)error.code]];
        [self.voiceHud hide:YES];
        return;
    }
    else
    {
        if ([player isEqual:_player])
        {
            [_player stopRealPlay];
        }
        else
        {
            [_talkPlayer stopVoiceTalk];
        }
    }
    
    [UIView dd_showDetailMessage:[NSString stringWithFormat:@"%d", (int)error.code]];
    [self.voiceHud hide:YES];
    [self.loadingView stopSquareClockwiseAnimation];
    self.messageLabel.text = [NSString stringWithFormat:@"%@(%d)",NSLocalizedString(@"device_play_fail", @"播放失败"), (int)error.code];
//    if (error.code > 370000)
    {
        self.messageLabel.hidden = NO;
    }
    [UIView animateWithDuration:0.3
                     animations:^{
                         self.speakImageView.alpha = 0.0;
                         self.talkViewContraint.constant = self.bottomView.frame.size.height;
                         [self.bottomView setNeedsUpdateConstraints];
                         [self.bottomView layoutIfNeeded];
                     }
                     completion:^(BOOL finished) {
                         self.speakImageView.alpha = 0;
                         self.talkView.hidden = YES;
                     }];
}

- (void)player:(EZPlayer *)player didReceivedMessage:(NSInteger)messageCode
{
    NSLog(@"player: %@, didReceivedMessage: %d", player, (int)messageCode);
    if (messageCode == PLAYER_REALPLAY_START)
    {
        self.captureButton.enabled = YES;
        self.localRecordButton.enabled = YES;
        [self.loadingView stopSquareClockwiseAnimation];
        self.playButton.enabled = YES;
        [self.playButton setImage:[UIImage imageNamed:@"preview_stopplay_btn_sel"] forState:UIControlStateHighlighted];
        [self.playButton setImage:[UIImage imageNamed:@"preview_stopplay_btn"] forState:UIControlStateNormal];
        _isPlaying = YES;
        
        if (!_isOpenSound)
        {
            [_player closeSound];
        }
        self.messageLabel.hidden = YES;
    }
    else if(messageCode == PLAYER_VOICE_TALK_START)
    {
        self.messageLabel.hidden = YES;
        [_player closeSound];
        self.isStartingTalk = NO;
        [self.voiceHud hide:YES];
        [self.bottomView bringSubviewToFront:self.talkView];
        self.talkView.hidden = NO;
        self.speakImageView.alpha = 0;
        self.speakImageView.highlighted = self.deviceInfo.isSupportTalk == 1;
        self.speakImageView.userInteractionEnabled = self.deviceInfo.isSupportTalk == 3;
        [UIView animateWithDuration:0.3
                         animations:^{
                             self.talkViewContraint.constant = 0;
                             self.speakImageView.alpha = 1.0;
                             [self.bottomView setNeedsUpdateConstraints];
                             [self.bottomView layoutIfNeeded];
                         }
                         completion:^(BOOL finished) {
                         }];
    }
    else if (messageCode == PLAYER_VOICE_TALK_END)
    {
        //对讲结束开启声音
        [_player openSound];
    }
    else if (messageCode == PLAYER_NET_CHANGED)
    {
        [_player stopRealPlay];
        [_player startRealPlay];
    }
}

#pragma mark - ValidateCode Methods

- (void)showSetPassword
{
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"device_input_verify_code", @"请输入视频图片加密密码")
                                                        message:NSLocalizedString(@"device_verify_code_tip", @"您的视频已加密，请输入密码进行查看，初始密码为机身标签上的验证码，如果没有验证码，请输入ABCDEF（密码区分大小写)")
                                                       delegate:self
                                              cancelButtonTitle:NSLocalizedString(@"cancel", @"取消")
                                              otherButtonTitles:NSLocalizedString(@"done", @"确定"), nil];
    alertView.alertViewStyle = UIAlertViewStyleSecureTextInput;
    [alertView show];
}

- (void)showRetry
{
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"device_tip_title", @"温馨提示")
                                                        message:NSLocalizedString(@"device_verify_code_wrong", @"设备密码错误")
                                                       delegate:self
                                              cancelButtonTitle:NSLocalizedString(@"cancel", @"取消")
                                              otherButtonTitles:NSLocalizedString(@"retry", @"重试"), nil];
    [alertView show];
}

#pragma mark - Action Methods

- (IBAction)large:(id)sender
{
    NSNumber *value = [NSNumber numberWithInt:UIInterfaceOrientationLandscapeLeft];
    [[UIDevice currentDevice] setValue:value forKey:@"orientation"];
}

- (IBAction)largeBack:(id)sender
{
    NSNumber *value = [NSNumber numberWithInt:UIInterfaceOrientationPortrait];
    [[UIDevice currentDevice] setValue:value forKey:@"orientation"];
}

- (IBAction)capture:(id)sender
{
    UIImage *image = [_player capturePicture:100];
    [self saveImageToPhotosAlbum:image];
}

- (IBAction)talkButtonClicked:(id)sender
{
    if (self.deviceInfo.isSupportTalk != 1 && self.deviceInfo.isSupportTalk != 3)
    {
        [self.view makeToast:NSLocalizedString(@"not_support_talk", @"设备不支持对讲")
                    duration:1.5
                    position:@"center"];
        return;
    }
    
    __weak EZLivePlayViewController *weakSelf = self;
    [self checkMicPermissionResult:^(BOOL enable) {
        dispatch_async(dispatch_get_main_queue(), ^{
            if (enable)
            {
                if (!weakSelf.voiceHud) {
                    weakSelf.voiceHud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                }
                weakSelf.voiceHud.labelText = NSLocalizedString(@"device_restart_talk", @"正在开启对讲，请稍候...");
                weakSelf.isStartingTalk = YES;
                NSString *verifyCode = [[GlobalKit shareKit].deviceVerifyCodeBySerial objectForKey:weakSelf.deviceInfo.deviceSerial];
                if (verifyCode)
                {
                    [weakSelf.talkPlayer setPlayVerifyCode:verifyCode];
                }
                [weakSelf.talkPlayer startVoiceTalk];
            }
            else
            {
                [weakSelf.view makeToast:NSLocalizedString(@"no_mic_permission", @"未开启麦克风权限")
                                duration:1.5
                                position:@"center"];
            }
        });
    }];
    
    

}

- (IBAction)voiceButtonClicked:(id)sender
{
    if(_isOpenSound){
        [_player closeSound];
        [self.voiceButton setImage:[UIImage imageNamed:@"preview_unvoice_btn_sel"] forState:UIControlStateHighlighted];
        [self.voiceButton setImage:[UIImage imageNamed:@"preview_unvoice_btn"] forState:UIControlStateNormal];
    }
    else
    {
        [_player openSound];
        [self.voiceButton setImage:[UIImage imageNamed:@"preview_voice_btn_sel"] forState:UIControlStateHighlighted];
        [self.voiceButton setImage:[UIImage imageNamed:@"preview_voice_btn"] forState:UIControlStateNormal];
    }
    _isOpenSound = !_isOpenSound;
}

- (IBAction)playButtonClicked:(id)sender
{
    if(_isPlaying)
    {
        [_player stopRealPlay];
        [_playerView setBackgroundColor:[UIColor blackColor]];
        [self.playButton setImage:[UIImage imageNamed:@"preview_play_btn_sel"] forState:UIControlStateHighlighted];
        [self.playButton setImage:[UIImage imageNamed:@"preview_play_btn"] forState:UIControlStateNormal];
        self.localRecordButton.enabled = NO;
        self.captureButton.enabled = NO;
        self.playerPlayButton.hidden = NO;
    }
    else
    {
        [_player startRealPlay];
        self.playerPlayButton.hidden = YES;
        [self.playButton setImage:[UIImage imageNamed:@"preview_stopplay_btn_sel"] forState:UIControlStateHighlighted];
        [self.playButton setImage:[UIImage imageNamed:@"preview_stopplay_btn"] forState:UIControlStateNormal];
        [self.loadingView startSquareClcokwiseAnimation];
    }
    _isPlaying = !_isPlaying;
}

- (IBAction)qualityButtonClicked:(id)sender
{
    if(self.qualityButton.selected)
    {
        self.qualityView.hidden = YES;
    }
    else
    {
        self.qualityView.hidden = NO;
        //停留5s以后隐藏视频质量View.
        [self performSelector:@selector(hideQualityView) withObject:nil afterDelay:5.0f];
    }
    self.qualityButton.selected = !self.qualityButton.selected;
}

- (void)hideQualityView
{
    self.qualityButton.selected = NO;
    self.qualityView.hidden = YES;
}

- (IBAction)qualitySelectedClicked:(id)sender
{
    BOOL result = NO;
    EZVideoLevelType type = EZVideoLevelLow;
    if (sender == self.highButton)
    {
        type = EZVideoLevelHigh;
    }
    else if (sender == self.middleButton)
    {
        type = EZVideoLevelMiddle;
    }
    else
    {
        type = EZVideoLevelLow;
    }
    [EZOPENSDK setVideoLevel:_cameraInfo.deviceSerial
                    cameraNo:_cameraInfo.cameraNo
                  videoLevel:type
                  completion:^(NSError *error) {
                      if (error)
                      {
                          return;
                      }
                      [_player stopRealPlay];
                      
                      _cameraInfo.videoLevel = type;
                      if (sender == self.highButton)
                      {
                          [self.qualityButton setTitle:NSLocalizedString(@"device_quality_high", @"高清") forState:UIControlStateNormal];
                      }
                      else if (sender == self.middleButton)
                      {
                          [self.qualityButton setTitle:NSLocalizedString(@"device_quality_median", @"均衡") forState:UIControlStateNormal];
                      }
                      else
                      {
                          [self.qualityButton setTitle:NSLocalizedString(@"device_quality_low", @"流畅") forState:UIControlStateNormal];
                      }
                      if (result)
                      {
                          [self.loadingView startSquareClcokwiseAnimation];
                      }
                      self.qualityView.hidden = YES;
                      [_player startRealPlay];
                  }];
}

- (IBAction)ptzControlButtonTouchDown:(id)sender
{
    EZPTZCommand command;
    NSString *imageName = nil;
    if(sender == self.ptzLeftButton)
    {
        command = EZPTZCommandLeft;
        imageName = @"ptz_left_sel";
    }
    else if (sender == self.ptzDownButton)
    {
        command = EZPTZCommandDown;
        imageName = @"ptz_bottom_sel";
    }
    else if (sender == self.ptzRightButton)
    {
        command = EZPTZCommandRight;
        imageName = @"ptz_right_sel";
    }
    else {
        command = EZPTZCommandUp;
        imageName = @"ptz_up_sel";
    }
    [self.ptzControlButton setImage:[UIImage imageNamed:imageName] forState:UIControlStateDisabled];
    EZCameraInfo *cameraInfo = [_deviceInfo.cameraInfo firstObject];
    [EZOPENSDK controlPTZ:cameraInfo.deviceSerial
                 cameraNo:cameraInfo.cameraNo
                  command:command
                   action:EZPTZActionStart
                    speed:2
                   result:^(NSError *error) {
                       NSLog(@"error is %@", error);
                   }];
}

- (IBAction)ptzControlButtonTouchUpInside:(id)sender
{
    EZPTZCommand command;
    if(sender == self.ptzLeftButton)
    {
        command = EZPTZCommandLeft;
    }
    else if (sender == self.ptzDownButton)
    {
        command = EZPTZCommandDown;
    }
    else if (sender == self.ptzRightButton)
    {
        command = EZPTZCommandRight;
    }
    else {
        command = EZPTZCommandUp;
    }
    [self.ptzControlButton setImage:[UIImage imageNamed:@"ptz_bg"] forState:UIControlStateDisabled];
    EZCameraInfo *cameraInfo = [_deviceInfo.cameraInfo firstObject];
    [EZOPENSDK controlPTZ:cameraInfo.deviceSerial
                 cameraNo:cameraInfo.cameraNo
                  command:command
                   action:EZPTZActionStop
                    speed:3.0
                   result:^(NSError *error) {
                   }];
}

- (IBAction)ptzViewShow:(id)sender
{
    self.ptzView.hidden = NO;
    [self.bottomView bringSubviewToFront:self.ptzView];
    self.ptzControlButton.alpha = 0;
    [UIView animateWithDuration:0.3
                     animations:^{
                         self.ptzViewContraint.constant = 0;
                         self.ptzControlButton.alpha = 1.0;
                         [self.bottomView setNeedsUpdateConstraints];
                         [self.bottomView layoutIfNeeded];
                     }
                     completion:^(BOOL finished) {
                     }];
}

- (IBAction)closePtzView:(id)sender
{
    [UIView animateWithDuration:0.3
                     animations:^{
                         self.ptzControlButton.alpha = 0.0;
                         self.ptzViewContraint.constant = self.bottomView.frame.size.height;
                         [self.bottomView setNeedsUpdateConstraints];
                         [self.bottomView layoutIfNeeded];
                     }
                     completion:^(BOOL finished) {
                         self.ptzControlButton.alpha = 0;
                         self.ptzView.hidden = YES;
                     }];
}

- (IBAction)closeTalkView:(id)sender
{
    [_talkPlayer stopVoiceTalk];
    [UIView animateWithDuration:0.3
                     animations:^{
                         self.speakImageView.alpha = 0.0;
                         self.talkViewContraint.constant = self.bottomView.frame.size.height;
                         [self.bottomView setNeedsUpdateConstraints];
                         [self.bottomView layoutIfNeeded];
                     }
                     completion:^(BOOL finished) {
                         self.speakImageView.alpha = 0;
                         self.talkView.hidden = YES;
                     }];
}

- (IBAction)localButtonClicked:(id)sender
{

    //结束本地录像
    if(self.localRecordButton.selected)
    {
        [_player stopLocalRecord];
        [_recordTimer invalidate];
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, 1.0 * NSEC_PER_SEC), dispatch_get_main_queue(), ^{
            _recordTimer = nil;
            [_fileData writeToFile:_filePath atomically:YES];
            self.localRecordLabel.hidden = YES;
            [self saveRecordToPhotosAlbum:_filePath];
            _filePath = nil;
        });
    }
    else
    {
        //开始本地录像
        NSString *path = @"/OpenSDK/EzvizLocalRecord";
        
        NSArray * docdirs = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        NSString * docdir = [docdirs objectAtIndex:0];
        
        NSString * configFilePath = [docdir stringByAppendingPathComponent:path];
        if(![[NSFileManager defaultManager] fileExistsAtPath:configFilePath]){
            NSError *error = nil;
            [[NSFileManager defaultManager] createDirectoryAtPath:configFilePath
                                      withIntermediateDirectories:YES
                                                       attributes:nil
                                                            error:&error];
        }
        NSDateFormatter *dateformatter = [[NSDateFormatter alloc] init];
        dateformatter.dateFormat = @"yyyyMMddHHmmssSSS";
        _filePath = [NSString stringWithFormat:@"%@/%@.mov",configFilePath,[dateformatter stringFromDate:[NSDate date]]];
        _fileData = [NSMutableData new];
 
        self.localRecordLabel.text = @"  00:00";

        __weak __typeof(self) weakSelf = self;
        [_player startLocalRecord:^(NSData *data) {
            if (!_recordTimer)
            {
                _recordTimer = [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(timerStart:) userInfo:nil repeats:YES];
            }
            if (!data || !weakSelf.fileData) {
                return;
            }
            [weakSelf.fileData appendData:data];
        }];
        self.localRecordLabel.hidden = NO;
        _seconds = 0;
    }
    self.localRecordButton.selected = !self.localRecordButton.selected;
}

- (void)timerStart:(NSTimer *)timer
{
    NSInteger currentTime = ++_seconds;
    self.localRecordLabel.text = [NSString stringWithFormat:@"  %02d:%02d", (int)currentTime/60, (int)currentTime % 60];
    if (!_orangeLayer)
    {
        _orangeLayer = [CALayer layer];
        _orangeLayer.frame = CGRectMake(10.0, 8.0, 8.0, 8.0);
        _orangeLayer.backgroundColor = [UIColor dd_hexStringToColor:@"0xff6000"].CGColor;
        _orangeLayer.cornerRadius = 4.0f;
    }
    if(currentTime % 2 == 0)
    {
        [_orangeLayer removeFromSuperlayer];
    }
    else
    {
        [self.localRecordLabel.layer addSublayer:_orangeLayer];
    }
}

- (IBAction)talkPressed:(id)sender
{
    if (!_isPressed)
    {
        self.speakImageView.highlighted = YES;
        [self.talkPlayer audioTalkPressed:YES];
    }
    else
    {
        self.speakImageView.highlighted = NO;
        [self.talkPlayer audioTalkPressed:NO];
    }
    _isPressed = !_isPressed;
}

#pragma mark - Private Methods

- (void) checkMicPermissionResult:(void(^)(BOOL enable)) retCb
{
    AVAuthorizationStatus authStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeAudio];
    
    switch (authStatus)
    {
        case AVAuthorizationStatusNotDetermined://未决
        {
            AVAudioSession *avSession = [AVAudioSession sharedInstance];
            [avSession performSelector:@selector(requestRecordPermission:) withObject:^(BOOL granted) {
                if (granted)
                {
                    if (retCb)
                    {
                        retCb(YES);
                    }
                }
                else
                {
                    if (retCb)
                    {
                        retCb(NO);
                    }
                }
            }];
        }
            break;
            
        case AVAuthorizationStatusRestricted://未授权，家长限制
        case AVAuthorizationStatusDenied://未授权
            if (retCb)
            {
                retCb(NO);
            }
            break;
            
        case AVAuthorizationStatusAuthorized://已授权
            if (retCb)
            {
                retCb(YES);
            }
            break;
            
        default:
            if (retCb)
            {
                retCb(NO);
            }
            break;
    }
}

- (void)saveImageToPhotosAlbum:(UIImage *)savedImage
{
    PHAuthorizationStatus status = [PHPhotoLibrary authorizationStatus];
    if (status == PHAuthorizationStatusNotDetermined)
    {
        [PHPhotoLibrary requestAuthorization:^(PHAuthorizationStatus status) {
            if(status == PHAuthorizationStatusAuthorized)
            {
                UIImageWriteToSavedPhotosAlbum(savedImage, self, @selector(imageSavedToPhotosAlbum:didFinishSavingWithError:contextInfo:), NULL);
            }
        }];
    }
    else
    {
        if (status == PHAuthorizationStatusAuthorized)
        {
            UIImageWriteToSavedPhotosAlbum(savedImage, self, @selector(imageSavedToPhotosAlbum:didFinishSavingWithError:contextInfo:), NULL);
        }
    }
}

- (void)saveRecordToPhotosAlbum:(NSString *)path
{
    PHAuthorizationStatus status = [PHPhotoLibrary authorizationStatus];
    if (status == PHAuthorizationStatusNotDetermined)
    {
        [PHPhotoLibrary requestAuthorization:^(PHAuthorizationStatus status) {
            if(status == PHAuthorizationStatusAuthorized)
            {
                UISaveVideoAtPathToSavedPhotosAlbum(path, self, @selector(imageSavedToPhotosAlbum:didFinishSavingWithError:contextInfo:), NULL);
            }
        }];
    }
    else
    {
        if (status == PHAuthorizationStatusAuthorized)
        {
            UISaveVideoAtPathToSavedPhotosAlbum(path, self, @selector(imageSavedToPhotosAlbum:didFinishSavingWithError:contextInfo:), NULL);
        }
    }
}

// 指定回调方法
- (void)imageSavedToPhotosAlbum:(UIImage *)image didFinishSavingWithError:(NSError *)error contextInfo:(void *)contextInfo
{
    NSString *message = nil;
    if (!error) {
        message = NSLocalizedString(@"device_save_gallery", @"已保存至手机相册");
    }
    else
    {
        message = [error description];
    }
    [UIView dd_showMessage:message];
}

- (void)addLine
{
    for (UIView *view in self.toolBar.subviews) {
        if ([view isKindOfClass:[UIImageView class]])
        {
            [view removeFromSuperview];
        }
    }
    CGFloat averageWidth = [UIScreen mainScreen].bounds.size.width/5.0;
    UIImageView *lineImageView1 = [UIView dd_instanceVerticalLine:20 color:[UIColor grayColor]];
    lineImageView1.frame = CGRectMake(averageWidth, 7, lineImageView1.frame.size.width, lineImageView1.frame.size.height);
    [self.toolBar addSubview:lineImageView1];
    UIImageView *lineImageView2 = [UIView dd_instanceVerticalLine:20 color:[UIColor grayColor]];
    lineImageView2.frame = CGRectMake(averageWidth * 2, 7, lineImageView2.frame.size.width, lineImageView2.frame.size.height);
    [self.toolBar addSubview:lineImageView2];
    UIImageView *lineImageView3 = [UIView dd_instanceVerticalLine:20 color:[UIColor grayColor]];
    lineImageView3.frame = CGRectMake(averageWidth * 3, 7, lineImageView3.frame.size.width, lineImageView3.frame.size.height);
    [self.toolBar addSubview:lineImageView3];
    UIImageView *lineImageView4 = [UIView dd_instanceVerticalLine:20 color:[UIColor grayColor]];
    lineImageView4.frame = CGRectMake(averageWidth * 4, 7, lineImageView4.frame.size.width, lineImageView4.frame.size.height);
    [self.toolBar addSubview:lineImageView4];
}

@end
