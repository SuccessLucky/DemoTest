//
//  AirconditionAlterView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/16.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "AirconditionAlterView.h"
#import "AirConditionAlterTableView.h"


@interface AirconditionAlterView ()<UIGestureRecognizerDelegate>

@property (strong, nonatomic)NSArray *arrList;
@property (strong, nonatomic) NSString *strTitle;
@property (strong, nonatomic) AirConditionAlterTableView *tableView;
@property (assign, nonatomic) int iViewHeight;

@end


@implementation AirconditionAlterView

- (instancetype)initWithFrame:(CGRect)frame arrList:(NSArray *)arrList title:(NSString *)title
{
    self = [super initWithFrame:frame];
    if (self) {
        
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(doTapBackView)];
        [self addGestureRecognizer:tap];
        tap.delegate =self;
        self.backgroundColor = [UIColor colorWithWhite:0.0 alpha:0.5];
        
        
        self.iViewHeight = ((int)arrList.count*45 + 40);
        
        [self doAddSubViews:arrList title:title];
        self.arrList = arrList;
        self.strTitle = title;
        [self doAddAction];
    }
    return self;
}

- (void)doAddSubViews:(NSArray *)arrList title:(NSString *)title
{
    self.tableView = [[AirConditionAlterTableView alloc] initWithFrame:CGRectMake(0,UI_SCREEN_HEIGHT,UI_SCREEN_WIDTH,self.iViewHeight)
                                                            style:UITableViewStylePlain title:title];
    self.tableView.arrList =arrList;
    [self addSubview:self.tableView];
    
    [UIView animateWithDuration:0.25 animations:^{
//        self.tableView.transform = CGAffineTransformTranslate(self.tableView.transform, 0, -self.iViewHeight);
        self.tableView.frame = CGRectMake(0,UI_SCREEN_HEIGHT - self.iViewHeight,UI_SCREEN_WIDTH,self.iViewHeight);
    }];
}

- (void)doAddAction
{
    @weakify(self);
    [self.tableView setBlockTableViewDidSelected:^(AirConditionBtnModel* model) {
        @strongify(self);
        if (self.BlockGetTypeCompleteHandle) {
            self.BlockGetTypeCompleteHandle(model);
        }
        [self dismiss];
    }];
    
    [self.tableView setBlockPressCancellBtn:^{
        @strongify(self);
        [self dismiss];
    }];
    
}


- (void)doTapBackView
{
    [self dismiss];
}

- (void)dismiss
{
    [UIView animateWithDuration:0.25 animations:^{
//        self.tableView.transform = CGAffineTransformTranslate(self.tableView.transform, 0,self.iViewHeight);
        self.tableView.frame = CGRectMake(0,UI_SCREEN_HEIGHT,UI_SCREEN_WIDTH,self.iViewHeight);
    } completion:^(BOOL finished) {
        [self.tableView removeFromSuperview];
        [self removeFromSuperview];
        
    }];
}

- (void)show
{
//    [UIView animateWithDuration:0.25 animations:^{
//        self.tableView.transform = CGAffineTransformTranslate(self.tableView.transform, 0, -kViewHeight);
//    }];
}

#pragma mark - UIGestureRecognizerDelegate
- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{
    // 输出点击的view的类名
    NSLog(@"%@", NSStringFromClass([touch.view class]));
    
    // 若为UITableViewCellContentView（即点击了tableViewCell），则不截获Touch事件
    if ([NSStringFromClass([touch.view class]) isEqualToString:@"UITableViewCellContentView"]) {
        return NO;
    }
    return  YES;
}


@end
