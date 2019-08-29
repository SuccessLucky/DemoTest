//
//  ScreenAllVC.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/18.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "ScreenAllVC.h"
#import "ScreenThreeCV.h"
#import "ScreenManager.h"
#import "ScreenEditVC.h"

@interface ScreenAllVC ()

@property (strong, nonatomic) ScreenThreeCV *screenCV;
@property (assign, nonatomic) NetworkEngine *networkEngine;
@property (strong, nonatomic) ScreenManager *screenManager;

@end

@implementation ScreenAllVC

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
//    self.isHideNaviBar = YES;
    NSLog(@"wo 要的====%lu",self.tabBarController.selectedIndex);
    NSData *dataread = [[NetworkEngine shareInstance] doReadTheCurrentTimeFromGateway];
    [[NetworkEngine shareInstance] sendRequestData:dataread];
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self doInitSubViews];
    [self doRegisterKVO];
    [self doLoadData];
    [self doAddAction];
    
//    NSData *dataSend = [[NetworkEngine shareInstance] doHandleGetScreenInGatewayCount];
//    [[NetworkEngine shareInstance] sendRequestData:dataSend];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(doHadDeletedRefrshScreenList)
                                                 name:kScreenDelteShouldRefresh
                                               object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(doHadDeletedRefrshScreenList)
                                                 name:kScreenShouldRefresh
                                               object:nil];
}

- (void)doHadDeletedRefrshScreenList
{
    [[NSNotificationCenter defaultCenter] removeObserver:kScreenDelteShouldRefresh];
    [[NSNotificationCenter defaultCenter] removeObserver:kScreenShouldRefresh];
    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
    [self.screenManager handleGetScreenListFromNetworkWithType:SHScreenType_All];
}

#pragma mark -
#pragma mark - 初始化UI
- (void)doInitSubViews
{
    self.tabBarController.tabBar.hidden = !self.tabBarController.tabBar.hidden;
    self.networkEngine = [NetworkEngine shareInstance];
    [self setTitleViewText:@"场景"];
    self.isHideNaviBar = YES;
    [self.view addSubview:self.screenCV];
}


#pragma mark -
#pragma mark - 监听数据上报
- (void)doRegisterKVO
{
    @weakify(self);
    [self observeKeyPath:@keypath(self.screenManager.arrScreenList)
                   block:^(id value) {
                       @strongify(self);
                       [XWHUDManager hideInWindow];
                       NSArray *arrScreenList = (NSArray *)value;
                       self.screenCV.arrDataList = arrScreenList;
                   }];
    
    [self observeKeyPath:@keypath(self.screenManager.errorInfo)
                   block:^(id value) {
                       [XWHUDManager hideInWindow];
                       NSDictionary *dict = (NSDictionary *)value;
                       [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",dict[@"message"]]];
                   }];
    
    //场景触发指令返回
    [self observeKeyPath:@keypath(self.networkEngine.screenNew)
                   block:^(id value)
     {
         if (self.tabBarController.selectedIndex == 0) {
             SHModelScreenNew *screen = value;
             [XWHUDManager hideInWindow];
             if ([screen.strCmdID isEqualToString:@"50"]) {
                 
                 if([screen.strSubcommandIdentifer isEqualToString:@"09"]){
                     if ([screen.strAnswerIdentifer isEqualToString:@"00"]) {
                          [XWHUDManager showSuccessTipHUD:@"发送场景命令成功！"];
                         NSLog(@"发送场景命令成功！");
                     }
                 }else{
                     NSLog(@"strCmdID == %@,strSubcommandIdentifer==%@,strAnswerIdentifer==%@",screen.strCmdID,screen.strSubcommandIdentifer,screen.strAnswerIdentifer);
                 }
             }else if ([screen.strCmdID isEqualToString:@"D0"]){
                 if([screen.strSubcommandIdentifer isEqualToString:@"09"]){
                     if ([screen.strAnswerIdentifer isEqualToString:@"00"]) {
                          [XWHUDManager showSuccessTipHUD:@"发送场景命令成功！"];
                         NSLog(@"发送场景命令成功！");
                     }
                 }
             }else{
                 [XWHUDManager showWarningTipHUD:@"未知错误!"];
             }
         }
     }];
}

#pragma mark -
#pragma mark - 拉取数据刷新UI
- (void)doLoadData
{
    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
    [self.screenManager doGetScreenListDataFromDB];
    [self.screenManager handleGetScreenListFromNetworkWithType:SHScreenType_All];
}

#pragma mark -
#pragma mark -action
- (IBAction)btnBackPressed:(UIButton *)sender {
    self.tabBarController.tabBar.hidden = !self.tabBarController.tabBar.hidden;
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)btnAddScreenPressed:(UIButton *)sender {
    LLog(@"添加场景");
     [self performSegueWithIdentifier:@"SEG_TO_ScreenEditVC" sender:@(ScreenEditVCType_Add)];
}

- (void)doAddAction
{
    @weakify(self);
    [self.screenCV setBlockCollectionSelected:^(NSIndexPath *indexPath,ScreenThreeCVType cvType,ScreenThreeCVActionType actionType) {
        @strongify(self);
        if (cvType == ScreenThreeCVType_All) {
            if (actionType == ScreenThreeCVActionType_LongPressed) {
                //长按进行 编辑和删除
                [self doEditTheScreen:indexPath];
            }else{
                //短按触发场景
                ScreenModel *screenModel = self.screenCV.arrDataList[indexPath.row];
                if (screenModel.str_serial_number.length) {
                    //如果存在ScreenNo,则进行如下
                    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
                    NSData *data = [[NetworkEngine shareInstance] doHandleSendScreenOrderToControlWithScreenNO:screenModel.str_serial_number];
                    [[NetworkEngine shareInstance] sendRequestData:data];
                }
            }
        }
    }];
}

#pragma mark - 弹出的actionSheet
- (void)doEditTheScreen:(NSIndexPath *)indexPath
{
    @weakify(self);
    LCActionSheet *actionSheet = [LCActionSheet sheetWithTitle:@"请选择你要进行的操作"
                                             cancelButtonTitle:@"取消"
                                                       clicked:^(LCActionSheet *actionSheet, NSInteger buttonIndex) {
        
    } otherButtonTitles:@"编辑", @"删除",nil];
    
    actionSheet.didDismissHandler = ^(LCActionSheet * _Nonnull actionSheet, NSInteger buttonIndex) {
        @strongify(self);
        switch (buttonIndex) {
            case 1:
            {
                ScreenModel *model = self.screenCV.arrDataList[indexPath.row];
                [self performSegueWithIdentifier:@"SEG_TO_ScreenEditVC" sender:model];
            }
                break;
            case 2:
            {
                ScreenModel *screenModel = self.screenCV.arrDataList[indexPath.row];
                [self.screenManager handleDeleteScreenDataWithScreenID:screenModel.iScreen_scene_id
                                                        completeHandle:^(BOOL success, id result)
                 {
                     if (success) {
                         //删除网关的
                         [self doDleteScreenWithScreenNo:screenModel.str_serial_number];
                         [self doLoadData];
                         [[NSNotificationCenter defaultCenter] postNotificationName:kScreenDelteShouldRefresh
                                                                             object:nil
                                                                           userInfo:nil];
                     }else{
                         [XWHUDManager showErrorTipHUD:@"删除失败"];
                     }
                 }];
            }
                break;
            case 0:
            {
                NSLog(@"点了取消");
            }
                break;
            default:
                break;
        }
    };
    [actionSheet show];
}

#pragma mark -
#pragma mark - 删除场景
- (void)doDleteScreenWithScreenNo:(NSString *)strScreenNoHex
{
    NSData *dataSend = [[NetworkEngine shareInstance] doHandleSendDleteScreenOrderToControlWithScreenNO:strScreenNoHex];
    [[NetworkEngine shareInstance] sendRequestData:dataSend];
}


#pragma mark -int
#pragma mark - screenCollectionView
- (ScreenThreeCV *)screenCV
{
    if (!_screenCV) {
        UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
        flowLayout.sectionHeadersPinToVisibleBounds = YES;
        [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
        flowLayout.itemSize = CGSizeMake(kSTCVItemWidth, kSTCVItemHeight);
        flowLayout.minimumLineSpacing = kSTCVMinimumLineSpacing;
        flowLayout.minimumInteritemSpacing = kSTCVMinimumInteritemSpacing;
        flowLayout.sectionInset = UIEdgeInsetsMake(kSTCVFromTop, kSTCVFromLeft,kSTCVFromBottom, kSTCVFromRight);
        
        flowLayout.headerReferenceSize = CGSizeMake(UI_SCREEN_WIDTH, 60);
        flowLayout.footerReferenceSize = CGSizeMake(UI_SCREEN_WIDTH, 20);
        
        _screenCV = [[ScreenThreeCV alloc] initWithFrame:CGRectMake(0,
                                                                    64,
                                                                    UI_SCREEN_WIDTH,
                                                                    UI_SCREEN_HEIGHT -64)
                                    collectionViewLayout:flowLayout withScreenThreeCVTYpe:ScreenThreeCVType_All];
    }
    return _screenCV;
}

#pragma mark -ScreenManager
-(ScreenManager *)screenManager
{
    if (!_screenManager) {
        _screenManager = [ScreenManager new];
    }
    return _screenManager;
}


#pragma mark -
#pragma mark - other
- (NSString *)doGetScreenNoHEXType
{
    NSMutableArray *mutTemp = [NSMutableArray new];
    if (self.screenManager.arrScreenList.count == 0) {
        return @"01";
    }else{
        for (int i = 0; i < self.screenManager.arrScreenList.count; i ++) {
            ScreenModel *model = self.screenManager.arrScreenList[i];
            NSString *stNOBigHEx  = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:model.str_serial_number];
            [mutTemp addObject:stNOBigHEx];
        }
        for (int i = 1; i <= 45; i ++) {
            
            NSString *strHexValue = [[ToolHexManager sharedManager] converIntToHex:i];
            NSString *strBigSytemState  = [[ToolHexManager sharedManager] doMakeUpperCaseAndAddSpace:strHexValue];
            if (![mutTemp containsObject:strBigSytemState]) {
                return strBigSytemState;
            }
        }
        return @"2D";
    }
    
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    if ([segue.identifier isEqualToString:@"SEG_TO_ScreenEditVC"]){
        
        ScreenEditVC *vc = (ScreenEditVC *)segue.destinationViewController;
        
        
        if ([sender isKindOfClass:[ScreenModel class]]) {
            
            ScreenModel *screenModel =  (ScreenModel *)sender;
            vc.vcType = ScreenEditVCType_Edit;
            vc.screenModel = screenModel;
            vc.strShouldGiveScreenNoHexTransi = screenModel.str_serial_number;
        }else{
            vc.vcType = ScreenEditVCType_Add;
            vc.strShouldGiveScreenNoHexTransi = [self doGetScreenNoHEXType];
            
        }
    }
}

@end
