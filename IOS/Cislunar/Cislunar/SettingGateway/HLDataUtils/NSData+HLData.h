//
//  NSData+HLData.h
//  JiZhiSDK
//
//  Created by 王振 DemoKing on 2016/11/30.
//  Copyright © 2016年 DemoKing. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSData (HLData)

/**
 截取data长度

 @param location 位置
 @param length 长度
 @return data
 */
- (NSData *)subDataLocation:(int)location length:(int)length;
- (NSString *)host;
@end
