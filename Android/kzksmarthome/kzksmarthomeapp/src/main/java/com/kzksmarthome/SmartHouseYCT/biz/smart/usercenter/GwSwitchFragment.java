package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.content.Intent;
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
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultSelectGw;
import com.kzksmarthome.SmartHouseYCT.biz.login.LoginActivity;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.GatewayInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.DeleteGwResponse;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.task.ForegroundTaskExecutor;
import com.kzksmarthome.common.lib.tcp.TCPMgr;
import com.kzksmarthome.common.module.net.ApiHost;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * @author laixj
 * @version V1.0
 * @Title: GwSwitchFragment
 * @Description: 网关选择界面
 * @date 2016/10/10 23:36
 */
public class GwSwitchFragment extends BaseRequestFragment implements GwListAdapter.OnGwItemClick,
        GwOperationSelectDialog.OnOperationSelectItemClick, GwDelConfirmDialog.OnEnsureClick, RequestCallback {

    public static final int REQUEST_CODE_GW_SWITCH = 101;

    @BindView(R.id.smart_gw_switch_recycle)
    RecyclerView smartGwSwitchRecycle;
    private GwOperationSelectDialog selectDialog = null;
    private GwDelConfirmDialog delConfirmDialog = null;

    private List<GatewayInfo> gwList = new ArrayList<GatewayInfo>();

    private GwListAdapter adapter = null;

    /**
     * 选中的网关
     */
    private GatewayInfo selectedGw = null;
    private int delPosition = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_gw_switch_layout, container, false);
        ButterKnife.bind(this, mRootView);
        initData();
        initView();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData() {
        Bundle bundle = getArguments();
        Object selected = bundle.getSerializable("selectedgw");
        Object object = bundle.getSerializable("gwlist");
        if (null == object) {
            gwList = new ArrayList<GatewayInfo>();
        } else {
            gwList = (List<GatewayInfo>) object;
        }
        if (null != selected) {
            selectedGw = (GatewayInfo) selected;
        } else {
            for (GatewayInfo gw : gwList) {
                if(null != gw.getMac_address() && gw.getMac_address().equals(SmartHomeAppLib.getUserMgr().getUser().gateway)){
                    selectedGw = gw;
                }
            }
        }
        GatewayInfo gatewayInfo = new GatewayInfo();
        gatewayInfo.setViewType(1);
        gwList.add(gatewayInfo);
    }

    private void initView() {
        smartGwSwitchRecycle.setLayoutManager(new LinearLayoutManager(smartGwSwitchRecycle.getContext()));
        adapter = new GwListAdapter(getActivity(), gwList, selectedGw);
        adapter.setOnGwItemClickListener(this);
        smartGwSwitchRecycle.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRightBtnClick() {
        initIOT();
        EventOfResultSelectGw event = new EventOfResultSelectGw();
        event.gwInfo = selectedGw;
        GjjEventBus.getInstance().post(event);
        getActivity().onBackPressed();
    }

    /**
     * 选中网关后重新连接网关
     */
    private void initIOT() {
        SmartHomeAppLib.getInstance().getPreferences().edit().putString(SharePrefConstant.PREFS_KEY_IOT_WIFI_MAC, selectedGw.getWifi_mac_address()).commit();
        SmartHomeAppLib.getInstance().getPreferences().edit().putString(SharePrefConstant.PREFS_KEY_IOT_MAC, selectedGw.getMac_address()).commit();
        ForegroundTaskExecutor.executeTask(new Runnable() {
            @Override
            public void run() {
                if (null != selectedGw) {
                    //SmartHomeApp.getInstance().getIOTInfo(false);
                    SmartHomeApp.getInstance().getIOTInfoNew(false,5);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //getActivity().onBackPressed();
        if (null == selectedGw) {
            Log.d("laixj", "请选择网关");
            SmartHomeApp.showToast(R.string.please_select_gw);
        } else {
            initIOT();
            EventOfResultSelectGw event = new EventOfResultSelectGw();
            event.gwInfo = selectedGw;
            GjjEventBus.getInstance().post(event);
            getActivity().onBackPressed();
        }
    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_right_tv:
                // TODO: 2016/10/10
                EventOfResultSelectGw event = new EventOfResultSelectGw();
                event.gwInfo = selectedGw;
                initIOT();
                GjjEventBus.getInstance().post(event);
                getActivity().finish();
                break;
            case R.id.top_back_tv:
                // TODO: 2016/10/10
                if (null == selectedGw) {
                    Intent intent = new Intent(getActivity(), SettingIOTRadeyActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                initIOT();
                break;
            default:
                break;
        }
    }*/

    @Override
    public void onGwItemClick(int position, GatewayInfo gwInfo) {
        selectedGw = gwInfo;
    }

    @Override
    public void onGwItemLongClick(int position, GatewayInfo gwInfo) {
        com.kzksmarthome.common.module.user.UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        if (null == userInfo) {
            return;
        }
        //if(!gwInfo.getMac_address().equals(userInfo.gateway)){
            showOperatorSelectDialog(position, gwInfo);
       // }
    }

    private void showOperatorSelectDialog(int position, GatewayInfo gwInfo){
        if(null == selectDialog){
            selectDialog = new GwOperationSelectDialog(getActivity(), position, gwInfo);
        }else{
            selectDialog.setPosition(position);
            selectDialog.setGwInfo(gwInfo);
        }
        selectDialog.setOnOperationSelectClickListener(this);
        selectDialog.setCancelable(true);
        selectDialog.setCanceledOnTouchOutside(true);
        selectDialog.show();
    }

    @Override
    public void onDeleteClick(int position, GatewayInfo gwInfo) {
        showDelConfirmDialog(position, gwInfo);
    }

    @Override
    public void onEditClick(int position, GatewayInfo gwInfo) {

    }

    private void showDelConfirmDialog(int position, GatewayInfo gwInfo){
        if(null == delConfirmDialog){
            delConfirmDialog = new GwDelConfirmDialog(getActivity(), position, gwInfo);
        }else{
            delConfirmDialog.setPosition(position);
            delConfirmDialog.setGwInfo(gwInfo);
        }
        delConfirmDialog.setOnEnsureClickListener(this);
        delConfirmDialog.setCancelable(true);
        delConfirmDialog.setCanceledOnTouchOutside(true);
        delConfirmDialog.show();
    }

    @Override
    public void onEnsureClick(int position, GatewayInfo gwInfo) {
        delPosition = position;
        if(null == gwInfo){
            return;
        }
        showLoadingDialog(R.string.loading_str, false);
        RestRequestApi.deleteGW(getActivity(), gwInfo.getMac_address(), this);
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
        if (url.equals(ApiHost.DELETE_GW_URL)) {
            dismissLoadingDialog();
            if (response != null) {
                DeleteGwResponse param = (DeleteGwResponse) response.body;
                if (param != null) {
                    if (param.isSuccess()) {
                        SmartHomeApp.showToast("网关删除成功");
                        gwList.remove(delPosition);
                        adapter.notifyDataSetChanged();
                        if(gwList.size() == 1){
                           logout();
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
     * 退出登录
     */
    private void logout(){
        SmartHomeAppLib.getUserMgr().logOut();
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
        SmartHomeAppLib.getInstance().getPreferences().edit()
                .putString(SharePrefConstant.PREFS_KEY_IOT_MAC, null)
                .commit();
        SmartHomeAppLib.getInstance().getPreferences().edit().putString(SharePrefConstant.PREFS_KEY_IOT_WIFI_MAC,null).commit();
        ApiHost.NETWORK_ISREMOTE = false;
        //退出登录断开网络连接
        TCPMgr.getInstance().closeConnect();
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        if (getActivity().isFinishing()) {
            return;
        }
        dismissLoadingDialog();
    }
}
