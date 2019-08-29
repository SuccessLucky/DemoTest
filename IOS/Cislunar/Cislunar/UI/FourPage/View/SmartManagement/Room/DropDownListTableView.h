//
//  DropDownListTableView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/13.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DropDownListTableView : UITableView

@property (strong, nonatomic) NSArray *arrList;

@property (copy, nonatomic) void(^BlockTableViewDidSelected)(NSIndexPath *indexPath);

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style;

@end
