//
//  MOBAIKTokenRequest.h
//  MobAPI
//
//  Created by admin on 16/6/1.
//  Copyright © 2016年 mob. All rights reserved.
//

#import <MobAPI/MobAPI.h>

/**
 *  词库分词相关查询
 *
 *  @author ShengQiangLiu
 */
@interface MOBAIKTokenRequest : MOBARequest

/**
 *  词库类型列表查询
 *
 *  @return 请求对象
 */
+ (MOBAIKTokenRequest *) wordAnalyzerCategoryRequest;

/**
 *  根据不同的词库分词查询
 *
 *  @param type 选择的词库类型(默认为通用词库:common)
 *  @param text 分词原文本 (base64编码处理[url safe模式]，上限长度length为1024)，必填项，不允许为nil
 *
 *  @return 请求对象
 */
+ (MOBAIKTokenRequest *) wordAnalyzerRequestByType:(NSString *)type text:(NSString *)text;

@end
