//
//  SHAdaptiveVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/3.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHAdaptiveVC.h"
#import "AirConditionVC.h"
#import "SHDeviceManager.h"

@interface SHAdaptiveVC ()<UINavigationControllerDelegate>

@property (strong, nonatomic) SHDeviceManager *manager;

@property (weak, nonatomic) IBOutlet UILabel *labelAdaptiveNum;
@property (strong, nonatomic) NSString *strName;

@property (strong, nonatomic) NSArray *arrCodes;
@property (assign, nonatomic) int iRemberCode;

@property (weak, nonatomic) IBOutlet UIButton *btnMinus;
@property (weak, nonatomic) IBOutlet UIButton *btnAdd;
@property (weak, nonatomic) IBOutlet UIButton *btnCold;
@property (weak, nonatomic) IBOutlet UIButton *btnOn;
@property (weak, nonatomic) IBOutlet UIButton *btnTemperatureAdd;
@property (weak, nonatomic) IBOutlet UIButton *btnTemperatureMinus;

@end

@implementation SHAdaptiveVC

#pragma mark - UINavigationControllerDelegate
- (void)navigationController:(UINavigationController *)navigationController
      willShowViewController:(UIViewController *)viewController
                    animated:(BOOL)animated {
    BOOL isHomePage = [viewController isKindOfClass:[self class]];
    
    [self.navigationController setNavigationBarHidden:!isHomePage animated:YES];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navigationController.delegate =self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.strName = self.dict[@"data"][@"name"];
    self.arrCodes = self.dict[@"data"][@"code"];
    
    self.iRemberCode = 0;
    self.labelAdaptiveNum.text = [NSString stringWithFormat:@"%@空调的型号%d/%lu",self.strName,self.iRemberCode,(unsigned long)self.arrCodes.count];
    int iFirstNO = [self.arrCodes[self.iRemberCode] intValue];
    NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionModelTargetAddr:self.device.strDevice_mac_address modelNO:iFirstNO];
    [[NetworkEngine shareInstance] sendRequestData:data];
    
}

-(void)setIRemberCode:(int)iRemberCode
{
    _iRemberCode = iRemberCode;
    if (iRemberCode == 0) {
        self.btnMinus.enabled = NO;
    }else if (self.iRemberCode == self.arrCodes.count) {
        self.btnAdd.enabled = NO;
    }else{
    
        self.btnAdd.enabled = YES;
        self.btnMinus.enabled = YES;
    }
}

- (IBAction)btnAdaptiveMinusPressed:(UIButton *)sender {
    
//    if (self.iRemberCode < self.arrCodes.count &&self.iRemberCode > 0) {
//        self.iRemberCode --;
//        self.labelAdaptiveNum.text = [NSString stringWithFormat:@"%@空调的型号%d/%lu",self.strName,self.iRemberCode,(unsigned long)self.arrCodes.count];
//        [self doSendCode];
//    }else{
//    
//        [[GAlertMessageManager shareInstance] showError:@"搜索完毕"];
//    }
    
    if (self.iRemberCode > 0) {
        self.iRemberCode --;
        self.labelAdaptiveNum.text = [NSString stringWithFormat:@"%@空调的型号%d/%lu",self.strName,self.iRemberCode,(unsigned long)self.arrCodes.count];
        [self doSendCode];
    }else{
         [XWHUDManager showSuccessTipHUD:@"搜索完毕"];
    }
}

- (IBAction)btnAdaptiveAddPressed:(UIButton *)sender {
    if (self.iRemberCode < self.arrCodes.count ) {
        self.labelAdaptiveNum.text = [NSString stringWithFormat:@"%@空调的型号%d/%lu",self.strName,self.iRemberCode + 1,(unsigned long)self.arrCodes.count];
        [self doSendCode];
        self.iRemberCode ++;
    }else{
    
          [XWHUDManager showSuccessTipHUD:@"搜索完毕"];
    }
}

#pragma mark - 发送模式命令
- (void)doSendCode
{
    int iFirstNO = [self.arrCodes[self.iRemberCode] intValue];
    NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionModelTargetAddr:self.device.strDevice_mac_address modelNO:iFirstNO];
    [[NetworkEngine shareInstance] sendRequestData:data];
}


#pragma mark - 制冷操作
- (IBAction)btnMakeColdpress:(UIButton *)sender
{
    NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionModeTargetAddr:self.device.strDevice_mac_address mode:@"01"];
    [[NetworkEngine shareInstance] sendRequestData:data];
    [self doAlter:sender];
}

#pragma mark - 开关操作
- (IBAction)btnTurnOnPressed:(UIButton *)sender
{
    NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionOnOrOffTargetAddr:self.device.strDevice_mac_address strOnOrOff:@"FF"];
    [[NetworkEngine shareInstance] sendRequestData:data];
    [self doAlter:sender];
}

#pragma mark - 温度➕操作
- (IBAction)btnVoiceAddPressed:(UIButton *)sender
{
    NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionTemperatureTargetAddr:self.device.strDevice_mac_address temperature:26];
    [[NetworkEngine shareInstance] sendRequestData:data];
    [self doAlter:sender];
}

#pragma mark - 温度➖操作
- (IBAction)btnVoiceMinusPressed:(UIButton *)sender
{
    NSData *data = [[NetworkEngine shareInstance] doGetSetAirConditionTemperatureTargetAddr:self.device.strDevice_mac_address temperature:23];
    [[NetworkEngine shareInstance] sendRequestData:data];
    
    [self doAlter:sender];
}

- (IBAction)btnAddTheDevice:(UIButton *)sender {
    NSLog(@"添加完成");
    
    SHModelDevice *deviceNew = [self handleGetNewDevice:self.device];
    [self doAddDeviceToNetServer:@[deviceNew]];
    
}

- (SHModelDevice *)handleGetNewDevice:(SHModelDevice *)deviceReport
{
    SHModelDevice *deviceAdd                = [SHModelDevice new];
    
    deviceAdd.iDevice_room_id               = deviceReport.iDevice_room_id;
    deviceAdd.strDevice_device_name         = deviceReport.strDevice_device_name;
    deviceAdd.strDevice_image               = deviceReport.strDevice_image;
    deviceAdd.strDevice_device_OD           = deviceReport.strDevice_device_OD;
    
    deviceAdd.strDevice_device_type         = deviceReport.strDevice_device_type;
    deviceAdd.strDevice_category            = deviceReport.strDevice_category;
    deviceAdd.strDevice_sindex              = deviceReport.strDevice_sindex;
    deviceAdd.strDevice_sindex_length       = deviceReport.strDevice_sindex_length;
    
    deviceAdd.strDevice_cmdId               = deviceReport.strDevice_cmdId;
    deviceAdd.strDevice_mac_address         = deviceReport.strDevice_mac_address;
    deviceAdd.iDevice_device_state1         = deviceReport.iDevice_device_state1;
    deviceAdd.iDevice_device_state2         = deviceReport.iDevice_device_state2;
    
    deviceAdd.iDevice_device_state3         = deviceReport.iDevice_device_state3;
    deviceAdd.strDevice_other_status        = [NSString stringWithFormat:@"%@",self.arrCodes[self.iRemberCode]];
    deviceAdd.arrBtns                       = deviceReport.arrBtns;
    return deviceAdd;
}

-(NSArray *)arrCodes
{
    if (!_arrCodes) {
        _arrCodes = [NSArray new];
    }
    return _arrCodes;
}

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

- (void)doAlter:(UIButton *)sender
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"温馨提示" message:@"按钮是否正常相应了吗？" preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"没有" style:UIAlertActionStyleCancel handler:nil];
    
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"有" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        sender.selected = YES;
        
    }];
    [alertController addAction:cancelAction];
    [alertController addAction:okAction];
    [self presentViewController:alertController animated:YES completion:nil];
}

- (void)doAddDeviceToNetServer:(NSArray *)deviceArr
{
    NSLog(@"获取到设备开始进行网络添加");
    @weakify(self);
    [self.manager handleTheAddDeviceDataWithArrModel:deviceArr completeHandle:^(BOOL success, id result) {
        @strongify(self);
        if (success) {
            [XWHUDManager hideInWindow];
            NSArray *arrDevice = (NSArray *)result;
            UIStoryboard *constructStoryBoard = [UIStoryboard storyboardWithName:@"SecondPage" bundle:[NSBundle mainBundle]];
            AirConditionVC *VC = (AirConditionVC *)[constructStoryBoard instantiateViewControllerWithIdentifier:@"AirConditionVC"];
            VC.device = arrDevice[0];
            VC.itype = 1;
            VC.iCode = [self.arrCodes[self.iRemberCode] intValue];
//            VC.strOnOrOff = @"00";
//            VC.strTemperature = @"26";
            [self.navigationController pushViewController:VC animated:YES];
            
        }else{
            [XWHUDManager hideInWindow];
             [XWHUDManager showErrorTipHUD:[NSString stringWithFormat:@"%@",result]];
        }
    }];
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
