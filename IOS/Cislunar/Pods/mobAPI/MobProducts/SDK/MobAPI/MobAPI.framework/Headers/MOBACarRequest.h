//
//  MOBACarRequest.h
//  MobAPI
//
//  Created by admin on 16/7/5.
//  Copyright © 2016年 mob. All rights reserved.
//

#import <MobAPI/MobAPI.h>

/**
 *  汽车信息相关查询
 *
 *  @author ShengQiangLiu
 */
@interface MOBACarRequest : MOBARequest

/**
 *  查询所有汽车品牌
 *
 *  @return 请求对象
 */
+ (MOBACarRequest *) carBrandRequest;

/**
 *  根据车系名称查询车型
 *
 *  @param name 车系名称(从所有汽车品牌接口中取)，必填项，不允许为 nil
 *
 *  @return 请求对象
 */
+ (MOBACarRequest *) carSeriesNameRequestByName:(NSString *)name;

/**
 *  查询车型详细信息
 *
 *  @param cid  车型接口id(默认使用id查询)，必填项，不允许为 nil
 *
 *  @return 请求对象
 */
+ (MOBACarRequest *) carSeriesDetailRequestByCid:(NSString *)cid;

@end
