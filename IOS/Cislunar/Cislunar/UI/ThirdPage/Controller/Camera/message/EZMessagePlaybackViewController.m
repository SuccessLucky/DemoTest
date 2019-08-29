//
//  EZMessagePlaybackViewController.m
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/11/15.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import "EZMessagePlaybackViewController.h"
#import "UIViewController+EZBackPop.h"

//#import "EZPlayer.h"
#import "HIKLoadView.h"
#import "Masonry.h"
#import "DDKit.h"
//#import "EZDeviceRecordFile.h"
//#import "EZCloudRecordFile.h"

@interface EZMessagePlaybackViewController ()<EZPlayerDelegate, UIAlertViewDelegate>
{
    BOOL _isOpenSound;
    BOOL _isPlaying;
    BOOL _isShowToolbox;
    
    NSTimer *_recordTimer;
    
    FILE *_localRecord;
    
    NSString *_filePath;
    
    NSTimeInterval _seconds;
    CALayer *_orangeLayer;
    
    NSTimeInterval _playSeconds; //播放秒数
    NSTimeInterval _duringSeconds; //录像时长
    
    EZDeviceRecordFile *_deviceRecord;
    EZCloudRecordFile *_cloudRecord;
}

@property (nonatomic, strong) NSMutableArray *records;
@property (nonatomic, strong) NSDate *beginTime;
@property (nonatomic, strong) NSDate *endTime;
@property (nonatomic, strong) EZPlayer *player;
@property (nonatomic, strong) HIKLoadView *loadingView;
@property (nonatomic) BOOL isSelectedDevice;
@property (nonatomic, weak) IBOutlet UIView *playerView;
@property (nonatomic, weak) IBOutlet UIView *playerToolbox;
@property (nonatomic, weak) IBOutlet UIButton *voiceButton;
@property (nonatomic, weak) IBOutlet UIButton *playButton;
@property (nonatomic, weak) IBOutlet UIButton *largeButton;
@property (nonatomic, weak) IBOutlet UILabel *playTimeLabel;
@property (nonatomic, weak) IBOutlet UILabel *duringTimeLabel;
@property (nonatomic, weak) IBOutlet UISlider *duringSlider;
@property (nonatomic, weak) IBOutlet UIButton *largeBackButton;
@property (nonatomic, weak) IBOutlet NSLayoutConstraint *playerToolboxConstraint;
@property (nonatomic, weak) IBOutlet UIButton *captureButton;
@property (nonatomic, weak) IBOutlet UIButton *localRecordButton;
@property (nonatomic, weak) IBOutlet UILabel *localRecordLabel;
@property (nonatomic, strong) NSTimer *playbackTimer;
@property (nonatomic, strong) NSMutableData *fileData;

@end

@implementation EZMessagePlaybackViewController

- (void)dealloc
{
    [EZOPENSDK releasePlayer:_player];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = self.info.alarmName;
    self.isAutorotate = YES;
    
    self.largeBackButton.hidden = YES;
    _isOpenSound = YES;
    
    if(!_records)
        _records = [NSMutableArray new];
    
    
    _player = [EZPlayer createPlayerWithDeviceSerial:self.info.deviceSerial cameraNo:self.info.cameraNo];
    _player.delegate = self;
    [_player setPlayerView:_playerView];
    //判断设备是否加密，加密就从demo的内存中获取设备验证码填入到播放器的验证码接口里，本demo只处理内存存储，本地持久化存储用户自行完成
    if (self.deviceInfo.isEncrypt)
    {
        NSString *verifyCode = [[GlobalKit shareKit].deviceVerifyCodeBySerial objectForKey:self.info.deviceSerial];
        [_player setPlayVerifyCode:verifyCode];
    }
    
    
    if(!_loadingView)
        _loadingView = [[HIKLoadView alloc] initWithHIKLoadViewStyle:HIKLoadViewStyleSqureClockWise];
    [self.view insertSubview:_loadingView aboveSubview:self.playerView];
    [_loadingView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.width.height.mas_equalTo(@14);
        make.centerX.mas_equalTo(self.playerView.mas_centerX);
        make.centerY.mas_equalTo(self.playerView.mas_centerY);
    }];
    [self.loadingView startSquareClcokwiseAnimation];
    
    [self.captureButton dd_centerImageAndTitle];
    [self.localRecordButton dd_centerImageAndTitle];
    
    [self.duringSlider setThumbImage:[UIImage imageNamed:@"slider"] forState:UIControlStateNormal];
    [self.duringSlider setThumbImage:[UIImage imageNamed:@"slider_sel"] forState:UIControlStateHighlighted];
    
    self.captureButton.enabled = NO;
    self.localRecordButton.enabled = NO;
    
    self.beginTime = [self.info.alarmStartTime dateByAddingTimeInterval:-(self.info.preTime)];
    self.endTime = [self.info.alarmStartTime dateByAddingTimeInterval:self.info.delayTime];
    __weak typeof (self) weakSelf = self;
    [EZOPENSDK searchRecordFileFromDevice:self.info.deviceSerial
                                 cameraNo:self.info.cameraNo
                                beginTime:self.beginTime
                                  endTime:self.endTime
                               completion:^(NSArray *deviceRecords, NSError *error) {
                                   [weakSelf.records removeAllObjects];
                                   if([deviceRecords count] == 0)
                                   {
                                       [weakSelf doCloudSearch];
                                   }
                                   else
                                   {
                                       weakSelf.isSelectedDevice = YES;
                                       [weakSelf.records addObjectsFromArray:deviceRecords];
                                       [weakSelf doPlayFirst];
                                   }
                               }];
    
    self.localRecordLabel.layer.borderColor = [UIColor whiteColor].CGColor;
    self.localRecordLabel.layer.cornerRadius = 12.0f;
    self.localRecordLabel.layer.borderWidth = 1.0f;
    self.localRecordLabel.clipsToBounds = YES;

    _isShowToolbox = YES;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navigationController.navigationBar.barStyle = UIBarStyleDefault;
    self.navigationController.navigationBar.tintColor = UIBarStyleDefault;
}

- (void)viewWillDisappear:(BOOL)animated
{
    
    if (self.playbackTimer)
    {
        [self.playbackTimer invalidate];
        self.playbackTimer = nil;
    }
    
    if (_recordTimer)
    {
        [_recordTimer invalidate];
        _recordTimer = nil;
    }
    [NSObject cancelPreviousPerformRequestsWithTarget:self selector:@selector(hiddenPlayerToolbox:) object:nil];
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

- (UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskAllButUpsideDown;
}

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation
                                duration:(NSTimeInterval)duration
{
    self.navigationController.navigationBarHidden = NO;
    self.largeBackButton.hidden = YES;
    self.playerToolboxConstraint.constant = 60.0f;
    self.largeButton.hidden = NO;
    self.voiceButton.hidden = NO;
    self.playButton.hidden = NO;
    if (toInterfaceOrientation == UIInterfaceOrientationLandscapeLeft ||
        toInterfaceOrientation == UIInterfaceOrientationLandscapeRight)
    {
        self.playerToolboxConstraint.constant = 23.0f;
        self.playButton.hidden = YES;
        self.voiceButton.hidden = YES;
        self.largeButton.hidden = YES;
        self.largeBackButton.hidden = NO;
        self.navigationController.navigationBarHidden = YES;
    }
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
            [self.player setPlayVerifyCode:checkCode];
            [self doPlayFirst];
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

- (void)player:(EZPlayer *)player didPlayFailed:(NSError *)error
{
    NSLog(@"player: %@ didPlayFailed: %@", player, error);
    //如果是需要验证码或者是验证码错误
    if (error.code == EZ_SDK_NEED_VALIDATECODE) {
        [self showSetPassword];
        return;
    } else if (error.code == EZ_SDK_VALIDATECODE_NOT_MATCH) {
        [self showRetry];
        return;
    }
    [UIView dd_showDetailMessage:[NSString stringWithFormat:@"%d", (int)error.code]];
}

- (void)player:(EZPlayer *)player didReceivedMessage:(NSInteger)messageCode
{
    NSLog(@"player: %@, didReceivedMessage: %d", player, (int)messageCode);
    if (messageCode == PLAYER_PLAYBACK_START)
    {
        _isPlaying = YES;
        [self.playButton setImage:[UIImage imageNamed:@"pause_sel"] forState:UIControlStateHighlighted];
        [self.playButton setImage:[UIImage imageNamed:@"pause"] forState:UIControlStateNormal];
        [self.loadingView stopSquareClockwiseAnimation];
        
        if(!_isOpenSound)
        {
            [_player closeSound];
        }
        
        if(self.playbackTimer)
        {
            [self.playbackTimer invalidate];
            self.playbackTimer = nil;
        }
        
        self.playbackTimer = [NSTimer scheduledTimerWithTimeInterval:1
                                                              target:self
                                                            selector:@selector(messagePlaybackRefresh:)
                                                            userInfo:nil
                                                             repeats:YES];
        [[NSRunLoop currentRunLoop] addTimer:self.playbackTimer forMode:NSRunLoopCommonModes];
        
        self.localRecordButton.enabled = YES;
        self.captureButton.enabled = YES;
        
        [self performSelector:@selector(hiddenPlayerToolbox:) withObject:nil afterDelay:5.0f];
    }
    else if (messageCode == PLAYER_PLAYBACK_STOP)
    {
        if(self.playbackTimer)
        {
            [self.playbackTimer invalidate];
            self.playbackTimer = nil;
        }
    }
}


#pragma mark - Actions

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

- (IBAction)voiceButtonClicked:(id)sender
{
    if(_isOpenSound)
    {
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

- (IBAction)duringValueChange:(id)sender
{
    NSDate *offsetTime = nil;
    if(_isSelectedDevice)
    {
        offsetTime = [_deviceRecord.startTime dateByAddingTimeInterval:_duringSeconds * self.duringSlider.value];
    }
    else
    {
        offsetTime = [_cloudRecord.startTime dateByAddingTimeInterval:_duringSeconds * self.duringSlider.value];
    }
    if(self.playbackTimer)
    {
        [self.playbackTimer invalidate];
        self.playbackTimer = nil;
    }
    [_player seekPlayback:offsetTime];
    [self.loadingView startSquareClcokwiseAnimation];
}

- (IBAction)playButtonClicked:(id)sender
{
    if(_isPlaying)
    {
        [_player pausePlayback];
        [self.playButton setImage:[UIImage imageNamed:@"preview_play_btn_sel"] forState:UIControlStateHighlighted];
        [self.playButton setImage:[UIImage imageNamed:@"preview_play_btn"] forState:UIControlStateNormal];
        self.localRecordButton.enabled = NO;
        self.captureButton.enabled = NO;
        if(_playbackTimer && _cloudRecord)
        {
            [_playbackTimer invalidate];
            _playbackTimer = nil;
        }
    }
    else
    {
        [_player resumePlayback];
        [self.playButton setImage:[UIImage imageNamed:@"pause_sel"] forState:UIControlStateHighlighted];
        [self.playButton setImage:[UIImage imageNamed:@"pause"] forState:UIControlStateNormal];
        self.localRecordButton.enabled = YES;
        self.captureButton.enabled = YES;
        if(_cloudRecord)
           [self.loadingView startSquareClcokwiseAnimation];
    }
    _isPlaying = !_isPlaying;
}

- (void)doPlayFirst
{
    id record = [_records dd_objectAtIndex:0];
    if(record && [record isKindOfClass:[EZDeviceRecordFile class]])
    {
        EZDeviceRecordFile *alarmRecrod = record;
        alarmRecrod.startTime = self.beginTime;
        alarmRecrod.stopTime = self.endTime;
        [_player startPlaybackFromDevice:alarmRecrod];
        _deviceRecord = alarmRecrod;
    }
    else if (record && [record isKindOfClass:[EZCloudRecordFile class]])
    {
        EZCloudRecordFile *alarmRecrod = record;
        alarmRecrod.startTime = self.beginTime;
        alarmRecrod.stopTime = self.endTime;
        [_player startPlaybackFromCloud:alarmRecrod];
        _cloudRecord = alarmRecrod;
    }
    else {
        return;
    }
    
    [self duringTimeShow];
    [self.loadingView startSquareClcokwiseAnimation];
    
    _isPlaying = YES;
}

- (void)duringTimeShow
{
    if(_isSelectedDevice)
    {
        _duringSeconds = [_deviceRecord.stopTime timeIntervalSinceDate:_deviceRecord.startTime];
    }
    else
    {
        _duringSeconds = [_cloudRecord.stopTime timeIntervalSinceDate:_cloudRecord.startTime];
    }
    
    if(_duringSeconds >= 3600)
    {
        int hour = (int)_duringSeconds / 3600;
        int minute =  ((int)_duringSeconds % 3600) / 60;
        int seconds = (int)_duringSeconds % 60;
        self.duringTimeLabel.text = [NSString stringWithFormat:@"%02d:%02d:%02d", hour, minute, seconds];
        self.playTimeLabel.text = @"00:00:00";
    }
    else
    {
        int minute =  (int)_duringSeconds / 60;
        int seconds = (int)_duringSeconds % 60;
        self.duringTimeLabel.text = [NSString stringWithFormat:@"%02d:%02d", minute, seconds];
        self.playTimeLabel.text = @"00:00";
    }
}

- (void)messagePlaybackRefresh:(NSTimer *)timer
{
    NSDate *currentTime = [_player getOSDTime];
    if(_isSelectedDevice)
    {
        _playSeconds = [currentTime timeIntervalSinceDate:_deviceRecord.startTime];
    }
    else
    {
        _playSeconds = [currentTime timeIntervalSinceDate:_cloudRecord.startTime];
    }
    
    if (_playSeconds < 0)
    {
        _playSeconds = 0;
    }
    
    if(_duringSeconds >= 3600)
    {
        int hour = (int)_playSeconds / 3600;
        int minute =  ((int)_playSeconds % 3600) / 60;
        int seconds = (int)_playSeconds % 60;
        self.playTimeLabel.text = [NSString stringWithFormat:@"%02d:%02d:%02d", hour, minute, seconds];
    }
    else
    {
        int minute =  (int)_playSeconds / 60;
        int seconds = (int)_playSeconds % 60;
        self.playTimeLabel.text = [NSString stringWithFormat:@"%02d:%02d", minute, seconds];
    }
    
    self.duringSlider.value = _playSeconds/_duringSeconds;
}

- (IBAction)capture:(id)sender
{
    UIImage *image = [_player capturePicture:100];
    [self saveImageToPhotosAlbum:image];
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
        if (!_fileData) {
            _fileData = [NSMutableData new];
        }
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
    if(!_orangeLayer)
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

- (IBAction)showToolBar:(id)sender
{
    if(!_isShowToolbox){
        _isShowToolbox = YES;
        [UIView animateWithDuration:0.3 animations:^{
            self.playerToolbox.alpha = 1.0f;
        }];
        [self performSelector:@selector(hiddenPlayerToolbox:) withObject:nil afterDelay:5.0f];
    }
    else{

        [NSObject cancelPreviousPerformRequestsWithTarget:self selector:@selector(hiddenPlayerToolbox:) object:nil];
        [self hiddenPlayerToolbox:nil];
    }
}

- (IBAction)hiddenPlayerToolbox:(id)sender
{
    [UIView animateWithDuration:0.3
                     animations:^{
                         self.playerToolbox.alpha = 0.0f;
                     }
                     completion:^(BOOL finished) {
                         _isShowToolbox = NO;
                     }];
}

- (void)doCloudSearch
{
    __weak typeof(self) weakSelf = self;
    [EZOPENSDK searchRecordFileFromCloud:self.info.deviceSerial
                                cameraNo:self.info.cameraNo
                               beginTime:self.beginTime
                                 endTime:self.endTime
                              completion:^(NSArray *cloudRecords, NSError *error) {
                                  
                                  [weakSelf.records removeAllObjects];
                                  weakSelf.isSelectedDevice = NO;
                                  
                                  if (!cloudRecords || cloudRecords.count == 0)
                                  {
                                      [weakSelf.loadingView stopSquareClockwiseAnimation];
                                      [UIView dd_showMessage:NSLocalizedString(@"message_file_search_fail", @"文件查询失败")];
                                  }
                                  
                                  [weakSelf.records addObjectsFromArray:cloudRecords];
                                  [weakSelf doPlayFirst];
                              }];
}

#pragma mark - Private Methods

- (void)saveImageToPhotosAlbum:(UIImage *)savedImage
{
    UIImageWriteToSavedPhotosAlbum(savedImage, self, @selector(imageSavedToPhotosAlbum:didFinishSavingWithError:contextInfo:), NULL);
}

- (void)saveRecordToPhotosAlbum:(NSString *)path
{
    UISaveVideoAtPathToSavedPhotosAlbum(path, self, @selector(imageSavedToPhotosAlbum:didFinishSavingWithError:contextInfo:), NULL);
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

@end
