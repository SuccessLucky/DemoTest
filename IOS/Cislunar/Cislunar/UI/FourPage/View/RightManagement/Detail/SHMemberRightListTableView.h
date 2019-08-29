//
//  SHMemberRightListTableView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/22.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SHMemberRightListTableView : UITableView

//@property (copy, nonatomic) void(^didSelectRowBlock)(NSIndexPath *indexPath);

@property (strong, nonatomic) NSArray *arrScreenList;
@property (strong, nonatomic) NSArray *arrDeviceList;

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style;

@end
