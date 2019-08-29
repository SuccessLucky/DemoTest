//
//  UIViewController+EZBackPop.m
//  EZOpenSDKDemo
//
//  Created by DeJohn Dong on 15/11/2.
//  Copyright © 2015年 hikvision. All rights reserved.
//

#import "UIViewController+EZBackPop.h"
#import "Aspects.h"
#import <objc/runtime.h>

@implementation UIViewController (EZBackPop)

+ (void)load
{
    [UIViewController aspect_hookSelector:@selector(viewDidLoad)
                              withOptions:AspectPositionAfter
                               usingBlock:^(id<AspectInfo> info) {
                                   [[info instance] aspect_hookSelector:@selector(prepareForSegue:sender:)
                                                            withOptions:AspectPositionBefore
                                                             usingBlock:^(id<AspectInfo> aspectInfo){
                                                                 UIBarButtonItem *returnButtonItem = [[UIBarButtonItem alloc] init];
                                                                 returnButtonItem.title = @"";
                                                                 [(UIViewController *)[aspectInfo instance] navigationItem].backBarButtonItem = returnButtonItem;
                                                             }
                                                                  error:NULL];
                               }
                                    error:NULL];
}

- (BOOL)isAutorotate
{
    return [objc_getAssociatedObject(self, _cmd) boolValue];
}

- (void)setIsAutorotate:(BOOL)isAutorotate
{
    objc_setAssociatedObject(self, @selector(isAutorotate), @(isAutorotate), OBJC_ASSOCIATION_ASSIGN);
}

@end

