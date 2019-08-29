//
//  EZLocalCameraListViewController.m
//  EZOpenSDKDemo
//
//  Created by linyong on 2017/8/16.
//  Copyright © 2017年 hikvision. All rights reserved.
//

#import "EZLocalCameraListViewController.h"
//#import "EZHCNetDeviceInfo.h"
#import "EZLocalRealPlayViewController.h"

#define CAMERA_LIST_ID @"CAMERA_LIST_ID"
#define HEADER_HEIGHT (30)
#define CELL_HEIGHT (50)

@interface EZLocalCameraListViewController ()

@property (nonatomic,assign) NSInteger cameraNo;

@end

@implementation EZLocalCameraListViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.title = NSLocalizedString(@"device_camera_list_title", @"设备通道列表");
    self.tableView.tableFooterView = [UIView new];
    
    // Do any additional setup after loading the view.
}


#pragma mark - override

- (UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskPortrait;
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([[segue destinationViewController] isKindOfClass:[EZLocalRealPlayViewController class]])
    {
        EZLocalRealPlayViewController *VC = (EZLocalRealPlayViewController *)[segue destinationViewController];
        VC.deviceInfo = self.deviceInfo;
        VC.cameraNo = self.cameraNo;
    }
}

#pragma mark - delegate

- (NSInteger) numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;//模拟通道列表和数字通道列表
}

- (CGFloat) tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return HEADER_HEIGHT;
}

- (UIView *) tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    NSString *str = nil;
    if (section == 0)
    {
        str = NSLocalizedString(@"device_analogue_camera", @"模拟通道");
    }
    else
    {
        str = NSLocalizedString(@"device_digital_camera", @"数字通道");
    }
    
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 200, HEADER_HEIGHT)];
    view.backgroundColor = [UIColor colorWithWhite:0.6 alpha:0.7];
    UILabel *title = [[UILabel alloc] initWithFrame:CGRectMake(20, 0, 200, HEADER_HEIGHT)];
    title.text = str;
    title.font = [UIFont boldSystemFontOfSize:14.0];
    title.backgroundColor = [UIColor clearColor];
    
    [view addSubview:title];
    
    return view;
}

- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (!self.deviceInfo)
    {
        return 0;
    }
    
    if (section == 0)
    {
        return self.deviceInfo.channelCount;
    }
    
    return self.deviceInfo.dChannelCount;
}

- (CGFloat) tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return CELL_HEIGHT;
}

- (UITableViewCell *) tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CAMERA_LIST_ID];
    if (!cell)
    {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CAMERA_LIST_ID];
    }
    
    if ([indexPath section] == 0)
    {
        cell.textLabel.text = [NSString stringWithFormat:@"Camera %02ld",self.deviceInfo.startChannelNo + [indexPath row]];
    }
    else
    {
        cell.textLabel.text = [NSString stringWithFormat:@"Camera %02ld",self.deviceInfo.dStartChannelNo + [indexPath row]];
    }
    
    return cell;
}


- (void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([indexPath section] == 0)
    {
        self.cameraNo = self.deviceInfo.startChannelNo + [indexPath row];
    }
    else
    {
        self.cameraNo = self.deviceInfo.dStartChannelNo + [indexPath row];
    }
    
    [self go2LocalRealPlay];
}

#pragma mark - support

- (void) go2LocalRealPlay
{
    if (!self.deviceInfo)
    {
        return;
    }
    
    [self performSegueWithIdentifier:@"cameraList2RealPlay" sender:nil];
}


@end
