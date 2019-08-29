//
//  HIKLoadViewItem+configPath.h
//  VideoGo
//
//  Created by zhil.shi on 15/3/9.
//  Copyright (c) 2015年 HIKVison. All rights reserved.
//

#import "HIKLoadViewItem.h"

typedef NS_ENUM(NSInteger,HIKLoadViewStartLoadingSyle)
{
    HIKLoadViewStartFromLeftTop     = 0,//初始位置正方形左上角，做顺时针正方形运动
    HIKLoadViewStartFromRightTop    = 1,//初始位置正方形右上角，做顺时针正方形运动
    HIKLoadViewStartFromRightBottom = 2,//初始位置正方形右下角，做顺时针正方形运动
    HIKLoadViewStartFromLeftBottom//初始位置正方形左下角，做顺时针正方形运动
};

@interface HIKLoadViewItem (configPath)
/**
 *  根据路径起始位置，初始化正方形顺时针路径，并返回keyFrameAnimation
 *
 *  @param style 起始位置
 *
 *  @return
 */
- (void)configKeyFrameAnimationForColorLayerWithStartLoadingStyle:(HIKLoadViewStartLoadingSyle)style;

/**
 *  设置modelLayer的position为当前preseterLayer的positon
 *
 */
- (void)stayOnPresentationLayerPosition;

@end
