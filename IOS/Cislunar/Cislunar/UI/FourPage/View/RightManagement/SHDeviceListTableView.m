//
//  SHDeviceListTableView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/9.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHDeviceListTableView.h"
#import "SHDeviceListTableViewCell.h"

@interface SHDeviceListTableView ()<UITableViewDelegate,UITableViewDataSource>

@property(nonatomic,strong) NSMutableArray * choosedArr;

@property (nonatomic,strong) NSMutableArray *chooseOtherArr;

@property (nonatomic,assign) SHDeviceListTableViewType type;

@property (strong,nonatomic) SHModelDevice * deiviceSeleted;


@end

@implementation SHDeviceListTableView

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style  type:(SHDeviceListTableViewType)type
{
    self = [super initWithFrame:frame style:style];
    if (self) {
        self.delegate = self;
        self.dataSource = self;
        self.showsVerticalScrollIndicator = NO;
        self.tableHeaderView = nil;
        self.type = type;
        self.separatorStyle = UITableViewCellSeparatorStyleNone;
        self.backgroundColor = [UIColor clearColor];
        _choosedArr = [[NSMutableArray alloc]initWithCapacity:0];
        _chooseOtherArr = [[NSMutableArray alloc]initWithCapacity:1];
    }
    return self;
}

-(void)setArrList:(NSArray *)arrList
{
    _arrList = arrList;
    [self reloadData];
}

- (void)setArrHasAdd:(NSArray *)arrHasAdd
{
    _arrHasAdd = arrHasAdd;
    
    if (self.type == SHDeviceListTableViewType_ControlDevice) {
        [self.choosedArr removeAllObjects];
        [self.choosedArr addObjectsFromArray:arrHasAdd];
    }else{
//        self.deiviceSeleted = arrHasAdd[0];
        [self.chooseOtherArr removeAllObjects];
        [self.chooseOtherArr addObjectsFromArray:arrHasAdd];
    }
//    [self reloadData];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.arrList.count;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *indentifier = @"SHDeviceListTableViewCell";
    SHDeviceListTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:indentifier];
    if (!cell) {
        cell = [[[NSBundle mainBundle]loadNibNamed:@"SHDeviceListTableViewCell" owner:self options:nil]objectAtIndex:0];
    }
    
    if (indexPath.row == 0) {
        cell.imageVTopLine.hidden = NO;
        cell.imageVBottomLine.hidden = NO;
    }else{
        cell.imageVTopLine.hidden = YES;
        cell.imageVBottomLine.hidden = NO;
    }
    
    SHModelDevice *model = self.arrList[indexPath.row];
    if (self.type == SHDeviceListTableViewType_ControlDevice) {
        //多选
        if ([self.choosedArr containsObject:model]) {
            cell.btnSelected.selected = YES;
        }else{
            cell.btnSelected.selected = NO;
        }
    }else{
        //单选
//        if (self.deiviceSeleted.iDevice_device_id == model.iDevice_device_id) {
//            cell.btnSelected.selected = YES;
//        }else{
//            cell.btnSelected.selected = NO;
//        }
        //多选
        if ([self.chooseOtherArr containsObject:model]) {
            cell.btnSelected.selected = YES;
        }else{
            cell.btnSelected.selected = NO;
        }
    }
    

    NSString *strUnUrl = [[SHAppCommonRequest shareInstance] doGetDeviceGrayUIPicWithPicName:model.strDevice_image];
    NSString *strPrUrl = [[SHAppCommonRequest shareInstance] doGetDeviceHighLightUIPicWithPicName:model.strDevice_image];
    [cell.btnIcon sd_setImageWithURL:[NSURL URLWithString:strUnUrl] forState:UIControlStateNormal];
    [cell.btnIcon sd_setImageWithURL:[NSURL URLWithString:strPrUrl] forState:UIControlStateSelected];
    
    cell.labelName.text = model.strDevice_device_name;
    cell.labelAddr.text = [NSString stringWithFormat:@"%@%@",model.strDevice_floor_name,model.strDevice_room_name];
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return  75;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    SHModelDevice *model = self.arrList[indexPath.row];
    
    if (self.type == SHDeviceListTableViewType_ControlDevice) {
        //多选
        if ([self.choosedArr containsObject:model]) {
            [self.choosedArr removeObject:model];
            [self reloadData];
        }else{
            
            [self.choosedArr addObject:model];
            [self reloadData];
        }
        if (self.BlockGetDeviceArrList) {
            self.BlockGetDeviceArrList(_choosedArr);
        }
       
    }else{
        //单选
//        self.deiviceSeleted = model;
//        [self reloadData];
//        
//        NSArray *arrTemp = @[model];
//        if (self.BlockGetDeviceArrList) {
//            self.BlockGetDeviceArrList(arrTemp);
//        }
        
        if ([self.chooseOtherArr containsObject:model]) {
            [self.chooseOtherArr removeObject:model];
            [self reloadData];
        }else{
            [self.chooseOtherArr removeAllObjects];
            [self.chooseOtherArr addObject:model];
            [self reloadData];
        }
        if (self.BlockGetDeviceArrList) {
            self.BlockGetDeviceArrList(_chooseOtherArr);
        }

    }
    
    

    
}


@end
