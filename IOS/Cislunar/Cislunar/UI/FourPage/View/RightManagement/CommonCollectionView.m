//
//  EquipmentCollectionView.m
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/13.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import "CommonCollectionView.h"
#import "CommonCollectionViewCell.h"

#define kItemWidth (UI_SCREEN_WIDTH - 35 * 4)/3.00f
#define kItemHeight kItemWidth + 29

@interface CommonCollectionView ()<UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout>

@property(assign, nonatomic) CommonCollectionViewType type;

@end

@implementation CommonCollectionView


- (id)initWithFrame:(CGRect)frame collectionViewLayout:(UICollectionViewLayout *)layout type:(CommonCollectionViewType)type
{
    self = [super initWithFrame:frame collectionViewLayout:layout];
    if (self) {
        self.frame = frame;
        self.type = type;
        self.delegate = self;
        self.dataSource = self;
        self.backgroundColor = UIColorFromRGB(0xffffff);
        self.scrollEnabled = YES;
        self.showsHorizontalScrollIndicator = NO;
        
        UINib *nib = [UINib nibWithNibName:@"CommonCollectionViewCell" bundle:[NSBundle mainBundle]];
        [self registerNib:nib forCellWithReuseIdentifier:@"CommonCollectionViewCell"];
        
        [self registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"header"];
        
        [self registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:@"footer"];
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
    if (self.type == CommonCollectionViewType_Common) {
        return self.arrList.count;
    }else if(self.type == CommonCollectionViewType_Edit){
        return self.arrList.count + 1;
    }
    return 0;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identify = @"CommonCollectionViewCell";
    CommonCollectionViewCell *cell = (CommonCollectionViewCell *)[collectionView dequeueReusableCellWithReuseIdentifier:identify forIndexPath:indexPath];
    
    if (self.type == CommonCollectionViewType_Edit) {
        
        if (self.arrList.count == 0) {
            cell.labelTitle.text = @"添加";
            [cell.btnImage setImage:[UIImage imageNamed:@"添加.png"] forState:UIControlStateNormal];
            cell.btnImage.selected = NO;
            return cell;
        }else{
            if (indexPath.row == self.arrList.count) {
                cell.labelTitle.text = @"添加";
                [cell.btnImage setImage:[UIImage imageNamed:@"添加.png"] forState:UIControlStateNormal];
                cell.btnImage.selected = NO;
            }else{
                SHModelDevice *modelDevice = self.arrList[indexPath.row];
                [self doHandleTheDeviceState:modelDevice cell:cell];
                @weakify(self);
                [cell setBlockBtnLongPressed:^{
                    @strongify(self);
                    if (self.didSelectedItemBlock) {
                        self.didSelectedItemBlock(indexPath,CommonCollectionViewActionType_LongPressed,modelDevice);
                    }
                }];
            }
            return cell;
        }
    }else if (self.type == CommonCollectionViewType_Common) {
        SHModelDevice *modelDevice = self.arrList[indexPath.row];
        [self doHandleTheDeviceState:modelDevice cell:cell];
        return cell;
    }else{
        
        return cell;
    }
    
}

- (void)doHandleTheDeviceState:(SHModelDevice *)modelDevice cell:(CommonCollectionViewCell *)cell
{
    if ([modelDevice.strDevice_device_OD isEqualToString:@"0F AA"]) {
        if ([modelDevice.strDevice_device_type isEqualToString:@"81"]) {
            //                        GLOG_INFO(@"0FAA里面的报警设备");
            if (modelDevice.iDevice_device_state1 == 1) {
                cell.btnImage.selected = YES;
            }else{
                cell.btnImage.selected = NO;
            }
        }else {
            //                        GLOG_INFO(@"0FAA里面的其它设备");
            NSString *strHex = [self doGetDeviceState:modelDevice];
            if ([strHex intValue] == 1) {
                cell.btnImage.selected = YES;
            }else if ([strHex intValue] == 2){
                cell.btnImage.selected = NO;
            }else{
                cell.btnImage.selected = NO;
            }
        }
    }else if ([modelDevice.strDevice_device_OD isEqualToString:@"0F BE"]) {
        if (modelDevice.iDevice_device_state1 == 1) {
            cell.btnImage.selected = YES;
        }else{
            cell.btnImage.selected = NO;
        }
    }else if ([modelDevice.strDevice_device_OD isEqualToString:@"0F E6"]) {
        cell.btnImage.selected = NO;
    }else if ([modelDevice.strDevice_device_OD isEqualToString:@"0F C8"]) {
        if (modelDevice.iDevice_device_state1 == 1) {
            cell.btnImage.selected = YES;
        }else{
            cell.btnImage.selected = NO;
        }
    } else{
        cell.btnImage.selected = NO;
    }
    cell.labelTitle.text = modelDevice.strDevice_device_name;
    NSString *strUnUrl = [[SHAppCommonRequest shareInstance] doGetDeviceGrayUIPicWithPicName:modelDevice.strDevice_image];
    NSString *strPrUrl = [[SHAppCommonRequest shareInstance] doGetDeviceHighLightUIPicWithPicName:modelDevice.strDevice_image];
    [cell.btnImage sd_setImageWithURL:[NSURL URLWithString:strUnUrl] forState:UIControlStateNormal];
    [cell.btnImage sd_setImageWithURL:[NSURL URLWithString:strPrUrl] forState:UIControlStateSelected];
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
    return UIEdgeInsetsMake(21, 35, 0, 35);
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
    return 35.0f;
}

//上下间距设定
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
    return 32.0f;
}

#pragma mark -UICollectionViewDelegate
- (BOOL)collectionView:(UICollectionView *)collectionView shouldSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (self.type == CommonCollectionViewType_Edit) {
        
        if (self.arrList.count == 0) {
            if (self.didSelectedItemBlock)
            {
                self.didSelectedItemBlock(indexPath,CommonCollectionViewActionType_Add,nil);
            }
        }else{
            if (indexPath.row == self.arrList.count) {
                if (self.didSelectedItemBlock)
                {
                    self.didSelectedItemBlock(indexPath,CommonCollectionViewActionType_Add,nil);
                }
            }else{
                id object = self.arrList[indexPath.row];
                SHModelDevice *device = (SHModelDevice *)object;
                if (self.didSelectedItemBlock)
                {
                    self.didSelectedItemBlock(indexPath,CommonCollectionViewActionType_Common,device);
                }
            }
        }
    }else if (self.type == CommonCollectionViewType_Common) {
        id object = self.arrList[indexPath.row];
        //tabbar设备里面设备控制页面
        SHModelDevice *device = (SHModelDevice *)object;
        self.didSelectedItemBlock(indexPath,CommonCollectionViewActionType_Control,device);
    }else{
        
        NSLog(@"点击了未知领域");
    }
}

- (NSString *)doGetDeviceState:(SHModelDevice *)device
{
    NSString *strHexState = @"00";
    if (!device.strDevice_other_status) {
        if (device.iDevice_device_state1  == 1) {
            
            strHexState = @"01";
        }else{
            
            strHexState = @"02";
        }
    }else if ([device.strDevice_other_status intValue] == 1) {
        
        if (device.iDevice_device_state1  == 1) {
            
            strHexState = @"01";
        }else{
            
            strHexState = @"02";
        }
        
    }else if ([device.strDevice_other_status intValue] == 2){
        
        if (device.iDevice_device_state2  == 1) {
            strHexState = @"01";
        }else{
            strHexState = @"02";
        }
    }else if ([device.strDevice_other_status intValue] == 3){
        if (device.iDevice_device_state3  == 1) {
            strHexState = @"01";
        }else{
            strHexState = @"02";
        }
    }
    return strHexState;
}


@end


