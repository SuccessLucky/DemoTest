package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.SmartHouseYCT.biz.base.TopNavSubActivity;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfUpdateDeviceList;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetDeviceListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetLockPwdListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter.DoorLockPwddAddFragment;
import com.kzksmarthome.SmartHouseYCT.util.DeviceTypeEnums;
import com.kzksmarthome.SmartHouseYCT.util.UserRoleEnums;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.biz.widget.CustomProgressDialog;
import com.kzksmarthome.common.event.EventOfTcpResult;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;
import com.kzksmarthome.common.lib.tools.DeviceState;
import com.kzksmarthome.common.lib.tools.Tools;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.net.ApiHost;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;

import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;


/**
 * 房间设备
 * Created by jack on 2016/9/6.
 */
public class DeviceFragment extends BaseRequestFragment implements RequestCallback,
        RoomSelectPopWindowAdapter.OnRoomItemClick, View.OnClickListener, DeviceAdapter.OnDeviceItemClick {
    //@BindView(R.id.top_rl)
    //RelativeLayout titleRl;

    @BindView(R.id.smart_device_room_recycle)
    RecyclerView smartDeviceRoomRecycle;

    private DeviceAdapter deviceAdapter;
    /**
     * 房间Map
     */
    //private Map<RoomInfo, List<DeviceInfo>> roomMap = new HashMap<RoomInfo, List<DeviceInfo>>();
    private Map<RoomInfo, List<DeviceInfo>> deviceMap = new HashMap<RoomInfo, List<DeviceInfo>>();
    private List<DeviceInfo> deviceList = new ArrayList<DeviceInfo>();
    /**
     * 选中的房间信息
     */
    private RoomInfo selectedRoom;

    private List<RoomInfo> roomList = new ArrayList<RoomInfo>();

    private PopupWindow roomPopWindow;
    private DeviceInfo lockInfo = null;
    private CustomProgressDialog mLoginDialog;
    /**
     * 更新图标的位置
     */
    private int mUpdatePosition = -1;
    /**
     * 拖动帮助类
     */
    //private ItemTouchHelper itemTouchHelper;
    /**
     * 4040设备控制弹窗
     */
    private Device4040Dialog mDevice4040Dialog;

    /**
     * 计量插座数据信息
     */
    private String[]  jlArray;
    /**
     * 是否在控制计量插座
     */
    private boolean isControl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_device_room_layout, container, false);
        ButterKnife.bind(this, mRootView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        smartDeviceRoomRecycle.setLayoutManager(gridLayoutManager);
        deviceAdapter = new DeviceAdapter(getActivity(),this);
        deviceAdapter.setOnDeviceItemClickListener(this);
        smartDeviceRoomRecycle.setAdapter(deviceAdapter);
        Bundle bundle = getArguments();
        selectedRoom = bundle.getParcelable("roomInfo");
        roomList = (List<RoomInfo>) bundle.getSerializable("roomList");
        //roomMap = (HashMap<RoomInfo, List<DeviceInfo>>) bundle.getSerializable("roomMap");
        if (null != selectedRoom) {
            Log.d("laixj", "房间信息：" + selectedRoom.toString());
        }
        /*if (null != roomMap) {
            Log.d("laixj", "房间设备信息：" + roomMap.toString());
            if (roomMap.containsKey(selectedRoom)) {
                Log.d("laixj", "设备信息：" + roomMap.get(selectedRoom).toString());
                deviceAdapter.setDeviceList(roomMap.get(selectedRoom));
            }
        }*/
        RestRequestApi.getDeviceByRoomId(getActivity(), selectedRoom.getId(), this);
        ((TopNavSubActivity) getActivity()).getTopTitleTV().setOnClickListener(this);
        GjjEventBus.getInstance().register(deviceEvent);
        com.kzksmarthome.common.module.user.UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        RestRequestApi.getAll4010NodeInfo(userInfo.gateway);
        /*MainTaskExecutor.scheduleTaskOnUiThread(8000, runnable);
        showLoading(R.string.loading,true);*/
        //itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        //itemTouchHelper.attachToRecyclerView(smartDeviceRoomRecycle);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 执行10秒检测
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            dismissProgressDialog();
        }
    };

    private void showLoading(int str, boolean flag) {
        CustomProgressDialog loginDialog = mLoginDialog;
        if (null == loginDialog) {
            loginDialog = new CustomProgressDialog(getActivity());
            mLoginDialog = loginDialog;
            loginDialog.setTipText(str);
            loginDialog.setCancelable(flag);
            loginDialog.setCanceledOnTouchOutside(true);
        }
        loginDialog.show();
    }

    /**
     * 关闭登录提示框
     */
    private void dismissProgressDialog() {
        if (null != mLoginDialog) {
            mLoginDialog.dismiss();
        }
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
            //roomList.addAll(roomMap.keySet());
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
    public void onDestroyView() {
        super.onDestroyView();
        dismissRoomPopWindow();
        GjjEventBus.getInstance().unregister(deviceEvent);
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
        // RestRequestApi.getDeviceByRoomId(getActivity(), selectedRoom.getId(), this, CacheControlMode.CACHE_REMOTE_NO_CACHE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_title_tv:
                showRoomPopWindow();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDeviceItemClick(int position, DeviceInfo deviceInfo) {
        if (null != deviceInfo && (DeviceTools.getDeviceType(deviceInfo) == DeviceTypeEnums.LOCK.getCode() || DeviceTools.getDeviceType(deviceInfo) == DeviceTypeEnums.DOOR.getCode())) {
            lockInfo = deviceInfo;
            RestRequestApi.getLockPwdList(getActivity(), deviceInfo.getDevice_id(), this);
        }
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
            deviceList = new ArrayList<DeviceInfo>();
            deviceMap.put(selectedRoom, deviceList);
            deviceAdapter.setDeviceList(deviceList);
            deviceAdapter.getDeviceList().add(new DeviceInfo(-1));
            deviceAdapter.notifyDataSetChanged();
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
                            deviceMap.put(selectedRoom, deviceList);
                            deviceAdapter.setDeviceList(deviceMap.get(selectedRoom));
                            deviceAdapter.notifyDataSetChanged();
                        } else {
                            deviceList = new ArrayList<DeviceInfo>();
                            deviceMap.put(selectedRoom, deviceList);
                            deviceAdapter.setDeviceList(deviceList);
                            deviceAdapter.getDeviceList().add(new DeviceInfo(-1));
                            deviceAdapter.notifyDataSetChanged();
                        }
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            SmartHomeApp.showToast("请求失败");
                        }
                        deviceList = new ArrayList<DeviceInfo>();
                        deviceMap.put(selectedRoom, deviceList);
                        deviceAdapter.setDeviceList(deviceList);
                        deviceAdapter.getDeviceList().add(new DeviceInfo(-1));
                        deviceAdapter.notifyDataSetChanged();
                    }
                } else {
                    SmartHomeApp.showToast("请求失败");
                    deviceList = new ArrayList<DeviceInfo>();
                    deviceMap.put(selectedRoom, deviceList);
                    deviceAdapter.setDeviceList(deviceList);
                    deviceAdapter.getDeviceList().add(new DeviceInfo(-1));
                    deviceAdapter.notifyDataSetChanged();
                }
            }
        } else if (url.startsWith(ApiHost.GET_LOCK_PWD_LIST_URL.substring(0, ApiHost.GET_LOCK_PWD_LIST_URL.lastIndexOf("=")))) {
            if (response != null) {
                GetLockPwdListResponse param = (GetLockPwdListResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult() && param.getResult().size() > 0) {
                            if (null != lockInfo) {
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("deviceInfo", lockInfo);
                                Log.d("laixj", "设备信息跳转：" + lockInfo.toString());
                                com.kzksmarthome.common.module.user.UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
                                if (UserRoleEnums.ADMIN.getCode() == userInfo.role) {
                                    PageSwitcher.switchToTopNavPage(getActivity(), DoorLockControlFragment.class, bundle, "", "门锁", "联系人", -1, null, R.drawable.icon_add);
                                } else if (UserRoleEnums.USER.getCode() == userInfo.role) {
                                    PageSwitcher.switchToTopNavPage(getActivity(), DoorLockControlFragment.class, bundle, "", "门锁", "");
                                }
                            }
                        } else {
                            if (null != lockInfo && (DeviceTools.getDeviceType(lockInfo) == DeviceTypeEnums.LOCK.getCode() || DeviceTools.getDeviceType(lockInfo) == DeviceTypeEnums.DOOR.getCode())) {
                                com.kzksmarthome.common.module.user.UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
                                if (UserRoleEnums.ADMIN.getCode() == userInfo.role) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("deviceid", lockInfo.getDevice_id());
                                    Log.d("laixj", "跳转到添加到开锁密码界面：" + lockInfo.toString());
                                    PageSwitcher.switchToTopNavPage(getActivity(), DoorLockPwddAddFragment.class, bundle, "", "设置开锁密码", "确定");
                                } else if (UserRoleEnums.USER.getCode() == userInfo.role) {
                                    showToast("该指纹锁尚未设置密码");
                                }
                            }
                        }
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            SmartHomeApp.showToast("请求失败");
                        }
                    }
                } else {
                    SmartHomeApp.showToast("请求失败");
                }
            }
        }
    }

    /**
     * 设备回调
     */
    public Object deviceEvent = new Object() {
        public void onEventBackgroundThread(EventOfTcpResult eventOfTcpResult) {
            if (getActivity() == null) {
                return;
            }
            try {
                if (eventOfTcpResult != null) {
                    final DeviceState deviceState = eventOfTcpResult.deviceState;
                    byte[] od = deviceState.deviceOD;
                    if (deviceState.dstAddr == null) {
                        return;
                    }
                    if (od[0] == 0x0f && od[1] == (byte) 0xe6) {//不处理红外转发设备
                        return;
                    }
                    if (od[0] == 0x0f && od[1] == (byte) 0xc8) {//判断是否为计量设备
                        if (deviceState.jlArray != null && deviceState.jlArray.length > 3) {
                            jlArray = deviceState.jlArray;
                            if(isControl) {
                                MainTaskExecutor.runTaskOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        show4040Dialog(deviceState.jlArray);
                                    }
                                });
                                isControl = false;
                            }
                        }
                    }
                    L.i("Request# connect to xxxxxxxxxxx");
                    if (deviceAdapter != null) {
                        List<DeviceInfo> deviceInfoArrayList = deviceAdapter.getDeviceList();
                        if (!Util.isListEmpty(deviceInfoArrayList)) {
                            for (DeviceInfo deviceInfo : deviceInfoArrayList) {
                                if (Tools.byte2HexStr(deviceState.dstAddr).equals(deviceInfo.getMac_address())) {//判断mac地址是否相同
                                    if (Tools.hexStr2Byte(deviceInfo.getDevice_type()) == deviceState.deviceType) {
                                        deviceInfo.setDevice_state1(deviceState.result_data_01);
                                        deviceInfo.setDevice_state2(deviceState.result_data_02);
                                        deviceInfo.setDevice_state3(deviceState.result_data_03);
                                    }
                                }
                            }
                            MainTaskExecutor.runTaskOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    deviceAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onEventMainThread(EventOfUpdateDeviceList event) {
            RestRequestApi.getDeviceByRoomId(getActivity(), selectedRoom.getId(), DeviceFragment.this);
        }
    };

    /**
     * 显示4040设备弹窗
     */
    public void show4040Dialog(String[] deviceState) {
        if (mDevice4040Dialog == null) {
            mDevice4040Dialog = new Device4040Dialog(getActivity());
        }
        if (mDevice4040Dialog.isShowing()) {
            mDevice4040Dialog.dismiss();
        }
        if (deviceAdapter.getmJLDeviceInfo() == null) {
            return;
        }
        //显示数据
        mDevice4040Dialog.getScene4040TitleTv().setText(deviceAdapter.getmJLDeviceInfo().getDevice_name());
        mDevice4040Dialog.getScene4040DyTv().setText("计量电压：" + deviceState[0] + "V");
        mDevice4040Dialog.getScene4040DlTv().setText("计量电量：" + deviceState[1] + "A");
        mDevice4040Dialog.getScene4040YgGlTv().setText("有功功率：" + deviceState[2] + "W");
        mDevice4040Dialog.getScene4040YgDlTv().setText("有功电量：" + deviceState[3] + "度");

        mDevice4040Dialog.getScene4040OpenTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestRequestApi.contorl4040(deviceAdapter.getIotMac(), deviceAdapter.getmJLDeviceInfo().getMac_address(), (byte) 0x01);
                mDevice4040Dialog.dismiss();
            }
        });
        mDevice4040Dialog.getScene4040CloseTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestRequestApi.contorl4040(deviceAdapter.getIotMac(), deviceAdapter.getmJLDeviceInfo().getMac_address(), (byte) 0x02);
                mDevice4040Dialog.dismiss();
            }
        });
        mDevice4040Dialog.getScene4040Cancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDevice4040Dialog.dismiss();
            }
        });
        mDevice4040Dialog.show();
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        if (getActivity().isFinishing()) {
            return;
        }
        dismissLoadingDialog();
        if (url.startsWith(ApiHost.GET_DEVICE_BY_ROOMID_URL.substring(0, ApiHost.GET_DEVICE_BY_ROOMID_URL.lastIndexOf("=")))) {
            deviceList = new ArrayList<DeviceInfo>();
            deviceMap.put(selectedRoom, deviceList);
            deviceAdapter.setDeviceList(deviceList);
            deviceAdapter.getDeviceList().add(new DeviceInfo(-1));
            deviceAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 条目拖动监听
     */
    private ItemTouchHelper.Callback itemTouchCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = 0;
            int swipeFlags = 0;
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                dragFlags = UP | DOWN | LEFT | RIGHT;
            } else {
                dragFlags = UP | DOWN;
            }
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
            int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(deviceList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(deviceList, i, i - 1);
                }
            }
            deviceAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(0);
        }
    };

    public String[] getJlArray() {
        return jlArray;
    }

    public void setJlArray(String[] jlArray) {
        this.jlArray = jlArray;
    }

    public boolean isControl() {
        return isControl;
    }

    public void setControl(boolean control) {
        isControl = control;
    }
}
