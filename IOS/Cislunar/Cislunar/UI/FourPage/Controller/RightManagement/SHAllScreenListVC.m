//
//  SHAllScreenListVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/24.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHAllScreenListVC.h"
#import "SHAllScreensTableView.h"
#import "ScreenManager.h"


@interface SHAllScreenListVC ()

@property (strong, nonatomic) SHAllScreensTableView *tableView;
@property (strong, nonatomic) ScreenManager *manager;

@end

@implementation SHAllScreenListVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self doInitSubViews];
    [self doRegisterKVO];
    [self doLoadDataFromNetOrDB];
    [self doAddAction];
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
    if (self.BlockGetScreenList) {
        self.BlockGetScreenList(self.arrHasAddedList);
    }
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark -重写父类方法
- (void)backBarButtonClicked
{
    if (self.BlockGetScreenList) {
        
        self.BlockGetScreenList(self.arrHasAddedList);
    }
    [self.navigationController popViewControllerAnimated:YES];
}


- (void)doLoadDataFromNetOrDB
{
    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
    [self.manager doGetScreenListDataFromDB];
    [self.manager handleGetScreenListFromNetworkWithType:SHScreenType_All];
}

- (void)doRegisterKVO
{
    @weakify(self);
    [self observeKeyPath:@keypath(self.manager.arrScreenList)
                   block:^(id value) {
                       @strongify(self);
                       [XWHUDManager hideInWindow];
                       NSArray *arrScreenList = (NSArray *)value;
                       self.tableView.arrList = arrScreenList;
                       [self doCamparWithArrFromNet:arrScreenList arrTransmit:self.arrHasAddedList];
                   }];
    
    [self observeKeyPath:@keypath(self.manager.errorInfo)
                   block:^(id value) {
                       [XWHUDManager hideInWindow];
                       NSDictionary *dict = (NSDictionary *)value;
                       [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",dict[@"message"]]];
                       
                   }];
}

#pragma mark -
#pragma mark - action
- (void)doAddAction
{
    @weakify(self);
    [self.tableView setBlockGetScreenArrList:^(NSArray *arrScreen) {
        @strongify(self);
        self.arrHasAddedList = arrScreen;
        
    }];
}

- (void)doCamparWithArrFromNet:(NSArray *)arrNet arrTransmit:(NSArray *)arrTransmit
{
    NSMutableArray *mutArrChoose = [NSMutableArray new];
    for (int i = 0; i < arrNet.count; i ++)
    {
        ScreenModel *deviceFromNet = arrNet[i];
        
        for (int j = 0; j < arrTransmit.count; j ++)
        {
            ScreenModel *deviceTransmit = arrTransmit[j];
            
            if (deviceFromNet.iScreen_scene_id == deviceTransmit.iScreen_scene_id)
            {
                [mutArrChoose addObject:deviceFromNet];
            }
            
        }
    }
    self.tableView.arrHasAdd = mutArrChoose;
}




#pragma mark -
#pragma mark - 懒加载
- (SHAllScreensTableView *)tableView
{
    if (!_tableView) {
        _tableView = [[SHAllScreensTableView alloc] initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, UI_SCREEN_HEIGHT - 64) style:UITableViewStylePlain];
    }
    return _tableView;
}

-(ScreenManager *)manager
{
    if (!_manager) {
        _manager = [ScreenManager new];
    }
    return _manager;
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

@end
