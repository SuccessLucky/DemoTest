//
//  SHUIPicModel.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 16/10/26.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SHUIPicModel : NSObject

@property (assign, nonatomic) int iUIPic_id;
@property (nonatomic, strong) NSString *strUIPic_base_url;
@property (nonatomic, strong) NSString *strUIPic_name;
@property (nonatomic, strong) NSString *strUIPic_image_type;

@end
