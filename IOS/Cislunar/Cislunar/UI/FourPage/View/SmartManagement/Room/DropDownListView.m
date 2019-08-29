//
//  DropDownListView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/13.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "DropDownListView.h"
#import "DropDownListTableView.h"

@interface DropDownListView ()

@property (strong, nonatomic)NSArray *arrList;
@property (strong, nonatomic) DropDownListTableView *tableView;

@end

@implementation DropDownListView

- (instancetype)initWithFrame:(CGRect)frame arrList:(NSArray *)arrList
{
    self = [super initWithFrame:frame];
    if (self) {
        
//        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(doTapBackView)];
//        [self addGestureRecognizer:tap];
        self.backgroundColor = [UIColor colorWithWhite:0.0 alpha:0.5];
        
        
        [self doAddSubViews:arrList];
        self.arrList = arrList;
        [self doAddAction];
        
        
    }
    return self;
}

- (void)doAddSubViews:(NSArray *)arrList
{
    self.tableView = [[DropDownListTableView alloc] initWithFrame:CGRectMake(0,0,UI_SCREEN_WIDTH,0)
                                                        style:UITableViewStylePlain];
    self.tableView.arrList =arrList;
    [self addSubview:self.tableView];
}

- (void)doAddAction
{
    @weakify(self);
    [self.tableView setBlockTableViewDidSelected:^(NSIndexPath *indexPath) {
        @strongify(self);
        if (self.BlockGetObjectCompleteHandle) {
            self.BlockGetObjectCompleteHandle(self.arrList[indexPath.row]);
        }
        
        if (self.BlockTapOnTheBackgroundView) {
            self.BlockTapOnTheBackgroundView();
        }
    }];
    
}


- (void)doTapBackView
{
    if (self.BlockTapOnTheBackgroundView) {
        self.BlockTapOnTheBackgroundView();
    }
    [self dismiss];
}

- (void)dismiss
{
    [UIView animateWithDuration:0.25 animations:^{
        self.tableView.frame = CGRectMake(0,0,UI_SCREEN_WIDTH,0);
    } completion:^(BOOL finished) {
        [self.tableView removeFromSuperview];
        [self removeFromSuperview];
        
    }];
}

- (void)show
{
    [UIView animateWithDuration:0.25 animations:^{
        
        if (44*self.arrList.count > UI_SCREEN_HEIGHT - 64 - 49) {
            
            self.tableView.frame = CGRectMake(0,0,UI_SCREEN_WIDTH,UI_SCREEN_HEIGHT - 64 - 49);
        } else {
            
            self.tableView.frame = CGRectMake(0,0,UI_SCREEN_WIDTH,44 * self.arrList.count);
        }
        
    }];
}



@end
