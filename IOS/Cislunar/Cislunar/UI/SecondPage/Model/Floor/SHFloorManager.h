//
//  SHFloorManager.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/25.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void (^GetFloorListCallBack)(BOOL success, id result);

@interface SHFloorManager : NSObject

@property (nonatomic, strong) NSArray *arrFloor;
@property (nonatomic, strong) NSDictionary *errorInfo;

- (void)doGetFloorListFromNetwork;
- (void)doGetFloorListDataFromDB;

//添加楼层的
- (void)handleTheAddFloorDataWithModel:(SHModelFloor *)model
                        completeHandle:(GetFloorListCallBack)callBack;
//删除楼层
- (void)handleTheDelteFloorDataWithModel:(SHModelFloor *)model
                          completeHandle:(GetFloorListCallBack)callBack;

//更新楼层
- (void)handleTheUpdateFloorDataWithModel:(SHModelFloor *)model
                             floorNewName:(NSString *)floorNewName
                           completeHandle:(GetFloorListCallBack)callBack;

@end
