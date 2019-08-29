//
//  GEConstructionInfoView.h
//  GJJERP
//
//  Created by 余长涛 on 16/1/13.
//  Copyright © 2016年 过家家. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SHCustomView : UIView

@property (strong , nonatomic) NSString *strTitle;
@property (strong , nonatomic) NSString *strImage;

-(instancetype)initWithFrame:(CGRect)frame
                       image:(UIImage *)image
                       title:(NSString *)title
               titleFontSize:(CGFloat)titleFontSize
              picAndTitleGap:(CGFloat)fGap;
@end
