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
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.SmartHouseYCT.biz.base.TopNavSubActivity;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultAddRoom;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultUpdateRoom;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FloorInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FloorSelectPopWindowAdapter;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.ImageInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.RoomInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddRoomResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetImageListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetRoomListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.UpdateRoomResponse;
import com.kzksmarthome.SmartHouseYCT.util.ImageTypeEnums;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.net.ApiHost;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;
import com.squareup.okhttp.Request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * @author laixj
 * @version V1.0
 * @Title: RoomManageFragment
 * @Description: 房间管理界面
 * @date 2016/9/18 9:48
 */
public class RoomManageFragment extends BaseRequestFragment implements RequestCallback,
        RoomManageAdapter.OnRoomItemClick, FloorSelectPopWindowAdapter.OnFloorItemClick,
        RoomAddDialog.OnEnsureClick, View.OnClickListener {
    @BindView(R.id.smart_room_manage_recycle)
    RecyclerView smartRoomManageRecycle;

    //@BindView(R.id.room_manage_grid)
    //GridView roomManageGrid;

    private RoomManageAdapter roomAdapter;
    /**
     * 位置
     */
    private int mPosition = -1;
    /**
     * 设备Map
     */
    //private Map<FloorInfo, Map<RoomInfo, List<DeviceInfo>>> deviceMap = new HashMap<FloorInfo, Map<RoomInfo, List<DeviceInfo>>>();
    private Map<FloorInfo, List<RoomInfo>> roomMap = new HashMap<FloorInfo, List<RoomInfo>>();
    /**
     * 房间列表
     */
    private List<FloorInfo> floorList = new ArrayList<FloorInfo>();

    private PopupWindow floorPopWindow;

    private RoomAddDialog addDialog = null;
    private RoomAddDialog editDialog = null;

    private RoomAddDialogIconAdapter typeAdapter;

    private FloorInfo selectedFloor = null;
    List<RoomInfo> roomList = new ArrayList<RoomInfo>();
    private RoomInfo addInfo = null;
    private RoomInfo editInfo = null;
    private int editPosition = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_room_manage_layout, container, false);
        ButterKnife.bind(this, mRootView);
        Bundle bundle = getArguments();
        selectedFloor = (FloorInfo) bundle.getSerializable("floorInfo");
        floorList = (List<FloorInfo>) bundle.getSerializable("floorList");
        Log.d("laixj", "RoomManageFragment null == selectedFloor->" + (null == selectedFloor));
        Log.d("laixj", "RoomManageFragment null == floorList-->" + (null == floorList));
        if (null == floorList) {
            floorList = new ArrayList<FloorInfo>();
        }
        if (null != selectedFloor) {
            RestRequestApi.getRoomListByFloorId(getActivity(), selectedFloor.getId(), this);
        }
        initData();
        initViews();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData() {
        /*if(floorList != null && floorList.size() > 0){
            selectedFloor = floorList.get(0);
        }*/
        RestRequestApi.getImageList(getActivity(), ImageTypeEnums.ROOM.getCode(), this);
        typeAdapter = new RoomAddDialogIconAdapter(getActivity());
        typeAdapter.setSelected(-1);
    }

    private void initViews() {
        roomAdapter = new RoomManageAdapter(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        smartRoomManageRecycle.setLayoutManager(gridLayoutManager);
        roomAdapter.setOnRoomItemClick(this);
        smartRoomManageRecycle.setAdapter(roomAdapter);
        /*roomManageGrid.setAdapter(roomAdapter);
        roomManageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                RoomInfo roomInfo = roomAdapter.getRoomList().get(position);
                if (null != roomInfo) {
                    if (roomAdapter.isCanDelete()) {
                        SmartHomeApp.showToast("删除");
                        mPosition = position;
                        // TODO: 2016/9/17
                        RestRequestApi.deleteRoom(getActivity(), roomInfo.getId(), RoomManageFragment.this);
                    } else {
                        if (-1 == roomInfo.getId()) {
                            SmartHomeApp.showToast("添加房间");
                            // TODO: 2016/9/17
                            showAddDialog();
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("roomInfo", roomInfo);
                            bundle.putSerializable("roomList", (Serializable) roomAdapter.getRoomList());
                            //bundle.putSerializable("roomMap", (Serializable) roomAdapter.getRoomMap());
                            PageSwitcher.switchToTopNavPage(getActivity(), DeviceManageFragment.class, bundle, "", roomInfo.getName(), getString(R.string.delete_str), R.drawable.upload_icon_choose2_un, null);
                        }
                    }
                }
            }
        });*/
    }

    @Override
    public void onDeleteClick(int position, RoomInfo roomInfo) {
        mPosition = position;
        RestRequestApi.deleteRoom(getActivity(), roomInfo.getId(), this);
    }

    @Override
    public void onRoomItemClick(int position, RoomInfo roomInfo) {
        if (-1 == roomInfo.getId()) {
            SmartHomeApp.showToast("添加房间");
            showAddDialog();
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable("roomInfo", roomInfo);
            bundle.putSerializable("roomList", (Serializable) roomAdapter.getRoomList());
            //bundle.putSerializable("roomMap", (Serializable) roomAdapter.getRoomMap());
            PageSwitcher.switchToTopNavPage(getActivity(), DeviceManageFragment.class, bundle, "", roomInfo.getName(), getString(R.string.delete_str), R.drawable.pop_select_arrow_down, null);
        }
    }

    @Override
    public void onRoomItemLongClick(int position, RoomInfo roomInfo) {
        showEditDialog(position,roomInfo);
    }

    private void showAddDialog() {
        typeAdapter.setSelected(-1);
        addInfo = new RoomInfo(selectedFloor.getId());
        addDialog = new RoomAddDialog(getActivity(), 0, -1, addInfo, typeAdapter);
        addDialog.setOnEnsureClickListener(RoomManageFragment.this);
        addDialog.setCancelable(true);
        addDialog.setCanceledOnTouchOutside(true);
        addDialog.show();
    }

    private void showEditDialog(int position, RoomInfo roomInfo) {
        typeAdapter.setSelected(position);
        editInfo = roomInfo;
        editDialog = new RoomAddDialog(getActivity(), 1, position, roomInfo, typeAdapter);
        editDialog.setOnEnsureClickListener(RoomManageFragment.this);
        editDialog.setCancelable(true);
        editDialog.setCanceledOnTouchOutside(true);
        editDialog.show();
    }

    /**
     * 房间选择
     */
    @SuppressWarnings("unused")
    private void showFloorPopWindow() {
        dismissFloorPopWindow();
        View contentView;
        if (floorPopWindow == null) {
            contentView = LayoutInflater.from(getActivity()).inflate(R.layout.floor_select_popwindow, null);
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SmartHomeApp.showToast("click popwindow");
                    dismissFloorPopWindow();
                }
            });
            RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.floor_select_recycle);
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            final FloorSelectPopWindowAdapter adapter = new FloorSelectPopWindowAdapter(getActivity(), floorList);
            adapter.setOnFloorItemClickListener(this);
            recyclerView.setAdapter(adapter);

            floorPopWindow = new PopupWindow(contentView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, false);
            floorPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            floorPopWindow.setAnimationStyle(R.style.popwin_anim_style);
            floorPopWindow.setOutsideTouchable(true);
            floorPopWindow.setFocusable(true);
        } else {
            contentView = floorPopWindow.getContentView();
        }
        floorPopWindow.showAsDropDown(((TopNavSubActivity) getActivity()).getTopTitleTV());
    }

    /**
     * 取消房间选择弹出框
     *
     * @return
     */
    private boolean dismissFloorPopWindow() {
        if (null != floorPopWindow && floorPopWindow.isShowing()) {
            floorPopWindow.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("laixj", "RoomManageFragment onDestroy：");
        boolean flag = dismissFloorPopWindow();
        Log.d("laixj", "RoomManageFragment onDestroy：" + flag);
        try {
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
    public void onTitleBtnClick() {
        Log.d("laixj", "RoomManageFragment onTitleBtnClick");
        showFloorPopWindow();
    }

    @Override
    public void onRightBtnClick() {
        Log.d("laixj", "RoomManageFragment onRightBtnClick");
        if (null == roomAdapter) {
            return;
        }
        if (roomAdapter.isCanDelete()) {
            ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.delete_str);
            roomAdapter.setCanDelete(false);
            roomAdapter.getRoomList().add(new RoomInfo(-1, selectedFloor.getId()));
        } else if(roomAdapter.getRoomList().size() > 0){
            ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.commit_arrow_title);
            roomAdapter.setCanDelete(true);
            roomAdapter.getRoomList().remove(roomAdapter.getRoomList().size() - 1);
        }
        roomAdapter.notifyDataSetChanged();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_title_tv:
                //SmartHomeApp.showToast("显示房间");
                showFloorPopWindow();
                break;
            case R.id.top_right_tv:
                if (null == roomAdapter) {
                    return;
                }
                if (roomAdapter.isCanDelete()) {
                    ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.delete_str);
                    roomAdapter.setCanDelete(false);
                    roomAdapter.getRoomList().add(new RoomInfo(-1, selectedFloor.getId()));
                } else {
                    ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.commit_arrow_title);
                    roomAdapter.setCanDelete(true);
                    roomAdapter.getRoomList().remove(roomAdapter.getRoomList().size() - 1);
                }
                roomAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onFloorItemClick(FloorInfo floorInfo) {
        dismissFloorPopWindow();
        if (selectedFloor.equals(floorInfo)) {
            return;
        }
        selectedFloor = floorInfo;
        ((TopNavSubActivity) getActivity()).getTopTitleTV().setText(floorInfo.getName());
        ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.delete_str);
        /*List<RoomInfo> roomList = new ArrayList<RoomInfo>();
        roomList.addAll(deviceMap.get(floorInfo).keySet());
        roomAdapter.setRoomList(roomList);
        roomAdapter.getRoomList().add(new RoomInfo(-1));
        roomAdapter.setCanDelete(false);
        roomAdapter.setRoomMap(deviceMap.get(floorInfo));
        roomAdapter.notifyDataSetChanged();*/
        RestRequestApi.getRoomListByFloorId(getActivity(), floorInfo.getId(), this);
    }

    @Override
    public void onEnsureClick(int flag, int position, RoomInfo roomInfo) {
        hideKeyboardForCurrentFocus();
        Log.d("laixj", "添加/编辑：flag=" + flag + ";position=" + position);
        Log.d("laixj", "添加/编辑：roomInfo=" + roomInfo.toString());
        if (flag == 0) {//添加
            // TODO: 2016/9/11
            /*roomInfo.setRoomId(2046);
            deviceMap.get(selectedFloor).put(roomInfo, new ArrayList<DeviceInfo>());
            roomAdapter.getRoomList().add(roomAdapter.getRoomList().size()-1, roomInfo);
            roomAdapter.notifyDataSetChanged();*/
            RestRequestApi.addRoom(getActivity(), roomInfo.getFloor_id(), roomInfo.getName(), roomInfo.getImage(), this);
        } else if (flag == 1) {//编辑
            // TODO: 2016/9/11
            /*List<DeviceInfo> deviceList = deviceMap.get(selectedFloor).get(roomInfo);
            deviceMap.get(selectedFloor).remove(roomInfo);
            deviceMap.get(selectedFloor).put(roomInfo, deviceList);
            roomAdapter.getRoomList().set(position, roomInfo);
            roomAdapter.notifyDataSetChanged();*/
            editPosition = position;
            RestRequestApi.updateRoom(getActivity(), roomInfo.getId(), roomInfo.getName(), roomInfo.getImage(), this);
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
        if (url.startsWith(ApiHost.GET_ROOM_LIST_URL.substring(0, ApiHost.GET_ROOM_LIST_URL.lastIndexOf("=")))) {
            roomAdapter.setRoomList(roomList);
            roomAdapter.getRoomList().add(new RoomInfo(-1, selectedFloor.getId()));
            roomMap.put(selectedFloor, roomAdapter.getRoomList());
            roomAdapter.setCanDelete(false);
            roomAdapter.notifyDataSetChanged();
            ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.delete_str);
        }
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        if (getActivity() == null) {
            return;
        }
        dismissLoadingDialog();
        if (url.startsWith(ApiHost.GET_ROOM_LIST_URL.substring(0, ApiHost.GET_ROOM_LIST_URL.lastIndexOf("=")))) {
            if (response != null) {
                GetRoomListResponse param = (GetRoomListResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            roomList = param.getResult();
                            roomAdapter.setRoomList(roomList);
                            roomAdapter.getRoomList().add(new RoomInfo(-1, selectedFloor.getId()));
                            roomMap.put(selectedFloor, roomAdapter.getRoomList());
                            roomAdapter.setCanDelete(false);
                            roomAdapter.notifyDataSetChanged();
                            ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.delete_str);
                        } else {
                            roomAdapter.setRoomList(new ArrayList<RoomInfo>());
                            roomAdapter.getRoomList().add(new RoomInfo(-1, selectedFloor.getId()));
                            roomMap.put(selectedFloor, roomAdapter.getRoomList());
                            roomAdapter.setCanDelete(false);
                            roomAdapter.notifyDataSetChanged();
                            ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.delete_str);
                        }
                    } else {
                        roomAdapter.setRoomList(new ArrayList<RoomInfo>());
                        roomAdapter.getRoomList().add(new RoomInfo(-1, selectedFloor.getId()));
                        roomMap.put(selectedFloor, roomAdapter.getRoomList());
                        roomAdapter.setCanDelete(false);
                        roomAdapter.notifyDataSetChanged();
                        ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.delete_str);
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            SmartHomeApp.showToast("请求失败");
                        }
                    }
                } else {
                    roomAdapter.setRoomList(new ArrayList<RoomInfo>());
                    roomAdapter.getRoomList().add(new RoomInfo(-1, selectedFloor.getId()));
                    roomMap.put(selectedFloor, roomAdapter.getRoomList());
                    roomAdapter.setCanDelete(false);
                    roomAdapter.notifyDataSetChanged();
                    ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.delete_str);
                    SmartHomeApp.showToast("请求失败");
                }
            }
        } else if (ApiHost.ADD_ROOM_URL.equals(url)) {
            if (response != null) {
                AddRoomResponse param = (AddRoomResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            RoomInfo newInfo = new RoomInfo(param.getResult());
                            roomAdapter.getRoomList().add(roomAdapter.getRoomList().size() - 1, newInfo);
                            roomMap.put(selectedFloor, roomAdapter.getRoomList());
                            roomAdapter.notifyDataSetChanged();
                            EventOfResultAddRoom event = new EventOfResultAddRoom();
                            event.roomInfo = newInfo;
                            GjjEventBus.getInstance().post(event);
                        }
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            SmartHomeApp.showToast("房间添加失败");
                        }
                    }
                } else {
                    SmartHomeApp.showToast("房间添加失败");
                }
            }
        } else if (ApiHost.UPDATE_ROOM_URL.equals(url)) {
            if (response != null) {
                UpdateRoomResponse param = (UpdateRoomResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            roomAdapter.getRoomList().set(editPosition, new RoomInfo(param.getResult()));
                            roomMap.put(selectedFloor, roomAdapter.getRoomList());
                            roomAdapter.notifyDataSetChanged();
                        }
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            SmartHomeApp.showToast("房间修改失败");
                        }
                    }
                } else {
                    SmartHomeApp.showToast("房间修改失败");
                }
            }
        } else if (url.startsWith(ApiHost.GET_IMG_URL.substring(0, ApiHost.GET_IMG_URL.lastIndexOf("=")))) {
            if (response != null) {
                GetImageListResponse param = (GetImageListResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            Log.d("laixj", "房间图片->" + param.getResult().toString());
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
                            Log.d("laixj", "房间图片->" + "fail");
                            SmartHomeApp.showToast("拉取房间图片失败");
                        }
                    }
                } else {
                    Log.d("laixj", "房间图片->" + "(param == null)");
                    SmartHomeApp.showToast("拉取房间图片失败");
                }
            }
        } else if (ApiHost.DELETE_ROOM_URL.equals(url)) {
            if (!Util.isListEmpty(roomAdapter.getRoomList()) && mPosition != -1 && roomAdapter.getRoomList().size() > mPosition) {
                RoomInfo roomInfo = roomAdapter.getRoomList().get(mPosition);
                roomAdapter.getRoomList().remove(mPosition);
                roomAdapter.notifyDataSetChanged();
                EventOfResultUpdateRoom event = new EventOfResultUpdateRoom();
                event.floorId = roomInfo.getFloor_id();
                GjjEventBus.getInstance().post(event);
            }
        }
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        L.d("loginBizFail");
        if (getActivity().isFinishing()) {
            return;
        }
        dismissLoadingDialog();
        if (url.startsWith(ApiHost.GET_ROOM_LIST_URL.substring(0, ApiHost.GET_ROOM_LIST_URL.lastIndexOf("=")))) {
            roomAdapter.setRoomList(roomList);
            roomAdapter.getRoomList().add(new RoomInfo(-1, selectedFloor.getId()));
            roomMap.put(selectedFloor, roomAdapter.getRoomList());
            roomAdapter.setCanDelete(false);
            roomAdapter.notifyDataSetChanged();
            ((TopNavSubActivity) getActivity()).getTopRightTV().setText(R.string.delete_str);
        }
    }

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
}
