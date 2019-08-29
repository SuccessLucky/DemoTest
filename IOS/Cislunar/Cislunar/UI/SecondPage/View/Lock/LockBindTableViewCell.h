//
//  LockBindTableViewCell.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/16.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LockBindTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UILabel *labelName;
@property (weak, nonatomic) IBOutlet UILabel *labelDetail;
@property (weak, nonatomic) IBOutlet UIImageView *imageVBottomLine;
@property (weak, nonatomic) IBOutlet UIImageView *imageVTopLine;

@end
