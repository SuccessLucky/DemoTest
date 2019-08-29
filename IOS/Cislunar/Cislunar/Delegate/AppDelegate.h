//
//  AppDelegate.h
//  Cislunar
//
//  Created by 余长涛 on 2018/8/23.
//  Copyright © 2018年 余长涛. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) NSString *trackViewUrl;

+ (AppDelegate *)sharedAppDelegate;


@end

