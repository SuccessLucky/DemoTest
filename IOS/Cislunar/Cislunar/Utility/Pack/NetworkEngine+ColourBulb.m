//
//  NetworkEngine+ColourBulb.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine+ColourBulb.h"

@implementation NetworkEngine (ColourBulb)

//3.8 读取智能多彩灯设备参数 (OD:4010)

- (NSData *)doGetColourBulbParameterWithTargetAddr:(NSString *)strTargetAddr
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 15 01 01";
    
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
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
    
    //OD的新写法
    NSString *strOD = @"0F AA";
    NSData *dataOD = [self dataWithHexString:strOD];
    unsigned char *pDataOD = (unsigned char *)[dataOD bytes];
    memcpy(pWrite, pDataOD, 2);
    pWrite +=2;
    iWriteBytes +=2;
    
    //开启子索引选项
    char pOpenSonIndex = 0x00;
    memcpy(pWrite, &pOpenSonIndex, 1);
    pWrite ++;
    iWriteBytes ++;
    
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
    return dataWrite;
}

//3.8.1.1 调光
- (NSData *)doGetColourBulbDimmerWithTargetAddr:(NSString *)strTargetAddr
                                   lightingMode:(NSString *)lightingMode
                                   lightingType:(NSString *)lightingType
                                    controlTime:(NSString *)strControlTime
                                 lightingSwitch:(NSString *)lightingSwitch
                              brightnessControl:(NSString *)brightnessControl
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 20 02 01";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
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
    
    //OD的新写法
    NSString *strOD = @"0F AA";
    NSData *dataOD = [self dataWithHexString:strOD];
    unsigned char *pDataOD = (unsigned char *)[dataOD bytes];
    memcpy(pWrite, pDataOD, 2);
    pWrite +=2;
    iWriteBytes +=2;
    
    //开启子索引选项
    char pOpenSonIndex = 0xFF;
    memcpy(pWrite, &pOpenSonIndex, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //数据域长度
    char pDataSonLength = 0x0A;
    memcpy(pWrite, &pDataSonLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    NSString *strSonIndex = @"1F 00 00 00";
    NSData *dataSonIndex = [self dataWithHexString:strSonIndex];
    unsigned char *pSonIndex = (unsigned char *)[dataSonIndex bytes];
    memcpy(pWrite, pSonIndex, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    //灯光模式
    NSData *dataLightingMode = [self dataWithHexString:lightingMode];
    unsigned char *pLightingMode = (unsigned char *)[dataLightingMode bytes];
    memcpy(pWrite, pLightingMode, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //灯光类型
    NSData *dataLightingType = [self dataWithHexString:lightingType];
    unsigned char *pLightingType = (unsigned char *)[dataLightingType bytes];
    memcpy(pWrite, pLightingType, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //控制时间 0005s，因为灯光模式为“直接”类型所以此处无意义(bit26)
    NSData *dataControlTime = [self dataWithHexString:strControlTime];
    unsigned char *pControlTime = (unsigned char *)[dataControlTime bytes];
    memcpy(pWrite, pControlTime, 2);
    pWrite += 2;
    iWriteBytes += 2;
    
    //灯光开关
    NSData *dataLightingSwitch = [self dataWithHexString:lightingSwitch];
    unsigned char *pLightingSwitch = (unsigned char *)[dataLightingSwitch bytes];
    memcpy(pWrite, pLightingSwitch, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //白光亮度
    NSData *dataBrightnessControl = [self dataWithHexString:brightnessControl];
    unsigned char *pBrightnessControl = (unsigned char *)[dataBrightnessControl bytes];
    memcpy(pWrite, pBrightnessControl, 1);
    pWrite ++;
    iWriteBytes ++;
    
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
    return dataWrite;
}

//3.8.1.2 调色
- (NSData *)doGetColourBulbPaletteWithTargetAddr:(NSString *)strTargetAddr
                                    lightingMode:(NSString *)lightingMode
                                    lightingType:(NSString *)lightingType
                                     controlTime:(NSString *)strControlTime
                                  lightingSwitch:(NSString *)lightingSwitch
                                   redLightValue:(NSString *)redLightValue
                                 greenLightValue:(NSString *)greenLightValue
                                  blueLightValue:(NSString *)blueLightValue

{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 22 02 01";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
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
    
    //OD的新写法
    NSString *strOD = @"0F AA";
    NSData *dataOD = [self dataWithHexString:strOD];
    unsigned char *pDataOD = (unsigned char *)[dataOD bytes];
    memcpy(pWrite, pDataOD, 2);
    pWrite +=2;
    iWriteBytes +=2;
    
    //开启子索引选项
    char pOpenSonIndex = 0xFF;
    memcpy(pWrite, &pOpenSonIndex, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //数据域长度
    char pDataSonLength = 0x0C;
    memcpy(pWrite, &pDataSonLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    NSString *strSonIndex = @"7F 00 00 00";
    NSData *dataSonIndex = [self dataWithHexString:strSonIndex];
    unsigned char *pSonIndex = (unsigned char *)[dataSonIndex bytes];
    memcpy(pWrite, pSonIndex, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    //灯光模式
    NSData *dataLightingMode = [self dataWithHexString:lightingMode];
    unsigned char *pLightingMode = (unsigned char *)[dataLightingMode bytes];
    memcpy(pWrite, pLightingMode, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //灯光类型
    NSData *dataLightingType = [self dataWithHexString:lightingType];
    unsigned char *pLightingType = (unsigned char *)[dataLightingType bytes];
    memcpy(pWrite, pLightingType, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //控制时间 0005s，因为灯光模式为“直接”类型所以此处无意义(bit26)
    NSData *dataControlTime = [self dataWithHexString:strControlTime];
    unsigned char *pControlTime = (unsigned char *)[dataControlTime bytes];
    memcpy(pWrite, pControlTime, 2);
    pWrite += 2;
    iWriteBytes += 2;
    
    //灯光开关
    NSData *dataLightingSwitch = [self dataWithHexString:lightingSwitch];
    unsigned char *pLightingSwitch = (unsigned char *)[dataLightingSwitch bytes];
    memcpy(pWrite, pLightingSwitch, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //红灯值
    NSData *dataRedLightValue = [self dataWithHexString:redLightValue];
    unsigned char *pRedLightValue = (unsigned char *)[dataRedLightValue bytes];
    memcpy(pWrite, pRedLightValue, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //绿灯值
    NSData *dataGreenLightValue = [self dataWithHexString:greenLightValue];
    unsigned char *pGreenLightValue = (unsigned char *)[dataGreenLightValue bytes];
    memcpy(pWrite, pGreenLightValue, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //蓝灯值
    NSData *dataBlueLightValue = [self dataWithHexString:blueLightValue];
    unsigned char *pBlueLightValue = (unsigned char *)[dataBlueLightValue bytes];
    memcpy(pWrite, pBlueLightValue, 1);
    pWrite ++;
    iWriteBytes ++;
    
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
    return dataWrite;
}

- (NSData *)doGetColourBulbXXXWithTargetAddr:(NSString *)strTargetAddr
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 15 01 01";
    
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
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
    
    //OD的新写法
    NSString *strOD = @"17 71";
    NSData *dataOD = [self dataWithHexString:strOD];
    unsigned char *pDataOD = (unsigned char *)[dataOD bytes];
    memcpy(pWrite, pDataOD, 2);
    pWrite +=2;
    iWriteBytes +=2;
    
    //开启子索引选项
    char pOpenSonIndex = 0x00;
    memcpy(pWrite, &pOpenSonIndex, 1);
    pWrite ++;
    iWriteBytes ++;
    
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
    return dataWrite;
}


#pragma mark -
#pragma mark -  临时使用彩色球泡灯一些模式命令
//3.8.11xxxxxxx 渐变
- (NSData *)doGetColourBulbAllKindsOfModelOneWithTargetAddr:(NSString *)strTargetAddr
                                               lightingMode:(NSString *)lightingMode

{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 20 02 01";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
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
    
    //OD的新写法
    NSString *strOD = @"0F AA";
    NSData *dataOD = [self dataWithHexString:strOD];
    unsigned char *pDataOD = (unsigned char *)[dataOD bytes];
    memcpy(pWrite, pDataOD, 2);
    pWrite +=2;
    iWriteBytes +=2;
    
    //开启子索引选项
    char pOpenSonIndex = 0xFF;
    memcpy(pWrite, &pOpenSonIndex, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //数据域长度
    char pDataSonLength = 0x0A;
    memcpy(pWrite, &pDataSonLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    NSString *strSonIndex = @"7B 00 00 00";
    NSData *dataSonIndex = [self dataWithHexString:strSonIndex];
    unsigned char *pSonIndex = (unsigned char *)[dataSonIndex bytes];
    memcpy(pWrite, pSonIndex, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    //灯光模式
    NSData *dataLightingMode = [self dataWithHexString:lightingMode];
    unsigned char *pLightingMode = (unsigned char *)[dataLightingMode bytes];
    memcpy(pWrite, pLightingMode, 1);
    pWrite ++;
    iWriteBytes ++;
    
    NSString *strIDontKnow = @"01 01 00 00 00";
    NSData *dataIDontKnow = [self dataWithHexString:strIDontKnow];
    unsigned char *pIDontKnow = (unsigned char *)[dataIDontKnow bytes];
    memcpy(pWrite, pIDontKnow, 5);
    pWrite += 5;
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
    return dataWrite;
}

//3.8.12xxxxxxx 七彩跳变
- (NSData *)doGetColourBulbAllKindsOfModelTwoWithTargetAddr:(NSString *)strTargetAddr
                                               lightingMode:(NSString *)lightingMode

{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 22 02 01";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
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
    
    //OD的新写法
    NSString *strOD = @"0F AA";
    NSData *dataOD = [self dataWithHexString:strOD];
    unsigned char *pDataOD = (unsigned char *)[dataOD bytes];
    memcpy(pWrite, pDataOD, 2);
    pWrite +=2;
    iWriteBytes +=2;
    
    //开启子索引选项
    char pOpenSonIndex = 0xFF;
    memcpy(pWrite, &pOpenSonIndex, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //数据域长度
    char pDataSonLength = 0x0C;
    memcpy(pWrite, &pDataSonLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    NSString *strSonIndex = @"7F 00 00 00";
    NSData *dataSonIndex = [self dataWithHexString:strSonIndex];
    unsigned char *pSonIndex = (unsigned char *)[dataSonIndex bytes];
    memcpy(pWrite, pSonIndex, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    //灯光模式
    NSData *dataLightingMode = [self dataWithHexString:lightingMode];
    unsigned char *pLightingMode = (unsigned char *)[dataLightingMode bytes];
    memcpy(pWrite, pLightingMode, 1);
    pWrite ++;
    iWriteBytes ++;
    
    NSString *strIDontKnow = @"01 00 05 01 00 00 00";
    NSData *dataIDontKnow = [self dataWithHexString:strIDontKnow];
    unsigned char *pIDontKnow = (unsigned char *)[dataIDontKnow bytes];
    memcpy(pWrite, pIDontKnow, 7);
    pWrite += 7;
    iWriteBytes += 7;
    
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
    return dataWrite;
}


//3.8.11xxxxxxx 呼吸灯
- (NSData *)doGetColourBulbAllKindsOfModelThreeWithTargetAddr:(NSString *)strTargetAddr
                                                 lightingMode:(NSString *)lightingMode

{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 20 02 01";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
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
    
    //OD的新写法
    NSString *strOD = @"0F AA";
    NSData *dataOD = [self dataWithHexString:strOD];
    unsigned char *pDataOD = (unsigned char *)[dataOD bytes];
    memcpy(pWrite, pDataOD, 2);
    pWrite +=2;
    iWriteBytes +=2;
    
    //开启子索引选项
    char pOpenSonIndex = 0xFF;
    memcpy(pWrite, &pOpenSonIndex, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //数据域长度
    char pDataSonLength = 0x0A;
    memcpy(pWrite, &pDataSonLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    NSString *strSonIndex = @"7B 00 00 00";
    NSData *dataSonIndex = [self dataWithHexString:strSonIndex];
    unsigned char *pSonIndex = (unsigned char *)[dataSonIndex bytes];
    memcpy(pWrite, pSonIndex, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    //灯光模式
    NSData *dataLightingMode = [self dataWithHexString:lightingMode];
    unsigned char *pLightingMode = (unsigned char *)[dataLightingMode bytes];
    memcpy(pWrite, pLightingMode, 1);
    pWrite ++;
    iWriteBytes ++;
    
    NSString *strIDontKnow = @"03 01 00 FF 13";
    NSData *dataIDontKnow = [self dataWithHexString:strIDontKnow];
    unsigned char *pIDontKnow = (unsigned char *)[dataIDontKnow bytes];
    memcpy(pWrite, pIDontKnow, 5);
    pWrite += 5;
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
    return dataWrite;
}


#pragma mark -
#pragma mark - 设置区域
- (NSData *)doSendSetAreaNOWithTargetAddr:(NSString *)strTargetAddr andAreaNo:(NSString *)strHex
{
    
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 17 02 01";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
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
    
    //OD的新写法
    NSString *strOD = @"03 EF";
    NSData *dataOD = [self dataWithHexString:strOD];
    unsigned char *pDataOD = (unsigned char *)[dataOD bytes];
    memcpy(pWrite, pDataOD, 2);
    pWrite +=2;
    iWriteBytes +=2;
    
    //开启子索引选项
    char pOpenSonIndex = 0x08;
    memcpy(pWrite, &pOpenSonIndex, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //数据域长度
    char pDataSonLength = 0x01;
    memcpy(pWrite, &pDataSonLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    NSString *strSonIndex = strHex;
    NSData *dataSonIndex = [self dataWithHexString:strSonIndex];
    unsigned char *pSonIndex = (unsigned char *)[dataSonIndex bytes];
    memcpy(pWrite, pSonIndex, 1);
    pWrite += 1;
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
    return dataWrite;
    
}


#pragma mark -
#pragma mark - 单控
//3.8.11xxxxxxx 呼吸灯
- (NSData *)doHandleSigleControlWithTargetAddr:(NSString *)strTargetAddr
                                  lightingMode:(NSString *)lightingMode

{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 1C 02 01";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
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
    
    //OD的新写法
    NSString *strOD = @"0F AA";
    NSData *dataOD = [self dataWithHexString:strOD];
    unsigned char *pDataOD = (unsigned char *)[dataOD bytes];
    memcpy(pWrite, pDataOD, 2);
    pWrite +=2;
    iWriteBytes +=2;
    
    //开启子索引选项
    char pOpenSonIndex = 0xFF;
    memcpy(pWrite, &pOpenSonIndex, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //数据域长度
    char pDataSonLength = 0x06;
    memcpy(pWrite, &pDataSonLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    NSString *strSonIndex = @"00 01 80 00";
    NSData *dataSonIndex = [self dataWithHexString:strSonIndex];
    unsigned char *pSonIndex = (unsigned char *)[dataSonIndex bytes];
    memcpy(pWrite, pSonIndex, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    //灯光模式
    NSData *dataLightingMode = [self dataWithHexString:lightingMode];
    unsigned char *pLightingMode = (unsigned char *)[dataLightingMode bytes];
    memcpy(pWrite, pLightingMode, 1);
    pWrite ++;
    iWriteBytes ++;
    
    NSString *strIDontKnow = @"03 01 00 FF 13";
    NSData *dataIDontKnow = [self dataWithHexString:strIDontKnow];
    unsigned char *pIDontKnow = (unsigned char *)[dataIDontKnow bytes];
    memcpy(pWrite, pIDontKnow, 5);
    pWrite += 5;
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
    return dataWrite;
}

@end
