//
//  ColorDefine.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/13.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#ifndef ColorDefine_h
#define ColorDefine_h

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
 *  设备宽度
 */
#define UI_SCREEN_WIDTH ([[UIScreen mainScreen] bounds].size.width)
/**
 *  设备高度
 */
#define UI_SCREEN_HEIGHT  ([[UIScreen mainScreen] bounds].size.height)

/**
 *  设定颜色值
 */

#define UIColorFromRGB(rgbValue) [UIColor colorWithRed:((float)((rgbValue & 0xFF0000) >> 16))/255.0 green:((float)((rgbValue & 0xFF00) >> 8))/255.0 blue:((float)(rgbValue & 0xFF))/255.0 alpha:1.0]

//设置大小
#define HELVETICANEUEMEDIUM_FONT(f) [UIFont systemFontOfSize:(f)]

/**
 *  获取tabbarViewController
 */
#define GET_TBARVC [[UIApplication sharedApplication].delegate window].rootViewController;

/**
 *  获取倍率 以5s为标准
 */
#define D_RateX (UI_SCREEN_WIDTH > 320 ? UI_SCREEN_WIDTH/320.0f : 1.0)
#define D_RateY (UI_SCREEN_HEIGHT > 568 ? UI_SCREEN_HEIGHT/568.0f : 1.0)

//获取网关Zigbee的MacAddress
#define kGatewayZigbeeMacAddr


/**
 *  常用色 蓝色
 */
#define kNavColor            UIColorFromRGB(0x202657)
#define kTabarColor          UIColorFromRGB(0x313c83)




#endif /* ColorDefine_h */
