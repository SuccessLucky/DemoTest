//
//  SHControlBoxVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/3.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHControlBoxVC.h"

@interface SHControlBoxVC ()<UINavigationControllerDelegate>

@property (weak, nonatomic) IBOutlet UIButton *btnBack;
@property (weak, nonatomic) IBOutlet UILabel *labelMainTitle;
@property (weak, nonatomic) IBOutlet UIButton *btnOn;
@property (weak, nonatomic) IBOutlet UIButton *btnOFF;
@property (weak, nonatomic) IBOutlet UIButton *btnPause;

@end

@implementation SHControlBoxVC

- (IBAction)btnBackPressed:(UIButton *)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    if (self.itype == 0) {
        self.tabBarController.tabBar.hidden = YES;
    }
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    if (self.itype == 0) {
        self.tabBarController.tabBar.hidden = NO;
    }
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.labelMainTitle.text = self.device.strDevice_device_name;
    [self.btnBack setEnlargeEdgeWithTop:20 right:20 bottom:20 left:20];
}

- (IBAction)btnOnPressed:(UIButton *)sender {
    NSData *data = [[NetworkEngine shareInstance] doGetSwitchControlWithTargetAddr:self.device.strDevice_mac_address
                                                                              device:self.device
                                                                                 way:[self.device.strDevice_other_status intValue]
                                                                         controlMode:@"01"
                                                                        controlState:@"01"];
    NSLog(@"控制盒进行控制页面:%@",data);
    [[NetworkEngine shareInstance] sendRequestData:data];
}

- (IBAction)btnOFFPressed:(UIButton *)sender {
    
    NSData *data = [[NetworkEngine shareInstance] doGetSwitchControlWithTargetAddr:self.device.strDevice_mac_address
                                                                              device:self.device
                                                                                 way:[self.device.strDevice_other_status intValue]
                                                                         controlMode:@"01"
                                                                        controlState:@"02"];
    NSLog(@"控制盒进行控制页面:%@",data);
    [[NetworkEngine shareInstance] sendRequestData:data];
    
}

#pragma mark -
#pragma mark - action

- (IBAction)btnPausePressed:(UIButton *)sender {
    NSData *data = [[NetworkEngine shareInstance] doGetSwitchControlWithTargetAddr:self.device.strDevice_mac_address
                                                                              device:self.device
                                                                                 way:[self.device.strDevice_other_status intValue]
                                                                         controlMode:@"01"
                                                                        controlState:@"04"];
    NSLog(@"控制盒进行控制页面:%@",data);
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
