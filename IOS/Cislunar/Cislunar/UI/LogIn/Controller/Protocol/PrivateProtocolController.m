//
//  PrivateProtocolController.m
//  SmartHouseYCT
//
//  Created by apple on 16/9/10.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "PrivateProtocolController.h"

@interface PrivateProtocolController ()

@property (weak, nonatomic) IBOutlet UITextView *textView;

@end

@implementation PrivateProtocolController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self doInitSubViews];
    NSError *error;
    NSString *textContents = [NSString stringWithContentsOfFile:[[NSBundle mainBundle]pathForResource:@"PrivateProtocol" ofType:@"rtf"] encoding:NSUTF8StringEncoding error:&error];
    NSLog(@"%@",textContents);
    self.textView.text = textContents;
    // Do any additional setup after loading the view.
}

- (void)doInitSubViews
{
    [self setTitleViewText:@"用户协议"];
    
    [self setNavigationBarBackButtonWithTitle:@"" action:@selector(leftAction:)];
}

- (void)leftAction:(UIButton *)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillDisappear:animated];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
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
