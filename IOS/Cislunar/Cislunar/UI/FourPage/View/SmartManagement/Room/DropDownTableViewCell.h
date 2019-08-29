//
//  DropDownTableViewCell.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/13.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DropDownTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UILabel *labelName;

@property (weak, nonatomic) IBOutlet UIImageView *imageViewSelected;

@property (assign, nonatomic) BOOL isSelected;

@property (assign, nonatomic) BOOL isCouldSelected;

@property (weak, nonatomic) IBOutlet UIButton *btnTap;

@end
