//
//  LockViewController.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/16.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "LockViewController.h"
#import "LockNumCollectionView.h"
#import "LockBindMemberVC.h"
#import "SHLockManager.h"

typedef NS_ENUM(NSInteger, SHLockStatue)
{
    SHLockStatue_Ready              = 0,   //准备开锁
    SHLockStatue_Ing                = 1,   //开锁中
    SHLockStatue_Finish             = 2,   //开锁完成
    SHLockStatue_Fail               = 3,   //开锁失败
};

@interface LockViewController ()<UINavigationControllerDelegate,CAAnimationDelegate>

@property (weak, nonatomic) IBOutlet UIButton *btnBack;

@property (weak, nonatomic) IBOutlet UIButton *btnLockImage;

@property (weak, nonatomic) IBOutlet UIView *viewDot;

@property (weak, nonatomic) IBOutlet UIView *viewCollectionBg;

@property (strong, nonatomic) LockNumCollectionView *collectionView;

@property (strong, nonatomic) NSMutableString * numlockStr;

@property (assign, nonatomic) NetworkEngine *networkEngine;

@property (strong, nonatomic) SHRequestTimer *timer;

@property (assign, nonatomic) SHLockStatue lockOpenStatue;

@property (strong, nonatomic) SHLockManager *manager;

@end

@implementation LockViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    if (self.itype == 0) {
        self.tabBarController.tabBar.hidden = YES;
    }
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    if (self.itype == 0) {
        self.tabBarController.tabBar.hidden = NO;
    }
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.numlockStr = [[NSMutableString alloc] initWithCapacity:6];
    [self doInitSubViews];
    [self doRegisterKVO];
    [self doLoadData];
    [self doAddAction];
}

- (void)doInitSubViews
{
    self.networkEngine = [NetworkEngine shareInstance];
    [self.viewCollectionBg addSubview:self.collectionView];
    [self doAddCollectionViewConstraints];
    
    [self.btnBack setEnlargeEdgeWithTop:20 right:20 bottom:20 left:20];
    //dotView
    self.viewDot.backgroundColor = [UIColor clearColor];
    for (int i = 0; i < 6; i ++)
    {
        UIImageView *dropImg = [[UIImageView alloc] initWithFrame:CGRectMake((UI_SCREEN_WIDTH - 220)/2.0 + i*40, 0, 20, 20)];
        [self.viewDot addSubview:dropImg];
        dropImg.tag = 2000 + i;
        [dropImg setImage:[UIImage imageNamed:@"LockUn"]];
    }
}

- (void)doRegisterKVO{
    
    //监测设备上报添加
    @weakify(self);
    [self observeKeyPath:@keypath(self.networkEngine.modelDevice) block:^(id value) {
        @strongify(self);
        SHModelDevice *deviceReport = (SHModelDevice *)value;
        if ([deviceReport.strDevice_device_OD isEqualToString:@"0F BE"])
        {
            if ([deviceReport.strDevice_device_type isEqualToString:@"02"])
            {
                if ([deviceReport.strDevice_category isEqualToString:@"02"])
                {
                    if ([deviceReport.strDevice_sindex_length isEqualToString:@"00"] || [deviceReport.strDevice_sindex_length isEqualToString:@"53"])
                    {
                        [XWHUDManager hideInWindow];
                         [XWHUDManager showSuccessTipHUD:@"开锁成功"];
                        
                        [self stopTimer];
                        self.lockOpenStatue = SHLockStatue_Finish;
                    }
                }else if([deviceReport.strDevice_category isEqualToString:@"03"]){
                    [self stopTimer];
                    self.lockOpenStatue = SHLockStatue_Finish;
                    [XWHUDManager hideInWindow];
                    NSString *strDescription;
                    if ([deviceReport.strDevice_sindex_length isEqualToString:@"0002"]) {
                        
                        if ([deviceReport.strOpenXMYLockReport_0002_IDType isEqualToString:@"01"]) {
                            NSLog(@"小蛮腰-->管理指纹");
                            strDescription = [NSString stringWithFormat:@"管理员指纹:%@",deviceReport.strFingerID];
                        }else if ([deviceReport.strOpenXMYLockReport_0002_IDType isEqualToString:@"02"]) {
                            NSLog(@"小蛮腰-->普通指纹");
                            strDescription = [NSString stringWithFormat:@"普通指纹:%@",deviceReport.strFingerID];
                        }else if ([deviceReport.strOpenXMYLockReport_0002_IDType isEqualToString:@"03"]) {
                            NSLog(@"小蛮腰-->客人指纹");
                            strDescription = [NSString stringWithFormat:@"客人指纹:%@",deviceReport.strFingerID];
                        }else if ([deviceReport.strOpenXMYLockReport_0002_IDType isEqualToString:@"04"]) {
                            NSLog(@"小蛮腰-->挟持指纹：");
                            strDescription = [NSString stringWithFormat:@"挟持指纹:%@",deviceReport.strFingerID];
                        }else if ([deviceReport.strOpenXMYLockReport_0002_IDType isEqualToString:@"05"]) {
                            NSLog(@"小蛮腰-->遥控：");
                            strDescription = @"遥控";
                        }else if ([deviceReport.strOpenXMYLockReport_0002_IDType isEqualToString:@"06"]) {
                            NSLog(@"小蛮腰-->门铃：");
                            strDescription = @"门铃";
                        }else if ([deviceReport.strOpenXMYLockReport_0002_IDType isEqualToString:@"07"]) {
                            NSLog(@"小蛮腰-->普通密码：");
                            strDescription = [NSString stringWithFormat:@"普通密码:%@",deviceReport.strFingerID];
                        }else if ([deviceReport.strOpenXMYLockReport_0002_IDType isEqualToString:@"08"]) {
                            NSLog(@"小蛮腰-->劫持密码：");
                            strDescription = @"劫持密码";
                        }else if ([deviceReport.strOpenXMYLockReport_0002_IDType isEqualToString:@"09"]) {
                            NSLog(@"小蛮腰-->指纹加密码：");
                            strDescription = @"指纹加密码";
                        }else if ([deviceReport.strOpenXMYLockReport_0002_IDType isEqualToString:@"0A"]) {
                            NSLog(@"小蛮腰-->远程开锁：");
                            strDescription = @"远程开锁";
                        }else if ([deviceReport.strOpenXMYLockReport_0002_IDType isEqualToString:@"0B"]) {
                            NSLog(@"小蛮腰-->门卡：");
                            strDescription = @"门卡";
                        }else if ([deviceReport.strOpenXMYLockReport_0002_IDType isEqualToString:@"0C"]) {
                            NSLog(@"小蛮腰-->指纹加卡：");
                            strDescription = @"指纹加卡";
                        }else {
                            NSLog(@"小蛮腰-->其它ID类型");
                            strDescription = @"其它ID类型";
                        }
                        
                        if (deviceReport.iDevice_device_state1 == 1){
                            [XWHUDManager showSuccessTipHUD:[NSString stringWithFormat:@"%@开锁成功!",strDescription]];
                        }else{
                            //                                NSLog(@"小蛮腰-->锁的状态（%@）:关",deviceReport.iDevice_device_state1);
                            [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@开锁失败!",strDescription]];
                        }
                        
                    }
                }
            }
        }
    }];
}


- (void)doLoadData
{
    NSArray *arr = @[@"1",@"2",@"3",@"4",
                     @"5",@"6",@"7",@"8",
                     @"9",@"开锁",@"0",@"删除"];
    self.collectionView.arrList = arr;
}

- (void)doAddAction
{
    @weakify(self);
    [self.collectionView setBlockCollectionSelected:^(NSIndexPath *indexPath, NSString *strNum) {
        @strongify(self);
        if (indexPath.row == 9) {
            [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
            
            [self.manager handleToOpenTheLockWithDeviceID:self.iDeviceID
                                                unLockPsw:self.numlockStr
                                           completeHandle:^(BOOL success, id result)
             {
                 if (success) {
                     [XWHUDManager hideInWindow];
                     [self doSendRemoteOrder];
                 }else{
                     [XWHUDManager hideInWindow];
                      [XWHUDManager showWarningTipHUD:@"密码输入错误！"];
                 }
             }];

        }else if (indexPath.row == 11){
            
            //删除触发
            [self deleteNumlock];
        }else{
            
            if (self.numlockStr.length < 6)
            {
                [self.numlockStr appendFormat:@"%@",self.collectionView.arrList[indexPath.row]];
                NSLog(@"%@",self.numlockStr);
                UIImageView * dropImg = (UIImageView *)[self.view viewWithTag:self.numlockStr.length + 2000 - 1];
                
                CATransition *animation = [CATransition animation];
                animation.delegate = self;
                animation.duration = 0.4;
                animation.timingFunction = UIViewAnimationCurveEaseInOut;
                animation.type = kCATransitionFade;
                [dropImg setImage:[UIImage imageNamed:@"LockPr"]];
                [[dropImg layer] addAnimation:animation forKey:@"animation"];
                if (self.numlockStr.length == 6)
                {
                    self.btnLockImage.selected = YES;
                    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
                    [self.manager handleToOpenTheLockWithDeviceID:self.iDeviceID
                                                        unLockPsw:self.numlockStr
                                                   completeHandle:^(BOOL success, id result)
                     {
                         if (success) {
                             [XWHUDManager hideInWindow];
                             [self doSendRemoteOrder];
                         }else{
                             [XWHUDManager hideInWindow];
                             
                             self.btnLockImage.selected = NO;
                             [self startShake:self.viewDot];
                             [self.numlockStr setString:@""];
                             for (int i = 0; i < 6; i ++)
                             {
                                 UIImageView *dropImg = (UIImageView *)[self.view viewWithTag:2000 + i];
                                 [dropImg setImage:[UIImage imageNamed:@"LockUn"]];
                             }
                             
                             [XWHUDManager showWarningTipHUD:@"密码输入错误！"];
                         }
                     }];
                }
            }
        }
    }];
}

- (void)deleteNumlock
{
    if (_numlockStr.length > 0)
    {
        NSString * numStr = [_numlockStr substringToIndex:_numlockStr.length - 1];
        [_numlockStr setString:numStr];
        UIImageView * dropImg = (UIImageView *)[self.view viewWithTag:_numlockStr.length + 2000];
        
        CATransition *animation = [CATransition animation];
        animation.delegate = self;
        animation.duration = 0.4;
        animation.timingFunction = UIViewAnimationCurveEaseInOut;
        animation.type = kCATransitionFade;
        [dropImg setImage:[UIImage imageNamed:@"LockUn"]];
        [[dropImg layer] addAnimation:animation forKey:@"animation"];
    }
}


#pragma mark -
#pragma mark - 初始化

- (LockNumCollectionView *)collectionView
{
    if (!_collectionView) {
        UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
        flowLayout.sectionHeadersPinToVisibleBounds = YES;
        [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
        _collectionView = [[LockNumCollectionView alloc] initWithFrame:CGRectMake(0,
                                                                                  0,
                                                                                  UI_SCREEN_WIDTH,
                                                                                  self.viewCollectionBg.frame.size.height)
                                                  collectionViewLayout:flowLayout];
    }
    return _collectionView;
}

- (void)doAddCollectionViewConstraints
{
    @weakify(self);
    [self.collectionView mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.top.equalTo(self.viewCollectionBg.mas_top);
        make.left.equalTo(self.viewCollectionBg.mas_left);
        make.right.equalTo(self.viewCollectionBg.mas_right);
        make.bottom.equalTo(self.viewCollectionBg.mas_bottom);
    }];
}


#pragma mark - 拖动晃动
- (void)startShake:(UIView* )imageV
{
    // 晃动次数
    static int numberOfShakes = 4;
    // 晃动幅度（相对于总宽度）
    //    static float vigourOfShake = 0.04f;
    // 晃动延续时常（秒）
    static float durationOfShake = 0.5f;
    
    CAKeyframeAnimation *shakeAnimation = [CAKeyframeAnimation animationWithKeyPath:@"position"];
    
    // 方法一：绘制路径
    CGRect frame = imageV.frame;
    // 创建路径
    CGMutablePathRef shakePath = CGPathCreateMutable();
    // 起始点
    CGPathMoveToPoint(shakePath, NULL, CGRectGetMidX(frame), CGRectGetMidY(frame));
    for (int index = 0; index < numberOfShakes; index++)
    {
        // 添加晃动路径,固定路径
        CGPathAddLineToPoint(shakePath, NULL, CGRectGetMidX(frame) - 20.0f,CGRectGetMidY(frame));
        CGPathAddLineToPoint(shakePath, NULL,  CGRectGetMidX(frame) + 20.0f,CGRectGetMidY(frame));
    }
    // 闭合
    CGPathCloseSubpath(shakePath);
    shakeAnimation.path = shakePath;
    shakeAnimation.duration = durationOfShake;
    // 释放
    CFRelease(shakePath);
    [imageV.layer addAnimation:shakeAnimation forKey:kCATransition];
}

#pragma mark -
#pragma mark - action
- (IBAction)btnBackPressed:(UIButton *)sender {
    //    [self.navigationController popViewControllerAnimated:YES];
    if (self.itype == 0) {
        [self.navigationController popViewControllerAnimated:YES];
    }else if (self.itype == 1){
        [self.navigationController popToViewController:self.navigationController.viewControllers[2] animated:YES];
    }else if (self.itype == 3){
        [self.navigationController popToViewController:self.navigationController.viewControllers[1] animated:YES];
    }else{
        NSLog(@"出错!");
    }
}

- (IBAction)addMemberPressed:(UIButton *)sender {
    
    [self performSegueWithIdentifier:@"seg_to_LockBindMemberVC" sender:self];
}

- (SHLockManager *)manager
{
    if (!_manager) {
        _manager = [SHLockManager new];
    }
    return  _manager;
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
    if ([segue.identifier isEqualToString:@"seg_to_LockBindMemberVC"]) {
        LockBindMemberVC *VC = segue.destinationViewController;
        VC.iDeviceID = self.iDeviceID;
        VC.strDeviceMacAddr = self.strMacAddr;
    }
}


#pragma mark -new
- (void)doSendRemoteOrder
{
    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
    if ([self.deviceTransmit.strDevice_category isEqualToString:@"02"]) {
        //普通锁
        self.lockOpenStatue = SHLockStatue_Ing;
        [self startTimer];
        NSData *dataSend = [[NetworkEngine shareInstance] doGetFingerprintCombinationLockOpenTheDoorDataWithTargetMacAddr:self.strMacAddr];
        [[NetworkEngine shareInstance] sendRequestData:dataSend];
        
    }else if ([self.deviceTransmit.strDevice_category isEqualToString:@"03"]){
        //小蛮腰
        [self startTimer];
        self.lockOpenStatue = SHLockStatue_Ing;
        NSData *dataSend = [[NetworkEngine shareInstance] doSendRemoteOpenXMYLockOrderWithTargetMacAddr:self.strMacAddr
                                                                                                    lockPsw:self.numlockStr];
        [[NetworkEngine shareInstance] sendRequestData:dataSend];
        
    }else{
        
        [XWHUDManager hideInWindow];
    }
    
    
}

#pragma mark Timer
- (void)startTimer
{
    if (self.timer == nil) {
        self.timer = [[SHRequestTimer alloc] init];
    }
    int timeout_ = 10; //self.timeout > 0 ? self.timeout : DEFAULT_REQUEST_TIMEOUT;
    [self.timer start:timeout_ target:self sel:@selector(timerAction)];
}

- (void)stopTimer
{
    self.timer = nil;
    [self.timer stop];
}

- (void)timerAction
{
    NSLog(@"看时间");
    [XWHUDManager hideInWindow];
    if (self.lockOpenStatue == SHLockStatue_Ing) {
        [self stopTimer];
        [XWHUDManager hideInWindow];
        
        [XWHUDManager showErrorTipHUD:@"远程开锁失败！"];
        
    }else{
        
        return;
    }
}


#pragma mark - 小蛮腰远程开锁


@end
