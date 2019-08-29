package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultDeviceCtrl;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.util.DeviceStatusEnums;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.event.EventOfTcpResult;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.tools.DeviceState;
import com.kzksmarthome.common.lib.tools.Tools;
import com.kzksmarthome.common.module.user.UserInfo;
import com.squareup.okhttp.Request;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: SmartCurtainControlFragment
 * @Description: 推拉窗控制界面
 * @date 2016/9/11 8:39
 */
public class PushWindowCtrFragment extends BaseRequestFragment implements RequestCallback {

    @BindView(R.id.push_window_ctr_iv)
    ImageView pushWindowCtrIv;
    @BindView(R.id.push_window_switch_iv)
    ImageView pushWindowSwitchIv;

    /**
     * 设备信息
     */
    private DeviceInfo deviceInfo;
    private int flag = 0;
    /**
     * 网关
     */
    private String iotMac;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_layout_push_window_control, container, false);
        ButterKnife.bind(this, mRootView);

        Bundle bundle = getArguments();
        deviceInfo = bundle.getParcelable("deviceInfo");
        if (null != deviceInfo) {
            flag = deviceInfo.getDevice_state1();
            if(flag == DeviceStatusEnums.ON.getCode()){
                pushWindowCtrIv.setImageResource(R.drawable.push_window_ctr_open);
            }else if(flag == DeviceStatusEnums.OFF.getCode()){
                pushWindowCtrIv.setImageResource(R.drawable.push_window_ctr_closed);
            }
        }
        UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        if(userInfo != null){
            iotMac = userInfo.gateway;
        }
        GjjEventBus.getInstance().register(deviceEvent);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {

    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {

    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GjjEventBus.getInstance().unregister(deviceEvent);
    }

    @OnClick({R.id.push_window_switch_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.push_window_switch_iv:
                //TODO
                if (flag == DeviceStatusEnums.OFF.getCode()) {
                    RestRequestApi.contorOneWay(iotMac,deviceInfo.getMac_address(), (byte) 0x01);
                    flag = DeviceStatusEnums.ON.getCode();
                    showToast("开窗户");
                } else if (flag == DeviceStatusEnums.ON.getCode()) {
                    RestRequestApi.contorOneWay(iotMac,deviceInfo.getMac_address(), (byte) 0x02);
                    flag = DeviceStatusEnums.OFF.getCode();
                    showToast("关窗户");
                }
                break;
        }
    }

    /**
     * 请求回调
     */
    public Object deviceEvent = new Object() {
        public void onEventMainThread(EventOfTcpResult eventOfTcpResult) {
            if(getActivity() == null){
                return;
            }
            try {
                if (eventOfTcpResult != null) {
                    DeviceState deviceState = eventOfTcpResult.deviceState;
                    if (deviceState != null && deviceInfo.getMac_address().equals(Tools.byte2HexStr(deviceState.dstAddr))) {
                        switch (deviceState.result_data_01) {
                            case 0x01://开
                                flag = DeviceStatusEnums.ON.getCode();
                                deviceInfo.setDevice_state1(flag);
                                break;
                            case 0x02:// 关
                                flag = DeviceStatusEnums.OFF.getCode();
                                deviceInfo.setDevice_state1(flag);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onBackPressed() {
        EventOfResultDeviceCtrl event = new EventOfResultDeviceCtrl();
        event.deviceInfo = deviceInfo;
        GjjEventBus.getInstance().post(event);
        getActivity().onBackPressed();
    }
}
