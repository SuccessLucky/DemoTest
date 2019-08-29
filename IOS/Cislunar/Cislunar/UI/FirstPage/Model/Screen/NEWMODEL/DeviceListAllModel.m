//
//  DeviceListAllModel.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/27.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import "DeviceListAllModel.h"

@implementation DeviceModel

//+(instancetype)detailCdModelWithDic:(NSDictionary *)dic{
//    
//    DetailCDModel *model = [[DetailCDModel alloc]initWithCdModelWithDic:dic];
//    
//    return model;
//}
//
//-(instancetype)initWithCdModelWithDic:(NSDictionary *)dic{
//    if (self = [super init]) {
//        self.cdName  = dic[@"CDname"];
//        self.cdPrice = dic[@"CDprice"];
//        self.cdImage = dic[@"CDimage"];
//        self.cdChooseCount = dic[@"CDchooseCount"];
//    }
//    return self;
//}

@end

@implementation RoomModel

-(instancetype)init
{
    if (self == [super init]) {
        self.recordRoomModelSelected = [NSMutableArray array];
        self.detailDateArr = [NSMutableArray array];
    }
    return self;
}

//+(instancetype)detailModelWithDic:(NSDictionary *)dic{
//    DetailModel *model = [[DetailModel alloc]initWithDic:dic];
//    model.name = dic[@"name"];
//    return model;
//}
//
//
//-(instancetype)initWithDic:(NSDictionary *)dic{
//    if (self= [super init]) {
//        
//        NSMutableArray *mutableArr = [NSMutableArray array];
//        NSInteger value = 0;
//        NSInteger sum = 0;
//        for (NSDictionary *dicCd  in dic[@"detail"]) {
//            DetailCDModel *cdModel = [DetailCDModel detailCdModelWithDic:dicCd];
//            
//            value = [cdModel.cdPrice integerValue];
//            sum = sum+value;
//            [mutableArr addObject:cdModel];
//        }
//        self.sectionTotalPrice = sum;
//        self.detailDateArr = mutableArr;
//        self.recordCdModelSelected = [NSMutableArray array];
//        NSLog(@"sectionTotalPrice  %zd",self.sectionTotalPrice);
//        
//    }
//    return self;
//}


@end




@implementation DeviceListAllModel

//+(instancetype)goShopCarModelWith:(NSArray *)arr{
//    
//    DeviceListAllModel *model = [[DeviceListAllModel alloc]initWithModelArr:arr];
//    
//    return model;
//}
//
//-(instancetype)initWithModelArr:(NSArray *)arr{
//    
//    if (self == [super init]) {
//        NSMutableArray *mutableArr = [NSMutableArray array];
//        
//        for (NSDictionary *dic in arr) {
//            RoomModel *detailModel = [RoomModel detailModelWithDic:dic];
//            [mutableArr addObject:detailModel];
//            self.recordArr = [NSMutableArray array];
//        }
//        self.headModelArr = mutableArr;
//        
//    }
//    return self;
//}
-(instancetype)init
{
    if (self == [super init]) {
        self.recordArr = [NSMutableArray array];
        self.headModelArr = [NSMutableArray array];
    }
    return self;
}


@end
