//
//  SecurityTableView.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/20.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "SecurityTableView.h"
#import "SecurityTableCell.h"
#import "SecurityTableSectionView.h"
#import "ModelSecurity.h"
#import "SecurityHeaderView.h"

@interface SecurityTableView ()<UITableViewDataSource,UITableViewDelegate>

@property(nonatomic,strong)NSMutableArray*isOpenArr;

@property (assign, nonatomic) BOOL isTouchedOnSection;

@end

@implementation SecurityTableView

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
        self.separatorStyle = UITableViewCellEditingStyleNone;
        self.isTouchedOnSection = NO;
    }
    return self;
    
}

- (void)setArrDatasource:(NSArray *)arrDatasource
{
    _arrDatasource = arrDatasource;
    [self reloadData];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    return self.arrDatasource.count;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    ModelSecurity *model = self.arrDatasource[section];
    
    if ([model.strType isEqualToString:@"000"]) {
        return 0;
    }
    if (model.iShowDetail) {
        return 1;
    }else{
        return 0;
    }
    
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    SecurityTableCell *cell = [tableView dequeueReusableCellWithIdentifier:@"SecurityTableCell"];
    if (!cell) {
        NSArray *tempArr = [[NSBundle mainBundle]loadNibNamed:@"SecurityTableCell" owner:self options:nil];
        cell = tempArr[0];
    }
    ModelSecurity *model = self.arrDatasource[indexPath.section];
    cell.arrData  = model.arrDeviceList;
    
    return cell;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 70;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{

    return 66;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    
    return 0.01;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    SecurityTableSectionView *sectionHeaderView = [[[NSBundle mainBundle] loadNibNamed:@"SecurityTableSectionView"
                                                                      owner:self
                                                                    options:nil] lastObject];
    
    ModelSecurity *model = self.arrDatasource[section];
    sectionHeaderView.strIcon = model.strIcon;
    sectionHeaderView.strName = model.strName;
    sectionHeaderView.strNum = [NSString stringWithFormat:@"%lu",(unsigned long)model.arrDeviceList.count];
    sectionHeaderView.strArrow = model.strImageArrow;
    sectionHeaderView.iSection = section;
    sectionHeaderView.iRedDotCount = model.iRedDotCount;
    
    @weakify(self);
    [sectionHeaderView setBlockBtnPressed:^(NSInteger iSection){
        @strongify(self);
        
        if (self.BlockAnfangSectionPressed) {
            self.BlockAnfangSectionPressed(iSection);
        }
        
        for (int i = 0; i <self.arrDatasource.count; i ++) {
            
            if (i == iSection) {
                ModelSecurity *modelTemp = self.arrDatasource[i];
                modelTemp.iShowDetail = !modelTemp.iShowDetail;
            }
        }
        
        NSIndexSet *indexSet = [[NSIndexSet alloc] initWithIndex:iSection];
        [self reloadSections:indexSet withRowAnimation:UITableViewRowAnimationFade];
    }];
    
    return sectionHeaderView;
}

-(UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    UIView *view = [[UIView alloc] init];
    return view;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:NO];
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


-(NSMutableArray *)isOpenArr
{
    if (!_isOpenArr) {
        _isOpenArr = [NSMutableArray new];
    }
    return _isOpenArr;
}


@end
