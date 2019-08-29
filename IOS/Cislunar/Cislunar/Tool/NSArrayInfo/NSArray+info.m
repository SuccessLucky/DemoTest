//
//  NSArray+info.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/1/11.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import "NSArray+info.h"

@implementation NSArray (info)

-(NSString *)descriptionWithLocale:(id)locale {
    
    NSMutableString *str = [NSMutableString stringWithFormat:@"\n\t{"];
    [self enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        
        [str appendFormat:@"\t%@\n", obj];
        
    }];
    [str appendFormat:@"\t}"];
    return str;
    
}

@end

@implementation NSDictionary (info)

-(NSString *)descriptionWithLocale:(id)locale {
    
    NSMutableString *str = [NSMutableString stringWithFormat:@"\n\t{"];
    [self enumerateKeysAndObjectsUsingBlock:^(id  _Nonnull key, id  _Nonnull obj, BOOL * _Nonnull stop) {
        
        [str appendFormat:@"\t%@\n", obj];
        
    }];
    [str appendFormat:@"\t}"];
    return str;
    
}

@end

