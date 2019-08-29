//
//  YCTShowView.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/27.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "EasyLoadingTypes.h"
#import "EasyTextTypes.h"

NS_ASSUME_NONNULL_BEGIN

typedef void (^CustomAlterViewBtnPressedCallback)( long index);
typedef void (^CustomActionSheetBtnPressedtCallback)(long index);


@interface YCTShowView : NSObject

+(id)shareInstance;

#pragma mark - showText
//显示错误信息
- (void)showErrorDefult:(NSString *)error;
- (void)showError:(NSString *)error withType:(TextStatusType)type;

//显示成功的信息
- (void)showSuccess:(NSString *)success;

//在导航栏上方显示信息
- (void)showNetworkRequestOnTopWithMsg:(NSString *)strMsg;

//显示信息可以自定义时间
- (void)showMessagDefult:(NSString *)message;
- (void)showMessag:(NSString *)message delayTime:(CGFloat)fDelayTime;

//自定义图片显示图片信息弹出框
- (void)showImageText:(NSString *)strMsg imageName:(NSString *)strImageName;

#pragma mark - showLoading
//转圈加载框
- (void)doShowLoadingCircleWithMsg:(NSString *)strMsg  delayTime:(CGFloat)fDelayTime;

//菊花加载框loading
- (void)doShowJuHuaLoadingWithMsg:(NSString *)strMsg withType:(LoadingShowType)type delayTime:(CGFloat)fDelayTime;

//自定义图片加载框loading
- (void)doShowCustomPictureLoadingWithMsg:(NSString *)strMsg withType:(LoadingShowType)type delayTime:(CGFloat)fDelayTime;

// 图片反转加载框loading
- (void)doShowPictureFlipLoadingWithMsg:(NSString *)strMsg withType:(LoadingShowType)type delayTime:(CGFloat)fDelayTime;

// 隐藏loading
- (void)doHideLoading;

#pragma mark - showAlert
//带 “确定” “取消” 按钮的alterview
- (void)doShowCustomAlterViewWithTitle:(NSString *)strTitle
                               content:(NSString *)strContent
                      strConfirmButton:(NSString *)strConfirmButton
                      strCancellButton:(NSString *)strCancellButton
                        callBackHandle:(CustomAlterViewBtnPressedCallback)callBack;

//actionsSheet
- (void)doShowActionSheetWithTitle:(NSString *)strTitle
                          subTitle:(NSString *)strSubTitle
                        arrContent:(NSArray<NSString *> *)arrConttent
                     cancellButton:(NSString *)strCancell
                    callBackHandle:(CustomActionSheetBtnPressedtCallback)callBack;


@end

NS_ASSUME_NONNULL_END
