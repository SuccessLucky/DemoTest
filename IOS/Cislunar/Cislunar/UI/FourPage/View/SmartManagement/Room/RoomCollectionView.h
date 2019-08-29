//
//  RoomCollectionView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef enum {
    RoomCollectionViewActionType_Common               = 0,
    RoomCollectionViewActionType_Delete               = 1,
    RoomCollectionViewActionType_LongPressed          = 2,
    RoomCollectionViewActionType_Add                  = 3,
}RoomCollectionViewActionType;


@interface RoomCollectionView : UICollectionView

@property (nonatomic, strong) NSArray * arrList;

@property (nonatomic, assign) BOOL hideSplitLine;

@property (nonatomic, copy) void(^didSelectedItemBlock)( NSIndexPath * indexPath,RoomCollectionViewActionType type,SHModelRoom *modelRoom);

- (instancetype)initWithFrame:(CGRect)frame collectionViewLayout: (UICollectionViewLayout *)layout;

@end
