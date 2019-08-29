//
//  DeviceManagementVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/27.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "DeviceManagementVC.h"
#import "CommonCollectionView.h"
#import "SHDeviceManager.h"
#import "CommonAlterView.h"
#import "LightViewController.h"
#import "SHControlBoxVC.h"
#import "AirConditionVC.h"
#import "InfraredDeviceViewController.h"
#import "CurtainViewController.h"
#import "WindowTransmitViewController.h"
#import "LockViewController.h"
#import "SHRequestTimer.h"
#import "SHAirHandleTypeVC.h"
#import "SHLockSetPswVC.h"
#import "SHLockManager.h"
#import "SHWaterPurifierVC.h"
#import "SHColourBulbVC.h"
#import "XFViewController.h"
#import "XFNewViewController.h"
#import "HUDManager.h"


#define delayInSeconds  10.0;

typedef NS_ENUM(NSInteger, SHConfigStatue)
{
    SHConfigStatue_Ready              = 0,   //准备配置
    SHConfigStatue_Ing                = 1,   //配置中
    SHConfigStatue_Finish             = 2,   //配置完成
    SHConfigStatue_Fail               = 3,   //配置失败
};

@interface DeviceManagementVC ()<UINavigationControllerDelegate>

@property (strong, nonatomic) SHRequestTimer *timer;

@property (strong, nonatomic) CommonCollectionView *commonCollectionView;

@property (strong, nonatomic) SHDeviceManager *manager;

@property (assign, nonatomic) NetworkEngine *networkEngine;

@property (assign, nonatomic) SHConfigStatue configStatue;
//设备的sindex_length 手动添加的  区分红外转发器，空调和其它设备
@property (assign, nonatomic) SHSindexLength deviceSindexLength;
//记录所有已经添加设备的MacAddr
@property (strong, nonatomic) NSMutableArray *mutArrRemMacAddr;
//记录已经添加红外转发器的MacAddr
@property (strong, nonatomic) NSMutableArray *mutArrRemInfraredMacAddr;
//为了在添加设备的时候再收到数据弹框
@property (strong, nonatomic) NSString *strRemberMacAddr;
//添加上报设备的
@property (strong, nonatomic) NSMutableArray *mutArrRemberReportDevice;
//记录添加设备的
@property (strong, nonatomic) SHModelDevice *shouldAddDevice;
//针对添加设备的
@property (strong, nonatomic) NSMutableArray *mutArrReadyAdd;

@property (assign, nonatomic) int iRemberCount;

@property (assign, nonatomic) int  iRoadCount;
//还没有添加的仓库号
@property (strong, nonatomic) NSArray *arrNotAddHouseNO;
//获取指纹锁密码列表
@property (strong, nonatomic) SHLockManager *lockManager;

@property (strong, nonatomic) SHModelDevice *deviceRemberForLock;

@end

@implementation DeviceManagementVC

#pragma mark - UINavigationControllerDelegate
- (void)navigationController:(UINavigationController *)navigationController
      willShowViewController:(UIViewController *)viewController
                    animated:(BOOL)animated {
    BOOL isHomePage = [viewController isKindOfClass:[self class]];
    
    [self.navigationController setNavigationBarHidden:!isHomePage animated:YES];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    [self doInitSubViews];
    [self doRegisterKVO];
    [self doAddAction];
    [self doLoadData];
    [self doAskAll4010Device];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navigationController.delegate = self;
    
    //#warning 需要优化
    [self.manager doGetDeviceListFromNetworkWithRoomID:self.room.iRoom_id];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
}

#pragma mark -
#pragma mark - 询问所有4010设备的状态
- (void)doAskAll4010Device
{
    NSData *data = [[NetworkEngine shareInstance] doGetRefreshDeviceStatue];
    [[NetworkEngine shareInstance] sendRequestData:data];
}

#pragma mark -
#pragma mark - 添加 subViews
- (void)doInitSubViews
{
    [self setTitleViewText:self.room.strRoom_name];
    self.networkEngine = [NetworkEngine shareInstance];
    [self.view addSubview:self.commonCollectionView];
    [self doAddCommonCollectionViewConstraints];
    [self setNavigationBarLeftBarButtonWithTitle:@"返回" action:@selector(leftAction:)];
}

- (void)leftAction:(UIButton *)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark -
#pragma mark - loadData
- (void)doLoadData
{
    [self.manager doGetDeviceListDataFromDBWithRoomID:self.room.iRoom_id];
    [self.manager doGetDeviceListFromNetworkWithRoomID:self.room.iRoom_id];
}

#pragma mark -
#pragma mark - 注册kvo
- (void)doRegisterKVO
{
    @weakify(self);
    [self observeKeyPath:@keypath(self.manager.arrDeviceList)
                   block:^(id value) {
                       @strongify(self);
                       //                       [XWHUDManager hideInWindow];
                       [self.mutArrRemMacAddr removeAllObjects];
                       NSArray *arr = (NSArray *)value;
                       if (arr.count) {
                           self.arrNotAddHouseNO = [self doHandleGetNotAddHouseNOWithAllDevice:arr];
                           self.commonCollectionView.arrList = [NSMutableArray arrayWithArray:arr];
                           dispatch_async(dispatch_get_main_queue(), ^{
                               @strongify(self);
                               [self doGetHasAddedDeviceMacAddr:arr];
                           });
                       }else{
                           self.commonCollectionView.arrList = [NSMutableArray arrayWithArray:arr];
                           [self.commonCollectionView reloadData];
                       }
                   }];
    
    [self observeKeyPath:@keypath(self.manager.errorInfo)
                   block:^(id value) {
                       //                       [XWHUDManager hideInWindow];
                       NSDictionary *dict = (NSDictionary *)value;
                       [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",dict[@"message"]]];
                   }];
    
    //监测设备上报添加
    [self observeKeyPath:@keypath(self.networkEngine.modelDevice) block:^(id value) {
        @strongify(self);
        SHModelDevice *deviceReport = (SHModelDevice *)value;
        
        if (self.configStatue == SHConfigStatue_Ing) {
            NSLog(@"处于添加设备状态");
            if (self.strRemberMacAddr.length > 0) {
                NSLog(@"正在添加设备ing.....");
            }else{
                //首先知道这个设备添加三次了
                if ([self doJudgeGetTheReportDevice:deviceReport]) {
                    [self stopTimer];
                    //其次判断应该不应该加入
                    if ([self doJudgeShouldPopToAddDevice]) {
                        self.strRemberMacAddr = deviceReport.strDevice_mac_address;
                        [self doHandleAddThreeRoadFirstSwitch:self.shouldAddDevice];
                    }
                }
            }
        }else{
            //            NSLog(@"处于普通上报刷新状态");
            NSMutableArray *mutArrDevice = [NSMutableArray new];
            for (int i = 0; i < self.commonCollectionView.arrList.count; i ++) {
                SHModelDevice *modelDevice = self.commonCollectionView.arrList[i];
                if ([modelDevice.strDevice_mac_address isEqualToString:deviceReport.strDevice_mac_address]) {
                    SHModelDevice *deviceNew            = [SHModelDevice new];
                    deviceNew.iDevice_device_id         = modelDevice.iDevice_device_id;
                    deviceNew.iDevice_room_id           = modelDevice.iDevice_room_id;
                    deviceNew.strDevice_device_name     = modelDevice.strDevice_device_name;
                    deviceNew.strDevice_image           = modelDevice.strDevice_image;
                    
                    deviceNew.strDevice_device_OD       = modelDevice.strDevice_device_OD;
                    deviceNew.strDevice_device_type     = modelDevice.strDevice_device_type;
                    deviceNew.strDevice_category        = modelDevice.strDevice_category;
                    deviceNew.strDevice_cmdId           = modelDevice.strDevice_cmdId;
                    
                    deviceNew.strDevice_sindex          = modelDevice.strDevice_sindex;
                    deviceNew.strDevice_sindex_length   = modelDevice.strDevice_sindex_length;
                    deviceNew.strDevice_mac_address     = modelDevice.strDevice_mac_address;
                    deviceNew.iDevice_device_state1     = deviceReport.iDevice_device_state1;
                    
                    deviceNew.iDevice_device_state2     = deviceReport.iDevice_device_state2;
                    deviceNew.iDevice_device_state3     = deviceReport.iDevice_device_state3;
                    deviceNew.strDevice_other_status    = modelDevice.strDevice_other_status;
                    deviceNew.arrBtns                   = modelDevice.arrBtns;
                    
                    deviceNew.strDCVReal                = deviceReport.strDCVReal;
                    deviceNew.strCurrentReal            = deviceReport.strCurrentReal;
                    deviceNew.strUsefulPowerReal        = deviceReport.strUsefulPowerReal;
                    deviceNew.strElectricQuantityReal   = deviceReport.strElectricQuantityReal;
                    
                    [mutArrDevice addObject:deviceNew];
                }else{
                    [mutArrDevice addObject:modelDevice];
                }
            }
            self.commonCollectionView.arrList = mutArrDevice;
        }
    }];
    
    //指纹锁密码列表
    [self observeKeyPath:@keypath(self.lockManager.arrLockPswList)
                   block:^(id value) {
                       @strongify(self);
                       [XWHUDManager hideInWindow];
                       NSArray *arr = (NSArray *)value;
                       if (arr.count) {
                           SHLockPswModel *pswModel = arr[0];
                           UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                           LockViewController *VC = (LockViewController *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"LockViewController"];
                           VC.strPsw = pswModel.strUnlockPsw;
                           VC.iDeviceID = pswModel.iDeviceID;
                           VC.itype = 1;
                           VC.strMacAddr = self.deviceRemberForLock.strDevice_mac_address;
                           VC.deviceTransmit = self.deviceRemberForLock;
                           [self.navigationController pushViewController:VC animated:YES];
                       }else{
                           
                           UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                           SHLockSetPswVC *VC = (SHLockSetPswVC *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"SHLockSetPswVC"];
                           VC.iDeviceID = self.deviceRemberForLock.iDevice_device_id;
                           VC.strMacAddr = self.deviceRemberForLock.strDevice_mac_address;
                           VC.deviceTransmit = self.deviceRemberForLock;
                           VC.itype = 1;
                           [self.navigationController pushViewController:VC animated:YES];
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
#pragma mark - 从网络里拉取获取红外设备 并且获取其所有设备的MAC 地址
- (void)doGetHasAddedDeviceMacAddr:(NSArray *)arr;
{
    [self.mutArrRemInfraredMacAddr removeAllObjects];
    [self.mutArrRemMacAddr removeAllObjects];
    for (int i = 0; i < arr.count; i ++) {
        SHModelDevice *modelDevice = arr[i];
        [self.mutArrRemMacAddr addObject:modelDevice.strDevice_mac_address];
        if ([self doJudgeIsInfraredDevice:modelDevice]) {
            //是红外转发器
            [self.mutArrRemInfraredMacAddr addObject:modelDevice.strDevice_mac_address];
        }
    }
}

#pragma mark -
#pragma mark - 点击Item

- (void)doAddAction
{
    @weakify(self);
    [self.commonCollectionView setDidSelectedItemBlock:^(NSIndexPath *indexPath, CommonCollectionViewActionType type, SHModelDevice *device) {
        @strongify(self);
        
        switch (type) {
            case CommonCollectionViewActionType_Common:
            {
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
                            VC.itype = 1;
                            [self.navigationController pushViewController:VC animated:YES];
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
                        }else if ([device.strDevice_category isEqualToString:@"04"]){
                            NSLog(@"多联三路灯开关");
                            [self doHandleControlDevice:device];
                        }
                        
                    }else if ([device.strDevice_device_type isEqualToString:@"06"]){
                        if ([device.strDevice_category isEqualToString:@"02"]) {
                            NSLog(@"二路灯开关");
                            [self doHandleControlDevice:device];
                        }else if ([device.strDevice_category isEqualToString:@"04"]) {
                            NSLog(@"二路灯开关");
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
                        controlBoxVC.itype = 1;
                        [self.navigationController pushViewController:controlBoxVC animated:YES];
                        
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
                    if ([device.strDevice_device_type isEqualToString:@"01"]) {
                        if ([device.strDevice_category isEqualToString:@"02"]) {
                            NSLog(@"门磁");
                            
                        }
                    }else if ([device.strDevice_device_type isEqualToString:@"02"]) {
                        if ([device.strDevice_category isEqualToString:@"02"]) {
                            NSLog(@"指纹锁");
                            [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
                            self.deviceRemberForLock = device;
                            [self.lockManager doGetLockPswListFromNetworkWithDeviceID:device.iDevice_device_id];
                        }else if ([device.strDevice_category isEqualToString:@"03"]){
                            NSLog(@"小蛮腰指纹锁");
                            [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
                            self.deviceRemberForLock = device;
                            [self.lockManager doGetLockPswListFromNetworkWithDeviceID:device.iDevice_device_id];
                        }
                    } else if ([device.strDevice_device_type isEqualToString:@"03"]){
                        
                        if ([device.strDevice_category isEqualToString:@"02"]){
                            NSLog(@"燃气传感器BE");
                        }
                    } else if ([device.strDevice_device_type isEqualToString:@"04"]){
                        
                        if ([device.strDevice_category isEqualToString:@"02"]){
                            NSLog(@"人体红外传感器BE");
                        }
                    } else if ([device.strDevice_device_type isEqualToString:@"05"]){
                        
                        if ([device.strDevice_category isEqualToString:@"02"]){
                            NSLog(@"水浸传感器BE");
                        }
                    } else if ([device.strDevice_device_type isEqualToString:@"07"]) {
                        if ([device.strDevice_category isEqualToString:@"02"]) {
                            NSLog(@"烟雾传感器");
                            
                        }
                    }else if ([device.strDevice_device_type isEqualToString:@"81"]) {
                        if ([device.strDevice_category isEqualToString:@"02"]) {
                            NSLog(@"门磁");
                            
                        }else if ([device.strDevice_category isEqualToString:@"03"]){
                            NSLog(@"窗磁");
                            
                        }
                    }else if ([device.strDevice_device_type isEqualToString:@"83"]) {
                        if ([device.strDevice_category isEqualToString:@"02"]) {
                            NSLog(@"水浸传感器");
                            
                        }
                    }else if ([device.strDevice_device_type isEqualToString:@"86"]) {
                        if ([device.strDevice_category isEqualToString:@"02"]) {
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
                                [self performSegueWithIdentifier:@"seg_to_SHAirHandleTypeVC" sender:device];
                                
                            }else if ([device.strDevice_sindex_length intValue] == SHSindexLength_InfraredOther_Other){
                                NSLog(@"其它遥控设备");
                                UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                                InfraredDeviceViewController *VC = (InfraredDeviceViewController *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"InfraredDeviceViewController"];
                                VC.strDeviceMacAddr = device.strDevice_mac_address;
                                VC.iDeviceId = device.iDevice_device_id;
                                VC.mutArrHouseNO = [NSMutableArray arrayWithArray:self.arrNotAddHouseNO];
                                VC.mutArrKeyBtns = [NSMutableArray arrayWithArray:device.arrBtns];
                                VC.device = device;
                                VC.isCouldStudy = YES;
                                VC.itype = 1;
                                [self.navigationController pushViewController:VC animated:YES];
                            }else{
                                
                                NSLog(@"其它遥控设备");
                                UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                                InfraredDeviceViewController *VC = (InfraredDeviceViewController *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"InfraredDeviceViewController"];
                                VC.strDeviceMacAddr = device.strDevice_mac_address;
                                VC.iDeviceId = device.iDevice_device_id;
                                VC.mutArrHouseNO = [NSMutableArray arrayWithArray:self.arrNotAddHouseNO];
                                VC.mutArrKeyBtns = [NSMutableArray arrayWithArray:device.arrBtns];
                                VC.device = device;
                                VC.isCouldStudy = YES;
                                VC.itype = 1;
                                [self.navigationController pushViewController:VC animated:YES];
                            }
                        }else if ([device.strDevice_category isEqualToString:@"03"]){
                            
                            NSLog(@"音乐背景器");
                        } else if ([device.strDevice_category isEqualToString:@"04"]){
                            
                            NSLog(@"RS232转发器");
                        } else if ([device.strDevice_category isEqualToString:@"05"]){
                            
                            NSLog(@"风机盘管");
                        }else if ([device.strDevice_category isEqualToString:@"10"]){
                            
                            NSLog(@"电动窗帘");
                            UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                            WindowTransmitViewController *VC = (WindowTransmitViewController *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"WindowTransmitViewController"];
                            VC.device = device;
                            VC.itype = 1;
                            [self.navigationController pushViewController:VC animated:YES];
                        }else if ([device.strDevice_category isEqualToString:@"11"]){
                            
                            NSLog(@"平移开窗器");
                            UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                            WindowTransmitViewController *VC = (WindowTransmitViewController *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"WindowTransmitViewController"];
                            VC.device = device;
                            VC.itype = 1;
                            [self.navigationController pushViewController:VC animated:YES];
                        }else if ([device.strDevice_category isEqualToString:@"12"]){
                            
                            NSLog(@"电动床");
                        }else if ([device.strDevice_category isEqualToString:@"13"]){
                            
                            NSLog(@"新风系统");
//                            UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
//                            XFViewController *VC = (XFViewController *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"XFViewController"];
//                            VC.device = device;
//                            [self.navigationController pushViewController:VC animated:YES];
                            
                            UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
                            XFNewViewController *VC = (XFNewViewController *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"XFNewViewController"];
                            VC.device = device;
                            VC.itype = 1;
                            [self.navigationController pushViewController:VC animated:YES];

                        }else if ([device.strDevice_category isEqualToString:@"15"]){
                            NSLog(@"中央空调");
                            NSData *dataSend = [[NetworkEngine shareInstance] doSendVRVDataWithModelDevice:device];
                            [[NetworkEngine shareInstance] sendRequestData:dataSend];
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
            }
            break;
            case CommonCollectionViewActionType_LongPressed:
            {
                NSLog(@"长按设备");
                [self doAddActionSheetAboutDeleteAndEdit:device];
            }
            break;
            case CommonCollectionViewActionType_Add:
            {
                NSLog(@"添加设备");
                [self doAddDevice];
            }
            break;
            case CommonCollectionViewActionType_Control:
            {
                NSLog(@"cotrol设备");
            }
            break;
            
            default:
            break;
        }
    }];
}

#pragma mark - 弹出的actionSheet
- (void)doAddDevice
{
    @weakify(self);
    LCActionSheet *actionSheet = [LCActionSheet sheetWithTitle:@"请选择要添加设备的类型"
                                             cancelButtonTitle:@"取消"
                                                       clicked:^(LCActionSheet * _Nonnull actionSheet, NSInteger buttonIndex) {
                                                           @strongify(self);
                                                           switch (buttonIndex) {
                                                               case 1:
                                                               {
                                                                   self.configStatue = SHConfigStatue_Ing;
                                                                   
//                                                                   dispatch_async(dispatch_get_main_queue(), ^{
//                                                                      [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
//                                                                   });
                                                                   
                                                                   [HUDManager showHud:@"自动上报设备添加中.." customImgView:nil btnTitle:@"取消" btnFont:[UIFont systemFontOfSize:17] btnTitleColor:nil btnBackColor:nil Target:self action:@selector(clickSure) isShowMaskView:YES afterDelay:60 isHide:YES onView:self.view completionBlock:^{
                                                                       NSLog(@"自定义带按钮的视图设置显示时间");
                                                                   }];
                                                                   
                                                                   
                                                                   self.deviceSindexLength = SHSindexLength_OtherReport;
                                                                   NSLog(@"自动上报设备");
                                                               }
                                                                   break;
                                                               case 2:
                                                               {
                                                                   NSLog(@"空调");
                                                                   self.configStatue = SHConfigStatue_Ing;
                                                                   [HUDManager showHud:@"空调设备添加中.." customImgView:nil btnTitle:@"取消" btnFont:[UIFont systemFontOfSize:17] btnTitleColor:nil btnBackColor:nil Target:self action:@selector(clickSure) isShowMaskView:YES afterDelay:60 isHide:YES onView:self.view completionBlock:^{
                                                                       NSLog(@"自定义带按钮的视图设置显示时间");
                                                                   }];
                                                                   self.deviceSindexLength = SHSindexLength_InfraredAirCondition;
                                                                   
                                                                   if (self.mutArrRemInfraredMacAddr.count) {
                                                                       [XWHUDManager hideInWindow];
                                                                       self.strRemberMacAddr = [self.mutArrRemInfraredMacAddr firstObject];
                                                                       SHModelDevice *deviceInfrared = [self doHanleSetInfraredDeviceWithInfraredMacAddr:self.strRemberMacAddr infraredCategory:self.deviceSindexLength];
                                                                       [self doHandleAddThreeRoadFirstSwitch:deviceInfrared];
                                                                   }
                                                               }
                                                                   break;
                                                               case 3:
                                                               {
                                                                   NSLog(@"其他红外转发器设备");
                                                                   self.configStatue = SHConfigStatue_Ing;
                                                                   [HUDManager showHud:@"其它设备添加中.." customImgView:nil btnTitle:@"取消" btnFont:[UIFont systemFontOfSize:17] btnTitleColor:nil btnBackColor:nil Target:self action:@selector(clickSure) isShowMaskView:YES afterDelay:60 isHide:YES onView:self.view completionBlock:^{
                                                                       NSLog(@"自定义带按钮的视图设置显示时间");
                                                                   }];
                                                                   self.deviceSindexLength = SHSindexLength_InfraredOther_Other;
                                                                   
                                                                   if (self.mutArrRemInfraredMacAddr.count) {
                                                                       [XWHUDManager hideInWindow];
                                                                       self.strRemberMacAddr = [self.mutArrRemInfraredMacAddr firstObject];
                                                                       SHModelDevice *deviceInfrared = [self doHanleSetInfraredDeviceWithInfraredMacAddr:self.strRemberMacAddr infraredCategory:self.deviceSindexLength];
                                                                       [self doHandleAddThreeRoadFirstSwitch:deviceInfrared];
                                                                   }
                                                               }
                                                                   break;
                                                               case 0:
                                                               {
                                                                   NSLog(@"点了取消");
                                                                   self.configStatue = SHConfigStatue_Finish;
                                                                   
                                                               }
                                                                   break;
                                                                   
                                                               default:
                                                                   break;
                                                           }
                                                           [self startTimer];
                                                       }
                                             otherButtonTitleArray:@[@"其它设备",@"空调",@"其他红外转发器设备"]];
    [actionSheet show];
    
}

-(void)clickSure{
    [HUDManager hidenHudFromView:self.view];
}

#pragma mark -
#pragma mark Timer
- (void)startTimer
{
    if (self.timer == nil) {
        self.timer = [[SHRequestTimer alloc] init];
    }
    //    int timeout_ = GNetReqDefaultTimeOut; //self.timeout > 0 ? self.timeout : DEFAULT_REQUEST_TIMEOUT;
    
    int timeout_ = 135;
    [self.timer start:timeout_ target:self sel:@selector(timerAction)];
}

- (void)stopTimer
{
    self.timer = nil;
    [self.timer stop];
}

- (void)timerAction
{
    NSLog(@"看时间");
    [self clickSure];
    if (self.configStatue == SHConfigStatue_Ing) {
        [self stopTimer];
        [self handleAddFailUsedToDo];
        [XWHUDManager hideInWindow];
        [XWHUDManager showErrorTipHUD:@"添加设备失败"];
        
    }else{
        
        return;
    }
}

- (void)handleAddFailUsedToDo
{
    self.configStatue = SHConfigStatue_Fail;
    self.deviceSindexLength = 0;
    self.strRemberMacAddr = nil;
    self.shouldAddDevice = nil;
    [self.mutArrRemberReportDevice removeAllObjects];
}

- (void)doClearAllShouldClear
{
    [self.mutArrReadyAdd removeAllObjects];
    [self handleAddFailUsedToDo];
    self.iRemberCount = 0;
    self.iRoadCount = 0;
}

#pragma mark - 如果红外转发器已经存在，生成显得红外转发器设备model
- (SHModelDevice *)doHanleSetInfraredDeviceWithInfraredMacAddr:(NSString *)strInfraredMacAddr infraredCategory:(SHSindexLength)sindexLenth
{
    SHModelDevice *deviceNew = [SHModelDevice new];
    
    deviceNew.iDevice_room_id = self.room.iRoom_id;
    deviceNew.strDevice_device_name = @"红外转发器";
    deviceNew.strDevice_image = @"";
    deviceNew.strDevice_device_OD = @"0F E6";
    
    deviceNew.strDevice_device_type = @"02";
    deviceNew.strDevice_category = @"02";
    deviceNew.strDevice_cmdId = @"01";
    deviceNew.strDevice_sindex = @"";
    
    deviceNew.strDevice_sindex_length = [NSString stringWithFormat:@"%ld",(long)sindexLenth];
    deviceNew.strDevice_mac_address = strInfraredMacAddr;
    deviceNew.iDevice_device_state1 = 0;
    deviceNew.iDevice_device_state2 = 0;
    
    deviceNew.iDevice_device_state3 = 0;
    deviceNew.strDevice_other_status = @"00";
    
    return deviceNew;
}


#pragma mark -
#pragma mark - 添加判断的条件

#pragma mark - 二路设备
- (BOOL)isTwoRoadSwitch:(SHModelDevice *)device
{
    if ([device.strDevice_device_OD isEqualToString:@"0F AA"]
        && [device.strDevice_device_type isEqualToString:@"06"]
        &&[device.strDevice_category isEqualToString:@"02"]) {
        return YES;
    }else if ([device.strDevice_device_OD isEqualToString:@"0F AA"]
              && [device.strDevice_device_type isEqualToString:@"06"]
              &&[device.strDevice_category isEqualToString:@"04"]) {
        return YES;
    }else{
        return NO;
    }
}

#pragma mark - 三路设备
- (BOOL)isThreeRoadSwitch:(SHModelDevice *)device
{
    if ([device.strDevice_device_OD isEqualToString:@"0F AA"]
        &&[device.strDevice_device_type isEqualToString:@"07"]
        &&[device.strDevice_category isEqualToString:@"02"] ) {
        return YES;
    }else if ([device.strDevice_device_OD isEqualToString:@"0F AA"]
              &&[device.strDevice_device_type isEqualToString:@"07"]
              &&[device.strDevice_category isEqualToString:@"04"] ) {
        return YES;
    }else{
        return NO;
    }
}


- (BOOL)doJudgeShouldPopToAddDevice
{
    if ([self doIsDeviceHasNotAddedWithWarning:self.shouldAddDevice.strDevice_mac_address]
        &&[self doJudgeHasChosseDeviceType]
        &&self.strRemberMacAddr.length == 0){
        return YES;
    }else{
        return NO;
    }
}


//连续上报三次确定为添加成功
- (BOOL)doJudgeGetTheReportDevice:(SHModelDevice *)deviceReport
{
    if (self.mutArrRemberReportDevice.count == 0) {
        SHModelDevice * deviceNew = [self doMakeDeviceCountAddOne:deviceReport];
        [self.mutArrRemberReportDevice addObject:deviceNew];
        return NO;
    }
    
    for (int i = 0; i < self.mutArrRemberReportDevice.count; i ++) {
        SHModelDevice *deviceRem = self.mutArrRemberReportDevice[i];
        if ([deviceRem.strDevice_mac_address isEqualToString:deviceReport.strDevice_mac_address]) {
            
            //获取设备后技术➕1
            SHModelDevice *deviceNew = [self doMakeDeviceCountAddOne:deviceRem];
            [self.mutArrRemberReportDevice replaceObjectAtIndex:i withObject:deviceNew];
        }else{
            [self.mutArrRemberReportDevice addObject:deviceReport];
        }
        
        if (deviceRem.iRemberCount >= 3) {
            
            //目的是为了获取设备的最新状态
            SHModelDevice *deviceTemp = [self doGetNewDeviceModelForMakeCountWithOldDevie:deviceRem newDevice:deviceReport];
            self.shouldAddDevice = deviceTemp;
            self.configStatue = SHConfigStatue_Finish;
            [self.mutArrRemberReportDevice removeAllObjects];
            return YES;
        }
    }
    return NO;
}

- (SHModelDevice *)doGetNewDeviceModelForMakeCountWithOldDevie:(SHModelDevice *)deviceOld newDevice:(SHModelDevice *)deviceReport
{
    SHModelDevice *deviceNew            = [SHModelDevice new];
    
    deviceNew.iDevice_room_id           = self.room.iRoom_id;
    deviceNew.strDevice_device_name     = deviceReport.strDevice_device_name;
    deviceNew.strDevice_image           = deviceReport.strDevice_image;
    deviceNew.strDevice_device_OD       = deviceReport.strDevice_device_OD;
    
    deviceNew.strDevice_device_type     = deviceReport.strDevice_device_type;
    deviceNew.strDevice_category        = deviceReport.strDevice_category;
    deviceNew.strDevice_sindex          = deviceReport.strDevice_sindex;
    deviceNew.strDevice_sindex_length   = deviceReport.strDevice_sindex_length;
    
    deviceNew.strDevice_cmdId           = deviceReport.strDevice_cmdId;
    deviceNew.strDevice_mac_address     = deviceReport.strDevice_mac_address;
    deviceNew.iDevice_device_state1     = deviceReport.iDevice_device_state1;
    deviceNew.iDevice_device_state2     = deviceReport.iDevice_device_state2;
    
    deviceNew.iDevice_device_state3     = deviceReport.iDevice_device_state3;
    deviceNew.strDevice_other_status    = deviceReport.strDevice_other_status;
    deviceNew.arrBtns                   = deviceReport.arrBtns;
    deviceNew.iRemberCount              = deviceOld.iRemberCount;
    
    deviceNew.strDCVReal                = deviceReport.strDCVReal;
    deviceNew.strCurrentReal            = deviceReport.strCurrentReal;
    deviceNew.strUsefulPowerReal        = deviceReport.strUsefulPowerReal;
    deviceNew.strElectricQuantityReal   = deviceReport.strElectricQuantityReal;
    
    return deviceNew;
}

- (SHModelDevice *)doMakeDeviceCountAddOne:(SHModelDevice *)device
{
    device.iRemberCount ++;
    
    SHModelDevice *deviceNew            = [SHModelDevice new];
    
    deviceNew.iDevice_room_id           = self.room.iRoom_id;
    deviceNew.strDevice_device_name     = device.strDevice_device_name;
    deviceNew.strDevice_image           = device.strDevice_image;
    deviceNew.strDevice_device_OD       = device.strDevice_device_OD;
    
    deviceNew.strDevice_device_type     = device.strDevice_device_type;
    deviceNew.strDevice_category        = device.strDevice_category;
    deviceNew.strDevice_sindex          = device.strDevice_sindex;
    deviceNew.strDevice_sindex_length   = device.strDevice_sindex_length;
    
    deviceNew.strDevice_cmdId           = device.strDevice_cmdId;
    deviceNew.strDevice_mac_address     = device.strDevice_mac_address;
    deviceNew.iDevice_device_state1     = device.iDevice_device_state1;
    deviceNew.iDevice_device_state2     = device.iDevice_device_state2;
    
    deviceNew.iDevice_device_state3     = device.iDevice_device_state3;
    deviceNew.strDevice_other_status    = device.strDevice_other_status;
    deviceNew.arrBtns                   = device.arrBtns;
    deviceNew.iRemberCount              = device.iRemberCount;
    
    deviceNew.strDCVReal                = device.strDCVReal;
    deviceNew.strCurrentReal            = device.strCurrentReal;
    deviceNew.strUsefulPowerReal        = device.strUsefulPowerReal;
    deviceNew.strElectricQuantityReal   = device.strElectricQuantityReal;
    
    return deviceNew;
}

- (BOOL)doJudgeHasChosseDeviceType
{
    if (self.deviceSindexLength == SHSindexLength_OtherReport
        ||self.deviceSindexLength == SHSindexLength_InfraredOther_Other
        ||self.deviceSindexLength == SHSindexLength_InfraredAirCondition)
    {
        return YES;
    }else{
        [XWHUDManager showWarningTipHUD:@"请选择要添加的设备类型"];
        return NO;
    }
}

- (BOOL)doIsDeviceHasNotAddedWithWarning:(NSString *)deviceMacAddr
{
    if ([self doJudgeHasNotAddDevice:deviceMacAddr]) {
        return YES;
    }else{
        [self timerAction];
        [XWHUDManager showWarningTipHUD:@"该设备已经添加，请确认"];

        return NO;
    }
}

#pragma mark - 判断此设备还没有添加  YES： 为还没有添加 NO：表示已经添加
- (BOOL)doJudgeHasNotAddDevice:(NSString *)strMacAddr
{
    if (self.mutArrRemMacAddr.count > 0) {
        for (int i = 0; i < self.mutArrRemMacAddr.count; i ++) {
            NSString *strRemMacAddr = self.mutArrRemMacAddr[i];
            if ([strMacAddr isEqualToString:strRemMacAddr]) {
                return NO;
            }
        }
        return YES;
        
    }else{
        return YES;
    }
}

#pragma mark - 判断是红外转发器 YES:是红外转发器  NO:不是红外转发器
- (BOOL)doJudgeIsInfraredDevice:(SHModelDevice *)modelDevice
{
    if ([modelDevice.strDevice_device_OD isEqualToString:@"0F E6"]) {
        if ([modelDevice.strDevice_device_type isEqualToString:@"02"]) {
            if ([modelDevice.strDevice_category isEqualToString:@"02"]) {
                return YES;
            }
        }
    }
    return NO;
}

#pragma mark -
#pragma mark - 添加操作
- (void)doHandleAddThreeRoadFirstSwitch:(SHModelDevice *)deviceReport
{
    NSLog(@"开始添加设备");
    if ([deviceReport.strDevice_device_OD isEqualToString:@"0F AA"]) {
        
        if ([deviceReport.strDevice_device_type isEqualToString:@"07"]) {
            if ([deviceReport.strDevice_category isEqualToString:@"02"] || [deviceReport.strDevice_category isEqualToString:@"04"]) {
                self.iRoadCount = 3;
            }else{
                self.iRoadCount = 0;
            }
        }else if ([deviceReport.strDevice_device_type isEqualToString:@"06"]) {
            if ([deviceReport.strDevice_category isEqualToString:@"02"] || [deviceReport.strDevice_category isEqualToString:@"04"]) {
                self.iRoadCount = 2;
            }else{
                self.iRoadCount = 0;
            }
        }else if ([deviceReport.strDevice_device_type isEqualToString:@"05"]) {
            if ([deviceReport.strDevice_category isEqualToString:@"02"] || [deviceReport.strDevice_category isEqualToString:@"04"]) {
                self.iRoadCount = 1;
            }else{
                self.iRoadCount = 0;
            }
        }else{
            
            self.iRoadCount = 0;
        }
        
    }else if ([deviceReport.strDevice_device_OD isEqualToString:@"0F BE"]){
        self.iRoadCount = 0;
    }else if ([deviceReport.strDevice_device_OD isEqualToString:@"0F E6"]){
        self.iRoadCount = 0;
    }else if ([deviceReport.strDevice_device_OD isEqualToString:@"0F C8"]){
        self.iRoadCount = 0;
    }
    NSLog(@"进行%d路设备添加操作",self.iRoadCount);
    [self doHandleAddCommonDevice:deviceReport];
}

- (void)doHandleAddCommonDevice:(SHModelDevice *)deviceReport
{
    self.iRemberCount ++;
    NSLog(@"进行第%d路设备添加",self.iRoadCount);
    NSArray *arrDevicePicList = [[SHAppCommonRequest shareInstance] doGetDeviceUIPicAll];
    CommonAlterView *alter = [[CommonAlterView alloc] initWithFrame:CGRectMake(0,
                                                                               0,
                                                                               UI_SCREEN_WIDTH,
                                                                               UI_SCREEN_HEIGHT)
                                                      dataSourceArr:arrDevicePicList
                                                               type:CommonAlterViewType_Device];
    alter.strDefaultTitle = deviceReport.strDevice_device_name;
    [alter popWithFatherView:self.view];
    
    @weakify(self);
    [alter setBlockGetNameAndPic:^(NSString *strName, SHUIPicModel *picModel) {
        @strongify(self);
        
        SHModelDevice *deviceAdd = [self doHandleGetNewDeviceWithDeviceReprot:deviceReport
                                                                       roomId:self.room.iRoom_id
                                                                   deviceName:strName
                                                                      picName:picModel.strUIPic_name
                                                                   switchRoad:self.iRemberCount];
        [self.mutArrReadyAdd addObject:deviceAdd];
        
        if (self.iRemberCount < self.iRoadCount) {
            
            [self doHandleAddCommonDevice:deviceReport];
            
        }else{
            NSArray *deviceArr = [NSArray arrayWithArray:self.mutArrReadyAdd];
            [self.mutArrReadyAdd removeAllObjects];
            
            SHModelDevice *model = deviceArr[0];
            if ([model.strDevice_device_OD isEqualToString:@"0F E6"]) {
                if ([model.strDevice_sindex_length intValue] == SHSindexLength_InfraredAirCondition) {
                    [XWHUDManager hideInWindow];
                    [self performSegueWithIdentifier:@"seg_to_SHAirHandleTypeVC" sender:model];
                }else{
                    [self doAddDeviceToNetServer:deviceArr];
                }
            }else{
                [self doAddDeviceToNetServer:deviceArr];
            }
        }
    }];
    
    [alter setBlockDimis:^{
        @strongify(self);
        [XWHUDManager hideInWindow];
        [self doClearAllShouldClear];
        [XWHUDManager showErrorTipHUD:@"放弃添加设备！"];
    }];
}

//进行网络添加操作
- (void)doAddDeviceToNetServer:(NSArray *)deviceArr
{
    NSLog(@"获取到设备开始进行网络添加");
    @weakify(self);
    [self.manager handleTheAddDeviceDataWithArrModel:deviceArr completeHandle:^(BOOL success, id result) {
        [self doClearAllShouldClear];
        if (success) {
            [XWHUDManager hideInWindow];
            NSArray *arrDevice = (NSArray *)result;
            
            for (int i = 0; i < arrDevice.count; i ++) {
                SHModelDevice *deviceBack = arrDevice[i];
                //如果是红外设备添加成功则存储起来
                [self doSaveHasUploadSucceedInfraredDeviceMacAddrWithDevice:deviceBack];
                //把已经添加成功的设备存储起来
                [self doSaveHasUploadSucceedCommonDeviceMacAddrWithDevice:deviceBack];
            }
            
            //#warning 需要考虑成功后不拉去网络数据情况下刷新collectionview  已经加入需要测试
            [self doHanleAtOnceAddDeviceSucceedToRefreshList:arrDevice];
            //重新拉取下网络数据
            dispatch_async(dispatch_get_main_queue(), ^{
                @strongify(self);
                [self.manager doGetDeviceListFromNetworkWithRoomID:self.room.iRoom_id];
            });
        }else{
            [XWHUDManager hideInWindow];
            [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",result]];
        }
    }];
}

#pragma mark - 添加完成后，立即刷新列表
- (void)doHanleAtOnceAddDeviceSucceedToRefreshList:(NSArray *)arrDevice
{
    NSMutableArray *mutTemp = [NSMutableArray arrayWithArray:self.commonCollectionView.arrList];
    [mutTemp addObjectsFromArray:arrDevice];
    
    NSMutableArray *mutArrOFAA = [NSMutableArray new];
    NSMutableArray *mutArrOFE6 = [NSMutableArray new];
    NSMutableArray *mutArrOFC8 = [NSMutableArray new];
    NSMutableArray *mutArrOFBE = [NSMutableArray new];
    for (int i = 0; i < mutTemp.count; i ++) {
        SHModelDevice *modeDevcieTemp = mutTemp[i];
        if ([modeDevcieTemp.strDevice_device_OD isEqualToString:@"0F AA"]) {
            
            if ([modeDevcieTemp.strDevice_device_type isEqualToString:@"81"]) {
                [mutArrOFBE addObject:modeDevcieTemp];
            }else
            [mutArrOFAA addObject:modeDevcieTemp];
        }else if ([modeDevcieTemp.strDevice_device_OD isEqualToString:@"0F E6"]){
            [mutArrOFE6 addObject:modeDevcieTemp];
        }else if ([modeDevcieTemp.strDevice_device_OD isEqualToString:@"0F C8"]){
            [mutArrOFC8 addObject:modeDevcieTemp];
        }else{
            [mutArrOFBE addObject:modeDevcieTemp];
        }
    }
    [mutTemp removeAllObjects];
    [mutTemp addObjectsFromArray:mutArrOFAA];
    [mutTemp addObjectsFromArray:mutArrOFE6];
    [mutTemp addObjectsFromArray:mutArrOFC8];
    [mutTemp addObjectsFromArray:mutArrOFBE];
    self.commonCollectionView.arrList = mutTemp;
}

#pragma mark - 存储已经添加的红外设备macAddr
- (void)doSaveHasUploadSucceedInfraredDeviceMacAddrWithDevice:(SHModelDevice *)device
{
    if ([self doJudgeIsInfraredDevice:device])
    {
        if (self.mutArrRemInfraredMacAddr.count == 0)
        {
            [self.mutArrRemInfraredMacAddr addObject:device.strDevice_mac_address];
        }
    }
}

#pragma mark - 存储已经上传后台成功的设备macAddr
- (void)doSaveHasUploadSucceedCommonDeviceMacAddrWithDevice:(SHModelDevice *)device
{
    if ( [self doJudgeHasNotAddDevice:device.strDevice_mac_address])
    {
        [self.mutArrRemMacAddr addObject:device.strDevice_mac_address];
    }
}

- (SHModelDevice *)doHandleGetNewDeviceWithEditDevice:(SHModelDevice *)editDevice
                                           deviceName:(NSString *)strDeviceName
                                              picName:(NSString *)strPicName
{
    SHModelDevice *deviceNew = [SHModelDevice new];
    deviceNew.iDevice_device_id             = editDevice.iDevice_device_id;
    deviceNew.iDevice_room_id               = editDevice.iDevice_room_id;
    deviceNew.strDevice_device_name         = strDeviceName;
    deviceNew.strDevice_image               = strPicName;
    deviceNew.strDevice_device_OD           = editDevice.strDevice_device_OD;
    
    deviceNew.strDevice_device_type         = editDevice.strDevice_device_type;
    deviceNew.strDevice_category            = editDevice.strDevice_category;
    deviceNew.strDevice_sindex              = editDevice.strDevice_sindex;
    deviceNew.strDevice_sindex_length       = editDevice.strDevice_sindex_length;
    
    deviceNew.strDevice_cmdId               = editDevice.strDevice_cmdId;
    deviceNew.strDevice_mac_address         = editDevice.strDevice_mac_address;
    deviceNew.iDevice_device_state1         = editDevice.iDevice_device_state1;
    deviceNew.iDevice_device_state2         = editDevice.iDevice_device_state2;
    
    deviceNew.iDevice_device_state3         = editDevice.iDevice_device_state3;
    deviceNew.strDevice_other_status        = editDevice.strDevice_other_status;
    deviceNew.arrBtns                       = editDevice.arrBtns;
    
    deviceNew.strDCVReal                = editDevice.strDCVReal;
    deviceNew.strCurrentReal            = editDevice.strCurrentReal;
    deviceNew.strUsefulPowerReal        = editDevice.strUsefulPowerReal;
    deviceNew.strElectricQuantityReal   = editDevice.strElectricQuantityReal;
    
    return deviceNew;
}

- (SHModelDevice *)doHandleGetNewDeviceWithDeviceReprot:(SHModelDevice *)deviceReport
                                                 roomId:(int) iRoomId
                                             deviceName:(NSString *)strDeviceName
                                                picName:(NSString *)strPicName
                                             switchRoad:(int) iRoad
{
    
    NSString *strSindexLengthTemp;
    if (self.deviceSindexLength == SHSindexLength_InfraredAirCondition || self.deviceSindexLength == SHSindexLength_InfraredOther_Other) {
        strSindexLengthTemp = [NSString stringWithFormat:@"%ld",(long)self.deviceSindexLength];
        
    }else{
        strSindexLengthTemp = deviceReport.strDevice_sindex_length;
    }
    
    NSString *strOtherStatusTemp;
    if ([deviceReport.strDevice_device_OD isEqualToString:@"0F E6"]
        || [deviceReport.strDevice_device_OD isEqualToString:@"0F BE"]
        || [deviceReport.strDevice_device_OD isEqualToString:@"0F C8"]) {
        strOtherStatusTemp = deviceReport.strDevice_other_status;
    }else{
        strOtherStatusTemp = [NSString stringWithFormat:@"%d",iRoad];
    }
    
    SHModelDevice *deviceAdd = [SHModelDevice new];
    
    deviceAdd.iDevice_room_id               = iRoomId;
    deviceAdd.strDevice_device_name         = strDeviceName;
    deviceAdd.strDevice_image               = strPicName;
    deviceAdd.strDevice_device_OD           = deviceReport.strDevice_device_OD;
    
    deviceAdd.strDevice_device_type         = deviceReport.strDevice_device_type;
    deviceAdd.strDevice_category            = deviceReport.strDevice_category;
    deviceAdd.strDevice_sindex              = deviceReport.strDevice_sindex;
    deviceAdd.strDevice_sindex_length       = strSindexLengthTemp;
    
    deviceAdd.strDevice_cmdId               = deviceReport.strDevice_cmdId;
    deviceAdd.strDevice_mac_address         = deviceReport.strDevice_mac_address;
    deviceAdd.iDevice_device_state1         = deviceReport.iDevice_device_state1;
    deviceAdd.iDevice_device_state2         = deviceReport.iDevice_device_state2;
    
    deviceAdd.iDevice_device_state3         = deviceReport.iDevice_device_state3;
    deviceAdd.strDevice_other_status        = strOtherStatusTemp;
    deviceAdd.arrBtns                       = deviceReport.arrBtns;
    
    deviceAdd.strDCVReal                = deviceReport.strDCVReal;
    deviceAdd.strCurrentReal            = deviceReport.strCurrentReal;
    deviceAdd.strUsefulPowerReal        = deviceReport.strUsefulPowerReal;
    deviceAdd.strElectricQuantityReal   = deviceReport.strElectricQuantityReal;
    
    return deviceAdd;
}

#pragma mark -
#pragma mark - 编辑和删除
- (void)doAddActionSheetAboutDeleteAndEdit:(SHModelDevice *)deviceEdit
{
    @weakify(self);
    LCActionSheet *actionSheetEdit = [LCActionSheet sheetWithTitle:@"删除无法恢复"
                                                 cancelButtonTitle:@"取消"
                                                           clicked:^(LCActionSheet * _Nonnull actionSheet, NSInteger buttonIndex) {
                                                               @strongify(self);
                                                               switch (buttonIndex) {
                                                                   case 1:
                                                                   {
                                                                       NSLog(@"走了编辑");
                                                                       NSArray *arrDevicePicList = [[SHAppCommonRequest shareInstance] doGetDeviceUIPicAll];
                                                                       CommonAlterView *alter = [[CommonAlterView alloc] initWithFrame:CGRectMake(0,
                                                                                                                                                  0,
                                                                                                                                                  UI_SCREEN_WIDTH,
                                                                                                                                                  UI_SCREEN_HEIGHT)
                                                                                                                         dataSourceArr:arrDevicePicList
                                                                                                                                  type:CommonAlterViewType_Device];
                                                                       alter.strDefaultTitle = deviceEdit.strDevice_device_name;
                                                                       [alter popWithFatherView:self.view];
                                                                       
                                                                       @weakify(self);
                                                                       [alter setBlockGetNameAndPic:^(NSString *strName, SHUIPicModel *picModel) {
                                                                           @strongify(self);
                                                                           [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
                                                                           SHModelDevice *deviceNew = [self doHandleGetNewDeviceWithEditDevice:deviceEdit
                                                                                                                                    deviceName:strName
                                                                                                                                       picName:picModel.strUIPic_name];
                                                                           [self.manager handleTheUpdateDeviceDataWithModel:deviceNew
                                                                                                             completeHandle:^(BOOL success, id result)
                                                                            {
                                                                                if (success) {
                                                                                    [XWHUDManager hideInWindow];
                                                                                      [XWHUDManager showSuccessTipHUD:@"更新成功"];
                                                                                    [self.manager doGetDeviceListFromNetworkWithRoomID:self.room.iRoom_id];
                                                                                    
                                                                                }else{
                                                                                    [XWHUDManager hideInWindow];
                                                                                   
                                                                                    
                                                                                    [XWHUDManager showErrorTipHUD:@"更新失败"];
                                                                                }
                                                                            }];
                                                                       }];
                                                                       
                                                                       [alter setBlockDimis:^{
                                                                           @strongify(self);
                                                                           [XWHUDManager hideInWindow];
                                                                           [self doClearAllShouldClear];
                                                                           
                                                                           [XWHUDManager showErrorTipHUD:@"放弃添加设备！"];
                                                                       }];
                                                                   }
                                                                       break;
                                                                   case 2:
                                                                   {
                                                                       NSLog(@"走了删除");
                                                                       if ([self isTwoRoadSwitch:deviceEdit]) {
                                                                           [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
                                                                           @weakify(self);
                                                                           [self.manager handleDeleteDeviceByMacAddr:deviceEdit.strDevice_mac_address completeHandle:^(BOOL success, id result)
                                                                            {
                                                                                @strongify(self);
                                                                                if (success) {
                                                                                    [XWHUDManager hideInWindow];
                                                                                    [XWHUDManager showSuccessTipHUD:@"删除成功"];
                                                                                    
                                                                                    [self.manager doGetDeviceListFromNetworkWithRoomID:self.room.iRoom_id];
                                                                                }else{
                                                                                    [XWHUDManager hideInWindow];
                                                                                    [XWHUDManager showErrorTipHUD:@"删除失败"];
                                                                                }
                                                                            }];
                                                                       }else if ([self isThreeRoadSwitch:deviceEdit]){
                                                                           
                                                                           [self.manager handleDeleteDeviceByMacAddr:deviceEdit.strDevice_mac_address completeHandle:^(BOOL success, id result)
                                                                            {
                                                                                if (success) {
                                                                                    [XWHUDManager hideInWindow];
                                                                                    [XWHUDManager showSuccessTipHUD:@"删除成功"];
                                                                                    [self.manager doGetDeviceListFromNetworkWithRoomID:self.room.iRoom_id];
                                                                                }else{
                                                                                    [XWHUDManager hideInWindow];
                                                                                    [XWHUDManager showErrorTipHUD:@"删除失败"];
                                                                                }
                                                                            }];
                                                                       }else{
                                                                           [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
                                                                           [self.manager handleDeleteDeviceByDeviceId:deviceEdit.iDevice_device_id completeHandle:^(BOOL success, id result)
                                                                            {
                                                                                if (success) {
                                                                                    [XWHUDManager hideInWindow];
                                                                                    
                                                                                    if (self.mutArrRemInfraredMacAddr.count != 0) {
                                                                                        [self.mutArrRemInfraredMacAddr removeObject:deviceEdit.strDevice_mac_address];
                                                                                    }
                                                                                    
                                                                                    [XWHUDManager showSuccessTipHUD:@"删除成功"];
                                                                                    [self.manager doGetDeviceListFromNetworkWithRoomID:self.room.iRoom_id];
                                                                                }else{
                                                                                    
                                                                                    [XWHUDManager hideInWindow];
                                                                                    [XWHUDManager showErrorTipHUD:@"删除失败"];
                                                                                }
                                                                            }];
                                                                       }
                                                                   }
                                                                       break;
                                                                   case 0:
                                                                   {
                                                                       NSLog(@"走了取消");
                                                                   }
                                                                       break;
                                                                       
                                                                   default:
                                                                       break;
                                                               }
                                                           }
                                                 otherButtonTitles:@"编辑",@"删除", nil];
    [actionSheetEdit show];
    
    
}

//#warning  暂时未用
- (void)doHandleDeleteTheRemeberDevice:(NSString *)strDeviceMacAddress
{
    
    NSMutableArray *mutArr = [NSMutableArray arrayWithArray:self.mutArrRemMacAddr];
    
    for (int i = 0; i < self.mutArrRemMacAddr.count; i ++) {
        NSString *strMacOld = self.mutArrRemMacAddr[i];
        if ([strMacOld isEqualToString:strDeviceMacAddress]) {
            [mutArr removeObject:strMacOld];
        }
    }
    [self.mutArrRemMacAddr removeAllObjects];
    self.mutArrRemMacAddr = [NSMutableArray arrayWithArray:mutArr];
    
}

#pragma mark -
#pragma mark - 懒加载
-(SHLockManager *)lockManager
{
    if (!_lockManager) {
        _lockManager = [SHLockManager new];
    }
    return _lockManager;
}

- (CommonCollectionView *)commonCollectionView
{
    if (!_commonCollectionView) {
        UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
        flowLayout.sectionHeadersPinToVisibleBounds = YES;
        [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
        _commonCollectionView = [[CommonCollectionView alloc] initWithFrame:CGRectMake(0,
                                                                                       64,
                                                                                       UI_SCREEN_WIDTH,
                                                                                       UI_SCREEN_HEIGHT - 64)
                                                       collectionViewLayout:flowLayout type:CommonCollectionViewType_Edit];
    }
    return _commonCollectionView;
}

- (void)doAddCommonCollectionViewConstraints
{
    @weakify(self);
    [self.commonCollectionView mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.top.equalTo(self.view.mas_top);
        make.left.equalTo(self.view.mas_left);
        make.right.equalTo(self.view.mas_right);
        make.bottom.equalTo(self.view.mas_bottom);
    }];
}

- (SHDeviceManager *)manager
{
    if (!_manager) {
        _manager = [SHDeviceManager new];
    }
    return _manager;
}

- (NSMutableArray *)mutArrRemMacAddr
{
    if (!_mutArrRemMacAddr) {
        _mutArrRemMacAddr = [NSMutableArray new];
    }
    return _mutArrRemMacAddr;
}

- (NSMutableArray *)mutArrRemInfraredMacAddr
{
    if (!_mutArrRemInfraredMacAddr) {
        _mutArrRemInfraredMacAddr = [NSMutableArray new];
    }
    return _mutArrRemInfraredMacAddr;
}

- (NSMutableArray *)mutArrRemberReportDevice
{
    if (!_mutArrRemberReportDevice) {
        _mutArrRemberReportDevice = [NSMutableArray new];
    }
    return _mutArrRemberReportDevice;
}

- (NSMutableArray *)mutArrReadyAdd
{
    if (!_mutArrReadyAdd) {
        _mutArrReadyAdd = [NSMutableArray new];
    }
    return _mutArrReadyAdd;
}

- (NSArray *)arrNotAddHouseNO
{
    if (!_arrNotAddHouseNO) {
        _arrNotAddHouseNO = [NSArray new];
    }
    return _arrNotAddHouseNO;
}


#pragma mark -
#pragma mark -  private获取红外设备还没有被添加的按钮
- (NSMutableArray *)doHandleGetArrHasAddedIfraredBtnList:(NSArray *)arr
{
    NSMutableArray *mutArrAddBtns = [NSMutableArray new];
    for (int i = 0; i < arr.count; i ++) {
        SHModelDevice *deviceTemp = arr[i];
        for (int j = 0; j < deviceTemp.arrBtns.count; j ++) {
            SHInfraredKeyModel *keyModel = deviceTemp.arrBtns[j];
            [mutArrAddBtns addObject:keyModel.strWarehouseNO];
        }
    }
    return mutArrAddBtns;
}

- (NSMutableArray *)doHandleGetArrAllIfraredBtnList
{
    NSMutableArray *mutAll = [[NSMutableArray alloc] init];
    for (int i = 0; i < 100; i ++) {
        [mutAll addObject:[NSString stringWithFormat:@"%d",i]];
    }
    return mutAll;
}

- (NSArray *)doHandleGetNotAddHouseNOWithAllDevice:(NSArray *)arrAllDeivce
{
    NSArray *allList = [self doHandleGetArrAllIfraredBtnList];
    NSArray *arrhasAdded = [self doHandleGetArrHasAddedIfraredBtnList:arrAllDeivce];
    
    NSPredicate *predicate1 = [NSPredicate predicateWithFormat:@"NOT (SELF in %@)",arrhasAdded];
    
    NSArray *temp1 = [allList filteredArrayUsingPredicate:predicate1];
    
    //    NSLog(@"还没有添加的houseNO:%@",temp1);
    return temp1;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark -
#pragma mark - private

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
    
    NSData *dataSend = [[NetworkEngine shareInstance] doGetSwitchControlWithTargetAddr:device.strDevice_mac_address
                                                                                  device:device
                                                                                     way:[device.strDevice_other_status intValue]
                                                                             controlMode:@"01"
                                                                            controlState:strHexState];
    NSString *strHexWholeData = [[ToolHexManager sharedManager] convertDataToHexStr:dataSend];
    NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexWholeData];
    NSLog(@"0FAA进行控制页面:\n%@",strBigTemp);
    [[NetworkEngine shareInstance] sendRequestData:dataSend];
}

#pragma mark -
#pragma mark - 计量插座
- (void)doHandleTheMeasureDevice:(SHModelDevice *)device
{
    
    NSString *strTitle = [NSString stringWithFormat:@"%@详细用电信息",device.strDevice_device_name];
    //    NSString *strDetail = [NSString stringWithFormat:@"电压:%@;\n电流:%@;\n功率:%@;\n电量:%@",device.strDCVReal,device.strCurrentReal,device.strUsefulPowerReal,device.strElectricQuantityReal];
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

#pragma mark -
#pragma mark - 计量插座控制
- (void)doHandleMeasureDevice4040ControlDevice:(SHModelDevice *)device strHexState:(NSString *)strHexState
{
    //    if (device.iDevice_device_state1  == 1) {
    //        strHexState = @"02";
    //    }else{
    //
    //        strHexState = @"01";
    //    }
    
    NSData *dataSend = [[NetworkEngine shareInstance] doGetMeasureDeviceControlWithTargetAddr:device.strDevice_mac_address
                                                                                         device:device
                                                                                   controlState:strHexState];
    NSString *strHexWholeData = [[ToolHexManager sharedManager] convertDataToHexStr:dataSend];
    NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexWholeData];
    NSLog(@"0FC8进行控制页面:\n%@",strBigTemp);
    [[NetworkEngine shareInstance] sendRequestData:dataSend];
}




#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    if ([segue.identifier isEqualToString:@"seg_to_SHAirHandleTypeVC"]) {
        SHAirHandleTypeVC *VC = segue.destinationViewController;
        VC.device = (SHModelDevice *)sender;
    }
    
}

@end


