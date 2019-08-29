//
//  NetworkEngine+UnPack.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#define RANGE(_location_,_length_) NSMakeRange(_location_,_length_)

#import "NetworkEngine+UnPack.h"
#import "HLDataUtil.h"
#import "HLOrderUtil.h"

@implementation NetworkEngine (UnPack)

- (void)setStrWaterFirstFrame:(NSString *)strWaterFirstFrame
{
    [self bk_associateValue:strWaterFirstFrame withKey:@"strWaterFirstFrame"];
}

-(NSString *)strWaterFirstFrame
{
    return [self bk_associatedValueForKey:@"strWaterFirstFrame"];
}

- (void)setStrWaterSecondFrame:(NSString *)strWaterSecondFrame
{
    [self bk_associateValue:strWaterSecondFrame withKey:@"strWaterSecondFrame"];
}

-(NSString *)strWaterSecondFrame
{
    return [self bk_associatedValueForKey:@"strWaterSecondFrame"];
}

- (void)setRespondData:(NSMutableData *)respondData
{
    [self bk_associateValue:respondData withKey:@"rspData"];
}

- (NSMutableData *)respondData
{
    return [self bk_associatedValueForKey:@"rspData"];
}

- (void)setIRememberLength:(int)iRememberLength
{
    [self bk_associateValue:@(iRememberLength) withKey:@"rememberLength"];
}

- (int)iRememberLength
{
    return [[self bk_associatedValueForKey:@"rememberLength"] intValue];
}

- (void)setIResponseSize:(int)iResponseSize
{
    [self bk_associateValue:@(iResponseSize) withKey:@"iResponseSize"];
}

- (int)iResponseSize
{
    return [[self bk_associatedValueForKey:@"iResponseSize"] intValue];
}

- (void)setCloudRespondData:(NSMutableData *)cloudRespondData
{
    [self bk_associateValue:cloudRespondData withKey:@"cloudRespondData"];
}

- (NSMutableData *)cloudRespondData
{
    return [self bk_associatedValueForKey:@"cloudRespondData"];
}

- (void)setICloudRememberLength:(int)iCloudRememberLength
{
    [self bk_associateValue:@(iCloudRememberLength) withKey:@"iCloudRememberLength"];
}

- (int)iCloudRememberLength
{
    return [[self bk_associatedValueForKey:@"iCloudRememberLength"] intValue];
}

- (void)setICloudResponseSize:(int)iCloudResponseSize
{
    [self bk_associateValue:@(iCloudResponseSize) withKey:@"iCloudResponseSize"];
}

- (int)iCloudResponseSize
{
    return [[self bk_associatedValueForKey:@"iCloudResponseSize"] intValue];
}

#pragma mark -
#pragma mark 处理Rsp数据（粘包，缺包等问题）

- (void)handleLocalRspData:(NSData *)pData
{
    @try {
        if (!self.respondData) {
            self.respondData = [[NSMutableData alloc] init];
            self.iRememberLength = 0;
            self.iResponseSize = 0;
        }
        
        //获取包头
//        Byte *testByte = (Byte *)[pData bytes];
        unsigned char *pDataBytes = (unsigned char *)[pData bytes];
        unsigned char pFrameStartOperator = *pDataBytes;    //帧头
        unsigned char pDataFrameLength = *(pDataBytes + 1); //帧长本身的数据即代表数据域长度
//        unsigned char pFrameEndOperator = *(pDataBytes + (int)pDataFrameLength + 3); //帧尾
//        unsigned char pFrameCS  = *(pDataBytes + (int)pDataFrameLength + 2); //校验码
        
        
        if (pFrameStartOperator == CMW_FrameStart) {
            //            if (self.respondData.length > 0 || self.iRememberLength != 0) {
            //                NSLog(@"异常情况，包未收完，却来了新包，之前的丢掉");
            //                self.respondData = nil;
            //                self.respondData = [[NSMutableData alloc] init];
            //                self.iRememberLength = 0;
            //                self.iResponseSize = 0;
            //            }
            
            int iTheLengthOfFrameStart = 1;                       //帧头长度
            int iTheLengthOfFrameEnd = 1;                         //帧尾长度
            int iTheLengthOfFrameLengthSelf = 1;                  //帧长本身长度
            int iTheLengthOfCS = 1;                               //CS长度
            int iTheLengthOfDataFields = (int)pDataFrameLength;   //数据域长度
            
            int iTheLengthOfDataFrame = iTheLengthOfFrameStart + iTheLengthOfFrameLengthSelf + iTheLengthOfDataFields + iTheLengthOfCS + iTheLengthOfFrameEnd;
            self.iResponseSize = iTheLengthOfDataFrame;  //整个包的大小
            
            if (pData.length == iTheLengthOfDataFrame) {
                LLog(@"正确情况!");
                if([self doHandleJudeThePackIsShouldUnPack:pData]){
                    [self doUnPackReceivedData:pData];
                    [self handleTheRemDataAndiRememberLength];
                }else{
                    LLog(@"异常情况");
                    self.respondData = nil;
                    self.respondData = [[NSMutableData alloc] init];
                    self.iRememberLength = 0;
                    self.iResponseSize = 0;
                }
            }else if (pData.length < iTheLengthOfDataFrame){
                
                self.iRememberLength = iTheLengthOfDataFrame - (int)pData.length;
                [self.respondData appendData:pData];
                LLog([NSString stringWithFormat:@"body未接收完, iRememberLength = %d", self.iRememberLength]);
            }else{
                LLog(@"粘包情况，截包处理");
                [self.respondData appendData:[pData subdataWithRange:NSMakeRange(0, iTheLengthOfDataFrame)]];
                
                if ([self doHandleJudeThePackIsShouldUnPack:self.respondData]) {
                    [self doUnPackReceivedData:self.respondData];
                    [self handleTheRemDataAndiRememberLength];
                }else{
                    LLog(@"****异常情况*****");
                    self.respondData = nil;
                    self.respondData = [[NSMutableData alloc] init];
                    self.iRememberLength = 0;
                    self.iResponseSize = 0;
                }
                NSMutableData *remainData = [[NSMutableData alloc] init];
                [remainData appendData:[pData subdataWithRange:NSMakeRange(iTheLengthOfDataFrame, (int)pData.length - iTheLengthOfDataFrame)]];
                [self handleLocalRspData:remainData];
            }
        }else{
            LLog([NSString stringWithFormat:@"处理数据： pDataLength = %lu, iRememberLength = %d ", (unsigned long)pData.length, self.iRememberLength]);
            if (pData.length == self.iRememberLength)
            {
                LLog(@"粘包处理，剩下的数据和收到数据相等");
                [self.respondData appendData:[pData subdataWithRange:NSMakeRange(0, self.iRememberLength)]];
                
                if ([self doHandleJudeThePackIsShouldUnPack:self.respondData]) {
                    [self doUnPackReceivedData:self.respondData];
                    [self handleTheRemDataAndiRememberLength];
                }else{
                    LLog(@"****异常情况*****");
                    self.respondData = nil;
                    self.respondData = [[NSMutableData alloc] init];
                    self.iRememberLength = 0;
                    self.iResponseSize = 0;
                }
            }else if (pData.length < self.iRememberLength){
                LLog(@"粘包处理，收到数据小于剩余该收的数据");
                self.iRememberLength = self.iRememberLength - (int)pData.length;
                [self.respondData appendData:pData];
            }else{
                LLog(@"粘包处理，收到数据大于剩余该收的数据");
                [self.respondData appendData:[pData subdataWithRange:NSMakeRange(0, self.iRememberLength)]];
                int tmpRememberLength = self.iRememberLength;
                if ([self doHandleJudeThePackIsShouldUnPack:self.respondData]) {
                    [self doUnPackReceivedData:self.respondData];
                }else{
                    LLog(@"****异常情况*****");
                }
                [self handleTheRemDataAndiRememberLength];
                NSMutableData *remainData = [[NSMutableData alloc] init];
                [remainData appendData:[pData subdataWithRange:NSMakeRange(tmpRememberLength, (int)pData.length - tmpRememberLength)]];
                [self handleLocalRspData:remainData];
            }
        }
    } @catch (NSException *exception) {
        
        LLog([NSString stringWithFormat:@"处理粘包异常Three :%@", [exception reason]]);
        [self handleTheRemDataAndiRememberLength];
        
    } @finally {
        
        
    }
}


- (BOOL)doHandleJudeThePackIsShouldUnPack:(NSData *)dataTemp
{
    Byte *dataByte = (Byte *)[dataTemp bytes];
    long packetHeader = dataByte[0];
    long packetEnd = dataByte[dataTemp.length - 1];
    long packetCS = dataByte[dataTemp.length - 2];
    long packetLength = dataByte[1];
    
    //先校验包头
    if (packetHeader == 42) {
        //其次校验包尾
        if (packetEnd == 35) {
            
            //然后校验CS
            long  packageCSDataResult = dataByte[2];
            for (long i = 3; i < packetLength + 2; i ++)
            {
                packageCSDataResult = packageCSDataResult + dataByte[i];
            }
            long pTempCS = packageCSDataResult%256;
            if (pTempCS == packetCS) {
                return YES;
            }
        }
    }
    return NO;
}

#pragma mark - 云端少包，缺包处理
- (void)handleCloudSeverData:(NSData *)cloudSeverData
{
    [self doUnPackReceivedData:cloudSeverData];
}

#pragma mark - private method
/**
 *  清空清零_respondData 和 self.iRememberLength
 */
- (void)handleTheRemDataAndiRememberLength
{
    //    NSLog(@"清除数据1");
    self.respondData = nil;
    self.iRememberLength = 0;
    self.iResponseSize = 0;
}

- (void)handleTheCloudRemDataAndIRememberLength
{
    NSLog(@"清除数据2");
    self.cloudRespondData = nil;
    self.iCloudRememberLength = 0;
    self.iCloudResponseSize = 0;
}

/**
 *  解析局域网数据数据
 *  @param dataReceive sever return data
 */
- (void)doUnPackReceivedData:(NSData *)dataReceive
{
    NSString *strHexWholeData = [[ToolHexManager sharedManager] convertDataToHexStr:dataReceive];
    NSString *strHexHeader = [strHexWholeData substringWithRange:NSMakeRange(0, 2)];
    if ([strHexHeader isEqualToString:@"55"]) {
        //连接cloudsever返回的数据
        [self doUnPackReceivedCludData:dataReceive];
        
    }else{
        //获取到包CmdID
        //目前用到的有01，07
        NSString * strHexCmID = [strHexWholeData substringWithRange:NSMakeRange(4, 2)];
        if ([strHexCmID isEqualToString:kCmdID_StrHexRead_OD]) {
            //一般数据上报
            [self doHandleCommonDataReported:strHexWholeData];
        }else if ([strHexCmID isEqualToString:kCmdID_StrHexAdvertisement]){
            //广播命令返回数据
        }else if ([strHexCmID isEqualToString:kCmdID_StrHexTransmit]){
            //协议转发命令返回数据
            [self doHandleProtocolTransmitDataReported:strHexWholeData];
        }else if ([strHexCmID isEqualToString:kCmdID_StrHexRead_OD_Error]){
            LLog(@"一般上报数据错误");
        }else if ([strHexCmID isEqualToString:kCmdID_StrHexWrite_OD]){
            LLog(@"Write_OD命令");
            [self doHandleGetGatewayWifiDetailsWithWholeData:strHexWholeData];
            
        }else if ([strHexCmID isEqualToString:kCmdID_StrHexWrite_OD_Error]){
            LLog(@"Write_OD命令执行错误");
        }else if([strHexCmID isEqualToString:kCmdID_StrHexScreenConfig]){
            //            NSLog(@"配置场景命令成功返回");
            
            NSString *strHexIdentifer = [strHexWholeData substringWithRange:NSMakeRange(24, 2)];
            if ([strHexIdentifer isEqualToString:@"01"]) {
                LLog(@"子命令标识，表示参数配置命令");
                
            }else if ([strHexIdentifer isEqualToString:@"02"]){
                
                LLog(@"子命令标识，表示控制帧载入命令");
            }else if ([strHexIdentifer isEqualToString:@"03"]){
                
                LLog(@"子命令标识，表示参数读取命令");
            }else if ([strHexIdentifer isEqualToString:@"05"]){
                
                LLog(@"子命令标识，设防类型配置命令");
            }else if ([strHexIdentifer isEqualToString:@"06"]){
                
                LLog(@"子命令标识，场景联动信息读取命令");
                NSString *strScreenInGatewayCount = [strHexWholeData substringWithRange:NSMakeRange(34, 2)];
                NSInteger iCount = [[ToolHexManager sharedManager] numberWithHexString:strScreenInGatewayCount];
                NSLog(@"目前场景中场景数量为：%ld",(long)iCount);
                
            }else if ([strHexIdentifer isEqualToString:@"07"]){
                
                LLog(@"子命令标识，场景联动删除命令");
            }else if ([strHexIdentifer isEqualToString:@"08"]){
                
                LLog(@"子命令标识，报警状态解除命令");
            }
            
            [self doHandleAnalyseConfigurationScreenWithWholeData:strHexWholeData];
        }else if([strHexCmID isEqualToString:kCmdID_StrHexScreenConfigFail]){
            //            NSLog(@"配置场景命令失败返回");
            [self doHandleAnalyseConfigurationScreenWithWholeData:strHexWholeData];
        }else{
            LLog(@"其它未知CMD_ID");
        }
    }
}

#pragma mark -
#pragma mark - Private 一般数据上报收到Data
- (void)doHandleCommonDataReported:(NSString *)strHexWholeData
{
    if (strHexWholeData.length < 38) {
        return;
    }
    
    NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexWholeData];
    NSString *strName;
    NSString * strHexOD = [strHexWholeData substringWithRange:NSMakeRange(22, 4)];
    if ([strHexOD isEqualToString:kOD_SwitchActionControl_Samll]) {
        strName = @"多用途开关动作控制器";
        LLog([NSString stringWithFormat:@"上报\n%@:\n%@",strName,strBigTemp]);
        [self doHandleNewSwitchActionControlSonIndexWholeData:strHexWholeData];
    }else if ([strHexOD isEqualToString:kOD_DormancyControl_Samll]) {
        strName = @"多用途休眠设备控制器";
        LLog([NSString stringWithFormat:@"上报\n%@:\n%@",strName,strBigTemp]);
        [self doHandleNew4030DormancyControlSonIndexWholeData:strHexWholeData];
    }else if ([strHexOD isEqualToString:kOD_Socket_Samll]) {
        strName = @"插座";
        LLog([NSString stringWithFormat:@"上报\n%@:\n%@",strName,strBigTemp]);
    }else if ([strHexOD isEqualToString:kOD_Ammeter_Samll]) {
        strName = @"电表";
        LLog([NSString stringWithFormat:@"上报\n%@:\n%@",strName,strBigTemp]);
        [self doHandle4040MeasureSoketWithWholeData:strHexWholeData];
        
    }else if ([strHexOD isEqualToString:kOD_ProtocolTransmit_Samll]) {
        //        strName = @"协议转发-主动上报";
        //        NSLog(@"\n%@:\n%@",strName,strBigTemp);
        [self doHandleNew4070ProtocolTransmitDataReported:strHexWholeData];
    }else if ([strHexOD isEqualToString:kOD_EquipmentParameter_Samll]) {
        strName = @"设备配置参数";
        LLog([NSString stringWithFormat:@"上报\n%@:\n%@",strName,strBigTemp]);
    }else if ([strHexOD isEqualToString:kOD_EquipmentNeighbourParameter_Samll]) {
        /*NSLog(@"设备邻居表参数(获取网关MacAddr) :%@",strHexWholeData);*/
        strName = @"设备邻居表参数(获取网关MacAddr)";
        LLog([NSString stringWithFormat:@"上报\n%@:\n%@",strName,strBigTemp]);
        [self doHandleTheGatewayMacAddress:strHexWholeData];
    }else if ([strHexOD isEqualToString:kOD_TimeParameter_Samll]) {
        /*NSLog(@"当前时间参数");*/
        strName = @"当前时间参数";
        LLog([NSString stringWithFormat:@"上报\n%@:\n%@",strName,strBigTemp]);
    }else if ([strHexOD isEqualToString:kOD_NetParameter_Samll]) {
        /*NSLog(@"节点网络参数");*/
        strName = @"节点网络参数";
        LLog([NSString stringWithFormat:@"上报\n%@:\n%@",strName,strBigTemp]);
    }else if ([strHexOD isEqualToString:kOD_Heart_Small]){
        /*NSLog(@"心跳数据%@",strHexWholeData);*/
        strName = @"心跳数据";
        LLog([NSString stringWithFormat:@"上报\n%@:\n%@",strName,strBigTemp]);
        [self doxxxxxxxx:strHexWholeData];
        LLog(@"***********发送心跳归零*************");
        NSData *data = [self doGetGatewayHeartClearToZero];
        [self sendRequestData:data];
        
    }else if ([strHexOD isEqualToString:kOD_HeartLoginFrameSmall]){
        /*NSLog(@"心跳数据%@",strHexWholeData);*/
        strName = @"登录帧发送成功返回数据";
        LLog([NSString stringWithFormat:@"上报\n%@:\n%@",strName,strBigTemp]);
        NSData *data = [self doGetGatewayHeartClearToZero];
        [self sendRequestData:data];
        
    }else if ([strHexOD isEqualToString:kOD_GetGatewayTime_Small]){
        /*NSLog(@"心跳数据%@",strHexWholeData);*/
        strName = @"返回获取网关当前时间";
        LLog([NSString stringWithFormat:@"上报\n%@:\n%@",strName,strBigTemp]);
        [self doHandleGatewayCurrentTime:strHexWholeData];
        
        
    }else if ([strHexOD isEqualToString:kOD_GetGatewayWifiDetail_small]){
        /*NSLog(@"心跳数据%@",strHexWholeData);*/
        strName = @"返回获取网关wii详细信息";
        NSLog(@"%@", [NSString stringWithFormat:@"上报\n%@:\n%@",strName,strBigTemp]);
        [self doHandleGetGatewayWifiDetailsWithWholeData:strHexWholeData];
        
        
    }else {
        /*NSLog(@"意外数据%@",strHexWholeData);*/
        strName = @"意外数据";
        LLog([NSString stringWithFormat:@"上报\n%@:\n%@",strName,strBigTemp]);
    }
}


#pragma mark -
#pragma mark - 获取当前网关的时间
- (void)doHandleGatewayCurrentTime:(NSString *)strHexWholeData
{
    NSString *strYearHex       = [strHexWholeData substringWithRange:NSMakeRange(30, 2)];
    NSString *strMonthHex      = [strHexWholeData substringWithRange:NSMakeRange(32, 2)];
    NSString *strDayHex        = [strHexWholeData substringWithRange:NSMakeRange(34, 2)];
    
//    NSString *strWeekHex       = [strHexWholeData substringWithRange:NSMakeRange(36, 2)];
    
    NSString *strHourHex       = [strHexWholeData substringWithRange:NSMakeRange(38, 2)];
    NSString *strMinuteHex     = [strHexWholeData substringWithRange:NSMakeRange(40, 2)];
    NSString *strSecondHex     = [strHexWholeData substringWithRange:NSMakeRange(42, 2)];
    
    /*
     NSString *strYear       = [NSString stringWithFormat:@"20%ld",(long)[[ToolHexManager sharedManager] numberWithHexString:strYearHex]];
     NSString *strMonth      = [NSString stringWithFormat:@"%ld",(long)[[ToolHexManager sharedManager] numberWithHexString:strMonthHex]];
     NSString *strDay        = [NSString stringWithFormat:@"%ld",(long)[[ToolHexManager sharedManager] numberWithHexString:strDayHex]];
     
     NSString *strWeek       = [NSString stringWithFormat:@"%ld",(long)[[ToolHexManager sharedManager] numberWithHexString:strWeekHex]];
     
     NSString *strHour       = [NSString stringWithFormat:@"%ld",(long)[[ToolHexManager sharedManager] numberWithHexString:strHourHex]];
     NSString *strMinute     = [NSString stringWithFormat:@"%ld",(long)[[ToolHexManager sharedManager] numberWithHexString:strMinuteHex]];
     NSString *strSecond     = [NSString stringWithFormat:@"%ld",(long)[[ToolHexManager sharedManager] numberWithHexString:strSecondHex]];
     
     NSString *strWholeTime = [NSString stringWithFormat:@"%@:%@:%@:%@:%@:%@",strYear,strMonth,strDay,strHour,strMinute,strSecond];
     NSLog(@"获取时间为:%@",strWholeTime);
     */
    
    
    NSString *strYear       = [NSString stringWithFormat:@"20%@",[self doHandleNumberWithHexString:strYearHex]];
    NSString *strMonth      = [NSString stringWithFormat:@"%@",[self doHandleNumberWithHexString:strMonthHex]];
    NSString *strDay        = [NSString stringWithFormat:@"%@",[self doHandleNumberWithHexString:strDayHex]];
    
//    NSString *strWeek       = [NSString stringWithFormat:@"%@",[self doHandleNumberWithHexString:strWeekHex]];
    
    NSString *strHour       = [NSString stringWithFormat:@"%@",[self doHandleNumberWithHexString:strHourHex]];
    NSString *strMinute     = [NSString stringWithFormat:@"%@",[self doHandleNumberWithHexString:strMinuteHex]];
    NSString *strSecond     = [NSString stringWithFormat:@"%@",[self doHandleNumberWithHexString:strSecondHex]];
    
    NSString *strWholeTime = [NSString stringWithFormat:@"%@:%@:%@:%@:%@:%@",strYear,strMonth,strDay,strHour,strMinute,strSecond];
    LLog([NSString stringWithFormat:@"获取时间为:%@",strWholeTime]);
    
    
    
}




- (void)doxxxxxxxx:(NSString *)strHexWholeData
{
    if (strHexWholeData.length < 50) {
        return;
    }
    NSString *strGatewayHexMacAddr  = [strHexWholeData substringWithRange:NSMakeRange(6, 16)];
    NSString *strHexOD              = [strHexWholeData substringWithRange:NSMakeRange(22, 4)];
    NSString *strSytemState         = [strHexWholeData substringWithRange:NSMakeRange(38, 2)];
    
    NSString *strContinuousCode     = [strHexWholeData substringWithRange:NSMakeRange(42, 2)];
    NSString *strHeartCycle         = [strHexWholeData substringWithRange:NSMakeRange(44, 2)];
    NSString *strWifiMacAddr        = [strHexWholeData substringWithRange:NSMakeRange(46, 16)];
    NSString *strZigbeeMacAddr      = [strHexWholeData substringWithRange:NSMakeRange(62, 16)];
    
    NSString *strGatewayHexBigMacAddr   = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strGatewayHexMacAddr];
    NSString *strBigHexOD               = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexOD];
    NSString *strBigSytemState          = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strSytemState];
    
    NSString *strBigContinuousCode      = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strContinuousCode];
    NSString *strBigHeartCycle          = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHeartCycle];
    NSString *strBigWifiMacAddr         = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strWifiMacAddr];
    NSString *strBigZigbeeMacAddr       = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strZigbeeMacAddr];
    
    NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexWholeData];
    LLog([NSString stringWithFormat:@"\n心跳:\n%@",strBigTemp]);
    LLog([NSString stringWithFormat:@"\n网关的macAddr:%@;\nOD:%@;\n系统状态:%@;\n细条连续码:%@;\n心跳周期:%@;\n wifiMacAddr:%@;\n zigbeeMacAddr:%@",strGatewayHexBigMacAddr,strBigHexOD,strBigSytemState,strBigContinuousCode,strBigHeartCycle,strBigWifiMacAddr,strBigZigbeeMacAddr]);
}

#pragma mark -
#pragma mark - 收到心跳数据就进行回复
- (void)doAnswerToTheGateway
{
    
}

#pragma mark - 获取网关MacAddr
- (void)doHandleTheGatewayMacAddress:(NSString *)strHexWholeData
{
    NSString *strHexMacAddr = [strHexWholeData substringWithRange:NSMakeRange(6, 16)];
    
    NSString *strZigbeeBigMacAddr = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexMacAddr];
    [[SHLoginManager shareInstance] doWriteGatewayMacAddr:strZigbeeBigMacAddr];
    
    //暂时@“设定时间”放在这里
    NSData *dataTime = [[NetworkEngine shareInstance] doHandleSetGatewayTimeNew];
    [[NetworkEngine shareInstance] sendRequestData:dataTime];
    [[NSNotificationCenter defaultCenter] postNotificationName:kGetGatewayMacAddr
                                                        object:strHexMacAddr
                                                      userInfo:nil];
    
    
}


#pragma mark -
#pragma mark - private 协议转发的
- (void)doHandleProtocolTransmitDataReported:(NSString *)strHexWholeData
{
    
    NSString *strHexCmID           = [strHexWholeData substringWithRange:NSMakeRange(4,  2)];
    NSString *strDeviceHexMacAddr  = [strHexWholeData substringWithRange:NSMakeRange(6, 16)];
    NSString *strHexOD             = [strHexWholeData substringWithRange:NSMakeRange(22, 4)];
    NSString *strDeviceStyle       = [strHexWholeData substringWithRange:NSMakeRange(26, 2)];
    NSString *strDeviceSort        = [strHexWholeData substringWithRange:NSMakeRange(28, 2)];
    
    NSString *strBigHexCmID        = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexCmID];
    NSString *strZigbeeBigMacAddr  = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strDeviceHexMacAddr];
    NSString *strBigHexOD          = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexOD];
    NSString *strBigHexDeviceStyle = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strDeviceStyle];
    NSString *strBigSort           = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strDeviceSort];
    LLog([NSString stringWithFormat:@"CmdID:%@,OD:%@,type:%@,sort:%@",strBigHexCmID,strBigHexOD,strBigHexDeviceStyle,strBigSort]);
    
    NSString *strFingerID = @"";
    NSString *strBigIdentifer = @"";
    NSString *strBigLocation = @"00";
    NSString *strOpenXMYLockReport_0002_IDType = @"00";
    int iState = 0;
    
    if ([strBigHexOD isEqualToString:@"0F BE"]) {
        if ([strBigHexDeviceStyle isEqualToString:@"02"]) {
            if ([strBigSort isEqualToString:@"02"])
            {
                if (strHexWholeData.length > 48) {
                    NSString *strCmdID = [self handleGetLockCmdIDWithWholeFrame:strHexWholeData];
                    NSString *strBigConfirmOpenLockRight = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strCmdID];
                    if ([strBigConfirmOpenLockRight isEqualToString:@"00"] || [strBigConfirmOpenLockRight isEqualToString:@"53"]) {
                        LLog(@"确认正确指令");
                        strBigIdentifer = strBigConfirmOpenLockRight;
                    }else{
                        NSString * strIdentiferTemp = [self handleGetLockCmdIDWithWholeFrame:strHexWholeData];
                        strBigIdentifer = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strIdentiferTemp];
                        if ([strBigIdentifer isEqualToString:@"00"]) {
                            LLog(@"确认正确指令");
                        }else if ([strBigIdentifer isEqualToString:@"01"]){
                            LLog(@"确认错误指令");
                            
                        }else if ([strBigIdentifer isEqualToString:@"10"]){
                            LLog(@"恢复出厂设置");
                            
                        }else if ([strBigIdentifer isEqualToString:@"11"]){
                            LLog(@"注册密码成功");
                            
                        }else if ([strBigIdentifer isEqualToString:@"12"]){
                            LLog(@"删除密码成功");
                            
                        }else if ([strBigIdentifer isEqualToString:@"13"]){
                            LLog(@"注册指纹成功");
                            
                        }else if ([strBigIdentifer isEqualToString:@"14"]){
                            LLog(@"删除指纹成功");
                            
                        }else if ([strBigIdentifer isEqualToString:@"15"]){
                            LLog(@"注册IC卡成功");
                            
                        }else if ([strBigIdentifer isEqualToString:@"16"]){
                            LLog(@"删除IC卡成功");
                            
                        }else if ([strBigIdentifer isEqualToString:@"20"]){
                            LLog(@"指纹锁非法操作");
                            
                        }else if ([strBigIdentifer isEqualToString:@"21"]){
                            LLog(@"指纹锁防撬报警");
                            
                        }else if ([strBigIdentifer isEqualToString:@"30"]){
                            LLog(@"电池欠压状态上报");
                            
                        }else if ([strBigIdentifer isEqualToString:@"31"]){
                            LLog(@"斜舌状态上报");
                            
                        }else if ([strBigIdentifer isEqualToString:@"32"]){
                            LLog(@"方舌状态上报");
                            
                        }else if ([strBigIdentifer isEqualToString:@"33"]){
                            LLog(@"机械钥匙状态上报");
                            
                        }else if ([strBigIdentifer isEqualToString:@"34"]){
                            LLog(@"反锁状态上报");
                            
                        }else if ([strBigIdentifer isEqualToString:@"35"]){
                            LLog(@"门磁状态上报");
                            
                        }else if ([strBigIdentifer isEqualToString:@"40"]){
                            LLog(@"无线模块入网指令");
                            
                        }else if ([strBigIdentifer isEqualToString:@"41"]){
                            LLog(@"无线模块退网指令");
                            
                        }else if ([strBigIdentifer isEqualToString:@"50"]){
                            NSString * strFinger = [strHexWholeData substringWithRange:NSMakeRange(strHexWholeData.length - 8,  2)];
                            NSString * strBigFinger = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strFinger];
                            strFingerID = strBigFinger;
                            LLog([NSString stringWithFormat:@"指纹开锁情况上报,指纹开锁成功ID:%@",strFingerID]);
                            //                            NSString *strName = [self doHanleGetFingerIDWithMacAddr:strZigbeeBigMacAddr fingerId:strBigFinger];
                            //                            [[GAlertMessageManager shareInstance] showMessag:[NSString stringWithFormat:@"%@ 开锁成功！",strName] delayTime:1];
                            
                        }else if ([strBigIdentifer isEqualToString:@"51"]){
                            LLog(@"密码开锁情况上报");
                            
                             [XWHUDManager showSuccessTipHUD:@"密码开锁成功"];
                            
                        }else if ([strBigIdentifer isEqualToString:@"52"]){
                            LLog(@"IC卡开锁情况上报");
                            
                        }else if ([strBigIdentifer isEqualToString:@"60"]){
                            LLog(@"智能家居系统指令开锁");
                            
                        }else if ([strBigIdentifer isEqualToString:@"70"]){
                            LLog(@"按键扫描成功：清空键");
                            
                        }else if ([strBigIdentifer isEqualToString:@"71"]){
                            LLog(@"按键扫描成功：设置键");
                            
                        }else if ([strBigIdentifer isEqualToString:@"72"]){
                            LLog(@"按键扫描成功：语音键");
                            
                        }else if ([strBigIdentifer isEqualToString:@"90"]){
                            LLog(@"密码验证");
                            
                        }else if ([strBigIdentifer isEqualToString:@"91"]){
                            LLog(@"动态密码");
                            
                        }else if ([strBigIdentifer isEqualToString:@"A0"]){
                            LLog(@"校准时间");
                            
                        }else{
                            
                            LLog(@"其它");
                        }
                    }
                    
                }else{
                    LLog(@"d****************d");
                }
            }else if ([strBigSort isEqualToString:@"03"]){
                
                LLog(@"小蛮腰指纹锁");
                //锁包
                NSString *strXMYLockWholeData = [strHexWholeData substringWithRange:NSMakeRange(40,  16)];
                NSString *strBigXMYLockWholeData        = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strXMYLockWholeData];
                LLog([NSString stringWithFormat:@"小蛮腰指纹锁***=%@",strXMYLockWholeData]);
                
                //命令字
                NSString *strXMYLockCmdIDHigh = [strXMYLockWholeData substringWithRange:NSMakeRange(4,  2)];
                NSString *strXMYLockCmdIDHigh_Big = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strXMYLockCmdIDHigh];
                
                NSString *strXMYLockCmdIDLow = [strXMYLockWholeData substringWithRange:NSMakeRange(6,  2)];
                NSString *strXMYLockCmdIDLow_Big = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strXMYLockCmdIDLow];
                NSString*strXMYLockCmdID_Big = [NSString stringWithFormat:@"%@%@",strXMYLockCmdIDLow_Big,strXMYLockCmdIDHigh_Big];
                
                strBigIdentifer = strXMYLockCmdID_Big;
                if ([strXMYLockCmdID_Big isEqualToString:@"0002"]) {
                    //注册时候的ID
                    NSString *strXMYLockRegisterID  = [strXMYLockWholeData substringWithRange:NSMakeRange(8,  2)];
                    strFingerID = strXMYLockRegisterID;
                    //ID 类型
                    NSString *strXMYLockIDType  = [strXMYLockWholeData substringWithRange:NSMakeRange(10,  2)];
                    NSString *strXMYLockIDType_Big = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strXMYLockIDType];
                    strOpenXMYLockReport_0002_IDType = strXMYLockIDType_Big;
                    //锁的状态
                    NSString *strXMYLockState  = [strXMYLockWholeData substringWithRange:NSMakeRange(12,  2)];
                    NSString *strXMYLockState_Big = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strXMYLockState];
                    if ([strXMYLockState_Big isEqualToString:@"00"] ) {
                        iState = 0;
                    }else if ([strXMYLockIDType_Big isEqualToString:@"01"]){
                        iState = 1;
                    }else{
                        
                    }
                    LLog([NSString stringWithFormat:@"小蛮腰指纹锁*注册ID=%@,ID类型=%@,锁的状态=%@",strXMYLockRegisterID,strXMYLockIDType,strXMYLockState]);
                    
                    if ([strXMYLockIDType isEqualToString:@"01"]) {
                        LLog(@"小蛮腰-->管理指纹");
                    }else if ([strXMYLockIDType isEqualToString:@"02"]) {
                        LLog(@"小蛮腰-->普通指纹");
                    }else if ([strXMYLockIDType isEqualToString:@"03"]) {
                        LLog(@"小蛮腰-->客人指纹");
                    }else if ([strXMYLockIDType isEqualToString:@"04"]) {
                        LLog(@"小蛮腰-->挟持指纹：");
                    }else if ([strXMYLockIDType isEqualToString:@"05"]) {
                        LLog(@"小蛮腰-->遥控：");
                    }else if ([strXMYLockIDType isEqualToString:@"06"]) {
                        LLog(@"小蛮腰-->门铃：");
                    }else if ([strXMYLockIDType isEqualToString:@"07"]) {
                        LLog(@"小蛮腰-->普通密码：");
                    }else if ([strXMYLockIDType isEqualToString:@"08"]) {
                        LLog(@"小蛮腰-->劫持密码：");
                    }else if ([strXMYLockIDType isEqualToString:@"09"]) {
                        LLog(@"小蛮腰-->指纹加密码：");
                    }else if ([strXMYLockIDType isEqualToString:@"0a"]) {
                        LLog(@"小蛮腰-->网络开启：");
                    }else if ([strXMYLockIDType isEqualToString:@"0b"]) {
                        LLog(@"小蛮腰-->门卡：");
                    }else if ([strXMYLockIDType isEqualToString:@"0c"]) {
                        LLog(@"小蛮腰-->指纹加卡：");
                    }else {
                        LLog(@"小蛮腰-->其它ID类型");
                    }
                    if ([strXMYLockState isEqualToString:@"01"]) {
                        LLog([NSString stringWithFormat:@"小蛮腰-->锁的状态（%@）:开",strXMYLockState]);
                        iState = 1;
                    }else{
                        LLog([NSString stringWithFormat:@"小蛮腰-->锁的状态（%@）:关",strXMYLockState]);
                        iState = 0;
                    }
                }else if ([strXMYLockCmdID_Big isEqualToString:@"8002"]){
                    LLog(@"小蛮腰-->开锁报告回复");
                    NSString *strXMYLockContentID  = [strXMYLockWholeData substringWithRange:NSMakeRange(8,  2)];
                    if ([strXMYLockContentID isEqualToString:@"01"]) {
                        LLog([NSString stringWithFormat:@"小蛮腰-->开锁报告回复接收-成功%@",strXMYLockContentID]);
                    }else{
                        LLog([NSString stringWithFormat:@"小蛮腰-->开锁报告回复接收-失败%@",strXMYLockContentID]);
                    }
                }else if ([strXMYLockCmdID_Big isEqualToString:@"0006"]){
                    LLog(@"小蛮腰-->申请加入网络");
                    NSString *strXMYLockContentID  = [strXMYLockWholeData substringWithRange:NSMakeRange(8,  2)];
                    if ([strXMYLockContentID isEqualToString:@"01"]) {
                        LLog([NSString stringWithFormat:@"小蛮腰-->申请加入网络-成功%@",strXMYLockContentID]);
                    }else{
                        LLog([NSString stringWithFormat:@"小蛮腰-->申请加入网络-失败%@",strXMYLockContentID]);
                    }
                    
                }else if ([strXMYLockCmdID_Big isEqualToString:@"8006"]){
                    LLog(@"小蛮腰-->申请加入网络回复");
                    NSString *strXMYLockContentID  = [strXMYLockWholeData substringWithRange:NSMakeRange(8,  2)];
                    if ([strXMYLockContentID isEqualToString:@"01"]) {
                        LLog([NSString stringWithFormat:@"小蛮腰-->申请加入网络回复-加入成功%@",strXMYLockContentID]);
                    }else if ([strXMYLockContentID isEqualToString:@"00"]) {
                        LLog([NSString stringWithFormat:@"小蛮腰-->申请加入网络回复-加入失败%@",strXMYLockContentID]);
                    }else if ([strXMYLockContentID isEqualToString:@"02"]) {
                        LLog([NSString stringWithFormat:@"小蛮腰-->申请加入网络回复-离开失败%@",strXMYLockContentID]);
                    }else if ([strXMYLockContentID isEqualToString:@"03"]) {
                        LLog([NSString stringWithFormat:@"小蛮腰-->申请加入网络回复-离开成功%@",strXMYLockContentID]);
                    }else{
                        LLog([NSString stringWithFormat:@"小蛮腰-->申请加入网络回复-未知%@",strXMYLockContentID]);
                    }
                    
                }else if ([strXMYLockCmdID_Big isEqualToString:@"000A"]){
                    LLog(@"小蛮腰-->定时回复门状态信息");
                    NSString *strXMYLockContentID  = [strXMYLockWholeData substringWithRange:NSMakeRange(8,  2)];
                    if ([strXMYLockContentID isEqualToString:@"00"]) {
                        LLog([NSString stringWithFormat:@"小蛮腰-->定时回复门状态信息-电量饱和%@",strXMYLockContentID]);
                    }else if ([strXMYLockContentID isEqualToString:@"01"]) {
                        LLog([NSString stringWithFormat:@"小蛮腰-->定时回复门状态信息-电量充足%@",strXMYLockContentID]);
                    }else if ([strXMYLockContentID isEqualToString:@"02"]) {
                        LLog([NSString stringWithFormat:@"小蛮腰-->定时回复门状态信息-电量不足%@",strXMYLockContentID]);
                    }else if ([strXMYLockContentID isEqualToString:@"03"]) {
                        LLog([NSString stringWithFormat:@"小蛮腰-->定时回复门状态信息-电量用尽%@",strXMYLockContentID]);
                    }else{
                        LLog([NSString stringWithFormat:@"小蛮腰-->申请加入网络回复-未知%@",strXMYLockContentID]);
                    }
                    
                }else if ([strXMYLockCmdID_Big isEqualToString:@"800A"]){
                    LLog(@"小蛮腰-->定时回复门状态信息回复");
                    NSString *strXMYLockContentID  = [strXMYLockWholeData substringWithRange:NSMakeRange(8,  2)];
                    if ([strXMYLockContentID isEqualToString:@"00"]) {
                        LLog([NSString stringWithFormat:@"小蛮腰-->定时回复门状态信息回复-接受失败%@",strXMYLockContentID]);
                    }else if ([strXMYLockContentID isEqualToString:@"01"]) {
                        LLog([NSString stringWithFormat:@"小蛮腰-->定时回复门状态信息回复-接受成功%@",strXMYLockContentID]);
                    }else{
                        LLog([NSString stringWithFormat:@"小蛮腰-->定时回复门状态信息回复-未知%@",strXMYLockContentID]);
                    }
                    
                }else if ([strXMYLockCmdID_Big isEqualToString:@"0010"]){
                    LLog(@"小蛮腰-->未知 0010");
                    //                    NSLog(@"小蛮腰-->远程开关门");
                    //                    NSString *strXMYLockContentID  = [strXMYLockWholeData substringWithRange:NSMakeRange(8,  2)];
                    //                    if ([strXMYLockContentID isEqualToString:@"00"]) {
                    //                        NSLog(@"小蛮腰-->远程开关门-不开门%@",strXMYLockContentID);
                    //                    }else if ([strXMYLockContentID isEqualToString:@"01"]) {
                    //                        NSLog(@"小蛮腰-->远程开关门-开门%@",strXMYLockContentID);
                    //                    }else{
                    //                        NSLog(@"小蛮腰-->远程开关门-未知%@",strXMYLockContentID);
                    //                    }
                    
                }else if ([strXMYLockCmdID_Big isEqualToString:@"8010"]){
                    LLog(@"小蛮腰-->远程开关门回复（待完善）");
//                    NSString *strXMYLockContentID  = [strXMYLockWholeData substringWithRange:NSMakeRange(8,  2)];
                }
                
            }
        }
    } else if ([strBigHexOD isEqualToString:@"0F E6"]){
        if ([strBigHexDeviceStyle isEqualToString:@"02"]) {
            //红外转发器
            if ([strBigSort isEqualToString:@"02"]) {
                NSString *strInfraredStudyErrorOrAirConditionSendOKCode = [strHexWholeData substringWithRange:NSMakeRange(strHexWholeData.length - 6, 2)];
                NSString *strBigInfraredStudyErrorOrAirConditionSendOKCode = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strInfraredStudyErrorOrAirConditionSendOKCode];
                
                NSString *strLength = [strHexWholeData substringWithRange:NSMakeRange(2, 2)];
                int iLength = (int)[[ToolHexManager sharedManager] numberWithHexString:strLength];
                
                if ([strBigInfraredStudyErrorOrAirConditionSendOKCode isEqualToString:@"E0"]) {
                    NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexWholeData];
                    LLog([NSString stringWithFormat:@"\n学习不成功返回：\n%@",strBigTemp]);
                    self.strStudyCode = [NSString stringWithFormat:@"%ld",(long)SHStudyCodeFail];
                } else if ([strBigInfraredStudyErrorOrAirConditionSendOKCode isEqualToString:@"89"]){
                    //空调发送成功返回
                    self.strStudyCode = strBigInfraredStudyErrorOrAirConditionSendOKCode;
                    
                } else if(iLength >=64){
                    NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexWholeData];
                    LLog([NSString stringWithFormat:@"\n学习成功返回：\n%@",strBigTemp]);
                    self.strStudyCode = [NSString stringWithFormat:@"%ld",(long)SHStudyCodeSucc];
                }
                strBigIdentifer = self.strStudyCode;
            } else if ([strBigSort isEqualToString:@"03"]){
                //音乐背景器
                
            } else if ([strBigSort isEqualToString:@"10"] || [strBigSort isEqualToString:@"11"]){
                //电动窗帘、平移开窗器
                NSString *strIdentifer = [strHexWholeData substringWithRange:NSMakeRange(strHexWholeData.length - 6,2)];
                strBigIdentifer = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strIdentifer];
                if ([strBigIdentifer isEqualToString:@"50"]){
                    LLog(@"正转");
                }else if ([strBigIdentifer isEqualToString:@"4C"]){
                    LLog(@"反转");
                }else if ([strBigIdentifer isEqualToString:@"54"]){
                    LLog(@"停止");
                }else if ([strBigIdentifer isEqualToString:@"53"]){
                    LLog(@"重启");
                }else if ([strBigIdentifer isEqualToString:@"4F"]){
                    LLog(@"状态读取");
                    if ([strBigSort isEqualToString:@"11"]) {
                        NSString *strLocation = [strHexWholeData substringWithRange:NSMakeRange(strHexWholeData.length - 8,  2)];
                        strBigLocation = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strLocation];
                        LLog([NSString stringWithFormat:@"平移开窗器开到位置是%@",strLocation]);
                    }
                    
                }else if([strBigIdentifer isEqualToString:@"25"]){
                    if ([strBigSort isEqualToString:@"10"]) {
                        NSString *strLocation = [strHexWholeData substringWithRange:NSMakeRange(strHexWholeData.length - 8,  2)];
                        strBigLocation = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strLocation];
                        LLog([NSString stringWithFormat:@"电动窗帘开到位置是%@",strLocation]);
                    }
                    
                }
            } else if ([strBigSort isEqualToString:@"12"]){
                //电动床
            } else if ([strBigSort isEqualToString:@"13"]){
                //新风系统
                NSString *strXFCode = [strHexWholeData substringWithRange:NSMakeRange(40, strHexWholeData.length - 40 - 4)];
                NSString *strBigXFCode = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strXFCode];
                LLog([NSString stringWithFormat:@"XF数据帧上报:%@",strBigXFCode]);
                NSDictionary *dictXF = [self doHandleXFReportWithXFWholeData:strXFCode];
                self.dictXF = dictXF;
                
            } else if ([strBigSort isEqualToString:@"14"]){
                //净水器
                NSString *strLength = [strHexWholeData substringWithRange:NSMakeRange(2, 2)];
                int iLength = (int)[[ToolHexManager sharedManager] numberWithHexString:strLength];
                if (iLength == 64) {
                    NSString *strDataFirst        = [strHexWholeData substringWithRange:NSMakeRange(36, 2)];
                    if ([strDataFirst intValue] == 1) {
                        self.strWaterFirstFrame = strHexWholeData;
                        return;
                    }else{
                        return;
                    }
                }else if (iLength == 23){
                    NSString *strDataFirst = [strHexWholeData substringWithRange:NSMakeRange(36, 2)];
                    if ([strDataFirst intValue] == 2) {
                        if (self.strWaterFirstFrame) {
                            self.strWaterSecondFrame = strHexWholeData;
                            NSDictionary *dcit = [self handleWaterFilterDataFrameWithFirstFrame:self.strWaterFirstFrame secondeDataFram:self.strWaterSecondFrame];
                            strBigIdentifer = [ToolCommon dictionaryToJson:dcit];
                        }else{
                            return;
                        }
                    }
                }else{
                    
                    return;
                }
            } else if ([strBigSort isEqualToString:@"20"]){
                //浴霸
            }else{
                LLog(@"未知透传设备控制07上传");
            }
        }
        
    }else{}
    
    NSString *strDeviceName = [self handleGetNameWithOD:strBigHexOD strType:strBigHexDeviceStyle strCategory:strBigSort];
    
    NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexWholeData];
    LLog([NSString stringWithFormat:@"\n上报%@:\n%@",strDeviceName,strBigTemp]);
    
    
    SHModelDevice *deviceReport                 = [SHModelDevice new];
    deviceReport.iDevice_room_id                = 0;
    deviceReport.strDevice_device_name          = strDeviceName;
    deviceReport.strDevice_image                = @"";
    deviceReport.strDevice_device_OD            = strBigHexOD;
    deviceReport.strDevice_device_type          = strBigHexDeviceStyle;
    deviceReport.strDevice_category             = strBigSort;
    deviceReport.strDevice_cmdId                = strBigHexCmID;
    deviceReport.strDevice_sindex               = @"";
    deviceReport.strDevice_sindex_length        = strBigIdentifer;
    deviceReport.strDevice_mac_address          = strZigbeeBigMacAddr;
    deviceReport.iDevice_device_state1          = iState;
    deviceReport.iDevice_device_state2          = 0;
    deviceReport.iDevice_device_state3          = 0;
    deviceReport.strDevice_other_status         = strBigLocation;
    deviceReport.strFingerID                    = strFingerID;
    deviceReport.strOpenXMYLockReport_0002_IDType  = strOpenXMYLockReport_0002_IDType;
    self.modelDevice = deviceReport;
}

#pragma mark - 处理云端数据的
/**
 *  解析云端数据数据
 *  @param dataReceive sever return data
 */
- (void)doUnPackReceivedCludData:(NSData *)dataReceiveCloud
{
    unsigned char * dataByte = (unsigned char *)[dataReceiveCloud bytes];
//    unsigned char packageLength = *(dataByte + 1);
    SHCloudSeverConState cloudConState;
    unsigned char packageDataFields = *(dataByte + 2);
    if(packageDataFields == 0x41) {
        cloudConState = SHCloudSeverConStateSucc;
        LLog(@"\n\n成功连接cloudsever，获取到手机登记信息！\n");
    }else {
        self.socketConState = GSocketConStateFail;
        unsigned char packageErrorCode = *(dataByte + 3);
        if (packageErrorCode == 0x01) {
            cloudConState = SHCloudSeverConState_NORecord;
            LLog(@"\n\n 无此网关记录\n");
        }else if(packageErrorCode == 0x02) {
            cloudConState = SHCloudSeverConState_DisCon;
            LLog(@"\n\n cloudsever断开连接\n");
        }else{
            LLog(@"连接cloudsever其它错误");
        }
    }
    NSMutableDictionary*dict =[NSMutableDictionary dictionary];
    [dict setObject:[NSNumber numberWithInt:cloudConState] forKey:@"kRegisterCloudsever"];
    [[NSNotificationCenter defaultCenter] postNotificationName:kRegisterCloudsever
                                                        object:nil
                                                      userInfo:dict];
}

/*
 - (void)doUnPackReceivedCludData:(NSData *)dataReceiveCloud
 {
 unsigned char * dataByte = (unsigned char *)[dataReceiveCloud bytes];
 //    unsigned char packageLength = *(dataByte + 1);
 SHCloudSeverConState cloudConState;
 unsigned char packageDataFields = *(dataByte + 2);
 if(packageDataFields == 0x41) {
 cloudConState = SHCloudSeverConStateSucc;
 LLog(@"\n\n成功连接cloudsever，获取到手机登记信息！\n");
 }else if(packageDataFields == 0x7f) {
 self.socketConState = GSocketConStateFail;
 unsigned char packageErrorCode = *(dataByte + 3);
 if (packageErrorCode == 0x01) {
 cloudConState = SHCloudSeverConState_NORecord;
 LLog(@"\n\n 无此网关记录\n");
 }else if(packageErrorCode == 0x02) {
 cloudConState = SHCloudSeverConState_DisCon;
 LLog(@"\n\n cloudsever断开连接\n");
 }else{
 LLog(@"连接cloudsever其它错误");
 }
 }else{
 LLog(@"连接cloudsever其它错误");
 }
 NSMutableDictionary*dict =[NSMutableDictionary dictionary];
 [dict setObject:[NSNumber numberWithInt:cloudConState] forKey:@"kRegisterCloudsever"];
 [[NSNotificationCenter defaultCenter] postNotificationName:kRegisterCloudsever
 object:nil
 userInfo:dict];
 }
 */


#pragma mark -
#pragma mark - 以下皆为最新协议

#pragma mark - 透传协议4070
- (void)doHandleNew4070ProtocolTransmitDataReported:(NSString *)strHexWholeData
{
    NSString *strDeviceHexMacAddr  = [strHexWholeData substringWithRange:NSMakeRange(6, 16)];
    NSString *strHexOD             = [strHexWholeData substringWithRange:NSMakeRange(22, 4)];
    NSString *strDeviceStyle       = [strHexWholeData substringWithRange:NSMakeRange(38, 2)];
    NSString *strDeviceSort        = [strHexWholeData substringWithRange:NSMakeRange(40, 2)];
    
    NSString *strZigbeeBigMacAddr  = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strDeviceHexMacAddr];
    NSString *strBigHexOD          = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexOD];
    NSString *strBigHexDeviceStyle = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strDeviceStyle];
    NSString *strBigSort           = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strDeviceSort];
    
    NSString *strDeviceName = [self handleGetNameWithOD:strBigHexOD strType:strBigHexDeviceStyle strCategory:strBigSort];
    NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexWholeData];
    LLog([NSString stringWithFormat:@"\n%@:\n%@",strDeviceName,strBigTemp]);
    
    SHModelDevice *deviceReport = [SHModelDevice new];
    deviceReport.iDevice_room_id                = 0;
    deviceReport.strDevice_device_name          = strDeviceName;
    deviceReport.strDevice_image                = @"";
    deviceReport.strDevice_device_OD            = strBigHexOD;
    
    deviceReport.strDevice_device_type          = strBigHexDeviceStyle;
    deviceReport.strDevice_category             = strBigSort;
    deviceReport.strDevice_cmdId                = @"01";
    deviceReport.strDevice_sindex               = @"";
    
    deviceReport.strDevice_sindex_length        = @"";
    deviceReport.strDevice_mac_address          = strZigbeeBigMacAddr;
    deviceReport.iDevice_device_state1          = 0;
    deviceReport.iDevice_device_state2          = 0;
    
    deviceReport.iDevice_device_state3          = 0;
    
    //#warning  需要做下处理
    deviceReport.strDevice_other_status         = @"00";
    
    self.modelDevice = deviceReport;
}

#pragma mark -智能计量类设备 OD=4040(0XFC8)
- (void)doHandle4040MeasureSoketWithWholeData:(NSString *)strHexWholeData
{
    NSString *strDeviceHexMacAddr           = [strHexWholeData substringWithRange:NSMakeRange(6, 16)];
    NSString *strHexOD                      = [strHexWholeData substringWithRange:NSMakeRange(22, 4)];
    NSString *strSonIndexLength             = [strHexWholeData substringWithRange:NSMakeRange(28, 2)];
    NSString *strSonIndexSelectedDetail     = [strHexWholeData substringWithRange:NSMakeRange(30, 8)];
    NSString *strDeviceStyle                = [strHexWholeData substringWithRange:NSMakeRange(38, 2)];
    NSString *strDeviceSort                 = [strHexWholeData substringWithRange:NSMakeRange(40, 2)];
    
    NSString *strDCV1                        = [strHexWholeData substringWithRange:NSMakeRange(62, 2)];
    NSString *strDCV2                        = [strHexWholeData substringWithRange:NSMakeRange(64, 2)];
    NSString *strDVC = [NSString stringWithFormat:@"%@%@",strDCV2,strDCV1];
    double iDCVReal = [strDVC doubleValue]/10;
    NSString *strDCVReal                    = [NSString stringWithFormat:@"电压:%.2fV",iDCVReal];
    
    NSString *strCurrent1                   = [strHexWholeData substringWithRange:NSMakeRange(66, 2)];
    NSString *strCurrent2                   = [strHexWholeData substringWithRange:NSMakeRange(68, 2)];
    NSString *strCurrent3                   = [strHexWholeData substringWithRange:NSMakeRange(70, 2)];
    NSString *strCurrent = [NSString stringWithFormat:@"%@%@%@",strCurrent3,strCurrent2,strCurrent1];
    double dCurrentReal = [strCurrent doubleValue]/1000;
    NSString *strCurrentReal                    = [NSString stringWithFormat:@"电流:%.3fA",dCurrentReal];
    
    NSString *strUsefulPower1               = [strHexWholeData substringWithRange:NSMakeRange(72, 2)];
    NSString *strUsefulPower2               = [strHexWholeData substringWithRange:NSMakeRange(74, 2)];
    NSString *strUsefulPower3               = [strHexWholeData substringWithRange:NSMakeRange(76, 2)];
    NSString *strUsefulPower = [NSString stringWithFormat:@"%@%@%@",strUsefulPower3,strUsefulPower2,strUsefulPower1];
    double dUsefulPowerReal = [strUsefulPower doubleValue]/10;
    NSString *strUsefulPowerReal            = [NSString stringWithFormat:@"功率：%.2fW",dUsefulPowerReal];
    
    NSString *strElectricQuantity1          = [strHexWholeData substringWithRange:NSMakeRange(78, 2)];
    NSString *strElectricQuantity2          = [strHexWholeData substringWithRange:NSMakeRange(80, 2)];
    NSString *strElectricQuantity3          = [strHexWholeData substringWithRange:NSMakeRange(82, 2)];
    NSString *strElectricQuantity4          = [strHexWholeData substringWithRange:NSMakeRange(84, 2)];
    NSString *strElectricQuantity = [NSString stringWithFormat:@"%@%@%@%@",strElectricQuantity4,strElectricQuantity3,strElectricQuantity2,strElectricQuantity1];
    float dElectricQuantityReal = [strElectricQuantity doubleValue]/100;
    NSString *strElectricQuantityReal            = [NSString stringWithFormat:@"电量:%.2f度",dElectricQuantityReal];
    NSLog(@"\n%@\n%@\n%@\n%@\n",strDCVReal,strCurrentReal,strUsefulPowerReal,strElectricQuantityReal);
    
    NSString *strStatueFirst = [strHexWholeData substringWithRange:NSMakeRange(strHexWholeData.length - 6, 2)];
    
    
    NSString *strZigbeeBigMacAddr           = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strDeviceHexMacAddr];
    NSString *strBigHexOD                   = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexOD];
    NSString *strBigHexDeviceStyle          = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strDeviceStyle];
    NSString *strBigSort                    = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strDeviceSort];
    NSString *strDeviceBigSonIndexLength    = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strSonIndexLength];
    NSString *strDeviceBigSonIndex          = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strSonIndexSelectedDetail];
    
    NSString *strDeviceName = [self handleGetNameWithOD:strBigHexOD strType:strBigHexDeviceStyle strCategory:strBigSort];
    NSString *strBigTemp = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexWholeData];
    LLog([NSString stringWithFormat:@"\n%@:\n%@",strDeviceName,strBigTemp]);
    
    
    SHModelDevice *deviceReport = [SHModelDevice new];
    deviceReport.iDevice_room_id            = 0;
    deviceReport.strDevice_device_name      = strDeviceName;
    deviceReport.strDevice_image            = @"";
    
    deviceReport.strDevice_device_OD        = strBigHexOD;
    deviceReport.strDevice_device_type      = strBigHexDeviceStyle;
    deviceReport.strDevice_category         = strBigSort;
    deviceReport.strDevice_cmdId            = kCmdID_StrHexRead_OD;
    
    deviceReport.strDevice_sindex           = strDeviceBigSonIndex;
    deviceReport.strDevice_sindex_length    = strDeviceBigSonIndexLength;
    deviceReport.strDevice_mac_address      = strZigbeeBigMacAddr;
    deviceReport.iDevice_device_state1      = [strStatueFirst intValue];
    deviceReport.strDevice_other_status     = @"";
    deviceReport.strDCVReal                 = strDCVReal;
    deviceReport.strCurrentReal             = strCurrentReal;
    deviceReport.strUsefulPowerReal         = strUsefulPowerReal;
    deviceReport.strElectricQuantityReal    = strElectricQuantityReal;
    self.modelDevice = deviceReport;
}


#pragma mark - 休眠设备4030
- (void)doHandleNew4030DormancyControlSonIndexWholeData:(NSString *)strHexWholeData
{
    NSString *strDeviceHexMacAddr           = [strHexWholeData substringWithRange:NSMakeRange(6, 16)];
    NSString *strHexOD                      = [strHexWholeData substringWithRange:NSMakeRange(22, 4)];
    NSString *strSonIndexLength             = [strHexWholeData substringWithRange:NSMakeRange(28, 2)];
    NSString *strSonIndexSelectedDetail     = [strHexWholeData substringWithRange:NSMakeRange(30, 8)];
    NSString *strDeviceStyle                = [strHexWholeData substringWithRange:NSMakeRange(38, 2)];
    NSString *strDeviceSort                 = [strHexWholeData substringWithRange:NSMakeRange(40, 2)];
    NSString *strDeviceStatue               = [strHexWholeData substringWithRange:NSMakeRange(62, 2)];
    NSString *strDeviceDismantledStatue     = [strHexWholeData substringWithRange:NSMakeRange(64, 2)];
    
    NSString *strZigbeeBigMacAddr           = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strDeviceHexMacAddr];
    NSString *strBigHexOD                   = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexOD];
    NSString *strBigHexDeviceStyle          = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strDeviceStyle];
    NSString *strBigSort                    = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strDeviceSort];
    NSString *strDeviceBigSonIndexLength    = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strSonIndexLength];
    NSString *strDeviceBigSonIndex          = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strSonIndexSelectedDetail];
    int iDeviceStatue                       = (int)[[ToolHexManager sharedManager] numberWithHexString:strDeviceStatue];
    int iDeviceDismantledStatue             = (int)[[ToolHexManager sharedManager] numberWithHexString:strDeviceDismantledStatue];
    
    NSString *strName = [self handleGetNameWithOD:strBigHexOD strType:strBigHexDeviceStyle strCategory:strBigSort];
    LLog([NSString stringWithFormat:@"设备是：%@",strName]);
    
    SHModelDevice *deviceReport = [SHModelDevice new];
    deviceReport.iDevice_room_id                = 0;
    deviceReport.strDevice_device_name          = strName;
    deviceReport.strDevice_image                = @"";
    
    deviceReport.strDevice_device_OD            = strBigHexOD;
    deviceReport.strDevice_device_type          = strBigHexDeviceStyle;
    deviceReport.strDevice_category             = strBigSort;
    deviceReport.strDevice_cmdId                = kCmdID_StrHexRead_OD;
    
    deviceReport.strDevice_sindex               = strDeviceBigSonIndex;
    deviceReport.strDevice_sindex_length        = strDeviceBigSonIndexLength;
    deviceReport.strDevice_mac_address          = strZigbeeBigMacAddr;
    deviceReport.iDevice_device_state1          = iDeviceStatue;                //第一路即门磁输入信号为“开状态”， 0-Null；1-开；2-关；
    
    deviceReport.iDevice_device_state2          = iDeviceDismantledStatue;      //第二路即防拆开关输入信号为“关状态”， 0-Null；1-开；2-关；
    deviceReport.iDevice_device_state3          = 0;
    deviceReport.strDevice_other_status         = @"";
    self.modelDevice = deviceReport;
}

#pragma mark - 多用途控制协议4010
- (void)doHandleNewSwitchActionControlSonIndexWholeData:(NSString *)strHexWholeData
{
    NSString *strDeviceHexMacAddr           = [strHexWholeData substringWithRange:NSMakeRange(6, 16)];
    NSString *strHexOD                      = [strHexWholeData substringWithRange:NSMakeRange(22, 4)];
    NSString *strSonIndexLength             = [strHexWholeData substringWithRange:NSMakeRange(28, 2)];
    NSString *strSonIndexSelectedDetail     = [strHexWholeData substringWithRange:NSMakeRange(30, 8)];
    NSString *strDeviceStyle                = [strHexWholeData substringWithRange:NSMakeRange(38, 2)];
    NSString *strDeviceSort                 = [strHexWholeData substringWithRange:NSMakeRange(40, 2)];
    
    NSString *strZigbeeBigMacAddr           = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strDeviceHexMacAddr];
    NSString *strBigHexOD                   = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexOD];
    NSString *strBigHexDeviceStyle          = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strDeviceStyle];
    NSString *strBigSort                    = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strDeviceSort];
    NSString *strDeviceBigSonIndexLength    = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strSonIndexLength];
    NSString *strDeviceBigSonIndex          = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strSonIndexSelectedDetail];
    
    NSString *strName = [self handleGetNameWithOD:strBigHexOD strType:strBigHexDeviceStyle strCategory:strBigSort];
    
    NSString *strStatueFirst =@"0";
    NSString *strStatueSecond = @"0";
    NSString *strStatueThird = @"0";
    if ([strBigHexDeviceStyle isEqualToString:@"07"]) {
        if ([strBigSort isEqualToString:@"02"] || [strBigSort isEqualToString:@"04"] ) {
            //三路开关
            strStatueFirst = [strHexWholeData substringWithRange:NSMakeRange(62, 2)];
            strStatueSecond = [strHexWholeData substringWithRange:NSMakeRange(64, 2)];
            strStatueThird = [strHexWholeData substringWithRange:NSMakeRange(66, 2)];
            LLog([NSString stringWithFormat:@"第一路状态：%@第二路状态：%@第三路状态：%@",strStatueFirst,strStatueSecond,strStatueThird]);
        }else{
            strStatueFirst = [strHexWholeData substringWithRange:NSMakeRange(62, 2)];
            strStatueSecond = [strHexWholeData substringWithRange:NSMakeRange(64, 2)];
            strStatueThird = [strHexWholeData substringWithRange:NSMakeRange(66, 2)];
            LLog(@"三路未知设备");
        }
        
    }else if ([strBigHexDeviceStyle isEqualToString:@"06"]){
        //二路开关
        if ([strBigSort isEqualToString:@"02"] || [strBigSort isEqualToString:@"04"]) {
            strStatueFirst = [strHexWholeData substringWithRange:NSMakeRange(62, 2)];
            strStatueSecond = [strHexWholeData substringWithRange:NSMakeRange(64, 2)];
            LLog([NSString stringWithFormat:@"第一路状态：%@第二路状态：%@",strStatueFirst,strStatueSecond]);
        }else{
            strStatueFirst = [strHexWholeData substringWithRange:NSMakeRange(62, 2)];
            strStatueSecond = [strHexWholeData substringWithRange:NSMakeRange(64, 2)];
            LLog(@"二路未知设备");
        }
    }else if ([strBigHexDeviceStyle isEqualToString:@"05"]){
        //一路开关
        if ([strBigSort isEqualToString:@"02"]  || [strBigSort isEqualToString:@"04"]) {
            strStatueFirst = [strHexWholeData substringWithRange:NSMakeRange(62, 2)];
            LLog([NSString stringWithFormat:@"一路开关状态：%@",strStatueFirst]);
        }else{
            strStatueFirst = [strHexWholeData substringWithRange:NSMakeRange(62, 2)];
            LLog(@"一路未知设备");
        }
    }else if ([strBigHexDeviceStyle isEqualToString:@"0B"]){
        //一路开关
        if ([strBigSort isEqualToString:@"02"]) {
            
            NSString *strLength = [strHexWholeData substringWithRange:NSMakeRange(2, 2)];
            int iLength = (int)[[ToolHexManager sharedManager] numberWithHexString:strLength];
            if (iLength > 31) {
                //读取上报
                strSonIndexLength = [strHexWholeData substringWithRange:NSMakeRange(strHexWholeData.length - 10, 2)];
                strDeviceBigSonIndexLength    = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strSonIndexLength];
                
            }else{
                //自动上报
            }
            
            strStatueFirst = [strHexWholeData substringWithRange:NSMakeRange(62, 2)];
            LLog([NSString stringWithFormat:@"多彩灯开关状态：%@",strStatueFirst]);
        }else{
            strStatueFirst = [strHexWholeData substringWithRange:NSMakeRange(62, 2)];
            LLog(@"一路未知设备");
        }
    }else if ([strBigHexDeviceStyle isEqualToString:@"0E"]){
        
        LLog([NSString stringWithFormat:@"%@",strName]);
    }else if ([strBigHexDeviceStyle isEqualToString:@"81"]){
        LLog([NSString stringWithFormat:@"%@",strName]);
        if ([strBigSort isEqualToString:@"02"]) {
            strStatueFirst = [strHexWholeData substringWithRange:NSMakeRange(strHexWholeData.length - 6, 2)];
            LLog([NSString stringWithFormat:@"（%@）：%@",strName,strStatueFirst]);
            
        }else if ([strBigSort isEqualToString:@"03"]) {
            strStatueFirst = [strHexWholeData substringWithRange:NSMakeRange(strHexWholeData.length - 6, 2)];
            LLog([NSString stringWithFormat:@"（%@）：%@",strName,strStatueFirst]);
        }else if ([strBigSort isEqualToString:@"04"]) {
            strStatueFirst = [strHexWholeData substringWithRange:NSMakeRange(strHexWholeData.length - 6, 2)];
            LLog([NSString stringWithFormat:@"（%@）：%@",strName,strStatueFirst]);
        }else{
            strStatueFirst = [strHexWholeData substringWithRange:NSMakeRange(strHexWholeData.length - 6, 2)];
            LLog([NSString stringWithFormat:@"（%@）：%@",strName,strStatueFirst]);
        }
        
    }else if ([strBigHexDeviceStyle isEqualToString:@"0B"]){
        
        
    }else if ([strBigHexDeviceStyle isEqualToString:@"0B"]){
        
        
    }else{
        strStatueFirst = [strHexWholeData substringWithRange:NSMakeRange(62, 2)];
        LLog([NSString stringWithFormat:@"其它类似一路开关状态（%@）：%@",strName,strStatueFirst]);
    }
    SHModelDevice *deviceReport = [SHModelDevice new];
    deviceReport.iDevice_room_id            = 0;
    deviceReport.strDevice_device_name      = strName;
    deviceReport.strDevice_image            = @"";
    
    deviceReport.strDevice_device_OD        = strBigHexOD;
    deviceReport.strDevice_device_type      = strBigHexDeviceStyle;
    deviceReport.strDevice_category         = strBigSort;
    deviceReport.strDevice_cmdId            = kCmdID_StrHexRead_OD;
    
    deviceReport.strDevice_sindex           = strDeviceBigSonIndex;
    deviceReport.strDevice_sindex_length    = strDeviceBigSonIndexLength;
    deviceReport.strDevice_mac_address      = strZigbeeBigMacAddr;
    deviceReport.iDevice_device_state1      = [strStatueFirst intValue];
    
    deviceReport.iDevice_device_state2      = [strStatueSecond intValue];
    deviceReport.iDevice_device_state3      = [strStatueThird intValue];
    deviceReport.strDevice_other_status     = @"";
    self.modelDevice = deviceReport;
    
}

#pragma mark - 获取设备的名字
- (NSString *)handleGetNameWithOD:(NSString *)strOD strType:(NSString *)strType strCategory:(NSString *)strCategory
{
    NSString *strName;
    if ([strOD isEqualToString:@"0F AA"]) {
        if ([strType isEqualToString:@"C1"]) {
            if ([strCategory isEqualToString:@"02"]) {
                strName = @"六路面板";
            }
        }else if ([strType isEqualToString:@"8A"]) {
            if ([strCategory isEqualToString:@"02"]) {
                strName = @"场景控制器";
            }
            
        }else if ([strType isEqualToString:@"81"]) {
            if ([strCategory isEqualToString:@"02"]) {
                strName = @"人体热释传感器";
            }else if ([strCategory isEqualToString:@"03"]){
                strName = @"一氧化碳检测";
            }else if ([strCategory isEqualToString:@"04"]){
                strName = @"烟雾传感器";
            }
            
        }else if ([strType isEqualToString:@"0E"]){
            
            if ([strCategory isEqualToString:@"02"]) {
                strName = @"多彩冷暖灯";
            }
            
        }else if ([strType isEqualToString:@"0B"]){
            if ([strCategory isEqualToString:@"02"]) {
                strName = @"多彩球泡灯";
            }
            
        }else if ([strType isEqualToString:@"09"]){
            if ([strCategory isEqualToString:@"02"]) {
                strName = @"声光报警器";
            }
            
        }else if ([strType isEqualToString:@"07"]){
            if ([strCategory isEqualToString:@"02"]) {
                strName = @"三路灯开关";
            }else if ([strCategory isEqualToString:@"04"]) {
                strName = @"多联三路灯开关";
            }
            
        }else if ([strType isEqualToString:@"06"]){
            if ([strCategory isEqualToString:@"02"]) {
                strName = @"二路灯开关";
            }else if ([strCategory isEqualToString:@"04"]) {
                strName = @"多联二路灯开关";
            }
            
        }else if ([strType isEqualToString:@"05"]){
            if ([strCategory isEqualToString:@"02"] ) {
                strName = @"一路灯开关";
            }else if ([strCategory isEqualToString:@"04"]) {
                strName = @"多联一路灯开关";
            }else if ([strCategory isEqualToString:@"03"]) {
                strName = @"电动玻璃";
            }else if ([strCategory isEqualToString:@"10"]) {
                strName = @"86插座";
            }else if ([strCategory isEqualToString:@"11"]) {
                strName = @"移动插座";
            }
            
        }else if ([strType isEqualToString:@"02"]){
            if ([strCategory isEqualToString:@"02"]) {
                strName = @"电动幕布";
            }else if ([strCategory isEqualToString:@"10"]) {
                strName = @"投影架";
            }else if ([strCategory isEqualToString:@"11"]) {
                strName = @"推拉开窗器";
            }else if ([strCategory isEqualToString:@"12"]) {
                strName = @"平推开窗器";
            }else if ([strCategory isEqualToString:@"13"]) {
                strName = @"机械手控制器";
            }
            
        }else if ([strType isEqualToString:@"01"]){
            if ([strCategory isEqualToString:@"02"]) {
                strName = @"普通电动窗帘";
            }
        }
        
    }else if([strOD isEqualToString:@"0F BE"]){
        
        if ([strType isEqualToString:@"01"]){
            
            if ([strCategory isEqualToString:@"02"]){
                strName = @"门磁";
            }
        }else if ([strType isEqualToString:@"02"]){
            
            if ([strCategory isEqualToString:@"02"]){
                strName = @"指纹锁";
            }else if ([strCategory isEqualToString:@"03"]){
                strName = @"小蛮腰指纹锁";
            }
        }else if ([strType isEqualToString:@"03"]){
            
            if ([strCategory isEqualToString:@"02"]){
                strName = @"燃气传感器BE";
            }
        }else if ([strType isEqualToString:@"04"]){
            
            if ([strCategory isEqualToString:@"02"]){
                strName = @"人体红外传感器BE";
            }
        }else if ([strType isEqualToString:@"05"]){
            
            if ([strCategory isEqualToString:@"02"]){
                strName = @"水浸传感器BE";
            }
        }else if ([strType isEqualToString:@"07"]){
            
            if ([strCategory isEqualToString:@"02"]){
                strName = @"烟雾传感器";
            }
        }else if ([strType isEqualToString:@"81"]){
            
            if ([strCategory isEqualToString:@"02"]){
                strName = @"门磁";
            }else if ([strCategory isEqualToString:@"03"]){
                strName = @"窗磁";
            }
        }else if ([strType isEqualToString:@"83"]){
            
            if ([strCategory isEqualToString:@"02"]){
                strName = @"水浸传感器";
            }
        }else if ([strType isEqualToString:@"86"]){
            
            if ([strCategory isEqualToString:@"02"]){
                strName = @"人体红外传感器";
            }
        }
        
    }else if([strOD isEqualToString:@"0F E6"]){
        
        if ([strType isEqualToString:@"02"]) {
            
            if ([strCategory isEqualToString:@"02"]) {
                
                strName = @"红外学习仪";
            } else if ([strCategory isEqualToString:@"03"]){
                
                strName = @"音乐背景器";
            } else if ([strCategory isEqualToString:@"04"]){
                
                strName = @"RS232转发器";
            } else if ([strCategory isEqualToString:@"05"]){
                
                strName = @"风机盘管";
            } else if ([strCategory isEqualToString:@"10"]){
                
                strName = @"电动窗帘";
            } else if ([strCategory isEqualToString:@"11"]){
                
                strName = @"平移开窗器";
            } else if ([strCategory isEqualToString:@"12"]){
                
                strName = @"电动床";
            } else if ([strCategory isEqualToString:@"13"]){
                
                strName = @"新风系统";
            } else if ([strCategory isEqualToString:@"14"]){
                
                strName = @"净水器";
            }  else if ([strCategory isEqualToString:@"15"]){
                
                strName = @"中央空调";
            } else if ([strCategory isEqualToString:@"20"]){
                
                strName = @"浴霸";
            }
        }
    }else if([strOD isEqualToString:@"0F C8"]){
        
        if ([strType isEqualToString:@"01"]) {
            
            if ([strCategory isEqualToString:@"02"]) {
                
                strName = @"单相电表";
            }
        }else if ([strType isEqualToString:@"02"]) {
            
            if ([strCategory isEqualToString:@"02"]) {
                strName = @"计量控制盒";
            }
        }else if ([strType isEqualToString:@"03"]) {
            
            if ([strCategory isEqualToString:@"02"]) {
                strName = @"三相电表";
            }
        }else if ([strType isEqualToString:@"04"]) {
            
            if ([strCategory isEqualToString:@"02"]) {
                strName = @"计量插座（10A）";
            }else if ([strCategory isEqualToString:@"03"]) {
                strName = @"计量插座（16A）";
            }
        }
        
    }
    return strName;
}




/*
 #pragma mark -
 #pragma mark -  private lock
 - (NSString *)doHanleGetFingerIDWithMacAddr:(NSString *)strBigMacAddr fingerId:(NSString *)strFingerID
 {
 int iReportFingerID = (int)[[ToolHexManager sharedManager] numberWithHexString:strFingerID];
 NSString *strJson = [[SHDataBaseManager sharedInstance] doQueryLockMemberListDataWithDeviceMacAddr:strBigMacAddr];
 if (strJson) {
 NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
 NSArray *arrResult = jsonDict[@"result"];
 NSArray *arrLockMemberList = [self handleGetModel:arrResult];
 
 for (int i = 0; i < arrLockMemberList.count; i ++) {
 SHLockMemModel *lockMem = arrLockMemberList[i];
 if ([lockMem.strLock_fingerprintId intValue] == iReportFingerID) {
 return lockMem.strLock_user_name;
 }
 }
 }
 return @"未知";
 }
 
 - (NSMutableArray *)handleGetModel:(NSArray *)result
 {
 NSMutableArray *mutArr = [NSMutableArray new];
 
 for (int i = 0; i < result.count; i ++) {
 SHLockMemModel *lockMem = [SHLockMemModel new];
 NSDictionary *dict = result[i];
 lockMem.iDeviceID = [dict[@"device_id"] intValue];
 lockMem.iLockID = [dict[@"lock_id"] intValue];
 lockMem.strLock_fingerprintId = dict[@"fingerprintId"];
 lockMem.strLock_user_name = dict[@"user_name"];
 lockMem.iUnlock_times = [dict[@"unlock_times"] intValue];
 [mutArr addObject:lockMem];
 }
 return mutArr;
 }
 */

- (NSDictionary *)handleWaterFilterDataFrameWithFirstFrame:(NSString *)strFirstData secondeDataFram:(NSString *)strSecondData
{
    NSString *strHaveBeenUsingTime1 = [strFirstData substringWithRange:NSMakeRange(68, 4)];
    NSString *strHaveBeenUsingTime2 = [strFirstData substringWithRange:NSMakeRange(72, 4)];
    NSString *strHaveBeenUsingTime3 = [strFirstData substringWithRange:NSMakeRange(76, 4)];
    NSString *strHaveBeenUsingTime4 = [strFirstData substringWithRange:NSMakeRange(80, 4)];
    NSString *strHaveBeenUsingTime5 = [strFirstData substringWithRange:NSMakeRange(84, 4)];
    
    //设备状态：0，冲洗；1：水满； 2，制水； 3，缺水；4，检修；5，关机；
    NSString *strWaterFilterStatue = [strFirstData substringWithRange:NSMakeRange(104, 2)];
    //水压，0,缺水；1，有水；
    NSString *strWaterPressurestr = [strFirstData substringWithRange:NSMakeRange(106, 2)];
    //水温
    NSString *strWaterTemperture = [strFirstData substringWithRange:NSMakeRange(108, 2)];
    
    //原水TDS1，最大值为65535
    NSString *strTDS1 =  [strFirstData substringWithRange:NSMakeRange(110, 2)];
    
    //纯水TDS2，最大值为65535
    NSString *strTDS2 =  [strFirstData substringWithRange:NSMakeRange(112, 2)];
    
    NSString *strSetUsingTime1 = [strFirstData substringWithRange:NSMakeRange(118, 4)];
    NSString *strSetUsingTime2 = [strFirstData substringWithRange:NSMakeRange(122, 4)];
    NSString *strSetUsingTime3 = [strFirstData substringWithRange:NSMakeRange(126, 4)];
    
    NSString *strSetUsingTime4 = [strSecondData substringWithRange:NSMakeRange(40, 4)];
    NSString *strSetUsingTime5 = [strSecondData substringWithRange:NSMakeRange(44, 4)];
    
    NSDictionary *dict = @{@"strHaveBeenUsingTime1":strHaveBeenUsingTime1,
                           @"strHaveBeenUsingTime2":strHaveBeenUsingTime2,
                           @"strHaveBeenUsingTime3":strHaveBeenUsingTime3,
                           @"strHaveBeenUsingTime4":strHaveBeenUsingTime4,
                           @"strHaveBeenUsingTime5":strHaveBeenUsingTime5,
                           @"strWaterFilterStatue":strWaterFilterStatue,
                           @"strWaterPressurestr":strWaterPressurestr,
                           @"strWaterTemperture":strWaterTemperture,
                           @"strTDS1":strTDS1,
                           @"strTDS2":strTDS2,
                           @"strSetUsingTime1":strSetUsingTime1,
                           @"strSetUsingTime2":strSetUsingTime2,
                           @"strSetUsingTime3":strSetUsingTime3,
                           @"strSetUsingTime4":strSetUsingTime4,
                           @"strSetUsingTime5":strSetUsingTime5};
    return dict;
}


#pragma mark -
#pragma mark - 场景有关的
- (void)doHandleAnalyseConfigurationScreenWithWholeData:(NSString *)strHexWholeData
{
    NSString *strCmdIDHexMacAddr  = [strHexWholeData substringWithRange:NSMakeRange(4, 2)];
    NSString *strCmdIDBigMacAddr  = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strCmdIDHexMacAddr];
    
    NSString *strZigbeeHexMacAddr  = [strHexWholeData substringWithRange:NSMakeRange(6, 16)];
    NSString *strZigbeeBigMacAddr  = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strZigbeeHexMacAddr];
    
    NSString *strSubCommandLength = [strHexWholeData substringWithRange:NSMakeRange(22, 2)];
    NSString *strSubCommandLengthBig = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strSubCommandLength];
    int iSubCommandLength = (int)[[ToolHexManager sharedManager] numberWithHexString:strSubCommandLength];
    
    NSString *strSubcommandIdentifer = [strHexWholeData substringWithRange:NSMakeRange(24, 2)];
    NSString *strSubcommandIdentiferBig = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strSubcommandIdentifer];
    
    NSString *strScreenNo = [strHexWholeData substringWithRange:NSMakeRange(26, 2)];
    NSString *strScreenNoBig = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strScreenNo];
    
    NSString *strAnswerIdentifer = [strHexWholeData substringWithRange:NSMakeRange(28, 2)];
    NSString *strAnswerIdentiferBig = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strAnswerIdentifer];
    
    
    LLog([NSString stringWithFormat:@"从场景配置中获取网关的MAC地址：%@;编号：%@;成功表示符：%@",strZigbeeBigMacAddr,strScreenNoBig,strAnswerIdentiferBig]);
    
    SHModelScreenNew *screen            = [SHModelScreenNew new];
    screen.strCmdID                     = strCmdIDBigMacAddr;
    screen.strSubCommandLength          = strSubCommandLengthBig;
    screen.strSubcommandIdentifer       = strSubcommandIdentiferBig;
    screen.strScreenNo                  = strScreenNoBig;
    screen.strAnswerIdentifer           = strAnswerIdentiferBig;
    
    if (iSubCommandLength == 3) {
        if ([strSubcommandIdentiferBig isEqualToString:@"01"]) {
            //场景配置指令
            LLog(@"收到上报场景配置指令数据帧");
        }else if ([strSubcommandIdentiferBig isEqualToString:@"05"]){
            //设防布防
            LLog(@"收到上报设防布防数据帧");
        }else if ([strSubcommandIdentiferBig isEqualToString:@"07"]){
            //场景联动删除命令
            LLog([NSString stringWithFormat:@"上报删除场景编号:%@",strScreenNoBig]);
        }else if ([strSubcommandIdentiferBig isEqualToString:@"08"]){
            //网关报警状态解除命令
            LLog(@"收到上报网关报警状态解除命令");
        }
        screen.strInstructionCount          = @"";
        screen.strCurrentInstructionNO      = @"";
        screen.strArmingType                = @"";
        screen.strDelteScreenNo             = @"";
        
    }else if (iSubCommandLength == 5){
        //场景载入指令
        NSString *strInstructionCount = [strHexWholeData substringWithRange:NSMakeRange(30, 2)];
        NSString *strInstructionCountBig = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strInstructionCount];
        
        NSString *strCurrentInstructionNO = [strHexWholeData substringWithRange:NSMakeRange(32, 2)];
        NSString *strCurrentInstructionNOBig = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strCurrentInstructionNO];
        
        screen.strInstructionCount = strInstructionCountBig;
        screen.strCurrentInstructionNO = strCurrentInstructionNOBig;
        screen.strArmingType = @"";
        screen.strDelteScreenNo = @"";
        
    }
    
    //    [[NSNotificationCenter defaultCenter] postNotificationName:kScreenCofigNoti
    //                                                        object:screen
    //                                                      userInfo:nil];
    
    self.screenNew = screen;
}



#pragma mark -
#pragma mark - 远程开锁密码展示
- (NSString *)handleGetLockCmdIDWithWholeFrame:(NSString *)strWhole
{
    for (int i = 0; i < strWhole.length/2; i ++) {
        
        NSString *strTemp = [strWhole substringWithRange:NSMakeRange(i*2, 2)];
        
        if ([strTemp isEqualToString:@"a1"] || [strTemp isEqualToString:@"53"]) {
            
            NSString *strNeed = [strWhole substringWithRange:NSMakeRange((i + 1)*2, 2)];
            LLog([NSString stringWithFormat:@"strTemp == %@;iii===%d,strneed=%@",strTemp,i*2,strNeed]);
            return strNeed;
            
        }
    }
    return @"FF";
}




#pragma mark -
#pragma mark - 场景
//设防布防配置指令返回
- (void)doHandleAnalyseSetArmingBackFromGatewayWithWholeData:(NSString *)strHexWholeData
{
    
    NSString *strCmdIDHexMacAddr  = [strHexWholeData substringWithRange:NSMakeRange(4, 2)];
    NSString *strCmdIDBigMacAddr  = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strCmdIDHexMacAddr];
    
    NSString *strSubcommandIdentifer    = [strHexWholeData substringWithRange:NSMakeRange(24, 2)];
    NSString *strSubcommandIdentiferBig = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strSubcommandIdentifer];
    
    NSString *strAnswerIdentifer    = [strHexWholeData substringWithRange:NSMakeRange(28, 2)];
    NSString *strAnswerIdentiferBig = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strAnswerIdentifer];
    
    SHModelScreenNew *screen            = [SHModelScreenNew new];
    screen.strCmdID                     = strCmdIDBigMacAddr;
    screen.strSubcommandIdentifer       = strSubcommandIdentiferBig;
    screen.strAnswerIdentifer           = strAnswerIdentiferBig;
    
    self.screenNew = screen;
    
}


#pragma mark -
#pragma mark -  private
- (NSString *)doHandleNumberWithHexString:(NSString *)hexString{
    
    const char *hexChar = [hexString cStringUsingEncoding:NSUTF8StringEncoding];
    
    int hexNumber;
    
    sscanf(hexChar, "%x", &hexNumber);
    
    
    NSString *strResult;
    if (hexNumber >= 10) {
        strResult = [NSString stringWithFormat:@"%d",hexNumber];
    }else{
        
        strResult = [NSString stringWithFormat:@"0%d",hexNumber];
    }
    return strResult;
}


#pragma mark -
#pragma mark - 新风系统

//#define xf_fuctionCode          "xf_fuctionCode";
//#define xf_switch               "xf_switch";
//#define xf_helpWarm             "xf_helpWarm";
//#define xf_workMode             "xf_workMode";
//
//#define xf_circulationMode      "xf_circulationMode";
//#define xf_inAndOutWindScale    "xf_inAndOutWindScale";
//#define xf_windSpeedGear        "xf_windSpeedGear";
//#define xf_airVolume            "xf_airVolume";
//
//#define xf_particulates         "xf_particulates";
//#define xf_CO2ConCtent          "xf_CO2ConCtent";
//#define xf_intakeTemperature    "xf_intakeTemperature";
//#define xf_outflowTemperature   "xf_outflowTemperature";
//
//#define xf_normalOrMalfunction  "xf_normalOrMalfunction";
//#define xf_chuLvTimePer         "xf_chuLvTimePer";
//#define xf_dustBoxTimePer       "xf_dustBoxTimePer";
//#define xf_gaoLvTimePer         "xf_gaoLvTimePer";
- (NSDictionary *)doHandleXFReportWithXFWholeData:(NSString *)strXFWholeData
{
    //返回的对应功能码
    NSString *str_xf_fuctionCode = [strXFWholeData substringWithRange:NSMakeRange(4, 2)];
    LLog([NSString stringWithFormat:@"功能码：%@",str_xf_fuctionCode]);
    
    NSString *str_BaseStatus = [strXFWholeData substringWithRange:NSMakeRange(8, 2)];
    NSString *str_getBaseStatusBit = [[ToolHexManager sharedManager] getBinaryByhex:str_BaseStatus];
    NSString *str_ValidBit = [self getLastSixBitWithStr:str_getBaseStatusBit];
    NSString *str_xf_switch = [self doGetSwitchStatusWithStrMix:str_ValidBit];
    NSString *str_xf_helpWarm = [self doGetHelpWarmStatusWithStrMix:str_ValidBit];
    NSInteger i_xf_workMode = [self doGetWorkStatusWithStrMix:str_ValidBit];
    NSInteger i_xf_circulationMode  = [self doGetCirlutionModeWithStrMix:str_ValidBit];
    LLog([NSString stringWithFormat:@"基本状态：%@;\n allbit:%@;\nvalidBit:%@",str_BaseStatus,str_getBaseStatusBit,str_ValidBit]);
    
    LLog([NSString stringWithFormat:@"开关状态:%@",str_xf_switch]);
    
    LLog([NSString stringWithFormat:@"加热:%@",str_xf_helpWarm]);
    
    LLog([NSString stringWithFormat:@"工作状态:%ld",(long)i_xf_workMode]);
    
    LLog([NSString stringWithFormat:@"循环模式:%ld",(long)i_xf_circulationMode]);
    
    NSString *str_xf_inAndOutWindScale = [strXFWholeData substringWithRange:NSMakeRange(10, 2)];
    LLog([NSString stringWithFormat:@"进回风比例：%@",str_xf_inAndOutWindScale]);
    NSString *str_xf_windSpeedGear = [strXFWholeData substringWithRange:NSMakeRange(12, 2)];
    NSInteger i_xf_windSpeedGear =[[ToolHexManager sharedManager] numberWithHexString:str_xf_windSpeedGear];
    LLog([NSString stringWithFormat:@"风速：%@--%ld",str_xf_windSpeedGear,(long)i_xf_windSpeedGear]);
    
    NSString *str_xf_airVolume = [strXFWholeData substringWithRange:NSMakeRange(14, 4)];
    NSInteger i_xf_airVolume = [[ToolHexManager sharedManager] numberWithHexString:str_xf_airVolume];
    LLog([NSString stringWithFormat:@"风量：%@--%ld",str_xf_airVolume,(long)i_xf_airVolume]);
    
    NSString *str_xf_particulates = [strXFWholeData substringWithRange:NSMakeRange(18, 4)];
    NSInteger i_xf_particulates =[[ToolHexManager sharedManager] numberWithHexString:str_xf_particulates];
    LLog([NSString stringWithFormat:@"颗粒物量：%@--%ld",str_xf_particulates,(long)i_xf_particulates]);
    
    NSString *str_xf_CO2ConCtent = [strXFWholeData substringWithRange:NSMakeRange(22, 4)];
    NSInteger i_xf_CO2ConCtent =[[ToolHexManager sharedManager] numberWithHexString:str_xf_CO2ConCtent];
    LLog([NSString stringWithFormat:@"co2量：%@,十进制为:%ld",str_xf_CO2ConCtent,(long)i_xf_CO2ConCtent]);
    
    NSString *str_xf_intakeTemperature = [strXFWholeData substringWithRange:NSMakeRange(26, 2)];
    NSInteger i_xf_intakeTemperature = [[ToolHexManager sharedManager] numberWithHexString:str_xf_intakeTemperature];
    LLog([NSString stringWithFormat:@"进风温度：%@--%ld",str_xf_intakeTemperature,i_xf_intakeTemperature -30]);
    
    NSString *str_xf_outflowTemperature = [strXFWholeData substringWithRange:NSMakeRange(28, 2)];
    NSInteger i_xf_outflowTemperature = [[ToolHexManager sharedManager] numberWithHexString:str_xf_outflowTemperature];
    LLog([NSString stringWithFormat:@"出风温度:%@--%ld",str_xf_outflowTemperature,i_xf_outflowTemperature - 30]);
    
    NSString *str_xf_normalOrMalfunction = [strXFWholeData substringWithRange:NSMakeRange(30, 2)];
    LLog([NSString stringWithFormat:@"设备是否正常：%@",str_xf_normalOrMalfunction]);
    
    NSString *str_xf_chuLvTimePer = [strXFWholeData substringWithRange:NSMakeRange(32, 2)];
    LLog([NSString stringWithFormat:@"初滤剩余时间百分比:%@",str_xf_chuLvTimePer]);
    NSString *str_xf_dustBoxTimePer = [strXFWholeData substringWithRange:NSMakeRange(36, 2)];
    LLog([NSString stringWithFormat:@"集尘箱剩余时间百分比：%@",str_xf_dustBoxTimePer]);
    
    NSString *str_xf_gaoLvTimePer = [strXFWholeData substringWithRange:NSMakeRange(40, 2)];
    LLog([NSString stringWithFormat:@"高滤剩余时间百分比：%@",str_xf_gaoLvTimePer]);
    
    NSDictionary *dictTemp = @{@"xf_fuctionCode":str_xf_fuctionCode,
                               @"xf_switch":str_xf_switch,
                               @"xf_helpWarm":str_xf_helpWarm,
                               @"xf_workMode":@(i_xf_workMode),
                               @"xf_circulationMode":@(i_xf_circulationMode),
                               @"xf_inAndOutWindScale":str_xf_inAndOutWindScale,
                               @"xf_windSpeedGear":@(i_xf_windSpeedGear),
                               @"xf_airVolume":@(i_xf_airVolume),
                               @"xf_particulates":@(i_xf_particulates),
                               @"xf_CO2ConCtent":@(i_xf_CO2ConCtent),
                               @"xf_intakeTemperature":@(i_xf_intakeTemperature-30),
                               @"xf_outflowTemperature":@(i_xf_outflowTemperature-30),
                               @"xf_normalOrMalfunction":str_xf_normalOrMalfunction,
                               @"xf_chuLvTimePer":str_xf_chuLvTimePer,
                               @"xf_dustBoxTimePer":str_xf_dustBoxTimePer,
                               @"xf_gaoLvTimePer":str_xf_gaoLvTimePer};
    return dictTemp;
    
}

#pragma mark - 取后六位bit
- (NSString *)getLastSixBitWithStr:(NSString *)strBit
{
    NSString *strResult = [strBit substringWithRange:NSMakeRange(2, 6)];
    return strResult;
}

#pragma mark - 获取开关状态
- (NSString *)doGetSwitchStatusWithStrMix:(NSString *)strMix
{
    NSString *strResult = [strMix substringWithRange:NSMakeRange(5, 1)];
    //    NSLog(@"strResult == %@",strResult);
    return strResult;
}

#pragma mark -辅助电解热状态
- (NSString *)doGetHelpWarmStatusWithStrMix:(NSString *)strMix
{
    NSString *strResult = [strMix substringWithRange:NSMakeRange(4, 1)];
    return strResult;
}

#pragma mark -获取工作模式
- (NSInteger)doGetWorkStatusWithStrMix:(NSString *)strMix
{
    NSString *strResult1 = [strMix substringWithRange:NSMakeRange(3, 1)];
    NSString *strResult2 = [strMix substringWithRange:NSMakeRange(2, 1)];
    NSString *strResult = [NSString stringWithFormat:@"%@%@",strResult2,strResult1];
    NSInteger iWorkStatus = [[ToolHexManager sharedManager] toDecimalSystemWithBinarySystem:strResult];
    return iWorkStatus;
}

#pragma mark -获取循环模式
- (NSInteger)doGetCirlutionModeWithStrMix:(NSString *)strMix
{
    NSString *strResult1 = [strMix substringWithRange:NSMakeRange(1, 1)];
    NSString *strResult2 = [strMix substringWithRange:NSMakeRange(0, 1)];
    NSString *strResult = [NSString stringWithFormat:@"%@%@",strResult2,strResult1];
    NSInteger iCirculation = [[ToolHexManager sharedManager] toDecimalSystemWithBinarySystem:strResult];
    return iCirculation;
}


#pragma mark -
#pragma mark - 获取处理网关设备的回调
- (void)doHandleGetGatewayWifiDetailsWithWholeData:(NSString *)strHexWholeData
{
    NSString *strReturnCmd           = [strHexWholeData substringWithRange:NSMakeRange(4, 2)];
    if ([strReturnCmd isEqualToString:@"02"]) {
        //网关写入配置成功
        NSString *strDeviceHexMacAddr           = [strHexWholeData substringWithRange:NSMakeRange(6, 16)];
        NSString *strHexOD = [strHexWholeData substringWithRange:NSMakeRange(22, 4)];
        if ([strHexOD isEqualToString:@"13c4"]) {
            NSString *strHexSuccedIdentifer = [strHexWholeData substringWithRange:NSMakeRange(36, 2)];
            SHModelGateway *gateway = [SHModelGateway new];
            if ([strHexSuccedIdentifer isEqualToString:@"00"]) {
                NSLog(@"wifi配置成功!");
                gateway.isWriteWifiConfigSucced = YES;
                gateway.strGateway_mac_address = strDeviceHexMacAddr;
                
            }else{
                NSLog(@"wifi配置失败");
                gateway.isWriteWifiConfigSucced = NO;
            }
            dispatch_async(dispatch_get_main_queue(), ^{
                [[NSNotificationCenter defaultCenter] postNotificationName:kYGatewayConfigNotify
                                                                    object:gateway
                                                                  userInfo:nil];
            });
            
        }
    }else{
        //读取网关配置
        NSString *strDeviceHexMacAddr           = [strHexWholeData substringWithRange:NSMakeRange(6, 16)];
        NSString *strHexOD                      = [strHexWholeData substringWithRange:NSMakeRange(22, 4)];
        NSString *strLocalPort                  = [strHexWholeData substringWithRange:NSMakeRange(38, 4)];
        NSString *strOutPort                    = [strHexWholeData substringWithRange:NSMakeRange(42, 4)];
        NSString *strIp                         = [strHexWholeData substringWithRange:NSMakeRange(46, 46)];
        
        NSString *portString = [NSString stringWithFormat:@"%d", [HLDataUtil valueWithHexString:strLocalPort]];
        NSString *remotePortString = [NSString stringWithFormat:@"%d", [HLDataUtil valueWithHexString:strOutPort]];
        NSString *ipString = [HLDataUtil stringFromHexString:strIp];
        if ([ipString containsString:@"\\0"]) {//“\”的转义字符
            ipString = [ipString componentsSeparatedByString:@"\\0"][0];
        }
        NSLog(@"没有转换前的值为port：%@， remotePort：%@， ipString：%@", strLocalPort, strOutPort, strIp);
        NSLog(@"获取到的port：%@， remotePort：%@， ipString：%@", portString, remotePortString, ipString);
        
        [[NetworkEngine shareInstance] doGetRestarWifiModuleData];
        
        SHModelGateway *gateway = [SHModelGateway new];
        gateway.strGateway_mac_address = strDeviceHexMacAddr;
        gateway.strOD = strHexOD;
        gateway.strGatewayPort = portString;
        gateway.strRemotePort = remotePortString;
        gateway.strRemoteIP = ipString;
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [[NSNotificationCenter defaultCenter] postNotificationName:kYReadGatewayConfigInfoNotify
                                                                object:gateway
                                                              userInfo:nil];
        });
    }
    
}

@end
