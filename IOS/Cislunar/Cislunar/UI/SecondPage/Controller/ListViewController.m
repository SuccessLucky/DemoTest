//
//  ListViewController.m
//  JXCategoryView
//
//  Created by jiaxin on 2018/8/8.
//  Copyright © 2018年 jiaxin. All rights reserved.
//

#import "ListViewController.h"
#import "DeviceCV.h"
#import "SHDeviceManager.h"
#import "SHLockManager.h"
#import "LockViewController.h"
#import "SHLockSetPswVC.h"
#import "SHColourBulbVC.h"
#import "SHControlBoxVC.h"
#import "AirConditionVC.h"
#import "InfraredDeviceViewController.h"
#import "WindowTransmitViewController.h"
#import "XFNewViewController.h"
#import "SHWaterPurifierVC.h"

@interface ListViewController ()

@property (strong, nonatomic) DeviceCV *deviceCV;
@property (strong, nonatomic) SHDeviceManager *manager;
@property (assign, nonatomic) NetworkEngine *networkEngine;
//获取指纹锁密码列表
@property (strong, nonatomic) SHLockManager *lockManager;
@property (strong, nonatomic) SHModelDevice *deviceRemberForLock;


@end

@implementation ListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self doInitSubViews];
    [self doRegisterKVO];
    [self doAddAction];
//    [self doAskAll4010Device];
    // Do any additional setup after loading the view.
}

- (void)doInitSubViews
{
    self.networkEngine = [NetworkEngine shareInstance];
    [self.view addSubview:self.deviceCV];
    [self doAddScreenCollectionViewConstraints];
}

#pragma mark -
#pragma mark - 询问所有4010设备的状态
- (void)doAskAll4010Device
{
//    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
    NSData *data = [[NetworkEngine shareInstance] doGetRefreshDeviceStatue];
    [[NetworkEngine shareInstance] sendRequestData:data];
}

#pragma mark -
#pragma mark - loadData
- (void)setModelRoom:(SHModelRoom *)modelRoom
{
    _modelRoom = modelRoom;
    
}

#pragma mark -
#pragma mark - 数据加载
- (void)loadDataForFirst {
    
    //第一次才加载，后续触发的不处理
//    if (!self.isDataLoaded) {
//        //为什么要手动设置contentoffset：https://stackoverflow.com/questions/14718850/uirefreshcontrol-beginrefreshing-not-working-when-uitableviewcontroller-is-ins
//        [self.tableView setContentOffset:CGPointMake(0, -self.refreshControl.bounds.size.height) animated:YES];
//        [self headerRefresh];
//        self.isDataLoaded = YES;
//    }
}

#pragma mark - 获取到房间下面的设备列表
- (void)doLoadInRoomDeviceListDataWithRoomID:(int)iRoomID
{
    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
    [self.manager doGetDeviceListDataFromDBWithRoomID:iRoomID];
    [self.manager doGetDeviceListFromNetworkWithRoomID:iRoomID];
}

#pragma mark -
#pragma mark - 注册kvo
- (void)doRegisterKVO
{
    @weakify(self);
    [self observeKeyPath:@keypath(self.manager.arrDeviceList)
                   block:^(id value) {
                       @strongify(self);
                       [XWHUDManager hideInWindow];
                       NSArray *arr = (NSArray *)value;
                       if (arr.count) {
                           self.deviceCV.hidden = NO;
                           [self doHanleAtOnceAddDeviceSucceedToRefreshList:arr];
                       }else{
                           self.deviceCV.hidden = YES;
                           [XWHUDManager showWarningTipHUD:@"该房间暂无设备"];
                       }
                   }];
    
    [self observeKeyPath:@keypath(self.manager.errorInfo)
                   block:^(id value) {
                       [XWHUDManager hideInWindow];
                       NSDictionary *dict = (NSDictionary *)value;
                       [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",dict[@"message"]]];
                   }];
    
    //监测设备上报添加
    [self observeKeyPath:@keypath(self.networkEngine.modelDevice) block:^(id value) {
        @strongify(self);
//        [XWHUDManager hideInWindow];
        SHModelDevice *deviceReport = (SHModelDevice *)value;
        // NSLog(@"处于普通上报刷新状态");
        
        NSMutableArray *mutArrDataOrigin = [NSMutableArray arrayWithArray:self.deviceCV.arrDataList];
        
        for (int i = 0; i < mutArrDataOrigin.count; i ++) {
            SHModelDevice *modelDevice = mutArrDataOrigin[i];
            if ([modelDevice.strDevice_mac_address isEqualToString:deviceReport.strDevice_mac_address]) {
                
                SHModelDevice *deviceNew        = [SHModelDevice new];
                deviceNew.iDevice_device_id     = modelDevice.iDevice_device_id;
                deviceNew.iDevice_room_id       = modelDevice.iDevice_room_id;
                deviceNew.strDevice_room_name   = modelDevice.strDevice_room_name;
                deviceNew.strDevice_floor_name  = modelDevice.strDevice_floor_name;
                deviceNew.strDevice_device_name = modelDevice.strDevice_device_name;
                deviceNew.strDevice_image       = modelDevice.strDevice_image;
                
                deviceNew.strDevice_device_OD   = modelDevice.strDevice_device_OD;
                deviceNew.strDevice_device_type = modelDevice.strDevice_device_type;
                deviceNew.strDevice_category    = modelDevice.strDevice_category;
                deviceNew.strDevice_cmdId       = modelDevice.strDevice_cmdId;
                
                deviceNew.strDevice_sindex      = modelDevice.strDevice_sindex;
                deviceNew.strDevice_sindex_length = modelDevice.strDevice_sindex_length;
                deviceNew.strDevice_mac_address = modelDevice.strDevice_mac_address;
                deviceNew.arrBtns = modelDevice.arrBtns;
                
                if ([modelDevice.strDevice_device_OD isEqualToString:@"0F AA"]) {
                    
                    deviceNew.iDevice_device_state1 = deviceReport.iDevice_device_state1;
                    deviceNew.iDevice_device_state2 = deviceReport.iDevice_device_state2;
                    deviceNew.iDevice_device_state3 = deviceReport.iDevice_device_state3;
                    deviceNew.strDevice_other_status = modelDevice.strDevice_other_status;
                    
                }else if ([modelDevice.strDevice_device_OD isEqualToString:@"0F E6"]){
                    
                    deviceNew.iDevice_device_state1 = modelDevice.iDevice_device_state1;
                    
                    deviceNew.iDevice_device_state2 = modelDevice.iDevice_device_state2;
                    deviceNew.iDevice_device_state3 = modelDevice.iDevice_device_state3;
                    
                    if ([modelDevice.strDevice_device_type isEqualToString:@"02"]) {
                        if ([modelDevice.strDevice_category isEqualToString:@"02"]) {
                            deviceNew.strDevice_other_status =modelDevice.strDevice_other_status;
                        }else{
                            deviceNew.strDevice_other_status =deviceReport.strDevice_other_status;
                        }
                    }else{
                        deviceNew.strDevice_other_status =deviceReport.strDevice_other_status;
                    }
                    
                    
                    
                } else if ([modelDevice.strDevice_device_OD isEqualToString:@"0F BE"]){
                    
                    deviceNew.iDevice_device_state1 = deviceReport.iDevice_device_state1;
                    deviceNew.iDevice_device_state2 = deviceReport.iDevice_device_state2;
                    deviceNew.iDevice_device_state3 = modelDevice.iDevice_device_state3;
                    deviceNew.strDevice_other_status =modelDevice.strDevice_other_status;
                    
                }else{
                    deviceNew.iDevice_device_state1 = deviceReport.iDevice_device_state1;
                    deviceNew.iDevice_device_state2 = deviceReport.iDevice_device_state2;
                    deviceNew.iDevice_device_state3 = deviceReport.iDevice_device_state3;
                    deviceNew.strDevice_other_status =deviceReport.strDevice_other_status;
                    
                    deviceNew.strDCVReal                = deviceReport.strDCVReal;
                    deviceNew.strCurrentReal            = deviceReport.strCurrentReal;
                    deviceNew.strUsefulPowerReal        = deviceReport.strUsefulPowerReal;
                    deviceNew.strElectricQuantityReal   = deviceReport.strElectricQuantityReal;
                    
                }
                
                [mutArrDataOrigin replaceObjectAtIndex:i withObject:deviceNew];
                
            }else{
            }
        }
        
        self.deviceCV.arrDataList = mutArrDataOrigin;
    }];
    
    //指纹锁密码列表
    [self observeKeyPath:@keypath(self.lockManager.arrLockPswList)
                   block:^(id value) {
                       @strongify(self);
                       [XWHUDManager hideInWindow];
                       SecondPageVC *secondVC = [self viewControllerSupportView:self.view];
                       NSArray *arr = (NSArray *)value;
                       if (arr.count) {
                           SHLockPswModel *pswModel = arr[0];
                           UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                           LockViewController *VC = (LockViewController *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"LockViewController"];
                           VC.strPsw = pswModel.strUnlockPsw;
                           VC.iDeviceID = pswModel.iDeviceID;
                           VC.strMacAddr = self.deviceRemberForLock.strDevice_mac_address;
                           VC.itype = 0;
                           VC.deviceTransmit = self.deviceRemberForLock;
                           secondVC.hidesBottomBarWhenPushed = YES;
                           [secondVC.navigationController pushViewController:VC animated:YES];
                       }else{
                           
                           UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                           SHLockSetPswVC *VC = (SHLockSetPswVC *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"SHLockSetPswVC"];
                           VC.iDeviceID = self.deviceRemberForLock.iDevice_device_id;
                           VC.strMacAddr = self.deviceRemberForLock.strDevice_mac_address;
                           VC.deviceTransmit = self.deviceRemberForLock;
                           VC.itype = 3;
                           secondVC.hidesBottomBarWhenPushed = YES;
                           [secondVC.navigationController pushViewController:VC animated:YES];
                       }
                   }];
    
    [self observeKeyPath:@keypath(self.lockManager.getLockPswListErrorInfo)
                   block:^(id value) {
                       [XWHUDManager hideInWindow];
                       NSDictionary *dict = (NSDictionary *)value;
                       [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",dict[@"message"]]];
                   }];
}


#pragma mark -
#pragma mark - 初始化

-(SHLockManager *)lockManager
{
    if (!_lockManager) {
        _lockManager = [SHLockManager new];
    }
    return _lockManager;
}

- (SHDeviceManager *)manager
{
    if (!_manager) {
        _manager = [SHDeviceManager new];
    }
    return _manager;
}

- (DeviceCV *)deviceCV
{
    if (!_deviceCV) {
        
        UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
        flowLayout.sectionHeadersPinToVisibleBounds = YES;
        [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
        flowLayout.itemSize = CGSizeMake(kDCVItemWidth, kDCVItemHeight);
        flowLayout.minimumLineSpacing = kDCVMinimumLineSpacing;
        flowLayout.minimumInteritemSpacing = kDCVMinimumInteritemSpacing;
        flowLayout.sectionInset = UIEdgeInsetsMake(kDCVFromTop, kDCVFromLeft,kDCVFromBottom, kDCVFromRight);
        
        flowLayout.headerReferenceSize = CGSizeMake(UI_SCREEN_WIDTH, 0);
        flowLayout.footerReferenceSize = CGSizeMake(UI_SCREEN_WIDTH, 0);
        
        _deviceCV = [[DeviceCV alloc] initWithFrame:CGRectMake(0,
                                                               0,
                                                               self.view.frame.size.width,
                                                               self.view.frame.size.height)
                               collectionViewLayout:flowLayout
                                               type:CommonCollectionViewType_Common];
    }
    return _deviceCV;
}

- (void)doAddScreenCollectionViewConstraints
{
    @weakify(self);
    [self.deviceCV mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.top.equalTo(self.view.mas_top);
        make.left.equalTo(self.view.mas_left);
        make.right.equalTo(self.view.mas_right);
        make.bottom.equalTo(self.view.mas_bottom);
    }];
}

#pragma mark -
#pragma mark - add action
- (void)doAddAction
{
    @weakify(self);
    [self.deviceCV setDidSelectedDeviceItemBlock:^(NSIndexPath *indexPath, CommonCollectionViewActionType type, SHModelDevice *device) {
        @strongify(self);
        
        SecondPageVC *secondVC = [self viewControllerSupportView:self.view];
        
        if (type == CommonCollectionViewActionType_Control) {
            if ([device.strDevice_device_OD isEqualToString:@"0F AA"]) {
                
                if ([device.strDevice_device_type isEqualToString:@"C1"] || [device.strDevice_device_type isEqualToString:@"c1"]) {
                    if ([device.strDevice_category isEqualToString:@"02"]) {
                        NSLog(@"六路面板");
                    }
                    
                }else if ([device.strDevice_device_type isEqualToString:@"8A"] || [device.strDevice_device_type isEqualToString:@"8a"]) {
                    if ([device.strDevice_category isEqualToString:@"02"]) {
                        NSLog(@"场景控制器");
                    }
                    
                }else if ([device.strDevice_device_type isEqualToString:@"81"]) {
                    if ([device.strDevice_category isEqualToString:@"02"]) {
                        NSLog(@"人体热释传感器");
                    }else if ([device.strDevice_category isEqualToString:@"03"]){
                        
                        NSLog(@"一氧化碳检测");
                    }else if ([device.strDevice_category isEqualToString:@"04"]){
                        
                        NSLog(@"烟雾传感器");
                    }
                    [self doHandleControlDevice:device];
                    
                }else if ([device.strDevice_device_type isEqualToString:@"0E"] || [device.strDevice_device_type isEqualToString:@"0e"]){
                    
                    if ([device.strDevice_category isEqualToString:@"02"]) {
                        NSLog(@"多彩冷暖灯");
                    }
                    
                }else if ([device.strDevice_device_type isEqualToString:@"0B"] || [device.strDevice_device_type isEqualToString:@"0b"]){
                    if ([device.strDevice_category isEqualToString:@"02"]) {
                        NSLog(@"多彩球泡灯");
                        UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                        SHColourBulbVC *VC = (SHColourBulbVC *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"SHColourBulbVC"];
                        VC.device = device;
                        secondVC.hidesBottomBarWhenPushed = YES;
                        [secondVC.navigationController pushViewController:VC animated:YES];
                    }
                    
                }else if ([device.strDevice_device_type isEqualToString:@"09"]){
                    if ([device.strDevice_category isEqualToString:@"02"]) {
                        NSLog(@"声光报警器");
                        [self doHandleControlDevice:device];
                    }
                    
                }else if ([device.strDevice_device_type isEqualToString:@"07"]){
                    if ([device.strDevice_category isEqualToString:@"02"]) {
                        NSLog(@"三路灯开关");
                        [self doHandleControlDevice:device];
                    } else if ([device.strDevice_category isEqualToString:@"04"]) {
                        NSLog(@"多联三路灯开关");
                        [self doHandleControlDevice:device];
                    }
                    
                }else if ([device.strDevice_device_type isEqualToString:@"06"]){
                    if ([device.strDevice_category isEqualToString:@"02"]) {
                        NSLog(@"二路灯开关");
                        [self doHandleControlDevice:device];
                    }else if ([device.strDevice_category isEqualToString:@"04"]) {
                        NSLog(@"多联二路灯开关");
                        [self doHandleControlDevice:device];
                    }
                    
                }else if ([device.strDevice_device_type isEqualToString:@"05"]){
                    if ([device.strDevice_category isEqualToString:@"02"]) {
                        NSLog(@"一路灯开关");
                    }else if ([device.strDevice_category isEqualToString:@"04"]) {
                        NSLog(@"多联一路灯开关");
                    }
                    else if ([device.strDevice_category isEqualToString:@"03"]) {
                        NSLog(@"电动玻璃");
                    }else if ([device.strDevice_category isEqualToString:@"10"]) {
                        NSLog(@"86插座");
                    }else if ([device.strDevice_category isEqualToString:@"11"]) {
                        NSLog(@"移动插座");
                    }
                    [self doHandleControlDevice:device];
                    
                }else if ([device.strDevice_device_type isEqualToString:@"02"]){
                    if ([device.strDevice_category isEqualToString:@"02"]) {
                        NSLog(@"电动幕布");
                    }else if ([device.strDevice_category isEqualToString:@"10"]) {
                        NSLog(@"投影架");
                    }else if ([device.strDevice_category isEqualToString:@"11"]) {
                        NSLog(@"推拉开窗器");
                    }else if ([device.strDevice_category isEqualToString:@"12"]) {
                        NSLog(@"平推开窗器");
                    }else if ([device.strDevice_category isEqualToString:@"13"]) {
                        NSLog(@"机械手控制器");
                    }
                    UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                    SHControlBoxVC *controlBoxVC = (SHControlBoxVC *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"SHControlBoxVC"];
                    controlBoxVC.device = device;
                    secondVC.hidesBottomBarWhenPushed = YES;
                    [secondVC.navigationController pushViewController:controlBoxVC animated:YES];
                    
                }else if ([device.strDevice_device_type isEqualToString:@"01"]){
                    if ([device.strDevice_category isEqualToString:@"02"]) {
                        NSLog(@"普通电动窗帘");
                        [self doHandleControlDevice:device];
                    }
                }else{
                    NSLog(@"其它设备");
                    [self doHandleControlDevice:device];
                }
            }else if ([device.strDevice_device_OD isEqualToString:@"0F BE"]){
                
                NSLog(@"休眠设备");
                if ([device.strDevice_device_type isEqualToString:@"01"]){
                    
                    if ([device.strDevice_category isEqualToString:@"02"]){
                        
                        NSLog(@"门磁");
                    }
                }else if ([device.strDevice_device_type isEqualToString:@"02"]){
                    
                    if ([device.strDevice_category isEqualToString:@"02"]){
                        
                        NSLog(@"指纹锁");
                        /*
                         UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                         LockViewController *VC = (LockViewController *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"LockViewController"];
                         [self.navigationController pushViewController:VC animated:YES];
                         */
                        [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
                        self.deviceRemberForLock = device;
                        [self.lockManager doGetLockPswListFromNetworkWithDeviceID:device.iDevice_device_id];
                    }else if ([device.strDevice_category isEqualToString:@"03"]){
                        NSLog(@"小蛮腰指纹锁");
                        
                        [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
                        self.deviceRemberForLock = device;
                        [self.lockManager doGetLockPswListFromNetworkWithDeviceID:device.iDevice_device_id];
                        
                        
                    }
                }else if ([device.strDevice_device_type isEqualToString:@"07"]){
                    
                    if ([device.strDevice_category isEqualToString:@"02"]){
                        
                        NSLog(@"烟雾传感器");
                    }
                }else if ([device.strDevice_device_type isEqualToString:@"81"]){
                    
                    if ([device.strDevice_category isEqualToString:@"02"]){
                        
                        NSLog(@"门磁");
                    }else if ([device.strDevice_category isEqualToString:@"03"]){
                        
                        NSLog(@"窗磁");
                    }
                }else if ([device.strDevice_device_type isEqualToString:@"83"]){
                    
                    if ([device.strDevice_category isEqualToString:@"02"]){
                        
                        NSLog(@"水浸传感器");
                    }
                }else if ([device.strDevice_device_type isEqualToString:@"86"]){
                    
                    if ([device.strDevice_category isEqualToString:@"02"]){
                        
                        NSLog(@"人体红外传感器");
                    }
                }else{
                    NSLog(@"其它休眠设备");
                }
                
            }else if ([device.strDevice_device_OD isEqualToString:@"0F E6"]){
                if ([device.strDevice_device_type isEqualToString:@"02"]){
                    if ([device.strDevice_category isEqualToString:@"02"]){
                        NSLog(@"红外学习仪");
                        if ([device.strDevice_sindex_length intValue] == SHSindexLength_InfraredAirCondition) {
                            NSLog(@"空调");
                            UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                            AirConditionVC *VC = (AirConditionVC *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"AirConditionVC"];
                            VC.device = device;
                            VC.iCode = [device.strDevice_other_status intValue];
                            VC.itype = 0;
                            //                            VC.strOnOrOff = device.strDevice_sindex;
                            //                            VC.strTemperature = device.strDevice_sindex_length;
                            secondVC.hidesBottomBarWhenPushed = YES;
                            [secondVC.navigationController pushViewController:VC animated:YES];
                            
                        }else if ([device.strDevice_sindex_length intValue] == SHSindexLength_InfraredOther_Other){
                            NSLog(@"其它遥控设备");
                            UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                            InfraredDeviceViewController *VC = (InfraredDeviceViewController *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"InfraredDeviceViewController"];
                            VC.strDeviceMacAddr = device.strDevice_mac_address;
                            VC.iDeviceId = device.iDevice_device_id;
                            VC.mutArrKeyBtns = [NSMutableArray arrayWithArray:device.arrBtns];
                            VC.device = device;
                            VC.isCouldStudy = NO;
                            VC.itype = 0;
                            secondVC.hidesBottomBarWhenPushed = YES;
                            [secondVC.navigationController pushViewController:VC animated:YES];
                        }else{
                            
                            NSLog(@"其它遥控设备");
                            UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                            InfraredDeviceViewController *VC = (InfraredDeviceViewController *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"InfraredDeviceViewController"];
                            VC.strDeviceMacAddr = device.strDevice_mac_address;
                            VC.iDeviceId = device.iDevice_device_id;
                            VC.mutArrKeyBtns = [NSMutableArray arrayWithArray:device.arrBtns];
                            VC.device = device;
                            VC.isCouldStudy = NO;
                            VC.itype = 0;
                            secondVC.hidesBottomBarWhenPushed = YES;
                            [secondVC.navigationController pushViewController:VC animated:YES];
                        }
                    }else if ([device.strDevice_category isEqualToString:@"03"]){
                        
                        NSLog(@"音乐背景器");
                    }else if ([device.strDevice_category isEqualToString:@"10"]){
                        
                        NSLog(@"电动窗帘");
                        UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                        WindowTransmitViewController *VC = (WindowTransmitViewController *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"WindowTransmitViewController"];
                        VC.device = device;
                        [secondVC.navigationController pushViewController:VC animated:YES];
                    }else if ([device.strDevice_category isEqualToString:@"11"]){
                        
                        NSLog(@"平移开窗器");
                        UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                        WindowTransmitViewController *VC = (WindowTransmitViewController *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"WindowTransmitViewController"];
                        VC.device = device;
                        VC.itype = 0;
                        secondVC.hidesBottomBarWhenPushed = YES;
                        [secondVC.navigationController pushViewController:VC animated:YES];
                    }else if ([device.strDevice_category isEqualToString:@"12"]){
                        
                        NSLog(@"电动床");
                    }else if ([device.strDevice_category isEqualToString:@"13"]){
                        
                        NSLog(@"新风系统");
                        UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                        XFNewViewController *VC = (XFNewViewController *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"XFNewViewController"];
                        VC.device = device;
                        VC.itype = 0;
                        secondVC.hidesBottomBarWhenPushed = YES;
                        [secondVC.navigationController pushViewController:VC animated:YES];
                    }else if ([device.strDevice_category isEqualToString:@"14"]){
                        NSLog(@"净水器");
                        UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                        SHWaterPurifierVC *VC = (SHWaterPurifierVC *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"SHWaterPurifierVC"];
                        VC.device = device;
                        VC.itype = 0;
                        secondVC.hidesBottomBarWhenPushed = YES;
                        [secondVC.navigationController pushViewController:VC animated:YES];
                        
                    }else if ([device.strDevice_category isEqualToString:@"20"]){
                        NSLog(@"浴霸");
                    }
                }
            }else if ([device.strDevice_device_OD isEqualToString:@"0F C8"]){
                
                if ([device.strDevice_device_type isEqualToString:@"01"]){
                    
                    if ([device.strDevice_category isEqualToString:@"02"]){
                        
                        NSLog(@"单相电表");
                    }
                }else if ([device.strDevice_device_type isEqualToString:@"02"]){
                    
                    if ([device.strDevice_category isEqualToString:@"02"]){
                        
                        NSLog(@"计量控制盒");
                    }
                }else if ([device.strDevice_device_type isEqualToString:@"03"]){
                    
                    if ([device.strDevice_category isEqualToString:@"02"]){
                        
                        NSLog(@"三相电表");
                    }
                }else if ([device.strDevice_device_type isEqualToString:@"04"]){
                    
                    if ([device.strDevice_category isEqualToString:@"02"]){
                        
                        NSLog(@"10A的计量插座");
                    }else if ([device.strDevice_category isEqualToString:@"03"]){
                        
                        NSLog(@"16A的计量插座");
                    }
                    [self doHandleTheMeasureDevice:device];
                }
            }else{
                NSLog(@"未知，OD:%@,type:%@",device.strDevice_device_OD,device.strDevice_device_type);
            }
            NSLog(@"短点Item");
        }
        
    }];
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

#pragma mark -  private
#pragma mark - 添加完成后，立即刷新列表
- (void)doHanleAtOnceAddDeviceSucceedToRefreshList:(NSArray *)arrDevice
{
    NSMutableArray *mutTemp = [NSMutableArray arrayWithCapacity:0];
    [mutTemp addObjectsFromArray:arrDevice];
    
    NSMutableArray *mutArrOFAA = [NSMutableArray new];
    NSMutableArray *mutArrOFBE = [NSMutableArray new];
    NSMutableArray *mutArrOFE6 = [NSMutableArray new];
    for (int i = 0; i < mutTemp.count; i ++) {
        SHModelDevice *modeDevcieTemp = mutTemp[i];
        if ([modeDevcieTemp.strDevice_device_OD isEqualToString:@"0F AA"]) {
            
            if ([modeDevcieTemp.strDevice_device_type isEqualToString:@"81"]) {
                [mutArrOFBE addObject:modeDevcieTemp];
            }else
                [mutArrOFAA addObject:modeDevcieTemp];
        }else if ([modeDevcieTemp.strDevice_device_OD isEqualToString:@"0F BE"]){
            [mutArrOFE6 addObject:modeDevcieTemp];
        }else{
            [mutArrOFBE addObject:modeDevcieTemp];
        }
    }
    [mutTemp removeAllObjects];
    [mutTemp addObjectsFromArray:mutArrOFAA];
    [mutTemp addObjectsFromArray:mutArrOFE6];
    [mutTemp addObjectsFromArray:mutArrOFBE];
    self.deviceCV.arrDataList = mutTemp;
    [self doAskAll4010Device];
}

#pragma mark -

- (void)doHandleControlDevice:(SHModelDevice *)device
{
    NSString *strHexState = @"00";
    if (!device.strDevice_other_status) {
        if (device.iDevice_device_state1  == 1) {
            
            strHexState = @"01";
        }else{
            
            strHexState = @"02";
        }
    }else if ([device.strDevice_other_status intValue] == 1) {
        
        if (device.iDevice_device_state1  == 1) {
            strHexState = @"02";
        }else{
            
            strHexState = @"01";
        }
        
    }else if ([device.strDevice_other_status intValue] == 2){
        
        if (device.iDevice_device_state2  == 1) {
            strHexState = @"02";
        }else{
            
            strHexState = @"01";
        }
    }else if ([device.strDevice_other_status intValue] == 3){
        if (device.iDevice_device_state3  == 1) {
            strHexState = @"02";
        }else{
            strHexState = @"01";
        }
    }
    NSData *data = [[NetworkEngine shareInstance] doGetSwitchControlWithTargetAddr:device.strDevice_mac_address
                                                                              device:device
                                                                                 way:[device.strDevice_other_status intValue]
                                                                         controlMode:@"01"
                                                                        controlState:strHexState];
    
    NSString *strHexWholeData = [[ToolHexManager sharedManager] convertDataToHexStr:data];
    NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexWholeData];
    NSLog(@"0FAA进行控制页面发送:%@",strBigTemp);
    [[NetworkEngine shareInstance] sendRequestData:data];
}

#pragma mark - 计量插座
- (void)doHandleTheMeasureDevice:(SHModelDevice *)device
{
    
    NSString *strTitle = [NSString stringWithFormat:@"%@详细用电信息",device.strDevice_device_name];
    //    NSString *strDetail = [NSString stringWithFormat:@"电压:%@;\n电流：%@;\n功率:%@;\n电量:%@",device.strDCVReal,device.strCurrentReal,device.strUsefulPowerReal,device.strElectricQuantityReal];
    NSString *strDetail = [NSString stringWithFormat:@"%@;\n%@;\n%@;\n%@",device.strDCVReal,device.strCurrentReal,device.strUsefulPowerReal,device.strElectricQuantityReal];
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:strTitle
                                                                             message:strDetail
                                                                      preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消"
                                                           style:UIAlertActionStyleCancel
                                                         handler:^(UIAlertAction * _Nonnull action)
                                   {
                                       
                                   }];
    
    UIAlertAction *openAction = [UIAlertAction actionWithTitle:@"打开"
                                                         style:UIAlertActionStyleDefault
                                                       handler:^(UIAlertAction * _Nonnull action)
                                 {
                                     [self doHandleMeasureDevice4040ControlDevice:device strHexState:@"01"];
                                 }];
    UIAlertAction *closeAction = [UIAlertAction actionWithTitle:@"关闭"
                                                          style:UIAlertActionStyleDefault
                                                        handler:^(UIAlertAction * _Nonnull action)
                                  {
                                      [self doHandleMeasureDevice4040ControlDevice:device strHexState:@"02"];
                                  }];
    
    
    [alertController addAction:openAction];
    [alertController  addAction:cancelAction];
    [alertController addAction:closeAction];
    
    
    [self presentViewController:alertController animated:YES completion:nil];
}

#pragma mark - 计量插座控制
- (void)doHandleMeasureDevice4040ControlDevice:(SHModelDevice *)device strHexState:(NSString *)strHexState
{
    NSData *dataSend = [[NetworkEngine shareInstance] doGetMeasureDeviceControlWithTargetAddr:device.strDevice_mac_address
                                                                                         device:device
                                                                                   controlState:strHexState];
    NSString *strHexWholeData = [[ToolHexManager sharedManager] convertDataToHexStr:dataSend];
    NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexWholeData];
    NSLog(@"0FC8进行控制页面:\n%@",strBigTemp);
    [[NetworkEngine shareInstance] sendRequestData:dataSend];
}






- (SecondPageVC *)viewControllerSupportView:(UIView *)view {
    for (UIView* next = [view superview]; next; next = next.superview) {
        UIResponder *nextResponder = [next nextResponder];
        if ([nextResponder isKindOfClass:[SecondPageVC class]]) {
            return (SecondPageVC *)nextResponder;
        }
    }
    return nil;
}


- (UIViewController *)topViewController {
    UIViewController *resultVC;
    resultVC = [self _topViewController:[[UIApplication sharedApplication].keyWindow rootViewController]];
    while (resultVC.presentedViewController) {
        resultVC = [self _topViewController:resultVC.presentedViewController];
    }
    return resultVC;
}

- (UIViewController *)_topViewController:(UIViewController *)vc {
    if ([vc isKindOfClass:[UINavigationController class]]) {
        return [self _topViewController:[(UINavigationController *)vc topViewController]];
    } else if ([vc isKindOfClass:[UITabBarController class]]) {
        return [self _topViewController:[(UITabBarController *)vc selectedViewController]];
    } else {
        return vc;
    }
    return nil;
}

@end
