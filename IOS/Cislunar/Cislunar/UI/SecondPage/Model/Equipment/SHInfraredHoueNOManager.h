//
//  SHInfraredHoueNOManager.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/4.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void (^GetListCallBack)(BOOL success, id result);

@interface SHInfraredHoueNOManager : NSObject

//添加红外设备按钮
- (void)handleTheAddInfraredBtnsDataWithDeviceId:(int)deviceID
                                        arrModel:(NSArray *)arrModels
                                  completeHandle:(GetListCallBack)callBack;

@end
