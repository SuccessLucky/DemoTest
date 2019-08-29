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
 * @author jack
 * @version V1.0
 * @Title: SmartCurtainControlFragment
 * @Description: 控制盒
 * @date 2016/9/11 8:39
 */
public class ControlBoxFragment extends BaseRequestFragment implements RequestCallback {

    @BindView(R.id.smart_curtain_ctr_switch_iv)
    ImageView smartCurtainCtrSwitchIv;
    @BindView(R.id.smart_curtain_ctr_pause_iv)
    ImageView smartCurtainCtrPauseIv;
    @BindView(R.id.smart_curtain_ctr_iv)
    ImageView smartCurtainCtrIv;
    @BindView(R.id.smart_curtain_ctr_switch_close)
    ImageView smartCurtainCtrSwitchClose;
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
        mRootView = inflater.inflate(R.layout.smart_layout_control_box, container, false);
        ButterKnife.bind(this, mRootView);

        Bundle bundle = getArguments();
        deviceInfo = bundle.getParcelable("deviceInfo");
        if (null != deviceInfo) {
            flag = deviceInfo.getDevice_state1();
        }
        UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        if (userInfo != null) {
            iotMac = userInfo.gateway;
        }
        GjjEventBus.getInstance().register(deviceEvent);
        RestRequestApi.getAll4010NodeInfo(iotMac);
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

    @OnClick({R.id.smart_curtain_ctr_switch_iv, R.id.smart_curtain_ctr_pause_iv,R.id.smart_curtain_ctr_switch_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.smart_curtain_ctr_switch_iv://上（开）2
                 RestRequestApi.contorOneWay(iotMac,deviceInfo.getMac_address(), (byte) 0x01);
                break;
            case R.id.smart_curtain_ctr_pause_iv://停止 4
                RestRequestApi.contorOneWay(iotMac,deviceInfo.getMac_address(), (byte) 0x04);
                break;
            case R.id.smart_curtain_ctr_switch_close://下（关）1
                RestRequestApi.contorOneWay(iotMac,deviceInfo.getMac_address(), (byte) 0x02);
                break;
        }
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
                    if (deviceState != null && deviceInfo.getMac_address().equals(Tools.byte2HexStr(deviceState.dstAddr))) {
                        deviceInfo.setDevice_state1(deviceState.result_data_01);
                        switch (deviceState.result_data_01) {
                            case 0x01:// 正转
                                showToast("开");
                                break;
                            case 0x02:// 反转
                                showToast("关");
                                break;
                            case 0x04:// 停止
                                showToast("停止");
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
