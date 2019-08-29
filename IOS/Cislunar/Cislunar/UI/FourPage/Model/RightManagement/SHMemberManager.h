//
//  SHMemberManager.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/19.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void (^GetMemberListCallBack)(BOOL success, id result);

typedef void (^GetMemberControlListCallBack)(BOOL success, id result);


@interface SHMemberManager : NSObject

@property (strong, nonatomic) NSArray *arrMemberList;
@property (strong, nonatomic) NSDictionary *errorInfo;

@property (strong, nonatomic) NSDictionary *dictControlList;
@property (strong, nonatomic) NSDictionary *controlListErrorInfo;

//拉取网关的列表From Net
- (void)doGetMemberListFromNetwork;

//拉取网关列表 From DB
- (void)doGetMemberListFromDB;

//添加成员
- (void)handleTheAddMemberListDataWithModel:(NSString *)strAccount
                             completeHandle:(GetMemberListCallBack)callBack;

//删除成员
- (void)handleTheDeleteMemberListDataWithMemberID:(int)iMemberId
                                completeHandle:(GetMemberListCallBack)callBack;


//根据MemberID获取其控制列表
- (void)doGetMemberRightListFromNetworkWithMemberID:(int)memberId;

- (void)doGetMemberRightListFromDBWithMemberID:(int)iMemberID;

//添加子账号权限
- (void)handleTheAddMemberRightListDataWithMemberID:(int)iMemberID
                                          arrScreen:(NSArray *)arrScreen
                                          arrDevice:(NSArray *)arrDeice
                                     completeHandle:(GetMemberControlListCallBack)callBack;

@end
