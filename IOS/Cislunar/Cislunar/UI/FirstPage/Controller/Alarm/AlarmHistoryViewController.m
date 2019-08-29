//
//  AlarmHistoryViewController.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/11.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "AlarmHistoryViewController.h"
#import "AlarmHistoryTableView.h"
#import "WHUCalendarPopView.h"
#import "FirstPageManager.h"
#import "HUDManager.h"

@interface AlarmHistoryViewController ()

@property (strong, nonatomic) AlarmHistoryTableView *tableView;
@property (strong, nonatomic) WHUCalendarPopView* pop;
@property (strong, nonatomic) FirstPageManager *manager;

@end

@implementation AlarmHistoryViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitleViewText:@"报警历史"];
    [self doInitSubViews];
    [self doRegisterKVO];

    [self doInitCalendarPop];
    [self doLoadData];
    [self doAddAction];
}

- (void)doInitSubViews
{
    
     [self setNavigationBarLeftBarButtonWithTitle:@"返回" action:@selector(leftAction:)];
    
    [self.view addSubview:self.tableView];
    [self doAddTableViewConstraint];
    
    
    CGRect frame = CGRectMake(0, -UI_SCREEN_HEIGHT, self.view.bounds.size.width,  UI_SCREEN_HEIGHT);
    UIView *view = [[UIView alloc] initWithFrame:frame];
    view.backgroundColor = kCommonColor;
    [self.tableView addSubview:view];
}

- (void)leftAction:(UIButton *)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)doLoadData
{
    [HUDManager showLoadingHud:@"加载中..."];
//    [self.manager doGetAlarmInfoDataFromDB];
    [self.manager doGetAlarmInfoFromNetworkWithTime:[self doGetCurrentTime]];
}

- (void)doRegisterKVO
{
    @weakify(self);
    [self observeKeyPath:@keypath(self.manager.arrAlarmInfo)
                   block:^(id value) {
                       @strongify(self);
                       [HUDManager hidenHud];
                       NSArray *arrAlarmInfo = (NSArray *)value;
                       self.tableView.arrList = [NSMutableArray arrayWithArray:arrAlarmInfo];
                   }];
    
    [self observeKeyPath:@keypath(self.manager.errorInfo)
                   block:^(id value) {
                       [HUDManager hidenHud];
                       NSDictionary *dict = (NSDictionary *)value;
                       [HUDManager showStateHud:[NSString stringWithFormat:@"%@",dict[@"message"]] state:HUDStateTypeSuccess afterDelay:1];
                   }];
}

- (void)doInitCalendarPop
{
    _pop=[[WHUCalendarPopView alloc] initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, UI_SCREEN_HEIGHT - 64)];
    @weakify(self);
    _pop.onDateSelectBlk=^(NSDate* date){
        @strongify(self);
        NSDateFormatter *format = [[NSDateFormatter alloc] init];
        [format setDateFormat:@"YYYY-MM-dd"];
        NSString *dateString = [format stringFromDate:date];
        NSLog(@"%@",dateString);
        self.tableView.strTime = dateString;
        [self.manager doGetAlarmInfoFromNetworkWithTime:dateString];
    };

}

- (void)doAddAction
{
    @weakify(self);
    [self.tableView setDidGetDateBlock:^(NSDate *date) {
        @strongify(self);
        NSDateFormatter *format = [[NSDateFormatter alloc] init];
        [format setDateFormat:@"YYYY-MM-dd"];
        NSString *dateString = [format stringFromDate:date];
        NSLog(@"%@",dateString);
        self.tableView.strTime = dateString;
        [self.manager doGetAlarmInfoFromNetworkWithTime:dateString];
    }];
    
    [self.tableView setDidPressMoreDateBlock:^{
        @strongify(self);
        [self.pop show];
    }];
}

#pragma mark -
#pragma mark - private
- (NSString *)doGetCurrentTime
{
    NSDate *currentDate = [NSDate date];//获取当前时间，日期
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"YYYY-MM-dd"];
    NSString *dateString = [dateFormatter stringFromDate:currentDate];
    return dateString;
}


#pragma mark - 
#pragma mark - init

- (FirstPageManager *)manager
{
    if (!_manager) {
        _manager = [FirstPageManager new];
    }
    return  _manager;
}

- (AlarmHistoryTableView *)tableView
{
    if (!_tableView) {
        _tableView = [[AlarmHistoryTableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
    }
    return _tableView;
}

- (void)doAddTableViewConstraint
{
    @weakify(self);
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.top.equalTo(self.view.mas_top);
        make.left.equalTo(self.view.mas_left);
        make.bottom.equalTo(self.view.mas_bottom);
        make.right.equalTo(self.view.mas_right);
    }];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
