//
//  YCTBaseViewController+Tips.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/15.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "YCTBaseViewController+Tips.h"

#define UIViewControllerTipsTipsView                @"tips_tipsView"
#define UIViewControllerTipsActionBlock             @"tips_actionBlock"

@implementation YCTBaseViewController (Tips)

- (void)showDefaultViewIn:(UIView *)view title:(NSString *)title
{
    [self.tipsView setTitle:title];
    [self.tipsView setImage:[UIImage imageNamed:@"blank_img_blank"]];
    [view addSubview:self.tipsView];
    [view sendSubviewToBack:self.tipsView];
    [self addTipsViewConstraints];
}
- (void)showDefaultViewInFrame:(CGRect)frame title:(NSString *)title
{
    [self.tipsView setTitle:title];
    [self.tipsView setImage:[UIImage imageNamed:@"blank_img_blank"]];
    [self.view addSubview:self.tipsView];
    [self.view sendSubviewToBack:self.tipsView];
    self.tipsView.frame = frame;
}

- (void)showDefaultViewIn:(UIView *)view frame:(CGRect)frame title:(NSString *)title
{
    [self.tipsView setTitle:title];
    [self.tipsView setImage:[UIImage imageNamed:@"blank_img_blank"]];
    [view addSubview:self.tipsView];
    [view sendSubviewToBack:self.tipsView];
    self.tipsView.frame = frame;
}

- (void)showErrorViewIn:(UIView *)view title:(NSString *)title
{
    [self.tipsView setTitle:title];
    [self.tipsView setImage:[UIImage imageNamed:@"blank_img_failed"]];
    [view addSubview:self.tipsView];
    [view sendSubviewToBack:self.tipsView];
    [self addTipsViewConstraints];
}

- (void)showErrorViewInFrame:(CGRect)frame title:(NSString *)title
{
    [self.tipsView setTitle:title];
    [self.tipsView setImage:[UIImage imageNamed:@"blank_img_failed"]];
    [self.view addSubview:self.tipsView];
    [self.view sendSubviewToBack:self.tipsView];
    self.tipsView.frame = frame;
}

- (void)showErrorViewIn:(UIView *)view frame:(CGRect)frame title:(NSString *)title
{
    [self.tipsView setTitle:title];
    [self.tipsView setImage:[UIImage imageNamed:@"blank_img_failed"]];
    [view addSubview:self.tipsView];
    [view sendSubviewToBack:self.tipsView];
    self.tipsView.frame = frame;
}

/**
 *  提示页点击事件
 */
- (void)setTipsViewTouchBlock:(GTipsViewTouchBlock)touchBlock
{
    [self.tipsView setTouchBlock:touchBlock];
}

- (void)hideTipsView
{
    self.tipsView.hidden = YES;
}

- (GTipsView *)tipsView
{
    GTipsView *view = [self bk_associatedValueForKey:UIViewControllerTipsTipsView];
    if (!view) {
        view = [[GTipsView alloc] initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, 131)];
        [self setTipsView:view];
    }
    view.hidden = NO;
    return view;
}

- (void)setTipsView:(GTipsView *)tipsView
{
    [self bk_associateValue:tipsView withKey:UIViewControllerTipsTipsView];
}

- (void)addTipsViewConstraints
{
    UIView *view = self.tipsView.superview;
    if (view) {
        @weakify(self, view);
        [self.tipsView mas_makeConstraints:^(MASConstraintMaker *make) {
            @strongify(self, view);
            
            make.width.equalTo(@(self.tipsView.frame.size.width));
            make.height.equalTo(@(self.tipsView.frame.size.height));
            make.centerY.equalTo(view.mas_centerY);
        }];
    }
}

- (GMViewControllerActionSelectBlock)tips_actionBlock
{
    return [self bk_associatedValueForKey:UIViewControllerTipsActionBlock];
}

- (void)setTips_actionBlock:(GMViewControllerActionSelectBlock)tips_actionBlock
{
    [self bk_associateValue:tips_actionBlock withKey:UIViewControllerTipsActionBlock];
}

- (void)showActionSheet:(NSString *)title
             withTitles:(NSArray *)titles
          selectedBlock:(GMViewControllerActionSelectBlock)block;
{
    UIActionSheet *sheet = [[UIActionSheet alloc] initWithTitle:title
                                                       delegate:self
                                              cancelButtonTitle:@"取消"
                                         destructiveButtonTitle:nil
                                              otherButtonTitles:nil, nil];
    
    if (titles.count > 0) {
        for (NSString *buttonTitle in titles) {
            [sheet addButtonWithTitle:buttonTitle];
        }
    }
    
    [self setTips_actionBlock:block];
    [sheet showInView:self.view];
}

- (void)showAlertView:(NSString *)title withCancelTitle:(NSString *)cancelTitle withOther:(NSString *)otherTitle selectedBlock:(GMViewControllerActionSelectBlock)block
{
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:title
                                                        message:nil
                                                       delegate:self
                                              cancelButtonTitle:@"取消" otherButtonTitles:otherTitle, nil];
    [self setTips_actionBlock:block];
    [alertView show];
}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex > 0) {
        if ([self tips_actionBlock]) {
            self.tips_actionBlock(buttonIndex - 1);
            self.tips_actionBlock = nil;
        }
    }
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if ([self tips_actionBlock]) {
        self.tips_actionBlock(buttonIndex);
        self.tips_actionBlock = nil;
    }
}


/**
 *  默认背景的描述和高度
 *
 *  @param titile     描述文字
 *  @param image      显示的图片  传nil 为默认
 *  @param viewHeight 要显示的可视区域大小   传0 表示默认值 非0为要显示区域的高度
 */
- (void)showDefaultView:(NSString *)titile withViewHeight:(CGFloat)viewHeight withImage:(UIImage *)image
{
    [self showDefaultViewIn:self.view title:titile];
}

/**
 *  默认背景点击操作
 */
- (void)addDefaultViewActionBlcok:(void(^)(id value))block
{
    [self setTipsViewTouchBlock:block];
}
/**
 *  隐藏默认背景
 */
- (void)hidDefaultView
{
    [self hideTipsView];
}
/**
 *  默认错误背景的描述和高度
 *
 *  @param titile     描述文字
 *  @param image      显示的图片  传nil 为默认
 *  @param viewHeight 要显示的可视区域大小   传0 表示默认值 非0为要显示区域的高度
 */
- (void)showErrorView:(NSString *)titile withViewHeight:(CGFloat)viewHeight withImage:(UIImage *)image
{
    [self showErrorViewIn:self.view title:titile];
}
/**
 *  隐藏错误默认背景
 */
- (void)hidErrorView
{
    [self hideTipsView];
}

/**
 *  默认错误背景点击操作
 */
- (void)addErrorViewActionBlcok:(void(^)(id value))block
{
    [self setTipsViewTouchBlock:block];
}
/**
 *  添加默认图片
 *
 *  @return 要显示的视图
 */
- (id)addedBaseView
{
    return self.view;
}

@end
