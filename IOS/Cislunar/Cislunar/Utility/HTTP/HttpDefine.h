//
//  HttpDefine.h
//  SmartHouseYCT
//
//  Created by apple on 16/9/20.
//  Copyright © 2016年 余长涛. All rights reserved.
//

#ifndef HttpDefine_h
#define HttpDefine_h

#define httpCommonHeader        [NSString stringWithFormat:@"http://%@",cloudIP]

//发送注册短信验证码
#define httpGetRegisterCode     @"/rest/v1/send_sms/register.json"

//注册
#define httpRegister            @"/rest/v1/register/phone.json"

//发送重置密码验证码
#define httpResetPswCode        @"/rest/v1/send_sms/reset_password.json"

//重置密码
#define httpResetPsw            @"/rest/v1/reset_password.json"

//用户登陆接口
#define httpLogin               @"/rest/v1/login.json"

//用户退出登录
#define httpLogOut              @"/rest/v1/unlogin.json"

//获取网关列表
#define httpGatewayList         @"/rest/v1/gateway/list.json"

//获取当前网关信息
#define httpGetCurrentGatewayInfo @"/rest/v1/gateway/info.json"

//添加网关
#define httpAddGateway          @"/rest/v1/gateway/add.json"

//删除网关
#define httpDeleteGateway       @"/rest/v1/gateway/delete.json"

//获取场景列表
#define httpGetScreenList       @"/rest/v1/scene/list.json"

//添加场景
#define httpAddScreen           @"/rest/v1/scene/add.json"

//更新场景
#define httpUpdateScreen        @"/rest/v1/scene/update.json"

//删除场景
#define httpDeleteScreen        @"/rest/v1/scene/delete.json"



//拉取楼层列表
#define httpGetFloorList        @"/rest/v1/floor/list.json"

//添加楼层列表
#define httpAddFloor            @"/rest/v1/floor/add.json"

//删除楼层
#define httpDeleteFloor         @"/rest/v1/floor/delete.json"

//修改楼层
#define httpModifyFloor         @"/rest/v1/floor/update.json"

//通过楼层id获取房间列表
#define httpGetRoomList         @"/rest/v1/room/list.json"

//增加房间
#define httpAddRoom             @"/rest/v1/room/add.json"

//删除房间
#define httpDeleteRoom          @"/rest/v1/room/delete.json"

//修改房间
#define httpUpdateRoom          @"/rest/v1/room/update.json"

//获取图片UI 类别：1 设备，2 房间，3场景
#define httpGetPic              @"/rest/v1/image/list.json?category="

//获取网关下所有设备信息
#define httpGetAllDeviceList        @"/rest/v1/device/all.json"
#define httpGetAllDeviceListV2        @"/rest/v2/device/all.json"

//根据roomID拉取设备列表
#define httpGetDeviceList       @"/rest/v1/device.json?room_id="

//添加设备
#define httpGetDeviceNew        @"/rest/v1/device/add.json"

//删除设备
#define httpDeleteDevice        @"/rest/v1/device/delete.json"

//更新设备
#define httpUpdateDevice        @"/rest/v1/device/update.json"

//添加红外设备按钮
#define httpAddInfraredBtn      @"/rest/v1/device/button.json"

//子账号

//获取家庭成员列表
#define httpGetMemberList   @"/rest/v1/gateway_user/list.json"

//添加家庭成员
#define httpAddMember       @"/rest/v1/gateway_user/add.json"

//删除家庭成员
#define httpDeleteMember    @"/rest/v1/gateway_user/delete.json"

//获取子账号的权限列表
#define httpGetMemberRightList  @"/rest/v1/gateway_user/permissions.json?member_id="

//添加子账号权限
#define httpAddMemberRight      @"/rest/v1/gateway_user/permissions/save.json"

//获取报警信息列表接口
#define httpGetAlarmInfo        @"/rest//v1/alarm/list.json?date="

//修改安防状态
#define httpModiftySecurity     @"/rest/v1/gateway/security.json"

//添加门锁密码
#define httpAddLockPsw          @"/rest/v1/gate_lock/add/psw.json"

//添加指纹用户
#define httpAddLockMember       @"/rest/v1/gate_lock/add/fingerprint.json"

//密码解锁
#define httpDoOpenLock          @"/rest/v1/gate_lock/unlock/psw.json"

//获取门锁用户列表ID
#define httpGetLockMemberListID   @"/rest/v1/gate_lock/list/lock_user.json?device_id="

//获取门锁用户列表Addr
#define httpGetLockMemberListAddr   @"/rest/v1/gate_lock/list/lock_user.json?mac_address="

//删除门锁用户/密码
#define httpDeleteLockMember    @"/rest/v1/gate_lock/delete.json"

//获取指纹密码列表
#define httpGetLockPswList      @"/rest/v1/gate_lock/list/psw.json?device_id="

//
#define httpPush                @"/rest/v1/push.json"

//获取网关用户详情
#define httpGetGateayMemberInfo @"/rest/v1/gateway/details.json?token=d6e63b481ac9a3371c1d6e38129cde70&mac="




#endif /* HttpDefine_h */
