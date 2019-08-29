//
//  AlarmModel.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/11.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SHAlarmModel : NSObject

@property (assign, nonatomic) int iAlarm_alarm_id;
@property (strong, nonatomic) NSString *strAlarm_create_date;
@property (strong, nonatomic) NSString *strAlarm_alarm_msg;

@end
