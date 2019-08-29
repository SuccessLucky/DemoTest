//
//  RoomManagementVC.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RoomManagementVC : UIViewController

@property (strong, nonatomic) NSArray *arrFloor;
@property (assign, nonatomic) int iFloorID;
@property (strong, nonatomic) NSString *strFloorName;

@end
