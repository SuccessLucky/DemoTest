//
//  UIButton+TitleImgEdgeInsets.h
//  Button
//
//  Created by lisonglin on 19/04/2017.
//  Copyright © 2017 lisonglin. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSUInteger, LSButtonEdgeInsetsStyle){
    //文字在左 图片在右
    LSButtonEdgeInsetsStyleLeft,
    //文字在右 图片在左
    LSButtonEdgeInsetsStyleRight,
    //文字在上 图片在下
    LSButtonEdgeInsetsStyleTop,
    //文字在下 图片在上
    LSButtonEdgeInsetsStyleBottom
};

@interface UIButton (TitleImgEdgeInsets)

- (void)layoutButtonTitleImageEdgeInsetsWithStyle:(LSButtonEdgeInsetsStyle)style titleImgSpace:(CGFloat)space;

@end
