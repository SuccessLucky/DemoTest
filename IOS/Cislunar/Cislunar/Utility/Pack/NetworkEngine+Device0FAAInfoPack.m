//
//  NetworkEngine+Device0FAAInfoPack.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine+Device0FAAInfoPack.h"

@implementation NetworkEngine (Device0FAAInfoPack)

#pragma mark - 2.1 多用途开关动作控制器定义(OD索引4010= 0x0FAA )
/*此控制器定义主要适用于输出设备（主要是用来控制设备的开、关、停、调光及调色等）和输出设备（主要是用来上报设备的状态
 *  子索引：
 *  00 01 80 00 表示第一路
 *  00 0D 80 00 表示第二路
 *  00 6D 80 00 表示第三路
 *  状态控制字：
 *  01-开，02-关，04-停
 */
#pragma mark - 2.1.1.1 开、关、停控制 (单路控制)
- (NSData *)doGetSwitchControlWithTargetAddr:(NSString *)strTargetAddr
                                      device:(SHModelDevice *)device
                                         way:(int)iWay
                                 controlMode:(NSString *)controlMode
                                controlState:(NSString *)controlState
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed;
    NSString *strSonOptions;
    
    
    if ([device.strDevice_device_type isEqualToString:@"05"] ) {
        strHeaderMixed = @"2A 1C 02 01";
        strSonOptions = @"00 01 80 00";
    }else if ([device.strDevice_device_type isEqualToString:@"06"]){
        strHeaderMixed = @"2A 1E 02 01";
        strSonOptions = @"00 0D 80 00";
    }else if ([device.strDevice_device_type isEqualToString:@"07"]){
        strHeaderMixed = @"2A 20 02 01";
        strSonOptions = @"00 6D 80 00";
    }else{
        strHeaderMixed = @"2A 1C 02 01";
        strSonOptions = @"00 01 80 00";
    }
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
    
    
    if ([device.strDevice_device_type isEqualToString:@"05"]) {
        
        //数据域长度
        char pSonDataLength = 0x06;
        memcpy(pWrite, &pSonDataLength, 1);
        pWrite ++;
        iWriteBytes ++;
    }else if ([device.strDevice_device_type isEqualToString:@"06"]){
        //数据域长度
        char pSonDataLength = 0x08;
        memcpy(pWrite, &pSonDataLength, 1);
        pWrite ++;
        iWriteBytes ++;
    }else if ([device.strDevice_device_type isEqualToString:@"07"]){
        //数据域长度
        char pSonDataLength = 0x0A;
        memcpy(pWrite, &pSonDataLength, 1);
        pWrite ++;
        iWriteBytes ++;
    }else{
        //数据域长度
        char pSonDataLength = 0x06;
        memcpy(pWrite, &pSonDataLength, 1);
        pWrite ++;
        iWriteBytes ++;
    }
    
    //子选择项 4byte
    memcpy(pWrite,pSonOptions, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    //控制字
    if ([device.strDevice_device_type isEqualToString:@"05"]) {
        if ([device.strDevice_category isEqualToString:@"02"]) {
            //第一路
            NSData *dataControlMode = [self dataWithHexString:controlMode];
            unsigned char *pControlMode = (unsigned char *)[dataControlMode bytes];
            memcpy(pWrite, pControlMode, 1);
            pWrite ++;
            iWriteBytes ++;
            
            NSData *dataControlState = [self dataWithHexString:controlState];
            unsigned char *pControlState = (unsigned char *)[dataControlState bytes];
            memcpy(pWrite, pControlState, 1);
            pWrite ++;
            iWriteBytes ++;
        } else{
            
            //第一路
            NSData *dataControlMode = [self dataWithHexString:controlMode];
            unsigned char *pControlMode = (unsigned char *)[dataControlMode bytes];
            memcpy(pWrite, pControlMode, 1);
            pWrite ++;
            iWriteBytes ++;
            
            NSData *dataControlState = [self dataWithHexString:controlState];
            unsigned char *pControlState = (unsigned char *)[dataControlState bytes];
            memcpy(pWrite, pControlState, 1);
            pWrite ++;
            iWriteBytes ++;
        }
        
    }else if([device.strDevice_device_type isEqualToString:@"06"]){
        switch (iWay) {
            case 1:
            {
                //第一路
                NSData *dataControlMode = [self dataWithHexString:controlMode];
                unsigned char *pControlMode = (unsigned char *)[dataControlMode bytes];
                memcpy(pWrite, pControlMode, 1);
                pWrite ++;
                iWriteBytes ++;
                
                NSData *dataControlState = [self dataWithHexString:controlState];
                unsigned char *pControlState = (unsigned char *)[dataControlState bytes];
                memcpy(pWrite, pControlState, 1);
                pWrite ++;
                iWriteBytes ++;
                
                
                //第二路
                NSString *strSecondRoad = @"00 00";
                NSData *dataSencondRoad = [self dataWithHexString:strSecondRoad];
                unsigned char *pSencondRoad = (unsigned char *)[dataSencondRoad bytes];
                memcpy(pWrite,pSencondRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
            }
                break;
            case 2:
            {
                //第一路
                NSString *strFirstRoad = @"00 00";
                NSData *dataFirstRoad = [self dataWithHexString:strFirstRoad];
                unsigned char *pFirstRoad = (unsigned char *)[dataFirstRoad bytes];
                memcpy(pWrite,pFirstRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
                
                
                //第二路
                NSData *dataControlMode = [self dataWithHexString:controlMode];
                unsigned char *pControlMode = (unsigned char *)[dataControlMode bytes];
                memcpy(pWrite, pControlMode, 1);
                pWrite ++;
                iWriteBytes ++;
                
                NSData *dataControlState = [self dataWithHexString:controlState];
                unsigned char *pControlState = (unsigned char *)[dataControlState bytes];
                memcpy(pWrite, pControlState, 1);
                pWrite ++;
                iWriteBytes ++;
            }
                break;
                
            default:
            {
                //第一路
                NSString *strFirstRoad = @"00 00";
                NSData *dataFirstRoad = [self dataWithHexString:strFirstRoad];
                unsigned char *pFirstRoad = (unsigned char *)[dataFirstRoad bytes];
                memcpy(pWrite,pFirstRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
                
                //第二路
                NSString *strSecondRoad = @"00 00";
                NSData *dataSencondRoad = [self dataWithHexString:strSecondRoad];
                unsigned char *pSencondRoad = (unsigned char *)[dataSencondRoad bytes];
                memcpy(pWrite,pSencondRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
            }
                break;
        }
        
        
    }else if([device.strDevice_device_type isEqualToString:@"07"]){
        
        switch (iWay) {
            case 1:
            {
                //第一路
                NSData *dataControlMode = [self dataWithHexString:controlMode];
                unsigned char *pControlMode = (unsigned char *)[dataControlMode bytes];
                memcpy(pWrite, pControlMode, 1);
                pWrite ++;
                iWriteBytes ++;
                
                NSData *dataControlState = [self dataWithHexString:controlState];
                unsigned char *pControlState = (unsigned char *)[dataControlState bytes];
                memcpy(pWrite, pControlState, 1);
                pWrite ++;
                iWriteBytes ++;
                
                //第二路
                NSString *strSecondRoad = @"00 00";
                NSData *dataSencondRoad = [self dataWithHexString:strSecondRoad];
                unsigned char *pSencondRoad = (unsigned char *)[dataSencondRoad bytes];
                memcpy(pWrite,pSencondRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
                //第三路
                NSString *strThirdRoad = @"00 00";
                NSData *dataThirdRoad = [self dataWithHexString:strThirdRoad];
                unsigned char *pThirdRoad = (unsigned char *)[dataThirdRoad bytes];
                memcpy(pWrite,pThirdRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
            }
                break;
            case 2:
            {
                //第一路
                NSString *strFirstRoad = @"00 00";
                NSData *dataFirstRoad = [self dataWithHexString:strFirstRoad];
                unsigned char *pFirstRoad = (unsigned char *)[dataFirstRoad bytes];
                memcpy(pWrite,pFirstRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
                //第二路
                NSData *dataControlMode = [self dataWithHexString:controlMode];
                unsigned char *pControlMode = (unsigned char *)[dataControlMode bytes];
                memcpy(pWrite, pControlMode, 1);
                pWrite ++;
                iWriteBytes ++;
                
                NSData *dataControlState = [self dataWithHexString:controlState];
                unsigned char *pControlState = (unsigned char *)[dataControlState bytes];
                memcpy(pWrite, pControlState, 1);
                pWrite ++;
                iWriteBytes ++;
                
                //第三路
                NSString *strThirdRoad = @"00 00";
                NSData *dataThirdRoad = [self dataWithHexString:strThirdRoad];
                unsigned char *pThirdRoad = (unsigned char *)[dataThirdRoad bytes];
                memcpy(pWrite,pThirdRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
            }
                break;
            case 3:
            {
                //第一路
                NSString *strFirstRoad = @"00 00";
                NSData *dataFirstRoad = [self dataWithHexString:strFirstRoad];
                unsigned char *pFirstRoad = (unsigned char *)[dataFirstRoad bytes];
                memcpy(pWrite,pFirstRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
                //第二路
                NSString *strSecondRoad = @"00 00";
                NSData *dataSencondRoad = [self dataWithHexString:strSecondRoad];
                unsigned char *pSencondRoad = (unsigned char *)[dataSencondRoad bytes];
                memcpy(pWrite,pSencondRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
                //第三路
                NSData *dataControlMode = [self dataWithHexString:controlMode];
                unsigned char *pControlMode = (unsigned char *)[dataControlMode bytes];
                memcpy(pWrite, pControlMode, 1);
                pWrite ++;
                iWriteBytes ++;
                
                NSData *dataControlState = [self dataWithHexString:controlState];
                unsigned char *pControlState = (unsigned char *)[dataControlState bytes];
                memcpy(pWrite, pControlState, 1);
                pWrite ++;
                iWriteBytes ++;
            }
                break;
                
            default:
            {
                //第一路
                NSString *strFirstRoad = @"00 00";
                NSData *dataFirstRoad = [self dataWithHexString:strFirstRoad];
                unsigned char *pFirstRoad = (unsigned char *)[dataFirstRoad bytes];
                memcpy(pWrite,pFirstRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
                
                //第二路
                NSString *strSecondRoad = @"00 00";
                NSData *dataSencondRoad = [self dataWithHexString:strSecondRoad];
                unsigned char *pSencondRoad = (unsigned char *)[dataSencondRoad bytes];
                memcpy(pWrite,pSencondRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
                
                //第三路
                NSString *strThirdRoad = @"00 00";
                NSData *dataThirdRoad = [self dataWithHexString:strThirdRoad];
                unsigned char *pThirdRoad = (unsigned char *)[dataThirdRoad bytes];
                memcpy(pWrite,pThirdRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
            }
                break;
        }
    } else {
        
        NSData *dataControlMode = [self dataWithHexString:controlMode];
        unsigned char *pControlMode = (unsigned char *)[dataControlMode bytes];
        memcpy(pWrite, pControlMode, 1);
        pWrite ++;
        iWriteBytes ++;
        
        NSData *dataControlState = [self dataWithHexString:controlState];
        unsigned char *pControlState = (unsigned char *)[dataControlState bytes];
        memcpy(pWrite, pControlState, 1);
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
    char packageEnd = 0x23;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    return dataWrite;
}

#pragma mark - 2.1.1.1 开、关、停控制 (多路同时控制，针对二路，和三路)
- (NSData *)doGetSwitchControlAllWithSendDevice:(SHModelDevice *)device
{
    
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    NSData *dataTargetAddr = [self dataWithHexString:device.strDevice_mac_address];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed;
    NSString *strSonOptions;
    if ([device.strDevice_device_type isEqualToString:@"05"] ) {
        strHeaderMixed = @"2A 1C 02 01";
        strSonOptions = @"00 01 80 00";
    }else if ([device.strDevice_device_type isEqualToString:@"06"]){
        strHeaderMixed = @"2A 1E 02 01";
        strSonOptions = @"00 0D 80 00";
    }else if ([device.strDevice_device_type isEqualToString:@"07"]){
        strHeaderMixed = @"2A 20 02 01";
        strSonOptions = @"00 6D 80 00";
    }else{
        strHeaderMixed = @"2A 1C 02 01";
        strSonOptions  = @"00 01 80 00";
    }
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
    
    
    //    if ([device.strDevice_sindex_length isEqualToString:@"11"] ) {
    //        //数据域长度
    //        char pSonDataLength = 0x06;
    //        memcpy(pWrite, &pSonDataLength, 1);
    //        pWrite ++;
    //        iWriteBytes ++;
    //    }else if ([device.strDevice_sindex_length isEqualToString:@"12"]){
    //        //数据域长度
    //        char pSonDataLength = 0x08;
    //        memcpy(pWrite, &pSonDataLength, 1);
    //        pWrite ++;
    //        iWriteBytes ++;
    //    }else if ([device.strDevice_sindex_length isEqualToString:@"13"]){
    //        //数据域长度
    //        char pSonDataLength = 0x0A;
    //        memcpy(pWrite, &pSonDataLength, 1);
    //        pWrite ++;
    //        iWriteBytes ++;
    //    }
    
    if ([device.strDevice_device_type isEqualToString:@"05"] ) {
        //数据域长度
        char pSonDataLength = 0x06;
        memcpy(pWrite, &pSonDataLength, 1);
        pWrite ++;
        iWriteBytes ++;
    }else if ([device.strDevice_device_type isEqualToString:@"06"]){
        //数据域长度
        char pSonDataLength = 0x08;
        memcpy(pWrite, &pSonDataLength, 1);
        pWrite ++;
        iWriteBytes ++;
    }else if ([device.strDevice_device_type isEqualToString:@"07"]){
        //数据域长度
        char pSonDataLength = 0x0A;
        memcpy(pWrite, &pSonDataLength, 1);
        pWrite ++;
        iWriteBytes ++;
    }else{
        //数据域长度
        char pSonDataLength = 0x06;
        memcpy(pWrite, &pSonDataLength, 1);
        pWrite ++;
        iWriteBytes ++;
    }
    
    
    //子选择项 4byte
    memcpy(pWrite,pSonOptions, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    //控制字
    if ([device.strDevice_device_type isEqualToString:@"05"]) {
        //第一路
        NSString *controlMode1 = @"01";
        NSString *controlState1 = @"00";
        if (device.iDevice_device_state1 == 1) {
            controlState1 = @"01";
        }else{
            controlState1 = @"02";
        }
        
        NSData *dataControlMode1 = [self dataWithHexString:controlMode1];
        unsigned char *pControlMode1 = (unsigned char *)[dataControlMode1 bytes];
        memcpy(pWrite, pControlMode1, 1);
        pWrite ++;
        iWriteBytes ++;
        
        
        NSData *dataControlState1 = [self dataWithHexString:controlState1];
        unsigned char *pControlState1 = (unsigned char *)[dataControlState1 bytes];
        memcpy(pWrite, pControlState1, 1);
        pWrite ++;
        iWriteBytes ++;
        
    }else if([device.strDevice_device_type isEqualToString:@"06"]){
        
        
        NSString *controlMode1 = @"01";
        NSString *controlState1 = @"00";
        if (device.iDevice_device_state1 == 1) {
            controlState1 = @"01";
        }else{
            controlState1 = @"02";
        }
        //第一路
        NSData *dataControlMode1 = [self dataWithHexString:controlMode1];
        unsigned char *pControlMode1 = (unsigned char *)[dataControlMode1 bytes];
        memcpy(pWrite, pControlMode1, 1);
        pWrite ++;
        iWriteBytes ++;
        
        NSData *dataControlState1 = [self dataWithHexString:controlState1];
        unsigned char *pControlState1 = (unsigned char *)[dataControlState1 bytes];
        memcpy(pWrite, pControlState1, 1);
        pWrite ++;
        iWriteBytes ++;
        
        //第二路
        NSString *controlMode2 = @"01";
        NSString *controlState2 = @"00";
        if (device.iDevice_device_state2 == 1) {
            controlState2 = @"01";
        }else{
            controlState2 = @"02";
        }
        NSData *dataControlMode2 = [self dataWithHexString:controlMode2];
        unsigned char *pControlMode2 = (unsigned char *)[dataControlMode2 bytes];
        memcpy(pWrite, pControlMode2, 1);
        pWrite ++;
        iWriteBytes ++;
        
        NSData *dataControlState2 = [self dataWithHexString:controlState2];
        unsigned char *pControlState2 = (unsigned char *)[dataControlState2 bytes];
        memcpy(pWrite, pControlState2, 1);
        pWrite ++;
        iWriteBytes ++;
        
        
    }else if([device.strDevice_device_type isEqualToString:@"07"]){
        
        //第一路
        NSString *controlMode1 = @"01";
        NSString *controlState1 = @"00";
        if (device.iDevice_device_state1 == 1) {
            controlState1 = @"01";
        }else{
            controlState1 = @"02";
        }
        NSData *dataControlMode1 = [self dataWithHexString:controlMode1];
        unsigned char *pControlMode1 = (unsigned char *)[dataControlMode1 bytes];
        memcpy(pWrite, pControlMode1, 1);
        pWrite ++;
        iWriteBytes ++;
        
        NSData *dataControlState1 = [self dataWithHexString:controlState1];
        unsigned char *pControlState1 = (unsigned char *)[dataControlState1 bytes];
        memcpy(pWrite, pControlState1, 1);
        pWrite ++;
        iWriteBytes ++;
        
        //第二路
        NSString *controlMode2 = @"01";
        NSString *controlState2 = @"00";
        if (device.iDevice_device_state2 == 1) {
            controlState2 = @"01";
        }else{
            controlState2 = @"02";
        }
        NSData *dataControlMode2 = [self dataWithHexString:controlMode2];
        unsigned char *pControlMode2 = (unsigned char *)[dataControlMode2 bytes];
        memcpy(pWrite, pControlMode2, 1);
        pWrite ++;
        iWriteBytes ++;
        
        NSData *dataControlState2 = [self dataWithHexString:controlState2];
        unsigned char *pControlState2 = (unsigned char *)[dataControlState2 bytes];
        memcpy(pWrite, pControlState2, 1);
        pWrite ++;
        iWriteBytes ++;
        
        //第三路
        NSString *controlMode3 = @"01";
        NSString *controlState3 = @"00";
        if (device.iDevice_device_state3 == 1) {
            controlState3 = @"01";
        }else{
            controlState3 = @"02";
        }
        NSData *dataControlMode3 = [self dataWithHexString:controlMode3];
        unsigned char *pControlMode3 = (unsigned char *)[dataControlMode3 bytes];
        memcpy(pWrite, pControlMode3, 1);
        pWrite ++;
        iWriteBytes ++;
        
        NSData *dataControlState3 = [self dataWithHexString:controlState3];
        unsigned char *pControlState3 = (unsigned char *)[dataControlState3 bytes];
        memcpy(pWrite, pControlState3, 1);
        pWrite ++;
        iWriteBytes ++;
    }else{
        //第一路
        NSString *controlMode1 = @"01";
        NSString *controlState1 = @"00";
        if (device.iDevice_device_state1 == 1) {
            controlState1 = @"01";
        }else{
            controlState1 = @"02";
        }
        
        NSData *dataControlMode1 = [self dataWithHexString:controlMode1];
        unsigned char *pControlMode1 = (unsigned char *)[dataControlMode1 bytes];
        memcpy(pWrite, pControlMode1, 1);
        pWrite ++;
        iWriteBytes ++;
        
        
        NSData *dataControlState1 = [self dataWithHexString:controlState1];
        unsigned char *pControlState1 = (unsigned char *)[dataControlState1 bytes];
        memcpy(pWrite, pControlState1, 1);
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
    char packageEnd = 0x23;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    return dataWrite;
}

#pragma mark - 2.1.1.1 开、关、停控制 (单路控制)
- (NSData *)doGetMultiSwitchControlWithTargetAddr:(NSString *)strTargetAddr
                                           device:(SHModelDevice *)device
                                              way:(int)iWay
                                      controlMode:(NSString *)controlMode
                                     controlState:(NSString *)controlState
{
    //网关Zigbee的MacAddr
    NSData *dataZigbee = [self doGetGatewayZigbeeMacAddrFromLocal];
    unsigned char *pMacAddress = (unsigned char *)[dataZigbee bytes]; //8byte
    
    NSData *dataTargetAddr = [self dataWithHexString:strTargetAddr];
    unsigned char *pTargetAddr = (unsigned char *)[dataTargetAddr bytes];
    
    //帧头 帧长 CmdID Number
    NSString *strHeaderMixed;
    NSString *strSonOptions;
    
    
    if ([device.strDevice_device_type isEqualToString:@"05"] ) {
        strHeaderMixed = @"2A 1C 02 01";
        strSonOptions = @"00 01 80 00";
    }else if ([device.strDevice_device_type isEqualToString:@"06"]){
        strHeaderMixed = @"2A 1E 02 01";
        strSonOptions = @"00 0D 80 00";
    }else if ([device.strDevice_device_type isEqualToString:@"07"]){
        strHeaderMixed = @"2A 20 02 01";
        strSonOptions = @"00 6D 80 00";
    }else{
        strHeaderMixed = @"2A 1C 02 01";
        strSonOptions = @"00 01 80 00";
    }
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
    
    
    if ([device.strDevice_device_type isEqualToString:@"05"]) {
        
        //数据域长度
        char pSonDataLength = 0x06;
        memcpy(pWrite, &pSonDataLength, 1);
        pWrite ++;
        iWriteBytes ++;
    }else if ([device.strDevice_device_type isEqualToString:@"06"]){
        //数据域长度
        char pSonDataLength = 0x08;
        memcpy(pWrite, &pSonDataLength, 1);
        pWrite ++;
        iWriteBytes ++;
    }else if ([device.strDevice_device_type isEqualToString:@"07"]){
        //数据域长度
        char pSonDataLength = 0x0A;
        memcpy(pWrite, &pSonDataLength, 1);
        pWrite ++;
        iWriteBytes ++;
    }else{
        //数据域长度
        char pSonDataLength = 0x06;
        memcpy(pWrite, &pSonDataLength, 1);
        pWrite ++;
        iWriteBytes ++;
    }
    
    //子选择项 4byte
    memcpy(pWrite,pSonOptions, 4);
    pWrite += 4;
    iWriteBytes += 4;
    
    //控制字
    if ([device.strDevice_device_type isEqualToString:@"05"]) {
        if ([device.strDevice_category isEqualToString:@"02"]) {
            //第一路
            NSData *dataControlMode = [self dataWithHexString:controlMode];
            unsigned char *pControlMode = (unsigned char *)[dataControlMode bytes];
            memcpy(pWrite, pControlMode, 1);
            pWrite ++;
            iWriteBytes ++;
            
            NSData *dataControlState = [self dataWithHexString:controlState];
            unsigned char *pControlState = (unsigned char *)[dataControlState bytes];
            memcpy(pWrite, pControlState, 1);
            pWrite ++;
            iWriteBytes ++;
        } else{
            
            //第一路
            NSData *dataControlMode = [self dataWithHexString:controlMode];
            unsigned char *pControlMode = (unsigned char *)[dataControlMode bytes];
            memcpy(pWrite, pControlMode, 1);
            pWrite ++;
            iWriteBytes ++;
            
            NSData *dataControlState = [self dataWithHexString:controlState];
            unsigned char *pControlState = (unsigned char *)[dataControlState bytes];
            memcpy(pWrite, pControlState, 1);
            pWrite ++;
            iWriteBytes ++;
        }
        
    }else if([device.strDevice_device_type isEqualToString:@"06"]){
        switch (iWay) {
            case 1:
            {
                //第一路
                NSData *dataControlMode = [self dataWithHexString:controlMode];
                unsigned char *pControlMode = (unsigned char *)[dataControlMode bytes];
                memcpy(pWrite, pControlMode, 1);
                pWrite ++;
                iWriteBytes ++;
                
                NSData *dataControlState = [self dataWithHexString:controlState];
                unsigned char *pControlState = (unsigned char *)[dataControlState bytes];
                memcpy(pWrite, pControlState, 1);
                pWrite ++;
                iWriteBytes ++;
                
                
                //第二路
                NSString *strSecondRoad = @"00 00";
                NSData *dataSencondRoad = [self dataWithHexString:strSecondRoad];
                unsigned char *pSencondRoad = (unsigned char *)[dataSencondRoad bytes];
                memcpy(pWrite,pSencondRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
            }
                break;
            case 2:
            {
                //第一路
                NSString *strFirstRoad = @"00 00";
                NSData *dataFirstRoad = [self dataWithHexString:strFirstRoad];
                unsigned char *pFirstRoad = (unsigned char *)[dataFirstRoad bytes];
                memcpy(pWrite,pFirstRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
                
                
                //第二路
                NSData *dataControlMode = [self dataWithHexString:controlMode];
                unsigned char *pControlMode = (unsigned char *)[dataControlMode bytes];
                memcpy(pWrite, pControlMode, 1);
                pWrite ++;
                iWriteBytes ++;
                
                NSData *dataControlState = [self dataWithHexString:controlState];
                unsigned char *pControlState = (unsigned char *)[dataControlState bytes];
                memcpy(pWrite, pControlState, 1);
                pWrite ++;
                iWriteBytes ++;
            }
                break;
                
            default:
            {
                //第一路
                NSString *strFirstRoad = @"00 00";
                NSData *dataFirstRoad = [self dataWithHexString:strFirstRoad];
                unsigned char *pFirstRoad = (unsigned char *)[dataFirstRoad bytes];
                memcpy(pWrite,pFirstRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
                
                //第二路
                NSString *strSecondRoad = @"00 00";
                NSData *dataSencondRoad = [self dataWithHexString:strSecondRoad];
                unsigned char *pSencondRoad = (unsigned char *)[dataSencondRoad bytes];
                memcpy(pWrite,pSencondRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
            }
                break;
        }
        
        
    }else if([device.strDevice_device_type isEqualToString:@"07"]){
        
        switch (iWay) {
            case 1:
            {
                //第一路
                NSData *dataControlMode = [self dataWithHexString:controlMode];
                unsigned char *pControlMode = (unsigned char *)[dataControlMode bytes];
                memcpy(pWrite, pControlMode, 1);
                pWrite ++;
                iWriteBytes ++;
                
                NSData *dataControlState = [self dataWithHexString:controlState];
                unsigned char *pControlState = (unsigned char *)[dataControlState bytes];
                memcpy(pWrite, pControlState, 1);
                pWrite ++;
                iWriteBytes ++;
                
                //第二路
                NSString *strSecondRoad = @"00 00";
                NSData *dataSencondRoad = [self dataWithHexString:strSecondRoad];
                unsigned char *pSencondRoad = (unsigned char *)[dataSencondRoad bytes];
                memcpy(pWrite,pSencondRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
                //第三路
                NSString *strThirdRoad = @"00 00";
                NSData *dataThirdRoad = [self dataWithHexString:strThirdRoad];
                unsigned char *pThirdRoad = (unsigned char *)[dataThirdRoad bytes];
                memcpy(pWrite,pThirdRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
            }
                break;
            case 2:
            {
                //第一路
                NSString *strFirstRoad = @"00 00";
                NSData *dataFirstRoad = [self dataWithHexString:strFirstRoad];
                unsigned char *pFirstRoad = (unsigned char *)[dataFirstRoad bytes];
                memcpy(pWrite,pFirstRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
                //第二路
                NSData *dataControlMode = [self dataWithHexString:controlMode];
                unsigned char *pControlMode = (unsigned char *)[dataControlMode bytes];
                memcpy(pWrite, pControlMode, 1);
                pWrite ++;
                iWriteBytes ++;
                
                NSData *dataControlState = [self dataWithHexString:controlState];
                unsigned char *pControlState = (unsigned char *)[dataControlState bytes];
                memcpy(pWrite, pControlState, 1);
                pWrite ++;
                iWriteBytes ++;
                
                //第三路
                NSString *strThirdRoad = @"00 00";
                NSData *dataThirdRoad = [self dataWithHexString:strThirdRoad];
                unsigned char *pThirdRoad = (unsigned char *)[dataThirdRoad bytes];
                memcpy(pWrite,pThirdRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
            }
                break;
            case 3:
            {
                //第一路
                NSString *strFirstRoad = @"00 00";
                NSData *dataFirstRoad = [self dataWithHexString:strFirstRoad];
                unsigned char *pFirstRoad = (unsigned char *)[dataFirstRoad bytes];
                memcpy(pWrite,pFirstRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
                //第二路
                NSString *strSecondRoad = @"00 00";
                NSData *dataSencondRoad = [self dataWithHexString:strSecondRoad];
                unsigned char *pSencondRoad = (unsigned char *)[dataSencondRoad bytes];
                memcpy(pWrite,pSencondRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
                //第三路
                NSData *dataControlMode = [self dataWithHexString:controlMode];
                unsigned char *pControlMode = (unsigned char *)[dataControlMode bytes];
                memcpy(pWrite, pControlMode, 1);
                pWrite ++;
                iWriteBytes ++;
                
                NSData *dataControlState = [self dataWithHexString:controlState];
                unsigned char *pControlState = (unsigned char *)[dataControlState bytes];
                memcpy(pWrite, pControlState, 1);
                pWrite ++;
                iWriteBytes ++;
            }
                break;
                
            default:
            {
                //第一路
                NSString *strFirstRoad = @"00 00";
                NSData *dataFirstRoad = [self dataWithHexString:strFirstRoad];
                unsigned char *pFirstRoad = (unsigned char *)[dataFirstRoad bytes];
                memcpy(pWrite,pFirstRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
                
                //第二路
                NSString *strSecondRoad = @"00 00";
                NSData *dataSencondRoad = [self dataWithHexString:strSecondRoad];
                unsigned char *pSencondRoad = (unsigned char *)[dataSencondRoad bytes];
                memcpy(pWrite,pSencondRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
                
                
                //第三路
                NSString *strThirdRoad = @"00 00";
                NSData *dataThirdRoad = [self dataWithHexString:strThirdRoad];
                unsigned char *pThirdRoad = (unsigned char *)[dataThirdRoad bytes];
                memcpy(pWrite,pThirdRoad, 2);
                pWrite += 2;
                iWriteBytes += 2;
            }
                break;
        }
    } else {
        
        NSData *dataControlMode = [self dataWithHexString:controlMode];
        unsigned char *pControlMode = (unsigned char *)[dataControlMode bytes];
        memcpy(pWrite, pControlMode, 1);
        pWrite ++;
        iWriteBytes ++;
        
        NSData *dataControlState = [self dataWithHexString:controlState];
        unsigned char *pControlState = (unsigned char *)[dataControlState bytes];
        memcpy(pWrite, pControlState, 1);
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
    char packageEnd = 0x23;
    memcpy(pWrite, &packageEnd, 1);
    pWrite ++;
    iWriteBytes++;
    
    NSData *dataWrite = [NSData dataWithBytes:buffer length:iWriteBytes];
    return dataWrite;
}

@end
