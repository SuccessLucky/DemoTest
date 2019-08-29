//
//  EZCameraTableViewController.m
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/10/28.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import "EZDeviceTableViewController.h"

//#import "EZAccessToken.h"
#import "MJRefresh.h"
#import "DeviceListCell.h"
#import "DDKit.h"
#import "EZLivePlayViewController.h"
#import "EZPlaybackViewController.h"
#import "EZMessageListViewController.h"
#import "EZSettingViewController.h"
#import "EZCameraTableViewController.h"
//#import "EZAreaInfo.h"
//#import "EZUserInfo.h"

#define EZDeviceListPageSize 10

@interface EZDeviceTableViewController ()

@property (nonatomic, strong) NSMutableArray *deviceList;
@property (nonatomic) NSInteger currentPageIndex;
@property (nonatomic, strong) IBOutlet UIBarButtonItem *addButton;
@property (nonatomic, weak) IBOutlet UISegmentedControl *segmentedControl;
@property (nonatomic) BOOL isSharedDevice; //是否是分享设备的segmented档选中
@property (nonatomic) NSInteger go2Type;

@end

@implementation EZDeviceTableViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
     [self.navigationController setNavigationBarHidden:NO animated:NO];
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    
    if(!_deviceList)
        _deviceList = [NSMutableArray new];
    
    
    //判断本地保存的accessToken，然后向SDK设置AccessToken。
    if ([EZOPENSDK isLogin])
    {
        [self addRefreshKit];
    }
    else
    {
        self.navigationItem.rightBarButtonItem = nil;
        [EZOPENSDK openLoginPage:^(EZAccessToken *accessToken) {
            [self addRefreshKit];
            self.navigationItem.rightBarButtonItem = self.addButton;
        }];
        return;
    }
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    if (_needRefresh)
    {
        _needRefresh = NO;
        [self.tableView.header beginRefreshing];
    }
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
    return [_deviceList count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    DeviceListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"EZDeviceCell" forIndexPath:indexPath];
    
    // Configure the cell...
    EZDeviceInfo *info = [_deviceList dd_objectAtIndex:indexPath.row];

    cell.isShared = _isSharedDevice;
    [cell setDeviceInfo:info];
    cell.parentViewController = self;
    
    return cell;
}

/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the specified item to be editable.
    return YES;
}

// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
    }
}
*/

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 73.0 * [UIScreen mainScreen].bounds.size.width/320.0f;
}

#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    EZDeviceInfo *deviceInfo = sender;
    if ([[segue destinationViewController] isKindOfClass:[EZLivePlayViewController class]]) {
        ((EZLivePlayViewController *)[segue destinationViewController]).deviceInfo = deviceInfo;
    } else if ([[segue destinationViewController] isKindOfClass:[EZPlaybackViewController class]]) {
        ((EZPlaybackViewController *)[segue destinationViewController]).deviceInfo = deviceInfo;
    } else if ([[segue destinationViewController] isKindOfClass:[EZMessageListViewController class]]) {
        ((EZMessageListViewController *)[segue destinationViewController]).deviceInfo = deviceInfo;
    } else if ([[segue destinationViewController] isKindOfClass:[EZSettingViewController class]]) {
        ((EZSettingViewController *)[segue destinationViewController]).deviceInfo = deviceInfo;
    } else if ([[segue destinationViewController] isKindOfClass:[EZCameraTableViewController class]]) {
        ((EZCameraTableViewController *)[segue destinationViewController]).deviceInfo = deviceInfo;
        ((EZCameraTableViewController *)[segue destinationViewController]).go2Type = _go2Type;
    }
}

- (IBAction)segmentControl:(id)sender {
    _isSharedDevice = NO;
    if (self.segmentedControl.selectedSegmentIndex == 1)
    {
        _isSharedDevice = YES;
    }
    [self addRefreshKit];
}

#pragma mark - Custom Methods

- (void)addRefreshKit
{
    __weak typeof(self) weakSelf = self;
    self.tableView.header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
        weakSelf.currentPageIndex = 0;
        
        if (!_isSharedDevice)
        {
#if 0
            //调试单设备
            [EZOPENSDK getDeviceInfo:@"533467667" completion:^(EZDeviceInfo *deviceInfo, NSError *error) {
                [weakSelf.deviceList removeAllObjects];
                [weakSelf.deviceList addObject:deviceInfo];
                [weakSelf.tableView reloadData];
                [weakSelf.tableView.header endRefreshing];
                [weakSelf.tableView.footer endRefreshingWithNoMoreData];
            }];
#else
            //获取设备列表接口
            [EZOPENSDK getDeviceList:weakSelf.currentPageIndex++
                            pageSize:EZDeviceListPageSize
                          completion:^(NSArray *deviceList, NSInteger totalCount, NSError *error) {
                              if(error)
                              {
                                  [weakSelf.view makeToast:error.description duration:2.0 position:@"bottom"];
                                  return;
                              }
                              [weakSelf.deviceList removeAllObjects];
                              [weakSelf.deviceList addObjectsFromArray:deviceList];
                              [weakSelf.tableView reloadData];
                              [weakSelf.tableView.header endRefreshing];
                              if (weakSelf.deviceList.count == totalCount)
                              {
                                  [weakSelf.tableView.footer endRefreshingWithNoMoreData];
                              }
                              else
                              {
                                  [weakSelf addFooter];
                              }
                          }];
#endif
        } else {
            [EZOPENSDK getSharedDeviceList:weakSelf.currentPageIndex++
                                  pageSize:EZDeviceListPageSize
                                completion:^(NSArray *deviceList, NSInteger totalCount, NSError *error) {
                                    if(error)
                                    {
                                        [weakSelf.view makeToast:error.description duration:2.0 position:@"bottom"];
                                        return;
                                    }
                                    [weakSelf.deviceList removeAllObjects];
                                    [weakSelf.deviceList addObjectsFromArray:deviceList];
                                    [weakSelf.tableView reloadData];
                                    [weakSelf.tableView.header endRefreshing];
                                    if (weakSelf.deviceList.count == totalCount)
                                    {
                                        [weakSelf.tableView.footer endRefreshingWithNoMoreData];
                                    }
                                    else
                                    {
                                        [weakSelf addFooter];
                                    }
                                }];
        }
    }];
    self.tableView.header.automaticallyChangeAlpha = YES;
    [self.tableView.header beginRefreshing];
    
}

- (void)addFooter {
    __weak typeof(self) weakSelf = self;
    self.tableView.footer  = [MJRefreshAutoNormalFooter footerWithRefreshingBlock:^{
        if (!_isSharedDevice)
        {
            //获取设备列表接口
            [EZOPENSDK getDeviceList:weakSelf.currentPageIndex++
                            pageSize:EZDeviceListPageSize
                          completion:^(NSArray *deviceList, NSInteger totalCount, NSError *error) {
                              if(error)
                              {
                                  [weakSelf.view makeToast:error.description duration:2.0 position:@"bottom"];
                                  return;
                              }
                              [weakSelf.deviceList addObjectsFromArray:deviceList];
                              [weakSelf.tableView reloadData];
                              [weakSelf.tableView.footer endRefreshing];
                              if (weakSelf.deviceList.count == totalCount)
                              {
                                  [weakSelf.tableView.footer endRefreshingWithNoMoreData];
                              }
                          }];
        } else {
            [EZOPENSDK getSharedDeviceList:weakSelf.currentPageIndex++
                                  pageSize:EZDeviceListPageSize
                                completion:^(NSArray *deviceList, NSInteger totalCount, NSError *error) {
                                    if(error)
                                    {
                                        [weakSelf.view makeToast:error.description duration:2.0 position:@"bottom"];
                                        return;
                                    }
                                    [weakSelf.deviceList addObjectsFromArray:deviceList];
                                    [weakSelf.tableView reloadData];
                                    [weakSelf.tableView.footer endRefreshing];
                                    if (weakSelf.deviceList.count == totalCount)
                                    {
                                        [weakSelf.tableView.footer endRefreshingWithNoMoreData];
                                    }
                                }];
        }
    }];
}

- (IBAction)go2AddDevice:(id)sender {
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 7.0) {
        UIStoryboard *addDeviceStoryBoard = [UIStoryboard storyboardWithName:@"AddDevice" bundle:nil];
        UIViewController *rootViewController = [addDeviceStoryBoard instantiateViewControllerWithIdentifier:@"AddByQRCode"];
        UIBarButtonItem *returnButton = [[UIBarButtonItem alloc] init];
        returnButton.title = @"";
        self.navigationItem.backBarButtonItem = returnButton;
        [self.navigationController pushViewController:rootViewController animated:YES];
    } else {
        [UIView dd_showMessage:NSLocalizedString(@"device_scan_function_tip", @"iOS 7.0以下扫码功能请自行实现")];
    }
}

- (void)go2Play:(EZDeviceInfo *)deviceInfo {
    _go2Type = 0;
    if (deviceInfo.cameraNum == 1) {
        [self performSegueWithIdentifier:@"go2LivePlay" sender:deviceInfo];
    } else if(deviceInfo.cameraNum > 1) {
        [self performSegueWithIdentifier:@"go2CameraList" sender:deviceInfo];
    }
}

- (void)go2Record:(EZDeviceInfo *)deviceInfo
{
    _go2Type = 1;
    if (deviceInfo.cameraNum == 1) {
        [self performSegueWithIdentifier:@"go2Playback" sender:deviceInfo];
    } else if(deviceInfo.cameraNum > 1) {
        [self performSegueWithIdentifier:@"go2CameraList" sender:deviceInfo];
    }
}

- (void)go2Setting:(EZDeviceInfo *)deviceInfo
{
    [self performSegueWithIdentifier:@"go2Setting" sender:deviceInfo];
}

- (void)go2Message:(EZDeviceInfo *)deviceInfo
{
    [self performSegueWithIdentifier:@"go2MessageList" sender:deviceInfo];
}

@end
