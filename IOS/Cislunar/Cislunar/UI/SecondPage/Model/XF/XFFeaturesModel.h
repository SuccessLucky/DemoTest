//
//  XFFeaturesModel.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2018/8/5.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface XFFeaturesModel : NSObject

@property (strong, nonatomic)NSString *strXFFeatureCode;
@property (strong, nonatomic)NSString *strXFSwitchState; //开关
@property (strong, nonatomic)NSString *strXFHeatingState; //点解热状态开关
@property (strong, nonatomic)NSString *strXFWorkMode; //工作模式

@property (strong, nonatomic)NSString *strXFCircleMode; //循环模式
@property (strong, nonatomic)NSString *strXFReturnAirProportion; //进回风比例
@property (strong, nonatomic)NSString *strXFWindSpeed; //风速  风速为第5档
@property (strong, nonatomic)NSString *strXFAirVolume; //风量 风量为40立方每小时风量为40立方每小时

@property (strong, nonatomic)NSString *strXFParticulates; //颗粒物含量为26ppm
@property (strong, nonatomic)NSString *strXFCO2Volume;    //CO2含量222
@property (strong, nonatomic)NSString *strXFInWindTempreture;   //进风温度
@property (strong, nonatomic)NSString *strXFOutWindTempreture;  //回风温度

@property (strong, nonatomic)NSString *strXFErrorCode; //故障码
@property (strong, nonatomic)NSString *strXFChuLvPer;  //初滤百分比
@property (strong, nonatomic)NSString *strXFJiChenXiangPer; //集尘箱剩余百分比
@property (strong, nonatomic)NSString *strXFGaoLvPer; //高滤剩余百分比

@end
