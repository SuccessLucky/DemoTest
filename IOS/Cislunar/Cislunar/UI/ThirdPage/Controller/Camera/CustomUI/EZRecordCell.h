//
//  EZRecordCell.h
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/11/3.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import <UIKit/UIKit.h>

@class EZCloudRecordFile;
@class EZDeviceRecordFile;

@interface EZRecordCell : UICollectionViewCell

@property (nonatomic, weak) IBOutlet UIImageView *imageView;
@property (nonatomic, weak) IBOutlet UILabel *timeLabel;
@property (nonatomic, copy) NSString *deviceSerial;

- (void)setCloudRecord:(EZCloudRecordFile *)cloudFile selected:(BOOL)selected;

- (void)setDeviceRecord:(EZCloudRecordFile *)deviceFile selected:(BOOL)selected;

@end
