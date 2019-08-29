//
//  MOBATrainTicketsRequest.h
//  MobAPI
//
//  Created by admin on 16/6/1.
//  Copyright © 2016年 mob. All rights reserved.
//

#import <MobAPI/MobAPI.h>

/**
 *  火车票相关查询
 *
 *  @author ShengQiangLiu
 */
@interface MOBATrainTicketsRequest : MOBARequest

/**
 *  车次查询
 *
 *  @param trainno 车次号，必填项，不允许为 nil
 *
 *  @return 请求对象
 */
+ (MOBATrainTicketsRequest *) trainTicketsQueryRequestByTrainno:(NSString *)trainno;

/**
 *  站站查询
 *
 *  @param start 出发站名，必填项，不允许为 nil
 *  @param end   到达站名，必填项，不允许为 nil
 *
 *  @return 请求对象
 */
+ (MOBATrainTicketsRequest *) trainTicketsQueryRequestByStationStart:(NSString *)start toEnd:(NSString *)end;

@end
