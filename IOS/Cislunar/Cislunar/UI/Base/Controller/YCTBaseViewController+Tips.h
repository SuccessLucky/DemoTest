//
//  YCTBaseViewController+Tips.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/15.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "YCTBaseViewController.h"
#import "GTipsView.h"

typedef void(^GMViewControllerActionSelectBlock)(NSUInteger selectedIndex);

@interface YCTBaseViewController (Tips)<UIActionSheetDelegate, UIAlertViewDelegate>

@property (nonatomic, strong) GTipsView *tipsView;

@property (nonatomic, copy) GMViewControllerActionSelectBlock tips_actionBlock;

/**
 *  显示默认背景
 *
 *  @param view  要显示的父容器 eg.UITableView
 *  @param title 描述文字
 */
- (void)showDefaultViewIn:(UIView *)view title:(NSString *)title;
/**
 *  显示默认背景
 *
 *  @param frame 要显示的区域
 *  @param title 描述文字
 */
- (void)showDefaultViewInFrame:(CGRect)frame title:(NSString *)title;

/**
 *  显示默认背景
 *  @param view  要显示的父容器 eg.UITableView
 *  @param frame 要显示的区域
 *  @param title 描述文字
 */
- (void)showDefaultViewIn:(UIView *)view frame:(CGRect)frame title:(NSString *)title;

/**
 *  显示错误背景
 *
 *  @param view  要显示的父容器 eg.UITableView
 *  @param title 描述文字
 */
- (void)showErrorViewIn:(UIView *)view title:(NSString *)title;

/**
 *  显示错误背景
 *
 *  @param view  要显示的区域
 *  @param title 描述文字
 */
- (void)showErrorViewInFrame:(CGRect)frame title:(NSString *)title;

/**
 *  显示错误背景
 *  @param view  要显示的父容器 eg.UITableView
 *  @param view  要显示的区域
 *  @param title 描述文字
 */
- (void)showErrorViewIn:(UIView *)view frame:(CGRect)frame title:(NSString *)title;

/**
 *  提示页点击事件
 */
- (void)setTipsViewTouchBlock:(GTipsViewTouchBlock)touchBlock;

/**
 *  隐藏背景
 */
- (void)hideTipsView;

- (void)showActionSheet:(NSString *)title
             withTitles:(NSArray *)titles
          selectedBlock:(GMViewControllerActionSelectBlock)block;

- (void)showAlertView:(NSString *)title
      withCancelTitle:(NSString *)cancelTitle
            withOther:(NSString *)otherTitle
        selectedBlock:(GMViewControllerActionSelectBlock)block;


/**
 *  默认背景的描述和高度
 *
 *  @param titile     描述文字
 *  @param image      显示的图片  传nil 为默认
 *  @param viewHeight 要显示的可视区域大小   传0 表示默认值 非0为要显示区域的高度
 */
- (void)showDefaultView:(NSString *)titile withViewHeight:(CGFloat)viewHeight withImage:(UIImage *)image;

/**
 *  默认背景点击操作
 */
- (void)addDefaultViewActionBlcok:(void(^)(id value))block;
/**
 *  隐藏默认背景
 */
- (void)hidDefaultView;
/**
 *  默认错误背景的描述和高度
 *
 *  @param titile     描述文字
 *  @param image      显示的图片  传nil 为默认
 *  @param viewHeight 要显示的可视区域大小   传0 表示默认值 非0为要显示区域的高度
 */
- (void)showErrorView:(NSString *)titile withViewHeight:(CGFloat)viewHeight withImage:(UIImage *)image;
/**
 *  隐藏错误默认背景
 */
- (void)hidErrorView;

/**
 *  默认错误背景点击操作
 */
- (void)addErrorViewActionBlcok:(void(^)(id value))block;
/**
 *  添加默认图片
 *
 *  @return 要显示的视图
 */
- (id)addedBaseView;

@end
