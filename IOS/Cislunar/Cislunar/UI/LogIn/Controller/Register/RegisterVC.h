//
//  RegisterVC.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/15.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "YCTBaseViewController.h"

typedef enum {
    registerType               = 1,
    forgetPswType               = 2,
}RegisterVCType;

@interface RegisterVC : YCTBaseViewController

@property (assign, nonatomic) RegisterVCType vcType;

@end
