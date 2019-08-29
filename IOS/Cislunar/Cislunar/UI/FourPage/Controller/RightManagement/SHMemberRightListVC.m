//
//  SHMemberRightListVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/21.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHMemberRightListVC.h"
#import "SHMemberRightListTableView.h"
#import "SHMemberManager.h"
#import "SHAllDeviceListVC.h"
#import "SHAllScreenListVC.h"

@interface SHMemberRightListVC ()

@property (strong, nonatomic) SHMemberRightListTableView *tableView;
@property (strong, nonatomic) SHMemberManager *manager;

@end

@implementation SHMemberRightListVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitleViewText:@"权限列表"];
//    self.title = @"权限列表";
    [self doInitSubViews];
    [self doRegisterKVO];
    [self doLoadData];
}

#pragma mark -
#pragma mark - 添加 subViews

- (void)doInitSubViews
{
    [self setNavigationBarBackButtonWithTitle:@"返回" action:@selector(backBarButtonClicked)];
    [self.view addSubview:self.tableView];
    [self doSetRightBtn];
}

- (void)backBarButtonClicked
{
    
    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
    NSMutableArray *mutArrDeviceID = [NSMutableArray new];
    NSMutableArray *mutArrScreenID = [NSMutableArray new];
    
    for (int i = 0; i < self.tableView.arrDeviceList.count; i ++) {
        SHModelDevice *model = self.tableView.arrDeviceList[i];
        [mutArrDeviceID addObject:@(model.iDevice_device_id)];
    }
    
    for (int i = 0; i < self.tableView.arrScreenList.count; i ++) {
        ScreenModel *model = self.tableView.arrScreenList[i];
        [mutArrScreenID addObject:@(model.iScreen_scene_id)];
    }
    

    
    [self.manager handleTheAddMemberRightListDataWithMemberID:self.iMemberID
                                                    arrScreen:mutArrScreenID
                                                    arrDevice:mutArrDeviceID
                                               completeHandle:^(BOOL success, id result)
     {
         [XWHUDManager hideInWindow];
        [self.navigationController popViewControllerAnimated:YES];
    }];
}


//点击编辑进行添加
- (void)doSetRightBtn {
    UIButton *rightBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [rightBtn setFrame:CGRectMake(80, 10, 70, 25)];
    rightBtn.titleLabel.font = [UIFont systemFontOfSize:16];
    [rightBtn setTitleEdgeInsets:UIEdgeInsetsMake(0, 0, 0, -15)];
    [rightBtn addTarget:self action:@selector(rightAction:) forControlEvents:UIControlEventTouchUpInside];
    [rightBtn setTitle:@"添加" forState:UIControlStateNormal];
    [rightBtn setTag:12];
    UIBarButtonItem *rightBtnItem = [[UIBarButtonItem alloc]initWithCustomView:rightBtn];
    self.navigationItem.rightBarButtonItem = rightBtnItem;
}
- (void)rightAction:(UIButton *)btn
{
    @weakify(self);
    
     LCActionSheet *actionSheet = [LCActionSheet sheetWithTitle:@"请选择你要添加的设备类型"
                                              cancelButtonTitle:@"取消"
                                                        clicked:^(LCActionSheet * _Nonnull actionSheet, NSInteger buttonIndex) {
                                                            @strongify(self);
                                                            switch (buttonIndex) {
                                                                case 1:
                                                                {
                                                                    [self performSegueWithIdentifier:@"seg_to_SHAllScreenListVC" sender:self];
                                                                }
                                                                    break;
                                                                case 2:
                                                                {
                                                                    
                                                                    [self performSegueWithIdentifier:@"seg_to_SHAllDeviceListVC" sender:self];
                                                                }
                                                                    break;
                                                                case 0:
                                                                {
                                                                    NSLog(@"点了取消");
                                                                }
                                                                    break;
                                                                default:
                                                                    break;
                                                            }
                                                        }
                                              otherButtonTitles:@"情景模式",@"单一设备", nil];
    
    [actionSheet show];

}

#pragma mark -
#pragma mark - loadData
- (void)doLoadData
{
    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
    [self.manager doGetMemberRightListFromNetworkWithMemberID:self.iMemberID];
    [self.manager doGetMemberRightListFromDBWithMemberID:self.iMemberID];
}

#pragma mark -
#pragma mark - 注册kvo
- (void)doRegisterKVO
{
    @weakify(self);
    [self observeKeyPath:@keypath(self.manager.dictControlList)
                   block:^(id value) {
                       @strongify(self);
                       [XWHUDManager hideInWindow];
                       NSDictionary *dict = (NSDictionary *)value;
                       self.tableView.arrDeviceList = dict[@"MemberDeviceList"];
                       self.tableView.arrScreenList = dict[@"MemberScreenList"];
                   }];
    
    [self observeKeyPath:@keypath(self.manager.controlListErrorInfo)
                   block:^(id value) {
                       [XWHUDManager hideInWindow];
                       NSDictionary *dict = (NSDictionary *)value;
                       [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",dict[@"message"]]];
                   }];
}


#pragma mark -
#pragma mark - 懒加载
- (SHMemberRightListTableView *)tableView
{
    if (!_tableView) {
        _tableView = [[SHMemberRightListTableView alloc] initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, UI_SCREEN_HEIGHT -64) style:UITableViewStyleGrouped];
    }
    return _tableView;
}

- (SHMemberManager *)manager
{
    if (!_manager) {
        _manager = [SHMemberManager new];
    }
    return _manager;
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
    if ([segue.identifier isEqualToString:@"seg_to_SHAllDeviceListVC"]) {
        SHAllDeviceListVC *vc = (SHAllDeviceListVC *)segue.destinationViewController;
        vc.arrHasAddedList = self.tableView.arrDeviceList;
        [vc setBlockGetDeviceList:^(NSArray *arrDeviceHasAdd) {
            self.tableView.arrDeviceList = arrDeviceHasAdd;
        }];
        
        
    }
    if ([segue.identifier isEqualToString:@"seg_to_SHAllScreenListVC"]) {
        SHAllScreenListVC *vc = (SHAllScreenListVC *)segue.destinationViewController;
        vc.arrHasAddedList = self.tableView.arrScreenList;
        [vc setBlockGetScreenList:^(NSArray *arrScreenHasAdd) {
            self.tableView.arrScreenList = arrScreenHasAdd;
        }];
        
        
    }
    
}

@end
