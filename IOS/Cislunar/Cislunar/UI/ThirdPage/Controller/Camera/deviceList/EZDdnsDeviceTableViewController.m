//
//  EZDdnsDeviceTableViewController.m
//  EZOpenSDKDemo
//
//  Created by linyong on 2017/9/11.
//  Copyright © 2017年 hikvision. All rights reserved.
//

#import "EZDdnsDeviceTableViewController.h"
#import "EZLocalRealPlayViewController.h"
#import "EZLocalCameraListViewController.h"
//#import "EZHiddnsDeviceInfo.h"
//#import "EZHCNetDeviceSDK.h"
//#import "EZHCNetDeviceInfo.h"
//#import "EZAccessToken.h"
//#import "EZAreaInfo.h"
#import "MJRefresh.h"
//#import "Toast+UIView.h"
#import "MBProgressHUD.h"
#import "DDKit.h"

#define DEVICE_LIST_ID @"ddnsDeviceList"
#define CELL_HEIGHT (60)
#define PAGE_SIZE (10)


@interface EZDdnsDeviceTableViewController ()

@property (nonatomic,assign) BOOL isSharedDevice;
@property (nonatomic,assign) NSInteger currentPageIndex;
@property (nonatomic,strong) NSMutableArray *deviceList;
@property (weak, nonatomic) IBOutlet UISegmentedControl *segment;
@property (weak, nonatomic) IBOutlet UIBarButtonItem *addButton;
@property (weak, nonatomic) IBOutlet UISegmentedControl *segmentedControl;
@property (nonatomic,strong) EZHiddnsDeviceInfo *ddnsDeviceInfo;
@property (nonatomic,strong) EZHCNetDeviceInfo *loginedInfo;

@end

@implementation EZDdnsDeviceTableViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [self initViews];
    
    [self initData];
    
    [self loginProcess];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    if (self.needRefresh)
    {
        self.needRefresh = NO;
        [self.tableView.header beginRefreshing];
    }
}


#pragma mark - action

- (IBAction)segmentControl:(id)sender
{
    self.isSharedDevice = NO;
    if (self.segmentedControl.selectedSegmentIndex == 1)
    {
        self.isSharedDevice = YES;
    }
    [self addTopRefresh];
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


#pragma mark - Table view data source

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.deviceList.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:DEVICE_LIST_ID];
    if (!cell)
    {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:DEVICE_LIST_ID];
    }
    
    EZHiddnsDeviceInfo *info = [self.deviceList objectAtIndex:[indexPath row]];
    cell.textLabel.text = info.deviceName;
    cell.textLabel.font = [UIFont boldSystemFontOfSize:13.0];
    cell.detailTextLabel.text = info.deviceIp;
    
    return cell;
}

- (void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    EZHiddnsDeviceInfo *info = [self.deviceList objectAtIndex:[indexPath row]];

    [self loginWithDevice:info];
}

#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([[segue destinationViewController] isKindOfClass:[EZLocalRealPlayViewController class]])
    {
        EZHCNetDeviceInfo *deviceInfo = sender;
        EZLocalRealPlayViewController *VC = (EZLocalRealPlayViewController *)[segue destinationViewController];
        VC.deviceInfo = deviceInfo;
        VC.cameraNo = deviceInfo.channelCount == 1?deviceInfo.startChannelNo:deviceInfo.dStartChannelNo;
    }
    else if ([[segue destinationViewController] isKindOfClass:[EZLocalCameraListViewController class]])
    {
        EZHCNetDeviceInfo *deviceInfo = sender;
        EZLocalCameraListViewController *VC = (EZLocalCameraListViewController *)[segue destinationViewController];
        VC.deviceInfo = deviceInfo;
    }
}

#pragma mark - support

- (void) initData
{
    self.deviceList = [NSMutableArray array];
    self.currentPageIndex = 0;
    self.isSharedDevice = NO;
    self.needRefresh = NO;
}

- (void) loginProcess
{
    //判断本地保存的accessToken，然后向SDK设置AccessToken。
    if ([EZOPENSDK isLogin])
    {
        [self addTopRefresh];
    }
    else
    {
        self.navigationItem.rightBarButtonItem = nil;
        [EZOPENSDK getAreaList:^(NSArray *areaList, NSError *error) {
            EZAreaInfo *areaInfo = areaList.firstObject;
            [EZOPENSDK openLoginPage:[NSString stringWithFormat:@"%ld",(long)areaInfo.id]
                          completion:^(EZAccessToken *accessToken) {
                              [self addTopRefresh];
                          }];
        }];

        return;
    }
}


- (void) loginWithDevice:(EZHiddnsDeviceInfo *) deviceInfo
{
    if (!deviceInfo)
    {
        return;
    }
    
    self.loginedInfo = nil;
    UIAlertController *alertVC = [UIAlertController alertControllerWithTitle:NSLocalizedString(@"device_login_device", @"登录设备")
                                                                     message:NSLocalizedString(@"device_input_account_pw", @"请输入帐号和密码")
                                                              preferredStyle:UIAlertControllerStyleAlert];
    
    [alertVC addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
        textField.text = @"admin";
        textField.placeholder = NSLocalizedString(@"device_account", @"帐号");
        textField.keyboardType = UIKeyboardTypeASCIICapable;
    }];
    
    [alertVC addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
        textField.secureTextEntry = YES;
        textField.placeholder = NSLocalizedString(@"device_password", @"密码");
    }];
    UIAlertAction *actionCancel = [UIAlertAction actionWithTitle:NSLocalizedString(@"cancel", @"取消")
                                                           style:UIAlertActionStyleCancel
                                                         handler:^(UIAlertAction * _Nonnull action) {
                                                             
                                                         }];
    
    UIAlertAction *actionDone = [UIAlertAction actionWithTitle:NSLocalizedString(@"done", @"确定")
                                                         style:UIAlertActionStyleDefault
                                                       handler:^(UIAlertAction * _Nonnull action) {
                                                           UITextField *nameInput = [alertVC.textFields firstObject];
                                                           UITextField *pwdInput = [alertVC.textFields lastObject];
                                                           
                                                           if (nameInput.text == 0 || pwdInput.text.length == 0)
                                                           {
                                                               [self showToastWithStr:NSLocalizedString(@"device_account_pw_empty", @"帐号或密码不能为空")];
                                                               return;
                                                           }
                                                           
                                                           [self doLoginWithInfo:deviceInfo
                                                                        userName:nameInput.text
                                                                             pwd:pwdInput.text];
                                                       }];
    [alertVC addAction:actionDone];
    [alertVC addAction:actionCancel];
    
    [self presentViewController:alertVC animated:YES completion:^{
        UITextField *pwdInput = [alertVC.textFields lastObject];
        [pwdInput becomeFirstResponder];
    }];
}



- (void) doLoginWithInfo:(EZHiddnsDeviceInfo *) deviceInfo userName:(NSString *) userName pwd:(NSString *) pwd
{
    if (!deviceInfo || !userName || !pwd)
    {
        return;
    }
    
    [MBProgressHUD showHUDAddedTo:self.navigationController.view animated:YES];
    
    //根据映射模式进行端口选择
    NSInteger cmdPort = deviceInfo.upnpMappingMode == 1?deviceInfo.mappingHiddnsCmdPort:deviceInfo.hiddnsCmdPort;
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        //登录可能为耗时处理过程，可考虑异步处理
        self.loginedInfo = [EZHCNetDeviceSDK loginDeviceWithUerName:userName
                                                                pwd:pwd
                                                             ipAddr:deviceInfo.deviceIp
                                                               port:cmdPort];
        dispatch_async(dispatch_get_main_queue(), ^{
            
            [MBProgressHUD hideHUDForView:self.navigationController.view animated:YES];
            
            if (self.loginedInfo)
            {
                if (self.loginedInfo.channelCount + self.loginedInfo.dChannelCount > 1)
                {
                    [self go2CameraListWithInfo:self.loginedInfo];
                }
                else
                {
                    [self go2RealPlayWithInfo:self.loginedInfo];
                }
            }
            else
            {
                [self showToastWithStr:NSLocalizedString(@"device_login_fail", @"登录失败")];
            }
        });
    });
}


#pragma mark - view

- (void) initViews
{
    self.navigationItem.rightBarButtonItem = self.addButton;
    self.tableView.tableFooterView = [UIView new];
    [self.segment setTitle:NSLocalizedString(@"device_mine", @"我的") forSegmentAtIndex:0];
    [self.segment setTitle:NSLocalizedString(@"device_shared", @"分享") forSegmentAtIndex:1];
}


- (void) addTopRefresh
{
    __weak typeof(self) weakSelf = self;
    self.tableView.header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
        weakSelf.currentPageIndex = 0;
        
        if (!self.isSharedDevice)
        {
            [EZOPENSDK getHiddnsDeviceList:weakSelf.currentPageIndex
                                  pageSize:PAGE_SIZE
                                completion:^(NSArray *ddnsDeviceList, NSInteger totalCount, NSError *error) {
                                    [weakSelf.deviceList removeAllObjects];
                                    [weakSelf.deviceList addObjectsFromArray:ddnsDeviceList];
                                    [weakSelf.tableView reloadData];
                                    [weakSelf.tableView.header endRefreshing];
                                    if (weakSelf.deviceList.count == totalCount)
                                    {
                                        [weakSelf.tableView.footer endRefreshingWithNoMoreData];
                                    }
                                    else
                                    {
                                        [weakSelf addBottomRefresh];
                                    }
                                }];
        }
        else
        {
            [EZOPENSDK getShareHiddnsDeviceList:weakSelf.currentPageIndex
                                       pageSize:PAGE_SIZE
                                     completion:^(NSArray *ddnsDeviceList, NSInteger totalCount, NSError *error) {
                                         [weakSelf.deviceList removeAllObjects];
                                         [weakSelf.deviceList addObjectsFromArray:ddnsDeviceList];
                                         [weakSelf.tableView reloadData];
                                         [weakSelf.tableView.header endRefreshing];
                                         if (weakSelf.deviceList.count == totalCount)
                                         {
                                             [weakSelf.tableView.footer endRefreshingWithNoMoreData];
                                         }
                                         else
                                         {
                                             [weakSelf addBottomRefresh];
                                         }
                                     }];
        }
    }];
    self.tableView.header.automaticallyChangeAlpha = YES;
    [self.tableView.header beginRefreshing];
    
}

- (void)addBottomRefresh
{
    __weak typeof(self) weakSelf = self;
    self.tableView.footer  = [MJRefreshAutoNormalFooter footerWithRefreshingBlock:^{
        if (!weakSelf.isSharedDevice)
        {
            //获取设备列表接口
            [EZOPENSDK getHiddnsDeviceList:weakSelf.currentPageIndex++
                                  pageSize:PAGE_SIZE
                                completion:^(NSArray *ddnsDeviceList, NSInteger totalCount, NSError *error) {
                                    [weakSelf.deviceList addObjectsFromArray:ddnsDeviceList];
                                    [weakSelf.tableView reloadData];
                                    [weakSelf.tableView.footer endRefreshing];
                                    if (weakSelf.deviceList.count == totalCount)
                                    {
                                        [weakSelf.tableView.footer endRefreshingWithNoMoreData];
                                    }
                                }];
        }
        else
        {
            [EZOPENSDK getShareHiddnsDeviceList:weakSelf.currentPageIndex++
                                       pageSize:PAGE_SIZE
                                     completion:^(NSArray *ddnsDeviceList, NSInteger totalCount, NSError *error) {
                                         [weakSelf.deviceList addObjectsFromArray:ddnsDeviceList];
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

- (void) showToastWithStr:(NSString *) str
{
    if (!str)
    {
        return;
    }
    [self.navigationController.view makeToast:str duration:1.5 position:@"center"];
}

- (void) go2RealPlayWithInfo:(EZHCNetDeviceInfo *) deviceInfo
{
    if (!deviceInfo)
    {
        return;
    }
    
    [self performSegueWithIdentifier:@"go2RealPlayFromDdns" sender:deviceInfo];
}

- (void) go2CameraListWithInfo:(EZHCNetDeviceInfo *) deviceInfo
{
    if (!deviceInfo)
    {
        return;
    }
    
    [self performSegueWithIdentifier:@"go2CameraListFromDdns" sender:deviceInfo];
}



@end
