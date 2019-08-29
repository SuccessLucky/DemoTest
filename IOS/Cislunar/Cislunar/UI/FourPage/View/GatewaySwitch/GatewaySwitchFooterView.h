//
//  GatewaySwitchHeaderView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GatewaySwitchFooterView : UIView

@property (copy, nonatomic) void(^BlockAddAcountPressed)();
@property (weak, nonatomic) IBOutlet UIImageView *imageUpLine;
@property (weak, nonatomic) IBOutlet UIImageView *iamgeDownLine;

@end
