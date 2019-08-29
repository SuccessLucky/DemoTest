//
//  CommonAlterFooterView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/26.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef NS_ENUM(NSInteger, CommonAlterBtnType)
{
    CommonAlterBtnType_Cancell  = 0,
    CommonAlterBtnType_OK       = 1,
};

typedef void(^BlockBtnPressed)(CommonAlterBtnType btnType);

@interface CommonAlterFooterView : UIView
@property (weak, nonatomic) IBOutlet UIButton *btnCancell;
@property (weak, nonatomic) IBOutlet UIButton *btnOK;
@property (weak, nonatomic) IBOutlet UIImageView *imageViewLine;

@property (copy, nonatomic) BlockBtnPressed blockBtnPressed;

@end
