//
//  SHDeviceListTableView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/9.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, SHDeviceListTableViewType)
{
    SHDeviceListTableViewType_ControlDevice    = 0,    //4010和4070
    SHDeviceListTableViewType_AlarmingDevice   = 1,    //4030
};

@interface SHDeviceListTableView : UITableView

@property (strong, nonatomic) NSArray *arrList;

@property (strong,nonatomic) NSArray * arrHasAdd;

@property (copy, nonatomic)void(^BlockGetDeviceArrList)(id object);

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style type:(SHDeviceListTableViewType)type;


@end
