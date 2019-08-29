//
//  MOBAUserCenterRequest.h
//  MobAPI
//
//  Created by ShengQiangLiu on 16/5/3.
//  Copyright © 2016年 mob. All rights reserved.
//

#import <MobAPI/MobAPI.h>

/**
 *  用户系统相关请求
 *
 *  @author ShengQiangLiu
 */
@interface MOBAUserCenterRequest : MOBARequest

/**
 *  用户注册，(必须使用带 email 的新接口注册，不带 email 参数的接口已经废弃不能再使用 !)
 *
 *  @param username 用户名（一个key只能存在唯一username），必填项，不允许为nil
 *  @param password 用户密码（建议加密），必填项，不允许为nil
 *
 *  @return 请求对象
 */
+ (MOBAUserCenterRequest *) userRigisterRequestByUsername:(NSString *)username
                                          password:(NSString *)password DEPRECATED_MSG_ATTRIBUTE("use -[userRigisterRequestByUsername:password:email] method instead.");

/**
 *  用户注册
 *
 *  @param username 用户名（一个key只能存在唯一username），必填项，不允许为nil
 *  @param password 用户密码（建议加密），必填项，不允许为nil
 *  @param email    邮箱，必填项，不允许为nil
 *
 *  @return 请求对象
 */
+ (MOBAUserCenterRequest *) userRigisterRequestByUsername:(NSString *)username
                                          password:(NSString *)password
                                             email:(NSString *)email;

/**
 *  用户登录
 *
 *  @param username 用户名，必填项，不允许为nil
 *  @param password 用户密码，必填项，不允许为nil
 *
 *  @return 请求对象
 */
+ (MOBAUserCenterRequest *) userLoginRequestByUsername:(NSString *)username password:(NSString *)password;

/**
 *  修改用户密码
 *
 *  @param username 用户名，必填项，不允许为nil
 *  @param oldPwd   旧密码，必填项，不允许为nil
 *  @param newPwd   新密码，必填项，不允许为nil
 *
 *  @return 请求对象
 */
+ (MOBAUserCenterRequest *) userPasswordChangeRequestByUsername:(NSString *)username
                                             oldPassword:(NSString *)oldPwd
                                             newPassword:(NSString *)newPwd DEPRECATED_MSG_ATTRIBUTE("use -[userPasswordChangeRequestByUsername:oldPassword:newPassword:mode] method instead.");

/**
 *  修改用户密码
 *
 *  @param username 用户名。必填项，不允许为nil
 *  @param oldPwd   旧密码[当mode=1时，标示旧密码，当mode=2时，标示系统随机码，默认为旧密码]。必填项，不允许为nil
 *  @param newPwd   新密码。必填项，不允许为nil
 *  @param mode     模式：1-用户输入旧密码;2-由用户通过找回密码接口获取系统随机码，默认为1
 *
 *  @return 请求对象
 */
+ (MOBAUserCenterRequest *) userPasswordChangeRequestByUsername:(NSString *)username
                                             oldPassword:(NSString *)oldPwd
                                             newPassword:(NSString *)newPwd
                                                    mode:(NSString *)mode;

/**
 *  用户资料插入/更新
 *
 *  @param token 用户登录时生成的token。必填项，不允许为nil
 *  @param uid   用户id。必填项，不允许为nil
 *  @param item  用户资料项(长度length不超过1024)，标准base64编码，且为URLSafe模式。必填项，不允许为nil
 *  @param value 用户资料项对应的值()用户资料项(长度length不超过1024)，标准base64编码，且为URLSafe模式。必填项，不允许为nil
 *
 *  @return 请求对象
 */
+ (MOBAUserCenterRequest *) userProfilePutRequestByToken:(NSString *)token
                                              uid:(NSString *)uid
                                             item:(NSString *)item
                                            value:(NSString *)value;

/**
 *  查询用户资料
 *
 *  @param uid  用户id，用户注册时生成。必填项，不允许为nil
 *  @param item 用户资料项： 1、当为空时，返回该用户所有数据； 2、多个数据项用逗号分隔，每个数据项为Base64编码[url safe模式]
 *
 *  @return 请求对象
 */
+ (MOBAUserCenterRequest *) userProfileQueryRequestByUid:(NSString *)uid item:(NSString *)item;

/**
 *  删除用户资料项
 *
 *  @param token 用户登录时生成。必填项，不允许为nil
 *  @param uid   用户id。必填项，不允许为nil
 *  @param item  用户资料项，标准base64编码，且为URLSafe模式。必填项，不允许为nil
 *
 *  @return 请求对象
 */
+ (MOBAUserCenterRequest *) userProfileDeleteRequestByToken:(NSString *)token
                                                 uid:(NSString *)uid
                                                item:(NSString *)item;

/**
 *  用户数据插入/更新
 *
 *  @param token 用户登录时生成的token，必填项，不允许为nil
 *  @param uid   用户id，必填项，不允许为nil
 *  @param item  用户数据项(长度length不超过1024)，标准base64编码，且为URLSafe模式。必填项，不允许为nil
 *  @param value 用户数据项对应的值(长度length不超过1024)，标准base64编码，且为URLSafe模式。必填项，不允许为nil
 *
 *  @return 请求对象
 */
+ (MOBAUserCenterRequest *) userDataPutRequestByToken:(NSString *)token
                                           uid:(NSString *)uid
                                          item:(NSString *)item
                                         value:(NSString *)value;

/**
 *  用户数据查询
 *
 *  @param token 用户登录时生成的token，必填项，不允许为nil
 *  @param uid   用户id，必填项，不允许为nil
 *  @param item  用户数据项： 1、当为空时，返回该用户所有数据； 2、多个数据项用逗号分隔，每个数据项为Base64编码[url safe模式]
 *
 *  @return 请求对象
 */
+ (MOBAUserCenterRequest *) userDataQueryRequestByToken:(NSString *)token
                                             uid:(NSString *)uid
                                            item:(NSString *)item;

/**
 *  用户数据删除项
 *
 *  @param token 用户登录时生成的token，必填项，不允许为nil
 *  @param uid   用户id，必填项，不允许为nil
 *  @param item  用户数据项，标准base64编码，且为URLSafe模式。必填项，不允许为nil
 *
 *  @return 请求对象
 */
+ (MOBAUserCenterRequest *) userDataDeleteRequestByToken:(NSString *)token
                                              uid:(NSString *)uid
                                             item:(NSString *)item;

/**
 *  用户密码找回[获取随机码]
 *
 *  @param username 用户名。必填项，不允许为nil
 *
 *  @return 请求对象
 */
+ (MOBAUserCenterRequest *) userPasswordRetrieveRequestByUsername:(NSString *)username;

@end
