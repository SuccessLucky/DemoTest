//
//  AppDelegate+Camera.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/14.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "AppDelegate+Camera.h"

@implementation AppDelegate (Camera)


- (void)doInitEZOpenSDK
{
    [EZOPENSDK setDebugLogEnable:YES];
    
    [EZOPENSDK initLibWithAppKey:EZOpenSDK_AppKey];
    
    [EZOpenSDK setAccessToken:@"at.c96gtsqjdbv57jj233nf67wf7me395u5-4cigwxll7p-0e29sg7-zfh4dfaez"];
    
    [EZHCNetDeviceSDK initSDK];
    
    [EZOPENSDK enableP2P:YES];
    
    NSLog(@"EZOpenSDK Version = %@", [EZOPENSDK getVersion]);
}

- (void)doLogOutEZOpenSDK
{
    [EZOPENSDK logout:^(NSError *error) {
    }];
}










- (void)doGetEZOpenSDKBasicValue
    {
//        https://open.ys7.com/api/lapp/token/get
        
        NSMutableDictionary *params = [NSMutableDictionary dictionary];
        params[@"appKey"] = @"e3cd95f361cc4f3ea2965a5c0007e411";
        params[@"appSecret"] = @"e1799ff4f5972f40fb7216f1bbf19ecf";
        
        [[HTTPNetworkEngine sharedInstance] postWithURLString:@"https://open.ys7.com/api/lapp/token/get"
                                                   parameters:params
                                                      success:^(id responseObject) {
            
            
                                                          LLog([NSString stringWithFormat:@"%@",params]);
//                                                          NSDictionary *jsonDict = (NSDictionary *)responseObject;
//                                                          if ([jsonDict[@"code"] intValue] == 200) {
//
//                                                              NSDictionary *dictData = jsonDict[@"data"];
//                                                              LLog([NSString stringWithFormat:@"accessToken :%@,\nexpireTime:%@",dictData[@"accessToken"],dictData[@"nexpireTime"]]);
//
//                                                          }
            
        } failure:^(NSError *error) {
            
        }];
    }
    
@end
