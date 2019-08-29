//
//  EZPlaybackViewController.m
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/11/3.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import "EZPlaybackViewController.h"
#import "UIViewController+EZBackPop.h"
#import "DDCollectionViewFlowLayout.h"
#import "MJRefresh.h"

#import "EZRecordCell.h"
#import "DDKit.h"
//#import "EZCloudRecordFile.h"
//#import "EZDeviceRecordFile.h"
//#import "EZPlayer.h"
#import "HIKLoadView.h"
#import "Masonry.h"
//#import "EZCameraInfo.h"
#import "MBProgressHUD.h"

@interface EZPlaybackViewController ()<DDCollectionViewDelegateFlowLayout, UICollectionViewDataSource,EZPlayerDelegate, UIAlertViewDelegate>
{
    BOOL _isOpenSound;
    BOOL _isPlaying;
    
    NSTimeInterval _playSeconds; //播放秒数
    NSTimeInterval _duringSeconds; //录像时长
    
    EZDeviceRecordFile *_deviceRecord;
    EZCloudRecordFile *_cloudRecord;
    
    BOOL _isShowToolbox;
    
//    BOOL _isDoBack;
}

@property (nonatomic, strong) NSIndexPath *selectedIndexPath;
@property (nonatomic, strong) NSMutableArray *records;
@property (nonatomic, strong) NSDate *beginTime;
@property (nonatomic, strong) NSDate *endTime;
@property (nonatomic, strong) EZPlayer *player;
@property (nonatomic, strong) HIKLoadView *loadingView;
@property (nonatomic) BOOL isSelectedDevice;
@property (nonatomic, weak) IBOutlet UIView *playerView;
@property (nonatomic, weak) IBOutlet UILabel *largeTitleLabel;
@property (nonatomic, weak) IBOutlet UIDatePicker *datePicker;
@property (nonatomic, weak) IBOutlet UITextField *dateTextField;
@property (nonatomic, weak) IBOutlet UIToolbar *dateToolbar;
@property (nonatomic, weak) IBOutlet UIButton *dateButton;
@property (nonatomic, weak) IBOutlet UICollectionView *playbackList;
@property (nonatomic, weak) IBOutlet UIView *toolView;
@property (nonatomic, weak) IBOutlet UIButton *cloudButton;
@property (nonatomic, weak) IBOutlet UIButton *deviceButton;
@property (nonatomic, weak) IBOutlet UIView *selectedImageView;
@property (nonatomic, weak) IBOutlet UIView *playerToolbox;
@property (nonatomic, weak) IBOutlet UIButton *voiceButton;
@property (nonatomic, weak) IBOutlet UIButton *playButton;
@property (nonatomic, weak) IBOutlet UIButton *largeButton;
@property (nonatomic, weak) IBOutlet UILabel *playTimeLabel;
@property (nonatomic, weak) IBOutlet UILabel *duringTimeLabel;
@property (nonatomic, weak) IBOutlet UISlider *duringSlider;
@property (nonatomic, weak) IBOutlet UIButton *largeBackButton;
@property (nonatomic, weak) IBOutlet NSLayoutConstraint *selectedImageViewConstraint;
@property (nonatomic, weak) IBOutlet NSLayoutConstraint *playerToolboxConstraint;
@property (nonatomic, strong) NSTimer *playbackTimer;
@property (nonatomic, weak) IBOutlet UIImageView *noVideoImageView;
@property (nonatomic, weak) IBOutlet UILabel *noVideoLabel;
@property (nonatomic, strong) NSOperation *operation;

@end

@implementation EZPlaybackViewController

- (void)dealloc
{
    NSLog(@"%@ dealloc", self.class);
    [EZOPENSDK releasePlayer:_player];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.isAutorotate = YES;
    self.largeTitleLabel.text = self.deviceInfo.deviceName;
    self.largeTitleLabel.hidden = YES;
    
    if(!_records)
        _records = [NSMutableArray new];
    
    DDCollectionViewFlowLayout *flowLayout = [[DDCollectionViewFlowLayout alloc] init];
    flowLayout.delegate = self;
    [self.playbackList setCollectionViewLayout:flowLayout];
    
    [self addHeaderRefresh];
    
    //demo只获取了设备的第一个通道
    EZCameraInfo *cameraInfo = [self.deviceInfo.cameraInfo dd_objectAtIndex:_cameraIndex];
    _player = [EZOPENSDK createPlayerWithDeviceSerial:cameraInfo.deviceSerial cameraNo:cameraInfo.cameraNo];
    _player.delegate = self;
    [_player setPlayerView:_playerView];
    //判断设备是否加密，加密就从demo的内存中获取设备验证码填入到播放器的验证码接口里，本demo只处理内存存储，本地持久化存储用户自行完成
    if (self.deviceInfo.isEncrypt)
    {
        NSString *verifyCode = [[GlobalKit shareKit].deviceVerifyCodeBySerial objectForKey:cameraInfo.deviceSerial];
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
    [_loadingView stopSquareClockwiseAnimation];
    _isOpenSound = YES;
    
    [self.duringSlider setThumbImage:[UIImage imageNamed:@"slider"] forState:UIControlStateNormal];
    [self.duringSlider setThumbImage:[UIImage imageNamed:@"slider_sel"] forState:UIControlStateHighlighted];

    self.cloudButton.selected = YES;
    self.largeBackButton.hidden = YES;
    
    self.dateTextField.inputView = self.datePicker;
    self.dateTextField.inputAccessoryView = self.dateToolbar;
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    dateFormatter.dateFormat = @"MM-dd ▽";
    [self.dateButton setTitle:[dateFormatter stringFromDate:self.datePicker.date] forState:UIControlStateNormal];
    dateFormatter.dateFormat = @"MM-dd △";
    [self.dateButton setTitle:[dateFormatter stringFromDate:self.datePicker.date] forState:UIControlStateSelected];
 
    _isShowToolbox = YES;
}

- (void)viewWillDisappear:(BOOL)animated {
    
    if (self.playbackTimer)
    {
        [self.playbackTimer invalidate];
        self.playbackTimer = nil;
    }
    [self.loadingView stopSquareClockwiseAnimation];
    [_player closeSound];
    [_player stopPlayback];
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
    self.toolView.hidden = NO;
    self.playbackList.hidden = NO;
    self.largeBackButton.hidden = YES;
    self.largeTitleLabel.hidden = YES;
    self.playerToolboxConstraint.constant = 60.0f;
    self.largeButton.hidden = NO;
    self.voiceButton.hidden = NO;
    self.playButton.hidden = NO;
    if(toInterfaceOrientation == UIInterfaceOrientationLandscapeLeft ||
       toInterfaceOrientation == UIInterfaceOrientationLandscapeRight)
    {
        self.playerToolboxConstraint.constant = 23.0f;
        self.playButton.hidden = YES;
        self.voiceButton.hidden = YES;
        self.largeButton.hidden = YES;
        self.toolView.hidden = YES;
        self.largeTitleLabel.hidden = NO;
        self.playbackList.hidden = YES;
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

#pragma mark - UICollectionViewDataSource Methods

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return [_records count];
}

- (NSInteger)collectionView:(UICollectionView *)collectionView layout:(DDCollectionViewFlowLayout *)layout numberOfColumnsInSection:(NSInteger)section
{
    return 3;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    EZRecordCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"RecordCell" forIndexPath:indexPath];
    cell.deviceSerial = self.deviceInfo.deviceSerial;
    if(_isSelectedDevice)
    {
        [cell setDeviceRecord:[_records dd_objectAtIndex:indexPath.row] selected:(indexPath.row == self.selectedIndexPath.row)];
    }
    else
    {
        [cell setCloudRecord:[_records dd_objectAtIndex:_records.count- 1 -indexPath.row] selected:(indexPath.row == self.selectedIndexPath.row)];
    }
    return cell;
}

#pragma mark - UICollectionViewDelegate Methods

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    return CGSizeMake(106 * [UIScreen mainScreen].bounds.size.width / 320.0f, 80.0 * [UIScreen mainScreen].bounds.size.width / 320.0f);
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    if(_isSelectedDevice)
    {
        _deviceRecord = [_records dd_objectAtIndex:indexPath.row];
        [_player startPlaybackFromDevice:_deviceRecord];
    }
    else
    {
        _cloudRecord = [_records dd_objectAtIndex:_records.count - 1 - indexPath.row];
        [_player startPlaybackFromCloud:_cloudRecord];
    }
    
    [self duringTimeShow];
    
    self.selectedIndexPath = indexPath;
    [collectionView reloadData];
    
    [self.loadingView startSquareClcokwiseAnimation];

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
    else if (error.code == 34)//特殊处理
    {
        [_player stopPlayback];
        if (_isSelectedDevice)
        {
            _deviceRecord = [_records firstObject];
            [_player startPlaybackFromDevice:_deviceRecord];
        }
        else
        {
            _cloudRecord = [_records lastObject];
            [_player startPlaybackFromCloud:_cloudRecord];
        }
        return;

    }
    [UIView dd_showDetailMessage:[NSString stringWithFormat:@"%d", (int)error.code]];
    if (error.code == 400409) {
        _isPlaying = NO;
        [self.playButton setImage:[UIImage imageNamed:@"preview_play_btn_sel"] forState:UIControlStateHighlighted];
        [self.playButton setImage:[UIImage imageNamed:@"preview_play_btn"] forState:UIControlStateNormal];
    }
}

- (void)player:(EZPlayer *)player didReceivedMessage:(NSInteger)messageCode
{
    NSLog(@"player: %@ didReceivedMessage: %d", player, (int)messageCode);
    if(messageCode == PLAYER_PLAYBACK_START)
    {
        _isPlaying = YES;
        [self.playButton setImage:[UIImage imageNamed:@"pause_sel"] forState:UIControlStateHighlighted];
        [self.playButton setImage:[UIImage imageNamed:@"pause"] forState:UIControlStateNormal];
        [self.loadingView stopSquareClockwiseAnimation];
        
        if (!_isOpenSound)
        {
            [player closeSound];
        }
        
        if (self.playbackTimer)
        {
            [self.playbackTimer invalidate];
            self.playbackTimer = nil;
        }
        
        self.playbackTimer = [NSTimer scheduledTimerWithTimeInterval:1
                                                              target:self
                                                            selector:@selector(playBoxToolRefresh:)
                                                            userInfo:nil
                                                             repeats:YES];
        [[NSRunLoop currentRunLoop] addTimer:self.playbackTimer forMode:NSRunLoopCommonModes];
        
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
//    [UIView animateWithDuration:0.3
//                     animations:^{
//                         self.playerToolbox.alpha = 0.0f;
//                     }
//                     completion:^(BOOL finished) {
//                         _isShowToolbox = NO;
//                     }];
}

#pragma mark - MJRefresh Methods

- (void)addHeaderRefresh
{
    [self.operation cancel];
    [self.playbackList.header endRefreshing];
    __weak __typeof(self) weakSelf = self;
    EZCameraInfo *cameraInfo = [self.deviceInfo.cameraInfo objectAtIndex:_cameraIndex];
    self.playbackList.header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
        weakSelf.noVideoImageView.hidden = YES;
        weakSelf.noVideoLabel.hidden = YES;
        if (weakSelf.isSelectedDevice) {
            weakSelf.operation = [EZOPENSDK searchRecordFileFromDevice:cameraInfo.deviceSerial
                                                              cameraNo:cameraInfo.cameraNo
                                                             beginTime:weakSelf.beginTime
                                                               endTime:weakSelf.endTime
                                                            completion:^(NSArray *deviceRecords, NSError *error) {
                                                                NSLog(@"deviceRecords is %@, error is %@", deviceRecords, error);
                                                                if (!error)
                                                                {
                                                                    [weakSelf.records removeAllObjects];
                                                                    if(deviceRecords.count == 0)
                                                                    {
                                                                        weakSelf.noVideoLabel.hidden = NO;
                                                                        weakSelf.noVideoImageView.hidden = NO;
                                                                        [weakSelf.playbackList reloadData];
                                                                        [weakSelf.playbackList.header endRefreshing];
                                                                        return;
                                                                    }
                                                                    [weakSelf.records addObjectsFromArray:deviceRecords];
                                                                    [weakSelf.playbackList reloadData];
                                                                    [weakSelf.playbackList.header endRefreshing];
                                                                    [weakSelf doPlayFirst];
                                                                }
                                                                else
                                                                {
                                                                    [UIView dd_showMessage:[NSString stringWithFormat:@"error code is %d",(int) error.code] onParentView:self.view];
                                                                    [weakSelf.playbackList.header endRefreshing];
                                                                    [weakSelf.records removeAllObjects];
                                                                    [weakSelf.playbackList reloadData];
                                                                }
                                                            }];
        }
        else
        {
            weakSelf.operation =  [EZOPENSDK searchRecordFileFromCloud:cameraInfo.deviceSerial
                                                              cameraNo:cameraInfo.cameraNo
                                                             beginTime:weakSelf.beginTime
                                                               endTime:weakSelf.endTime
                                                            completion:^(NSArray *cloudRecords, NSError *error) {
                                                                NSLog(@"cloudRecords is %@, error is %@", cloudRecords, error);
                                                                if (error)
                                                                {
                                                                    [UIView dd_showMessage:[NSString stringWithFormat:@"error code is %d",(int) error.code] onParentView:self.view];
                                                                    [weakSelf.playbackList.header endRefreshing];
                                                                    [weakSelf.records removeAllObjects];
                                                                    [weakSelf.playbackList reloadData];
                                                                    return ;
                                                                }
                                                                [weakSelf.records removeAllObjects];
                                                                if(cloudRecords.count == 0)
                                                                {
                                                                    weakSelf.noVideoLabel.hidden = NO;
                                                                    weakSelf.noVideoImageView.hidden = NO;
                                                                    [weakSelf.playbackList reloadData];
                                                                    [weakSelf.playbackList.header endRefreshing];
                                                                    return;
                                                                }
                                                                [weakSelf.records addObjectsFromArray:cloudRecords];
                                                                [weakSelf.playbackList reloadData];
                                                                [weakSelf.playbackList.header endRefreshing];
                                                                [weakSelf doPlayFirst];
                                                            }];
        }
    }];
    self.playbackList.header.automaticallyChangeAlpha = YES;
    [self.playbackList.header beginRefreshing];
}

#pragma mark - Action Methods

- (void)doPlayFirst
{
    if (_isSelectedDevice)
    {
        _deviceRecord = [_records firstObject];
//        EZDeviceRecordFile *firstFile = [_records firstObject];
//        EZDeviceRecordFile *lastFile = [_records lastObject];
//        _deviceRecord = [[EZDeviceRecordFile alloc] init];
//        _deviceRecord.startTime = firstFile.startTime;
//        _deviceRecord.stopTime = lastFile.stopTime;
        [_player startPlaybackFromDevice:_deviceRecord];
    }
    else
    {
        _cloudRecord = [_records lastObject];
        [_player startPlaybackFromCloud:_cloudRecord];
    }
    
    [self duringTimeShow];
    [self.loadingView startSquareClcokwiseAnimation];
    
    _isPlaying = YES;
}

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
    if (self.playbackTimer)
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
        if(_playbackTimer && !_isSelectedDevice)
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
        if(!_isSelectedDevice)
            [self.loadingView startSquareClcokwiseAnimation];
    }
    _isPlaying = !_isPlaying;
}

- (IBAction)cloudButtonClicked:(id)sender
{
    self.selectedIndexPath = nil;
    [UIView animateWithDuration:0.3
                     animations:^{
                         self.selectedImageViewConstraint.constant = 0;
                         [self.toolView setNeedsUpdateConstraints];
                         [self.toolView layoutIfNeeded];
                     }
                     completion:^(BOOL finished) {
                         self.cloudButton.selected = YES;
                         self.deviceButton.selected = NO;
                     }];
    _isSelectedDevice = NO;
    [self.records removeAllObjects];
    [self.playbackList reloadData];
    [self addHeaderRefresh];
}

- (IBAction)deviceButtonClicked:(id)sender
{
    self.selectedIndexPath = nil;
    [UIView animateWithDuration:0.3
                     animations:^{
                         self.selectedImageViewConstraint.constant = self.view.bounds.size.width / 2.0f;
                         [self.toolView setNeedsUpdateConstraints];
                         [self.toolView layoutIfNeeded];
                     }
                     completion:^(BOOL finished) {
                         self.cloudButton.selected = NO;
                         self.deviceButton.selected = YES;
                     }];
    _isSelectedDevice = YES;
    [self.records removeAllObjects];
    [self.playbackList reloadData];
    [self addHeaderRefresh];
}

- (IBAction)cancel:(id)sender
{
    [self.dateTextField resignFirstResponder];
    self.dateButton.selected = NO;
}

- (IBAction)confirm:(id)sender
{
    [self.dateTextField resignFirstResponder];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    dateFormatter.dateFormat = @"MM-dd ▽";
    [self.dateButton setTitle:[dateFormatter stringFromDate:self.datePicker.date] forState:UIControlStateNormal];
    dateFormatter.dateFormat = @"MM-dd △";
    [self.dateButton setTitle:[dateFormatter stringFromDate:self.datePicker.date] forState:UIControlStateSelected];
    self.dateButton.selected = NO;
    
    [self.playbackList.header beginRefreshing];
}

- (IBAction)dateButtonClicked:(id)sender
{
    [self.dateTextField becomeFirstResponder];
    self.datePicker.maximumDate = [NSDate date];
    self.dateButton.selected = YES;
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

- (void)playBoxToolRefresh:(NSTimer *)timer
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
    
    if(_playSeconds >= 3600)
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

#pragma mark - Get Methods

- (NSDate *)beginTime
{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    dateFormatter.dateFormat = @"yyyy-MM-dd";
    NSString *beginTimeString = [NSString stringWithFormat:@"%@ 00:00:00", [dateFormatter stringFromDate:self.datePicker.date]];
    dateFormatter.dateFormat = @"yyyy-MM-dd HH:mm:ss";
    _beginTime = [dateFormatter dateFromString:beginTimeString];
    return _beginTime;
}

- (NSDate *)endTime
{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    dateFormatter.dateFormat = @"yyyy-MM-dd";
    NSString *beginTimeString = [NSString stringWithFormat:@"%@ 23:59:59", [dateFormatter stringFromDate:self.datePicker.date]];
    dateFormatter.dateFormat = @"yyyy-MM-dd HH:mm:ss";
    _endTime = [dateFormatter dateFromString:beginTimeString];
    return _endTime;
}

@end
