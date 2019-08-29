//
//  NetworkEngine+Device0FE6InfoPack.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine+Device0FE6InfoPack.h"

@implementation NetworkEngine (Device0FE6InfoPack)

#pragma mark - 2.7 协议转发（OD索引=0FE6）

#pragma mark - private  针对红外转发器的私有方法
- (NSData *)doHandleTheInfraredProtolWithCMD:(NSString *)strCMD studyCode:(int)studyCode
{
    //cmd 88 和 86
    NSData *dataCMD = [self dataWithHexString:strCMD];
    unsigned char *pCMD = (unsigned char *)[dataCMD bytes];
    
    unsigned char PAR1 = (char)(studyCode % 256);
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //BYTE1 1 byte
    memcpy(pWrite, pCMD, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //BYTE2 1 byte
    memcpy(pWrite, &PAR1, 1);
    pWrite ++;
    iWriteBytes ++;
    
    
    //BYTE3
    char pPAR2 = 0x00;
    memcpy(pWrite, &pPAR2, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //BYTE4
    char pPAR3 = 0x00;
    memcpy(pWrite, &pPAR3, 1);
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
    NSData *data = [NSData dataWithBytes:buffer length:iWriteBytes];
    return data;
}
#pragma mark 2.7.1 红外转发器-学习红外信号
- (NSData *)doGetInfraredStudyDataTargetAddr:(NSString *)strTargetAddr studyCode:(int)studyCode
{
    NSData *dataInfrared = [self doHandleTheInfraredProtolWithCMD:kInfraredStudyCMD studyCode:studyCode];
    unsigned char *pInfrared = (unsigned char *)[dataInfrared bytes];
    
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 18 07 01";
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
    
    //被转发协议的长度
    char pForwardProtocol = 0x05;
    memcpy(pWrite, &pForwardProtocol, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pInfrared, 5);
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
    
    LLog([NSString stringWithFormat:@"发送红外学习命令:\n\n%@\n\n",dataWrite]);
    return dataWrite;
}

#pragma mark 2.7.1 红外转发器-发射已经学习红外信号
- (NSData *)doGetInfraredSendDataTargetAddr:(NSString *)strTargetAddr studyCode:(int)studyCode
{
    NSData *dataInfrared = [self doHandleTheInfraredProtolWithCMD:kInfraredSendCMD studyCode:studyCode];
    unsigned char *pInfrared = (unsigned char *)[dataInfrared bytes];
    
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 18 07 01";
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
    
    //被转发协议的长度
    char pForwardProtocol = 0x05;
    memcpy(pWrite, &pForwardProtocol, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pInfrared, 5);
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
    
    LLog([NSString stringWithFormat:@"发送红外命令:\n\n%@\n\n",dataWrite]);
    return dataWrite;
}

#pragma mark - 电动窗帘
- (NSData *)doGetElectricCurtainsOrWindowDataWithModelDevice:(SHModelDevice *)device
                                                  actionType:(ElectricTransimitObjecActionType)actionType
                                                    location:(NSString *)strHexLocation
{
    //网关Zigbee的MacAddr
    NSString *strTargetAddr = device.strDevice_mac_address;
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 19 07 01";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
    //被转发数据域
    NSData *dataTransmit = [self doHanleGetElectricCurtainsOrWindowDevice:device actionType:actionType location:strHexLocation];
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
    char pForwardProtocol = 0x06;
    memcpy(pWrite, &pForwardProtocol, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pTransmit, 6);
    pWrite +=6;
    iWriteBytes += 6;
    
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
    LLog([NSString stringWithFormat:@"发送电动窗帘命令:\n\n%@\n\n",dataWrite]);
    return dataWrite;
}


//-(NSData *)doHanleGetElectricCurtainsOrWindowDevice:(SHModelDevice *)device
//                                           actionType:(ElectricTransimitObjecActionType) actionType
//                                             location:(NSString *)strHexLocation
//{
//    if ([device.strDevice_category isEqualToString:@"10"]) {
//        //窗帘
//
//        NSString *strHex;
//        //电动窗帘
//        switch (actionType) {
//            case PositiveRotationType:
//            {
//                //正传
//                strHex = @"FF 00 00 3D 4F 50";
//            }
//                break;
//            case ReverseRotationType:
//            {
//                //反传
//                strHex = @"FF 00 00 3D 43 4C";
//            }
//                break;
//            case StopRotationType:
//            {
//                //停止
//                strHex = @"FF 00 00 3D 53 54";
//            }
//                break;
//            case RestartTypeType:
//            {
//                //重启
//                strHex = @"FF 00 00 3D 52 53";
//            }
//                break;
//            case ReadStatusType:
//            {
//                //状态读取
//                strHex = @"FF 00 00 3D 50 4F";
//            }
//                break;
//            case LocationSWType:
//            {
//                //开启到指定位置
//                strHex = [NSString stringWithFormat:@"FF 00 00 3D %@ 25",strHexLocation];
//            }
//                break;
//
//            default:
//                break;
//        }
//        NSData *data = [self dataWithHexString:strHex];
//        return data;
//
//    }else{
//
//        //11平移开窗器
//        NSString *strHex;
//        //平移开窗器
//        switch (actionType) {
//            case PositiveRotationType:
//            {
//                //正传
//                strHex = @"FF 01 00 3D 4F 50";
//            }
//                break;
//            case ReverseRotationType:
//            {
//                //反传
//                strHex = @"FF 01 00 3D 43 4C";
//            }
//                break;
//            case StopRotationType:
//            {
//                //停止
//                strHex = @"FF 01 00 3D 53 54";
//            }
//                break;
//            case RestartTypeType:
//            {
//                //重启
//                strHex = @"FF 01 00 3D 52 53";
//            }
//                break;
//            case ReadStatusType:
//            {
//                //状态读取
//                strHex = @"FF 01 00 3D 50 4F";
//            }
//                break;
//            case LocationSWType:
//            {
//                //开启到指定位置
//                strHex = [NSString stringWithFormat:@"FF 01 00 3D %@ 25",strHexLocation];
//            }
//                break;
//
//            default:
//                break;
//        }
//        NSData *data = [self dataWithHexString:strHex];
//        return data;
//
//    }
//}

-(NSData *)doHanleGetElectricCurtainsOrWindowDevice:(SHModelDevice *)device
                                         actionType:(ElectricTransimitObjecActionType) actionType
                                           location:(NSString *)strHexLocation
{
    NSString *strHexIdentifer;
    NSString *strHex;
    
    if ([device.strDevice_category isEqualToString:@"10"]) {
        //窗帘
        strHexIdentifer = @"00";
    }else{
        //11平移开窗器
        strHexIdentifer = @"01";
    }
    
    switch (actionType) {
        case PositiveRotationType:
        {
            //正传
            strHex = [NSString stringWithFormat:@"FF %@ 00 3D 4F 50",strHexIdentifer];
        }
            break;
        case ReverseRotationType:
        {
            //反传
            strHex = [NSString stringWithFormat:@"FF %@ 00 3D 43 4C",strHexIdentifer];
        }
            break;
        case StopRotationType:
        {
            //停止
            strHex = [NSString stringWithFormat:@"FF %@ 00 3D 53 54",strHexIdentifer];
        }
            break;
        case RestartTypeType:
        {
            //重启
            strHex = [NSString stringWithFormat:@"FF %@ 00 3D 52 53",strHexIdentifer];
        }
            break;
        case ReadStatusType:
        {
            //状态读取
            strHex = [NSString stringWithFormat:@"FF %@ 00 3D 50 4F",strHexIdentifer];
        }
            break;
        case LocationSWType:
        {
            //开启到指定位置
            strHex = [NSString stringWithFormat:@"FF %@ 00 3D %@ 25",strHexIdentifer,strHexLocation];
        }
            break;
            
        default:
            break;
    }
    NSData *data = [self dataWithHexString:strHex];
    return data;
}


#pragma mark -
#pragma mark - 净水器
- (NSData *)doGetGetWaterPurifierSendDataWithTargetMacAddr:(NSString *)strTargetAddr waterPurifierFunctionType:(SHWaterPurifierFunctionType)type
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed;
    //被转发数据域
    NSString *strDataTransmit;
    //名字
    NSString *strName;
    
    switch (type) {
        case SHWaterPurifierFunctionType_GetStatue:
        {
            strHeaderMixed = @"2A 1D 07 01";
            strDataTransmit = @"FF FF 00 06 03 00 00 00 02 0B";
            strName = @"读取设备状态";
        }
            break;
        case SHWaterPurifierFunctionType_Water:
        {
            strHeaderMixed = @"2A 36 07 01";
            strDataTransmit = @"FF FF 00 1F 03 00 00 00 01 00 20 00 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 44";
            strName = @"下发冲洗";
        }
            break;
        case SHWaterPurifierFunctionType_Heart:
        {
            strHeaderMixed = @"2A 1B 07 01";
            strDataTransmit = @"FF FF 00 05 07 00 00 0C";
            strName = @"心跳";
        }
            break;
        case SHWaterPurifierFunctionType_FilterElementOne:
        {
            strHeaderMixed = @"2A 36 07 01";
            strDataTransmit = @"FF FF 00 1F 03 00 00 00 01 00 40 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 63";
            strName = @"清滤芯1";
        }
            break;
        case SHWaterPurifierFunctionType_FilterElementSec:
        {
            strHeaderMixed = @"2A 36 07 01";
            strDataTransmit = @"FF FF 00 1F 03 00 00 00 01 00 80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 A3";
            strName = @"清滤芯2";
        }
            break;
        case SHWaterPurifierFunctionType_FilterElementThird:
        {
            strHeaderMixed = @"2A 36 07 01";
            strDataTransmit = @"FF FF 00 1F 03 00 00 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 24";
            strName = @"清滤芯3";
        }
            break;
        case SHWaterPurifierFunctionType_FilterElementFourth:
        {
            strHeaderMixed = @"2A 36 07 01";
            strDataTransmit = @"FF FF 00 1F 03 00 00 00 01 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 25";
            strName = @"清滤芯4";
        }
            break;
        case SHWaterPurifierFunctionType_FilterElementFifth:
        {
            strHeaderMixed = @"2A 36 07 01";
            strDataTransmit = @"FF FF 00 1F 03 00 00 00 01 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 27";
            strName = @"清滤芯5";
        }
            break;
        case SHWaterPurifierFunctionType_Off:
        {
            strHeaderMixed = @"2A 36 07 01";
            strDataTransmit = @"FF FF 00 1F 03 00 00 00 01 00 02 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 27";
            strName = @"净水器关机";
        }
            break;
        case SHWaterPurifierFunctionType_On:
        {
            strHeaderMixed = @"2A 36 07 01";
            strDataTransmit = @"FF FF 00 1F 03 00 00 00 01 00 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 25";
            strName = @"净水器开机";
        }
            break;
        default:
            break;
    }
    
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
    
    NSData *dataTransmit = [self dataWithHexString:strDataTransmit];
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
    
    switch (type) {
        case SHWaterPurifierFunctionType_GetStatue:
        {
            //被转发协议的长度
            char pForwardProtocol = 0x0A;
            memcpy(pWrite, &pForwardProtocol, 1);
            pWrite ++;
            iWriteBytes ++;
            
            memcpy(pWrite, pTransmit, 10);
            pWrite +=10;
            iWriteBytes += 10;
        }
            break;
        case SHWaterPurifierFunctionType_Water:
        {
            //被转发协议的长度
            char pForwardProtocol = 0x23;
            memcpy(pWrite, &pForwardProtocol, 1);
            pWrite ++;
            iWriteBytes ++;
            
            memcpy(pWrite, pTransmit, 35);
            pWrite +=35;
            iWriteBytes += 35;
        }
            break;
        case SHWaterPurifierFunctionType_Heart:
        {
            //被转发协议的长度
            char pForwardProtocol = 0x08;
            memcpy(pWrite, &pForwardProtocol, 1);
            pWrite ++;
            iWriteBytes ++;
            
            memcpy(pWrite, pTransmit, 8);
            pWrite +=8;
            iWriteBytes += 8;
        }
            break;
        case SHWaterPurifierFunctionType_FilterElementOne:
        {
            //被转发协议的长度
            char pForwardProtocol = 0x23;
            memcpy(pWrite, &pForwardProtocol, 1);
            pWrite ++;
            iWriteBytes ++;
            
            memcpy(pWrite, pTransmit, 35);
            pWrite +=35;
            iWriteBytes += 35;
        }
            break;
        case SHWaterPurifierFunctionType_FilterElementSec:
        {
            //被转发协议的长度
            char pForwardProtocol = 0x23;
            memcpy(pWrite, &pForwardProtocol, 1);
            pWrite ++;
            iWriteBytes ++;
            
            memcpy(pWrite, pTransmit, 35);
            pWrite +=35;
            iWriteBytes += 35;
        }
            break;
        case SHWaterPurifierFunctionType_FilterElementThird:
        {
            //被转发协议的长度
            char pForwardProtocol = 0x23;
            memcpy(pWrite, &pForwardProtocol, 1);
            pWrite ++;
            iWriteBytes ++;
            
            memcpy(pWrite, pTransmit, 35);
            pWrite +=35;
            iWriteBytes += 35;
        }
            break;
        case SHWaterPurifierFunctionType_FilterElementFourth:
        {
            //被转发协议的长度
            char pForwardProtocol = 0x23;
            memcpy(pWrite, &pForwardProtocol, 1);
            pWrite ++;
            iWriteBytes ++;
            
            memcpy(pWrite, pTransmit, 35);
            pWrite +=35;
            iWriteBytes += 35;
        }
            break;
        case SHWaterPurifierFunctionType_FilterElementFifth:
        {
            //被转发协议的长度
            char pForwardProtocol = 0x23;
            memcpy(pWrite, &pForwardProtocol, 1);
            pWrite ++;
            iWriteBytes ++;
            
            memcpy(pWrite, pTransmit, 35);
            pWrite +=35;
            iWriteBytes += 35;
        }
            break;
        case SHWaterPurifierFunctionType_Off:
        {
            //被转发协议的长度
            char pForwardProtocol = 0x23;
            memcpy(pWrite, &pForwardProtocol, 1);
            pWrite ++;
            iWriteBytes ++;
            
            memcpy(pWrite, pTransmit, 35);
            pWrite +=35;
            iWriteBytes += 35;
        }
            break;
        case SHWaterPurifierFunctionType_On:
        {
            //被转发协议的长度
            char pForwardProtocol = 0x23;
            memcpy(pWrite, &pForwardProtocol, 1);
            pWrite ++;
            iWriteBytes ++;
            
            memcpy(pWrite, pTransmit, 35);
            pWrite +=35;
            iWriteBytes += 35;
        }
            break;
        default:
            break;
    }
    
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
    NSLog(@"发送净水器读取设备状态命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

#pragma mark - 豪力士锁
- (NSData *)doGetLockHLSWithModelDevice:(SHModelDevice *)device strHexHLSCmdID:(NSString *)strHexHLSCmdID
{
    //网关Zigbee的MacAddr
    NSString *strTargetAddr = device.strDevice_mac_address;
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 1A 07 01";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
    //被转发数据域
    NSData *dataTransmit = [self doGetHLSPrivateDataWithHLSLockModeID:device.strDevice_other_status queryID:strHexHLSCmdID];
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
    char pForwardProtocol = 0x07;
    memcpy(pWrite, &pForwardProtocol, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pTransmit, 7);
    pWrite +=7;
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
    NSLog(@"发送豪力士锁命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

- (NSData *)doGetHLSPrivateDataWithHLSLockModeID:(NSString *)strHexModelID queryID:(NSString *)strHexCmID
{
    NSString *strModelIDHexBig =  [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexModelID];
    NSData *dataModeID = [self dataWithHexString:strModelIDHexBig];
    unsigned char *pModeID = (unsigned char *)[dataModeID bytes];
    
    NSData *dataCmID = [self dataWithHexString:strHexCmID];
    unsigned char *pCmID = (unsigned char*)[dataCmID bytes];
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    
    //OD 2 bytes
    char pHeader = 0x02;
    memcpy(pWrite, &pHeader, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pModeID, 3);
    pWrite +=3;
    iWriteBytes += 3;
    
    memcpy(pWrite, pCmID, 1);
    pWrite +=1;
    iWriteBytes += 1;
    
    //CS 1bytes
    char  packageCSDataResult = *(buffer + 1);
    for (int i = 2; i < iWriteBytes; i ++)
    {
        packageCSDataResult = packageCSDataResult + *(buffer + i);
    }
    
    char packageCS = packageCSDataResult%256;
    
    memcpy(pWrite, &packageCS, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //结束符 1byte
    char packageEnd = 0xFF;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    return dataWrite;
}


#pragma mark - 新风系统
/*
 strFuctionIdentifer:功能码（F0，02，03，04，05，06，07，08，09，0a）
 strDataRangeIdentifer:数据---------------------------------
 ↓
 描述               功能码         数据长度            数据1（取值范围）
 设置开关机风         0x02          0x01              0x00:关机;0x01:开机;
 设置加热模风         0x03          0x01              0x00：关闭；0x01:开启;
 设置工作模式         0x04          0x01              0x00：手动；0x01:自动;0x02:定时;
 设置循环模式         0x05          0x01              0x00：外循环；0x01:内循环;0x02:自动;
 设置风速模式         0x06          0x01              20-30共11档，手动模式才有效
 设置进回风比         0x07          0x01              0x00-10/4, 0x01-10/5, 0x02-10/6
 0x03-10/7, 0x04-10/8, 0x05-10/10
 0x06-10/0单进风,  0x07-0/10单排风
 初滤使用时间清零      0x08          0x01              0x01-清零(滤网报警时才有效)
 集尘箱使用时间清零    0x09          0x01              0x01-清零(滤网报警时才有效)
 高滤使用时间清零      0x0a          0x01              0x01-清零(滤网报警时才有效)
 */


- (NSData *)doSendXFTest
{
    NSString *strHeaderMixed = @"2a 1a 07 01 00 00 00 00 00 00 00 00 f6 aa e8 0d 00 6f 0d 00 07 f1 f1 02 01 00 03 7e 86 23";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    NSLog(@"发送新风系统命令:\n\n%@\n\n",dataHeaderMixed);
    return  dataHeaderMixed;
}

- (NSData *)doSendXinFengDataWithModelDevice:(SHModelDevice *)device
                           functionIdentifer:(NSString *)strFuctionIdentifer
                          dataRangeIdentifer:(NSString *)strDataRangeIdentifer
{
    //网关Zigbee的MacAddr
    NSString *strTargetAddr = device.strDevice_mac_address;
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 1A 07 01";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
    //被转发数据域
    NSData *dataTransmit;
    if ([strFuctionIdentifer isEqualToString:@"F0"]) {
        dataTransmit = [self doGetXFDeviceState][0];
    }else if ([strFuctionIdentifer isEqualToString:@"02"]) {
        dataTransmit = [self doSendXFOnOrOFFWithIdentifer:strDataRangeIdentifer][0];
    }else if ([strFuctionIdentifer isEqualToString:@"03"]) {
        dataTransmit = [self doSetXFHeatOrderWithIdentifer:strDataRangeIdentifer][0];
    }else if ([strFuctionIdentifer isEqualToString:@"04"]) {
        dataTransmit = [self doSetXFModeOrderWithIdentifer:strDataRangeIdentifer][0];
    }else if ([strFuctionIdentifer isEqualToString:@"05"]) {
        dataTransmit = [self doSetXFCirculationModeOrderWithIdentifer:strDataRangeIdentifer][0];
    }else if ([strFuctionIdentifer isEqualToString:@"06"]) {
        dataTransmit = [self doSetXFWindSpeedOrderWithIdentifer:strDataRangeIdentifer][0];
    }else if ([strFuctionIdentifer isEqualToString:@"07"]) {
        dataTransmit = [self doSetXFInAndOutWindScaleOrderWithIdentifer:strDataRangeIdentifer][0];
    }else if ([strFuctionIdentifer isEqualToString:@"08"]) {
        dataTransmit = [self doSetXFUseTimeZeroOrderWithIdentifer:@"08"][0];
    }else if ([strFuctionIdentifer isEqualToString:@"09"]) {
        dataTransmit = [self doSetXFUseTimeZeroOrderWithIdentifer:@"09"][0];
    }else if ([strFuctionIdentifer isEqualToString:@"0a"] || [strFuctionIdentifer isEqualToString:@"0A"]) {
        dataTransmit = [self doSetXFUseTimeZeroOrderWithIdentifer:@"0a"][0];
    }else {
        LLog(@"新风错误代码");
    }
    
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
    char pForwardProtocol = 0x07;
    memcpy(pWrite, &pForwardProtocol, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pTransmit, dataTransmit.length);
    pWrite +=dataTransmit.length;
    iWriteBytes += dataTransmit.length;
    
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
    if([strFuctionIdentifer isEqualToString:@"01"]){
        LLog([NSString stringWithFormat:@"获取新风机设备状态GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu,\n\n%@\n\n",strFuctionIdentifer,strDataRangeIdentifer,dataTransmit,(unsigned long)dataTransmit.length,dataWrite]);
        
    }else if ([strFuctionIdentifer isEqualToString:@"02"]){
        LLog([NSString stringWithFormat:@"设置开关机GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu,\n\n%@\n\n",strFuctionIdentifer,strDataRangeIdentifer,dataTransmit,(unsigned long)dataTransmit.length,dataWrite]);
        
    }else if ([strFuctionIdentifer isEqualToString:@"03"]){
        LLog([NSString stringWithFormat:@"设置开关机GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu,\n\n%@\n\n",strFuctionIdentifer,strDataRangeIdentifer,dataTransmit,(unsigned long)dataTransmit.length,dataWrite]);
        
    }else if ([strFuctionIdentifer isEqualToString:@"04"]){
        LLog([NSString stringWithFormat:@"设置工作模式GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu,\n\n%@\n\n",strFuctionIdentifer,strDataRangeIdentifer,dataTransmit,(unsigned long)dataTransmit.length,dataWrite]);
        
    }else if ([strFuctionIdentifer isEqualToString:@"05"]){
        LLog([NSString stringWithFormat:@"设置工作模式GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu,\n\n%@\n\n",strFuctionIdentifer,strDataRangeIdentifer,dataTransmit,(unsigned long)dataTransmit.length,dataWrite]);
        
    }else if ([strFuctionIdentifer isEqualToString:@"06"]){
        LLog([NSString stringWithFormat:@"设置风速GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu,\n\n%@\n\n",strFuctionIdentifer,strDataRangeIdentifer,dataTransmit,(unsigned long)dataTransmit.length,dataWrite]);
        
    }else if ([strFuctionIdentifer isEqualToString:@"07"]){
        LLog([NSString stringWithFormat:@"设置进回风比例GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu,\n\n%@\n\n",strFuctionIdentifer,strDataRangeIdentifer,dataTransmit,(unsigned long)dataTransmit.length,dataWrite]);
        
    }else if ([strFuctionIdentifer isEqualToString:@"08"]){
        LLog([NSString stringWithFormat:@"设置进回风比例GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu,\n\n%@\n\n",strFuctionIdentifer,strDataRangeIdentifer,dataTransmit,(unsigned long)dataTransmit.length,dataWrite]);
        
    }else if ([strFuctionIdentifer isEqualToString:@"09"]){
        LLog([NSString stringWithFormat:@"集尘箱使用时间清零GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu,\n\n%@\n\n",strFuctionIdentifer,strDataRangeIdentifer,dataTransmit,(unsigned long)dataTransmit.length,dataWrite]);
    }else if ([strFuctionIdentifer isEqualToString:@"1a"]){
        LLog([NSString stringWithFormat:@"高滤使用时间清零GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu,\n\n%@\n\n",strFuctionIdentifer,strDataRangeIdentifer,dataTransmit,(unsigned long)dataTransmit.length,dataWrite]);
    }
    return dataWrite;
}


#pragma mark -
#pragma mark -  1.获取新风机设备状态
- (NSArray *)doGetXFDeviceState
{
    char buffer[256];
    char *pWrite =  buffer;
    int iWriteBytes = 0; //计算总共长度
    
    char pCommand1 = 0xF1;
    memcpy(pWrite, &pCommand1, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pCommand2 = 0xF1;
    memcpy(pWrite, &pCommand2, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pCommand3 = 0xF0;
    memcpy(pWrite, &pCommand3, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pCommand4 = 0x01;
    memcpy(pWrite, &pCommand4, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pCommand5 = 0x00;
    memcpy(pWrite, &pCommand5, 1);
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
    char packageEnd = 0x7E;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    
    
    NSLog(@"发送获取新风机设备状态命令:\n\n%@\n\n",dataWrite);
    
    return @[dataWrite,@(dataWrite.length)];
}

#pragma mark - 设置开关机
//0x00:关机;0x01:开机;
//strIdentifer:@"00" 关机  @“01”开机
- (NSArray *)doSendXFOnOrOFFWithIdentifer:(NSString *)strIdentifer
{
    char buffer[256];
    char *pWrite =  buffer;
    int iWriteBytes = 0; //计算总共长度
    
    char pHedaer1 = 0xF1;
    memcpy(pWrite, &pHedaer1, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pHedaer2 = 0xF1;
    memcpy(pWrite, &pHedaer2, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pFunction = 0x02;
    memcpy(pWrite, &pFunction, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pDataLength = 0x01;
    memcpy(pWrite, &pDataLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    if ([strIdentifer isEqualToString:@"00"]) {
        LLog(@"发送新风系统关机指令");
        char pData = 0x00;
        memcpy(pWrite, &pData, 1);
        pWrite ++;
        iWriteBytes ++;
        
    }else if([strIdentifer isEqualToString:@"01"]){
        LLog(@"发送新风系统开机指令");
        char pData = 0x01;
        memcpy(pWrite, &pData, 1);
        pWrite ++;
        iWriteBytes ++;
    }
    
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
    char packageEnd = 0x7E;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    
    
    if ([strIdentifer isEqualToString:@"00"]) {
        LLog([NSString stringWithFormat:@"发送新风系统关机指令:\n\n%@\n\n ",dataWrite]);
    }else if([strIdentifer isEqualToString:@"01"]){
        LLog([NSString stringWithFormat:@"发送新风系统开机指令:\n\n%@\n\n ",dataWrite]);
    }
    
    return @[dataWrite,@(dataWrite.length)];
}


#pragma mark - 设置加热
/*
 0x00：关闭；0x01:开启;
 */
- (NSArray *)doSetXFHeatOrderWithIdentifer:(NSString *)strIdentifer
{
    char buffer[256];
    char *pWrite =  buffer;
    int iWriteBytes = 0; //计算总共长度
    
    char pHedaer1 = 0xF1;
    memcpy(pWrite, &pHedaer1, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pHedaer2 = 0xF1;
    memcpy(pWrite, &pHedaer2, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pFunction = 0x03;
    memcpy(pWrite, &pFunction, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pDataLength = 0x01;
    memcpy(pWrite, &pDataLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    if ([strIdentifer isEqualToString:@"00"]) {
        LLog(@"发送新风系统关闭加热指令");
        char pData = 0x00;
        memcpy(pWrite, &pData, 1);
        pWrite ++;
        iWriteBytes ++;
        
    }else if([strIdentifer isEqualToString:@"01"]){
        LLog(@"发送新风系统开启加热指令");
        char pData = 0x01;
        memcpy(pWrite, &pData, 1);
        pWrite ++;
        iWriteBytes ++;
    }
    
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
    char packageEnd = 0x7E;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    
    
    if ([strIdentifer isEqualToString:@"00"]) {
        LLog([NSString stringWithFormat:@"发送新风系统关闭加热指令:\n\n%@\n\n ",dataWrite]);
    }else if([strIdentifer isEqualToString:@"01"]){
        LLog([NSString stringWithFormat:@"发送新风系统开启加热指令:\n\n%@\n\n ",dataWrite]);
    }
    
    return @[dataWrite,@(dataWrite.length)];
}

#pragma mark - 设置工作模式
/*
 0x00：手动；0x01:自动;0x02:定时;
 */
- (NSArray *)doSetXFModeOrderWithIdentifer:(NSString *)strIdentifer
{
    char buffer[256];
    char *pWrite =  buffer;
    int iWriteBytes = 0; //计算总共长度
    
    char pHedaer1 = 0xF1;
    memcpy(pWrite, &pHedaer1, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pHedaer2 = 0xF1;
    memcpy(pWrite, &pHedaer2, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pFunction = 0x04;
    memcpy(pWrite, &pFunction, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pDataLength = 0x01;
    memcpy(pWrite, &pDataLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    if ([strIdentifer isEqualToString:@"00"]) {
        LLog(@"发送新风系统手动");
        char pData = 0x00;
        memcpy(pWrite, &pData, 1);
        pWrite ++;
        iWriteBytes ++;
        
    }else if([strIdentifer isEqualToString:@"01"]){
        LLog(@"发送新风系统自动");
        char pData = 0x01;
        memcpy(pWrite, &pData, 1);
        pWrite ++;
        iWriteBytes ++;
    }else if([strIdentifer isEqualToString:@"02"]){
        LLog(@"发送新风系统定时");
        char pData = 0x02;
        memcpy(pWrite, &pData, 1);
        pWrite ++;
        iWriteBytes ++;
    }
    
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
    char packageEnd = 0x7E;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    
    
    if ([strIdentifer isEqualToString:@"00"]) {
        LLog([NSString stringWithFormat:@"发送新风系统手动:\n\n%@\n\n ",dataWrite]);
    }else if([strIdentifer isEqualToString:@"01"]){
        LLog([NSString stringWithFormat:@"发送新风系统自动:\n\n%@\n\n ",dataWrite]);
    }else if([strIdentifer isEqualToString:@"02"]){
        LLog([NSString stringWithFormat:@"发送新风系统定时:\n\n%@\n\n ",dataWrite]);
    }
    
    return @[dataWrite,@(dataWrite.length)];
}

#pragma mark - 设置循环模式
/*
 0x00：外循环；0x01:内循环;0x02:自动;
 */
- (NSArray *)doSetXFCirculationModeOrderWithIdentifer:(NSString *)strIdentifer
{
    char buffer[256];
    char *pWrite =  buffer;
    int iWriteBytes = 0; //计算总共长度
    
    char pHedaer1 = 0xF1;
    memcpy(pWrite, &pHedaer1, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pHedaer2 = 0xF1;
    memcpy(pWrite, &pHedaer2, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pFunction = 0x05;
    memcpy(pWrite, &pFunction, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pDataLength = 0x01;
    memcpy(pWrite, &pDataLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    if ([strIdentifer isEqualToString:@"00"]) {
        LLog(@"发送新风系统外循环");
        char pData = 0x00;
        memcpy(pWrite, &pData, 1);
        pWrite ++;
        iWriteBytes ++;
        
    }else if([strIdentifer isEqualToString:@"01"]){
        LLog(@"发送新风系统内循环");
        char pData = 0x01;
        memcpy(pWrite, &pData, 1);
        pWrite ++;
        iWriteBytes ++;
    }else if([strIdentifer isEqualToString:@"02"]){
        LLog(@"发送新风系统自动");
        char pData = 0x02;
        memcpy(pWrite, &pData, 1);
        pWrite ++;
        iWriteBytes ++;
    }
    
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
    char packageEnd = 0x7E;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    
    
    if ([strIdentifer isEqualToString:@"00"]) {
        LLog([NSString stringWithFormat:@"发送新风系统手动:\n\n%@\n\n ",dataWrite]);
    }else if([strIdentifer isEqualToString:@"01"]){
        LLog([NSString stringWithFormat:@"发送新风系统自动:\n\n%@\n\n ",dataWrite]);
    }else if([strIdentifer isEqualToString:@"02"]){
        LLog([NSString stringWithFormat:@"发送新风系统定时:\n\n%@\n\n ",dataWrite]);
    }
    
    return @[dataWrite,@(dataWrite.length)];
}

#pragma mark - 设置设置风速
/*
 strIdentifer:
 20-30共11档，手动模式才有效
 0x14 0x15 0x16 0x17
 0x18 0x19 0x1a 0x1b
 0x1c 0x1e 0x1f
 */
- (NSArray *)doSetXFWindSpeedOrderWithIdentifer:(NSString *)strIdentifer
{
    char buffer[256];
    char *pWrite =  buffer;
    int iWriteBytes = 0; //计算总共长度
    
    char pHedaer1 = 0xF1;
    memcpy(pWrite, &pHedaer1, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pHedaer2 = 0xF1;
    memcpy(pWrite, &pHedaer2, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pFunction = 0x06;
    memcpy(pWrite, &pFunction, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pDataLength = 0x01;
    memcpy(pWrite, &pDataLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    
    NSData *dataWindSpeed = [self dataWithHexString:strIdentifer];
    unsigned char *pdataWindSpeed = (unsigned char *)[dataWindSpeed bytes];
    memcpy(pWrite, pdataWindSpeed, dataWindSpeed.length);
    pWrite +=dataWindSpeed.length;
    iWriteBytes += dataWindSpeed.length;
    
    
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
    char packageEnd = 0x7E;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    
    LLog([NSString stringWithFormat:@"发送设置风速:\n\n%@\n\n 长度：%lu",dataWrite,(unsigned long)dataWrite.length]);
    
    return @[dataWrite,@(dataWrite.length)];
}

#pragma mark - 设置进回风比例
/*
 strIdentifer:
 0x00-10/4, 0x01-10/5, 0x02-10/6
 0x03-10/7, 0x04-10/8, 0x05-10/10
 0x06-10/0单进风,  0x07-0/10单排风
 */
- (NSArray *)doSetXFInAndOutWindScaleOrderWithIdentifer:(NSString *)strIdentifer
{
    char buffer[256];
    char *pWrite =  buffer;
    int iWriteBytes = 0; //计算总共长度
    
    char pHedaer1 = 0xF1;
    memcpy(pWrite, &pHedaer1, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pHedaer2 = 0xF1;
    memcpy(pWrite, &pHedaer2, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pFunction = 0x06;
    memcpy(pWrite, &pFunction, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pDataLength = 0x01;
    memcpy(pWrite, &pDataLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    
    NSData *dataWindSpeed = [self dataWithHexString:strIdentifer];
    unsigned char *pdataWindSpeed = (unsigned char *)[dataWindSpeed bytes];
    memcpy(pWrite, pdataWindSpeed, dataWindSpeed.length);
    pWrite +=dataWindSpeed.length;
    iWriteBytes += dataWindSpeed.length;
    
    
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
    char packageEnd = 0x7E;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    
    LLog([NSString stringWithFormat:@"发送设置风速:\n\n%@\n\n 长度：%lu",dataWrite,(unsigned long)dataWrite.length]);
    
    return @[dataWrite,@(dataWrite.length)];
}


#pragma mark -初滤使用时间清零/集尘箱使用时间清零/高滤使用时间清零
/*
 strIdentifer:初滤使用时间清零,0x08;
 集尘箱使用时间清零,0x09
 高滤使用时间清零,0x0a
 0x01-清零(滤网报警时才有效)
 */
- (NSArray *)doSetXFUseTimeZeroOrderWithIdentifer:(NSString *)strIdentifer
{
    char buffer[256];
    char *pWrite =  buffer;
    int iWriteBytes = 0; //计算总共长度
    
    char pHedaer1 = 0xF1;
    memcpy(pWrite, &pHedaer1, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pHedaer2 = 0xF1;
    memcpy(pWrite, &pHedaer2, 1);
    pWrite ++;
    iWriteBytes ++;
    
    
    NSData *dataFunction = [self dataWithHexString:strIdentifer];
    unsigned char *pFunction = (unsigned char *)[dataFunction bytes];
    memcpy(pWrite, pFunction, dataFunction.length);
    pWrite +=dataFunction.length;
    iWriteBytes += dataFunction.length;
    
    char pDataLength = 0x01;
    memcpy(pWrite, &pDataLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pData = 0x01;
    memcpy(pWrite, &pData, 1);
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
    char packageEnd = 0x7E;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    
    LLog([NSString stringWithFormat:@"发送初滤使用时间清零:\n\n%@\n\n 长度：%lu",dataWrite,(unsigned long)dataWrite.length]);
    
    return @[dataWrite,@(dataWrite.length)];
}


#pragma mark -新风系统所有指令综合
/*
 strFuctionIdentifer:功能码（F0，02，03，04，05，06，07，08，09，0a）
 strRangeIdentifer:数据---------------------------------
 ↓
 描述               功能码         数据长度            数据1（取值范围）
 设置开关机风         0x02          0x01              0x00:关机;0x01:开机;
 设置加热模风         0x03          0x01              0x00：关闭；0x01:开启;
 设置工作模式         0x04          0x01              0x00：手动；0x01:自动;0x02:定时;
 设置循环模式         0x05          0x01              0x00：外循环；0x01:内循环;0x02:自动;
 设置风速模式         0x06          0x01              20-30共11档，手动模式才有效
 设置进回风比         0x07          0x01              0x00-10/4, 0x01-10/5, 0x02-10/6
 0x03-10/7, 0x04-10/8, 0x05-10/10
 0x06-10/0单进风,  0x07-0/10单排风
 初滤使用时间清零      0x08          0x01              0x01-清零(滤网报警时才有效)
 集尘箱使用时间清零    0x09          0x01              0x01-清零(滤网报警时才有效)
 高滤使用时间清零      0x0a          0x01              0x01-清零(滤网报警时才有效)
 */

- (NSArray *)doSetXAllFuctionOrderWithFunctionIdentifer:(NSString *)strFuctionIdentifer dataRangeIdentifer:(NSString *)strDataRangeIdentifer
{
    char buffer[256];
    char *pWrite =  buffer;
    int iWriteBytes = 0; //计算总共长度
    
    char pHedaer1 = 0xF1;
    memcpy(pWrite, &pHedaer1, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pHedaer2 = 0xF1;
    memcpy(pWrite, &pHedaer2, 1);
    pWrite ++;
    iWriteBytes ++;
    
    if([strFuctionIdentifer isEqualToString:@"01"]){
        LLog(@"获取新风机设备状态Identifer:%@,realFuctionNum:01");
        
    }else if ([strFuctionIdentifer isEqualToString:@"02"]){
        LLog(@"设置开关机Identifer:%@,realFuctionNum:02");
        
    }else if ([strFuctionIdentifer isEqualToString:@"03"]){
        LLog(@"设置开关机Identifer:%@,realFuctionNum:03");
        
    }else if ([strFuctionIdentifer isEqualToString:@"04"]){
        LLog(@"设置工作模式Identifer:%@,realFuctionNum:04");
        
    }else if ([strFuctionIdentifer isEqualToString:@"05"]){
        LLog(@"设置工作模式Identifer:%@,realFuctionNum:05");
        
    }else if ([strFuctionIdentifer isEqualToString:@"06"]){
        LLog(@"设置风速Identifer:%@,realFuctionNum:06");
        
    }else if ([strFuctionIdentifer isEqualToString:@"07"]){
        LLog(@"设置进回风比例Identifer:%@,realFuctionNum:07");
        
    }else if ([strFuctionIdentifer isEqualToString:@"08"]){
        LLog(@"设置进回风比例Identifer:%@,realFuctionNum:08");
        
    }else if ([strFuctionIdentifer isEqualToString:@"09"]){
        LLog(@"集尘箱使用时间清零Identifer:%@,realFuctionNum:09");
        
    }else if ([strFuctionIdentifer isEqualToString:@"1a"]){
        LLog(@"高滤使用时间清零Identifer:%@,realFuctionNum:1a");
        
    }
    
    NSData *dataFunction = [self dataWithHexString:strFuctionIdentifer];
    unsigned char *pFunction = (unsigned char *)[dataFunction bytes];
    memcpy(pWrite, pFunction, dataFunction.length);
    pWrite +=dataFunction.length;
    iWriteBytes += dataFunction.length;
    
    char pDataLength = 0x01;
    memcpy(pWrite, &pDataLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    NSData *dataRange = [self dataWithHexString:strDataRangeIdentifer];
    unsigned char *pdataRange = (unsigned char *)[dataRange bytes];
    memcpy(pWrite, pdataRange, dataRange.length);
    pWrite +=dataRange.length;
    iWriteBytes += dataRange.length;
    
    
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
    char packageEnd = 0x7E;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    
    if([strFuctionIdentifer isEqualToString:@"01"]){
        LLog([NSString stringWithFormat:@"获取新风机设备状态GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu",strFuctionIdentifer,strDataRangeIdentifer,dataWrite,(unsigned long)dataWrite.length]);
        
    }else if ([strFuctionIdentifer isEqualToString:@"02"]){
        LLog([NSString stringWithFormat:@"设置开关机GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu",strFuctionIdentifer,strDataRangeIdentifer,dataWrite,(unsigned long)dataWrite.length]);
        
    }else if ([strFuctionIdentifer isEqualToString:@"03"]){
        LLog([NSString stringWithFormat:@"设置开关机GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu",strFuctionIdentifer,strDataRangeIdentifer,dataWrite,(unsigned long)dataWrite.length]);
        
    }else if ([strFuctionIdentifer isEqualToString:@"04"]){
        LLog([NSString stringWithFormat:@"设置工作模式GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu",strFuctionIdentifer,strDataRangeIdentifer,dataWrite,(unsigned long)dataWrite.length]);
        
    }else if ([strFuctionIdentifer isEqualToString:@"05"]){
        LLog([NSString stringWithFormat:@"设置工作模式GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu",strFuctionIdentifer,strDataRangeIdentifer,dataWrite,(unsigned long)dataWrite.length]);
        
    }else if ([strFuctionIdentifer isEqualToString:@"06"]){
        LLog([NSString stringWithFormat:@"设置风速GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu",strFuctionIdentifer,strDataRangeIdentifer,dataWrite,(unsigned long)dataWrite.length]);
        
    }else if ([strFuctionIdentifer isEqualToString:@"07"]){
        LLog([NSString stringWithFormat:@"设置进回风比例GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu",strFuctionIdentifer,strDataRangeIdentifer,dataWrite,(unsigned long)dataWrite.length]);
        
    }else if ([strFuctionIdentifer isEqualToString:@"08"]){
        LLog([NSString stringWithFormat:@"设置进回风比例GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu",strFuctionIdentifer,strDataRangeIdentifer,dataWrite,(unsigned long)dataWrite.length]);
        
    }else if ([strFuctionIdentifer isEqualToString:@"09"]){
        LLog([NSString stringWithFormat:@"集尘箱使用时间清零GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu",strFuctionIdentifer,strDataRangeIdentifer,dataWrite,(unsigned long)dataWrite.length]);
    }else if ([strFuctionIdentifer isEqualToString:@"1a"]){
        LLog([NSString stringWithFormat:@"高滤使用时间清零GetFuctionIdentifer:%@,realFuctionNum:01,getRangeIdentifer:%@\n\n 命令:\n\n%@\n\n 长度：%lu",strFuctionIdentifer,strDataRangeIdentifer,dataWrite,(unsigned long)dataWrite.length]);
    }
    
    return @[dataWrite,@(dataWrite.length)];
}








#pragma mark -
#pragma mark -  中央空调
- (NSData *)doSendVRVDataWithModelDevice:(SHModelDevice *)device
{
    
    //被转发数据域
    NSData *dataTransmit = [self doGetVRTState];
    unsigned char *pTransmit = (unsigned char *)[dataTransmit bytes];
    
    //4070的，计算数据长度 cmdID 1byte，number 1byte，源地址 8byte，目标地址 8byte 被转发协议长度 1byte ，转发数据协议长度7byte
    int iCmdIDLength = 1;
    int iNumberLength = 1;
    int iGatewayMacAddressLength = 8;
    int iTargetAffressLength = 8;
    int iBeTransmitedLength = 1;
    int iTransmitLength = (int)dataTransmit.length;
    int iAllDataLength = iCmdIDLength + iNumberLength + iGatewayMacAddressLength + iTargetAffressLength + iBeTransmitedLength + iTransmitLength;
    
    
    
    NSString *strAllDataLength = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%02x",iAllDataLength]];
    NSData *dataAllDataLength = [self dataWithHexString:strAllDataLength];
    unsigned char *pAllDataLength = (unsigned char *)[dataAllDataLength bytes];
    
    
    //网关Zigbee的MacAddr
    NSString *strTargetAddr = device.strDevice_mac_address;
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    
    
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    char packageStart = 0x2A;
    memcpy(pWrite, &packageStart, 1);
    pWrite ++;
    iWriteBytes++;
    
    
    memcpy(pWrite, pAllDataLength, dataAllDataLength.length);
    pWrite +=dataAllDataLength.length;
    iWriteBytes += dataAllDataLength.length;
    
    char pCmdID = 0x07;
    memcpy(pWrite, &pCmdID, 1);
    pWrite ++;
    iWriteBytes++;
    
    char pNumber = 0x01;
    memcpy(pWrite, &pNumber, 1);
    pWrite ++;
    iWriteBytes++;
    
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    memcpy(pWrite, pTargetAddr, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //被转发协议的长度
    NSString *strBeTransmitDataLength = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%02x",iTransmitLength]];
    NSData *dataBeTransmitDataLength = [self dataWithHexString:strBeTransmitDataLength];
    unsigned char *pBeTransmitDataLength = (unsigned char *)[dataBeTransmitDataLength bytes];
    memcpy(pWrite, pBeTransmitDataLength, dataBeTransmitDataLength.length);
    pWrite +=dataBeTransmitDataLength.length;
    iWriteBytes += dataBeTransmitDataLength.length;
    
    
    memcpy(pWrite, pTransmit, dataTransmit.length);
    pWrite +=dataTransmit.length;
    iWriteBytes += dataTransmit.length;
    
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
    NSLog(@"发送中央空调系统命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

#pragma mark - 查询所有状态
- (NSData *)doGetVRTState
{
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //本网关地址
    char pSelfGatewayMacAddress = 0x01;
    memcpy(pWrite, &pSelfGatewayMacAddress, 1);
    pWrite ++;
    iWriteBytes++;
    
    //功能码
    char pFuctionCode = 0x50;
    memcpy(pWrite, &pFuctionCode, 1);
    pWrite ++;
    iWriteBytes++;
    
    //控制值
    char pControlValue = 0xFF;
    memcpy(pWrite, &pControlValue, 1);
    pWrite ++;
    iWriteBytes++;
    
    //空调数量
    char pVRFCount = 0xFF;
    memcpy(pWrite, &pVRFCount, 1);
    pWrite ++;
    iWriteBytes++;
    
    //空调地址外
    char pVRFOutMacAddress = 0xFF;
    memcpy(pWrite, &pVRFOutMacAddress, 1);
    pWrite ++;
    iWriteBytes++;
    
    //空调地址内
    char pVRFInMacAddress = 0xFF;
    memcpy(pWrite, &pVRFInMacAddress, 1);
    pWrite ++;
    iWriteBytes++;
    
    //CS 1bytes
    char  packageCSDataResult = *(buffer + 0);
    for (int i = 1; i < iWriteBytes; i ++)
    {
        packageCSDataResult = packageCSDataResult + *(buffer + i);
    }
    
    char packageCS = packageCSDataResult%256;
    
    
    memcpy(pWrite, &packageCS, 1);
    pWrite ++;
    iWriteBytes ++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    NSLog(@"发送新风系统命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

#pragma mark - 查询其中一台或者多台的状态

@end
