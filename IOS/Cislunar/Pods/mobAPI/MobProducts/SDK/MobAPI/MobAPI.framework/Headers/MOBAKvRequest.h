//
//  MOBAKvRequest.h
//  MobAPI
//
//  Created by liyc on 16/2/16.
//  Copyright © 2016年 mob. All rights reserved.
//

#import "MOBARequest.h"

/**
 *   k-v 存储相关操作
 */
@interface MOBAKvRequest : MOBARequest

/**
 *  @author liyc
 */

/**
 *  k-v存储
 *
 *  @param table 集合名称，一个用户只能拥有5个自定义的table。必填项，不允许为nil
 *  @param k     标准base64编码，且为URLSafe模式。必填项，不允许为nil
 *  @param v     1、JSON数据格式的base64编码，且URLSafe模式   2、长度最大限制为2KB    必填项，不允许为nil
 *
 *  @return 请求的对象
 */
+ (MOBAKvRequest *) kvPutRequest:(NSString *)table
                            key:(NSString *)k
                          value:(NSString *)v;

/**
 *  k-v查询
 *
 *  @param table 集合名称。必填项，不允许为nil
 *  @param k     标准base64编码，且为URLSafe模式。必填项，不允许为nil
 *
 *  @return 请求的对象
 */
+ (MOBAKvRequest *) kvGetRequest:(NSString *)table key:(NSString *)k;

/**
 *  @author ShengQiangLiu
 */

/**
 *  查询用户表中所有数据
 *
 *  @param table 集合名称。必填项，不允许为nil
 *  @param page  起始页(默认1)
 *  @param size  返回数据条数(默认20)
 *
 *  @return 请求对象
 */
+ (MOBAKvRequest *) kvGetAllDataRequestByTable:(NSString *)table
                                          page:(NSInteger)page
                                          size:(NSInteger)size;

/**
 *  统计用户表中数据总数
 *
 *  @param table 集合名称。必填项，不允许为nil
 *
 *  @return 请求对象
 */
+ (MOBAKvRequest *) kvStatisticsDataCountRequestByTable:(NSString *)table;

/**
 *  删除数据表中的单条数据
 *
 *  @param table 集合名称。必填项，不允许为nil
 *  @param k     数据主键。必填项，不允许为 nil
 *
 *  @return 请求对象
 */
+ (MOBAKvRequest *) kvDeleteSigleDataRequestyByTable:(NSString *)table key:(NSString *)k;

/**
 *  查询用户的所有表
 *
 *  @return 请求对象
 */
+ (MOBAKvRequest *) kvGetAllTablesRequest;



@end
