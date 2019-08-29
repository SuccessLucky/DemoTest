//
//  UIStyleDefine.h
//  GJJUser
//
//  Created by gjj on 15/1/20.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

/************************* 颜色字体定义 *****************************/
// 0xff5500
/**
 *  常用色 蓝色
 */
#define kCommonColor            UIColorFromRGB(0x0090ff)

/**
 *  背景灰色
 */
#define kBackgroundGrayColor    UIColorFromRGB(0xeeeef4)
/**
 *  背景白色
 */
#define kBackgroundWhiteColor   UIColorFromRGB(0xffffff)

/**
 *  小图白色背景
 */
#define kImageViewWhiteColor    UIColorFromRGB(0xe2e2e8)

/**
 *  边框颜色
 */
#define kLineColor              UIColorFromRGB(0xc8c8ce)

/**
 *  cell点击态颜色
 */
#define kCellSelectedColor      UIColorFromRGB(0xf4f4fa)

/**
 *  cell上下分割线颜色
 */
#define kCellSeparatorColor     UIColorFromRGB(0xc8c8ce)
#define kCellSeparatorColorH    UIColorFromRGB(0xc8c8ce)


/**
 *  16号字体颜色
 */
#define kTitleLabelColor        UIColorFromRGB(0x202026)

/**
 *  14号字体颜色
 */
#define kSubTitleLbaelColor     UIColorFromRGB(0x808086)

/**
 *  灰色字体颜色
 */
#define kLightGrayTextColor     UIColorFromRGB(0x606066)

/**
 *  其它字体颜色
 */
#define kOtherLabelColor        UIColorFromRGB(0xff5500)

/**
 *  16号字
 */
#define kTitleLabelFont         [UIFont systemFontOfSize:16]

/**
 *  14号字
 */
#define kDetailLabelFont        [UIFont systemFontOfSize:14]
/**
 *  14号字体颜色
 */
#define kSubTitleLabelColor     UIColorFromRGB(0x808086)
/**
 *  12号字体
 */
#define kDescriptionLabelFont   [UIFont systemFontOfSize:12]

/**
 *  上拉和下拉加载的字体颜色
 */
#define TEXT_COLOR              UIColorFromRGB(0xa0a0a6)

/**
 *  设置UILabel大小和颜色   以及设计师 木木同学 即将UI大改的样式
 *
 *  @param str UILabel的text
 *
 *  @return
 */
#define kLabelFont11(str) NSAttributedString *attributed = [[NSAttributedString alloc] initWithString:str]\
attributes:@{\
NSFontAttributeName:[UIFont systemFontOfSize:11],\
NSForegroundColorAttributeName:UIColorFromRGB(0xa0a0a8)}];

#define kLabelFont12(str) NSAttributedString *attributed = [[NSAttributedString alloc] initWithString:str]\
attributes:@{\
NSFontAttributeName:[UIFont systemFontOfSize:12],\
NSForegroundColorAttributeName:UIColorFromRGB(0x808086)}];

#define kLabelFont14(str) NSAttributedString *attributed = [[NSAttributedString alloc] initWithString:str]\
attributes:@{\
NSFontAttributeName:[UIFont systemFontOfSize:14],\
NSForegroundColorAttributeName:UIColorFromRGB(0x808086)}];

#define kLabelFont15(str) NSAttributedString *attributed = [[NSAttributedString alloc] initWithString:str]\
attributes:@{\
NSFontAttributeName:[UIFont systemFontOfSize:15],\
NSForegroundColorAttributeName:UIColorFromRGB(0x404046)}];

#define kLabelFont16(str) NSAttributedString *attributed = [[NSAttributedString alloc] initWithString:str]\
attributes:@{\
NSFontAttributeName:[UIFont systemFontOfSize:16],\
NSForegroundColorAttributeName:UIColorFromRGB(0x202026)}];
/************************* 自定义控件头文件 *****************************/
#import "Masonry.h"
#import "GTitleLabel.h"
#import "GDetailLabel.h"
#import "GDescriptionLabel.h"


