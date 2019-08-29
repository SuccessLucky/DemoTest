//
//  ModelSecurity.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/21.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ModelSecurity : NSObject

@property (strong, nonatomic) NSString *strIcon;
@property (strong, nonatomic) NSString *strName;
@property (strong, nonatomic) NSString *strImageArrow;
@property (strong, nonatomic) NSArray *arrDeviceList;
@property (strong, nonatomic) NSString *strType;
@property (assign, nonatomic) BOOL iShowDetail;

@property (assign, nonatomic) int iRedDotCount;

@end
