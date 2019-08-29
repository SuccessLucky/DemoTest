//
//  EZMessageListViewController.m
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/11/3.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import "EZMessageListViewController.h"

#import "MJRefresh.h"
#import "DDKit.h"
#import "MessageListCell.h"
#import "Masonry.h"
#import "MBProgressHUD.h"
#import "EZMessagePhotoViewController.h"
#import "NSDate-Utilities.h"

#define EZMessageListPageSize 10

@interface EZMessageListViewController ()<UIActionSheetDelegate>
{
}

@property (nonatomic) BOOL isSelectedAll;
@property (nonatomic, strong) NSMutableArray *selectedMessageArray;
@property (nonatomic, strong) NSDate *lastDate;
@property (nonatomic, strong) NSDate *beginTime;
@property (nonatomic, strong) NSDate *endTime;
@property (nonatomic) NSInteger totalCount;
@property (nonatomic) NSInteger currentIndex;
@property (nonatomic, strong) NSMutableArray *messageList;
@property (nonatomic, strong) NSDateFormatter *dateFormatter;
@property (nonatomic, strong) NSMutableArray *sections;
@property (nonatomic) NSInteger lastIndex;
@property (nonatomic, weak) IBOutlet UIImageView *noMessage;
@property (nonatomic, weak) IBOutlet UILabel *noMessageLabel;
@property (nonatomic, weak) IBOutlet UIBarButtonItem *selectedAll;
@property (nonatomic, weak) IBOutlet UIBarButtonItem *deleteSelected;
@property (nonatomic, weak) IBOutlet UIBarButtonItem *readAll;
@property (nonatomic, strong) IBOutlet UIBarButtonItem *editButton;


@end

@implementation EZMessageListViewController

- (void)dealloc
{
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    
    self.title = NSLocalizedString(@"dmessage_title", @"消息");
    [EZOPENSDK getUnreadMessageCount:self.deviceInfo.deviceSerial messageType:EZMessageTypeAlarm completion:^(NSInteger count, NSError *error) {
        self.title = [NSString stringWithFormat:@"%@（%d）",NSLocalizedString(@"dmessage_title", @"消息"), (int)count];
    }];
    
    if(!_messageList)
        _messageList = [NSMutableArray new];
    
    if(!_sections)
        _sections = [NSMutableArray new];
    
    if(!_selectedMessageArray)
        _selectedMessageArray = [NSMutableArray new];
    
    [self addHeaderRefresh];
    
    //获取过去3天的告警消息，开发者可以自己设置时间范围。
    _beginTime = [NSDate dateWithTimeIntervalSinceNow:-3600 * 24 * 7];
    _endTime = [NSDate date];
    
    if(!_dateFormatter)
        _dateFormatter = [[NSDateFormatter alloc] init];
    
    self.navigationItem.rightBarButtonItem = nil;
}

- (void)viewWillDisappear:(BOOL)animated {
    if (self.tableView.allowsMultipleSelectionDuringEditing) {
        [self editTableView:self.navigationItem.rightBarButtonItem];
    }
    [super viewWillDisappear:animated];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return _sections.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (_sections.count == 1)
    {
        return _messageList.count;
    }
    else
    {
        if (section == 0) {
            return [_sections[1][@"index"] integerValue];
        }
        else if (section == _sections.count - 1)
        {
            return _messageList.count - [_sections[section][@"index"] integerValue];
        }
        else
        {
            return [_sections[section + 1][@"index"] integerValue] - [_sections[section][@"index"] integerValue];
        }
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    // Configure the cell...
    MessageListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MessageCell" forIndexPath:indexPath];
    cell.deviceSerial = self.deviceInfo.deviceSerial;
    EZAlarmInfo *info = [_messageList dd_objectAtIndex:[[_sections dd_objectAtIndex:indexPath.section][@"index"] integerValue] + indexPath.row];
    [cell setAlarmInfo:info];
    if (tableView.allowsMultipleSelectionDuringEditing == YES) {
        if([_selectedMessageArray containsObject:info])
        {
            [tableView selectRowAtIndexPath:indexPath animated:NO scrollPosition:UITableViewScrollPositionNone];
        }
        else
        {
            [tableView deselectRowAtIndexPath:indexPath animated:NO];
        }
    }
    return cell;
}

// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the specified item to be editable.
    return YES;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
     EZAlarmInfo *info = [_messageList dd_objectAtIndex:[[_sections dd_objectAtIndex:indexPath.section][@"index"] integerValue] + indexPath.row];
    if(tableView.allowsMultipleSelectionDuringEditing)
    {
        if ([self.selectedMessageArray containsObject:info])
        {
            [self.selectedMessageArray removeObject:info];
        }
        else
        {
            [self.selectedMessageArray addObject:info];
        }
        if(self.selectedMessageArray.count > 0)
        {
            self.deleteSelected.title = [NSString stringWithFormat:@"%@(%d)",NSLocalizedString(@"delete", @"删除"),(int)[self.selectedMessageArray count]];
            self.deleteSelected.enabled = YES;
            self.readAll.title = [NSString stringWithFormat:@"%@(%d)",NSLocalizedString(@"dmessage_read", @"标记已读"),(int)[self.selectedMessageArray count]];
            self.readAll.enabled = YES;
        }
        else
        {
            self.deleteSelected.title = NSLocalizedString(@"delete", @"删除");
            self.deleteSelected.enabled = NO;
            self.readAll.title = NSLocalizedString(@"dmessage_read", @"标记已读");
            self.readAll.enabled = NO;
        }
        return;
    }
    MessageListCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    [self performSegueWithIdentifier:@"go2MessagePhoto" sender:@{@"image":cell.actionImageView.image?:[UIImage new],
                                                                 @"alarmInfo":info}];
    [EZOPENSDK getUnreadMessageCount:self.deviceInfo.deviceSerial messageType:EZMessageTypeAlarm completion:^(NSInteger count, NSError *error) {
        self.title = [NSString stringWithFormat:@"%@（%d）",NSLocalizedString(@"dmessage_title", @"消息"), (int)count];
    }];
}

- (void)tableView:(UITableView *)tableView didDeselectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(tableView.allowsMultipleSelectionDuringEditing)
    {
        EZAlarmInfo *info = [_messageList dd_objectAtIndex:[[_sections dd_objectAtIndex:indexPath.section][@"index"] integerValue] + indexPath.row];
        if ([self.selectedMessageArray containsObject:info])
        {
            [self.selectedMessageArray removeObject:info];
        }
        else
        {
            [self.selectedMessageArray addObject:info];
        }
        if(self.selectedMessageArray.count > 0)
        {
            self.deleteSelected.title = [NSString stringWithFormat:@"%@(%d)",NSLocalizedString(@"delete", @"删除"),(int)[self.selectedMessageArray count]];
            self.deleteSelected.enabled = YES;
            self.readAll.title = [NSString stringWithFormat:@"%@(%d)",NSLocalizedString(@"dmessage_read", @"标记已读"),(int)[self.selectedMessageArray count]];
            self.readAll.enabled = YES;
        }
        else
        {
            self.deleteSelected.title = NSLocalizedString(@"delete", @"删除");
            self.deleteSelected.enabled = NO;
            self.readAll.title = NSLocalizedString(@"dmessage_read", @"标记已读");
            self.readAll.enabled = NO;
        }
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 75.0f;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 20.0f;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UILabel *headerLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, self.view.bounds.size.width, 20)];
    NSString *key = [self.sections dd_objectAtIndex:section][@"date"];
    headerLabel.text = [NSString stringWithFormat:@"  %@",[self dateStringWithUserDefine:key]];
    headerLabel.font = [UIFont systemFontOfSize:14.0f];
    headerLabel.backgroundColor = [UIColor dd_hexStringToColor:@"0xf0f0f3"];
    return headerLabel;
}

// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        EZAlarmInfo *info = [_messageList dd_objectAtIndex:[[_sections dd_objectAtIndex:indexPath.section][@"index"] integerValue] + indexPath.row];
        MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        hud.labelText = NSLocalizedString(@"message_deleting", @"正在删除，请稍候...");
        [EZOPENSDK deleteAlarm:@[info.alarmId] completion:^(NSError *error) {
            [hud hide:YES];
            [_messageList removeObject:info];
            [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
        }];
    }
}

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath {
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

#pragma mark - UIActionSheetDelegate Methods

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == 0)
    {
        __weak MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        hud.labelText = NSLocalizedString(@"message_deleting", @"正在删除，请稍候...");
        NSMutableArray *alarmIds = [NSMutableArray new];
        for (int i = 0; i < self.selectedMessageArray.count; i++) {
            EZAlarmInfo *info = [self.selectedMessageArray dd_objectAtIndex:i];
            [alarmIds addObject:info.alarmId];
        }
        [EZOPENSDK deleteAlarm:alarmIds completion:^(NSError *error) {
            if(error)
            {
                hud.labelText = error.localizedDescription;
                hud.mode = MBProgressHUDModeText;
                [hud hide:YES afterDelay:1.2];
            }
            else
            {
                [hud hide:YES];
            }
            [self.messageList removeObjectsInArray:self.selectedMessageArray];
            [self editTableView:self.navigationItem.rightBarButtonItem];
            if(self.messageList.count > 0){
                [self.tableView reloadData];
            }
            else
            {
                [self.tableView.header beginRefreshing];
            }
        }];
    }
}

#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    NSDictionary *dict = sender;
    EZAlarmInfo *info = dict[@"alarmInfo"];
    if(!info.isRead){
        [EZOPENSDK setAlarmStatus:@[info.alarmId] alarmStatus:EZMessageStatusRead completion:^(NSError *error) {
            if(!error)
            {
                info.isRead = YES;
                [self.tableView reloadData];
            }
        }];
    }
    EZMessagePhotoViewController *nextVC = [segue destinationViewController];
    nextVC.image = dict[@"image"];
    nextVC.info = info;
    nextVC.deviceInfo = self.deviceInfo;
}

#pragma mark - MJRefresh Methods

- (void)addHeaderRefresh
{
    __weak typeof(self) weakSelf = self;
    self.tableView.header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
        weakSelf.currentIndex = 0;
        [weakSelf.noMessage removeFromSuperview];
        [weakSelf.noMessageLabel removeFromSuperview];
        [EZOPENSDK getAlarmList:weakSelf.deviceInfo.deviceSerial
                      pageIndex:weakSelf.currentIndex++
                       pageSize:EZMessageListPageSize
                      beginTime:weakSelf.beginTime
                        endTime:weakSelf.endTime
//                      beginTime:nil
//                        endTime:nil
                     completion:^(NSArray *alarmList, NSInteger alarmCount, NSError *error) {
                         [weakSelf.messageList removeAllObjects];
                         [weakSelf.messageList addObjectsFromArray:alarmList];
                         weakSelf.totalCount = alarmCount;
                         if(weakSelf.messageList.count != weakSelf.totalCount)
                         {
                             [weakSelf addFooter];
                         }
                         [weakSelf tableViewDidReload:alarmList];
                         [weakSelf.tableView.header endRefreshing];
                         if(weakSelf.messageList.count > 0)
                             weakSelf.navigationItem.rightBarButtonItem = self.editButton;
                     }];

    }];
    self.tableView.header.automaticallyChangeAlpha = YES;
    [self.tableView.header beginRefreshing];
}

- (void)addFooter
{
    __weak typeof(self) weakSelf = self;
    self.tableView.footer  = [MJRefreshAutoNormalFooter footerWithRefreshingBlock:^{
        [EZOPENSDK getAlarmList:weakSelf.deviceInfo.deviceSerial
                      pageIndex:weakSelf.currentIndex++
                       pageSize:EZMessageListPageSize
                      beginTime:weakSelf.beginTime
                        endTime:weakSelf.endTime
                     completion:^(NSArray *alarmList, NSInteger alarmCount, NSError *error) {
                         [weakSelf.messageList addObjectsFromArray:alarmList];
                         if(weakSelf.messageList.count >= weakSelf.totalCount)
                         {
                             weakSelf.tableView.footer.hidden = YES;
                             return;
                         }
                         [weakSelf tableViewDidReload:alarmList];
                         [weakSelf.tableView.footer endRefreshing];
                     }];

    }];
}

#pragma mark - Action Methods

- (void)tableViewDidReload:(NSArray *)messageList
{
    if(messageList.count == 0)
    {
        self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        [self.tableView reloadData];
        [self.tableView addSubview:self.noMessage];
        [self.tableView addSubview:self.noMessageLabel];
        
        self.noMessage.frame = CGRectMake((self.tableView.bounds.size.width - 94)/2.0, self.tableView.center.y - 150, 94, 94);
        self.noMessageLabel.frame = CGRectMake((self.tableView.frame.size.width - 100)/2.0, self.noMessage.frame.origin.y + self.noMessage.frame.size.height + 10, 100, 20.0);
        return;
    }
    if(self.currentIndex == 1)
    {
        [self.sections removeAllObjects];
        [self.tableView.footer endRefreshing];
        self.lastDate = [messageList[0] alarmStartTime];
        self.dateFormatter.dateFormat = @"yyyy-MM-dd";
        NSString *key = [self.dateFormatter stringFromDate:self.lastDate];
        NSDictionary *dict = @{@"index":@0, @"date":key};
        [self.sections addObject:dict];
    }
    for (int i = 0; i < messageList.count; i++) {
        EZAlarmInfo *info = [messageList dd_objectAtIndex:i];
        if(![info.alarmStartTime isSameToDate:self.lastDate])
        {
            NSInteger index = [self.messageList indexOfObject:info];
            self.dateFormatter.dateFormat = @"yyyy-MM-dd";
            NSString *key = [self.dateFormatter stringFromDate:info.alarmStartTime];
            NSDictionary *dict = @{@"index":@(index),@"date":key};
            [self.sections addObject:dict];
        }
        self.lastDate = info.alarmStartTime;
    }
    [self.tableView reloadData];
}

- (IBAction)editTableView:(id)sender
{
    if(self.tableView.editing){
        self.tableView.allowsMultipleSelectionDuringEditing = NO;
        [self.tableView setEditing:NO animated:YES];
        self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemEdit target:self action:@selector(editTableView:)];
        [self.navigationController setToolbarHidden:YES animated:YES];
        self.deleteSelected.title = NSLocalizedString(@"delete", @"删除");
        self.deleteSelected.enabled = NO;
        self.readAll.title = NSLocalizedString(@"dmessage_read", @"标记已读");
        self.readAll.enabled = NO;
        [self.selectedMessageArray removeAllObjects];
        self.tableView.header.hidden = NO;
        if(self.messageList.count < self.totalCount)
            self.tableView.footer.hidden = NO;
    }else{
        self.tableView.allowsMultipleSelectionDuringEditing = YES;
        [self.tableView setEditing:YES animated:YES];
        self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(editTableView:)];
        [self.navigationController setToolbarHidden:NO animated:YES];
        self.tableView.header.hidden = YES;
        self.tableView.footer.hidden = YES;
        self.selectedAll.enabled = YES;
        self.deleteSelected.enabled = NO;
        self.readAll.enabled = NO;
        if (self.messageList.count > 10)
        {
            self.selectedAll.enabled = NO;
        }
    }
}

- (IBAction)selectedAll:(id)sender
{
    _isSelectedAll = !_isSelectedAll;
    if(_isSelectedAll)
    {
        [self.selectedMessageArray removeAllObjects];
        [self.selectedMessageArray addObjectsFromArray:self.messageList];
        self.deleteSelected.title = [NSString stringWithFormat:@"%@(%d)",NSLocalizedString(@"delete", @"删除"),(int)[self.selectedMessageArray count]];
        self.deleteSelected.enabled = YES;
        self.readAll.title = [NSString stringWithFormat:@"%@(%d)",NSLocalizedString(@"dmessage_read", @"标记已读"),(int)[self.selectedMessageArray count]];
        self.readAll.enabled = YES;
    }
    else
    {
        self.deleteSelected.title = NSLocalizedString(@"delete", @"删除");
        self.readAll.title = NSLocalizedString(@"dmessage_read", @"标记已读");
        [self.selectedMessageArray removeAllObjects];
        self.deleteSelected.enabled = NO;
        self.readAll.enabled = NO;
    }
    [self.tableView reloadData];
    self.tableView.footer.hidden = YES;
}

- (IBAction)deleteMessage:(id)sender
{
    if(self.selectedMessageArray.count > 10)
    {
        MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:[UIApplication sharedApplication].keyWindow animated:YES];
        hud.mode = MBProgressHUDModeText;
        hud.userInteractionEnabled = NO;
        hud.labelText = NSLocalizedString(@"message_count_limit", @"数量不能超过10");
        [hud show:YES];
        [hud hide:YES afterDelay:1.0f];
        return;
    }
    UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:NSLocalizedString(@"cancel", @"取消") destructiveButtonTitle:NSLocalizedString(@"delete", @"删除") otherButtonTitles:nil];
    [actionSheet showInView:self.view];
}

- (IBAction)readMessage:(id)sender
{
    if(self.selectedMessageArray.count > 10)
    {
        MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:[UIApplication sharedApplication].keyWindow animated:YES];
        hud.mode = MBProgressHUDModeText;
        hud.userInteractionEnabled = NO;
        hud.labelText = NSLocalizedString(@"message_count_limit", @"数量不能超过10");
        [hud show:YES];
        [hud hide:YES afterDelay:1.0f];
        return;
    }
    __weak MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:[UIApplication sharedApplication].keyWindow animated:YES];
    hud.userInteractionEnabled = NO;
    hud.labelText = NSLocalizedString(@"message_setting", @"正在设置已读，请稍候...");
    NSMutableArray *alarmIds = [NSMutableArray new];
    for (int i = 0; i < self.selectedMessageArray.count; i++) {
        EZAlarmInfo *info = [self.selectedMessageArray dd_objectAtIndex:i];
        [alarmIds addObject:info.alarmId];
    }
    [EZOPENSDK setAlarmStatus:alarmIds
                  alarmStatus:EZMessageStatusRead
                   completion:^(NSError *error) {
                       if(error)
                       {
                           hud.labelText = error.localizedDescription;
                           hud.mode = MBProgressHUDModeText;
                           [hud hide:YES afterDelay:1.2];
                       }
                       else
                       {
                           [hud hide:YES];
                       }
                       if(!error)
                       {
                           for (int i = 0; i < self.selectedMessageArray.count; i++) {
                               EZAlarmInfo *info = [self.selectedMessageArray dd_objectAtIndex:i];
                               info.isRead = YES;
                           }
                           [self editTableView:self.navigationItem.rightBarButtonItem];
                           [self.tableView reloadData];
                           [EZOPENSDK getUnreadMessageCount:self.deviceInfo.deviceSerial messageType:EZMessageTypeAlarm completion:^(NSInteger count, NSError *error) {
                               self.title = [NSString stringWithFormat:@"%@（%d）",NSLocalizedString(@"message_title", @"消息"), (int)count];
                           }];
                       }
    }];
}

- (NSString *)dateStringWithUserDefine:(NSString *)dateString
{
    NSDateFormatter * formatter = [[NSDateFormatter alloc ] init];
    [formatter setDateFormat:@"yyyy-MM-dd"];
    NSDate *date = [formatter dateFromString:dateString];
    
    NSArray *weekDays = @[NSLocalizedString(@"message_sunday", @"周日"),
                          NSLocalizedString(@"message_monday", @"周一"),
                          NSLocalizedString(@"message_tuesday", @"周二"),
                          NSLocalizedString(@"message_wednesday", @"周三"),
                          NSLocalizedString(@"message_thursday", @"周四"),
                          NSLocalizedString(@"message_friday", @"周五"),
                          NSLocalizedString(@"message_saturday", @"周六")];
    
    if ([date isThisYear])
    {
        if ([date isToday])
        {
            return NSLocalizedString(@"message_today", @"今天");
        }
        else if ([date weekday] > 0 && [date weekday] <= 7)
        {
            return [NSString stringWithFormat:@"%ld-%ld %@", (long)[date month], (long)[date day], weekDays[[date weekday]-1]];
        }
    }
    else
    {
        if ([date weekday] > 0 && [date weekday] <= 7)
        {
            return [NSString stringWithFormat:@"%@ %@", dateString, weekDays[[date weekday]-1]];
        }
    }
    
    return nil;
}

@end
