//
//  SecurityTableView.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/20.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SecurityTableView : UITableView

@property (strong, nonatomic) NSArray *arrDatasource;
@property (assign, nonatomic) BOOL isOpen;
@property (copy, nonatomic) void(^BlockAnfangSectionPressed)(NSInteger iSection);

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style;



@end
