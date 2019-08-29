//
//  SHLoginManager+Network.h
//  SmartHouseYCT
//
//  Created by apple on 16/9/22.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHLoginManager.h"
typedef void (^BlockLogOutHandleComplete)(BOOL success, id result);

@interface SHLoginManager (Network)

//登录
- (void)handleTheLoginDataWithUserName:(NSString *)userName
                              password:(NSString *)psw
                        callBackHandle:(LoginRspCallBack)callBack;

//登出
- (void)doLogOutFromNetworkCallBack:(BlockLogOutHandleComplete)complete;



@end
