//
//  EZMessagePhotoViewController.m
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/11/16.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import "EZMessagePhotoViewController.h"
#import "EZMessagePlaybackViewController.h"

@interface EZMessagePhotoViewController ()<MWPhotoBrowserDelegate>

@property (nonatomic, weak) IBOutlet UIView *messageDetailInfoView;
@property (nonatomic, weak) IBOutlet UILabel *titleLabel;
@property (nonatomic, weak) IBOutlet UILabel *timeLabel;
@property (nonatomic, weak) IBOutlet UILabel *contentLabel;
@property (nonatomic, weak) IBOutlet UIButton *recordButton;

@end

@implementation EZMessagePhotoViewController

- (void)viewDidLoad {
    
    self.delegate = self;
    self.displayActionButton = NO;
    self.displaySelectionButtons = NO;
    
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self.view bringSubviewToFront:self.messageDetailInfoView];
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    formatter.dateFormat = @"yyyy/MM/dd HH:mm:ss";
    self.timeLabel.text = [formatter stringFromDate:self.info.alarmStartTime];
    self.contentLabel.text = [NSString stringWithFormat:@"%@:%@",NSLocalizedString(@"message_from", @"来自"),self.info.alarmName];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    EZAlarmInfo *info = sender;
    
    EZMessagePlaybackViewController *messagePlaybackVC = [segue destinationViewController];
    messagePlaybackVC.info = info;
    messagePlaybackVC.deviceInfo = self.deviceInfo;
    
}

#pragma mark - MWPhotoBrowserDelegate Methods

- (NSUInteger)numberOfPhotosInPhotoBrowser:(MWPhotoBrowser *)photoBrowser
{
    return 1;
}

- (id <MWPhoto>)photoBrowser:(MWPhotoBrowser *)photoBrowser photoAtIndex:(NSUInteger)index
{
    MWPhoto *photo = [[MWPhoto alloc] initWithImage:_image];
    return photo;
}


- (void)updateNavigation
{
    self.title = NSLocalizedString(@"message_detail", @"消息详情");
}

- (IBAction)go2Next:(id)sender
{
    [self performSegueWithIdentifier:@"go2MessagePlayback" sender:self.info];
}

@end
