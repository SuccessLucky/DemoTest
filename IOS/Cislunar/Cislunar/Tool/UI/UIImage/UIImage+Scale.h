//
//  UIImage+Scale.h
//  GJJUser
//
//  Created by 林淦雄-MAC on 15/6/15.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIImage (Scale)

-(UIImage*)scaleToSize:(CGSize)size;
-(UIImage*)getSubImage:(CGRect)rect;

@end
