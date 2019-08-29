//
//  GAutoLock.m
//  GJJUser
//
//  Created by gjj on 15/1/13.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#import "GAutoLock.h"

//自动互斥锁
AutoLock::AutoLock(NSRecursiveLock* locker_) : locker(locker_)
{
    if (locker)
    {
        CFRetain(locker);
        [locker lock];
    }
}

AutoLock::~AutoLock()
{
    if (locker)
    {
        [locker unlock];
        CFRelease(locker);
    }
}