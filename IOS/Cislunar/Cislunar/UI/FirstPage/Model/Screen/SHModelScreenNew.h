//
//  SHModelScreenNew.h
//  SmartHouseYCT
//
//  Created by 余长涛 on 2017/1/5.
//  Copyright © 2017年 余长涛. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef void (^ScreenConfigCompleteHandle)(NSString *strSubcommandIdentifer, NSString *strScreenNo,NSString *strAnswerIdentifer);
typedef void (^ScreenFrameWriteCompleteHandle)(NSString *strSubcommandIdentifer, NSString *screenNo,NSString *strAnswerIdentifer,NSString *strInstructionCount,NSString *strCurrentInstructionNO);

@interface SHModelScreenNew : NSObject

@property (strong, nonatomic) NSString *strCmdID;

@property (strong, nonatomic) NSString *strSubCommandLength;

/*
 01:子命令标识，表示参数配置命令
 02:子命令标识，表示控制帧载入命令
 03:子命令标识，表示参数读取命令
 05:子命令标识，设防类型配置命令
 06:子命令标识，场景联动信息读取命令
 07:子命令标识，场景联动删除命令
 08:子命令标识，报警状态解除命令
 */
@property (strong, nonatomic) NSString *strSubcommandIdentifer;

/*
 场景编号
 */
@property (strong, nonatomic) NSString *strScreenNo;

/*
  00:应答标志 00，表示载入成功，表示配置成功，应答错误标志详见后面说明
 */
@property (strong, nonatomic) NSString *strAnswerIdentifer;

/*
 03:指令总数为 3 条
 */
@property (strong, nonatomic) NSString *strInstructionCount;

/*
  01:当前计数值为 01，表示指令总数 3 条中的第一条
 */
@property (strong, nonatomic) NSString *strCurrentInstructionNO;

/*
  01:设防类型为布防，01 为布防，02 为撤防
 */
@property (strong, nonatomic) NSString *strArmingType;

/*
 01:删除 1 号场景/联动;02:删除 2 号场景/联动;FF:删除全部场景/联
 动)
 */
@property (strong, nonatomic) NSString *strDelteScreenNo;



@end
