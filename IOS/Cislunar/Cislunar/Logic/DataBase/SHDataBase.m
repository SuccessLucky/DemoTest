//
//  SHDataBase.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/25.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHDataBase.h"

@implementation SHDataBase

static FMDatabase *db;

+ (FMDatabase *)getDataBase
{
    // 创建一个FMDatabase类的对象
    db = [[FMDatabase alloc] initWithPath:[self getDBPath]];
    return db;
}

// 数据库文件的路径
+ (NSString *)getDBPath
{
    NSString *path = [NSHomeDirectory() stringByAppendingPathComponent:@"Documents"];
    return [path stringByAppendingPathComponent:@"db.sqlite"];
}


@end
