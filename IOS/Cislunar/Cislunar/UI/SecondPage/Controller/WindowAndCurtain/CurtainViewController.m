//
//  WindowViewController.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/13.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "CurtainViewController.h"

@interface CurtainViewController ()

@property (weak, nonatomic) IBOutlet UIButton *btnBack;
@property (weak, nonatomic) IBOutlet UILabel *labelTitle;
@property (weak, nonatomic) IBOutlet UIImageView *iamgeLogo;
@property (weak, nonatomic) IBOutlet UIButton *btnSwitch;

@end

@implementation CurtainViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - 
#pragma mark - action

- (IBAction)btnBackPressed:(UIButton *)sender {
    [self.navigationController popViewControllerAnimated:YES];
}


- (IBAction)btnSwichPress:(UIButton *)sender {
    
    
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
