//
//  NetworkEngine+Pack.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine+Pack.h"

@implementation NetworkEngine (Pack)

#pragma mark - private
- (NSData *)dataWithHexString:(NSString *)str
{
    NSString *strHex = [str stringByReplacingOccurrencesOfString:@" " withString:@""];
    NSData *data = [[ToolHexManager sharedManager] convertHexStrToData:strHex];
    return data;
}


#pragma mark -
#pragma mark - 刷新
- (NSData *)doGetRefreshDeviceStatue
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

- (NSData *)doGetDeviceTokenData
{
    NSString *strDeviceToken = [[SHAppInfoManager shareInstance] handleGetDeviceToken];
    NSString *strHex = [strDeviceToken stringByReplacingOccurrencesOfString:@" " withString:@""];
    NSData *dataDeviceToken = [[ToolHexManager sharedManager] convertHexStrToData:strHex];
    return dataDeviceToken;
}


#pragma mark - 心跳
#pragma mark -2.0.8网关维持和服务器通信的心跳帧
- (NSData *)doGetGatewayHeart
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 10 02 00";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    memcpy(pWrite, pHeaderMixed, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=4;
    iWriteBytes += 4;
    
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
    char deviceIndex = 0x06;
    memcpy(pWrite,&deviceIndex,1);
    pWrite ++;
    iWriteBytes ++;
    
    //indexLength 1byte
    char indexLength = 0x02;
    memcpy(pWrite, &indexLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //信道号
    NSString *strSignalRoad = @"86 87";
    NSData *dataSignalRoad = [self dataWithHexString:strSignalRoad];
    unsigned char *pSignalRoad = (unsigned char *)[dataSignalRoad bytes];
    memcpy(pWrite, pSignalRoad, 2);
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
    return dataWrite;
}


- (NSData *)doAskAll0FAADeviceState
{
    NSString *strOriginal = @"2A 0D 01 FF 00 00 00 00 00 00 00 00 0F AA 00 B9 23";
    NSData *data = [self dataWithHexString:strOriginal];
    LLog([NSString stringWithFormat:@"发送刷新所有设备状态的命令:\n\n%@\n\n",data]);
    return data;
}



#pragma mark - 2.5 电话设定
/*
 *  设定网关电话号码子索引： 00 00 00 10
 *  设定报警电话号码：00 00 00 20
 *  短信号码1-00 00 00 40，
 *  短信号码2-00 00 00 80，
 *  短信号码3-00 00 01 00
 *  短信号码4-00 00 02 00，
 *  短信号码5-00 00 04 00
 */
#pragma mark- 2.5.1 设定网关电话号码  2.5.2 设定报警电话号码 2.5.3 设定报警短信号码

- (NSData *)doGetSetGatewayPhoneNum:(NSString *)strTel sonIndex:(NSString *)strSonIndex
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    //电话号码
    const char * packageTel = [strTel cStringUsingEncoding:NSUTF8StringEncoding];
    
    //子索引
    NSData *dataSonIndex = [self dataWithHexString:strSonIndex];
    unsigned char *pSonIndex = (unsigned char*)[dataSonIndex bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 1E 02 00";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
    memcpy(pWrite, pHeaderMixed, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //OD的新写法
    NSString *strOD = @"8201";
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
    char pSonDataLength = 0x10;
    memcpy(pWrite, &pSonDataLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite,pSonIndex, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    //电话号码
    memcpy(pWrite, packageTel, 11);
    pWrite +=11;
    iWriteBytes += 11;
    
    //预留
    char packageReserved = 0x46;
    memcpy(pWrite, &packageReserved, 1);
    pWrite ++;
    iWriteBytes ++;
    
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
    NSLog(@"发送电话设定命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

//#pragma mark -撤防 布防
//#pragma mark - 撤防
//- (NSData *)doGetDismissAlertData
//{
//    NSString *strOriginal = @"2a 13 02 00 00 00 00 00 00 00 00 00 82 01 ff 05 00 00 00 02 04 8f 23";
//    NSData *data = [self dataWithHexString:strOriginal];
//    NSLog(@"发送撤防命令:\n\n%@\n\n",data);
//    return data;
//}
//
//#pragma mark - 布防
//- (NSData *)doGetSetAlertData
//{
//    NSString *strOriginal = @"2a 13 02 00 00 00 00 00 00 00 00 00 82 01 ff 05 00 00 00 02 03 8e 23";
//    NSData *data = [self dataWithHexString:strOriginal];
//    NSLog(@"发送布防防命令:\n\n%@\n\n",data);
//    return data;
//}
//



#pragma mark -
#pragma mark - 连接cloudSever
- (NSData *)doGetSendTelPhoneInfoToCloudSeverDataWithGatewayAddr
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    
    NSData *tockenData = [self doGetDeviceTokenData];
    unsigned char *deviceToken = (unsigned char *)[tockenData bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //帧头 1 byte
    char packageStart = 0x55;
    memcpy(pWrite, &packageStart, 1);
    pWrite ++;
    iWriteBytes ++;
    
    
    //帧长 1 byte
    char packageLength = 0x49;
    memcpy(pWrite, &packageLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //Cmd ID 1 byte
    char packageCmdID = 0x01;
    memcpy(pWrite, &packageCmdID, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //网关地址 8 bytes
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    
    //token 33byte
    memcpy(pWrite, deviceToken, tockenData.length);
    pWrite = pWrite + tockenData.length;
    iWriteBytes += tockenData.length;
    
    //补全token  32bttes
    memset(pWrite, 0, (64-tockenData.length));
    pWrite +=(64-tockenData.length);
    iWriteBytes += (64-tockenData.length);
    
    
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
    char packageEnd = 0xAA;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes ++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    NSLog(@"发送手机登记信息命令:\n\n%@\n\n",dataWrite);
    
    return dataWrite;
}

@end
