//
//  EZEditViewController.m
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/12/16.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import "EZEditViewController.h"
#import "DDKit.h"


@interface EZEditViewController ()<UITextFieldDelegate>

@property (nonatomic, weak) IBOutlet UITextField *deviceNameTextField;

@end

@implementation EZEditViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.title = NSLocalizedString(@"device_modify_name_title", @"修改设备名称");
    
//    self.deviceNameTextField.text = self.cameraInfo.deviceName;
//    
//    self.deviceNameTextField.leftView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, 30)];
//    self.deviceNameTextField.leftViewMode = UITextFieldViewModeAlways;
//    [self.deviceNameTextField dd_addSeparatorWithType:ViewSeparatorTypeVerticalSide];
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

#pragma mark - UITextFieldDelegate Methods

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    if (self.deviceNameTextField == textField)
    {
        NSString *strTemp = [NSString stringWithFormat:@"%@%@", textField.text, string];
        if ([self convertToInt:strTemp] > 50)
        {
            return NO;
        }
        
        return YES;
    }
    return YES;
}

#pragma mark - Action Methods

- (int)convertToInt:(NSString *)strtemp
{
    int strlength = 0;
    char *p = (char *)[strtemp cStringUsingEncoding:NSUnicodeStringEncoding];
    for (int i = 0; i < [strtemp lengthOfBytesUsingEncoding:NSUnicodeStringEncoding]; i++) {
        if (*p)
        {
            p++;
            strlength++;
        }
        else
        {
            p++;
        }
    }
    return (strlength + 1)/2;
}

- (IBAction)saveName:(id)sender
{
    [EZOPENSDK setDeviceName:self.deviceNameTextField.text
                deviceSerial:self.deviceInfo.deviceSerial
                  completion:^(NSError *error) {
                      NSLog(@"error = %@",error);
                      if(!error)
                      {
//                          self.cameraInfo.deviceName = self.deviceNameTextField.text;
                          [self.navigationController popViewControllerAnimated:YES];
                      }
                  }];
}

@end
