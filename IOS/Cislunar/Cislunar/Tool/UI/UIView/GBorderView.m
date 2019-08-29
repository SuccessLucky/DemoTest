//
//  GBorderView.m
//  GJJUser
//
//  Created by yuchangtao on 15/1/22.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#import "GBorderView.h"

@implementation GBorderView

- (void) drawRect:(CGRect)rect
{
    CGContextRef ctx = UIGraphicsGetCurrentContext();
    
    CGContextSetLineWidth(ctx, _lineWith * [[UIScreen mainScreen] scale]);
    CGFloat red, green, blue, alpha;
    [self.lineColor getRed:&red green:&green blue:&blue alpha:&alpha];
    CGContextSetRGBStrokeColor(ctx, red, green, blue, alpha);
    
    if(self.drawsTopLine) {
        CGContextBeginPath(ctx);
        CGContextMoveToPoint(ctx, CGRectGetMinX(rect), CGRectGetMinY(rect));
        CGContextAddLineToPoint(ctx, CGRectGetMaxX(rect), CGRectGetMinY(rect));
        CGContextStrokePath(ctx);
    }
    if(self.drawsBottomLine) {
        CGContextBeginPath(ctx);
        CGContextMoveToPoint(ctx, CGRectGetMinX(rect), CGRectGetMaxY(rect));
        CGContextAddLineToPoint(ctx, CGRectGetMaxX(rect), CGRectGetMaxY(rect));
        CGContextStrokePath(ctx);
    }
    if(self.drawsLeftLine) {
        CGContextBeginPath(ctx);
        CGContextMoveToPoint(ctx, CGRectGetMinX(rect), CGRectGetMinY(rect));
        CGContextAddLineToPoint(ctx, CGRectGetMinX(rect), CGRectGetMaxY(rect));
        CGContextStrokePath(ctx);
    }
    if(self.drawsRightLine) {
        CGContextBeginPath(ctx);
        CGContextMoveToPoint(ctx, CGRectGetMaxX(rect), CGRectGetMinY(rect));
        CGContextAddLineToPoint(ctx, CGRectGetMaxX(rect), CGRectGetMaxY(rect));
        CGContextStrokePath(ctx);
    }
    
    [super drawRect:rect];
}

@end
