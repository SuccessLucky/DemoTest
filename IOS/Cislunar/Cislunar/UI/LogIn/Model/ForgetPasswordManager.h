//
//  ForgetPasswordManager.h
//  SmartHouseYCT
//
//  Created by apple on 16/9/10.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void (^RestPswCodeCallBack)(BOOL success, id result);

@interface ForgetPasswordManager : NSObject

- (void)handleRequestGetResetPswCode:(NSString *)telNum complete:(RestPswCodeCallBack)completeHandle;

//重置密码接口
- (void)handleRequestResetPswCodeWithTelNum:(NSString *)strTelNum
                                       code:(NSString *)strCode
                                     newPsw:(NSString *)strNewPsw
                                   complete:(RestPswCodeCallBack)completeHandle;

@end
