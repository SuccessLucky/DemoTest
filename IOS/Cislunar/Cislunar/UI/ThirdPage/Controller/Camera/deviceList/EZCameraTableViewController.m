//
//  EZCameraTableViewController.m
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 16/9/20.
//  Copyright © 2016年 hikvision. All rights reserved.
//

#import "EZCameraTableViewController.h"
#import "EZLivePlayViewController.h"
#import "EZPlaybackViewController.h"
//#import "EZCameraInfo.h"
#import "DDKit.h"

@interface EZCameraTableViewController ()

@property (nonatomic, strong) NSMutableArray *cameraList;
@property (nonatomic) NSInteger cameraIndex;

@end

@implementation EZCameraTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.title = NSLocalizedString(@"device_camera_list_title", @"设备通道列表");

    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    if (!_cameraList) {
        _cameraList = [NSMutableArray new];
    }
    [_cameraList addObjectsFromArray:self.deviceInfo.cameraInfo];
    [self.tableView reloadData];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [_cameraList count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"EZCameraCell" forIndexPath:indexPath];
    
    // Configure the cell...
    EZCameraInfo *info = [_cameraList dd_objectAtIndex:indexPath.row];
    
    cell.textLabel.text = [NSString stringWithFormat:@"%@_%d", info.deviceSerial, (int)info.cameraNo];
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    _cameraIndex = indexPath.row;
    if (_go2Type == 1) {
        [self performSegueWithIdentifier:@"go2Playback" sender:self.deviceInfo];
    } else {
        [self performSegueWithIdentifier:@"go2LivePlay" sender:self.deviceInfo];
    }
}

/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    } else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath {
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    EZDeviceInfo *deviceInfo = sender;
    if ([[segue destinationViewController] isKindOfClass:[EZLivePlayViewController class]]) {
        ((EZLivePlayViewController *)[segue destinationViewController]).deviceInfo = deviceInfo;
        ((EZLivePlayViewController *)[segue destinationViewController]).cameraIndex = _cameraIndex;
    } else if ([[segue destinationViewController] isKindOfClass:[EZPlaybackViewController class]]) {
        ((EZPlaybackViewController *)[segue destinationViewController]).deviceInfo = deviceInfo;
        ((EZPlaybackViewController *)[segue destinationViewController]).cameraIndex = _cameraIndex;
    }
}

@end
