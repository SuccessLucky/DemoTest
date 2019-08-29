//
//  SHRequestTimer.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/1/23.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHRequestTimer.h"

@interface SHRequestTimer ()

@property(nonatomic, retain)    NSTimer     *timer;
@property(nonatomic, assign)    int         timeout;
@property(nonatomic, assign)    BOOL        isDelayReq;     // 是否延迟过，如果延迟过就不要再延迟了
@property(nonatomic, assign)    BOOL        hasRestart;     // 是否重启过(超时重试的时候会重启)

@end

@implementation SHRequestTimer

- (void)dealloc
{
    [self stop];
}

- (void)stop
{
    if (_timer) {
        [_timer invalidate];
        self.timer = nil;
    }
}

- (void)start:(int)time target:(id)target sel:(SEL)selecotr
{
    [self stop];
    
    _timeout = time;
    _timer = [NSTimer scheduledTimerWithTimeInterval:_timeout
                                              target:target
                                            selector:selecotr
                                            userInfo:nil
                                             repeats:NO];
    [[NSRunLoop currentRunLoop] addTimer:_timer forMode:NSDefaultRunLoopMode];
}

- (void)updateTimer
{
    if (_isDelayReq)    // 已经延时过，就不再延时了
        return;
    if (![_timer isValid])   // timer是否是有效的
        return;
    
    // 获取超时时间点
    NSDate *date = [_timer fireDate];
    
    NSTimeInterval interval = [date timeIntervalSinceNow];
    if (interval >= _timeout / 2) {
        return;
    }
    
    // WNSLOG_INFO(@"openSession成功，请求cmd=%@ (seqNo=%d)还有%.2f秒就要超时，延长30秒", self.command, self.seqNo, interval);
    _isDelayReq = YES;
    NSDate *newDate = [date dateByAddingTimeInterval:(self.timeout/2)];
    [_timer setFireDate:newDate];
}

- (BOOL)checkHasRestart
{
    return _hasRestart;
}

- (void)restart:(int)time target:(id)target sel:(SEL)selecotr
{
    if (_hasRestart)
    {
        return;
    }
    
    _hasRestart = YES;
    
    [self start:time target:target sel:selecotr];
    
}


@end
