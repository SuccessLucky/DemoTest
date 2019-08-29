//
//  ScreenIconView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/9.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "ScreenIconView.h"
#import "ScreenIconCollectionViewCell.h"
#define kViewHeight 160


@interface ScreenIconView ()<UICollectionViewDelegate, UICollectionViewDataSource>

@property (nonatomic, strong) UICollectionView *collectionView;
@property (nonatomic, assign) CGFloat width;
@property (strong, nonatomic) UIView *bgView;
@property (strong, nonatomic) NSArray *arrIconList;

@end

@implementation ScreenIconView

- (instancetype)initWithFrame:(CGRect)frame {
    
    self = [super initWithFrame:frame];
    if (self) {
        
        self.frame = frame;
        self.arrIconList = [NSArray new];
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleDismiss)];
        [self addGestureRecognizer:tap];
    }
    
    return self;
}

- (void)handleDismiss
{
    [self dismiss];
}

- (void)show:(NSArray *)arrIconList
{
    self.backgroundColor = [[UIColor alloc] initWithRed:0 green:0 blue:0 alpha:.25];
    [[UIApplication sharedApplication].keyWindow addSubview:self];
    [self doInitBgView:arrIconList];
    [UIView animateWithDuration:0.25 animations:^{
        self.bgView.transform = CGAffineTransformTranslate(self.bgView.transform, 0, -kViewHeight);
    }];
}

- (void)dismiss
{
    [UIView animateWithDuration:0.25 animations:^{
        self.bgView.transform = CGAffineTransformTranslate(self.bgView.transform, 0,kViewHeight);
    } completion:^(BOOL finished) {
        [self.bgView removeFromSuperview];
        [self removeFromSuperview];
        
    }];
}


#pragma mark -
#pragma mark - bgView
- (void)doInitBgView:(NSArray *)arrIconList
{
    self.bgView = [[UIView alloc] initWithFrame:CGRectMake(0, UI_SCREEN_HEIGHT, UI_SCREEN_WIDTH, kViewHeight)];
    self.bgView.backgroundColor = [UIColor whiteColor];
    [[UIApplication sharedApplication].keyWindow addSubview:self.bgView];
    
    UIButton *canlcelBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [canlcelBtn setTitleColor:[UIColor blueColor] forState:UIControlStateNormal];
    [canlcelBtn setFrame:CGRectMake(0, 0, 60, 40)];
    [canlcelBtn setTitleColor:kCommonColor forState:UIControlStateNormal];
    [canlcelBtn setTitle:@"取消" forState:UIControlStateNormal];
    [canlcelBtn addTarget:self action:@selector(handleDismiss) forControlEvents:UIControlEventTouchUpInside];
    [self.bgView addSubview:canlcelBtn];
    
    UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake((UI_SCREEN_WIDTH - 100) / 2, 0, 100, 40)];
    titleLabel.text = @"选择场景";
    titleLabel.textAlignment = NSTextAlignmentCenter;
    [self.bgView addSubview:titleLabel];
    
    UILabel *line = [[UILabel alloc] initWithFrame:CGRectMake(0, 39, UI_SCREEN_WIDTH, .5)];
    line.backgroundColor = [UIColor grayColor];
    [self.bgView addSubview:line];
    
    [self.bgView addSubview:self.collectionView];
    self.arrIconList = arrIconList;
}

#pragma mark -
#pragma mark - UICollectionView

- (UICollectionView *)collectionView {
    
    if (!_collectionView) {
        
        UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
        layout.itemSize = CGSizeMake(120, 120);
        layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
        
        _collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 40, UI_SCREEN_WIDTH, 120) collectionViewLayout:layout];
        _collectionView.alwaysBounceVertical = NO;
        _collectionView.alwaysBounceHorizontal = YES;
        _collectionView.dataSource = self;
        _collectionView.delegate = self;
        _collectionView.showsHorizontalScrollIndicator = NO;
        _collectionView.backgroundColor = [UIColor whiteColor];
        [_collectionView registerNib:[UINib nibWithNibName:NSStringFromClass([ScreenIconCollectionViewCell class]) bundle:nil]
          forCellWithReuseIdentifier:NSStringFromClass([ScreenIconCollectionViewCell class])];
    }
    
    return _collectionView;
}

#pragma mark UICollectionView

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    
    return self.arrIconList.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    
    ScreenIconCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:NSStringFromClass([ScreenIconCollectionViewCell class]) forIndexPath:indexPath];
    
    @weakify(self);
    [cell setBlockBtnIconPressed:^{
        @strongify(self);
        [self dismiss];
        if (self.blockCompleteHandle) {
            self.blockCompleteHandle(indexPath);
        }
    }];
    
    SHUIPicModel *model = self.arrIconList[indexPath.row];
    NSString *strPrImage = [NSString stringWithFormat:@"%@Un_%@@2x.%@",model.strUIPic_base_url,model.strUIPic_name,model.strUIPic_image_type];
    NSString *strUnImage = [NSString stringWithFormat:@"%@Pr_%@@2x.%@",model.strUIPic_base_url,model.strUIPic_name,model.strUIPic_image_type];
    [cell.btnIcon sd_setImageWithURL:[NSURL URLWithString:strPrImage] forState:UIControlStateNormal];
    [cell.btnIcon sd_setImageWithURL:[NSURL URLWithString:strUnImage] forState:UIControlStateSelected];
        
    return cell;
}


//- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
//{
//    ScreenIconCollectionViewCell *cell = (ScreenIconCollectionViewCell *)[collectionView cellForItemAtIndexPath:indexPath];
//    cell.btnIcon.highlighted = YES;
//    
//    if (self.blockCompleteHandle) {
//        self.blockCompleteHandle(indexPath);
//    }
//}

@end
