//
//  ScreenEditPropertyModel.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/31.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>



@interface ScreenEditPropertyModel : NSObject

@property (strong, nonatomic) NSString *strScreeenEditIconUrl;
@property (strong, nonatomic) NSString *strScreenEditName;
@property (strong, nonatomic) NSString *strScreenIsLinkage;
@property (strong, nonatomic) NSString *strScreenEditDelayed;
@property (strong, nonatomic) NSString *strScreenEditTimer;
@property (strong, nonatomic) NSString *strScreenEditArming;
@property (strong, nonatomic) NSString *strScreenEditDisarming;

//是否延时 1 是 0 否
@property (assign, nonatomic) int iScreen_need_time_delay;
//是否定时 1 是 0 否
@property (assign, nonatomic) int iScreen_need_timing;


@property (strong, nonatomic) NSString *str_linkage_time;

@end
