//
//  SHColourBulbVC.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/12/29.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHColourBulbVC.h"
#import "ISColorWheel.h"
#import "LLSwitch.h"
#import <IQKeyboardManager.h>
#import <IQKeyboardReturnKeyHandler.h>

@interface SHColourBulbVC ()<ISColorWheelDelegate,UINavigationControllerDelegate,LLSwitchDelegate,UITextFieldDelegate>
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


@end

@implementation SHColourBulbVC

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
    if (self.itype == 0) {
        self.tabBarController.tabBar.hidden = YES;
    }
    self.navigationController.delegate = self;
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
    [self setTitleViewText:@"多彩灯泡"];
//    self.title = @"多彩灯泡";
    self.isFirstAsk = YES;
    self.networkEngine = [NetworkEngine shareInstance];
    [self doRegisterKVO];
    self.strHexLightingMode = @"01";
    self.iDelayTime = 5;
    self.view.backgroundColor = [UIColor whiteColor];
    [self doInitSubViews];
    
    [self doGetColourBulbState];
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
            _brightnessSlider.value = iLength;
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
    [self setNavigationBarLeftBarButtonWithTitle:@"返回" action:@selector(leftAction:)];
    
    CGSize size = self.view.bounds.size;
    CGSize wheelSize = CGSizeMake(300, 300);
    
    CGRect frameColorWheel = CGRectMake((UI_SCREEN_WIDTH - 300)/2.0,
                                        20,
                                        wheelSize.width,
                                        wheelSize.height);
    [self doInitWheel:frameColorWheel];
    
    
    CGRect frameSlider = CGRectMake(10,
                                    _colorWheel.frame.origin.x + _colorWheel.frame.size.height + 20,
                                    UI_SCREEN_WIDTH - 20,
                                    size.height * 0.05);
    [self doInitSlider:frameSlider];
    

    CGRect frameSwitch = CGRectMake(10,
                                    _brightnessSlider.frame.origin.y + _brightnessSlider.frame.size.height + 15,
                                    UI_SCREEN_WIDTH - 20,
                                    30);
    [self doInitLLSwitch:frameSwitch];
    
   
    CGRect frameSege = CGRectMake(10,
                                  frameSwitch.origin.y + frameSwitch.size.height + 15,
                                  UI_SCREEN_WIDTH - 20,
                                  30);
    [self doInitSegementWithFrame:frameSege];
    
   
    CGRect frameTextFiled = CGRectMake(10,
                                       frameSege.origin.y + frameSege.size.height + 15,
                                       UI_SCREEN_WIDTH - 20,
                                       30);
    [self doInitTextFiled:frameTextFiled];
    
    CGRect frameSegeModel = CGRectMake(10,
                                       frameTextFiled.origin.y + frameTextFiled.size.height + 15,
                                       UI_SCREEN_WIDTH - 20,
                                       30);
    [self doInitSegementModelWithFrame:frameSegeModel];
    
}

- (void)leftAction:(UIButton *)sender
{
    [self.navigationController popViewControllerAnimated:YES];
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
    [self.view addSubview:_brightnessSlider];
}

- (void)doInitLLSwitch:(CGRect)frame
{
    LLSwitch *llSwitch = [[LLSwitch alloc] initWithFrame:frame];
    llSwitch.delegate = self;
    [self.view addSubview:llSwitch];
    
    if (self.device.iDevice_device_state1  == 1) {
        llSwitch.on = YES;
    }else{
        llSwitch.on = NO;
    }
}

- (void)doInitSegementWithFrame:(CGRect)frame
{
    NSArray *itemArray = @[@"直接",@"渐渐",@"延时"];
    UISegmentedControl *segment = [[UISegmentedControl alloc]initWithItems:itemArray];
    segment.frame = frame;
    segment.selectedSegmentIndex = 0;
    segment.apportionsSegmentWidthsByContent = YES;
    segment.tintColor = kCommonColor;
    [segment addTarget:self action:@selector(change:) forControlEvents:UIControlEventValueChanged];
    [self.view addSubview:segment];
}

- (void)doInitTextFiled:(CGRect)frameTextFiled
{
    [IQKeyboardManager sharedManager].enableAutoToolbar = NO;
    returnKeyHandler = [[IQKeyboardReturnKeyHandler alloc] initWithViewController:self];
    [IQKeyboardManager sharedManager].shouldResignOnTouchOutside = YES;
    
    text = [[UITextField alloc]initWithFrame:frameTextFiled];
    text.borderStyle = UITextBorderStyleRoundedRect;
    text.backgroundColor = [UIColor whiteColor];
    text.delegate = self;
    // 设置enable为NO 时的背景图片
    text.background = [UIImage imageNamed:@"dd.png"];
    text.disabledBackground = [UIImage imageNamed:@"cc.png"];
    text.placeholder = @"请输入延时时间";
    text.font = [UIFont fontWithName:@"Arial" size:15.0f];
    text.textColor = [UIColor redColor];
    text.clearButtonMode = UITextFieldViewModeAlways;
    text.clearsOnBeginEditing = YES;
    text.textAlignment = NSTextAlignmentLeft;
    text.adjustsFontSizeToFitWidth = YES;
    text.returnKeyType =UIReturnKeyDone;
    [self.view addSubview:text];
    
    text.hidden = YES;
}

- (void)doInitSegementModelWithFrame:(CGRect)frame
{
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
-(void)change:(UISegmentedControl *)sender{
    if (sender.selectedSegmentIndex == 0) {
        text.hidden = YES;
        self.strHexLightingMode = @"01";
    }else if (sender.selectedSegmentIndex == 1){
        text.hidden = YES;
        self.strHexLightingMode = @"02";
    }else if (sender.selectedSegmentIndex == 2){
        text.hidden = NO;
        self.strHexLightingMode = @"03";
    }
}

- (void)changeModel:(UISegmentedControl *)sender
{
    if (sender.selectedSegmentIndex == 0) {
        
        self.strHexLightingMode = @"09";
        
        NSData *data = [[NetworkEngine shareInstance] doGetColourBulbAllKindsOfModelOneWithTargetAddr:self.device.strDevice_mac_address
                                                                                           lightingMode:@"09"];
        [[NetworkEngine shareInstance] sendRequestData:data];
        
    }else if (sender.selectedSegmentIndex == 1){
        self.strHexLightingMode = @"0A";
        NSData *data = [[NetworkEngine shareInstance] doGetColourBulbAllKindsOfModelTwoWithTargetAddr:self.device.strDevice_mac_address
                                                                                           lightingMode:@"0A"];
        [[NetworkEngine shareInstance] sendRequestData:data];
    }else if (sender.selectedSegmentIndex == 2){
        self.strHexLightingMode = @"0B";
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
    [self doSendPaletteLightData];
}

//UITextFieldDelegate
- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    return [textField resignFirstResponder];
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
