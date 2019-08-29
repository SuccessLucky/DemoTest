package com.kzksmarthome.SmartHouseYCT.biz.smart.http;

import android.content.Context;
import android.text.TextUtils;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceButtonInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.TvRedOrderNumberRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.TvRedOrderNumberResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.home.WarningInfoRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.home.WarningInfoResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.AddButtonsRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.AddDeviceBean;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.AddDeviceRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.AddDoorPwdRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.AddFingerRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.AddFloorRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.AddGwRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.AddRedButtonRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.AddRoomRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.AddSceneRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.AddUserRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.AddUserRightsRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.DelDeviceRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.DelFingerPwdRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.DelFloorRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.DelGwRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.DelMacDeviceRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.DelRoomRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.DelSceneRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.DelUserRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.GetAllDeviceListRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.GetDeviceByIdRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.GetDeviceListRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.GetDoorLockUserListRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.GetFloorInfoRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.GetFloorListRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.GetGwInfoRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.GetGwListRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.GetImgListRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.GetLockPwdListRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.GetRightsListRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.GetRoomListRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.GetUserListRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.LoginRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.LogoutRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.OpenDoorbyFingerRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.OpenDoorbyPwdRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.PushReportRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.RegisterRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.RegisterSmsRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.ResetPwdRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.ResetPwdSmsRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.UpdateDeviceRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.UpdateFloorRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.UpdateGwSecurityRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.UpdateRoomRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.requestparam.UpdateSceneRequestParam;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddButtonsResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddDeviceResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddDoorPwdResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddFingerResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddFloorResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddGwResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddRedButtonResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddRoomResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddSceneResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddUserResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddUserRightsResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.DelFingerPwdResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.DelUserResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.DeleteDeviceResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.DeleteFloorResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.DeleteGwResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.DeleteRoomResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.DeleteSceneResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetAllDeviceListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetAllDeviceListResponse1;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetDeviceByIdResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetDeviceListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetDoorLockUserListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetFloorInfoResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetFloorListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetGatewayInfoResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetGwListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetImageListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetLockPwdListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetRightsListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetRoomListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetSceneListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetUserListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.LoginResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.LogoutResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.OpenDoorbyFingerResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.OpenDoorbyPwdResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.PushReportResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.RegisterResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.RegisterSmsResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.ResetPwdResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.ResetPwdSmsResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.UpdateDeviceResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.UpdateFloorResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.UpdateGwSecurityResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.UpdateRoomResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.UpdateSceneResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.SceneInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.SimpleSceneDetailInfo;
import com.kzksmarthome.common.lib.okhttp.CacheControlMode;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.RequestMgr;
import com.kzksmarthome.common.lib.task.BackgroundTaskExecutor;
import com.kzksmarthome.common.lib.tcp.SocketRequest;
import com.kzksmarthome.common.lib.tcp.TCPMgr;
import com.kzksmarthome.common.lib.tools.BusinessTool;
import com.kzksmarthome.common.lib.tools.Tools;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.net.ApiHost;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.srain.cube.util.Encrypt;

public class RestRequestApi {

    /**
     * 发送注册验证码
     *
     * @param context
     * @param phone
     * @param callback
     */
    public static void registerSms(Context context, String phone, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        RegisterSmsRequestParam param = new RegisterSmsRequestParam();
        param.setPhone(phone);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.REGISTER_SMS_URL, mode, param, RegisterSmsRequestParam.class, RegisterSmsResponse.class, callback);
    }

    /**
     * 注册
     *
     * @param context
     * @param phone
     * @param code
     * @param password
     * @param nickname
     * @param callback
     */
    public static void register(Context context, String phone, String code, String password, String nickname, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        password = Encrypt.md5(password);
        RegisterRequestParam param = new RegisterRequestParam();
        param.setPhone(phone);
        param.setCode(code);
        param.setPassword(password);
        param.setNickname(nickname);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.REGISTER_URL, mode, param, RegisterRequestParam.class, RegisterResponse.class, callback);
    }

    /**
     * 登陆
     *
     * @param context
     * @param username
     * @param password
     * @param callback
     */
    public static void login(Context context, String username, String password, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        //password = Encrypt.md5(password);
        LoginRequestParam param = new LoginRequestParam();
        param.setUsername(username);
        param.setPassword(password);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.LOGIN_URL, mode, param, LoginRequestParam.class, LoginResponse.class, callback);
    }

    /**
     * 发送重置密码短信
     *
     * @param context
     * @param phone
     * @param callback
     */
    public static void resetPwdSms(Context context, String phone, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        ResetPwdSmsRequestParam param = new ResetPwdSmsRequestParam();
        param.setPhone(phone);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.RESET_PWD_SMS_URL, mode, param, ResetPwdSmsRequestParam.class, ResetPwdSmsResponse.class, callback);
    }

    /**
     * 重置密码
     *
     * @param context
     * @param phone
     * @param code
     * @param password
     * @param callback
     */
    public static void resetPwd(Context context, String phone, String code, String password, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        password = Encrypt.md5(password);
        ResetPwdRequestParam param = new ResetPwdRequestParam();
        param.setPhone(phone);
        param.setCode(code);
        param.setNew_password(password);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.RESET_PWD_URL, mode, param, ResetPwdRequestParam.class, ResetPwdResponse.class, callback);
    }

    /**
     * 根据id获取楼层信息
     *
     * @param context
     * @param id
     * @param callback
     */
    public static void getFloorInfo(Context context, int id, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_CACHE, CacheControlMode.DEFAULT_LOCAL_CACHE_TIME_WEEK);
        GetFloorInfoRequestParam param = new GetFloorInfoRequestParam();
        RequestMgr.execute(RequestMgr.Method.GET, String.format(ApiHost.GET_FLOOR_INFO_URL, id), mode, param, GetFloorInfoRequestParam.class, GetFloorInfoResponse.class, callback);
    }

    /**
     * 获取楼层列表
     *
     * @param context
     * @param callback
     */
    public static void getFloorList(Context context, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_CACHE, CacheControlMode.DEFAULT_LOCAL_CACHE_TIME_WEEK);
        GetFloorListRequestParam param = new GetFloorListRequestParam();
        RequestMgr.execute(RequestMgr.Method.GET, ApiHost.FLOOR_LIST_URL, mode, param, GetFloorListRequestParam.class, GetFloorListResponse.class, callback);
    }

    /**
     * 获取网关下所有红外按键指令
     *
     * @param context
     * @param callback
     */
    public static void getAllRedOrderNumber(Context context, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_CACHE, CacheControlMode.DEFAULT_LOCAL_CACHE_TIME_WEEK);
        TvRedOrderNumberRequestParam param = new TvRedOrderNumberRequestParam();
        RequestMgr.execute(RequestMgr.Method.GET, ApiHost.GET_ALL_READ_ORDER_NUMBER, mode, param, TvRedOrderNumberRequestParam.class, TvRedOrderNumberResponse.class, callback);
    }


    /**
     * 添加楼层
     *
     * @param context
     * @param floor_name
     * @param image
     * @param callback
     */
    public static void addFloor(Context context, String floor_name, String image, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        AddFloorRequestParam param = new AddFloorRequestParam();
        param.setFloor_name(floor_name);
        param.setImage(image);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.ADD_FLOOR_URL, mode, param, AddFloorRequestParam.class, AddFloorResponse.class, callback);
    }

    /**
     * 添加红外按钮
     *
     * @param context
     * @param deviceId
     * @param cmdList
     * @param callback
     */
    public static void addRedButton(Context context, int deviceId, List<DeviceButtonInfo> cmdList, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        AddRedButtonRequestParam param = new AddRedButtonRequestParam();
        param.setDevice_id(deviceId);
        if (null == cmdList) {
            cmdList = new ArrayList<DeviceButtonInfo>();
        }
        param.setDevice_buttons(cmdList);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.ADD_DEVICE_BUTTON_URL, mode, param, AddRedButtonRequestParam.class, AddRedButtonResponse.class, callback);
    }

    /**
     * 修改楼层
     *
     * @param context
     * @param floor_id
     * @param floor_name
     * @param image
     * @param callback
     */
    public static void updateFloor(Context context, int floor_id, String floor_name, String image, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        UpdateFloorRequestParam param = new UpdateFloorRequestParam();
        param.setFloor_id(floor_id);
        param.setFloor_name(floor_name);
        param.setImage(image);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.UPDATE_FLOOR_URL, mode, param, UpdateFloorRequestParam.class, UpdateFloorResponse.class, callback);
    }

    /**
     * 删除楼层
     *
     * @param context
     * @param floor_id
     * @param callback
     */
    public static void deleteFloor(Context context, int floor_id, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        DelFloorRequestParam param = new DelFloorRequestParam();
        param.setFloor_id(floor_id);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.DELETE_FLOOR_URL, mode, param, DelFloorRequestParam.class, DeleteFloorResponse.class, callback);
    }

    /**
     * 根据楼层id获取房间列表
     *
     * @param context
     * @param callback
     */
    public static void getRoomListByFloorId(Context context, int floor_id, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_CACHE, CacheControlMode.DEFAULT_LOCAL_CACHE_TIME_WEEK);
        GetRoomListRequestParam param = new GetRoomListRequestParam();
        RequestMgr.execute(RequestMgr.Method.GET, String.format(ApiHost.GET_ROOM_LIST_URL, floor_id), mode, param, GetRoomListRequestParam.class, GetRoomListResponse.class, callback);
    }

    /**
     * 添加房间
     *
     * @param context
     * @param floor_id
     * @param room_name
     * @param image
     * @param callback
     */
    public static void addRoom(Context context, int floor_id, String room_name, String image, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        AddRoomRequestParam param = new AddRoomRequestParam();
        param.setFloor_id(floor_id);
        param.setRoom_name(room_name);
        param.setImage(image);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.ADD_ROOM_URL, mode, param, AddRoomRequestParam.class, AddRoomResponse.class, callback);
    }

    /**
     * 修改房间
     *
     * @param context
     * @param room_id
     * @param room_name
     * @param image
     * @param callback
     */
    public static void updateRoom(Context context, int room_id, String room_name, String image, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        UpdateRoomRequestParam param = new UpdateRoomRequestParam();
        param.setRoom_id(room_id);
        param.setRoom_name(room_name);
        param.setImage(image);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.UPDATE_ROOM_URL, mode, param, UpdateRoomRequestParam.class, UpdateRoomResponse.class, callback);
    }

    /**
     * 删除房间
     *
     * @param context
     * @param room_id
     * @param callback
     */
    public static void deleteRoom(Context context, int room_id, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        DelRoomRequestParam param = new DelRoomRequestParam();
        param.setRoom_id(room_id);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.DELETE_ROOM_URL, mode, param, DelRoomRequestParam.class, DeleteRoomResponse.class, callback);
    }

    /**
     * 增加设备
     *
     * @param context
     * @param deviceInfoList
     * @param callback
     */
    public static void addDevice(Context context, ArrayList<DeviceInfo> deviceInfoList, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        if (Util.isListEmpty(deviceInfoList)) {
            return;
        }
        AddDeviceRequestParam param = new AddDeviceRequestParam();
        ArrayList<AddDeviceBean> list = new ArrayList<AddDeviceBean>();
        for (DeviceInfo deviceInfo : deviceInfoList) {
            AddDeviceBean bean = new AddDeviceBean();
            bean.setRoom_id(deviceInfo.getRoom_id());
            bean.setDevice_name(deviceInfo.getDevice_name());
            bean.setImage(deviceInfo.getImage());
            bean.setDevice_OD(deviceInfo.getDevice_OD());
            bean.setCmd_id(deviceInfo.getCmdId());
            bean.setDevice_type(deviceInfo.getDevice_type());
            bean.setCategory(deviceInfo.getCategory());
            bean.setMac_address(deviceInfo.getMac_address());
            bean.setSindex(deviceInfo.getSindex());
            bean.setSindex_length(deviceInfo.getSindex_length());
            bean.setOther_status(deviceInfo.getOther_status());
            bean.setDevice_state1(deviceInfo.getDevice_state1());
            bean.setDevice_state2(deviceInfo.getDevice_state2());
            bean.setDevice_state3(deviceInfo.getDevice_state3());
            list.add(bean);
        }
        param.setDevices(list);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.ADD_DEVICE_URL, mode, param, AddDeviceRequestParam.class, AddDeviceResponse.class, callback);
    }

    /**
     * 更新设备
     *
     * @param context
     * @param deviceInfo
     * @param callback
     */
    public static void updateDevice(Context context, DeviceInfo deviceInfo, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        UpdateDeviceRequestParam param = new UpdateDeviceRequestParam();
        param.setDevice_id(deviceInfo.getDevice_id());
        param.setDevice_name(deviceInfo.getDevice_name());
        param.setImage(deviceInfo.getImage());
        param.setOther_state(deviceInfo.getOther_status());
        param.setDevice_state1(deviceInfo.getDevice_state1());
        param.setDevice_state2(deviceInfo.getDevice_state2());
        param.setDevice_state3(deviceInfo.getDevice_state3());
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.UPDATE_DEVICE_URL, mode, param, UpdateDeviceRequestParam.class, UpdateDeviceResponse.class, callback);
    }

    /**
     * 删除设备
     *
     * @param context
     * @param device_id
     * @param callback
     */
    public static void deleteDevice(Context context, int device_id,String mac, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        if(mac == null) {
            DelDeviceRequestParam param = new DelDeviceRequestParam();
            param.setDevice_id(device_id);
            RequestMgr.execute(RequestMgr.Method.POST, ApiHost.DELETE_DEVICE_URL, mode, param, DelDeviceRequestParam.class, DeleteDeviceResponse.class, callback);
        }else{
            DelMacDeviceRequestParam param = new DelMacDeviceRequestParam();
            param.setMac_address(mac);
            RequestMgr.execute(RequestMgr.Method.POST, ApiHost.DELETE_DEVICE_URL, mode, param, DelMacDeviceRequestParam.class, DeleteDeviceResponse.class, callback);
        }
    }

    /**
     * 获取告警信息
     *
     * @param context
     * @param dateStr
     * @param callback
     */
    public static void getWarningInfo(Context context, String dateStr, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_CACHE, CacheControlMode.DEFAULT_LOCAL_CACHE_TIME_WEEK);
        WarningInfoRequestParam param = new WarningInfoRequestParam();
        param.setDateStr(dateStr);
        RequestMgr.execute(RequestMgr.Method.GET, ApiHost.WARNING_INFO_URL+dateStr, mode, param, WarningInfoRequestParam.class, WarningInfoResponse.class, callback);
    }

    /**
     * 根据设备id获取设备
     *
     * @param context
     * @param id
     * @param callback
     */
    public static void getDeviceById(Context context, int id, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_CACHE, CacheControlMode.DEFAULT_LOCAL_CACHE_TIME_WEEK);
        GetDeviceByIdRequestParam param = new GetDeviceByIdRequestParam();
        RequestMgr.execute(RequestMgr.Method.GET, String.format(ApiHost.GET_DEVICE_BY_ID_URL, id), mode, param, GetDeviceByIdRequestParam.class, GetDeviceByIdResponse.class, callback);
    }

    /**
     * 根据roomid获取设备
     *
     * @param context
     * @param room_id
     * @param callback
     */
    public static void getDeviceByRoomId(Context context, int room_id, RequestCallback callback) {
        CacheControlMode  mode = new CacheControlMode(CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_CACHE, CacheControlMode.DEFAULT_LOCAL_CACHE_TIME_WEEK);
        GetDeviceListRequestParam param = new GetDeviceListRequestParam();
        RequestMgr.execute(RequestMgr.Method.GET, String.format(ApiHost.GET_DEVICE_BY_ROOMID_URL, room_id), mode, param, GetDeviceListRequestParam.class, GetDeviceListResponse.class, callback);
    }

    /**
     * 添加红外设备按钮
     *
     * @param context
     * @param device_id
     * @param buttons
     * @param callback
     */
    public static void addDeviceButton(Context context, int device_id, List<DeviceButtonInfo> buttons, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        AddButtonsRequestParam param = new AddButtonsRequestParam();
        param.setDevice_id(device_id);
        param.setDevice_buttons(buttons);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.ADD_DEVICE_BUTTON_URL, mode, param, AddButtonsRequestParam.class, AddButtonsResponse.class, callback);
    }

    /**
     * 添加家庭成员
     *
     * @param context
     * @param phone
     * @param callback
     */
    public static void addFamilyUser(Context context, String phone, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        AddUserRequestParam param = new AddUserRequestParam();
        param.setPhone(phone);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.ADD_USER_URL, mode, param, AddUserRequestParam.class, AddUserResponse.class, callback);
    }

    /**
     * 删除家庭成员
     *
     * @param context
     * @param member_id
     * @param callback
     */
    public static void delFamilyUser(Context context, int member_id, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        DelUserRequestParam param = new DelUserRequestParam();
        param.setMember_id(member_id);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.DELETE_USER_URL, mode, param, DelUserRequestParam.class, DelUserResponse.class, callback);
    }

    /**
     * 获取家庭成员列表
     *
     * @param context
     * @param username
     * @param password
     * @param name
     * @param callback
     */
    public static void getFamilyUserList(Context context, String username, String password, String name, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_CACHE, CacheControlMode.DEFAULT_LOCAL_CACHE_TIME_WEEK);
        GetUserListRequestParam param = new GetUserListRequestParam();
        param.setUsername(username);
        param.setPassword(password);
        param.setName(name);
        RequestMgr.execute(RequestMgr.Method.GET, ApiHost.GET_USER_LIST_URL, mode, param, GetUserListRequestParam.class, GetUserListResponse.class, callback);
    }

    /**
     * 添加门锁密码
     *
     * @param context
     * @param device_id
     * @param unlock_psw
     * @param callback
     */
    public static void addDoorPwd(Context context, int device_id, String unlock_psw, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        AddDoorPwdRequestParam param = new AddDoorPwdRequestParam();
        param.setDevice_id(device_id);
        param.setUnlock_psw(unlock_psw);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.ADD_DOOR_PWD_URL, mode, param, AddDoorPwdRequestParam.class, AddDoorPwdResponse.class, callback);
    }

    /**
     * 添加门锁指纹
     *
     * @param context
     * @param device_id
     * @param user_name
     * @param fingerprint_id
     * @param callback
     */
    public static void addDoorFinger(Context context, int device_id, String user_name, String fingerprint_id, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        AddFingerRequestParam param = new AddFingerRequestParam();
        param.setDevice_id(device_id);
        param.setUser_name(user_name);
        param.setFingerprint_id(fingerprint_id);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.ADD_FINGERPRINT_URL, mode, param, AddFingerRequestParam.class, AddFingerResponse.class, callback);
    }

    /**
     * 删除门锁指纹/密码
     *
     * @param context
     * @param lock_id
     * @param lock_type
     * @param callback
     */
    public static void delFingerorPwd(Context context, int lock_id, String lock_type, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        DelFingerPwdRequestParam param = new DelFingerPwdRequestParam();
        param.setLock_id(lock_id);
        param.setLock_type(lock_type);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.DELETE_FINGER_PWD_URL, mode, param, DelFingerPwdRequestParam.class, DelFingerPwdResponse.class, callback);
    }

    /**
     * 获取门锁用户列表
     * @param context
     * @param device_id
     * @param callback
     */
    public static void getDoorUserList(Context context, int device_id,String mac, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_CACHE, CacheControlMode.DEFAULT_LOCAL_CACHE_TIME_WEEK);
        GetDoorLockUserListRequestParam param = new GetDoorLockUserListRequestParam();
        String url = null;
        if(mac == null){
            url =  String.format(ApiHost.GET_LOCK_USER_URL, device_id);
        }else{
            url =  String.format(ApiHost.GET_LOCK_USER_MAC_URL, mac);
        }
        RequestMgr.execute(RequestMgr.Method.GET,url, mode, param, GetDoorLockUserListRequestParam.class, GetDoorLockUserListResponse.class, callback);
    }

    /**
     * 指纹解锁
     *
     * @param context
     * @param device_id
     * @param fingerprint_id
     * @param callback
     */
    public static void unlockbyFinger(Context context, int device_id, String fingerprint_id, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        OpenDoorbyFingerRequestParam param = new OpenDoorbyFingerRequestParam();
        param.setDevice_id(device_id);
        param.setFingerprint_id(fingerprint_id);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.UNLOCK_DOOR_FINGER_URL, mode, param, OpenDoorbyFingerRequestParam.class, OpenDoorbyFingerResponse.class, callback);
    }

    /**
     * 密码解锁
     *
     * @param context
     * @param device_id
     * @param unlock_psw
     * @param callback
     */
    public static void unlockbyPwd(Context context, int device_id, String unlock_psw, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        OpenDoorbyPwdRequestParam param = new OpenDoorbyPwdRequestParam();
        param.setDevice_id(device_id);
        param.setUnlock_psw(unlock_psw);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.DELETE_FINGER_PWD_URL, mode, param, OpenDoorbyPwdRequestParam.class, OpenDoorbyPwdResponse.class, callback);
    }

    /**
     * 获取图片
     *
     * @param context
     * @param category
     * @param callback
     */
    public static void getImageList(Context context, int category, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_CACHE, CacheControlMode.DEFAULT_LOCAL_CACHE_TIME_WEEK);
        GetImgListRequestParam param = new GetImgListRequestParam();
        RequestMgr.execute(RequestMgr.Method.GET, String.format(ApiHost.GET_IMG_URL, category), mode, param, GetImgListRequestParam.class, GetImageListResponse.class, callback);
    }

    /**
     * 添加网关
     *
     * @param context
     * @param gateway_name
     * @param mac_address
     * @param callback
     */
    public static void addGateway(Context context, String gateway_name, String mac_address, String wifi_mac_address, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        AddGwRequestParam param = new AddGwRequestParam();
        param.setGateway_name(gateway_name);
        param.setMac_address(mac_address);
        param.setWifi_mac_address(wifi_mac_address);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.ADD_GW_URL, mode, param, AddGwRequestParam.class, AddGwResponse.class, callback);
    }

    /**
     * 获取网关列表
     *
     * @param context
     * @param callback
     */
    public static void getGwList(Context context, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_CACHE, CacheControlMode.DEFAULT_LOCAL_CACHE_TIME_WEEK);
        GetGwListRequestParam param = new GetGwListRequestParam();
        RequestMgr.execute(RequestMgr.Method.GET, ApiHost.GET_GW_LIST_URL, mode, param, GetGwListRequestParam.class, GetGwListResponse.class, callback);
    }

    /**
     * 获取场景列表
     *
     * @param context
     * @param type
     * @param callback
     */
    public static void getSceneList(Context context, int type, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_CACHE, CacheControlMode.DEFAULT_LOCAL_CACHE_TIME_WEEK);
        GetRoomListRequestParam param = new GetRoomListRequestParam();
        String url = ApiHost.GET_SCENE_LIST_URL;
        if (type > -1) {
            url = ApiHost.GET_SCENE_LIST_URL + "?type=" + type;
        }
        RequestMgr.execute(RequestMgr.Method.GET, url, mode, param, GetRoomListRequestParam.class, GetSceneListResponse.class, callback);
    }

    /**
     * 添加场景
     *
     * @param context
     * @param scene_id
     * @param scene_type
     * @param scene_name
     * @param image
     * @param callback
     */
    public static void addScene(Context context,String serial_number, int scene_id, int scene_type, String scene_name, String image,SceneInfo sceneInfo,
                                List<SimpleSceneDetailInfo> details, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        AddSceneRequestParam param = new AddSceneRequestParam();
        param.setScene_id(scene_id);
        param.setScene_type(scene_type);
        param.setName(scene_name);
        param.setImage(image);
        param.setSerial_number(serial_number);
        param.setScene_details(details);
        param.setNeed_linkage(sceneInfo.getNeed_linkage());
        param.setNeed_time_delay(sceneInfo.getNeed_time_delay());
        param.setNeed_timing(sceneInfo.getNeed_timing());
        param.setNeed_security_off(sceneInfo.getNeed_security_off());
        param.setNeed_security_on(sceneInfo.getNeed_security_on());
        param.setDelay_time(sceneInfo.getDelay_time());
        param.setTiming_time(sceneInfo.getTiming_time());
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.ADD_SCENE_URL, mode, param, AddSceneRequestParam.class, AddSceneResponse.class, callback);
    }

    /**
     * 修改场景
     *
     * @param context
     * @param scene_id
     * @param scene_type
     * @param scene_name
     * @param image
     * @param callback
     * @param serial_number
     */
    public static void updateScene(Context context,String serial_number, int scene_id, int scene_type, String scene_name, String image,SceneInfo sceneInfo,
                                   List<SimpleSceneDetailInfo> details, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        UpdateSceneRequestParam param = new UpdateSceneRequestParam();
        param.setScene_id(scene_id);
        param.setScene_type(scene_type);
        param.setName(scene_name);
        param.setImage(image);
        param.setSerial_number(serial_number);
        param.setScene_details(details);
        param.setNeed_linkage(sceneInfo.getNeed_linkage());
        param.setNeed_time_delay(sceneInfo.getNeed_time_delay());
        param.setNeed_timing(sceneInfo.getNeed_timing());
        param.setNeed_security_off(sceneInfo.getNeed_security_off());
        param.setNeed_security_on(sceneInfo.getNeed_security_on());
        param.setDelay_time(sceneInfo.getDelay_time());
        param.setTiming_time(sceneInfo.getTiming_time());
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.UPDATE_SCENE_URL, mode, param, UpdateSceneRequestParam.class, UpdateSceneResponse.class, callback);
    }

    /**
     * 删除场景
     *
     * @param context
     * @param scene_id
     * @param callback
     */
    public static void deleteScene(Context context, int scene_id, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        DelSceneRequestParam param = new DelSceneRequestParam();
        param.setScene_id(scene_id);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.DELETE_SCENE_URL, mode, param, DelSceneRequestParam.class, DeleteSceneResponse.class, callback);
    }

    /**
     * 根据roomid获取设备
     *
     * @param context
     * @param callback
     */
    public static void getAllDeviceList(Context context, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_CACHE, CacheControlMode.DEFAULT_LOCAL_CACHE_TIME_WEEK);
        GetAllDeviceListRequestParam param = new GetAllDeviceListRequestParam();
        RequestMgr.execute(RequestMgr.Method.GET, ApiHost.GET_ALL_DEVICE_URL, mode, param, GetAllDeviceListRequestParam.class, GetAllDeviceListResponse.class, callback);
    }

    /**
     * 根据roomid获取设备
     *
     * @param context
     * @param callback
     */
    public static void getAllDeviceList1(Context context, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_CACHE, CacheControlMode.DEFAULT_LOCAL_CACHE_TIME_WEEK);
        GetAllDeviceListRequestParam param = new GetAllDeviceListRequestParam();
        RequestMgr.execute(RequestMgr.Method.GET, ApiHost.GET_ALL_DEVICE_URL, mode, param, GetAllDeviceListRequestParam.class, GetAllDeviceListResponse1.class, callback);
    }

    /**
     * 添加子账户权限
     *
     * @param context
     * @param member_id
     * @param scenes
     * @param devices
     * @param callback
     */
    public static void addUserRights(Context context, int member_id, List<SceneInfo> scenes,
                                     List<DeviceInfo> devices, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        AddUserRightsRequestParam param = new AddUserRightsRequestParam();
        param.setMember_id(member_id);
        List<Integer> sceneIds = new ArrayList<Integer>();
        for (SceneInfo scene : scenes) {
            sceneIds.add(scene.getScene_id());
        }
        List<Integer> deviceIds = new ArrayList<Integer>();
        for (DeviceInfo device : devices) {
            deviceIds.add(device.getDevice_id());
        }
        param.setScenes(sceneIds);
        param.setDevices(deviceIds);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.ADD_USER_RIGHTS_URL, mode, param, AddUserRightsRequestParam.class, AddUserRightsResponse.class, callback);
    }

    /**
     * 获取子账户权限列表
     *
     * @param context
     * @param callback
     */
    public static void getUserRightsList(Context context, int member_id, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_CACHE, CacheControlMode.DEFAULT_LOCAL_CACHE_TIME_WEEK);
        GetRightsListRequestParam param = new GetRightsListRequestParam();
        RequestMgr.execute(RequestMgr.Method.GET, String.format(ApiHost.GET_USER_RIGHTS_LIST, member_id), mode, param, GetRightsListRequestParam.class, GetRightsListResponse.class, callback);
    }

    /**
     * 修改网关安防状态
     *
     * @param context
     * @param security_status
     * @param callback
     */
    public static void updateGwSecurity(Context context, int security_status, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        UpdateGwSecurityRequestParam param = new UpdateGwSecurityRequestParam();
        param.setSecurity_status(security_status);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.UPDATE_GW_SECURITY_URL, mode, param, UpdateGwSecurityRequestParam.class, UpdateGwSecurityResponse.class, callback);
    }

    /**
     * 获取当前网关信息
     *
     * @param context
     * @param callback
     */
    public static void getGatewayInfo(Context context, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_CACHE, CacheControlMode.DEFAULT_LOCAL_CACHE_TIME_WEEK);
        GetGwInfoRequestParam param = new GetGwInfoRequestParam();
        RequestMgr.execute(RequestMgr.Method.GET, ApiHost.GET_GW_INFO_URL, mode, param, GetGwInfoRequestParam.class, GetGatewayInfoResponse.class, callback);
    }

    /**
     * 推送上报
     *
     * @param context
     * @param cid
     * @param callback
     */
    public static void pushReport(Context context, String cid, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        PushReportRequestParam param = new PushReportRequestParam();
        param.uuid = cid;
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.PUSH_REPORT_URL, mode, param, PushReportRequestParam.class, PushReportResponse.class, callback);
    }

    /**
     * 获取指纹密码列表
     *
     * @param context
     * @param device_id
     * @param callback
     */
    public static void getLockPwdList(Context context, int device_id, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_LOCAL_FIRST_REMOTE_CACHE, 0);
        GetLockPwdListRequestParam param = new GetLockPwdListRequestParam();
        RequestMgr.execute(RequestMgr.Method.GET, String.format(ApiHost.GET_LOCK_PWD_LIST_URL, device_id), mode, param, GetLockPwdListRequestParam.class, GetLockPwdListResponse.class, callback);
    }

    /**
     * 布防
     */
    public static void contorBF(Context context, RequestCallback callback,String srcAdd) {
        try {
                SocketRequest request = new SocketRequest();
                byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
                byte[] senMsg = BusinessTool.getInstance().buFangOrCheFang(srcMac, (byte) 1);
                request.requestData = senMsg;
                TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }


    /**
     * 撤防
     */
    public static void contorCF(Context context, RequestCallback callback,String srcAdd) {
        try {
                SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
                byte[] senMsg = BusinessTool.getInstance().buFangOrCheFang(srcMac, (byte) 2);
                request.requestData = senMsg;
                TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 解除告警
     * @param context
     * @param srcAdd
     */
    public static void liftAlarm(Context context,String srcAdd){
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] senMsg = BusinessTool.getInstance().liftAlarm(srcMac);
            request.requestData = senMsg;
            TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 给网关对时
     * @param srcAdd
     */
    public static void setIOTTime(final String srcAdd){
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int week = cal.get(Calendar.DAY_OF_WEEK);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int mint = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);

            byte[] timeByte = new byte[7];
            timeByte[0] = Byte.valueOf(Integer.toString(year).substring(2,4));
            timeByte[1] = (byte) month;
            timeByte[2] = (byte)day;
            timeByte[3] = (byte)week;
            timeByte[4] = (byte)hour;
            timeByte[5] = (byte)mint;
            timeByte[6] = (byte)second;
            byte[] senMsg = BusinessTool.getInstance().setIOTTime(srcMac,timeByte);
            request.requestData = senMsg;
            TCPMgr.getInstance().sendRequest(request);
            BackgroundTaskExecutor.scheduleTask(1000, new Runnable() {
                @Override
                public void run() {
                    getIOTTime(srcAdd);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }
    /**
    *  获取网关时间
     * @param srcAdd
     */
    public static void getIOTTime(String srcAdd){
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] senMsg = BusinessTool.getInstance().getIOTTime(srcMac);
            request.requestData = senMsg;
            TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 控制一路
     *
     * @param srcAdd
     * @param dstAdd
     * @param control
     */
    public static void contorOneWay(String srcAdd, String dstAdd, byte control) {
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAdd);
            //byte[] senMsg = BusinessTool.getInstance().controlMsg(srcMac, dstMac, (byte) 0x01, (byte) 0x01, control, (byte) 0x00);
            byte[] senMsg = BusinessTool.getInstance().control4010Msg(srcMac, dstMac, (byte) 0x01, (byte) 0x01, control, (byte) 0x00);
            request.requestData = senMsg;
            TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 获取1路灯控制命令
     * @param srcAdd
     * @param dstAdd
     * @param control
     * @return
     */
    public static byte[] getContorOneWay(String srcAdd, String dstAdd, byte control) {
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAdd);
            //byte[] senMsg = BusinessTool.getInstance().controlMsg(srcMac, dstMac, (byte) 0x01, (byte) 0x01, control, (byte) 0x00);
            byte[] senMsg = BusinessTool.getInstance().control4010Msg(srcMac, dstMac, (byte) 0x01, (byte) 0x01, control, (byte) 0x00);
            return senMsg;
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
        return null;
    }

    /**
     * 多路控制器
     *
     * @param srcAdd
     * @param dstAdd
     * @param way
     * @param controlMode
     * @param control
     * @param delayTime
     */
    public static void contorMoreWay(String srcAdd, String dstAdd, byte way, byte controlMode, byte control, byte delayTime) {
        try {
                SocketRequest request = new SocketRequest();
                byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
                byte[] dstMac = Tools.hexStr2Bytes(dstAdd);
                //byte[] senMsg = BusinessTool.getInstance().controlMsg(srcMac, dstMac, way, controlMode, control, delayTime);
                byte[] senMsg = BusinessTool.getInstance().control4010Msg(srcMac, dstMac, way, controlMode, control, delayTime);
                request.requestData = senMsg;
                TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 控制4040设备
     * @param srcAdd
     * @param dstAdd
     * @param control
     */
    public static void contorl4040(String srcAdd ,String dstAdd,byte control){
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAdd);
            byte[] senMsg = BusinessTool.getInstance().control4040(srcMac, dstMac, control);
            request.requestData = senMsg;
            TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 获取控制4040设备
     * @param srcAdd
     * @param dstAdd
     * @param control
     */
    public static byte[] getcontorl4040(String srcAdd ,String dstAdd,byte control){
        try {
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAdd);
            byte[] senMsg = BusinessTool.getInstance().control4040(srcMac, dstMac, control);
            return senMsg;
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
        return null;
    }

    /**
     * 获取多路控制命令
     * @param srcAdd
     * @param dstAdd
     * @param way
     * @param controlMode
     * @param control
     * @param delayTime
     * @return
     */
    public static byte[] getContorMoreWay(String srcAdd, String dstAdd, byte way, byte controlMode, byte control, byte delayTime) {
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAdd);
            //byte[] senMsg = BusinessTool.getInstance().controlMsg(srcMac, dstMac, way, controlMode, control, delayTime);
            byte[] senMsg = BusinessTool.getInstance().control4010Msg(srcMac, dstMac, way, controlMode, control, delayTime);
            return senMsg;
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
        return null;
    }


    /**
     * 学习红外编码
     *
     * @param srcAdd
     * @param dstAdd
     * @param keyNumber 1-100 不能重复
     */
    public static void redLeaner(String srcAdd, String dstAdd, int keyNumber) {
        try {
                SocketRequest request = new SocketRequest();
                byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
                byte[] dstMac = Tools.hexStr2Bytes(dstAdd);
                byte[] senMsg = BusinessTool.getInstance().redLinear(srcMac, dstMac, (byte) keyNumber);
                request.requestData = senMsg;
                TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 发送红外控制指令
     *
     * @param srcAdd
     * @param dstAdd
     * @param keyNumber
     */
    public static void sendRedOrder(String srcAdd, String dstAdd, int keyNumber) {
        try {
                SocketRequest request = new SocketRequest();
                byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
                byte[] dstMac = Tools.hexStr2Bytes(dstAdd);
                byte[] senMsg = BusinessTool.getInstance().sendRedOrder(srcMac, dstMac, (byte) keyNumber);
                request.requestData = senMsg;
                TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 获取红外控制命令
     * @param srcAdd
     * @param dstAdd
     * @param keyNumber
     * @return
     */
    public static byte[] getSendRedOrder(String srcAdd, String dstAdd, int keyNumber) {
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAdd);
            byte[] senMsg = BusinessTool.getInstance().sendRedOrder(srcMac, dstMac, (byte) keyNumber);
            return senMsg;
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
        return null;
    }

    /**
     * 设置空调类型
     *
     * @param srcAdd
     * @param dstAdd
     * @param type
     */
    public static void setAirConditionerType(String srcAdd, String dstAdd, int type) {
        try {
                SocketRequest request = new SocketRequest();
                byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
                byte[] dstMac = Tools.hexStr2Bytes(dstAdd);
                byte[] senMsg = BusinessTool.getInstance().setAirConditionerType(srcMac, dstMac, type);
                request.requestData = senMsg;
                TCPMgr.getInstance().sendRequest(request);

        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }


    /**
     * 发送空调控制命令
     *
     * @param srcAdd
     * @param dstAdd
     * @param order
     */
    public static void setAirConditionerOrder(String srcAdd, String dstAdd, byte[] order) {
        try {
                SocketRequest request = new SocketRequest();
                byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
                byte[] dstMac = Tools.hexStr2Bytes(dstAdd);
                byte[] senMsg = BusinessTool.getInstance().setAirConditionerOrder(srcMac, dstMac, order);
                request.requestData = senMsg;
                TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 获取发送空调控制命令
     * @param srcAdd
     * @param dstAdd
     * @param order
     * @return
     */
    public static byte[] getSetAirConditionerOrder(String srcAdd, String dstAdd, byte[] order) {
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAdd);
            byte[] senMsg = BusinessTool.getInstance().setAirConditionerOrder(srcMac, dstMac, order);
            return senMsg;
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
        return null;
    }

    /**
     * 发送协议转发控制命令
     *
     * @param srcAdd
     * @param dstAdd
     * @param order
     */
    public static void sendForwardOrder(String srcAdd, String dstAdd, byte[] order) {
        try {
                SocketRequest request = new SocketRequest();
                byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
                byte[] dstMac = Tools.hexStr2Bytes(dstAdd);
                byte[] senMsg = BusinessTool.getInstance().sendForwardOrder(srcMac, dstMac, order);
                request.requestData = senMsg;
                TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 获取协议转发命令
     * @param srcAdd
     * @param dstAdd
     * @param order
     * @return
     */
    public static byte[] getSendForwardOrder(String srcAdd, String dstAdd, byte[] order) {
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAdd);
            byte[] senMsg = BusinessTool.getInstance().sendForwardOrder(srcMac, dstMac, order);
            return senMsg;
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
        return null;
    }

    /**
     * 开锁命令
     * @param srcAdd
     */
    public static void senLockOrder(String srcAdd,String dstAddr){
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAddr);
            byte[] lockOpenOrder = new byte[]{0x08,(byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xA1,0x60,0x01,0x02};
            byte[] senMsg = BusinessTool.getInstance().sendLockOrder(srcMac,dstMac,lockOpenOrder);
            request.requestData = senMsg;
            TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 开锁命令
     * @param srcAdd
     */
    public static void senLoeveaLockOrder(String srcAdd,String dstAddr,byte[] passWord){
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAddr);
            int length = 5 + passWord.length;
            byte[] lockOpenOrder = new byte[length + 1];
            //0B 80 08 10 80 01 01 01 01 01 01 90
            lockOpenOrder[0] = (byte)length;
            lockOpenOrder[1] = (byte) 0x80;
            lockOpenOrder[2] = 0x08;
            lockOpenOrder[3] = 0x10;
            lockOpenOrder[4] = (byte) 0x80;
            System.arraycopy(passWord,0,lockOpenOrder,5,passWord.length);
            lockOpenOrder[lockOpenOrder.length -1] = Tools.lockDataSum(lockOpenOrder);
            byte[] senMsg = BusinessTool.getInstance().sendLockOrder(srcMac,dstMac,lockOpenOrder);
            request.requestData = senMsg;
            TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 获取所有4010节点信息
     */
    public static void getAll4010NodeInfo(String mac){
        try {
            if(!TextUtils.isEmpty(mac)) {
                SocketRequest request = new SocketRequest();
                request.requestData = BusinessTool.getInstance().getDeviceInfo(Tools.hexStr2Bytes(mac));
                TCPMgr.getInstance().sendRequest(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 获取调光灯参数
     * @param srcAdd
     * @param dstAddr
     */
    public static void getColorLightInfo(String srcAdd,String dstAddr){
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAddr);
            byte[] senMsg = BusinessTool.getInstance().getColorLightInfo(srcMac,dstMac);
            request.requestData = senMsg;
            TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 设置调光灯亮度
     * @param srcAdd
     * @param dstAddr
     * @param light
     * @param openOrClose
     */
    public static void sendColorLightLight(String srcAdd,String dstAddr,byte light,byte openOrClose){
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAddr);
            byte[] senMsg = BusinessTool.getInstance().sendColorLightLight(srcMac,dstMac,light,openOrClose);
            request.requestData = senMsg;
            TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 获取多彩灯命令
     * @param srcAdd
     * @param dstAddr
     * @param light
     * @param openOrClose
     * @return
     */
    public static byte[] getSendColorLightLight(String srcAdd,String dstAddr,byte light,byte openOrClose){
        try {
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAddr);
            byte[] senMsg = BusinessTool.getInstance().sendColorLightLight(srcMac,dstMac,light,openOrClose);
            return senMsg;
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
        return null;
    }

    /**
     * 设置调光灯颜色
     * @param srcAdd
     * @param dstAddr
     * @param colorR
     * @param colorG
     * @param colorB
     */
    public static void sendColorLightColor(String srcAdd,String dstAddr,byte type,byte time,byte colorR,byte colorG,byte colorB){
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAddr);
            byte[] senMsg = BusinessTool.getInstance().sendColorLightColor(srcMac,dstMac,type,time,colorR, colorG, colorB);
            request.requestData = senMsg;
            TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 获取RGB灯颜色设置命令
     * @param srcAdd
     * @param dstAddr
     * @param type 0x01：直接、0x02：渐渐、0x03：延时
     * @param time 延时时间
     * @param colorR
     * @param colorG
     * @param colorB
     * @return
     */
    public static byte[] getColorLightColor(String srcAdd,String dstAddr,byte type,byte time,byte colorR,byte colorG,byte colorB){
        try {
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAddr);
            byte[] senMsg = BusinessTool.getInstance().sendColorLightColor(srcMac,dstMac,type,time,colorR, colorG, colorB);
            return senMsg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 七彩渐变等操作
     * @param srcAdd
     * @param dstAddr
     * @param type 1:七彩渐变、2:七彩跳变
     */
    public static void sendColorLightQc(String srcAdd,String dstAddr,int type){
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAddr);
            byte[] senMsg = null;
            switch (type){
                case 1:
                    senMsg = BusinessTool.getInstance().sendColorLightColorQCJB(srcMac,dstMac);
                    break;
                case 2:
                    senMsg = BusinessTool.getInstance().sendColorLightColorTB(srcMac,dstMac);
                    break;
            }

            request.requestData = senMsg;
            TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 获取七彩操作命令
     * @param srcAdd
     * @param dstAddr
     * @param type 1:七彩渐变、2:七彩跳变
     */
    public static byte[] getColorLightQc(String srcAdd,String dstAddr,int type){
        try {
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAddr);
            byte[] senMsg = null;
            switch (type){
                case 1:
                    senMsg = BusinessTool.getInstance().sendColorLightColorQCJB(srcMac,dstMac);
                    break;
                case 2:
                    senMsg = BusinessTool.getInstance().sendColorLightColorTB(srcMac,dstMac);
                    break;
            }
            return senMsg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 呼吸灯
     * @param srcAdd
     * @param dstAddr
     * @param colorR
     * @param colorG
     * @param colorB
     */
    public static void sendColorLightHXD(String srcAdd,String dstAddr,byte colorR,byte colorG,byte colorB){
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAddr);
            byte[] senMsg  = BusinessTool.getInstance().sendColorLightColorHXD(srcMac,dstMac,colorR,colorG,colorB);
            request.requestData = senMsg;
            TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 获取呼吸灯命令
     * @param srcAdd
     * @param dstAddr
     * @param colorR
     * @param colorG
     * @param colorB
     * @return
     */
    public static byte[] getColorLightHXD(String srcAdd,String dstAddr,byte colorR,byte colorG,byte colorB){
        try {
            byte[] srcMac = Tools.hexStr2Bytes(srcAdd);
            byte[] dstMac = Tools.hexStr2Bytes(dstAddr);
            byte[] senMsg = BusinessTool.getInstance().sendColorLightColorHXD(srcMac, dstMac, colorR, colorG, colorB);
            return senMsg;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 删除场景
     * @param srcAddr
     * @param sceneNum 场景编号
     */
    public static void sendDelSceneOrder(String srcAddr,byte sceneNum){
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAddr);
            byte[] senMsg = BusinessTool.getInstance().delSceneOrder( srcMac, sceneNum);
            request.requestData = senMsg;
            TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 配置联动场景
     * @param srcAddr
     * @param dstAddr
     * @param sceneNum
     * @param orderSum
     * @param isRQ
     */
    public static void sendSetInitSceneOrder(String srcAddr,String dstAddr,byte sceneNum,byte orderSum,boolean isRQ ,String ysTime,int cf){
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAddr);
            byte[] dstMac = Tools.hexStr2Bytes(dstAddr);
            byte[] ysTime_byte = {0x00,0x00};
            if(ysTime != null){
                ysTime_byte[1] = Byte.valueOf(ysTime);
            }
            byte bf_byte = 0x01;
            if(cf == 1){
                bf_byte = 0x02;
            }
            byte[] senMsg = BusinessTool.getInstance().setInitSceneOrder(srcMac,dstMac, sceneNum, orderSum, isRQ,ysTime_byte,bf_byte);
            request.requestData = senMsg;
            TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 配置普通场景
     * @param srcAddr
     * @param sceneNum
     * @param orderSum
     */
    public static void sendSetInitBaseSceneOrder(String srcAddr ,byte sceneNum,byte orderSum,String dsTime,String ysTime){
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAddr);
            byte[] dsTime_byte = {(byte) 0xff, (byte) 0xff};
            if(dsTime != null&&dsTime.contains(":")){
                String[] array = dsTime.split(":");
                if(array != null && array.length >1) {
                    dsTime_byte[0] = Byte.valueOf(array[0]);
                    dsTime_byte[1] = Byte.valueOf(array[1]);
                }
            }
            byte[] ysTime_byte = {0x00,0x00};
            if(ysTime != null){
                ysTime_byte[1] = Byte.valueOf(ysTime);
            }
            byte[] senMsg = BusinessTool.getInstance().setInitBaseSceneOrder(srcMac, sceneNum, orderSum,dsTime_byte,ysTime_byte);
            request.requestData = senMsg;
            TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 加载场景命令
     * @param srcAddr
     * @param sceneNum
     * @param orderSum
     */
    public static void sendLoadSceneOrder(String srcAddr,byte sceneNum,byte orderSum,byte orderNum,byte[] orderData){
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAddr);
            byte[] senMsg = BusinessTool.getInstance().loadSceneOrder( srcMac, sceneNum, orderSum,orderNum, orderData);
            request.requestData = senMsg;
            TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }

    /**
     * 出发场景命令
     * @param srcAddr
     * @param sceneNum
     */
    public static void sendSendSceneOrder(String srcAddr,byte sceneNum){
        try {
            SocketRequest request = new SocketRequest();
            byte[] srcMac = Tools.hexStr2Bytes(srcAddr);
            byte[] senMsg = BusinessTool.getInstance().sendSceneOrder(srcMac, sceneNum);
            request.requestData = senMsg;
            TCPMgr.getInstance().sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            SmartHomeApp.showToast(R.string.send_msg_fail);
        }
    }


    /**
     * 删除网关
     *
     * @param context
     * @param gw_mac
     * @param callback
     */
    public static void deleteGW(Context context, String gw_mac, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        DelGwRequestParam param = new DelGwRequestParam();
        param.setGateway_mac(gw_mac);
        RequestMgr.execute(RequestMgr.Method.POST, ApiHost.DELETE_GW_URL, mode, param, DelGwRequestParam.class, DeleteGwResponse.class, callback);
    }

    /**
     * 退出登录
     *
     * @param context
     * @param callback
     */
    public static void logout(Context context, RequestCallback callback) {
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        LogoutRequestParam param = new LogoutRequestParam();
        RequestMgr.execute(RequestMgr.Method.GET, ApiHost.LOGOUT_URL, mode, param, LogoutRequestParam.class, LogoutResponse.class, callback);
    }



}