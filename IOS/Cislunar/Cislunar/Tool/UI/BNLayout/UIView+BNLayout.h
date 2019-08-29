//
//  UIView+BNLayout.h
//  LayoutStudy
//
//  Created by Feather Chan on 13-7-1.
//  Copyright (c) 2013年 Feather Chan. All rights reserved.
//

#import <UIKit/UIKit.h>

#define AUTO (-FLT_MAX)

struct BNLayout {
    UIEdgeInsets edge;
    CGSize  size;
};
typedef struct BNLayout BNLayout;



CG_INLINE BNLayout
BNLayoutMake(CGFloat top,CGFloat left,CGFloat bottom,CGFloat right,CGFloat width,CGFloat height)
{
    BNLayout layout;
    
    layout.edge = UIEdgeInsetsMake(top, left, bottom, right);
    layout.size = CGSizeMake(width, height);
    
    return layout;
}

CG_EXTERN bool BNLayoutEqualToLayout(BNLayout layout1, BNLayout layout2);

CG_INLINE bool
__BNLayoutEqualToLayout(BNLayout layout1, BNLayout layout2)
{
    return UIEdgeInsetsEqualToEdgeInsets(layout1.edge, layout2.edge) && CGSizeEqualToSize(layout1.size, layout2.size);
}
#define BNLayoutEqualToLayout __BNLayoutEqualToLayout

#define BNLayoutZero BNLayoutMake(0,0,0,0,0,0)
#define BNLayoutAuto BNLayoutMake(AUTO,AUTO,AUTO,AUTO,AUTO,AUTO)

@interface UIView (BNLayout)

// get or set the layout pattern
@property (nonatomic,assign) BNLayout layout;

// indicated that if it is a view initialized with BNLayout
@property (nonatomic,readonly) BOOL isLayoutView;

// get or modify the position or pattern, AUTO is available
@property (nonatomic) CGFloat left;
@property (nonatomic) CGFloat top;
@property (nonatomic) CGFloat right;
@property (nonatomic) CGFloat bottom;
@property (nonatomic) CGFloat width;
@property (nonatomic) CGFloat height;

// init with the layout struct
- (id)initWithLayout:(BNLayout)layout;

@end
