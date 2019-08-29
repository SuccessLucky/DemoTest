//
//  AirConditionBottomCollectionView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/15.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface AirConditionBottomCollectionView : UICollectionView

@property (strong, nonatomic) NSArray *arrList;

@property (copy, nonatomic) void(^BlockCollectionViewDidSelected)(AirConditionBtnType type);

- (id)initWithFrame:(CGRect)frame collectionViewLayout:(UICollectionViewLayout *)layout;

@end
