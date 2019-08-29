//
//  ScreenCVCell.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/18.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ScreenCVCell : UICollectionViewCell

@property (strong, nonatomic) NSString *strTitle;
@property (strong, nonatomic) NSString *strNormalImageName;
@property (strong, nonatomic) NSString *strHighLightImageName;
@property (assign, nonatomic) BOOL isCommonImage;
@property (copy, nonatomic) void(^BlockBtnLongPressed)(void);


@end
