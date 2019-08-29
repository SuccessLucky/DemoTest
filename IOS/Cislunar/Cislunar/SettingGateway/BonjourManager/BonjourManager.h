//
//  BonjourManager.h
//  JiZhi
//
//  Created by 王宗成 on 17/7/10.
//  Copyright © 2017年 DemoKing. All rights reserved.
//

#import <Foundation/Foundation.h>
//#import "MessageModel.h"

typedef void(^BonjourTimeOutBlock)(void);

@interface BonjourManager : NSObject<NSNetServiceDelegate,NSNetServiceBrowserDelegate>

@property (strong, nonatomic) SHModelGateway *gwModel;
@property (strong, nonatomic) NSArray *arrGatewayInfoGet;
@property (strong, nonatomic) NSMutableArray *mutArrInfo;

@property(strong,nonatomic)NSNetServiceBrowser *brower;
@property(strong,nonatomic)NSMutableArray *displayServices;
@property(nonatomic,strong)NSMutableArray *services;
@property(nonatomic,copy)BonjourTimeOutBlock timeOutBlock;
@property(nonatomic,strong)NSMutableArray *dataArray;

- (void)startSearchDevice;

-(void)startSearchDeviceWithOutTime:(NSInteger)outTime callBackTimeOutBlock:(BonjourTimeOutBlock)block;

- (void)stopSearchDevice;
-(void)restartSearchService;
@end
