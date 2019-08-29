//
//  HLOrderUtil.m
//  JiZhiSDK
//
//  Created by 王振 DemoKing on 2016/11/30.
//  Copyright © 2016年 DemoKing. All rights reserved.
//

#import "HLOrderUtil.h"
#import "HLDataUtil.h"


@implementation HLOrderUtil

/**
 4010 获取子索引选项
 
 @param array 置位数组
 @return string
 */
+ (NSString *)subIndex4010DataWithArray:(NSArray <NSNumber *>*)array {
    NSMutableArray *arrayIndes = [NSMutableArray arrayWithArray:array];
    NSMutableArray *arrayBinary = [NSMutableArray arrayWithCapacity:0];
    
    for (int i = 31; i >= 0; i --) {
        
        NSNumber *number = @0;
        if (arrayIndes.count) {
            int index = [[arrayIndes lastObject] intValue];
            if (i == index) {
                number = @1;
                [arrayIndes removeLastObject];
            }else {
                number = @0;
            }
        }else {
            number = @0;
        }
        [arrayBinary addObject:number];
    }
    NSString *subIndexSe = [arrayBinary componentsJoinedByString:@""];
    NSLog(@"二进制子索引选项：%@",subIndexSe);
    int a1 =
    [arrayBinary [0] intValue] * pow(2, 3) +
    [arrayBinary [1] intValue] * pow(2, 2) +
    [arrayBinary [2] intValue] * pow(2, 1) +
    [arrayBinary [3] intValue] * pow(2, 0);
    
    int a2 =
    [arrayBinary [4] intValue] * pow(2, 3) +
    [arrayBinary [5] intValue] * pow(2, 2) +
    [arrayBinary [6] intValue] * pow(2, 1) +
    [arrayBinary [7] intValue] * pow(2, 0);
    
    int b1 =
    [arrayBinary [8] intValue] * pow(2, 3) +
    [arrayBinary [9] intValue] * pow(2, 2) +
    [arrayBinary [10] intValue] * pow(2, 1) +
    [arrayBinary [11] intValue] * pow(2, 0);
    
    int b2 =
    [arrayBinary [12] intValue] * pow(2, 3) +
    [arrayBinary [13] intValue] * pow(2, 2) +
    [arrayBinary [14] intValue] * pow(2, 1) +
    [arrayBinary [15] intValue] * pow(2, 0);
    
    int c1 =
    [arrayBinary [16] intValue] * pow(2, 3) +
    [arrayBinary [17] intValue] * pow(2, 2) +
    [arrayBinary [18] intValue] * pow(2, 1) +
    [arrayBinary [19] intValue] * pow(2, 0);
    
    int c2 =
    [arrayBinary [20] intValue] * pow(2, 3) +
    [arrayBinary [21] intValue] * pow(2, 2) +
    [arrayBinary [22] intValue] * pow(2, 1) +
    [arrayBinary [23] intValue] * pow(2, 0);
    
    int d1 =
    [arrayBinary [24] intValue] * pow(2, 3) +
    [arrayBinary [25] intValue] * pow(2, 2) +
    [arrayBinary [26] intValue] * pow(2, 1) +
    [arrayBinary [27] intValue] * pow(2, 0);
    
    int d2 =
    [arrayBinary [28] intValue] * pow(2, 3) +
    [arrayBinary [29] intValue] * pow(2, 2) +
    [arrayBinary [30] intValue] * pow(2, 1) +
    [arrayBinary [31] intValue] * pow(2, 0);
    
    NSString *aString = [NSString stringWithFormat:@"%x%x",a1,a2];
    NSString *bString = [NSString stringWithFormat:@"%x%x",b1,b2];
    NSString *cString = [NSString stringWithFormat:@"%x%x",c1,c2];
    NSString *dString = [NSString stringWithFormat:@"%x%x",d1,d2];
    return [NSString stringWithFormat:@"%@%@%@%@",aString,bString,cString,dString];
}

/**
 重新构建需要发送的数据
 
 @param model model对象
 @return 新数据
 */
+ (NSData *)sendDataWithSendModel:(SendOrderModel *)model {
    NSMutableData *data = [NSMutableData dataWithData:model.cmdId];
    [data appendData:model.dstAddrFmt];
    [data appendData:model.originMac];
    [data appendData:model.dstAddr];
    [data appendData:model.od];
    [data appendData:model.subIndex];
    
    // dataOrder = 子索引选项 + data
    NSMutableData *dataOrder = [NSMutableData dataWithData:model.subIndexSelection];
    [dataOrder appendData:model.data];
    
    // 计算长度
    NSInteger dataOrderLength = dataOrder.length;
    NSString *dataOrderLengthString = [NSString stringWithFormat:@"%lx",(long)dataOrderLength];
    if (dataOrderLengthString.length == 1) {
        dataOrderLengthString = [NSString stringWithFormat:@"0%@",dataOrderLengthString];
    }
    NSData *length = [HLDataUtil dataWithHexString:dataOrderLengthString];
    
    [data appendData:length];
    [data appendData:dataOrder];
    
    // 数据域长度
    NSString *dataLengthString = [NSString stringWithFormat:@"%lx",(unsigned long)data.length];
    if (dataLengthString.length == 1) {
        dataLengthString = [NSString stringWithFormat:@"0%@",dataOrderLengthString];
    }
    NSData *dataLength = [HLDataUtil dataWithHexString:dataLengthString];
    NSLog(@"发送 数据域长度 :%@",dataLength);
    
    NSMutableData *sendData = [NSMutableData dataWithData:[HLDataUtil dataWithHexString:@"2a"]];
    [sendData appendData:dataLength];
    [sendData appendData:data];
    [sendData appendData:[HLOrderUtil cs:data]];
    NSLog(@"发送 校验位 :%@",[HLOrderUtil cs:data]);
    [sendData appendData:[HLDataUtil dataWithHexString:@"23"]];
    return sendData;
}

#pragma mark - 静音窗帘 子命令校验和
+(NSString *)silentCurtainCs:(NSString *)dataString
{
    
    NSMutableArray *array = [NSMutableArray arrayWithCapacity:0];
    
    for (int i = 0; i < dataString.length; i = i + 2) {
        
        NSString *string = [dataString substringWithRange:NSMakeRange(i, 2)];
        
        [array addObject:string];
    }
    
    __block int value = 0;
    [array enumerateObjectsUsingBlock:^(NSString *obj, NSUInteger idx, BOOL * _Nonnull stop) {
        unsigned int a ;
        [[NSScanner scannerWithString:obj] scanHexInt:&a];
        value = value + a;
    }];
    
    unsigned int b;
    [[NSScanner scannerWithString:@"2017"] scanHexInt:&b];
    
    value = value+b;
    
    NSString *countString = [NSString stringWithFormat:@"%04x",value];
    
    return countString;
}


+ (NSData *)cs:(NSData *)data {
    
    NSString *dataString = [HLDataUtil stringWithData:data];
    
    NSMutableArray *array = [NSMutableArray arrayWithCapacity:0];
    
    for (int i = 0; i < dataString.length; i = i + 2) {
        
        NSString *string = [dataString substringWithRange:NSMakeRange(i, 2)];
        
        [array addObject:string];
    }
    
    __block int value = 0;
    [array enumerateObjectsUsingBlock:^(NSString *obj, NSUInteger idx, BOOL * _Nonnull stop) {
        unsigned int a ;
        [[NSScanner scannerWithString:obj] scanHexInt:&a];
        value = value + a;
        if (value >= 256) {
            value = value - 256;
        }
    }];
    
    NSString *countString = [NSString stringWithFormat:@"%x",value];
    if (countString.length == 1) {
        
        countString = [NSString stringWithFormat:@"0%@",countString];
    }
    return [HLDataUtil dataWithHexString:countString];
}

/**
 求异或和
 */
+ (NSData *)xorWithData:(NSData *)data {
    if (!data) {
        return nil;
    }
    Byte * bytes = (Byte *)[data bytes];
    Byte byte = bytes[0];
    for (int i = 1; i < data.length; i ++) {
        byte ^= bytes[i];
    }
    unsigned char value [1];
    value [0] = byte;
    NSData *valueData = [NSData dataWithBytes:&value length:1];
    return valueData;
}

/*
 * bref 求空调编码 两个字节的值
 */
+ (NSString *)ariConditionerCodeWithCodeInteger:(NSInteger)codeInteger {
    if (!codeInteger) {
        return nil;
    } else {
        NSString * stringCode = [NSString stringWithFormat:@"%lx", (long)codeInteger];
        if (stringCode.length == 1) {
            stringCode = [NSString stringWithFormat:@"000%@", stringCode];
        }
        if (stringCode.length == 2) {
            stringCode = [NSString stringWithFormat:@"00%@", stringCode];
        }
        if (stringCode.length == 3) {
            stringCode =[NSString stringWithFormat:@"0%@", stringCode];
        }
        return stringCode;
    }
}
@end
