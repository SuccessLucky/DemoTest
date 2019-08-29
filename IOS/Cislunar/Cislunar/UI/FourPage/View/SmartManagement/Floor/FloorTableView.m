//
//  FloorTableView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/18.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "FloorTableView.h"
#import "FloorTableViewCell.h"

@interface FloorTableView ()<UITableViewDelegate,UITableViewDataSource>

@end

@implementation FloorTableView

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style
{
    self = [super initWithFrame:frame style:style];
    if (self) {
        self.delegate = self;
        self.dataSource = self;
        self.backgroundColor = kBackgroundGrayColor ;
        self.showsVerticalScrollIndicator = NO;//隐藏滚动条
    }
    return self;
}

- (void)setArrList:(NSMutableArray *)arrList
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
    FloorTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"FloorTableViewCell"];
    if (!cell) {
        cell = [[[NSBundle mainBundle] loadNibNamed:@"FloorTableViewCell" owner:nil options:nil] firstObject];
    }
    SHModelFloor *model = self.arrList[indexPath.row];
    cell.labelTitle.text = model.strFloor_name;
//    [cell.btnHeader setTitle:[NSString stringWithFormat:@"%ldF",(long)indexPath.row] forState:UIControlStateNormal];
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 44.0f;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 0.01f;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 20.0f;
}

- (nullable UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, 20)];
    return view;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, 0.01)];
    return view;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (self.BlockTabelViewCellPressed) {
        self.BlockTabelViewCellPressed(indexPath);
    }
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (void)tableView:(UITableView *)tableView didHighlightRowAtIndexPath:(NSIndexPath *)indexPath
{
    //    if (tableView == self.tableNodeName) {}else{
    //
    //        GMProjectNodeCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    //        cell.labelName.textColor = UIColorFromRGB(0xff5500);
    //    }
    
}

//设置相应的按钮。通过 UITableViewRowAction 来实现对应的按钮及按钮的响应事件，按钮的排列顺序可以通过更换按钮的响应事件在数组中的位置来调整
- (nullable NSArray<UITableViewRowAction *> *)tableView:(UITableView *)tableView editActionsForRowAtIndexPath:(NSIndexPath *)indexPath{
    //    设置删除
    UITableViewRowAction *deleteRowAction = [UITableViewRowAction rowActionWithStyle:UITableViewRowActionStyleDestructive title:@"删除" handler:^(UITableViewRowAction * _Nonnull action, NSIndexPath * _Nonnull indexPath) {
//        [self.arrList removeObjectAtIndex:indexPath.row];
//        [self deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationLeft];
        if (self.BlockTabelViewCellEdit) {
            self.BlockTabelViewCellEdit(FloorTableViewEditTypeDelete,indexPath);
        }
    }];
   
    UITableViewRowAction *topRowAction = [UITableViewRowAction rowActionWithStyle:UITableViewRowActionStyleDefault title:@"编辑" handler:^(UITableViewRowAction * _Nonnull action, NSIndexPath * _Nonnull indexPath) {
//        [self.arrList exchangeObjectAtIndex:indexPath.row withObjectAtIndex:0];
//        NSIndexPath *firstindexpath = [NSIndexPath indexPathForRow:0 inSection:indexPath.section];
//        [self moveRowAtIndexPath:indexPath toIndexPath:firstindexpath];
//        [self reloadData];
        
        if (self.BlockTabelViewCellEdit) {
            self.BlockTabelViewCellEdit(FloorTableViewEditTypeEdit,indexPath);
        }
    }];
    topRowAction.backgroundColor = kCommonColor;
    return @[deleteRowAction,topRowAction];
}


@end
