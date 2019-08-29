package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.SmartHouseYCT.biz.base.TopNavSubActivity;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultAddFloor;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultDeleteFloor;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultUpdateFloor;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfUpdateDeviceList;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FloorInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddFloorResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.DeleteFloorResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetFloorListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.UpdateFloorResponse;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.net.ApiHost;
import com.squareup.okhttp.Request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import in.srain.cube.util.CLog;

/**
 * @author laixj
 * @version V1.0
 * @Title: SmartManageFragment
 * @Description: 智能管理界面
 * @date 2016/9/16 18:03
 */
public class SmartManageFragment extends BaseRequestFragment implements RequestCallback, View.OnClickListener,
		FloorListAdapter.OnFloorItemClick, FloorAddDialog.OnEnsureClick, FloorOperationSelectDialog.OnOperationSelectItemClick,
		FloorDelConfirmDialog.OnEnsureClick {

	@BindView(R.id.smart_smart_manage_recycle)
	RecyclerView smartSmartManageRecycle;
	private FloorOperationSelectDialog selectDialog = null;
	private FloorDelConfirmDialog delConfirmDialog = null;

	private FloorListAdapter adapter;

	private List<FloorInfo> floorList = new ArrayList<FloorInfo>();

	private FloorAddDialog addDialog = null;

	private FloorAddDialog editDialog = null;

	private FloorInfo addInfo = null;
	private FloorInfo editInfo = null;
	private int editPosition = -1;
	private int delPosition = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.smart_smart_manage_layout, container, false);
		ButterKnife.bind(this, mRootView);
		initData();
		initView();
		//((TopNavSubActivity) getActivity()).getTopRightTV().setOnClickListener(this);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void initData() {
		showLoadingDialog(R.string.loading_str, true);
		RestRequestApi.getFloorList(getActivity(), this);
	}

	private void initView() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(smartSmartManageRecycle.getContext());
		smartSmartManageRecycle.setLayoutManager(linearLayoutManager);
		adapter = new FloorListAdapter(getActivity(), floorList);
		adapter.setOnFloorItemClickListener(this);
		smartSmartManageRecycle.setAdapter(adapter);
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
		if (url.startsWith(ApiHost.FLOOR_LIST_URL)) {
			if (response != null) {
				GetFloorListResponse param = (GetFloorListResponse) response.body;
				if (param != null) {
					if (param.isSuccess()) {
						if (null != param.getResult()) {
							floorList = param.getResult();
							adapter.setFloorList(floorList);
							adapter.notifyDataSetChanged();
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
		} else if (ApiHost.ADD_FLOOR_URL.equals(url)) {
			if (response != null) {
				AddFloorResponse param = (AddFloorResponse) response.body;
				if (param != null) {
					if (param.isSuccess()) {
						if (null != param.getResult()) {
							FloorInfo newInfo = new FloorInfo(param.getResult());
							adapter.getFloorList().add(newInfo);
							adapter.notifyDataSetChanged();
							EventOfResultAddFloor event = new EventOfResultAddFloor();
							event.floorInfo = newInfo;
							GjjEventBus.getInstance().post(event);
						}
					} else {
						if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
							SmartHomeApp.showToast(param.getError().getMessage());
						} else {
							SmartHomeApp.showToast("楼层添加失败");
						}
					}
				} else {
					SmartHomeApp.showToast("楼层添加失败");
				}
			}
		} else if (ApiHost.UPDATE_FLOOR_URL.equals(url)) {
			if (response != null) {
				UpdateFloorResponse param = (UpdateFloorResponse) response.body;
				if (param != null) {
					if (param.isSuccess()) {
						if (null != param.getResult()) {
							FloorInfo newInfo = new FloorInfo(param.getResult());
							adapter.getFloorList().set(editPosition, newInfo);
							adapter.notifyDataSetChanged();
							EventOfResultUpdateFloor event = new EventOfResultUpdateFloor();
							event.floorInfo = newInfo;
							GjjEventBus.getInstance().post(event);
						}
						//adapter.getFloorList().set(editPosition, new FloorInfo(editInfo));
						//adapter.notifyDataSetChanged();
					} else {
						if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
							SmartHomeApp.showToast(param.getError().getMessage());
						} else {
							SmartHomeApp.showToast("楼层修改失败");
						}
					}
				} else {
					SmartHomeApp.showToast("楼层修改失败");
				}
			}
		} else if (url.equals(ApiHost.DELETE_FLOOR_URL)) {
			dismissLoadingDialog();
			if (response != null) {
				DeleteFloorResponse param = (DeleteFloorResponse) response.body;
				if (param != null) {
					if (param.isSuccess()) {
						SmartHomeApp.showToast("楼层删除成功");
						CLog.i("laixj", "楼层删除成功");
						EventOfResultDeleteFloor event = new EventOfResultDeleteFloor();
						event.floorInfo = floorList.get(delPosition);;
						GjjEventBus.getInstance().post(event);
						floorList.remove(delPosition);
						adapter.notifyDataSetChanged();
					} else {
						if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
							CLog.i("laixj", param.getError().getMessage());
							SmartHomeApp.showToast(param.getError().getMessage());
						} else {
							CLog.i("laixj", "楼层删除失败");
							SmartHomeApp.showToast("请求失败");
						}
					}
				} else {
					CLog.i("laixj", "楼层删除失败");
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

	@Override
	public void onDestroyView() {
		super.onDestroyView();
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
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.top_right_tv:
				// TODO: 2016/9/15
				showAddDialog();
				break;
			default:
				break;
		}
	}

	@Override
	public void onRightBtnClick() {
		Log.d("laixj", "SmartManageFragment onRightBtnClick");
		showAddDialog();
	}

	private void showAddDialog() {
		addInfo = new FloorInfo();
		addDialog = new FloorAddDialog(getActivity(), 0, -1, new FloorInfo());
		addDialog.setOnEnsureClickListener(SmartManageFragment.this);
		addDialog.setCancelable(true);
		addDialog.setCanceledOnTouchOutside(true);
		addDialog.show();
	}

	private void showEditDialog(int position, FloorInfo floorInfo) {
		editInfo = floorInfo;
		editDialog = new FloorAddDialog(getActivity(), 1, position, floorInfo);
		editDialog.setOnEnsureClickListener(SmartManageFragment.this);
		editDialog.setCancelable(true);
		editDialog.setCanceledOnTouchOutside(true);
		editDialog.show();
	}

	@Override
	public void onEnsureClick(int flag, int position, FloorInfo floorInfo) {
		hideKeyboardForCurrentFocus();
		Log.d("laixj", "添加/编辑：flag=" + flag + ";position=" + position);
		Log.d("laixj", "添加/编辑：floorInfo=" + floorInfo.toString());
		if (flag == 0) {//添加
			// TODO: 2016/9/11
			addInfo = floorInfo;
			showLoadingDialog(R.string.loading_str, false);
			RestRequestApi.addFloor(getActivity(), floorInfo.getName(), "", this);
		} else if (flag == 1) {//编辑
			// TODO: 2016/9/11
			editInfo = floorInfo;
			editPosition = position;
			showLoadingDialog(R.string.loading_str, false);
			RestRequestApi.updateFloor(getActivity(), floorInfo.getId(), floorInfo.getName(), "", this);
		}
	}

	@Override
	public void onFloorItemClick(int position, FloorInfo floorInfo) {
		// TODO: 2016/9/16
		Log.d("laixj", "SmartManageFragment onFloorItemClick：" + (getActivity() instanceof TopNavSubActivity));
		/*Intent intent = new Intent(getActivity(), RoomManageFragment.class);
		intent.putExtra("floorInfo", (Serializable) floorInfo);
        PageSwitcher.startActivity(getActivity(), intent);*/
		Bundle bundle = new Bundle();
		bundle.putSerializable("floorInfo", (Serializable) floorInfo);
		bundle.putSerializable("floorList", (Serializable) floorList);
		//bundle.putInt("layoutid", R.layout.activity_top_nav1);
		Log.d("laixj", "房间管理跳转：" + floorInfo.toString());
		PageSwitcher.switchToTopNavPage(getActivity(), RoomManageFragment.class, bundle, "", floorInfo.getName(), "删除", R.drawable.pop_select_arrow_down);
	}

	@Override
	public void onFloorItemLongClick(int position, FloorInfo floorInfo) {
		showOperatorSelectDialog(position, floorInfo);
	}

	private void showOperatorSelectDialog(int position, FloorInfo floorInfo) {
		if (null == selectDialog) {
			selectDialog = new FloorOperationSelectDialog(getActivity(), position, floorInfo);
		} else {
			selectDialog.setPosition(position);
			selectDialog.setFloorInfo(floorInfo);
		}
		selectDialog.setOnOperationSelectClickListener(this);
		selectDialog.setCancelable(true);
		selectDialog.setCanceledOnTouchOutside(true);
		selectDialog.show();
	}

	@Override
	public void onEnsureClick(int position, FloorInfo floorInfo) {
		delPosition = position;
		if (null == floorInfo) {
			return;
		}
		showLoadingDialog(R.string.loading_str, false);
		RestRequestApi.deleteFloor(getActivity(), floorInfo.getId(), this);
	}

	@Override
	public void onDeleteClick(int position, FloorInfo floorInfo) {
		showDelConfirmDialog(position, floorInfo);
	}

	@Override
	public void onEditClick(int position, FloorInfo floorInfo) {
		showEditDialog(position, floorInfo);
	}

	private void showDelConfirmDialog(int position, FloorInfo floorInfo) {
		if (null == delConfirmDialog) {
			delConfirmDialog = new FloorDelConfirmDialog(getActivity(), position, floorInfo);
		} else {
			delConfirmDialog.setPosition(position);
			delConfirmDialog.setFloorInfo(floorInfo);
		}
		delConfirmDialog.setOnEnsureClickListener(this);
		delConfirmDialog.setCancelable(true);
		delConfirmDialog.setCanceledOnTouchOutside(true);
		delConfirmDialog.show();
	}

	@Override
	public boolean goBack() {
		GjjEventBus.getInstance().post(new EventOfUpdateDeviceList());
		return super.goBack();
	}
}
