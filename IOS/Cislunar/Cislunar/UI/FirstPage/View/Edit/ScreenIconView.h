//
//  ScreenIconView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/9.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ScreenIconView : UIView

@property (copy, nonatomic) void(^blockCompleteHandle)(NSIndexPath *indexPath);

- (void)show:(NSArray *)arrIconList;
- (void)dismiss;

@end
