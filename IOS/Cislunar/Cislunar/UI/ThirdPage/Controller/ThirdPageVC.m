//
//  ThirdPageVC.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/14.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "ThirdPageVC.h"
#import "SecurityTableView.h"
#import "SecurityHeaderView.h"
#import "ModelSecurity.h"
#import "EZDeviceTableViewController.h"
#import "SHDeviceManager.h"
#import "FirstPageManager.h"


@interface ThirdPageVC ()

@property (strong, nonatomic) SecurityTableView *tableView;
@property (strong, nonatomic) SHDeviceManager *manager;
@property (strong, nonatomic) SecurityHeaderView *tableHeaderView;
@property (assign, nonatomic) NetworkEngine *networkEngine;
@property (strong, nonatomic) NSString *strHexArmingIndentifer; //是点击了布防还是撤防
@property (strong, nonatomic) FirstPageManager *firstManager;

@property (strong, nonatomic) NSMutableArray *mutArrAllDevice;

@end

@implementation ThirdPageVC

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.tabBarController.tabBar.hidden = NO;
//    self.isHideNaviBar = YES;
}

-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
//    self.isHideNaviBar = NO;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.isHideNaviBar = YES;
    [self doInitSubViews];
    [self doRegisterKVO];
    [self doLoadData];
    [self doAddActionThird];
    
    NSLog(@"*********************%lu",self.tabBarController.selectedIndex);
    // Do any additional setup after loading the view.
}

- (void)doInitSubViews
{
    [self.view addSubview:self.tableView];
    [self.tableView setTableHeaderView:self.tableHeaderView];
}

#pragma mark -
#pragma mark - loadData
- (void)doLoadData
{
    //撤防布防的出事状态
    NSString *strSecurityStaute = [[SHLoginManager shareInstance] doGetSecurityStatus];
    if ([strSecurityStaute intValue] == 1) {
        //布防
        self.tableHeaderView.isArming = YES;
    }else{
        //撤防
        self.tableHeaderView.isArming = NO;
    }
    
    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
    [self.manager doGetAllDeviceListDataFromDBV2];
    [self.manager doGetAllDeviceListFromNetworkV2];
}

#pragma mark -
#pragma mark - 询问所有4010设备的状态
- (void)doAskAll4010Device
{
    NSData *data = [[NetworkEngine shareInstance] doGetRefreshDeviceStatue];
    [[NetworkEngine shareInstance] sendRequestData:data];
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
                       NSArray *arrNetOrDB = (NSArray *)value;
                       
                       //遍历添加所有房间的设备到数组arrTemp里面
                       [self.mutArrAllDevice removeAllObjects];
                       for (int i = 0; i < arrNetOrDB.count; i ++) {
                           SHModelRoom *modelRoom = arrNetOrDB[i];
                           for (int j = 0; j < modelRoom.arrDeviceList.count; j ++) {
                               SHModelDevice *modelDevice = modelRoom.arrDeviceList[j];
                               [self.mutArrAllDevice addObject:modelDevice];
                           }
                       }
                       [self doLoadDatasourceWithArr:self.mutArrAllDevice];
                       NSLog(@"*****arrResult = %@",self.mutArrAllDevice);
                   }];
    
    [self observeKeyPath:@keypath(self.manager.errorInfo)
                   block:^(id value) {
                       [XWHUDManager hideInWindow];
                       NSDictionary *dict = (NSDictionary *)value;
                       [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",dict[@"message"]]];
                   }];
    
    //设防布防返回信息处理
    [self observeKeyPath:@keypath(self.networkEngine.screenNew)
                   block:^(id value)
     {
         @strongify(self);
         
         if (self.tabBarController.selectedIndex == 3) {
             
             [XWHUDManager hideInWindow];
             SHModelScreenNew *screen = value;
             if ([screen.strCmdID isEqualToString:@"50"]) {
                 if ([screen.strSubcommandIdentifer isEqualToString:@"05"]) {
                     if ([screen.strAnswerIdentifer isEqualToString:@"00"]) {
                         //设防或者布防成功
                         [self doSetArmingOrDisarmingToServer];
                     }
                 }else if ([screen.strSubcommandIdentifer isEqualToString:@"08"]){
                     if ([screen.strAnswerIdentifer isEqualToString:@"00"]) {
                         //解除报警成功
                          [XWHUDManager showSuccessTipHUD:@"解除报警成功！"];
                     }
                 }
             }else if ([screen.strCmdID isEqualToString:@"D0"]){
                 //设防或者布防成功
                 if ([self.strHexArmingIndentifer isEqualToString:@"01"] || [self.strHexArmingIndentifer isEqualToString:@"02"]) {
                     [XWHUDManager showErrorTipHUD:@"操作失败"];
                 }
                 
             }else{
                 if ([self.strHexArmingIndentifer isEqualToString:@"01"] || [self.strHexArmingIndentifer isEqualToString:@"02"]) {
                     [XWHUDManager showWarningTipHUD:@"其他原因"];
                 }
             }
         }
     }];
    
    //监测设备上报添加
    [self observeKeyPath:@keypath(self.networkEngine.modelDevice) block:^(id value) {
        @strongify(self);
        SHModelDevice *deviceReport = (SHModelDevice *)value;
        // GLOG_INFO(@"处于普通上报刷新状态");
        NSMutableArray *mutArrDevice = [NSMutableArray new];
        for (int i = 0; i < self.mutArrAllDevice.count; i ++) {
            SHModelDevice *modelDevice = self.mutArrAllDevice[i];
            if ([modelDevice.strDevice_mac_address isEqualToString:deviceReport.strDevice_mac_address]) {
                
                SHModelDevice *deviceNew        = [SHModelDevice new];
                deviceNew.iDevice_device_id     = modelDevice.iDevice_device_id;
                deviceNew.iDevice_room_id       = modelDevice.iDevice_room_id;
                deviceNew.strDevice_device_name = modelDevice.strDevice_device_name;
                deviceNew.strDevice_image       = modelDevice.strDevice_image;
                
                deviceNew.strDevice_room_name  = modelDevice.strDevice_room_name;
                deviceNew.strDevice_floor_name = modelDevice.strDevice_floor_name;
                
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
                
                
                [mutArrDevice addObject:deviceNew];
            }else{
                [mutArrDevice addObject:modelDevice];
            }
        }
        
        [self doLoadDatasourceWithArr:mutArrDevice];
    }];
    
}

#pragma mark -
#pragma mark -  设防或者布防后，网关返回来的数据进行处理
//布防设防到cloudsever
- (void)doSetArmingOrDisarmingToServer
{
    if ([self.strHexArmingIndentifer isEqualToString:@"01"]) {
        //布防
        self.tableHeaderView.isArming = YES;
        
        [self.firstManager handleSetTheSecurityStatus:1 callBackHandle:^(BOOL success, id result) {
            if (success) {
                  [XWHUDManager showSuccessTipHUD:@"布防成功"];
            }else{
                [XWHUDManager showErrorTipHUD:@"布防失败"];
            }
        }];
    }else if ([self.strHexArmingIndentifer isEqualToString:@"02"]){
        //撤防
        self.tableHeaderView.isArming = NO;
        
        [self.firstManager handleSetTheSecurityStatus:0 callBackHandle:^(BOOL success, id result) {
            if (success) {
                 [XWHUDManager showSuccessTipHUD:@"撤防成功"];
            }else{
                [XWHUDManager showErrorTipHUD:@"撤防失败"];
            }
        }];
    }else{
        NSLog(@"设防和布防出现意外情况，请联系开发人员");
    }
}

    
#pragma mark -
#pragma mark - doAddAction
- (void)doAddActionThird
{
    @weakify(self);
    [self.tableView setBlockAnfangSectionPressed:^(NSInteger iSection) {
        @strongify(self);
        NSLog(@"ddddddddddddddddddd==%ld",(long)iSection);
        if (iSection == 0) {
            //摄像头
            //    //获取EZMain的stroyboard文件
            //    UIStoryboard *ezMainStoryboard = [UIStoryboard storyboardWithName:@"EZMain" bundle:nil];
            //    //获取EZMain.storyboard的实例ViewController--获取摄像头列表
            //    UIViewController *instanceVC = [ezMainStoryboard instantiateViewControllerWithIdentifier:@"EZCameraList"];
            //    //push摄像头列表的viewController
            //    [self.navigationController pushViewController:instanceVC animated:YES];
            
            /**
             *  下面代码功能与以上的注释方法相同
             */
            [self performSegueWithIdentifier:@"go2CameraList" sender:nil];
        }else{
            self.tableView.isOpen = !self.tableView.isOpen;
            NSIndexSet *indexSet = [[NSIndexSet alloc] initWithIndex:0];
            [self.tableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationFade];
        }        
    }];
    
    
    [self.tableHeaderView setBlockCollectionHeaderSelected:^(UIButton *btn, UIImageView *imageV, BOOL isArming) {
        @strongify(self);
        
        [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
//        self.tableHeaderView.isArming = !self.tableHeaderView.isArming;
        if (self.tableHeaderView.isArming) {
            self.strHexArmingIndentifer = @"02";
        }else{
            
            self.strHexArmingIndentifer = @"01";
        }
        NSData *data = [[NetworkEngine shareInstance] doHandleSendArmingOrDisarmingOrderToControlWithArmOrDisarmingIdentifer:self.strHexArmingIndentifer];
        [[NetworkEngine shareInstance] sendRequestData:data];
        
    }];
    
    [self.tableHeaderView setBlockBtnCancellAlarmPressed:^(UIButton *btnCancellAlarm) {
        NSData *data = [[NetworkEngine shareInstance] doHandleDisarmingGatewayOrderToControl];
        [[NetworkEngine shareInstance] sendRequestData:data];
    }];
}

#pragma mark -
#pragma mark - 解除报警
- (void)doSendDisAlarming
{
    NSData *data = [[NetworkEngine shareInstance] doHandleDisarmingGatewayOrderToControl];
    [[NetworkEngine shareInstance] sendRequestData:data];
}

    
    

#pragma mark -
#pragma mark - load data
- (void)doLoadDatasourceWithArr:(NSArray<SHModelDevice *> *)arrTemp
{
    
//    SHModelDevice *modelDevice = [SHModelDevice new];
//    modelDevice.strDevice_device_name = @"门磁";
//    modelDevice.strDevice_floor_name = @"一楼";
//    modelDevice.strDevice_room_name = @"客厅";
//    modelDevice.strDevice_device_OD = @"0FBE";
//    modelDevice.strDevice_device_type = @"01";
//    modelDevice.strDevice_category = @"02";
//    modelDevice.iDevice_device_state1 = 0;
//
//    SHModelDevice *modelDevice1 = [SHModelDevice new];
//    modelDevice1.strDevice_device_name = @"人体红外";
//    modelDevice1.strDevice_floor_name = @"一楼";
//    modelDevice1.strDevice_room_name = @"客厅";
//    modelDevice1.strDevice_device_OD = @"0FBE";
//    modelDevice1.strDevice_device_type = @"86";
//    modelDevice1.strDevice_category = @"02";
//    modelDevice1.iDevice_device_state1 = 1;
//
//    SHModelDevice *modelDevice2 = [SHModelDevice new];
//    modelDevice2.strDevice_device_name = @"门磁2";
//    modelDevice2.strDevice_floor_name = @"一楼";
//    modelDevice2.strDevice_room_name = @"客厅";
//    modelDevice2.strDevice_device_OD = @"0FBE";
//    modelDevice2.strDevice_device_type = @"01";
//    modelDevice2.strDevice_category = @"02";
//    modelDevice2.iDevice_device_state1 = 1;
//
//    SHModelDevice *modelDevice3 = [SHModelDevice new];
//    modelDevice3.strDevice_device_name = @"门磁3";
//    modelDevice3.strDevice_floor_name = @"一楼";
//    modelDevice3.strDevice_room_name = @"客厅";
//    modelDevice3.strDevice_device_OD = @"0FBE";
//    modelDevice3.strDevice_device_type = @"01";
//    modelDevice3.strDevice_category = @"02";
//    modelDevice3.iDevice_device_state1 = 0;
//
//    SHModelDevice *modelDevice4 = [SHModelDevice new];
//    modelDevice4.strDevice_device_name = @"门磁4";
//    modelDevice4.strDevice_floor_name = @"一楼";
//    modelDevice4.strDevice_room_name = @"客厅";
//    modelDevice4.strDevice_device_OD = @"0FBE";
//    modelDevice4.strDevice_device_type = @"01";
//    modelDevice4.strDevice_category = @"02";
//    modelDevice4.iDevice_device_state1 = 1;
//
//    SHModelDevice *modelDevice5 = [SHModelDevice new];
//    modelDevice5.strDevice_device_name = @"门磁5";
//    modelDevice5.strDevice_floor_name = @"一楼";
//    modelDevice5.strDevice_room_name = @"客厅";
//    modelDevice5.strDevice_device_OD = @"0FBE";
//    modelDevice5.strDevice_device_type = @"01";
//    modelDevice5.strDevice_category = @"02";
//    modelDevice5.iDevice_device_state1 = 1;
//
//    NSArray *arrTemp = @[modelDevice,modelDevice1,modelDevice3,modelDevice4,modelDevice5];
    
    ModelSecurity *modelCamera = [ModelSecurity new];
    modelCamera.strName = @"摄像头";
    modelCamera.strIcon = @"摄像头";
    modelCamera.iShowDetail = NO;
    modelCamera.iRedDotCount = 0;
    modelCamera.strType = @"000";
    
    
    
    ModelSecurity *modelSecurity_4010_8102_HumanBodyInfrared = [self doGetModelSensor_4010_8102_HumanBodyInfrared:arrTemp];
    ModelSecurity *modelSecurity_4010_8103_CO = [self doGetModelSensor_4010_8103_CO:arrTemp];
    ModelSecurity *modelSecurity_4010_8104_StrongSmoke = [self doGetModelSensor_4010_8104_StrongSmoke:arrTemp];
    
    ModelSecurity *modelSecurity_4020_0102_TemperatureHumidity = [self doGetModelSensor_4020_0102_TemperatureHumidity:arrTemp];
    ModelSecurity *modelSecurity_4020_0202_Brightness = [self doGetModelSensor_4020_0202_Brightness:arrTemp];
    
    ModelSecurity *modelSecurity_4030_0102_Door = [self doGetModelSensor_4030_0102_Door:arrTemp];
    ModelSecurity *modelSecurity_4030_0302_Gas  = [self doGetModelSensor_4030_0302_Gas:arrTemp];
    ModelSecurity *modelSecurity_4030_0402_WallMountHumanBodyInfrared = [self doGetModelSensor_4030_0402_WallMountHumanBodyInfrared:arrTemp];
    ModelSecurity *modelSecurity_4030_0502_WaterLeakage = [self doGetModelSensor_4030_0502_WaterLeakage:arrTemp];
    
    ModelSecurity *modelSecurity_4030_0702_WeakSmoke = [self doGetModelSensor_4030_0702_WeakSmoke:arrTemp];
    ModelSecurity *modelSecurity_4030_8102_Door = [self doGetModelSensor_4030_8102_Door:arrTemp];
    ModelSecurity *modelSecurity_4030_8103_Window = [self doGetModelSensor_4030_8103_Window:arrTemp];
    
    ModelSecurity *modelSecurity_4030_8302_WaterLeakage = [self doGetModelSensor_4030_8302_WaterLeakage:arrTemp];
    ModelSecurity *modelSecurity_4030_8602_HumanBodyInfrared = [self doGetModelSensor_4030_8602_HumanBodyInfrared:arrTemp];
    
    NSMutableArray *mutArrTemp = [NSMutableArray new];
    
    [mutArrTemp addObject:modelCamera];
    
    if (modelSecurity_4010_8102_HumanBodyInfrared.arrDeviceList.count) {
        [mutArrTemp addObject: modelSecurity_4010_8102_HumanBodyInfrared];
    }
    if (modelSecurity_4010_8103_CO.arrDeviceList.count) {
        [mutArrTemp addObject: modelSecurity_4010_8103_CO];
    }
    if (modelSecurity_4010_8104_StrongSmoke.arrDeviceList.count) {
        [mutArrTemp addObject: modelSecurity_4010_8104_StrongSmoke];
    }
    
    if (modelSecurity_4020_0102_TemperatureHumidity.arrDeviceList.count) {
        [mutArrTemp addObject: modelSecurity_4020_0102_TemperatureHumidity];
    }
    if (modelSecurity_4020_0202_Brightness.arrDeviceList.count) {
        [mutArrTemp addObject: modelSecurity_4020_0202_Brightness];
    }
    if (modelSecurity_4030_0102_Door.arrDeviceList.count) {
        [mutArrTemp addObject: modelSecurity_4030_0102_Door];
    }
    if (modelSecurity_4030_0302_Gas.arrDeviceList.count) {
        [mutArrTemp addObject: modelSecurity_4030_0302_Gas];
    }
    if (modelSecurity_4030_0402_WallMountHumanBodyInfrared.arrDeviceList.count) {
        [mutArrTemp addObject: modelSecurity_4030_0402_WallMountHumanBodyInfrared];
    }
    if (modelSecurity_4030_0502_WaterLeakage.arrDeviceList.count) {
        [mutArrTemp addObject: modelSecurity_4030_0502_WaterLeakage];
    }
    if (modelSecurity_4030_0702_WeakSmoke.arrDeviceList.count) {
        [mutArrTemp addObject:modelSecurity_4030_0702_WeakSmoke ];
    }
    if (modelSecurity_4030_8102_Door.arrDeviceList.count) {
        [mutArrTemp addObject: modelSecurity_4030_8102_Door];
    }
    if (modelSecurity_4030_8103_Window.arrDeviceList.count) {
        [mutArrTemp addObject:modelSecurity_4030_8103_Window ];
    }
    if (modelSecurity_4030_8302_WaterLeakage.arrDeviceList.count) {
        [mutArrTemp addObject:modelSecurity_4030_8302_WaterLeakage ];
    }
    if (modelSecurity_4030_8602_HumanBodyInfrared.arrDeviceList.count) {
        [mutArrTemp addObject: modelSecurity_4030_8602_HumanBodyInfrared];
    }
    
    self.tableView.arrDatasource = mutArrTemp;
    self.tableView.isOpen = NO;
    
//    @weakify(self);
//    [self.tableView setBlockAnfangSectionPressed:^(NSInteger iSection) {
//        @strongify(self);
//        self.tableView.isOpen = !self.tableView.isOpen;
//        NSIndexSet *indexSet = [[NSIndexSet alloc] initWithIndex:0];
//        [self.tableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationFade];
//    }];
    
}





#pragma mark -
#pragma mark - 懒加载

-(NSMutableArray *)mutArrAllDevice
{
    if (!_mutArrAllDevice) {
        _mutArrAllDevice = [NSMutableArray new];
    }
    return _mutArrAllDevice;
}

-(FirstPageManager *)firstManager
{
    if (!_firstManager) {
        _firstManager = [FirstPageManager new];
    }
    return _firstManager;
}

-(NetworkEngine *)networkEngine
{
    if (!_networkEngine) {
        _networkEngine = [NetworkEngine shareInstance];
    }
    return _networkEngine;
}

- (SHDeviceManager *)manager
{
    if (!_manager) {
        _manager = [SHDeviceManager new];
    }
    return _manager;
}

-(SecurityTableView *)tableView
{
    if (!_tableView) {
        _tableView = [[SecurityTableView alloc] initWithFrame:CGRectMake(0,
                                                                         0,
                                                                         UI_SCREEN_WIDTH,
                                                                         UI_SCREEN_HEIGHT - 49)
                                                        style:UITableViewStyleGrouped];
    }
    return _tableView;
}

- (SecurityHeaderView *)tableHeaderView
{
    if (!_tableHeaderView) {
        _tableHeaderView = [[[NSBundle mainBundle] loadNibNamed:@"SecurityHeaderView"
                                                          owner:self
                                                        options:nil] lastObject];
    }
    return  _tableHeaderView;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark -
#pragma mark -  数据处理
//4010
- (ModelSecurity *)doGetModelSensor_4010_8102_HumanBodyInfrared:(NSArray *)arrOgin
{
    int iTemp = 0;
    NSMutableArray * mutArrSensor_4010_8102_HumanBodyInfrared = [NSMutableArray new];
    for (int i = 0; i < arrOgin.count; i ++) {
        SHModelDevice *modelDevice = arrOgin[i];
        
        if ([modelDevice.strDevice_device_OD isEqualToString:@"0FAA"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0F AA"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0faa"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0f aa"]){
            if ([modelDevice.strDevice_device_type isEqualToString:@"81"]) {
                
                if ([modelDevice.strDevice_category isEqualToString:@"02"]) {
                    [mutArrSensor_4010_8102_HumanBodyInfrared  addObject:modelDevice];
                }else{}
                
                if (modelDevice.iDevice_device_state1 == 1 ) {
                    iTemp ++;
                }
            }
        }
    }
    ModelSecurity *modelSecurity_4010_8102_HumanBodyInfrared = [ModelSecurity new];
    NSString * strName = @"强电人体热释传感器";
    NSString * strIcon = @"人体红外";
    NSString *strType = @"40108102";
    modelSecurity_4010_8102_HumanBodyInfrared.strIcon = strIcon;
    modelSecurity_4010_8102_HumanBodyInfrared.strName = strName;
    modelSecurity_4010_8102_HumanBodyInfrared.strType = strType;
    modelSecurity_4010_8102_HumanBodyInfrared.arrDeviceList = mutArrSensor_4010_8102_HumanBodyInfrared;
    modelSecurity_4010_8102_HumanBodyInfrared.iRedDotCount = iTemp;
    return modelSecurity_4010_8102_HumanBodyInfrared;
}

- (ModelSecurity *)doGetModelSensor_4010_8103_CO:(NSArray *)arrOgin
{
    int iTemp = 0;
    NSMutableArray * mutArrSensor_4010_8103_CO = [NSMutableArray new];
    for (int i = 0; i < arrOgin.count; i ++) {
        SHModelDevice *modelDevice = arrOgin[i];
        
        if ([modelDevice.strDevice_device_OD isEqualToString:@"0FAA"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0F AA"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0faa"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0f aa"]){
            if ([modelDevice.strDevice_device_type isEqualToString:@"81"]) {
                
                if ([modelDevice.strDevice_category isEqualToString:@"03"]) {
                    [mutArrSensor_4010_8103_CO  addObject:modelDevice];
                }else{}
                if (modelDevice.iDevice_device_state1 == 1 ) {
                    iTemp ++;
                }
            }
        }
    }
    ModelSecurity *modelSecurity_4010_8103_CO = [ModelSecurity new];
    NSString * strName = @"强电一氧化碳传感器";
    NSString * strIcon = @"CO感应器";
    NSString *strType = @"40108103";
    modelSecurity_4010_8103_CO.strIcon = strIcon;
    modelSecurity_4010_8103_CO.strName = strName;
    modelSecurity_4010_8103_CO.strType = strType;
    modelSecurity_4010_8103_CO.arrDeviceList = mutArrSensor_4010_8103_CO;
    modelSecurity_4010_8103_CO.iRedDotCount = iTemp;
    return modelSecurity_4010_8103_CO;
}

- (ModelSecurity *)doGetModelSensor_4010_8104_StrongSmoke:(NSArray *)arrOgin
{
    int iTemp = 0;
    NSMutableArray * mutArrSensor_4010_8104_StrongSmoke = [NSMutableArray new];
    for (int i = 0; i < arrOgin.count; i ++) {
        SHModelDevice *modelDevice = arrOgin[i];
        
        if ([modelDevice.strDevice_device_OD isEqualToString:@"0FAA"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0F AA"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0faa"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0f aa"]){
            if ([modelDevice.strDevice_device_type isEqualToString:@"81"]) {
                
                if ([modelDevice.strDevice_category isEqualToString:@"04"]) {
                    [mutArrSensor_4010_8104_StrongSmoke  addObject:modelDevice];
                }else{}
                
                if (modelDevice.iDevice_device_state1 == 1 ) {
                    iTemp ++;
                }
            }
        }
    }
    ModelSecurity *modelSecurity_4010_8104_StrongSmoke = [ModelSecurity new];
    NSString * strName = @"强电烟雾传感器";
    NSString * strIcon = @"烟感";
    NSString *strType = @"40108104";
    modelSecurity_4010_8104_StrongSmoke.strIcon = strIcon;
    modelSecurity_4010_8104_StrongSmoke.strName = strName;
    modelSecurity_4010_8104_StrongSmoke.strType = strType;
    modelSecurity_4010_8104_StrongSmoke.arrDeviceList = mutArrSensor_4010_8104_StrongSmoke;
    modelSecurity_4010_8104_StrongSmoke.iRedDotCount = iTemp;
    return modelSecurity_4010_8104_StrongSmoke;
}


//4030
- (ModelSecurity *)doGetModelSensor_4030_0102_Door:(NSArray *)arrOgin
{
   int iTemp = 0;
    NSMutableArray * mutArrSensor_4030_0102_Door = [NSMutableArray new];
    for (int i = 0; i < arrOgin.count; i ++) {
        SHModelDevice *modelDevice = arrOgin[i];
        
        if ([modelDevice.strDevice_device_OD isEqualToString:@"0FBE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0F BE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0fbe"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0f be"]){
            if ([modelDevice.strDevice_device_type isEqualToString:@"01"]) {
                
                if ([modelDevice.strDevice_category isEqualToString:@"02"]) {
                    //门磁
                    [mutArrSensor_4030_0102_Door  addObject:modelDevice];
                }else{}
                
                if (modelDevice.iDevice_device_state1 == 1 ) {
                    iTemp ++;
                }
            }
        }
    }
    ModelSecurity *modelSecurity_4030_0102_Door = [ModelSecurity new];
    NSString * strName = @"门磁报警器";
    NSString * strIcon = @"门磁报警器";
    NSString *strType = @"40300102";
    modelSecurity_4030_0102_Door.strIcon = strIcon;
    modelSecurity_4030_0102_Door.strName = strName;
    modelSecurity_4030_0102_Door.strType = strType;
    modelSecurity_4030_0102_Door.arrDeviceList = mutArrSensor_4030_0102_Door;
    modelSecurity_4030_0102_Door.iRedDotCount = iTemp;
    return modelSecurity_4030_0102_Door;
}

- (ModelSecurity *)doGetModelSensor_4030_0302_Gas:(NSArray *)arrOgin
{
    int iTemp = 0;
    NSMutableArray * mutArrSensor_4030_0302_Gas = [NSMutableArray new];
    for (int i = 0; i < arrOgin.count; i ++) {
        SHModelDevice *modelDevice = arrOgin[i];
        if ([modelDevice.strDevice_device_OD isEqualToString:@"0FBE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0F BE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0fbe"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0f be"]){
            if ([modelDevice.strDevice_device_type isEqualToString:@"03"]) {
                
                if ([modelDevice.strDevice_category isEqualToString:@"02"]) {
                    //门磁
                    [mutArrSensor_4030_0302_Gas  addObject:modelDevice];
                    
                }else{}
                
                if (modelDevice.iDevice_device_state1 == 1 ) {
                    iTemp ++;
                }
            }
        }
    }
    ModelSecurity *modelSecurity_4030_0302_Gas = [ModelSecurity new];
    NSString * strName = @"燃气报警器";
    NSString * strIcon = @"燃气报警器";
    NSString *strType = @"40300302";

    modelSecurity_4030_0302_Gas.strType = strType;
    modelSecurity_4030_0302_Gas.strIcon = strIcon;
    modelSecurity_4030_0302_Gas.strName = strName;
    modelSecurity_4030_0302_Gas.arrDeviceList = mutArrSensor_4030_0302_Gas;
    modelSecurity_4030_0302_Gas.iRedDotCount = iTemp;
    return modelSecurity_4030_0302_Gas;
}

- (ModelSecurity *)doGetModelSensor_4030_0402_WallMountHumanBodyInfrared:(NSArray *)arrOgin
{
    int iTemp = 0;
    NSMutableArray * mutArrSensor_4030_0402_WallMountHumanBodyInfrared = [NSMutableArray new];
    for (int i = 0; i < arrOgin.count; i ++) {
        SHModelDevice *modelDevice = arrOgin[i];
        if ([modelDevice.strDevice_device_OD isEqualToString:@"0FBE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0F BE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0fbe"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0f be"]){
            if ([modelDevice.strDevice_device_type isEqualToString:@"04"]) {
                
                if ([modelDevice.strDevice_category isEqualToString:@"02"]) {
                    [mutArrSensor_4030_0402_WallMountHumanBodyInfrared  addObject:modelDevice];
                }else{}
                
                if (modelDevice.iDevice_device_state1 == 1 ) {
                    iTemp ++;
                }
            }
        }
    }
    ModelSecurity *modelSecurity_4030_0402_WallMountHumanBodyInfrared = [ModelSecurity new];
    NSString * strName = @"弱电壁挂式人体红外传感器";
    NSString * strIcon = @"人体红外";
    NSString *strType = @"40300402";
    
    modelSecurity_4030_0402_WallMountHumanBodyInfrared.strType = strType;
    modelSecurity_4030_0402_WallMountHumanBodyInfrared.strIcon = strIcon;
    modelSecurity_4030_0402_WallMountHumanBodyInfrared.strName = strName;
    modelSecurity_4030_0402_WallMountHumanBodyInfrared.arrDeviceList = mutArrSensor_4030_0402_WallMountHumanBodyInfrared;
    modelSecurity_4030_0402_WallMountHumanBodyInfrared.iRedDotCount = iTemp;
    return modelSecurity_4030_0402_WallMountHumanBodyInfrared;
}


- (ModelSecurity *)doGetModelSensor_4030_0502_WaterLeakage:(NSArray *)arrOgin
{
    int iTemp = 0;
    NSMutableArray * mutArrSensor_4030_0502_WaterLeakage = [NSMutableArray new];
    for (int i = 0; i < arrOgin.count; i ++) {
        SHModelDevice *modelDevice = arrOgin[i];
        if ([modelDevice.strDevice_device_OD isEqualToString:@"0FBE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0F BE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0fbe"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0f be"]){
            if ([modelDevice.strDevice_device_type isEqualToString:@"05"]) {
                
                if ([modelDevice.strDevice_category isEqualToString:@"02"]) {
                    [mutArrSensor_4030_0502_WaterLeakage  addObject:modelDevice];
                }else{}
                
                if (modelDevice.iDevice_device_state1 == 1 ) {
                    iTemp ++;
                }
            }
        }
    }
    ModelSecurity *modelSecurity_4030_0502_WaterLeakage = [ModelSecurity new];
    NSString * strName = @"水浸感应器";
    NSString * strIcon = @"水浸感应器";
    NSString *strType = @"40300502";
    
    modelSecurity_4030_0502_WaterLeakage.strType = strType;
    modelSecurity_4030_0502_WaterLeakage.strIcon = strIcon;
    modelSecurity_4030_0502_WaterLeakage.strName = strName;
    modelSecurity_4030_0502_WaterLeakage.arrDeviceList = mutArrSensor_4030_0502_WaterLeakage;
    modelSecurity_4030_0502_WaterLeakage.iRedDotCount = iTemp;
    return modelSecurity_4030_0502_WaterLeakage;
}

- (ModelSecurity *)doGetModelSensor_4030_0702_WeakSmoke:(NSArray *)arrOgin
{
    int iTemp = 0;
    NSMutableArray * mutArrSensor_4030_0702_WeakSmoke = [NSMutableArray new];
    for (int i = 0; i < arrOgin.count; i ++) {
        SHModelDevice *modelDevice = arrOgin[i];
        if ([modelDevice.strDevice_device_OD isEqualToString:@"0FBE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0F BE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0fbe"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0f be"]){
            if ([modelDevice.strDevice_device_type isEqualToString:@"07"]) {
                
                if ([modelDevice.strDevice_category isEqualToString:@"02"]) {
                    [mutArrSensor_4030_0702_WeakSmoke  addObject:modelDevice];
                }else{}
                
                if (modelDevice.iDevice_device_state1 == 1 ) {
                    iTemp ++;
                }
            }
        }
    }
    ModelSecurity *modelSecurity_4030_0702_WeakSmoke = [ModelSecurity new];
    NSString * strName = @"弱电烟雾传感器";
    NSString * strIcon = @"烟感";
    NSString *strType = @"40300702";
    
    modelSecurity_4030_0702_WeakSmoke.strType = strType;
    modelSecurity_4030_0702_WeakSmoke.strIcon = strIcon;
    modelSecurity_4030_0702_WeakSmoke.strName = strName;
    modelSecurity_4030_0702_WeakSmoke.arrDeviceList = mutArrSensor_4030_0702_WeakSmoke;
    modelSecurity_4030_0702_WeakSmoke.iRedDotCount = iTemp;
    return modelSecurity_4030_0702_WeakSmoke;
}

- (ModelSecurity *)doGetModelSensor_4030_8102_Door:(NSArray *)arrOgin
{
    int iTemp = 0;
    NSMutableArray * mutArrSensor_4030_8102_Door = [NSMutableArray new];
    for (int i = 0; i < arrOgin.count; i ++) {
        SHModelDevice *modelDevice = arrOgin[i];
        
        if ([modelDevice.strDevice_device_OD isEqualToString:@"0FBE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0F BE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0fbe"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0f be"]){
            if ([modelDevice.strDevice_device_type isEqualToString:@"81"]) {
                
                if ([modelDevice.strDevice_category isEqualToString:@"02"]) {
                    [mutArrSensor_4030_8102_Door  addObject:modelDevice];
                }else{}
                
                if (modelDevice.iDevice_device_state1 == 1 ) {
                    iTemp ++;
                }
            }
        }
    }
    ModelSecurity *modelSecurity_4030_8102_Door = [ModelSecurity new];
    NSString * strName = @"门磁报警器";
    NSString * strIcon = @"门磁报警器";
    NSString *strType = @"40308102";
    
    modelSecurity_4030_8102_Door.strType = strType;
    modelSecurity_4030_8102_Door.strIcon = strIcon;
    modelSecurity_4030_8102_Door.strName = strName;
    modelSecurity_4030_8102_Door.arrDeviceList = mutArrSensor_4030_8102_Door;
    modelSecurity_4030_8102_Door.iRedDotCount = iTemp;
    return modelSecurity_4030_8102_Door;
}

- (ModelSecurity *)doGetModelSensor_4030_8103_Window:(NSArray *)arrOgin
{
    int iTemp = 0;
    NSMutableArray * mutArrSensor_4030_8103_Window = [NSMutableArray new];
    for (int i = 0; i < arrOgin.count; i ++) {
        SHModelDevice *modelDevice = arrOgin[i];
        
        if ([modelDevice.strDevice_device_OD isEqualToString:@"0FBE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0F BE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0fbe"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0f be"]){
            if ([modelDevice.strDevice_device_type isEqualToString:@"81"]) {
                
                if ([modelDevice.strDevice_category isEqualToString:@"03"]) {
                    [mutArrSensor_4030_8103_Window  addObject:modelDevice];
                }else{}
                
                if (modelDevice.iDevice_device_state1 == 1 ) {
                    iTemp ++;
                }
            }
        }
    }
    ModelSecurity *modelSecurity_4030_8103_Window = [ModelSecurity new];
    NSString * strName = @"窗磁";
    NSString * strIcon = @"门磁报警器";
    NSString *strType = @"40308103";
    
    modelSecurity_4030_8103_Window.strType = strType;
    modelSecurity_4030_8103_Window.strIcon = strIcon;
    modelSecurity_4030_8103_Window.strName = strName;
    modelSecurity_4030_8103_Window.arrDeviceList = mutArrSensor_4030_8103_Window;
    modelSecurity_4030_8103_Window.iRedDotCount = iTemp;
    return modelSecurity_4030_8103_Window;
}

- (ModelSecurity *)doGetModelSensor_4030_8302_WaterLeakage:(NSArray *)arrOgin
{
    int iTemp = 0;
    NSMutableArray * mutArrSensor_4030_8302_WaterLeakage = [NSMutableArray new];
    for (int i = 0; i < arrOgin.count; i ++) {
        SHModelDevice *modelDevice = arrOgin[i];
        if ([modelDevice.strDevice_device_OD isEqualToString:@"0FBE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0F BE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0fbe"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0f be"]){
            if ([modelDevice.strDevice_device_type isEqualToString:@"83"]) {
                
                if ([modelDevice.strDevice_category isEqualToString:@"02"]) {
                    [mutArrSensor_4030_8302_WaterLeakage  addObject:modelDevice];
                }else{}
                
                if (modelDevice.iDevice_device_state1 == 1 ) {
                    iTemp ++;
                }
            }
        }
    }
    ModelSecurity *modelSecurity_4030_8302_WaterLeakage = [ModelSecurity new];
    NSString * strName = @"水浸传感器";
    NSString * strIcon = @"水浸传感器";
    NSString *strType = @"40308302";
    
    modelSecurity_4030_8302_WaterLeakage.strType = strType;
    modelSecurity_4030_8302_WaterLeakage.strIcon = strIcon;
    modelSecurity_4030_8302_WaterLeakage.strName = strName;
    modelSecurity_4030_8302_WaterLeakage.arrDeviceList = mutArrSensor_4030_8302_WaterLeakage;
    modelSecurity_4030_8302_WaterLeakage.iRedDotCount = iTemp;
    return modelSecurity_4030_8302_WaterLeakage;
}

- (ModelSecurity *)doGetModelSensor_4030_8602_HumanBodyInfrared:(NSArray *)arrOgin
{
    int iTemp = 0;
    NSMutableArray * mutArrSensor_4030_8602_HumanBodyInfrared = [NSMutableArray new];
    for (int i = 0; i < arrOgin.count; i ++) {
        SHModelDevice *modelDevice = arrOgin[i];
        if ([modelDevice.strDevice_device_OD isEqualToString:@"0FBE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0F BE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0fbe"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0f be"]){
            if ([modelDevice.strDevice_device_type isEqualToString:@"86"]) {
                
                if ([modelDevice.strDevice_category isEqualToString:@"02"]) {
                    [mutArrSensor_4030_8602_HumanBodyInfrared  addObject:modelDevice];
                }else{}
                
                if (modelDevice.iDevice_device_state1 == 1 ) {
                    iTemp ++;
                }
            }
        }
    }
    ModelSecurity *modelSecurity_4030_8602_HumanBodyInfrared = [ModelSecurity new];
    NSString * strName = @"弱电人体红外感应器";
    NSString * strIcon = @"人体红外";
    NSString *strType = @"40308602";
    
    modelSecurity_4030_8602_HumanBodyInfrared.strType = strType;
    modelSecurity_4030_8602_HumanBodyInfrared.strIcon = strIcon;
    modelSecurity_4030_8602_HumanBodyInfrared.strName = strName;
    modelSecurity_4030_8602_HumanBodyInfrared.arrDeviceList = mutArrSensor_4030_8602_HumanBodyInfrared;
    modelSecurity_4030_8602_HumanBodyInfrared.iRedDotCount = iTemp;
    return modelSecurity_4030_8602_HumanBodyInfrared;
}

//4020
- (ModelSecurity *)doGetModelSensor_4020_0102_TemperatureHumidity:(NSArray *)arrOgin
{
    int iTemp = 0;
    NSMutableArray * mutArrSensor_4020_0102_TemperatureHumidity = [NSMutableArray new];
    for (int i = 0; i < arrOgin.count; i ++) {
        SHModelDevice *modelDevice = arrOgin[i];
        if ([modelDevice.strDevice_device_OD isEqualToString:@"0FB4"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0F B4"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0fb4"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0f b4"]){
            if ([modelDevice.strDevice_device_type isEqualToString:@"01"]) {
                
                if ([modelDevice.strDevice_category isEqualToString:@"02"]) {
                    [mutArrSensor_4020_0102_TemperatureHumidity  addObject:modelDevice];
                }else{}
                
                if (modelDevice.iDevice_device_state1 == 1 ) {
                    iTemp ++;
                }
            }
        }
    }
    ModelSecurity *modelSecurity_4020_0102_TemperatureHumidity = [ModelSecurity new];
    NSString * strName = @"温湿度传感器";
    NSString * strIcon = @"温湿度";
    NSString *strType = @"40200102";
    
    modelSecurity_4020_0102_TemperatureHumidity.strType = strType;
    modelSecurity_4020_0102_TemperatureHumidity.strIcon = strIcon;
    modelSecurity_4020_0102_TemperatureHumidity.strName = strName;
    modelSecurity_4020_0102_TemperatureHumidity.arrDeviceList = mutArrSensor_4020_0102_TemperatureHumidity;
    modelSecurity_4020_0102_TemperatureHumidity.iRedDotCount = iTemp;
    return modelSecurity_4020_0102_TemperatureHumidity;
}

- (ModelSecurity *)doGetModelSensor_4020_0202_Brightness:(NSArray *)arrOgin
{
    int iTemp = 0;
    NSMutableArray * mutArrSensor_4020_0202_Brightness = [NSMutableArray new];
    for (int i = 0; i < arrOgin.count; i ++) {
        SHModelDevice *modelDevice = arrOgin[i];
        if ([modelDevice.strDevice_device_OD isEqualToString:@"0FBE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0F BE"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0fbe"]
            || [modelDevice.strDevice_device_OD isEqualToString:@"0f be"]){
            if ([modelDevice.strDevice_device_type isEqualToString:@"02"]) {
                
                if ([modelDevice.strDevice_category isEqualToString:@"02"]) {
                    
                    [mutArrSensor_4020_0202_Brightness  addObject:modelDevice];
                }else{}
                
                if (modelDevice.iDevice_device_state1 == 1 ) {
                    iTemp ++;
                }
            }
        }
    }
    ModelSecurity *modelSecurity_4020_0202_Brightness = [ModelSecurity new];
    NSString * strName = @"温湿度传感器";
    NSString * strIcon = @"温湿度";
    NSString *strType = @"40200202";
    
    modelSecurity_4020_0202_Brightness.strType = strType;
    modelSecurity_4020_0202_Brightness.strIcon = strIcon;
    modelSecurity_4020_0202_Brightness.strName = strName;
    modelSecurity_4020_0202_Brightness.arrDeviceList = mutArrSensor_4020_0202_Brightness;
    modelSecurity_4020_0202_Brightness.iRedDotCount = iTemp;
    return modelSecurity_4020_0202_Brightness;
}




#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    if ([segue.identifier isEqualToString:@"go2CameraList"]) {
        EZDeviceTableViewController *vc = (EZDeviceTableViewController *)segue.destinationViewController;
        vc.hidesBottomBarWhenPushed = YES;
//        self.tabBarController.tabBar.hidden = YES;
        
    }
}

@end
