//
//  DeviceListHeaderView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/26.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DeviceListAllModel.h"

@protocol DeviceListHeaderViewDelegate <NSObject>

-(void)DeviceListHeaderViewDelegateMethodClickSection:(NSInteger)section;

@end

@interface DeviceListHeaderView : UIView

/** 区头name*/
@property (nonatomic,copy)NSString *name;

/** 记录区 */
@property (nonatomic,assign)NSInteger  section;

/** detailModel*/
@property (nonatomic,strong)RoomModel *detailModel;

@property (nonatomic,assign)id<DeviceListHeaderViewDelegate>delegate;


@end
