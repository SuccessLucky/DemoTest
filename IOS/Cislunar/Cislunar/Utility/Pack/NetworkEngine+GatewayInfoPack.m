//
//  NetworkEngine+GatewayInfoPack.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine+GatewayInfoPack.h"
#import "HLDataUtil.h"
#import "HLOrderUtil.h"

@implementation NetworkEngine (GatewayInfoPack)

#pragma mark - private
- (NSData *)dataWithHexString:(NSString *)str
{
    NSString *strHex = [str stringByReplacingOccurrencesOfString:@" " withString:@""];
    NSData *data = [[ToolHexManager sharedManager] convertHexStrToData:strHex];
    return data;
}



//- (NSData *)doHandleTheCS:(void *)buffer pwrite:(char *)pWrite iWriteBytes:(int)iWriteBytes
//{
//    //CS 1bytes
//    char  packageCSDataResult = *(buffer + 2);
//    for (int i = 3; i < iWriteBytes; i ++)
//    {
//        packageCSDataResult = packageCSDataResult + *(buffer + i);
//    }
//
//    char packageCS = packageCSDataResult%256;
//
//    memcpy(pWrite, &packageCS, 1);
//    pWrite ++;
//    iWriteBytes ++;
//
//    //结束符 1byte
//    char packageEnd = 0x23;
//    memcpy(pWrite, &packageEnd, 1);
//    pWrite ++;
//    iWriteBytes++;
//
//    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
//    return dataWrite;
//}

#pragma mark - 读取网关Zigbee的MacAddress
#pragma mark - 从网关读取网关
- (NSData *)doGetGatewayZigbeeMacAddressFromGateway
{
    NSString *strOriginal = @"2A 0D 01 00 00 00 00 00 00 00 00 00 03 EB 00 EF 23";
    NSData *data = [self dataWithHexString:strOriginal];
    return data;
}

#pragma mark -
#pragma mark -3.1、读取全部网关参数（OD：1001）
- (NSData *)doGetGatewayAllParameters
{
    //    2A 0D 01 00 5A 8F 38 04 00 6F 0D 00 03 E9 00 8E 23
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 0D 01 00";
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
    
    //OD 2 bytes
    char packageODFirst = 0x03;
    memcpy(pWrite, &packageODFirst, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char packageODSecond = 0xE9;
    memcpy(pWrite, &packageODSecond, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //子索引 1byte
    char deviceIndex = 0x00;
    memcpy(pWrite,&deviceIndex,1);
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

#pragma mark -3.2、读取网关参数 （OD：1005）
- (NSData *)doGetGatewayPartOfParameters
{
    //    2A 0D 01 00 4A A3 C6 0A 00 6F 0D 00 03 ED 0F 39 23
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 0D 01 00";
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
    
    //OD 2 bytes
    char packageODFirst = 0x03;
    memcpy(pWrite, &packageODFirst, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char packageODSecond = 0xED;
    memcpy(pWrite, &packageODSecond, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //子索引 1byte
    char deviceIndex = 0x0F;
    memcpy(pWrite,&deviceIndex,1);
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

#pragma mark -3.3、读取网关登入帧（OD：5010）
- (NSData *)doGetGatewayLoginFrame
{
    // 2A 0D 01 00 5A 8F 38 04 00 6F 0D 00 13 92 00 47 23
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 0D 01 00";
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
    
    //OD 2 bytes
    char packageODFirst = 0x13;
    memcpy(pWrite, &packageODFirst, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char packageODSecond = 0x92;
    memcpy(pWrite, &packageODSecond, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //子索引 1byte
    char deviceIndex = 0x00;
    memcpy(pWrite,&deviceIndex,1);
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

#pragma mark - 3.4、读取网关心跳帧（OD：5020）
- (NSData *)doReadGatewayHeart
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 0D 01 00";
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
    
    //OD 2 bytes
    char packageODFirst = 0x13;
    memcpy(pWrite, &packageODFirst, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char packageODSecond = 0x9C;
    memcpy(pWrite, &packageODSecond, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //子索引 1byte
    char deviceIndex = 0x00;
    memcpy(pWrite,&deviceIndex,1);
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

#pragma mark - 3.5、读取网关基本信息帧（OD：5040）
- (NSData *)doReadGatewayBaseInfoFrame
{
    //    2A 0D 01 00 5A 8F 38 04 00 6F 0D 00 13 B0 00 65 23
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 0D 01 00";
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
    
    //OD 2 bytes
    char packageODFirst = 0x13;
    memcpy(pWrite, &packageODFirst, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char packageODSecond = 0xB0;
    memcpy(pWrite, &packageODSecond, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //子索引 1byte
    char deviceIndex = 0x00;
    memcpy(pWrite,&deviceIndex,1);
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

#pragma mark - 从本地获取网关的MAC地址
- (NSData *)doGetGatewayZigbeeMacAddrFromLocal
{
    NSString *strMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    NSString *strHex = [strMacAddr stringByReplacingOccurrencesOfString:@" " withString:@""];
    NSData *dataGatewayZigbeeMacAddr = [[ToolHexManager sharedManager] convertHexStrToData:strHex];
    return dataGatewayZigbeeMacAddr;
}

- (NSData *)doGetGatewayZigbeeMacAddrFromLocalWithZero
{
    NSString *strMacAddr = @"00 00 00 00 00 00 00 00";
    NSString *strHex = [strMacAddr stringByReplacingOccurrencesOfString:@" " withString:@""];
    NSData *dataGatewayZigbeeMacAddr = [[ToolHexManager sharedManager] convertHexStrToData:strHex];
    return dataGatewayZigbeeMacAddr;
}

#pragma mark-  获取网关时间
- (NSData *)doGetGatewayTimeData
{
    char *a = "AT+GETTIME:";
    NSData *data = [NSData dataWithBytes:a length:11];
    LLog([NSString stringWithFormat:@"获取网关时间的命令:\n\n%@\n\n",data]);
    return data;
}

#pragma mark- 设置网关时间
- (NSData *)doSetGatewayTimeDataWith:(NSString *)time
{
    NSData *data = [time dataUsingEncoding:NSUTF8StringEncoding];
    LLog([NSString stringWithFormat:@"设置网关时间的命令:\n\n%@\n\n",data]);
    return data;
}

#pragma mark - 重启wifi模块
- (NSData *)doGetRestarWifiModuleData
{
    NSString *strHex = @"2A 0F 02 00 00 00 00 00 00 00 00 00 13 B0 06 01 01 CD 23";
    NSData *dataHex = [self dataWithHexString:strHex];
    return dataHex;
}

#pragma mark - 重启Zigbee模块
- (NSData *)doGetRestarZigbeeModuleData
{
    NSString *strHex = @"2A 0F 02 00 00 00 00 00 00 00 00 00 13 B0 07 01 01 CE 23";
    NSData *dataHex = [self dataWithHexString:strHex];
    return dataHex;
}

#pragma mark - 重启系统模块
- (NSData *)doGetRestarSystemModuleData
{
    NSString *strHex = @"2A 0F 02 00 00 00 00 00 00 00 00 00 13 B0 09 01 01 D0 23";
    NSData *dataHex = [self dataWithHexString:strHex];
    return dataHex;
}

#pragma mark - 心跳归零数据帧

//2A 0F 02 00 00 00 00 00 00 00 00 00 13 9C 04 01 00 B6 23
- (NSData *)doGetGatewayHeartClearToZero
{
    NSString *strHex = @"2A 0F 02 00 00 00 00 00 00 00 00 00 13 9C 04 01 00 B6 23";
    NSData *dataHex = [self dataWithHexString:strHex];
    return dataHex;
}


//
//- (NSData *)doGetGatewayHeartClearToZero
//{
////    2A 0F 02 00 00 00 00 00 00 00 00 00 13 9C 04 01 00 B6 23
//    //网关Zigbee的MacAddr
//    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
//    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
//
//    //帧头 帧长 CmdID Number
//    NSString *strHeaderMixed = @"2A 0D 02 00";
//    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
//    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
//
//    char buffer[256];
//    char *pWrite = buffer;
//    int iWriteBytes = 0; //计算总共长度
//
//    memcpy(pWrite, pHeaderMixed, 4);
//    pWrite += 4;
//    iWriteBytes += 4;
//
//    memcpy(pWrite, pMacAddress, 8);
//    pWrite +=8;
//    iWriteBytes += 8;
//
//    //OD 2 bytes
//    char packageODFirst = 0x13;
//    memcpy(pWrite, &packageODFirst, 1);
//    pWrite ++;
//    iWriteBytes ++;
//
//    char packageODSecond = 0x9C;
//    memcpy(pWrite, &packageODSecond, 1);
//    pWrite ++;
//    iWriteBytes ++;
//
//    //子索引 1byte
//    char deviceIndex = 0x04;
//    memcpy(pWrite,&deviceIndex,1);
//    pWrite ++;
//    iWriteBytes ++;
//
//    char px1 = 0x01;
//    memcpy(pWrite,&px1,1);
//    pWrite ++;
//    iWriteBytes ++;
//
//    char px2 = 0x00;
//    memcpy(pWrite,&px2,1);
//    pWrite ++;
//    iWriteBytes ++;
//
//    //CS 1bytes
//    char  packageCSDataResult = *(buffer + 2);
//    for (int i = 3; i < iWriteBytes; i ++)
//    {
//        packageCSDataResult = packageCSDataResult + *(buffer + i);
//    }
//
//    char packageCS = packageCSDataResult%256;
//
//    memcpy(pWrite, &packageCS, 1);
//    pWrite ++;
//    iWriteBytes ++;
//
//    //结束符 1byte
//    char packageEnd = 0x23;
//    memcpy(pWrite, &packageEnd, 1);
//    pWrite ++;
//    iWriteBytes++;
//
//    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
//    return dataWrite;
//}


//激活心跳数据帧
- (NSData *)doActivateHeartFrame
{
    NSString *strHex = @"2A 0D 01 00 00 00 00 00 00 00 00 00 13 92 00 A6 23";
    NSData *dataHex = [self dataWithHexString:strHex];
    return dataHex;
}


#pragma mark - New
#pragma mark - 读取网关时间
- (NSData *)doReadTheCurrentTimeFromGateway
{
    
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 0D 01 00";
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
    
    //OD 2 bytes
    char packageODFirst = 0x13;
    memcpy(pWrite, &packageODFirst, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char packageODSecond = 0xB0;
    memcpy(pWrite, &packageODSecond, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //子索引 1byte
    char deviceIndex = 0x02;
    memcpy(pWrite,&deviceIndex,1);
    pWrite ++;
    iWriteBytes ++;
    
    //    //子索引 1byte
    //    char cIDontKnow = 0xF0;
    //    memcpy(pWrite,&cIDontKnow,1);
    //    pWrite ++;
    //    iWriteBytes ++;
    
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



#pragma mark - 设置网关时间
- (NSData *)doHandleSetGatewayTimeNew
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 16 02 00";
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
    
    //OD 2 bytes
    char packageODFirst = 0x13;
    memcpy(pWrite, &packageODFirst, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char packageODSecond = 0xB0;
    memcpy(pWrite, &packageODSecond, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //子索引 1byte
    char deviceIndex = 0x02;
    memcpy(pWrite,&deviceIndex,1);
    pWrite ++;
    iWriteBytes ++;
    
    char sonDataLength = 0x08;
    memcpy(pWrite,&sonDataLength,1);
    pWrite ++;
    iWriteBytes ++;
    
    NSData *dataYear = [self getCurrentTimes:SHDataType_Year];
    unsigned char *pYear = (unsigned char *)[dataYear bytes];
    memcpy(pWrite, pYear, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    NSData *dataMonth = [self getCurrentTimes:SHDataType_Month];
    unsigned char *pMonth = (unsigned char *)[dataMonth bytes];
    memcpy(pWrite, pMonth, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    NSData *dataDay = [self getCurrentTimes:SHDataType_Day];
    unsigned char *pDay = (unsigned char *)[dataDay bytes];
    memcpy(pWrite, pDay, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    
    NSData *dataWeek = [self weekdayStringFromDate];
    unsigned char *pWeek = (unsigned char *)[dataWeek bytes];
    memcpy(pWrite, pWeek, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    
    NSData *dataHour = [self getCurrentTimes:SHDataType_Hour];
    unsigned char *pHour = (unsigned char *)[dataHour bytes];
    memcpy(pWrite, pHour, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    NSData *dataMinute = [self getCurrentTimes:SHDataType_Minute];
    unsigned char *pMinute = (unsigned char *)[dataMinute bytes];
    memcpy(pWrite, pMinute, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    NSData *dataSecond = [self getCurrentTimes:SHDataType_Second];
    unsigned char *pSecond = (unsigned char *)[dataSecond bytes];
    memcpy(pWrite, pSecond, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    char packageKeep = 0x23;
    memcpy(pWrite, &packageKeep, 1);
    pWrite ++;
    iWriteBytes++;
    
    
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

-(NSData*)getCurrentTimes:(SHDataType)dataType{
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    
    //    [formatter setDateFormat:@"YYYY:MM:dd:HH:mm:ss:eeee"];
    [formatter setDateFormat:@"YYYY:MM:dd:HH:mm:ss"];
    
    NSDate *datenow = [NSDate date];
    
    NSString *currentTimeString = [formatter stringFromDate:datenow];
    
    NSLog(@"currentTimeString =  %@",currentTimeString);
    
    NSArray *arr = [currentTimeString componentsSeparatedByString:@":"];
    
    switch (dataType) {
        case SHDataType_Year:
        {
            NSString *strYearWhole = arr[0];
            NSString *strYearNeed = [strYearWhole substringFromIndex:2];
            NSString *strHexYear = [[ToolHexManager sharedManager] converIntToHex:[strYearNeed intValue]];
            NSData *dataYear = [self dataWithHexString:strHexYear];
            return dataYear;
        }
            break;
        case SHDataType_Month:
        {
            NSString *strMonthWhole = arr[1];
            NSString *strHexMonth = [[ToolHexManager sharedManager] converIntToHex:[strMonthWhole intValue]];
            NSData *dataMonth = [self dataWithHexString:strHexMonth];
            return dataMonth;
        }
            break;
        case SHDataType_Day:
        {
            NSString *strDay = arr[2];
            NSString *strHexDay = [[ToolHexManager sharedManager] converIntToHex:[strDay intValue]];
            NSData *dataDay = [self dataWithHexString:strHexDay];
            return dataDay;
        }
            break;
        case SHDataType_Hour:
        {
            NSString *strHour = arr[3];
            NSString *strHexHour = [[ToolHexManager sharedManager] converIntToHex:[strHour intValue]];
            NSData *dataHour = [self dataWithHexString:strHexHour];
            return dataHour;
        }
            break;
        case SHDataType_Minute:
        {
            NSString *strMinute = arr[4];
            NSString *strHexMinute = [[ToolHexManager sharedManager] converIntToHex:[strMinute intValue]];
            NSData *dataMinute = [self dataWithHexString:strHexMinute];
            return dataMinute;
        }
            break;
        case SHDataType_Second:
        {
            NSString *strSecond = arr[5];
            NSString *strHexSecond = [[ToolHexManager sharedManager] converIntToHex:[strSecond intValue]];
            NSData *dataSecond = [self dataWithHexString:strHexSecond];
            return dataSecond;
        }
            break;
            
        default:
            return nil;
            break;
    }
}

- (NSData *)weekdayStringFromDate {
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    
    [formatter setDateFormat:@"YYYY-MM-dd"];
    
    NSDate *datenow = [NSDate date];
    
    NSArray *weekdays = [NSArray arrayWithObjects: [NSNull null], @"周日", @"周一", @"周二", @"周三", @"周四", @"周五", @"周六",nil];
    
    NSCalendar *calendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    
    NSTimeZone *timeZone = [[NSTimeZone alloc] initWithName:@"Asia/Shanghai"];
    
    [calendar setTimeZone: timeZone];
    
    NSCalendarUnit calendarUnit = NSCalendarUnitWeekday;
    
    NSDateComponents *theComponents = [calendar components:calendarUnit fromDate:datenow];
    
    NSLog(@"今天是:%@------%ld",[weekdays objectAtIndex:theComponents.weekday],(long)theComponents.weekday);
    
    
    NSString *strHexWeek = [[ToolHexManager sharedManager] converIntToHex:(int)theComponents.weekday];
    NSData *dataWeek = [self dataWithHexString:strHexWeek];
    return dataWeek;
    
    
}



- (NSData *)doGatewayAllowAddDevicesWithStrIdentiferModel:(NSString *)strIdentiferModel
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    //入网状态开启主动关闭超时时间为240秒，配置参数范围：0x00-0xFF，0x00禁止入网，0xFF永久允许
    NSData *dataTransi = [self dataWithHexString:strIdentiferModel];
    unsigned char *pSonIndex = (unsigned char*)[dataTransi bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 11 60 00";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
    memcpy(pWrite, pHeaderMixed, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //数据域长度
    char pSonDataLength = 0x06;
    memcpy(pWrite, &pSonDataLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //子命令标识，入网状态配置命令
    char pSonCmdIdentifer = 0x01;
    memcpy(pWrite, &pSonCmdIdentifer, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //开启，入网状态标志
    char pNetworkAccessIdentifer = 0x01;
    memcpy(pWrite, &pNetworkAccessIdentifer, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //普通模式；入网模式标志
    char pModel = 0x01;
    memcpy(pWrite, &pModel, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //入网状态开启主动关闭超时时间为240秒，配置参数范围：0x00-0xFF，0x00禁止入网，
    memcpy(pWrite,pSonIndex, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    
    //保留字节
    NSString *strReserved = @"0000";
    NSData *dataReserved = [self dataWithHexString:strReserved];
    unsigned char *pDataReserved  = (unsigned char *)[dataReserved bytes];
    memcpy(pWrite, pDataReserved, 2);
    pWrite +=2;
    iWriteBytes +=2;
    
    
    //CS
    char  packageCSDataResult = *(buffer + 2);
    for (int i = 3; i < iWriteBytes; i ++)
    {
        packageCSDataResult = packageCSDataResult + *(buffer + i);
    }
    
    char packageCS = packageCSDataResult%256;
    
    NSLog(@"cs === %x",packageCS);
    
    memcpy(pWrite, &packageCS, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //结束符 1byte
    char packageEnd = 0x23;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    NSLog(@"发送入网状态命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

#pragma mark -
#pragma mark - 服务器信息配置
-(NSData*)doSendIpToServerWithIp:(NSString *)strIp
                       localPort:(int)iLocalPort
                         outPort:(int)iOutPort
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    
    
    //本地端口
    NSString *strLocalPortHex = [[ToolHexManager sharedManager] ToHex:iLocalPort];
    NSData *dataLocalPort = [self dataWithHexString:strLocalPortHex];
    unsigned char *pLocalPort = (unsigned char *)[dataLocalPort bytes];
    
    //外部端口
    NSString *strOutPortHex = [[ToolHexManager sharedManager] ToHex:iOutPort];
    NSData *dataOutPort = [self dataWithHexString:strOutPortHex];
    unsigned char *pOutPort = (unsigned char *)[dataOutPort bytes];
    
    //strIp 转16进制
    NSString *strIpHex = [[ToolHexManager sharedManager] hexStringFromString:strIp];
    NSData *dataIp = [self dataWithHexString:strIpHex];
    unsigned char *pIp = (unsigned char *)[dataIp bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 2E 02 00";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
    memcpy(pWrite, pHeaderMixed, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //OD的新写法
    NSString *strOD = @"13C4";
    NSData *dataOD = [self dataWithHexString:strOD];
    unsigned char *pDataOD = (unsigned char *)[dataOD bytes];
    memcpy(pWrite, pDataOD, 2);
    pWrite +=2;
    iWriteBytes +=2;
    
    //可变索引
    char pDataIndex = 0xFF;
    memcpy(pWrite, &pDataIndex, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //长度
    char pSonDataLength = 0x20;
    memcpy(pWrite, &pSonDataLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //写入第2，3，7项
    NSString *strOther = @"0000004c";
    NSData *dataOther = [self dataWithHexString:strOther];
    unsigned char *pDataOther = (unsigned char *)[dataOther bytes];
    memcpy(pWrite, pDataOther, 4);
    pWrite +=4;
    iWriteBytes +=4;
    
    //本地端口
    memcpy(pWrite, pLocalPort, dataLocalPort.length);
    pWrite +=dataLocalPort.length;
    iWriteBytes += dataLocalPort.length;
    
    //外部端口
    memcpy(pWrite, pOutPort, dataLocalPort.length);
    pWrite +=dataLocalPort.length;
    iWriteBytes += dataLocalPort.length;
    
    //ip 或者域名
    memcpy(pWrite, pIp, dataIp.length);
    pWrite +=dataIp.length;
    iWriteBytes += dataIp.length;

    //CS
    char  packageCSDataResult = *(buffer + 2);
    for (int i = 3; i < iWriteBytes; i ++)
    {
        packageCSDataResult = packageCSDataResult + *(buffer + i);
    }
    
    char packageCS = packageCSDataResult%256;
    
    NSLog(@"cs === %x",packageCS);
    
    memcpy(pWrite, &packageCS, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //结束符 1byte
    char packageEnd = 0x23;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    NSLog(@"发送设置网关ip和端口命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}




//发送配置远程地址、远程端口、本地端口的指令
- (void)sendCMDToChangeRemoteIpLocalPortAndRemotePortWithGatewayZigbeeMacWithGatewayMacAddr:(NSString *)strMacAddr {
    
    NSString *gatewayZigbeeMac1 = @"00000000";
    if (strMacAddr.length != 0) {
        gatewayZigbeeMac1 = strMacAddr;
    }else{
        gatewayZigbeeMac1 = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    }
    
    
    NSString *gatewayZigbeeMac = [[gatewayZigbeeMac1 lowercaseString] stringByReplacingOccurrencesOfString:@" " withString:@""];
    //    NSString *localPortString = [HLDataUtil ToHex:8686];
    NSString *localPortString = [HLDataUtil getHexByDecimal:[NSDecimalNumber decimalNumberWithString:@"8686"]].lowercaseString;
    if (localPortString.length < 4) {
        NSString *zero;
        switch (localPortString.length) {
            case 3:
                zero = @"0";
                break;
            case 2:
                zero = @"00";
            case 1:
                zero = @"000";
            default:
                //                zero = @"0000";
                break;
        }
        localPortString = [zero stringByAppendingString:localPortString];
    }
    //    NSString *remotePortString = [HLDataUtil ToHex:33168];
    NSString *remotePortString = [HLDataUtil getHexByDecimal:[NSDecimalNumber decimalNumberWithString:@"7002"]].lowercaseString;
    if (remotePortString.length < 4) {
        NSString *zero;
        switch (remotePortString.length) {
            case 3:
                zero = @"0";
                break;
            case 2:
                zero = @"00";
            case 1:
                zero = @"000";
            default:
                //                zero = @"0000";
                break;
        }
        remotePortString = [zero stringByAppendingString:remotePortString];
    }
    NSString *strCloudIp = cloudIP;
    NSString *ipString = [HLDataUtil stringWithData:[strCloudIp dataUsingEncoding:NSUTF8StringEncoding]];
    if (ipString.length < 48) {
        NSInteger len = 48 - ipString.length;
        NSString *zeros = @"";
        for (int i = 0; i < len; i++) {
            zeros = [zeros stringByAppendingString:@"0"];
        }
        ipString = [ipString stringByAppendingString:zeros];
    }
    NSString *tmp = [NSString stringWithFormat:@"0000004c%@%@%@", localPortString, remotePortString, ipString];
    NSInteger tmplength = [HLDataUtil dataWithHexString:tmp].length;
    NSString *tmpLengthString = [HLDataUtil getHexByDecimal:[NSDecimalNumber decimalNumberWithString:[NSString stringWithFormat:@"%ld", (long)tmplength]]].lowercaseString;
    if (tmpLengthString.length < 2) {
        tmpLengthString = [@"0" stringByAppendingString:tmpLengthString];
    }
    NSString *cmd = [NSString stringWithFormat:@"0200%@13c4ff%@%@", gatewayZigbeeMac.lowercaseString, tmpLengthString.lowercaseString, tmp.lowercaseString];
    NSData *cmdData = [HLDataUtil dataWithHexString:cmd];
    NSInteger cmdLength = cmdData.length;
    NSString *cmdLengthString = [HLDataUtil getHexByDecimal:[NSDecimalNumber decimalNumberWithString:[NSString stringWithFormat:@"%ld", (long)cmdLength]]].lowercaseString;
    NSString *csString = [HLDataUtil stringWithData:[HLOrderUtil cs:cmdData]];
    NSLog(@"cmd: %@\nlocalport:%@, remoteport:%@, ipstring:%@, tmplengthString:%@ mac为%@",  cmd,localPortString, remotePortString, ipString, tmpLengthString, gatewayZigbeeMac);
    NSString *totalString = [NSString stringWithFormat:@"2a%@%@%@23", cmdLengthString.lowercaseString, cmd, csString];
    NSLog(@"配置远程地址：%@", totalString);
    NSData *data = [HLDataUtil dataWithHexString:totalString];
    [[NetworkEngine shareInstance] sendRequestData:data];
}

#pragma mark -
#pragma mark - 读取网关wifi信息的命令
- (void)doReadGatewayWifiDetailsWithGatewayMacAddr:(NSString *)strGatewayMacAddr
{
    NSString *gatewayZigbeeMac1 = @"00000000";;
    if (strGatewayMacAddr.length != 0) {
        gatewayZigbeeMac1 = strGatewayMacAddr;
    }else{
        gatewayZigbeeMac1 = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    }
    
    NSString *macString = [[gatewayZigbeeMac1 lowercaseString] stringByReplacingOccurrencesOfString:@" " withString:@""];
    NSString *csString = [NSString stringWithFormat:@"0100%@13c4ff0000004c", macString];
    NSData *csData = [HLOrderUtil cs:[HLDataUtil dataWithHexString:csString]];
    NSString *cmdString = [NSString stringWithFormat:@"2a110100%@13c4ff0000004c%@23", macString, [HLDataUtil stringWithData: csData]];
    NSLog(@"%@", cmdString);
    NSData *data = [HLDataUtil dataWithHexString:cmdString];
    [[NetworkEngine shareInstance] sendRequestData:data];
}


#pragma mark -
#pragma mark -此时再发一个网关复位的指令，这个指令没有回调
- (void)deResetGatewayOrderWithGatewayMacAddr:(NSString *)strGatewayMacAddr
{
    NSString *gatewayZigbeeMac1 = @"00000000";;
    if (strGatewayMacAddr.length != 0) {
        gatewayZigbeeMac1 = strGatewayMacAddr;
    }else{
        gatewayZigbeeMac1 = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    }
    NSString *cmdString = [NSString stringWithFormat:@"2200%@%@", gatewayZigbeeMac1, gatewayZigbeeMac1];
    NSData *cmdData = [HLDataUtil dataWithHexString:cmdString];
    NSString *csString = [HLDataUtil stringWithData:[HLOrderUtil cs:cmdData]];
    NSInteger cmdDataLength = cmdData.length;
    NSString *lengthString = [HLDataUtil getHexByDecimal:[NSDecimalNumber decimalNumberWithString:[NSString stringWithFormat:@"%d", cmdDataLength]]];
    if (lengthString.length < 2) {
        lengthString = [@"0" stringByAppendingString:lengthString];
    }
    NSString *realCmd = [NSString stringWithFormat:@"2a%@%@%@23", lengthString, cmdString, csString].lowercaseString;
    NSLog(@"复位指令为 %@", realCmd);
    NSData *realCmdData = [HLDataUtil dataWithHexString:realCmd];
    [[NetworkEngine shareInstance] sendRequestData:realCmdData];
}


@end
