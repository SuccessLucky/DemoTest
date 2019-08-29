//
//  FourPageVC.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/14.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "FourPageVC.h"
#import "FourPageTableView.h"
#import "ModelPersonalCenter.h"
#import "PersonalCenterHeaderView.h"
#import "AppDelegate+Camera.h"

@interface FourPageVC ()

@property (strong, nonatomic) FourPageTableView *tableView;

@property (strong, nonatomic) NSArray *arrData;

@property (strong, nonatomic) PersonalCenterHeaderView *fourPageHeaderView;

@end

@implementation FourPageVC

//-(void)viewWillAppear:(BOOL)animated
//{
//    [super viewWillAppear:animated];
//    self.isHideNaviBar = NO;
//}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.isHideNaviBar = YES;
    
    [self doInitSubViews];
    [self doLoadDataSource];
    [self doAddAction];
    
    //增加暗门
    @weakify(self);
    [self.view bk_whenTouches:3 tapped:3 handler:^{
        @strongify(self);
         [XWHUDManager showHUDMessage:@"全部场景删除中..." afterDelay:20];
        [self doDleteScreenWithScreenNo:@"FF"];
    }];
    // Do any additional setup after loading the view.
}

- (void)doDleteScreenWithScreenNo:(NSString *)strScreenNoHex
{
    NSData *dataSend = [[NetworkEngine shareInstance] doHandleSendDleteScreenOrderToControlWithScreenNO:strScreenNoHex];
    [[NetworkEngine shareInstance] sendRequestData:dataSend];
}

- (void)doInitSubViews
{
    [self.view addSubview:self.tableView];
    [self.tableView setTableHeaderView:self.fourPageHeaderView];
}

- (void)doLoadDataSource
{
    self.fourPageHeaderView.labelGatewayName.text = [[SHLoginManager shareInstance] doGetRememerGatewayName];
    NSString *strMemberType = [[SHLoginManager shareInstance] doGetGatewayMemberType];
    if ([strMemberType intValue] == 1) {
        self.fourPageHeaderView.labelRole.text = @"管理员";
    }else{
        self.fourPageHeaderView.labelRole.text = @"子账号";
    }
    
    ModelPersonalCenter *model1 = [ModelPersonalCenter new];
    model1.strTitle = @"切换网关";
    model1.strIcon = @"切换";
    
    ModelPersonalCenter *model2 = [ModelPersonalCenter new];
    model2.strTitle = @"权限管理";
    model2.strIcon = @"权限";
    
    ModelPersonalCenter *model3 = [ModelPersonalCenter new];
    model3.strTitle = @"智能管理";
    model3.strIcon = @"管理";
    
    ModelPersonalCenter *model4 = [ModelPersonalCenter new];
    model4.strTitle = @"退出登录";
    model4.strIcon = @"退出";
    
    self.arrData = @[model1,model2,model3,model4];
    self.tableView.arrData =self.arrData;
}

- (void)doAddAction
{
    @weakify(self);
    [self.tableView setBlockTableviewDideSelectedRowPressed:^(NSInteger indexPathRow) {
        @strongify(self);
        switch (indexPathRow) {
            case 0:
            {
                //切换网关
                [self performSegueWithIdentifier:@"seg_to_GatewaSwitchVC" sender:self];
            }
                break;
            case 1:
            {
                //权限管理
                 [self performSegueWithIdentifier:@"seg_to_SHMemberManagementVC" sender:self];
            }
                break;
            case 2:
            {
                //智能管理
                [self performSegueWithIdentifier:@"seg_to_FloorManagementVC" sender:self];
            }
                break;
            case 3:
            {
                //退出登录
                [[AppDelegate sharedAppDelegate] doLogoutClearAll];
            }
                break;
                
            default:
                break;
        }
    }];
}

-(FourPageTableView *)tableView
{
    if (!_tableView) {
        _tableView = [[FourPageTableView alloc] initWithFrame:CGRectMake(0,
                                                                         0,
                                                                         UI_SCREEN_WIDTH,
                                                                         UI_SCREEN_HEIGHT)
                                                        style:UITableViewStyleGrouped];
    }
    return _tableView;
}

- (void)doAddScreenCollectionViewConstraints
{
    @weakify(self);
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.top.equalTo(self.view.mas_top);
        make.left.equalTo(self.view.mas_left);
        make.right.equalTo(self.view.mas_right);
        make.bottom.equalTo(self.view.mas_bottom);
    }];
}

-(PersonalCenterHeaderView *)fourPageHeaderView
{
    if (!_fourPageHeaderView) {
        _fourPageHeaderView = [[[NSBundle mainBundle] loadNibNamed:@"PersonalCenterHeaderView"
                                                             owner:self
                                                           options:nil] lastObject];
    }
    return _fourPageHeaderView;
}

//- (UIView *)doGetTableHeaderView
//{
//    PersonalCenterHeaderView *fourPageHeaderView = [[[NSBundle mainBundle] loadNibNamed:@"PersonalCenterHeaderView"
//                                                                           owner:self
//                                                                         options:nil] lastObject];
//    return fourPageHeaderView;
//
//
//}

-(NSArray *)arrData
{
    if (!_arrData) {
        _arrData = [NSArray new];
    }
    return _arrData;
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
