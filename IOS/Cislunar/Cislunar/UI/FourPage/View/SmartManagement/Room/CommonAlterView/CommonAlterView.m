//
//  CommonAlterView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/26.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "CommonAlterView.h"
#import "CommonAlterHeaderView.h"
#import "CommonAlterFooterView.h"
#import "CommonHeaderCollectionReusableView.h"
#import "CommonAlterCollectionViewCell.h"

#define kItemWidth 32
#define kItemHeight 32
#define kGapWith (collectionViewWidth - 32 * 4 - 20 *2)/3.00


@interface CommonAlterView ()<UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout>

//背景
@property (nonatomic, strong) UIView *backgroundView;

@property (nonatomic, strong) UIView *contentView;

//标题
@property (nonatomic, strong) CommonAlterHeaderView *titleView;

@property (nonatomic, strong) CommonAlterFooterView *footer;

@property (nonatomic, strong) UICollectionView *collectionView;

@property (nonatomic, strong) NSArray *arrPic;

@property (nonatomic, strong) SHUIPicModel *picModel;

@property (nonatomic, assign) CommonAlterViewType viewType;

@end

@implementation CommonAlterView

- (instancetype)initWithFrame:(CGRect)frame dataSourceArr:(NSArray *)arrPicList  type:(CommonAlterViewType) type {
    self = [super initWithFrame:frame];
    if (self) {
        //初始化各种控件
        self.viewType = type;
        _backgroundView = [[UIView alloc] initWithFrame:frame];
        _backgroundView.backgroundColor = [UIColor lightGrayColor];
        [self addSubview:_backgroundView];
        [self doAddSubViews];
        self.arrPic = arrPicList;
        
    }
    return self;
}

- (void)doAddSubViews
{
//    NSLog(@"%d",collectionViewHeight);
//    NSLog(@"%f",UI_SCREEN_HEIGHT);
    CGFloat topY;
    
    topY = (UI_SCREEN_HEIGHT - collectionViewHeight) / 2;
    
    _contentView = [[UIView alloc] initWithFrame:CGRectMake(15, topY, UI_SCREEN_WIDTH - 30, collectionViewHeight)];
    _contentView.backgroundColor = [UIColor whiteColor];
    _contentView.layer.masksToBounds = YES;
    _contentView.layer.cornerRadius = 5;
    [self addSubview:_contentView];
    
    _titleView = [[CommonAlterHeaderView alloc] init];
    _titleView.frame = CGRectMake(0, 0, UI_SCREEN_WIDTH - 30, 90);
    [self.contentView addSubview:_titleView];
    
    
    [self doAddCollectionView];
    
    _footer = [[CommonAlterFooterView alloc] init];
    _footer.frame = CGRectMake(0, collectionViewHeight - 60, UI_SCREEN_WIDTH - 30, 60);
    @weakify(self);
    _footer.blockBtnPressed = ^(CommonAlterBtnType type) {
        @strongify(self);
        if (type == CommonAlterBtnType_Cancell) {
            [self dismiss:nil];
            if (self.BlockDimis) {
                self.BlockDimis();
            }
            
        }else{
            if ([self isShouldAddItem]) {
                [self dismiss:nil];
                if (self.BlockGetNameAndPic) {
                    self.BlockGetNameAndPic(self.titleView.textFieldName.text,self.picModel);
                }
            }
            
        }
    };
    [self.contentView addSubview:_footer];

}

- (BOOL)isShouldAddItem
{
    if (self.titleView.textFieldName.text.length <= 0) {
       [XWHUDManager showWarningTipHUD:@"请输入名称"];
        return  NO;
    }else if (!self.picModel) {
        [XWHUDManager showWarningTipHUD:@"请选择图片"];
        return NO;
    }else{
        return YES;
    }
    
}

- (void)setArrPic:(NSArray *)arrPic
{
    _arrPic = arrPic;
    [self.collectionView reloadData];
}

#pragma mark -
#pragma mark - collectionView
- (void)doAddCollectionView
{
    UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
    flowLayout.sectionHeadersPinToVisibleBounds = YES;
    [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
    self.collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0,90, collectionViewWidth, collectionViewHeight - 150)
                                                       collectionViewLayout:flowLayout];
    
    self.collectionView.delegate = self;
    self.collectionView.dataSource = self;
    self.collectionView.backgroundColor = [UIColor whiteColor];
    self.collectionView.scrollEnabled = YES;
    
    self.collectionView.showsHorizontalScrollIndicator = NO;
    
    UINib *nib = [UINib nibWithNibName:@"CommonAlterCollectionViewCell" bundle:[NSBundle mainBundle]];
    [self.collectionView registerNib:nib forCellWithReuseIdentifier:@"CommonAlterCollectionViewCell"];
    
    [self.collectionView registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"header"];
    [self.collectionView registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:@"footer"];
    [self.contentView addSubview:self.collectionView];
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.arrPic.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identify = @"CommonAlterCollectionViewCell";
    CommonAlterCollectionViewCell *cell = (CommonAlterCollectionViewCell *)[collectionView dequeueReusableCellWithReuseIdentifier:identify forIndexPath:indexPath];
    SHUIPicModel *model = self.arrPic[indexPath.row];
    
    if (self.viewType == CommonAlterViewType_Common) {
        if (self.picModel.iUIPic_id == model.iUIPic_id) {
            cell.btnSelected.selected = YES;
        }else{
            cell.btnSelected.selected = NO;
        }
        
        NSString *strCommonUrl = [[SHAppCommonRequest shareInstance] doGetRoomCommonUIPicWithPicName:model.strUIPic_name];
        [cell.btnIcon sd_setImageWithURL:[NSURL URLWithString:strCommonUrl] forState:UIControlStateNormal];
        
    }else{
        if (self.picModel.iUIPic_id == model.iUIPic_id) {
             cell.btnSelected.selected = YES;
        }else{
            cell.btnSelected.selected = NO;
        }
        NSString *strPrImage = [NSString stringWithFormat:@"%@Pr_%@@2x.%@",model.strUIPic_base_url,model.strUIPic_name,model.strUIPic_image_type];
        NSString *strUnImage = [NSString stringWithFormat:@"%@Un_%@@2x.%@",model.strUIPic_base_url,model.strUIPic_name,model.strUIPic_image_type];
        [cell.btnIcon sd_setImageWithURL:[NSURL URLWithString:strUnImage] forState:UIControlStateNormal];
        [cell.btnIcon sd_setImageWithURL:[NSURL URLWithString:strPrImage] forState:UIControlStateSelected];
        
    }
    return cell;
}

- (UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath
{
    UICollectionReusableView *reusableView = nil;
    if (kind == UICollectionElementKindSectionHeader) {
        
        UICollectionReusableView *footer = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"header" forIndexPath:indexPath];
        reusableView = footer;
    }else {
        
        UICollectionReusableView *footer = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:@"footer" forIndexPath:indexPath];
        reusableView = footer;
    }
    return reusableView;
}

#pragma mark - UICollectionViewDelegateFlowLayout
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    return CGSizeMake(kItemWidth, kItemHeight);
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
    return UIEdgeInsetsMake(10, 20, 0, 20);
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout referenceSizeForHeaderInSection:(NSInteger)section
{
    return CGSizeMake(UI_SCREEN_WIDTH, 0.001);
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout referenceSizeForFooterInSection:(NSInteger)section
{
    return CGSizeMake(UI_SCREEN_WIDTH, 0.001);
}

//纵向间距设定
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section
{
    return kGapWith;
}

//上下间距设定
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
    return 26.0f;
}

#pragma mark -UICollectionViewDelegate
- (BOOL)collectionView:(UICollectionView *)collectionView shouldSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    [collectionView deselectItemAtIndexPath:indexPath animated:YES];
    
    SHUIPicModel *model = self.arrPic[indexPath.row];
    self.picModel = model;
    [self.collectionView reloadData];
}

#pragma mark pop和dismiss

- (void)popWithFatherView:(UIView *)view {
//    UIWindow *keyWindow = [[UIApplication sharedApplication] keyWindow];
//    [keyWindow addSubview:self];
    [view addSubview:self];
    
    
    //动画效果入场
    self.contentView.transform = CGAffineTransformMakeScale(0.2, 0.2);
    self.contentView.alpha = 0;
    [UIView animateWithDuration:.35 animations:^{
        self.contentView.transform = CGAffineTransformMakeScale(1.0, 1.0);
        self.contentView.alpha = 1;
    }];
}

- (void)dismiss:(BlockGetObject)blockObject {
    //动画效果出场
    [UIView animateWithDuration:.35 animations:^{
        self.contentView.transform = CGAffineTransformMakeScale(0.2, 0.2);
        self.contentView.alpha = 0;
    } completion:^(BOOL finished) {
        if (finished) {
            [self removeFromSuperview];
        }
    }];
}


- (void)setStrDefaultTitle:(NSString *)strDefaultTitle
{
    _titleView.labelTitle.text = strDefaultTitle;
}

//- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
//    UITouch *touch = [touches anyObject];
//    if (![touch.view isEqual:self.contentView] && ![touch.view isEqual:self.titleView] && ![touch.view isEqual:self.footer]) {
//        [self dismiss:nil];
//    }
//}




@end
