//
//  DropDownListTableView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/13.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "DropDownListTableView.h"
#import "DropDownTableViewCell.h"

@interface DropDownListTableView ()<UITableViewDelegate,UITableViewDataSource>

@property (strong, nonatomic) SHModelFloor *floorTemp;
@property (strong, nonatomic) SHModelRoom *roomTemp;


@end

@implementation DropDownListTableView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style
{
    self = [super initWithFrame:frame style:style];
    if (self) {
        self.delegate = self;
        self.dataSource = self;
        self.showsVerticalScrollIndicator = NO;
        self.separatorStyle = UITableViewCellSeparatorStyleNone;
        self.backgroundColor = kBackgroundGrayColor;
        
        UINib *nib = [UINib nibWithNibName:@"DropDownTableViewCell" bundle:[NSBundle mainBundle]];
        [self registerNib:nib forCellReuseIdentifier:@"DropDownTableViewCell"];
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
    DropDownTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"DropDownTableViewCell"];
    
    id object = self.arrList[indexPath.row];
    
    if ([object isKindOfClass:[SHModelRoom class]]) {
        
        SHModelRoom *model = self.arrList[indexPath.row];
        cell.labelName.text = [NSString stringWithFormat:@"%@",model.strRoom_name];
        cell.imageViewSelected.hidden = YES;
        if (self.roomTemp.iRoom_id == model.iRoom_id) {
            cell.imageViewSelected.hidden = NO;
        }else{
        
            cell.imageViewSelected.hidden = YES;
        }
        
    }else{
    
        SHModelFloor *model = self.arrList[indexPath.row];
        cell.labelName.text = [NSString stringWithFormat:@"%@",model.strFloor_name];
        cell.imageViewSelected.hidden = YES;
        if (self.floorTemp.iFloor_id == model.iFloor_id) {
            cell.imageViewSelected.hidden = NO;
        }else{
            cell.imageViewSelected.hidden = YES;
        }
    }
   
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 45.0f;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 0.01;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    return [[UIView alloc] initWithFrame:CGRectZero];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    DropDownTableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    cell.imageViewSelected.hidden = NO;
    
    id object = self.arrList[indexPath.row];
    if ([object isKindOfClass:[SHModelRoom class]]) {
        
        self.roomTemp = object;
        
    }else{
        
        self.floorTemp = object;
    }
    
    if (self.BlockTableViewDidSelected) {
        self.BlockTableViewDidSelected(indexPath);
    }
    
    [self reloadData];
    
}

//- (void)tableView:(UITableView *)tableView didHighlightRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    DropDownTableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
//    cell.labelName.textColor = kCommonColor;
//    
//}



@end
