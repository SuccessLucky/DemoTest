//
//  GatewayListTableView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/9/25.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "GatewayListTableView.h"
#import "GatewayListCell.h"

@interface GatewayListTableView ()<UITableViewDataSource,UITableViewDelegate>

@property (assign, nonatomic) TableType tableType;

@end

@implementation GatewayListTableView

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style tableType:(TableType)type
{
    self = [super initWithFrame:frame style:style];
    if (self) {
        self.delegate = self;
        self.dataSource = self;
        self.showsVerticalScrollIndicator = NO;
        self.tableHeaderView = nil;
        self.separatorStyle = UITableViewCellSeparatorStyleNone;
        self.backgroundColor = [UIColor clearColor];
        self.tableType = type;
    }
    return self;
}

- (void)setArrGatewayList:(NSArray *)arrGatewayList
{
    _arrGatewayList = arrGatewayList;
    [self reloadData];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.arrGatewayList.count;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *indentifier = @"GatewayListCell";
    GatewayListCell *cell = [tableView dequeueReusableCellWithIdentifier:indentifier];
    if (!cell) {
        cell = [[[NSBundle mainBundle]loadNibNamed:@"GatewayListCell" owner:self options:nil]objectAtIndex:0];
    }
    
    SHModelGateway *model = self.arrGatewayList[indexPath.row];
    
    if (self.tableType == LoacalListTableType) {
        cell.title.text = [NSString stringWithFormat:@"网关的名字：%@",model.strGateway_gateway_name];
        cell.subTitle.text = [NSString stringWithFormat:@"网关的IP：%@",model.strGatewayIp];
        cell.port.text = [NSString stringWithFormat:@"网关的端口：%@",model.strGatewayPort];
        
    }else{
    
        cell.title.text = [NSString stringWithFormat:@"网关的名字：%@",model.strGateway_gateway_name];
        cell.subTitle.text = [NSString stringWithFormat:@"网关的类型：%d",model.iGateway_member_type];
        cell.port.text = [NSString stringWithFormat:@"网关的MacAddr：%@",model.strGateway_mac_address];
    }
    
    
    
    if (model == self.arrGatewayList.lastObject) {
        cell.bottomLine.frame = CGRectMake(0,cell.frame.size.height - 0.5, UI_SCREEN_WIDTH, 0.5);
    } else {
        cell.bottomLine.frame = CGRectMake(15,cell.frame.size.height - 0.5, UI_SCREEN_WIDTH, 0.5);
    }
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return  75;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
     [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    SHModelGateway *model = self.arrGatewayList[indexPath.row];
    
    if (self.DidSelectRowBlock) {
        self.DidSelectRowBlock(indexPath,model);
    }
}



@end
