//
//  NSData+HLData.m
//  JiZhiSDK
//
//  Created by 王振 DemoKing on 2016/11/30.
//  Copyright © 2016年 DemoKing. All rights reserved.
//

#import "NSData+HLData.h"
#import <sys/socket.h>
#import <netinet/in.h>
#include <arpa/inet.h>
@implementation NSData (HLData)
/**
 截取data长度
 
 @param location 位置
 @param length 长度
 @return data
 */
- (NSData *)subDataLocation:(int)location length:(int)length {
    if (location + length > self.length) {
        return nil;
    }else {
        return [self subdataWithRange:NSMakeRange(location, length)];
    }
}

- (NSString *)host
{
    struct sockaddr *addr = (struct sockaddr *)[self bytes];
    if(addr->sa_family == AF_INET) {
        char *address = inet_ntoa(((struct sockaddr_in *)addr)->sin_addr);
        if (address)
            return [NSString stringWithCString: address encoding: NSASCIIStringEncoding];
    }
    else if(addr->sa_family == AF_INET6) {
        struct sockaddr_in6 *addr6 = (struct sockaddr_in6 *)addr;
        char straddr[INET6_ADDRSTRLEN];
        inet_ntop(AF_INET6, &(addr6->sin6_addr), straddr,
                  sizeof(straddr));
        return [NSString stringWithCString: straddr encoding: NSASCIIStringEncoding];
    }
    return nil;
}
@end
