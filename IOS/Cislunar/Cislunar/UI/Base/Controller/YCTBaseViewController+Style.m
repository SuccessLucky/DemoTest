//
//  YCTBaseViewController+Style.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/15.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "YCTBaseViewController+Style.h"

#define UIViewControllerStylePropertyTitleView          @"style_titleView"

@implementation YCTBaseViewController (Style)

- (void)initUIStyle
{
//    self.view.backgroundColor = UIColorFromRGB(0x313c83);
    if (self.title) {
        [self setTitleViewText:self.title];
    }
}

- (void)setTitleViewText:(NSString *)title
{
    self.titleView.text = title;
}

- (void)setTitleViewPrompt:(NSString *)prompt
{
    self.titleView.prompt = prompt;
}

- (void)setNetworkStatus:(GNetworkActifityStatus)networkStatus
{
    NSString *title;
    switch (networkStatus) {
        case GNetworkActifityStatusNormal:
            title = self.title;
            break;
        case GNetworkActifityStatusConnecting:
            title = @"连接中...";
            break;
        case GNetworkActifityStatusSuccess:
            title = self.title;
            break;
        case GNetworkActifityStatusFailed:
            title = [NSString stringWithFormat:@"%@(未连接)", self.title];
            break;
    }
    [self setTitleViewText:title];
}

- (GNavigationTitleView *)titleView
{
    GNavigationTitleView *titleView = [self bk_associatedValueForKey:UIViewControllerStylePropertyTitleView];
    if (!titleView) {
        titleView = [[GNavigationTitleView alloc] initWithFrame:CGRectMake(0, 0, 140, 44)];
        if (self.navigationItem) {
            self.navigationItem.titleView = titleView;
        }
        titleView.backgroundColor = [UIColor clearColor];
        [self setTitleView:titleView];
    }
    return titleView;
}

- (void)setTitleView:(GNavigationTitleView *)titleView
{
    [self bk_associateValue:titleView withKey:UIViewControllerStylePropertyTitleView];
}

- (void)setNavigationBarRightBarButtonWithTitle:(NSString *)title
                                         action:(SEL)selector
{
    UIBarButtonItem *barButton = [[UIBarButtonItem alloc] initWithTitle:title
                                                                  style:UIBarButtonItemStylePlain
                                                                 target:self
                                                                 action:selector];
    barButton.tintColor = [UIColor whiteColor];
    self.navigationItem.rightBarButtonItem = barButton;
}
- (void)setNavigationBarLeftBarButtonWithTitle:(NSString *)title
                                        action:(SEL)selector
{
    UIBarButtonItem *barButton = [[UIBarButtonItem alloc] initWithTitle:title
                                                                  style:UIBarButtonItemStylePlain
                                                                 target:self
                                                                 action:selector];
    barButton.tintColor = [UIColor whiteColor];
    self.navigationItem.leftBarButtonItem = barButton;
}


- (void)setNavigationBarBackButtonWithTitle:(NSString *)title action:(SEL)selector
{
    UIButton *backButton = [UIButton buttonWithType:UIButtonTypeCustom];
    backButton.frame = CGRectMake(0, 0, 50, 30);
    [backButton setTitle:title forState:UIControlStateNormal];
    backButton.titleLabel.font = [UIFont systemFontOfSize:17];
    
    [backButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [backButton setTitleColor:RGB(253, 183, 165) forState:UIControlStateHighlighted];
    [backButton setImage:[UIImage imageNamed:@"all_icon_back_un"] forState:UIControlStateNormal];
    [backButton setImage:[UIImage imageNamed:@"all_icon_back_pr"] forState:UIControlStateHighlighted];
    
    backButton.imageEdgeInsets = UIEdgeInsetsMake(0, -11, 0, 0);
    
    [backButton addTarget:self action:selector forControlEvents:UIControlEventTouchUpInside];
    
    UIBarButtonItem *barButton = [[UIBarButtonItem alloc] initWithCustomView:backButton];
    
    UIBarButtonItem *negativeSpacer = [[UIBarButtonItem alloc]
                                       initWithBarButtonSystemItem:UIBarButtonSystemItemFixedSpace
                                       target:nil action:nil];
    negativeSpacer.width = -6;
    [self.navigationItem setLeftBarButtonItems:[NSArray arrayWithObjects:negativeSpacer, barButton, nil] animated:NO];
}

- (void)setNavigationBarLeftBarButtonWithCustomView:(UIView *)view
{
    UIBarButtonItem *barButton = [[UIBarButtonItem alloc] initWithCustomView:view];
    self.navigationItem.leftBarButtonItem = barButton;
}

- (void)setNavigationBarRightBarButtonWithCustomView:(UIView *)view
{
    UIBarButtonItem *barButton = [[UIBarButtonItem alloc] initWithCustomView:view];
    self.navigationItem.rightBarButtonItem = barButton;
}

- (void)removeRightNavigationBarButton
{
    self.navigationItem.rightBarButtonItem = nil;
}

- (void)removeLeftNavigationBarButton
{
    self.navigationItem.leftBarButtonItem = nil;
}

@end
