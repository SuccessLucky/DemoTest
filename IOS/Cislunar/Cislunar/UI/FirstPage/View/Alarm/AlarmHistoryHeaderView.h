//
//  AlarmHistoryHeaderView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/11.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AlarmHistoryHeaderView : UIView

@property (strong, nonatomic) NSString *strTime;
@property (strong, nonatomic) NSArray *arrList;
@property (copy, nonatomic) void(^BlockDateSelected)(NSDate *date);
@property (copy, nonatomic) void(^BlockMoreDateSelected)();


- (id)initWithFrame:(CGRect)frame;

@end
