//
//  NetworkEngine+ScreenPack.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine.h"

NS_ASSUME_NONNULL_BEGIN

@interface NetworkEngine (ScreenPack)

/**
 场景
 
 @param strDataFieldAllLength ---------------------------------------->(1byte) 数据长度
 @param strCmdID ----------------------------------------------------->(1byte) 命令标识
 @param strNumber ---------------------------------------------------->(1byte) 命令对象
 @param strGatewayMacAddr -------------------------------------------->(8byte) 网关MAC地址
 @param strDataFieldLength ------------------------------------------->(1byte) 数据长度
 @param strSonOrderIdentifer ----------------------------------------->(1byte) 子命令标识，表示参数配置命令
 @param strName  ----------------------------------------------------->(20byte)场景/联动名称
 @param strProperty -------------------------------------------------->(1byte) 配置场景属性，01代表场景；02代表联动
 @param strSerialNumber ---------------------------------------------->(1byte) 场景/联动编号
 @param strCharacter ------------------------------------------------->(1byte) 配置场景/联动属性；00表示初次设置/修改有效；01表示无效；FF表示删除
 @param strBranches -------------------------------------------------->(1byte) 配置设备场景/联动控制指令的条数
 @param strExecutiveSerialNumber ------------------------------------->(1byte) 配置场景执行序号01--SCN=01代表01号场景——回家模式
 @param strTimingHour ------------------------------------------------>(1byte) 表示执行场景定时功能，XX小时
 @param strTimingMinute ---------------------------------------------->(1byte) 表示执行场景定时功能, XX分钟
 @param strTimeDelay ------------------------------------------------->(2byte) 时间为秒 (场景延时执行时间， 如4660s对应12 34)
 @return data
 */
- (NSData *)doHandleConfigureTheScreenOrderWithLength:(NSString *)strDataFieldAllLength
                                                cmdID:(NSString *)strCmdID
                                               number:(NSString *)strNumber
                                       gatewayMacAddr:(NSString *)strGatewayMacAddr
                                      dataFieldLength:(NSString *)strDataFieldLength
                                    sonOrderIdentifer:(NSString *)strSonOrderIdentifer
                                           screenName:(NSString *)strName
                                       screenProperty:(NSString *)strProperty
                            screenSettingSerialNumber:(NSString *)strSerialNumber
                                      screenCharacter:(NSString *)strCharacter
                                       screenBranches:(NSString *)strBranches
                          screenExecutiveSerialNumber:(NSString *)strExecutiveSerialNumber
                               screenScreenTimingHour:(NSString *)strTimingHour
                                   screenTimingMinute:(NSString *)strTimingMinute
                                      screenTimeDelay:(NSString *)strTimeDelay;

/**
 场景载入指令 或者 联动载入指令 不一样的地方在screenNO：一个为场景编号，一个为联动编号
 
 @param strWholeDataLength 数据长度
 @param strSonDataLength 子数据长度
 @param strControlFrameWriteIdentifer  02:子命令标识，表示控制帧载入命令
 @param strScreenNO  01:场景编号为 01，与配置命令相对应
 @param strInstructionCount  03:指令总数为 3 条
 @param strCurrentInstructionNO  01:当前计数值为 01，表示指令总数 3 条中的第一条
 @param dataInstruction 当前设备控制命令帧，详细信息见各设备操作指南
 @return data
 */
- (NSData *)doHandleWriteScreenOrderToGatewayWithDataLength:(NSString *)strWholeDataLength
                                              sonDataLength:(NSString *)strSonDataLength
                                 controlFrameWriteIdentifer:(NSString *)strControlFrameWriteIdentifer
                                                   screenNO:(NSString *)strScreenNO
                                           instructionCount:(NSString *)strInstructionCount
                                    currentInstructionWhich:(NSString *)strCurrentInstructionNO
                                          instructionDetail:(NSData *)dataInstruction;


/**
 场景触发指令 或 联动触发指令
 
 @param strScreenNO 场景编号
 @return data
 */
- (NSData *)doHandleSendScreenOrderToControlWithScreenNO:(NSString *)strScreenNO;

#pragma mark - 联动配置指令
/**
 联动配置指令
 
 @param strDataFieldAllLength ---------------------------------------->(1byte) 数据长度
 @param strCmdID ----------------------------------------------------->(1byte) 命令标识
 @param strNumber ---------------------------------------------------->(1byte) 命令对象
 @param strGatewayMacAddr -------------------------------------------->(8byte) 网关MAC地址
 @param strDataFieldLength ------------------------------------------->(1byte) 数据长度
 @param strSonOrderIdentifer ----------------------------------------->(1byte) 子命令标识，表示参数配置命令
 @param strName  ----------------------------------------------------->(20byte)场景/联动名称
 @param strProperty -------------------------------------------------->(1byte) 配置场景属性，01代表场景；02代表联动
 @param strSerialNumber ---------------------------------------------->(1byte) 场景/联动编号
 @param strCharacter ------------------------------------------------->(1byte) 配置场景/联动属性；00表示初次设置/修改有效；01表示无效；FF表示删除
 @param strBranches -------------------------------------------------->(1byte) 配置设备场景/联动控制指令的条数
 @param strForceLinkage 01: 表示为强制联动，01 为强制联动;02 为非强制联动
 @param strEnabledOrDisableIdentifer FF: 启用/禁用状态标志字节，01 为启用;02 为禁用;若强制联动为 FF
 @param strArmingOrDisarmingIdentifer 布放/撤防联动使能标志字节，01 为布防有效，02 为撤防有效，若强制联动为 FF
 @param strLinkageDeviceMacAddr 发起联动设备 MAC 地址
 @param strWhichRoad 01:数据比较通道为 01，01 代表 A 路;02 代表 B 路;03 代表 C 路
 @param strLinkageDeviceDataType 01:联动设备数据类型为 01，-Hex 型为 01;BCD 型为 02
 @param strLinkageDeviceDataRange 01:联动设备数据和阈值数据比较类型为 01， 详见下表
 @param strLinkageTime 00 18:联动时间段设置 0 点~24 点，在时间段内才可以进行联动，单位小时
 @param strThresholdLowLimit 00 00 00 01:阈值数据/阈值下限数据为 00 00 00 01
 @param strThresholdUpperLimit FF FF FF FF:阈值上限数据
 @param strLinkageDelayTime strTimeDelay （十进制）------------------------------------------------->(2byte) 时间为秒 (场景延时执行时间， 如4660s对应12 34)
 @return data
 */
- (NSData *)doHandleLinkageScreenOrderWithLength:(NSString *)strDataFieldAllLength
                                           cmdID:(NSString *)strCmdID
                                          number:(NSString *)strNumber
                                  gatewayMacAddr:(NSString *)strGatewayMacAddr
                                 dataFieldLength:(NSString *)strDataFieldLength
                               sonOrderIdentifer:(NSString *)strSonOrderIdentifer
                                      screenName:(NSString *)strName
                                  screenProperty:(NSString *)strProperty
                       screenSettingSerialNumber:(NSString *)strSerialNumber
                                 screenCharacter:(NSString *)strCharacter
                                  screenBranches:(NSString *)strBranches
                                    forceLinkage:(NSString *)strForceLinkage
                       enabledOrDisableIdentifer:(NSString *)strEnabledOrDisableIdentifer
                      armingOrDisarmingIdentifer:(NSString *)strArmingOrDisarmingIdentifer
                            linkageDeviceMacAddr:(NSString *)strLinkageDeviceMacAddr
                                       whichRoad:(NSString *)strWhichRoad
                           linkageDeviceDataType:(NSString *)strLinkageDeviceDataType
                          linkageDeviceDataRange:(NSString *)strLinkageDeviceDataRange
                                     linkageTime:(NSString *)strLinkageTime
                               thresholdLowLimit:(NSString *)strThresholdLowLimit
                             thresholdUpperLimit:(NSString *)strThresholdUpperLimit
                                linkageDelayTime:(NSString *)strLinkageDelayTime;

#pragma mark - 设防 布防
/**
 设防 布防
 
 @param strArmOrDisarmingIdentifer   01:设防类型为布防，01 为布防，02 为撤防
 @return data
 */
- (NSData *)doHandleSendArmingOrDisarmingOrderToControlWithArmOrDisarmingIdentifer:(NSString *)strArmOrDisarmingIdentifer;

#pragma mark - 场景联动删除命令
/**
 场景联动删除命令
 @param strScreenNO 01:删除 1 号场景/联动;02:删除 2 号场景/联动;FF:删除全部场景/联
 @return data
 */
- (NSData *)doHandleSendDleteScreenOrderToControlWithScreenNO:(NSString *)strScreenNO;

#pragma mark - 网关报警状态解除命令
/**
 网关报警状态解除命令
 
 @param strScreenNO 08:子命令标识，报警状态解除命令
 @return data
 */
- (NSData *)doHandleDisarmingGatewayOrderToControl;


#pragma mark -
#pragma mark - 获取场景存在网关的s个数
- (NSData *)doHandleGetScreenInGatewayCount;

@end

NS_ASSUME_NONNULL_END
