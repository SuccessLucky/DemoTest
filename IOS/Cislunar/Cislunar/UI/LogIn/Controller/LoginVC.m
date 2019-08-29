//
//  LoginVC.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/14.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "LoginVC.h"
#import "GatewayListLocalVC.h"
#import "RegisterVC.h"
#import "EasyTextView.h"
#import "SHLoginManager.h"
#import "GatewayListNetVC.h"

@interface LoginVC ()
@property (weak, nonatomic) IBOutlet UITextField *tfUserName;
@property (weak, nonatomic) IBOutlet UITextField *tfPsw;
@property (weak, nonatomic) IBOutlet UIButton *btnLogin;
@property (weak, nonatomic) IBOutlet UIButton *btnRegister;
@property (weak, nonatomic) IBOutlet UIButton *btnForgetPsw;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *line1With;

@end

@implementation LoginVC

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self doJudgeNetwork];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self doInitSubViews];
    // Do any additional setup after loading the view.
}

#pragma mark -
#pragma mark - 加载UI
- (void)doInitSubViews
{
    self.isHideNaviBar = YES;
    self.line1With.constant = 0.5;
    self.btnLogin.layer.cornerRadius = 20;
    self.btnLogin.layer.masksToBounds = YES;
    [self changeClearButtonPhoto];
    
    self.tfUserName.text = [[SHLoginManager shareInstance] doGetRememberAccount];
    
}

- (void)changeClearButtonPhoto{
    //清除userID按钮
    UIButton *clearButton = [self.tfUserName valueForKey:@"_clearButton"];
    [clearButton setImage:[UIImage imageNamed:@"login_img_delete_pr.png"] forState:UIControlStateNormal];
    [clearButton setImage:[UIImage imageNamed:@"login_img_delete_un.png"] forState:UIControlStateHighlighted];
    
    //清除userPwd按钮
    UIButton *clearUserPwd = [self.tfPsw valueForKey:@"_clearButton"];
    [clearUserPwd setImage:[UIImage imageNamed:@"login_img_delete_pr.png"] forState:UIControlStateNormal];
    [clearUserPwd setImage:[UIImage imageNamed:@"login_img_delete_un.png"] forState:UIControlStateHighlighted];
}

#pragma mark -
#pragma mark -是否满足登录的条件一 最基本的
- (BOOL)shoulogin
{
    if ([self.tfUserName.text isEqualToString:@""])
    {
        [[YCTShowView shareInstance] showMessagDefult:@"亲,请输入用户名"];

        return NO;
    }
    else if (self.tfUserName.text.length <11)
    {
        [[YCTShowView shareInstance] showMessagDefult:@"您输入的手机号码格式不正确"];
        return NO;
    }
    else if ([self.tfPsw.text isEqualToString:@""])
    {
        [[YCTShowView shareInstance] showMessagDefult:@"亲,请输入密码"];
        return NO;
    }
    else if (self.tfPsw.text.length <6)
    {
         [[YCTShowView shareInstance] showMessagDefult:@"亲,密码长度至少六位"];
        return NO;
    }else if (!self.tfUserName.text.length){
        
        [[YCTShowView shareInstance] showMessagDefult:@"请输入你的用户名"];
        return NO;
        
    }else if (!self.tfPsw.text.length){
        
         [[YCTShowView shareInstance] showMessagDefult:@"请输入你的密码"];
        return NO;
        
    }else{
        
        return YES;
    }
}

#pragma mark -
#pragma mark - action

- (IBAction)btnLogInPressed:(UIButton *)sender {
    
//    [self performSegueWithIdentifier:@"SEG_TO_GatewayListLocalVC" sender:self];
    if ([self shoulogin]) {
        [XWHUDManager showHUDMessage:@"登录中..." afterDelay:20];
        @weakify(self);
        [[SHLoginManager shareInstance] handleTheLoginDataWithUserName:self.tfUserName.text
                                                              password:self.tfPsw.text
                                                        callBackHandle:^(BOOL success, id result) {
                                                            @strongify(self);
                                                            if (!success) {
                                                                [XWHUDManager hideInWindow];
                                                                [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",result]];
                                                            }else{
                                                                [XWHUDManager hideInWindow];
                                                                NSArray *gateWayList = (NSArray *)result;
                                                                if (gateWayList.count == 0) {
                                                                    //需要进入到配置网关模式
                                                                    [self performSegueWithIdentifier:@"SEG_TO_GatewayListLocalVC" sender:self];
                                                                    
                                                                }else{
                                                                    [self performSegueWithIdentifier:@"SEG_TO_GatewayListNetVC" sender:gateWayList];
                                                                }
                                                            }
                                                        }];
    }
    
    
}

- (IBAction)btnRegisterPressed:(UIButton *)sender {
    
    [self performSegueWithIdentifier:@"SEG_TO_RegisterVC" sender:@(1)];
}

- (IBAction)btnForgetPswPressed:(UIButton *)sender {
    [self performSegueWithIdentifier:@"SEG_TO_RegisterVC" sender:@(2)];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - segue delegateMethod
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    
    if ([segue.identifier isEqualToString:@"SEG_TO_GatewayListLocalVC"]) {
//        GatewayListLocalVC *registerNacC = (GatewayListLocalVC *)segue.destinationViewController;
        
    }else if ([segue.identifier isEqualToString:@"SEG_TO_RegisterVC"]){
        
    RegisterVC *vc =  (RegisterVC *)segue.destinationViewController;
        if ([sender intValue] == 1) {
            vc.vcType = registerType;
        }else{
            vc.vcType = forgetPswType;
        }

    }else if ([segue.identifier isEqualToString:@"SEG_TO_GatewayListNetVC"]){
        GatewayListNetVC *vc = (GatewayListNetVC *)segue.destinationViewController;
        vc.gatewayList = (NSArray *)sender;
        
    }
}

#pragma mark -
#pragma mark -logic1
- (void)doJudgeNetwork
{
    if ([ToolCommon isNotReachable]) {
        [[YCTShowView shareInstance] doShowCustomAlterViewWithTitle:@"温馨提示"
                                                            content:@"目前网络不可用，请连接网络"
                                                   strConfirmButton:@"确定"
                                                   strCancellButton:@""
                                                     callBackHandle:^(long index)
        {
            if (index == 0) {
                
            }
        }];
    }
}

@end
