//
//  TVUICollectionView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/14.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "InfraredUICollectionView.h"
#import "InfraredCollectionViewCell.h"

#define kItemWidth (UI_SCREEN_WIDTH - 24 * 2 - 51 * 2)/3.00f
#define kItemHeight kItemWidth

@interface InfraredUICollectionView ()<UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout>

@end

@implementation InfraredUICollectionView

- (id)initWithFrame:(CGRect)frame collectionViewLayout:(UICollectionViewLayout *)layout
{
    self = [super initWithFrame:frame collectionViewLayout:layout];
    if (self) {
        self.frame = frame;
        self.delegate = self;
        self.dataSource = self;
        self.backgroundColor = [UIColor clearColor];
        self.scrollEnabled = YES;
        self.showsHorizontalScrollIndicator = NO;
        
        UINib *nib = [UINib nibWithNibName:@"InfraredCollectionViewCell" bundle:[NSBundle mainBundle]];
        [self registerNib:nib forCellWithReuseIdentifier:@"InfraredCollectionViewCell"];
        
        [self registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"header"];
        
        [self registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:@"footer"];
    }
    return self;
}

- (void)setArrList:(NSArray *)arrList
{
    _arrList = arrList;
//    [UIView animateWithDuration:0.25 animations:^{
//        [self reloadData];
//    }];
    
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.arrList.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identify = @"InfraredCollectionViewCell";
    InfraredCollectionViewCell *cell = (InfraredCollectionViewCell *)[collectionView dequeueReusableCellWithReuseIdentifier:identify forIndexPath:indexPath];
    id object = self.arrList[indexPath.row];
    
    if ([object isKindOfClass:[SHInfraredKeyModel class]]) {
        SHInfraredKeyModel *model = self.arrList[indexPath.row];
        [cell.btnItem setTitle:model.strName forState:UIControlStateNormal];
        
        @weakify(self);
        [cell setBlockBtnLongPressed:^{
            @strongify(self);
            NSLog(@"走了 长按");
            if (self.didSelectedItemBlock) {
                self.didSelectedItemBlock(indexPath,InfraredUICollectionView_LongPressed,model);
            }
        }];
//        if (indexPath.row == 0) {
//           //默认第一个是开关默认不可编辑
//        }else{
//            
//        }
        
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
//    NSLog(@"w == %f,h == %f",kItemWidth, kItemHeight);
    return CGSizeMake(kItemWidth, kItemHeight);
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
    return UIEdgeInsetsMake(20, 24, 0, 24);
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout referenceSizeForHeaderInSection:(NSInteger)section
{
    return CGSizeMake(UI_SCREEN_WIDTH, 0);
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout referenceSizeForFooterInSection:(NSInteger)section
{
    return CGSizeMake(UI_SCREEN_WIDTH, 20);
}

//纵向间距设定
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section
{
    return 51.0f;
}

//上下间距设定
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
    return 40.0f;
}

#pragma mark -UICollectionViewDelegate
- (BOOL)collectionView:(UICollectionView *)collectionView shouldSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    NSLog(@"走啦啦啦啦啦啦啦啦啦");
//    InfraredCollectionViewCell *cell = (InfraredCollectionViewCell *)[collectionView cellForItemAtIndexPath:indexPath];
    SHInfraredKeyModel *model = self.arrList[indexPath.row];
    if (indexPath.row == 0) {
        if (self.didSelectedItemBlock) {
            self.didSelectedItemBlock(indexPath,InfraredUICollectionView_On,model);
        }
    }else{
        if (self.didSelectedItemBlock) {
            self.didSelectedItemBlock(indexPath,InfraredUICollectionView_Common,model);
        }
    }
}


@end
