//
//  MOBAFlightRequest.h
//  MobAPI
//
//  Created by admin on 16/6/1.
//  Copyright © 2016年 mob. All rights reserved.
//

#import <MobAPI/MobAPI.h>

/**
 *  航班信息相关查询
 *
 *  @author ShengQiangLiu
 */
@interface MOBAFlightRequest : MOBARequest

/**
 *  查询所有城市机场三字码信息
 *
 *  @return 请求对象
 */
+ (MOBAFlightRequest *) flightCityRequest;

/**
 *  根据航班号查询航班信息
 *
 *  @param name 航班号 如：CZ8319 ，必填项，不允许为 nil
 *
 *  @return 请求对象
 */
+ (MOBAFlightRequest *) flightNoRequestByName:(NSString *)name;

/**
 *  航线查询航班信息
 *
 *  @param start 出发城市名称 如上海,或具体机场三字码 如:SHA（上海虹桥国际机场）。必填项，不允许为 nil
 *  @param end   到达城市名称 如：海口,或具体机场三字码 如:HAK (美兰国际机场)
 *
 *  @return 请求对象
 */
+ (MOBAFlightRequest *) flightLineRequestByStart:(NSString *)start end:(NSString *)end;

@end
