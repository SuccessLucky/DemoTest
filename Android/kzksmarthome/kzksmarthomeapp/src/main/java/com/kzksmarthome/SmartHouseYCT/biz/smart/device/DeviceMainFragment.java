package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

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
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultAddFloor;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultAddRoom;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultDeleteFloor;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultSelectGw;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultUpdateFloor;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultUpdateRoom;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfUpdateDeviceList;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetFloorListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetRoomListResponse;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.net.ApiHost;
import com.kzksmarthome.common.module.user.UserInfo;
import com.squareup.okhttp.Request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设备界面
 * Created by jack on 2016/8/27.
 */
public class DeviceMainFragment extends BaseRequestFragment implements RequestCallback,
		FloorSelectPopWindowAdapter.OnFloorItemClick, RoomAdapter.OnRoomItemClick {

	@BindView(R.id.device_main_recycle)
	RecyclerView deviceMainRecycle;

	private RoomAdapter roomAdapter;
	/**
	 * 设备Map
	 */
	//private Map<FloorInfo, Map<RoomInfo, List<DeviceInfo>>> deviceMap = new HashMap<FloorInfo, Map<RoomInfo, List<DeviceInfo>>>();
	/**
	 * 楼层列表
	 */
	private List<FloorInfo> floorList = new ArrayList<FloorInfo>();
	//private Map<FloorInfo, List<RoomInfo>> roomMap = new HashMap<FloorInfo, List<RoomInfo>>();
	private FloorInfo selectedFloor = null;

	private PopupWindow floorPopWindow;
	private String originGwMac = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.smart_layout_device_fragment, container, false);
		ButterKnife.bind(this, mRootView);
		Log.d("laixj", "设备页请求数据-onCreateView->");
		initData();
		initView();
		GjjEventBus.getInstance().register(mDeviceMainEvent);
		UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
		if (null != userInfo) {
			originGwMac = userInfo.gateway;
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden){
			initData();
		}
	}

	@Override
	public void onRoomItemClick(int position) {
		RoomInfo roomInfo = roomAdapter.getRoomList().get(position);
		if (null != roomInfo) {
			Bundle bundle = new Bundle();
			bundle.putParcelable("roomInfo", roomInfo);
			bundle.putSerializable("roomList", (Serializable) roomAdapter.getRoomList());
			PageSwitcher.switchToTopNavPage(getActivity(), DeviceFragment.class, bundle, "", roomInfo.getName(), "", R.drawable.pop_select_arrow_down, null);
		}
	}

	private void initData() {
		RestRequestApi.getFloorList(getActivity(), this);
	}

	private void refreshRoomInfo() {
		RestRequestApi.getRoomListByFloorId(getActivity(), selectedFloor.getId(), this);
	}

	private void initView() {
		GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
		deviceMainRecycle.setLayoutManager(gridLayoutManager);
		roomAdapter = new RoomAdapter(getActivity());
		roomAdapter.setOnRoomItemClickListener(this);
		deviceMainRecycle.setAdapter(roomAdapter);
	}

	/**
	 * 请求回调
	 */
	public Object mDeviceMainEvent = new Object() {
		/**
		 * 切换网关之后需重新请求数据
		 * @param data
		 */
		public void onEventMainThread(EventOfResultSelectGw data) {
			if (null == data.gwInfo) {
				return;
			}
			Log.d("switchgw", "设备页切换网关-data.gwInfo->" + data.gwInfo.getGateway_name());
			GatewayInfo newGw = data.gwInfo;
			if (!data.isAddGW && newGw.getMac_address().equals(originGwMac)) {
				return;
			}
			Log.d("switchgw", "设备页切换网关-originGwMac->" + originGwMac);
			originGwMac = newGw.getMac_address();
			Log.d("laixj", "设备页切换网关重新请求数据->");
			floorList = new ArrayList<FloorInfo>();
			roomAdapter.setRoomList(new ArrayList<RoomInfo>());
			roomAdapter.notifyDataSetChanged();
			initData();
		}

		public void onEventMainThread(EventOfUpdateDeviceList data) {
			initData();
		}

		/**
		 * 增加楼层后更新楼层数据
		 * @param data
		 */
		public void onEventMainThread(EventOfResultAddFloor data) {
			if (null == data.floorInfo) {
				return;
			}
			floorPopWindow = null;
			floorList.add(data.floorInfo);
		}

		/**
		 * 更新楼层后刷新楼层数据
		 * @param data
		 */
		public void onEventMainThread(EventOfResultUpdateFloor data) {
			if (null == data.floorInfo) {
				return;
			}
			int size = floorList.size();
			for (int i = 0; i < size; i++) {
				FloorInfo floorInfo = floorList.get(i);
				if (floorInfo.equals(data.floorInfo)) {
					floorPopWindow = null;
					floorList.set(i, data.floorInfo);
					break;
				}
			}
		}

		/**
		 * 删除楼层后更新楼层数据
		 * @param data
		 */
		public void onEventMainThread(EventOfResultDeleteFloor data) {
			if (null == data.floorInfo) {
				return;
			}
			int size = floorList.size();
			for (int i = 0; i < size; i++) {
				FloorInfo floorInfo = floorList.get(i);
				if (floorInfo.equals(data.floorInfo)) {
					floorPopWindow = null;
					floorList.remove(i);
					break;
				}
			}
			if (selectedFloor.equals(data.floorInfo)) {
				if(floorList.size() == 0){
					roomAdapter.getRoomList().clear();
					roomAdapter.notifyDataSetChanged();
				}else{
					selectedFloor = floorList.get(0);
					refreshRoomInfo();
				}
			}
		}

		/**
		 * 更新房间数据
		 * @param data
		 */
		public void onEventMainThread(EventOfResultUpdateRoom data) {
			if (data.floorId <= 0 || null == selectedFloor) {
				return;
			}
			if (selectedFloor.getId() == data.floorId) {
				refreshRoomInfo();
			}
		}

		/**
		 * 更新房间数据
		 * @param data
		 */
		public void onEventMainThread(EventOfResultAddRoom data) {
			if (null == data.roomInfo || null == selectedFloor) {
				return;
			}
			RoomInfo newInfo = data.roomInfo;
			if (selectedFloor.getId() == newInfo.getFloor_id()) {
				roomAdapter.getRoomList().add(newInfo);
				roomAdapter.notifyDataSetChanged();
			}
		}
	};

	/**
	 * 楼层选择
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
					showToast("click popwindow");
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
	}

	/**
	 * 取消楼层选择弹出框
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
		GjjEventBus.getInstance().unregister(mDeviceMainEvent);
		dismissFloorPopWindow();
	}

	@OnClick(R.id.title_tv)
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.title_tv:
				showToast("显示楼层");
				showFloorPopWindow();
				break;
		}
	}

	@Override
	public void onFloorItemClick(FloorInfo floorInfo) {
		dismissFloorPopWindow();
		if (null == floorInfo) {
			return;
		}
		List<RoomInfo> roomList = new ArrayList<RoomInfo>();
        /*roomList.addAll(deviceMap.get(floorInfo).keySet());
        roomAdapter.setRoomList(roomList);
        roomAdapter.setRoomMap(deviceMap.get(floorInfo));
        roomAdapter.notifyDataSetChanged();*/
		selectedFloor = floorInfo;
		RestRequestApi.getRoomListByFloorId(getActivity(), floorInfo.getId(), this);
	}

	@Override
	public void onFailure(Request request, String url, Exception e) {
		L.d("loginFail");
		if (getActivity() == null) {
			return;
		}
		dismissLoadingDialog();
		SmartHomeApp.showToast("请检查网络");
	}

	@Override
	public void onBizSuccess(ResponseParam response, String url, int from) {
		if (getActivity() == null) {
			return;
		}
		dismissLoadingDialog();
		if ( url.startsWith(ApiHost.FLOOR_LIST_URL)) {
			if (response != null) {
				GetFloorListResponse param = (GetFloorListResponse) response.body;
				if (param != null) {
					if (param.isSuccess()) {
						if (null != param.getResult()) {
							floorList = param.getResult();
							if (null != floorList && floorList.size() > 0) {
								selectedFloor = floorList.get(0);
								RestRequestApi.getRoomListByFloorId(getActivity(), selectedFloor.getId(), this);
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
		} else if (url.startsWith(ApiHost.GET_ROOM_LIST_URL.substring(0, ApiHost.GET_ROOM_LIST_URL.lastIndexOf("=")))) {
			if (response != null) {
				GetRoomListResponse param = (GetRoomListResponse) response.body;
				if (param != null) {
					if (param.isSuccess()) {
						if (null != param.getResult()) {
							//roomMap.put(selectedFloor, param.getResult());
							roomAdapter.setRoomList(param.getResult());
							//roomAdapter.setRoomMap(deviceMap.get(floorInfo));
							roomAdapter.notifyDataSetChanged();
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

	@Override
	public void onBizFail(ResponseParam response, String url, int from) {
		L.d("loginBizFail");
		if (getActivity().isFinishing()) {
			return;
		}
		dismissLoadingDialog();
	}
}
