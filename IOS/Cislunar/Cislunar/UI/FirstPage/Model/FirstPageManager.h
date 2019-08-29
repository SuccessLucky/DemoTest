//
//  FirstPageManager.h
//  Cislunar
//
//  Created by 余长涛 on 2018/10/1.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>
typedef void (^HandleSetSecurityStatueCompelte)(BOOL success, id result);

NS_ASSUME_NONNULL_BEGIN

@interface FirstPageManager : NSObject

@property (nonatomic, strong) NSArray *arrAlarmInfo;
@property (nonatomic, strong) NSDictionary *errorInfo;

- (void)doGetAlarmInfoFromNetworkWithTime:(NSString *)strTime;
- (void)doGetAlarmInfoDataFromDB;

- (void)handleSetTheSecurityStatus:(int)iSecurityStatus
                    callBackHandle:(HandleSetSecurityStatueCompelte)callBack;

@end

NS_ASSUME_NONNULL_END
