//
//  RightManagementTableView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RightManagementTableView : UITableView

@property (strong, nonatomic) NSArray *arrList;

@property (copy, nonatomic) void(^BlockTabelViewCellPressed)(NSIndexPath *indexPath);
@property (copy, nonatomic) void(^BlockTabelViewCellDelete)(NSIndexPath *indexPath);

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style;

@end
