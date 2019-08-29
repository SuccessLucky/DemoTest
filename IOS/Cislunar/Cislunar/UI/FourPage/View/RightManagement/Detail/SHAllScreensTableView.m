//
//  SHAllScreensTableView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/24.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHAllScreensTableView.h"
#import "SHDeviceListTableViewCell.h"

@interface SHAllScreensTableView()<UITableViewDelegate,UITableViewDataSource>

@property(nonatomic,strong) NSMutableArray * choosedArr;

@end

@implementation SHAllScreensTableView

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style
{
    self = [super initWithFrame:frame style:style];
    if (self) {
        self.delegate = self;
        self.dataSource = self;
        self.showsVerticalScrollIndicator = NO;
        self.tableHeaderView = nil;
        self.separatorStyle = UITableViewCellSeparatorStyleNone;
        self.backgroundColor = [UIColor clearColor];
        _choosedArr = [[NSMutableArray alloc]initWithCapacity:0];
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
    [self.choosedArr removeAllObjects];
    [self.choosedArr addObjectsFromArray:arrHasAdd];
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
    
    ScreenModel *model = self.arrList[indexPath.row];
    NSString *strUnUrl = [[SHAppCommonRequest shareInstance] doGetScreenGrayUIPicWithPicName:model.strScreen_image];
    NSString *strPrUrl = [[SHAppCommonRequest shareInstance] doGetScreenHighLightUIPicWithPicName:model.strScreen_image];
    [cell.btnIcon sd_setImageWithURL:[NSURL URLWithString:strUnUrl] forState:UIControlStateNormal];
    [cell.btnIcon sd_setImageWithURL:[NSURL URLWithString:strPrUrl] forState:UIControlStateSelected];
    cell.labelName.text = model.strScreen_name;
    cell.labelAddr.text = @"暂无区域信息";
    
    if ([self.choosedArr containsObject:model]) {
        cell.btnSelected.selected = YES;
    }else{
        cell.btnSelected.selected = NO;
    }
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return  75;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    ScreenModel *model = self.arrList[indexPath.row];
    if ([self.choosedArr containsObject:model]) {
        [self.choosedArr removeObject:model];
        [self reloadData];
    }else{
        
        [self.choosedArr addObject:model];
        [self reloadData];
    }
    
    if (self.BlockGetScreenArrList) {
        self.BlockGetScreenArrList(_choosedArr);
    }
}


@end
