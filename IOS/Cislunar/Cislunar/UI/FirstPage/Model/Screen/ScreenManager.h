//
//  ScreenManager.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/25.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ScreenModel.h"

typedef void(^GetScreeNameCallBackBlcok) (NSArray *arrScreenName);

typedef NS_ENUM(NSInteger, SHScreenType)
{
    SHScreenType_Common    = 0,    //普通的
    SHScreenType_Favorite  = 1,    //常用的
    SHScreenType_All       = 2,    //所有的
};

typedef void (^BlockHandleScreenCallBack)(BOOL success, id result);

typedef void (^BlockGetDeviceSingleInfoCompleteHandle)(BOOL success, SHModelDevice* device);

@interface ScreenManager : NSObject

@property (nonatomic, strong) NSArray *arrScreenList;
@property (nonatomic, strong) NSArray *arrCommonScreenList;
@property (nonatomic, strong) NSDictionary *errorInfo;

- (void)handleGetScreenListFromNetworkWithType:(SHScreenType)type;
- (void)doGetScreenListDataFromDB;

//添加场景
- (void)handleTheAddScreenDataWithModel:(ScreenModel *)model
                         completeHandle:(BlockHandleScreenCallBack)callBack;

//更新场景
- (void)handleTheUpdateScreenDataWithModel:(ScreenModel *)model
                            completeHandle:(BlockHandleScreenCallBack)callBack;

//删除场景
- (void)handleDeleteScreenDataWithScreenID:(int)iScreenID
                            completeHandle:(BlockHandleScreenCallBack)callBack;


//获取所有场景的名字
- (void)doGetScreenListNameDataFromDBWithCompleteHandle:(GetScreeNameCallBackBlcok)callBack;;


@end
