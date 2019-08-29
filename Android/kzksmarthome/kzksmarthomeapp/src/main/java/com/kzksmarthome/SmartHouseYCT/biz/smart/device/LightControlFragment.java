package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultDeviceCtrl;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.util.DeviceStatusEnums;
import com.kzksmarthome.SmartHouseYCT.util.DeviceTypeEnums;
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
 * @Title: LightControlFragment
 * @Description: 灯光控制界面
 * @date 2016/9/11 8:08
 */
public class LightControlFragment extends BaseRequestFragment implements RequestCallback {


    @BindView(R.id.light_ctr_iv_1)
    ImageView lightCtrIv1;
    @BindView(R.id.light_ctr_switch_iv_1)
    ImageView lightCtrSwitchIv1;
    /**
     * 设备信息
     */
    private DeviceInfo deviceInfo;
    private int flag1 = 0;
    /**
     * 网关
     */
    private String iotMac;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_layout_light_control, container, false);
        ButterKnife.bind(this, mRootView);
        Bundle bundle = getArguments();
        deviceInfo = bundle.getParcelable("deviceInfo");
        if (null != deviceInfo) {
            flag1 = deviceInfo.getDevice_state1();
            if (flag1 == DeviceStatusEnums.ON.getCode()) {
                lightCtrIv1.setImageResource(R.drawable.light_ctr_on);
                lightCtrSwitchIv1.setImageResource(R.drawable.light_ctr_power_on);
            } else {
                lightCtrIv1.setImageResource(R.drawable.light_ctr_off);
                lightCtrSwitchIv1.setImageResource(R.drawable.light_ctr_power_off);
            }
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

    @OnClick(R.id.light_ctr_switch_iv_1)
    public void onClick(View view) {
        String way = deviceInfo.getOther_status();
        if (flag1 == DeviceStatusEnums.ON.getCode()) {
            if (way == null) {
                RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x02);
            } else {
                RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), Byte.valueOf(way), (byte) 0x01, (byte) 0x02, (byte) 0x00);
            }
            flag1 = DeviceStatusEnums.OFF.getCode();
            lightCtrSwitchIv1.setImageResource(R.drawable.light_ctr_power_on);
            lightCtrIv1.setImageResource(R.drawable.light_ctr_on);
        } else {
            if (way == null) {
                RestRequestApi.contorOneWay(iotMac, deviceInfo.getMac_address(), (byte) 0x01);
            } else {
                RestRequestApi.contorMoreWay(iotMac, deviceInfo.getMac_address(), Byte.valueOf(way), (byte) 0x01, (byte) 0x01, (byte) 0x00);
            }
            flag1 = DeviceStatusEnums.ON.getCode();
            lightCtrSwitchIv1.setImageResource(R.drawable.light_ctr_power_off);
            lightCtrIv1.setImageResource(R.drawable.light_ctr_off);
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
                        if (deviceState.dstAddr == null) {
                            return;
                        }
                        byte[] od = deviceState.deviceOD;
                        if (od[0] == 0x0f && od[1] == (byte) 0xe6) {//不处理红外转发设备
                            return;
                        }
                        int deviceType = DeviceTools.getDeviceType(deviceInfo);
                        if (DeviceTypeEnums.TABLELAMP.getCode() == deviceType || DeviceTypeEnums.DROPLIGHT.getCode() == deviceType) {
                            String way = deviceInfo.getOther_status();
                            if (TextUtils.isEmpty(way)) {
                                return;
                            }
                            switch (Byte.valueOf(way)) {
                                case 1:
                                    updateView(deviceState.result_data_01,1);
                                    break;
                                case 2:
                                    updateView(deviceState.result_data_02,2);
                                    break;
                                case 3:
                                    updateView(deviceState.result_data_03,3);
                                    break;
                            }
                        } else {
                            updateView(deviceState.result_data_01,0);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 更新UI
     *
     * @param state
     */

    private void updateView(int state,int way) {
        switch (state) {
            case 0x01:
                flag1 = DeviceStatusEnums.ON.getCode();
                lightCtrSwitchIv1.setImageResource(R.drawable.light_ctr_power_on);
                lightCtrIv1.setImageResource(R.drawable.light_ctr_on);
                break;
            case 0x02:
                flag1 = DeviceStatusEnums.OFF.getCode();
                lightCtrSwitchIv1.setImageResource(R.drawable.light_ctr_power_off);
                lightCtrIv1.setImageResource(R.drawable.light_ctr_off);
                break;
        }
        if(way == 2){
            deviceInfo.setDevice_state2(flag1);
        }else if(way == 3){
            deviceInfo.setDevice_state3(flag1);
        }else{
            deviceInfo.setDevice_state1(flag1);
        }
    }

    @Override
    public void onBackPressed() {
        EventOfResultDeviceCtrl event = new EventOfResultDeviceCtrl();
        event.deviceInfo = deviceInfo;
        GjjEventBus.getInstance().post(event);
        getActivity().onBackPressed();
    }
}
