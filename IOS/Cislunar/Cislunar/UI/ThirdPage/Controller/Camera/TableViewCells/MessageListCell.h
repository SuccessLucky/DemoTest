//
//  MessageListCell.h
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/11/3.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import <UIKit/UIKit.h>
//#import "EZAlarmInfo.h"

@interface MessageListCell : UITableViewCell

@property (nonatomic, weak) IBOutlet UIImageView *actionImageView;
@property (nonatomic, weak) IBOutlet UILabel *titleLabel;
@property (nonatomic, weak) IBOutlet UILabel *timeLabel;
@property (nonatomic, weak) IBOutlet UILabel *descriptionLabel;
@property (nonatomic, weak) IBOutlet UIImageView *readStatusIcon;
@property (nonatomic, copy) NSString *deviceSerial;

- (void)setAlarmInfo:(EZAlarmInfo *)info;

@end
