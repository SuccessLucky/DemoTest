//
//  EasyLinkManager.m
//  JiZhi
//
//  Created by 王宗成 on 17/7/11.
//  Copyright © 2017年 DemoKing. All rights reserved.
//

#import "EasyLinkManager.h"
#import <ifaddrs.h>
#import <arpa/inet.h>

#import <sys/socket.h>
#import <sys/sockio.h>
#import <sys/ioctl.h>
#import <net/if.h>

#import <CommonCrypto/CommonDigest.h>
@implementation EasyLinkManager

#pragma mark __PRI__EASYLINK__
- (void)prepareEasyLinker {
    
    NSString* ssid = [EASYLINK ssidForConnectedNetwork];
    if (!ssid.length) {
        
        return;
    }
    
    self.wifiSSID = ssid;
}

#pragma mark - 配置参数

- (void)  configureEasyLinker {
    NSMutableDictionary* configDic = [@{KEY_SSID: [self.wifiSSID dataUsingEncoding:NSUTF8StringEncoding],
                                         KEY_PASSWORD: self.wifiPSD,
                                         KEY_DHCP: @YES,
                                         } mutableCopy];
    if ([EASYLINK getIPAddress]) {
        
        configDic[KEY_IP] = [EASYLINK getIPAddress];
    }
    
    if ([EASYLINK getGatewayAddress]) {
        
        configDic[KEY_GATEWAY] = [EASYLINK getGatewayAddress];
        configDic[KEY_DNS1] = [EASYLINK getGatewayAddress];
    }
    
    if ([EASYLINK getNetMask]) {
        
        configDic[KEY_NETMASK] = [EASYLINK getNetMask];
    }
    
    NSLog(@"配置参数:%@",configDic);
    
//    NSString *userInfo = @"网关一号";
//    const char *temp = [userInfo cStringUsingEncoding:NSUTF8StringEncoding];
//    NSData *dataInfo = [NSData dataWithBytes:temp length:strlen(temp)];
    
    self.actKey = [self genActKey];
    
    NSData* attchData = [self genAttachDataWithKey:self.actKey];
    
    [_easyLinker prepareEasyLink:configDic info:attchData mode:EASYLINK_V2_PLUS];
}

- (void)runningEasyLinker {
    
    if (_easyLinker==nil) {
        
        _easyLinker = [[EASYLINK alloc]init];
        [self prepareEasyLinker];
        [self configureEasyLinker];
    }
    [_easyLinker transmitSettings];
    
}
- (void)stopEasyLinker {
    
    [_easyLinker stopTransmitting];
    [_easyLinker unInit];
    _easyLinker=nil;
}

- (NSString *)genActKey {
    // get time interv
    long timeInterv = (long)[NSDate.date timeIntervalSinceReferenceDate];
    NSMutableData* randMutData = [NSMutableData dataWithBytes:&timeInterv length:sizeof(timeInterv)];
    
    // get middle 16bytes md5 string
    return [[self md5String:randMutData] substringWithRange:NSMakeRange(16, 6)];
}

- (NSString *)md5String:(NSData *)data{
    
    unsigned char result[CC_MD5_DIGEST_LENGTH];
    CC_MD5(data.bytes, (CC_LONG)data.length, result);
    return [NSString stringWithFormat:
            @"%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x",
            result[0], result[1], result[2], result[3],
            result[4], result[5], result[6], result[7],
            result[8], result[9], result[10], result[11],
            result[12], result[13], result[14], result[15]
            ];
}

- (NSData *)genAttachDataWithKey:(NSString *)key {
    NSUInteger len = key.length;
    if (len != 6) return nil;
    
    NSMutableString* retAttachDataString = [[NSMutableString alloc] initWithString:@"diy"];
    [retAttachDataString appendString:key];
    
    NSMutableData* retMutData = [[NSMutableData alloc] initWithBytes:[retAttachDataString cStringUsingEncoding:NSASCIIStringEncoding]
                                                              length:retAttachDataString.length];
    return retMutData;
}


@end
