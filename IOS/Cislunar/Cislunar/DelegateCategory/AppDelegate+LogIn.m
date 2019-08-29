//
//  AppDelegate+LogIn.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/14.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "AppDelegate+LogIn.h"
#import "LogInNav.h"
#import "YCTTabBarController.h"
#import "AppDelegate+Camera.h"
#import "AppDelegate+GeTui.h"

@implementation AppDelegate (LogIn)

//切换到登录页面
- (void)didEnterLoginViewController
{
    UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"LogIn" bundle:[NSBundle mainBundle]];
    LogInNav *vc = (LogInNav*)[storyBoard instantiateViewControllerWithIdentifier:@"LogInNav"];
    
    typedef void (^Animation)(void);
    vc.modalTransitionStyle = UIModalTransitionStyleCrossDissolve;
    Animation animation = ^{
        BOOL oldState = [UIView areAnimationsEnabled];
        [UIView  setAnimationsEnabled:NO];
        self.window.rootViewController = vc;
        [UIView setAnimationsEnabled:oldState];
    };
    
    [UIView transitionWithView:self.window
                      duration:0.5f
                       options:UIViewAnimationOptionTransitionCrossDissolve
                    animations:animation
                    completion:nil];
    
    self.window.rootViewController = vc;
    [self.window makeKeyAndVisible];
}

#pragma mark - 切换到主tabbar
- (void)didEnterMainTabBarController
{
    UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main"
                                                         bundle:[NSBundle mainBundle]];
    YCTTabBarController *vc = (YCTTabBarController*)[storyBoard instantiateViewControllerWithIdentifier:@"YCTTabBarController"];
    self.window.rootViewController = vc;
    [self.window makeKeyAndVisible];
}

#pragma mark -
#pragma mark -退出登录需要处理的数据

//登陆成功后的处理逻辑
- (void)didLoginSuccAfterAction
{
    NSInteger iLoginState = [[SHLoginManager shareInstance] doGetLoginState];
    if (iLoginState == SHUserLoginState_NeedLogin) {
        
    }else if(iLoginState == SHUserLoginStateLocal_LoginSucc){
        NSString *strClientID = [[SHLoginManager shareInstance] doGetGeTuiClientID];
        [[SHAppCommonRequest shareInstance] handlePostPushWithUUID:strClientID
                                              pushRegisterCallBack:^(BOOL success, id result)
         {}];
        [self didEnterMainTabBarController];
    }else{
        
    }
    
    
}


#pragma mark -
#pragma mark - 退出登录的操作
- (void)doLogoutClearAll
{
    NSInteger iLoginState = [[SHLoginManager shareInstance] doGetLoginState];
    if (iLoginState == SHUserLoginState_NeedLogin) {
        
    }else{
        [[SHLoginManager shareInstance] doLogOutFromNetworkCallBack:^(BOOL success, id result) {
            [XWHUDManager hideInWindow];
            //        if (success) {
            //            [[AppDelegate sharedAppDelegate] didUserLogoutActionWithNoRequest];
            //        }else{
            //            [XWHUDManager showErrorTipHUD:@"登出失败"];
            //
            //        }
            [[AppDelegate sharedAppDelegate] didUserLogoutActionWithNoRequest];
        }];
    }
    
}

- (void)didUserLogoutActionWithNoRequest
{
    //清除本地数据
    [[SHLoginManager shareInstance] doClearAllShouldData];
    //跳转到登录界面
    [[AppDelegate sharedAppDelegate] didEnterLoginViewController];
    
    [[NetworkEngine shareInstance] setSocketConState:GSocketConStateFail];
    [[NetworkEngine shareInstance] doForceDisConnect];
    
    [[SHLoginManager shareInstance] doWriteLoginState:SHUserLoginState_NeedLogin];
    
    //摄像头退出
    [self doLogOutEZOpenSDK];
    //停止
    [self doStopGT];
    
    
    
}


@end
