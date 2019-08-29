//
//  CommonAlterView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/26.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>
#define collectionViewHeight  290
#define collectionViewWidth   [UIScreen mainScreen].bounds.size.width - 30

typedef NS_ENUM(NSInteger, CommonAlterViewType)
{
    CommonAlterViewType_Common   = 0,    //常规的
    CommonAlterViewType_Device   = 1,    //有高亮显示的
};


typedef void(^BlockGetObject)(id object);

@interface CommonAlterView : UIView

@property (copy, nonatomic) void(^BlockGetNameAndPic)(NSString *strName,SHUIPicModel *model);
@property (copy, nonatomic) void(^BlockDimis)();
@property (strong, nonatomic) NSString *strDefaultTitle;

- (instancetype)initWithFrame:(CGRect)frame dataSourceArr:(NSArray *)arrPicList type:(CommonAlterViewType) type;

//弹出
//- (void)pop;
- (void)popWithFatherView:(UIView *)view;

////隐藏
//- (void)dismiss:(BlockGetObject)blockObject;

@end
