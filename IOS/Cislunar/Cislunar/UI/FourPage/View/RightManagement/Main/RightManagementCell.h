//
//  RightManagementCell.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RightManagementCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UIImageView *imageViewHeader;
@property (weak, nonatomic) IBOutlet UILabel *labelName;
@property (weak, nonatomic) IBOutlet UILabel *labelAcount;
@property (weak, nonatomic) IBOutlet UIButton *btnIdentifer;

@property (weak, nonatomic) IBOutlet UIImageView *imageViewShortLine;
@property (weak, nonatomic) IBOutlet UIImageView *imageViewLongLine;
@end
