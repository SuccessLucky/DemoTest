package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultDeviceRights;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultSceneRights;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FamilyMemberInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddUserRightsResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetRightsListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.SceneInfo;
import com.kzksmarthome.SmartHouseYCT.biz.widget.MyLinearLayoutManager;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.module.net.ApiHost;
import com.squareup.okhttp.Request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * @author laixj
 * @version V1.0
 * @Title: RightsListFragment
 * @Description: 权限列表界面
 * @date 2016/9/16 8:09
 */
public class RightsListFragment extends BaseRequestFragment implements RequestCallback, View.OnClickListener,
        SceneRightsListAdapter.OnSceneItemClick, DeviceRightsListAdapter.OnDeviceItemClick, RightsTypeSelectDialog.OnRightsTypeItemClick {

    @BindView(R.id.smart_rights_scene_recycle)
    RecyclerView smartRightsSceneRecycle;
    @BindView(R.id.smart_rights_device_recycle)
    RecyclerView smartRightsDeviceRecycle;
    @BindView(R.id.rights_scene_empty_tv)
    TextView rightsSceneEmptyTv;
    @BindView(R.id.rights_device_empty_tv)
    TextView rightsDeviceEmptyTv;

    private SceneRightsListAdapter sceneAdapter;

    private DeviceRightsListAdapter deviceAdapter;

    private List<SceneInfo> sceneList = new ArrayList<SceneInfo>();

    private List<DeviceInfo> deviceList = new ArrayList<DeviceInfo>();

    private RightsTypeSelectDialog selectDialog = null;

    private FamilyMemberInfo userInfo = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_rights_list_layout, container, false);
        ButterKnife.bind(this, mRootView);
        initData();
        initView();
        //((TopNavSubActivity) getActivity()).getTopRightTV().setOnClickListener(this);
        GjjEventBus.getInstance().register(mRightsManageEvent);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData() {
        Bundle bundle = getArguments();
        userInfo = (FamilyMemberInfo) bundle.getSerializable("userInfo");
        if (null != userInfo) {
            RestRequestApi.getUserRightsList(getActivity(), userInfo.getMember_id(), this);
        } else {
            showToast(R.string.sorry_str);
            getActivity().onBackPressed();
        }
    }

    private void initView() {
        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(getActivity());
        smartRightsSceneRecycle.setLayoutManager(linearLayoutManager);
        sceneAdapter = new SceneRightsListAdapter(getActivity(), sceneList);
        sceneAdapter.setOnSceneItemClickListener(this);
        smartRightsSceneRecycle.setAdapter(sceneAdapter);

        MyLinearLayoutManager linearLayoutManager1 = new MyLinearLayoutManager(getActivity());
        smartRightsDeviceRecycle.setLayoutManager(linearLayoutManager1);
        deviceAdapter = new DeviceRightsListAdapter(getActivity(), deviceList);
        deviceAdapter.setOnDeviceItemClickListener(this);
        smartRightsDeviceRecycle.setAdapter(deviceAdapter);
    }

    @Override
    public void onBackPressed() {
        //showLoadingDialog(R.string.loading_str, false);
        RestRequestApi.addUserRights(getActivity(), userInfo.getMember_id(), sceneAdapter.getSceneList(), deviceAdapter.getDeviceList(), this);
        getActivity().onBackPressed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GjjEventBus.getInstance().unregister(mRightsManageEvent);
        try {
            if (null != selectDialog) {
                selectDialog.dismiss();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onRightBtnClick() {
        showSelectDialog();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_right_tv:
                // TODO: 2016/9/15
                showSelectDialog();
                break;
            default:
                break;
        }
    }

    private void showSelectDialog() {
        if (null == selectDialog) {
            selectDialog = new RightsTypeSelectDialog(getActivity());
        }
        selectDialog.setOnRightsTypeClickListener(RightsListFragment.this);
        selectDialog.setCancelable(true);
        selectDialog.setCanceledOnTouchOutside(true);
        selectDialog.show();
    }

    /*private void showOperateDialog(){
        addDialog = new UserAddDialog(getActivity(), 0, -1, new UserInfo());
        addDialog.setOnEnsureClickListener(RightsListFragment.this);
        addDialog.setCancelable(true);
        addDialog.setCanceledOnTouchOutside(true);
        addDialog.show();
    }*/

    @Override
    public void onDeviceItemClick(int position, DeviceInfo deviceInfo) {
        showToast("点击设备");
    }

    @Override
    public void onSceneItemClick(int position, SceneInfo sceneInfo) {
        showToast("点击场景");
    }

    @Override
    public void onSceneClick() {
        showToast("添加场景");
        /*Intent switchIntent = new Intent(getActivity(), SceneRightsSelectFragment.class);
        if (null != sceneList) {
            switchIntent.putExtra("selected", (Serializable) sceneList);
        }
        getActivity().startActivityForResult(switchIntent, SceneRightsSelectFragment.REQUEST_CODE_SCENE_SELECT);*/
        Bundle bundle = new Bundle();
        bundle.putSerializable("selected", (Serializable) sceneList);
        PageSwitcher.switchToTopNavPage(getActivity(), SceneRightsSelectFragment.class, bundle, "", "场景列表", "确定", -1, "");
    }

    @Override
    public void onDeviceClick() {
        showToast("添加设备");
        /*Intent switchIntent = new Intent(getActivity(), SceneDeviceSelectFragment.class);
        if (null != deviceList) {
            switchIntent.putExtra("selected", (Serializable) deviceList);
        }
        getActivity().startActivityForResult(switchIntent, SceneDeviceSelectFragment.REQUEST_CODE_DEVICE_SELECT);*/

        Bundle bundle = new Bundle();
        bundle.putSerializable("selected", (Serializable) deviceList);
        PageSwitcher.switchToTopNavPage(getActivity(), DeviceRightsSelectFragment.class, bundle, "", "设备列表", "确定", -1, "");
    }

    /**
     * 请求回调
     */
    public Object mRightsManageEvent = new Object() {
        public void onEventMainThread(EventOfResultDeviceRights data) {
            List<DeviceInfo> selected = (List<DeviceInfo>) data.deviceList;
            if (null == selected) {
                selected = new ArrayList<DeviceInfo>();
            }
            Log.d("laixj", "已选择的设备权限-->" + selected.toString());
            deviceList = selected;
            deviceAdapter.setDeviceList(deviceList);
            judgeDevice();
        }

        public void onEventMainThread(EventOfResultSceneRights data) {
            List<SceneInfo> selected = (List<SceneInfo>) data.sceneList;
            if (null == selected) {
                selected = new ArrayList<SceneInfo>();
            }
            Log.d("laixj", "已选择的权限权限-->" + selected.toString());
            sceneList = selected;
            sceneAdapter.setSceneList(sceneList);
            judgeScene();
        }
    };

    private void judgeDevice() {
        if (null == deviceList || (null != deviceList && deviceList.size() == 0)) {
            smartRightsDeviceRecycle.setVisibility(View.GONE);
            rightsDeviceEmptyTv.setVisibility(View.VISIBLE);
        }else{
            rightsDeviceEmptyTv.setVisibility(View.GONE);
            smartRightsDeviceRecycle.setVisibility(View.VISIBLE);
        }
        deviceAdapter.notifyDataSetChanged();
    }

    private void judgeScene() {
        if (null == sceneList || (null != sceneList && sceneList.size() == 0)) {
            smartRightsSceneRecycle.setVisibility(View.GONE);
            rightsSceneEmptyTv.setVisibility(View.VISIBLE);
        }else{
            rightsSceneEmptyTv.setVisibility(View.GONE);
            smartRightsSceneRecycle.setVisibility(View.VISIBLE);
        }
        sceneAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {
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
        if (url.startsWith(ApiHost.GET_USER_RIGHTS_LIST.substring(0, ApiHost.GET_USER_RIGHTS_LIST.lastIndexOf("=")))) {
            if (response != null) {
                GetRightsListResponse param = (GetRightsListResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            sceneList = param.getResult().getScenes();
                            deviceList = param.getResult().getDevices();
                            if (null == sceneList) {
                                sceneList = new ArrayList<SceneInfo>();
                            }
                            if (null == deviceList) {
                                deviceList = new ArrayList<DeviceInfo>();
                            }
                            sceneAdapter.setSceneList(sceneList);
                            deviceAdapter.setDeviceList(deviceList);
                            judgeDevice();
                            judgeScene();
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
        } else if (url.equals(ApiHost.ADD_USER_RIGHTS_URL)) {
            if (response != null) {
                AddUserRightsResponse param = (AddUserRightsResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        SmartHomeApp.showToast("保存成功");
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
        if (getActivity().isFinishing()) {
            return;
        }
        dismissLoadingDialog();
    }
}
