//
//  RegisterVC.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/15.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "RegisterVC.h"
#import "UIButton+Code.h"
#import "RegisterManager.h"
#import "ForgetPasswordManager.h"

@interface RegisterVC ()

@property (weak, nonatomic) IBOutlet UITextField *tfTelNumber;
@property (weak, nonatomic) IBOutlet UITextField *tfCode;
@property (weak, nonatomic) IBOutlet UITextField *tfPsw;
@property (weak, nonatomic) IBOutlet UIButton *btnCode;
@property (weak, nonatomic) IBOutlet UIButton *btnRegister;
@property (strong, nonatomic) ForgetPasswordManager *manager;



@end

@implementation RegisterVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self doInitSubViews];
    // Do any additional setup after loading the view.
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    [self.btnCode cancelCountdown];
}


#pragma mark -
#pragma mark -UI load

- (void)doInitSubViews
{
    switch (self.vcType) {
        case registerType:
        {
            [self setTitleViewText:@"注册"];
            [self.btnRegister setTitle:@"注册" forState:UIControlStateNormal];
            
        }
            break;
        case forgetPswType:
        {
            [self setTitleViewText:@"重置密码"];
            [self.btnRegister setTitle:@"确定" forState:UIControlStateNormal];
        }
            break;
            
        default:
            break;
    }
    
    self.btnRegister.layer.cornerRadius = 8;
    self.btnRegister.layer.masksToBounds = YES;
    
    [self setNavigationBarBackButtonWithTitle:@"" action:@selector(leftAction:)];
    
    
}

#pragma mark -
#pragma mark - action

- (IBAction)btnCodePressed:(UIButton *)sender {
    
    [sender setCountdown:60];
    
    if (self.vcType == registerType) {
        [self handleRegisterGetCode];
    }else{
        [self handleForgetGetCode];
    }
}

- (void)leftAction:(UIButton *)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)btnProtocl:(UIButton *)sender {
    
    [self performSegueWithIdentifier:@"SEG_TO_PrivateProtocolController" sender:nil];
}

- (IBAction)btnRegisterPressed:(UIButton *)sender {
    if ([self shouldRegister]) {
        
         [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
        if (self.vcType == registerType) {
            @weakify(self);
            [RegisterManager handleTheRegisterDataWithUserName:self.tfTelNumber.text
                                                      password:self.tfPsw.text
                                                        mobile:@"三次元"
                                              verificationCode:self.tfCode.text
                                                callBackHandle:^(BOOL success, id result){
                                                    [XWHUDManager hideInWindow];
                                                    @strongify(self);
                                                    if (!success){
                                                        [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",result]];
                                                    }else{
                                                        [XWHUDManager showSuccessTipHUD:@"注册成功"];
                                                        //登录
                                                        [self.navigationController popViewControllerAnimated:YES];
                                                    }
                                                }];
            
        }else{
            
            @weakify(self);
            [self.manager handleRequestResetPswCodeWithTelNum:self.tfTelNumber.text
                                                         code:self.tfCode.text
                                                       newPsw:self.tfPsw.text
                                                     complete:^(BOOL success, id result)
             {
                 [XWHUDManager hideInWindow];
                 @strongify(self);
                 if (!success){
                     [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",result]];
                 }else{
                     [XWHUDManager showSuccessTipHUD:@"密码重置成功"];
                     [self.navigationController popViewControllerAnimated:YES];
                 }
             }];
        }
        
        
    }
    
}
#pragma mark -
#pragma mark -  获取验证码
- (void)handleRegisterGetCode{
    //获取验证码请求
    [XWHUDManager showHUDMessage:@"验证码中..." afterDelay:60];
    [RegisterManager handleTheGetMobileRegisterTokenReqWithTelNum:self.tfTelNumber.text
                                   mobileRegisterTokenReqCallBack:^(BOOL success, id result) {
                                       [XWHUDManager hideInWindow];
                                       if (success) {
                                           [XWHUDManager showSuccessTipHUD:@"验证码发送成功"];
                                       }else{
                                           
                                           [XWHUDManager showSuccessTipHUD:[NSString stringWithFormat:@"%@",result]];
                                           [self.btnCode cancelCountdown];
                                       }
                                   }];
}

- (void)handleForgetGetCode
{
    [XWHUDManager showHUDMessage:@"验证码中..." afterDelay:60];
    [self.manager handleRequestGetResetPswCode:self.tfTelNumber.text
                                      complete:^(BOOL success, id result)
     {
         [XWHUDManager hideInWindow];
         if (success) {
             [XWHUDManager showSuccessTipHUD:@"验证码发送成功"];
         }else{
             [XWHUDManager showSuccessTipHUD:[NSString stringWithFormat:@"%@",result]];
             [self.btnCode cancelCountdown];
         }
     }];
}

#pragma mark - private method

- (BOOL)shouldRegister
{
    if (!self.tfTelNumber.text.length){
        [XWHUDManager showWarningTipHUD:@"请输入您的电话号码！"];
        return NO;
        
    }else if (!self.tfPsw.text.length){
        
        [XWHUDManager showWarningTipHUD:@"密码不能为空！"];
        return NO;
        
    }else if (!self.tfCode.text.length){
        
        [XWHUDManager showWarningTipHUD:@"请输入验证码！"];
        return NO;
        
    }else if (![ToolCommon isAllNumber:self.tfTelNumber.text] || self.tfTelNumber.text.length != 11){
        
        [XWHUDManager showWarningTipHUD:@"您输入的电话号码不正确，请重新输入"];
        return NO;
        
    }else if (self.tfPsw.text.length <6 || self.tfPsw.text.length > 16){
        
        [XWHUDManager showWarningTipHUD:@"您输入的密码不正确，请重新输入"];
        return NO;
        
    }else{
        
        return YES;
    }
    return YES;
}

#pragma mark -
#pragma mark - 懒加载
- (ForgetPasswordManager *)manager {
    if (!_manager) {
        _manager = [[ForgetPasswordManager alloc]init];
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
