//
//  SecurityDeviceCVCell.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/20.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SecurityDeviceCVCell : UICollectionViewCell

@property (strong, nonatomic) NSString *strDeviceName;
@property (strong, nonatomic) NSString *strDeviceRegion;
@property (assign, nonatomic) BOOL isShowRedDot;

@end
