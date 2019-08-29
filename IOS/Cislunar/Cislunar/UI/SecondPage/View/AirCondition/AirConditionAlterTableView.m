//
//  AirConditionAlterTableView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/15.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "AirConditionAlterTableView.h"
#import "AirConditionAlterCellTableViewCell.h"
#import "AirConditionBtnModel.h"
#import "AirConditionAlterHeaderView.h"

@interface AirConditionAlterTableView ()<UITableViewDelegate,UITableViewDataSource>

@property (strong, nonatomic) NSString *strTitle;

@end


@implementation AirConditionAlterTableView

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style title:(NSString *)title
{
    self = [super initWithFrame:frame style:style];
    if (self) {
        self.delegate = self;
        self.dataSource = self;
        self.showsVerticalScrollIndicator = NO;
        self.separatorStyle = UITableViewCellSeparatorStyleNone;
        self.backgroundColor = kBackgroundGrayColor;
        self.strTitle = title;
//        
//        UINib *nib = [UINib nibWithNibName:@"AirConditionAlterCellTableViewCell" bundle:[NSBundle mainBundle]];
//        [self registerNib:nib forCellReuseIdentifier:@"AirConditionAlterCellTableViewCell"];
    }
    return self;
}

- (void)setArrList:(NSArray *)arrList
{
    _arrList = arrList;
    [self reloadData];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.arrList.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    AirConditionAlterCellTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"AirConditionAlterCellTableViewCell"];
    if (!cell) {
        cell = [[[NSBundle mainBundle]loadNibNamed:@"AirConditionAlterCellTableViewCell" owner:nil options:nil] firstObject];;
    }
    AirConditionBtnModel *model = self.arrList[indexPath.row];
    
    [cell.btnImage setImage:[UIImage imageNamed:model.strPic] forState:UIControlStateNormal];
    cell.LabelName.text = model.strName;
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 45.0f;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 40;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    NSArray *libs = [[NSBundle mainBundle] loadNibNamed:@"AirConditionAlterHeaderView" owner:nil options:nil];
    AirConditionAlterHeaderView *headerView = (AirConditionAlterHeaderView *)libs[0];
    headerView.labelTitle.text = self.strTitle;
    [headerView setBlockCancellPressed:^{
        if (self.BlockPressCancellBtn) {
            self.BlockPressCancellBtn();
        }
    }];
    return headerView;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    AirConditionBtnModel *model = (AirConditionBtnModel *)self.arrList[indexPath.row];
    if (self.BlockTableViewDidSelected) {
        self.BlockTableViewDidSelected(model);
    }
}

@end
