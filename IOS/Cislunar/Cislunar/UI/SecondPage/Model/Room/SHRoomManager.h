//
//  SHRoomManager.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/26.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void (^GetRoomListCallBack)(BOOL success, id result);


@interface SHRoomManager : NSObject

@property (nonatomic, strong) NSArray *arrRoom;
@property (nonatomic, strong) NSDictionary *errorInfo;

- (void)doGetRoomListFromNetworkWithFloorID:(int)iFloorID;
- (void)doGetRoomListDataFromDBWithFloorID:(int)iFloorID;

//添加
- (void)handleTheAddRoomDataWithModel:(SHModelRoom *)model
                       completeHandle:(GetRoomListCallBack)callBack;

//删除
- (void)handleTheDeleteRoomDataWithRoomId:(int)roomID
                           completeHandle:(GetRoomListCallBack)callBack;

//修改
- (void)handleTheUpdateRoomDataWithModel:(SHModelRoom *)model
                          completeHandle:(GetRoomListCallBack)callBack;


@end
