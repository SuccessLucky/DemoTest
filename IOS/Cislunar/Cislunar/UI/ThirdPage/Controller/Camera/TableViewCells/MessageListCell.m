//
//  MessageListCell.m
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/11/3.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import "MessageListCell.h"
#import "UIImageView+EzvizOpenSDK.h"
#import "UIImageView+AFNetworking.h"

#import "DDKit.h"

static dispatch_semaphore_t ezviz_sema; //全局信号量

#define device_verify_code_check_queue_name "com.ys7.open.verify.check.queue"

static __unused dispatch_queue_t device_verify_code_check_queue()
{
    static dispatch_queue_t device_verify_check_queue;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        device_verify_check_queue = dispatch_queue_create(device_verify_code_check_queue_name, DISPATCH_QUEUE_SERIAL);
    });
    return device_verify_check_queue;
}

@implementation MessageListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.readStatusIcon.layer.cornerRadius = 5.0f;
    self.readStatusIcon.clipsToBounds = YES;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state    
    self.readStatusIcon.backgroundColor = [UIColor dd_hexStringToColor:@"0x1b9ee2"];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (alertView.alertViewStyle == UIAlertViewStyleSecureTextInput)
    {
        if (buttonIndex == 1)
        {
            NSString *checkCode = [alertView textFieldAtIndex:0].text;
            [[GlobalKit shareKit].deviceVerifyCodeBySerial setValue:checkCode forKey:_deviceSerial];
        }
    }
    dispatch_semaphore_signal(ezviz_sema);
}

- (void)setAlarmInfo:(EZAlarmInfo *)info
{
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    formatter.dateFormat = @"HH:mm:ss";
    self.timeLabel.text = [formatter stringFromDate:info.alarmStartTime];
    self.readStatusIcon.hidden = NO;
    if(info.isRead)
    {
        self.readStatusIcon.hidden = YES;
    }
    
    /*有2种图片链接
     https://whpic.ys7.com:8009/HIK_1447836214_458BEBDAE4AB6a6f_525400286893421023815?isEncrypted=1&isCloudStored=0
     https://www.ys7.com:9090/api/cloud?method=download&fid=5ed92258-8e0f-11e5-8000-c19249a52dcf&deviceSerialNo=504242549&isEncrypted=0&isCloudStored=1
     */
    NSURL *url = [NSURL URLWithString:info.alarmPicUrl];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
    request.HTTPMethod = @"GET";
    __weak typeof(self) weakSelf = self;
    __block BOOL isEncrypt = NO;
    NSRange range = [info.alarmPicUrl rangeOfString:@"isEncrypted="];
    if(range.location != NSNotFound)
    {
        isEncrypt = [[info.alarmPicUrl substringWithRange:NSMakeRange(range.location + range.length, 1)] boolValue];
    }
    
    [self.actionImageView ez_setImageWithURLRequest:request
                                   placeholderImage:[UIImage imageNamed:@"message_callhelp"]
                                            success:^(NSURLRequest * _Nonnull request, NSHTTPURLResponse * _Nullable response, NSData * _Nonnull data) {
                                                if (!isEncrypt) {
                                                    weakSelf.actionImageView.image = [[UIImage alloc] initWithData:data];
                                                } else {
                                                    dispatch_async(device_verify_code_check_queue(), ^{
                                                        //该demo写的密码方案是简单方案，用户体验一般，用户可以自行实现密码方案，本方案仅供参考
                                                        if (isEncrypt && [GlobalKit shareKit].deviceVerifyCodeBySerial[_deviceSerial] == nil)
                                                        {
                                                            if (!ezviz_sema) {
                                                                ezviz_sema = dispatch_semaphore_create(0);
                                                            }
                                                            [self showSetPassword];
                                                            dispatch_semaphore_wait(ezviz_sema, DISPATCH_TIME_FOREVER);
                                                        }
                                                        
                                                        NSData *decodeData = [EZOPENSDK decryptData:data verifyCode:[GlobalKit shareKit].deviceVerifyCodeBySerial[_deviceSerial]];
                                                        dispatch_async(dispatch_get_main_queue(), ^{
                                                            weakSelf.actionImageView.image = [[UIImage alloc] initWithData:decodeData];
                                                            if (weakSelf.actionImageView.image == nil)
                                                            {
                                                                weakSelf.actionImageView.image = [UIImage imageNamed:@"message_callhelp"];
                                                            }
                                                        });
                                                    });
                                                }
                                            }
                                            failure:^(NSURLRequest * _Nonnull request, NSHTTPURLResponse * _Nullable response, NSError * _Nonnull error) {
                                                NSLog(@"error = %@",error);
                                            }];
    
    self.descriptionLabel.text = [NSString stringWithFormat:@"%@:%@",NSLocalizedString(@"message_from", @"来自"), info.alarmName];
}


- (void)showSetPassword
{
    dispatch_async(dispatch_get_main_queue(), ^{
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"device_input_verify_code", @"请输入视频图片加密密码")
                                                            message:NSLocalizedString(@"device_verify_code_tip", @"您的视频已加密，请输入密码进行查看，初始密码为机身标签上的验证码，如果没有验证码，请输入ABCDEF（密码区分大小写)")
                                                           delegate:self
                                                  cancelButtonTitle:NSLocalizedString(@"cancel", @"取消")
                                                  otherButtonTitles:NSLocalizedString(@"done", @"确定"), nil];
        alertView.alertViewStyle = UIAlertViewStyleSecureTextInput;
        [alertView show];
    });
}

@end
