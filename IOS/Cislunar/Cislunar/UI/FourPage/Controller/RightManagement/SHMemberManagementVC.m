//
//  SHMemberManagementVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/19.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHMemberManagementVC.h"
#import "RightManagementTableView.h"
#import "SHMemberManager.h"
#import "SHMemberModel.h"
#import "SHMemberRightListVC.h"

@interface SHMemberManagementVC ()

@property (strong, nonatomic) RightManagementTableView *tableView;
@property (strong, nonatomic) SHMemberManager *manager;

@end

@implementation SHMemberManagementVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self doInitSubViews];
    [self doRegisterKVO];
    [self doLoadData];
    [self doAddAction];
}

- (void)doInitSubViews
{
    [self setTitleViewText:@"子账号管理"];
    [self doSetRightBtn];
    [self.view addSubview:self.tableView];
    [self doAddTableViewConstraints];
    self.tabBarController.tabBar.hidden = !self.tabBarController.tabBar.hidden;
    [self setNavigationBarLeftBarButtonWithTitle:@"取消" action:@selector(leftAction:)];
}

- (void)leftAction:(UIButton *)sender
{
    self.tabBarController.tabBar.hidden = !self.tabBarController.tabBar.hidden;
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)doLoadData
{
    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
    [self.manager doGetMemberListFromDB];
    [self.manager doGetMemberListFromNetwork];
}

- (void)doRegisterKVO
{
    @weakify(self);
    [self observeKeyPath:@keypath(self.manager.arrMemberList)
                   block:^(id value) {
                       @strongify(self);
                       [XWHUDManager hideInWindow];
                       NSArray *arrGatewayList = (NSArray *)value;
                       if (arrGatewayList.count) {
                           self.tableView.arrList = [self doGetAllMemberExpectMaster:arrGatewayList];
                       }
                   }];
    
    [self observeKeyPath:@keypath(self.manager.errorInfo)
                   block:^(id value) {
                       [XWHUDManager hideInWindow];
                       NSDictionary *dict = (NSDictionary *)value;
                       [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",dict[@"message"]]];
                       
                   }];
}

#pragma mark -
#pragma mark - add action
- (void)doAddAction
{
    @weakify(self)
    [self.tableView setBlockTabelViewCellPressed:^(NSIndexPath *indexPath) {
        @strongify(self);
        SHMemberModel *model = self.tableView.arrList[indexPath.row];
        [self performSegueWithIdentifier:@"seg_to_SHMemberRightListVC" sender:model];
        
    }];
    
    [self.tableView setBlockTabelViewCellDelete:^(NSIndexPath *indexPath) {
        @strongify(self);
        [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
        SHMemberModel *model = self.tableView.arrList[indexPath.row];
        
        [self.manager handleTheDeleteMemberListDataWithMemberID:model.iMember_id completeHandle:^(BOOL success, id result) {
            [XWHUDManager hideInWindow];
            if (success) {
                
                [XWHUDManager showSuccessTipHUD:@"删除成功"];
                [self doLoadData];
            }else{
                [XWHUDManager showErrorTipHUD:@"删除失败"];
            }
            
        }];
    }];
}

#pragma mark -
#pragma mark - private 
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
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"添加子账号" message:nil preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    
    @weakify(self);
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        @strongify(self);
        UITextField *textFiled = alertController.textFields.firstObject;
        
        [self.manager handleTheAddMemberListDataWithModel:textFiled.text
                                           completeHandle:^(BOOL success, id result)
        {
            if (success) {
                [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
                [self.manager doGetMemberListFromNetwork];
            }else{
                
                NSString *strErro = (NSString *)result;                
                [XWHUDManager showErrorTipHUD:strErro];
            }
        }];
    }];
    
    [alertController addTextFieldWithConfigurationHandler:^(UITextField *textField){
        textField.placeholder = @"输入子账号电话号码";
    }];
    
    [alertController addAction:cancelAction];
    [alertController addAction:okAction];
    [self presentViewController:alertController animated:YES completion:nil];
}

//筛选出成员数据，过滤掉管理员
- (NSArray *)doGetAllMemberExpectMaster:(NSArray *)arrList
{
    NSMutableArray *mutArr = [NSMutableArray new];
    for (int i = 0; i < arrList.count; i ++) {
        
        SHMemberModel *memberModel = arrList[i];
        if (memberModel.iMember_type == 1) {
            
        }else{
            [mutArr addObject:memberModel];
        }
    }
    return mutArr;
}


#pragma mark -
#pragma mark - init

- (SHMemberManager *)manager
{
    if (!_manager) {
        _manager = [SHMemberManager new];
    }
    return _manager;
}

- (RightManagementTableView *)tableView
{
    if (!_tableView) {
        _tableView = [[RightManagementTableView alloc] initWithFrame:CGRectMake(0,
                                                                                0,
                                                                                UI_SCREEN_WIDTH,
                                                                                UI_SCREEN_HEIGHT)
                                                               style:UITableViewStylePlain];
    }
    return _tableView;
}

- (void)doAddTableViewConstraints
{
    @weakify(self);
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.top.equalTo(self.view.mas_top);
        make.left.equalTo(self.view.mas_left);
        make.right.equalTo(self.view.mas_right);
        make.bottom.equalTo(self.view.mas_bottom);
    }];
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
    if ([segue.identifier isEqualToString:@"seg_to_SHMemberRightListVC"]) {
        
        SHMemberModel *model = (SHMemberModel *)sender;
        
        SHMemberRightListVC *listVC = (SHMemberRightListVC *)segue.destinationViewController;
        listVC.iMemberID = model.iMember_id;
    }
}

@end
