//
//  SHLockSetPswVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/29.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHLockSetPswVC.h"
#import "SHLockManager.h"
#import "LockViewController.h"

@interface SHLockSetPswVC ()<UITextFieldDelegate,UINavigationControllerDelegate>
@property (weak, nonatomic) IBOutlet UIImageView *imageVLineOne;
@property (weak, nonatomic) IBOutlet UIImageView *imageVLineTwo;
@property (weak, nonatomic) IBOutlet UIImageView *imageVLineThree;

@property (weak, nonatomic) IBOutlet UITextField *textFiledPsw;
@property (weak, nonatomic) IBOutlet UITextField *textFieldConfirm;

@property (weak, nonatomic) IBOutlet UIButton *btnNext;

@property (strong, nonatomic) SHLockManager *manager;

@end

@implementation SHLockSetPswVC


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
    [self doInitSubViews];
    // Do any additional setup after loading the view.
}

- (void)doInitSubViews
{
//    [self setTitleViewText:@"设置指纹锁密码"];
    
    self.title = @"设置指纹锁密码";
    self.textFiledPsw.delegate = self;
    self.textFieldConfirm.delegate = self;
    
    self.imageVLineOne.backgroundColor = kLineColor;
    self.imageVLineTwo.backgroundColor = kLineColor;
    self.imageVLineThree.backgroundColor = kLineColor;
    
    self.btnNext.layer.cornerRadius = 6;
    [self.btnNext setTitle:@"下一步" forState:UIControlStateNormal];
    [self.btnNext setTitleColor:kBackgroundWhiteColor forState:UIControlStateNormal];
    self.btnNext.backgroundColor = kCommonColor;
}

- (IBAction)btnNextPressed:(UIButton *)sender {
    if ([self shouldSetNewPsw]) {
        [self.manager handleTheAddLockPswDataWithDeviceID:self.iDeviceID
                                                      psw:self.textFiledPsw.text
                                           completeHandle:^(BOOL success, id result)
         {
             if (success) {
                 
                 [self performSegueWithIdentifier:@"seg_to_LockViewController" sender:nil];
             }else{
                 
                  [XWHUDManager showErrorTipHUD:@"添加密码失败"];
             }
         }];
    }
    
}

- (BOOL)shouldSetNewPsw
{
    if (!self.textFiledPsw.text.length){
        
        [XWHUDManager showWarningTipHUD:@"请输入你的新密码"];
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            [XWHUDManager hideInWindow];
        });
        return NO;
        
    }else if (!self.textFieldConfirm.text.length){
        
        [XWHUDManager showWarningTipHUD:@"请再次输入你的密码"];
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            [XWHUDManager hideInWindow];
        });
        return NO;
        
    }else if (self.textFieldConfirm.text.length  > 6 ){
        
        [XWHUDManager showWarningTipHUD:@"输入密码不能超过6位数字"];
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            [XWHUDManager hideInWindow];
        });
        return NO;
        
    }else if (self.textFiledPsw.text.length  > 6 ){
        
        [XWHUDManager showWarningTipHUD:@"输入密码不能超过6位数字"];
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            [XWHUDManager hideInWindow];
        });
        return NO;
        
    }else{
        
        return YES;
    }
}



- (SHLockManager *)manager
{
    if (!_manager) {
        _manager = [SHLockManager new];
    }
    return  _manager;
}


- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self.textFieldConfirm resignFirstResponder];
    [self.textFiledPsw resignFirstResponder];
    return  YES;
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
    if ([segue.identifier isEqualToString:@"seg_to_LockViewController"]) {
        LockViewController *vc = segue.destinationViewController;
        vc.iDeviceID = self.iDeviceID;
        vc.strMacAddr = self.strMacAddr;
        vc.strPsw = self.textFiledPsw.text;
        vc.itype = self.itype;
        vc.deviceTransmit = self.deviceTransmit;
    }
}

@end


