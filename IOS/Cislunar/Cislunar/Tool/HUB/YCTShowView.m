//
//  YCTShowView.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/27.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "YCTShowView.h"
#import "EasyTextView.h"
#import "EasyLoadingView.h"
#import "EasyAlertTypes.h"
#import "EasyAlertPart.h"
#import "EasyAlertConfig.h"
#import "EasyAlertView.h"

@implementation YCTShowView

+(id)shareInstance
{
    static YCTShowView *errorMessage = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        errorMessage = [[YCTShowView alloc] init];
        
    });
    return errorMessage;
}

#pragma mark - showText
#pragma mark - 显示错误信息

- (void)showError:(NSString *)error withType:(TextStatusType)type
{
    [EasyTextView showErrorText:error config:^EasyTextConfig *{
        return [EasyTextConfig shared].setStatusType(type) ;
    }];
}

- (void)showErrorDefult:(NSString *)error
{
    [EasyTextView showErrorText:error config:^EasyTextConfig *{
        return [EasyTextConfig shared].setStatusType(TextStatusTypeMidden) ;
    }];
}

#pragma mark -显示成功的信息
- (void)showSuccess:(NSString *)success
{
    [EasyTextView showSuccessText:success];
}

#pragma mark -在导航栏上方显示信息
- (void)showNetworkRequestOnTopWithMsg:(NSString *)strMsg
{
    [EasyTextView showErrorText:strMsg config:^EasyTextConfig *{
        return [EasyTextConfig shared].setStatusType(TextStatusTypeNavigation) ;
    }];
}


#pragma mark -显示信息可以自定义时间

- (void)showMessagDefult:(NSString *)message
{
    [EasyTextView showText:message config:^EasyTextConfig *{
        EasyTextConfig *config = [EasyTextConfig shared];
        config.bgColor = [UIColor lightGrayColor] ;
        config.shadowColor = [UIColor clearColor] ;
        config.animationType = TextAnimationTypeFade;
        config.statusType = TextStatusTypeMidden ;
        return config ;
    }];
}

- (void)showMessag:(NSString *)message delayTime:(CGFloat)fDelayTime
{
    [EasyTextView showText:message config:^EasyTextConfig *{
        //（这三种方法都是一样的，根据使用习惯选择一种就行。|| 不传的参数就会使用EasyTextGlobalConfig全局配置的属性）
        //方法一
        //return [EasyTextConfig configWithSuperView:self.view superReceiveEvent:ShowTextEventUndefine animationType:TextAnimationTypeNone textStatusType:TextStatusTypeBottom];
        //方法二
        //return [EasyTextConfig shared].setBgColor([UIColor lightGrayColor]).setShadowColor([UIColor clearColor]).setStatusType(TextStatusTypeBottom);
        //方法三
        EasyTextConfig *config = [EasyTextConfig shared];
        config.bgColor = [UIColor lightGrayColor] ;
        config.shadowColor = [UIColor clearColor] ;
        config.animationType = TextAnimationTypeFade;
        config.statusType = TextStatusTypeMidden ;
        config.textShowTimeBlock = ^float(NSString * _Nonnull text) {
            return fDelayTime;
        };
        
        return config ;
    }];
}


#pragma mark -自定义图片显示图片信息弹出框
- (void)showImageText:(NSString *)strMsg imageName:(NSString *)strImageName
{
    [EasyTextView showImageText:strMsg imageName:strImageName config:^EasyTextConfig *{
        return [EasyTextConfig shared].setAnimationType(TextAnimationTypeNone).setShadowColor([UIColor clearColor]).setBgColor([UIColor blackColor]).setTitleColor([UIColor whiteColor]).setSuperReceiveEvent(NO);
    }];
}


#pragma mark - showLoading
#pragma mark - 转圈加载框
- (void)doShowLoadingCircleWithMsg:(NSString *)strMsg  delayTime:(CGFloat)fDelayTime
{
    EasyLoadingView *LoadingV =  [EasyLoadingView showLoadingText:strMsg];
    if (fDelayTime) {
        dispatch_queue_after_S(fDelayTime, ^{
            [EasyLoadingView hidenLoading:LoadingV];
        });
    }
}

#pragma mark - 菊花加载框loading
- (void)doShowJuHuaLoadingWithMsg:(NSString *)strMsg withType:(LoadingShowType)type delayTime:(CGFloat)fDelayTime
{
    EasyLoadingView *LoadingV =  [EasyLoadingView showLoadingText:strMsg config:^EasyLoadingConfig *{
        return [EasyLoadingConfig shared].setLoadingType(type);
    }];
    
    if (fDelayTime) {
        dispatch_queue_after_S(fDelayTime, ^{
            [EasyLoadingView hidenLoading:LoadingV];
        });
    }
}

#pragma mark - 自定义图片加载框loading
- (void)doShowCustomPictureLoadingWithMsg:(NSString *)strMsg withType:(LoadingShowType)type delayTime:(CGFloat)fDelayTime
{
    EasyLoadingView *LoadingV = [EasyLoadingView showLoadingText:strMsg config:^EasyLoadingConfig *{
        return [EasyLoadingConfig shared].setLoadingType(type).setSuperReceiveEvent(NO);
    }];
    if (fDelayTime) {
        dispatch_queue_after_S(fDelayTime, ^{
            [EasyLoadingView hidenLoading:LoadingV];
        });
    }
}

#pragma mark - 图片反转加载框loading
- (void)doShowPictureFlipLoadingWithMsg:(NSString *)strMsg withType:(LoadingShowType)type delayTime:(CGFloat)fDelayTime
{
    EasyLoadingView *LoadingV = [EasyLoadingView showLoadingText:strMsg imageName:@"HUD_NF.png" config:^EasyLoadingConfig *{
        return [EasyLoadingConfig shared].setLoadingType(type).setBgColor([UIColor blackColor]).setTintColor([UIColor whiteColor]);
    }];
    
    if (fDelayTime) {
        dispatch_queue_after_S(fDelayTime, ^{
            [EasyLoadingView hidenLoading:LoadingV];
        });
    }
}

#pragma mark - 隐藏loading
- (void)doHideLoading
{
    [EasyLoadingView hidenLoading];
}


#pragma mark - showAlert
#pragma mark - 带 “确定” “取消” 按钮的alterview
- (void)doShowCustomAlterViewWithTitle:(NSString *)strTitle
                               content:(NSString *)strContent
                      strConfirmButton:(NSString *)strConfirmButton
                      strCancellButton:(NSString *)strCancellButton
                        callBackHandle:(CustomAlterViewBtnPressedCallback)callBack
{

    AlertAnimationType aniType =AlertAnimationTypeFade;
    UIColor *tintC = [UIColor groupTableViewBackgroundColor] ;
    
    
    [EasyAlertView alertViewWithPart:^EasyAlertPart *{
        return [EasyAlertPart shared].setTitle(strTitle).setSubtitle(strContent).setAlertType(AlertViewTypeAlert) ;
    } config:^EasyAlertConfig *{
        return [EasyAlertConfig shared].settwoItemHorizontal(YES).setAnimationType(aniType).setTintColor(tintC).setBgViewEvent(NO).setSubtitleTextAligment(NSTextAlignmentLeft).setEffectType(AlertBgEffectTypeAlphaCover) ;
    } buttonArray:^NSArray<NSString *> *{
        if (strConfirmButton.length == 0) {
            return @[strCancellButton] ;
        }else if (strCancellButton.length == 0) {
            return @[strConfirmButton] ;
        }else
            return @[strConfirmButton,strCancellButton] ;
    } callback:^(EasyAlertView *showview , long index) {
        if (callBack) {
            callBack(index);
        }
    }];
}

#pragma mark -actionsSheet
- (void)doShowActionSheetWithTitle:(NSString *)strTitle
                          subTitle:(NSString *)strSubTitle
                        arrContent:(NSArray<NSString *> *)arrConttent
                     cancellButton:(NSString *)strCancell
                    callBackHandle:(CustomActionSheetBtnPressedtCallback)callBack
{
    //第一步 创建alertview
    EasyAlertView *alertV = [EasyAlertView alertViewWithPart:^EasyAlertPart *{
        return [EasyAlertPart shared].setTitle(strTitle).setSubtitle(strSubTitle).setAlertType(AlertViewTypeActionSheet) ;
    } config:nil buttonArray:nil callback:^(EasyAlertView *showview, long index) {
        NSLog(@"点击了 index = %ld",index );
        if (callBack) {
            callBack(index);
        }
    }];
    
    //第二步 添加上面的按钮
    [alertV addAlertItemWithTitleArray:arrConttent callback:nil];
    [alertV addAlertItem:^EasyAlertItem *{
        return [EasyAlertItem itemWithTitle:strCancell type:AlertItemTypeBlodRed callback:^(EasyAlertView *showview, long index) {
            //因为上面已经加了一个全局的回调，所以这个地方是不会回调的
            NSLog(@"红色粗体 = %ld",index );
        }];
    }];
    //第三步  显示alertview
    [alertV showAlertView];
}


#pragma mark -
#pragma mark - 参考
//系统AlterView
- (void)doShowSystemAterView
{
    EasyAlertView *alertView = [EasyAlertView alertViewWithTitle:@"标题" subtitle:@"负表笔" AlertViewType:AlertViewTypeSystemAlert config:nil];
    [alertView addAlertItem:^EasyAlertItem *{
        return [EasyAlertItem itemWithTitle:@"红色按钮_1" type:AlertItemTypeSystemDestructive callback:nil];
    }];
    [alertView addAlertItem:^EasyAlertItem *{
        return [EasyAlertItem itemWithTitle:@"按钮 " type:AlertItemTypeSystemDefault callback:nil];
    }];
    [alertView addAlertItem:^EasyAlertItem *{
        return [EasyAlertItem itemWithTitle:@"取消控件(一定在最下面)" type:AlertItemTypeSystemCancel callback:nil];
    }];
    [alertView addAlertItem:^EasyAlertItem *{
        return [EasyAlertItem itemWithTitle:@"红色按钮_2" type:AlertItemTypeSystemDestructive callback:nil];
    }];
    [alertView addAlertItem:^EasyAlertItem *{
        return [EasyAlertItem itemWithTitle:@"按钮" type:AlertItemTypeSystemDefault callback:nil];
    }];
    [alertView showAlertView];
}

//系统ActionSheet
- (void)doShowSystemActionSheetWithTitle
{
    static int a = 0 ;
    EasyAlertView *alertV = [EasyAlertView alertViewWithTitle:nil subtitle:@"只有添加Cancel按钮的时候，点击背景才会销毁alertview" AlertViewType:AlertViewTypeSystemActionSheet config:nil];
    if (++a%2) {
        [alertV addAlertItem:^EasyAlertItem *{
            return [EasyAlertItem itemWithTitle:@"取消按钮" type:AlertItemTypeSystemCancel callback:nil];
        }];
    }
    [alertV addAlertItemWithTitle:@"红色按钮" type:AlertItemTypeSystemDestructive callback:nil];
    [alertV addAlertItemWithTitleArray:@[@"你好",@"谢谢"] callback:nil];
    
    [alertV showAlertView];
}


@end
