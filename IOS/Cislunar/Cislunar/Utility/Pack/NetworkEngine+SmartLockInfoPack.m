//
//  NetworkEngine+SmartLockInfoPack.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine+SmartLockInfoPack.h"

@implementation NetworkEngine (SmartLockInfoPack)

#pragma mark-  指纹锁

- (NSData *)doGetFingerprintCombinationLockWakeUpTheDoorDataWithTargetMacAddr:(NSString *)strTargetAddr
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    
    //    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocalWithZero];
    //    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 1B 07 01";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
    //被转发数据域
    NSString *strTransmit = @"FE FE FE FE FE FE FE FE";
    NSData *dataTransmit = [self dataWithHexString:strTransmit];
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
    char pForwardProtocol = 0x08;
    memcpy(pWrite, &pForwardProtocol, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pTransmit, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
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
    NSLog(@"发送设定指纹密码锁命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

//....
- (NSData *)doGetFingerprintCombinationLockOpenTheDoorDataWithTargetMacAddr:(NSString *)strTargetAddr
{
    
    //    //网关Zigbee的MacAddr
    //    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    //    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocalWithZero];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 1B 07 01";
    NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
    unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
    
    //被转发数据域
    NSString *strTransmit = @"FE FE FE FE A1 60 01 02";
    //    NSString *strTransmit = @"A1 60 01 02";
    NSData *dataTransmit = [self dataWithHexString:strTransmit];
    unsigned char *pTransmit = (unsigned char *)[dataTransmit bytes];
    
    char buffer[256];
    char *pWrite =  buffer;
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
    char pForwardProtocol = 0x08;
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
    NSLog(@"发送设定指纹密码锁命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}


- (NSData *)doGetFingerprintCombinationLockDataWithTargetMacAddr:(NSString *)strTargetAddr cmdID:(NSString *)strCmdID
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
    NSData *dataTransmit = [self doGetFingerprintCombinationLockTransmit:strCmdID];
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
    NSLog(@"发送设定指纹密码锁命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

- (NSData *)doGetFingerprintCombinationLockTransmit:(NSString*)cmdID
{
    NSData *dataCmdID = [self dataWithHexString:cmdID];
    unsigned char *pCmdID = (unsigned char *)[dataCmdID bytes];
    
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0;
    
    //锁帧头
    char packageODSecond = 0xA1;
    memcpy(pWrite, &packageODSecond, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //锁 命令标识符CmdID
    memcpy(pWrite, pCmdID, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //锁 数据域长度
    char indexLength = 0x01;
    memcpy(pWrite, &indexLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //数据域
    char cData = 0x00;
    memcpy(pWrite, &cData, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //    //锁 检验码
    //    char  lockCSDataResult = *(buffer + 0);
    //    for (int i = 1; i < 3; i ++)
    //    {
    //        lockCSDataResult = lockCSDataResult + *(buffer + i + 0);
    //    }
    //    char lockCS = lockCSDataResult%256;
    
    //锁 检验码
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
    return dataWrite;
}


#pragma mark -------------------------- 新指纹锁 -----------------------------------
- (NSData *)sendNewLockControlWithGatewayMacAddr:(NSData *)dataGatewayMacAddr
                              andDestinationAddr:(NSString *)strDestinationAddr
                                withcontrolIndex:(unsigned char)controlIndex {
    
    //    if (dataGatewayMacAddr.length == 0) {
    //
    //        return nil;
    //    }
    
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    
    NSData *dataTargetAddr = [self dataWithHexString:strDestinationAddr];
    unsigned char *pDestinationAddress = (unsigned char *)[dataTargetAddr bytes];
    
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //帧头 1 byte
    char packageStart = 0x2A;
    memcpy(pWrite, &packageStart, 1);
    pWrite ++;
    iWriteBytes ++;
    
    
    //帧长 1 byte
    char packageLength = 0x2b;
    memcpy(pWrite, &packageLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //Cmd ID 1 byte
    char packageCmdID = 0x07;
    memcpy(pWrite, &packageCmdID, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //Number 1 byte
    char packageNumber = 0x01;
    memcpy(pWrite, &packageNumber, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //源地址（即目标地址） 8 bytes
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //目的地址 //8bttes
    memcpy(pWrite, pDestinationAddress, 8);
    pWrite += 8;
    iWriteBytes += 8;
    
    //OD 2 bytes
    char packageODFirst = 0x04;
    memcpy(pWrite, &packageODFirst, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //    char yl1Length = 0xFF;
    //    memcpy(pWrite, &yl1Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl2Length = 0xFF;
    //    memcpy(pWrite, &yl2Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl3Length = 0xFF;
    //    memcpy(pWrite, &yl3Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl4Length = 0xFF;
    //    memcpy(pWrite, &yl4Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl5Length = 0xFF;
    //    memcpy(pWrite, &yl5Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl6Length = 0xFF;
    //    memcpy(pWrite, &yl6Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl7Length = 0xFF;
    //    memcpy(pWrite, &yl7Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl8Length = 0xFF;
    //    memcpy(pWrite, &yl8Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl9Length = 0xFF;
    //    memcpy(pWrite, &yl9Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl10Length = 0xFF;
    //    memcpy(pWrite, &yl10Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl11Length = 0xFF;
    //    memcpy(pWrite, &yl11Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl12Length = 0xFF;
    //    memcpy(pWrite, &yl12Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl13Length = 0xFF;
    //    memcpy(pWrite, &yl13Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl14Length = 0xFF;
    //    memcpy(pWrite, &yl14Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl15Length = 0xFF;
    //    memcpy(pWrite, &yl15Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl16Length = 0xFF;
    //    memcpy(pWrite, &yl16Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl17Length = 0xFF;
    //    memcpy(pWrite, &yl17Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl18Length = 0xFF;
    //    memcpy(pWrite, &yl18Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl19Length = 0xFF;
    //    memcpy(pWrite, &yl19Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    //
    //    char yl20Length = 0xFF;
    //    memcpy(pWrite, &yl20Length, 1);
    //    pWrite ++;
    //    iWriteBytes ++;
    
    
    
    
    //锁帧头
    char packageODSecond = 0xA1;
    memcpy(pWrite, &packageODSecond, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //锁 CMD
    memcpy(pWrite, &controlIndex, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //锁 长度
    char indexLength = 0x01;
    memcpy(pWrite, &indexLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //锁 检验码
    char  lockCSDataResult = *(buffer + 21);
    for (int i = 1; i < 3; i ++)
    {
        lockCSDataResult = lockCSDataResult + *(buffer + i + 21 );
    }
    
    char lockCS = lockCSDataResult%256;
    
    memcpy(pWrite, &lockCS, 1);
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
    NSLog(@"发送新指纹锁命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}



#pragma mark - 随机密码开锁

- (NSData *)doGetRandomNumLockOpenTheDoorDataWithTargetMacAddr:(NSString *)strTargetAddr
                                                  randomNumber:(NSString *)strHEXRandomNum
                                                     validTime:(NSString *)strHEXValidTime
                                                         times:(NSString *)strHEXTimes
{
    
    NSString *strDataHEXMix = [NSString stringWithFormat:@"%@%@%@",strHEXRandomNum,strHEXValidTime,strHEXTimes];
    LLog([NSString stringWithFormat:@"远程开锁的密码组合数据yu:%@",strDataHEXMix]);
    
    NSData *dataRandomPsw = [self doGetOpenLockOriginalDataWithStr:strDataHEXMix];
    unsigned char *pRandompsw = (unsigned char *)[dataRandomPsw bytes];
    
    
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    
    //    //网关Zigbee的MacAddr
    //    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocalWithZero];
    //    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed = @"2A 1F 07 01";
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
    char pForwardProtocol = 0x0C;
    memcpy(pWrite, &pForwardProtocol, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pRandompsw, dataRandomPsw.length);
    pWrite +=dataRandomPsw.length;
    iWriteBytes += dataRandomPsw.length;
    
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
    NSLog(@"发送设定指纹密码锁命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}


- (NSData *)doGetOpenLockOriginalDataWithStr:(NSString *)strDataFieldHex
{
    
    NSData *dataFieldHexMixed = [self dataWithHexString:strDataFieldHex];
    unsigned char *pdataField = (unsigned char *)[dataFieldHexMixed bytes];
    
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //帧头 1 byte
    char packageStart = 0xA1;
    memcpy(pWrite, &packageStart, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pCmd = 0x91;
    memcpy(pWrite, &pCmd, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //长度
    char indexLength = 0x09;
    memcpy(pWrite, &indexLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    memcpy(pWrite, pdataField, 8);
    pWrite += 8;
    iWriteBytes += 8;
    
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
    NSLog(@"发送设定指纹动态密码完整数据包:\n\n%@\n\n",dataWrite);
    return dataWrite;
    
    
}




-(void)sendTheSetGatewayTelePhoneWithTheTypeIndentifier:(int)indentifierTelSet andTelNum:(NSString *)strTel WithGatewayMacAddr:(NSData *)dataGatewayMacAddr
{
    
    if (dataGatewayMacAddr.length == 0) {
        
        return;
    }
    
    const char * cPackageTel = [strTel cStringUsingEncoding:NSUTF8StringEncoding];
    //    NSData *dataTemp = [NSData dataWithBytes:cPackageTel length:11];
    //    NSLog(@"dataTemp === %@",dataTemp);
    
    unsigned char *pMacAddress = (unsigned char *)[dataGatewayMacAddr bytes]; //8byte
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //帧头 1 byte
    char packageStart = 0x2A;
    memcpy(pWrite, &packageStart, 1);
    pWrite ++;
    iWriteBytes ++;
    
    
    //帧长 1 byte
    char packageLength = 0x1E;
    memcpy(pWrite, &packageLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //Cmd ID 1 byte
    char packageCmdID = 0x02;
    memcpy(pWrite, &packageCmdID, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //Number 1 byte
    char packageNumber = 0x00;
    memcpy(pWrite, &packageNumber, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //源地址（即网关地址） 8 bytes
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //OD 2 bytes
    unsigned char packageODFirst = 0x82;
    memcpy(pWrite, &packageODFirst, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char packageODSecond = 0x01;
    memcpy(pWrite, &packageODSecond, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //子索引 1 byte
    unsigned char packageChildIndex = 0xff;
    memcpy(pWrite, &packageChildIndex, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //长度
    char packageChildIndexLength = 0x10;
    memcpy(pWrite, &packageChildIndexLength, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //子选项选择
    char childOption[4] = {(char)0x00,(char)0x00,(char)0x00,(char)0x10};
    memcpy(pWrite, &childOption, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    //电话号码
    memcpy(pWrite, cPackageTel, 11);
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
}



- (NSString *)handleIntToStrBCD:(int)iNum
{
    NSString *strResult;
    if (iNum == 0) {
        
        strResult = @"0000";
        
    }else if (iNum == 1){
        
        strResult = @"0001";
    }else if (iNum == 2){
        
        strResult = @"0010";
        
    }else if (iNum == 3){
        
        strResult = @"0011";
        
    }else if (iNum == 4){
        
        strResult = @"0100";
        
    }else if (iNum == 5){
        
        strResult = @"0101";
        
    }else if (iNum == 6){
        
        strResult = @"0110";
        
    }else if (iNum == 7){
        
        strResult = @"0111";
        
    }else if (iNum == 8){
        
        strResult = @"1000";
        
    }else if (iNum == 9){
        
        strResult = @"1001";
        
    }else{
        
        strResult = @"1111";
    }
    
    return  strResult;
}



//  二进制转十进制
- (NSString *)toDecimalSystemWithBinarySystem:(NSString *)binary
{
    int ll = 0 ;
    int  temp = 0 ;
    for (int i = 0; i < binary.length; i ++)
    {
        temp = [[binary substringWithRange:NSMakeRange(i, 1)] intValue];
        temp = temp * powf(2, binary.length - i - 1);
        ll += temp;
    }
    
    NSString * result = [NSString stringWithFormat:@"%d",ll];
    
    return result;
}



/*
 #pragma mark - 随机密码开锁
 
 - (NSString *)handleStrRandomNumOriginToHexNum:(NSString *)strWhole
 {
 NSString *str1 = [self handleIntToStrBCD:[strWhole substringWithRange:NSMakeRange(0, 1)]];
 NSString *str2 = [self handleIntToStrBCD:[strWhole substringWithRange:NSMakeRange(1, 1)]];
 
 NSString *str3 = [self handleIntToStrBCD:[strWhole substringWithRange:NSMakeRange(2, 1)]];
 NSString *str4 = [self handleIntToStrBCD:[strWhole substringWithRange:NSMakeRange(3, 1)]];
 
 NSString *str5 = [self handleIntToStrBCD:[strWhole substringWithRange:NSMakeRange(4, 1)]];
 NSString *str6 = [self handleIntToStrBCD:[strWhole substringWithRange:NSMakeRange(5, 1)]];
 
 NSString *strMix1 = [NSString stringWithFormat:@"%@%@",str1,str2];
 NSString *strTemp1 = [[ToolHexManager sharedManager] convertBin:strMix1];
 
 NSString *strMix2 = [NSString stringWithFormat:@"%@%@",str3,str4];
 NSString *strTemp2 = [[ToolHexManager sharedManager] convertBin:strMix2];
 
 NSString *strMix3 = [NSString stringWithFormat:@"%@%@",str5,str6];
 NSString *strTemp3 = [[ToolHexManager sharedManager] convertBin:strMix3];
 
 NSLog(@"strTemp1 == %@,strTemp2 == %@,strTemp3 == %@",strTemp1,strTemp2,strTemp3);
 return [NSString stringWithFormat:@"%@%@%@",strTemp1,strTemp2,strTemp3];
 }
 
 
 - (NSData *)doGetRandomNumLockOpenTheDoorDataWithTargetMacAddr:(NSString *)strTargetAddr
 randomNumber:(NSString *)strRandomNumOrigin
 validTime:(NSString *)strHEXValidTime
 times:(NSString *)strHEXTimes
 {
 
 NSString *strHEXRandomNum = [self handleStrRandomNumOriginToHexNum:strRandomNumOrigin];
 
 NSString *strDataHEXMix = [NSString stringWithFormat:@"%@%@%@",strHEXRandomNum,strHEXValidTime,strHEXTimes];
 NSLog(@"远程开锁的密码组合数据yu:%@",strDataHEXMix);
 
 NSData *dataRandomPsw = [self doGetOpenLockOriginalDataWithStr:strDataHEXMix];
 unsigned char *pRandompsw = (unsigned char *)[dataRandomPsw bytes];
 
 
 //网关Zigbee的MacAddr
 NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
 unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
 
 NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
 unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
 
 //帧头 帧长 CmdID Number
 NSString *strHeaderMixed = @"2A 1F 07 01";
 NSData *dataHeaderMixed = [self dataWithHexString:strHeaderMixed];
 unsigned char *pHeaderMixed = (unsigned char *)[dataHeaderMixed bytes];
 
 //被转发数据域
 //    NSString *strTransmit = @"FE FE FE FE A1 60 01 02";
 //    //    NSString *strTransmit = @"A1 60 01 02";
 //    NSData *dataTransmit = [self dataWithHexString:strTransmit];
 //    unsigned char *pTransmit = (unsigned char *)[dataTransmit bytes];
 
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
 char pForwardProtocol = 0x09;
 memcpy(pWrite, &pForwardProtocol, 1);
 pWrite ++;
 iWriteBytes ++;
 
 memcpy(pWrite, pRandompsw, dataRandomPsw.length);
 pWrite +=dataRandomPsw.length;
 iWriteBytes += dataRandomPsw.length;
 
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
 NSLog(@"发送设定指纹密码锁命令:\n\n%@\n\n",dataWrite);
 return dataWrite;
 }
 
 
 - (NSData *)doGetOpenLockOriginalDataWithStr:(NSString *)strDataFieldHex
 {
 
 NSData *dataFieldHexMixed = [self dataWithHexString:strDataFieldHex];
 unsigned char *pdataField = (unsigned char *)[dataFieldHexMixed bytes];
 
 
 char buffer[256];
 char *pWrite = buffer;
 int iWriteBytes = 0; //计算总共长度
 
 //帧头 1 byte
 char packageStart = 0xA1;
 memcpy(pWrite, &packageStart, 1);
 pWrite ++;
 iWriteBytes ++;
 
 char pCmd = 0x91;
 memcpy(pWrite, &pCmd, 1);
 pWrite ++;
 iWriteBytes ++;
 
 //长度
 char indexLength = 0x06;
 memcpy(pWrite, &indexLength, 1);
 pWrite ++;
 iWriteBytes ++;
 
 memcpy(pWrite, pdataField, dataFieldHexMixed.length);
 pWrite += dataFieldHexMixed.length;
 iWriteBytes += dataFieldHexMixed.length;
 
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
 NSLog(@"发送设定指纹动态密码完整数据包:\n\n%@\n\n",dataWrite);
 return dataWrite;
 }
 
 
 
 - (NSString *)handleIntToStrBCD:(NSString *)strNum
 {
 int iNum = [strNum intValue];
 NSString *strResult;
 if (iNum == 0) {
 
 strResult = @"0000";
 
 }else if (iNum == 1){
 
 strResult = @"0001";
 }else if (iNum == 2){
 
 strResult = @"0010";
 
 }else if (iNum == 3){
 
 strResult = @"0011";
 
 }else if (iNum == 4){
 
 strResult = @"0100";
 
 }else if (iNum == 5){
 
 strResult = @"0101";
 
 }else if (iNum == 6){
 
 strResult = @"0110";
 
 }else if (iNum == 7){
 
 strResult = @"0111";
 
 }else if (iNum == 8){
 
 strResult = @"1000";
 
 }else if (iNum == 9){
 
 strResult = @"1001";
 
 }else{
 
 strResult = @"1111";
 }
 
 return  strResult;
 }
 
 
 
 //  二进制转十进制
 - (NSString *)toDecimalSystemWithBinarySystem:(NSString *)binary
 {
 int ll = 0 ;
 int  temp = 0 ;
 for (int i = 0; i < binary.length; i ++)
 {
 temp = [[binary substringWithRange:NSMakeRange(i, 1)] intValue];
 temp = temp * powf(2, binary.length - i - 1);
 ll += temp;
 }
 
 NSString * result = [NSString stringWithFormat:@"%d",ll];
 
 return result;
 }
 */



- (NSData *)doTestCodeForLock
{
    return nil;
}



#pragma mark - 小蛮腰远程开锁
- (NSData *)doSendRemoteOpenXMYLockOrderWithTargetMacAddr:(NSString *)strTargetAddr lockPsw:(NSString *)strPsw
{
    
    //转发命令标识（Cmd ID）
    int iCmdIDLength = 1;
    
    //转发数据目的地址域使用MAC地址（DstAddrFmt）
    int iDstAddrFmtLenth = 1;
    
    //网关通用MAC地址
    int iGatewayMackAddrLength = 8;
    
    //指纹锁MAC地址
    int iLockMacAdrrLenth = 8;
    
    //data长度
    int iAllTransmitLength = 1;
    
    //转发内容长度
    //    int iWakeUpFF = 8;
    int iLockHeader = 1;
    int iLockDataLength = 1;
    int iLockCommandWord = 2;
    int iContentLength = (int)strPsw.length;
    int iCS = 1;
    //    int iLockAllDataLength = iWakeUpFF + iLockHeader + iLockDataLength + iLockCommandWord + iContentLength + iCS;
    int iLockAllDataLength = iLockHeader + iLockDataLength + iLockCommandWord + iContentLength + iCS;
    NSString *strHexLockAllLength = [[ToolHexManager sharedManager] converIntToHex:iLockAllDataLength];
    NSData *dataLockAllLength = [self dataWithHexString:strHexLockAllLength];
    unsigned char *pDataLockAllLength = (unsigned char *)[dataLockAllLength bytes];
    
    
    int iDataAllLenth = iCmdIDLength + iDstAddrFmtLenth + iGatewayMackAddrLength + iLockMacAdrrLenth + iAllTransmitLength + iLockAllDataLength;
    NSString *strHexDataLength = [[ToolHexManager sharedManager] converIntToHex:iDataAllLenth];
    NSData *dataLength = [self dataWithHexString:strHexDataLength];
    unsigned char *pDataLength = (unsigned char *)[dataLength bytes];
    
    
    char buffer[256];
    char *pWrite =  buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //帧头
    char pHeaer = 0x2A;
    memcpy(pWrite, &pHeaer, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //帧长
    memcpy(pWrite, pDataLength, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    //转发命令
    char pCmdID = 0x07;
    memcpy(pWrite, &pCmdID, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //转发命令
    char pDstAddrFmt = 0x01;
    memcpy(pWrite, &pDstAddrFmt, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //网关macAddrr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocalWithZero];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes];
    memcpy(pWrite, pMacAddress, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    
    //lock macdizhi
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    memcpy(pWrite, pTargetAddr, 8);
    pWrite +=8;
    iWriteBytes += 8;
    
    //转发数据长度
    memcpy(pWrite, pDataLockAllLength, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    //转发内容
    NSData *dataLock = [self doGetXMYLockRemoteOpenOrderWithPsw:strPsw];
    unsigned char *pDataLocLength = (unsigned char *)[dataLock bytes];
    memcpy(pWrite, pDataLocLength, iLockAllDataLength);
    pWrite += iLockAllDataLength;
    iWriteBytes += iLockAllDataLength;
    
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
    NSLog(@"发送远程开锁小蛮腰锁命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

#pragma mark - Private 组合小蛮腰开锁指令
- (NSData *)doGetXMYLockRemoteOpenOrderWithPsw:(NSString *)strPsw
{
    
    //内容的长度
    int iContentLength = (int)strPsw.length;
    int iCommandLength = 2;
    
    int iDataLength = iContentLength + iCommandLength;
    
    
    NSString *strHexPsw = @"";
    for (int i = 0; i < strPsw.length; i ++) {
        
        NSString *strITempPsw = [strPsw substringWithRange: NSMakeRange(i, 1)];
        NSString * strHextTemp = [[ToolHexManager sharedManager] converIntToHex: [strITempPsw intValue]];
        strHexPsw = [NSString stringWithFormat:@"%@%@",strHexPsw,strHextTemp];
    }
    NSData *dataStrPsw = [self dataWithHexString:strHexPsw];
    NSLog(@"strHexPsw = %@;dataStrPsw = %@",strHexPsw,dataStrPsw);
    unsigned char *pDataStrPsw = (unsigned char *)[dataStrPsw bytes];
    NSLog(@"strHexPsw = %@",strHexPsw);
    
    char buffer[256];
    char *pWrite = buffer;
    int iWriteBytes = 0; //计算总共长度
    
    //小蛮腰帧头
    char pHeaer = 0x80;
    memcpy(pWrite, &pHeaer, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //小蛮腰帧长（命令 + 内容）
    NSString *strHexLength = [[ToolHexManager sharedManager] converIntToHex:iDataLength];
    NSData *dataLength = [self dataWithHexString:strHexLength];
    unsigned char *pDataLength = (unsigned char *)[dataLength bytes];
    
    memcpy(pWrite, pDataLength, 1);
    pWrite += 1;
    iWriteBytes += 1;
    
    //小蛮腰命令字
    char pCommandOne = 0x10;
    memcpy(pWrite, &pCommandOne, 1);
    pWrite ++;
    iWriteBytes ++;
    
    char pCommandTwo = 0x80;
    memcpy(pWrite, &pCommandTwo, 1);
    pWrite ++;
    iWriteBytes ++;
    
    //小蛮腰内容
    memcpy(pWrite, pDataStrPsw, iContentLength);
    pWrite += iContentLength;
    iWriteBytes += iContentLength;
    
    //CS 1bytes
    char  packageCSDataResult = *(buffer + 2);
    for (int i = 3; i < iWriteBytes; i ++)
    {
        packageCSDataResult = packageCSDataResult ^ *(buffer + i);
    }
    
    char packageCS = packageCSDataResult;
    
    
    memcpy(pWrite, &packageCS, 1);
    pWrite ++;
    iWriteBytes ++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    NSLog(@"发送小蛮腰远程开锁命令:\n\n%@\n\n",dataWrite);
    return dataWrite;
}

@end
