//
//  CommonCollectionViewCell.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/13.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CommonCollectionViewCell : UICollectionViewCell


@property (weak, nonatomic) IBOutlet UIButton *btnImage;

@property (weak, nonatomic) IBOutlet UILabel *labelTitle;

@property (copy, nonatomic) void(^BlockBtnLongPressed)();

@end
