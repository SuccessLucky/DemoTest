//
//  XFViewController.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2018/6/22.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "XFViewController.h"
#import "NetworkEngine.h"

@interface XFViewController ()

@end

@implementation XFViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - 获取新风设备状态
- (IBAction)btnGetXFStatus:(UIButton *)sender {
    [self doSendXFOrderWithStrFunctionIdentifer:@"F0" strDataRangeIdentifer:@"00"];
}

#pragma mark - 开机关机

- (IBAction)btnSetXFOnOrOff:(UIButton *)sender {
    sender.selected = !sender.selected;
    if (sender.selected) {
        [self doSendXFOrderWithStrFunctionIdentifer:@"02" strDataRangeIdentifer:@"00"];
        NSLog(@"设置关机！");
    }else{
        [self doSendXFOrderWithStrFunctionIdentifer:@"02" strDataRangeIdentifer:@"01"];
        NSLog(@"设置开机！");
    }
}

#pragma mark - 设置加热

- (IBAction)btnSetWarmPressed:(UIButton *)sender {
    sender.selected = !sender.selected;
    if (sender.selected) {
        [self doSendXFOrderWithStrFunctionIdentifer:@"03" strDataRangeIdentifer:@"00"];
        NSLog(@"设置加热关！");
    }else{
        [self doSendXFOrderWithStrFunctionIdentifer:@"03" strDataRangeIdentifer:@"01"];
        NSLog(@"设置加热开！");
    }
}

#pragma  mark - 设置工作模式
//0x00：手动；0x01:自动;0x02:定时;
- (IBAction)btnSetXFModePressed:(UIButton *)sender {
    sender.selected = !sender.selected;
    if (sender.selected) {
        [self doSendXFOrderWithStrFunctionIdentifer:@"04" strDataRangeIdentifer:@"00"];
        NSLog(@"设置工作模式手动！");
    }else{
        [self doSendXFOrderWithStrFunctionIdentifer:@"04" strDataRangeIdentifer:@"01"];
        NSLog(@"设置工作模式自动！");
    }
    
}

#pragma mark - 设置循环模式
//0x00：手动；0x01:自动;0x02:定时;
- (IBAction)btnSetCirculationPressed:(UIButton *)sender {
    sender.selected = !sender.selected;
    if (sender.selected) {
        [self doSendXFOrderWithStrFunctionIdentifer:@"05" strDataRangeIdentifer:@"00"];
        NSLog(@"设置循环模式外循环！");
    }else{
        [self doSendXFOrderWithStrFunctionIdentifer:@"05" strDataRangeIdentifer:@"01"];
        NSLog(@"设置循环模式内循环！");
    }
}

#pragma mark - 设置风速
/*
20-30共11档，手动模式才有效
0x14 0x15 0x16 0x17
0x18 0x19 0x1a 0x1b
0x1c 0x1e 0x1f
 */

- (IBAction)btnSetWindSpeedPressed:(UIButton *)sender {
    sender.selected = !sender.selected;
    if (sender.selected) {
        [self doSendXFOrderWithStrFunctionIdentifer:@"06" strDataRangeIdentifer:@"14"];
        NSLog(@"设置风速14！");
    }else{
        [self doSendXFOrderWithStrFunctionIdentifer:@"06" strDataRangeIdentifer:@"15"];
        NSLog(@"设置风速15！");
    }
}

#pragma mark - 设置进回风比例
/*
 0x00-10/4, 0x01-10/5, 0x02-10/6
 0x03-10/7, 0x04-10/8, 0x05-10/10
 0x06-10/0单进风,  0x07-0/10单排风
 */
- (IBAction)btnSetXFInAndOutWindPressed:(UIButton *)sender {
    sender.selected = !sender.selected;
    if (sender.selected) {
        [self doSendXFOrderWithStrFunctionIdentifer:@"07" strDataRangeIdentifer:@"00"];
        NSLog(@"设置进回风比例10/4！");
    }else{
        [self doSendXFOrderWithStrFunctionIdentifer:@"07" strDataRangeIdentifer:@"01"];
        NSLog(@"设置进回风比例10/5！");
    }
}

#pragma mark - 初滤使用时间清零

- (IBAction)btnSetXFUseTimeToZeroPressed:(UIButton *)sender {
    [self doSendXFOrderWithStrFunctionIdentifer:@"08" strDataRangeIdentifer:@"01"];
    NSLog(@"初滤使用时间清零！");
}

#pragma mark - 集尘箱使用时间清零

- (IBAction)btnSetXFDustBoxUseTimeToZeroPressed:(UIButton *)sender {

    [self doSendXFOrderWithStrFunctionIdentifer:@"09" strDataRangeIdentifer:@"01"];
    NSLog(@"集尘箱使用时间清零！");
}

#pragma mark- 高滤使用时间清零

- (IBAction)btnSetXFHighUseTimeToZeroPressed:(UIButton *)sender {
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

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
