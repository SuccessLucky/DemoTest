//
//  DeviceListAllModel.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/27.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

/*****************最里面层的model,字段cdPrice、cdImage、cdName*******************/

@interface DeviceModel : SHModelDevice

/** cd的个数*/
@property (nonatomic,copy)NSString *deviceChooseCount;

/** 记录是否选中*/
@property (nonatomic,assign)BOOL  isChoosed;


@end


/**************************中间层model,字段name********************************/


@interface RoomModel : SHModelRoom

/** 区头是否选中*/
@property (nonatomic,assign)BOOL  isChoose;

/** 记录选中的cell*/
@property (nonatomic,strong)NSMutableArray *recordRoomModelSelected ;

/** 存放model*/
@property (nonatomic,strong)NSMutableArray *detailDateArr ;

/** 记录选中的行*/
@property (nonatomic,assign)NSInteger  indexPathRow;

/** 记录选中的区*/
@property (nonatomic,assign)NSInteger  indexPathSection;


@end

/**************************最外层model,字段name********************************/

@interface DeviceListAllModel : NSObject

/** 记录区是否被全选*/
@property (nonatomic,strong)NSMutableArray *recordArr;

/** 存放model*/
@property (nonatomic,strong)NSMutableArray *headModelArr;

@end
