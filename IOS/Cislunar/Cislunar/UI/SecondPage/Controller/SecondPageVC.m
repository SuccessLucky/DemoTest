//
//  SecondPageVC.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/14.
//  Copyright © 2018年 余长涛. All rights reserved.
//
#define WindowsSize [UIScreen mainScreen].bounds.size

#import "SecondPageVC.h"
#import "JXCategoryView.h"
#import "ListViewController.h"
#import "SHFloorManager.h"
#import "SHRoomManager.h"

@interface SecondPageVC () <JXCategoryViewDelegate, UIScrollViewDelegate>

@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;
//特斯拉
@property (nonatomic, strong) JXCategoryTitleView *titleCategoryView;
@property (nonatomic, strong) NSMutableArray <ListViewController *> *listVCArray;

@property (strong, nonatomic) SHFloorManager *floorManager;
@property (strong, nonatomic) SHRoomManager *roomManager;

@property (strong, nonatomic) NSArray *arrRoomsRemember;

@end

@implementation SecondPageVC

- (void)viewDidAppear:(BOOL)animated {
    
    [super viewDidAppear:animated];
    self.tabBarController.tabBar.hidden = NO;
    self.navigationController.interactivePopGestureRecognizer.enabled = (self.titleCategoryView.selectedIndex == 0);
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.isHideNaviBar = YES;
    
    [self doInitSubViews];
    [self doRegisterKVO];
    [self doLoadFloorData];
}

- (void)doLoadFloorData
{
//    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
    [self.floorManager doGetFloorListDataFromDB];
    [self.floorManager doGetFloorListFromNetwork];
}

- (void)doGetRoomListWithFloorID:(int)iFloorID
{
//    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
    [self.roomManager doGetRoomListDataFromDBWithFloorID:iFloorID];
    [self.roomManager doGetRoomListFromNetworkWithFloorID:iFloorID];
}

- (void)doRegisterKVO
{
    @weakify(self);
    [self observeKeyPath:@keypath(self.floorManager.arrFloor)
                   block:^(id value) {
                       @strongify(self);
//                       [XWHUDManager hideInWindow];
                       NSArray *arrFloor = (NSArray *)value;
                       if (arrFloor.count) {
                           SHModelFloor *floor = arrFloor[0];
                           [self doGetRoomListWithFloorID:floor.iFloor_id];
                       }else{
                           
                           [XWHUDManager showCustomTipHUD:@"请到“我的”->“智能管理”页面进行楼层添加"
                                               isLineFeed:YES
                                          backgroundColor:[[UIColor blackColor]colorWithAlphaComponent:0.6]
                                                textColor:[UIColor whiteColor]
                                                 textFont:[UIFont systemFontOfSize:12.0]
                                                   margin:10.0
                                                   offset:CGPointMake(0, 300)
                                                 isWindow:NO];
                       }
                   }];
    
    [self observeKeyPath:@keypath(self.floorManager.errorInfo)
                   block:^(id value) {
                       [XWHUDManager hide];
                       NSDictionary *dict = (NSDictionary *)value;
                       [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",dict[@"message"]]];
                   }];
    
    [self observeKeyPath:@keypath(self.roomManager.arrRoom)
                   block:^(id value) {
                       @strongify(self);
                       [XWHUDManager hide];
                       NSArray *arr = (NSArray *)value;
                       if (arr.count) {
                           self.arrRoomsRemember = arr;
                           [self reloadDataWithArrRooms:arr];
                       }else{
                           
                           [XWHUDManager showCustomTipHUD:@"请到“我的”->“智能管理”->“房间管理”页面进行楼层添加"
                                               isLineFeed:YES
                                          backgroundColor:[[UIColor blackColor] colorWithAlphaComponent:0.6]
                                                textColor:[UIColor whiteColor]
                                                 textFont:[UIFont systemFontOfSize:12.0]
                                                   margin:10.0
                                                   offset:CGPointMake(0, 300)
                                                 isWindow:NO];
                       }
                   }];
    
    [self observeKeyPath:@keypath(self.roomManager.errorInfo)
                   block:^(id value) {
//                       [XWHUDManager hideInWindow];
                       NSDictionary *dict = (NSDictionary *)value;
                       [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",dict[@"message"]]];
                       
                   }];
}



/************************************************************************************************/
/**********************************************特斯拉*********************************************/
/************************************************************************************************/
#pragma mark -
#pragma mark -重载数据源：比如从服务器获取新的数据、否则用户对分类进行了排序等
- (void)reloadDataWithArrRooms:(NSArray *)arrRooms {
    
    NSMutableArray *mutArrTitles = [NSMutableArray new];
    for (int i = 0; i < arrRooms.count; i ++) {
        SHModelRoom *roomModel = arrRooms[i];
        [mutArrTitles addObject:roomModel.strRoom_name];
    }
    NSArray *titles = mutArrTitles;
    
    //先把之前的listView移除掉
    for (UIViewController *vc in self.listVCArray) {
        [vc.view removeFromSuperview];
    }
    [self.listVCArray removeAllObjects];
    
    //根据新的数据源重新添加listView
    for (int i = 0; i < arrRooms.count; i ++) {
        ListViewController *listVC = [[ListViewController alloc] init];
        listVC.modelRoom = arrRooms[i];
        listVC.view.frame = CGRectMake(i*self.scrollView.bounds.size.width, 0, self.scrollView.bounds.size.width, self.scrollView.bounds.size.height);
        [self.scrollView addSubview:listVC.view];
        [self.listVCArray addObject:listVC];
    }
    self.scrollView.contentSize = CGSizeMake(self.scrollView.bounds.size.width*titles.count, self.scrollView.bounds.size.height);
    //重载之后默认回到0，你也可以指定一个index
    self.titleCategoryView.defaultSelectedIndex = 0;
    self.titleCategoryView.titles = titles;
    [self.titleCategoryView reloadData];
    //触发首次加载
    
    SHModelRoom *modelRoom = arrRooms[0];
    
    [self.listVCArray.firstObject doLoadInRoomDeviceListDataWithRoomID:modelRoom.iRoom_id];
}

- (NSArray <NSString *> *)getRandomTitles {
    NSArray *titles = @[@"客厅",
                        @"卧室",
                        @"洗手间",
                        @"书房",
                        @"厨房",
                        @"餐厅"].mutableCopy;
    return titles;
}


#pragma mark -特斯拉
#pragma mark - 特斯拉view
- (void)doInitSubViews
{
    NSUInteger count = 0;
    CGFloat titleCategoryViewHeight = 50;
    CGFloat width = WindowsSize.width;
    CGFloat height =self.scrollView.frame.size.height;
    
    self.scrollView.delegate = self;
    self.scrollView.pagingEnabled = YES;
    self.scrollView.contentSize = CGSizeMake(width*count, height-49);
    self.scrollView.bounces = NO;
    self.scrollView.showsVerticalScrollIndicator = NO;
    [self.view addSubview:self.scrollView];
    
    for (int i = 0; i < count; i ++) {
        ListViewController *listVC = [[ListViewController alloc] init];
        [self addChildViewController:listVC];
        listVC.view.frame = CGRectMake(i*width, 0, width, height);
        [self.scrollView addSubview:listVC.view];
        [self.listVCArray addObject:listVC];
    }
    
    self.titleCategoryView = [[JXCategoryTitleView alloc] init];
    self.titleCategoryView.frame = CGRectMake(0, self.scrollView.frame.origin.y - titleCategoryViewHeight, width, titleCategoryViewHeight);
    self.titleCategoryView.delegate = self;
    self.titleCategoryView.contentScrollView = self.scrollView;
//    self.titleCategoryView.titles = titles;
    self.titleCategoryView.titleColor = [UIColor whiteColor];
    self.titleCategoryView.titleSelectedColor = RGB(69, 182, 250);
    self.titleCategoryView.averageCellSpacingEnabled = NO;
    
    JXCategoryIndicatorLineView *lineView = [[JXCategoryIndicatorLineView alloc] init];
    lineView.indicatorLineWidth = JXCategoryViewAutomaticDimension;
    lineView.indicatorLineViewColor = RGB(69, 182, 250);
    [self.view addSubview:self.titleCategoryView];
    
    JXCategoryIndicatorBackgroundView *backgroundView = [[JXCategoryIndicatorBackgroundView alloc] init];
    backgroundView.backgroundViewColor = [UIColor clearColor];
    backgroundView.backgroundViewWidth = JXCategoryViewAutomaticDimension;
    
    self.titleCategoryView.indicators = @[lineView, backgroundView];
    self.titleCategoryView.contentScrollView = self.scrollView;
    [self.view addSubview:self.titleCategoryView];
    
    UIView *line =  [[UIView alloc] initWithFrame:CGRectMake(0, self.titleCategoryView.frame.origin.y + 50, self.view.frame.size.width, 0.5)];
    line.backgroundColor = [UIColor lightGrayColor];
    [self.view addSubview:line];
    
//    [self.listVCArray.firstObject loadDataForFirst];
}

#pragma mark - JXCategoryViewDelegate
- (void)categoryView:(JXCategoryBaseView *)categoryView didSelectedItemAtIndex:(NSInteger)index {
    //侧滑手势处理
    self.navigationController.interactivePopGestureRecognizer.enabled = (index == 0);
    SHModelRoom *modelRoom = self.arrRoomsRemember[index];
    [self.listVCArray[index] doLoadInRoomDeviceListDataWithRoomID:modelRoom.iRoom_id];
}


- (Class)preferredListViewControllerClass {
    return [ListViewController class];
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    //    if ([self isKindOfClass:[NestViewController class]]) {
    //        CGFloat index = scrollView.contentOffset.x/scrollView.bounds.size.width;
    //        CGFloat absIndex = fabs(index - self.currentIndex);
    //        if (absIndex >= 1) {
    //            //嵌套使用的时候，最外层的VC持有的scrollView在翻页之后，就断掉一次手势。解决快速滑动的时候，只响应最外层VC持有的scrollView。子VC持有的scrollView却没有响应
    //            self.scrollView.panGestureRecognizer.enabled = NO;
    //            self.scrollView.panGestureRecognizer.enabled = YES;
    //            _currentIndex = floor(index);
    //        }
    //    }
}


#pragma mark -
#pragma mark - 懒加载

-(NSArray *)arrRoomsRemember
{
    if (!_arrRoomsRemember) {
        _arrRoomsRemember = [NSArray new];
    }
    return _arrRoomsRemember;
}

- (SHRoomManager *)roomManager
{
    if (!_roomManager) {
        _roomManager = [SHRoomManager new];
    }
    return _roomManager;
}


- (SHFloorManager *)floorManager
{
    if (!_floorManager) {
        _floorManager = [SHFloorManager new];
    }
    return  _floorManager;
}

- (JXCategoryTitleView *)titleCategoryView
{
    if (!_titleCategoryView) {
        _titleCategoryView = [[JXCategoryTitleView alloc] init];
    }
    return _titleCategoryView;
}

- (NSMutableArray<ListViewController *> *)listVCArray
{
    if (!_listVCArray) {
        _listVCArray = [NSMutableArray new];
    }
    return _listVCArray;
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




#pragma mark -
#pragma mark -备用 原始胎
- (void)doInitSubViewsWithRoomList:(NSArray *)arrRooms
{
    
    NSMutableArray *mutArrTitles = [NSMutableArray new];
    for (int i = 0; i < arrRooms.count; i ++) {
        SHModelRoom *roomModel = arrRooms[i];
        [mutArrTitles addObject:roomModel.strRoom_name];
    }
    NSArray *titles = mutArrTitles;
    
    
    NSUInteger count = titles.count;
    CGFloat titleCategoryViewHeight = 50;
    CGFloat width = WindowsSize.width;
    CGFloat height =self.scrollView.frame.size.height;
    
    self.scrollView.delegate = self;
    self.scrollView.pagingEnabled = YES;
    self.scrollView.contentSize = CGSizeMake(width*count, height-49);
    self.scrollView.bounces = NO;
    self.scrollView.showsVerticalScrollIndicator = NO;
    [self.view addSubview:self.scrollView];
    
    for (int i = 0; i < count; i ++) {
        ListViewController *listVC = [[ListViewController alloc] init];
        [self addChildViewController:listVC];
        listVC.view.frame = CGRectMake(i*width, 0, width, height);
        [self.scrollView addSubview:listVC.view];
        [self.listVCArray addObject:listVC];
    }
    
    self.titleCategoryView = [[JXCategoryTitleView alloc] init];
    self.titleCategoryView.frame = CGRectMake(0, self.scrollView.frame.origin.y - titleCategoryViewHeight, width, titleCategoryViewHeight);
    self.titleCategoryView.delegate = self;
    self.titleCategoryView.contentScrollView = self.scrollView;
    self.titleCategoryView.titles = titles;
    self.titleCategoryView.titleColor = [UIColor whiteColor];
    self.titleCategoryView.titleSelectedColor = RGB(69, 182, 250);
    
    JXCategoryIndicatorLineView *lineView = [[JXCategoryIndicatorLineView alloc] init];
    lineView.indicatorLineWidth = JXCategoryViewAutomaticDimension;
    lineView.indicatorLineViewColor = RGB(69, 182, 250);
    [self.view addSubview:self.titleCategoryView];
    
    JXCategoryIndicatorBackgroundView *backgroundView = [[JXCategoryIndicatorBackgroundView alloc] init];
    backgroundView.backgroundViewColor = [UIColor clearColor];
    backgroundView.backgroundViewWidth = JXCategoryViewAutomaticDimension;
    
    self.titleCategoryView.indicators = @[lineView, backgroundView];
    self.titleCategoryView.contentScrollView = self.scrollView;
    [self.view addSubview:self.titleCategoryView];
    
    UIView *line =  [[UIView alloc] initWithFrame:CGRectMake(0, self.titleCategoryView.frame.origin.y + 50, self.view.frame.size.width, 0.5)];
    line.backgroundColor = [UIColor lightGrayColor];
    [self.view addSubview:line];
    
    //    [self.listVCArray.firstObject loadDataForFirst];
}

@end
