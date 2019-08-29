//
//  GetCityLocationInfo.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/10.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "LocationInfoManager.h"
#import <CoreLocation/CoreLocation.h>
@interface LocationInfoManager ()<CLLocationManagerDelegate>


@property (nonatomic ,strong)CLLocationManager *locationManager;
// 地理位置解码编码器
@property (nonatomic ,strong) CLGeocoder *geo;
@end


@implementation LocationInfoManager

+ (id)shareInstance
{
    static LocationInfoManager *locationManager;
    static dispatch_once_t oneceToken;
    dispatch_once(&oneceToken, ^{
        locationManager = [[LocationInfoManager alloc] init];
    });
    
    return locationManager;
}

- (id)init
{
    self = [super init];
    if (self) {
        //初始化定位服务
        self.locationManager = [[CLLocationManager alloc] init];
        
        //设置定位的精确度和更新频率
        self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
        self.locationManager.distanceFilter = 10.f;
        
        self.locationManager.delegate = self;
        
        //授权状态是没有做过选择
        if ([CLLocationManager authorizationStatus] == kCLAuthorizationStatusNotDetermined) {
            [self.locationManager requestAlwaysAuthorization];
        }
    }
    return self;
}

- (void)doStartLocation
{
    //定位服务开启
    if ([CLLocationManager locationServicesEnabled]) {
        [self.locationManager startUpdatingLocation];
    }
}

-(void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray<CLLocation *> *)locations{
    
    // 1.既然已经定位到了用户当前的经纬度了,那么可以让定位管理器 停止定位了
    [self.locationManager stopUpdatingLocation];
    // 2.然后,取出第一个位置,根据其经纬度,通过CLGeocoder反向解析,获得该位置所在的城市名称,转成城市对象,用工具保存
    CLLocation *loc = [locations firstObject];
    // 3.CLGeocoder反向通过经纬度,获得城市名
    // 获取当前所在的城市名
    CLGeocoder *geocoder = [[CLGeocoder alloc] init];
    //根据经纬度反向地理编译出地址信息
    [geocoder reverseGeocodeLocation:loc completionHandler:^(NSArray *array, NSError *error){
        if (array.count > 0){
            CLPlacemark *placemark = [array objectAtIndex:0];
            //将获得的所有信息显示到label上
            NSString *str = [NSString stringWithFormat:@"%@-%@",placemark.administrativeArea,placemark.locality];
            NSLog(@"--name--%@",placemark.name);
            NSLog(@"--locality--%@",placemark.locality);
            
            NSLog(@"--administrativeArea--%@",placemark.administrativeArea);
            self.strCityName = str;
            NSLog(@"----%@",str);
            NSLog(@"--country--%@",placemark.country);
            
            //            //获取城市
            //            NSString *city = placemark.locality;
            //            if (!city) {
            //                //四大直辖市的城市信息无法通过locality获得，只能通过获取省份的方法来获得（如果city为空，则可知为直辖市）
            //                city = placemark.administrativeArea;
            //            }
            //            NSLog(@"city = %@", city);
            //            self.lable.text = city;
            //
            //        }
            //        else if (error == nil && [array count] == 0)
            //        {
            //            NSLog(@"No results were returned.");
            //        }
            //        else if (error != nil)
            //        {
            //            NSLog(@"An error occurred = %@", error);
        }
    }];
    //系统会一直更新数据，直到选择停止更新，因为我们只需要获得一次经纬度即可，所以获取之后就停止更新
    //  [manager stopUpdatingLocation];
}

//定位失败的方法
-(void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error{
    NSLog(@"=====%@", error);
}

//授权状态改变的方法
-(void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status{
    NSLog(@"---%d", status);
}

@end
