//
//  RoomCollectionViewCell.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RoomCollectionViewCell : UICollectionViewCell

@property (weak, nonatomic) IBOutlet UIButton *btnIcon;

@property (weak, nonatomic) IBOutlet UILabel *iconLabel;

@property (weak, nonatomic) IBOutlet UIView *bottomLineView;

@property (weak, nonatomic) IBOutlet UIView *rightLineView;

@property (weak, nonatomic) IBOutlet UIButton *btnDelete;

@property (copy, nonatomic) void(^BlockBtnIconPressed)();

@property (copy, nonatomic) void(^BlockBtnDeletePressed)();

@property (copy, nonatomic) void(^BlockBtnLongPressed)();

@end
