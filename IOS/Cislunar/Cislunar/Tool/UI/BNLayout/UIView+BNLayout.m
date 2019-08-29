//
//  UIView+BNLayout.m
//  LayoutStudy
//
//  Created by Feather Chan on 13-7-1.
//  Copyright (c) 2013年 Feather Chan. All rights reserved.
//

#import "UIView+BNLayout.h"
#import <objc/runtime.h>

#define IMAGINED_RECT CGRectMake(0, 0, 320, 480)

static NSString *layoutKey;


@implementation UIView (BNLayout)

+ (void)load
{
    layoutKey = @"layout";
    
    Method orig_method = nil, alt_method = nil;
    
    // First, look for the methods
    orig_method = class_getInstanceMethod([UIView class], @selector(willMoveToSuperview:));
    alt_method = class_getInstanceMethod([UIView class], @selector(willMoveToSuperviewExchanged:));
	
    // If both are found, swizzle them
    if ((orig_method != nil) && (alt_method != nil)) {
        method_exchangeImplementations(orig_method, alt_method);
	}
    
}

- (id)initWithLayout:(BNLayout)layout
{
    self = [self initWithFrame:CGRectZero];
    if (self) {
        
        self.layout = layout;
        
    }
    return self;
}

#pragma mark - Adjusting Layout -

- (void)setLayout:(BNLayout)layout withSuperview:(UIView *)superview;
{
    //NSLog(@"set Layout UIView: %@",self);
    
    CGRect frame = self.frame;
    CGRect superviewFrame = superview.frame;

    self.autoresizingMask =
    UIViewAutoresizingFlexibleTopMargin|
    UIViewAutoresizingFlexibleRightMargin|
    UIViewAutoresizingFlexibleBottomMargin|
    UIViewAutoresizingFlexibleLeftMargin|
    UIViewAutoresizingFlexibleHeight|
    UIViewAutoresizingFlexibleWidth;
    
    // horizontal size
    
    if (layout.edge.left != AUTO) {
        [self removeAutoresizingMask:UIViewAutoresizingFlexibleLeftMargin];
    }
    
    if (layout.size.width != AUTO) {
        [self removeAutoresizingMask:UIViewAutoresizingFlexibleWidth];
    }
    
    if (layout.edge.right != AUTO) {
        [self removeAutoresizingMask:UIViewAutoresizingFlexibleRightMargin];
    }
    
    if (layout.edge.left == AUTO) {
        
        if (layout.size.width == AUTO) {
            
            if (layout.edge.right == AUTO) {
                
            }else{
                
            }
            
        }else{
            
            if (layout.edge.right == AUTO) {
                
                
            }else{
                
                frame.size.width    = layout.size.width;
                frame.origin.x      = superviewFrame.size.width - layout.size.width - layout.edge.right;
            }
        }
        
    }else{
        
        if (layout.size.width == AUTO) {
            
            if (layout.edge.right == AUTO) {
                
                
            }else{
                
                frame.origin.x      = layout.edge.left;
                frame.size.width    = superviewFrame.size.width - layout.edge.left - layout.edge.right;
            }
            
        }else{
                
            frame.origin.x      = layout.edge.left;
            frame.size.width    = layout.size.width;
        }
    }
    
    // vertival resize
    
    if (layout.edge.top != AUTO) {
        [self removeAutoresizingMask:UIViewAutoresizingFlexibleTopMargin];
    }
    
    if (layout.size.height != AUTO) {
        [self removeAutoresizingMask:UIViewAutoresizingFlexibleHeight];
    }
    
    if (layout.edge.bottom != AUTO) {
        [self removeAutoresizingMask:UIViewAutoresizingFlexibleBottomMargin];
    }
    
    if (layout.edge.top == AUTO) {
        
        if (layout.size.height == AUTO) {
            
            if (layout.edge.bottom == AUTO) {
                
                
            }else{
                
            }
            
        }else{
            
            if (layout.edge.bottom == AUTO) {
                
                
            }else{
                
                frame.size.height   = layout.size.height;
                frame.origin.y      = superviewFrame.size.height - layout.size.height - layout.edge.bottom;
            }
        }
        
    }else{
        
        if (layout.size.height == AUTO) {
            
            if (layout.edge.bottom == AUTO) {
                
                
            }else{
                
                frame.origin.y      = layout.edge.top;
                frame.size.height   = superviewFrame.size.height - layout.edge.top - layout.edge.bottom;
            }
            
        }else{
                
                frame.origin.y      = layout.edge.top;
                frame.size.height   = layout.size.height;
        }
    }
    
    self.frame = frame;
}

#pragma mark - Layout Getter & Setter -

- (void)setLayout:(BNLayout)layout
{
    
    objc_setAssociatedObject(self, "layout", @[
                                                 [NSValue valueWithUIEdgeInsets:layout.edge],
                                                 [NSValue valueWithCGSize:layout.size]
                                                 ], OBJC_ASSOCIATION_RETAIN);
    
    if (self.superview) {
        [self setLayout:layout withSuperview:self.superview];
    }
}


- (BNLayout)layout
{
    
    NSArray *layoutList = objc_getAssociatedObject(self, "layout");
    
    if (layoutList) {

        BNLayout layout = BNLayoutZero;
        layout.edge = [layoutList[0] UIEdgeInsetsValue];
        layout.size = [layoutList[1] CGSizeValue];
        
        return layout;
        
    }else{
        
        return BNLayoutZero;
        
    }
}

- (BOOL)isLayoutView
{
    NSArray *layoutList = objc_getAssociatedObject(self, "layout");

    return !!layoutList;
}

#pragma mark - Exchanged Methods -

- (void)willMoveToSuperviewExchanged:(UIView *)newSuperview
{
    [self willMoveToSuperviewExchanged:newSuperview];
    
    if ([self isLayoutView] && newSuperview)
    {
        [self setLayout:self.layout withSuperview:newSuperview];
    }
}


#pragma mark - AutoresizeMask Convenient Methods -

- (void)addAutoresizingMask:(UIViewAutoresizing)autoresizingMask
{
    self.autoresizingMask = self.autoresizingMask | autoresizingMask;
}

- (void)removeAutoresizingMask:(UIViewAutoresizing)autoresizingMask
{
    self.autoresizingMask = self.autoresizingMask ^ autoresizingMask;
}

- (BOOL)hasAutoresizingMask:(UIViewAutoresizing)autoresizingMask
{
    return (self.autoresizingMask & autoresizingMask) > 0;
}

#pragma mark - Property Getter & Setter -

- (CGFloat)left{
    return self.frame.origin.x;
}


- (void)setLeft:(CGFloat)left {
    
    if ([self isLayoutView]){
        
        BNLayout layout = self.layout;
        UIEdgeInsets edge = layout.edge;
        edge.left = left;
        layout.edge = edge;
        self.layout = layout;
        
        if (self.superview) {
            [self setLayout:layout withSuperview:self.superview];
        }
        
    }else{
        
        CGRect frame = self.frame;
        frame.origin.x = left;
        self.frame = frame;
    }
}


- (CGFloat)width {
    return self.frame.size.width;
}



- (void)setWidth:(CGFloat)width {
    
    if ([self isLayoutView]){
            
            BNLayout layout = self.layout;
            CGSize size = layout.size;
            size.width = width;
            layout.size = size;
            self.layout = layout;
            
            if (self.superview) {
                [self setLayout:layout withSuperview:self.superview];
            }
        
    }else{
        
        CGRect frame = self.frame;
        frame.size.width = width;
        self.frame = frame;
    }
    
}




- (CGFloat)right {
    return self.frame.origin.x + self.frame.size.width;
}


- (void)setRight:(CGFloat)right {
    
    if ([self isLayoutView]){
            
        BNLayout layout = self.layout;
        UIEdgeInsets edge = layout.edge;
        edge.right = right;
        layout.edge = edge;
        self.layout = layout;
        
        if (self.superview) {
            [self setLayout:layout withSuperview:self.superview];
        }
        
    }else{
        
        
        CGRect frame = self.frame;
        frame.origin.x = right - frame.size.width;
        self.frame = frame;
    }
    
}

- (CGFloat)top {
    return self.frame.origin.y;
}


- (void)setTop:(CGFloat)top {
    
    if (self.isLayoutView) {
        
        BNLayout layout = self.layout;
        UIEdgeInsets edge = layout.edge;
        edge.top = top;
        layout.edge = edge;
        self.layout = layout;
        
        if (self.superview) {
            [self setLayout:layout withSuperview:self.superview];
        }
        
    }else{
    
        CGRect frame = self.frame;
        frame.origin.y = top;
        self.frame = frame;
    }
}


- (CGFloat)bottom {
    return self.frame.origin.y + self.frame.size.height;
}


- (void)setBottom:(CGFloat)bottom {
    
    if (self.isLayoutView) {
        
        BNLayout layout = self.layout;
        UIEdgeInsets edge = layout.edge;
        edge.bottom = bottom;
        layout.edge = edge;
        self.layout = layout;
        
        if (self.superview) {
            [self setLayout:layout withSuperview:self.superview];
        }
        
    }else{
        
        CGRect frame = self.frame;
        frame.origin.y = bottom - frame.size.height;
        self.frame = frame;
    }
}


- (CGFloat)height {
    return self.frame.size.height;
}



- (void)setHeight:(CGFloat)height {
    
    if (self.isLayoutView) {
        
        BNLayout layout = self.layout;
        CGSize size = layout.size;
        size.height = height;
        layout.size = size;
        self.layout = layout;
        
        if (self.superview) {
            [self setLayout:layout withSuperview:self.superview];
        }
        
    }else{
        
        CGRect frame = self.frame;
        frame.size.height = height;
        self.frame = frame;
    }
}


@end
