//
//  GAutoLock.h
//  GJJUser
//
//  Created by gjj on 15/1/13.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#import <Foundation/Foundation.h>

//自动互斥锁
class AutoLock
{
public:
    AutoLock(NSRecursiveLock* locker_);
    ~AutoLock();
private:
    NSRecursiveLock * locker;
};
