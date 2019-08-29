//
//  CommonHeaderCollectionReusableView.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/26.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CommonHeaderCollectionReusableView : UICollectionReusableView<UITextFieldDelegate>

@property (weak, nonatomic) IBOutlet UITextField *textFieldTitle;

@end
