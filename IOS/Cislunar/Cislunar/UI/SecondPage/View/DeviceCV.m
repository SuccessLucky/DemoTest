//
//  DeviceCV.m
//  Cislunar
//
//  Created by 余长涛 on 2018/9/19.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import "DeviceCV.h"
#import "DeviceCVCell.h"
//#import "ScreenAllCR.h"

@interface DeviceCV()<UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout>

@property (assign, nonatomic) BOOL isSelected;

@property(assign, nonatomic) CommonCollectionViewType type;

@end

@implementation DeviceCV

- (id)initWithFrame:(CGRect)frame collectionViewLayout:(UICollectionViewLayout *)layout  type:(CommonCollectionViewType)type
{
    
    self = [super initWithFrame:frame collectionViewLayout:layout];
    if (self) {
        self.frame = frame;
        self.delegate = self;
        self.dataSource = self;
        self.backgroundColor = [UIColor clearColor];
        self.scrollEnabled = YES;
        self.showsHorizontalScrollIndicator = NO;
        
        UINib *nib = [UINib nibWithNibName:@"DeviceCVCell" bundle:[NSBundle mainBundle]];
        [self registerNib:nib forCellWithReuseIdentifier:@"DeviceCVCell"];
        
        [self registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"header"];

        [self registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:@"footer"];
        
    }
    return self;
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
    if (self.type == CommonCollectionViewType_Common) {
        return self.arrDataList.count;
    }else if(self.type == CommonCollectionViewType_Edit){
        return self.arrDataList.count + 1;
    }else{
        return 0;
    }
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identify = @"DeviceCVCell";
    DeviceCVCell *cell = (DeviceCVCell *)[collectionView dequeueReusableCellWithReuseIdentifier:identify
                                                                                   forIndexPath:indexPath];
    if (self.type == CommonCollectionViewType_Edit) {
        
        if (self.arrDataList.count == 0) {
            cell.strDeviceName = @"添加";
            cell.strDeviceNormalImage = @"添加.png";
            cell.strDeviceSelectedImage = @"添加.png";
            cell.strNormalImageVBg = @"moren";
            cell.isCommonImage = YES;
            return cell;
        }else{
            if (indexPath.row == self.arrDataList.count) {
                cell.strDeviceName = @"添加";
                cell.strDeviceNormalImage = @"添加.png";
                cell.strDeviceSelectedImage = @"添加.png";
                cell.strNormalImageVBg = @"moren";
                cell.isCommonImage = YES;
            }else{
                SHModelDevice *modelDevice = self.arrDataList[indexPath.row];
                cell.isCommonImage = NO;
                [self doHandleTheDeviceState:modelDevice cell:cell];
                @weakify(self);
                [cell setBlockBtnLongPressed:^{
                    @strongify(self);
                    if (self.didSelectedDeviceItemBlock) {
                        self.didSelectedDeviceItemBlock(indexPath,CommonCollectionViewActionType_LongPressed,modelDevice);
                    }
                }];
            }
            return cell;
        }
    }else if (self.type == CommonCollectionViewType_Common) {
        SHModelDevice *modelDevice = self.arrDataList[indexPath.row];
        cell.isCommonImage = NO;
        [self doHandleTheDeviceState:modelDevice cell:cell];
        return cell;
    }else{
        
        return cell;
    }
    
    
    /*
    SHModelDevice *model = self.arrDataList[indexPath.row];
    cell.strDeviceNormalImage = model.strDevice_image;
    
    if (self.isSelected) {
        cell.strDeviceNormalImage = @"插座";
        cell.strNormalImageVBg = @"moren";
        
        self.isSelected = !self.isSelected;
    }else{
        cell.strDeviceSelectedImage = @"插座_pre";
        cell.strSelctedImageVBg = @"anxia";
        self.isSelected = !self.isSelected;
    }
    
    
    cell.strDeviceName = @"插座";
    cell.strDeviceRegion = @"客厅";
    cell.strDeviceState = @"打开";
     */
    
    return cell;
}


- (UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath
{
    UICollectionReusableView *reusableView = nil;
    
    if (kind == UICollectionElementKindSectionHeader) {

        UICollectionReusableView *headerView = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"header" forIndexPath:indexPath];
        reusableView = headerView;
    }else {
        
        UICollectionReusableView *footer = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:@"footer" forIndexPath:indexPath];
        reusableView = footer;
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
    if (self.type == CommonCollectionViewType_Edit) {
        
        if (self.arrDataList.count == 0) {
            if (self.didSelectedDeviceItemBlock)
            {
                self.didSelectedDeviceItemBlock(indexPath,CommonCollectionViewActionType_Add,nil);
            }
        }else{
            if (indexPath.row == self.arrDataList.count) {
                if (self.didSelectedDeviceItemBlock)
                {
                    self.didSelectedDeviceItemBlock(indexPath,CommonCollectionViewActionType_Add,nil);
                }
            }else{
                id object = self.arrDataList[indexPath.row];
                SHModelDevice *device = (SHModelDevice *)object;
                if (self.didSelectedDeviceItemBlock)
                {
                    self.didSelectedDeviceItemBlock(indexPath,CommonCollectionViewActionType_Common,device);
                }
            }
        }
    }else if (self.type == CommonCollectionViewType_Common) {
        id object = self.arrDataList[indexPath.row];
        SHModelDevice *device = (SHModelDevice *)object;
        if (self.didSelectedDeviceItemBlock) {
            self.didSelectedDeviceItemBlock(indexPath,CommonCollectionViewActionType_Control,device);
        }
        
    }else{
        
        NSLog(@"点击了未知领域");
    }

}
/*
 // Only override drawRect: if you perform custom drawing.
 // An empty implementation adversely affects performance during animation.
 - (void)drawRect:(CGRect)rect {
 // Drawing code
 }
 */

#pragma mark -
#pragma mark - private
- (void)doHandleTheDeviceState:(SHModelDevice *)modelDevice cell:(DeviceCVCell *)cell
{
    NSString *strHex;
    if ([modelDevice.strDevice_device_OD isEqualToString:@"0F AA"]) {
        if ([modelDevice.strDevice_device_type isEqualToString:@"81"]) {
            //                        NSLog(@"0FAA里面的报警设备");
            if (modelDevice.iDevice_device_state1 == 1) {
                cell.isSelected = YES;
            }else{
                cell.isSelected = NO;
            }
        }else {
            //                        NSLog(@"0FAA里面的其它设备");
            strHex = [self doGetDeviceState:modelDevice];
            if ([strHex intValue] == 1) {
                cell.isSelected = YES;
            }else if ([strHex intValue] == 2){
                cell.isSelected = NO;
            }else{
                cell.isSelected = NO;
            }
        }
    }else if ([modelDevice.strDevice_device_OD isEqualToString:@"0F BE"]) {
        if (modelDevice.iDevice_device_state1 == 1) {
            cell.isSelected = YES;
        }else{
            cell.isSelected = NO;
        }
    }else if ([modelDevice.strDevice_device_OD isEqualToString:@"0F E6"]) {
        cell.isSelected = NO;
    }else if ([modelDevice.strDevice_device_OD isEqualToString:@"0F C8"]) {
        if (modelDevice.iDevice_device_state1 == 1) {
            cell.isSelected = YES;
        }else{
            cell.isSelected = NO;
        }
    } else{
        cell.isSelected = NO;
    }
    cell.strDeviceName = modelDevice.strDevice_device_name;
    NSString *strUnUrl = [[SHAppCommonRequest shareInstance] doGetDeviceGrayUIPicWithPicName:modelDevice.strDevice_image];
    NSString *strPrUrl = [[SHAppCommonRequest shareInstance] doGetDeviceHighLightUIPicWithPicName:modelDevice.strDevice_image];
    cell.strDeviceSelectedImage = strPrUrl;
    cell.strDeviceNormalImage = strUnUrl;
    cell.strDeviceRegion = modelDevice.strDevice_room_name;
    
    if ([strHex isEqualToString:@"01"]) {
        cell.strDeviceState =@"开";
    }else{
        cell.strDeviceState =@"关";
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
