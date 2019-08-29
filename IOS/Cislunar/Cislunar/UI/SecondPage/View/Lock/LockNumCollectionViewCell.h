//
//  LockNumCollectionViewCell.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/16.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LockNumCollectionViewCell : UICollectionViewCell

@property (weak, nonatomic) IBOutlet UIButton *btnNum;

@property (copy, nonatomic) void(^BlockBtnNumPressed)();

@end
