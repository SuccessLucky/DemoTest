//
//  ToolHexManager.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/9/27.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "ToolHexManager.h"
#import <SystemConfiguration/CaptiveNetwork.h>

@implementation ToolHexManager

static ToolHexManager *manager;
+ (id)sharedManager
{
    @synchronized(self)
    {
        if (!manager)
        {
            manager = [[ToolHexManager alloc] init];
        }
        
        return manager;
    }
}

-(NSData *)stringToByte:(NSString*)string
{
    NSString *hexString=[[string uppercaseString] stringByReplacingOccurrencesOfString:@" " withString:@""];
    if ([hexString length]%2!=0) {
        
        return nil;
    }
    Byte tempbyt[1]={0};
    NSMutableData* bytes=[NSMutableData data];
    for(int i=0;i<[hexString length];i++)
    {
        unichar hex_char1 = [hexString characterAtIndex:i]; ////两位16进制数中的第一位(高位*16)
        int int_ch1;
        if(hex_char1 >= '0' && hex_char1 <='9')
            int_ch1 = (hex_char1-48)*16;   //// 0 的Ascll - 48
        else if(hex_char1 >= 'A' && hex_char1 <='F')
            int_ch1 = (hex_char1-55)*16; //// A 的Ascll - 65
        else
            return nil;
        i++;
        
        unichar hex_char2 = [hexString characterAtIndex:i]; ///两位16进制数中的第二位(低位)
        int int_ch2;
        if(hex_char2 >= '0' && hex_char2 <='9')
            int_ch2 = (hex_char2-48); //// 0 的Ascll - 48
        else if(hex_char2 >= 'A' && hex_char2 <='F')
            int_ch2 = hex_char2-55; //// A 的Ascll - 65
        else
            return nil;
        
        tempbyt[0] = int_ch1+int_ch2;  ///将转化后的数放入Byte数组里
        [bytes appendBytes:tempbyt length:1];
    }
    return bytes;
}

#pragma mark - 获取空调相关固定数据 -

//空调的运行模式
- (NSArray *)arrAirConditonModel
{
    NSArray *arr = @[@{@"自动":@"00"},@{@"制冷":@"01"},@{@"除湿":@"02"},@{@"送风":@"03"},@{@"制暖":@"04"}];
    return arr;
}

//风速
- (NSArray *)arrAirConditionWindSpeed
{
    NSArray *arr = @[@{@"自动":@"00"},@{@"1档":@"01"},@{@"2档":@"02"},@{@"3档":@"03"}];
    return arr;
}

//风向
- (NSArray *)arrAirConditionWindDirection
{
    NSArray *arr = @[@{@"自动摆风":@"00"},@{@"手动摆风":@"01"}];
    return arr;
}

#pragma mark - 16进制字符串转 char数组 -
- (NSData *)temp16ToCharWithHEX:(NSString *)hexString
{
    //    NSString *hexString = @"2a3b4cef23"; //16进制字符串
    
    NSData *newData;
    NSInteger iLengthTemp = 0;
    //    Byte bytes[iLengthTemp];
    if (hexString.length == 1)
    {
        int j=0;
        iLengthTemp = hexString.length;
        Byte bytes[iLengthTemp];
        ///3ds key的Byte 数组， 128位
        for(int i=0;i<[hexString length];i++)
        {
            int int_ch;  /// 两位16进制数转化后的10进制数
            
            unichar hex_char1 = [hexString characterAtIndex:i]; ////两位16进制数中的第一位(高位*16)
            int int_ch1;
            if(hex_char1 >= '0' && hex_char1 <='9')
                int_ch1 = (hex_char1-48);   //// 0 的Ascll - 48
            else if(hex_char1 >= 'A' && hex_char1 <='F')
                int_ch1 = (hex_char1-55); //// A 的Ascll - 65
            else
                int_ch1 = (hex_char1-87); //// a 的Ascll - 97
            i++;
            
            int_ch = int_ch1;
            NSLog(@"int_ch=%d",int_ch);
            bytes[j] = int_ch;  ///将转化后的数放入Byte数组里
            j++;
        }
        newData = [[NSData alloc] initWithBytes:bytes length:iLengthTemp];
    }
    else
    {
        int j=0;
        
        iLengthTemp = hexString.length/2;
        Byte bytes[iLengthTemp];
        ///3ds key的Byte 数组， 128位
        for(int i=0;i<[hexString length];i++)
        {
            int int_ch;  /// 两位16进制数转化后的10进制数
            
            unichar hex_char1 = [hexString characterAtIndex:i]; ////两位16进制数中的第一位(高位*16)
            int int_ch1;
            if(hex_char1 >= '0' && hex_char1 <='9')
                int_ch1 = (hex_char1-48)*16;   //// 0 的Ascll - 48
            else if(hex_char1 >= 'A' && hex_char1 <='F')
                int_ch1 = (hex_char1-55)*16; //// A 的Ascll - 65
            else
                int_ch1 = (hex_char1-87)*16; //// a 的Ascll - 97
            i++;
            
            unichar hex_char2 = [hexString characterAtIndex:i]; ///两位16进制数中的第二位(低位)
            int int_ch2;
            if(hex_char2 >= '0' && hex_char2 <='9')
                int_ch2 = (hex_char2-48); //// 0 的Ascll - 48
            else if(hex_char1 >= 'A' && hex_char1 <='F')
                int_ch2 = hex_char2-55; //// A 的Ascll - 65
            else
                int_ch2 = hex_char2-87; //// a 的Ascll - 97
            
            int_ch = int_ch1+int_ch2;
            NSLog(@"int_ch=%d",int_ch);
            bytes[j] = int_ch;  ///将转化后的数放入Byte数组里
            j++;
        }
        newData = [[NSData alloc] initWithBytes:bytes length:iLengthTemp];
    }
    
    
    
    NSLog(@"newData=%@",newData);
    return newData;
}

#pragma mark - 二进制转16进制 -
- (NSString*)convertBin:(NSString *)bin
{
    if ([bin length] > 16) {
        NSMutableArray *bins = [NSMutableArray array];
        for (int i = 0;i < [bin length]; i += 16) {
            [bins addObject:[bin substringWithRange:NSMakeRange(i, 16)]];
        }
        NSMutableString *ret = [NSMutableString string];
        for (NSString *abin in bins) {
            [ret appendString:[self convertBin:abin]];
        }
        return ret;
    } else {
        int value = 0;
        for (int i = 0; i < [bin length]; i++) {
            value += pow(2,i)*[[bin substringWithRange:NSMakeRange([bin length]-1-i, 1)] intValue];
        }
        return [NSString stringWithFormat:@"%X", value];
    }
}


#pragma mark - 1 二进制转十进制 -
- (NSInteger)toDecimalSystemWithBinarySystem:(NSString *)binary

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
    return [result integerValue] ;
    
}

#pragma mark - 十进制转二进制 -
- (NSString *)toBinarySystemWithDecimalSystem:(NSString *)decimal

{
    int num = [decimal intValue];
    int remainder = 0;      //余数
    int divisor = 0;        //除数
    NSString * prepare = @"";
    
    while (true)
    {
        remainder = num%2;
        
        divisor = num/2;
        
        num = divisor;
        
        prepare = [prepare stringByAppendingFormat:@"%d",remainder];
        
        if (divisor == 0)
        {
            break;
        }
    }
    
    NSString * result = @"";
    
    for (int i = (int)prepare.length - 1; i >= 0; i --)
    {
        result = [result stringByAppendingFormat:@"%@",
                  
                  [prepare substringWithRange:NSMakeRange(i , 1)]];
        
    }
    
    return result;
    
}

#pragma mark - 将16进制转化为二进制 -

-(NSString *)getBinaryByhex:(NSString *)hex
{
    NSMutableDictionary  *hexDic = [[NSMutableDictionary alloc] init];
    
    hexDic = [[NSMutableDictionary alloc] initWithCapacity:16];
    
    [hexDic setObject:@"0000" forKey:@"0"];
    
    [hexDic setObject:@"0001" forKey:@"1"];
    
    [hexDic setObject:@"0010" forKey:@"2"];
    
    [hexDic setObject:@"0011" forKey:@"3"];
    
    [hexDic setObject:@"0100" forKey:@"4"];
    
    [hexDic setObject:@"0101" forKey:@"5"];
    
    [hexDic setObject:@"0110" forKey:@"6"];
    
    [hexDic setObject:@"0111" forKey:@"7"];
    
    [hexDic setObject:@"1000" forKey:@"8"];
    
    [hexDic setObject:@"1001" forKey:@"9"];
    
    [hexDic setObject:@"1010" forKey:@"A"];
    
    [hexDic setObject:@"1011" forKey:@"B"];
    
    [hexDic setObject:@"1100" forKey:@"C"];
    
    [hexDic setObject:@"1101" forKey:@"D"];
    
    [hexDic setObject:@"1110" forKey:@"E"];
    
    [hexDic setObject:@"1111" forKey:@"F"];
    
    NSMutableString *binaryString=[[NSMutableString alloc] init];
    
    for (int i=0; i<[hex length]; i++) {
        
        NSRange rage;
        
        rage.length = 1;
        
        rage.location = i;
        
        NSString *key = [hex substringWithRange:rage];
        
        //NSLog(@"%@",[NSString stringWithFormat:@"%@",[hexDic objectForKey:key]]);
        
        binaryString = (NSMutableString *)[NSString stringWithFormat:@"%@%@",binaryString,[NSString stringWithFormat:@"%@",[hexDic objectForKey:key]]];
        
    }
    
    //NSLog(@"转化后的二进制为:%@",binaryString);
    
    return binaryString;
    
}

#pragma mark - 1 将十进制转化为十六进制 -
- (NSString *)ToHex:(uint16_t)tmpid
{
    NSString *nLetterValue;
    NSString *str =@"";
    uint16_t ttmpig;
    for (int i = 0; i<9; i++) {
        ttmpig=tmpid%16;
        tmpid=tmpid/16;
        switch (ttmpig)
        {
            case 10:
                nLetterValue =@"A";break;
            case 11:
                nLetterValue =@"B";break;
            case 12:
                nLetterValue =@"C";break;
            case 13:
                nLetterValue =@"D";break;
            case 14:
                nLetterValue =@"E";break;
            case 15:
                nLetterValue =@"F";break;
            default:
                nLetterValue = [NSString stringWithFormat:@"%u",ttmpig];
                
        }
        str = [nLetterValue stringByAppendingString:str];
        if (tmpid == 0) {
            break;
        }
        
    }
    return str;
}

#pragma mark - 1 将十进制转化为二进制,设置返回NSString 长度  -
- (NSString *)decimalTOBinary:(uint16_t)tmpid backLength:(int)length
{
    NSString *a = @"";
    while (tmpid)
    {
        a = [[NSString stringWithFormat:@"%d",tmpid%2] stringByAppendingString:a];
        if (tmpid/2 < 1)
        {
            break;
        }
        tmpid = tmpid/2 ;
    }
    
    if (a.length <= length)
    {
        NSMutableString *b = [[NSMutableString alloc]init];;
        for (int i = 0; i < length - a.length; i++)
        {
            [b appendString:@"0"];
        }
        
        a = [b stringByAppendingString:a];
    }
    
    return a;
    
}



- (void)hanleBtnRound:(UIButton *)btn
{
    btn.layer.cornerRadius = 5;
    btn.titleLabel.font = [UIFont systemFontOfSize:16];
    btn.backgroundColor = RGB(22, 154, 218);
}


#pragma mark - 获取网管的macAddr
- (NSString *)fetchRouterSSIDInfo {
    NSArray *ifs = (__bridge_transfer id)CNCopySupportedInterfaces();
    //    NSLog(@"Supported interfaces: %@", ifs);
    id info = nil;
    for (NSString *ifnam in ifs) {
        info = (__bridge_transfer id)CNCopyCurrentNetworkInfo((__bridge CFStringRef)ifnam);
        //        NSLog(@"%@ => %@", ifnam, info);
        if (info && [info count]) { break; }
    }
    //    NSLog(@"========%@",info);
    NSDictionary *dictInfo = (NSDictionary *)info;
    return [NSString stringWithFormat:@"%@",dictInfo[@"BSSID"]];
}


#pragma mark -  十六制字符串与NSData的相互转化
- (NSData *)convertHexStrToData:(NSString *)str {
    if (!str || [str length] == 0) {
        return nil;
    }
    
    NSMutableData *hexData = [[NSMutableData alloc] initWithCapacity:8];
    NSRange range;
    if ([str length] % 2 == 0) {
        range = NSMakeRange(0, 2);
    } else {
        range = NSMakeRange(0, 1);
    }
    for (NSInteger i = range.location; i < [str length]; i += 2) {
        unsigned int anInt;
        NSString *hexCharStr = [str substringWithRange:range];
        NSScanner *scanner = [[NSScanner alloc] initWithString:hexCharStr];
        
        [scanner scanHexInt:&anInt];
        NSData *entity = [[NSData alloc] initWithBytes:&anInt length:1];
        [hexData appendData:entity];
        
        range.location += range.length;
        range.length = 2;
    }
    
//    NSLog(@"hexdata: %@", hexData);
    return hexData;
}

- (NSString *)convertDataToHexStr:(NSData *)data {
    if (!data || [data length] == 0) {
        return @"";
    }
    NSMutableString *string = [[NSMutableString alloc] initWithCapacity:[data length]];
    
    [data enumerateByteRangesUsingBlock:^(const void *bytes, NSRange byteRange, BOOL *stop) {
        unsigned char *dataBytes = (unsigned char*)bytes;
        for (NSInteger i = 0; i < byteRange.length; i++) {
            NSString *hexStr = [NSString stringWithFormat:@"%x", (dataBytes[i]) & 0xff];
            if ([hexStr length] == 2) {
                [string appendString:hexStr];
            } else {
                [string appendFormat:@"0%@", hexStr];
            }
        }
    }];
    
    return string;
}

#pragma mark - 十进制转16进制 后为两个字节
- (NSString *)converIntToHex:(int)iTemp
{
    return [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%02x",iTemp]];
}

//-(int)doHandleGetDecimalByHexadecimal:(NSString*)tmpid
//{
//    
//    int int_ch;
//    
//    
//    unichar hex_char1 = [tmpid
//                         characterAtIndex:0];
//    ////两位16进制数中的第一位(高位*16)
//    
//    int int_ch1;
//    
//    if(hex_char1 >= '0'&& hex_char1 <='9')
//        int_ch1 = (hex_char1-48)*16;
//    //// 0 的Ascll - 48
//    
//    else if(hex_char1 >=
//            'A'&& hex_char1 <='F')
//        int_ch1 = (hex_char1-55)*16;
//    //// A 的Ascll - 65
//    
//    else
//        int_ch1 = (hex_char1-87)*16;
//    //// a 的Ascll - 97
//    
//    
//    
//    unichar hex_char2 = [tmpid
//                         characterAtIndex:1];
//    ///两位16进制数中的第二位(低位)
//    
//    int int_ch2;
//    
//    if(hex_char2 >= '0'&& hex_char2 <='9')
//        int_ch2 = (hex_char2-48);
//    //// 0 的Ascll - 48
//    
//    else if(hex_char1 >=
//            'A'&& hex_char1 <='F')
//        int_ch2 = hex_char2-55;
//    //// A 的Ascll - 65
//    
//    else
//        int_ch2 = hex_char2-87;
//    //// a 的Ascll - 97
//    
//    int_ch = int_ch1+int_ch2;
//    
//    NSLog(@"int_ch=%d",int_ch);
//    
//    return int_ch;
//}

- (NSInteger)numberWithHexString:(NSString *)hexString{
    
    const char *hexChar = [hexString cStringUsingEncoding:NSUTF8StringEncoding];
    
    int hexNumber;
    
    sscanf(hexChar, "%x", &hexNumber);
    
    return (NSInteger)hexNumber;
}

- (NSString *)doMakeUpperCaseAndAddSpace:(NSString*)originalString {
    if (originalString.length == 0) {
        return @"";
    }
    NSString *strBig = [originalString uppercaseString];
    NSMutableString *resultString = [NSMutableString string];
    for(int i = 0; i<[strBig length]/2; i++)
    {
        NSUInteger fromIndex = i * 2;
        NSUInteger len = [strBig length] - fromIndex;
        if (len > 2) {
            len = 2;
        }
        
        [resultString appendFormat:@"%@ ",[strBig substringWithRange:NSMakeRange(fromIndex, len)]];
    }
    NSString *strEnd = [resultString substringToIndex:resultString.length - 1];
    
    return strEnd;
}

- (NSString *)doMakeLowerCaseAndRemoveSpace:(NSString *)strOriginal
{
    NSString *strBig = [strOriginal lowercaseString];
    NSString *strHex = [strBig stringByReplacingOccurrencesOfString:@" " withString:@""];
    NSString *strLower = [strHex lowercaseString];
    return strLower;
}


//将十六进制的字符串转换成NSString则可使用如下方式:
- (NSString *)convertHexStrToString:(NSString *)str {
    if (!str || [str length] == 0) {
        return nil;
    }
    
    NSMutableData *hexData = [[NSMutableData alloc] initWithCapacity:8];
    NSRange range;
    if ([str length] % 2 == 0) {
        range = NSMakeRange(0, 2);
    } else {
        range = NSMakeRange(0, 1);
    }
    for (NSInteger i = range.location; i < [str length]; i += 2) {
        unsigned int anInt;
        NSString *hexCharStr = [str substringWithRange:range];
        NSScanner *scanner = [[NSScanner alloc] initWithString:hexCharStr];
        
        [scanner scanHexInt:&anInt];
        NSData *entity = [[NSData alloc] initWithBytes:&anInt length:1];
        [hexData appendData:entity];
        
        range.location += range.length;
        range.length = 2;
    }
    NSString *string = [[NSString alloc]initWithData:hexData encoding:NSUTF8StringEncoding];
    return string;
}


//将NSString转换成十六进制的字符串则可使用如下方式:
- (NSString *)convertStringToHexStr:(NSString *)str {
    if (!str || [str length] == 0) {
        return @"";
    }
    NSData *data = [str dataUsingEncoding:NSUTF8StringEncoding];
    
    NSMutableString *string = [[NSMutableString alloc] initWithCapacity:[data length]];
    
    [data enumerateByteRangesUsingBlock:^(const void *bytes, NSRange byteRange, BOOL *stop) {
        unsigned char *dataBytes = (unsigned char*)bytes;
        for (NSInteger i = 0; i < byteRange.length; i++) {
            NSString *hexStr = [NSString stringWithFormat:@"%x", (dataBytes[i]) & 0xff];
            if ([hexStr length] == 2) {
                [string appendString:hexStr];
            } else {
                [string appendFormat:@"0%@", hexStr];
            }
        }
    }];
    
    return string;
}


- (NSString *)hexStringFromString:(NSString *)string{
    NSData *myD = [string dataUsingEncoding:NSUTF8StringEncoding];
    Byte *bytes = (Byte *)[myD bytes];
    //下面是Byte 转换为16进制。
    NSString *hexStr=@"";
    for(int i=0;i<[myD length];i++)
    {
        NSString *newHexStr = [NSString stringWithFormat:@"%x",bytes[i]&0xff];///16进制数
        if([newHexStr length]==1)
            hexStr = [NSString stringWithFormat:@"%@0%@",hexStr,newHexStr];
        else
            hexStr = [NSString stringWithFormat:@"%@%@",hexStr,newHexStr];
    }
    
    NSMutableString *mutStr = [NSMutableString stringWithString:hexStr];
    if (hexStr.length < 46) {
        for (int i = 0; i < 46 - hexStr.length; i ++) {
            [mutStr appendString:@"0"];
        }
    }
    return mutStr;
}



@end
