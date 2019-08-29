/*
 By: Justin Meiners
 
 Copyright (c) 2015 Justin Meiners
 Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */

#import <UIKit/UIKit.h>

typedef struct
{
    unsigned char r;
    unsigned char g;
    unsigned char b;
} ISColorWheelPixelRGB;

@class ISColorWheel;


@protocol ISColorWheelDelegate <NSObject>
@required
- (void)colorWheelDidChangeColor:(ISColorWheel*)colorWheel currentPixel:(ISColorWheelPixelRGB)currentPixel;
@end


@interface ISColorWheel : UIControl


@property(nonatomic, weak) IBOutlet id <ISColorWheelDelegate> delegate;
@property(nonatomic, assign)CGSize knobSize;
@property(nonatomic, strong)UIView* knobView;
@property(nonatomic, assign)CGFloat brightness;
@property(nonatomic, assign)BOOL continuous;
@property(nonatomic, strong)UIColor* borderColor;
@property(nonatomic, assign)CGFloat borderWidth;
@property(nonatomic, strong)UIColor* currentColor;
@property(nonatomic, assign)ISColorWheelPixelRGB currentPixel;

- (void)updateImage;
- (void)setTouchPoint:(CGPoint)point;



@end
