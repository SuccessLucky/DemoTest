//
//  ScreenModel.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/10.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "ScreenModel.h"

@implementation ScreenModel

-(NSArray *)arrScreen_scene_details
{
    if (!_arrScreen_scene_details) {
        _arrScreen_scene_details = [NSArray new];
    }
    return _arrScreen_scene_details;
}

@end
