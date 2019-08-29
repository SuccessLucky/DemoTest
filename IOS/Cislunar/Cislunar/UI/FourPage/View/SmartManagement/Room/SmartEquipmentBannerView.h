//
//  SmartEquipmentBannerView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/12.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SmartEquipmentBannerView : UIView

@property (weak, nonatomic) IBOutlet UIButton *btnBack;

@property (weak, nonatomic) IBOutlet UIButton *btnAdd;

@property (weak, nonatomic) IBOutlet UIButton *btnTitle;

@property (copy, nonatomic) void (^BlockBtnTitlePressed)(id object,UIButton *btn);

@property (copy, nonatomic) void (^BlockBtnBackPressed)(id object,UIButton *btn);
@property (copy, nonatomic) void (^BlockBtnAddPressed)(id object,UIButton *btn);



@end
