//
//  NetworkEngine+WIFIBaseInfo.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine+WIFIBaseInfo.h"

@implementation NetworkEngine (WIFIBaseInfo)

//#pragma mark - 从本地获取网关的MAC地址
//- (NSData *)doGetGatewayZigbeeMacAddrFromLocal
//{
//    NSString *strMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
//    NSString *strHex = [strMacAddr stringByReplacingOccurrencesOfString:@" " withString:@""];
//    NSData *dataGatewayZigbeeMacAddr = [[ToolHexManager sharedManager] convertHexStrToData:strHex];
//    return dataGatewayZigbeeMacAddr;
//}

- (NSData *)doGetSetReadGatewayWifiInfoData
{
    //    NSString *strOriginal = @"2A 11 01 00 FD A3 C6 0A 00 6F 0D 00 13 C4 FF 00 00 00 46 09 23";
    //    NSData *data = [self dataWithHexString:strOriginal];
    //    NSLog(@"发送读取WIFI模块基本信息:\n\n%@\n\n",data);
    //    return data;
    
    
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
    char pAllLength = 0x11;
    memcpy(pWrite, &pAllLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //命令标识
    char packageCmdID = 0x01;
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
    
    //OD的新写法
    NSString *strOD = @"13C4";
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
    
    
    //读取第2，3，7项
    NSString *strReadTemp = @"00000046";
    NSData *dataReadTemp = [self dataWithHexString:strReadTemp];
    unsigned char *pDataReadTemp = (unsigned char *)[dataReadTemp bytes];
    memcpy(pWrite, pDataReadTemp, 4);
    pWrite +=4;
    iWriteBytes +=4;
    
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






@end
