//
//  AirConditionVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/15.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#define degreesToRadians(x) (M_PI*(x)/180.0) //把角度转换成PI的方式
#define  PROGREESS_WIDTH 230 //圆直径
#define PROGRESS_LINE_WIDTH 8 //弧线的宽度

#import "AirConditionVC.h"
#import "AirConditionBtnModel.h"
#import "AirConditionBottomCollectionView.h"
#import "AirconditionAlterView.h"
#import "SHAirHandleTypeVC.h"
#import "SHDeviceManager.h"

@interface AirConditionVC ()

@property (weak, nonatomic) IBOutlet UIImageView *iamgeViewCircle;

@property (strong, nonatomic) CAShapeLayer *trackLayer;

@property (strong, nonatomic) CAShapeLayer *progressLayer;

@property (assign, nonatomic) int iCurrent;

@property (weak, nonatomic) IBOutlet UILabel *labelTemperature;

@property (strong,nonatomic)AirConditionBottomCollectionView *collectionView;

@property (strong, nonatomic) AirconditionAlterView *alterView;

@property (weak, nonatomic) IBOutlet UIView *viewTemperatuer;

@property (weak, nonatomic) IBOutlet UIButton *btnCover;

@property (strong, nonatomic) SHDeviceManager *manager;

@property (weak, nonatomic) IBOutlet UIButton *btnOnOrOFF;

@property (weak, nonatomic) IBOutlet UIButton *btnBack;

@end

@implementation AirConditionVC


- (void)viewDidLoad {
    [super viewDidLoad];
    [self doInitCircle];
    [self doInitSubViews];
    [self doInitValue];
    [self doLoadData];
    [self doHandleAction];
    
}

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

- (void)doInitValue
{
    self.btnCover.hidden = YES;
    self.iCurrent = 26;
    [self setPercent:(self.iCurrent-16) animated:YES];
    
    if (self.itype == 0) {
        [self performSelector:@selector(doSendAirConditionON) withObject:nil afterDelay:0.1];
        
        [self performSelector:@selector(doSend26) withObject:nil afterDelay:1.5];
    }else{
    
        NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionModelTargetAddr:self.device.strDevice_mac_address
                                                                                    modelNO:self.iCode];
        [[NetworkEngine shareInstance] sendRequestData:data];
        
        [self performSelector:@selector(doSendAirConditionON) withObject:nil afterDelay:0.5];
        
        [self performSelector:@selector(doSend26) withObject:nil afterDelay:1.5];
    }
    
}

- (void)doInitSubViews
{
    [self.btnBack setEnlargeEdgeWithTop:20 right:20 bottom:20 left:20];
    
    [self.view addSubview:self.collectionView];
    [self doAddCollectionViewConstraints];
}

#pragma mark -
#pragma mark - 空调开
- (void)doSendAirConditionON
{
    NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionOnOrOffTargetAddr:self.device.strDevice_mac_address
                                                                               strOnOrOff:@"FF"];
    [[NetworkEngine shareInstance] sendRequestData:data];
}

#pragma mark -
#pragma mark - 空调关
- (void)doSendAirconditonOFF
{
    NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionOnOrOffTargetAddr:self.device.strDevice_mac_address
                                                                               strOnOrOff:@"00"];
    [[NetworkEngine shareInstance] sendRequestData:data];
}


#pragma mark -
#pragma mark - 发送固定温度 26°
- (void)doSend26
{
    self.iCurrent = 26;
    NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionTemperatureTargetAddr:self.device.strDevice_mac_address temperature:26];
    [[NetworkEngine shareInstance] sendRequestData:data];
}

#pragma mark -
#pragma mark - 发送温度控制命令
- (void)doSendAirconditonTemperatureInstruction
{
    NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionTemperatureTargetAddr:self.device.strDevice_mac_address
                                                                                  temperature:self.iCurrent];
    [[NetworkEngine shareInstance] sendRequestData:data];
}

#pragma mark -
#pragma mark - loadata
- (void)doLoadData
{
    AirConditionBtnModel *model = [AirConditionBtnModel new];
    model.strName = @"模式";
    model.strPic = @"模式.png";
    model.type = AirConditionBtnType_Mode;
    
    AirConditionBtnModel *model2 = [AirConditionBtnModel new];
    model2.strName = @"风速";
    model2.strPic = @"风速.png";
    model2.type = AirConditionBtnType_WindSpeed;
    
    AirConditionBtnModel *model3 = [AirConditionBtnModel new];
    model3.strName = @"自动摆风";
    model3.strPic = @"左右.png";
    model3.type = AirConditionBtnType_RightAndLight;
    
    AirConditionBtnModel *model4 = [AirConditionBtnModel new];
    model4.strName = @"手动摆风";
    model4.strPic = @"上下.png";
    model4.type = AirConditionBtnType_UpAndDown;
    
    self.collectionView.arrList = @[model,model2,model3,model4];
}


#pragma mark -
#pragma mark - do Action
- (IBAction)btnCoverPressed:(UIButton *)sender {
   [XWHUDManager showErrorTipHUD:@"空调已关，无法控制"];
}


- (IBAction)btnBack:(UIButton *)sender {
    
    if (self.itype == 0) {
        [self.navigationController popViewControllerAnimated:YES];
    }else{
        [self.navigationController popToViewController:self.navigationController.viewControllers[3] animated:YES];
    }
}

- (IBAction)btnOnOrOffPressed:(UIButton *)sender {
    //开关控制
    sender.selected = !sender.selected;
    if (sender.selected) {
        [self doSendAirconditonOFF];
        self.btnCover.hidden = NO;
        
    }else{
        [self doSendAirConditionON];
//        [self performSelector:@selector(doSendAirconditonTemperatureInstruction) withObject:nil afterDelay:0.5];
        self.btnCover.hidden = YES;
    }
}


- (IBAction)btnAddPressed:(UIButton *)sender {
    
    self.iCurrent ++;
    if (self.iCurrent <=16) {
        self.iCurrent = 16;
    }
    if (self.iCurrent >= 32) {
        self.iCurrent = 32;
    }
    [self setPercent:(self.iCurrent - 16) animated:YES];
    NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionTemperatureTargetAddr:self.device.strDevice_mac_address
                                                                                  temperature:self.iCurrent];
    [[NetworkEngine shareInstance] sendRequestData:data];
}

- (IBAction)btnReduce:(UIButton *)sender {
    
    self.iCurrent --;
    
    if (self.iCurrent >= 32) {
        self.iCurrent = 32;
    }
    if (self.iCurrent <=16) {
        self.iCurrent = 16;
    }
    
    [self setPercent:(self.iCurrent-16) animated:YES];
    
    //控制度数操作
    NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionTemperatureTargetAddr:self.device.strDevice_mac_address
                                                                                  temperature:self.iCurrent];
    [[NetworkEngine shareInstance] sendRequestData:data];
}



- (void)doHandleAction
{
    @weakify(self);
    [self.collectionView setBlockCollectionViewDidSelected:^(AirConditionBtnType type) {
        @strongify(self);
        switch (type) {
            case AirConditionBtnType_Mode:
            {
                //模式
                [self doShowAllMode];
            }
                break;
            case AirConditionBtnType_WindSpeed:
            {
                [self doGetAllWindSpeed];
            }
                break;
            case AirConditionBtnType_RightAndLight:
            {
                //自动摆风
                NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionDirectionTargetAddr:self.device.strDevice_mac_address direction:@"00"];
                [[NetworkEngine shareInstance] sendRequestData:data];
            }
                break;
            case AirConditionBtnType_UpAndDown:
            {
                //手动摆风
                NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionDirectionTargetAddr:self.device.strDevice_mac_address direction:@"01"];
                [[NetworkEngine shareInstance] sendRequestData:data];
            }
                break;
                
            default:
                break;
        }
    }];

}

#pragma mark - 模式
- (void)doShowAllMode
{
    AirConditionBtnModel *model = [AirConditionBtnModel new];
    model.strName = @"自动";
    model.strPic = @"自动.png";
    model.type = AirConditionBtnType_Automatic;
    
    AirConditionBtnModel *model2 = [AirConditionBtnModel new];
    model2.strName = @"制冷";
    model2.strPic = @"制冷.png";
    model2.type = AirConditionBtnType_Cold;
    
    AirConditionBtnModel *model3 = [AirConditionBtnModel new];
    model3.strName = @"制热";
    model3.strPic = @"tcl-制热.png";
    model3.type = AirConditionBtnType_Hot;
    
    AirConditionBtnModel *model4 = [AirConditionBtnModel new];
    model4.strName = @"送风";
    model4.strPic = @"送风.png";
    model4.type = AirConditionBtnType_BlowIn;
    
    AirConditionBtnModel *model5 = [AirConditionBtnModel new];
    model5.strName = @"除湿";
    model5.strPic = @"加湿.png";
    model5.type = AirConditionBtnType_Dehumidifier;
    
    NSArray *arr = @[model,model2,model3,model4,model5];
    [self doPopAlterViewWIthData:arr withTitle:@"模式"];
}

#pragma mark - 风速
- (void)doGetAllWindSpeed
{
    AirConditionBtnModel *model = [AirConditionBtnModel new];
    model.strName = @"自动";
    model.strPic = @"自动.png";
    model.type = AirConditionBtnType_AutomaticWind;
    
    AirConditionBtnModel *model2 = [AirConditionBtnModel new];
    model2.strName = @"高风";
    model2.strPic = @"高风.png";
    model2.type = AirConditionBtnType_HighWind;
    
    AirConditionBtnModel *model3 = [AirConditionBtnModel new];
    model3.strName = @"中风";
    model3.strPic = @"中风.png";
    model3.type = AirConditionBtnType_MiddleWind;
    
    AirConditionBtnModel *model4 = [AirConditionBtnModel new];
    model4.strName = @"低风";
    model4.strPic = @"低风.png";
    model4.type = AirConditionBtnType_LowWind;
    
    NSArray *arr = @[model,model2,model3,model4];
    [self doPopAlterViewWIthData:arr withTitle:@"风速"];
}

#pragma mark -
#pragma mark - init
- (AirConditionBottomCollectionView *)collectionView
{
    if (!_collectionView) {
        UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
        flowLayout.sectionHeadersPinToVisibleBounds = YES;
        [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
        _collectionView = [[AirConditionBottomCollectionView alloc] initWithFrame:CGRectMake(0,
                                                                                             UI_SCREEN_HEIGHT - 105,
                                                                                             UI_SCREEN_WIDTH,
                                                                                             105)
                                                       collectionViewLayout:flowLayout];
    }
    return _collectionView;
}

- (void)doAddCollectionViewConstraints
{
    @weakify(self);
    [self.collectionView mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.height.equalTo(@105);
        make.left.equalTo(self.view.mas_left);
        make.right.equalTo(self.view.mas_right);
        make.bottom.equalTo(self.view.mas_bottom);
    }];
}

#pragma mark -
#pragma mark- 画圆圈

- (void)doInitCircle
{
    self.iCurrent = 16;
    self.labelTemperature.text = [NSString stringWithFormat:@"%d",self.iCurrent];
    
    _trackLayer = [CAShapeLayer layer];//创建一个track shape layer
    _trackLayer.frame = self.view.bounds;
    [self.view.layer addSublayer:_trackLayer];
    _trackLayer.fillColor = [[UIColor clearColor] CGColor];
    _trackLayer.strokeColor = [UIColor clearColor].CGColor;//指定path的渲染颜色
    _trackLayer.opacity = 0.25; //背景同学你就甘心做背景吧，不要太明显了，透明度小一点
    _trackLayer.lineCap = kCALineCapRound;//指定线的边缘是圆的
    _trackLayer.lineWidth = PROGRESS_LINE_WIDTH;//线的宽度
    
    UIBezierPath *path = [UIBezierPath bezierPathWithArcCenter:CGPointMake(230/2.0, 230/2.0)
                                                        radius:(PROGREESS_WIDTH-PROGRESS_LINE_WIDTH)/2
                                                    startAngle:degreesToRadians(-227)
                                                      endAngle:degreesToRadians(47)
                                                     clockwise:YES];//上面说明过了用来构建圆形
    _trackLayer.path =[path CGPath]; //把path传递給layer，然后layer会处理相应的渲染，整个逻辑和CoreGraph是一致的。
    
    _progressLayer = [CAShapeLayer layer];
    _progressLayer.frame = self.view.bounds;
    _progressLayer.fillColor =  [[UIColor clearColor] CGColor];
    _progressLayer.strokeColor  = [UIColor greenColor].CGColor;
    _progressLayer.lineCap = kCALineCapRound;
    _progressLayer.lineWidth = PROGRESS_LINE_WIDTH;
    _progressLayer.path = [path CGPath];
    _progressLayer.strokeEnd = 0;
    [self.viewTemperatuer.layer addSublayer:_progressLayer];
    
}


-(void)setPercent:(NSInteger)percent animated:(BOOL)animated
{
    self.labelTemperature.text = [NSString stringWithFormat:@"%ld°",(long)self.iCurrent];
    [CATransaction begin];
    [CATransaction setDisableActions:!animated];
    [CATransaction setAnimationTimingFunction:[CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseIn]];
    [CATransaction setAnimationDuration:0.25];
    self.progressLayer.strokeEnd = percent/16.00;
    [CATransaction commit];

}

#pragma mark -
#pragma mark - 弹出View
- (void)doPopAlterViewWIthData:(NSArray *)arrDatasource withTitle:(NSString *)title
{
    self.alterView = [[AirconditionAlterView alloc] initWithFrame:CGRectMake(0,
                                                                             0,
                                                                             UI_SCREEN_WIDTH,
                                                                             UI_SCREEN_HEIGHT)
                                                          arrList:arrDatasource
                                                            title:title];
    [self.view addSubview:self.alterView];
    [self.alterView show];
    
    @weakify(self);
    [self.alterView setBlockGetTypeCompleteHandle:^(AirConditionBtnModel *model) {
        @strongify(self);
        switch (model.type) {
            case AirConditionBtnType_Automatic:
            {
                NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionModeTargetAddr:self.device.strDevice_mac_address mode:@"00"];
                [[NetworkEngine shareInstance] sendRequestData:data];
            }
                break;
            case AirConditionBtnType_Cold:
            {
                NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionModeTargetAddr:self.device.strDevice_mac_address mode:@"01"];
                [[NetworkEngine shareInstance] sendRequestData:data];
            }
                break;
            case AirConditionBtnType_Hot:
            {
                NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionModeTargetAddr:self.device.strDevice_mac_address mode:@"04"];
                [[NetworkEngine shareInstance] sendRequestData:data];
            }
                break;
            case AirConditionBtnType_BlowIn:
            {
                NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionModeTargetAddr:self.device.strDevice_mac_address mode:@"03"];
                [[NetworkEngine shareInstance] sendRequestData:data];
            }
                break;
            case AirConditionBtnType_Dehumidifier:
            {
                NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionModeTargetAddr:self.device.strDevice_mac_address mode:@"02"];
                [[NetworkEngine shareInstance] sendRequestData:data];
            }
                break;
            case AirConditionBtnType_AutomaticWind:
            {
                NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionSpeedTargetAddr:self.device.strDevice_mac_address speed:@"00"];
                [[NetworkEngine shareInstance] sendRequestData:data];
            }
                break;
            case AirConditionBtnType_HighWind:
            {
                NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionSpeedTargetAddr:self.device.strDevice_mac_address speed:@"03"];
                [[NetworkEngine shareInstance] sendRequestData:data];
            }
                break;
            case AirConditionBtnType_MiddleWind:
            {
                NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionSpeedTargetAddr:self.device.strDevice_mac_address speed:@"02"];
                [[NetworkEngine shareInstance] sendRequestData:data];
            }
                break;
            case AirConditionBtnType_LowWind:
            {
                NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionSpeedTargetAddr:self.device.strDevice_mac_address speed:@"01"];
                [[NetworkEngine shareInstance] sendRequestData:data];
            }
                break;
                
            default:
                break;
        }
    }];
}


//#pragma mark -
//#pragma mark -  更新设备
//- (void)doSendUpadateDeviceToSaveSwitchAndTemperature
//{
//    [XWHUDManager showHUDMessage:@"加载中..." afterDelay:20];
//    SHModelDevice *deviceNew = [self doHandleGetNewDeviceWithEditDevice:self.device];
//    [self.manager handleTheUpdateDeviceDataWithModel:deviceNew
//                                      completeHandle:^(BOOL success, id result)
//     {
//         if (success) {
//             [XWHUDManager hideInWindow];
//             [[GAlertMessageManager shareInstance] showMessag:@"更新设备成功" delayTime:1];
//             
//         }else{
//             [XWHUDManager hideInWindow];
//             [XWHUDManager showErrorTipHUD:@"更新失败"];
//         }
//         
//         if (self.itype == 0) {
//             [self.navigationController popViewControllerAnimated:YES];
//         }else{
//             [self.navigationController popToViewController:self.navigationController.viewControllers[3] animated:YES];
//         }
//         
//     }];
//}
//

//- (SHModelDevice *)doHandleGetNewDeviceWithEditDevice:(SHModelDevice *)editDevice
//{
//    SHModelDevice *deviceNew                = [SHModelDevice new];
//    deviceNew.iDevice_device_id             = editDevice.iDevice_device_id;
//    deviceNew.iDevice_room_id               = editDevice.iDevice_room_id;
//    deviceNew.strDevice_device_name         = editDevice.strDevice_device_name;
//    deviceNew.strDevice_image               = editDevice.strDevice_image;
//    deviceNew.strDevice_device_OD           = editDevice.strDevice_device_OD;
//    
//    deviceNew.strDevice_device_type         = editDevice.strDevice_device_type;
//    deviceNew.strDevice_category            = editDevice.strDevice_category;
//    
//    deviceNew.strDevice_sindex              = self.strOnOrOff;
//    deviceNew.strDevice_sindex_length       = [NSString stringWithFormat:@"%d",self.iCurrent];
//    
//    deviceNew.strDevice_cmdId               = editDevice.strDevice_cmdId;
//    deviceNew.strDevice_mac_address         = editDevice.strDevice_mac_address;
//    
//    deviceNew.iDevice_device_state1        = editDevice.iDevice_device_state1;
//    deviceNew.iDevice_device_state2         = editDevice.iDevice_device_state2;
//    deviceNew.iDevice_device_state3         = editDevice.iDevice_device_state3;
//    deviceNew.strDevice_other_status        = editDevice.strDevice_other_status;
//    deviceNew.arrBtns                       = editDevice.arrBtns;
//    
//    return deviceNew;
//}

- (SHDeviceManager *)manager
{
    if (!_manager) {
        _manager = [SHDeviceManager new];
    }
    return _manager;
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
    if ([segue.identifier isEqualToString:@"seg_to_SHAirHandleTypeVC"]) {
        SHAirHandleTypeVC *VC = segue.destinationViewController;
        VC.device = self.device;
    }
}

@end
