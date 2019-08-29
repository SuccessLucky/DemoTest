//
//  EasyLinkManager.h
//  JiZhi
//
//  Created by 王宗成 on 17/7/11.
//  Copyright © 2017年 DemoKing. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "EASYLINK.h"//加easylink头文件
@interface EasyLinkManager : NSObject
@property(nonatomic,copy)NSString *wifiSSID;
@property(nonatomic,copy)NSString *wifiPSD;
@property(nonatomic,strong)EASYLINK *easyLinker;
@property(nonatomic,copy)NSString *actKey;

- (void)runningEasyLinker ;
- (void)stopEasyLinker ;

- (void)configureEasyLinker;
- (void)prepareEasyLinker ;
@end
