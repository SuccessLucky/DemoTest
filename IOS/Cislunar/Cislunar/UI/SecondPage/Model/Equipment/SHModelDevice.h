//
//  SHModelDevice.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/1/15.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SHModelDevice : NSObject

@property (assign, nonatomic) int iDevice_device_id;
@property (assign, nonatomic) int iDevice_room_id;
@property (strong, nonatomic) NSString *strDevice_room_name;
@property (strong, nonatomic) NSString *strDevice_device_name;
@property (strong, nonatomic) NSString *strDevice_image;

@property (strong, nonatomic) NSString *strDevice_device_OD;
@property (strong, nonatomic) NSString *strDevice_device_type;
@property (strong, nonatomic) NSString *strDevice_category;
@property (strong, nonatomic) NSString *strDevice_sindex;
@property (strong, nonatomic) NSString *strDevice_sindex_length;

@property (strong, nonatomic) NSString *strDevice_mac_address;
@property (strong, nonatomic) NSString *strDevice_other_status;
@property (assign, nonatomic) int iDevice_device_state1;
@property (assign, nonatomic) int iDevice_device_state2;

@property (assign, nonatomic) int iDevice_device_state3;
@property (assign, nonatomic) int iDevice_floor_id;
@property (strong, nonatomic) NSString *strDevice_floor_name;
@property (strong, nonatomic) NSString *strDevice_alarm_status;

@property (strong, nonatomic) NSArray *arrBtns;
@property (strong, nonatomic) NSString *strDevice_cmdId;

@property (strong, nonatomic) NSString *strDevice_lockID;
@property (assign, nonatomic) int iRemberCount;
@property (assign, nonatomic) int iScreenDetailId;

//指纹锁
@property (strong, nonatomic) NSString *strFingerID;

//小蛮腰指纹锁
//@property (strong, nonatomic) NSString *strOpenXMYLockReport_0002_ID;
@property (strong, nonatomic) NSString *strOpenXMYLockReport_0002_IDType;

//计量插座
@property (strong, nonatomic) NSString *strDCVReal;
@property (strong, nonatomic) NSString *strCurrentReal;
@property (strong, nonatomic) NSString *strUsefulPowerReal;
@property (strong, nonatomic) NSString *strElectricQuantityReal;


//for 多彩球泡灯
@property (strong, nonatomic) NSArray *arrOrder;

//新风系统



@property (strong, nonatomic) NSDictionary *dictXF;


@end
