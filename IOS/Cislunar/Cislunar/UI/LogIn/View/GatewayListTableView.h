//
//  GatewayListTableView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/9/25.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef enum{
    LoacalListTableType,//本地局域网搜索
    NetListTableType,//从网络中获取到的
}TableType;

@interface GatewayListTableView : UITableView

@property (strong, nonatomic) NSArray *arrGatewayList;
@property (copy, nonatomic) void(^DidSelectRowBlock)(NSIndexPath *indexPath, id object);;

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style tableType:(TableType)type;

@end
