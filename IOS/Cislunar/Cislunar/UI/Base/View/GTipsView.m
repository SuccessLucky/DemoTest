//
//  GTipsView.m
//  GJJManager
//
//  Created by yangpei on 15/5/4.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#import "GTipsView.h"

@interface GTipsView()

@property (nonatomic, strong) UIImageView *imageView;
@property (nonatomic, strong) UILabel *tipsLabel;
@end

@implementation GTipsView

- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame]) {
        [self initSubviews];
    }
    return self;
}

- (instancetype)initWithTitle:(NSString *)title image:(UIImage *)image
{
    if (self = [super initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, 131)]) {
        self.imageView.image = image;
        self.tipsLabel.text = title;
    }
    return self;
}
- (void)initSubviews
{
    [self addSubview:self.imageView];
    [self addImageViewConstraints];
    
    [self addSubview:self.tipsLabel];
    [self addTipsLabelConstraints];
}

- (void)setTitle:(NSString *)title
{
    self.tipsLabel.text = title;
}

- (void)setImage:(UIImage *)image
{
    self.imageView.image = image;
}

- (UIImageView *)imageView
{
    if (!_imageView) {
        _imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 105, 105)];
    }
    return _imageView;
}

- (void)addImageViewConstraints
{
    @weakify(self);
    [self.imageView mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        
        make.top.equalTo(self.mas_top);
        make.width.equalTo(@(self.imageView.frame.size.width));
        make.height.equalTo(@(self.imageView.frame.size.height));
        make.centerX.equalTo(self.mas_centerX);
    }];
}

- (UILabel *)tipsLabel
{
    if (!_tipsLabel) {
        _tipsLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, UI_SCREEN_WIDTH, 16)];
        _tipsLabel.textAlignment = NSTextAlignmentCenter;
        _tipsLabel.textColor = kSubTitleLbaelColor;
        _tipsLabel.font = kDetailLabelFont;
    }
    return _tipsLabel;
}

- (void)addTipsLabelConstraints
{
    @weakify(self);
    [self.tipsLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        
        make.top.equalTo(@(CGRectGetMaxY(self.imageView.frame) + 8));
        make.width.equalTo(self.mas_width);
        make.height.equalTo(@(self.tipsLabel.frame.size.height));
        make.centerX.equalTo(self.mas_centerX);
    }];
}

- (void)setTouchBlock:(GTipsViewTouchBlock)touchBlock
{
    [self bk_whenTapped:^{
        if (touchBlock) touchBlock();
    }];
}
@end
