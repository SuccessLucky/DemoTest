//
//  AlarmHistoryTableViewView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/11.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AlarmHistoryTableView : UITableView

@property (strong, nonatomic) NSString *strTime;
@property (strong, nonatomic) NSArray *arrList;

@property (nonatomic, copy) void (^didGetDateBlock)(NSDate *date);
@property (nonatomic, copy) void (^didPressMoreDateBlock)();

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style;

@end
