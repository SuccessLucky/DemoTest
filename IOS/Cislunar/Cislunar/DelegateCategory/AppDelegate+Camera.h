//
//  AppDelegate+Camera.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/14.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "AppDelegate.h"


#define EZOpenSDK_AppKey @"d5ac70fc636b4ae7a077d763060b1eec"
#define EZOpenSDK_AppSecret @"e34fbe4425b5d2b4938cfa2c0a10c9e0"

//满佳
//#define EZOpenSDK_AppKey @"e3cd95f361cc4f3ea2965a5c0007e411"
//#define EZOpenSDK_AppSecret @"4fc0e1c0e21984247555f45863e74947"

@interface AppDelegate (Camera)

- (void)doInitEZOpenSDK;

- (void)doLogOutEZOpenSDK;

    
@end
