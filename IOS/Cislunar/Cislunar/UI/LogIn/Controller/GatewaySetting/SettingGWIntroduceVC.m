//
//  SettingGWIntroduceVC.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/15.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "SettingGWIntroduceVC.h"
#import "SettingGWWiFiVC.h"

@interface SettingGWIntroduceVC ()
@property (weak, nonatomic) IBOutlet UIButton *btnNext;
@property (weak, nonatomic) IBOutlet UIImageView *imageViewGWInt;

@end

@implementation SettingGWIntroduceVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self doInitSubViews];
    // Do any additional setup after loading the view.
}

- (void)doInitSubViews
{
    
    [self setTitleViewText:@"语音网关"];
    self.btnNext.layer.cornerRadius = 20;
    self.btnNext.layer.masksToBounds = YES;
    [self setNavigationBarBackButtonWithTitle:@"" action:@selector(leftAction:)];
    

}

#pragma mark -
#pragma mark - action

- (void)leftAction:(UIButton *)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)btnNextPressed:(UIButton *)sender {
    
    [self performSegueWithIdentifier:@"SEG_TO_SettingGWWiFiVC" sender:nil];
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
    if ([segue.identifier isEqualToString:@"SEG_TO_SettingGWWiFiVC"]) {
        SettingGWWiFiVC *setWifiVC = (SettingGWWiFiVC *)segue.destinationViewController;
        setWifiVC.arrGWLists = self.arrGWLists;
        setWifiVC.strFromIdentifer = @"fromSwitch";
    }
}


@end
