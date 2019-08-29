//
//  HLDataUtil.m
//  JiZhiSDK
//
//  Created by 王振 DemoKing on 2016/11/29.
//  Copyright © 2016年 DemoKing. All rights reserved.
//

#import "HLDataUtil.h"

@implementation HLDataUtil

// 十六进制字符串转换为普通字符串
+ (NSString *)stringFromHexString:(NSString *)hexString {
    char *myBuffer = (char *)malloc((int)[hexString length] / 2 + 1);
    bzero(myBuffer, [hexString length] / 2 + 1);
    for (int i = 0; i < [hexString length] - 1; i += 2) {
        unsigned int anInt;
        NSString * hexCharStr = [hexString substringWithRange:NSMakeRange(i, 2)];
        NSScanner * scanner = [[NSScanner alloc] initWithString:hexCharStr];
        [scanner scanHexInt:&anInt];
        myBuffer[i / 2] = (char)anInt;
    }
    NSString *utf8String = [NSString stringWithCString:myBuffer encoding:4];
    
    return utf8String;
}

+  (NSString *)getHexByDecimal:(NSDecimalNumber *)decimal {
    
    //10进制转换16进制（支持无穷大数）
    NSString *hex =@"";
    NSString *letter;
    NSDecimalNumber *lastNumber = decimal;
    for (int i = 0; i<999; i++) {
        NSDecimalNumber *tempShang = [lastNumber decimalNumberByDividingBy:[NSDecimalNumber decimalNumberWithString:@"16"]];
        NSString *tempShangString = [tempShang stringValue];
        if ([tempShangString containsString:@"."]) {
            // 有小数
            tempShangString = [tempShangString substringToIndex:[tempShangString rangeOfString:@"."].location];
            //            DLog(@"%@", tempShangString);
            NSDecimalNumber *number = [[NSDecimalNumber decimalNumberWithString:tempShangString] decimalNumberByMultiplyingBy:[NSDecimalNumber decimalNumberWithString:@"16"]];
            NSDecimalNumber *yushu = [lastNumber decimalNumberBySubtracting:number];
            int yushuInt = [[yushu stringValue] intValue];
            switch (yushuInt) {
                case 10:
                    letter =@"A"; break;
                case 11:
                    letter =@"B"; break;
                case 12:
                    letter =@"C"; break;
                case 13:
                    letter =@"D"; break;
                case 14:
                    letter =@"E"; break;
                case 15:
                    letter =@"F"; break;
                default:
                    letter = [NSString stringWithFormat:@"%d", yushuInt];
            }
            lastNumber = [NSDecimalNumber decimalNumberWithString:tempShangString];
        } else {
            // 没有小数
            if (tempShangString.length <= 2 && [tempShangString intValue] < 16) {
                int num = [tempShangString intValue];
                if (num == 0) {
                    break;
                }
                switch (num) {
                    case 10:
                        letter =@"A"; break;
                    case 11:
                        letter =@"B"; break;
                    case 12:
                        letter =@"C"; break;
                    case 13:
                        letter =@"D"; break;
                    case 14:
                        letter =@"E"; break;
                    case 15:
                        letter =@"F"; break;
                    default:
                        letter = [NSString stringWithFormat:@"%d", num];
                }
                hex = [letter stringByAppendingString:hex];
                break;
            } else {
                letter = @"0";
            }
            lastNumber = tempShang;
        }
        
        hex = [letter stringByAppendingString:hex];
    }
    //    return hex;
    return hex.length > 0 ? hex : @"0";
}

//+(NSString *)ToHex:(long long int)tmpid
//{
//    NSString *nLetterValue;
//    NSString *str =@"";
//    long long int ttmpig;
//    for (int i = 0; i<9; i++) {
//        ttmpig=tmpid%16;
//        tmpid=tmpid/16;
//        switch (ttmpig)
//        {
//            case 10:
//                nLetterValue =@"A";break;
//            case 11:
//                nLetterValue =@"B";break;
//            case 12:
//                nLetterValue =@"C";break;
//            case 13:
//                nLetterValue =@"D";break;
//            case 14:
//                nLetterValue =@"E";break;
//            case 15:
//                nLetterValue =@"F";break;
//            default:nLetterValue=[[NSString alloc]initWithFormat:@"%i",ttmpig];
//
//        }
//        str = [nLetterValue stringByAppendingString:str];
//        if (tmpid == 0) {
//            break;
//        }
//        if (str.length == 1) {
//            str = [NSString stringWithFormat:@"000%@", str];
//        }
//    }
//    return str;
//}


/**
 Data 转 字符串
 
 @param data data数据
 @return 翻译成的字符串
 */

+ (NSString *)stringWithData:(NSData *)data {
    if (!data) {
        return nil;
    }
    Byte *bytes = (Byte *)[data bytes];
    NSString *hexString = @"";
    for(int i=0;i<[data length];i++) {
        NSString *newHexStr = [NSString stringWithFormat:@"%x",bytes[i]&0xff];
        if([newHexStr length] == 1) {
            hexString = [NSString stringWithFormat:@"%@0%@",hexString,newHexStr];
        }else {
            hexString = [NSString stringWithFormat:@"%@%@",hexString,newHexStr];
        }
    }
    return hexString;
}

/**
 data 转 byte数组
 
 @param data data
 @return byte数组
 */
+ (Byte *)byteWithData:(NSData *)data {
    Byte *byte = (Byte *)[data bytes];
    return byte;
}

/**
 data截取
 
 @param data data
 @param location 截取位置
 @param length 截取长度
 @return data
 */
+ (NSData *)dataWithData:(NSData *)data location:(NSInteger)location length:(NSInteger)length {
    if (location + length > data.length) {
        return nil;
    }
    return [data subdataWithRange:NSMakeRange(location, length)];
}

/**
 data 通过 UTF_8 转字符串
 
 @param data data
 @return string
 */
+ (NSString *)UTF8StringWithData:(NSData *)data {
    return [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
}

/**
 16进制字符串 字节 转 data
 
 @param hexString hexString
 @return data
 */
+ (NSData *)dataWithHexString:(NSString *)hexString {
    if (!hexString || [hexString length] == 0) {
        return nil;
    }
    NSMutableData *hexData = [[NSMutableData alloc] initWithCapacity:8];
    NSRange range;
    if ([hexString length] % 2 == 0) {
        range = NSMakeRange(0,2);
    } else {
        range = NSMakeRange(0,1);
    }
    for (NSInteger i = range.location; i < [hexString length]; i += 2) {
        unsigned int anInt;
        NSString *hexCharStr = [hexString substringWithRange:range];
        NSScanner *scanner = [[NSScanner alloc] initWithString:hexCharStr];
        [scanner scanHexInt:&anInt];
        NSData *entity = [[NSData alloc] initWithBytes:&anInt length:1];
        [hexData appendData:entity];
        range.location += range.length;
        range.length = 2;
    }
    return hexData;
}
+ (int)valueWithSingleHexString:(NSString *)string {
    NSString *f = [string substringToIndex:1];
    NSString *l = [string substringFromIndex:1];
    if ([f isEqualToString:@"a"]) {
        f = @"10";
    }else if ([f isEqualToString:@"b"]) {
        f = @"11";
    }else if ([f isEqualToString:@"c"]) {
        f = @"12";
    }else if ([f isEqualToString:@"d"]) {
        f = @"13";
    }else if ([f isEqualToString:@"e"]) {
        f = @"14";
    }else if ([f isEqualToString:@"f"]) {
        f = @"15";
    }
    if ([l isEqualToString:@"a"]) {
        l = @"10";
    }else if ([l isEqualToString:@"b"]) {
        l = @"11";
    }else if ([l isEqualToString:@"c"]) {
        l = @"12";
    }else if ([l isEqualToString:@"d"]) {
        l = @"13";
    }else if ([l isEqualToString:@"e"]) {
        l = @"14";
    }else if ([l isEqualToString:@"f"]) {
        l = @"15";
    }
    
    int fv = [f intValue];
    int lv = [l intValue];
    return fv * pow(16, 1) + lv;
}
/**
 16进制字符串 转 int 值
 
 @param hexString 0faa 等
 @return int
 */
+ (int)valueWithHexString:(NSString *)hexString {
    if (!hexString) {
        return 0;
    }
    unsigned int value = 0;
    NSScanner *scanner = [NSScanner scannerWithString:hexString];
    //[scanner setScanLocation:1];
    [scanner scanHexInt:&value];
    return value;
}

- (NSInteger)numberWithHexString:(NSString *)hexString{
    
    const char *hexChar = [hexString cStringUsingEncoding:NSUTF8StringEncoding];
    
    int hexNumber;
    
    sscanf(hexChar, "%x", &hexNumber);
    
    return (NSInteger)hexNumber;
}

/**
 data 转 int
 
 @param data data
 @return int
 */
+ (int)valueWithData:(NSData *)data {
    NSString *hexString = [HLDataUtil stringWithData:data];
    return [HLDataUtil valueWithHexString:hexString];
}

//二进制转10进制
+ (NSInteger)getDecimalByBinary:(NSString *)binary {
    
    NSInteger decimal = 0;
    for (int i=0; i<binary.length; i++) {
        
        NSString *number = [binary substringWithRange:NSMakeRange(binary.length - i - 1, 1)];
        if ([number isEqualToString:@"1"]) {
            
            decimal += pow(2, i);
        }
    }
    return decimal;
}

// 十六进制转二进制
+(NSString *)getBinaryByhex:(NSString *)hex
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
    
    [hexDic setObject:@"1010" forKey:@"a"];
    [hexDic setObject:@"1011" forKey:@"b"];
    [hexDic setObject:@"1100" forKey:@"c"];
    [hexDic setObject:@"1101" forKey:@"d"];
    [hexDic setObject:@"1110" forKey:@"e"];
    [hexDic setObject:@"1111" forKey:@"f"];
    
    NSString *binaryString = @"";
    for (int i=0; i<[hex length]; i++) {
        NSRange rage;
        rage.length = 1;
        rage.location = i;
        NSString *key = [hex substringWithRange:rage];
        NSString *d = [NSString stringWithFormat:@"%@",[hexDic objectForKey:key]];
        binaryString = [NSString stringWithFormat:@"%@%@",binaryString,d];
    }
    
    return binaryString;
}


//将十六进制的字符串转换成NSString则可使用如下方式:
+ (NSString *)convertHexToString:(NSString *)str {
    
    if (!str || [str length] == 0) {
        return nil;
    }
    
    NSMutableData *hexData = [[NSMutableData alloc] init];
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

//+ (NSString *)getHexByDecimal:(NSInteger)decimal {
//    
//    NSString *hex =@"";
//    NSString *letter;
//    NSInteger number;
//    for (int i = 0; i<9; i++) {
//        
//        number = decimal % 16;
//        decimal = decimal / 16;
//        switch (number) {
//                
//            case 10:
//                letter =@"A"; break;
//            case 11:
//                letter =@"B"; break;
//            case 12:
//                letter =@"C"; break;
//            case 13:
//                letter =@"D"; break;
//            case 14:
//                letter =@"E"; break;
//            case 15:
//                letter =@"F"; break;
//            default:
//                letter = [NSString stringWithFormat:@"%ld", number];
//        }
//        hex = [letter stringByAppendingString:hex];
//        if (decimal == 0) {
//            
//            break;
//        }
//    }
//    return hex;
//}

@end
