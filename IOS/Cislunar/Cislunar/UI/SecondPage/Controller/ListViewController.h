//
//  ListViewController.h
//  JXCategoryView
//
//  Created by jiaxin on 2018/8/8.
//  Copyright © 2018年 jiaxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SecondPageVC.h"

@interface ListViewController : UIViewController

@property (strong, nonatomic) SHModelRoom *modelRoom;

//- (void)loadDataForFirst;
- (void)doLoadInRoomDeviceListDataWithRoomID:(int)iRoomID;

@end
