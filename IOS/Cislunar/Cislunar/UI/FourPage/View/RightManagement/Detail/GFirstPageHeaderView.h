//
//  GFirstPageHeaderView.h
//  GJJUser
//
//  Created by 余长涛 on 15/12/16.
//  Copyright © 2015年 过家家. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GFirstPageHeaderView : UIView

@property (weak, nonatomic) IBOutlet UIView *viewHeader;
@property (weak, nonatomic) IBOutlet UIImageView *imageViewPic;
@property (weak, nonatomic) IBOutlet UILabel *labelTitle;

@property (copy, nonatomic) void(^didPressAddBtn)();
@property (weak, nonatomic) IBOutlet UIButton *btnAdd;

@end
