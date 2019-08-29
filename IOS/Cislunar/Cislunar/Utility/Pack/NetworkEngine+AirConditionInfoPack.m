//
//  NetworkEngine+AirConditionInfoPack.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine+AirConditionInfoPack.h"

@implementation NetworkEngine (AirConditionInfoPack)

#pragma mark -空调
#pragma mark - 设定空调型号
- (NSData *)doGetSetAirConditionModelTargetAddr:(NSString *)strTargetAddr modelNO:(int)modelNo
{
    
    //    unsigned char pModelNo = (char)(modelNo % 256);
    
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 18 07 01";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
    //被转发数据域
    NSData *dataTransmit = [self doGetSetAirConditionModelTransmitWithModelNO:modelNo];
    unsigned char *pTransmit = (unsigned char *)[dataTransmit bytes];
    
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    memcpy(pWrite, pHeaderMixed, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    memcpy(pWrite, pTargetAddr, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //被转发协议的长度
    char pForwardProtocol = 0x05;
    memcpy(pWrite, &pForwardProtocol, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pTransmit, 5);
    pWrite +=5;
    iWriteBytes += 5;
    
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
    NSLog(@"发送设定空调型号命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
    
    
}

- (NSData *)doGetSetAirConditionModelTransmitWithModelNO:(int)modelNO
{
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //BYTE1 1 byte
    char dataPackageCmd = 0x02;
    memcpy(pWrite, &dataPackageCmd, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //BYTE2 1 byte
    char pDataH = (char)(modelNO/256);
    memcpy(pWrite, &pDataH, 1);
    pWrite ++;
    iWriteBytes ++;
    
    
    //BYTE3
    char pDataL = (char)(modelNO%256);
    memcpy(pWrite, &pDataL, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //BYTE4
    char dataPackageTM = 0x08;
    memcpy(pWrite, &dataPackageTM, 1);
    pWrite ++;
    iWriteBytes ++;
    
    
    //FCS 1Byte
    char FCS = *(buffer + 0)^*(buffer + 1);
    for (int i = 2; i < iWriteBytes; i++)
    {
        FCS = FCS ^ *(buffer + i);
    }
    
    memcpy(pWrite, &FCS, 1);
    pWrite++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    NSLog(@"发送设定空调型号命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

#pragma mark -发送开关命令
/*
 *  strOnOrOff：FF： 开，00：关，其余参数无效
 */
- (NSData *)doGetSetAirConditionOnOrOffTargetAddr:(NSString *)strTargetAddr strOnOrOff:(NSString *)strOnOrOff
{
    
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 18 07 01";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
    //被转发数据域
    NSData *dataTransmit = [self doGetSetAirConditionOnOrOffTransmit:strOnOrOff];
    unsigned char *pTransmit = (unsigned char *)[dataTransmit bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    memcpy(pWrite, pHeaderMixed, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    memcpy(pWrite, pTargetAddr, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //被转发协议的长度
    char pForwardProtocol = 0x05;
    memcpy(pWrite, &pForwardProtocol, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pTransmit, 5);
    pWrite +=5;
    iWriteBytes += 5;
    
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
    NSLog(@"发送设定空调开关命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}


- (NSData *)doGetSetAirConditionOnOrOffTransmit:(NSString *)strOnOrOff
{
    
    NSData *dataOnOrOff = [self dataWithHexString:strOnOrOff];
    unsigned char *pOnOrOff = (unsigned char *)[dataOnOrOff bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //BYTE1 1 byte
    char dataPackageCmd = 0x04;
    memcpy(pWrite, &dataPackageCmd, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pOnOrOff, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    char dataPackageHour = 0x08;
    memcpy(pWrite, &dataPackageHour, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char dataPackageMinute = 0x08;
    memcpy(pWrite, &dataPackageMinute, 1);
    pWrite ++;
    iWriteBytes ++;
    
    
    //FCS 1Byte
    char FCS = *(buffer + 0)^*(buffer + 1);
    for (int i = 2; i < iWriteBytes; i++)
    {
        FCS = FCS ^ *(buffer + i);
    }
    
    memcpy(pWrite, &FCS, 1);
    pWrite++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    return dataWrite;
}


#pragma mark -发送模式命令
//mode：00：自动  01：制冷   02：除湿    03：送风   04：制暖
- (NSData *)doGetSetAirConditionModeTargetAddr:(NSString *)strTargetAddr mode:(NSString *)mode
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 18 07 01";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
    //被转发数据域
    NSData *dataTransmit = [self doGetSetAirConditionModeTransmit:mode];
    unsigned char *pTransmit = (unsigned char *)[dataTransmit bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    memcpy(pWrite, pHeaderMixed, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    memcpy(pWrite, pTargetAddr, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //被转发协议的长度
    char pForwardProtocol = 0x05;
    memcpy(pWrite, &pForwardProtocol, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pTransmit, 5);
    pWrite +=5;
    iWriteBytes += 5;
    
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
    NSLog(@"发送设定空调模式命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

- (NSData *)doGetSetAirConditionModeTransmit:(NSString *)mode
{
    
    NSData *dataMode = [self dataWithHexString:mode];
    unsigned char *pMode = (unsigned char *)[dataMode bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //BYTE1 1 byte
    char dataPackageCmd = 0x05;
    memcpy(pWrite, &dataPackageCmd, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pMode, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    char dataPackageHour = 0x08;
    memcpy(pWrite, &dataPackageHour, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char dataPackageMinute = 0x08;
    memcpy(pWrite, &dataPackageMinute, 1);
    pWrite ++;
    iWriteBytes ++;
    
    
    //FCS 1Byte
    char FCS = *(buffer + 0)^*(buffer + 1);
    for (int i = 2; i < iWriteBytes; i++)
    {
        FCS = FCS ^ *(buffer + i);
    }
    
    memcpy(pWrite, &FCS, 1);
    pWrite++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    return dataWrite;
}

#pragma mark -发送温度命令
//PAR1：温度值 范围：10H - 1EH （16-31度）其余无效
- (NSData *)doGetSetAirConditionTemperatureTargetAddr:(NSString *)strTargetAddr temperature:(int)temperature
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 18 07 01";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
    //被转发数据域
    NSData *dataTransmit = [self doGetSetAirConditionTemperatureTransmit:temperature];
    unsigned char *pTransmit = (unsigned char *)[dataTransmit bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    memcpy(pWrite, pHeaderMixed, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    memcpy(pWrite, pTargetAddr, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //被转发协议的长度
    char pForwardProtocol = 0x05;
    memcpy(pWrite, &pForwardProtocol, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pTransmit, 5);
    pWrite +=5;
    iWriteBytes += 5;
    
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
    NSLog(@"发送设定空调温度命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
    
}

- (NSData *)doGetSetAirConditionTemperatureTransmit:(int)Temperature
{
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //BYTE1 1 byte
    char dataPackageCmd = 0x06;
    memcpy(pWrite, &dataPackageCmd, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pTemperature = (char)(Temperature%256);
    memcpy(pWrite, &pTemperature, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    char dataPackageHour = 0x08;
    memcpy(pWrite, &dataPackageHour, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char dataPackageMinute = 0x08;
    memcpy(pWrite, &dataPackageMinute, 1);
    pWrite ++;
    iWriteBytes ++;
    
    
    //FCS 1Byte
    char FCS = *(buffer + 0)^*(buffer + 1);
    for (int i = 2; i < iWriteBytes; i++)
    {
        FCS = FCS ^ *(buffer + i);
    }
    
    memcpy(pWrite, &FCS, 1);
    pWrite++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    return dataWrite;
    
}

#pragma mark 发送风速命令
//PAR1：00 = 自动  01=1档   02=2档    03=3档  其余无效
- (NSData *)doGetSetAirConditionSpeedTargetAddr:(NSString *)strTargetAddr speed:(NSString *)speed
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 18 07 01";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
    //被转发数据域
    NSData *dataTransmit = [self doGetSetAirConditionSpeedTransmit:speed];
    unsigned char *pTransmit = (unsigned char *)[dataTransmit bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    memcpy(pWrite, pHeaderMixed, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    memcpy(pWrite, pTargetAddr, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //被转发协议的长度
    char pForwardProtocol = 0x05;
    memcpy(pWrite, &pForwardProtocol, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pTransmit, 5);
    pWrite +=5;
    iWriteBytes += 5;
    
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
    NSLog(@"发送设定空调风速命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

- (NSData *)doGetSetAirConditionSpeedTransmit:(NSString*)speed
{
    NSData *dataSpeed = [self dataWithHexString:speed];
    unsigned char *pSpeed = (unsigned char *)[dataSpeed bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //BYTE1 1 byte
    char dataPackageCmd = 0x07;
    memcpy(pWrite, &dataPackageCmd, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pSpeed, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    char dataPackageHour = 0x08;
    memcpy(pWrite, &dataPackageHour, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char dataPackageMinute = 0x08;
    memcpy(pWrite, &dataPackageMinute, 1);
    pWrite ++;
    iWriteBytes ++;
    
    
    //FCS 1Byte
    char FCS = *(buffer + 0)^*(buffer + 1);
    for (int i = 2; i < iWriteBytes; i++)
    {
        FCS = FCS ^ *(buffer + i);
    }
    
    memcpy(pWrite, &FCS, 1);
    pWrite++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    return dataWrite;
}


#pragma mark 发送风向命令
//PAR1：00 = 自动摆风  01手动摆风  其余无效
- (NSData *)doGetSetAirConditionDirectionTargetAddr:(NSString *)strTargetAddr direction:(NSString *)direction
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 18 07 01";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
    //被转发数据域
    NSData *dataTransmit = [self doGetSetAirConditionDirectionTransmit:direction];
    unsigned char *pTransmit = (unsigned char *)[dataTransmit bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    memcpy(pWrite, pHeaderMixed, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    memcpy(pWrite, pTargetAddr, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //被转发协议的长度
    char pForwardProtocol = 0x05;
    memcpy(pWrite, &pForwardProtocol, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pTransmit, 5);
    pWrite +=5;
    iWriteBytes += 5;
    
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
    NSLog(@"发送设定空调风速命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

- (NSData *)doGetSetAirConditionDirectionTransmit:(NSString*)direction
{
    NSData *dataDirection = [self dataWithHexString:direction];
    unsigned char *pDirection = (unsigned char *)[dataDirection bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //BYTE1 1 byte
    char dataPackageCmd = 0x08;
    memcpy(pWrite, &dataPackageCmd, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pDirection, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    char dataPackageHour = 0x08;
    memcpy(pWrite, &dataPackageHour, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char dataPackageMinute = 0x08;
    memcpy(pWrite, &dataPackageMinute, 1);
    pWrite ++;
    iWriteBytes ++;
    
    
    //FCS 1Byte
    char FCS = *(buffer + 0)^*(buffer + 1);
    for (int i = 2; i < iWriteBytes; i++)
    {
        FCS = FCS ^ *(buffer + i);
    }
    
    memcpy(pWrite, &FCS, 1);
    pWrite++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    return dataWrite;
}

@end
