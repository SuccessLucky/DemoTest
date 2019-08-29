package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

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
import com.kzksmarthome.common.lib.tools.IOTConfig;
import com.kzksmarthome.common.lib.tools.Tools;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.user.UserInfo;
import com.squareup.okhttp.Request;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: SmartCurtainControlFragment
 * @Description: 智能窗帘控制界面
 * @date 2016/9/11 8:39
 */
public class SmartCurtainControlFragment extends BaseRequestFragment implements RequestCallback {


    @BindView(R.id.smart_curtain_ctr_seekbar)
    SeekBar smartCurtainCtrSeekbar;
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
        mRootView = inflater.inflate(R.layout.smart_layout_smart_curtain_control, container, false);
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
        try {
            if(Tools.hexStr2Byte(deviceInfo.getCategory()) == 0x11){
                smartCurtainCtrSeekbar.setMax(10);
                smartCurtainCtrIv.setImageResource(R.drawable.ch_0);
            }else {
                smartCurtainCtrSeekbar.setMax(16);
                smartCurtainCtrIv.setImageResource(R.drawable.cl_0);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        smartCurtainCtrSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    byte seek = (byte) seekBar.getProgress();//刻度
                     byte[] orde = {(byte) 0xff,0x00,0x00,0x3d,seek,0x25};
                    if(Tools.hexStr2Byte(deviceInfo.getCategory()) == 0x11){
                        orde[1] = 0x01;
                        RestRequestApi.sendForwardOrder(iotMac, deviceInfo.getMac_address(), orde);
                    }else {
                        orde[1] = 0x00;
                        RestRequestApi.sendForwardOrder(iotMac, deviceInfo.getMac_address(), orde);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

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
        try {
            switch (view.getId()) {
                case R.id.smart_curtain_ctr_switch_iv://正转（开）
                    if(Tools.hexStr2Byte(deviceInfo.getCategory()) == 0x11){
                        RestRequestApi.sendForwardOrder(iotMac, deviceInfo.getMac_address(), IOTConfig.WINDOW_OPENER_ZZ);
                    }else {
                        RestRequestApi.sendForwardOrder(iotMac, deviceInfo.getMac_address(), IOTConfig.ELECTRIC_CURTAIN_FZ);
                    }
                    break;
                case R.id.smart_curtain_ctr_pause_iv://停止
                    if(Tools.hexStr2Byte(deviceInfo.getCategory()) == 0x11){
                        RestRequestApi.sendForwardOrder(iotMac, deviceInfo.getMac_address(), IOTConfig.WINDOW_OPENER_STOP);
                    }else {
                        RestRequestApi.sendForwardOrder(iotMac, deviceInfo.getMac_address(), IOTConfig.ELECTRIC_CURTAIN_STOP);
                    }
                    break;
                case R.id.smart_curtain_ctr_switch_close://反转（关）
                    if(Tools.hexStr2Byte(deviceInfo.getCategory()) == 0x11){
                        RestRequestApi.sendForwardOrder(iotMac, deviceInfo.getMac_address(), IOTConfig.WINDOW_OPENER_FZ);
                    }else {
                        RestRequestApi.sendForwardOrder(iotMac, deviceInfo.getMac_address(), IOTConfig.ELECTRIC_CURTAIN_ZZ);
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
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
                        deviceInfo.setDevice_state1(deviceState.sindex_length);
                        switch (deviceState.sindex_length) {
                            case 0x4f:// 正转
                                showToast("开");
                                break;
                            case 0x43:// 反转
                                showToast("关");
                                break;
                            case 0x53:// 停止
                                showToast("停");
                                break;
                            default:
                                smartCurtainCtrSeekbar.setProgress(deviceState.sindex_length);
                                int progress = deviceState.sindex_length;

                                if(Tools.hexStr2Byte(deviceInfo.getCategory()) == 0x11){
                                    if(progress > 9) {
                                        progress = 9;
                                    }
                                    smartCurtainCtrIv.setImageResource(Util.getResuseId(getActivity(), "ch_" + progress, "drawable"));
                                }else {
                                    if(progress > 15) {
                                        progress = 15;
                                    }
                                    smartCurtainCtrIv.setImageResource(Util.getResuseId(getActivity(), "cl_" + progress, "drawable"));
                                }
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
