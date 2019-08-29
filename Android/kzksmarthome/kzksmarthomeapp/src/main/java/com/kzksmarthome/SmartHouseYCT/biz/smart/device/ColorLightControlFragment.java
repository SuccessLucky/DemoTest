package com.kzksmarthome.SmartHouseYCT.biz.smart.device;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultDeviceCtrl;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.util.DeviceStatusEnums;
import com.kzksmarthome.SmartHouseYCT.widget.ColorPicker;
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
 * @Title: ColorLightControlFragment
 * @Description: 多彩灯控制界面
 * @date 2016/9/11 8:08
 */
public class ColorLightControlFragment extends BaseRequestFragment implements RequestCallback, View.OnClickListener {

    @BindView(R.id.color_picker)
    ColorPicker colorPicker;
    @BindView(R.id.tv_mode_right_now)
    TextView tvModeRightNow;
    @BindView(R.id.tv_mode_gradually)
    TextView tvModeGradually;
    @BindView(R.id.tv_mode_delay)
    TextView tvModeDelay;
    @BindView(R.id.collor_light_ctr_seekbar)
    SeekBar collorLightCtrSeekbar;
    @BindView(R.id.collor_light_switch_ib)
    ImageButton collorLightSwitchIb;
    @BindView(R.id.color_light_ys_time)
    EditText colorLightYsTime;
    @BindView(R.id.tv_mode_qc_jm)
    TextView tvModeQcJm;
    @BindView(R.id.tv_qc_tb)
    TextView tvQcTb;
    @BindView(R.id.tv_hxd)
    TextView tvHxd;
    @BindView(R.id.tv_mode_dk)
    TextView tvModeDk;
    @BindView(R.id.tv_mode_qk)
    TextView tvModeQk;
    /**
     * 是否为呼吸灯
     */
    private boolean isHxD;

    /**
     * 设备信息
     */
    private DeviceInfo deviceInfo;
    private int flag1 = 0;
    /**
     * 网关
     */
    private String iotMac;
    private byte type = 0x01;
    private byte mRed;
    private byte mGreen;
    private byte mBlue;
    //是否开启
    private boolean mOpened = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_layout_color_light_ctr, container, false);
        ButterKnife.bind(this, mRootView);
        Bundle bundle = getArguments();
        deviceInfo = bundle.getParcelable("deviceInfo");
        colorPicker.setOnColorChangedListener(new ColorChangedListener());
        collorLightCtrSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    byte seek = (byte) seekBar.getProgress();
                    // TODO: 2017/1/1 设置多彩灯亮度
                    RestRequestApi.sendColorLightLight(iotMac, deviceInfo.getMac_address(), seek, (byte) 1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (null != deviceInfo) {
            flag1 = deviceInfo.getDevice_state1();
            if (flag1 == DeviceStatusEnums.ON.getCode()) {
                mOpened = true;
                /*lightCtrIv1.setImageResource(R.drawable.light_ctr_on);
                lightCtrSwitchIv1.setImageResource(R.drawable.light_ctr_power_on);*/
            } else {
                mOpened = false;
				/*lightCtrIv1.setImageResource(R.drawable.light_ctr_off);
				lightCtrSwitchIv1.setImageResource(R.drawable.light_ctr_power_off);*/
            }
            checkStatus();
        }
        UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        if (userInfo != null) {
            iotMac = userInfo.gateway;
        }
        GjjEventBus.getInstance().register(deviceEvent);

        //RestRequestApi.getAll4010NodeInfo(iotMac);
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

    private void updateView(int state, int way) {
        switch (state) {
            case 0x01:
                flag1 = DeviceStatusEnums.ON.getCode();
                //lightCtrSwitchIv1.setImageResource(R.drawable.light_ctr_power_on);
                //lightCtrIv1.setImageResource(R.drawable.light_ctr_on);
                break;
            case 0x02:
                flag1 = DeviceStatusEnums.OFF.getCode();
                //lightCtrSwitchIv1.setImageResource(R.drawable.light_ctr_power_off);
                //lightCtrIv1.setImageResource(R.drawable.light_ctr_off);
                break;
        }
        if (way == 2) {
            deviceInfo.setDevice_state2(flag1);
        } else if (way == 3) {
            deviceInfo.setDevice_state3(flag1);
        } else {
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

    @OnClick({R.id.collor_light_switch_ib, R.id.tv_mode_right_now, R.id.tv_mode_gradually, R.id.tv_mode_delay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.collor_light_switch_ib:
                if (mOpened) {
                    mOpened = false;
                    //多彩灯关
                    RestRequestApi.sendColorLightLight(iotMac, deviceInfo.getMac_address(), (byte) 0x00, (byte) 2);
                } else {
                    mOpened = true;
                    //多彩灯开
                    RestRequestApi.sendColorLightLight(iotMac, deviceInfo.getMac_address(), (byte) 240, (byte) 1);
                }
                isHxD = false;
                type = 0x01;
                checkRightModeNow();
                checkQCDefault();
                checkRightNow();
                checkStatus();
                break;
            case R.id.tv_mode_right_now:
                checkRightNow();
                //设置多彩灯模式 --直接
                type = 0x01;
                colorLightYsTime.setVisibility(View.INVISIBLE);
                colorLightYsTime.setText("");
                break;
            case R.id.tv_mode_gradually:
                checkGradually();
                //设置多彩灯模式--渐渐
                type = 0x02;
                colorLightYsTime.setVisibility(View.INVISIBLE);
                colorLightYsTime.setText("");
                break;
            case R.id.tv_mode_delay:
                checkDelay();
                //设置多彩灯模式--延时
                type = 0x03;
                colorLightYsTime.setVisibility(View.VISIBLE);
                colorLightYsTime.setText("");
                break;
        }
    }

    private void checkStatus() {
        if (mOpened) {
            collorLightSwitchIb.setImageResource(R.drawable.smart_check_on);
            collorLightCtrSeekbar.setProgress(240);
        } else {
            collorLightSwitchIb.setImageResource(R.drawable.smart_check_off);
            collorLightCtrSeekbar.setProgress(0);
        }
    }

    private void checkRightNow() {
        tvModeDelay.setTextColor(getResources().getColor(R.color.a1_blue_color));
        tvModeDelay.setBackgroundResource(R.drawable.shape_3_item_right_normal);
        tvModeGradually.setTextColor(getResources().getColor(R.color.a1_blue_color));
        tvModeGradually.setBackgroundResource(R.drawable.shape_3_item_center_normal);
        tvModeRightNow.setTextColor(getResources().getColor(R.color.white));
        tvModeRightNow.setBackgroundResource(R.drawable.shape_3_item_left_pressed);
    }

    private void checkGradually() {
        tvModeDelay.setTextColor(getResources().getColor(R.color.a1_blue_color));
        tvModeDelay.setBackgroundResource(R.drawable.shape_3_item_right_normal);
        tvModeRightNow.setTextColor(getResources().getColor(R.color.a1_blue_color));
        tvModeRightNow.setBackgroundResource(R.drawable.shape_3_item_left_normal);
        tvModeGradually.setTextColor(getResources().getColor(R.color.white));
        tvModeGradually.setBackgroundResource(R.drawable.shape_3_item_center_pressed);
    }

    private void checkDelay() {
        tvModeGradually.setTextColor(getResources().getColor(R.color.a1_blue_color));
        tvModeGradually.setBackgroundResource(R.drawable.shape_3_item_center_normal);
        tvModeRightNow.setTextColor(getResources().getColor(R.color.a1_blue_color));
        tvModeRightNow.setBackgroundResource(R.drawable.shape_3_item_left_normal);
        tvModeDelay.setTextColor(getResources().getColor(R.color.white));
        tvModeDelay.setBackgroundResource(R.drawable.shape_3_item_right_pressed);
    }



    private void checkRightQCNow() {
        tvHxd.setTextColor(getResources().getColor(R.color.a1_blue_color));
        tvHxd.setBackgroundResource(R.drawable.shape_3_item_right_normal);
        tvQcTb.setTextColor(getResources().getColor(R.color.a1_blue_color));
        tvQcTb.setBackgroundResource(R.drawable.shape_3_item_center_normal);
        tvModeQcJm.setTextColor(getResources().getColor(R.color.white));
        tvModeQcJm.setBackgroundResource(R.drawable.shape_3_item_left_pressed);
    }

    private void checkQCGradually() {
        tvHxd.setTextColor(getResources().getColor(R.color.a1_blue_color));
        tvHxd.setBackgroundResource(R.drawable.shape_3_item_right_normal);
        tvModeQcJm.setTextColor(getResources().getColor(R.color.a1_blue_color));
        tvModeQcJm.setBackgroundResource(R.drawable.shape_3_item_left_normal);
        tvQcTb.setTextColor(getResources().getColor(R.color.white));
        tvQcTb.setBackgroundResource(R.drawable.shape_3_item_center_pressed);
    }

    private void checkQCDelay() {
        tvQcTb.setTextColor(getResources().getColor(R.color.a1_blue_color));
        tvQcTb.setBackgroundResource(R.drawable.shape_3_item_center_normal);
        tvModeQcJm.setTextColor(getResources().getColor(R.color.a1_blue_color));
        tvModeQcJm.setBackgroundResource(R.drawable.shape_3_item_left_normal);
        tvHxd.setTextColor(getResources().getColor(R.color.white));
        tvHxd.setBackgroundResource(R.drawable.shape_3_item_right_pressed);
    }

    private void checkQCDefault() {
        tvQcTb.setTextColor(getResources().getColor(R.color.a1_blue_color));
        tvQcTb.setBackgroundResource(R.drawable.shape_3_item_center_normal);
        tvModeQcJm.setTextColor(getResources().getColor(R.color.a1_blue_color));
        tvModeQcJm.setBackgroundResource(R.drawable.shape_3_item_left_normal);
        tvHxd.setTextColor(getResources().getColor(R.color.a1_blue_color));
        tvHxd.setBackgroundResource(R.drawable.shape_3_item_right_normal);
    }

    private void checkRightModeNow() {
        tvModeQk.setTextColor(getResources().getColor(R.color.a1_blue_color));
        tvModeQk.setBackgroundResource(R.drawable.shape_3_item_right_normal);
        tvModeDk.setTextColor(getResources().getColor(R.color.white));
        tvModeDk.setBackgroundResource(R.drawable.shape_3_item_left_pressed);
    }


    private void checkModeDelay() {
        tvModeDk.setTextColor(getResources().getColor(R.color.a1_blue_color));
        tvModeDk.setBackgroundResource(R.drawable.shape_3_item_left_normal);
        tvModeQk.setTextColor(getResources().getColor(R.color.white));
        tvModeQk.setBackgroundResource(R.drawable.shape_3_item_right_pressed);
    }


    @OnClick({R.id.tv_mode_qc_jm, R.id.tv_qc_tb, R.id.tv_hxd,R.id.tv_mode_dk, R.id.tv_mode_qk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_mode_qc_jm:
                isHxD = false;
                checkRightQCNow();
                RestRequestApi.sendColorLightQc(iotMac, deviceInfo.getMac_address(), 1);
                break;
            case R.id.tv_qc_tb:
                isHxD = false;
                checkQCGradually();
                RestRequestApi.sendColorLightQc(iotMac, deviceInfo.getMac_address(), 2);
                break;
            case R.id.tv_hxd:
                checkQCDelay();
                isHxD = true;
                RestRequestApi.sendColorLightHXD(iotMac, deviceInfo.getMac_address(), (byte)0xff, (byte)0xff, (byte)0xff);
                break;
            case R.id.tv_mode_dk:
                checkRightModeNow();
                break;
            case R.id.tv_mode_qk:
                checkModeDelay();
                break;
        }
    }


    private class ColorChangedListener implements ColorPicker.OnColorChangedListener {

        @Override
        public void onColorChanged(int value) {
            Log.i("laixj", "onColorChanged:" + value);
            int color = value;
            mRed = (byte) Color.red(color);
            mGreen = (byte) Color.green(color);
            mBlue = (byte) Color.blue(color);
            String time_str = colorLightYsTime.getText().toString();
            byte time = 0x00;
            if (!TextUtils.isEmpty(time_str)) {
                byte time_byte = Byte.valueOf(time_str);
                if (time_byte > 0) {
                    time = time_byte;
                } else {
                    showToast("延时时间不能大于255秒");
                }
            }
            if(!isHxD) {
                RestRequestApi.sendColorLightColor(iotMac, deviceInfo.getMac_address(), type, time, mRed, mGreen, mBlue);
            }else{
                RestRequestApi.sendColorLightHXD(iotMac, deviceInfo.getMac_address(), mRed, mGreen, mBlue);
            }
        }
    }
}
