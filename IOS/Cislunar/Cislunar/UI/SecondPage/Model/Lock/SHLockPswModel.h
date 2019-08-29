//
//  SHLockPswModel.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/30.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SHLockPswModel : NSObject

@property (assign, nonatomic) int iDeviceID;
@property (assign, nonatomic) int iLockID;
@property (strong, nonatomic) NSString * strUnlockPsw;
@property (assign, nonatomic) int iUnLockTimes;

@end
