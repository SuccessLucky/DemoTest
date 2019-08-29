//
//  DeviceAllListVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/26.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import "DeviceAllListVC.h"
#import "SHDeviceManager.h"
#import "DeviceAllListTable.h"

#import "DeviceListAllModel.h"

@interface DeviceAllListVC ()

@property (strong, nonatomic) SHDeviceManager *manager;
@property (strong, nonatomic) DeviceAllListTable *tableView;
@property (weak, nonatomic) IBOutlet UIButton *btnSelectedAllDevice;

@end

@implementation DeviceAllListVC

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.isHideNaviBar = NO;
    [self doInitSubViews];
    [self doRegisterKVO];
    [self doLoadData];
    
    if (self.iType == ScreenEditAddDeviceTypePeform) {
        //执行设备列表
        NSLog(@"执行设备");
    }else{
    
        NSLog(@"联动设备");
        
    }
    NSLog(@"self.type == %d",self.iType);
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

#pragma mark -
#pragma mark - init
- (SHDeviceManager *)manager
{
    if (!_manager) {
        _manager = [SHDeviceManager new];
    }
    return _manager;
}

- (DeviceAllListTable *)tableView
{
    if (!_tableView) {
        _tableView = [[DeviceAllListTable alloc]
                      initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, UI_SCREEN_HEIGHT-64 - 50)
                      style:UITableViewStylePlain];
    }
    return _tableView;
}

#pragma mark -
#pragma mark - initSubViews
- (void)doInitSubViews
{
    [self setTitleViewText:@"设备列表"];
    [self doSetRightBtn];
    [self setNavigationBarBackButtonWithTitle:@"返回" action:@selector(backBarButtonClicked)];
    [self.view addSubview:self.tableView];
}

#pragma mark -- 点击编辑进行添加
- (void)doSetRightBtn {
    UIButton *rightBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [rightBtn setFrame:CGRectMake(80, 10, 70, 25)];
    rightBtn.titleLabel.font = [UIFont systemFontOfSize:17];
    [rightBtn setTitleEdgeInsets:UIEdgeInsetsMake(0, 0, 0, -15)];
    [rightBtn addTarget:self action:@selector(rightAction:) forControlEvents:UIControlEventTouchUpInside];
    [rightBtn setTitle:@"完成" forState:UIControlStateNormal];
    [rightBtn setTag:12];
    UIBarButtonItem *rightBtnItem = [[UIBarButtonItem alloc]initWithCustomView:rightBtn];
    self.navigationItem.rightBarButtonItem = rightBtnItem;
}

- (void)rightAction:(UIButton *)sender
{
    NSArray *arrSelected = [self doTraverseAllSelectedDevice:self.tableView.deviceListAllModel];
    NSArray *arrNeed = [self handleGetStandardDeviceModel:arrSelected];
    if (self.BlockGetDeviceList) {
        self.BlockGetDeviceList(self.iType,arrNeed);
    }
    
    NSLog(@"选中的数据帧selectArr.coun == %lu",(unsigned long)arrSelected.count);
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark -重写父类方法
- (void)backBarButtonClicked
{
    [self.navigationController popViewControllerAnimated:YES];
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
                       if (arrNetOrDB.count == 0) {
                           self.tableView.hidden = YES;
                       }else{
                           self.tableView.hidden = NO;
                           NSArray *arrGet = [self doHandleTheDatasourceToNewTable:arrNetOrDB];
                           
                           NSArray *arrTemp;
                           if (self.iType == ScreenEditAddDeviceTypePeform) {
                               NSLog(@"执行设备");
                               arrTemp = [self doGetArrPeform:arrGet];
                               
                           }else{
                               NSLog(@"联动设备");
                               arrTemp = [self doGetArrLinkage:arrGet];
                               
                           }
                           
                           self.tableView.deviceListAllModel = [self doInitDeviceListAllModel:arrTemp];
                           
                           //遍历所有已经添加的设备重新刷新table
                           [self doComparedWithHasAddedAndFromNet:self.tableView.deviceListAllModel];
                           
                           self.tableView.btnSelectedAllTr = self.btnSelectedAllDevice;
                       }
                       NSLog(@"*****arrResult = %@",arrNetOrDB);
                   }];
    
    [self observeKeyPath:@keypath(self.manager.errorInfo)
                   block:^(id value) {
                       [XWHUDManager hideInWindow];
                       NSDictionary *dict = (NSDictionary *)value;
                       [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",dict[@"message"]]];
                   }];
}

#pragma mark -
#pragma mark - loadData
- (void)doLoadData
{
    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
    [self.manager doGetAllDeviceListDataFromDBV2];
    [self.manager doGetAllDeviceListFromNetworkV2];
}


#pragma mark -
#pragma mark - 转换为需要的数据源
- (NSArray *)doHandleTheDatasourceToNewTable:(NSArray *)arrResult
{
    NSMutableArray *mutArrAllDeviceList = [NSMutableArray new];
    for (int i = 0; i < arrResult.count; i ++) {
        
        SHModelRoom *roomOld = arrResult[i];
        RoomModel *roomNew = [RoomModel new];
        roomNew.detailDateArr = [self handleGetDeviceNewModel:roomOld.arrDeviceList];
        roomNew.iRoom_id = roomOld.iRoom_id;
        roomNew.strRoom_name = roomOld.strRoom_name;
        roomNew.strRoom_image = roomOld.strRoom_image;
        roomNew.strFloorName = roomOld.strFloorName;
        roomNew.arrDeviceList = roomOld.arrDeviceList;
        [mutArrAllDeviceList addObject:roomNew];
    }
    return mutArrAllDeviceList;
}


#pragma mark - 由SHModelDevice转换为DeviceModel
- (NSMutableArray *)handleGetDeviceNewModel:(NSArray *)result
{
    NSMutableArray *mutArr = [NSMutableArray new];
    for (int i = 0; i < result.count; i ++) {
        SHModelDevice *modelOld = result[i];
        DeviceModel *modelNew   = [DeviceModel new];
        modelNew.iDevice_device_id             = modelOld.iDevice_device_id;
        modelNew.iDevice_room_id               = modelOld.iDevice_room_id;
        modelNew.strDevice_room_name           = modelOld.strDevice_room_name;
        modelNew.strDevice_device_name         = modelOld.strDevice_device_name;
        
        modelNew.strDevice_image               = modelOld.strDevice_image;
        modelNew.strDevice_device_OD           = modelOld.strDevice_device_OD;
        modelNew.strDevice_device_type         = modelOld.strDevice_device_type;
        modelNew.strDevice_category            = modelOld.strDevice_category ;
        
        modelNew.strDevice_sindex              = modelOld.strDevice_sindex;
        modelNew.strDevice_sindex_length       = modelOld.strDevice_sindex_length;
        modelNew.strDevice_cmdId               = modelOld.strDevice_cmdId;
        modelNew.strDevice_mac_address         = modelOld.strDevice_mac_address;
        
        modelNew.strDevice_other_status        = modelOld.strDevice_other_status;
        modelNew.iDevice_device_state1         = modelOld.iDevice_device_state1;
        modelNew.iDevice_device_state2         = modelOld.iDevice_device_state2;
        modelNew.iDevice_device_state3         = modelOld.iDevice_device_state3;
        
        modelNew.iDevice_floor_id              = modelOld.iDevice_floor_id;
        modelNew.strDevice_floor_name          = modelOld.strDevice_floor_name;
        modelNew.strDevice_alarm_status        = modelOld.strDevice_alarm_status;
        
        modelNew.arrBtns = modelOld.arrBtns;
 
        [mutArr addObject:modelNew];
    }
    return mutArr;
}

#pragma mark - 由DeviceModel转换为SHModelDevice
- (NSMutableArray *)handleGetStandardDeviceModel:(NSArray *)result
{
    NSMutableArray *mutArr = [NSMutableArray new];
    for (int i = 0; i < result.count; i ++) {
        DeviceModel *modelOld = result[i];
        SHModelDevice *modelNew   = [SHModelDevice new];
        modelNew.iDevice_device_id             = modelOld.iDevice_device_id;
        modelNew.iDevice_room_id               = modelOld.iDevice_room_id;
        modelNew.strDevice_room_name           = modelOld.strDevice_room_name;
        modelNew.strDevice_device_name         = modelOld.strDevice_device_name;
        
        modelNew.strDevice_image               = modelOld.strDevice_image;
        modelNew.strDevice_device_OD           = modelOld.strDevice_device_OD;
        modelNew.strDevice_device_type         = modelOld.strDevice_device_type;
        modelNew.strDevice_category            = modelOld.strDevice_category ;
        
        modelNew.strDevice_sindex              = modelOld.strDevice_sindex;
        modelNew.strDevice_sindex_length       = modelOld.strDevice_sindex_length;
        modelNew.strDevice_cmdId               = modelOld.strDevice_cmdId;
        modelNew.strDevice_mac_address         = modelOld.strDevice_mac_address;
        
        modelNew.strDevice_other_status        = modelOld.strDevice_other_status;
        modelNew.iDevice_device_state1         = 2;
        modelNew.iDevice_device_state2         = 2;
        modelNew.iDevice_device_state3         = 2;
        
        modelNew.iDevice_floor_id              = modelOld.iDevice_floor_id;
        modelNew.strDevice_floor_name          = modelOld.strDevice_floor_name;
        modelNew.strDevice_alarm_status        = modelOld.strDevice_alarm_status;
        
        modelNew.arrBtns = modelOld.arrBtns;
        
        [mutArr addObject:modelNew];
    }
    return mutArr;
}

- (DeviceListAllModel *)doInitDeviceListAllModel:(NSArray *)arr
{
    NSMutableArray *mutableArr = [NSMutableArray array];
    DeviceListAllModel *model = [[DeviceListAllModel alloc] init];
    for (int i =0; i < arr.count; i ++) {
        RoomModel *room = arr[i];
        [mutableArr addObject:room];
    }
    model.headModelArr = mutableArr;
    return model;
}

#pragma mark -
#pragma mark - do add action
- (IBAction)btnDoAddAllDevicePressed:(UIButton *)sender {
    sender.selected = !sender.selected;
    [self doTraverseDataWithDeviceListAllModel:self.tableView.deviceListAllModel isAlChoose:sender.selected];
    [self.tableView reloadData];
}

//点击全选
- (void)doTraverseDataWithDeviceListAllModel:(DeviceListAllModel *)deviceListAllModel isAlChoose:(BOOL)isAllChoose
{
    @weakify(self);
    [deviceListAllModel.headModelArr enumerateObjectsUsingBlock:^(RoomModel  *_Nonnull roomModel, NSUInteger idx, BOOL * _Nonnull stop) {
        @strongify(self);
        /** 所有的Section选中*/
        roomModel.isChoose = isAllChoose;
        [self doTraverseModelDevice:roomModel isAlChoose:isAllChoose];
    }];
}

- (void)doTraverseModelDevice:(RoomModel *)roomModel isAlChoose:(BOOL)isAllChoose
{
    [roomModel.detailDateArr enumerateObjectsUsingBlock:^(DeviceModel  *_Nonnull deviceModel, NSUInteger idx, BOOL * _Nonnull stop) {
        /** 所有的cell选中*/
        deviceModel.isChoosed = isAllChoose;
    }];
}



#pragma mark -
#pragma mark - 获取共有多少被选中
- (NSArray *)doTraverseAllSelectedDevice:(DeviceListAllModel *)deviceListAllModel
{
    NSMutableArray *mutArr = [NSMutableArray new];
    // 循环遍历  将所有选中的cell 添加到数组中
    for (RoomModel *roomModel in deviceListAllModel.headModelArr) {
        [roomModel.detailDateArr enumerateObjectsUsingBlock:^(DeviceModel  *_Nonnull deviceModel, NSUInteger idx, BOOL * _Nonnull stop) {
            
            //所有选中的cell
            if (deviceModel.isChoosed) {
                [mutArr addObject:deviceModel];
            }
        }];
    }
    return mutArr;
}


#pragma mark -
#pragma mark - 场景里面现存的设备列表和从网上拉取下来的设备列表对比
- (void)doComparedWithHasAddedAndFromNet:(DeviceListAllModel *)deviceListAllModel
{
    for (RoomModel *roomModel in deviceListAllModel.headModelArr) {
        
        @weakify(self);
        [roomModel.detailDateArr enumerateObjectsUsingBlock:^(DeviceModel  *_Nonnull deviceModel, NSUInteger idx, BOOL * _Nonnull stop) {
            @strongify(self);
            for (int i = 0; i < self.arrHasAddedList.count; i ++) {
                SHModelDevice *hasAddModelDeivce = self.arrHasAddedList[i];
                if (deviceModel.iDevice_device_id == hasAddModelDeivce.iDevice_device_id) {
                    deviceModel.isChoosed = YES;
                }
            }
        }];
    }
    [self.tableView reloadData];
}

#pragma mark -
#pragma mark - 筛选出我们需要的设备（场景里面只需要可以控制的设备，休眠设备筛选掉）
- (NSArray *)doGetArrPeform:(NSArray *)arrResult
{
    NSMutableArray *mutArrCouldControl = [NSMutableArray new];
//    NSMutableArray *mutDeviceListOnlyReport = [NSMutableArray new];
    for (int i = 0; i < arrResult.count; i ++) {
        RoomModel *roomNew = arrResult[i];
        roomNew.detailDateArr = [self handleGetDeviceNewModelCouldControl:roomNew.detailDateArr isControlDevice:YES];
//        [mutArrCouldControl addObject:roomNew];
        
        if (roomNew.detailDateArr.count) {
            [mutArrCouldControl addObject:roomNew];
        }
    }
    return mutArrCouldControl;
}

- (NSArray *)doGetArrLinkage:(NSArray *)arrResult
{
    NSMutableArray *mutDeviceListOnlyReport = [NSMutableArray new];
    for (int i = 0; i < arrResult.count; i ++) {
        RoomModel *roomNew = arrResult[i];
        roomNew.detailDateArr = [self handleGetDeviceNewModelCouldControl:roomNew.detailDateArr isControlDevice:NO];
//        [mutDeviceListOnlyReport addObject:roomNew];
        if (roomNew.detailDateArr.count) {
            [mutDeviceListOnlyReport addObject:roomNew];
        }
    }
    return mutDeviceListOnlyReport;
}

- (NSMutableArray *)handleGetDeviceNewModelCouldControl:(NSArray *)result isControlDevice:(BOOL)isControlDevice
{
    NSMutableArray *mutArrCouldControl = [NSMutableArray new];
    NSMutableArray *mutDeviceListOnlyReport = [NSMutableArray new];
    
    for (int i = 0; i < result.count; i ++) {
        DeviceModel *modelNew   = result[i];
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
                
                if ([modelNew.strDevice_device_type isEqualToString:@"8A"]) {
                    if ([modelNew.strDevice_category isEqualToString:@"02"]) {
                        
                    }
                }else{
                
                    [mutDeviceListOnlyReport addObject:modelNew];
                }
            }
            else
            {
                [mutArrCouldControl addObject:modelNew];
            }
        }
        else if ([modelNew.strDevice_device_OD isEqualToString:@"0F BE"])
        {
            if ([modelNew.strDevice_device_type isEqualToString:@"02"]) {
                if ([modelNew.strDevice_category isEqualToString:@"02"]) {
                    NSLog(@"指纹锁");
                }
            }else{
                [mutDeviceListOnlyReport addObject:modelNew];
            }
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



@end
