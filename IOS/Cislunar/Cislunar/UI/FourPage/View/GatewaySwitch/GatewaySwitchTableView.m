//
//  GatewaySwitchTableView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "GatewaySwitchTableView.h"
#import "GatewaySwitchCell.h"
#import "GatewaySwitchFooterView.h"

@interface GatewaySwitchTableView ()<UITableViewDelegate,UITableViewDataSource>

@property(nonatomic,strong) NSIndexPath * currentSelectIndex;

@end

@implementation GatewaySwitchTableView

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style
{
    self = [super initWithFrame:frame style:style];
    if (self) {
        self.delegate = self;
        self.dataSource = self;
        self.showsVerticalScrollIndicator = NO;
        self.tableHeaderView = nil;
        self.separatorStyle = UITableViewCellSeparatorStyleNone;
    }
    return self;
}

- (void)setArrList:(NSArray *)arrList
{
    _arrList = arrList;
    [self reloadData];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{

    return  self.arrList.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    GatewaySwitchCell *cell = [tableView dequeueReusableCellWithIdentifier:@"GatewaySwitchCell"];
    if (!cell) {
        cell = [[[NSBundle mainBundle] loadNibNamed:@"GatewaySwitchCell" owner:nil options:nil] firstObject];
    }
    
    NSString *strZigbeeMacAddr = [[SHLoginManager shareInstance] doGetGatewayMacAddr];
    SHModelGateway *model = self.arrList[indexPath.row];
    if ([model.strGateway_mac_address isEqualToString:strZigbeeMacAddr]) {
        cell.btnSelected.selected = YES;
        _currentSelectIndex = indexPath;
        
    }else{
        cell.btnSelected.selected = NO;
    }
    cell.imageViewHeader.image = [UIImage imageNamed:@"wangguanTemp.png"];
    cell.labelGatewayName.text = model.strGateway_gateway_name;
    cell.labelDetail.text = [NSString stringWithFormat:@"网关地址:%@",model.strGateway_mac_address];
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 75.0f;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 0.01;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 60.0f;
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    GatewaySwitchFooterView *header = [[[NSBundle mainBundle] loadNibNamed:@"GatewaySwitchFooterView" owner:nil options:nil] firstObject];
//    @weakify(self);
//    [header setBlockAddAcountPressed:^{
//        @strongify(self);
//        if (self.BlockTabelViewCellPressed) {
//            self.BlockTabelViewCellPressed(nil,GatewaySwitchTableViewCellPressedType_Header);
//        }
//    }];
    UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapGesture:)];
    tapGesture.numberOfTapsRequired = 1; //点击次数
    tapGesture.numberOfTouchesRequired = 1; //点击手指数
    [header addGestureRecognizer:tapGesture];
    return header;
}

-(void)tapGesture:(UITapGestureRecognizer *)sender
{
    if (self.BlockDidPressed) {
        self.BlockDidPressed(nil,GatewaySwitchTableViewCellPressedType_Header);
    }

}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    return [[UIView alloc] initWithFrame:CGRectZero];
}

//设置相应的按钮。通过 UITableViewRowAction 来实现对应的按钮及按钮的响应事件，按钮的排列顺序可以通过更换按钮的响应事件在数组中的位置来调整
- (nullable NSArray<UITableViewRowAction *> *)tableView:(UITableView *)tableView editActionsForRowAtIndexPath:(NSIndexPath *)indexPath{
    //    设置删除
    UITableViewRowAction *deleteRowAction = [UITableViewRowAction rowActionWithStyle:UITableViewRowActionStyleDestructive title:@"删除" handler:^(UITableViewRowAction * _Nonnull action, NSIndexPath * _Nonnull indexPath) {
        if (self.BlockTabelViewCellDelete) {
            self.BlockTabelViewCellDelete(indexPath);
        }
    }];
    return @[deleteRowAction];
}



- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:NO];
    
    if (_currentSelectIndex!=nil&&_currentSelectIndex != indexPath) {
        GatewaySwitchCell * cell = [tableView cellForRowAtIndexPath:_currentSelectIndex];
        [cell UpdateCellWithState:NO];
    }
    
    if (_currentSelectIndex == indexPath) {
        GatewaySwitchCell * cell = [tableView cellForRowAtIndexPath:_currentSelectIndex];
        [cell UpdateCellWithState:NO];
    }
    
    GatewaySwitchCell * cell = [tableView cellForRowAtIndexPath:indexPath];
    [cell UpdateCellWithState:!cell.isSelected];
    _currentSelectIndex = indexPath;
    
    if (self.BlockDidPressed) {
        self.BlockDidPressed(indexPath,GatewaySwitchTableViewCellPressedType_Common);
    }
}




@end
