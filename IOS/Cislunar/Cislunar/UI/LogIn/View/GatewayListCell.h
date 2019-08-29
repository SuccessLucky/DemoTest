//
//  GatewayListCell.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/9/25.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GatewayListCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UIImageView *imageForward;
@property (weak, nonatomic) IBOutlet UILabel *rightLabel;
@property (weak, nonatomic) IBOutlet UILabel *port;

@property (weak, nonatomic) IBOutlet UILabel *title;
@property (weak, nonatomic) IBOutlet UILabel *subTitle;
@property (strong, nonatomic)  UIView *bottomLine;

@end
