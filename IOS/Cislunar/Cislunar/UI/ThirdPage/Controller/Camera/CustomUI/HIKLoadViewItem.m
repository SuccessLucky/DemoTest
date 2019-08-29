//
//  HIKLoadViewItem.m
//  VideoGo
//
//  Created by zhil.shi on 15/3/9.
//  Copyright (c) 2015年 HIKVison. All rights reserved.
//

#import "HIKLoadViewItem.h"

@implementation HIKLoadViewItem
- (id)init
{
    if(self = [super init])
    {
        //todo
    }
    return self;
}


+ (instancetype)initWithImage:(UIImage*)image
{
    
    HIKLoadViewItem *item              = [[HIKLoadViewItem alloc]init];
    item.colorCirculeLayer             = [CALayer layer];
    item.colorCirculeLayer.contents    = (id)image.CGImage;
    item.colorCirculeLayer.frame       = CGRectMake(0.0, 0.0, 10, 10);
    return item;
}

- (void)setPosition:(CGPoint)position
{
    [self.colorCirculeLayer setPosition:position];
}

@end
