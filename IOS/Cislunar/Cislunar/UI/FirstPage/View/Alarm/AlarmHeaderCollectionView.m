//
//  AlarmHeaderCollectionView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/11.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "AlarmHeaderCollectionView.h"
#import "AlarmHistoryCollectionViewCell.h"

#define kItemWidth (UI_SCREEN_WIDTH -15*2 - 15*5)/5.00
#define kItemHeight kItemWidth

@interface AlarmHeaderCollectionView()<UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout>

@end

@implementation AlarmHeaderCollectionView

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
        UINib *nib = [UINib nibWithNibName:@"AlarmHistoryCollectionViewCell" bundle:[NSBundle mainBundle]];
        [self registerNib:nib forCellWithReuseIdentifier:@"AlarmHistoryCollectionViewCell"];
    }
    return self;
}

- (void)setArrList:(NSArray *)arrList
{
    _arrList = arrList;
    [self reloadData];
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.arrList.count + 1;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identify = @"AlarmHistoryCollectionViewCell";
    AlarmHistoryCollectionViewCell *cell = (AlarmHistoryCollectionViewCell *)[collectionView dequeueReusableCellWithReuseIdentifier:identify forIndexPath:indexPath];
    
//    if (/* DISABLES CODE */ (1)) {
//        [cell.btnTitle setBackgroundColor:[UIColor whiteColor]];
//        [cell.btnTitle setTitleColor:kCommonColor forState:UIControlStateNormal];
//    }else{
//        [cell.btnTitle setBackgroundColor:[UIColor clearColor]];
//        [cell.btnTitle setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
//    }
    
    if (indexPath.row == self.arrList.count - 1) {
        [cell.btnTitle setBackgroundImage:[UIImage imageNamed:@"dots"] forState:UIControlStateNormal];
        [cell.btnTitle setBackgroundImage:[UIImage imageNamed:@"拷贝"] forState:UIControlStateSelected];
    }else{
        
        NSDate *theDate = self.arrList[indexPath.row];
        NSDateFormatter *date_formatter = [[NSDateFormatter alloc] init];
        [date_formatter setDateFormat:@"yyyy/MM/dd"];
        NSString *strTime = [date_formatter stringFromDate:theDate];
        
        [cell.btnTitle setTitle:[strTime substringWithRange:NSMakeRange(strTime.length-2, 2)] forState:UIControlStateNormal];
    }

    return cell;
}

//- (UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath
//{
//    UICollectionReusableView *reusableView = nil;
//    if (kind == UICollectionElementKindSectionHeader) {
//        
//        ScreenCommonHeaderReusableView *header = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"ScreenCommonHeaderReusableView" forIndexPath:indexPath];
//        reusableView = header;
//    }else {
//        
//        UICollectionReusableView *footer = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:@"footer" forIndexPath:indexPath];
//        reusableView = footer;
//    }
//    return reusableView;
//}

#pragma mark - UICollectionViewDelegateFlowLayout
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    NSLog(@"w == %f,h == %f",kItemWidth, kItemHeight);
    return CGSizeMake(kItemWidth, kItemHeight);
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
    return UIEdgeInsetsMake(15, 15, 15, 15);
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout referenceSizeForHeaderInSection:(NSInteger)section
{
    return CGSizeMake(UI_SCREEN_WIDTH, 0);
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout referenceSizeForFooterInSection:(NSInteger)section
{
    return CGSizeMake(UI_SCREEN_WIDTH, 0);
}

//纵向间距设定
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section
{
    return 15.0f;
}

//上下间距设定
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
    return 15.0f;
}

#pragma mark -UICollectionViewDelegate
- (BOOL)collectionView:(UICollectionView *)collectionView shouldSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    AlarmHistoryCollectionViewCell *cell = (AlarmHistoryCollectionViewCell *)[collectionView cellForItemAtIndexPath:indexPath];
    NSDate *theDate = self.arrList[indexPath.row];
    if (self.BlockTimeCollectionItemSelected) {
        self.BlockTimeCollectionItemSelected(theDate,cell,indexPath);
    }
    
}


@end
