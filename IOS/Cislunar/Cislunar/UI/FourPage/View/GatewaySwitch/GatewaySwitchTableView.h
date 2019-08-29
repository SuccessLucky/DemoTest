//
//  GatewaySwitchTableView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef enum {
    GatewaySwitchTableViewCellPressedType_Common               = 1,
    GatewaySwitchTableViewCellPressedType_Header               = 2,
}GatewaySwitchTableViewCellPressedType;



@interface GatewaySwitchTableView : UITableView

@property (strong, nonatomic) NSArray *arrList;

@property (copy, nonatomic)void(^BlockDidPressed)(NSIndexPath *indexPath,GatewaySwitchTableViewCellPressedType type);

@property (copy, nonatomic) void(^BlockTabelViewCellDelete)(NSIndexPath *indexPath);

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style;

@end
