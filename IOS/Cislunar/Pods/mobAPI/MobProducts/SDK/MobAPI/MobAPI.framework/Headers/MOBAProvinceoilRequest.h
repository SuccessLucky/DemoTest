//
//  MOBAProvinceoilRequest.h
//  MobAPI
//
//  Created by ShengQiangLiu on 16/3/30.
//  Copyright © 2016年 mob. All rights reserved.
//

#import <MobAPI/MobAPI.h>

/**
 *  今日各省油价相关请求
 *
 *  @author ShengQiangLiu
 */
@interface MOBAProvinceoilRequest : MOBARequest

/**
 *  查询各省今日油价
 *
 *  @return 请求的对象
 */
+ (MOBAProvinceoilRequest *) provinceoilRequest;

@end
