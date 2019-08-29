//
//  FourPageTableView.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/22.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "FourPageTableView.h"
#import "FourPageCell.h"
#import "MoDelPersonalCenter.h"

@interface FourPageTableView()<UITableViewDataSource,UITableViewDelegate>

@end

@implementation FourPageTableView

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style
{
    self = [super initWithFrame:frame style:style];
    if (self) {
        self.delegate = self;
        self.dataSource = self;
        //cell分割线偏移
        self.backgroundColor = [UIColor clearColor] ;
        self.showsVerticalScrollIndicator = NO;//隐藏滚动条
        [self setSeparatorColor:kLineColor];
//        self.separatorStyle = UITableViewCellEditingStyleNone;
        self.scrollEnabled = NO;
    }
    return self;
    
}

- (void)setArrData:(NSArray *)arrData
{
    _arrData = arrData;
    [self reloadData];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.arrData.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    FourPageCell *cell = [tableView dequeueReusableCellWithIdentifier:@"FourPageCell"];
    if (!cell) {
        NSArray *tempArr = [[NSBundle mainBundle]loadNibNamed:@"FourPageCell" owner:self options:nil];
        cell = tempArr[0];
    }
    ModelPersonalCenter *model = self.arrData[indexPath.row];
    cell.strTitle  = model.strTitle;
    cell.strIcon = model.strIcon;
    
    return cell;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 50;
}

//- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
//{
//    
//    return 195;
//}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    
    return 0.01;
}

//- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
//{
//    FourPageHeaderView *fourPageHeaderView = [[[NSBundle mainBundle] loadNibNamed:@"FourPageHeaderView"
//                                                                            owner:self
//                                                                          options:nil] lastObject];
//    return fourPageHeaderView;
//}

-(UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    UIView *view = [[UIView alloc] init];
    return view;
}


//-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
//{
//    FourPageHeaderView *fourPageHeaderView = [[[NSBundle mainBundle] loadNibNamed:@"FourPageHeaderView"
//                                                                            owner:self
//                                                                          options:nil] lastObject];
//    return fourPageHeaderView;
//}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:NO];
    if (self.BlockTableviewDideSelectedRowPressed) {
        self.BlockTableviewDideSelectedRowPressed(indexPath.row);
    }
}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([cell respondsToSelector:@selector(setSeparatorInset:)])
    {
        [cell setSeparatorInset:UIEdgeInsetsZero];
    }
    if ([cell respondsToSelector:@selector(setPreservesSuperviewLayoutMargins:)])
    {
        [cell setPreservesSuperviewLayoutMargins:NO];
    }
    if ([cell respondsToSelector:@selector(setLayoutMargins:)])
    {
        [cell setLayoutMargins:UIEdgeInsetsZero];
    }
}






@end
