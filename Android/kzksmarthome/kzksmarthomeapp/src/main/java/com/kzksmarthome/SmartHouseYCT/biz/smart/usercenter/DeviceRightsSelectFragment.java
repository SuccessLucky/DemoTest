package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultDeviceRights;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetAllDeviceListResponse;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.module.net.ApiHost;
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
 * @Title: SceneDeviceSelectFragment
 * @Description: 设备权限列表
 * @date 2016/9/16 14:43
 */
public class DeviceRightsSelectFragment extends BaseRequestFragment implements DeviceRightsSelectListAdapter.OnDeviceItemClick,RequestCallback {

    public static final int REQUEST_CODE_DEVICE_SELECT = 104;

    @BindView(R.id.device_rights_recycle_list)
    RecyclerView deviceRightsRecycleList;

    private List<DeviceInfo> deviceList = new ArrayList<DeviceInfo>();
    private DeviceRightsSelectListAdapter adapter = null;
    /**
     * 已选中的设备
     */
    List<DeviceInfo> originSelected = new ArrayList<DeviceInfo>();
    private Map<DeviceInfo, DeviceInfo> originSelectedMap = new HashMap<DeviceInfo, DeviceInfo>();

    @Override
    public void onRightBtnClick() {
        List<DeviceInfo> selected = new ArrayList<DeviceInfo>();
        for (DeviceInfo deviceInfo : adapter.getDeviceList()) {
            if(deviceInfo.isSelected()){
                selected.add(deviceInfo);
            }
        }
        EventOfResultDeviceRights event = new EventOfResultDeviceRights();
        event.deviceList = selected;
        GjjEventBus.getInstance().post(event);
        super.onBackPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_device_rights_select_layout, container, false);
        ButterKnife.bind(this, mRootView);
        RestRequestApi.getAllDeviceList(getActivity(), this);
        initData();
        initViews();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData() {
        Bundle bundle = getArguments();
        originSelected = (List<DeviceInfo>) bundle.getSerializable("selected");
        if (null != originSelected) {
            for (DeviceInfo device : originSelected) {
                originSelectedMap.put(device, device);
            }
        }
    }

    private void initSelected(){
        for(DeviceInfo device : deviceList){
            if(originSelectedMap.containsKey(device)){
                device.setSelected(true);
            }
        }
    }

    private void initViews() {
        deviceRightsRecycleList.setLayoutManager(new LinearLayoutManager(deviceRightsRecycleList.getContext()));
        adapter = new DeviceRightsSelectListAdapter(getActivity(), deviceList);
        //adapter.setOnDeviceItemClickListener(this);
        deviceRightsRecycleList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDeviceItemClick(int position, DeviceInfo deviceInfo) {

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
        if (url.startsWith(ApiHost.GET_ALL_DEVICE_URL)) {
            if (response != null) {
                GetAllDeviceListResponse param = (GetAllDeviceListResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            deviceList = param.getResult();
                            adapter.setDeviceList(deviceList);
                            initSelected();
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
