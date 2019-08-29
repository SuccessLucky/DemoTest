//
//  GetCityLocationInfo.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/10.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface LocationInfoManager : NSObject

@property (weak, nonatomic) NSString *strCityName;

+ (id)shareInstance;

- (void)doStartLocation;

@end
