//
//  AirConditionBtnModel.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/15.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AirConditionBtnModel : NSObject

@property (strong, nonatomic) NSString *strName;
@property (strong, nonatomic) NSString *strPic;
@property (assign, nonatomic) AirConditionBtnType type;

@end
