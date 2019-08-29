//
//  AlarmHistoryHeaderView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/11.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "AlarmHistoryHeaderView.h"
#import "AlarmHeaderCollectionView.h"

@interface AlarmHistoryHeaderView ()

@property (strong, nonatomic)UILabel *labelTitle;
@property (strong, nonatomic)AlarmHeaderCollectionView *collectionView;

@end

@implementation AlarmHistoryHeaderView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = kCommonColor;
        [self doInitSubViews];
        [self doAddAction];
        [self doLoadData];
        
    }
    return self;
}

- (void)setStrTime:(NSString *)strTime
{
    self.labelTitle.text = strTime;
}

- (void)doInitSubViews
{
    [self addSubview:self.labelTitle];
    [self doAddLabelTitleConstraints];
    
    [self addSubview:self.collectionView];
    [self doAddCollectionViewConstraints];
}

- (void)doAddAction
{
    @weakify(self);
    [self.collectionView setBlockTimeCollectionItemSelected:^(NSDate *date, AlarmHistoryCollectionViewCell *item, NSIndexPath *indexPath) {
        @strongify(self);
        if (indexPath.row == 4) {
            if (self.BlockMoreDateSelected) {
                self.BlockMoreDateSelected();
            }
        }else{
            if (self.BlockDateSelected) {
                self.BlockDateSelected(date);
            }
        }
    }];
}

- (void)doLoadData
{
    NSMutableArray *mutArr = [NSMutableArray new];
    for (int i = 1; i < 6; i ++) {
        NSDate *strDate = [ToolCommon doGetDateNDay:-i];
        [mutArr addObject:strDate];
    }
    self.labelTitle.text = [self getCurrentDate];
    self.collectionView.arrList = mutArr;
}

-(NSString *)getCurrentDate{
    
    NSDateFormatter *date_formatter = [[NSDateFormatter alloc] init];
    [date_formatter setDateFormat:@"yyyy-MM-dd"];
    NSString *current_date_str = [date_formatter stringFromDate:[NSDate date]];
    return current_date_str;
}

#pragma mark-
#pragma mark - init
- (UILabel *)labelTitle
{
    if (!_labelTitle) {
        _labelTitle = [[UILabel alloc] init];
        _labelTitle.backgroundColor = [UIColor clearColor];
        _labelTitle.textColor = [UIColor whiteColor];
    }
    return _labelTitle;
}

- (void)doAddLabelTitleConstraints
{
    @weakify(self);
    [self.labelTitle mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.top.equalTo(self.mas_top).with.offset(15);
        make.left.equalTo(self.mas_left).with.offset(15);
        make.right.equalTo(self.mas_right).with.offset(15);
        make.height.equalTo(@12);
    }];
}

-(AlarmHeaderCollectionView *)collectionView
{
    if (!_collectionView) {
        UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
        flowLayout.sectionHeadersPinToVisibleBounds = YES;
        [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
        _collectionView = [[AlarmHeaderCollectionView alloc] initWithFrame:CGRectMake(0,64, UI_SCREEN_WIDTH, 0)
                                                       collectionViewLayout:flowLayout];
    }
    return _collectionView;
}

- (void)doAddCollectionViewConstraints
{
    @weakify(self);
    [self.collectionView mas_makeConstraints:^(MASConstraintMaker *make) {
        @strongify(self);
        make.top.equalTo(self.labelTitle.mas_top).with.offset(10);
        make.left.equalTo(self.mas_left);
        make.right.equalTo(self.mas_right);
        make.bottom.equalTo(self.mas_bottom);
    }];
}




@end
