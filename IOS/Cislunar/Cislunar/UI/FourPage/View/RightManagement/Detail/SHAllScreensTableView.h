//
//  SHAllScreensTableView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/24.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SHAllScreensTableView : UITableView

@property (strong, nonatomic) NSArray *arrList;

@property (strong,nonatomic) NSArray * arrHasAdd;

@property (copy, nonatomic)void(^BlockGetScreenArrList)(NSArray *arrScreen);

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style;

@end
