//
//  EquipmentCollectionView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/13.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CommonCollectionView : UICollectionView

@property (strong, nonatomic) NSArray *arrList;

@property (nonatomic, copy) void(^didSelectedItemBlock)( NSIndexPath * indexPath,CommonCollectionViewActionType type,SHModelDevice *modelDevice);

- (id)initWithFrame:(CGRect)frame collectionViewLayout:(UICollectionViewLayout *)layout type:(CommonCollectionViewType)type;

@end
