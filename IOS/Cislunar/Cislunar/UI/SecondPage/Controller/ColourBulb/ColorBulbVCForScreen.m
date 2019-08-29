//
//  ColorBulbVCForScreen.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/6/3.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import "ColorBulbVCForScreen.h"
#import "ISColorWheel.h"
#import "LLSwitch.h"
#import <IQKeyboardManager.h>
#import <IQKeyboardReturnKeyHandler.h>

@interface ColorBulbVCForScreen ()<ISColorWheelDelegate,UINavigationControllerDelegate,LLSwitchDelegate,UITextFieldDelegate>
{
    IQKeyboardReturnKeyHandler *returnKeyHandler;
    UITextField *text;
}
@property (strong, nonatomic) ISColorWheel* colorWheel;
@property (strong, nonatomic) UISlider* brightnessSlider;

@property (assign, nonatomic) int iDelayTime;
@property (strong, nonatomic) NSString *strHexLightingMode;
@property (assign, nonatomic) int iBrightnessValue;

@property (assign, nonatomic) ISColorWheelPixelRGB currentPixel;
@property (assign, nonatomic) NetworkEngine *networkEngine;
@property (assign, nonatomic) BOOL isFirstAsk;

//new
@property (assign, nonatomic) int iHexR;
@property (assign, nonatomic) int iHexG;
@property (assign, nonatomic) int iHexB;
@property (strong, nonatomic) NSString *strHexModel;

@end

@implementation ColorBulbVCForScreen

#pragma mark - UINavigationControllerDelegate
- (void)navigationController:(UINavigationController *)navigationController
      willShowViewController:(UIViewController *)viewController
                    animated:(BOOL)animated {
    BOOL isHomePage = [viewController isKindOfClass:[self class]];
    [self.navigationController setNavigationBarHidden:!isHomePage animated:YES];
}

- (void)viewDidLoad {
    [super viewDidLoad];
//    [self setTitleViewText:@"多彩灯泡"];
    self.title = @"多彩灯泡";
    self.isFirstAsk = YES;
    self.networkEngine = [NetworkEngine shareInstance];
    [self doRegisterKVO];
    self.strHexLightingMode = @"01";
    self.iDelayTime = 5;
    [self doInitSubViews];
    
    [self doGetColourBulbState];
    self.strHexModel = @"01";
    self.iHexR = 255;
    self.iHexG = 255;
    self.iHexB = 255;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navigationController.delegate = self;
}

- (void)doGetColourBulbState
{
    NSData *data = [[NetworkEngine shareInstance] doGetColourBulbParameterWithTargetAddr:self.device.strDevice_mac_address];
    [[NetworkEngine shareInstance] sendRequestData:data];
}

- (void)doRegisterKVO
{
    //监测设备上报添加
    [self observeKeyPath:@keypath(self.networkEngine.modelDevice) block:^(id value) {
        SHModelDevice *deviceReport = (SHModelDevice *)value;
        NSLog(@"deviceReport.sinDexlegth == %@",deviceReport.strDevice_sindex_length);
        if (self.isFirstAsk) {
            int iLength = (int)[[ToolHexManager sharedManager] numberWithHexString:deviceReport.strDevice_sindex_length];
            self->_brightnessSlider.value = iLength;
            self.isFirstAsk = NO;
        }
        
    }];
}

- (void)doSendLighterDimmerData
{
    //延时时间
    NSString *strHexDelayTime = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%04x",self.iDelayTime]];
    NSString *strHexBrightnessValue = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%02x",self.iBrightnessValue]];
    
    NSData *data = [[NetworkEngine shareInstance] doGetColourBulbDimmerWithTargetAddr:self.device.strDevice_mac_address
                                                                           lightingMode:self.strHexLightingMode
                                                                           lightingType:@"02"
                                                                            controlTime:strHexDelayTime
                                                                         lightingSwitch:@"01"
                                                                      brightnessControl:strHexBrightnessValue];
    [[NetworkEngine shareInstance] sendRequestData:data];
    
}

- (void)doSendPaletteLightData
{
    //延时时间
    NSString *strHexDelayTime = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%04x",self.iDelayTime]];
    NSString *strHexRedValue = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%02x",(int)self.currentPixel.r]];
    NSString *strHexGreenValue = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%02x",(int)self.currentPixel.g]];
    NSString *strHexBlueValue = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%02x",(int)self.currentPixel.b]];
    
    NSData *data = [[NetworkEngine shareInstance] doGetColourBulbPaletteWithTargetAddr:self.device.strDevice_mac_address
                                                                            lightingMode:self.strHexLightingMode
                                                                            lightingType:@"03"
                                                                             controlTime:strHexDelayTime
                                                                          lightingSwitch:@"01"
                                                                           redLightValue:strHexRedValue
                                                                         greenLightValue:strHexGreenValue
                                                                          blueLightValue:strHexBlueValue];
    [[NetworkEngine shareInstance] sendRequestData:data];
}

- (void)doInitSubViews
{
    [self doSetRightBtn];
    
    CGSize size = self.view.bounds.size;
    CGSize wheelSize = CGSizeMake(200, 200);
    
    CGRect frameColorWheel = CGRectMake((UI_SCREEN_WIDTH - 200)/2.0,
                                        20,
                                        wheelSize.width,
                                        wheelSize.height);
    [self doInitWheel:frameColorWheel];
    
    
    CGRect frameSlider = CGRectMake(10,
                                    _colorWheel.frame.origin.x + 200,
                                    UI_SCREEN_WIDTH - 20,
                                    size.height * 0.05);
    [self doInitSlider:frameSlider];
    
    
    
    CGRect frameSwitch = CGRectMake(10,
                                    frameSlider.origin.y + frameSlider.size.height + 15,
                                    UI_SCREEN_WIDTH - 20,
                                    30);
    [self doInitLLSwitch:frameSwitch];
    
    
    
    CGRect frameSegeModel = CGRectMake(10,
                                       frameSwitch.origin.y + frameSwitch.size.height + 15,
                                       UI_SCREEN_WIDTH - 20,
                                       30);
    [self doInitSegementModelWithFrame:frameSegeModel];
    
    
}

#pragma mark -- 设置导航栏右边历史账单按钮
- (void)doSetRightBtn {
    UIButton *rightBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [rightBtn setFrame:CGRectMake(80, 10, 70, 25)];
    rightBtn.titleLabel.font = [UIFont systemFontOfSize:16];
    [rightBtn setTitleEdgeInsets:UIEdgeInsetsMake(0, 0, 0, -15)];
    [rightBtn addTarget:self action:@selector(rightAction:) forControlEvents:UIControlEventTouchUpInside];
    [rightBtn setTitle:@"完成" forState:UIControlStateNormal];
    [rightBtn setTag:12];
    UIBarButtonItem *rightBtnItem = [[UIBarButtonItem alloc]initWithCustomView:rightBtn];
    self.navigationItem.rightBarButtonItem = rightBtnItem;
}


- (void)doInitWheel:(CGRect)frame
{
    _colorWheel = [[ISColorWheel alloc] initWithFrame:frame];
    _colorWheel.delegate = self;
    _colorWheel.continuous = true;
    //    _colorWheel.backgroundColor = [UIColor brownColor];
    [self.view addSubview:_colorWheel];
}

- (void)doInitSlider:(CGRect)frame
{
    _brightnessSlider = [[UISlider alloc] initWithFrame:frame];
    
    //    _brightnessSlider.backgroundColor = [UIColor orangeColor];
    _brightnessSlider.minimumValue = 0;
    _brightnessSlider.maximumValue = 255;
    _brightnessSlider.value = 0;
    _brightnessSlider.continuous = true;
    [_brightnessSlider addTarget:self action:@selector(changeBrightness:) forControlEvents:UIControlEventTouchUpInside];
    _brightnessSlider.hidden = YES;
    [self.view addSubview:_brightnessSlider];
}

- (void)doInitLLSwitch:(CGRect)frame
{
    LLSwitch *llSwitch = [[LLSwitch alloc] initWithFrame:frame];
    llSwitch.hidden = YES;
    llSwitch.delegate = self;
    [self.view addSubview:llSwitch];
    
    if (self.device.iDevice_device_state1  == 1) {
        llSwitch.on = YES;
    }else{
        llSwitch.on = NO;
    }
}


- (void)doInitSegementModelWithFrame:(CGRect)frame
{
    //    NSArray *itemArray = @[@"直接",@"七彩渐变",@"七彩跳变",@"呼吸灯"];
    //    UISegmentedControl *segment = [[UISegmentedControl alloc]initWithItems:itemArray];
    //    segment.frame = frame;
    //    segment.selectedSegmentIndex = 5;
    //    segment.apportionsSegmentWidthsByContent = YES;
    //    segment.tintColor = kCommonColor;
    //    [segment addTarget:self action:@selector(changeModel:) forControlEvents:UIControlEventValueChanged];
    //    [self.view addSubview:segment];
    
    NSArray *itemArray = @[@"七彩渐变",@"七彩跳变",@"呼吸灯"];
    UISegmentedControl *segment = [[UISegmentedControl alloc]initWithItems:itemArray];
    segment.frame = frame;
    segment.selectedSegmentIndex = 5;
    segment.apportionsSegmentWidthsByContent = YES;
    segment.tintColor = kCommonColor;
    [segment addTarget:self action:@selector(changeModel:) forControlEvents:UIControlEventValueChanged];
    [self.view addSubview:segment];
}





#pragma mark -
#pragma mark -action

- (void)rightAction:(id)sender
{
    //延时时间
    if ([self.strHexModel isEqualToString:@"01"] || [self.strHexModel isEqualToString:@"0B"]) {
        
        NSString *strHexDelayTime = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%04x",self.iDelayTime]];
        NSString *strHexRedValue = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%02x",self.iHexR]];
        NSString *strHexGreenValue = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%02x",self.iHexG]];
        NSString *strHexBlueValue = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%02x",self.iHexB]];
        
        NSData *data = [[NetworkEngine shareInstance] doGetColourBulbPaletteWithTargetAddr:self.device.strDevice_mac_address
                                                                                lightingMode:self.strHexModel
                                                                                lightingType:@"03"
                                                                                 controlTime:strHexDelayTime
                                                                              lightingSwitch:@"01"
                                                                               redLightValue:strHexRedValue
                                                                             greenLightValue:strHexGreenValue
                                                                              blueLightValue:strHexBlueValue];
        [[NetworkEngine shareInstance] sendRequestData:data];
        self.device.arrOrder = [NSArray arrayWithObject:data];
        
        
        
    } else if([self.strHexModel isEqualToString:@"09"]){
        
        NSData *data = [[NetworkEngine shareInstance] doGetColourBulbAllKindsOfModelOneWithTargetAddr:self.device.strDevice_mac_address
                                                                                           lightingMode:@"09"];
        [[NetworkEngine shareInstance] sendRequestData:data];
        
        self.device.arrOrder = [NSArray arrayWithObject:data];
    }else if([self.strHexModel isEqualToString:@"0A"]){
        
        NSData *data = [[NetworkEngine shareInstance] doGetColourBulbAllKindsOfModelTwoWithTargetAddr:self.device.strDevice_mac_address
                                                                                           lightingMode:@"0A"];
        [[NetworkEngine shareInstance] sendRequestData:data];
        
        self.device.arrOrder = [NSArray arrayWithObject:data];
    }
    
    
    if (self.BlockGetNewDevice) {
        self.BlockGetNewDevice(self.device);
    }
    
    
    [self.navigationController popViewControllerAnimated:YES];
    
}

- (void)changeModel:(UISegmentedControl *)sender
{
    /*
     if (sender.selectedSegmentIndex == 0) {
     
     self.strHexLightingMode = @"01";
     self.strHexModel = @"01";
     
     }else*/
    
    if (sender.selectedSegmentIndex == 0) {
        
        self.strHexLightingMode = @"09";
        self.strHexModel = @"09";
        
        NSData *data = [[NetworkEngine shareInstance] doGetColourBulbAllKindsOfModelOneWithTargetAddr:self.device.strDevice_mac_address
                                                                                           lightingMode:@"09"];
        [[NetworkEngine shareInstance] sendRequestData:data];
        
    }else if (sender.selectedSegmentIndex == 1){
        self.strHexLightingMode = @"0A";
        self.strHexModel = @"0A";
        NSData *data = [[NetworkEngine shareInstance] doGetColourBulbAllKindsOfModelTwoWithTargetAddr:self.device.strDevice_mac_address
                                                                                           lightingMode:@"0A"];
        [[NetworkEngine shareInstance] sendRequestData:data];
    }else if (sender.selectedSegmentIndex == 2){
        self.strHexLightingMode = @"0B";
        self.strHexModel = @"0B";
        NSData *data = [[NetworkEngine shareInstance] doGetColourBulbAllKindsOfModelThreeWithTargetAddr:self.device.strDevice_mac_address
                                                                                             lightingMode:@"0B"];
        [[NetworkEngine shareInstance] sendRequestData:data];
    }
}

#pragma mark -
#pragma mark - delegate

//LLSwitchDelegate
-(void)didTapLLSwitch:(LLSwitch *)llSwitch {
    NSLog(@"start");
}

- (void)animationDidStopForLLSwitch:(LLSwitch *)llSwitch {
    NSLog(@"stop");
    NSString *strHexState = @"00";
    if (llSwitch.on) {
        _brightnessSlider.value = 255;
        self.iBrightnessValue = (int)_brightnessSlider.value;;
        strHexState = @"01";
    }else{
        _brightnessSlider.value = 0;
        self.iBrightnessValue = (int)_brightnessSlider.value;
        strHexState = @"02";
    }
    
    //延时时间
    NSString *strHexDelayTime = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%04x",self.iDelayTime]];
    NSString *strHexBrightnessValue = [NSString stringWithFormat:@"%@",[[NSString alloc] initWithFormat:@"%02x",self.iBrightnessValue]];
    
    NSData *data = [[NetworkEngine shareInstance] doGetColourBulbDimmerWithTargetAddr:self.device.strDevice_mac_address
                                                                           lightingMode:self.strHexLightingMode
                                                                           lightingType:@"02"
                                                                            controlTime:strHexDelayTime
                                                                         lightingSwitch:strHexState
                                                                      brightnessControl:strHexBrightnessValue];
    [[NetworkEngine shareInstance] sendRequestData:data];
}


//ISColorWheelDelegate
- (void)changeBrightness:(UISlider*)sender
{
    /*
     向上取整:ceil(x),返回不小于x的最小整数;
     向下取整:floor(x),返回不大于x的最大整数;
     四舍五入:round(x)
     截尾取整函数:trunc(x)
     */
    
    self.iBrightnessValue = ceil(_brightnessSlider.value);
    NSLog(@"*********%f***********xxx=%d",_brightnessSlider.value,self.iBrightnessValue);
    //    [_colorWheel setBrightness:_brightnessSlider.value];
    
    [self doSendLighterDimmerData];
}

- (void)colorWheelDidChangeColor:(ISColorWheel *)colorWheel currentPixel:(ISColorWheelPixelRGB)currentPixel
{
    //    self.view.backgroundColor = RGB(currentPixel.r,currentPixel.g,currentPixel.b);
    self.currentPixel = currentPixel;
    
    int ired = (int)currentPixel.r;
    int iGreen = (int)currentPixel.g;
    int iBlue = (int)currentPixel.b;
    NSLog(@"彩色灯泡色值：r-%d;g-%d;b-%d",ired,iGreen,iBlue);
    self.iHexR = ired;
    self.iHexG = iGreen;
    self.iHexB = iBlue;
    [self doSendPaletteLightData];
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

