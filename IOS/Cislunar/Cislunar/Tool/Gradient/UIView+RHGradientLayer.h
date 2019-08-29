//
//  UIView+RHGradientLayer.h
//  CATextLayer
//
//  Created by 任航 on 2017/5/23.
//  Copyright © 2017年 任航. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIView (Utils)

@property (nonatomic, copy) NSString *text;
@property (nonatomic, strong) UIColor *textColor;
@property (nonatomic, strong) UIFont *font;

@property (nonatomic, strong) NSString *fromColor;
@property (nonatomic, strong) NSString *toColor;

//+ (CAGradientLayer *)setGradualChangingColor:(UIView *)view fromColor:(NSString *)fromHexColorStr toColor:(NSString *)toHexColorStr;
    
    

//    testView.fromColor = @"F76B1C";
//
//    testView.toColor = @"FBDA61";
//
//    testView.text = @"可爱的UIView";
@end
