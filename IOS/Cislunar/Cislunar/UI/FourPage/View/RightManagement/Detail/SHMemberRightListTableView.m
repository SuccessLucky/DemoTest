//
//  SHMemberRightListTableView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/22.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#define kItemWidth (UI_SCREEN_WIDTH - 35 * 4)/3.00f
#define kItemHeight kItemWidth + 29

#import "SHMemberRightListTableView.h"
#import "ScreenDeviceListTableViewCell.h"
#import "SHMemberRightListCollectionView.h"
#import "GFirstPageHeaderView.h"

@interface SHMemberRightListTableView()<UITableViewDelegate,UITableViewDataSource>

@property (strong, nonatomic) SHMemberRightListCollectionView *collectionView;

@end

@implementation SHMemberRightListTableView

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style
{
    self = [super initWithFrame:frame style:style];
    if (self) {
        self.delegate = self;
        self.dataSource = self;
        self.showsVerticalScrollIndicator = NO;
        self.tableHeaderView = nil;
        self.separatorStyle = UITableViewCellSeparatorStyleNone;
        self.backgroundColor = kBackgroundGrayColor;
        self.tableHeaderView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, 0.01)];
        
        self.arrDeviceList = [NSArray new];
        self.arrScreenList = [NSArray new];
    }
    return self;
}


- (void)setArrDeviceList:(NSArray *)arrDeviceList
{
    _arrDeviceList = arrDeviceList;
    NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:1];
    [self reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
}

- (void)setArrScreenList:(NSArray *)arrScreenList
{
    _arrScreenList = arrScreenList;
    NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:0];
    [self reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
//    [self reloadData];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0) {
        return 1;
    }else{
        return self.arrDeviceList.count;
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (indexPath.section == 0) {
        static NSString *identifer = @"Cell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifer];
        if (!cell) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifer];
        }
        [cell.contentView addSubview:self.collectionView];
        self.collectionView.arrList = self.arrScreenList;
        
        return cell;
    }else{
    
        ScreenDeviceListTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ScreenDeviceListTableViewCell"];
        if (!cell) {
            cell = [[[NSBundle mainBundle] loadNibNamed:@"ScreenDeviceListTableViewCell" owner:nil options:nil] firstObject];
        }
        
        if (indexPath.row == 0) {
            cell.imageTopLine.hidden = NO;
            cell.imageBottomLine.hidden = NO;
        }else {
            cell.imageTopLine.hidden = YES;
            cell.imageBottomLine.hidden = NO;
        }
        
        SHModelDevice *model = self.arrDeviceList[indexPath.row];
        
        NSString *strUnUrl = [[SHAppCommonRequest shareInstance] doGetDeviceGrayUIPicWithPicName:model.strDevice_image];
        NSString *strPrUrl = [[SHAppCommonRequest shareInstance] doGetDeviceHighLightUIPicWithPicName:model.strDevice_image];
        
//        NSString *strUnUrl = [[SHAppCommonRequest shareInstance] handleGetDeviceUnPressImg:model.strDevice_image];
//        NSString *strPrUrl = [[SHAppCommonRequest shareInstance] handleGetDevicePressImg:model.strDevice_image];
        [cell.btnIcon sd_setImageWithURL:[NSURL URLWithString:strPrUrl] forState:UIControlStateNormal];
        [cell.btnIcon sd_setImageWithURL:[NSURL URLWithString:strUnUrl] forState:UIControlStateSelected];
        cell.labelName.text = [NSString stringWithFormat:@"%@",model.strDevice_device_name];
        cell.labelAddr.text = [NSString stringWithFormat:@"%@%@",model.strDevice_floor_name,model.strDevice_room_name];
        
        cell.switchOn.hidden = YES;
        cell.imageArrow.hidden = YES;
        cell.labelState.hidden = YES;
        
        return cell;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0) {
        return kItemHeight + 10 + 10;
    }else
        return  75;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{

    if (section == 0){
        
        return 54;
    }else if(section == 1){
        
        return 54;
    } else {
        return 0.01;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    if (section == 0) {
        return 10;
    } else if (section == 1){
        
        return 10;
    } else {
        return 0.01;
    }
    
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    if (section == 0) {
        GFirstPageHeaderView *view = [[GFirstPageHeaderView alloc] init];
        view.imageViewPic.image = [UIImage imageNamed:@"home_icon_guess"];
        view.labelTitle.text = @"已经添加的场景";
        view.btnAdd.hidden = YES;
        
        UIImageView *imageV = [[UIImageView alloc] initWithFrame:CGRectMake(0, 53.5, UI_SCREEN_WIDTH, 0.5)];
        imageV.backgroundColor = kLineColor;
        [view addSubview:imageV];
        return view;
    }else if(section == 1){
        
        GFirstPageHeaderView *view = [[GFirstPageHeaderView alloc] init];
        view.imageViewPic.image = [UIImage imageNamed:@"home_icon_guess"];
        view.labelTitle.text = @"已经添加的设备";
        view.btnAdd.hidden = YES;
        return view;
    } else {
        
        UIView *view = [UIView new];
        return view;
    }
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    if (section == 0) {
        UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, 10)];
        view.backgroundColor = [UIColor whiteColor];
        UIImageView *imageV = [[UIImageView alloc] initWithFrame:CGRectMake(0, 9.5, UI_SCREEN_WIDTH, 0.5)];
        imageV.backgroundColor = kLineColor;
        [view addSubview:imageV];
        return view;
    } else if (section == 1){
        
        UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, 10)];
        view.backgroundColor = [UIColor clearColor];
        return view;
    }else {
        
        UIView *view = [UIView new];
        return view;
    }
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
}

#pragma mark -
#pragma mark - collectionView

- (SHMemberRightListCollectionView *)collectionView
{
    if (!_collectionView) {
        UICollectionViewFlowLayout * layout = [[UICollectionViewFlowLayout alloc]init];
        layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
        layout.itemSize = CGSizeMake(kItemWidth, kItemHeight);;
        layout.minimumLineSpacing = 32;
        layout.minimumInteritemSpacing = 35;
//        layout.headerReferenceSize = CGSizeMake(0, 0);
//        layout.footerReferenceSize = CGSizeMake(0, 0);
        layout.sectionInset = UIEdgeInsetsMake(10, 15, 0, 15);
        
        _collectionView = [[SHMemberRightListCollectionView alloc]initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, kItemHeight + 10 + 10) collectionViewLayout:layout];
    }
    return _collectionView;
}






@end
