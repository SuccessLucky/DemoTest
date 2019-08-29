//
//  NetworkEngine+UnPack.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/28.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "NetworkEngine.h"

NS_ASSUME_NONNULL_BEGIN

@interface NetworkEngine (UnPack)

@property (strong, nonatomic) NSMutableData *respondData;
@property (nonatomic, assign) int iRememberLength;
@property (nonatomic, assign) int iResponseSize;

@property (strong, nonatomic) NSMutableData *cloudRespondData;
@property (assign, nonatomic) int iCloudRememberLength;
@property (assign, nonatomic) int iCloudResponseSize;

@property (strong, nonatomic) NSString *strWaterFirstFrame;
@property (strong, nonatomic) NSString *strWaterSecondFrame;

//处理局域网Rsp数据（粘包，缺包问题）
- (void)handleLocalRspData:(NSData *)pData;

//处理云端Rsp数据（粘包，缺包问题）
- (void)handleCloudSeverData:(NSData *)cloudSeverData;

/**
 未经过处理进行数据解包
 
 @param dataReceive tcp接受到的数据
 */
- (void)doUnPackReceivedData:(NSData *)dataReceive;

- (void)doUnPackReceivedCludData:(NSData *)dataReceiveCloud;

@end

NS_ASSUME_NONNULL_END
