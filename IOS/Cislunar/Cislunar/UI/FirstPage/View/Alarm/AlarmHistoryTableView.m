//
//  AlarmHistoryTableViewView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/11.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "AlarmHistoryTableView.h"
#import "AlarmHistoryTableViewCell.h"
#import "AlarmHistoryHeaderView.h"

#define titleViewHeader (UI_SCREEN_WIDTH -15*2 - 15*5)/5.00

@interface AlarmHistoryTableView ()<UITableViewDelegate,UITableViewDataSource>

@property (strong, nonatomic) AlarmHistoryHeaderView *headerView;

@end

@implementation AlarmHistoryTableView

- (instancetype)initWithFrame:(CGRect)frame style:(UITableViewStyle)style
{
    self = [super initWithFrame:frame style:style];
    if (self) {
        self.delegate = self;
        self.dataSource = self;
        self.showsVerticalScrollIndicator = NO;//隐藏滚动条
//        [self setSeparatorStyle:UITableViewCellSeparatorStyleNone];
        UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, 40)];
        self.tableFooterView = view;
        [self doAddHeader];
    }
    return self;
}

- (void)doAddHeader
{
    self.headerView = [[AlarmHistoryHeaderView alloc] initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, titleViewHeader + 47)];
    self.tableHeaderView = self.headerView;
    
    @weakify(self);
    [self.headerView setBlockDateSelected:^(NSDate *date) {
        @strongify(self);
        if (self.didGetDateBlock) {
            self.didGetDateBlock(date);
        }
    }];
    
    [self.headerView setBlockMoreDateSelected:^{
        @strongify(self);
        if (self.didPressMoreDateBlock) {
            self.didPressMoreDateBlock();
        }
    }];
}

- (void)setStrTime:(NSString *)strTime
{
    self.headerView.strTime = strTime;
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
    return self.arrList.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identifer = @"GMTaskListTableViewCell";
    AlarmHistoryTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifer];
    if (!cell) {
        cell = [[[NSBundle mainBundle] loadNibNamed:@"AlarmHistoryTableViewCell" owner:self options:nil] firstObject];;
    }
    SHAlarmModel *model = self.arrList[indexPath.row];
    cell.labelContent.text = model.strAlarm_alarm_msg;
    cell.labelTime.text = [model.strAlarm_create_date substringWithRange:NSMakeRange(model.strAlarm_create_date.length - 8, 8)];
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 75;
}


//- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    [tableView deselectRowAtIndexPath:indexPath animated:YES];
//    if (self.didSelectedRowAtIndexPath) {
//        self.didSelectedRowAtIndexPath(indexPath);
//    }
//}


@end
