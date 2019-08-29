//
//  NSObject+KVO.m
//  GJJManager
//
//  Created by yangpei on 15/5/4.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#import "NSObject+KVO.h"
#import "FBKVOController.h"

@implementation NSObject (KVO)

- (void)observeKeyPath:(NSString *)keyPath block:(GKVONotifyBlock)block
{
    NSArray *paths = [keyPath componentsSeparatedByString:@"."];
    NSAssert(paths.count > 1, @"can't observe self");
    
    id observeObj = [self valueForKeyPath:[paths firstObject]];
    NSString *observePath = [[paths subarrayWithRange:NSMakeRange(1, paths.count - 1)] componentsJoinedByString:@"."];
    NSLog(@"the observePath = %@", observePath);
    
    [self.KVOController observe:observeObj
                        keyPath:observePath
                        options:NSKeyValueObservingOptionNew
                          block:^(id observer, id object, NSDictionary *change) {
                              if (block) {
                                  block((change[NSKeyValueChangeNewKey] != [NSNull null]) ? change[NSKeyValueChangeNewKey] : nil);
                              }
                          }];
}


@end
