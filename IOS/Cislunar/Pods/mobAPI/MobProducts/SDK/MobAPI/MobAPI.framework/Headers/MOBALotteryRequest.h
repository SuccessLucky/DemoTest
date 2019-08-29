//
//  MOBALotteryRequest.h
//  MobAPI
//
//  Created by ShengQiangLiu on 16/3/30.
//  Copyright © 2016年 mob. All rights reserved.
//

#import <MobAPI/MobAPI.h>

/**
 *  彩票开奖信息相关请求
 *
 *  @author ShengQiangLiu
 */
@interface MOBALotteryRequest : MOBARequest

/**
 *  查询彩票开奖结果
 *
 *  @param name   彩种，必填项，不允许为nil
 *  @param period 期次
 *
 *  @return 请求的对象
 */
+ (MOBALotteryRequest *) lotteryRequestByName:(NSString *)name period:(NSString *)period;

@end
