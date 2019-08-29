//
//  SendOrderModel.h
//  JiZhiSDK
//
//  Created by 王振 DemoKing on 2016/12/1.
//  Copyright © 2016年 DemoKing. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SendOrderModel : NSObject

/**
 OD
 */
@property (strong, nonatomic) NSData *od;

/**
 子索引
 */
@property (strong, nonatomic) NSData *subIndex;

/**
 子索引选项
 */
@property (strong, nonatomic) NSData *subIndexSelection;

/**
 源目标地址
 */
@property (strong, nonatomic) NSData *originMac;

/**
 命令标示
 */
@property (strong, nonatomic) NSData *cmdId;

/**
 目标地址形式
 */
@property (strong, nonatomic) NSData *dstAddrFmt;

/**
  目标地址
 */
@property (strong, nonatomic) NSData *dstAddr;

/**
 数据
 */
@property (strong, nonatomic) NSData *data;

@end
