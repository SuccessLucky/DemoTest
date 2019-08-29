//
//  AirConditionAlterTableView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/15.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AirConditionBtnModel.h"

@interface AirConditionAlterTableView : UITableView

@property (strong, nonatomic) NSArray *arrList;

@property (copy, nonatomic) void(^BlockTableViewDidSelected)(AirConditionBtnModel *model);
@property (copy, nonatomic) void(^BlockPressCancellBtn)();

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style title:(NSString *)title;

@end
