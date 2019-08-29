//
//  LockBindMemberVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/16.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "LockBindMemberVC.h"
#import "SHLockMemModel.h"
#import "LockBindTableViewCell.h"
#import "SHLockManager.h"

typedef NS_ENUM(NSInteger, SHLockStatue)
{
    SHLockStatue_Ready              = 0,   //准备开锁
    SHLockStatue_Ing                = 1,   //开锁中
    SHLockStatue_Finish             = 2,   //开锁完成
    SHLockStatue_Fail               = 3,   //开锁失败
};


@interface LockBindMemberVC ()<UITableViewDelegate,UITableViewDataSource>

@property (strong, nonatomic) UITableView *tableView;

@property (strong, nonatomic) NSArray *dataArray;

@property (strong, nonatomic)SHLockManager *lockManager;

@property (assign, nonatomic) NetworkEngine *networkEngine;

@property (strong, nonatomic) SHRequestTimer *timer;

@property (assign, nonatomic) SHLockStatue lockOpenStatue;

@property (assign, nonatomic) BOOL isTouchedOnAddMemberBtn;

@end

@implementation LockBindMemberVC

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    self.isTouchedOnAddMemberBtn = NO;
    [self.navigationController setNavigationBarHidden:YES animated:YES];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.isTouchedOnAddMemberBtn = NO;
//    [self setTitleViewText:@"绑定联系人"];
    self.title = @"绑定联系人";
    self.networkEngine = [NetworkEngine shareInstance];
    [self setRightBtn];
    [self.view addSubview:self.tableView];
    [self doRegisterKVO];
    [self doLoadData];
}


#pragma mark -
#pragma mark - loadData
- (void)doLoadData
{
    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
    [self.lockManager doGetLockMemberListDataFromDBWithDeviceMacAddr:self.strDeviceMacAddr];
    [self.lockManager doGetLockMemberListFromNetworkWithDeviceMacAddr:self.strDeviceMacAddr];
}

#pragma mark -
#pragma mark - 注册kvo
- (void)doRegisterKVO
{
    @weakify(self);
    //指纹锁密码列表
    [self observeKeyPath:@keypath(self.lockManager.arrLockMemberList)
                   block:^(id value) {
                       @strongify(self);
                       [XWHUDManager hideInWindow];
                       NSArray *arr = (NSArray *)value;
                       if (arr.count) {
                           self.tableView.hidden = NO;
                           self.dataArray = arr;
                           [self.tableView reloadData];
                       }else{
                           self.tableView.hidden = YES;
                       }
                   }];
    
    [self observeKeyPath:@keypath(self.lockManager.errorInfo)
                   block:^(id value) {
                       @strongify(self);
                       self.tableView.hidden = YES;
                       [XWHUDManager hideInWindow];
                       NSDictionary *dict = (NSDictionary *)value;
                       [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",dict[@"message"]]];
                   }];
    
    [self observeKeyPath:@keypath(self.networkEngine.modelDevice) block:^(id value) {
        @strongify(self);
        
        if (self.isTouchedOnAddMemberBtn) {
            [XWHUDManager hideInWindow];
            SHModelDevice *deviceReport = (SHModelDevice *)value;
            
            if (self.lockOpenStatue == SHLockStatue_Ing) {
                if ([deviceReport.strDevice_device_OD isEqualToString:@"0F BE"])
                {
                    if ([deviceReport.strDevice_device_type isEqualToString:@"02"])
                    {
                        if ([deviceReport.strDevice_category isEqualToString:@"02"])
                        {
                            if ([deviceReport.strDevice_sindex_length isEqualToString:@"50"])
                            {
                                self.lockOpenStatue = SHLockStatue_Finish;
                                [self stopTimer];
                                [self doSkipTheAddMemberWithLockID:deviceReport.strFingerID];
                            }
                        }else if ([deviceReport.strDevice_category isEqualToString:@"03"]){
                            if ([deviceReport.strOpenXMYLockReport_0002_IDType isEqualToString:@"01"]
                                ||[deviceReport.strOpenXMYLockReport_0002_IDType isEqualToString:@"02"]||[deviceReport.strOpenXMYLockReport_0002_IDType isEqualToString:@"03"]||[deviceReport.strOpenXMYLockReport_0002_IDType isEqualToString:@"04"]) {
                                self.lockOpenStatue = SHLockStatue_Finish;
                                [self stopTimer];
                                [self doSkipTheAddMemberWithLockID:deviceReport.strFingerID];
                            }
                            
                        }
                    }
                }
            }
        }
        
    }];
}

#pragma mark -- 设置导航栏右边历史账单按钮
- (void)setRightBtn {
    UIButton *bindMemBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [bindMemBtn setFrame:CGRectMake(80, 10, 70, 25)];
    bindMemBtn.titleLabel.font = [UIFont systemFontOfSize:16];
    [bindMemBtn setTitleEdgeInsets:UIEdgeInsetsMake(0, 0, 0, -15)];
    [bindMemBtn addTarget:self action:@selector(bindMemBtnAction) forControlEvents:UIControlEventTouchUpInside];
    [bindMemBtn setTitle:@"添加" forState:UIControlStateNormal];
    [bindMemBtn setTag:12];
    UIBarButtonItem *rightBtn = [[UIBarButtonItem alloc]initWithCustomView:bindMemBtn];
    self.navigationItem.rightBarButtonItem = rightBtn;
}

- (void)bindMemBtnAction
{
    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
    self.isTouchedOnAddMemberBtn = YES;
    self.lockOpenStatue = SHLockStatue_Ing;
    [self startTimerTwo];
    
}

- (void)doSkipTheAddMemberWithLockID:(NSString *)strID
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"绑定联系人" message:nil preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    
    @weakify(self);
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        @strongify(self);
        UITextField *textFiled1 = alertController.textFields.firstObject;
        UITextField *textFiled2 = alertController.textFields.lastObject;
        NSString *strPepName = textFiled1.text;
        NSString *strPepID = [NSString stringWithFormat:@"%d",[textFiled2.text intValue]];
        NSString *strHEXID =  [[ToolHexManager sharedManager] converIntToHex:[strPepID intValue]];
        NSString *strBigSytemID  = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHEXID];
        NSLog(@"获取到的指纹HEX类型ID == %@",strBigSytemID);
        
        [self doSendOrderAndSaveToNetserverWithPepName:strPepName pepID:strPepID];
        
    }];
    [alertController addTextFieldWithConfigurationHandler:^(UITextField *textField){
        textField.placeholder = @"请输入绑定的人名";
    }];
    
    [alertController addTextFieldWithConfigurationHandler:^(UITextField *textField) {
        
        textField.placeholder = @"请输入指纹ID";
        NSInteger iIntNo = [[ToolHexManager sharedManager] numberWithHexString:strID];
        textField.text = [NSString stringWithFormat:@"%ld",(long)iIntNo];
        
        NSLog(@"获取到的指纹int类型ID=%ld",(long)iIntNo);
        //        textField.secureTextEntry = YES;
        
    }];
    [alertController  addAction:cancelAction];
    [alertController addAction:okAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

- (void)doSendOrderAndSaveToNetserverWithPepName:(NSString *)strPepName pepID:(NSString *)strPepID
{
    if (strPepName.length > 0 && strPepID.length >0) {
        @weakify(self);
        [self.lockManager handleTheAddLockMemberDataWithDeviceID:self.iDeviceID
                                                      memberName:strPepName
                                                   fingerprintId:strPepID
                                                  completeHandle:^(BOOL success, id result)
         {
             @strongify(self);
             if (success) {
                 
                 [self.lockManager doGetLockMemberListFromNetworkWithDeviceMacAddr:self.strDeviceMacAddr];
                 //                 [self.lockManager doGetLockMemberListFromNetworkWithDeviceID:self.iDeviceID];
             }else{
                 
                [XWHUDManager showErrorTipHUD:@"添加失败"];
                 
             }
         }];
    }else{
        
        if (strPepName.length == 0) {
            [XWHUDManager showWarningTipHUD:@"请输入名字"];
        }else {
            
            [XWHUDManager showWarningTipHUD:@"请输入指纹ID"];
        }
    }
}

#pragma mark TableView

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return self.dataArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *cellIdentifier = @"LockBindTableViewCell";
    LockBindTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (!cell) {
        
        cell= [[[NSBundle mainBundle]loadNibNamed:@"LockBindTableViewCell" owner:nil options:nil] firstObject];
    }
    SHLockMemModel *model = self.dataArray[indexPath.row];
    cell.labelName.text = [NSString stringWithFormat:@"%@",model.strLock_user_name];
    cell.labelDetail.text = [NSString stringWithFormat:@"开锁次数：%d",model.iUnlock_times];
    return cell;
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    
    SHLockMemModel *model = self.dataArray[indexPath.row];
    
    @weakify(self);
    [self.lockManager doDeleteLockMemberWithLockID:model.iLockID
                                          lockType:@"fingerprint"
                                    completeHandle:^(BOOL success, id result)
     {
         @strongify(self);
         if (success) {
             [self.lockManager doGetLockMemberListFromNetworkWithDeviceMacAddr:self.strDeviceMacAddr];
         }else{
             
             [XWHUDManager showErrorTipHUD:@"删除失败"];
             
         }
     }];
    
    
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 75.0f;
}

#pragma mark -
#pragma mark - init

- (SHLockManager *)lockManager
{
    if (!_lockManager) {
        _lockManager = [SHLockManager new];
    }
    return  _lockManager;
}

- (UITableView *)tableView
{
    if (!_tableView) {
        _tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, UI_SCREEN_HEIGHT - 64)
                                                  style:UITableViewStylePlain];
        _tableView.delegate = self;
        _tableView.dataSource = self;
        _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        _tableView.backgroundColor = kBackgroundGrayColor;
    }
    return _tableView;
}

- (NSArray *)dataArray
{
    if (!_dataArray) {
        _dataArray = [NSArray new];
    }
    return _dataArray;
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
#pragma mark Timer
- (void)startTimerTwo
{
    if (self.timer == nil) {
        self.timer = [[SHRequestTimer alloc] init];
    }
    int timeout_ = 15; //self.timeout > 0 ? self.timeout : DEFAULT_REQUEST_TIMEOUT;
    [self.timer start:timeout_ target:self sel:@selector(timerActionTwo)];
}

- (void)stopTimer
{
    self.timer = nil;
    [self.timer stop];
}

- (void)timerActionTwo
{
    NSLog(@"看时间");
    if (self.lockOpenStatue == SHLockStatue_Ing) {
        [self stopTimer];
        [XWHUDManager hideInWindow];
       [XWHUDManager showErrorTipHUD:@"添加指纹ID失败"];
        
    }else{
        
        return;
    }
}


@end
































