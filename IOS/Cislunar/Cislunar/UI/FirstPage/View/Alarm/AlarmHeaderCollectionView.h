//
//  AlarmHeaderCollectionView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/11.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AlarmHistoryCollectionViewCell.h"

@interface AlarmHeaderCollectionView : UICollectionView

@property (strong, nonatomic)NSArray *arrList;
@property (copy, nonatomic) void(^BlockTimeCollectionItemSelected)(NSDate *date,AlarmHistoryCollectionViewCell *cell,NSIndexPath *indexPath);

- (id)initWithFrame:(CGRect)frame collectionViewLayout:(UICollectionViewLayout *)layout;

@end
