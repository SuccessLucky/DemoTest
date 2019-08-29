//
//  YCTBaseViewController+Style.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/15.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "YCTBaseViewController.h"
#import "GNavigationTitleView.h"

typedef NS_ENUM(NSInteger, GNetworkActifityStatus) {
    /**
     *  未连接
     */
    GNetworkActifityStatusNormal = -1,
    /**
     *  连接成功
     */
    GNetworkActifityStatusSuccess = 0,
    /**
     *  连接中
     */
    GNetworkActifityStatusConnecting = 1,
    /**
     *  连接失败
     */
    GNetworkActifityStatusFailed = 2
};

@interface YCTBaseViewController (Style)

@property (nonatomic, strong) GNavigationTitleView *titleView;

- (void)initUIStyle;

/**
 *  设置导航栏标题
 */
- (void)setTitleViewText:(NSString *)title;
/**
 *  设置导航栏副标题
 */
- (void)setTitleViewPrompt:(NSString *)prompt;
/**
 *  设置导航栏的网络状态显示
 */
- (void)setNetworkStatus:(GNetworkActifityStatus)networkStatus;

/**
 *  设置导航栏右侧按钮
 */
- (void)setNavigationBarRightBarButtonWithTitle:(NSString *)title
                                         action:(SEL)selector;
/*
 * 设置导航栏左侧按钮
 */
- (void)setNavigationBarLeftBarButtonWithTitle:(NSString *)title
                                        action:(SEL)selector;

/**
 *  设置导航栏右侧按钮
 */
- (void)setNavigationBarBackButtonWithTitle:(NSString *)title
                                     action:(SEL)selector;

/**
 *  设置导航栏左侧自定义视图
 */
- (void)setNavigationBarLeftBarButtonWithCustomView:(UIView *)view;

/**
 *  设置导航栏右侧自定义视图
 */
- (void)setNavigationBarRightBarButtonWithCustomView:(UIView *)view;

/**
 *  移除右侧导航栏按钮
 */
- (void)removeRightNavigationBarButton;

/**
 *  移除左侧导航栏按钮
 */
- (void)removeLeftNavigationBarButton;

@end
