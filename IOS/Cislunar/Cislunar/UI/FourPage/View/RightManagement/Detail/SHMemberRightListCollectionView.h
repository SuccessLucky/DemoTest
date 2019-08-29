//
//  SHMemberRightListCollectionView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/22.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SHMemberRightListCollectionView : UICollectionView


@property (strong, nonatomic) NSArray *arrList;
- (id)initWithFrame:(CGRect)frame collectionViewLayout:(UICollectionViewLayout *)layout;

@end
