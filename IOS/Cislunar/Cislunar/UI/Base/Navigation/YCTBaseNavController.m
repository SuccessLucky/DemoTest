//
//  YCTBaseNavController.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/14.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "YCTBaseNavController.h"

@interface YCTBaseNavController ()

@end

@implementation YCTBaseNavController

- (void)viewDidLoad {
    // Do any additional setup after loading the view.
    //去掉导航栏底部的横线
    [self.navigationBar setShadowImage:[UIImage new]];
    //设置导航栏背景
    [self.navigationBar setBackgroundImage:[self createImageWithColor:kNavColor] forBarMetrics:UIBarMetricsDefault];
    
//    UIImage * image =  [[UIImage imageNamed:@"testPng"]
//                        resizableImageWithCapInsets:UIEdgeInsetsMake(-1, 0, 0, 0) resizingMode:UIImageResizingModeStretch];
//    [self.navigationBar setBackgroundImage:image forBarMetrics:UIBarMetricsDefault];
    
    //添加右扫返回手势
    UISwipeGestureRecognizer *swipGesture = [[UISwipeGestureRecognizer alloc]initWithTarget:self action:@selector(swipGesture:)];
    swipGesture.direction = UISwipeGestureRecognizerDirectionRight;
    [self.view addGestureRecognizer:swipGesture];
    
    //默认开启手势右滑返回上一个界面
    self.isSwipGesture = YES;
    
}
    
    //手势右划退出界面
- (void)swipGesture:(UISwipeGestureRecognizer *)gesture
    {
        if(self.isSwipGesture){
            if (self.viewControllers.count > 1) {
                [self popViewControllerAnimated:YES];
            }
        }
        
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

- (UIImage*)createImageWithColor:(UIColor*)color{
    
    CGRect rect=CGRectMake(0.0f,0.0f,1.0f,1.0f);
    UIGraphicsBeginImageContext(rect.size);
    
    CGContextRef context=UIGraphicsGetCurrentContext();
    CGContextSetFillColorWithColor(context, [color CGColor]);
    
    CGContextFillRect(context, rect);
    
    UIImage*theImage=UIGraphicsGetImageFromCurrentImageContext();UIGraphicsEndImageContext();
    return theImage;
    
}

@end
