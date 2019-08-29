//
//  EZAddByQRCodeViewController.m
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/10/28.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import <AVFoundation/AVFoundation.h>
#import "EZAddByQRCodeViewController.h"
#import "EZQRView.h"
#import "DDKit.h"

@interface EZAddByQRCodeViewController ()<AVCaptureMetadataOutputObjectsDelegate>{
    AVAuthorizationStatus authStatus;
}

@property (strong, nonatomic) AVCaptureDevice *device;
@property (strong, nonatomic) AVCaptureDeviceInput *input;
@property (strong, nonatomic) AVCaptureMetadataOutput *output;
@property (strong, nonatomic) AVCaptureSession *session;
@property (strong, nonatomic) AVCaptureVideoPreviewLayer *preview;
@property (nonatomic, weak) IBOutlet UIImageView *lineImageView;
@property (nonatomic, weak) IBOutlet EZQRView *qrView;
@property (nonatomic, weak) IBOutlet UIButton *torchButton;
@property (nonatomic, weak) IBOutlet UILabel *tipsLabel;
@property (nonatomic, weak) IBOutlet UIActivityIndicatorView *activityView;

@end

@implementation EZAddByQRCodeViewController

- (void)dealloc
{
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = NSLocalizedString(@"ad_scan_qr_title", @"扫描二维码");
    
    authStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo];
    if (authStatus == AVAuthorizationStatusAuthorized)
    {
        [self qrSetup];
    }
    else if (authStatus == AVAuthorizationStatusNotDetermined)
    {
        [AVCaptureDevice requestAccessForMediaType:AVMediaTypeVideo
                                 completionHandler:^(BOOL granted) {
                                     if (granted)
                                     {
                                         [self qrSetup];
                                         [_session startRunning];
                                     }
                                 }];
    }
    else
    {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"alert_title", @"提示")
                                                        message:NSLocalizedString(@"ad_allow_camera",@"请在设备的`设置-隐私-相机`中允许访问相机。")
                                                       delegate:self
                                              cancelButtonTitle:NSLocalizedString(@"done",@"确定")
                                              otherButtonTitles:nil];
        [alert show];
    }
    self.qrView.hidden = YES;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [[GlobalKit shareKit] clearDeviceInfo];
    if (authStatus == AVAuthorizationStatusDenied ||
        authStatus == AVAuthorizationStatusRestricted)
    {
        self.view.backgroundColor = [UIColor whiteColor];
        self.activityView.hidden = YES;
        return;
    }
    [self.activityView startAnimating];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
    if (authStatus == AVAuthorizationStatusDenied ||
        authStatus == AVAuthorizationStatusRestricted) {
        return;
    }
    [_session startRunning];
    
    _preview.frame = CGRectMake(0, 64, self.qrView.bounds.size.width, self.qrView.bounds.size.height);
    
    //修正扫描区域
    CGFloat screenHeight = self.qrView.frame.size.height;
    CGFloat screenWidth = self.qrView.frame.size.width;
    CGRect cropRect = CGRectMake((screenWidth - 240.0)/2, (screenHeight - 240.0)/3, 240.0, 240.0);
    
    [_output setRectOfInterest:CGRectMake(cropRect.origin.y/screenHeight, cropRect.origin.x/screenWidth, cropRect.size.height/screenHeight, cropRect.size.width/screenWidth)];
    
    [self addLineAnimation];
    [self.activityView stopAnimating];
    self.activityView.hidden = YES;
    self.qrView.hidden = NO;
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

#pragma mark - AVCaptureMetadataOutputObjectsDelegate Methods
- (void)captureOutput:(AVCaptureOutput *)captureOutput didOutputMetadataObjects:(NSArray *)metadataObjects fromConnection:(AVCaptureConnection *)connection
{
    NSString *stringValue;
    if ([metadataObjects count] > 0)
    {
        //停止扫描
        [_session stopRunning];
        AVMetadataMachineReadableCodeObject * metadataObject = [metadataObjects objectAtIndex:0];
        stringValue = metadataObject.stringValue;
    }
    
    [self checkQRCode:stringValue];
}

#pragma mark - Custom Methods

- (void)qrSetup
{
    if(!_device)
        _device = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
    
    // Input
    if(!_input)
        _input = [AVCaptureDeviceInput deviceInputWithDevice:self.device error:nil];
    
    // Output
    if(!_output)
        _output = [[AVCaptureMetadataOutput alloc] init];
    [_output setMetadataObjectsDelegate:self queue:dispatch_get_main_queue()];
    
    // Session
    if(!_session)
        _session = [[AVCaptureSession alloc] init];
    [_session setSessionPreset:AVCaptureSessionPresetHigh];
    if ([_session canAddInput:self.input])
    {
        [_session addInput:self.input];
    }
    
    if ([_session canAddOutput:self.output])
    {
        [_session addOutput:self.output];
    }
    
    AVCaptureConnection *outputConnection = [_output connectionWithMediaType:AVMediaTypeVideo];
    outputConnection.videoOrientation = AVCaptureVideoOrientationPortrait;
    
    // 条码类型 AVMetadataObjectTypeQRCode
    _output.metadataObjectTypes = @[AVMetadataObjectTypeQRCode];
    
    // Preview
    if(!_preview)
        _preview = [AVCaptureVideoPreviewLayer layerWithSession:_session];
    _preview.videoGravity = AVLayerVideoGravityResize;
    _preview.frame = self.view.bounds;
    [self.view.layer insertSublayer:_preview atIndex:0];
    
    _preview.connection.videoOrientation = AVCaptureVideoOrientationPortrait;
}

- (void)addLineAnimation
{
    CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"transform.translation.y"];
    CGFloat height = [UIScreen mainScreen].bounds.size.height - 64.0;
    animation.fromValue = @(height/3.0 - 240);
    animation.toValue = @(height/3.0 - 80);
    animation.duration = 3.0f;
    animation.repeatCount = HUGE_VALF;
    animation.removedOnCompletion = NO;
    
    animation.fillMode = kCAFillModeForwards;
    
    [_lineImageView.layer addAnimation:animation forKey:nil];
}

- (IBAction)torchButtonClicked:(id)sender
{
    AVCaptureDevice *captureDevice = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
    if ([captureDevice hasTorch]) {
        int nTorchMode = captureDevice.torchMode;
        nTorchMode ++;
        nTorchMode = nTorchMode > 1 ? 0 : nTorchMode;
        
        [captureDevice lockForConfiguration:nil];
        captureDevice.torchMode = nTorchMode;
        [captureDevice unlockForConfiguration];
        
        switch (nTorchMode)
        {
            case 0:
            {
                _torchButton.selected = NO;
            }
                break;
            case 1:
            {
                _torchButton.selected = YES;
            }
                break;
            default:
                break;
        }
    }
}

- (IBAction)nextAction:(id)sender
{
    [self performSegueWithIdentifier:@"go2InputSerial" sender:nil];
}

- (void)checkQRCode:(NSString *)strQRcode
{
    NSArray *arrString = [strQRcode componentsSeparatedByCharactersInSet:[NSCharacterSet newlineCharacterSet]];
    
    if(arrString.count >=3)
    {
        [GlobalKit shareKit].deviceSerialNo = arrString[1];
        [GlobalKit shareKit].deviceVerifyCode = arrString[2];
        [GlobalKit shareKit].deviceModel = arrString[3];
        [self performSegueWithIdentifier:@"go2Result" sender:self];
    } else {
        [UIView dd_showMessage:NSLocalizedString(@"ad_not_support_scan", @"不支持的二维码类型，转用手动输入")];
        [self nextAction:nil];
    }
}

@end
