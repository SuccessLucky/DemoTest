//
//  GEConstructionInfoView.m
//  GJJERP
//
//  Created by 余长涛 on 16/1/13.
//  Copyright © 2016年 过家家. All rights reserved.
//

#import "SHCustomView.h"

@implementation SHCustomView

-(instancetype)initWithFrame:(CGRect)frame
                       image:(UIImage *)image
                       title:(NSString *)title
                titleFontSize:(CGFloat)titleFontSize
              picAndTitleGap:(CGFloat)fGap
{
    self = [super initWithFrame:frame];
    if (self) {
        self.frame = frame;
        CGFloat imageWidth = image.size.width;
        CGFloat imageHeight = image.size.height;
        CGFloat picGapTitleHeight = imageHeight + fGap + titleFontSize;
        UIFont *fontTitle = [UIFont systemFontOfSize:titleFontSize];
        UIImageView *imageV = [[UIImageView alloc] init];
        imageV.frame = CGRectMake(frame.size.width/2.0f - imageWidth/2.0f,
                                  (self.frame.size.height - picGapTitleHeight)/2.0f,
                                  imageWidth,
                                  imageHeight);
        imageV.image = image;
        [self addSubview:imageV];
        
        CGRect txtFrame = [title boundingRectWithSize:CGSizeMake(MAXFLOAT, titleFontSize)
                                              options:NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading
                                           attributes:[NSDictionary dictionaryWithObjectsAndKeys:fontTitle,NSFontAttributeName, nil]
                                              context:nil];
        UILabel *labelTitle = [UILabel new];
        labelTitle.frame = CGRectMake(frame.size.width/2.0f - txtFrame.size.width/2.0f,
                                      imageV.frame.origin.y + imageHeight + fGap,
                                      txtFrame.size.width,
                                      titleFontSize);
        labelTitle.textColor = UIColorFromRGB(0x202026);
        labelTitle.font = fontTitle;
        labelTitle.text = title;
        [self addSubview:labelTitle];
        
    }
    return self;
}
@end