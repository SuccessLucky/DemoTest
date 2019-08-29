//
//  FirstPageVC.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/14.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "FirstPageVC.h"
#import "ScreenThreeCV.h"
#import <MobAPI/MobAPI.h>
#import <CoreLocation/CoreLocation.h>      //添加定位服务头文件（不可缺少）
#import "FirstpageManager.h"
#import "ScreenManager.h"
#import "ScreenAllVC.h"
#import "HUDManager.h"
#import "AlarmHistoryViewController.h"

@interface FirstPageVC ()<CLLocationManagerDelegate>

@property (strong, nonatomic) ScreenThreeCV *screenCV;
@property (nonatomic, strong) CLLocationManager *locationManager;//定位服务管理类
@property (nonatomic, assign) NSInteger locationNumber;
@property (nonatomic, strong) NSDictionary *dic;
@property (strong, nonatomic) FirstPageManager *manager;
@property (strong, nonatomic) ScreenManager *screenManager;
@property (strong, nonatomic) SHAppCommonRequest *getPicManager;
@property (assign, nonatomic) NetworkEngine *networkEngine;

@end

@implementation FirstPageVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.isHideNaviBar = YES;
    [self doInitSubViews];
    [self doRegisterKVO];
    [self doInitGetLocation];
    [self doLoadData];
    [self doAddAction];
//    [self doJudgeTheLocation];
    

    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(doHadDeletedRefrshScreenList)
                                                 name:kScreenDelteShouldRefresh
                                               object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(doHadDeletedRefrshScreenList)
                                                 name:kScreenShouldRefresh
                                               object:nil];
    // Do any additional setup after loading the view.
}

- (void)doHadDeletedRefrshScreenList
{
    [[NSNotificationCenter defaultCenter] removeObserver:kScreenDelteShouldRefresh];
    [[NSNotificationCenter defaultCenter] removeObserver:kScreenShouldRefresh];
    [HUDManager showLoadingHud:@"加载中..."];
    [self.screenManager handleGetScreenListFromNetworkWithType:SHScreenType_All];
}

#pragma mark -
#pragma mark - 数据返回，注册kvo
- (void)doRegisterKVO
{
    @weakify(self);
    [self observeKeyPath:@keypath(self.screenManager.arrScreenList)
                   block:^(id value) {
                       @strongify(self);
                       [HUDManager hidenHud];
                       NSArray *arrScreenList = (NSArray *)value;
                       self.screenCV.arrDataList = arrScreenList;
                   }];
    
    [self observeKeyPath:@keypath(self.screenManager.errorInfo)
                   block:^(id value) {
                       [HUDManager hidenHud];
                       NSDictionary *dict = (NSDictionary *)value;
                       [HUDManager showStateHud:[NSString stringWithFormat:@"%@",dict[@"message"]]  state:HUDStateTypeFail afterDelay:1];
                   }];
    
    //报警信息
    [self observeKeyPath:@keypath(self.manager.arrAlarmInfo)
                   block:^(id value) {
                       @strongify(self);
                       [HUDManager hidenHud];
                       NSArray *arrAlarmInfo = (NSArray *)value;
                       self.screenCV.arrHistory = arrAlarmInfo;
                   }];
    
    [self observeKeyPath:@keypath(self.manager.errorInfo)
                   block:^(id value) {
                       [HUDManager hidenHud];
                       NSDictionary *dict = (NSDictionary *)value;
                       [HUDManager showStateHud:[NSString stringWithFormat:@"%@",dict[@"message"]]  state:HUDStateTypeFail afterDelay:1];
                   }];
    
    //场景触发指令返回
    [self observeKeyPath:@keypath(self.networkEngine.screenNew)
                   block:^(id value)
     {
         if (self.tabBarController.selectedIndex == 0) {
             SHModelScreenNew *screen = value;
             [HUDManager hidenHud];
             if ([screen.strCmdID isEqualToString:@"50"]) {
                 
                 if([screen.strSubcommandIdentifer isEqualToString:@"09"]){
                     if ([screen.strAnswerIdentifer isEqualToString:@"00"]) {
                         [HUDManager showStateHud:@"发送场景命令成功！" state:HUDStateTypeSuccess];
                         NSLog(@"发送场景命令成功！");
                     }
                 }else{
                     NSLog(@"strCmdID == %@,strSubcommandIdentifer==%@,strAnswerIdentifer==%@",screen.strCmdID,screen.strSubcommandIdentifer,screen.strAnswerIdentifer);
                 }
             }else if ([screen.strCmdID isEqualToString:@"D0"]){
                 if([screen.strSubcommandIdentifer isEqualToString:@"09"]){
                     if ([screen.strAnswerIdentifer isEqualToString:@"00"]) {
                         [HUDManager showStateHud:@"发送场景命令成功！" state:HUDStateTypeSuccess];
                         NSLog(@"发送场景命令成功！");
                     }
                 }
             }else{
                 [HUDManager showStateHud:@"未知错误!"  state:HUDStateTypeFail afterDelay:1];
             }
         }
     }];
}


#pragma mark -
#pragma mark - inti
- (void)doInitSubViews
{
    self.networkEngine = [NetworkEngine shareInstance];
    [self.view addSubview:self.screenCV];
}

#pragma mark -
#pragma mark - loadData
- (void)doLoadData
{
    /*创建信号量*/
    dispatch_semaphore_t semaphore = dispatch_semaphore_create(0);
    /*创建全局并行*/
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_group_t group = dispatch_group_create();
    @weakify(self);
    dispatch_group_async(group, queue, ^{
        @strongify(self);
        NSLog(@"拉取报警信息2");
        [self doLoadHistroyData];
        dispatch_semaphore_signal(semaphore);
    });
    
    dispatch_group_async(group, queue, ^{
        @strongify(self);
        NSLog(@"拉取公共图片3");
        [self doGetPubulicUIPicFirst];
        dispatch_semaphore_signal(semaphore);
    });
    dispatch_group_async(group, queue, ^{
        @strongify(self);
        NSLog(@"拉取设备图片4");
        [self doGetDeviceUIPicFirst];
        dispatch_semaphore_signal(semaphore);
    });
    dispatch_group_async(group, queue, ^{
        @strongify(self);
        NSLog(@"拉取房间图片5");
        [self doGetRoomUIPicFirst];
        dispatch_semaphore_signal(semaphore);
    });
    dispatch_group_async(group, queue, ^{
        @strongify(self);
        NSLog(@"拉取场景图片6");
        [self doGetScreenUIPicFirst];
        dispatch_semaphore_signal(semaphore);
    });

    dispatch_group_async(group, queue, ^{
        @strongify(self);
        NSLog(@"拉取场景列表7");
        [self doLoadScreenData];
        dispatch_semaphore_signal(semaphore);
    });
    
    dispatch_group_notify(group, queue, ^{
        /*四个请求对应四次信号等待*/
        dispatch_semaphore_wait(semaphore, DISPATCH_TIME_FOREVER);
        dispatch_semaphore_wait(semaphore, DISPATCH_TIME_FOREVER);
        dispatch_semaphore_wait(semaphore, DISPATCH_TIME_FOREVER);
        dispatch_semaphore_wait(semaphore, DISPATCH_TIME_FOREVER);
        dispatch_semaphore_wait(semaphore, DISPATCH_TIME_FOREVER);
        dispatch_semaphore_wait(semaphore, DISPATCH_TIME_FOREVER);
    });
    
    /*
    self.screenCV.arrDataList = @[@"三大范德萨发生法师打发斯蒂芬",@"三大范德萨发生法师打发斯蒂芬",@"三大范德萨发生法师打发斯蒂芬",@"三大范德萨发生法师打发斯蒂芬",@"三大范德萨发生法师打发斯蒂芬",@"三大范德萨发生法师打发斯蒂芬"];
    
    self.screenCV.arrHistory = @[@"三大范德萨发生法师打发斯蒂芬",@"三大范德萨发生法师打发斯蒂芬",@"三大范德萨发生法师打发斯蒂芬"];
     */
}

#pragma mark -
#pragma mark - add action
- (void)doAddAction
{
    @weakify(self);
    [self.screenCV setBlockCollectionSelected:^(NSIndexPath *indexPath,ScreenThreeCVType cvType,ScreenThreeCVActionType actionType) {
        @strongify(self);
        if (cvType == ScreenThreeCVType_First) {
            
            if (actionType == ScreenThreeCVActionType_NoneData) {
                [self performSegueWithIdentifier:@"SEG_TO_ScreenAllVC" sender:nil];
            }else{
                
                if (self.screenCV.arrDataList.count > 5) {
                    if (indexPath.row == 5) {
                        LLog(@"push 到场景页面");
                        [self performSegueWithIdentifier:@"SEG_TO_ScreenAllVC" sender:nil];
                    }else{
                        
                        //执行场景
                        //触发场景
                        [HUDManager showStateHud:@"加载中..." state:HUDStateTypeSuccess afterDelay:1];
                        ScreenModel *screenModel = self.screenCV.arrDataList[indexPath.row];
                        NSData *data = [[NetworkEngine shareInstance] doHandleSendScreenOrderToControlWithScreenNO:screenModel.str_serial_number];
                        [[NetworkEngine shareInstance] sendRequestData:data];
                    }
                }else if (self.screenCV.arrDataList.count == 5 ){
                    if (indexPath.row == 5) {
                        LLog(@"push 到场景页面");
                        [self performSegueWithIdentifier:@"SEG_TO_ScreenAllVC" sender:nil];
                    }else{
                        //执行场景
                        //触发场景
                        [HUDManager showStateHud:@"加载中..." state:HUDStateTypeSuccess afterDelay:1];
                        ScreenModel *screenModel = self.screenCV.arrDataList[indexPath.row];
                        NSData *data = [[NetworkEngine shareInstance] doHandleSendScreenOrderToControlWithScreenNO:screenModel.str_serial_number];
                        [[NetworkEngine shareInstance] sendRequestData:data];
                    }
                    
                }else if (self.screenCV.arrDataList.count < 5){
                    if (indexPath.row == self.screenCV.arrDataList.count) {
                        LLog(@"push 到场景页面");
                        [self performSegueWithIdentifier:@"SEG_TO_ScreenAllVC" sender:nil];
                    }else{
                        //触发场景
                        [HUDManager showStateHud:@"加载中..." state:HUDStateTypeSuccess afterDelay:1];
                        ScreenModel *screenModel = self.screenCV.arrDataList[indexPath.row];
                        NSData *data = [[NetworkEngine shareInstance] doHandleSendScreenOrderToControlWithScreenNO:screenModel.str_serial_number];
                        [[NetworkEngine shareInstance] sendRequestData:data];
                    }
                    
                }else{
                    LLog(@"push 到场景页面");
                    [self performSegueWithIdentifier:@"SEG_TO_ScreenAllVC" sender:nil];
                }
                
                /*
                if (indexPath.row == self.screenCV.arrDataList.count) {
                    LLog(@"push 到场景页面");
                    [self performSegueWithIdentifier:@"SEG_TO_ScreenAllVC" sender:nil];
                }else{
                    //触发场景
                    [HUDManager showStateHud:@"加载中..." state:HUDStateTypeSuccess afterDelay:1];
                    ScreenModel *screenModel = self.screenCV.arrDataList[indexPath.row];
                    NSData *data = [[NetworkEngine shareInstance] doHandleSendScreenOrderToControlWithScreenNO:screenModel.str_serial_number];
                    [[NetworkEngine shareInstance] sendRequestData:data];
                }
                 */
            }
        }
    }];
    
    [self.screenCV setBlockHistroyPressed:^{
        
        AlarmHistoryViewController *vc = [[AlarmHistoryViewController alloc] init];
        vc.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:vc animated:YES];
        
    }];
}

#pragma mark -
#pragma mark - 获取天气基本接口
- (void)doInitGetLocation
{
    NSLog(@"拉取位置公共信息1");
    _locationManager = [[CLLocationManager alloc] init];//创建CLLocationManager对象
    _locationManager.delegate = self;//设置代理，这样函数didUpdateLocations才会被回调
    [_locationManager setDesiredAccuracy:kCLLocationAccuracyNearestTenMeters];//精确到10米范围
    [_locationManager requestAlwaysAuthorization];
    [_locationManager startUpdatingLocation]; //启动定位服务
    self.locationNumber = 0;
    
}

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    // 设备的当前位置
    CLLocation *currLocation = [locations lastObject];
    
    [_locationManager stopUpdatingLocation];
    CLGeocoder *geocoder = [[CLGeocoder alloc]init];
    [geocoder reverseGeocodeLocation:currLocation completionHandler:^(NSArray<CLPlacemark *> * _Nullable placemarks, NSError * _Nullable error) {
        for (CLPlacemark *place in placemarks) {
            NSDictionary *location = [place addressDictionary];
            NSString *state = [location objectForKey:@"State"];
            NSString *city = [location objectForKey:@"City"];
            NSString *subLocality = [location objectForKey:@"SubLocality"];
            NSString *street = [location objectForKey:@"Street"];
            
            if (city == nil) {
                city = @"";
            }if (subLocality == nil) {
                subLocality = @"";
            }if (street == nil) {
                street = @"";
            }
            NSString *strLocation = [NSString stringWithFormat:@"%@%@%@%@",state,city,subLocality,street];
            [self doSendWeatherWithState:[state substringWithRange:NSMakeRange(0, state.length - 1)] city:[city substringWithRange:NSMakeRange(0, city.length - 1)]];
            
            NSLog(@"strLocation == %@",strLocation);
            
        }
    }];
    
}

- (void)doSendEnvironmentRequestWithState:(NSString *)strState city:(NSString *)strCity
{
    [MobAPI sendRequest:[MOBAEnvironmentRequest environmentRequestByCity:strCity province:strState]
               onResult:^(MOBAResponse *response) {
                   if (response.error)
                   {
                       NSLog(@"request error = %@", response.error);
                   }
                   else
                   {
                       NSLog(@"request success = %@", response.responder);
                   }
               }];
}


- (void)doSendWeatherWithState:(NSString *)strState city:(NSString *)strCity
{
    [MobAPI sendRequest:[MOBAWeatherRequest searchRequestByCity:strCity province:strState]
               onResult:^(MOBAResponse *response) {
                   if (response.error)
                   {
                       NSLog(@"request error = %@", response.error);
                   }
                   else
                   {
                       NSLog(@"request success = %@", response.responder);
                       
                       NSArray *arrResult = response.responder[@"result"];
                       NSDictionary *dictData = arrResult[0];
                       
                       NSArray *arrFuture = dictData[@"future"];
                       NSDictionary *dictFirst = arrFuture[0];
                       NSString *strTemperture = dictFirst[@"temperature"];//气温
//                       NSString *str_airCondition = dictData[@"airCondition"];//空气质量
                       //                       NSString *str_pollutionIndex = dictData[@"pollutionIndex"];//污染系数
                       NSString *str_humidity = dictData[@"humidity"]; //湿度
                       
                       NSString *str_weather = dictData[@"weather"];
                       
                       NSString *strWeek = dictData[@"week"];
                       
                       self.screenCV.strGreetings = [NSString stringWithFormat:@"%@好！",strWeek];
                       self.screenCV.strImageWeather = @"weather";
                       self.screenCV.strWeatherStatus =str_weather;
                       
                       self.screenCV.strTemperature = [NSString stringWithFormat:@"温度：%@",strTemperture];
                       self.screenCV.strHumidity = [NSString stringWithFormat:@"%@",str_humidity];
                   }
                   
               }];
}

#pragma mark -
#pragma mark - 拉取报警记录
- (void)doLoadHistroyData
{
    
    [self.manager doGetAlarmInfoFromNetworkWithTime:[self doGetCurrentTime]];
}

- (NSString *)doGetCurrentTime
{
    NSDate *currentDate = [NSDate date];//获取当前时间，日期
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"YYYY-MM-dd"];
    NSString *dateString = [dateFormatter stringFromDate:currentDate];
    return dateString;
}

#pragma mark - 场景拉取
- (void)doLoadScreenData
{
    [HUDManager showLoadingHud:@"加载中..."];
    [self.screenManager doGetScreenListDataFromDB];
    [self.screenManager handleGetScreenListFromNetworkWithType:SHScreenType_All];
}

#pragma mark - 拉取公共图片
- (void)doGetPubulicUIPicFirst
{
    [self.getPicManager doGetPicListFromNetworkWithTypeID:@"0"
                                           completeHandle:^(BOOL isSucceed, id response)
     {
     }];
}

#pragma mark -拉取设备
- (void)doGetDeviceUIPicFirst
{
    [self.getPicManager doGetPicListFromNetworkWithTypeID:@"1"
                                           completeHandle:^(BOOL isSucceed, id response)
     {
     }];
}

#pragma mark -拉取房间
- (void)doGetRoomUIPicFirst
{
    [self.getPicManager doGetPicListFromNetworkWithTypeID:@"2"
                                           completeHandle:^(BOOL isSucceed, id response)
     {
     }];
}


#pragma mark -拉取场景图片
- (void)doGetScreenUIPicFirst
{
    [self.getPicManager doGetPicListFromNetworkWithTypeID:@"3"
                                           completeHandle:^(BOOL isSucceed, id response)
     {
     }];
}


#pragma mark - 初始化
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
        
        flowLayout.headerReferenceSize = CGSizeMake(UI_SCREEN_WIDTH, 340);
        flowLayout.footerReferenceSize = CGSizeMake(UI_SCREEN_WIDTH, 20);
        
        _screenCV = [[ScreenThreeCV alloc] initWithFrame:CGRectMake(0,
                                                                    0,
                                                                    UI_SCREEN_WIDTH,
                                                                    UI_SCREEN_HEIGHT - 30)
                                    collectionViewLayout:flowLayout withScreenThreeCVTYpe:ScreenThreeCVType_First];
    }
    return _screenCV;
}

#pragma mark - 首页manager
- (FirstPageManager *)manager
{
    if (!_manager) {
        _manager = [FirstPageManager new];
    }
    return _manager;
}

-(ScreenManager *)screenManager
{
    if (!_screenManager) {
        _screenManager = [ScreenManager new];
    }
    return _screenManager;
}

-(SHAppCommonRequest *)getPicManager
{
    if (!_getPicManager) {
        _getPicManager = [SHAppCommonRequest new];
    }
    return _getPicManager;
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
    if ([segue.identifier isEqualToString:@"SEG_TO_ScreenAllVC"]) {
#warning 是否需要处理
        NSLog(@"是否需要把本页面拉取到的场景传递到下一个页面");
    }
}


#pragma mark -
#pragma mark - 是否开启定位功能
#pragma mark 判断是否打开定位

- (void)doJudgeTheLocation
{
    if ([self determineWhetherTheAPPOpensTheLocation]) {
        UIAlertView *alter = [UIAlertView bk_showAlertViewWithTitle:@"提示" message:@"请到设置->隐私->定位服务中开启【学易车】定位服务，以便于距离筛选能够准确获得你的位置信息" cancelButtonTitle:@"取消" otherButtonTitles:@[@"设置"] handler:^(UIAlertView *alertView, NSInteger buttonIndex) {
            if (buttonIndex == 1) {
                [[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString]];
            }
        }];
        [alter show];
    }
}

-(BOOL)determineWhetherTheAPPOpensTheLocation{
    

    if ([CLLocationManager locationServicesEnabled] && ([CLLocationManager authorizationStatus] ==kCLAuthorizationStatusAuthorizedWhenInUse || [CLLocationManager authorizationStatus] ==kCLAuthorizationStatusNotDetermined || [CLLocationManager authorizationStatus] ==kCLAuthorizationStatusAuthorized))
    { return YES;
        
    }else if ([CLLocationManager authorizationStatus] ==kCLAuthorizationStatusDenied) {
        return NO;
        
    }else{
        return NO;
    }
}

@end
