//
//  DeviceCVCell.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/19.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DeviceCVCell : UICollectionViewCell

@property (strong, nonatomic) NSString *strDeviceNormalImage;

@property (strong, nonatomic) NSString *strDeviceSelectedImage;

@property (strong, nonatomic) NSString *strDeviceName;

@property (strong, nonatomic) NSString *strDeviceState;

@property (strong, nonatomic) NSString *strDeviceRegion;

@property (strong, nonatomic) NSString *strNormalImageVBg;

@property (strong, nonatomic) NSString *strSelctedImageVBg;

@property (assign, nonatomic) BOOL isSelected;

@property (assign, nonatomic) BOOL isCommonImage;

@property (copy, nonatomic) void(^BlockBtnLongPressed)(void);

@end
