//
//  LightViewController.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/13.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "LightViewController.h"

@interface LightViewController ()<UINavigationControllerDelegate>

@property (weak, nonatomic) IBOutlet UIImageView *iamgeViewLight;
@property (weak, nonatomic) IBOutlet UIButton *btnSwitch;
@end

@implementation LightViewController

#pragma mark - UINavigationControllerDelegate
- (void)navigationController:(UINavigationController *)navigationController
      willShowViewController:(UIViewController *)viewController
                    animated:(BOOL)animated {
    BOOL isHomePage = [viewController isKindOfClass:[self class]];
    
    [self.navigationController setNavigationBarHidden:!isHomePage animated:YES];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navigationController.delegate =self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"灯光控制";
    self.navigationController.delegate = self;
}


/*
- (void)viewWillAppear:(BOOL)animated
{
    [self doMakeNavTranslucent];
}
#pragma mark - 视图将要消失
- (void)viewWillDisappear:(BOOL)animated
{
    [self doMakeNavNoTranslucent];
}



#pragma mark - 
#pragma mark - 设置导航栏透明
- (void)doMakeNavTranslucent
{
    [self.navigationController.navigationBar setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
    [self.navigationController.navigationBar setShadowImage:[UIImage new]];
    self.navigationController.navigationBar.translucent = true;
}

- (void)doMakeNavNoTranslucent
{
    [self.navigationController.navigationBar setBackgroundImage:nil forBarMetrics:UIBarMetricsDefault];
    [self.navigationController.navigationBar setShadowImage:nil];
    self.navigationController.navigationBar.translucent = NO;
}
 */


#pragma mark - 
#pragma mark - action

- (IBAction)btnBackPressed:(UIButton *)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

/*
- (IBAction)btnAddPressed:(UIButton *)sender {
    
}
 */

- (IBAction)btnSwitchPressed:(UIButton *)sender {
    
    
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
