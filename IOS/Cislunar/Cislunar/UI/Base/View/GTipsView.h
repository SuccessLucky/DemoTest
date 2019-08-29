//
//  GTipsView.h
//  GJJManager
//
//  Created by yangpei on 15/5/4.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^GTipsViewTouchBlock)();

@interface GTipsView : UIView
- (instancetype)initWithTitle:(NSString *)title image:(UIImage *)image;
- (void)setImage:(UIImage *)image;
- (void)setTitle:(NSString *)title;
- (void)setTouchBlock:(GTipsViewTouchBlock)touchBlock;
@end
