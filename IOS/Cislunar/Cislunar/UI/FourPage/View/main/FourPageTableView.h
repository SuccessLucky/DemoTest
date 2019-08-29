//
//  FourPageTableView.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/22.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface FourPageTableView : UITableView

@property (strong, nonatomic) NSArray *arrData;

@property (copy, nonatomic) void(^BlockTableviewDideSelectedRowPressed)(NSInteger indexPathRow);

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style;

@end

NS_ASSUME_NONNULL_END
