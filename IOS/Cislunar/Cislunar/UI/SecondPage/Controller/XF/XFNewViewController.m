//
//  XFNewViewController.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2018/8/5.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "XFNewViewController.h"
#import "WZLBadgeImport.h"
#import "SGEasyButton.h"
#import <LEEAlert/LEEAlert.h>
#import "FontSizeView.h"
#import "NSObject+AssociatedObject.h"
#import "NetworkEngine.h"

typedef enum : NSUInteger {
    XFButtonBorderStyleTop,
    XFButtonBorderStyleBottom,
    XFButtonBorderStyleLeft,
    XFButtonBorderStyleRigth,
} XFButtonBorderStyle;

@interface XFNewViewController ()

@property (assign, nonatomic) NetworkEngine *networkEngine;
//导航栏
@property (weak, nonatomic) IBOutlet UILabel *labelTitleName;
@property (weak, nonatomic) IBOutlet UIButton *btnAlarm;

//第一部分
@property (weak, nonatomic) IBOutlet UIImageView *imageVCircle;
@property (weak, nonatomic) IBOutlet UILabel *labelCO2Value;
@property (weak, nonatomic) IBOutlet UILabel *labelPMValue;
@property (weak, nonatomic) IBOutlet UIButton *btnTempretureOut;
@property (weak, nonatomic) IBOutlet UIButton *btnTempretureIn;
@property (weak, nonatomic) IBOutlet UILabel *labelWindSpeed;

//第二部分
@property (weak, nonatomic) IBOutlet UIView *featuresBgView;
@property (weak, nonatomic) IBOutlet UIButton *btnOn;
@property (weak, nonatomic) IBOutlet UIButton *btnHeating;
@property (weak, nonatomic) IBOutlet UIButton *btnTiming;
@property (weak, nonatomic) IBOutlet UIButton *btnWorkMode;
@property (weak, nonatomic) IBOutlet UIButton *btnCircleMode;
@property (weak, nonatomic) IBOutlet UIButton *btnReturnAirMode;

//第三部分风速
@property (weak, nonatomic) IBOutlet UIView *windSliderView;
@property (weak, nonatomic) IBOutlet UIButton *btnLess;
@property (weak, nonatomic) IBOutlet UIButton *btnAdd;
@property (weak, nonatomic) IBOutlet UISlider *sliderWindSpeed;
@property (strong, nonatomic)UILabel *labelWindSpeedValue;
@property (assign, nonatomic) NSInteger iWindSpeed;
@property (assign, nonatomic) NSInteger iWindTemp;

@end

@implementation XFNewViewController

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
    self.iWindTemp = 0;
    self.networkEngine = [NetworkEngine shareInstance];
    [self doRegiserKVO];
    [self doGetXFCurrentStatus];
    [self doInitOnePart];
    [self doInitSecondPart];
    [self doInitWindSpeed];
    // Do any additional setup after loading the view.
}

#pragma mark -
#pragma mark - 检测数据上报
- (void)doRegiserKVO
{
    //监测设备上报添加
    @weakify(self);
    [self observeKeyPath:@keypath(self.networkEngine.dictXF) block:^(id value) {
        @strongify(self);
        NSDictionary *dictXF = (NSDictionary *)value;
        // NSLog(@"处于普通上报刷新状态");
        NSLog(@"功能码:%@;\n开关状态码：%@;\n点解热状态:%@;\n工作模式:%@;\n循环模式:%@;\n进回风比例:%@;\n风速档位:%@\n风量:%@;\n颗粒物:%@\nCO2含量:%@;\n进风温度:%@;\n出风温度:%@;\n设备故障码:%@;\n初滤百分比:%@;\n集尘箱剩余百分比:%@;\n高滤剩余百分比:%@;\n\n",dictXF[@"xf_fuctionCode"],dictXF[@"xf_switch"],dictXF[@"xf_helpWarm"],dictXF[@"xf_workMode"],dictXF[@"xf_circulationMode"],dictXF[@"xf_inAndOutWindScale"],dictXF[@"xf_windSpeedGear"],dictXF[@"xf_airVolume"],dictXF[@"xf_particulates"],dictXF[@"xf_CO2ConCtent"],dictXF[@"xf_intakeTemperature"],dictXF[@"xf_outflowTemperature"],dictXF[@"xf_normalOrMalfunction"],dictXF[@"xf_chuLvTimePer"],dictXF[@"xf_dustBoxTimePer"],dictXF[@"xf_gaoLvTimePer"]);
        
        if ([dictXF[@"xf_switch"] intValue] == 0) {
            [self.btnOn setTitle:@"开" forState:(UIControlStateSelected)];
            [self.btnOn setImage:[UIImage imageNamed:@"xfOnPr"] forState:(UIControlStateSelected)];
        }else{
            [self.btnOn setTitle:@"关" forState:(UIControlStateNormal)];
            [self.btnOn setImage:[UIImage imageNamed:@"xfOnUn"] forState:(UIControlStateNormal)];
        }
        
        if ([dictXF[@"xf_helpWarm"] intValue] == 0) {
            [self.btnHeating setTitle:@"加热关" forState:(UIControlStateNormal)];
            [self.btnHeating setImage:[UIImage imageNamed:@"xfReturnHeatingUn"] forState:(UIControlStateNormal)];
        }else{
            [self.btnHeating setTitle:@"加热开" forState:(UIControlStateSelected)];
            [self.btnHeating setImage:[UIImage imageNamed:@"xfReturnHeatingPr"] forState:(UIControlStateSelected)];
        }
        
        if ([dictXF[@"xf_workMode"] intValue] == 0) {
            //手动
            [self.btnWorkMode setTitle:@"手动模式" forState:(UIControlStateNormal)];
            [self.btnWorkMode setImage:[UIImage imageNamed:@"xfWorkMode"] forState:(UIControlStateNormal)];
            
        }else if ([dictXF[@"xf_workMode"] intValue] == 1) {
            //自动
            [self.btnWorkMode setTitle:@"自动模式" forState:(UIControlStateNormal)];
            [self.btnWorkMode setImage:[UIImage imageNamed:@"xfAutoMode"] forState:(UIControlStateNormal)];
            
        }else{
            //定时
            [self.btnTiming setTitle:@"定时" forState:(UIControlStateNormal)];
            [self.btnTiming setImage:[UIImage imageNamed:@"xfTiming"] forState:(UIControlStateNormal)];
            [self doXFSetTopBorderWithButton:self.btnTiming borderType:(XFButtonBorderStyleBottom)];
        }
        
        if ([dictXF[@"xf_circulationMode"] intValue] == 0) {
            [self.btnCircleMode setTitle:@"外循环" forState:(UIControlStateNormal)];
            [self.btnCircleMode setImage:[UIImage imageNamed:@"xfCirclePr"] forState:(UIControlStateNormal)];
            
        }else if ([dictXF[@"xf_circulationMode"] intValue] == 1) {
            [self.btnCircleMode setTitle:@"内循环" forState:(UIControlStateNormal)];
            [self.btnCircleMode setImage:[UIImage imageNamed:@"xfCircleMode"] forState:(UIControlStateNormal)];
            
        }else{
            [self.btnCircleMode setTitle:@"自循环" forState:(UIControlStateNormal)];
            [self.btnCircleMode setImage:[UIImage imageNamed:@"xfAutoMode"] forState:(UIControlStateNormal)];
        }
        
        //        self.sliderWindSpeed.value = [dictXF[@"xf_windSpeedGear"] intValue];
        
        self.labelWindSpeed.text = [NSString stringWithFormat:@"%@m³/h",dictXF[@"xf_airVolume"]];
        
        self.labelPMValue.text = [NSString stringWithFormat:@"PM2.5:  %@ug/m³",dictXF[@"xf_particulates"]];
        
        self.labelCO2Value.text = [NSString stringWithFormat:@"%@",dictXF[@"xf_CO2ConCtent"]];
        
        
        NSString *strTempretureIn = [NSString stringWithFormat:@"%@°",dictXF[@"xf_intakeTemperature"]];
        NSString *strTempretureOut = [NSString stringWithFormat:@"%@°",dictXF[@"xf_outflowTemperature"]];
        
        [self.btnTempretureIn setTitle:strTempretureOut forState:(UIControlStateNormal)];
        [self.btnTempretureIn setImage:[UIImage imageNamed:@"xfTempreture"] forState:(UIControlStateNormal)];
        
        [self.btnTempretureOut setTitle:strTempretureIn forState:(UIControlStateNormal)];
        [self.btnTempretureOut setImage:[UIImage imageNamed:@"xfTempreture"] forState:(UIControlStateNormal)];
        
        
        NSString *strReturAirScale = [NSString stringWithFormat:@"%@",dictXF[@"xf_inAndOutWindScale"]];
        NSString *strTemp = @"0";
        switch ([strReturAirScale intValue]) {
            case 0:
            {
                NSLog(@"进回风比例：10：4");
                strTemp = @"10:4";
                [self.btnReturnAirMode showBadgeWithStyleText:WBadgeStyleNew text:strTemp animationType:WBadgeAnimTypeBreathe];
                
            }
                break;
            case 1:
            {
                NSLog(@"进回风比例：10：5");
                strTemp = @"10:5";
                [self.btnReturnAirMode showBadgeWithStyleText:WBadgeStyleNew text:strTemp animationType:WBadgeAnimTypeBreathe];
            }
                break;
            case 2:
            {
                NSLog(@"进回风比例：10：6");
                strTemp = @"10:6";
                [self.btnReturnAirMode showBadgeWithStyleText:WBadgeStyleNew text:strTemp animationType:WBadgeAnimTypeBreathe];
            }
                break;
            case 3:
            {
                NSLog(@"进回风比例：10：7");
                strTemp = @"10:7";
                [self.btnReturnAirMode showBadgeWithStyleText:WBadgeStyleNew text:strTemp animationType:WBadgeAnimTypeBreathe];
            }
                break;
            case 4:
            {
                NSLog(@"进回风比例：10：8");
                strTemp = @"10:8";
                [self.btnReturnAirMode showBadgeWithStyleText:WBadgeStyleNew text:strTemp animationType:WBadgeAnimTypeBreathe];
            }
                break;
            case 5:
            {
                NSLog(@"进回风比例：10：10");
                strTemp = @"10:10";
                [self.btnReturnAirMode showBadgeWithStyleText:WBadgeStyleNew text:strTemp animationType:WBadgeAnimTypeBreathe];
            }
                break;
            case 6:
            {
                NSLog(@"进回风比例：10：0");
                strTemp = @"10:0";
                [self.btnReturnAirMode showBadgeWithStyleText:WBadgeStyleNew text:strTemp animationType:WBadgeAnimTypeBreathe];
            }
                break;
            case 7:
            {
                NSLog(@"进回风比例：0：10");
                strTemp = @"0:10";
                [self.btnReturnAirMode showBadgeWithStyleText:WBadgeStyleNew text:strTemp animationType:WBadgeAnimTypeBreathe];
            }
                break;
                
                
            default:
                break;
        }
        
        
        
    }];
}

#pragma mark -
#pragma mark - 导航栏

- (void)doInitNavSubViews
{
    
}

- (IBAction)btnBackPressed:(UIButton *)sender {
    [self.navigationController popViewControllerAnimated:YES];
    
}

#pragma mark -
#pragma mark - 第一部分处理
- (void)doInitOnePart
{
    [self initAnimationWithImageView:self.imageVCircle onSpeed:0.2];
    
    [self.btnTempretureOut SG_imagePositionStyle:SGImagePositionStyleDefault spacing:5 imagePositionBlock:^(UIButton *button) {
        [button setTitle:@"32°" forState:(UIControlStateNormal)];
        [button setImage:[UIImage imageNamed:@"xfTempreture"] forState:(UIControlStateNormal)];
        
    }];
    [self.btnTempretureIn SG_imagePositionStyle:SGImagePositionStyleDefault spacing:5 imagePositionBlock:^(UIButton *button) {
        [button setTitle:@"28°" forState:(UIControlStateNormal)];
        [button setImage:[UIImage imageNamed:@"xfTempreture"] forState:(UIControlStateNormal)];
        
    }];
}

#pragma mark -
#pragma mark - 第二部分
- (void)doInitSecondPart
{
    //    [self.btnTiming showBadgeWithStyleText:WBadgeStyleNew text:@"2h" animationType:WBadgeAnimTypeBreathe];
    //    [self.btnWorkMode showBadgeWithStyle:WBadgeStyleNew text:@"手动" animationType:WBadgeAnimTypeShake];
    //    [self.btnCircleMode showBadgeWithStyle:WBadgeStyleNew text:@"内循环" animationType:WBadgeAnimTypeShake];
    [self.btnReturnAirMode showBadgeWithStyleText:WBadgeStyleNew text:@"1.8" animationType:WBadgeAnimTypeBreathe];
    
    [self.btnOn SG_imagePositionStyle:SGImagePositionStyleTop spacing:5 imagePositionBlock:^(UIButton *button) {
        [button setTitle:@"开" forState:(UIControlStateSelected)];
        [button setImage:[UIImage imageNamed:@"xfOnPr"] forState:(UIControlStateSelected)];
        
        [button setTitle:@"关" forState:(UIControlStateNormal)];
        [button setImage:[UIImage imageNamed:@"xfOnUn"] forState:(UIControlStateNormal)];
    }];
    
    [self.btnHeating SG_imagePositionStyle:SGImagePositionStyleTop spacing:5 imagePositionBlock:^(UIButton *button) {
        [button setTitle:@"加热关" forState:(UIControlStateNormal)];
        [button setImage:[UIImage imageNamed:@"xfReturnHeatingUn"] forState:(UIControlStateNormal)];
        
        [button setTitle:@"加热开" forState:(UIControlStateSelected)];
        [button setImage:[UIImage imageNamed:@"xfReturnHeatingPr"] forState:(UIControlStateSelected)];
    }];
    
    [self.btnTiming SG_imagePositionStyle:SGImagePositionStyleTop spacing:5 imagePositionBlock:^(UIButton *button) {
        [button setTitle:@"定时" forState:(UIControlStateNormal)];
        [button setImage:[UIImage imageNamed:@"xfTiming"] forState:(UIControlStateNormal)];
        
    }];
    
    [self.btnWorkMode SG_imagePositionStyle:SGImagePositionStyleTop spacing:5 imagePositionBlock:^(UIButton *button) {
        [button setTitle:@"手动模式" forState:(UIControlStateNormal)];
        [button setImage:[UIImage imageNamed:@"xfWorkMode"] forState:(UIControlStateNormal)];
        
    }];
    
    [self.btnCircleMode SG_imagePositionStyle:SGImagePositionStyleTop spacing:5 imagePositionBlock:^(UIButton *button) {
        [button setTitle:@"外循环" forState:(UIControlStateNormal)];
        [button setImage:[UIImage imageNamed:@"xfCirclePr"] forState:(UIControlStateNormal)];
        
    }];
    
    [self.btnReturnAirMode SG_imagePositionStyle:SGImagePositionStyleTop spacing:5 imagePositionBlock:^(UIButton *button) {
        [button setTitle:@"回风比例" forState:(UIControlStateNormal)];
        [button setImage:[UIImage imageNamed:@"xfReturnAir"] forState:(UIControlStateNormal)];
        
    }];
    
    [self doXFSetTopBorderWithButton:self.btnOn borderType:(XFButtonBorderStyleRigth)];
    [self doXFSetTopBorderWithButton:self.btnOn borderType:(XFButtonBorderStyleBottom)];
    
    [self doXFSetTopBorderWithButton:self.btnHeating borderType:(XFButtonBorderStyleRigth)];
    [self doXFSetTopBorderWithButton:self.btnHeating borderType:(XFButtonBorderStyleBottom)];
    
    [self doXFSetTopBorderWithButton:self.btnTiming borderType:(XFButtonBorderStyleBottom)];
    
    [self doXFSetTopBorderWithButton:self.btnWorkMode borderType:(XFButtonBorderStyleRigth)];
    [self doXFSetTopBorderWithButton:self.btnWorkMode borderType:(XFButtonBorderStyleBottom)];
    
    [self doXFSetTopBorderWithButton:self.btnCircleMode borderType:(XFButtonBorderStyleRigth)];
    [self doXFSetTopBorderWithButton:self.btnCircleMode borderType:(XFButtonBorderStyleBottom)];
    
    [self doXFSetTopBorderWithButton:self.btnReturnAirMode borderType:(XFButtonBorderStyleBottom)];
    
    
    
    
}



#pragma mark -
#pragma mark - 功能按钮

- (IBAction)btnOnPressed:(UIButton *)sender {
    sender.selected = !sender.selected;
    if (sender.selected) {
        [self doSetXFOn];
        NSLog(@"设置开机！");
    }else{
        [self doSetXFOff];
        NSLog(@"设置关机！");
    }
}

- (IBAction)btnHeatingPressed:(UIButton *)sender {
    sender.selected = !sender.selected;
    if (sender.selected) {
        [self doSetHeatingOn];
        NSLog(@"设置加热开！");
    }else{
        [self doSetHeatingOff];
        NSLog(@"设置加热关！");
    }
}

- (IBAction)btnTimingPressed:(UIButton *)sender {
    
    // 弹出时间选择
    [PopupTool popUpWithController:self type:PopType_TimeSelect success:^(id info) {
        
    }];
}

- (IBAction)btnWorkModePressed:(UIButton *)sender {
    [LEEAlert actionsheet].config
    .LeeTitle(@"工作模式")
    .LeeAddAction(^(LEEAction *action) {
        
        action.type = LEEActionTypeDefault;
        
        action.title = @"手动";
        
        action.titleColor = [UIColor brownColor];
        
        action.highlight = @"选择啦";
        
        action.highlightColor = [UIColor orangeColor];
        
        action.image = [UIImage imageNamed:@"xfWorkMode"];
        
        action.highlightImage = [UIImage imageNamed:@"xfWorkMode"];
        
        action.imageEdgeInsets = UIEdgeInsetsMake(0, -10, 0, 10);
        
        action.height = 60.0f;
        
        @weakify(self)
        action.clickBlock = ^{
            @strongify(self);
            // 点击事件Block
            [self.btnWorkMode SG_imagePositionStyle:SGImagePositionStyleTop spacing:5 imagePositionBlock:^(UIButton *button) {
                [button setTitle:@"手动模式" forState:(UIControlStateNormal)];
                [button setImage:[UIImage imageNamed:@"xfWorkMode"] forState:(UIControlStateNormal)];
                [self doSetWorkModeManal];
                
            }];
        };
        
    })
    .LeeAddAction(^(LEEAction *action) {
        
        action.type = LEEActionTypeDefault;
        
        action.title = @"自动";
        
        action.titleColor = [UIColor brownColor];
        
        action.highlightColor = [UIColor orangeColor];
        
        action.image = [UIImage imageNamed:@"xfAutoMode"];
        
        action.highlightImage = [UIImage imageNamed:@"xfAutoMode"];
        
        action.imageEdgeInsets = UIEdgeInsetsMake(0, -10, 0, 10);
        
        action.height = 60.0f;
        
        @weakify(self)
        action.clickBlock = ^{
            @strongify(self);
            // 点击事件Block
            [self.btnWorkMode SG_imagePositionStyle:SGImagePositionStyleTop spacing:5 imagePositionBlock:^(UIButton *button) {
                [button setTitle:@"自动模式" forState:(UIControlStateNormal)];
                [button setImage:[UIImage imageNamed:@"xfAutoMode"] forState:(UIControlStateNormal)];
                [self doSetWorkModeAuto];
                
            }];
        };
        
    })
    .LeeAddAction(^(LEEAction *action) {
        
        action.type = LEEActionTypeDefault;
        
        action.title = @"定时";
        
        action.titleColor = [UIColor brownColor];
        
        action.highlightColor = [UIColor orangeColor];
        
        action.image = [UIImage imageNamed:@"xfTiming"];
        
        action.highlightImage = [UIImage imageNamed:@"xfTiming"];
        
        action.imageEdgeInsets = UIEdgeInsetsMake(0, -10, 0, 10);
        
        action.height = 60.0f;
        
        @weakify(self)
        action.clickBlock = ^{
            @strongify(self);
            // 点击事件Block
            [self.btnWorkMode SG_imagePositionStyle:SGImagePositionStyleTop spacing:5 imagePositionBlock:^(UIButton *button) {
                [button setTitle:@"自动模式" forState:(UIControlStateNormal)];
                [button setImage:[UIImage imageNamed:@"xfAutoMode"] forState:(UIControlStateNormal)];
                [self doSetWorkModeTimeing];
                
            }];
        };
        
    })
    .LeeShow();
    
}

- (IBAction)btnCirclePressed:(UIButton *)sender {
    
    [LEEAlert actionsheet].config
    .LeeTitle(@"请选择循环模式")
    .LeeAddAction(^(LEEAction *action) {
        
        action.type = LEEActionTypeDefault;
        
        action.title = @"外循环";
        
        action.titleColor = [UIColor brownColor];
        
        action.highlight = @"选择啦";
        
        action.highlightColor = [UIColor orangeColor];
        
        action.image = [UIImage imageNamed:@"xfCirclePr"];
        
        action.highlightImage = [UIImage imageNamed:@"xfCirclePr"];
        
        action.imageEdgeInsets = UIEdgeInsetsMake(0, -10, 0, 10);
        
        action.height = 60.0f;
        
        @weakify(self)
        action.clickBlock = ^{
            @strongify(self);
            // 点击事件Block
            [self.btnCircleMode SG_imagePositionStyle:SGImagePositionStyleTop spacing:5 imagePositionBlock:^(UIButton *button) {
                [button setTitle:@"外循环" forState:(UIControlStateNormal)];
                [button setImage:[UIImage imageNamed:@"xfCirclePr"] forState:(UIControlStateNormal)];
                [self doSetXFOutsideLoop];
                
            }];
        };
        
    })
    .LeeAddAction(^(LEEAction *action) {
        
        action.type = LEEActionTypeDefault;
        
        action.title = @"内循环";
        
        action.titleColor = [UIColor brownColor];
        
        action.highlightColor = [UIColor orangeColor];
        
        action.image = [UIImage imageNamed:@"xfCircleMode"];
        
        action.highlightImage = [UIImage imageNamed:@"xfCircleMode"];
        
        action.imageEdgeInsets = UIEdgeInsetsMake(0, -10, 0, 10);
        
        action.height = 60.0f;
        
        @weakify(self)
        action.clickBlock = ^{
            @strongify(self);
            // 点击事件Block
            [self.btnCircleMode SG_imagePositionStyle:SGImagePositionStyleTop spacing:5 imagePositionBlock:^(UIButton *button) {
                
                [button setTitle:@"内循环" forState:(UIControlStateNormal)];
                [button setImage:[UIImage imageNamed:@"xfCircleMode"] forState:(UIControlStateNormal)];
                [self doSetXFCircleModeInnerLoop];
                
            }];
        };
        
    })
    .LeeAddAction(^(LEEAction *action) {
        
        action.type = LEEActionTypeDefault;
        
        action.title = @"自动";
        
        action.titleColor = [UIColor brownColor];
        
        action.highlight = @"选择啦";
        
        action.highlightColor = [UIColor orangeColor];
        
        action.image = [UIImage imageNamed:@"xfAutoMode"];
        
        action.highlightImage = [UIImage imageNamed:@"xfAutoMode"];
        
        action.imageEdgeInsets = UIEdgeInsetsMake(0, -10, 0, 10);
        
        action.height = 60.0f;
        
        @weakify(self)
        action.clickBlock = ^{
            @strongify(self);
            // 点击事件Block
            [self.btnCircleMode SG_imagePositionStyle:SGImagePositionStyleTop spacing:5 imagePositionBlock:^(UIButton *button) {
                
                [button setTitle:@"自循环" forState:(UIControlStateNormal)];
                [button setImage:[UIImage imageNamed:@"xfAutoMode"] forState:(UIControlStateNormal)];
                [self doSetXFAutoLoop];
                
            }];
        };
        
    })
    .LeeShow();
}

- (IBAction)btnReturnAirPressed:(UIButton *)sender {
    
    FontSizeView *view = [[FontSizeView alloc] init];
    
    
    @weakify(self);
    view.changeBlock = ^(NSInteger level){
        @strongify(self);
        NSString *strTemp;
        NSLog(@"回风比例:%ld",(long)level);
        [self doSetInOrOutProportionWithIntIdentifer:(int)level];
        
        switch (level) {
            case 0:
            {
                NSLog(@"进回风比例：10：4");
                strTemp = @"10:4";
                [self.btnReturnAirMode showBadgeWithStyleText:WBadgeStyleNew text:strTemp animationType:WBadgeAnimTypeBreathe];
                [self doSetInOrOutProportionWithIntIdentifer:(int)level];
                
            }
                break;
            case 1:
            {
                NSLog(@"进回风比例：10：5");
                strTemp = @"10:5";
                [self.btnReturnAirMode showBadgeWithStyleText:WBadgeStyleNew text:strTemp animationType:WBadgeAnimTypeBreathe];
                [self doSetInOrOutProportionWithIntIdentifer:(int)level];
            }
                break;
            case 2:
            {
                NSLog(@"进回风比例：10：6");
                strTemp = @"10:6";
                [self.btnReturnAirMode showBadgeWithStyleText:WBadgeStyleNew text:strTemp animationType:WBadgeAnimTypeBreathe];
                [self doSetInOrOutProportionWithIntIdentifer:(int)level];
            }
                break;
            case 3:
            {
                NSLog(@"进回风比例：10：7");
                strTemp = @"10:7";
                [self.btnReturnAirMode showBadgeWithStyleText:WBadgeStyleNew text:strTemp animationType:WBadgeAnimTypeBreathe];
                [self doSetInOrOutProportionWithIntIdentifer:(int)level];
            }
                break;
            case 4:
            {
                NSLog(@"进回风比例：10：8");
                strTemp = @"10:8";
                [self.btnReturnAirMode showBadgeWithStyleText:WBadgeStyleNew text:strTemp animationType:WBadgeAnimTypeBreathe];
                [self doSetInOrOutProportionWithIntIdentifer:(int)level];
            }
                break;
            case 5:
            {
                NSLog(@"进回风比例：10：10");
                strTemp = @"10:10";
                [self.btnReturnAirMode showBadgeWithStyleText:WBadgeStyleNew text:strTemp animationType:WBadgeAnimTypeBreathe];
                [self doSetInOrOutProportionWithIntIdentifer:(int)level];
            }
                break;
            case 6:
            {
                NSLog(@"进回风比例：10：0");
                strTemp = @"10:0";
                [self.btnReturnAirMode showBadgeWithStyleText:WBadgeStyleNew text:strTemp animationType:WBadgeAnimTypeBreathe];
                [self doSetInOrOutProportionWithIntIdentifer:(int)level];
            }
                break;
            case 7:
            {
                NSLog(@"进回风比例：0：10");
                strTemp = @"0:10";
                [self.btnReturnAirMode showBadgeWithStyleText:WBadgeStyleNew text:strTemp animationType:WBadgeAnimTypeBreathe];
                [self doSetInOrOutProportionWithIntIdentifer:(int)level];
            }
                break;
                
                
            default:
                break;
        }
        
    };
    
    
    
    [LEEAlert actionsheet].config
    .LeeAddCustomView(^(LEECustomView *custom) {
        
        custom.view = view;
        
        custom.isAutoWidth = YES;
    })
    .LeeItemInsets(UIEdgeInsetsMake(0, 0, 0, 0))
    .LeeAddAction(^(LEEAction *action) {
        
        action.title = @"取消";
        
        action.titleColor = [UIColor grayColor];
        
        action.height = 45.0f;
    })
    .LeeHeaderInsets(UIEdgeInsetsMake(0, 0, 0, 0))
    .LeeActionSheetBottomMargin(0.0f)
    .LeeCornerRadius(0.0f)
    .LeeConfigMaxWidth(^CGFloat(LEEScreenOrientationType type) {
        
        // 这是最大宽度为屏幕宽度 (横屏和竖屏)
        
        return type == LEEScreenOrientationTypeHorizontal ? CGRectGetHeight([[UIScreen mainScreen] bounds]) : CGRectGetWidth([[UIScreen mainScreen] bounds]);
    })
    .LeeShow();
}

#pragma mark -
#pragma mark -调试风速
- (void)doInitWindSpeed
{
    NSLog(@"***********%f",self.sliderWindSpeed.value);
    self.labelWindSpeedValue = [[UILabel alloc] initWithFrame:
                                CGRectMake(self.sliderWindSpeed.frame.origin.x,
                                           self.sliderWindSpeed.frame.origin.y,
                                           self.sliderWindSpeed.frame.size.height,
                                           self.sliderWindSpeed.frame.size.height)];
    self.labelWindSpeedValue.backgroundColor = [UIColor clearColor];
    self.labelWindSpeedValue.textColor = RGB(85, 152, 250);
    self.labelWindSpeedValue.text = [NSString stringWithFormat:@"%d",(int)self.sliderWindSpeed.value];
    self.labelWindSpeedValue.font = [UIFont systemFontOfSize:15.0f];
    self.labelWindSpeedValue.textAlignment = NSTextAlignmentCenter;
    [self.windSliderView addSubview:self.labelWindSpeedValue];
    
    self.iWindSpeed = (int)self.sliderWindSpeed.value;
}

- (IBAction)sliderWindSpeedValueChanged:(UISlider *)sender {
    
    //    NSLog(@"slider value%f",sender.value);
    self.iWindSpeed = (int)sender.value;
    [self doDrawRectThumbSlider];
    [self doHandleTemp:(int)self.iWindSpeed];
}

- (void)doHandleTemp:(int)iWindSpeedValue
{
    if (self.iWindTemp != iWindSpeedValue) {
        [self doSetXFWindSpeedWithISpeed:iWindSpeedValue];
        self.iWindTemp = iWindSpeedValue;
        //        NSLog(@"*****************")
    }else{
        //        self.iWindTemp = 0;
        //        NSLog(@"uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu");
    }
}


- (IBAction)btnWindSpeedAdd:(UIButton *)sender {
    self.iWindSpeed ++;
    [self.sliderWindSpeed setValue:self.iWindSpeed animated:YES];
    [self doDrawRectThumbSlider];
    [self doSetXFWindSpeedWithISpeed:(int)self.iWindSpeed];
    
}
- (IBAction)btnWindSpeedLess:(UIButton *)sender {
    self.iWindSpeed --;
    [self.sliderWindSpeed setValue:self.iWindSpeed animated:YES];
    [self doDrawRectThumbSlider];
    [self doSetXFWindSpeedWithISpeed:(int)self.iWindSpeed];
    
}

- (void)doDrawRectThumbSlider{
    CGRect trackRect = [self.sliderWindSpeed trackRectForBounds:self.sliderWindSpeed.bounds];
    CGRect thumbRect = [self.sliderWindSpeed thumbRectForBounds:self.sliderWindSpeed.bounds
                                                      trackRect:trackRect
                                                          value:self.sliderWindSpeed.value];
    
    CGRect r = [self.labelWindSpeedValue frame];
    r.origin.x = thumbRect.origin.x + self.sliderWindSpeed.frame.origin.x;
    [self.labelWindSpeedValue setFrame:r];
    
    self.labelWindSpeedValue.text = [NSString stringWithFormat:@"%d",(int)self.sliderWindSpeed.value];
    
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
#pragma mark -工具类
-(void)initAnimationWithImageView:(UIImageView *)imageView onSpeed:(float)speed
{
    CABasicAnimation* rotationAnimation;
    rotationAnimation = [CABasicAnimation animationWithKeyPath:@"transform.rotation.z"];
    rotationAnimation.toValue = [NSNumber numberWithFloat: M_PI * speed ];
    rotationAnimation.duration = 0.2;
    rotationAnimation.cumulative = YES;
    rotationAnimation.repeatCount = FLT_MAX;
    [imageView.layer addAnimation:rotationAnimation forKey:@"rotationAnimation"];
}


-(void)startAnimationWithImageView:(UIImageView *)imageView
{
    CFTimeInterval pausedTime = [imageView.layer timeOffset];
    imageView.layer.speed = 1.0;
    imageView.layer.timeOffset = 0.0;
    imageView.layer.beginTime = 0.0;
    CFTimeInterval timeSincePause = [imageView.layer convertTime:CACurrentMediaTime()
                                                       fromLayer:nil] - pausedTime;
    imageView.layer.beginTime = timeSincePause;
}

-(void)pauseAnimationWithImageView:(UIImageView *)imageView
{
    CFTimeInterval pausedTime = [imageView.layer convertTime:CACurrentMediaTime() fromLayer:nil];
    imageView.layer.speed = 0.0;
    imageView.layer.timeOffset = pausedTime;
}


#pragma mark - 设置button单边框
- (void)doXFSetTopBorderWithButton:(UIButton *)btn borderType:(XFButtonBorderStyle)btnBoderType
{
    
    switch (btnBoderType) {
        case XFButtonBorderStyleTop:
        {
            UIView *topBorder = [[UIView alloc] initWithFrame:CGRectMake(0, 0, self.btnOn.frame.size.width, 0.5f)];
            topBorder.backgroundColor = UIColorFromRGB(0xC8C8CE);
            [btn addSubview:topBorder];
        }
            break;
        case XFButtonBorderStyleBottom:
        {
            UIView *bottomBorder = [[UIView alloc] initWithFrame:CGRectMake(0, self.btnOn.frame.size.height - 0.5f, self.btnOn.frame.size.width, 0.5f)];
            bottomBorder.backgroundColor = UIColorFromRGB(0xC8C8CE);
            [btn addSubview:bottomBorder];
        }
            break;
        case XFButtonBorderStyleLeft:
        {
            UIView *leftBorder = [[UIView alloc] initWithFrame:CGRectMake(0.5f, 0, 0.5f, self.btnOn.frame.size.height)];
            leftBorder.backgroundColor = UIColorFromRGB(0xC8C8CE);
            [btn addSubview:leftBorder];
        }
            break;
        case XFButtonBorderStyleRigth:
        {
            UIView *rightBorder = [[UIView alloc] initWithFrame:CGRectMake(self.btnOn.frame.size.width - 0.5f, 0, 0.5f, self.btnOn.frame.size.height)];
            rightBorder.backgroundColor = UIColorFromRGB(0xC8C8CE);
            [btn addSubview:rightBorder];
        }
            break;
            
        default:
            break;
    }
}

#pragma mark -
#pragma mark - 命令组合-获取新风状态

- (void)doGetXFCurrentStatus
{
    [self doSendXFOrderWithStrFunctionIdentifer:@"F0" strDataRangeIdentifer:@"00"];
}

#pragma mark -设置开机、关机
- (void)doSetXFOn
{
    [self doSendXFOrderWithStrFunctionIdentifer:@"02" strDataRangeIdentifer:@"01"];
    NSLog(@"设置开机！");
}

- (void)doSetXFOff
{
    [self doSendXFOrderWithStrFunctionIdentifer:@"02" strDataRangeIdentifer:@"00"];
    NSLog(@"设置关机！");
}

#pragma mark -设置加热开/设置加热关
-  (void)doSetHeatingOn
{
    [self doSendXFOrderWithStrFunctionIdentifer:@"03" strDataRangeIdentifer:@"01"];
    NSLog(@"设置加热开！");
}

- (void)doSetHeatingOff
{
    [self doSendXFOrderWithStrFunctionIdentifer:@"03" strDataRangeIdentifer:@"00"];
    NSLog(@"设置加热关！");
}

#pragma mark -设置工作模式 手动 自动
//0x00：手动；0x01:自动;0x02:定时;
- (void)doSetWorkModeManal
{
    [self doSendXFOrderWithStrFunctionIdentifer:@"04" strDataRangeIdentifer:@"00"];
    NSLog(@"设置工作模式手动！");
}

- (void)doSetWorkModeAuto
{
    [self doSendXFOrderWithStrFunctionIdentifer:@"04" strDataRangeIdentifer:@"01"];
    NSLog(@"设置工作模式自动！");
}

- (void)doSetWorkModeTimeing
{
    [self doSendXFOrderWithStrFunctionIdentifer:@"04" strDataRangeIdentifer:@"02"];
    NSLog(@"设置工作模式自动！");
}

#pragma mark - 设置循环模式 内循环、外循环
- (void)doSetXFCircleModeInnerLoop
{
    [self doSendXFOrderWithStrFunctionIdentifer:@"05" strDataRangeIdentifer:@"01"];
    NSLog(@"设置循环模式内循环！");
}

- (void)doSetXFOutsideLoop
{
    [self doSendXFOrderWithStrFunctionIdentifer:@"05" strDataRangeIdentifer:@"00"];
    NSLog(@"设置循环模式外循环！");
}

- (void)doSetXFAutoLoop
{
    [self doSendXFOrderWithStrFunctionIdentifer:@"05" strDataRangeIdentifer:@"02"];
    NSLog(@"设置循环模式自动循环！");
}

#pragma mark - 设置风速
/*
 20-30共11档，手动模式才有效
 0x14 0x15 0x16 0x17
 0x18 0x19 0x1a 0x1b
 0x1c 0x1e 0x1f
 */
- (void)doSetXFWindSpeedWithISpeed:(int)iSpeed
{
    switch (iSpeed) {
        case 20:
        {
            
            [self doSendXFOrderWithStrFunctionIdentifer:@"06" strDataRangeIdentifer:@"14"];
            NSLog(@"设置风速20！");
        }
            break;
        case 21:
        {
            [self doSendXFOrderWithStrFunctionIdentifer:@"06" strDataRangeIdentifer:@"15"];
            NSLog(@"设置风速21！");
        }
            break;
        case 22:
        {
            [self doSendXFOrderWithStrFunctionIdentifer:@"06" strDataRangeIdentifer:@"16"];
            NSLog(@"设置风速22！");
        }
            break;
        case 23:
        {
            [self doSendXFOrderWithStrFunctionIdentifer:@"06" strDataRangeIdentifer:@"17"];
            NSLog(@"设置风速23！");
        }
            break;
        case 24:
        {
            [self doSendXFOrderWithStrFunctionIdentifer:@"06" strDataRangeIdentifer:@"18"];
            NSLog(@"设置风速24！");
        }
            break;
        case 25:
        {
            [self doSendXFOrderWithStrFunctionIdentifer:@"06" strDataRangeIdentifer:@"19"];
            NSLog(@"设置风速25！");
        }
            break;
        case 26:
        {
            [self doSendXFOrderWithStrFunctionIdentifer:@"06" strDataRangeIdentifer:@"1A"];
            NSLog(@"设置风速26！");
        }
            break;
        case 27:
        {
            [self doSendXFOrderWithStrFunctionIdentifer:@"06" strDataRangeIdentifer:@"1B"];
            NSLog(@"设置风速27！");
        }
            break;
        case 28:
        {
            [self doSendXFOrderWithStrFunctionIdentifer:@"06" strDataRangeIdentifer:@"1C"];
            NSLog(@"设置风速28！");
        }
            break;
        case 29:
        {
            [self doSendXFOrderWithStrFunctionIdentifer:@"06" strDataRangeIdentifer:@"1D"];
            NSLog(@"设置风速29！");
        }
            break;
        case 30:
        {
            [self doSendXFOrderWithStrFunctionIdentifer:@"06" strDataRangeIdentifer:@"1E"];
            NSLog(@"设置风速30！");
        }
            break;
            
            
        default:
            break;
    }
}

#pragma mark - 设置进回风比例
/*
 0x00-10/4, 0x01-10/5, 0x02-10/6
 0x03-10/7, 0x04-10/8, 0x05-10/10
 0x06-10/0单进风,  0x07-0/10单排风
 */
- (void)doSetInOrOutProportionWithIntIdentifer:(int)iTemp
{
    switch (iTemp) {
        case 0:
        {
            [self doSendXFOrderWithStrFunctionIdentifer:@"07" strDataRangeIdentifer:@"00"];
            NSLog(@"设置进回风比例10/4！");
        }
            break;
        case 1:
        {
            [self doSendXFOrderWithStrFunctionIdentifer:@"07" strDataRangeIdentifer:@"01"];
            NSLog(@"设置进回风比例10/5！");
        }
            break;
        case 2:
        {
            [self doSendXFOrderWithStrFunctionIdentifer:@"07" strDataRangeIdentifer:@"02"];
            NSLog(@"设置进回风比例10/6！");
        }
            break;
        case 3:
        {
            [self doSendXFOrderWithStrFunctionIdentifer:@"07" strDataRangeIdentifer:@"03"];
            NSLog(@"设置进回风比例10/7！");
        }
            break;
        case 4:
        {
            [self doSendXFOrderWithStrFunctionIdentifer:@"07" strDataRangeIdentifer:@"04"];
            NSLog(@"设置进回风比例10/8！");
        }
            break;
        case 5:
        {
            [self doSendXFOrderWithStrFunctionIdentifer:@"07" strDataRangeIdentifer:@"05"];
            NSLog(@"设置进回风比例10/10！");
        }
            break;
        case 6:
        {
            [self doSendXFOrderWithStrFunctionIdentifer:@"07" strDataRangeIdentifer:@"06"];
            NSLog(@"设置进回风比例10/0！");
        }
            break;
        case 7:
        {
            [self doSendXFOrderWithStrFunctionIdentifer:@"07" strDataRangeIdentifer:@"07"];
            NSLog(@"设置进回风比例0/10！");
        }
            break;
            
        default:
            break;
    }
}


#pragma mark - 初滤使用时间清零

- (void)doSetXFUseTimeToZeroPressed:(UIButton *)sender {
    [self doSendXFOrderWithStrFunctionIdentifer:@"08" strDataRangeIdentifer:@"01"];
    NSLog(@"初滤使用时间清零！");
}

#pragma mark - 集尘箱使用时间清零

- (void)doSetXFDustBoxUseTimeToZeroPressed:(UIButton *)sender {
    
    [self doSendXFOrderWithStrFunctionIdentifer:@"09" strDataRangeIdentifer:@"01"];
    NSLog(@"集尘箱使用时间清零！");
}

#pragma mark- 高滤使用时间清零

- (void)doSetXFHighUseTimeToZeroPressed:(UIButton *)sender {
    [self doSendXFOrderWithStrFunctionIdentifer:@"09" strDataRangeIdentifer:@"01"];
    NSLog(@"高滤使用时间清零！");
}

- (void)doSendXFOrderWithStrFunctionIdentifer:(NSString *)strFunctionIdentifer
                        strDataRangeIdentifer:(NSString *)strDataRangeIdentifer
{
    NSData *dataSend =  [[NetworkEngine shareInstance] doSendXinFengDataWithModelDevice:self.device
                                                                        functionIdentifer:strFunctionIdentifer
                                                                       dataRangeIdentifer:strDataRangeIdentifer];
    [[NetworkEngine shareInstance] sendRequestData:dataSend];
}

@end
