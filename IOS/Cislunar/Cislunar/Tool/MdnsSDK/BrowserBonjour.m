//
//  BrowserBonjour.m
//  MiCO
//
//  Created by zfw on 15/6/5.
//  Copyright (c) 2015年 mxchip. All rights reserved.
//

#import "BrowserBonjour.h"

@interface BrowserBonjour ()
{
    int g_count;
}
@property(strong,nonatomic)NSNetServiceBrowser   *m_netServiceBrowser;
@property(strong,nonatomic)NSMutableArray        *m_services;

//循环5秒查数据
@property(strong,nonatomic)NSTimer                 *m_loopTimer;
@property(strong,nonatomic)NSString                *m_serviceType;
@property(strong,nonatomic)NSString                *m_domain;

@property (assign, nonatomic) int iRememberSearchTimes;

@end

@implementation BrowserBonjour
@synthesize m_netServiceBrowser,m_services;
@synthesize m_loopTimer,m_domain,m_serviceType;

//重写init，加入一点初始化条件
- (instancetype)init;
{
    if(self=[super init])
    {
        m_services=[[NSMutableArray alloc]init];
        g_count=0;
    }
    return self;
}


-(void)getMdns:(NSString*)serviceType  andDomain:(NSString*)domain searchTimes:(int)iSearchTimes
{
    //init
    self.iRememberSearchTimes = iSearchTimes + 1;
    m_serviceType=serviceType;
    m_domain=domain;
    g_count=0;
    
    //进来就进行一次网关的搜索
    [self doFirstStartSearchGateway];
    
    if(m_loopTimer)
    {
        [m_loopTimer invalidate];
        m_loopTimer=nil;
    }
    m_loopTimer=[NSTimer scheduledTimerWithTimeInterval:LOOPTIMER
                                                 target:self
                                               selector:@selector(doRestartSearchGateway)
                                               userInfo:nil
                                                repeats:YES];
}

- (void)doFirstStartSearchGateway
{
    g_count++;
    m_services=nil;
    m_services=[[NSMutableArray alloc]init];
    self.m_netServiceBrowser=nil;
    self.m_netServiceBrowser = [[NSNetServiceBrowser alloc] init];
    self.m_netServiceBrowser.delegate=self;
    [self.m_netServiceBrowser searchForServicesOfType:m_serviceType inDomain:m_domain];//此方法不会阻塞
}

- (void)doRestartSearchGateway
{
    g_count++;
    if (g_count >= self.iRememberSearchTimes) {
        g_count = 0;
        self.iRememberSearchTimes = 0;
        [self stopSearchObject];
        [self.delegate returnError:@"没有搜到网关"];
    }else{
        [self.m_netServiceBrowser stop];
        m_services=nil;
        m_services=[[NSMutableArray alloc]init];
        self.m_netServiceBrowser=nil;
        self.m_netServiceBrowser = [[NSNetServiceBrowser alloc] init];
        self.m_netServiceBrowser.delegate=self;
        [self.m_netServiceBrowser searchForServicesOfType:m_serviceType inDomain:m_domain];//此方法不会阻塞
    }
}

-(void)stopSearchObject
{
    if(m_loopTimer)
    {
        [m_loopTimer invalidate];
        m_loopTimer=nil;
    }
    self.iRememberSearchTimes = 0;
    [self.m_netServiceBrowser stop];
}


-(void)stopMdns
{
    if(m_loopTimer)
    {
        [m_loopTimer invalidate];
        m_loopTimer=nil;
    }
    self.iRememberSearchTimes = 0;
    [self.m_netServiceBrowser stop];
    self.m_netServiceBrowser=nil;
    m_services=nil;
}

#pragma mark - NSNetServiceBrowserDelegate
- (void)netServiceBrowser:(NSNetServiceBrowser *)aNetServiceBrowser didNotSearch:(NSDictionary *)errorDict
{
    NSLog(@"error");
}

// New service was found
- (void)netServiceBrowser:(NSNetServiceBrowser *)aNetServiceBrowser didFindService:(NSNetService *)service moreComing:(BOOL)moreComing;
{
     NSMutableDictionary *moduleService=[[NSMutableDictionary alloc]init];
    service.delegate=self;
    // NSLog(@"count=%ld", service.addresses.count);
    [moduleService setObject:[service name] forKey:@"Name"];
    [moduleService setObject:service forKey:@"BonjourService"];
    [moduleService setObject:@YES forKey:@"resolving"];
    [service startMonitoring];
    
    [m_services addObject:moduleService];
    [service resolveWithTimeout:5.0];
    
    // If more entries are coming, no need to update UI just yet
    if ( !moreComing ) {
        //[self.m_netServiceBrowser stop];
        //self.m_netServiceBrowser=nil;
    }
}

#pragma mark - NSNetServiceDelegate

- (void)netService:(NSNetService *)sender didNotResolve:(NSDictionary *)errorDict
{
//    NSLog(@"didNotResolve");
}

- (void)netServiceDidStop:(NSNetService *)sender
{
//    NSLog(@"netServiceDidStop");
}


- (void)netServiceDidResolveAddress:(NSNetService *)service
{
    NSLog(@"netServiceDidResolveAddress");
    [service stop];//这行很重要
    if(g_count==self.iRememberSearchTimes - 1)
    {
        [self stopSearchObject];
        [self.delegate returnMndsData:m_services];
    }
}

- (void)netService:(NSNetService *)service didUpdateTXTRecordData:(NSData *)data
{
//    NSLog(@"didUpdateTXTRecordData");
}

@end
