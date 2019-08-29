//
//  GatewaySwitchCell.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GatewaySwitchCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UIImageView *imageViewHeader;
@property (weak, nonatomic) IBOutlet UILabel *labelGatewayName;
@property (weak, nonatomic) IBOutlet UILabel *labelDetail;
@property (weak, nonatomic) IBOutlet UIImageView *imageVTopLine;
@property (weak, nonatomic) IBOutlet UIImageView *imageVBottomLine;
@property (weak, nonatomic) IBOutlet UIButton *btnSelected;

@property (nonatomic,assign)BOOL isSelected;
-(void)UpdateCellWithState:(BOOL)select;

@end
