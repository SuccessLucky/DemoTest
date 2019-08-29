//
//  SHMemberModel.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2016/11/19.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SHMemberModel : NSObject

@property (assign, nonatomic) int iMember_id;
@property (strong, nonatomic) NSString *str_member_name;
@property (assign, nonatomic) int iMember_type;
@property (strong, nonatomic) NSString *str_image;
@property (strong, nonatomic) NSString *str_account;

@end
