//
//  ScreenEditCell.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/5/23.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import "ScreenEditCell.h"

@interface ScreenEditCell ()<UITextFieldDelegate>

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *lineOneHeight;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *lineTwoHeight;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *lineThreeHeight;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *lineFourHeight;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *lineFiveHeight;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *lineSixHeight;

@property (weak, nonatomic) IBOutlet UILabel *labelChange;


@end

@implementation ScreenEditCell

- (void)awakeFromNib {
    [super awakeFromNib];
    self.lineOneHeight.constant = 0.5;
    self.lineTwoHeight.constant = 0.5;
    self.lineThreeHeight.constant = 0.5;
    self.lineFourHeight.constant = 0.5;
    self.lineFiveHeight.constant = 0.5;
    self.lineSixHeight.constant = 0.5;
    
    self.viewLineFirst.backgroundColor = kLineColor;
    self.viewLineSecond.backgroundColor = kLineColor;
    self.viewLineThird.backgroundColor = kLineColor;
    
    self.viewLineFourth.backgroundColor = kLineColor;
    self.viewLineFifth.backgroundColor = kLineColor;
    self.viewLineSixth.backgroundColor = kLineColor;
    
    UIColor *color = [UIColor whiteColor];
    self.backgroundColor = [color colorWithAlphaComponent:0.0];
    
    self.textFieldDelayed.delegate = self;
    self.textFieldTimer.delegate = self;
    
    
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

#pragma mark -
#pragma mark - action

- (IBAction)switchLinkagePressed:(UISwitch *)sender {
    //根据tag值进行判断
    if (sender.isOn) {
        NSLog(@"开");
    }else{
    
        NSLog(@"关");
    }
    if (self.blockSwitchPressed) {
        self.blockSwitchPressed((int)sender.tag,[sender isOn]);
    }
}

- (IBAction)textFieldDelayedPressed:(UITextField *)sender {
    NSLog(@"ddddddd0");
    
}

- (IBAction)textFieldTimerPressed:(UITextField *)sender {
    if (self.blockTimerTextfieldPressed) {
        self.blockTimerTextfieldPressed(sender);
    }
}

- (IBAction)btnIconPressed:(UIButton *)sender {
    if (self.blockScreenIconPressed) {
        self.blockScreenIconPressed(sender);
    }
}
- (IBAction)textFieldDelayedEiditBegin:(UITextField *)sender {
    [sender resignFirstResponder];
    NSLog(@"ddddddd1");
    if (self.blockDelayedTextfieldPressed) {
        self.blockDelayedTextfieldPressed(sender);
    }
}

- (IBAction)textFieldTimerEditBegin:(UITextField *)sender {
    [sender resignFirstResponder];
    NSLog(@"xxxxxx1");
    if (self.blockTimerTextfieldPressed) {
        self.blockTimerTextfieldPressed(sender);
    }
}

-(void)textFieldDidBeginEditing:(UITextField*)textField
{
    [textField resignFirstResponder];
}


#pragma mark -
#pragma mark - 数据加载
- (void)setEditProperty:(ScreenEditPropertyModel *)editProperty
{
    _editProperty = editProperty;
    

    NSString *strUnUrl = [[SHAppCommonRequest shareInstance] doGetScreenGrayUIPicWithPicName:editProperty.strScreeenEditIconUrl];
    NSString *strPrUrl = [[SHAppCommonRequest shareInstance] doGetScreenHighLightUIPicWithPicName:editProperty.strScreeenEditIconUrl];
    [self.btnIcon sd_setImageWithURL:[NSURL URLWithString:strUnUrl] forState:UIControlStateNormal placeholderImage:[UIImage imageNamed:@"丰富"]];
    [self.btnIcon sd_setImageWithURL:[NSURL URLWithString:strPrUrl] forState:UIControlStateSelected  placeholderImage:[UIImage imageNamed:@"丰富"]];
    
    
    self.textFieldName.text = editProperty.strScreenEditName;
    
    NSString *strIsLinkage = editProperty.strScreenIsLinkage;
    if ([strIsLinkage intValue] == 0) {
        [self.switchLinkage setOn:NO animated:YES];
        
        self.viewLineFourth.hidden = YES;
        self.viewLineFifth.hidden = YES;
        self.viewLineSixth.hidden = YES;
        self.labelArming.hidden = YES;
        self.labelDisarming.hidden = YES;
        self.switchArming.hidden = YES;
        self.switchDisarming.hidden = YES;
        
        self.labelChange.text = @"定时";
        [self.textFieldTimer setPlaceholder:@"00:00"];
        
    }else{
        [self.switchLinkage setOn:YES animated:YES];
        self.viewLineFourth.hidden = NO;
        self.viewLineFifth.hidden = NO;
        self.viewLineSixth.hidden = NO;
        self.labelArming.hidden = NO;
        self.labelDisarming.hidden = NO;
        self.switchArming.hidden = NO;
        self.switchDisarming.hidden = NO;
        self.labelChange.text = @"联动时间段";
        [self.textFieldTimer setPlaceholder:@"联动时间段"];
        
    }
    /*
    NSString *strDelayed = editProperty.strScreenEditDelayed;
    if ([strDelayed intValue] == 0) {
        self.textFieldDelayed.text = @"";
        [self.switchDelayed setOn:NO animated:YES];
    }else{
        self.textFieldDelayed.text = strDelayed;
        [self.switchDelayed setOn:YES animated:YES];
    }
    
    
    NSString *strTimer = editProperty.strScreenEditTimer;
    if ([strTimer intValue] == 0) {
        self.textFieldTimer.text = @"";
        [self.switchTimer setOn:NO animated:YES];
    }else{
        self.textFieldTimer.text = strTimer;
        [self.switchTimer setOn:YES animated:YES];
    }
     */
    if (editProperty.iScreen_need_time_delay == 0) {
        
        self.textFieldDelayed.text = @"";
        [self.switchDelayed setOn:NO];
        [self.textFieldDelayed resignFirstResponder];
        self.textFieldDelayed.userInteractionEnabled = NO;
    }else{
//        [self.textFieldDelayed becomeFirstResponder];
        self.textFieldDelayed.userInteractionEnabled = YES;
        
        if ([editProperty.strScreenEditTimer isKindOfClass:[NSNull class]]) {
            self.textFieldDelayed.text = @"";
        }else{
            
            self.textFieldDelayed.text = editProperty.strScreenEditDelayed;
        }
        
        [self.switchDelayed setOn:YES];
    }
        
    
    if (editProperty.iScreen_need_timing == 0) {
        
        self.textFieldTimer.text = @"";
        [self.switchTimer setOn:NO];
        [self.textFieldTimer resignFirstResponder];
        self.textFieldTimer.userInteractionEnabled = NO;
        
    }else{
        
//        [self.textFieldTimer becomeFirstResponder];
        self.textFieldTimer.userInteractionEnabled = YES;
        
        if ([strIsLinkage intValue] == 0) {
            //非联动
            if ([editProperty.strScreenEditTimer isKindOfClass:[NSNull class]]) {
                self.textFieldTimer.text = @"";
            }else{
            
                self.textFieldTimer.text = editProperty.strScreenEditTimer;
            }
            
        }else{
            
            if ([editProperty.str_linkage_time isKindOfClass:[NSNull class]]) {
                self.textFieldTimer.text = @"";
            }else{
                self.textFieldTimer.text = editProperty.str_linkage_time;
            }
        }
        [self.switchTimer setOn:YES];
    }
    
    
    NSString *strIsArming = editProperty.strScreenEditArming;
    if ([strIsArming intValue] == 0) {
        [self.switchArming setOn:NO];
        [self.switchDisarming setOn:YES];
    }else{
        [self.switchArming setOn:YES];
        [self.switchDisarming setOn:NO];
    }
    
    NSString *strIsDisarming = editProperty.strScreenEditDisarming;
    if ([strIsDisarming intValue] == 0) {
         [self.switchArming setOn:YES];
        [self.switchDisarming setOn:NO];
    }else{
        [self.switchArming setOn:NO];
        [self.switchDisarming setOn:YES];
    }
}

//- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField {
//    return NO;
//
//}

@end
