//
//  FloorManagementVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "FloorManagementVC.h"
#import "FloorTableView.h"
#import "SHFloorManager.h"
#import "RoomManagementVC.h"

typedef NS_ENUM(NSInteger, ALterEditType)
{
    ALterEditTypeEdit  = 700,    //编辑
    ALterEditTypeAdd   = 701,    //删除
};


@interface FloorManagementVC ()<UINavigationControllerDelegate>

@property (strong, nonatomic) FloorTableView *tableView;
@property (strong, nonatomic) SHFloorManager *manager;

@end

@implementation FloorManagementVC

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
    [self doSetRightBtn];
    [self doRegisterKVO];
    [self doLoadData];
    [self doRegisterBlock];
    // Do any additional setup after loading the view.
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navigationController.delegate = self;
}

#pragma mark -- 设置导航栏右边历史账单按钮
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
    [self doxxxxxxxxWithAlterType:ALterEditTypeAdd modelTemp:nil];
}

- (void)doInitSubViews
{
    [self setTitleViewText:@"楼层管理"];
    self.tabBarController.tabBar.hidden = !self.tabBarController.tabBar.hidden;
    [self setNavigationBarLeftBarButtonWithTitle:@"返回" action:@selector(leftAction:)];
    [self.view addSubview:self.tableView];
    [self doAddTableViewConstraints];
}

- (void)leftAction:(UIButton *)sender
{
    self.tabBarController.tabBar.hidden = !self.tabBarController.tabBar.hidden;
    [self.navigationController popViewControllerAnimated:YES];
}


- (void)doLoadData
{
    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
    [self.manager doGetFloorListDataFromDB];
    [self.manager doGetFloorListFromNetwork];
}

- (void)doRegisterKVO
{
    @weakify(self);
    [self observeKeyPath:@keypath(self.manager.arrFloor)
                   block:^(id value) {
                       @strongify(self);
                       [XWHUDManager hideInWindow];
                       NSArray *arrFloor = (NSArray *)value;
                       self.tableView.arrList = [NSMutableArray arrayWithArray:arrFloor];
//                       if (arrFloor.count) {
//                           self.tableView.arrList = [NSMutableArray arrayWithArray:arrFloor];
//                       }
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
- (void)doRegisterBlock
{
    @weakify(self);
    [self.tableView setBlockTabelViewCellPressed:^(NSIndexPath *indexpath) {
        @strongify(self);
        //跳转到房间页面
        [self performSegueWithIdentifier:@"seg_to_RoomManagementVC" sender:self.tableView.arrList[indexpath.row]];
        
    }];
    
    [self.tableView setBlockTabelViewCellEdit:^(FloorTableViewEditType type,NSIndexPath *indexPath) {
        @strongify(self);
        if (type == FloorTableViewEditTypeEdit) {
            //编辑
            SHModelFloor *floor = self.tableView.arrList[indexPath.row];
            [self doxxxxxxxxWithAlterType:ALterEditTypeEdit modelTemp:floor];
            
        }else{
        
            [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
            SHModelFloor *floor = self.tableView.arrList[indexPath.row];
            //删除
            [self.manager handleTheDelteFloorDataWithModel:floor completeHandle:^(BOOL success, id result) {
                
                [XWHUDManager hideInWindow];
                if (success) {
                    [self.manager doGetFloorListFromNetwork];
                }else{
                    if ([result isKindOfClass:[NSString class]]) {
                        [XWHUDManager showSuccessTipHUD:[NSString stringWithFormat:@"%@",result]];
                    }else if([result isKindOfClass:[NSString class]] ){
                        NSDictionary *dict = (NSDictionary *)result;
                        [XWHUDManager showSuccessTipHUD:dict[@"message"]];
                    }else{
                    
                        [XWHUDManager showSuccessTipHUD:[NSString stringWithFormat:@"%@",result]];
                    }
                    
                }
            }];
        }
    }];
}

#pragma mark -
#pragma mark - init
- (FloorTableView *)tableView
{
    if (!_tableView) {
        _tableView = [[FloorTableView alloc] initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, UI_SCREEN_HEIGHT) style:UITableViewStylePlain];
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

-(SHFloorManager *)manager
{
    if (!_manager) {
        _manager = [SHFloorManager new];
    }
    return _manager;
}


- (void)doxxxxxxxxWithAlterType:(ALterEditType)type modelTemp:(SHModelFloor *)modelFloor
{
    if (self.tableView.arrList.count >=1) {
        [UIAlertView bk_showAlertViewWithTitle:@"提示"
                                       message:@"楼层只允许添加一层"
                             cancelButtonTitle:@"确定"
                             otherButtonTitles:nil
                                       handler:^(UIAlertView *alertView, NSInteger buttonIndex) {
            
        }];
        return;
    }
    
    NSString *strAlterName;
    if (type == ALterEditTypeEdit) {
        strAlterName = @"修改楼层名称";
    }else{
        strAlterName = @"添加楼层";
    }
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:strAlterName message:nil preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    
    @weakify(self);
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        @strongify(self);
        
        if (type == ALterEditTypeEdit) {
            UITextField *textFiled = alertController.textFields.firstObject;
            [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
            [self.manager handleTheUpdateFloorDataWithModel:modelFloor floorNewName:textFiled.text completeHandle:^(BOOL success, id result) {
                if (success) {
                    [self.manager doGetFloorListFromNetwork];
                }else{
                    NSDictionary *dict = (NSDictionary *)result;
                     [XWHUDManager showErrorTipHUD:dict[@"message"]];
                }
            }];
        }else{
            SHModelFloor *model = [SHModelFloor new];
            UITextField *textFiled = alertController.textFields.firstObject;
            model.strFloor_name = textFiled.text;
            model.strFloor_image = @"111";
            [self.manager handleTheAddFloorDataWithModel:model completeHandle:^(BOOL success, id result) {
                if (success) {
                    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
                    [self.manager doGetFloorListFromNetwork];
                }else{
                    
                    NSDictionary *dict = (NSDictionary *)result;
                    [XWHUDManager showErrorTipHUD:dict[@"message"]];
                     [XWHUDManager showErrorTipHUD:@"这是失败"];
                }
            }];
        }
    }];
    [alertController addTextFieldWithConfigurationHandler:^(UITextField *textField){
        textField.placeholder = @"请输入楼层的名字";
    }];
    
    [alertController  addAction:cancelAction];
    [alertController addAction:okAction];
    [self presentViewController:alertController animated:YES completion:nil];
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
    if ([segue.identifier isEqualToString:@"seg_to_RoomManagementVC"]) {
        SHModelFloor *model = (SHModelFloor *)sender;
        RoomManagementVC *vc = segue.destinationViewController;
        vc.iFloorID = model.iFloor_id;
        vc.strFloorName = model.strFloor_name;
        vc.arrFloor = self.tableView.arrList;
        
    }
}


@end
