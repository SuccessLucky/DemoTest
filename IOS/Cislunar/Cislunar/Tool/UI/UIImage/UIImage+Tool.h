//
//  UIImage+Tool.h
//  GJJManager
//
//  Created by yangpei on 15/5/7.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIImage (Tool)
/**
 *  通过颜色创建图片
 */
+ (UIImage *)imageWithColor:(UIColor *)color;

/**
 *  重设图片大小
 */
+ (UIImage *)imageWithImage:(UIImage *)image scaledToSize:(CGSize)newSize;

/**
 *  重设图片大小，原始像素
 */
+ (UIImage *)imageWithImage:(UIImage *)image scaledExactPixelToSize:(CGSize)newSize;

/**
 *  修正图片方向
 */
+ (UIImage *)fixOrientation:(UIImage *)image;
@end
