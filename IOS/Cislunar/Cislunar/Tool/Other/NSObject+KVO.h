//
//  NSObject+KVO.h
//  GJJManager
//
//  Created by yangpei on 15/5/4.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void(^GKVONotifyBlock)(id value);

@interface NSObject (KVO)

/**
 *  监听value改变事件
 *
 *  @param keyPath 监听的keypath
 *  @param block   值改变后调用的block
 */
- (void)observeKeyPath:(NSString *)keyPath block:(GKVONotifyBlock)block;

@end
