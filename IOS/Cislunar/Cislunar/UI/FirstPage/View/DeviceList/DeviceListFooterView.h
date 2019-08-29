//
//  DeviceListFooterView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/27.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DeviceListFooterView : UIView

@property (copy, nonatomic) void(^blockAllPressed)(UIButton *btn);
@property (weak, nonatomic) IBOutlet UIButton *btnSelectedAll;

@end
