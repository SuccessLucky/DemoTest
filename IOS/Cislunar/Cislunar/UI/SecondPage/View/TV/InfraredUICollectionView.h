//
//  TVUICollectionView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/14.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface InfraredUICollectionView : UICollectionView

@property (strong, nonatomic) NSArray *arrList;

@property (nonatomic, copy) void(^didSelectedItemBlock)( NSIndexPath * indexPath,InfraredUICollectionViewActionType type,SHInfraredKeyModel *modelDevice);;

- (id)initWithFrame:(CGRect)frame collectionViewLayout:(UICollectionViewLayout *)layout;

@end
