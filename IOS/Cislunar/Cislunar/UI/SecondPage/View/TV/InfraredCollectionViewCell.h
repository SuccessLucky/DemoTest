//
//  TVCollectionViewCell.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/14.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface InfraredCollectionViewCell : UICollectionViewCell

@property (weak, nonatomic) IBOutlet UIButton *btnItem;
@property (copy, nonatomic) void(^BlockBtnLongPressed)();

@end
