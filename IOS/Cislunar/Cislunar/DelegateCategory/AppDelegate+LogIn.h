//
//  AppDelegate+LogIn.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/14.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "AppDelegate.h"

@interface AppDelegate (LogIn)

//切换到登录页面
- (void)didEnterLoginViewController;

//切换到主tabbar
- (void)didEnterMainTabBarController;

//退出登录
- (void)doLogoutClearAll;

//登陆成功后的处理逻辑
- (void)didLoginSuccAfterAction;

@end
