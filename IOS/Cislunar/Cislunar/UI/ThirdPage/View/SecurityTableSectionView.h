//
//  SecurityTableSectionView.h
//  Cislunar
//
//  Created by 余长涛 on 2018/9/20.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SecurityTableSectionView : UIView

@property (strong, nonatomic) NSString *strIcon;
@property (strong, nonatomic) NSString *strName;
@property (strong, nonatomic) NSString *strNum;
@property (strong, nonatomic) NSString *strArrow;
@property (assign, nonatomic) NSInteger iSection;

@property (assign, nonatomic) int iRedDotCount;

@property (copy, nonatomic) void(^BlockBtnPressed)(NSInteger iSection);

@end
