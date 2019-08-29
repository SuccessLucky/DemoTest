//
//  LockMemModel.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/16.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SHLockMemModel : NSObject

@property (assign, nonatomic) int iDeviceID;
@property (assign, nonatomic) int iLockID;
@property (strong, nonatomic) NSString *strLock_user_name;
@property (strong, nonatomic) NSString *strLock_fingerprintId;
@property (assign, nonatomic) int iUnlock_times;

@end
