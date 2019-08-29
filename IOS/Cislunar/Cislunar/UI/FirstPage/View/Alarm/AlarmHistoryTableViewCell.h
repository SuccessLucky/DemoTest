//
//  AlarmHistoryTableViewCell.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/11.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AlarmHistoryTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *imageIdentity;
@property (weak, nonatomic) IBOutlet UILabel *labelContent;
@property (weak, nonatomic) IBOutlet UILabel *labelTime;

@end
