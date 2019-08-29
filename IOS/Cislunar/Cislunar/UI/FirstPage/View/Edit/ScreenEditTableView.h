//
//  ScreenEditTableView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/24.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ScreenEditPropertyModel.h"


@interface ScreenEditTableView : UITableView

@property (strong, nonatomic) ScreenEditPropertyModel *editPropertyModel;
@property (strong, nonatomic) NSArray *arrLinkageDeviceList;
@property (strong, nonatomic) NSArray *arrPerformDeviceList;


@property (nonatomic, copy) void (^didSelectedRowAtIndexPath)(NSIndexPath *indexPath);

//switch
@property (copy, nonatomic) void(^blockFirstCellSwitchPressed)(int iTag, BOOL isButtonOn);
@property (copy, nonatomic) void(^blockFirstCellScreenIconPressed)(UIButton *btn);
@property (copy, nonatomic) void(^blockFirstCellDelayedTextfieldPressed)(UITextField *tx);
@property (copy, nonatomic) void(^blockFirstCellTimerTextfieldPressed)(UITextField *tx);
@property (copy, nonatomic) void(^blockAddDeivcePressed)(ScreenEditAddDeviceType type);
@property (copy, nonatomic) void (^blockLinkageOrPerformDeviceSwitchPressed)(SHModelDevice *device,ScreenEditAddDeviceType type,UISwitch *switchTr);

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style;

@end
