//
//  ScreenModel.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/10.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface ScreenModel : NSObject

@property (assign, nonatomic) int iScreen_scene_id;
@property (assign, nonatomic) int iScreen_scene_type;
@property (strong, nonatomic) NSString *strScreen_name;
@property (strong, nonatomic) NSString *strScreen_image;

//(1byte) 子命令标识，表示参数配置命令
@property (strong, nonatomic) NSString *str_sub_command_identifer;

//(1byte) 场景/联动编号
@property (strong, nonatomic) NSString *str_serial_number;

//是否联动 1 联动 0 不联动
@property (assign, nonatomic) int iScreen_need_linkage;
//是否延时 1 是 0 否
@property (assign, nonatomic) int iScreen_need_time_delay;
//是否定时 1 是 0 否
@property (assign, nonatomic) int iScreen_need_timing;
//布防时有效 1 是 0 否
@property (assign, nonatomic) int iScreen_need_security_on;
//撤防时有效 1 是 0 否
@property (assign, nonatomic) int iScreen_need_security_off;
//延时时间 单位秒（s））
@property (strong, nonatomic) NSString *str_delay_time;
//定时时间 格式（HH:SS）
@property (strong, nonatomic) NSString *str_timing_time;
//1byte) 配置场景/联动属性；00表示初次设置/修改有效；01表示无效；FF表示删除
@property (strong, nonatomic) NSString *str_reserved_property;
//(1byte)01: 表示为强制联动，01 为强制联动;02 为非强制联动
@property (strong, nonatomic) NSString *str_force_linkage;

//(1byte)FF: 启用/禁用状态标志字节，01 为启用;02 为禁用;若强制联动为 FF
@property (strong, nonatomic) NSString *str_enabled_or_disable_identifer;

//(1byte) 布放/撤防联动使能标志字节，01 为布防有效，02 为撤防有效，若强制联动为 FF
@property (strong, nonatomic) NSString *str_arming_or_disarming_identifer;

//(8byte)发起联动设备 MAC 地址
@property (strong, nonatomic) NSString *str_linkage_device_mac_addr;

//(1byte) 01:数据比较通道为 01，01 代表 A 路;02 代表 B 路;03 代表 C 路
@property (strong, nonatomic) NSString *str_linkage_device_road;

//(1byte)  01:联动设备数据类型为 01，-Hex 型为 01;BCD 型为 02
@property (strong, nonatomic) NSString *str_linkage_device_data_type;

//(1byte) 01:联动设备数据和阈值数据比较类型为 01， 详见下表
@property (strong, nonatomic) NSString *str_linkage_device_data_range;

//(1byte) 00 18:联动时间段设置 0 点~24 点，在时间段内才可以进行联动，单位小时
@property (strong, nonatomic) NSString *str_linkage_time;

//(2byte) 时间为秒 (场景延时执行时间， 如4660s对应12 34)
@property (strong, nonatomic) NSString *str_linkage_delay_time;

@property (strong, nonatomic) NSArray *arrScreen_scene_details;

@end
