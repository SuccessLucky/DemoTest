//
//  HIKLoadViewItem.h
//  VideoGo
//
//  Created by zhil.shi on 15/3/9.
//  Copyright (c) 2015年 HIKVison. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <QuartzCore/QuartzCore.h>
#import <UIKit/UIKit.h>

@interface HIKLoadViewItem : NSObject

/**
 *  图像图层
 */
@property (nonatomic, strong) CALayer *colorCirculeLayer;
/**
 *  全能初始化
 *
 *  @param image 图片名称
 *
 *  @return 包含layer的item
 */
+ (instancetype)initWithImage:(UIImage*)image;


/**
 *  设置position
 *
 *  @param position 坐标
 */
- (void)setPosition:(CGPoint)position;
@end


