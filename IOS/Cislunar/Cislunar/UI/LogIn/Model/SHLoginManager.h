//
//  SHLoginManager.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/1/13.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void (^LoginRspCallBack)(BOOL success, id result);
typedef void (^LogOutRspCallBack)(BOOL success, id result);

@interface SHLoginManager : NSObject

+ (id)shareInstance;


@end
