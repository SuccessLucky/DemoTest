//
//  ScreenIconCollectionViewCell.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/9.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ScreenIconCollectionViewCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIButton *btnIcon;
@property (copy, nonatomic) void(^blockBtnIconPressed)();
@end
