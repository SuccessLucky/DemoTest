package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.base.TopNavSubActivity;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.BrandTypeOperationDialog;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceManageAdapter;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceTools;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.ImageInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.setting.IOTSelectOperationDialog;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.RoomInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.RoomSelectPopWindowAdapter;
import com.kzksmarthome.SmartHouseYCT.biz.smart.home.BrandType;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddDeviceResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetDeviceListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetImageListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.UpdateDeviceResponse;
import com.kzksmarthome.SmartHouseYCT.util.DeviceCtrTypeEnums;
import com.kzksmarthome.SmartHouseYCT.util.DeviceTypeEnums;
import com.kzksmarthome.SmartHouseYCT.util.ImageTypeEnums;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.biz.widget.ConfirmDialog;
import com.kzksmarthome.common.event.EventOfTcpResult;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;
import com.kzksmarthome.common.lib.tools.DeviceState;
import com.kzksmarthome.common.lib.tools.IOTConfig;
import com.kzksmarthome.common.lib.tools.Tools;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.net.ApiHost;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * @author laixj
 * @version V1.0
 * @Title: DeviceManageFragment
 * @Description: 设备管理界面
 * @date 2016/9/16 21:09
 */
public class DeviceManageFragment extends BaseRequestFragment implements RequestCallback,
        RoomSelectPopWindowAdapter.OnRoomItemClick, View.OnClickListener, DeviceManageAdapter.OnDeviceItemClick,
        DeviceCtrTypeSelectDialog.OnCtrTypeItemClick, DeviceAddDialog.OnEnsureClick, BrandTypeOperationDialog.OnBrandTypeClick{

    @BindView(R.id.smart_device_manage_recycle)
    RecyclerView smartDeviceManageRecycle;

    private DeviceManageAdapter deviceAdapter;
    /**
     * 设备Map
     */
    //private Map<RoomInfo, List<DeviceInfo>> roomMap = new HashMap<RoomInfo, List<DeviceInfo>>();
    private Map<RoomInfo, List<DeviceInfo>> deviceMap = new HashMap<RoomInfo, List<DeviceInfo>>();
    /**
     * 选中的房间信息
     */
    private RoomInfo selectedRoom;

    private List<RoomInfo> roomList = new ArrayList<RoomInfo>();

    private List<DeviceInfo> deviceList = new ArrayList<DeviceInfo>();

    private PopupWindow roomPopWindow;

    private DeviceCtrTypeSelectDialog ctrTypeDialog = null;

    private DeviceAddDialog addDialog = null;
    private DeviceAddDialog editDialog = null;

    private DeviceAddDialogIconAdapter typeAdapter;

    private DeviceInfo editInfo = null;
    private DeviceInfo addInfo = null;
    private int editPosition = -1;
    /**
     * 是否开始添加计时
     */
    private boolean mIsStartTime;
    /**
     * 网关mca地址
     */
    private String mIotMac;
    /**
     * 空调类型选择弹框
     */
    private BrandTypeOperationDialog mBrandTypeOperationDialog;
    /**
     * 红外类型选择
     */
    private IOTSelectOperationDialog mRedDeviceTypeOperationDialog;
    /**
     * 位置
     */
    private int mPosition = -1;
    /**
     * 其它红外设备
     */
    private int mDeviceType = -1;
    /**
     * 灯路计算
     */
    private int mLightCount;
    /**
     * 添加的设备链表
     */
    private ArrayList<DeviceInfo> deviceInfoList;
    /**
     * 设备返回状态列表
     */
    private ArrayList<DeviceState> mDeviceStateList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_device_manage_layout, container, false);
        ButterKnife.bind(this, mRootView);
        mIotMac = SmartHomeAppLib.getUserMgr().getUser().gateway;
        initData();
        initView();
        GjjEventBus.getInstance().register(mDeviceUploadEvent);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData() {
        Bundle bundle = getArguments();
        selectedRoom = bundle.getParcelable("roomInfo");
        roomList = (List<RoomInfo>) bundle.getSerializable("roomList");
        if (null != roomList && roomList.size() > 0 && roomList.get(roomList.size() - 1).getId() == -1) {
            roomList.remove(roomList.size() - 1);
        }
        Log.d("laixj", "房间信息：" + roomList.toString());
        RestRequestApi.getImageList(getActivity(), ImageTypeEnums.DEVICE.getCode(), this);
        //roomMap = (HashMap<RoomInfo, List<DeviceInfo>>) bundle.getSerializable("roomMap");
    }

    private void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        smartDeviceManageRecycle.setLayoutManager(gridLayoutManager);
        deviceAdapter = new DeviceManageAdapter(getActivity());
        deviceAdapter.setOnDeviceItemClickListener(this);
        smartDeviceManageRecycle.setAdapter(deviceAdapter);

        if (null != selectedRoom) {
            Log.d("laixj", "设备信息：" + selectedRoom.toString());
            RestRequestApi.getDeviceByRoomId(getActivity(), selectedRoom.getId(), this);
        }
        if (null != roomList) {
            //Log.d("laixj", "房间设备信息：" + roomMap.toString());
            /*if (roomMap.containsKey(selectedRoom)) {
                Log.d("laixj", "设备信息：" + roomMap.get(selectedRoom).toString());
                List<DeviceInfo> deviceList = new ArrayList<DeviceInfo>();
                deviceList.addAll(roomMap.get(selectedRoom));
                deviceAdapter.setDeviceList(deviceList);
                ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.delete_str);
                deviceAdapter.setCanDelete(false);
                deviceAdapter.getDeviceList().add(new DeviceInfo(-1));
            }*/
        }
        typeAdapter = new DeviceAddDialogIconAdapter(getActivity());
        typeAdapter.setSelected(-1);
        //((TopNavSubActivity) getActivity()).getTopTitleTV().setOnClickListener(this);
        //((TopNavSubActivity) getActivity()).getTopRightTV().setOnClickListener(this);
    }

    /**
     * 房间选择
     */
    @SuppressWarnings("unused")
    private void showRoomPopWindow() {
        dismissRoomPopWindow();
        View contentView;
        if (roomPopWindow == null) {
            contentView = LayoutInflater.from(getActivity()).inflate(R.layout.room_select_popwindow, null);
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showToast("click popwindow");
                    dismissRoomPopWindow();
                }
            });
            //roomList.addAll(roomList);
            RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.room_select_recycle);
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            final RoomSelectPopWindowAdapter adapter = new RoomSelectPopWindowAdapter(getActivity(), roomList);
            adapter.setOnRoomItemClickListener(this);
            recyclerView.setAdapter(adapter);

            roomPopWindow = new PopupWindow(contentView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, false);
            roomPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            roomPopWindow.setAnimationStyle(R.style.popwin_anim_style);
            roomPopWindow.setOutsideTouchable(true);
            roomPopWindow.setFocusable(true);
        } else {
            contentView = roomPopWindow.getContentView();
        }
        roomPopWindow.showAsDropDown(((TopNavSubActivity) getActivity()).getTopRl());
    }

    /**
     * 取消房间选择弹出框
     *
     * @return
     */
    private boolean dismissRoomPopWindow() {
        if (null != roomPopWindow && roomPopWindow.isShowing()) {
            roomPopWindow.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onTitleBtnClick() {
        Log.d("laixj", "DeviceManageFragment onTitleBtnClick");
        showRoomPopWindow();
    }

    @Override
    public void onRightBtnClick() {
        Log.d("laixj", "DeviceManageFragment onRightBtnClick");
        if (null == deviceAdapter) {
            return;
        }
        if (deviceAdapter.isCanDelete()) {
            ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.delete_str);
            deviceAdapter.setCanDelete(false);
            deviceAdapter.getDeviceList().add(new DeviceInfo(-1));
        } else {
            ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.commit_arrow_title);
            deviceAdapter.setCanDelete(true);
            if(deviceAdapter.getDeviceList().size() > 0) {
                deviceAdapter.getDeviceList().remove(deviceAdapter.getDeviceList().size() - 1);
            }
        }
        deviceAdapter.notifyDataSetChanged();
    }

    private void showAddDialog(DeviceInfo deviceInfo) {
        typeAdapter.setSelected(-1);
        addDialog = new DeviceAddDialog(getActivity(), 0, -1, deviceInfo, typeAdapter);
        addDialog.setOnEnsureClickListener(DeviceManageFragment.this);
        addDialog.setCancelable(true);
        addDialog.setCanceledOnTouchOutside(true);
        addDialog.show();
    }

    private void showEditDialog(int position, DeviceInfo deviceInfo) {
        typeAdapter.setSelected(position);
        editDialog = new DeviceAddDialog(getActivity(), 1, position, deviceInfo, typeAdapter);
        editDialog.setOnEnsureClickListener(DeviceManageFragment.this);
        editDialog.setCancelable(true);
        editDialog.setCanceledOnTouchOutside(true);
        editDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GjjEventBus.getInstance().unregister(mDeviceUploadEvent);
        dismissRoomPopWindow();
        try {
            //((TopNavSubActivity) getActivity()).getTopTitleTV().setOnClickListener(null);
            //((TopNavSubActivity) getActivity()).getTopRightTV().setOnClickListener(null);
            if (null != addDialog) {
                addDialog.dismiss();
            }
            if (null != editDialog) {
                editDialog.dismiss();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onRoomItemClick(RoomInfo roomInfo) {
        dismissRoomPopWindow();
        if (null == roomInfo) {
            return;
        }
        if (selectedRoom.equals(roomInfo)) {
            return;
        }
        selectedRoom = roomInfo;
        ((TopNavSubActivity) getActivity()).getTopTitleTV().setText(selectedRoom.getName());
        RestRequestApi.getDeviceByRoomId(getActivity(), selectedRoom.getId(), this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_title_tv:
                showRoomPopWindow();
                break;
            case R.id.top_right_tv:
                if (null == deviceAdapter) {
                    return;
                }
                if (deviceAdapter.isCanDelete()) {
                    ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.delete_str);
                    deviceAdapter.setCanDelete(false);
                    deviceAdapter.getDeviceList().add(new DeviceInfo(-1));
                } else {
                    ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.commit_arrow_title);
                    deviceAdapter.setCanDelete(true);
                    deviceAdapter.getDeviceList().remove(deviceAdapter.getDeviceList().size() - 1);
                }
                deviceAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void showSelectDialog() {
        if (null == ctrTypeDialog) {
            ctrTypeDialog = new DeviceCtrTypeSelectDialog(getActivity());
        }
        ctrTypeDialog.setOnCtrTypeItemClickListener(DeviceManageFragment.this);
        ctrTypeDialog.setCancelable(true);
        ctrTypeDialog.setCanceledOnTouchOutside(true);
        ctrTypeDialog.show();
    }

    @Override
    public void onDeleteClick(int position, DeviceInfo deviceInfo) {
        int deviceEumType = DeviceTools.getDeviceType(deviceInfo);
        if (DeviceTypeEnums.TABLELAMP.getCode() ==deviceEumType || DeviceTypeEnums.DROPLIGHT.getCode() == deviceEumType) {
            RestRequestApi.deleteDevice(getActivity(), -1,deviceInfo.getMac_address(), this);
        }else{
            RestRequestApi.deleteDevice(getActivity(), deviceInfo.getDevice_id(),null, this);
        }
        mPosition = position;
    }

    @Override
    public void onAddClick() {
        // TODO: 2016/9/17
        showSelectDialog();
    }

    @Override
    public void onEditClick(int position, DeviceInfo deviceInfo) {
        showEditDialog(position,deviceInfo);
    }

    @Override
    public void onCtrTypeItemClick(String ctrType) {
        //showToast("选择设备控制类型："+ctrType);
        //showAddDialog(new DeviceInfo(selectedRoom.getId(), selectedRoom.getName(), ctrType));
        //showAddDialog(new DeviceInfo(selectedRoom.getId()));
        Log.d("AddDevice", "房间ID：" + selectedRoom.getId());
        if (deviceInfoList != null) {
            deviceInfoList.clear();
        } else {
            deviceInfoList = new ArrayList<DeviceInfo>();
        }
        if (ctrType.equals(DeviceCtrTypeEnums.INFRARED.getCode())) {//红外转发设备
            mDeviceType = DeviceTypeEnums.INFRARED.getCode();
        } else if (ctrType.equals(DeviceCtrTypeEnums.AIRCONDITION.getCode())) {//空调
            mDeviceType = DeviceTypeEnums.AIRCONDITION.getCode();
        } else {//常用设备
            mDeviceType = 0;
        }
        if(mDeviceStateList != null) {
            mDeviceStateList.clear();
        }else{
            mDeviceStateList = new ArrayList<DeviceState>();
        }
        showLoadingDialog(R.string.loading_str, true);
    }

    @Override
    public void onEnsureClick(int flag, int position, DeviceInfo deviceInfo) {
        try {
            Log.d("laixj", "添加/编辑：flag=" + flag + ";position=" + position);
            Log.d("laixj", "添加/编辑：deviceInfo=" + deviceInfo.toString());
            if (flag == 0) {//添加
                // TODO: 2016/9/11
                //deviceAdapter.getDeviceList().add(deviceAdapter.getDeviceList().size() - 1, deviceInfo);
                //deviceAdapter.notifyDataSetChanged();
                if (deviceInfo == null) {
                    return;
                }
                addInfo = deviceInfo;
                if (deviceInfo.getSindex_length().equals(Integer.toString(DeviceTypeEnums.INFRARED.getCode()))) {
                    if (TextUtils.isEmpty(deviceInfo.getDevice_OD())) {
                        showToast(R.string.no_red_forward);
                        return;
                    }
                    addInfo.setMac_address(deviceInfo.getMac_address());
                    addInfo.setDevice_OD(deviceInfo.getDevice_OD());
                    addInfo.setDevice_type(deviceInfo.getDevice_type());
                    if (typeAdapter != null && !Util.isListEmpty(typeAdapter.getTypeList())) {
                        ImageInfo typeInfo = typeAdapter.getTypeList().get(typeAdapter.getSelected());
                        addInfo.setImage(typeInfo.getName());
                        //addInfo.setImage("Un_" + typeInfo.getName() + "." + typeInfo.getImage_type());
                    }
                    addInfo.setDevice_name(deviceInfo.getDevice_name());
                    addInfo.setCategory(deviceInfo.getCategory());
                    deviceInfoList.add(addInfo);
                    RestRequestApi.addDevice(getActivity(), deviceInfoList, DeviceManageFragment.this);
                } else if (deviceInfo.getSindex_length().equals(Integer.toString(DeviceTypeEnums.AIRCONDITION.getCode()))) {//空调
                    if (TextUtils.isEmpty(deviceInfo.getMac_address())) {
                        showToast(R.string.no_red_forward);
                        return;
                    }
                    if (typeAdapter != null && !Util.isListEmpty(typeAdapter.getTypeList())) {
                        ImageInfo typeInfo = typeAdapter.getTypeList().get(typeAdapter.getSelected());
                        addInfo.setImage(typeInfo.getName());
                        //addInfo.setImage("Un_" + typeInfo.getName() + "." + typeInfo.getImage_type());
                    }
                    addInfo.setMac_address(deviceInfo.getMac_address());
                    addInfo.setDevice_OD(deviceInfo.getDevice_OD());
                    addInfo.setDevice_type(deviceInfo.getDevice_type());
                    addInfo.setDevice_name(deviceInfo.getDevice_name());
                    addInfo.setCategory(deviceInfo.getCategory());
                    setBrandType(addInfo);
                } else {//常用设备
                    if (typeAdapter != null && !Util.isListEmpty(typeAdapter.getTypeList())) {
                        ImageInfo typeInfo = typeAdapter.getTypeList().get(typeAdapter.getSelected());
                        deviceInfo.setImage(typeInfo.getName());
                        //addInfo.setImage("Un_" + typeInfo.getName() + "." + typeInfo.getImage_type());
                    }
                    byte[] od = Tools.hexStr2Bytes(deviceInfo.getDevice_OD());
                    if (od[0] == 0x0f && od[1] == (byte) 0xaa) {
                        byte type = Tools.hexStr2Byte(deviceInfo.getDevice_type());
                        byte category = Tools.hexStr2Byte(deviceInfo.getCategory());
                        if (type == 0x06) {
                            if (category == 0x02) {
                                if (mLightCount < 1) {
                                    showAddDialog(new DeviceInfo(deviceInfo));
                                    mLightCount++;
                                    deviceInfo.setOther_status(Integer.toString(mLightCount));
                                    deviceInfoList.add(deviceInfo);
                                } else {
                                    deviceInfo.setOther_status(Integer.toString(mLightCount + 1));
                                    deviceInfoList.add(deviceInfo);
                                    RestRequestApi.addDevice(getActivity(), deviceInfoList, DeviceManageFragment.this);
                                }
                            }
                        } else if (type == 0x07) {
                            if (category == 0x02) {
                                if (mLightCount < 2) {
                                    showAddDialog(new DeviceInfo(deviceInfo));
                                    mLightCount++;
                                    deviceInfo.setOther_status(Integer.toString(mLightCount));
                                    deviceInfoList.add(deviceInfo);
                                } else {
                                    deviceInfo.setOther_status(Integer.toString(mLightCount + 1));
                                    deviceInfoList.add(deviceInfo);
                                    RestRequestApi.addDevice(getActivity(), deviceInfoList, DeviceManageFragment.this);
                                }
                            }
                        } else {
                            deviceInfoList.add(deviceInfo);
                            RestRequestApi.addDevice(getActivity(), deviceInfoList, DeviceManageFragment.this);
                        }
                    }else{
                        deviceInfoList.add(deviceInfo);
                        RestRequestApi.addDevice(getActivity(), deviceInfoList, DeviceManageFragment.this);
                    }
                }
            } else if (flag == 1) {//编辑
                editPosition = position;
                RestRequestApi.updateDevice(getActivity(), deviceInfo, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 设置空调类型
     *
     * @param deviceInfo
     */
    public void setBrandType(DeviceInfo deviceInfo) {
        mBrandTypeOperationDialog = new BrandTypeOperationDialog(getActivity(), deviceInfo);
        mBrandTypeOperationDialog.setOnBrandTypeClickListener(this);
        mBrandTypeOperationDialog.setCancelable(true);
        mBrandTypeOperationDialog.setCanceledOnTouchOutside(true);
        mBrandTypeOperationDialog.show();
    }

    int brandTypeArrayPosition = 0;
    int type = 0;

    @Override
    public void onBrandTypeClick(int position, DeviceInfo deviceInfo) {
        mBrandTypeOperationDialog.dismiss();
        brandTypeArrayPosition = 0;
        type = 0;
        BrandType brandType = SmartHomeApp.mBrandTypeArrayList.get(position);
        if (brandType != null) {
            showHintDialog(brandType, deviceInfo);
        }
    }

    /**
     * 显示天空提示
     *
     * @param deviceInfo
     */
    public void showHintDialog(final BrandType brandType, final DeviceInfo deviceInfo) {
        if (brandType != null) {
            if (brandType.brandTypeArray != null&&brandTypeArrayPosition< brandType.brandTypeArray.length) {
                if (brandTypeArrayPosition < brandType.brandTypeArray.length) {
                    type = brandType.brandTypeArray[brandTypeArrayPosition];
                    brandTypeArrayPosition++;
                }
            } else {
                if (brandType.endNum > 0) {
                    if (type == 0) {
                        type = brandType.startNum;
                    } else if (type < brandType.endNum) {
                        type++;
                    } else {
                        showToast("选型失败！");
                        return;
                    }
                }
            }
        } else {
            return;
        }
        RestRequestApi.setAirConditionerType(mIotMac, deviceInfo.getMac_address(), type);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showConfirDialog(brandType, deviceInfo);
            }
        }, 1000);
    }

    /**
     * 显示确定对话框
     *
     * @param brandType
     * @param deviceInfo
     */
    private void showConfirDialog(final BrandType brandType, final DeviceInfo deviceInfo) {
        byte[] order = {IOTConfig.KT_ORDER_OPEN_CLOSE, (byte) 0xff};
        RestRequestApi.setAirConditionerOrder(mIotMac, deviceInfo.getMac_address(), order);
        final ConfirmDialog confirmDialog = new ConfirmDialog(getActivity());
        confirmDialog.setContent(getResources().getString(R.string.brand_content_ok)+"？ 空调码："+type);

        confirmDialog.setConfirmClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog.dismiss();
                deviceInfo.setDevice_name(brandType.brandName);
                deviceInfo.setOther_status(Integer.toString(type));
                deviceInfoList.add(deviceInfo);
                RestRequestApi.addDevice(getActivity(), deviceInfoList, DeviceManageFragment.this);
            }
        });
        confirmDialog.setCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog.dismiss();
                if (type != -1) {
                    showHintDialog(brandType, deviceInfo);
                }
            }
        });
        confirmDialog.show();
    }


    @Override
    public void onFailure(Request request, String url, Exception e) {
        L.d("loginFail");
        if (getActivity() == null) {
            return;
        }
        dismissLoadingDialog();
        SmartHomeApp.showToast("请检查网络");
        if (url.startsWith(ApiHost.GET_DEVICE_BY_ROOMID_URL.substring(0, ApiHost.GET_DEVICE_BY_ROOMID_URL.lastIndexOf("=")))) {
            deviceAdapter.setDeviceList(deviceList);
            deviceAdapter.getDeviceList().add(new DeviceInfo(-1));
            deviceAdapter.setCanDelete(false);
            deviceAdapter.notifyDataSetChanged();
            ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.delete_str);
        }
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        if (getActivity() == null) {
            return;
        }
        dismissLoadingDialog();
        if (url.startsWith(ApiHost.GET_DEVICE_BY_ROOMID_URL.substring(0, ApiHost.GET_DEVICE_BY_ROOMID_URL.lastIndexOf("=")))) {
            if (response != null) {
                GetDeviceListResponse param = (GetDeviceListResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            deviceList = param.getResult();
                            deviceAdapter.setDeviceList(deviceList);
                            deviceAdapter.getDeviceList().add(new DeviceInfo(-1));
                            deviceMap.put(selectedRoom, deviceAdapter.getDeviceList());
                            deviceAdapter.setCanDelete(false);
                            deviceAdapter.notifyDataSetChanged();
                            ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.delete_str);
                        } else {
                            if (url.startsWith(ApiHost.GET_DEVICE_BY_ROOMID_URL.substring(0, ApiHost.GET_DEVICE_BY_ROOMID_URL.lastIndexOf("=")))) {
                                deviceAdapter.setDeviceList(deviceList);
                                deviceAdapter.setCanDelete(false);
                                deviceAdapter.notifyDataSetChanged();
                                ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.delete_str);
                            }
                        }
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            SmartHomeApp.showToast("请求失败");
                        }
                        if (url.startsWith(ApiHost.GET_DEVICE_BY_ROOMID_URL.substring(0, ApiHost.GET_DEVICE_BY_ROOMID_URL.lastIndexOf("=")))) {
                            deviceAdapter.setDeviceList(deviceList);
                            deviceAdapter.getDeviceList().add(new DeviceInfo(-1));
                            deviceAdapter.setCanDelete(false);
                            deviceAdapter.notifyDataSetChanged();
                            ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.delete_str);
                        }
                    }
                } else {
                    SmartHomeApp.showToast("请求失败");
                    if (url.startsWith(ApiHost.GET_DEVICE_BY_ROOMID_URL.substring(0, ApiHost.GET_DEVICE_BY_ROOMID_URL.lastIndexOf("=")))) {
                        deviceAdapter.setDeviceList(deviceList);
                        deviceAdapter.getDeviceList().add(new DeviceInfo(-1));
                        deviceAdapter.setCanDelete(false);
                        deviceAdapter.notifyDataSetChanged();
                        ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.delete_str);
                    }
                }
            }
        } else if (ApiHost.ADD_DEVICE_URL.equals(url)) {
            if (response != null) {
                AddDeviceResponse param = (AddDeviceResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            int index = (deviceAdapter.getDeviceList().size() - 1);
                            if(index >= 0) {
                                deviceAdapter.getDeviceList().addAll(index, param.getResult());
                                deviceMap.put(selectedRoom, deviceAdapter.getDeviceList());
                                deviceAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            SmartHomeApp.showToast("设备添加失败");
                        }
                    }
                } else {
                    SmartHomeApp.showToast("设备添加失败");
                }
            }
        } else if (ApiHost.UPDATE_DEVICE_URL.equals(url)) {
            if (response != null) {
                UpdateDeviceResponse param = (UpdateDeviceResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            deviceAdapter.getDeviceList().set(editPosition, new DeviceInfo(param.getResult()));
                            deviceMap.put(selectedRoom, deviceAdapter.getDeviceList());
                            deviceAdapter.notifyDataSetChanged();
                        }
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            SmartHomeApp.showToast("设备修改失败");
                        }
                    }
                } else {
                    SmartHomeApp.showToast("设备修改失败");
                }
            }
        } else if (url.equals(String.format(ApiHost.GET_IMG_URL, ImageTypeEnums.DEVICE.getCode()))) {
            if (response != null) {
                GetImageListResponse param = (GetImageListResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            Log.d("laixj", "设备图片->" + param.getResult().toString());
                            if (param.getResult().getImages().size() > 0) {
                                ImageInfo imageInfo = param.getResult().getImages().get(0);
                                if (url.equals(String.format(ApiHost.GET_IMG_URL, ImageTypeEnums.DEVICE.getCode()))) {
                                    saveImgBaseUrl(ImageTypeEnums.DEVICE.getCode(), imageInfo.getBase_url());
                                } else if (url.equals(String.format(ApiHost.GET_IMG_URL, ImageTypeEnums.ROOM.getCode()))) {
                                    saveImgBaseUrl(ImageTypeEnums.ROOM.getCode(), imageInfo.getBase_url());
                                } else if (url.equals(String.format(ApiHost.GET_IMG_URL, ImageTypeEnums.SCENE.getCode()))) {
                                    saveImgBaseUrl(ImageTypeEnums.SCENE.getCode(), imageInfo.getBase_url());
                                }
                                typeAdapter.setTypeList(param.getResult().getImages());
                            }
                        }
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            Log.d("laixj", "设备图片->" + "fail");
                            SmartHomeApp.showToast("拉取设备图片失败");
                        }
                    }
                } else {
                    Log.d("laixj", "设备图片->" + "(param == null)");
                    SmartHomeApp.showToast("拉取设备图片失败");
                }
            }
        } else if (ApiHost.DELETE_DEVICE_URL.equals(url)) {
            if (!Util.isListEmpty(deviceAdapter.getDeviceList()) && mPosition != -1 && deviceAdapter.getDeviceList().size() > mPosition) {
                DeviceInfo deviceInfo = deviceAdapter.getDeviceList().get(mPosition);
                if(deviceInfo != null) {
                    int deviceEumType = DeviceTools.getDeviceType(deviceInfo);
                    String mac = deviceInfo.getMac_address();
                    if(DeviceTypeEnums.DROPLIGHT.getCode() == deviceEumType || DeviceTypeEnums.TABLELAMP.getCode() == deviceEumType){//判断是否为二路三路灯
                        List<DeviceInfo> deviceInfoList = new ArrayList<DeviceInfo>();
                        for( DeviceInfo deviceInfo1 :deviceAdapter.getDeviceList()){
                            if(deviceInfo1.getMac_address().equals(mac)){
                                deviceInfoList.add(deviceInfo1);
                            }
                        }
                        deviceAdapter.getDeviceList().removeAll(deviceInfoList);
                    }else {
                        deviceAdapter.getDeviceList().remove(mPosition);
                    }
                    deviceAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        if (getActivity().isFinishing()) {
            return;
        }
        dismissLoadingDialog();
        if (url.startsWith(ApiHost.GET_DEVICE_BY_ROOMID_URL.substring(0, ApiHost.GET_DEVICE_BY_ROOMID_URL.lastIndexOf("=")))) {
            deviceAdapter.setDeviceList(deviceList);
            deviceAdapter.getDeviceList().add(new DeviceInfo(-1));
            deviceAdapter.setCanDelete(false);
            deviceAdapter.notifyDataSetChanged();
            ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.delete_str);
        }
    }

    /**
     * 请求回调
     */
    public Object mDeviceUploadEvent = new Object() {
        public void onEventMainThread(EventOfTcpResult data) {
            // TODO: 2016/10/12
            if (null == data) {
                return;
            }
            DeviceState deviceState = data.deviceState;
            if (null == deviceState|| deviceState.dstAddr == null || mDeviceType == -1) {
                return;
            }
            try {
                byte[] odArray = deviceState.deviceOD;
                if (odArray[0] != 0x03 && odArray[1] != (byte) 0xeb) {//判断是否为网关
                    mDeviceStateList.add(deviceState);
                    Log.d("AddDevice", "设备链表长度："+mDeviceStateList.size()+"房间ID----：" + selectedRoom.getId() + "--mDeviceType:" + mDeviceType +"---mAddDeviceMac--" + Tools.byte2HexStr(deviceState.dstAddr));
                    if(!mIsStartTime){
                        startTimer();
                    }
                    checkAddCount();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void saveImgBaseUrl(int category, String baseUrl) {
        SharedPreferences.Editor editor = SmartHomeAppLib.getInstance().getPreferences().edit();
        if (category == ImageTypeEnums.DEVICE.getCode()) {
            editor.putString(SharePrefConstant.PREFS_KEY_DEVICE_BASEURL, baseUrl);
        } else if (category == ImageTypeEnums.ROOM.getCode()) {
            editor.putString(SharePrefConstant.PREFS_KEY_ROOM_BASEURL, baseUrl);
        } else if (category == ImageTypeEnums.SCENE.getCode()) {
            editor.putString(SharePrefConstant.PREFS_KEY_SCENE_BASEURL, baseUrl);
        }
        editor.commit();
    }

    /**
     * 开始计时
     */
    private void startTimer() {
        mIsStartTime = true;
        MainTaskExecutor.scheduleTaskOnUiThread(60000, runnable);
    }

    /**
     * 执行10秒检测
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            DeviceState deviceState = checkDeviceAdd();
            updateCount(deviceState);
        }
    };

    /**
     * 检测是否上传三次
     */
    private void checkAddCount() {
       DeviceState deviceState = checkDeviceAdd();
        if(deviceState != null){
            MainTaskExecutor.cancelTaskOnUiThread(runnable);
            updateCount(deviceState);
        }
    }

    /**
     * 检测设备设备添加
     */
    public DeviceState checkDeviceAdd(){
        try {
        for(DeviceState deviceState :mDeviceStateList){
            int count = 0;
            for(DeviceState deviceState1 :mDeviceStateList){
                if(Tools.byte2HexStr(deviceState.dstAddr).equals(Tools.byte2HexStr(deviceState1.dstAddr))){
                    count++;
                    Log.d("AddDevice", "房间count----："+count);
                    if(count > 2){
                        return deviceState;
                    }
                }
            }
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 更新数据
     */
    private void updateCount(DeviceState deviceState) {
        dismissLoadingDialog();
        mIsStartTime = false;
        if (deviceState != null) {
            DeviceInfo newDevice = setDeviceInfo(deviceState);
            if(newDevice != null) {
                mLightCount = 0;
                showToast(DeviceTools.getDeviceTypeName(newDevice));//提示设备类型
                showAddDialog(newDevice);
            }else{
                showToast("新设备数据异常！");
            }
        } else {
            showToast("未找到新设备");
        }
        mDeviceType = -1;
    }

    /**
     * 初始化设备类型
     * @param deviceState
     * @return
     */
    private DeviceInfo setDeviceInfo(DeviceState deviceState) {
        DeviceInfo newDevice = null;
        try {
            newDevice = new DeviceInfo(selectedRoom.getId());
            newDevice.setRoom_id(selectedRoom.getId());
            String mac = Tools.byte2HexStr(deviceState.dstAddr);
            String od = Tools.byte2HexStr(deviceState.deviceOD);
            String type = null;
            String category = null;
            String sindex_length = Tools.byte2HexStr(deviceState.sindex_length);
            type =Tools.byte2HexStr(deviceState.deviceType);
            category = Tools.byte2HexStr(deviceState.deviceProduct);
            switch (mDeviceType) {
                case 0://常用设备

                    break;
                case 102://红外转发设备
                    sindex_length = DeviceTypeEnums.INFRARED.getCode() + "";
                    break;
                case 101://空调
                    sindex_length = DeviceTypeEnums.AIRCONDITION.getCode() + "";
                    break;
            }
            if (deviceState.sindex != null) {
                newDevice.setSindex(Tools.byte2HexStr(deviceState.sindex));
            }
            newDevice.setCmdId(Tools.byte2HexStr(deviceState.cmdId));
            newDevice.setSindex_length(sindex_length);
            newDevice.setMac_address(mac);
            newDevice.setDevice_OD(od);
            newDevice.setDevice_type(type);
            newDevice.setCategory(category);
            newDevice.setOther_status("1");
        }catch (Exception e){
            e.printStackTrace();
        }
        return  newDevice;
    }

    @Override
    public boolean goBack() {

        return super.goBack();
    }
}
