//
//  DeviceAllListTable.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/26.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import "DeviceAllListTable.h"
#import "DeviceAllListCell.h"
#import "DeviceListHeaderView.h"
#import "DeviceListAllModel.h"

@interface DeviceAllListTable ()<UITableViewDelegate,UITableViewDataSource,DeviceListHeaderViewDelegate>

@end

static NSString *cellId = @"DeviceAllListCell";

@implementation DeviceAllListTable

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style
{
    self = [super initWithFrame:frame style:style];
    if (self) {
        self.delegate = self;
        self.dataSource = self;
        self.showsVerticalScrollIndicator = NO;//隐藏滚动条
        [self setSeparatorStyle:UITableViewCellSeparatorStyleNone];
        self.backgroundColor = kBackgroundGrayColor;
        
        CGFloat space = 10;
        CGFloat temp  = 0;
        self.contentInset = UIEdgeInsetsMake(temp, temp, space*5, temp);
        //注册cell
        [self registerNib:[UINib nibWithNibName:@"DeviceAllListCell" bundle:nil] forCellReuseIdentifier:cellId];
        
    }
    return self;
}

-(void)setDeviceListAllModel:(DeviceListAllModel *)deviceListAllModel
{
    _deviceListAllModel = deviceListAllModel;
    [self reloadData];
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    
    return self.deviceListAllModel.headModelArr.count;
}


-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    RoomModel *room = self.deviceListAllModel.headModelArr[section];
    return room.detailDateArr.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    RoomModel *room = self.deviceListAllModel.headModelArr[indexPath.section];
    DeviceModel *device = room.detailDateArr[indexPath.row];
    
    room.indexPathRow = indexPath.row;
    room.indexPathSection = indexPath.section;
    
    DeviceAllListCell *cell = [tableView dequeueReusableCellWithIdentifier:cellId];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    if (indexPath.row == room.detailDateArr.count - 1) {
        cell.imageViewBottomLine.hidden = YES;
    }else{
        cell.imageViewBottomLine.hidden = NO;
    }
    cell.deviceModel = device;
    cell.roomModel = room;
    
    [cell setBlockCellChoosePressed:^(DeviceModel*deviceModel,RoomModel*roomModel){
        /** cell 的 section*/
        NSInteger section = [self.deviceListAllModel.headModelArr indexOfObject:roomModel];
        
        //先判断section，然后再判断row
        if (roomModel.isChoose) {
            /** 如果之前区头是选中的*/
            BOOL isContain = [roomModel.recordRoomModelSelected containsObject:deviceModel];
            if (isContain) [roomModel.recordRoomModelSelected removeObject:deviceModel];
        }
        
        //判断row是否是选中的
        if (!deviceModel.isChoosed) {
            deviceModel.isChoosed = YES;
            BOOL isContain = [roomModel.recordRoomModelSelected containsObject:deviceModel];
            if (!isContain) [roomModel.recordRoomModelSelected addObject:deviceModel];
            
        }else{
            deviceModel.isChoosed = NO;
            [roomModel.recordRoomModelSelected removeObject:deviceModel];
        }
        
        BOOL isEqual = roomModel.recordRoomModelSelected.count == roomModel.detailDateArr.count;
        //判断一个区的cell是否全部选中，如果全选，则让属于该cell的区头也选中
        if (isEqual) {
            [self DeviceListHeaderViewDelegateMethodClickSection:section];
            //判断是否全选
            [self addModel:roomModel JudgeSectionSelectedAll:self.deviceListAllModel.recordArr isChoose:isEqual];
        }else{
            //否则，区头就没有选中
            roomModel.isChoose = NO;
            //判断是否全选
            [self addModel:roomModel JudgeSectionSelectedAll:self.deviceListAllModel.recordArr isChoose:isEqual];
        }
        //计算所有
        [self caculateTotalMoney];
        
        [self reloadData];
    }];
    return cell;
}

//返回每个cell的高度
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 60;
}

//返回区头的高度
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 50;
}

//区头视图
-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    
    RoomModel *room = self.deviceListAllModel.headModelArr[section];
    DeviceListHeaderView *headV = [[DeviceListHeaderView alloc] init];
    headV.name = [NSString stringWithFormat:@"%@-%@",room.strFloorName,room.strRoom_name];
    headV.detailModel = room;
    headV.section = section;
    headV.delegate = self;
    return headV;
}

#pragma mark -- DeviceListHeaderViewDelegate 点击区头的代理方法
-(void)DeviceListHeaderViewDelegateMethodClickSection:(NSInteger)section{
    
    RoomModel *detailModel = self.deviceListAllModel.headModelArr[section];
    // 区  是否全选
    [self addOrRemoveModel:detailModel isChoose:!detailModel.isChoose];
    //所有cell是否  全选
    [self addModel:detailModel JudgeSectionSelectedAll:self.deviceListAllModel.recordArr isChoose:!detailModel.isChoose];
    //反选
    detailModel.isChoose = !detailModel.isChoose;
    
    //计算所有
    [self caculateTotalMoney];
    
    [self reloadData];
}

// 区  选中 (区选中--->区中的cell全部选中)
-(void)addOrRemoveModel:(RoomModel *)detailModel isChoose:(BOOL)isChoose{
    
    if (isChoose) {
        //区中的所有cell都选中
//        self.btnSelectedAllTr.selected = YES;
        [detailModel.detailDateArr enumerateObjectsUsingBlock:^(DeviceModel  *_Nonnull cdModel, NSUInteger idx, BOOL * _Nonnull stop) {
            /** 所有的cell选中*/
            cdModel.isChoosed = YES;
            BOOL isContain = [detailModel.recordRoomModelSelected containsObject:cdModel];
            if (!isContain) [detailModel.recordRoomModelSelected addObject:cdModel];
        }];
        
    }else{
//        self.btnSelectedAllTr.selected = NO;
        [detailModel.detailDateArr enumerateObjectsUsingBlock:^(DeviceModel  *_Nonnull cdModel, NSUInteger idx, BOOL * _Nonnull stop) {
            
            cdModel.isChoosed = NO;
            [detailModel.recordRoomModelSelected removeObject:cdModel];
            
        }];
    }
}

//选中section，或者单个选择中row，cell是否已经全部选中
-(void)addModel:(RoomModel *)detailModel JudgeSectionSelectedAll:(NSMutableArray *)recodArr isChoose:(BOOL)isChoose{
    
    BOOL isContain = [recodArr containsObject:detailModel];
    
    if (isChoose) {
        if (!isContain)  [recodArr addObject:detailModel];
    }else{
        if (isContain) [recodArr removeObject:detailModel];
    }
}

/** 计算总价*/
-(void)caculateTotalMoney{
    
    if (self.deviceListAllModel.recordArr.count == self.deviceListAllModel.headModelArr.count) {
        
        NSLog(@"全选");
        
        self.btnSelectedAllTr.selected = YES;
        
    }else{
        
        NSLog(@"没有全选");
        self.btnSelectedAllTr.selected = NO;
    }
    
}






@end
