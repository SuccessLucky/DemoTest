//
//  SettingManagementVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SettingManagementVC.h"
#import "SHColourBulbVC.h"

@interface SettingManagementVC ()<UINavigationControllerDelegate>

@end

@implementation SettingManagementVC

#pragma mark - UINavigationControllerDelegate
- (void)navigationController:(UINavigationController *)navigationController
      willShowViewController:(UIViewController *)viewController
                    animated:(BOOL)animated {
    BOOL isHomePage = [viewController isKindOfClass:[self class]];
    
    [self.navigationController setNavigationBarHidden:!isHomePage animated:YES];
}

- (void)viewDidLoad {
    [super viewDidLoad];
//    self.navigationController.delegate = self;
    // Do any additional setup after loading the view.
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navigationController.delegate = self;
}

- (IBAction)btnClearPressed:(UIButton *)sender {
    
    
    [self doWifiInfo];
    
}

#pragma mark -
#pragma mark - 跳转到彩色灯页面
- (void)doSkipToColourBulb
{
    UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
    SHColourBulbVC *VC = (SHColourBulbVC *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"SHColourBulbVC"];
    [self.navigationController pushViewController:VC animated:YES];
}

#pragma mark -
#pragma mark - 网关心跳基数归零
- (void)doSetGatewayHeartClearToZero
{
    NSData *data = [[NetworkEngine shareInstance] doGetGatewayHeartClearToZero];
    [[NetworkEngine shareInstance] sendRequestData:data];
}


#pragma mark - 暂时不起作用
#pragma mark - 读取WIFI模块基本信息
- (void)doWifiInfo
{
    NSData *data = [[NetworkEngine shareInstance] doGetSetReadGatewayWifiInfoData];
    [[NetworkEngine shareInstance] sendRequestData:data];
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
