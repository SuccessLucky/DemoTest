//
//  ScreenEditCell.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/23.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ScreenEditPropertyModel.h"

@interface ScreenEditCell : UITableViewCell

@property (strong, nonatomic) ScreenEditPropertyModel *editProperty;

@property (weak, nonatomic) IBOutlet UIButton *btnIcon;
@property (weak, nonatomic) IBOutlet UITextField *textFieldName;
@property (weak, nonatomic) IBOutlet UIButton *btnEditScreenName;

@property (weak, nonatomic) IBOutlet UISwitch *switchLinkage;

@property (weak, nonatomic) IBOutlet UITextField *textFieldDelayed;
@property (weak, nonatomic) IBOutlet UISwitch *switchDelayed;

@property (weak, nonatomic) IBOutlet UITextField *textFieldTimer;
@property (weak, nonatomic) IBOutlet UISwitch *switchTimer;

@property (weak, nonatomic) IBOutlet UISwitch *switchArming;
@property (weak, nonatomic) IBOutlet UISwitch *switchDisarming;

//无用参数
@property (weak, nonatomic) IBOutlet UIView *viewLineFirst;
@property (weak, nonatomic) IBOutlet UIView *viewLineSecond;
@property (weak, nonatomic) IBOutlet UIView *viewLineThird;

@property (weak, nonatomic) IBOutlet UIView *viewLineFourth;
@property (weak, nonatomic) IBOutlet UIView *viewLineFifth;
@property (weak, nonatomic) IBOutlet UIView *viewLineSixth;
@property (weak, nonatomic) IBOutlet UILabel *labelArming;
@property (weak, nonatomic) IBOutlet UILabel *labelDisarming;


@property (copy,nonatomic)void(^blockSwitchPressed)(int iTag, BOOL isButtonOn);
@property (copy,nonatomic)void(^blockScreenIconPressed)(UIButton *btn);
@property (copy,nonatomic)void(^blockDelayedTextfieldPressed)(UITextField *tx);
@property (copy,nonatomic)void(^blockTimerTextfieldPressed)(UITextField *tx);

@end
