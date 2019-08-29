//
//  SHModelDevice.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/1/15.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHModelDevice.h"

@implementation SHModelDevice

- (NSArray *)arrBtns
{
    if (!_arrBtns) {
        _arrBtns = [NSArray new];
    }
    return _arrBtns;
}

@end
