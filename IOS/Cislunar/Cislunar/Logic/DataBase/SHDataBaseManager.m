//
//  SHDataBaseManager.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/25.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHDataBaseManager.h"

/**
 *  数据库版本，当前表有更新过字段就要升级
 *  升级逻辑 每次 +1
 */
#define kDBVersion 1

@interface SHDataBaseManager ()
{
    FMDatabase *_dataBase;
    NSString *_dbPath;
    dispatch_queue_t _dbQueue;
}
@property (nonatomic,strong)  FMDatabaseQueue *queueData;

@end

@implementation SHDataBaseManager

+(instancetype)sharedInstance
{
    static SHDataBaseManager *dataBase = nil;
    static dispatch_once_t onceToken;
    
    dispatch_once(&onceToken, ^{
        dataBase = [[SHDataBaseManager alloc] init];
    });
    
    return dataBase;
}


- (instancetype)init
{
    self = [super init];
    if (self) {
        [self disPoseDB];
        _dbQueue = dispatch_queue_create("com.cmw.SmartHouseYCT", DISPATCH_QUEUE_SERIAL);
    }
    return self;
}

#pragma mark - 建库
//处理数据库
- (void)disPoseDB
{
    NSNumber *dbVersion = [[NSUserDefaults standardUserDefaults] objectForKey:@"dbVersion"];
    //配置文件里的dbVersion和当前应用的版本不一致就删除旧的数据库再创建新的
    if ([dbVersion intValue] != kDBVersion) {
        NSError *error = [self deleteDB];
        if (error) {
            NSLog(@"删除数据库失败");
        }
    }
    [self creatDB];
}

- (NSError *)deleteDB
{
    NSFileManager *fm = [NSFileManager defaultManager];
    NSString *dataPath = [[UICommon getContents]stringByAppendingPathComponent:@"CMW.db"];
    NSError *error = nil;
    [fm removeItemAtPath:dataPath error:&error];
    return error;
}

//写入数据库版本信息
- (void)writeDataBaseVersion
{
    [[NSUserDefaults standardUserDefaults] setObject:@(kDBVersion) forKey:@"dbVersion"];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

//创建数据库
- (void)creatDB
{
    //构造数据库路径
    _dbPath = [[UICommon getContents]stringByAppendingPathComponent:@"CMW.db"];
    NSLog(@"数据库目录%@",_dbPath);
    NSFileManager *fm = [NSFileManager defaultManager];
    //创建数据库
    _dataBase = [FMDatabase databaseWithPath:_dbPath];
    self.queueData = [FMDatabaseQueue databaseQueueWithPath:_dbPath];
    BOOL path = [fm fileExistsAtPath:_dbPath];
    
    if (path) {
        [self writeDataBaseVersion];
    }
}

- (BOOL)openDataBase
{
    if ([_dataBase open]) {
        return YES;
    } else {
        return NO;
    }
}

#pragma mark - 建表
- (void)createCommonTable
{
    if (![self openDataBase]) {
        NSLog(@"打开数据库失败");
    }
    NSString *cacheTable = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS '%@' ('%@' INTEGER PRIMARY KEY AUTOINCREMENT, '%@' TEXT,'%@' TEXT,'%@' TEXT,'%@' TEXT)",
                            Table_Cache,
                            Cache_ID,
                            Cache_UserAccount,
                            Cache_GatewayMacAddr,
                            Cache_Identifer,
                            Cache_JsonStr];
    NSString *cacheTableRoom = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS '%@' ('%@' INTEGER PRIMARY KEY AUTOINCREMENT,'%@' INTEGER,'%@' TEXT)",
                                Table_Cache_Room,
                                Cache_ID,
                                Table_Cache_Room_FloorID,
                                Cache_JsonStr
                                ];
    
    NSString *cacheTableDevice = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS '%@' ('%@' INTEGER PRIMARY KEY AUTOINCREMENT,'%@' INTEGER,'%@' TEXT)",
                                Table_Cache_Device,
                                Cache_ID,
                                Table_Cache_Device_RoomID,
                                Cache_JsonStr
                                ];
    
    NSString *cacheTableMemberControlList = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS '%@' ('%@' INTEGER PRIMARY KEY AUTOINCREMENT,'%@' INTEGER,'%@' TEXT)",
                                  Table_Cache_MemberControlList,
                                  Cache_ID,
                                  Table_Cache_MemberControlList_ID,
                                  Cache_JsonStr
                                  ];
    
    NSString *cacheTableLock = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS '%@' ('%@' INTEGER PRIMARY KEY AUTOINCREMENT,'%@' INTEGER,'%@' TEXT)",
                                  Table_Cache_Lock,
                                  Cache_ID,
                                  Table_Cache_Lock_DeviceID,
                                  Cache_JsonStr
                                  ];
    
    NSString *cacheTableLockMemberList = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS '%@' ('%@' INTEGER PRIMARY KEY AUTOINCREMENT,'%@' TEXT,'%@' TEXT)",
                                Table_Cache_LockMemberList,
                                Cache_ID,
                                Table_Cache_LockMemberList_Addr,
                                Cache_JsonStr
                                ];
    
    NSArray *dataArray = @[cacheTable,cacheTableRoom,cacheTableDevice,cacheTableMemberControlList,cacheTableLock,cacheTableLockMemberList];
    [self.queueData inDatabase:^(FMDatabase *db) {
        for (NSString *str in dataArray) {
            if (![db executeUpdate:str]) {
                LLog_Error([NSString stringWithFormat:@"创建%@表失败",str]);
            } else {
                LLog([NSString stringWithFormat:@"创建%@表成功",str]);
            }
        }
    }];
}

#pragma mark -
#pragma mark - 插入操作
//普通
- (void)doHandleInsertDataWithIdentifer:(NSString *)strIdentifer jsonStr:(NSString *)strJsonStr
{
    NSString *strUserAccount = [[SHLoginManager shareInstance] doGetRememberAccount];
    NSString *strZigbeeMacAddress = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    dispatch_async(_dbQueue, ^{
    
        if (![self openDataBase]) {
            NSLog(@"打开数据库失败");
        } else {
            NSString *insterInto = [NSString stringWithFormat:
                                    @"INSERT INTO '%@' ('%@','%@','%@','%@') VALUES ('%@','%@','%@','%@')",
                                    Table_Cache,
                                    Cache_UserAccount,
                                    Cache_GatewayMacAddr,
                                    Cache_Identifer,
                                    Cache_JsonStr,
                                    strUserAccount,
                                    strZigbeeMacAddress,
                                    strIdentifer,
                                    strJsonStr];
            [self.queueData inTransaction:^(FMDatabase *db, BOOL *rollback) {
                
                if ([db executeUpdate:insterInto]) {
                    NSLog(@" 插入成功");
                } else {
                    NSLog(@" 插入失败");
                }
            }];
        }
    });
}

//房间插入操作
- (void)doHandleInsertRoomListDataWithFloorID:(int)iFloorID jsonStr:(NSString *)strJsonStr
{
    dispatch_async(_dbQueue, ^{
        
        if (![self openDataBase]) {
            NSLog(@"打开数据库失败");
        } else {
            NSString *insterInto = [NSString stringWithFormat:
                                    @"INSERT INTO '%@' ('%@','%@') VALUES ('%d','%@')",
                                    Table_Cache_Room,
                                    Table_Cache_Room_FloorID,
                                    Cache_JsonStr,
                                    iFloorID,
                                    strJsonStr];
            [self.queueData inTransaction:^(FMDatabase *db, BOOL *rollback) {
                
                if ([db executeUpdate:insterInto]) {
                    NSLog(@" 房间插入成功");
                } else {
                    NSLog(@" 房间插入失败");
                }
            }];
        }
    });
}


//根据RoomID存储拉下来的设备列表插入操作
- (void)doHandleInsertDeviceDataWithRoomID:(int)iRoomID jsonStr:(NSString *)strJsonStr
{
    dispatch_async(_dbQueue, ^{
        
        if (![self openDataBase]) {
            NSLog(@"打开数据库失败");
        } else {
            NSString *insterInto = [NSString stringWithFormat:
                                    @"INSERT INTO '%@' ('%@','%@') VALUES ('%d','%@')",
                                    Table_Cache_Device,
                                    Table_Cache_Device_RoomID,
                                    Cache_JsonStr,
                                    iRoomID,
                                    strJsonStr];
            [self.queueData inTransaction:^(FMDatabase *db, BOOL *rollback) {
                
                if ([db executeUpdate:insterInto]) {
                    NSLog(@" 设备插入成功");
                } else {
                    NSLog(@" 设备插入失败");
                }
            }];
        }
    });
}


//根据MemebrID存储拉下来的设备列表插入操作
- (void)doHandleInsertMemberControlListDataWithMemberID:(int)iMemberID jsonStr:(NSString *)strJsonStr
{
    dispatch_async(_dbQueue, ^{
        
        if (![self openDataBase]) {
            NSLog(@"打开数据库失败");
        } else {
            NSString *insterInto = [NSString stringWithFormat:
                                    @"INSERT INTO '%@' ('%@','%@') VALUES ('%d','%@')",
                                    Table_Cache_MemberControlList,
                                    Table_Cache_MemberControlList_ID,
                                    Cache_JsonStr,
                                    iMemberID,
                                    strJsonStr];
            [self.queueData inTransaction:^(FMDatabase *db, BOOL *rollback) {
                
                if ([db executeUpdate:insterInto]) {
                    NSLog(@" 子账号管理设备插入成功");
                } else {
                    NSLog(@" 子账号管理设备插入失败");
                }
            }];
        }
    });
}

//根据DeviceID存储拉下来的门锁用户列表插入操作
- (void)doHandleInsertLockMemDataWithDeviceID:(int)iDeviceID jsonStr:(NSString *)strJsonStr
{
    dispatch_async(_dbQueue, ^{
        
        if (![self openDataBase]) {
            NSLog(@"打开数据库失败");
        } else {
            NSString *insterInto = [NSString stringWithFormat:
                                    @"INSERT INTO '%@' ('%@','%@') VALUES ('%d','%@')",
                                    Table_Cache_Lock,
                                    Table_Cache_Lock_DeviceID,
                                    Cache_JsonStr,
                                    iDeviceID,
                                    strJsonStr];
            [self.queueData inTransaction:^(FMDatabase *db, BOOL *rollback) {
                
                if ([db executeUpdate:insterInto]) {
                    NSLog(@" lock用户插入成功");
                } else {
                    NSLog(@" lock用户插入失败");
                }
            }];
        }
    });
}

//根据DeviceMacAddr存储拉下来的门锁用户列表插入操作
- (void)doHandleInsertLockMemDataWithDeviceMacAddr:(NSString *)strDeviceMacAddr jsonStr:(NSString *)strJsonStr
{
    dispatch_async(_dbQueue, ^{
        
        if (![self openDataBase]) {
            NSLog(@"打开数据库失败");
        } else {
            NSString *insterInto = [NSString stringWithFormat:
                                    @"INSERT INTO '%@' ('%@','%@') VALUES ('%@','%@')",
                                    Table_Cache_LockMemberList,
                                    Table_Cache_LockMemberList_Addr,
                                    Cache_JsonStr,
                                    strDeviceMacAddr,
                                    strJsonStr];
            [self.queueData inTransaction:^(FMDatabase *db, BOOL *rollback) {
                
                if ([db executeUpdate:insterInto]) {
                    NSLog(@" lock用户插入成功");
                } else {
                    NSLog(@" lock用户插入失败");
                }
            }];
        }
    });
}

#pragma mark -
#pragma mark - 查询操作
//普通查询
- (NSString *)doQueryDataWithIdentifer:(NSString *)identifer
{
    NSString *strUserAccount = [[SHLoginManager shareInstance] doGetRememberAccount];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    NSMutableArray *mutArr = [NSMutableArray array];
    NSString *selectSQL = [NSString stringWithFormat:@"SELECT * FROM %@ WHERE %@ = '%@' AND %@ = '%@' AND %@ = '%@'",Table_Cache,Cache_UserAccount,strUserAccount,Cache_GatewayMacAddr,strZigbeeMacAddr,Cache_Identifer,identifer];
    FMResultSet *resultSet = [_dataBase executeQuery:selectSQL];
    [self.queueData inDatabase:^(FMDatabase *db) {
        while ([resultSet next]) {
            SHCacheModel *model = [SHCacheModel new];
            model.strUserAccount = [resultSet stringForColumn:Cache_UserAccount];
            model.strZigbeeMacAddress = [resultSet stringForColumn:Cache_GatewayMacAddr];
            model.strIdentifer = [resultSet stringForColumn:Cache_Identifer];
            model.strJsonStr = [resultSet stringForColumn:Cache_JsonStr];
            [mutArr addObject:model];
        }
    }];
    SHCacheModel *modelJsonStr = [mutArr firstObject];
    return modelJsonStr.strJsonStr;
}

//根据FloorID查询Room表操作
- (NSString *)doQueryRoomListDataWithFloorID:(int)iFloorID
{
    NSMutableArray *mutArr = [NSMutableArray array];
    NSString *selectSQL = [NSString stringWithFormat:@"SELECT * FROM %@ WHERE %@ = '%d'",Table_Cache_Room,Table_Cache_Room_FloorID,iFloorID];
    FMResultSet *resultSet = [_dataBase executeQuery:selectSQL];
    [self.queueData inDatabase:^(FMDatabase *db) {
        while ([resultSet next]) {
            SHCacheModel *model = [SHCacheModel new];
            model.strJsonStr = [resultSet stringForColumn:Cache_JsonStr];
            [mutArr addObject:model];
        }
    }];
    SHCacheModel *modelJsonStr = [mutArr firstObject];
    return modelJsonStr.strJsonStr;
}


//根据RoomID查询Device表操作
- (NSString *)doQueryDevieListDataWithRoomID:(int)iRoomID
{
    NSMutableArray *mutArr = [NSMutableArray array];
    NSString *selectSQL = [NSString stringWithFormat:@"SELECT * FROM %@ WHERE %@ = '%d'",Table_Cache_Device,Table_Cache_Device_RoomID,iRoomID];
    FMResultSet *resultSet = [_dataBase executeQuery:selectSQL];
    [self.queueData inDatabase:^(FMDatabase *db) {
        while ([resultSet next]) {
            SHCacheModel *model = [SHCacheModel new];
            model.strJsonStr = [resultSet stringForColumn:Cache_JsonStr];
            [mutArr addObject:model];
        }
    }];
    SHCacheModel *modelJsonStr = [mutArr firstObject];
    return modelJsonStr.strJsonStr;
}

//根据memberID进行查询
- (NSString *)doQueryMemberControlListDataWithMemberID:(int)iMemberID
{
    NSMutableArray *mutArr = [NSMutableArray array];
    NSString *selectSQL = [NSString stringWithFormat:@"SELECT * FROM %@ WHERE %@ = '%d'",Table_Cache_MemberControlList,Table_Cache_MemberControlList_ID,iMemberID];
    FMResultSet *resultSet = [_dataBase executeQuery:selectSQL];
    [self.queueData inDatabase:^(FMDatabase *db) {
        while ([resultSet next]) {
            SHCacheModel *model = [SHCacheModel new];
            model.strJsonStr = [resultSet stringForColumn:Cache_JsonStr];
            [mutArr addObject:model];
        }
    }];
    SHCacheModel *modelJsonStr = [mutArr firstObject];
    return modelJsonStr.strJsonStr;
}

//根据DeviceID查询LockMemberList表操作
- (NSString *)doQueryLockMemberListDataWithDeviceID:(int)iDviceID
{
    NSMutableArray *mutArr = [NSMutableArray array];
    NSString *selectSQL = [NSString stringWithFormat:@"SELECT * FROM %@ WHERE %@ = '%d'",Table_Cache_Lock,Table_Cache_Lock_DeviceID,iDviceID];
    FMResultSet *resultSet = [_dataBase executeQuery:selectSQL];
    [self.queueData inDatabase:^(FMDatabase *db) {
        while ([resultSet next]) {
            SHCacheModel *model = [SHCacheModel new];
            model.strJsonStr = [resultSet stringForColumn:Cache_JsonStr];
            [mutArr addObject:model];
        }
    }];
    SHCacheModel *modelJsonStr = [mutArr firstObject];
    return modelJsonStr.strJsonStr;
}

//根据DeviceMacAddr查询LockMemberList表操作
- (NSString *)doQueryLockMemberListDataWithDeviceMacAddr:(NSString *)strDeviceMacAddr
{
    NSMutableArray *mutArr = [NSMutableArray array];
    NSString *selectSQL = [NSString stringWithFormat:@"SELECT * FROM %@ WHERE %@ = '%@'",Table_Cache_LockMemberList,Table_Cache_LockMemberList_Addr,strDeviceMacAddr];
    FMResultSet *resultSet = [_dataBase executeQuery:selectSQL];
    [self.queueData inDatabase:^(FMDatabase *db) {
        while ([resultSet next]) {
            SHCacheModel *model = [SHCacheModel new];
            model.strJsonStr = [resultSet stringForColumn:Cache_JsonStr];
            [mutArr addObject:model];
        }
    }];
    SHCacheModel *modelJsonStr = [mutArr firstObject];
    return modelJsonStr.strJsonStr;
}


#pragma mark -
#pragma mark - 删除操作
//普通
- (void)doDeleteDataWithIdentifer:(NSString *)identifer
{
    NSString *strUserAccount = [[SHLoginManager shareInstance] doGetRememberAccount];
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    
    dispatch_async(_dbQueue, ^{
    
        NSString *deleteSQL = [NSString stringWithFormat:@"DELETE FROM %@ WHERE %@ = '%@' AND %@ = '%@' AND %@ = '%@'",Table_Cache,Cache_UserAccount,strUserAccount,Cache_GatewayMacAddr,strZigbeeMacAddr,Cache_Identifer,identifer];
        [self.queueData inTransaction:^(FMDatabase *db, BOOL *rollback) {
            if ([db executeUpdate:deleteSQL]) {
                NSLog(@"identifer = %@ 删除成功",identifer);
            } else {
                NSLog(@"identifer = %@ 删除失败",identifer);
            }
        }];
    
        
    });
}

//根据floor_id删除room表操作
- (void)doDeleteRoomListDataWithFloorID:(int)iFloorID
{
    dispatch_async(_dbQueue, ^{
        
        NSString *deleteSQL = [NSString stringWithFormat:@"DELETE FROM %@ WHERE %@ = '%d'",Table_Cache_Room,Table_Cache_Room_FloorID,iFloorID];
        [self.queueData inTransaction:^(FMDatabase *db, BOOL *rollback) {
            if ([db executeUpdate:deleteSQL]) {
                NSLog(@"identifer = %d 删除成功",iFloorID);
            } else {
                NSLog(@"identifer = %d 删除失败",iFloorID);
            }
        }];
    });
}

//根据room_id删除device表操作
- (void)doDeleteDeviceListDataWithRoomID:(int)iRoomID
{
    dispatch_async(_dbQueue, ^{
        
        NSString *deleteSQL = [NSString stringWithFormat:@"DELETE FROM %@ WHERE %@ = '%d'",Table_Cache_Device,Table_Cache_Device_RoomID,iRoomID];
        [self.queueData inTransaction:^(FMDatabase *db, BOOL *rollback) {
            if ([db executeUpdate:deleteSQL]) {
                NSLog(@"identifer = %d 删除成功",iRoomID);
            } else {
                NSLog(@"identifer = %d 删除失败",iRoomID);
            }
        }];
    });
}

//根据memberiD删除可控制值列表的操作
- (void)doDeleteMemberControlListDataWithMemberID:(int)iMemberID
{
    dispatch_async(_dbQueue, ^{
        
        NSString *deleteSQL = [NSString stringWithFormat:@"DELETE FROM %@ WHERE %@ = '%d'",Table_Cache_MemberControlList,Table_Cache_MemberControlList_ID,iMemberID];
        [self.queueData inTransaction:^(FMDatabase *db, BOOL *rollback) {
            if ([db executeUpdate:deleteSQL]) {
                NSLog(@"identifer = %d 删除成功",iMemberID);
            } else {
                NSLog(@"identifer = %d 删除失败",iMemberID);
            }
        }];
    });
}


//根据DeviceID删除LockMemberList表操作
- (void)doDeleteLockMemberListDataWithDeviceID:(int)iDeviceID
{
    dispatch_async(_dbQueue, ^{
        
        NSString *deleteSQL = [NSString stringWithFormat:@"DELETE FROM %@ WHERE %@ = '%d'",Table_Cache_Lock,Table_Cache_Lock_DeviceID,iDeviceID];
        [self.queueData inTransaction:^(FMDatabase *db, BOOL *rollback) {
            if ([db executeUpdate:deleteSQL]) {
                NSLog(@"identifer = %d锁用户表 删除成功",iDeviceID);
            } else {
                NSLog(@"identifer = %d锁用户表 删除失败",iDeviceID);
            }
        }];
    });
}

//根据DeviceID删除LockMemberList表操作
- (void)doDeleteLockMemberListDataWithDeviceMacAddr:(NSString *)strDeviceMacAddr
{
    dispatch_async(_dbQueue, ^{
        
        NSString *deleteSQL = [NSString stringWithFormat:@"DELETE FROM %@ WHERE %@ = '%@'",Table_Cache_LockMemberList,Table_Cache_LockMemberList_Addr,strDeviceMacAddr];
        [self.queueData inTransaction:^(FMDatabase *db, BOOL *rollback) {
            if ([db executeUpdate:deleteSQL]) {
                NSLog(@"identifer = %@锁用户表 删除成功",strDeviceMacAddr);
            } else {
                NSLog(@"identifer = %@锁用户表 删除失败",strDeviceMacAddr);
            }
        }];
    });
}




#pragma mark - 删除操作
- (void)doDeleteTable:(NSString *)tableName complete:(BlockDidDeleteTableComplete)complete
{
    NSString *sqlString = [ NSString stringWithFormat:@"DELETE FROM %@",tableName];
    [self.queueData inTransaction:^(FMDatabase *db, BOOL *rollback) {
        if ([db executeUpdate:sqlString]) {
            
            if (complete) {
                complete(YES);
            }
            LLog([NSString stringWithFormat:@"%@  删除内容成功",tableName]);
        } else {
            if (complete) {
                complete(NO);
            }
            LLog_Error([NSString stringWithFormat:@"%@ 删除内容失败",tableName]);
        }
    }];
}




@end
