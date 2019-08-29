//
//  DeviceCV.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/19.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>


#define kDCVFromTop    25
#define kDCVFromBottom 20
#define kDCVFromLeft   10
#define kDCVFromRight  10
#define kDCVMinimumInteritemSpacing 5
#define kDCVMinimumLineSpacing 5

#define kDCVItemWidth (UI_SCREEN_WIDTH -2*10 - kDCVMinimumInteritemSpacing*2)/3.00
#define kDCVItemHeight kDCVItemWidth

@interface DeviceCV : UICollectionView

//公用
@property (strong, nonatomic) NSArray *arrDataList;

@property (nonatomic, copy) void(^didSelectedDeviceItemBlock)(NSIndexPath * indexPath,CommonCollectionViewActionType type,SHModelDevice *modelDevice);

- (id)initWithFrame:(CGRect)frame collectionViewLayout:(UICollectionViewLayout *)layout  type:(CommonCollectionViewType)type;

@end
