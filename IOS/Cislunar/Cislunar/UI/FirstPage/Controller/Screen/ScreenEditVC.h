//
//  ScreenEditVC.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/23.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import "YCTBaseViewController.h"

typedef enum {
    ScreenEditVCType_Add           = 1,
    ScreenEditVCType_Edit          = 2,
}ScreenEditVCType;

@interface ScreenEditVC : YCTBaseViewController

@property (assign, nonatomic) ScreenEditVCType vcType;
@property (strong, nonatomic) ScreenModel *screenModel;

/*
@property (strong, nonatomic) NSString *strShouldGiveScreenNoHex;
 */

@property (strong, nonatomic) NSString *strShouldGiveScreenNoHexTransi;

@end
