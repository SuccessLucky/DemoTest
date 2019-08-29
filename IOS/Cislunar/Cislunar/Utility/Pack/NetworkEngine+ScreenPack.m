//
//  NetworkEngine+ScreenPack.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine+ScreenPack.h"

@implementation NetworkEngine (ScreenPack)

#pragma mark - 场景配置帧指令
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
                                      screenTimeDelay:(NSString *)strTimeDelay
{
    //数据长度
    NSData *dataAllLength = [self dataWithHexString:strDataFieldAllLength];
    unsigned char *pAllLength = (unsigned char *)[dataAllLength bytes];
    
    /*
     0x01: Read_OD命令
     0x81: Read _OD命令执行错误
     0x02: Write_OD命令
     0x82: Write_OD命令执行错误
     0x05: 声明集中器命令也就是广告（Advertisement）命令
     0x21: Coordinator或手持设备立即发送Advertisement命令
     0x07: 转发命令
     0x08: 分包转发命令
     0x09: 主动上报设备的数据格式
     0x22: 复位命令命令
     0x24: 恢复参数缺省值命令
     */
    NSData *dataCmdID = [self dataWithHexString:strCmdID];
    unsigned char *pCmdID = (unsigned char *)[dataCmdID bytes];
    
    /*
     0x00: 针对节点自身，数据域中不包含目标地址，一般是指针对网关的相关操作。
     0x01: 针对其他节点，数据域中包含源地址和目标地址，源地址可用“00 00 00 00 00 00 00 00”替代。
     0xFF: 针对网络内所有节点，数据域中不包含目标地址
     */
    NSData *dataNumber = [self dataWithHexString:strNumber];
    unsigned char *pNumber = (unsigned char *)[dataNumber bytes];
    
    //网关的macAddr
    NSData *dataGatewayMacAddr = [self dataWithHexString:strGatewayMacAddr];
    unsigned char *pGatewayMacAddr = (unsigned char *)[dataGatewayMacAddr bytes];
    
    //分数据域长度
    NSData *dataFieldLength = [self dataWithHexString:strDataFieldLength];
    unsigned char *pFieldLength = (unsigned char *)[dataFieldLength bytes];
    
    //子命令标识 01:表示参数配置命令
    NSData *dataSonOrderIdentifer = [self dataWithHexString:strSonOrderIdentifer];
    unsigned char *pSonOrderIdentifer = (unsigned char *)[dataSonOrderIdentifer bytes];
    
    //场景的名字
//    NSString *strScreenNameHex = [[ToolHexManager sharedManager] convertStringToHexStr:strName];
    NSData *dataName = [self dataWithHexString:@"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00"];
    unsigned char *pName = (unsigned char *)[dataName bytes];
    
    //配置场景属性，01代表场景；02代表联动
    NSData *dataProperty = [self dataWithHexString:strProperty];
    unsigned char *pProperty = (unsigned char *)[dataProperty bytes];
    
    //配置场景编号
    NSData *dataSerialNumber = [self dataWithHexString:strSerialNumber];
    unsigned char *pSerialNumber = (unsigned char *)[dataSerialNumber bytes];
    
    //配置场景特性:00表示初次设置/修改有效；01表示无效；FF表示删除
    NSData *dataCharacter = [self dataWithHexString:strCharacter];
    unsigned char *pCharacter = (unsigned char *)[dataCharacter bytes];
    
    //配置场景控制指令总条数
    NSData *dataBranches = [self dataWithHexString:strBranches];
    unsigned char *pBranches = (unsigned char *)[dataBranches bytes];
    
    //配置场景执行序号
    NSData *dataExecutiveSerialNumber = [self dataWithHexString:strExecutiveSerialNumber];
    unsigned char *pExecutiveSerialNumber = (unsigned char *)[dataExecutiveSerialNumber bytes];
    
    //场景定时功能 xx时xx分
    //    int iSwitchValue = (int)[[ToolHexManager sharedManager] numberWithHexString:deviceReport.strDevice_other_status];
    //    NSString *strTimingHourTemp = [[ToolHexManager sharedManager] converIntToHex:[strTimingHour intValue]];
    NSData *dataTimingHour = [self dataWithHexString:strTimingHour];
    unsigned char *pTimingHour = (unsigned char *)[dataTimingHour bytes];
    
    //    NSString *strTimingMinuteTemp = [[ToolHexManager sharedManager] converIntToHex:[strTimingMinute intValue]];
    NSData *dataTimingMinute = [self dataWithHexString:strTimingMinute];
    unsigned char *pTimingMinute = (unsigned char *)[dataTimingMinute bytes];
    
    NSString *strTempFF = @"FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF";
    NSData *dataTempFF = [self dataWithHexString:strTempFF];
    unsigned char *pTempFF = (unsigned char *)[dataTempFF bytes];
    
    //场景延时执行时间， 如12 34对应1234hex = 4660s
    NSString *strTimeDelayTemp = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%04x",[strTimeDelay intValue]]];
    NSData *dataTimeDelay = [self dataWithHexString:strTimeDelayTemp];
    unsigned char *pTimeDelay = (unsigned char *)[dataTimeDelay bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    char pStart = 0x2A;
    memcpy(pWrite, &pStart, 1);
    pWrite ++;
    iWriteBytes++;
    
    memcpy(pWrite, pAllLength, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pCmdID, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pNumber, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pGatewayMacAddr, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    memcpy(pWrite, pFieldLength, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pSonOrderIdentifer, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pName, 20);
    pWrite += 20;
    iWriteBytes += 20;
    
    memcpy(pWrite, pProperty, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pSerialNumber, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pCharacter, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pBranches, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pExecutiveSerialNumber, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pTimingHour, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pTimingMinute, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pTempFF, 24);
    pWrite += 24;
    iWriteBytes += 24;
    
    memcpy(pWrite, pTimeDelay, 2);
    pWrite += 2;
    iWriteBytes += 2;
    
    //CS 1bytes
    char  packageCSDataResult = *(buffer + 2);
    for (int i = 3; i < iWriteBytes; i ++)
    {
        packageCSDataResult = packageCSDataResult + *(buffer + i);
    }
    char packageCS = packageCSDataResult%256;
    
    memcpy(pWrite, &packageCS, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //结束符 1byte
    char packageEnd = 0x23;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    NSLog(@"场景配置帧指令命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

//场景载入指令 或者 联动载入指令 不一样的地方在screenNO：一个为场景编号，一个为联动编号
- (NSData *)doHandleWriteScreenOrderToGatewayWithDataLength:(NSString *)strWholeDataLength
                                              sonDataLength:(NSString *)strSonDataLength
                                 controlFrameWriteIdentifer:(NSString *)strControlFrameWriteIdentifer
                                                   screenNO:(NSString *)strScreenNO
                                           instructionCount:(NSString *)strInstructionCount
                                    currentInstructionWhich:(NSString *)strCurrentInstructionNO
                                          instructionDetail:(NSData *)dataInstruction
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    //数据长度
    NSData *dataAllLength = [self dataWithHexString:strWholeDataLength];
    unsigned char *pAllLength = (unsigned char *)[dataAllLength bytes];
    
    //分数据域长度
    NSData *dataFieldLength = [self dataWithHexString:strSonDataLength];
    unsigned char *pFieldLength = (unsigned char *)[dataFieldLength bytes];
    
    NSData *dataControlFrameWriteIdentifer = [self dataWithHexString:strControlFrameWriteIdentifer];
    unsigned char *pControlFrameWriteIdentifer = (unsigned char *)[dataControlFrameWriteIdentifer bytes];
    
    NSData *dataScreenNO = [self dataWithHexString:strScreenNO];
    unsigned char *pScreenNO = (unsigned char *)[dataScreenNO bytes];
    
    NSData *dataInstructionCount = [self dataWithHexString:strInstructionCount];
    unsigned char *pInstructionCount = (unsigned char *)[dataInstructionCount bytes];
    
    NSData *dataCurrentInstructionNO = [self dataWithHexString:strCurrentInstructionNO];
    unsigned char *pCurrentInstructionNO = (unsigned char *)[dataCurrentInstructionNO bytes];
    
    unsigned char *pInstruction = (unsigned char *)[dataInstruction bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //帧头 1 byte
    char packageStart = 0x2A;
    memcpy(pWrite, &packageStart, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //帧长 1 byte
    memcpy(pWrite, pAllLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //命令标识
    char packageCmdID = 0x50;
    memcpy(pWrite, &packageCmdID, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //命令对象
    char pInstructionObject = 0x00;
    memcpy(pWrite, &pInstructionObject, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //网关MAC地址
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //数据长度
    memcpy(pWrite, pFieldLength, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    // 02:子命令标识，表示控制帧载入命令
    memcpy(pWrite, pControlFrameWriteIdentifer, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    // 01:场景编号为 01，与配置命令相对应
    memcpy(pWrite, pScreenNO, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    // 03:指令总数为
    memcpy(pWrite, pInstructionCount, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    //01:当前计数值为 01，表示指令总数 3 条中的第一条
    memcpy(pWrite, pCurrentInstructionNO, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    //当前设备控制命令帧，详细信息见各设备操作指南
    memcpy(pWrite, pInstruction, dataInstruction.length);
    pWrite +=dataInstruction.length;
    iWriteBytes += dataInstruction.length;
    
    //CS 1bytes
    char  packageCSDataResult = *(buffer + 2);
    for (int i = 3; i < iWriteBytes; i ++)
    {
        packageCSDataResult = packageCSDataResult + *(buffer + i);
    }
    
    char packageCS = packageCSDataResult%256;
    
    memcpy(pWrite, &packageCS, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //结束符 1byte
    char packageEnd = 0x23;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    NSLog(@"发送场景载入指令或者联动载入指令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

//场景触发指令 或 联动触发指令
- (NSData *)doHandleSendScreenOrderToControlWithScreenNO:(NSString *)strScreenNO
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    NSData *dataScreenNO = [self dataWithHexString:strScreenNO];
    unsigned char *pScreenNO = (unsigned char *)[dataScreenNO bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //帧头 1 byte
    char packageStart = 0x2A;
    memcpy(pWrite, &packageStart, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //帧长 1 byte
    char pAllLength = 0x0D;
    memcpy(pWrite, &pAllLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //命令标识
    char packageCmdID = 0x50;
    memcpy(pWrite, &packageCmdID, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //命令对象
    char pInstructionObject = 0x00;
    memcpy(pWrite, &pInstructionObject, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //网关MAC地址
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //数据长度
    char pFieldLength = 0x02;
    memcpy(pWrite, &pFieldLength, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    // 02:子命令标识，表示控制帧载入命令
    char pControlFrameWriteIdentifer = 0x09;
    memcpy(pWrite, &pControlFrameWriteIdentifer, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    // 01:场景编号为 01，与配置命令相对应
    memcpy(pWrite, pScreenNO, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    //CS 1bytes
    char  packageCSDataResult = *(buffer + 2);
    for (int i = 3; i < iWriteBytes; i ++)
    {
        packageCSDataResult = packageCSDataResult + *(buffer + i);
    }
    
    char packageCS = packageCSDataResult%256;
    
    memcpy(pWrite, &packageCS, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //结束符 1byte
    char packageEnd = 0x23;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    NSLog(@"场景触发指令命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}


#pragma mark - 联动配置指令
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
                                linkageDelayTime:(NSString *)strLinkageDelayTime
{
    //数据长度
    NSData *dataAllLength = [self dataWithHexString:strDataFieldAllLength];
    unsigned char *pAllLength = (unsigned char *)[dataAllLength bytes];
    
    /*
     0x01: Read_OD命令
     0x81: Read _OD命令执行错误
     0x02: Write_OD命令
     0x82: Write_OD命令执行错误
     0x05: 声明集中器命令也就是广告（Advertisement）命令
     0x21: Coordinator或手持设备立即发送Advertisement命令
     0x07: 转发命令
     0x08: 分包转发命令
     0x09: 主动上报设备的数据格式
     0x22: 复位命令命令
     0x24: 恢复参数缺省值命令
     */
    NSData *dataCmdID = [self dataWithHexString:strCmdID];
    unsigned char *pCmdID = (unsigned char *)[dataCmdID bytes];
    
    /*
     0x00: 针对节点自身，数据域中不包含目标地址，一般是指针对网关的相关操作。
     0x01: 针对其他节点，数据域中包含源地址和目标地址，源地址可用“00 00 00 00 00 00 00 00”替代。
     0xFF: 针对网络内所有节点，数据域中不包含目标地址
     */
    NSData *dataNumber = [self dataWithHexString:strNumber];
    unsigned char *pNumber = (unsigned char *)[dataNumber bytes];
    
    //网关的macAddr
    NSData *dataGatewayMacAddr = [self dataWithHexString:strGatewayMacAddr];
    unsigned char *pGatewayMacAddr = (unsigned char *)[dataGatewayMacAddr bytes];
    
    //分数据域长度
    NSData *dataFieldLength = [self dataWithHexString:strDataFieldLength];
    unsigned char *pFieldLength = (unsigned char *)[dataFieldLength bytes];
    
    //子命令标识 01:表示参数配置命令
    NSData *dataSonOrderIdentifer = [self dataWithHexString:strSonOrderIdentifer];
    unsigned char *pSonOrderIdentifer = (unsigned char *)[dataSonOrderIdentifer bytes];
    
    //场景的名字
    NSString *strScreenNameHex = [[ToolHexManager sharedManager] convertStringToHexStr:strName];
    //    NSData *dataName = [self dataWithHexString:strScreenNameHex];
    NSData *dataName = [self dataWithHexString:@"00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00"];
    unsigned char *pName = (unsigned char *)[dataName bytes];
    
    //配置场景属性，01代表场景；02代表联动
    NSData *dataProperty = [self dataWithHexString:strProperty];
    unsigned char *pProperty = (unsigned char *)[dataProperty bytes];
    
    //配置场景编号
    NSData *dataSerialNumber = [self dataWithHexString:strSerialNumber];
    unsigned char *pSerialNumber = (unsigned char *)[dataSerialNumber bytes];
    
    //配置场景特性:00表示初次设置/修改有效；01表示无效；FF表示删除
    NSData *dataCharacter = [self dataWithHexString:strCharacter];
    unsigned char *pCharacter = (unsigned char *)[dataCharacter bytes];
    
    //配置场景控制指令总条数
    NSData *dataBranches = [self dataWithHexString:strBranches];
    unsigned char *pBranches = (unsigned char *)[dataBranches bytes];
    
    
    NSData *dataIDonotKnow = [self dataWithHexString:@"FF FF FF"];
    unsigned char *pIDonotKnow = (unsigned char *)[dataIDonotKnow bytes];
    
    /*与场景配置不一样从这里开始*/
    //表示为强制联动，01 为强制联动;02 为非强制联动
    NSData *dataForceLinkage = [self dataWithHexString:strForceLinkage];
    unsigned char *pForceLinkage = (unsigned char *)[dataForceLinkage bytes];
    
    //启用/禁用状态标志字节，01 为启用;02 为禁用;若强制联动为 FF
    NSData *dataEnabledOrDisableIdentifer = [self dataWithHexString:strEnabledOrDisableIdentifer];
    unsigned char *pEnabledOrDisableIdentifer = (unsigned char *)[dataEnabledOrDisableIdentifer bytes];
    
    //布放/撤防联动使能标志字节，01 为布防有效，02 为撤防有效，若强制联动为 FF
    NSData *dataArmingOrDisarmingIdentifer = [self dataWithHexString:strArmingOrDisarmingIdentifer];
    unsigned char *pArmingOrDisarmingIdentifer = (unsigned char *)[dataArmingOrDisarmingIdentifer bytes];
    
    //发起联动设备 MAC 地址
    NSData *dataLinkageDeviceMacAddr = [self dataWithHexString:strLinkageDeviceMacAddr];
    unsigned char *pLinkageDeviceMacAddr = (unsigned char *)[dataLinkageDeviceMacAddr bytes];
    
    //数据比较通道为 01，01 代表 A 路;02 代表 B 路;03 代表 C 路
    NSData *dataWhichRoad = [self dataWithHexString:strWhichRoad];
    unsigned char *pWhichRoad = (unsigned char *)[dataWhichRoad bytes];
    
    //联动设备数据类型为 01，-Hex 型为 01;BCD 型为 02
    NSData *dataLinkageDeviceDataType = [self dataWithHexString:strLinkageDeviceDataType];
    unsigned char *pLinkageDeviceDataType = (unsigned char *)[dataLinkageDeviceDataType bytes];
    
    
    //联动设备数据和阈值数据比较类型为 01， 详见下表
    NSData *dataLinkageDeviceDataRange = [self dataWithHexString:strLinkageDeviceDataRange];
    unsigned char *pLinkageDeviceDataRange = (unsigned char *)[dataLinkageDeviceDataRange bytes];
    
    //联动时间段设置 0 点~24 点，在时间段内才可以进行联动，单位小时
    NSData *dataLinkageTime = [self dataWithHexString:strLinkageTime];
    unsigned char *pLinkageTime = (unsigned char *)[dataLinkageTime bytes];
    
    //阈值数据/阈值下限数据为 00 00 00 01
    NSData *dataThresholdLowLimit = [self dataWithHexString:strThresholdLowLimit];
    unsigned char *pThresholdLowLimit = (unsigned char *)[dataThresholdLowLimit bytes];
    
    //阈值上限数据
    NSData *dataThresholdUpperLimit = [self dataWithHexString:strThresholdUpperLimit];
    unsigned char *pThresholdUpperLimit = (unsigned char *)[dataThresholdUpperLimit bytes];
    
    //场景延时执行时间， 如12 34对应1234hex = 4660s
    NSString *strTimeDelayTemp = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%04x",[strLinkageDelayTime intValue]]];
    NSData *dataTimeDelay = [self dataWithHexString:strTimeDelayTemp];
    unsigned char *pTimeDelay = (unsigned char *)[dataTimeDelay bytes];
    
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    char pStart = 0x2A;
    memcpy(pWrite, &pStart, 1);
    pWrite ++;
    iWriteBytes++;
    
    memcpy(pWrite, pAllLength, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pCmdID, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pNumber, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pGatewayMacAddr, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    memcpy(pWrite, pFieldLength, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pSonOrderIdentifer, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pName, 20);
    pWrite += 20;
    iWriteBytes += 20;
    
    memcpy(pWrite, pProperty, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pSerialNumber, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pCharacter, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pBranches, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pIDonotKnow,3);
    pWrite += 3;
    iWriteBytes += 3;
    
    memcpy(pWrite, pForceLinkage, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pEnabledOrDisableIdentifer, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pArmingOrDisarmingIdentifer, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pLinkageDeviceMacAddr, 8);
    pWrite += 8;
    iWriteBytes += 8;
    
    memcpy(pWrite, pWhichRoad, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pLinkageDeviceDataType, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    memcpy(pWrite, pLinkageDeviceDataRange, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    
    memcpy(pWrite, pLinkageTime, 2);
    pWrite += 2;
    iWriteBytes += 2;
    
    
    memcpy(pWrite, pThresholdLowLimit, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    memcpy(pWrite, pThresholdUpperLimit, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    memcpy(pWrite, pTimeDelay, 2);
    pWrite += 2;
    iWriteBytes += 2;
    
    //CS 1bytes
    char  packageCSDataResult = *(buffer + 2);
    for (int i = 3; i < iWriteBytes; i ++)
    {
        packageCSDataResult = packageCSDataResult + *(buffer + i);
    }
    char packageCS = packageCSDataResult%256;
    
    memcpy(pWrite, &packageCS, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //结束符 1byte
    char packageEnd = 0x23;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    NSLog(@"联动配置指令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

#pragma mark -
#pragma mark - 设防 布防
- (NSData *)doHandleSendArmingOrDisarmingOrderToControlWithArmOrDisarmingIdentifer:(NSString *)strArmOrDisarmingIdentifer
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    NSData *dataIdentifer = [self dataWithHexString:strArmOrDisarmingIdentifer];
    unsigned char *pIdentifer = (unsigned char *)[dataIdentifer bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //帧头 1 byte
    char packageStart = 0x2A;
    memcpy(pWrite, &packageStart, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //帧长 1 byte
    char pAllLength = 0x0D;
    memcpy(pWrite, &pAllLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //命令标识
    char packageCmdID = 0x50;
    memcpy(pWrite, &packageCmdID, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //命令对象
    char pInstructionObject = 0x00;
    memcpy(pWrite, &pInstructionObject, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //网关MAC地址
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //数据长度
    char pFieldLength = 0x02;
    memcpy(pWrite, &pFieldLength, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    // 02:子命令标识，表示控制帧载入命令
    char pControlFrameWriteIdentifer = 0x05;
    memcpy(pWrite, &pControlFrameWriteIdentifer, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    // 01:场景编号为 01，与配置命令相对应
    memcpy(pWrite, pIdentifer, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    //CS 1bytes
    char  packageCSDataResult = *(buffer + 2);
    for (int i = 3; i < iWriteBytes; i ++)
    {
        packageCSDataResult = packageCSDataResult + *(buffer + i);
    }
    
    char packageCS = packageCSDataResult%256;
    
    memcpy(pWrite, &packageCS, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //结束符 1byte
    char packageEnd = 0x23;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    NSLog(@"设防 布防指令命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

#pragma mark -
#pragma mark - 场景联动删除命令
- (NSData *)doHandleSendDleteScreenOrderToControlWithScreenNO:(NSString *)strScreenNO
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    NSData *dataScreenNO = [self dataWithHexString:strScreenNO];
    unsigned char *pScreenNO = (unsigned char *)[dataScreenNO bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //帧头 1 byte
    char packageStart = 0x2A;
    memcpy(pWrite, &packageStart, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //帧长 1 byte
    char pAllLength = 0x0D;
    memcpy(pWrite, &pAllLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //命令标识
    char packageCmdID = 0x50;
    memcpy(pWrite, &packageCmdID, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //命令对象
    char pInstructionObject = 0x00;
    memcpy(pWrite, &pInstructionObject, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //网关MAC地址
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //数据长度
    char pFieldLength = 0x02;
    memcpy(pWrite, &pFieldLength, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    // 02:子命令标识，表示控制帧载入命令
    char pControlFrameWriteIdentifer = 0x07;
    memcpy(pWrite, &pControlFrameWriteIdentifer, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    // 01:场景编号为 01，与配置命令相对应
    memcpy(pWrite, pScreenNO, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    //CS 1bytes
    char  packageCSDataResult = *(buffer + 2);
    for (int i = 3; i < iWriteBytes; i ++)
    {
        packageCSDataResult = packageCSDataResult + *(buffer + i);
    }
    
    char packageCS = packageCSDataResult%256;
    
    memcpy(pWrite, &packageCS, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //结束符 1byte
    char packageEnd = 0x23;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    NSLog(@"场景联动删除命令指令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

#pragma mark -
#pragma mark - 网关报警状态解除命令
- (NSData *)doHandleDisarmingGatewayOrderToControl
{
    
    //    2A 0C 50 00 FD A3 C6 0A 00 6F 0D 00 01 08 45 23
    
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //帧头 1 byte
    char packageStart = 0x2A;
    memcpy(pWrite, &packageStart, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //帧长 1 byte
    char pAllLength = 0x0C;
    memcpy(pWrite, &pAllLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //命令标识
    char packageCmdID = 0x50;
    memcpy(pWrite, &packageCmdID, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //命令对象
    char pInstructionObject = 0x00;
    memcpy(pWrite, &pInstructionObject, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //网关MAC地址
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //数据长度
    char pFieldLength = 0x01;
    memcpy(pWrite, &pFieldLength, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    // 02:子命令标识，表示控制帧载入命令
    char pControlFrameWriteIdentifer = 0x08;
    memcpy(pWrite, &pControlFrameWriteIdentifer, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    //CS 1bytes
    char  packageCSDataResult = *(buffer + 2);
    for (int i = 3; i < iWriteBytes; i ++)
    {
        packageCSDataResult = packageCSDataResult + *(buffer + i);
    }
    
    char packageCS = packageCSDataResult%256;
    
    memcpy(pWrite, &packageCS, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //结束符 1byte
    char packageEnd = 0x23;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    NSLog(@"网关报警状态解除命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

#pragma mark -
#pragma mark - 获取场景存在网关的s个数
- (NSData *)doHandleGetScreenInGatewayCount
{
    
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //帧头 1 byte
    char packageStart = 0x2A;
    memcpy(pWrite, &packageStart, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //帧长 1 byte
    char pAllLength = 0x0C;
    memcpy(pWrite, &pAllLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //命令标识
    char packageCmdID = 0x50;
    memcpy(pWrite, &packageCmdID, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //命令对象
    char pInstructionObject = 0x00;
    memcpy(pWrite, &pInstructionObject, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //网关MAC地址
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //数据长度
    char pFieldLength = 0x01;
    memcpy(pWrite, &pFieldLength, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    // 02:子命令标识，表示控制帧载入命令
    char pControlFrameWriteIdentifer = 0x06;
    memcpy(pWrite, &pControlFrameWriteIdentifer, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    //CS 1bytes
    char  packageCSDataResult = *(buffer + 2);
    for (int i = 3; i < iWriteBytes; i ++)
    {
        packageCSDataResult = packageCSDataResult + *(buffer + i);
    }
    
    char packageCS = packageCSDataResult%256;
    
    memcpy(pWrite, &packageCS, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //结束符 1byte
    char packageEnd = 0x23;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    NSLog(@"获取网关场景个数:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

@end
