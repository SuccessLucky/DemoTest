//
//  BonjourManager.m
//  JiZhi
//
//  Created by 王宗成 on 17/7/10.
//  Copyright © 2017年 DemoKing. All rights reserved.
//

#import "BonjourManager.h"
#import "HLDataUtil.h"

bool newModuleFound;
bool enumerating = NO;
@implementation BonjourManager

+ (id)shareInstance
{
    static BonjourManager *bonjourmanager;
    static dispatch_once_t oneceToken;
    dispatch_once(&oneceToken, ^{
        bonjourmanager = [[BonjourManager alloc] init];
    });
    
    return bonjourmanager;
}

-(void)startSearchDevice
{
    [self.brower stop];
    
    [self.brower searchForServicesOfType:@"_easylink._tcp" inDomain:@"local."];
}

-(void)startSearchDeviceWithOutTime:(NSInteger)outTime callBackTimeOutBlock:(BonjourTimeOutBlock)block
{
    if (_timeOutBlock) {
        
        _timeOutBlock = nil;
    }
    _timeOutBlock = block;
    
    [self startSearchDevice];
    
   [self performSelector:@selector(timeOutStopSearchDevice) withObject:nil afterDelay:outTime];
}

-(void)timeOutStopSearchDevice
{
    [self.brower stop];
    
    if (_timeOutBlock) {
        _timeOutBlock();
    }
}

-(void)restartSearchService
{
    enumerating = NO;
    newModuleFound = NO;
    [_displayServices removeAllObjects];
    [_services removeAllObjects];
    [self.brower stop];
    _brower = nil;
    
    [self startSearchDevice];
}

-(NSNetServiceBrowser *)brower
{
    if (_brower==nil) {
        
        NSNetServiceBrowser *aNetServiceBrowser = [[NSNetServiceBrowser alloc] init];
   
        aNetServiceBrowser.delegate = self;
        _brower = aNetServiceBrowser;
    }
    
    return _brower;
}
//
-(NSMutableArray *)services
{
    if (_services == nil) {
        
        _services = [NSMutableArray array];
    }
    return _services;
}

-(NSMutableArray *)displayServices
{
    if (_displayServices == nil) {
        
        _displayServices = [NSMutableArray array];
    }
    return _displayServices;
}

-(void)stopSearchDevice
{
    [self.brower stop];
}

#pragma mark - Bonjour代理
- (void)netServiceBrowserWillSearch:(NSNetServiceBrowser *)browser
{

}

/* Sent to the NSNetServiceBrowser instance's delegate when the instance's previous running search request has stopped.
 */
- (void)netServiceBrowserDidStopSearch:(NSNetServiceBrowser *)browser
{
    
}

/* Sent to the NSNetServiceBrowser instance's delegate when an error in searching for domains or services has occurred. The error dictionary will contain two key/value pairs representing the error domain and code (see the NSNetServicesError enumeration above for error code constants). It is possible for an error to occur after a search has been started successfully.
 */
- (void)netServiceBrowser:(NSNetServiceBrowser *)browser didNotSearch:(NSDictionary<NSString *, NSNumber *> *)errorDict
{
    
}

/* Sent to the NSNetServiceBrowser instance's delegate for each domain discovered. If there are more domains, moreComing will be YES. If for some reason handling discovered domains requires significant processing, accumulating domains until moreComing is NO and then doing the processing in bulk fashion may be desirable.
 */
- (void)netServiceBrowser:(NSNetServiceBrowser *)browser didFindDomain:(NSString *)domainString moreComing:(BOOL)moreComing
{
    
}

/* Sent to the NSNetServiceBrowser instance's delegate for each service discovered. If there are more services, moreComing will be YES. If for some reason handling discovered services requires significant processing, accumulating services until moreComing is NO and then doing the processing in bulk fashion may be desirable.
 */
- (void)netServiceBrowser:(NSNetServiceBrowser *)browser didFindService:(NSNetService *)service moreComing:(BOOL)moreComing
{
    NSMutableDictionary *moduleService = [[NSMutableDictionary alloc] initWithCapacity:15];
    [moduleService setObject:[service name] forKey:@"Name"];
    [moduleService setObject:service forKey:@"BonjourService"];
    [moduleService setObject:@YES forKey:@"resolving"];
    
    service.delegate = self;
    [service startMonitoring];
    
    
    enumerating = YES;
    for (NSMutableDictionary *object in self.services)
    {
        if([[object objectForKey:@"Name"] isEqual:[service name]]){
            enumerating = NO;
            return;
        }
    }
    enumerating = NO;
    
    newModuleFound = YES;
    [self.services addObject:moduleService];
    [service resolveWithTimeout:0.0];
}

-(void)netServiceDidResolveAddress:(NSNetService *)sender{
    
    NSData *ipstr = nil;
    NSData* data = [sender TXTRecordData];
    
    NSDictionary* dict = [NSNetService dictionaryFromTXTRecordData:data];
//    NSLog(@"%@", dict);
//
//    NSLog(@"***********************==name=%@,type:%@,domain=%@,hostname=%@,address= %@,port=%ld",sender.name,sender.type,sender.domain,sender.hostName,sender.addresses,(long)sender.port);
    
    NSDictionary *_txtRecord = [NSNetService dictionaryFromTXTRecordData: [sender TXTRecordData]];
    NSMutableArray *_txtRecordArray = [NSMutableArray arrayWithCapacity:20];

    [_txtRecord enumerateKeysAndObjectsUsingBlock:^(id key, id obj, BOOL *stop) {
        NSString *content = [[NSString alloc]initWithData:obj encoding:NSUTF8StringEncoding];
        NSDictionary *dict = [NSDictionary dictionaryWithObject:content forKey:key];
        
        [_txtRecordArray addObject: dict];
    }];
    
    
    
    NSData *str = [dict objectForKey:@"MAC"];
    NSData *socket1Type = [dict objectForKey:@"Socket1_Type"];
    NSData *socket2Type = [dict objectForKey:@"Socket2_Type"];
    
    NSData *port1 = [dict objectForKey:@"Socket1_Port"];
    NSData *port2 = [dict objectForKey:@"Socket2_Port"];
    
    NSData *hardware = [dict objectForKey:@"Hardware"];
    
    NSString *strName = sender.name;
    NSString *oldmacadress = [[NSString alloc]initWithData:str encoding:NSUTF8StringEncoding];
    NSString *strSock1Type = [[NSString alloc] initWithData:socket1Type encoding:NSUTF8StringEncoding];
     NSString *strSock2Type = [[NSString alloc] initWithData:socket2Type encoding:NSUTF8StringEncoding];
    NSString *port1Str = [[NSString alloc] initWithData:port1 encoding:NSUTF8StringEncoding];
    NSString *port2Str = [[NSString alloc] initWithData:port2 encoding:NSUTF8StringEncoding];
    NSString *macadress = [oldmacadress stringByReplacingOccurrencesOfString:@":" withString:@""];
    
    NSString *strHardWare = [[NSString alloc] initWithData:hardware encoding:NSUTF8StringEncoding];
    
    if (macadress.length < 8) {
        return;
    }
    if([[sender addresses] count])
        ipstr = [[sender addresses] objectAtIndex:0];
    
    NSString *strPortOther = [NSString stringWithFormat:@"%@",[NSNumber numberWithInteger:[sender port]]];
    NSLog(@"dddddddddd=%@",strPortOther);
    
    NSString *detailString = [[NSString alloc] initWithFormat:
                              @"MAC: %@\nIP :%@",
                              macadress,
                              (ipstr!=nil)? [ipstr host]:@"Unknow"];
    NSLog(@"%@", [NSString stringWithFormat:@"名字:%@\n%@\nSocket1_Type:%@\n Socket1_Port:%@\nSocket2_Type:%@\nSocket2_Port:%@,portOther:%@,\nHardware:%@",strName,detailString, strSock1Type,port1Str, strSock2Type,port2Str,strPortOther,strHardWare]);
    
    SHModelGateway *gatewayModel = [SHModelGateway new];
    gatewayModel.strGateway_gateway_name = strName;
    
    gatewayModel.strGateway_wifi_mac_address = macadress;
    
    gatewayModel.strGatewayIp = [ipstr host];
    if (port1Str.length) {
        gatewayModel.strGatewayPort = port1Str;
    }else{
        gatewayModel.strGatewayPort = @"8686";
    }
    
    gatewayModel.strHardware = strHardWare;
    
    self.gwModel = gatewayModel;
    
    
    
    __block BOOL isExist = NO;
    [self.mutArrInfo enumerateObjectsUsingBlock:^(SHModelGateway * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([obj.strGateway_wifi_mac_address isEqualToString:gatewayModel.strGateway_wifi_mac_address]) {//数组中已经存在该对象
            *stop = YES;
            isExist = YES;
        }
    }];
    if (!isExist) {//如果不存在就添加进去
        [self.mutArrInfo addObject:gatewayModel];
    }
    self.arrGatewayInfoGet = self.mutArrInfo;
}

-(NSMutableArray *)dataArray
{
    if (_dataArray == nil) {
        
        _dataArray = [NSMutableArray array];
    }
    
    return _dataArray;
}
/*
 [self.dataArray addObject:gateway];
 HLAsyncSocket *socket = [[HLAsyncSocket alloc] initWithDelegate:self delegateQueue:dispatch_get_main_queue()];
 [socket connectToHost:gateway.IPString onPort:SOCKET_PORT error:nil];
 [self.socketArray addObject:socket];
 */

- (void)netService:(NSNetService *)service didUpdateTXTRecordData:(NSData *)data
{
    NSDictionary *oldTxtData, *newTxtData;
    NSData *oldSeed, *newSeed;
    
    newTxtData = [NSNetService dictionaryFromTXTRecordData:data];
    newSeed = [newTxtData objectForKey:@"Seed"];
    
    enumerating = YES;
    for (NSMutableDictionary *object in self.displayServices)
    {
        if([object objectForKey:@"BonjourService"] == service){
            
//            NSString *strName = [[object objectForKey:@"BonjourService"] name];
            
            oldTxtData = [NSNetService dictionaryFromTXTRecordData:[service TXTRecordData]];
            oldSeed = [oldTxtData objectForKey:@"Seed"];
            if(oldSeed==nil)
                return;
            
            if([oldSeed isEqualToData:newSeed]== NO)
                newModuleFound = YES;
            break;
        }
    }
    enumerating = NO;
}
/* Sent to the NSNetServiceBrowser instance's delegate when a previously discovered domain is no longer available.
 */
- (void)netServiceBrowser:(NSNetServiceBrowser *)browser didRemoveDomain:(NSString *)domainString moreComing:(BOOL)moreComing{
    NSLog(@"1********1");
}

/* Sent to the NSNetServiceBrowser instance's delegate when a previously discovered service is no longer published.
 */
//服务不可用的时候会 会调用此方法
- (void)netServiceBrowser:(NSNetServiceBrowser *)browser didRemoveService:(NSNetService *)service moreComing:(BOOL)moreComing{
    NSLog(@"2********2");
}

- (NSMutableArray *)mutArrInfo
{
    if (!_mutArrInfo) {
        _mutArrInfo = [[NSMutableArray alloc] initWithCapacity:0];
    }
    return _mutArrInfo;
}

- (NSArray *)arrGatewayInfoGet
{
    if (!_arrGatewayInfoGet) {
        _arrGatewayInfoGet = [NSArray new];
    }
    return _arrGatewayInfoGet;
}


#pragma mark - private

#pragma mark - 获取通讯Ip
-(NSString*)getdevIP:(NSArray*)array andObject:(id)object
{
    if(array.count==0)
    {
        return nil;
    }
    NSNetService *service=object[@"BonjourService"];
    NSData *ipAddress;
    if(service.addresses.count)
    {
        ipAddress = [service.addresses objectAtIndex:0];
    }
    NSString *deviceIP=[ipAddress host];
    return deviceIP;
}

#pragma mark - 通讯Port
- (NSString *)getPort:(NSArray *)array andObject:(id)object
{
    if(array.count==0)
    {
        return nil;
    }
    NSNetService *service=object[@"BonjourService"];
    NSDictionary *dictAll = [NSNetService dictionaryFromTXTRecordData:[service TXTRecordData]];
    NSData *dataSocket1_Port = dictAll[@"Socket1_Port"];
    NSString *strSocket1_Port = [[NSString alloc] initWithData: dataSocket1_Port encoding:NSASCIIStringEncoding];
    return strSocket1_Port;
}

#pragma mark - 获取其Name
//方法一
 - (NSString *)doGetGatewayNameWith:(NSString *)strOriginName
{
    NSString *ModuleName=nil;
    NSRange range = [strOriginName rangeOfCharacterFromSet:[NSCharacterSet characterSetWithCharactersInString:@"#"]
                                          options:NSBackwardsSearch];
    if(range.location == NSNotFound)
        range.length = [strOriginName length];
    else
        range.length = range.location;
    range.location = 0;
    ModuleName = [strOriginName substringWithRange:range];
    return ModuleName;
}
-(NSString*)getdevModuleName:(NSArray*)array andObject:(id)object
{
    if(array.count==0)
    {
        return nil;
    }
    /*显示模块名字*/
    NSString *Name = object[@"Name"];
    NSString *ModuleName=nil;
    NSRange range = [Name rangeOfCharacterFromSet:[NSCharacterSet characterSetWithCharactersInString:@"#"]
                                          options:NSBackwardsSearch];
    if(range.location == NSNotFound)
        range.length = [Name length];
    else
        range.length = range.location;
    range.location = 0;
    ModuleName = [Name substringWithRange:range];
    return ModuleName;
}

#pragma mark - 获取WIFI-MAC地址
-(NSString*)getdevMAC:(NSArray*)array andObject:(id)object
{
    if(array.count==0)
    {
        return nil;
    }
    NSNetService *service=object[@"BonjourService"];
    NSString *macAddress=nil;
    NSData *mac = [[NSNetService dictionaryFromTXTRecordData:[service TXTRecordData]] objectForKey:@"MAC"];
    macAddress = [[NSString alloc] initWithData: mac encoding:NSASCIIStringEncoding];
    
    //    NSDictionary *dictAll = [NSNetService dictionaryFromTXTRecordData:[service TXTRecordData]];
    //
    //    NSData *dataMac = dictAll[@"MAC"];
    //    NSString *strMacAddr = [[NSString alloc] initWithData: dataMac encoding:NSASCIIStringEncoding];
    //
    //    NSData *dataSocket2_Type = dictAll[@"Socket2_Type"];
    //    NSString *strSocket2_Type = [[NSString alloc] initWithData: dataSocket2_Type encoding:NSASCIIStringEncoding];
    //
    //    NSData *dataSocket2_Port = dictAll[@"Socket2_Port"];
    //    NSString *strSocket2_Port = [[NSString alloc] initWithData: dataSocket2_Port encoding:NSASCIIStringEncoding];
    //
    //    NSData *dataSocket1_Type = dictAll[@"Socket1_Type"];
    //    NSString *strSocket1_Type = [[NSString alloc] initWithData: dataSocket1_Type encoding:NSASCIIStringEncoding];
    //
    //    NSData *dataSocket1_Port = dictAll[@"Socket1_Port"];
    //    NSString *strSocket1_Port = [[NSString alloc] initWithData: dataSocket1_Port encoding:NSASCIIStringEncoding];
    //
    //    NSData *dataSocket2_Raddr = dictAll[@"Socket2_Raddr"];
    //    NSString *strSocket2_Raddr = [[NSString alloc] initWithData: dataSocket2_Raddr encoding:NSASCIIStringEncoding];
    //
    //    NSData *dataMAC = dictAll[@"MAC"];
    //    NSString *strMAC = [[NSString alloc] initWithData: dataMAC encoding:NSASCIIStringEncoding];
    //
    //    NSData *dataSeed = dictAll[@"Seed"];
    //    NSString *strSeed = [[NSString alloc] initWithData: dataSeed encoding:NSASCIIStringEncoding];
    //
    //    NSData *dataVendor = dictAll[@"Vendor"];
    //    NSString *strVendor = [[NSString alloc] initWithData: dataVendor encoding:NSASCIIStringEncoding];
    //
    //    NSData *dataFirmware = dictAll[@"Firmware"];
    //    NSString *strFirmware = [[NSString alloc] initWithData: dataFirmware encoding:NSASCIIStringEncoding];
    //
    //    NSData *dataProtocol = dictAll[@"Protocol"];
    //    NSString *strProtocol = [[NSString alloc] initWithData: dataProtocol encoding:NSASCIIStringEncoding];
    
    
    return macAddress;
}



@end
