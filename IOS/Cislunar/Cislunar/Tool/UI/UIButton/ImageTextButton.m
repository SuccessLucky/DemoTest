//
//  ImageTextButton.m
//  ButtonMul
//
//  Created by Jonren on 15/12/29.
//  Copyright © 2015年 Jonren. All rights reserved.
//

#import "ImageTextButton.h"

@implementation ImageTextButton

- (instancetype)initWithFrame:(CGRect)frame image:(UIImage *)img title:(NSString *)title {
    self = [super initWithFrame:frame];
    if (self) {
        [self setImage:img forState:UIControlStateNormal];
        [self setTitle:title forState:UIControlStateNormal];
        
        self.backgroundColor = [UIColor whiteColor];
        self.titleLabel.font = [UIFont systemFontOfSize:15];
        [self setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
        
        //default Alignment is in order to facilitate the layout
        [self setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
        [self setContentVerticalAlignment:UIControlContentVerticalAlignmentTop];
        
        self.imgTextDistance = 5;
    }
    return self;
    
    
}

- (void)setButtonTitleWithImageAlignment:(UIButtonTitleWithImageAlignment)buttonTitleWithImageAlignment {
    _buttonTitleWithImageAlignment = buttonTitleWithImageAlignment;
    [self alignmentValueChanged];
}

- (void)alignmentValueChanged {
    CGFloat buttonWidth = self.frame.size.width;
    CGFloat buttonHeight = self.frame.size.height;
    CGFloat imgWidth = self.imageView.image.size.width;
    CGFloat imgHeight = self.imageView.image.size.height;
    CGSize textSize = [self.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:self.titleLabel.font}];
    CGFloat textWitdh = textSize.width;
    CGFloat textHeight = textSize.height;
    
    CGFloat interval;      // distance between the whole image title part and button
    CGFloat imgOffsetX;    // horizontal offset of image
    CGFloat imgOffsetY;    // vertical offset of image
    CGFloat titleOffsetX;  // horizontal offset of title
    CGFloat titleOffsetY;  // vertical offset of title
    
    if (_buttonTitleWithImageAlignment == UIButtonTitleWithImageAlignmentUp) {
        interval = (buttonHeight - (imgHeight + _imgTextDistance + textHeight)) / 2;
        imgOffsetX = (buttonWidth - imgWidth) / 2;
        imgOffsetY = interval;
        titleOffsetX = (buttonWidth - textWitdh) / 2 - imgWidth;
        titleOffsetY = interval + imgHeight + _imgTextDistance;
    }else if (_buttonTitleWithImageAlignment == UIButtonTitleWithImageAlignmentLeft) {
        interval = (buttonWidth - (imgWidth + _imgTextDistance + textWitdh)) / 2;
        imgOffsetX = interval;
        imgOffsetY = (buttonHeight - imgHeight) / 2;
        titleOffsetX = buttonWidth - (imgWidth + textWitdh + interval);
        titleOffsetY = (buttonHeight - textHeight) / 2;
    }else if (_buttonTitleWithImageAlignment == UIButtonTitleWithImageAlignmentDown) {
        interval = (buttonHeight - (imgHeight + _imgTextDistance + textHeight)) / 2;
        imgOffsetX = (buttonWidth - imgWidth) / 2;
        imgOffsetY = interval + textHeight + _imgTextDistance;
        titleOffsetX = (buttonWidth - textWitdh) / 2 - imgWidth;
        titleOffsetY = interval;
    }else {
        interval = (buttonWidth - (imgWidth + _imgTextDistance + textWitdh)) / 2;
        imgOffsetX = interval + textWitdh + _imgTextDistance;
        imgOffsetY = (buttonHeight - imgHeight) / 2;
        titleOffsetX = - (imgWidth - interval);
        titleOffsetY = (buttonHeight - textHeight) / 2;
    }
    [self setImageEdgeInsets:UIEdgeInsetsMake(imgOffsetY, imgOffsetX, 0, 0)];
    [self setTitleEdgeInsets:UIEdgeInsetsMake(titleOffsetY, titleOffsetX, 0, 0)];
}

/*
 
 - (void)viewDidLoad {
 [super viewDidLoad];
 // Do any additional setup after loading the view, typically from a nib.
 
 self.view.backgroundColor = [UIColor lightGrayColor];
 
 

 / 调用的时候只需要如下代码即可实现
 / ImageTextButton继承于UIButton，所以UIButton的所有方法可以同样适用
 / imgTextButton点击事件测试即可秒懂
 */
 /*
self.imgTextButton = [[ImageTextButton alloc] initWithFrame:CGRectMake((self.view.frame.size.width - 150) / 2, 200, 150, 100)
                                                      image:[UIImage imageNamed:@"buttonImg.png"]
                                                      title:@"点我"];
//    self.imgTextButton.imgTextDistance = 10; // 可修改图片标题的间距，默认为5
self.imgTextButton.buttonTitleWithImageAlignment = UIButtonTitleWithImageAlignmentUp;
[self.imgTextButton addTarget:self action:@selector(changeValue:) forControlEvents:UIControlEventTouchUpInside];
[self.view addSubview:self.imgTextButton];
}

- (void)changeValue:(UIButton *)sender {
    sender.tag = self.imgTextButton.buttonTitleWithImageAlignment;
    switch (sender.tag) {
        case UIButtonTitleWithImageAlignmentUp: {
            [self.imgTextButton setImage:[UIImage imageNamed:@"buttonImg_1.png"] forState:UIControlStateNormal];
            [self.imgTextButton setTitle:@"再点我" forState:UIControlStateNormal];
            [self.imgTextButton setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
            [self.imgTextButton setButtonTitleWithImageAlignment:UIButtonTitleWithImageAlignmentLeft];
        }
            break;
        case UIButtonTitleWithImageAlignmentLeft: {
            [self.imgTextButton setImage:[UIImage imageNamed:@"buttonImg.png"] forState:UIControlStateNormal];
            [self.imgTextButton setTitle:@"接着点我" forState:UIControlStateNormal];
            [self.imgTextButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
            [self.imgTextButton setButtonTitleWithImageAlignment:UIButtonTitleWithImageAlignmentDown];
        }
            break;
        case UIButtonTitleWithImageAlignmentDown: {
            [self.imgTextButton setImage:[UIImage imageNamed:@"buttonImg_1.png"] forState:UIControlStateNormal];
            [self.imgTextButton setTitle:@"请继续点我" forState:UIControlStateNormal];
            [self.imgTextButton setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
            [self.imgTextButton setButtonTitleWithImageAlignment:UIButtonTitleWithImageAlignmentRight];
        }
            break;
        case UIButtonTitleWithImageAlignmentRight: {
            [self.imgTextButton setImage:[UIImage imageNamed:@"buttonImg.png"] forState:UIControlStateNormal];
            [self.imgTextButton setTitle:@"变回来了" forState:UIControlStateNormal];
            [self.imgTextButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
            [self.imgTextButton setButtonTitleWithImageAlignment:UIButtonTitleWithImageAlignmentUp];
        }
            break;
        default:
            break;
    }
}


 */


@end
