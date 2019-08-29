package com.kzksmarthome.common.module.net;

/**
 *
 * 类/接口注释
 *
 * @author panrq
 * @createDate Dec 29, 2014
外网ip
 @"114.67.57.99"
 */
public class ApiHost {

    /**
     * 服务器地址-发布地址
     */
    public final static String DEFAULT_SERVER_HOST = "http://116.62.215.198";
    /**
     * 转发服务器地址
     */
    public final static String FORWARD_SERVER_HOST = "116.62.215.198";
    /**
     * 转发服务器端口
     */
    public final static String FORWARD_SERVER_PORT = "7001";
    /**
     * TCP连接IP地址
     */
    public static String TCP_IOT_HOST = "";
    /**
     * TCP连接的端口
     */
    public static int TCP_IOT_PORT;
    /**
     * 是否是远程连接
     */
    public static boolean NETWORK_ISREMOTE = false;

    public final static String CONTENTTYPE = "application/json";
    public static String TOKEN = "";
    /**
     * 删除楼层地址
     */
    public final static String DELETE_FLOOR_URL = "/rest/v1/floor/delete.json";
    /**
     * 添加楼层地址
     */
    public final static String ADD_FLOOR_URL = "/rest/v1/floor/add.json";
    /**
     * 通过id获取楼层信息地址
     */
    public final static String GET_FLOOR_INFO_URL = "/rest/v1/floor/%s.json";
    /**
     * 修改楼层地址
     */
    public final static String UPDATE_FLOOR_URL = "/rest/v1/floor/update.json";
    /**
     * 获取楼层列表地址
     */
    public final static String FLOOR_LIST_URL = "/rest/v1/floor/list.json";
    /**
     * 删除房间地址
     */
    public final static String DELETE_ROOM_URL = "/rest/v1/room/delete.json";
    /**
     * 添加房间地址
     */
    public final static String ADD_ROOM_URL = "/rest/v1/room/add.json";
    /**
     * 通过楼层id获取房间信息地址
     */
    public final static String GET_ROOM_LIST_URL = "/rest/v1/room/list.json?floor_id=%s";
    /**
     * 修改房间地址
     */
    public final static String UPDATE_ROOM_URL = "/rest/v1/room/update.json";
    /**
     * 发送注册短信地址
     */
    public final static String REGISTER_SMS_URL = "/rest/v1/send_sms/register.json";
    /**
     * 注册地址
     */
    public final static String REGISTER_URL = "/rest/v1/register/phone.json";
    /**
     * 登陆地址
     */
    public final static String LOGIN_URL = "/rest/v1/login.json";
    /**
     * 重置密码地址
     */
    public final static String RESET_PWD_URL = "/rest/v1/reset_password.json";
    /**
     * 重置密码验证码地址
     */
    public final static String RESET_PWD_SMS_URL = "/rest/v1/send_sms/reset_password.json";
    /**
     * 增加设备地址
     */
    public final static String ADD_DEVICE_URL = "/rest/v1/device/add.json";
    /**
     * 更新设备地址
     */
    public final static String UPDATE_DEVICE_URL = "/rest/v1/device/update.json";
    /**
     * 通过id删除设备地址
     */
    public final static String DELETE_DEVICE_URL = "/rest/v1/device/delete.json";
    /**
     * 获取报警信息
     */
    public final static String WARNING_INFO_URL="/rest//v1/alarm/list.json?date=";
    /**
     * 通过id获取设备地址
     */
    public final static String GET_DEVICE_BY_ID_URL = "/rest/v1/device/%s.json";
    /**
     * 通过roomid获取设备地址
     */
    public final static String GET_DEVICE_BY_ROOMID_URL = "/rest/v1/device.json?room_id=%s";
    /**
     * 添加红外设备按钮地址
     */
    public final static String ADD_DEVICE_BUTTON_URL = "/rest/v1/device/button.json";
    /**
     * 添加用户指纹地址
     */
    public final static String ADD_FINGERPRINT_URL = "/rest/v1/gate_lock/add/fingerprint.json";
    /**
     * 添加门锁密码地址
     */
    public final static String ADD_DOOR_PWD_URL = "/rest/v1/gate_lock/add/psw.json";
    /**
     * 密码解锁地址
     */
    public final static String UNLOCK_DOOR_PWD_URL = "/rest/v1/gate_lock/unlock/psw.json";
    /**
     * 指纹解锁地址
     */
    public final static String UNLOCK_DOOR_FINGER_URL = "/rest/v1/gate_lock/unlock/fingerprint.json";
    /**
     * 删除门锁指纹/密码地址
     */
    public final static String DELETE_FINGER_PWD_URL = "/rest/v1/gate_lock/delete.json";
    /**
     * 获取门锁用户地址
     */
    public final static String GET_LOCK_USER_URL = "/rest/v1/gate_lock/list/lock_user.json?device_id=%s";
    /**
     * 获取门锁用户地址 mac地址
     */
    public final static String GET_LOCK_USER_MAC_URL = "/rest/v1/gate_lock/list/lock_user.json?mac_address=%s";
    /**
     * 添加网关地址
     */
    public final static String ADD_GW_URL = "/rest/v1/gateway/add.json";
    /**
     * 获取网关列表地址
     */
    public final static String GET_GW_LIST_URL = "/rest/v1/gateway/list.json";
    /**
     * 添加家庭成员地址
     */
    public final static String ADD_USER_URL = "/rest/v1/gateway_user/add.json";
    /**
     * 获取家庭成员列表地址
     */
    public final static String GET_USER_LIST_URL = "/rest/v1/gateway_user/list.json";
    /**
     * 删除家庭成员地址
     */
    public final static String DELETE_USER_URL = "/rest/v1/gateway_user/delete.json";

    /**
     * 获取图片地址
     */
    public final static String GET_IMG_URL = "/rest/v1/image/list.json?category=%s";

    /**
     * 增加场景地址
     */
    public final static String ADD_SCENE_URL = "/rest/v1/scene/add.json";
    /**
     * 更新场景地址
     */
    public final static String UPDATE_SCENE_URL = "/rest/v1/scene/update.json";
    /**
     * 通过id删除场景地址
     */
    public final static String DELETE_SCENE_URL = "/rest/v1/scene/delete.json";
    /**
     * 获取场景列表地址
     */
    public final static String GET_SCENE_LIST_URL = "/rest/v1/scene/list.json";
    /**
     * 获取网关下所有设备地址
     */
    public final static String GET_ALL_DEVICE_URL = "/rest/v1/device/all.json";
    /**
     * 获取网关下所有设备地址
     */
    public final static String GET_ALL_DEVICE_URL_V2 = "/rest/v2/device/all.json";
    /**
     * 获取网关下所有红外按键指令
     */
    public final static String  GET_ALL_READ_ORDER_NUMBER = "/rest/v1/device/button/list.json";
    /**
     * 获取子账户权限详情地址
     */
    public final static String  GET_USER_RIGHTS_LIST = "/rest/v1/gateway_user/permissions.json?member_id=%s";
    /**
     * 添加子账户权限地址
     */
    public final static String ADD_USER_RIGHTS_URL = "/rest/v1/gateway_user/permissions/save.json";
    /**
     * 修改网关安防状态地址
     */
    public final static String UPDATE_GW_SECURITY_URL = "/rest/v1/gateway/security.json";
    /**
     * 获取网关信息地址
     */
    public final static String GET_GW_INFO_URL = "/rest/v1/gateway/info.json";
    /**
     * push上报地址
     */
    public final static String PUSH_REPORT_URL = "/rest/v1/push.json";
    /**
     * 获取指纹密码列表地址
     */
    public final static String GET_LOCK_PWD_LIST_URL = "/rest/v1/gate_lock/list/psw.json?device_id=%s";
    /**
     * 删除网关地址
     */
    public final static String DELETE_GW_URL = "/rest/v1/gateway/delete.json";
    /**
     * 退出登录地址
     */
    public final static String LOGOUT_URL = "/rest/v1/unlogin.json";

}
