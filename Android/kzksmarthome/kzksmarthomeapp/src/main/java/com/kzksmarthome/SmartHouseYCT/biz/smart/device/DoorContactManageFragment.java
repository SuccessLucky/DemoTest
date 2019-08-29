package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

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
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddFingerResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetDoorLockUserListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter.DoorLockContactAddDialog;
import com.kzksmarthome.common.biz.widget.ConfirmDialog;
import com.kzksmarthome.common.event.EventOfTcpResult;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.tools.DeviceState;
import com.kzksmarthome.common.lib.tools.Tools;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.net.ApiHost;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * @author laixj
 * @version V1.0
 * @Title: DoorContactManageFragment
 * @Description: 门锁联系人管理界面
 * @date 2016/10/31 21:03
 */
public class DoorContactManageFragment extends BaseRequestFragment implements RequestCallback, View.OnClickListener,
        DoorLockContactListAdapter.OnDoorLockContactItemClick, DoorLockContactAddDialog.OnEnsureClick {

    @BindView(R.id.smart_door_contact_recycle)
    RecyclerView smartDoorContactRecycle;
    private DoorLockContactListAdapter adapter;

    private List<DoorAccessInfo> contactList = new ArrayList<DoorAccessInfo>();

    private DoorLockContactAddDialog addDialog = null;

    private DoorLockContactAddDialog editDialog = null;

    private DeviceInfo deviceInfo = null;
    /**
     * 删除编号
     */
    private int delPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_door_contact_manage_layout, container, false);
        ButterKnife.bind(this, mRootView);
        initData();
        initView();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData() {
        Bundle bundle = getArguments();
        deviceInfo = (DeviceInfo) bundle.getSerializable("deviceinfo");
        if (null != deviceInfo) {
            RestRequestApi.getDoorUserList(getActivity(), deviceInfo.getDevice_id(),null, this);
        }
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(smartDoorContactRecycle.getContext());
        smartDoorContactRecycle.setLayoutManager(linearLayoutManager);
        adapter = new DoorLockContactListAdapter(getActivity(), contactList);
        adapter.setOnDoorLockContactItemClick(this);
        smartDoorContactRecycle.setAdapter(adapter);
        GjjEventBus.getInstance().register(deviceEvent);
    }

    /**
     * 请求回调
     */
    public Object deviceEvent = new Object() {
        public void onEventMainThread(EventOfTcpResult eventOfTcpResult) {
            if (getActivity() == null) {
                return;
            }
            try {
                if (eventOfTcpResult != null) {
                    DeviceState deviceState = eventOfTcpResult.deviceState;
                    String dstAdd = Tools.byte2HexStr(deviceState.dstAddr);
                    if (deviceState != null && deviceInfo.getMac_address().equals(dstAdd)) {
                        if (deviceState.dstAddr == null) {
                            return;
                        }
                      if(deviceState.deviceOD != null && deviceState.deviceOD.length > 1 && deviceState.deviceOD[0] == 0x0f && deviceState.deviceOD[1] == (byte)0xbe){//判读是否为4030
                          if( deviceState.deviceType == 0x02 && (deviceState.deviceProduct == 0x02 && deviceState.lockOperateType == 0x50)|| deviceState.deviceProduct == 0x03) {//指纹锁
                              int lockId = deviceState.lockState;
                                  showAddDialog(lockId);
                          }
                      }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GjjEventBus.getInstance().unregister(deviceEvent);
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
                //showAddDialog();
                showLoadingDialog(R.string.loading_zw_sb,true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRightBtnClick() {
        showLoadingDialog(R.string.loading_zw_sb,true);
    }

    private void showAddDialog(int lockId) {
        dismissLoadingDialog();
        DoorAccessInfo doorAccessInfo = new DoorAccessInfo();
        doorAccessInfo.setLock_id(lockId);
        addDialog = new DoorLockContactAddDialog(getActivity(), 0, -1, doorAccessInfo);
        addDialog.getmEtvId().setText(lockId +"");
        addDialog.setOnEnsureClickListener(DoorContactManageFragment.this);
        addDialog.setCancelable(true);
        addDialog.setCanceledOnTouchOutside(true);
        addDialog.show();
    }

    private void showEditDialog(int position, DoorAccessInfo contactInfo) {
        editDialog = new DoorLockContactAddDialog(getActivity(), 1, position, contactInfo);
        editDialog.setOnEnsureClickListener(DoorContactManageFragment.this);
        editDialog.setCancelable(true);
        editDialog.setCanceledOnTouchOutside(true);
        editDialog.show();
    }

    @Override
    public void onDoorLockContactItemClick(int position, DoorAccessInfo doorAccessInfo) {

    }

    @Override
    public void onDoorLockContactItemLongClick(final int position, DoorAccessInfo doorAccessInfo) {
        final ConfirmDialog confirmDialog = new ConfirmDialog(getActivity());
        confirmDialog.setContent("确定删除联系人？");
        confirmDialog.setConfirm("确定");
        confirmDialog.setCancel("取消");
        confirmDialog.setConfirmClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contactList != null && position < contactList.size()) {
                    showLoadingDialog(R.string.loading, false);
                    delPosition = position;
                    RestRequestApi.delFingerorPwd(getActivity(), contactList.get(position).getLock_id(), "fingerprint", DoorContactManageFragment.this);
                }
                confirmDialog.dismiss();
            }
        });
        confirmDialog.show();
    }

    @Override
    public void onEnsureClick(int flag, int position, DoorAccessInfo contactInfo) {
        Log.d("laixj", "添加/编辑：flag=" + flag + ";position=" + position);
        Log.d("laixj", "添加/编辑：contactInfo=" + contactInfo.toString());
        if (flag == 0) {//添加
            contactInfo.setDevice_id(deviceInfo.getDevice_id());
            RestRequestApi.addDoorFinger(getActivity(), contactInfo.getDevice_id(), contactInfo.getUser_name(), contactInfo.getLock_id() + "", this);
        } else if (flag == 1) {//编辑
            // TODO: 2016/9/11
            //adapter.getSecurityUserList().set(position, contactInfo);
            //adapter.notifyDataSetChanged();
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
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        if (getActivity() == null) {
            return;
        }
        dismissLoadingDialog();
        if (url.startsWith(ApiHost.GET_LOCK_USER_URL.substring(0, ApiHost.GET_LOCK_USER_URL.lastIndexOf("=")))) {
            if (response != null) {
                GetDoorLockUserListResponse param = (GetDoorLockUserListResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            contactList = param.getResult();
                            adapter.setAccessList(contactList);
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
        } else if (ApiHost.ADD_FINGERPRINT_URL.equals(url)) {
            if (response != null) {
                AddFingerResponse param = (AddFingerResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            adapter.getAccessList().add(param.getResult());
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                            SmartHomeApp.showToast(param.getError().getMessage());
                        } else {
                            SmartHomeApp.showToast("联系人添加失败");
                        }
                    }
                } else {
                    SmartHomeApp.showToast("联系人添加失败");
                }
            }
        }else if(ApiHost.DELETE_FINGER_PWD_URL.equals(url)) {
            if (contactList != null && delPosition < contactList.size()) {
                contactList.remove(delPosition);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        if (getActivity().isFinishing()) {
            return;
        }
        dismissLoadingDialog();
    }
}
