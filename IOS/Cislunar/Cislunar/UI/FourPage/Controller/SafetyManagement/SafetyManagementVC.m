//
//  SafetyManagementVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SafetyManagementVC.h"
#import "SHSafetyManager.h"

@interface SafetyManagementVC ()

@property (strong, nonatomic) SHSafetyManager *manager;

@end

@implementation SafetyManagementVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"安防管理";
}

#pragma mark -- 点击编辑进行添加
- (void)doSetRightBtn {
    UIButton *rightBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [rightBtn setFrame:CGRectMake(80, 10, 70, 25)];
    rightBtn.titleLabel.font = [UIFont systemFontOfSize:16];
    [rightBtn setTitleEdgeInsets:UIEdgeInsetsMake(0, 0, 0, -15)];
    [rightBtn addTarget:self action:@selector(rightAction:) forControlEvents:UIControlEventTouchUpInside];
    [rightBtn setTitle:@"编辑" forState:UIControlStateNormal];
    [rightBtn setTag:12];
    UIBarButtonItem *rightBtnItem = [[UIBarButtonItem alloc]initWithCustomView:rightBtn];
    self.navigationItem.rightBarButtonItem = rightBtnItem;
}

- (void)rightAction:(UIButton *)btn
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"添加楼层" message:nil preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    
    @weakify(self);
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        @strongify(self);
        SHModelFloor *model = [SHModelFloor new];
        UITextField *textFiled = alertController.textFields.firstObject;
        model.strFloor_name = textFiled.text;
        model.strFloor_image = @"111";
//        [self.manager handleTheAddFloorDataWithModel:model completeHandle:^(BOOL success, id result) {
//            if (success) {
//                [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
//                [self.manager doGetFloorListFromNetwork];
//            }else{
//                
//                NSDictionary *dict = (NSDictionary *)result;
//                [[GAlertMessageManager shareInstance] showMessag:dict[@"message"]];
//            }
//        }];
        
        
    }];
    [alertController addTextFieldWithConfigurationHandler:^(UITextField *textField){
        textField.placeholder = @"请输入楼层的名字";
    }];
    
    [alertController  addAction:cancelAction];
    [alertController addAction:okAction];
    [self presentViewController:alertController animated:YES completion:nil];
}

- (SHSafetyManager *)manager
{
    if (!_manager) {
        _manager = [SHSafetyManager new];
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
