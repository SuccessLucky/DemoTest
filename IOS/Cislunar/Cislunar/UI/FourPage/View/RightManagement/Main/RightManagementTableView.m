//
//  RightManagementTableView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "RightManagementTableView.h"
#import "RightManagementCell.h"
#import "SHMemberModel.h"

@interface RightManagementTableView ()<UITableViewDelegate,UITableViewDataSource>

@end

@implementation RightManagementTableView

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style
{
    self = [super initWithFrame:frame style:style];
    if (self) {
        self.delegate = self;
        self.dataSource = self;
        self.backgroundColor = [UIColor clearColor] ;
        self.showsVerticalScrollIndicator = NO;//隐藏滚动条
        [self setSeparatorStyle:UITableViewCellSeparatorStyleNone];
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
    RightManagementCell *cell = [tableView dequeueReusableCellWithIdentifier:@"RightManagementCell"];
    if (!cell) {
        cell = [[[NSBundle mainBundle] loadNibNamed:@"RightManagementCell" owner:nil options:nil] firstObject];
    }
    
    if (indexPath.row == self.arrList.count - 1) {
        cell.imageViewShortLine.hidden = YES;
        cell.imageViewLongLine.hidden = NO;
    }else{
    
        cell.imageViewShortLine.hidden = NO;
        cell.imageViewLongLine.hidden = YES;
    }
    
    SHMemberModel *model = self.arrList[indexPath.row];
    cell.imageViewHeader.image = [UIImage imageNamed:@"touxiang"];
    cell.labelName.text = model.str_member_name;
    cell.labelAcount.text = model.str_account;
    
    NSString *strRole;
    if (model.iMember_type == 1) {
        strRole = @"网关管理员";
    }else{
        strRole = @"网关成员";
    }
    
    [cell.btnIdentifer setTitle:strRole forState:UIControlStateNormal];
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 72.0f;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 0.01f;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0.01f;
}

- (nullable UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    return [[UIView alloc] initWithFrame:CGRectZero];
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    return [[UIView alloc] initWithFrame:CGRectZero];
}

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
    if (self.BlockTabelViewCellPressed) {
        self.BlockTabelViewCellPressed(indexPath);
    }
}

@end
