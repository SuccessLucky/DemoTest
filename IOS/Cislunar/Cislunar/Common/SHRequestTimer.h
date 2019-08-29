//
//  SHRequestTimer.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/1/23.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SHRequestTimer : NSObject

// 启停timer
- (void)start:(int)time target:(id)target sel:(SEL)selecotr;
- (void)stop;

// 更新timer逻辑
- (void)updateTimer;

// 重开一次timer
- (void)restart:(int)time target:(id)target sel:(SEL)selecotr;
- (BOOL)checkHasRestart;

@end
