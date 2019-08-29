//
//  MOBATiKuRequest.h
//  MobAPI
//
//  Created by admin on 16/7/5.
//  Copyright © 2016年 mob. All rights reserved.
//

#import <MobAPI/MobAPI.h>

/**
 *  驾考题库相关查询
 *
 *  @author ShengQiangLiu
 */
@interface MOBATiKuRequest : MOBARequest

/**
 *  科目一题库列表查询接口
 *
 *  @param page 查询起始页：默认为1
 *  @param size 查询页大小：默认为10
 *
 *  @return 请求对象
 */
+ (MOBATiKuRequest *) subjectOneListRequestByPage:(NSInteger)page size:(NSInteger)size;

/**
 *  科目四题库列表查询接口
 *
 *  @param page 查询起始页：默认为1
 *  @param size 查询页大小：默认为10
 *
 *  @return 请求对象
 */
+ (MOBATiKuRequest *) subjectFourListRequestByPage:(NSInteger)page size:(NSInteger)size;

/**
 *  专项题库分类查询
 *
 *  @return 请求对象
 */
+ (MOBATiKuRequest *) specialExamCategoryRequest;

/**
 *  专项练习题库查询
 *
 *  @param page 查询起始页：默认为1
 *  @param size 查询页大小：默认为10
 *  @param cid  分类id。必填项，不允许为 nil
 *
 *  @return 请求对象
 */
+ (MOBATiKuRequest *) specialPracticeExamRequestByPage:(NSInteger)page
                                                  size:(NSInteger)size
                                                   cid:(NSString *)cid;

@end
