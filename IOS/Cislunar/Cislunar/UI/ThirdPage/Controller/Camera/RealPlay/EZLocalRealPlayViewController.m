//
//  EZLocalRealPlayViewController.m
//  EZOpenSDKDemo
//
//  Created by linyong on 2017/8/16.
//  Copyright © 2017年 hikvision. All rights reserved.
//

#import "EZLocalRealPlayViewController.h"
//#import "EZPlayer.h"
//#import "EZHCNetDeviceSDK.h"
//#import "EZHCNetDeviceInfo.h"

@interface EZLocalRealPlayViewController () <EZPlayerDelegate>

@property (weak, nonatomic) IBOutlet UIView *playerView;
@property (weak, nonatomic) IBOutlet UIButton *ptzBgBtn;
@property (weak, nonatomic) IBOutlet UIButton *ptzUpBtn;
@property (weak, nonatomic) IBOutlet UIButton *ptzDownBtn;
@property (weak, nonatomic) IBOutlet UIButton *ptzRightBtn;
@property (weak, nonatomic) IBOutlet UIButton *ptzLeftBtn;
@property (nonatomic,strong) EZPlayer *player;

@end

@implementation EZLocalRealPlayViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.title = NSLocalizedString(@"device_lan_preview_title", @"局域网预览");
    
    [self startRealPlay];
    // Do any additional setup after loading the view.
}

- (void) viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    [self.player stopRealPlay];
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskPortrait;
}

#pragma mark - ptz action

- (IBAction)ptzStart:(id)sender
{
    EZPTZCommandType command;
    NSString *imageName = nil;
    if(sender == self.ptzLeftBtn)
    {
        command = EZPTZCommandType_LEFT;
        imageName = @"ptz_left_sel";
    }
    else if (sender == self.ptzDownBtn)
    {
        command = EZPTZCommandType_DOWN;
        imageName = @"ptz_bottom_sel";
    }
    else if (sender == self.ptzRightBtn)
    {
        command = EZPTZCommandType_RIGHT;
        imageName = @"ptz_right_sel";
    }
    else {
        command = EZPTZCommandType_UP;
        imageName = @"ptz_up_sel";
    }
    
    [self.ptzBgBtn setImage:[UIImage imageNamed:imageName] forState:UIControlStateDisabled];
    [self ptzWithCommand:command action:EZPTZActionType_START];
}

- (IBAction)ptzStop:(id)sender
{
    EZPTZCommandType command;
    if(sender == self.ptzLeftBtn)
    {
        command = EZPTZCommandType_LEFT;
    }
    else if (sender == self.ptzDownBtn)
    {
        command = EZPTZCommandType_DOWN;
    }
    else if (sender == self.ptzRightBtn)
    {
        command = EZPTZCommandType_RIGHT;
    }
    else {
        command = EZPTZCommandType_UP;
    }
    [self.ptzBgBtn setImage:[UIImage imageNamed:@"ptz_bg"] forState:UIControlStateDisabled];
    [self ptzWithCommand:command action:EZPTZActionType_STOP];
}

#pragma mark - player delegate

- (void)player:(EZPlayer *)player didPlayFailed:(NSError *)error
{
    NSLog(@"local player error :%@",error);
}

- (void)player:(EZPlayer *)player didReceivedMessage:(NSInteger)messageCode
{

}

#pragma mark - support

- (void) startRealPlay
{
    self.player = [EZPlayer createPlayerWithUserId:self.deviceInfo.userId cameraNo:self.cameraNo streamType:1];
    
    [self.player setPlayerView:self.playerView];
    self.player.delegate = self;
    
    [self.player startRealPlay];
}

- (void) ptzWithCommand:(EZPTZCommandType) command action:(EZPTZActionType) action
{
    [EZHCNetDeviceSDK ptzControlWithUserId:self.deviceInfo.userId channelNo:self.cameraNo command:command action:action];
}

@end
