//
//  RoomCollectionView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/17.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "RoomCollectionView.h"
#import "RoomCollectionViewCell.h"
#define  kItemWidth (UI_SCREEN_WIDTH / 3.0)
#define  kItemHeight kItemWidth
@interface RoomCollectionView () <UICollectionViewDelegate, UICollectionViewDataSource>

@end


@implementation RoomCollectionView

static NSString *cellIdentifier = @"RoomCollectionViewCell";

- (instancetype)initWithFrame:(CGRect)frame collectionViewLayout:(UICollectionViewLayout *)layout
{
    self = [super initWithFrame:frame collectionViewLayout:layout];
    if (self) {
        self.delegate = self;
        self.dataSource = self;
        self.backgroundColor = kBackgroundGrayColor;
        [self registerNib:[UINib nibWithNibName:@"RoomCollectionViewCell" bundle:nil] forCellWithReuseIdentifier:cellIdentifier];
        
        [self registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"header"];
        
        [self registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:@"footer"];
    }
    return self;
}

- (void)setArrList:(NSArray *)arrList
{
    _arrList = arrList;
}


- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.arrList.count + 1;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    RoomCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:cellIdentifier forIndexPath:indexPath];
    
    if (self.hideSplitLine) {
        cell.rightLineView.hidden = YES;
        cell.bottomLineView.hidden = YES;
    } else {
        if ((indexPath.row + 1) % 3 != 0) {
            cell.rightLineView.hidden = NO;
        } else {
            cell.rightLineView.hidden = YES;
        }
    }
    if (indexPath.row == self.arrList.count) {
        cell.iconLabel.text = @"添加";
        [cell.btnIcon setImage:[UIImage imageNamed:@"添加.png"] forState:UIControlStateNormal];
    }else{
    
        SHModelRoom *model = self.arrList[indexPath.row];
        cell.iconLabel.text = model.strRoom_name;
        NSString *strCommonUrl = [[SHAppCommonRequest shareInstance] doGetRoomCommonUIPicWithPicName:model.strRoom_image];
//        [cell.btnIcon sd_setImageWithURL:[NSURL URLWithString:strUnUrl] forState:UIControlStateHighlighted];
        [cell.btnIcon sd_setImageWithURL:[NSURL URLWithString:strCommonUrl] forState:UIControlStateNormal];
        @weakify(self);
        [cell setBlockBtnLongPressed:^{
            @strongify(self);
            NSLog(@"走了 长按");
            if (self.didSelectedItemBlock) {
                self.didSelectedItemBlock(indexPath,RoomCollectionViewActionType_LongPressed,model);
            }
        }];
    }
    return cell;
}

- (UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath
{
    UICollectionReusableView *reusableView = nil;
    if (kind == UICollectionElementKindSectionHeader) {
        
        UICollectionReusableView *header = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"header" forIndexPath:indexPath];
        reusableView = header;
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
    return UIEdgeInsetsMake(0, 0, 0, 0);
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
    return 0.0f;
}

//上下间距设定
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
    return 0.0f;
}

#pragma mark -UICollectionViewDelegate
- (BOOL)collectionView:(UICollectionView *)collectionView shouldSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    [collectionView deselectItemAtIndexPath:indexPath animated:YES];
    NSLog(@"indeptah.row == %ld",indexPath.row);
    NSLog(@"走了 短按");
    if (indexPath.row == self.arrList.count) {
        if (self.didSelectedItemBlock) {
            self.didSelectedItemBlock(indexPath,RoomCollectionViewActionType_Add,nil);
        }
    }else{
    
        SHModelRoom *modelRoom = self.arrList[indexPath.row];
        if (self.didSelectedItemBlock) {
            self.didSelectedItemBlock(indexPath,RoomCollectionViewActionType_Common,modelRoom);
        }
    }
    
}


@end
