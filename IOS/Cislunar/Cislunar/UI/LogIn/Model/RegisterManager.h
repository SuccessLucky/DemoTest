//
//  RegisterManager.h
//  SmartHouseYCT
//
//  Created by apple on 16/9/10.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void (^RegisterCallBack)(BOOL success, id result);
typedef void (^MobileRegisterTokenReqCallBack)(BOOL success, id result);

@interface RegisterManager : NSObject

/**
 *  处理注册
 *
 *  @param userName 用户名
 *  @param psw      密码
 *  @param mobile   手机号
 *  @param email    邮箱
 *  @param callBack 回调
 */
+ (void)handleTheRegisterDataWithUserName:(NSString *)userName
                                 password:(NSString *)psw
                                   mobile:(NSString *)nickName
                         verificationCode:(NSString *)code
                           callBackHandle:(RegisterCallBack)callBack;

/**
 *  获取注册验证码
 *
 *  @param telNum   手机号
 *  @param callBack 成功失败和验证码
 */
+ (void)handleTheGetMobileRegisterTokenReqWithTelNum:(NSString *)telNum
                      mobileRegisterTokenReqCallBack:(MobileRegisterTokenReqCallBack)callBack;

@end
