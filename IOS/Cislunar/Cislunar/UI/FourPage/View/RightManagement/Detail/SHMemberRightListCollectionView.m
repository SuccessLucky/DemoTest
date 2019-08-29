//
//  SHMemberRightListCollectionView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/22.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "SHMemberRightListCollectionView.h"
#import "CommonCollectionViewCell.h"

#define kItemWidth (UI_SCREEN_WIDTH - 35 * 4)/3.00f
#define kItemHeight kItemWidth + 29

@interface SHMemberRightListCollectionView()
<UICollectionViewDelegate,UICollectionViewDataSource>

@end

@implementation SHMemberRightListCollectionView

- (id)initWithFrame:(CGRect)frame collectionViewLayout:(UICollectionViewLayout *)layout
{
    self = [super initWithFrame:frame collectionViewLayout:layout];
    if (self) {
        self.frame = frame;
        self.delegate = self;
        self.dataSource = self;
        self.backgroundColor = UIColorFromRGB(0xffffff);
        self.scrollEnabled = YES;
        self.showsHorizontalScrollIndicator = NO;
        
        UINib *nib = [UINib nibWithNibName:@"CommonCollectionViewCell" bundle:[NSBundle mainBundle]];
        [self registerNib:nib forCellWithReuseIdentifier:@"CommonCollectionViewCell"];
    }
    return self;
}

- (void)setArrList:(NSArray *)arrList
{
    _arrList = arrList;
    [self reloadData];
}


-(NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView{
    return 1;
}

-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    return self.arrList.count;
}

-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *identify = @"CommonCollectionViewCell";
    CommonCollectionViewCell *cell = (CommonCollectionViewCell *)[collectionView dequeueReusableCellWithReuseIdentifier:identify forIndexPath:indexPath];
    ScreenModel *model = self.arrList[indexPath.row];
    NSString *strUnUrl = [[SHAppCommonRequest shareInstance] doGetScreenGrayUIPicWithPicName:model.strScreen_image];
    NSString *strPrUrl = [[SHAppCommonRequest shareInstance] doGetScreenHighLightUIPicWithPicName:model.strScreen_image];
    [cell.btnImage sd_setImageWithURL:[NSURL URLWithString:strUnUrl] forState:UIControlStateNormal];
    [cell.btnImage sd_setImageWithURL:[NSURL URLWithString:strPrUrl] forState:UIControlStateSelected];
    cell.labelTitle.text = model.strScreen_name;
    return cell;
}








@end
