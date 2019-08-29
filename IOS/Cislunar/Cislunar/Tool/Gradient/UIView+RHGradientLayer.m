//
//  UIView+Utils.m
//  CATextLayer
//
//  Created by 任航 on 2017/5/22.
//  Copyright © 2017年 任航. All rights reserved.
//

#import "UIView+RHGradientLayer.h"


#import <objc/runtime.h>

static const void *rt_text = "text";
static const void *rt_textColor = "textColor";
static const void *rt_font = "font";
static const void *rt_fromColor = @"fromColor";
static const void *rt_toColor = @"toColor";

static const void *rt_textLayer = @"textLayer";
static const void *rt_gradientLayer = @"gradientLayer";


@interface UIView ()

@property (nonatomic, strong)CATextLayer *textLayer;
@property (nonatomic, strong)CAGradientLayer *gradientLayer;

@end

@implementation UIView (Utils)



- (NSString *)text
{
    
    return objc_getAssociatedObject(self, rt_text);
}

- (void)setText:(NSString *)text
{
    //    objc_setAssociatedObject(<#id object#>, <#const void *key#>, <#id value#>, <#objc_AssociationPolicy policy#>)
    objc_setAssociatedObject(self, rt_text, text, OBJC_ASSOCIATION_COPY_NONATOMIC);
    
    self.textLayer = [CATextLayer layer];
    
    [self.layer addSublayer:self.textLayer];
    
    self.textLayer.alignmentMode = kCAAlignmentCenter;
    self.textLayer.string = text;
    self.textLayer.contentsScale = [UIScreen mainScreen].scale;
    
    CFStringRef fontName = (__bridge CFStringRef)self.font.fontName;
    CGFontRef fontRef = CGFontCreateWithFontName(fontName);
    self.textLayer.font = fontRef;
    self.textLayer.fontSize = self.font.pointSize;
    
    CGSize textSize = [text sizeWithAttributes:@{NSFontAttributeName: self.font}];
    CGRect frame = self.textLayer.frame;
    frame.size = textSize;
    self.textLayer.frame = CGRectMake(self.frame.size.width/2 - frame.size.width/2, self.frame.size.height/2 - frame.size.height/2, textSize.width, textSize.height);
    
    CGFontRelease(fontRef);
    
}


- (UIFont *)font
{
    return objc_getAssociatedObject(self, rt_font);
}

- (void)setFont:(UIFont *)font
{
    //set layer font
    CFStringRef fontName = (__bridge CFStringRef)font.fontName;
    CGFontRef fontRef = CGFontCreateWithFontName(fontName);
    self.textLayer.font = fontRef;
    self.textLayer.fontSize = font.pointSize;
    CGFontRelease(fontRef);
    return objc_setAssociatedObject(self, rt_font, font, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (UIColor *)textColor
{
    return objc_getAssociatedObject(self, rt_textColor);
}

- (void)setTextColor:(UIColor *)textColor
{
    self.textLayer.foregroundColor = textColor.CGColor;
    return objc_setAssociatedObject(self, rt_textColor, textColor, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}


- (CATextLayer *)textLayer
{
    return objc_getAssociatedObject(self, rt_textLayer);
}

- (void)setTextLayer:(CATextLayer *)textLayer
{
    return objc_setAssociatedObject(self, rt_textLayer, textLayer, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}


- (CAGradientLayer *)gradientLayer
{
    return objc_getAssociatedObject(self, rt_gradientLayer);
}

- (void)setGradientLayer:(CAGradientLayer *)gradientLayer
{
    return objc_setAssociatedObject(self, rt_gradientLayer, gradientLayer, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}



- (NSString *)fromColor
{
    if (!self.gradientLayer) {
        
        self.gradientLayer = [CAGradientLayer layer];
        self.gradientLayer.frame = self.bounds;
        self.gradientLayer.startPoint = CGPointMake(0, 0);
        self.gradientLayer.endPoint = CGPointMake(1, 1);
        self.gradientLayer.locations = @[@0,@1];
        [self.layer addSublayer:self.gradientLayer];
        
    }
    
    return objc_getAssociatedObject(self, rt_fromColor);
}

- (void)setFromColor:(NSString *)fromColor
{
    return objc_setAssociatedObject(self, rt_fromColor, fromColor, OBJC_ASSOCIATION_COPY_NONATOMIC);
    
}


- (NSString *)toColor
{
    
    return objc_getAssociatedObject(self, rt_toColor);
}

- (void)setToColor:(NSString *)toColor
{
    
    if (!self.gradientLayer) {
        self.gradientLayer = [CAGradientLayer layer];
        self.gradientLayer.frame = self.bounds;
        self.gradientLayer.startPoint = CGPointMake(0, 0);
        self.gradientLayer.endPoint = CGPointMake(1, 1);
        self.gradientLayer.locations = @[@0,@1];
        [self.layer addSublayer:self.gradientLayer];
    }
    self.gradientLayer.colors = @[(__bridge id)[[self class] colorWithHex:self.fromColor].CGColor,(__bridge id)[[self class] colorWithHex:toColor].CGColor];
    return objc_setAssociatedObject(self, rt_toColor, toColor, OBJC_ASSOCIATION_COPY_NONATOMIC);
}


+ (UIColor *)colorWithHex:(NSString *)hexColor {
    hexColor = [hexColor stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    
    if ([hexColor length] < 6) {
        return nil;
    }
    
    if ([hexColor hasPrefix:@"#"]) {
        hexColor = [hexColor substringFromIndex:1];
    }
    
    NSRange range;
    range.length = 2;
    
    range.location = 0;
    NSString *rs = [hexColor substringWithRange:range];
    
    range.location = 2;
    NSString *gs = [hexColor substringWithRange:range];
    
    range.location = 4;
    NSString *bs = [hexColor substringWithRange:range];
    
    unsigned int r, g, b, a;
    [[NSScanner scannerWithString:rs] scanHexInt:&r];
    [[NSScanner scannerWithString:gs] scanHexInt:&g];
    [[NSScanner scannerWithString:bs] scanHexInt:&b];
    
    if ([hexColor length] == 8) {
        range.location = 4;
        NSString *as = [hexColor substringWithRange:range];
        [[NSScanner scannerWithString:as] scanHexInt:&a];
    } else {
        a = 255;
    }
    
    return [UIColor colorWithRed:((float)r / 255.0f) green:((float)g / 255.0f) blue:((float)b / 255.0f) alpha:((float)a / 255.0f)];
}







@end
