//
//  SecurityHeaderView.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/20.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SecurityHeaderView : UIView

@property (copy, nonatomic) void(^BlockCollectionHeaderSelected)(UIButton *btn,UIImageView *imageV,BOOL isArming);

@property (copy, nonatomic) void(^BlockBtnCancellAlarmPressed)(UIButton *btnCancellAlarm);

@property (assign, nonatomic) BOOL isArming;

@end
