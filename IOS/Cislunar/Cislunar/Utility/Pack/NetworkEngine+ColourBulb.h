//
//  NetworkEngine+ColourBulb.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine.h"

NS_ASSUME_NONNULL_BEGIN

@interface NetworkEngine (ColourBulb)

//3.8 读取智能多彩灯设备参数 (OD:4010)
- (NSData *)doGetColourBulbParameterWithTargetAddr:(NSString *)strTargetAddr;


/**
 //3.8.1.1 调光
 
 @param strTargetAddr 多彩的灯泡
 @param lightingMode 灯光模式：
 控制模式   数值      控制类别        RGB/W 初始值       备注
 Null     0x00
 直接      0x01     调光/调色         无
 渐渐      0x02     调光/调色
 延时      0x03     调光/调色
 情景变色   0x04     无               无               预留
 生活模式   0x05     调色             240/224/208      可以根据 app 设定 的值调色
 阅读模式   0x06     调色             240/240/208
 夜起模式   0x07     调色             208/0/208
 炫彩模式   0x08     无               无               预留
 七彩渐变   0x09     无               红(255/0/0)->
 橙(255/128/0)->
 黄(255/255/0)->
 绿(0/255/0)->
 青(0/255/255)->
 蓝(0/0/255)->
 紫(255/0/255)    固定不可更改
 
 七彩跳变   0x0A     无
 呼吸灯     0x0B     调色             240/0/240        可以根据 app 设定 的值调色，默认变 化周期 5S
 定时变色   0x0C     无               无                预留
 乐动模式   0x0D     无               无                预留
 睡眠模式   0x0E     调色             0/240/0           可以根据 app 设定 的值调色
 
 @param lightingType 灯光类型
 @param strControlTime 控制时间 两个字节
 @param lightingSwitch 灯光开关
 @param brightnessControl 白光亮度值
 @return data
 */
- (NSData *)doGetColourBulbDimmerWithTargetAddr:(NSString *)strTargetAddr
                                   lightingMode:(NSString *)lightingMode
                                   lightingType:(NSString *)lightingType
                                    controlTime:(NSString *)strControlTime
                                 lightingSwitch:(NSString *)lightingSwitch
                              brightnessControl:(NSString *)brightnessControl;

//3.8.1.2 调色
- (NSData *)doGetColourBulbPaletteWithTargetAddr:(NSString *)strTargetAddr
                                    lightingMode:(NSString *)lightingMode
                                    lightingType:(NSString *)lightingType
                                     controlTime:(NSString *)strControlTime
                                  lightingSwitch:(NSString *)lightingSwitch
                                   redLightValue:(NSString *)redLightValue
                                 greenLightValue:(NSString *)greenLightValue
                                  blueLightValue:(NSString *)blueLightValue;

//3.9 读取智能多彩灯开关动作控制器定义(OD:6001)
- (NSData *)doGetColourBulbXXXWithTargetAddr:(NSString *)strTargetAddr;

#pragma mark -
#pragma mark -  临时使用彩色球泡灯一些模式命令
//3.8.11xxxxxxx 渐变
- (NSData *)doGetColourBulbAllKindsOfModelOneWithTargetAddr:(NSString *)strTargetAddr
                                               lightingMode:(NSString *)lightingMode;
//3.8.12xxxxxxx 七彩跳变
- (NSData *)doGetColourBulbAllKindsOfModelTwoWithTargetAddr:(NSString *)strTargetAddr
                                               lightingMode:(NSString *)lightingMode;
//3.8.13xxxxxxx 呼吸灯
- (NSData *)doGetColourBulbAllKindsOfModelThreeWithTargetAddr:(NSString *)strTargetAddr
                                                 lightingMode:(NSString *)lightingMode;




#pragma mark -
#pragma mark - 设置区域
- (NSData *)doSendSetAreaNOWithTargetAddr:(NSString *)strTargetAddr andAreaNo:(NSString *)strHex;

@end

NS_ASSUME_NONNULL_END
