//
//  SHInfraredDeviceModel.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/14.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SHInfraredDeviceModel : NSObject

@property (strong, nonatomic) NSString *strName;
@property (strong, nonatomic) NSString *strType;
@property (strong, nonatomic) NSString *strId;
@property (strong, nonatomic) NSArray *arrKeys;

@end
