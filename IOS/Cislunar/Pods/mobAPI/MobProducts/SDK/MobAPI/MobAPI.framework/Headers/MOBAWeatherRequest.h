//
//  MOBAWeatherRequest.h
//  MOBApiCloud
//
//  Created by fenghj on 15/10/9.
//  Copyright © 2015年 mob. All rights reserved.
//

#import "MOBARequest.h"

/**
 *  天气相关请求
 */
@interface MOBAWeatherRequest : MOBARequest

/**
 *  查询天气
 *
 *  @param cityName 城市名（url编码）。必填项，不允许为 nil
 *  @param provinceName 所属省份名称
 *
 *  @return 请求对象
 */
+ (MOBAWeatherRequest *) searchRequestByCityName:(NSString *)cityName province:(NSString *)provinceName
                                                                      DEPRECATED_MSG_ATTRIBUTE("use -[searchRequestByCity:province:] method instead.");

/**
 *  查询天气
 *
 *  @param city 城市名（url编码）。必填项，不允许为 nil
 *  @param province 当前城市的所属省份 如：北京-通州、江苏-通州（url编码）
 *
 *  @return 请求对象
 */
+ (MOBAWeatherRequest *) searchRequestByCity:(NSString *)city province:(NSString *)province;

/**
 *  获取城市列表
 *
 *  @return 请求对象
 */
+ (MOBAWeatherRequest *) citysRequest DEPRECATED_MSG_ATTRIBUTE("use -[citiesRequest] method instead.");

/**
 *  获取城市列表
 *
 *  @return 请求对象
 */
+ (MOBAWeatherRequest *) citiesRequest;

/**
 *  根据ip地址查询天气
 *
 *  @param ip 网络ip地址。必填项，不允许为 nil
 *
 *  @return 请求对象
 */
+ (MOBAWeatherRequest *) searchRequestByIP:(NSString *)ip DEPRECATED_MSG_ATTRIBUTE("use -[searchRequestByCity:province:] method instead.");

/**
 *  根据ip地址查询天气
 *
 *  @param ip 网络ip地址。必填项，不允许为 nil
 *  @param province 当前城市的所属省份 如：北京-通州、江苏-通州（url编码）
 *
 *  @return 请求对象
 */
+ (MOBAWeatherRequest *) searchRequestByIP:(NSString *)ip province:(NSString *)province;

/**
 *  @author ShengQiangLiu
 */

/**
 *  天气类型查询
 *
 *  @return 请求对象
 */
+ (MOBAWeatherRequest *) weatherTypeRequest;

@end
