//
//  WindowTransmitViewController.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/14.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "WindowTransmitViewController.h"

@interface WindowTransmitViewController ()

@property (weak, nonatomic) IBOutlet UIButton *btnBack;
@property (weak, nonatomic) IBOutlet UILabel *labelTitle;
@property (weak, nonatomic) IBOutlet UIImageView *iamgeLogo;
@property (weak, nonatomic) IBOutlet UISlider *slider;
@property (weak, nonatomic) IBOutlet UIButton *btnStop;
@property (weak, nonatomic) IBOutlet UIButton *btnOn;
@property (weak, nonatomic) IBOutlet UIButton *btnOFF;
@property (assign, nonatomic) NetworkEngine *networkEngine;
@property (nonatomic, assign) int lastValue;

@end

@implementation WindowTransmitViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
        self.tabBarController.tabBar.hidden = YES;
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    if (self.itype == 0) {
        self.tabBarController.tabBar.hidden = NO;
    }else{
        self.tabBarController.tabBar.hidden = YES;
    }
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.btnBack setEnlargeEdgeWithTop:20 right:20 bottom:20 left:20];
    self.networkEngine = [NetworkEngine shareInstance];
    [self doRegisterKVO];
    [self doHandleTheSendOrderToClient:ReadStatusType location:@"00"];
    
    if ([self.device.strDevice_device_type isEqualToString:@"02"]
        && [self.device.strDevice_category isEqualToString:@"11"]) {
        self.slider.hidden = NO;
        self.labelTitle.text = @"平移开窗器";
        self.iamgeLogo.image = [UIImage imageNamed:@"window10"];
        self.slider.minimumValue = 0;
        self.slider.maximumValue = 10;
        self.slider.hidden = NO;
    }else{
        self.slider.hidden = NO;
        self.slider.minimumValue = 0;
        self.slider.maximumValue = 15;
        self.labelTitle.text = @"智能窗帘";
        self.iamgeLogo.image = [UIImage imageNamed:@"chuanglian0"];
        self.slider.hidden = NO;
    }
    
    self.lastValue = self.slider.value;
}

#pragma mark -
#pragma mark - 注册kvo
- (void)doRegisterKVO
{
    //监测设备上报添加
    @weakify(self);
    [self observeKeyPath:@keypath(self.networkEngine.modelDevice) block:^(id value) {
        @strongify(self);
        SHModelDevice *deviceReport = (SHModelDevice *)value;
        if ([deviceReport.strDevice_mac_address isEqualToString:self.device.strDevice_mac_address]) {
            if (self.blockGetStatueComplete) {
                self.blockGetStatueComplete(deviceReport.strDevice_other_status);
            }
            if ([deviceReport.strDevice_device_OD isEqualToString:@"0F E6"]) {
                if ([deviceReport.strDevice_device_type isEqualToString:@"02"]) {
                    if ([deviceReport.strDevice_category isEqualToString:@"10"]) {
                        int iSwitchValue = (int)[[ToolHexManager sharedManager] numberWithHexString:deviceReport.strDevice_other_status];
                        
                        self.slider.value = iSwitchValue;
                        self.lastValue = iSwitchValue;
                        if (iSwitchValue > 15) {
                            iSwitchValue = 15;
                            self.lastValue = iSwitchValue;
                            
                        }
                        self.iamgeLogo.image = [UIImage imageNamed:[NSString stringWithFormat:@"chuanglian%d",iSwitchValue]];
                        
                        if (iSwitchValue > 0) {
                            self.btnOn.selected = YES;
                            self.btnOFF.selected = NO;;
                        }else{
                            self.btnOn.selected = NO;
                            self.btnOFF.selected = YES;
                        }
                        
                    }else if ([deviceReport.strDevice_category isEqualToString:@"11"]){
                        
                        int iSwitchValue = (int)[[ToolHexManager sharedManager] numberWithHexString:deviceReport.strDevice_other_status];
                        self.slider.value = iSwitchValue;
                        self.lastValue = iSwitchValue;
                        if (iSwitchValue > 10) {
                            iSwitchValue = 10;
                            self.lastValue = iSwitchValue;
                        }
                        self.iamgeLogo.image = [UIImage imageNamed:[NSString stringWithFormat:@"window%d",iSwitchValue]];
                        if (iSwitchValue > 0) {
                            self.btnOn.selected = YES;
                            self.btnOFF.selected = NO;
                        }else{
                            self.btnOn.selected = NO;
                            self.btnOFF.selected = YES;
                        }
                    }else{}
                }
            }
        }
    }];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark -
#pragma mark - action

- (IBAction)btnBackPressed:(UIButton *)sender {
    self.tabBarController.tabBar.hidden = NO;
    [self.navigationController popViewControllerAnimated:YES];
}


- (IBAction)btnStopPressed:(UIButton *)sender {
    
    [self doHandleTheSendOrderToClient:StopRotationType location:@"00"];
}

- (IBAction)btnOnPressed:(UIButton *)sender {
    
    if ([self.device.strDevice_device_type isEqualToString:@"02"]
        && [self.device.strDevice_category isEqualToString:@"10"]) {
        
        [self doHandleTheSendOrderToClient:ReverseRotationType location:@"00"];
        
        [self doHandleOnWithDevice:self.device iLimitValue:15];
        
        
    }else if ([self.device.strDevice_device_type isEqualToString:@"02"]
              && [self.device.strDevice_category isEqualToString:@"11"]) {
        
        //        [self doHandleTheSendOrderToClient:ReverseRotationType location:@"00"];
//        self.iamgeLogo.image = [UIImage imageNamed:@"window0"];
        [self doHandleTheSendOrderToClient:ReverseRotationType location:@"00"];
        
    }else{}
    
    
}
- (IBAction)btnOFFPressed:(UIButton *)sender {
    
    if ([self.device.strDevice_device_type isEqualToString:@"02"]
        && [self.device.strDevice_category isEqualToString:@"10"]) {
        //反向
        
        [self doHandleTheSendOrderToClient:PositiveRotationType location:@"00"];
        
        [self doHandleOFFWithDevice:self.device iLimitValue:15];
        
        
    }else if ([self.device.strDevice_device_type isEqualToString:@"02"]
              && [self.device.strDevice_category isEqualToString:@"11"]){
        
        //        [self doHandleTheSendOrderToClient:PositiveRotationType location:@"00"];
//        self.iamgeLogo.image = [UIImage imageNamed:@"window10"];
        [self doHandleTheSendOrderToClient:PositiveRotationType location:@"00"];
    }else{}
}


- (void)doHandleOnWithDevice:(SHModelDevice *)device iLimitValue:(int)iLimitValue
{
    NSString *strName;
    if ([device.strDevice_category isEqualToString:@"10"]) {
        strName = @"chuanglian";
    }else{
        strName = @"window";
    }
    self.iamgeLogo.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@0",strName]];
    NSMutableArray *images = [NSMutableArray arrayWithCapacity:0];
    if (self.lastValue == 0) {
        
    }else if(self.lastValue == iLimitValue) {
        for (int i = iLimitValue; i >= 0 ; i --) {
            UIImage *image = [UIImage imageNamed:[NSString stringWithFormat:@"%@%d",strName,i]];
            [images addObject:image];
        }
    }else{
        for (int i = self.lastValue; i >= 0 ; i --) {
            UIImage *image = [UIImage imageNamed:[NSString stringWithFormat:@"%@%d",strName,i]];
            [images addObject:image];
        }
    }
    
    self.iamgeLogo.animationImages = images;
    self.iamgeLogo.animationDuration = 5.0 / 16 * abs(self.lastValue - 0);
    self.iamgeLogo.animationRepeatCount = 1;
    [self.iamgeLogo startAnimating];
    self.lastValue = 0;
    [self performSelector:@selector(setTopImageViewImage) withObject:nil afterDelay:self.iamgeLogo.animationDuration];
}

- (void)doHandleOFFWithDevice:(SHModelDevice *)device iLimitValue:(int)iLimitValue
{
    NSString *strName;
    if ([device.strDevice_category isEqualToString:@"10"]) {
        strName = @"chuanglian";
    }else{
        strName = @"window";
    }
    self.iamgeLogo.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@%d",strName,iLimitValue]];
    NSMutableArray *images = [NSMutableArray arrayWithCapacity:0];
    if (self.lastValue == 0) {
        for (int i = 0; i <= iLimitValue ; i ++) {
            UIImage *image = [UIImage imageNamed:[NSString stringWithFormat:@"%@%d",strName,i]];
            [images addObject:image];
        }
    }else if(self.lastValue == iLimitValue) {
        
    }else{
        
        for (int i = iLimitValue-self.lastValue; i <= iLimitValue ; i ++) {
            UIImage *image = [UIImage imageNamed:[NSString stringWithFormat:@"%@%d",strName,i]];
            [images addObject:image];
        }
    }
    
    self.iamgeLogo.animationImages = images;
    self.iamgeLogo.animationDuration = 5.0 / 16 * abs(self.lastValue - iLimitValue);
    self.iamgeLogo.animationRepeatCount = 1;
    [self.iamgeLogo startAnimating];
    self.lastValue = iLimitValue;
    [self performSelector:@selector(setTopImageViewImage) withObject:nil afterDelay:self.iamgeLogo.animationDuration];
}



- (IBAction)sliderHandlePressed:(UISlider *)sender {
    
    int itemp = roundf(sender.value);
    NSLog(@"itemp == %d,slider.value == %f",itemp,sender.value);
    if (itemp > 0) {
        self.btnOn.selected = YES;
        self.btnOFF.selected = NO;
    }else{
        self.btnOn.selected = NO;
        self.btnOFF.selected = YES;
    }
    
    NSString *strValue = [[ToolHexManager sharedManager] converIntToHex:itemp];
    
    if ([self.device.strDevice_category isEqualToString:@"10"]) {
        //窗帘
        self.iamgeLogo.image = [UIImage imageNamed:[NSString stringWithFormat:@"chuanglian%d",itemp]];
        NSMutableArray *images = [NSMutableArray arrayWithCapacity:0];
        if (self.lastValue < itemp) {
            for (int i = self.lastValue; i <= itemp ; i ++) {
                UIImage *image = [UIImage imageNamed:[NSString stringWithFormat:@"chuanglian%d",i]];
                [images addObject:image];
            }
        }else {
            for (int i = self.lastValue; i >= itemp ; i --) {
                if (i > 15) {
                    UIImage *image = [UIImage imageNamed:[NSString stringWithFormat:@"chuanglian15"]];
                    [images addObject:image];
                    
                }else{
                    UIImage *image = [UIImage imageNamed:[NSString stringWithFormat:@"chuanglian%d",i]];
                    [images addObject:image];
                }
            }
        }
        
        self.iamgeLogo.animationImages = images;
        self.iamgeLogo.animationDuration = 5.0 / 16 * abs(self.lastValue - itemp);
        self.iamgeLogo.animationRepeatCount = 1;
        [self.iamgeLogo startAnimating];
        self.lastValue = itemp;
        [self performSelector:@selector(setTopImageViewImage) withObject:nil afterDelay:self.iamgeLogo.animationDuration];
        
        [self doHandleTheSendOrderToClient:LocationSWType location:strValue];
        
    } else if ([self.device.strDevice_category isEqualToString:@"11"]){
        //平移开窗器
        [self doHandleTheSendOrderToClient:LocationSWType location:strValue];
        
    } else {}
    
}

- (void)doHandleTheSendOrderToClient:(ElectricTransimitObjecActionType)type location:(NSString *)strLoaction
{
    NSData *data = [self.networkEngine doGetElectricCurtainsOrWindowDataWithModelDevice:self.device
                                                                             actionType:type
                                                                               location:strLoaction];
    [self.networkEngine sendRequestData:data];
}


- (void)setTopImageViewImage {
    
    [self.iamgeLogo stopAnimating];
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
