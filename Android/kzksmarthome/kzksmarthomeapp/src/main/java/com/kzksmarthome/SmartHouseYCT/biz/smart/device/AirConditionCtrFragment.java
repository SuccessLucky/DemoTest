package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultDeviceCtrl;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.util.DeviceStatusEnums;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;
import com.kzksmarthome.common.lib.tools.IOTConfig;
import com.kzksmarthome.common.module.user.UserInfo;
import com.squareup.okhttp.Request;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: AirConditionCtrFragment
 * @Description: 空调控制界面
 * @date 2016/9/11 16:11
 */
public class AirConditionCtrFragment extends BaseRequestFragment implements RequestCallback,
        AirCtrModeSelectDialog.OnModeItemClick, AirCtrWindSelectDialog.OnWindItemClick {

    @BindView(R.id.air_ctr_mode_iv)
    ImageView airCtrModeIv;
    @BindView(R.id.air_ctr_mode_tv)
    TextView airCtrModeTv;
    @BindView(R.id.air_ctr_mode_rl)
    RelativeLayout airCtrModeRl;
    @BindView(R.id.air_ctr_wind_speed_iv)
    ImageView airCtrWindSpeedIv;
    @BindView(R.id.air_ctr_wind_speed_tv)
    TextView airCtrWindSpeedTv;
    @BindView(R.id.air_ctr_wind_speed_rl)
    RelativeLayout airCtrWindSpeedRl;
    @BindView(R.id.air_ctr_leftright_iv)
    ImageView airCtrLeftrightIv;
    @BindView(R.id.air_ctr_leftright_tv)
    TextView airCtrLeftrightTv;
    @BindView(R.id.air_ctr_leftright_rl)
    RelativeLayout airCtrLeftrightRl;
    @BindView(R.id.air_ctr_updown_iv)
    ImageView airCtrUpdownIv;
    @BindView(R.id.air_ctr_updown_tv)
    TextView airCtrUpdownTv;
    @BindView(R.id.air_ctr_updown_rl)
    RelativeLayout airCtrUpdownRl;
    @BindView(R.id.air_ctr_bottom_rl)
    RelativeLayout airCtrBottomRl;
    @BindView(R.id.air_ctr_switch_iv)
    ImageView airCtrSwitchIv;
    @BindView(R.id.air_ctr_temp_add_iv)
    ImageView airCtrTempAddIv;
    @BindView(R.id.air_ctr_temp_tv)
    TextView airCtrTempTv;
    @BindView(R.id.air_ctr_temp_subtract_iv)
    ImageView airCtrTempSubtractIv;
    @BindView(R.id.air_ctr_off_tv)
    TextView airCtrOffTv;
    @BindView(R.id.air_ctr_top_rl)
    RelativeLayout airCtrTopRl;
    @BindView(R.id.hint_tv)
    TextView hintTv;
    /**
     * 网关
     */
    private String iotMac;
    /**
     * 设备信息
     */
    private DeviceInfo deviceInfo;
    private int flag = 0;

    private AirCtrWindSelectDialog windSelectDialog;

    private AirCtrModeSelectDialog modeSelectDialog;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_layout_air_control, container, false);
        ButterKnife.bind(this, mRootView);
        Bundle bundle = getArguments();
        deviceInfo = bundle.getParcelable("deviceInfo");
        if (null != deviceInfo) {
            flag = deviceInfo.getDevice_state1();
            UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
            if (userInfo != null) {
                iotMac = userInfo.gateway;
            }
            try {
                RestRequestApi.setAirConditionerType(iotMac, deviceInfo.getMac_address(), Integer.valueOf(deviceInfo.getOther_status()));//发送空调型号适配
                MainTaskExecutor.scheduleTaskOnUiThread(300, new Runnable() {
                    @Override
                    public void run() {
                        RestRequestApi.setAirConditionerOrder(iotMac, deviceInfo.getMac_address(), new byte[]{IOTConfig.KT_ORDER_OPEN_CLOSE, (byte) 0xff});
                        flag = DeviceStatusEnums.ON.getCode();
                        MainTaskExecutor.scheduleTaskOnUiThread(300, new Runnable() {
                            @Override
                            public void run() {
                                RestRequestApi.setAirConditionerOrder(iotMac, deviceInfo.getMac_address(), new byte[]{IOTConfig.KT_ORDER_WD, (byte) 26});
                            }
                        });
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
    }

    @OnClick({R.id.air_ctr_mode_iv, R.id.air_ctr_wind_speed_iv, R.id.air_ctr_leftright_iv,
            R.id.air_ctr_updown_iv, R.id.air_ctr_switch_iv, R.id.air_ctr_temp_add_iv, R.id.air_ctr_temp_subtract_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.air_ctr_mode_iv:
                modeSelectDialog = new AirCtrModeSelectDialog(getActivity());
                modeSelectDialog.setOnModeItemClickListener(this);
                modeSelectDialog.setCancelable(true);
                modeSelectDialog.setCanceledOnTouchOutside(true);
                modeSelectDialog.show();
                break;
            case R.id.air_ctr_wind_speed_iv:
                windSelectDialog = new AirCtrWindSelectDialog(getActivity());
                windSelectDialog.setOnWindItemClickListener(this);
                windSelectDialog.setCancelable(true);
                windSelectDialog.setCanceledOnTouchOutside(true);
                windSelectDialog.show();
                break;
            case R.id.air_ctr_leftright_iv:
                RestRequestApi.setAirConditionerOrder(iotMac, deviceInfo.getMac_address(), new byte[]{IOTConfig.KT_ORDER_WIND_WAY, (byte) 0x00});
                break;
            case R.id.air_ctr_updown_iv:
                RestRequestApi.setAirConditionerOrder(iotMac, deviceInfo.getMac_address(), new byte[]{IOTConfig.KT_ORDER_WIND_WAY, (byte) 0x01});
                break;
            case R.id.air_ctr_switch_iv:
                if (flag != DeviceStatusEnums.ON.getCode()) {
                    flag = DeviceStatusEnums.ON.getCode();
                    RestRequestApi.setAirConditionerOrder(iotMac, deviceInfo.getMac_address(), new byte[]{IOTConfig.KT_ORDER_OPEN_CLOSE, (byte) 0xff});
                    deviceInfo.setDevice_state1(DeviceStatusEnums.ON.getCode());
                    hintTv.setVisibility(View.GONE);
                } else {
                    flag = DeviceStatusEnums.OFF.getCode();
                    RestRequestApi.setAirConditionerOrder(iotMac, deviceInfo.getMac_address(), new byte[]{IOTConfig.KT_ORDER_OPEN_CLOSE, 0x00});
                    deviceInfo.setDevice_state1(DeviceStatusEnums.OFF.getCode());
                    hintTv.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.air_ctr_temp_add_iv:
                setWd(true);
                break;
            case R.id.air_ctr_temp_subtract_iv:
                setWd(false);
                break;
        }
    }

    /**
     * 设置空调温度
     *
     * @param flag ture：温度加、false:温度减
     */
    private void setWd(boolean flag) {
        String wd = airCtrTempTv.getText().toString().trim().replace("°", "");
        if (!TextUtils.isEmpty(wd)) {
            int wd_int = Integer.valueOf(wd);
            if (flag) {
                if (wd_int < 32) {
                    wd_int++;
                } else {
                    return;
                }
            } else {
                if (wd_int > 16) {
                    wd_int--;
                } else {
                    return;
                }
            }
            RestRequestApi.setAirConditionerOrder(iotMac, deviceInfo.getMac_address(), new byte[]{IOTConfig.KT_ORDER_WD, (byte) wd_int});
            airCtrTempTv.setText(Integer.toString(wd_int) + "°");
        }
    }

    @Override
    public void onModeItemClick(short mode) {
        RestRequestApi.setAirConditionerOrder(iotMac, deviceInfo.getMac_address(), new byte[]{IOTConfig.KT_ORDER_MODEL, (byte) mode});
    }

    @Override
    public void onWindItemClick(short wind) {
        RestRequestApi.setAirConditionerOrder(iotMac, deviceInfo.getMac_address(), new byte[]{IOTConfig.KT_ORDER_SPEED, (byte) wind});
    }

    @Override
    public void onBackPressed() {
        EventOfResultDeviceCtrl event = new EventOfResultDeviceCtrl();
        event.deviceInfo = deviceInfo;
        GjjEventBus.getInstance().post(event);
        getActivity().onBackPressed();
    }
}
