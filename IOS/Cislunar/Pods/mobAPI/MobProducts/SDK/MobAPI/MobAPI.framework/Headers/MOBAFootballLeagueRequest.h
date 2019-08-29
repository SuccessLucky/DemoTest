//
//  MOBAFootballLeagueRequest.h
//  MobAPI
//
//  Created by admin on 16/7/5.
//  Copyright © 2016年 mob. All rights reserved.
//

#import <MobAPI/MobAPI.h>

/**
 *  足球5大联赛信息相关查询
 *
 *  @author ShengQiangLiu
 */
@interface MOBAFootballLeagueRequest : MOBARequest

/**
 *  参数词典查询
 *
 *  @return 请求对象
 */
+ (MOBAFootballLeagueRequest *) footballLeagueQueryParamRequest;

/**
 *  查询某个轮次的比赛信息
 *
 *  @param type   联赛类型，必填项，不允许为 nil
 *  @param season 赛季，必填项，不允许为 nil
 *  @param round  轮次，必填项，不允许为 nil
 *
 *  @return 请求对象
 */
+ (MOBAFootballLeagueRequest *) queryMatchInfoByLeagueTypeCn:(NSString *)type
                                                      season:(NSString *)season
                                                       round:(NSString *)round;

/**
 *  查询队伍的比赛信息
 *
 *  @param type   联赛类型，必填项，不允许为 nil
 *  @param teamA  队伍A（队伍必须输入一个）
 *  @param teamB  队伍B（队伍必须输入一个）
 *  @param season 赛季
 *  @param round  轮次
 *
 *  @return 请求对象
 */
+ (MOBAFootballLeagueRequest *) queryTeamMatchInfoByleagueTypeCn:(NSString *)type
                                                           teamA:(NSString *)teamA
                                                           teamB:(NSString *)teamB
                                                          season:(NSString *)season
                                                           round:(NSString *)round;


@end
