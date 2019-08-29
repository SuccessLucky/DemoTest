//
//  ScreenThreeCV.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/18.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ScreenModel.h"

#define kSTCVFromTop    22
#define kSTCVFromBottom 20
#define kSTCVFromLeft   20
#define kSTCVFromRight  20
#define kSTCVMinimumInteritemSpacing 10
#define kSTCVMinimumLineSpacing 10

#define kSTCVItemWidth (UI_SCREEN_WIDTH -2*26 - kSTCVMinimumInteritemSpacing*2)/3.00
#define kSTCVItemHeight kSTCVItemWidth*6/7.0
//#define kSTCVItemHeight kSTCVItemWidth


typedef enum {
    ScreenThreeCVActionType_Common            = 1,
    ScreenThreeCVActionType_LongPressed       = 2,
    ScreenThreeCVActionType_NoneData          = 3
}ScreenThreeCVActionType;


typedef enum {
    ScreenThreeCVType_First               = 1,
    ScreenThreeCVType_All                = 2,
}ScreenThreeCVType;

@interface ScreenThreeCV : UICollectionView

//FistPage头
@property (strong, nonatomic) NSString *strGreetings;

@property (strong, nonatomic) NSString *strImageWeather;

@property (strong, nonatomic) NSString *strWeatherStatus;

@property (strong, nonatomic) NSString *strTemperature;

@property (strong, nonatomic) NSString *strHumidity;

@property (strong, nonatomic) NSArray *arrHistory;

@property (copy, nonatomic) void(^BlockHistroyPressed)(void);

//公用
@property (strong, nonatomic) NSArray *arrDataList;

@property (copy, nonatomic) void(^BlockCollectionSelected)(NSIndexPath *indexPath,ScreenThreeCVType cvType,ScreenThreeCVActionType actionType);

- (id)initWithFrame:(CGRect)frame collectionViewLayout:(UICollectionViewLayout *)layout withScreenThreeCVTYpe:(ScreenThreeCVType)type;

@end
