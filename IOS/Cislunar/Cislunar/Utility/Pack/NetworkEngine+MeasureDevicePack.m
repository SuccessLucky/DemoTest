//
//  NetworkEngine+MeasureDevicePack.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine+MeasureDevicePack.h"

@implementation NetworkEngine (MeasureDevicePack)

#pragma mark - 计量插座
- (NSData *)doGetMeasureDeviceControlWithTargetAddr:(NSString *)strTargetAddr
                                             device:(SHModelDevice *)device
                                       controlState:(NSString *)controlState
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 1B 02 01";
    NSString *strSonOptions  = @"00 02 00 00";
    
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
    NSData *dataSonOptions = [self dataWithHexString:strSonOptions];
    unsigned char *pSonOptions = (unsigned char *)[dataSonOptions bytes];
    
    
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
    NSString *strOD = @"0F C8";
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
    char pSonDataLength = 0x05;
    memcpy(pWrite, &pSonDataLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //子选择项 4byte
    memcpy(pWrite,pSonOptions, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    NSData *dataControlState = [self dataWithHexString:controlState];
    unsigned char *pControlState = (unsigned char *)[dataControlState bytes];
    memcpy(pWrite, pControlState, 1);
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

@end
