//
//  FirstPageHeaderCR.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/18.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface FirstPageHeaderCR : UICollectionReusableView

@property (strong, nonatomic) NSString *strGreetings;

@property (strong, nonatomic) NSString *strImageWeather;

@property (strong, nonatomic) NSString *strWeatherStatus;

@property (strong, nonatomic) NSString *strTemperature;

@property (strong, nonatomic) NSString *strHumidity;

@property (strong, nonatomic) NSArray *arrHistory;

@property (copy, nonatomic) void(^BlockHistroyPressed)(void);

@end
