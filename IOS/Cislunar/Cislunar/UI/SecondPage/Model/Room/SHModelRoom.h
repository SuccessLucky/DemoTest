//
//  SHModelRoom.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/1/15.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SHModelRoom : NSObject

@property (assign, nonatomic) int iRoom_id;
@property (strong, nonatomic) NSString *strRoom_image;
@property (strong, nonatomic) NSString *strRoom_name;
@property (assign, nonatomic) int iRoom_floor_id;
@property (strong, nonatomic) NSArray *arrDeviceList;
@property (strong, nonatomic) NSString *strFloorName;


@end
