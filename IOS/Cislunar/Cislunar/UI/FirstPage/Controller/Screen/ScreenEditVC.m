//
//  ScreenEditVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/23.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import "ScreenEditVC.h"
#import "ScreenEditTableView.h"
#import "DelayedPickerView.h"
#import "TimerPickerView.h"
#import "ScreenIconView.h"
#import "DeviceAllListVC.h"
#import "ScreenEditPropertyModel.h"
#import "ScreenManager.h"
#import "ColorBulbVCForScreen.h"

typedef NS_ENUM(NSInteger, SHAddScreenStatue)
{
    SHAddScreenStatue_Ready              = 0,   //准备开锁
    SHAddScreenStatue_Ing                = 1,   //开锁中
    SHAddScreenStatue_Finish             = 2,   //开锁完成
    SHAddScreenStatue_Fail               = 3,   //开锁失败
};

typedef NS_ENUM(NSInteger, SHWhichRoadType)
{
    SHWhichRoadType_First              = 1,   //一路
    SHWhichRoadType_Second             = 2,   //二路
    SHWhichRoadType_Third              = 3,   //三路
};


@interface ScreenEditVC ()

@property (strong, nonatomic) ScreenEditTableView *tableView;
@property (strong, nonatomic) DelayedPickerView *pickerViewDelay;
@property (strong, nonatomic) DelayedPickerView *pickerViewPeriodOfTime;

@property (strong, nonatomic) TimerPickerView *timerPickerView;

@property (strong, nonatomic) ScreenIconView *viewIcon;
@property (strong, nonatomic) SHUIPicModel *modelPic;
@property (strong, nonatomic) NSString *strIconName;
@property (strong, nonatomic) NSArray *arrDelayedTimeList;
@property (strong, nonatomic) NetworkEngine *networkEngine;

@property (strong, nonatomic) ScreenManager *screenManager;

@property (strong, nonatomic) NSString *strShouldGiveScreenNoHex;

@property (strong, nonatomic) SHRequestTimer *timer;
@property (assign, nonatomic) SHAddScreenStatue lockOpenStatue;


@end

@implementation ScreenEditVC


- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = kBackgroundGrayColor;
    self.isHideNaviBar = YES;
    [self doInitSubViews];
    [self doRegisterScreenConfigKVO];
    [self doLoadTableViewData];
    [self doAddAction];
    
    //    self.strShouldGiveScreenNoHex = [[ToolHexManager sharedManager] converIntToHex:[self.strShouldGiveScreenNoInt intValue]];
    
    self.strShouldGiveScreenNoHex = self.strShouldGiveScreenNoHexTransi;
    
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    
    [self.view endEditing:YES];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [self.view endEditing:YES];
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
    
    if ([segue.identifier isEqualToString:@"seg_to_DeviceAllListVC"]) {
        DeviceAllListVC *vc = (DeviceAllListVC *)segue.destinationViewController;
        vc.iType = [[NSString stringWithFormat:@"%@",sender] intValue];
        if (vc.iType == ScreenEditAddDeviceTypeLinkage) {
            //联动列表
            vc.arrHasAddedList = self.tableView.arrLinkageDeviceList;
        }else{
            //执行设备列表
            vc.arrHasAddedList = self.tableView.arrPerformDeviceList;
        }
        
        @weakify(self);
        [vc setBlockGetDeviceList:^(int iType,NSArray *arrNeed){
            @strongify(self);
            //进行列表刷新
            if (vc.iType == ScreenEditAddDeviceTypeLinkage) {
                //联动列表
                self.tableView.arrLinkageDeviceList = arrNeed;
            }else{
                //执行设备列表
                self.tableView.arrPerformDeviceList = arrNeed;
            }
            
        }];
    }
}

#pragma mark -
#pragma mark - init subViews
- (void)doInitSubViews
{
    self.networkEngine = [NetworkEngine shareInstance];
    
    self.pickerViewDelay = [[DelayedPickerView alloc]init];
    
    self.pickerViewPeriodOfTime = [[DelayedPickerView alloc]init];
    
    self.timerPickerView = [[TimerPickerView alloc] init];
    
    [self.view addSubview:self.tableView];
}

#pragma mark -
#pragma mark - loadData

- (void)doLoadTableViewData
{
    if (self.vcType == ScreenEditVCType_Add) {
        
        ScreenEditPropertyModel *propertyModel = [ScreenEditPropertyModel new];
        propertyModel.strScreeenEditIconUrl = @"";
        propertyModel.strScreenEditName = @"";
        propertyModel.strScreenIsLinkage = @"0";
        propertyModel.strScreenEditDelayed = @"0";
        propertyModel.strScreenEditTimer = @"00:00";
        propertyModel.strScreenEditArming = @"";
        propertyModel.strScreenEditDisarming = @"1";//默认撤防有效
        propertyModel.iScreen_need_timing = 0;
        propertyModel.iScreen_need_time_delay = 0;
        propertyModel.str_linkage_time        = @"24";
        
        self.tableView.editPropertyModel = propertyModel;
        
    }else{
        ScreenEditPropertyModel *propertyModel          = [ScreenEditPropertyModel new];
        propertyModel.strScreeenEditIconUrl             = self.screenModel.strScreen_image;
        propertyModel.strScreenEditName                 = self.screenModel.strScreen_name;
        propertyModel.strScreenIsLinkage                = [NSString stringWithFormat:@"%d",self.screenModel.iScreen_need_linkage];
        propertyModel.strScreenEditDelayed              = self.screenModel.str_delay_time;
        propertyModel.strScreenEditTimer                = self.screenModel.str_timing_time;
        propertyModel.strScreenEditArming               = [NSString stringWithFormat:@"%d",self.screenModel.iScreen_need_security_on];
        propertyModel.strScreenEditDisarming            =  [NSString stringWithFormat:@"%d",self.screenModel.iScreen_need_security_off];
        propertyModel.iScreen_need_timing               = self.screenModel.iScreen_need_timing;
        propertyModel.iScreen_need_time_delay           = self.screenModel.iScreen_need_time_delay;
        propertyModel.str_linkage_time                  = self.screenModel.str_linkage_time;
        self.tableView.editPropertyModel = propertyModel;
        
        self.tableView.arrPerformDeviceList = [self handleGetDeviceNewModelCouldControl:self.screenModel.arrScreen_scene_details isControlDevice:YES];
        self.tableView.arrLinkageDeviceList = [self handleGetDeviceNewModelCouldControl:self.screenModel.arrScreen_scene_details isControlDevice:NO];
        
        //延时的数据
        self.arrDelayedTimeList = @[@"5",@"10",@"15",@"30",@"40",@"50",@"60",@"120",@"180"];
    }
}

#pragma mark -
#pragma mark - 分离联动设备和执行设备
- (NSMutableArray *)handleGetDeviceNewModelCouldControl:(NSArray *)result isControlDevice:(BOOL)isControlDevice
{
    NSMutableArray *mutArrCouldControl = [NSMutableArray new];
    NSMutableArray *mutDeviceListOnlyReport = [NSMutableArray new];
    
    for (int i = 0; i < result.count; i ++) {
        SHModelDevice *modelNew   = result[i];
        if ([modelNew.strDevice_device_OD isEqualToString:@"0F AA"])
        {
            if ([modelNew.strDevice_device_type isEqualToString:@"81"]
                || [modelNew.strDevice_device_type isEqualToString:@"8A"]
                || [modelNew.strDevice_device_type isEqualToString:@"8a"]
                || [modelNew.strDevice_device_type isEqualToString:@"C1"]
                || [modelNew.strDevice_device_type isEqualToString:@"c1"]
                || [modelNew.strDevice_device_type isEqualToString:@"0E"]
                || [modelNew.strDevice_device_type isEqualToString:@"0e"])
            {
                [mutDeviceListOnlyReport addObject:modelNew];
            }
            else
            {
                [mutArrCouldControl addObject:modelNew];
            }
        }
        else if ([modelNew.strDevice_device_OD isEqualToString:@"0F BE"])
        {
            
            [mutDeviceListOnlyReport addObject:modelNew];
        }
        else if ([modelNew.strDevice_device_OD isEqualToString:@"0F E6"])
        {
            
            [mutArrCouldControl addObject:modelNew];
        }
        else if ([modelNew.strDevice_device_OD isEqualToString:@"0F C8"])
        {
            
            [mutArrCouldControl addObject:modelNew];
        }
        
    }
    if (isControlDevice) {
        return mutArrCouldControl;
    }else{
        return mutDeviceListOnlyReport;
    }
}


#pragma mark -
#pragma mark - init

-(ScreenManager *)screenManager
{
    if (!_screenManager) {
        _screenManager = [ScreenManager new];
    }
    return _screenManager;
}


-(ScreenEditTableView *)tableView
{
    if (!_tableView) {
        _tableView = [[ScreenEditTableView alloc] initWithFrame:CGRectMake(10,
                                                                           84,
                                                                           UI_SCREEN_WIDTH - 20,
                                                                           UI_SCREEN_HEIGHT - 104)
                                                          style:UITableViewStyleGrouped];
    }
    return _tableView;
}

#pragma mark -
#pragma mark - action
- (void)doAddAction
{
    @weakify(self);
    [self.tableView setBlockFirstCellSwitchPressed:^(int iTag, BOOL isButtonOn){
        @strongify(self);
        BOOL isChange = isButtonOn;
        NSString *strChange = [NSString stringWithFormat:@"%d",isChange];
        ScreenEditPropertyModel *propertyModel = [ScreenEditPropertyModel new];
        switch (iTag) {
            case SwitchLinkageTag:
            {
                NSLog(@"点击了联动switch");
                //                ScreenEditPropertyModel *propertyModel = [ScreenEditPropertyModel new];
                propertyModel.strScreeenEditIconUrl     = self.tableView.editPropertyModel.strScreeenEditIconUrl;
                propertyModel.strScreenEditName         = self.tableView.editPropertyModel.strScreenEditName;
                propertyModel.strScreenIsLinkage        = strChange;
                propertyModel.strScreenEditDelayed      = self.tableView.editPropertyModel.strScreenEditDelayed;
                propertyModel.strScreenEditTimer        = self.tableView.editPropertyModel.strScreenEditTimer;
                propertyModel.strScreenEditArming       = self.tableView.editPropertyModel.strScreenEditArming;
                propertyModel.strScreenEditDisarming    = self.tableView.editPropertyModel.strScreenEditDisarming;//默认撤防有效
                propertyModel.iScreen_need_timing       = self.tableView.editPropertyModel.iScreen_need_timing;
                propertyModel.iScreen_need_time_delay   = self.tableView.editPropertyModel.iScreen_need_time_delay;
                propertyModel.str_linkage_time          = self.tableView.editPropertyModel.str_linkage_time;
                //                self.tableView.editPropertyModel = propertyModel;
                
                if (!isChange) {
                    self.tableView.arrLinkageDeviceList = nil;
                }
            }
                break;
            case SwitchDelayedTag:
            {
                NSLog(@"点击了延时switch");
                propertyModel.strScreeenEditIconUrl     = self.tableView.editPropertyModel.strScreeenEditIconUrl;
                propertyModel.strScreenEditName         = self.tableView.editPropertyModel.strScreenEditName;
                propertyModel.strScreenIsLinkage        = self.tableView.editPropertyModel.strScreenIsLinkage;
                
                if (isButtonOn) {
                    propertyModel.strScreenEditDelayed      = self.tableView.editPropertyModel.strScreenEditDelayed;
                }else{
                    propertyModel.strScreenEditDelayed      = @"0";
                }
                
                propertyModel.strScreenEditTimer        = self.tableView.editPropertyModel.strScreenEditTimer;
                propertyModel.strScreenEditArming       = self.tableView.editPropertyModel.strScreenEditArming;
                propertyModel.strScreenEditDisarming    = self.tableView.editPropertyModel.strScreenEditDisarming;//默认撤防有效
                
                propertyModel.iScreen_need_timing       = self.tableView.editPropertyModel.iScreen_need_timing;
                propertyModel.iScreen_need_time_delay   = [strChange intValue];
                propertyModel.str_linkage_time          = self.tableView.editPropertyModel.str_linkage_time;
                
            }
                break;
            case SwitchTimerTag:
            {
                NSLog(@"点击了定时switch");
                propertyModel.strScreeenEditIconUrl     = self.tableView.editPropertyModel.strScreeenEditIconUrl;
                propertyModel.strScreenEditName         = self.tableView.editPropertyModel.strScreenEditName;
                propertyModel.strScreenIsLinkage        = self.tableView.editPropertyModel.strScreenIsLinkage;
                propertyModel.strScreenEditDelayed      = self.tableView.editPropertyModel.strScreenEditDelayed;
                propertyModel.strScreenEditTimer        = self.tableView.editPropertyModel.strScreenEditTimer;
                propertyModel.strScreenEditArming       = self.tableView.editPropertyModel.strScreenEditArming;
                propertyModel.strScreenEditDisarming    = self.tableView.editPropertyModel.strScreenEditDisarming;//默认撤防有效
                propertyModel.iScreen_need_timing       = [strChange intValue];
                propertyModel.iScreen_need_time_delay   = self.tableView.editPropertyModel.iScreen_need_time_delay;
                propertyModel.str_linkage_time          = self.tableView.editPropertyModel.str_linkage_time;
            }
                break;
            case SwitchArmingTag:
            {
                NSLog(@"点击了布防switch");
                propertyModel.strScreeenEditIconUrl     = self.tableView.editPropertyModel.strScreeenEditIconUrl;
                propertyModel.strScreenEditName         = self.tableView.editPropertyModel.strScreenEditName;
                propertyModel.strScreenIsLinkage        = self.tableView.editPropertyModel.strScreenIsLinkage;
                propertyModel.strScreenEditDelayed      = self.tableView.editPropertyModel.strScreenEditDelayed;
                propertyModel.strScreenEditTimer        = self.tableView.editPropertyModel.strScreenEditTimer;
                propertyModel.strScreenEditArming       = strChange;
                propertyModel.strScreenEditDisarming    = [NSString stringWithFormat:@"%d",!isChange];//默认撤防有效
                propertyModel.iScreen_need_timing       = self.tableView.editPropertyModel.iScreen_need_timing;
                propertyModel.iScreen_need_time_delay   = self.tableView.editPropertyModel.iScreen_need_time_delay;
                propertyModel.str_linkage_time          = self.tableView.editPropertyModel.str_linkage_time;
            }
                break;
            case SwitchDisarmingTag:
            {
                NSLog(@"点击了撤防switch");
                propertyModel.strScreeenEditIconUrl     = self.tableView.editPropertyModel.strScreeenEditIconUrl;
                propertyModel.strScreenEditName         = self.tableView.editPropertyModel.strScreenEditName;
                propertyModel.strScreenIsLinkage        = self.tableView.editPropertyModel.strScreenIsLinkage;
                propertyModel.strScreenEditDelayed      = self.tableView.editPropertyModel.strScreenEditDelayed;
                propertyModel.strScreenEditTimer        = self.tableView.editPropertyModel.strScreenEditTimer;
                propertyModel.strScreenEditArming       = [NSString stringWithFormat:@"%d",!isChange];
                propertyModel.strScreenEditDisarming    = strChange;//默认撤防有效
                propertyModel.iScreen_need_timing       = self.tableView.editPropertyModel.iScreen_need_timing;
                propertyModel.iScreen_need_time_delay   = self.tableView.editPropertyModel.iScreen_need_time_delay;
                propertyModel.str_linkage_time          = self.tableView.editPropertyModel.str_linkage_time;
            }
                break;
                
            default:
                break;
        }
        self.tableView.editPropertyModel = propertyModel;
    }];
    
    [self.tableView setBlockFirstCellScreenIconPressed:^(UIButton *btn){
        NSLog(@"点击头像修改头像");
        @strongify(self);
        [self doAddScreenIcon:btn];
    }];
    
    [self.tableView setBlockFirstCellDelayedTextfieldPressed:^(UITextField *tx){
        NSLog(@"点击延时textField");
        @strongify(self);
        [self doShowDelayedPickerView];
        
    }];
    
    [self.tableView setBlockFirstCellTimerTextfieldPressed:^(UITextField *tx){
        NSLog(@"点击定时Textfield");
        @strongify(self);
        
        if ([self.tableView.editPropertyModel.strScreenIsLinkage intValue] == 0) {
            //非联动
            [self doShowTimerPickerView];
        }else{
            //联动
            [self doShowPickerViewPeriodOfTime];
        }
        
    }];
    
    [self.tableView setBlockAddDeivcePressed:^(ScreenEditAddDeviceType type){
        @strongify(self);
        [self performSegueWithIdentifier:@"seg_to_DeviceAllListVC" sender:@(type)];
    }];
    
    [self.tableView setBlockLinkageOrPerformDeviceSwitchPressed:^(SHModelDevice *deviceTr,ScreenEditAddDeviceType type,UISwitch *switchTr){
        @strongify(self);
        
        [self doHandleTempWithScreenEditAddDeviceType:type deviceTr:deviceTr switchTr:switchTr];
    }];
    
    [self.tableView setDidSelectedRowAtIndexPath:^(NSIndexPath *indexPath){
        @strongify(self);
        SHModelDevice *device = self.tableView.arrPerformDeviceList[indexPath.row];
        if ([device.strDevice_device_OD isEqualToString:@"0F AA"]) {
            if ([device.strDevice_device_type isEqualToString:@"0B"] || [device.strDevice_device_type isEqualToString:@"0b"]) {
                if ([device.strDevice_category isEqualToString:@"02"]) {
                    UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"FirstPage" bundle:[NSBundle mainBundle]];
                    ColorBulbVCForScreen *VC = (ColorBulbVCForScreen *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"ColorBulbVCForScreen"];
                    VC.device = device;
                    
                    @weakify(self);
                    [VC setBlockGetNewDevice:^(SHModelDevice *deviceNew){
                        @strongify(self);
                        NSMutableArray *mutArr = [[NSMutableArray alloc] initWithArray:self.tableView.arrPerformDeviceList];
                        [mutArr replaceObjectAtIndex:indexPath.row withObject:deviceNew];
                        self.tableView.arrPerformDeviceList = mutArr;
                        
                    }];
                    
                    [self.navigationController pushViewController:VC animated:YES];
                }
            }
        }
        
    }];
}


#pragma mark - 处理点击cell swicht 更改数据源
- (void)doHandleTempWithScreenEditAddDeviceType:(ScreenEditAddDeviceType)type
                                       deviceTr:(SHModelDevice *)device
                                       switchTr:(UISwitch *)switchTr
{
    int  iIdentifer;
    if (switchTr.isOn) {
        iIdentifer = 1;
    }else{
        iIdentifer = 2;
    }
    if (type == ScreenEditAddDeviceTypePeform) {
        
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
                }else if ([device.strDevice_category isEqualToString:@"05"]){
                    NSLog(@"SOS报警器");
                }
                [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_First statue:iIdentifer];
                
            }else if ([device.strDevice_device_type isEqualToString:@"0E"] || [device.strDevice_device_type isEqualToString:@"0e"]){
                if ([device.strDevice_category isEqualToString:@"02"]) {
                    NSLog(@"多彩冷暖灯");
                }
                
            }else if ([device.strDevice_device_type isEqualToString:@"0B"] || [device.strDevice_device_type isEqualToString:@"0b"]){
                if ([device.strDevice_category isEqualToString:@"02"]) {
                    NSLog(@"多彩球泡灯");
                    [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_First statue:iIdentifer];
                }
                
            }else if ([device.strDevice_device_type isEqualToString:@"09"]){
                if ([device.strDevice_category isEqualToString:@"02"]) {
                    NSLog(@"声光报警器");
                    [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_First statue:iIdentifer];
                }
                
            }else if ([device.strDevice_device_type isEqualToString:@"07"]){
                if ([device.strDevice_category isEqualToString:@"02"]) {
                    NSLog(@"三路灯开关");
                    
                    if ([device.strDevice_other_status intValue] == 1) {
                        [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_First statue:iIdentifer];
                        
                    }else if ([device.strDevice_other_status intValue] == 2){
                        
                        [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_Second statue:iIdentifer];
                    }else if ([device.strDevice_other_status intValue] == 3){
                        
                        [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_Third statue:iIdentifer];
                    }
                    
                }else if ([device.strDevice_category isEqualToString:@"04"]){
                    NSLog(@"三路多联多控灯开关");
                    if ([device.strDevice_other_status intValue] == 1) {
                        [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_First statue:iIdentifer];
                        
                    }else if ([device.strDevice_other_status intValue] == 2){
                        
                        [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_Second statue:iIdentifer];
                    }else if ([device.strDevice_other_status intValue] == 3){
                        
                        [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_Third statue:iIdentifer];
                    }
                }
                
            }else if ([device.strDevice_device_type isEqualToString:@"06"]){
                if ([device.strDevice_category isEqualToString:@"02"]) {
                    NSLog(@"二路灯开关");
                    if ([device.strDevice_other_status intValue] == 1) {
                        [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_First statue:iIdentifer];
                        
                    }else if ([device.strDevice_other_status intValue] == 2){
                        
                        [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_Second statue:iIdentifer];
                    }
                }else if ([device.strDevice_category isEqualToString:@"04"]){
                    NSLog(@"多联二路灯开关");
                    if ([device.strDevice_other_status intValue] == 1) {
                        [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_First statue:iIdentifer];
                        
                    }else if ([device.strDevice_other_status intValue] == 2){
                        
                        [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_Second statue:iIdentifer];
                    }
                }
                
            }else if ([device.strDevice_device_type isEqualToString:@"05"]){
                if ([device.strDevice_category isEqualToString:@"02"]) {
                    NSLog(@"一路灯开关");
                }else if ([device.strDevice_category isEqualToString:@"03"]) {
                    NSLog(@"电动玻璃");
                }else if ([device.strDevice_category isEqualToString:@"04"]) {
                    NSLog(@"多联多控一路灯开关");
                }else if ([device.strDevice_category isEqualToString:@"10"]) {
                    NSLog(@"86插座");
                }else if ([device.strDevice_category isEqualToString:@"11"]) {
                    NSLog(@"移动插座");
                }
                [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_First statue:iIdentifer];
                
            }else if ([device.strDevice_device_type isEqualToString:@"02"]){
                //开、关、停
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
                
                [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_First statue:iIdentifer];
                
            }else if ([device.strDevice_device_type isEqualToString:@"01"]){
                if ([device.strDevice_category isEqualToString:@"02"]) {
                    NSLog(@"普通电动窗帘");
                    [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_First statue:iIdentifer];
                }
            }else{
                NSLog(@"其它设备");
            }
            
        }else if ([device.strDevice_device_OD isEqualToString:@"0F E6"]){
            
            if ([device.strDevice_device_type isEqualToString:@"02"]){
                
                if ([device.strDevice_category isEqualToString:@"02"]){
                    NSLog(@"红外学习仪");
                    if ([device.strDevice_sindex_length intValue] == SHSindexLength_InfraredAirCondition) {
                        NSLog(@"空调");
                    }else if ([device.strDevice_sindex_length intValue] == SHSindexLength_InfraredOther_Other){
                        NSLog(@"其它遥控设备");
                    }else{
                        NSLog(@"其它遥控设备");
                    }
                    [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_First statue:iIdentifer];
                }else if ([device.strDevice_category isEqualToString:@"03"]){
                    NSLog(@"音乐背景器");
                }else if ([device.strDevice_category isEqualToString:@"10"]){
                    NSLog(@"电动窗帘");
                    [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_First statue:iIdentifer];
                    
                }else if ([device.strDevice_category isEqualToString:@"11"]){
                    NSLog(@"平移开窗器");
                    [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_First statue:iIdentifer];
                    
                }else if ([device.strDevice_category isEqualToString:@"12"]){
                    NSLog(@"电动床");
                }else if ([device.strDevice_category isEqualToString:@"13"]){
                    NSLog(@"新风系统");
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
                [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_First statue:iIdentifer];
            }
        }else{
            NSLog(@"未知，OD:%@,type:%@",device.strDevice_device_OD,device.strDevice_device_type);
        }
    }else{
        
        [self doHandleWhenTapOnSwitchWithDevice:device roadType:SHWhichRoadType_First statue:iIdentifer];
    }
    
}

- (void)doHandleWhenTapOnSwitchWithDevice:(SHModelDevice *)device roadType:(SHWhichRoadType)roadType statue:(int)iState
{
    [self.tableView.arrPerformDeviceList enumerateObjectsUsingBlock:^(SHModelDevice  *_Nonnull deviceModel,
                                                                      NSUInteger idx,
                                                                      BOOL * _Nonnull stop)
     {
         if (deviceModel.iDevice_device_id == device.iDevice_device_id) {
             if (roadType == SHWhichRoadType_First) {
                 deviceModel.iDevice_device_state1 = iState;
             }else if (roadType == SHWhichRoadType_Second){
                 deviceModel.iDevice_device_state2 = iState;
             }else if (roadType == SHWhichRoadType_Third){
                 
                 deviceModel.iDevice_device_state3 = iState;
             }
         }
     }];
    
    
    [self.tableView.arrLinkageDeviceList enumerateObjectsUsingBlock:^(SHModelDevice  *_Nonnull deviceModel,
                                                                      NSUInteger idx,
                                                                      BOOL * _Nonnull stop)
     {
         if (deviceModel.iDevice_device_id == device.iDevice_device_id) {
             if (roadType == SHWhichRoadType_First) {
                 deviceModel.iDevice_device_state1 = iState;
             }else if (roadType == SHWhichRoadType_Second){
                 deviceModel.iDevice_device_state2 = iState;
             }else if (roadType == SHWhichRoadType_Third){
                 
                 deviceModel.iDevice_device_state3 = iState;
             }
         }
     }];
    
    [self.tableView reloadData];
}

#pragma mark -
#pragma mark - 点击back按钮触发方法
- (IBAction)btnBackPressed:(UIButton *)sender {
    
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark -
#pragma mark -点击完成按钮
- (IBAction)EditDonePressed:(UIButton *)sender {
    int iCount = (int)self.tableView.arrLinkageDeviceList.count + (int)self.tableView.arrPerformDeviceList.count;
    if (iCount > 30) {
        [XWHUDManager showWarningTipHUD:@"情景模式不要添加多余30个设备"];
        return;
    }
    
    if ([self doJudgeCouldAddScreen]) {
        
        [self startTimer];
        self.lockOpenStatue = SHAddScreenStatue_Ing;
        
        if (self.tableView.arrLinkageDeviceList.count == 0) {
            //普通场景模式
            [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
            self.strShouldGiveScreenNoHex = self.strShouldGiveScreenNoHex;
            [self doDleteScreenWithScreenNo:self.strShouldGiveScreenNoHex];
            //            [self performSelector:@selector(doSendCommonScreenConfigNew) withObject:nil afterDelay:3];
            
        }else{
            //联动场景模式
            [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
            [self doDleteScreenWithScreenNo:self.strShouldGiveScreenNoHex];
            //            [self performSelector:@selector(doSendLinkageScreenFirstNew) withObject:nil afterDelay:3];
        }
    }
    
}

#pragma mark - 删除网关内场景的指令
//#warning  有待验证
- (void)doDleteScreenWithScreenNo:(NSString *)strScreenNoHex
{
    NSData *dataSend = [[NetworkEngine shareInstance] doHandleSendDleteScreenOrderToControlWithScreenNO:strScreenNoHex];
    [[NetworkEngine shareInstance] sendRequestData:dataSend];
}

#pragma mark - 判断是否可以点击完成轻模式
-(BOOL)doJudgeCouldAddScreen
{
    //    self.textFieldName.text = @"111";
    if (self.tableView.editPropertyModel.strScreeenEditIconUrl.length == 0) {
        [XWHUDManager showWarningTipHUD:@"请选择情景模式的图片"];
        return NO;
    }else if (self.tableView.editPropertyModel.strScreenEditName.length == 0){
        [XWHUDManager showWarningTipHUD:@"请输入情景模式的名字"];
        return NO;
    }else if(self.tableView.arrPerformDeviceList.count == 0){
        
        [XWHUDManager showWarningTipHUD:@"请添加情景模式要控制的设备"];
        return NO;
    }else
        return YES;
    
}


#pragma mark -
#pragma mark - 延时

- (void)doShowDelayedPickerView
{
    self.pickerViewDelay.dataSource =@[@"5s",@"10s",@"15s",@"20s",@"25s",@"30s",@"35s",@"40s",@"45s",@"50s",@"55s",@"60s"];
    self.pickerViewDelay.pickerTitle = @"请选择延时时间";
    __weak typeof(self) weakSelf = self;
    self.pickerViewDelay.valueDidSelect = ^(NSString *value){
        NSArray * stateArr = [value componentsSeparatedByString:@"/"];
        
        NSString *strGet = stateArr[0];
        NSString *strResult = [strGet substringToIndex:strGet.length - 1];
        ScreenEditPropertyModel *propertyModel = [ScreenEditPropertyModel new];
        propertyModel.strScreeenEditIconUrl     = weakSelf.tableView.editPropertyModel.strScreeenEditIconUrl;
        propertyModel.strScreenEditName         = weakSelf.tableView.editPropertyModel.strScreenEditName;
        propertyModel.strScreenIsLinkage        = weakSelf.tableView.editPropertyModel.strScreenIsLinkage;
        propertyModel.strScreenEditDelayed      = strResult;
        propertyModel.strScreenEditTimer        = weakSelf.tableView.editPropertyModel.strScreenEditTimer;
        propertyModel.strScreenEditArming       = weakSelf.tableView.editPropertyModel.strScreenEditArming;
        propertyModel.strScreenEditDisarming    = weakSelf.tableView.editPropertyModel.strScreenEditDisarming;//默认撤防有效
        propertyModel.iScreen_need_timing       = weakSelf.tableView.editPropertyModel.iScreen_need_timing;
        propertyModel.iScreen_need_time_delay   = 1;
        propertyModel.str_linkage_time          = weakSelf.tableView.editPropertyModel.str_linkage_time;
        weakSelf.tableView.editPropertyModel    = propertyModel;
    };
    [self.pickerViewDelay show];
}

- (void)doHideDeleyedPickerView
{
    [self.pickerViewDelay removeSelfFromSupView];
}

#pragma mark -
#pragma mark - 定时功能

- (void)doShowPickerViewPeriodOfTime
{
    self.pickerViewPeriodOfTime.dataSource =@[@"0",@"1",@"2",@"3",@"4",@"5",@"6",
                                              @"7",@"8",@"9",@"10",@"11",@"12",
                                              @"13",@"14",@"15",@"16",@"17",@"18",
                                              @"19",@"20",@"21",@"22",@"23",@"24"];
    self.pickerViewPeriodOfTime.pickerTitle = @"请选择定时时间段";
    __weak typeof(self) weakSelf = self;
    self.pickerViewPeriodOfTime.valueDidSelect = ^(NSString *value){
        NSArray * stateArr = [value componentsSeparatedByString:@"/"];
        
        NSString *strGet = stateArr[0];
        //NSString *strResult = [strGet substringToIndex:strGet.length - 1];
        //NSLog(@"******%@",[strGet substringToIndex:strGet.length - 1]);
        
        ScreenEditPropertyModel *propertyModel = [ScreenEditPropertyModel new];
        propertyModel.strScreeenEditIconUrl     = weakSelf.tableView.editPropertyModel.strScreeenEditIconUrl;
        propertyModel.strScreenEditName         = weakSelf.tableView.editPropertyModel.strScreenEditName;
        propertyModel.strScreenIsLinkage        = weakSelf.tableView.editPropertyModel.strScreenIsLinkage;
        propertyModel.strScreenEditDelayed      = weakSelf.tableView.editPropertyModel.strScreenEditDelayed;
        propertyModel.strScreenEditTimer        = weakSelf.tableView.editPropertyModel.strScreenEditTimer;
        propertyModel.strScreenEditArming       = weakSelf.tableView.editPropertyModel.strScreenEditArming;
        propertyModel.strScreenEditDisarming    = weakSelf.tableView.editPropertyModel.strScreenEditDisarming;
        propertyModel.iScreen_need_timing       = 1;
        propertyModel.iScreen_need_time_delay   = weakSelf.tableView.editPropertyModel.iScreen_need_time_delay;
        propertyModel.str_linkage_time          = strGet;
        weakSelf.tableView.editPropertyModel = propertyModel;
    };
    
    [self.pickerViewPeriodOfTime show];
}


- (void)doShowTimerPickerView
{
    self.timerPickerView.pickerTitle = @"请选择定时时间";
    @weakify(self);
    self.timerPickerView.valueDidSelect = ^(NSString *value){
        NSLog(@"******%@",value);
        @strongify(self);
        ScreenEditPropertyModel *propertyModel = [ScreenEditPropertyModel new];
        propertyModel.strScreeenEditIconUrl     = self.tableView.editPropertyModel.strScreeenEditIconUrl;
        propertyModel.strScreenEditName         = self.tableView.editPropertyModel.strScreenEditName;
        propertyModel.strScreenIsLinkage        = self.tableView.editPropertyModel.strScreenIsLinkage;
        propertyModel.strScreenEditDelayed      = self.tableView.editPropertyModel.strScreenEditDelayed;
        propertyModel.strScreenEditTimer        = value;
        propertyModel.strScreenEditArming       = self.tableView.editPropertyModel.strScreenEditArming;
        propertyModel.strScreenEditDisarming    = self.tableView.editPropertyModel.strScreenEditDisarming;//默认撤防有效
        propertyModel.iScreen_need_timing       = 1;
        propertyModel.iScreen_need_time_delay   = self.tableView.editPropertyModel.iScreen_need_time_delay;
        propertyModel.str_linkage_time          = self.tableView.editPropertyModel.str_linkage_time;
        self.tableView.editPropertyModel = propertyModel;
    };
    [self.timerPickerView show];
}


#pragma mark -
#pragma mark - 添加头像
- (void)doAddScreenIcon:(UIButton *)btnIcon
{
    self.viewIcon = [[ScreenIconView alloc] initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, UI_SCREEN_HEIGHT)];
    
    NSArray *arrScreenPicList = [[SHAppCommonRequest shareInstance] doGetScreenUIPicAll];
    [self.viewIcon show:arrScreenPicList];
    
    @weakify(self);
    [self.viewIcon setBlockCompleteHandle:^(NSIndexPath *indexPath) {
        @strongify(self);
        SHUIPicModel *model = arrScreenPicList[indexPath.row];
        self.modelPic = model;
        NSString *strPrImage = [NSString stringWithFormat:@"%@Pr_%@@2x.%@",model.strUIPic_base_url,model.strUIPic_name,model.strUIPic_image_type];
        NSString *strUnImage = [NSString stringWithFormat:@"%@Un_%@@2x.%@",model.strUIPic_base_url,model.strUIPic_name,model.strUIPic_image_type];
        [btnIcon sd_setImageWithURL:[NSURL URLWithString:strPrImage] forState:UIControlStateNormal];
        [btnIcon sd_setImageWithURL:[NSURL URLWithString:strUnImage] forState:UIControlStateSelected];
        self.strIconName = model.strUIPic_name;
        
        
        ScreenEditPropertyModel *propertyModel = [ScreenEditPropertyModel new];
        propertyModel.strScreeenEditIconUrl     = model.strUIPic_name;
        propertyModel.strScreenEditName         = self.tableView.editPropertyModel.strScreenEditName;
        propertyModel.strScreenIsLinkage        = self.tableView.editPropertyModel.strScreenIsLinkage;;
        propertyModel.strScreenEditDelayed      = self.tableView.editPropertyModel.strScreenEditDelayed;
        propertyModel.strScreenEditTimer        = self.tableView.editPropertyModel.strScreenEditTimer;
        propertyModel.strScreenEditArming       = self.tableView.editPropertyModel.strScreenEditArming;
        propertyModel.strScreenEditDisarming    = self.tableView.editPropertyModel.strScreenEditDisarming;//默认撤防有效
        propertyModel.iScreen_need_timing       = self.tableView.editPropertyModel.iScreen_need_timing;
        propertyModel.iScreen_need_time_delay   = self.tableView.editPropertyModel.iScreen_need_time_delay;
        propertyModel.str_linkage_time          = self.tableView.editPropertyModel.str_linkage_time;
        self.tableView.editPropertyModel = propertyModel;
    }];
}

- (void)doPopToTheForward
{
    [self performSelector:@selector(doPopTheViewControler) withObject:nil afterDelay:2];
}

- (void)doPopTheViewControler
{
    [self.navigationController popViewControllerAnimated:YES];
}


/***************************************以下为写入网关模式***************************************/
#pragma mark -
#pragma mark -  场景写入网关模式
- (void)doGoToTiggerAlarmPage
{
    [self performSegueWithIdentifier:@"seg_to_SHTriggeringAlarmVC" sender:self];
}


- (void)doRegisterScreenConfigKVO
{
    @weakify(self);
    [self observeKeyPath:@keypath(self.networkEngine.screenNew)
                   block:^(id value)
     {
         @strongify(self);
         SHModelScreenNew *screen = value;
         if ([screen.strCmdID isEqualToString:@"50"]) {
             [self screenCofigSuccess:screen];
         }else if ([screen.strCmdID isEqualToString:@"D0"]){
             [self screenCofigFail:screen];
         }else{
             [XWHUDManager showErrorTipHUD:@"配置失败"];
         }
         
     }];
}

- (void)screenCofigFail:(SHModelScreenNew *)screen
{
    [XWHUDManager hideInWindow];
    if ([screen.strSubCommandLength isEqualToString:@"03"]) {
        if ([screen.strSubcommandIdentifer isEqualToString:@"01"]) {
            NSLog(@"配置失败!");
            [XWHUDManager showErrorTipHUD:@"配置失败"];
            [self stopTimer];
            return;
            
        }else if ([screen.strSubcommandIdentifer isEqualToString:@"05"]){
            //设防布防
            NSLog(@"撤防布防失败");
            [XWHUDManager showErrorTipHUD:@"撤防布防失败"];
            return;
        }else if ([screen.strSubcommandIdentifer isEqualToString:@"07"]){
            NSString *strFailInfo = [NSString stringWithFormat:@"删除场景联动%@失败",screen.strDelteScreenNo];
            NSLog(@"删除场景联动%@失败",strFailInfo);
            [XWHUDManager showErrorTipHUD:strFailInfo];
            [self stopTimer];
            return;
            
        }else if ([screen.strSubcommandIdentifer isEqualToString:@"08"]){
            //网关报警状态解除命令
            NSLog(@"报警状态解除命令失败");
            [XWHUDManager showErrorTipHUD:@"报警状态解除命令失败"];
            
            return;
        }
    }else if ([screen.strSubCommandLength isEqualToString:@"05"]){
        
        if ([screen.strInstructionCount isEqualToString:screen.strCurrentInstructionNO]) {
            NSLog(@"场景所有设备载入失败!");
            [XWHUDManager showErrorTipHUD:@"场景所有设备载入失败!"];
            [self stopTimer];
            return;
            
        }else{
            NSLog(@"失败：所有%@条数;当前第%@条.",screen.strInstructionCount,screen.strCurrentInstructionNO);
            NSString *strFailInfo = [NSString stringWithFormat:@"失败：所有%@条数;当前第%@条.",screen.strInstructionCount,screen.strCurrentInstructionNO];
            [XWHUDManager showErrorTipHUD:strFailInfo];
            [self stopTimer];
            return;
        }
    }else{
        NSLog(@"the other fail");
    }
}

- (void)screenCofigSuccess:(SHModelScreenNew *)screen
{
    if ([screen.strAnswerIdentifer isEqualToString:@"00"]) {
        if ([screen.strSubCommandLength isEqualToString:@"03"]) {
            if ([screen.strSubcommandIdentifer isEqualToString:@"01"]) {
                NSLog(@"配置成功,进行载入指令!");
                [self doWriteToInGateway];
                
            }else if ([screen.strSubcommandIdentifer isEqualToString:@"05"]){
                //设防布防
                NSLog(@"收到上报设防布防数据帧");
            }else if ([screen.strSubcommandIdentifer isEqualToString:@"07"]){
                NSLog(@"删除场景联动%@成功",screen.strDelteScreenNo);
                
                //删除成功后进行场景的配置
                if (self.tableView.arrLinkageDeviceList.count == 0) {
                    [self doSendCommonScreenConfigNew];
                }else{
                    [self doSendLinkageScreenFirstNew];
                }
                
            }else if ([screen.strSubcommandIdentifer isEqualToString:@"08"]){
                //网关报警状态解除命令
                NSLog(@"收到上报网关报警状态解除命令");
            }
        }else if ([screen.strSubCommandLength isEqualToString:@"05"]){
            
            if ([screen.strInstructionCount isEqualToString:screen.strCurrentInstructionNO]) {
                NSLog(@"场景所有设备载入成功!");
                if (self.vcType == ScreenEditVCType_Add) {
                    [self doHandleAddScreenWithScreenSerialNumber:screen.strScreenNo];
                }else if(self.vcType == ScreenEditVCType_Edit){
                    [self doHandleUpdateScreenWithScreenSerialNumber:screen.strScreenNo];
                }else{
                    NSLog(@"场景虽然载入成功，但是没有相对应的类型(Add or Update)");
                }
            }else{
                NSLog(@"所有%@条数;当前第%@条.",screen.strInstructionCount,screen.strCurrentInstructionNO);
            }
        }else{
            NSLog(@"the other");
        }
    }
}

#pragma mark - 更新场景
- (void)doHandleUpdateScreenWithScreenSerialNumber:(NSString *)strSerialNumberHex
{
    ScreenModel *model = [ScreenModel new];
    model.iScreen_scene_id = self.screenModel.iScreen_scene_id;
    model.strScreen_name = self.tableView.editPropertyModel.strScreenEditName;
    model.strScreen_image = self.tableView.editPropertyModel.strScreeenEditIconUrl;
    model.iScreen_scene_type = 1;
    
    model.iScreen_need_linkage = [self.tableView.editPropertyModel.strScreenIsLinkage intValue];
    
    if (self.tableView.editPropertyModel.iScreen_need_time_delay == 0) {
        model.iScreen_need_time_delay = 0;
        model.str_delay_time = @"";
    }else{
        model.iScreen_need_time_delay = 1;
        model.str_delay_time = self.tableView.editPropertyModel.strScreenEditDelayed;
    }
    
    
    if (model.iScreen_need_linkage == 0) {
        if (self.tableView.editPropertyModel.iScreen_need_timing == 0) {
            model.iScreen_need_timing = 0;
            model.str_timing_time = @"";
        }else{
            model.iScreen_need_timing = 1;
            model.str_timing_time = self.tableView.editPropertyModel.strScreenEditTimer;
        }
    }else{
        if (self.tableView.editPropertyModel.iScreen_need_timing == 0) {
            model.iScreen_need_timing = 0;
            model.str_linkage_time = @"";
        }else{
            model.iScreen_need_timing = 1;
            model.str_timing_time = self.tableView.editPropertyModel.str_linkage_time;
        }
    }
    
    
    
    model.iScreen_need_security_on = [self.tableView.editPropertyModel.strScreenEditArming intValue];
    model.iScreen_need_security_off = [self.tableView.editPropertyModel.strScreenEditDisarming intValue];
    model.iScreen_need_linkage = [self.tableView.editPropertyModel.strScreenIsLinkage intValue];
    model.str_serial_number = strSerialNumberHex;
    
    NSMutableArray *mutArr = [NSMutableArray new];
    for (int i = 0; i < self.tableView.arrPerformDeviceList.count; i ++) {
        SHModelDevice *device = self.tableView.arrPerformDeviceList[i];
        SHModelDevice *newDevice = [SHModelDevice new];
        newDevice.iScreenDetailId = device.iScreenDetailId;
        newDevice.iDevice_device_id = device.iDevice_device_id;
        
        newDevice.iDevice_device_state1 = device.iDevice_device_state1;
        newDevice.iDevice_device_state2 = device.iDevice_device_state2;
        newDevice.iDevice_device_state3 = device.iDevice_device_state3;
        newDevice.strDevice_other_status = device.strDevice_other_status;
        [mutArr addObject:newDevice];
    }
    
    for (int i = 0; i < self.tableView.arrLinkageDeviceList.count; i ++) {
        SHModelDevice *device = self.tableView.arrLinkageDeviceList[i];
        SHModelDevice *newDevice = [SHModelDevice new];
        newDevice.iScreenDetailId = device.iScreenDetailId;
        newDevice.iDevice_device_id = device.iDevice_device_id;
        newDevice.iDevice_device_state1 = device.iDevice_device_state1;
        newDevice.iDevice_device_state2 = device.iDevice_device_state2;
        newDevice.iDevice_device_state3 = device.iDevice_device_state3;
        newDevice.strDevice_other_status = device.strDevice_other_status;
        [mutArr addObject:newDevice];
    }
    
    model.arrScreen_scene_details = mutArr;
    
    @weakify(self);
    [self.screenManager handleTheUpdateScreenDataWithModel:model completeHandle:^(BOOL success, id result) {
        @strongify(self);
        
        [XWHUDManager hideInWindow];
        
        [self stopTimer];
        if (success) {
             [XWHUDManager showSuccessTipHUD:@"更新成功"];
            
            [[NSNotificationCenter defaultCenter] postNotificationName:kScreenShouldRefresh
                                                                object:nil
                                                              userInfo:nil];
            [[SHAppInfoManager shareInstance] doSetStrShouldRefreshFavouriteScreen:@"1"];
            
            [self doPopToTheForward];
        }else{
            [XWHUDManager showErrorTipHUD:@"更新失败"];
        }
    }];
}


#pragma mark - 添加场景
- (void)doHandleAddScreenWithScreenSerialNumber:(NSString *)strSerialNumberHex
{
    
    //    NSInteger iIntNo = [[ToolHexManager sharedManager] numberWithHexString:strSerialNumberHex];
    //    NSString *strScreenNoInt = [NSString stringWithFormat:@"%ld",(long)iIntNo];
    
    ScreenModel *model = [ScreenModel new];
    model.iScreen_scene_id = 0;
    model.strScreen_name = self.tableView.editPropertyModel.strScreenEditName;
    model.strScreen_image = self.strIconName;
    //    model.iScreen_scene_type = self.isCommonUse;
    model.iScreen_scene_type = 1;//设置为全部为常用
    
    model.iScreen_need_linkage = [self.tableView.editPropertyModel.strScreenIsLinkage intValue];
    
    if (self.tableView.editPropertyModel.iScreen_need_time_delay == 0) {
        model.iScreen_need_time_delay = 0;
        model.str_delay_time = @"";
    }else{
        model.iScreen_need_time_delay = 1;
        model.str_delay_time = self.tableView.editPropertyModel.strScreenEditDelayed;
    }
    
    if (model.iScreen_need_linkage == 0) {
        if (self.tableView.editPropertyModel.iScreen_need_timing == 0) {
            model.iScreen_need_timing = 0;
            model.str_timing_time = @"";
        }else{
            model.iScreen_need_timing = 1;
            model.str_timing_time = self.tableView.editPropertyModel.strScreenEditTimer;
        }
    }else{
        
        if (self.tableView.editPropertyModel.iScreen_need_timing == 0) {
            model.iScreen_need_timing = 0;
            model.str_linkage_time = @"";
        }else{
            model.iScreen_need_timing = 1;
            model.str_linkage_time = self.tableView.editPropertyModel.str_linkage_time;
        }
    }
    
    
    model.iScreen_need_security_on = [self.tableView.editPropertyModel.strScreenEditArming intValue];
    model.iScreen_need_security_off = [self.tableView.editPropertyModel.strScreenEditDisarming intValue];
    model.iScreen_need_linkage = [self.tableView.editPropertyModel.strScreenIsLinkage intValue];
    model.str_serial_number = strSerialNumberHex;
    
    NSMutableArray *mutArr = [NSMutableArray new];
    for (int i = 0; i < self.tableView.arrPerformDeviceList.count; i ++) {
        SHModelDevice *device = self.tableView.arrPerformDeviceList[i];
        SHModelDevice *newDevice = [SHModelDevice new];
        newDevice.iScreenDetailId = 0;
        newDevice.iDevice_device_id     = device.iDevice_device_id;
        newDevice.iDevice_device_state1 = device.iDevice_device_state1;
        newDevice.iDevice_device_state2 = device.iDevice_device_state2;
        newDevice.iDevice_device_state3 = device.iDevice_device_state3;
        newDevice.strDevice_other_status = device.strDevice_other_status;
        [mutArr addObject:newDevice];
    }
    
    for (int i = 0; i < self.tableView.arrLinkageDeviceList.count; i ++) {
        SHModelDevice *device = self.tableView.arrLinkageDeviceList[i];
        SHModelDevice *newDevice = [SHModelDevice new];
        newDevice.iScreenDetailId = device.iScreenDetailId;
        newDevice.iDevice_device_id = device.iDevice_device_id;
        newDevice.iDevice_device_state1 = device.iDevice_device_state1;
        newDevice.iDevice_device_state2 = device.iDevice_device_state2;
        newDevice.iDevice_device_state3 = device.iDevice_device_state3;
        newDevice.strDevice_other_status = device.strDevice_other_status;
        [mutArr addObject:newDevice];
    }
    
    model.arrScreen_scene_details = mutArr;
    
    @weakify(self);
    [self.screenManager handleTheAddScreenDataWithModel:model completeHandle:^(BOOL success, id result) {
        @strongify(self);
        
        [XWHUDManager hideInWindow];
        [self stopTimer];
        if (success) {
             [XWHUDManager showSuccessTipHUD:@"添加成功"];
            
            [[NSNotificationCenter defaultCenter] postNotificationName:kScreenShouldRefresh
                                                                object:nil
                                                              userInfo:nil];
            //            if (self.isCommonUse ) {
            //                [[SHAppInfoManager shareInstance] doSetStrShouldRefreshFavouriteScreen:@"1"];
            //            }else{
            //                [[SHAppInfoManager shareInstance] doSetStrShouldRefreshFavouriteScreen:@"0"];
            //            }
            [[SHAppInfoManager shareInstance] doSetStrShouldRefreshFavouriteScreen:@"1"];
            
            [self doPopToTheForward];
        }else{
            
            [XWHUDManager showErrorTipHUD:@"加载失败"];
        }
    }];
    
}



/***************************************以下为场景配置模块***************************************/

#pragma mark -
#pragma mark - 普通场景配置指令
- (void)doSendCommonScreenConfigNew
{
    //获取场景指令的条数
    NSString *strInstructionCount = [[ToolHexManager sharedManager] converIntToHex:(int)self.tableView.arrPerformDeviceList.count];
    NSString *strMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    
    NSString *strHexHour;
    NSString *strHexMinute;
    if (self.tableView.editPropertyModel.iScreen_need_timing == 0
        || [self.tableView.editPropertyModel.strScreenEditTimer isKindOfClass:[NSNull class]]
        || [self.tableView.editPropertyModel.strScreenEditTimer isEqualToString:@""]) {
        strHexHour = @"FF";
        strHexMinute = @"FF";
    }else{
        NSArray * stateArr = [self.tableView.editPropertyModel.strScreenEditTimer componentsSeparatedByString:@":"];
        NSString *strHour = stateArr[0];
        NSString *strMinute = stateArr[1];
        strHexHour = [[ToolHexManager sharedManager] converIntToHex:[strHour intValue]];
        strHexMinute = [[ToolHexManager sharedManager] converIntToHex:[strMinute intValue]];
    }
    
    NSData *dataTemp = [[NetworkEngine shareInstance] doHandleConfigureTheScreenOrderWithLength:@"41"
                                                                                            cmdID:@"50"
                                                                                           number:@"00"
                                                                                   gatewayMacAddr:strMacAddr
                                                                                  dataFieldLength:@"36"
                                                                                sonOrderIdentifer:@"01"
                                                                                       screenName:self.tableView.editPropertyModel.strScreenEditName
                                                                                   screenProperty:@"01"//01 代表场景;02 代表联动
                                                                        screenSettingSerialNumber:self.strShouldGiveScreenNoHex
                                                                                  screenCharacter:@"00"
                                                                                   screenBranches:strInstructionCount
                                                                      screenExecutiveSerialNumber:self.strShouldGiveScreenNoHex
                                                                           screenScreenTimingHour:strHexHour
                                                                               screenTimingMinute:strHexMinute
                                                                                  screenTimeDelay:self.tableView.editPropertyModel.strScreenEditDelayed];
    [[NetworkEngine shareInstance] sendRequestData:dataTemp];
    
}


#pragma mark -
#pragma mark - 联动配置指令
- (void)doSendLinkageScreenFirstNew
{
    //获取场景指令的条数
    NSString *strInstructionCount = [[ToolHexManager sharedManager] converIntToHex:(int)self.tableView.arrPerformDeviceList.count];
    for (int i = 0; i < self.tableView.arrLinkageDeviceList.count; i ++) {
        NSLog(@"发送联动配置指令%d",i);
        SHModelDevice *linkageDevice = self.tableView.arrLinkageDeviceList[i];
        //发送联动配置指令
        [self doAddLinkage:self.strShouldGiveScreenNoHex
                  branches:strInstructionCount  //此处是否可以设置为最大值，有待验证
              linkageDevie:linkageDevice];
    }
}

- (void)doAddLinkage:(NSString *)strScreenNoHex
            branches:(NSString *)strBranchesHex
        linkageDevie:(SHModelDevice *)linkageDevice
{
    NSString *strMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSString *strIsForceLinkageHex;     //是否强制联动
    NSString *strEnabledOrDisable;      //启用或者禁用
    NSString *strArmingOrDisarmingHex;  //撤防布防
    
    NSString *strTempState = @"1";
    SHModelDevice *device = self.tableView.arrLinkageDeviceList[0];
    if (device.iDevice_device_state1 == 1) {
        strTempState = @"01";
    }else{
        
        strTempState = @"02";
    }
    
    NSLog(@"777788888********************************* %@",strTempState);
    
    if ([self doHandleDeviceNeedForceLinkage:linkageDevice]) {
        strIsForceLinkageHex        = @"01";
        strEnabledOrDisable         = @"FF";
        strArmingOrDisarmingHex     = @"FF";
    }else{
        strIsForceLinkageHex        = @"02";
        strEnabledOrDisable         = @"01";
        
        if ([self.tableView.editPropertyModel.strScreenEditArming intValue] == 0) {
            strArmingOrDisarmingHex     = @"02";
        }else{
            strArmingOrDisarmingHex     = @"01";
        }
    }
    
    NSString *strCommonPeriodOfTime;
//    if ([self.tableView.editPropertyModel.str_linkage_time isKindOfClass:[NSNull class]]
//        || self.tableView.editPropertyModel.iScreen_need_timing == 0
//        || self.tableView.editPropertyModel.str_linkage_time.length == 0) {
//        strCommonPeriodOfTime = @"24";
//    }else{
//        
//        strCommonPeriodOfTime = self.tableView.editPropertyModel.str_linkage_time;
//    }
    
    if ([self isBlankString:self.tableView.editPropertyModel.str_linkage_time]) {
        strCommonPeriodOfTime = @"24";
    }else{
        strCommonPeriodOfTime = self.tableView.editPropertyModel.str_linkage_time;
    }
    
    NSString *strDelayTime;
    if ([self isBlankString:self.tableView.editPropertyModel.strScreenEditDelayed]) {
        strDelayTime = @"0";
    }else{
        strDelayTime = self.tableView.editPropertyModel.strScreenEditDelayed;
    }
    
    
    NSString *strHexPeriodOfTime = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%04x",[strCommonPeriodOfTime intValue]]];
    
    NSData *dataTemp = [[NetworkEngine shareInstance] doHandleLinkageScreenOrderWithLength:@"41"
                                                                                       cmdID:@"50"
                                                                                      number:@"00"
                                                                              gatewayMacAddr:strMacAddr
                                                                             dataFieldLength:@"36"
                                                                           sonOrderIdentifer:@"01"
                                                                                  screenName:self.tableView.editPropertyModel.strScreenEditName
                                                                              screenProperty:@"02"
                                                                   screenSettingSerialNumber:strScreenNoHex
                                                                             screenCharacter:@"00"
                                                                              screenBranches:strBranchesHex
                                                                                forceLinkage:strIsForceLinkageHex
                                                                   enabledOrDisableIdentifer:strEnabledOrDisable
                                                                  armingOrDisarmingIdentifer:strArmingOrDisarmingHex
                                                                        linkageDeviceMacAddr:linkageDevice.strDevice_mac_address
                                                                                   whichRoad:@"01"
                                                                       linkageDeviceDataType:@"01"
                                                                      linkageDeviceDataRange:strTempState
                                                                                 linkageTime:strHexPeriodOfTime
                                                                           thresholdLowLimit:@"00000001"
                                                                         thresholdUpperLimit:@"FFFFFFFF"
                                                                            linkageDelayTime:strDelayTime];
    [[NetworkEngine shareInstance] sendRequestData:dataTemp];
}


-  (BOOL) isBlankString:(NSString *)string {
    
    if (string == nil || string == NULL) {
        
        return YES;
        
    }
    
    if ([string isKindOfClass:[NSNull class]]) {
        
        return YES;
        
    }
    
    if ([[string stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] length]==0) {
        
        return YES;
        
    }
    
    return NO;
    
}

#pragma mark - 判断哪些是需要强制联动的设备
- (BOOL)doHandleDeviceNeedForceLinkage:(SHModelDevice *)linkageDevice
{
    if ([linkageDevice.strDevice_device_OD isEqualToString:@"0F AA"])
    {
        if ([linkageDevice.strDevice_device_type isEqualToString:@"81"])
        {
            if ([linkageDevice.strDevice_category isEqualToString:@"03"]) {
                return YES;
            }
        }
    }
    else if ([linkageDevice.strDevice_device_OD isEqualToString:@"0F BE"])
    {
        if ([linkageDevice.strDevice_device_type isEqualToString:@"03"])
        {
            
            if ([linkageDevice.strDevice_category isEqualToString:@"02"]) {
                return YES;
            }
        }else if ([linkageDevice.strDevice_device_type isEqualToString:@"05"]){
            if ([linkageDevice.strDevice_category isEqualToString:@"02"]) {
                return YES;
            }
        }else if ([linkageDevice.strDevice_device_type isEqualToString:@"07"]){
            
            if ([linkageDevice.strDevice_category isEqualToString:@"02"]) {
                return YES;
            }
        }
    }
    return NO;
}

#pragma mark - 场景载入或者联动载入
- (void)doWriteToInGateway
{
    NSArray *arrDeviceList =  [self doMakeCombinationNewDeviceWithDeviceList:self.tableView.arrPerformDeviceList];
    for (int i = 0; i < arrDeviceList.count; i ++) {
        SHModelDevice *deviceModel = arrDeviceList[i];
        NSData * data = [self doSendControlOrder:deviceModel];
        //NSLog(@"组合新的数据帧存入网关测试:%@",data);
        int iInstructionIdentifer = 1;
        int iInstructionObject = 1;
        int iDeviceMacAdrr = 8;
        int iSubCommandLength = 1;
        int iSubCommandLengthValue = 4 + (int)data.length; //设备控制命令所有
        int iAll = iInstructionIdentifer + iInstructionObject + iDeviceMacAdrr + iSubCommandLength + iSubCommandLengthValue;
        
        //数据域长度
        NSString *dataLengthHex = [[ToolHexManager sharedManager] converIntToHex:iAll];
        //子数据域长度
        NSString *dataSubCommandLengthHex = [[ToolHexManager sharedManager] converIntToHex:iSubCommandLengthValue];
        //场景中设备的数量
        NSString *strDeviceCountHex = [[ToolHexManager sharedManager] converIntToHex:(int)arrDeviceList.count];
        
        //场景中设备的序号
        NSString *strDeviceCurrentIndexHex = [[ToolHexManager sharedManager] converIntToHex:i+1];
        
        NSData *dataSend = [[NetworkEngine shareInstance] doHandleWriteScreenOrderToGatewayWithDataLength:dataLengthHex
                                                                                              sonDataLength:dataSubCommandLengthHex
                                                                                 controlFrameWriteIdentifer:@"02"
                                                                                                   screenNO:self.strShouldGiveScreenNoHex
                                                                                           instructionCount:strDeviceCountHex
                                                                                    currentInstructionWhich:strDeviceCurrentIndexHex
                                                                                          instructionDetail:data];
        [[NetworkEngine shareInstance] sendRequestData:dataSend];
        
    }
}

#pragma mark - 针对新的情景模式的方法
#pragma mark - 组合新的设备
- (NSArray *)doMakeCombinationNewDeviceWithDeviceList:(NSArray *)arrDeviceListInScreen
{
    NSMutableArray *mutArrNew = [NSMutableArray new];
    
    for (int i = 0; i < arrDeviceListInScreen.count; i ++) {
        SHModelDevice *deviceOrigin = arrDeviceListInScreen[i];
        
        if ([deviceOrigin.strDevice_device_OD isEqualToString:@"0F AA"]) {
            if ([deviceOrigin.strDevice_device_type isEqualToString:@"07"]){
                if ([deviceOrigin.strDevice_category isEqualToString:@"02"]||[deviceOrigin.strDevice_category isEqualToString:@"04"]) {
                    //                    NSLog(@"三路灯开关");
                    if (mutArrNew.count == 0) {
                        [mutArrNew addObject:deviceOrigin];
                    }else{
                        
                        for (int i = 0; i < mutArrNew.count; i ++) {
                            SHModelDevice *deviceHasExisted = mutArrNew[i];
                            if ([deviceOrigin.strDevice_mac_address isEqualToString:deviceHasExisted.strDevice_mac_address]) {
                                SHModelDevice *deviceNew = [self handleGetNewThreeRoadDevice:deviceHasExisted deviceWillAddDevice:deviceOrigin];
                                [mutArrNew replaceObjectAtIndex:i withObject:deviceNew];
                                break;
                            }
                            if (i == mutArrNew.count -1) {
                                [mutArrNew addObject:deviceOrigin];
                            }
                        }
                    }
                }
            }else if ([deviceOrigin.strDevice_device_type isEqualToString:@"06"]){
                if ([deviceOrigin.strDevice_category isEqualToString:@"02"]||[deviceOrigin.strDevice_category isEqualToString:@"04"]) {
                    //                    NSLog(@"二路灯开关");
                    
                    if (mutArrNew.count == 0) {
                        [mutArrNew addObject:deviceOrigin];
                    }else{
                        
                        for (int i = 0; i < mutArrNew.count; i ++) {
                            SHModelDevice *deviceHasExisted = mutArrNew[i];
                            if ([deviceOrigin.strDevice_mac_address isEqualToString:deviceHasExisted.strDevice_mac_address]) {
                                SHModelDevice *deviceNew = [self handleGetNewThreeRoadDevice:deviceHasExisted deviceWillAddDevice:deviceOrigin];
                                [mutArrNew replaceObjectAtIndex:i withObject:deviceNew];
                                break;
                            }
                            if (i == mutArrNew.count -1) {
                                [mutArrNew addObject:deviceOrigin];
                            }
                        }
                    }
                }
            }else{
                [mutArrNew addObject:deviceOrigin];
            }
        }else{
            [mutArrNew addObject:deviceOrigin];
        }
    }
    //NSLog(@"dddddd==%lu",(unsigned long)mutArrNew.count);
    return mutArrNew;
}

- (SHModelDevice *)handleGetNewThreeRoadDevice:(SHModelDevice *)deviceHasExisted deviceWillAddDevice:(SHModelDevice *)deviceAdd
{
    SHModelDevice *deviceNew            = [SHModelDevice new];
    deviceNew.iScreenDetailId           = deviceHasExisted.iScreenDetailId;
    deviceNew.iDevice_device_id         = deviceHasExisted.iDevice_device_id;
    deviceNew.iDevice_room_id           = deviceHasExisted.iDevice_room_id;
    deviceNew.strDevice_room_name       = deviceHasExisted.strDevice_room_name;
    deviceNew.strDevice_device_name     = deviceHasExisted.strDevice_device_name;
    
    deviceNew.strDevice_image           = deviceHasExisted.strDevice_image;
    deviceNew.strDevice_device_OD       = deviceHasExisted.strDevice_device_OD;
    deviceNew.strDevice_device_type     = deviceHasExisted.strDevice_device_type;
    deviceNew.strDevice_category        = deviceHasExisted.strDevice_category;
    
    deviceNew.strDevice_sindex          = deviceHasExisted.strDevice_sindex;
    deviceNew.strDevice_sindex_length   = deviceHasExisted.strDevice_sindex_length;
    deviceNew.strDevice_cmdId           = deviceHasExisted.strDevice_cmdId;
    deviceNew.strDevice_mac_address     = deviceHasExisted.strDevice_mac_address;
    
    deviceNew.iDevice_floor_id          = deviceHasExisted.iDevice_floor_id;
    deviceNew.strDevice_floor_name      = deviceHasExisted.strDevice_floor_name;
    deviceNew.strDevice_alarm_status    = deviceHasExisted.strDevice_alarm_status;
    deviceNew.arrBtns                   = deviceHasExisted.arrBtns;
    
    if ([deviceHasExisted.strDevice_device_type isEqualToString:@"07"]){
        
        if ([deviceAdd.strDevice_other_status intValue] == 1) {
            deviceNew.iDevice_device_state1     = deviceAdd.iDevice_device_state1;
            deviceNew.iDevice_device_state2     = deviceHasExisted.iDevice_device_state2;
            deviceNew.iDevice_device_state3     = deviceHasExisted.iDevice_device_state3;
            
        }else if ([deviceAdd.strDevice_other_status intValue] == 2){
            
            deviceNew.iDevice_device_state1     = deviceHasExisted.iDevice_device_state1;
            deviceNew.iDevice_device_state2     = deviceAdd.iDevice_device_state2;
            deviceNew.iDevice_device_state3     = deviceHasExisted.iDevice_device_state3;
        }else if ([deviceAdd.strDevice_other_status intValue] == 3){
            
            deviceNew.iDevice_device_state1     = deviceHasExisted.iDevice_device_state1;
            deviceNew.iDevice_device_state2     = deviceHasExisted.iDevice_device_state2;
            deviceNew.iDevice_device_state3     = deviceAdd.iDevice_device_state3;
        }
        deviceNew.strDevice_other_status    = @"3";
        
    }else if ([deviceHasExisted.strDevice_device_type isEqualToString:@"06"]){
        
        if ([deviceAdd.strDevice_other_status intValue] == 1) {
            deviceNew.iDevice_device_state1     = deviceAdd.iDevice_device_state1;
            deviceNew.iDevice_device_state2     = deviceHasExisted.iDevice_device_state2;
            deviceNew.iDevice_device_state3     = deviceHasExisted.iDevice_device_state3;
            
        }else if ([deviceAdd.strDevice_other_status intValue] == 2){
            
            deviceNew.iDevice_device_state1     = deviceHasExisted.iDevice_device_state1;
            deviceNew.iDevice_device_state2     = deviceAdd.iDevice_device_state2;
            deviceNew.iDevice_device_state3     = deviceHasExisted.iDevice_device_state3;
        }
        deviceNew.strDevice_other_status    = @"2";
    }
    return deviceNew;
}

#pragma mark - 发送控制命令
- (NSData *)doSendControlOrder:(SHModelDevice *)device
{
    if ([device.strDevice_device_OD isEqualToString:@"0F AA"]) {
        
        if ([device.strDevice_device_type isEqualToString:@"C1"] || [device.strDevice_device_type isEqualToString:@"c1"]) {
            if ([device.strDevice_category isEqualToString:@"02"]) {
                //NSLog(@"六路面板");
            }
            return [self doSendScreen4010DeviceData:device];
        }else if ([device.strDevice_device_type isEqualToString:@"8A"] || [device.strDevice_device_type isEqualToString:@"8a"]) {
            if ([device.strDevice_category isEqualToString:@"02"]) {
                //NSLog(@"场景控制器");
            }
            return [self doSendScreen4010DeviceData:device];
        }else if ([device.strDevice_device_type isEqualToString:@"81"]) {
            if ([device.strDevice_category isEqualToString:@"02"]) {
                //NSLog(@"人体热释传感器");
            }else if ([device.strDevice_category isEqualToString:@"03"]){
                //NSLog(@"一氧化碳检测");
            }else if ([device.strDevice_category isEqualToString:@"04"]){
                //NSLog(@"烟雾传感器");
            }else if ([device.strDevice_category isEqualToString:@"05"]){
                //NSLog(@"SOS报警器");
            }
            return [self doSendScreen4010DeviceData:device];
            
        }else if ([device.strDevice_device_type isEqualToString:@"0E"] || [device.strDevice_device_type isEqualToString:@"0e"]){
            
            if ([device.strDevice_category isEqualToString:@"02"]) {
                //NSLog(@"多彩冷暖灯");
            }
            return [self doSendScreen4010DeviceData:device];
        }else if ([device.strDevice_device_type isEqualToString:@"0B"] ||[device.strDevice_device_type isEqualToString:@"0b"]){
            if ([device.strDevice_category isEqualToString:@"02"]) {
                NSLog(@"多彩球泡灯");
                
                if (device.arrOrder.count) {
                    
                    NSData *data = (NSData *)device.arrOrder[0];
                    return data;
                    
                }else{
                    NSString *strHexState = @"00";
                    int iBrightnessValue = 0;
                    if (device.iDevice_device_state1 == 1) {
                        strHexState = @"01";
                        iBrightnessValue = 255;
                    }else{
                        strHexState = @"02";
                        iBrightnessValue = 0;
                    }
                    
                    int iDelayTime = 0;
                    
                    //延时时间
                    NSString *strHexDelayTime = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%04x",iDelayTime]];
                    NSString *strHexBrightnessValue = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%02x",iBrightnessValue]];
                    
                    NSData *data = [[NetworkEngine shareInstance] doGetColourBulbDimmerWithTargetAddr:device.strDevice_mac_address
                                                                                           lightingMode:@"01"
                                                                                           lightingType:@"02"
                                                                                            controlTime:strHexDelayTime
                                                                                         lightingSwitch:strHexState
                                                                                      brightnessControl:strHexBrightnessValue];
                    return data;
                    
                    
                }
            }
            return nil;
        }else if ([device.strDevice_device_type isEqualToString:@"09"]){
            if ([device.strDevice_category isEqualToString:@"02"]) {
                NSLog(@"声光报警器");
            }
            return [self doSendScreen4010DeviceData:device];
            
        }else if ([device.strDevice_device_type isEqualToString:@"07"]){
            if ([device.strDevice_category isEqualToString:@"02"]) {
                NSLog(@"三路灯开关");
            }else if ([device.strDevice_category isEqualToString:@"04"]) {
                NSLog(@"三路多联多控灯开关");
            }
            return [self doSendScreen4010DeviceData:device];
            
        }else if ([device.strDevice_device_type isEqualToString:@"06"]){
            if ([device.strDevice_category isEqualToString:@"02"]) {
                NSLog(@"二路灯开关");
            }else if ([device.strDevice_category isEqualToString:@"04"]) {
                NSLog(@"二路多联多控灯开关");
            }
            return [self doSendScreen4010DeviceData:device];
            
        }else if ([device.strDevice_device_type isEqualToString:@"05"]){
            if ([device.strDevice_category isEqualToString:@"02"]) {
                NSLog(@"一路灯开关");
            }else if ([device.strDevice_category isEqualToString:@"03"]) {
                NSLog(@"电动玻璃");
            }else if ([device.strDevice_category isEqualToString:@"04"]) {
                NSLog(@"一路多联多控开关");
            }else if ([device.strDevice_category isEqualToString:@"10"]) {
                NSLog(@"86插座");
            }else if ([device.strDevice_category isEqualToString:@"11"]) {
                NSLog(@"移动插座");
            }
            return [self doSendScreen4010DeviceData:device];
        }else if ([device.strDevice_device_type isEqualToString:@"02"]){
            //开、关、停
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
            return [self doSendScreen4010DeviceData:device];
        }else if ([device.strDevice_device_type isEqualToString:@"01"]){
            if ([device.strDevice_category isEqualToString:@"02"]) {
                NSLog(@"普通电动窗帘");
            }
            return [self doSendScreen4010DeviceData:device];
            
        }else{
            NSLog(@"其它设备");
            return [self doSendScreen4010DeviceData:device];
        }
    }else if ([device.strDevice_device_OD isEqualToString:@"0F E6"]){
        if ([device.strDevice_device_type isEqualToString:@"02"]){
            if ([device.strDevice_category isEqualToString:@"02"]){
                NSLog(@"红外学习仪");
                if ([device.strDevice_sindex_length intValue] == SHSindexLength_InfraredAirCondition) {
                    NSLog(@"空调");
                    return [self doSendScreenAirConditionDeviceData:device];
                }else if ([device.strDevice_sindex_length intValue] == SHSindexLength_InfraredOther_Other){
                    NSLog(@"其它遥控设备");
                    return [self doSendScreenOtherRemoteControlDeviceData:device];
                }else{
                    return [self doSendScreenOtherRemoteControlDeviceData:device];
                }
            }else if ([device.strDevice_category isEqualToString:@"03"]){
                NSLog(@"音乐背景器");
                return nil;
            }else if ([device.strDevice_category isEqualToString:@"10"]){
                NSLog(@"电动窗帘");
                return [self doSendScreen4070DeviceData:device];
            }else if ([device.strDevice_category isEqualToString:@"11"]){
                NSLog(@"平移开窗器");
                return [self doSendScreen4070DeviceData:device];
            }else if ([device.strDevice_category isEqualToString:@"12"]){
                NSLog(@"电动床");
                return nil;
            }else if ([device.strDevice_category isEqualToString:@"13"]){
                NSLog(@"新风系统");
                return nil;
            }else if ([device.strDevice_category isEqualToString:@"20"]){
                NSLog(@"浴霸");
                return nil;
            }else{
                return nil;
            }
        }else{
            
            return nil;
        }
    } else if ([device.strDevice_device_OD isEqualToString:@"0F C8"]){
        
        if ([device.strDevice_device_type isEqualToString:@"01"]){
            
            if ([device.strDevice_category isEqualToString:@"02"]){
                NSLog(@"单相电表");
                
            }
            return nil;
        }else if ([device.strDevice_device_type isEqualToString:@"02"]){
            
            if ([device.strDevice_category isEqualToString:@"02"]){
                
                NSLog(@"计量控制盒");
                
            }
            return nil;
        }else if ([device.strDevice_device_type isEqualToString:@"03"]){
            
            if ([device.strDevice_category isEqualToString:@"02"]){
                
                NSLog(@"三相电表");
                
            }
            return nil;
        }else if ([device.strDevice_device_type isEqualToString:@"04"]){
            
            if ([device.strDevice_category isEqualToString:@"02"]){
                
                NSLog(@"10A的计量插座");
            }else if ([device.strDevice_category isEqualToString:@"03"]){
                
                NSLog(@"16A的计量插座");
            }
            return [self doHandleMeasureDevice4040ControlDevice:device];
        }else{
            return nil;
        }
    }else{
        
        return nil;
    }
}

//发送4010设备的
-(NSData *)doSendScreen4010DeviceData:(SHModelDevice *)device
{
    NSData *data = [[NetworkEngine shareInstance] doGetSwitchControlAllWithSendDevice:device];
    NSString *strHexWholeData = [[ToolHexManager sharedManager] convertDataToHexStr:data];
    NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexWholeData];
    NSLog(@"\n发送Screen4010:\n%@",strBigTemp);
    return data;
}

//其它遥控设备的
-(NSData *)doSendScreenOtherRemoteControlDeviceData:(SHModelDevice *)device
{
    SHInfraredKeyModel *keyModel = device.arrBtns[0];
    NSData *data = [[NetworkEngine shareInstance] doGetInfraredSendDataTargetAddr:device.strDevice_mac_address
                                                                          studyCode:[keyModel.strWarehouseNO intValue]];
    NSString *strHexWholeData = [[ToolHexManager sharedManager] convertDataToHexStr:data];
    NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexWholeData];
    NSLog(@"\n************发送Screen其它遥控设备*********:\n%@",strBigTemp);
    return data;
}

//发送平移开窗器或者电动窗帘（只有开和关）
-(NSData *)doSendScreen4070DeviceData:(SHModelDevice *)device
{
    ElectricTransimitObjecActionType actionType;
    if ([device.strDevice_category isEqualToString:@"10"]) {
        if (device.iDevice_device_state1 == 1) {
            actionType = ReverseRotationType;
        }else{
            actionType = PositiveRotationType;
        }
    }else{
        if (device.iDevice_device_state1 == 1) {
            actionType = PositiveRotationType;
        }else{
            actionType = ReverseRotationType;
        }
    }
    
    NSData *data = [[NetworkEngine shareInstance] doGetElectricCurtainsOrWindowDataWithModelDevice:device
                                                                                          actionType:actionType
                                                                                            location:@"00"];
    NSString *strHexWholeData = [[ToolHexManager sharedManager] convertDataToHexStr:data];
    NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexWholeData];
    NSLog(@"\n发送Screen4070:\n%@",strBigTemp);
    
    return data;
    
}

//发送空调的（只有开和关）
-(NSData *)doSendScreenAirConditionDeviceData:(SHModelDevice *)device
{
    NSString *strHexStatu;
    if (device.iDevice_device_state1 == 1) {
        strHexStatu = @"FF";
    }else{
        strHexStatu = @"00";
    }
    NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionOnOrOffTargetAddr:device.strDevice_mac_address
                                                                               strOnOrOff:strHexStatu];
    NSString *strHexWholeData = [[ToolHexManager sharedManager] convertDataToHexStr:data];
    NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexWholeData];
    NSLog(@"\n发送Screen空调:\n%@",strBigTemp);
    return  data;
}

// 发送计量插座控制
- (NSData *)doHandleMeasureDevice4040ControlDevice:(SHModelDevice *)device
{
    NSString *strHexState;
    
    if (device.iDevice_device_state1  == 1) {
        strHexState = @"01";
    }else{
        strHexState = @"02";
        
    }
    NSData *dataSend = [[NetworkEngine shareInstance] doGetMeasureDeviceControlWithTargetAddr:device.strDevice_mac_address
                                                                                         device:device
                                                                                   controlState:strHexState];
    NSString *strHexWholeData = [[ToolHexManager sharedManager] convertDataToHexStr:dataSend];
    NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexWholeData];
    NSLog(@"0FC8进行控制页面:\n%@",strBigTemp);
    return dataSend;
    
}


#pragma mark -
#pragma mark Timer
- (void)startTimer
{
    if (self.timer == nil) {
        self.timer = [[SHRequestTimer alloc] init];
    }
    int timeout_ = 20; //self.timeout > 0 ? self.timeout : DEFAULT_REQUEST_TIMEOUT;
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
    [XWHUDManager hideInWindow];
    if (self.lockOpenStatue == SHAddScreenStatue_Ing) {
        [self stopTimer];
        [XWHUDManager hideInWindow];
        [XWHUDManager showErrorTipHUD:@"配置场景失败"];
        
    }else{
        
        return;
    }
}





- (void)dealloc{
    
    NSLog(@"ddddddddddddddddddddddddddd");
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}






@end

