//
//  GNavigationTitleView.m
//  GJJManager
//
//  Created by yangpei on 15/5/4.
//  Copyright (c) 2015年 过家家. All rights reserved.
//

#import "GNavigationTitleView.h"

@interface GNavigationTitleView ()

@property (nonatomic, strong) UILabel *textLabel;
@property (nonatomic, strong) UILabel *promptLabel;
@end

@implementation GNavigationTitleView

- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame]) {
        [self initSubviews];
    }
    return self;
}

- (void)initSubviews
{
    [self addSubview:self.textLabel];
    [self addTextLabelConstraints];
    
    [self addSubview:self.promptLabel];
    [self addPromptLabelConstraints];
}

- (void)updateConstraints
{
    @weakify(self);
    [self.textLabel mas_updateConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
       
        if (self.prompt.length > 0) {
            make.height.equalTo(self.textLabel.superview.mas_height).with.offset(-18);
        } else {
            make.height.equalTo(self.textLabel.superview.mas_height);
        }
    }];
    [super updateConstraints];
}

- (void)setText:(NSString *)text
{
    _text = text;
    self.textLabel.text = text;
}

- (void)setPrompt:(NSString *)prompt
{
    _prompt = prompt;
    
    self.promptLabel.text = prompt;
    [self setNeedsUpdateConstraints];
}

- (UILabel *)textLabel
{
    if (!_textLabel) {
        _textLabel = [[UILabel alloc] initWithFrame:self.bounds];
        _textLabel.textAlignment = NSTextAlignmentCenter;
        _textLabel.textColor = [UIColor whiteColor];
        _textLabel.font = [UIFont boldSystemFontOfSize:17];
    }
    return _textLabel;
}

- (void)addTextLabelConstraints
{
    @weakify(self);
    [self.textLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.top.equalTo(self.mas_top);
        make.right.equalTo(self.mas_right);
        make.left.equalTo(self.mas_left);
        make.height.equalTo(self.textLabel.superview.mas_height);
    }];
}

- (UILabel *)promptLabel
{
    if (!_promptLabel) {
        _promptLabel = [[UILabel alloc] init];
        _promptLabel.textAlignment = NSTextAlignmentCenter;
        _promptLabel.textColor = [UIColor whiteColor];
        _promptLabel.font = [UIFont systemFontOfSize:11];
    }
    return _promptLabel;
}

- (void)addPromptLabelConstraints
{
    @weakify(self);
    [self.promptLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.top.equalTo(self.textLabel.mas_bottom);
        make.left.equalTo(self.textLabel.mas_left);
        make.right.equalTo(self.textLabel.mas_right);
        make.height.equalTo(@(11));
    }];
}
@end
