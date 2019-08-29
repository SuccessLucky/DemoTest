//
//  MOBAGlobalStockRequest.h
//  MobAPI
//
//  Created by ShengQiangLiu on 16/5/3.
//  Copyright © 2016年 mob. All rights reserved.
//

#import <MobAPI/MobAPI.h>

/**
 *  全球股指信息相关请求
 *
 *  @author ShengQiangLiu
 */
@interface MOBAGlobalStockRequest : MOBARequest

/**
 *  全球股指信息查询
 *
 *  @return 请求对象
 */
+ (MOBAGlobalStockRequest *) globalStockRequest;

/**
 *  全球股指明细查询
 *
 *  @param code 股指编码，三项选填一项
 *  @param name 国家名称(utf-8编码中文)，三项选填一项
 *  @param type 洲名，三项选填一项
 *
 *  @return 请求对象
 */
+ (MOBAGlobalStockRequest *) globalStockDetailRequestByCode:(NSString *)code
                                                countryName:(NSString *)name
                                               continetType:(NSString *)type;

@end
