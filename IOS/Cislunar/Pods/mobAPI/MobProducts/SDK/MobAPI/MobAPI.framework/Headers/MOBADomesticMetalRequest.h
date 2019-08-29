//
//  MOBADomesticMetalRequest.h
//  MobAPI
//
//  Created by ShengQiangLiu on 16/5/4.
//  Copyright © 2016年 mob. All rights reserved.
//

#import <MobAPI/MobAPI.h>

/**
 *  国内交易所贵金属数据相关请求
 *
 *  @author ShengQiangLiu
 */
@interface MOBADomesticMetalRequest : MOBARequest

/**
 *  国内交易所贵金属数据查询
 *
 *  @param exchange 交易所数据编号(1:天津贵金属交易所 2：广东贵金属 3：南方贵金属) 默认是 1
 *
 *  @return 请求对象
 */
+ (MOBADomesticMetalRequest *) domesticSpotRequestByExchange:(NSString *)exchange;

@end
