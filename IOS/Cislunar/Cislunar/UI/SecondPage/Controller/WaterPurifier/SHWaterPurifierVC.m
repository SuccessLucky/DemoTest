//
//  SHWaterPurifierVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/12/12.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHWaterPurifierVC.h"

@interface SHWaterPurifierVC ()

@property (assign, nonatomic) NetworkEngine *networkEngine;
@property (weak, nonatomic) IBOutlet UIButton *btnSwitchOn;
@property (weak, nonatomic) IBOutlet UIButton *btnSwitchOff;
@property (weak, nonatomic) IBOutlet UIButton *btnWash;

/*
@property (weak, nonatomic) IBOutlet UIButton *btnOne;
@property (weak, nonatomic) IBOutlet UIButton *btnTwo;
@property (weak, nonatomic) IBOutlet UIButton *btnThree;
@property (weak, nonatomic) IBOutlet UIButton *btnFour;
@property (weak, nonatomic) IBOutlet UIButton *btnFive;

@property (weak, nonatomic) IBOutlet UITextView *textViewOne;
@property (weak, nonatomic) IBOutlet UITextView *textViewTwo;
@property (weak, nonatomic) IBOutlet UITextView *textViewThree;
@property (weak, nonatomic) IBOutlet UITextView *textViewFour;
@property (weak, nonatomic) IBOutlet UITextView *textViewFive;
 */
@property (weak, nonatomic) IBOutlet UITextView *textViewContent;

@end

@implementation SHWaterPurifierVC

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
    [self doInitSubViews];
    self.networkEngine = [NetworkEngine shareInstance];
    [self doRegisterKVO];
    // Do any additional setup after loading the view.
}

- (void)doInitSubViews
{
    self.textViewContent.textColor = kCommonColor;
//    [self setTitleViewText:@"净水器"];
    self.title = @"净水器";
    [self handleTheWaterFilterBtn:self.btnSwitchOn];
    [self handleTheWaterFilterBtn:self.btnSwitchOff];
    [self handleTheWaterFilterBtn:self.btnWash];
    
    /*
    [self handleTheWaterFilterBtn:self.btnOne];
    [self handleTheWaterFilterBtn:self.btnTwo];
    [self handleTheWaterFilterBtn:self.btnThree];
    [self handleTheWaterFilterBtn:self.btnFour];
    [self handleTheWaterFilterBtn:self.btnFive];
    
    [self handleTheWaterFilterTextView:self.textViewOne];
    [self handleTheWaterFilterTextView:self.textViewTwo];
    [self handleTheWaterFilterTextView:self.textViewThree];
    [self handleTheWaterFilterTextView:self.textViewFour];
    [self handleTheWaterFilterTextView:self.textViewFive];
    
    [self.textViewOne setText:@"1:ddddddd.\n2:wwwwwwww.\n3:ttttttttt"];
     */
    
    
    CGFloat fWith = (UI_SCREEN_WIDTH - 15 *2 - 4 * 10)/5.0;
    for (int i = 0; i < 5; i ++) {
        UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
        btn.frame = CGRectMake(15 + fWith*i + 10 *i, 134,fWith, 208);
        [btn setTitleColor:kCommonColor forState:UIControlStateNormal];
        btn.titleLabel.font = [UIFont systemFontOfSize:12.0f];
        [self.view addSubview:btn];
        btn.tag = 100 + i;
        [self handleTheWaterFilterBtn:btn];
    }
    
    for (int i = 0; i < 5; i ++) {
        UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
        btn.frame = CGRectMake(15 + fWith*i + 10 *i, 134+208+5, fWith, 44);
        [btn setTitle:[NSString stringWithFormat:@"清滤芯%d",i] forState:UIControlStateNormal];
        [btn setTitleColor:kCommonColor forState:UIControlStateNormal];
        btn.titleLabel.font = [UIFont systemFontOfSize:12.0f];
        [self.view addSubview:btn];
        [btn addTarget:self action:@selector(btnPressed:) forControlEvents:UIControlEventTouchUpInside];
        btn.tag = 200 + i;
        [self handleTheWaterFilterBtn:btn];
    }
}

- (void)btnPressed:(UIButton *)sender
{
    SHWaterPurifierFunctionType typeTemp;
    switch (sender.tag) {
        case 200:
        {
            typeTemp = SHWaterPurifierFunctionType_FilterElementOne;
        }
            break;
        case 201:
        {
            typeTemp = SHWaterPurifierFunctionType_FilterElementSec;
        }
            break;
        case 202:
        {
            typeTemp = SHWaterPurifierFunctionType_FilterElementThird;
        }
            break;
        case 203:
        {
            typeTemp = SHWaterPurifierFunctionType_FilterElementFourth;
        }
            break;
        case 204:
        {
            typeTemp = SHWaterPurifierFunctionType_FilterElementFifth;
        }
            break;
        default:
            typeTemp = 0;
            break;
    }
    NSData *data = [[NetworkEngine shareInstance] doGetGetWaterPurifierSendDataWithTargetMacAddr:self.device.strDevice_mac_address waterPurifierFunctionType:typeTemp];
    [self.networkEngine sendRequestData:data];
}

- (void)handleTheWaterFilterBtn:(UIButton *)btn
{
    btn.layer.cornerRadius = 5;
    btn.layer.masksToBounds = YES;
    btn.layer.borderColor = kCommonColor.CGColor;
    btn.layer.borderWidth = 0.5;
}

- (void)handleTheWaterFilterTextView:(UITextView *)btn
{
    btn.layer.cornerRadius = 5;
    btn.layer.masksToBounds = YES;
    btn.layer.borderColor = kCommonColor.CGColor;
    btn.layer.borderWidth = 0.5;
}

- (IBAction)btnOnPressed:(UIButton *)sender {
    NSData *data = [[NetworkEngine shareInstance] doGetGetWaterPurifierSendDataWithTargetMacAddr:self.device.strDevice_mac_address waterPurifierFunctionType:SHWaterPurifierFunctionType_On];
    [self.networkEngine sendRequestData:data];
}


- (IBAction)btnOffPressed:(UIButton *)sender {
    NSData *data = [[NetworkEngine shareInstance] doGetGetWaterPurifierSendDataWithTargetMacAddr:self.device.strDevice_mac_address waterPurifierFunctionType:SHWaterPurifierFunctionType_Off];
    [self.networkEngine sendRequestData:data];
}

- (IBAction)btnWashPressed:(UIButton *)sender {
    NSData *data = [[NetworkEngine shareInstance] doGetGetWaterPurifierSendDataWithTargetMacAddr:self.device.strDevice_mac_address waterPurifierFunctionType:SHWaterPurifierFunctionType_Water];
    [self.networkEngine sendRequestData:data];
}
#pragma mark - 注册kvo
- (void)doRegisterKVO
{
    //监测设备上报添加
    @weakify(self);
    [self observeKeyPath:@keypath(self.networkEngine.modelDevice) block:^(id value) {
        @strongify(self);
        SHModelDevice *deviceReport = (SHModelDevice *)value;
        if ([deviceReport.strDevice_device_OD isEqualToString:@"0F E6"]) {
            if ([deviceReport.strDevice_device_type isEqualToString:@"02"]) {
                if ([deviceReport.strDevice_category isEqualToString:@"14"]) {
                    if ([deviceReport.strDevice_cmdId isEqualToString:@"07"]) {
                        if (deviceReport.strDevice_other_status.length > 0) {
                            NSString *strJson = deviceReport.strDevice_other_status;
                            NSDictionary *jsonDict = [ToolCommon dictionaryWithJsonString:strJson];
                            [self handleRefreshUI:jsonDict];
                        }
                    }
                }
            }
        }
        
    }];
}

- (void)handleRefreshUI:(NSDictionary *)jsonDict
{
    NSString *strHaveBeenUsingTime1 = jsonDict[@"strHaveBeenUsingTime1"];
    NSString *strHaveBeenUsingTime2 = jsonDict[@"strHaveBeenUsingTime2"];
    NSString *strHaveBeenUsingTime3 = jsonDict[@"strHaveBeenUsingTime3"];
    NSString *strHaveBeenUsingTime4 = jsonDict[@"strHaveBeenUsingTime4"];
    NSString *strHaveBeenUsingTime5 = jsonDict[@"strHaveBeenUsingTime5"];
    
    //设备状态：0，冲洗；1：水满； 2，制水； 3，缺水；4，检修；5，关机；
    NSString *strWaterFilterStatue = jsonDict[@"strWaterFilterStatue"];
    NSString *strWaterFilterStatueEnd = [self handleGetStatue:strWaterFilterStatue];
    
    //水压，0,缺水；1，有水；
    NSString *strWaterPressurestr = jsonDict[@"strWaterPressurestr"];
    NSString *strWaterPressurestrEnd = [self handleGetWaterPressurestr:strWaterPressurestr];
    //水温
    NSString *strWaterTemperture = jsonDict[@"strWaterTemperture"];
    int iWaterTemperture = (int)[[ToolHexManager sharedManager] numberWithHexString:strWaterTemperture];
    
    //原水TDS1，最大值为65535
    NSString *strTDS1 =  jsonDict[@"strTDS1"];
    int iTDS1 = (int)[[ToolHexManager sharedManager] numberWithHexString:strTDS1];
    
    //纯水TDS2，最大值为65535
    NSString *strTDS2 =  jsonDict[@"strTDS2"];
    int iTDS2 = (int)[[ToolHexManager sharedManager] numberWithHexString:strTDS2];
    
    NSString *strSetUsingTime1 = jsonDict[@"strSetUsingTime1"];
    NSString *strSetUsingTime2 = jsonDict[@"strSetUsingTime2"];
    NSString *strSetUsingTime3 = jsonDict[@"strSetUsingTime3"];
    
    NSString *strSetUsingTime4 = jsonDict[@"strSetUsingTime4"];
    NSString *strSetUsingTime5 = jsonDict[@"strSetUsingTime5"];
    
    
    //滤芯1
    NSString *strPercent1 = [self handleGetPercentWithSetTime:strSetUsingTime1 hasBeenUsingTime:strHaveBeenUsingTime1];
    NSString *strPercent2 = [self handleGetPercentWithSetTime:strSetUsingTime2 hasBeenUsingTime:strHaveBeenUsingTime2];
    NSString *strPercent3 = [self handleGetPercentWithSetTime:strSetUsingTime3 hasBeenUsingTime:strHaveBeenUsingTime3];
    NSString *strPercent4 = [self handleGetPercentWithSetTime:strSetUsingTime4 hasBeenUsingTime:strHaveBeenUsingTime4];
    NSString *strPercent5 = [self handleGetPercentWithSetTime:strSetUsingTime5 hasBeenUsingTime:strHaveBeenUsingTime5];
    
    
    UIButton *btn1 = [self.view viewWithTag:100];
    [btn1 setTitle:strPercent1 forState:UIControlStateNormal];
    
    UIButton *btn2 = [self.view viewWithTag:101];
    [btn2 setTitle:strPercent2 forState:UIControlStateNormal];
    
    UIButton *btn3 = [self.view viewWithTag:102];
    [btn3 setTitle:strPercent3 forState:UIControlStateNormal];
    
    UIButton *btn4 = [self.view viewWithTag:103];
    [btn4 setTitle:strPercent4 forState:UIControlStateNormal];
    
    UIButton *btn5 = [self.view viewWithTag:104];
    [btn5 setTitle:strPercent5 forState:UIControlStateNormal];
    
    self.textViewContent.text = [NSString stringWithFormat:@"设备状态：%@;\n水压：%@\n水温:%d;\n原水TDS1:%d\n纯水TDS2:%d",strWaterFilterStatueEnd,strWaterPressurestrEnd,iWaterTemperture,iTDS1,iTDS2];
}

- (NSString *)handleGetPercentWithSetTime:(NSString *)strSetTime hasBeenUsingTime:(NSString *)strHaveBeenUsingTime
{
    int iSetTime = (int)[[ToolHexManager sharedManager] numberWithHexString:strSetTime];
    int iUseTime = (int)[[ToolHexManager sharedManager] numberWithHexString:strHaveBeenUsingTime];
    
//    CGFloat fSetTime = iSetTime * 0.01;
//    CGFloat fUseTime = iUseTime * 0.01;
//    CGFloat fPercent = fUseTime/fSetTime;
//    
//    NSString *str1 = [NSString stringWithFormat:@"%f",fPercent];
//    
////    return [NSString stringWithFormat:@"%.2f%%",[str1 floatValue]*100];
//    return [NSString stringWithFormat:@"%f%%",[str1 floatValue]*100];
    
    
    int iPercent = iUseTime/iSetTime;
    
    NSString *str1 = [NSString stringWithFormat:@"%d",iPercent];
    
    //    return [NSString stringWithFormat:@"%.2f%%",[str1 floatValue]*100];
    return [NSString stringWithFormat:@"%d%%",[str1 intValue]*100];
}

- (NSString *)handleGetStatue:(NSString *)strWaterFilterStatue
{
    //设备状态：0，冲洗；1：水满； 2，制水； 3，缺水；4，检修；5，关机；
    NSString *strTemp = @"暂无状态";
    int iWaterFilterStatue = (int)[[ToolHexManager sharedManager] numberWithHexString:strWaterFilterStatue];
    switch (iWaterFilterStatue) {
        case 0:
        {
            strTemp = @"冲洗";
        }
            break;
        case 1:
        {
            strTemp = @"水满";
        }
            break;
        case 2:
        {
            strTemp = @"制水";
        }
            break;
        case 3:
        {
            strTemp = @"缺水";
        }
            break;
        case 4:
        {
            strTemp = @"关机";
        }
            break;
            
        default:
            break;
    }
    
    return strTemp;
}

- (NSString *)handleGetWaterPressurestr:(NSString *)strWaterPressurestr
{
    //水压，0,缺水；1，有水；
    NSString *strTemp = @"暂无水压";
    int iWaterPressurestr = (int)[[ToolHexManager sharedManager] numberWithHexString:strWaterPressurestr];
    switch (iWaterPressurestr) {
        case 0:
        {
           strTemp = @"有水";
        }
            break;
        case 1:
        {
            strTemp = @"无水";
        }
            break;
            
        default:
            break;
    }
    return strTemp;
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
