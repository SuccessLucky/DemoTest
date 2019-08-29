package com.kzksmarthome.SmartHouseYCT.biz.smart.scene;

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
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultDeviceScene;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetAllDeviceListResponse1;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.module.log.L;
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
public class SceneDeviceSelectFragment extends BaseRequestFragment implements SceneDeviceSelectListAdapter.OnDeviceItemClick,RequestCallback {

    @BindView(R.id.device_rights_recycle_list)
    RecyclerView deviceRightsRecycleList;

    private List<SceneDetailInfo> deviceList = new ArrayList<SceneDetailInfo>();
    private SceneDeviceSelectListAdapter adapter = null;
    /**
     * 是否是联动设备
     */
    private boolean isLD = false;
    /**
     * 已选中的设备
     */
    List<SceneDetailInfo> originSelected = new ArrayList<SceneDetailInfo>();
    private Map<SceneDetailInfo, SceneDetailInfo> originSelectedMap = new HashMap<SceneDetailInfo, SceneDetailInfo>();

    @Override
    public void onRightBtnClick() {
        List<SceneDetailInfo> selected = new ArrayList<SceneDetailInfo>();
        for (SceneDetailInfo deviceInfo : adapter.getDeviceList()) {
            if(deviceInfo.isSelected()){
                selected.add(deviceInfo);
            }
        }
        if(selected.size() > 1 && isLD){
            showToast(R.string.no_more_sceon_device_str);
            return;
        }
        EventOfResultDeviceScene event = new EventOfResultDeviceScene();
        event.deviceList = selected;
        event.isLD = isLD;
        GjjEventBus.getInstance().post(event);
        super.onBackPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_device_rights_select_layout, container, false);
        ButterKnife.bind(this, mRootView);
        RestRequestApi.getAllDeviceList1(getActivity(), this);
        initData();
        initViews();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData() {
        Bundle bundle = getArguments();
        isLD = bundle.getBoolean("isLD");
        originSelected = (List<SceneDetailInfo>) bundle.getSerializable("selected");
        if (null != originSelected) {
            for (SceneDetailInfo device : originSelected) {
                originSelectedMap.put(device, device);
            }
        }
    }

    private void initSelected(){
        try {
            if(deviceList == null){
                return;
            }
            for (SceneDetailInfo device : deviceList) {
                if (originSelectedMap.containsKey(device)) {
                    SceneDetailInfo origin = originSelectedMap.get(device);
                    device.setScene_detail_id(origin.getScene_detail_id());
                    device.setSelected(true);
                }
                //device.setDevice_name(device.getName());
                device.setImage(device.getImage());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initViews() {
        deviceRightsRecycleList.setLayoutManager(new LinearLayoutManager(deviceRightsRecycleList.getContext()));
        adapter = new SceneDeviceSelectListAdapter(getActivity(), deviceList);
        //adapter.setOnDeviceItemClickListener(this);
    deviceRightsRecycleList.setAdapter(adapter);
}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDeviceItemClick(int position, SceneDetailInfo deviceInfo) {

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
        if (url.startsWith(ApiHost.GET_ALL_DEVICE_URL)) {
            if (response != null) {
                GetAllDeviceListResponse1 param = (GetAllDeviceListResponse1) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        if (null != param.getResult()) {
                            deviceList = param.getResult();
                            sortList();
                            adapter.setDeviceList(deviceList);
                            adapter.notifyDataSetChanged();
                            initSelected();
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
     * 排序
     */
    public void sortList(){
        ArrayList<SceneDetailInfo> senceDeviceList = new ArrayList<SceneDetailInfo>();
        ArrayList<SceneDetailInfo> nomalDeviceList = new ArrayList<SceneDetailInfo>();
        for (SceneDetailInfo device : deviceList) {
            if (device.getDevice_OD().equals("0F BE")) {
                if(!device.getDevice_type().equals("02")) {//去掉锁
                    senceDeviceList.add(device);
                }
            }else if(device.getDevice_OD().equals("0F AA")){
                String type = device.getDevice_type();
                if(type.equals("81")||type.equals("82")||type.equals("83")){
                    senceDeviceList.add(device);
                }else if(!type.equals("8A")){
                    nomalDeviceList.add(device);
                }
            }else{
                nomalDeviceList.add(device);
            }
        }
        deviceList.clear();
        if(isLD) {
            deviceList.addAll(senceDeviceList);
        }else {
            deviceList.addAll(nomalDeviceList);
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
