//
//  LockNumCollection.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/16.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LockNumCollectionView : UICollectionView

@property (strong, nonatomic) NSArray *arrList;

@property (copy, nonatomic) void(^BlockCollectionSelected)(NSIndexPath *indexPath,NSString* num);

- (id)initWithFrame:(CGRect)frame collectionViewLayout:(UICollectionViewLayout *)layout;

@end
