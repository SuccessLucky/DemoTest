//
//  NetworkEngine+WIFIBaseInfo.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine.h"

NS_ASSUME_NONNULL_BEGIN

@interface NetworkEngine (WIFIBaseInfo)

/**
 读取WIFI模块基本信息
 
 @return data
 */
- (NSData *)doGetSetReadGatewayWifiInfoData;

@end

NS_ASSUME_NONNULL_END
