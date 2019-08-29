//
//  MOBASilverRequest.h
//  MobAPI
//
//  Created by ShengQiangLiu on 16/5/4.
//  Copyright © 2016年 mob. All rights reserved.
//

#import <MobAPI/MobAPI.h>

/**
 *  上海交易所白银数据相关请求
 *
 *  @author ShengQiangLiu
 */
@interface MOBASilverRequest : MOBARequest

/**
 *  现货白银数据查询
 *
 *  @return 请求对象
 */
+ (MOBASilverRequest *) silverSpotRequest;

/**
 *  期货白银数据查询
 *
 *  @return 请求对象
 */
+ (MOBASilverRequest *) silverShfutureRequest;

@end
