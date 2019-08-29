//
//  ScreenThreeCV.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/18.
//  Copyright © 2018年 余长涛. All rights reserved.
//




#import "ScreenThreeCV.h"
#import "ScreenCVCell.h"
#import "FirstPageHeaderCR.h"
#import "ScreenAllCR.h"

@interface ScreenThreeCV ()<UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout>

@property (assign, nonatomic) ScreenThreeCVType typeTemp;

@end

@implementation ScreenThreeCV

- (id)initWithFrame:(CGRect)frame collectionViewLayout:(UICollectionViewLayout *)layout withScreenThreeCVTYpe:(ScreenThreeCVType)type
{
    self = [super initWithFrame:frame collectionViewLayout:layout];
    if (self) {
        self.frame = frame;
        self.delegate = self;
        self.dataSource = self;
        self.backgroundColor = [UIColor clearColor];
        self.scrollEnabled = YES;
        
        self.showsHorizontalScrollIndicator = NO;
        
        UINib *nib = [UINib nibWithNibName:@"ScreenCVCell" bundle:[NSBundle mainBundle]];
        [self registerNib:nib forCellWithReuseIdentifier:@"ScreenCVCell"];
        
        if (type == ScreenThreeCVType_First) {
            UINib *nibHeader = [UINib nibWithNibName:@"FirstPageHeaderCR" bundle:[NSBundle mainBundle]];
            [self registerNib:nibHeader forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"FirstPageHeaderCR"];
        }else{
            
            UINib *nibHeader = [UINib nibWithNibName:@"ScreenAllCR" bundle:[NSBundle mainBundle]];
            [self registerNib:nibHeader forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"ScreenAllCR"];
        }

        [self registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:@"footer"];
        
        self.typeTemp = type;
    }
    return self;
}

#pragma mark -
#pragma mark - firstPage
- (void)setStrGreetings:(NSString *)strGreetings
{
    _strGreetings = strGreetings;
    [self reloadData];
}

- (void)setStrImageWeather:(NSString *)strImageWeather
{
    _strImageWeather = strImageWeather;
    [self reloadData];
}

- (void)setStrWeatherStatus:(NSString *)strWeatherStatus
{
    _strWeatherStatus = strWeatherStatus;
    [self reloadData];
}

- (void)setStrTemperature:(NSString *)strTemperature
{
    
    _strTemperature = strTemperature;
    [self reloadData];
}

- (void)setStrHumidity:(NSString *)strHumidity
{
    _strHumidity = strHumidity;
    [self reloadData];
}

- (void)setArrHistory:(NSArray *)arrHistory
{
    _arrHistory = arrHistory;
    [self reloadData];
}

#pragma mark -
#pragma mark - 公用

- (void)setArrDataList:(NSArray *)arrDataList
{
    _arrDataList = arrDataList;
    [self reloadData];
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    if (self.typeTemp == ScreenThreeCVType_First ){
        if (self.arrDataList.count > 5) {
            return 6;
        }else if(self.arrDataList.count == 0){
            return 1;
        }else{
            return self.arrDataList.count + 1;
        }
    }else{
        return self.arrDataList.count;
    }
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identify = @"ScreenCVCell";
    ScreenCVCell *cell = (ScreenCVCell *)[collectionView dequeueReusableCellWithReuseIdentifier:identify
                                                                                   forIndexPath:indexPath];
    if(self.arrDataList.count == 0)
    {
        cell.strTitle = @"全部";
        cell.strNormalImageName = @"更多";
        cell.strHighLightImageName = @"更多_pre";
        cell.isCommonImage = YES;
        
    }else{
        
        if (self.typeTemp == ScreenThreeCVType_All ){
            ScreenModel *model = self.arrDataList[indexPath.row];
            NSString *strUnUrl = [[SHAppCommonRequest shareInstance] doGetScreenGrayUIPicWithPicName:model.strScreen_image];
            NSString *strPrUrl = [[SHAppCommonRequest shareInstance] doGetScreenHighLightUIPicWithPicName:model.strScreen_image];
            cell.strTitle = model.strScreen_name;
            cell.strNormalImageName = model.strScreen_image;
            cell.strNormalImageName = strUnUrl;
            cell.strHighLightImageName = strPrUrl;
            cell.isCommonImage = NO;
            //所有场景列表
            @weakify(self);
            [cell setBlockBtnLongPressed:^{
                @strongify(self);
                NSLog(@"走了 长按");
                if (self.BlockCollectionSelected) {
                    self.BlockCollectionSelected(indexPath,self.typeTemp,ScreenThreeCVActionType_LongPressed);
                }
            }];
            
        }else{
            //首页常用5个场景
            if (self.arrDataList.count >5) {
                ScreenModel *model = self.arrDataList[indexPath.row];
                NSString *strUnUrl = [[SHAppCommonRequest shareInstance] doGetScreenGrayUIPicWithPicName:model.strScreen_image];
                NSString *strPrUrl = [[SHAppCommonRequest shareInstance] doGetScreenHighLightUIPicWithPicName:model.strScreen_image];
                if (indexPath.row == 5) {
                    
                    cell.strTitle = @"全部";
                    cell.strNormalImageName = @"更多";
                    cell.strHighLightImageName = @"更多_pre";
                    cell.isCommonImage = YES;
                    
                }else{
                    
                    cell.strTitle = model.strScreen_name;
                    cell.strNormalImageName = model.strScreen_image;
                    cell.strNormalImageName = strUnUrl;
                    cell.strHighLightImageName = strPrUrl;
                    cell.isCommonImage = NO;
                    
                }
                
            }else if (self.arrDataList.count == 0){
                cell.strTitle = @"全部";
                cell.strNormalImageName = @"更多";
                cell.strHighLightImageName = @"更多_pre";
                cell.isCommonImage = YES;
                
            }else {
                if (indexPath.row == self.arrDataList.count) {
                    
                    cell.strTitle = @"全部";
                    cell.strNormalImageName = @"更多";
                    cell.strHighLightImageName = @"更多_pre";
                    cell.isCommonImage = YES;
                    
                }else{
                    ScreenModel *model = self.arrDataList[indexPath.row];
                    NSString *strUnUrl = [[SHAppCommonRequest shareInstance] doGetScreenGrayUIPicWithPicName:model.strScreen_image];
                    NSString *strPrUrl = [[SHAppCommonRequest shareInstance] doGetScreenHighLightUIPicWithPicName:model.strScreen_image];
                    cell.strTitle = model.strScreen_name;
                    cell.strNormalImageName = model.strScreen_image;
                    cell.strNormalImageName = strUnUrl;
                    cell.strHighLightImageName = strPrUrl;
                    cell.isCommonImage = NO;
                    
                }
            }
        }
    }
    return cell;
}


- (UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath
{
    UICollectionReusableView *reusableView = nil;
    
    if (self.typeTemp == ScreenThreeCVType_First) {
        if (kind == UICollectionElementKindSectionHeader) {
            FirstPageHeaderCR *header = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader
                                                                           withReuseIdentifier:@"FirstPageHeaderCR"
                                                                                  forIndexPath:indexPath];
            header.strGreetings = self.strGreetings;
            header.strImageWeather = self.strImageWeather;
            header.strWeatherStatus = self.strWeatherStatus;
            
            header.strTemperature = self.strTemperature;
            header.strHumidity = self.strHumidity;
            header.arrHistory = self.arrHistory;
            
            @weakify(self);
            [header setBlockHistroyPressed:^{
                @strongify(self);
                if (self.BlockHistroyPressed) {
                    self.BlockHistroyPressed();
                }
            }];
            reusableView = header;
            
        }else {
            UICollectionReusableView *footer = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:@"footer" forIndexPath:indexPath];
            reusableView = footer;
        }
        
    }else{
        
        if (kind == UICollectionElementKindSectionHeader) {
            
            
            FirstPageHeaderCR *header = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader
                                                                           withReuseIdentifier:@"ScreenAllCR"
                                                                                  forIndexPath:indexPath];
            reusableView = header;
        }else {
            
            UICollectionReusableView *footer = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:@"footer" forIndexPath:indexPath];
            reusableView = footer;
        }
    }
    
    
    return reusableView;
}

- (void)collectionView:(UICollectionView *)collectionView didHighlightItemAtIndexPath:(NSIndexPath *)indexPath{
    
    UICollectionViewCell *cell = [collectionView cellForItemAtIndexPath:indexPath];
    cell.backgroundColor =RGB(79, 200, 249);
    
}
- (void)collectionView:(UICollectionView *)collectionView didUnhighlightItemAtIndexPath:(NSIndexPath *)indexPath{
    
    UICollectionViewCell *cell = [collectionView cellForItemAtIndexPath:indexPath];
    cell.backgroundColor = [UIColor clearColor];
}
- (BOOL)collectionView:(UICollectionView *)collectionView shouldHighlightItemAtIndexPath:(NSIndexPath *)indexPath{
    
    return YES;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    [collectionView deselectItemAtIndexPath:indexPath animated:YES];
//    ScreenCVCell *cell = (ScreenCVCell *)[collectionView cellForItemAtIndexPath:indexPath];
    NSLog(@"indeptah.row == %ld",(long)indexPath.row);
    if (self.BlockCollectionSelected) {
        
        if (self.arrDataList.count == 0) {
            self.BlockCollectionSelected(indexPath,self.typeTemp,ScreenThreeCVActionType_NoneData);
        }else{
            self.BlockCollectionSelected(indexPath,self.typeTemp,ScreenThreeCVActionType_Common);
        }
        
    }
}


@end
