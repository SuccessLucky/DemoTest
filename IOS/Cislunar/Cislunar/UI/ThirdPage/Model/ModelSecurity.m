//
//  ModelSecurity.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/21.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "ModelSecurity.h"

@implementation ModelSecurity

-(NSArray *)arrDeviceList
{
    if (!_arrDeviceList) {
        _arrDeviceList = [NSArray new];
    }
    return _arrDeviceList;
}

- (BOOL)iShowDetail
{
    if (!_iShowDetail) {
        _iShowDetail = 0;
    }
    return _iShowDetail;
}

@end
