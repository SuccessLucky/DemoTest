//
//  FloorTableView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/18.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, FloorTableViewEditType)
{
    FloorTableViewEditTypeEdit     = 600,    //编辑
    FloorTableViewEditTypeDelete   = 601,    //删除
};

@interface FloorTableView : UITableView

@property (strong, nonatomic) NSMutableArray *arrList;

@property (copy, nonatomic) void(^BlockTabelViewCellPressed)(NSIndexPath *indexPath);
@property (copy, nonatomic) void(^BlockTabelViewCellEdit)(FloorTableViewEditType type,NSIndexPath *indexPath);

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style;

@end
