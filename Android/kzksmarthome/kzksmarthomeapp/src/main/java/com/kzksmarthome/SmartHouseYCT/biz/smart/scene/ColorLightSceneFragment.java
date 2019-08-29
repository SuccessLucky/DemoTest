package com.kzksmarthome.SmartHouseYCT.biz.smart.scene;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfColorLight;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.widget.ColorPicker;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.module.user.UserInfo;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author jacl
 * @version V1.0
 * @Title: ColorLightControlFragment
 * @Description: 多彩灯场景设置界面

 */
public class ColorLightSceneFragment extends BaseRequestFragment {

    @BindView(R.id.color_picker)
    ColorPicker colorPicker;
    @BindView(R.id.tv_mode_qc_jm)
    TextView tvModeQcJm;
    @BindView(R.id.tv_qc_tb)
    TextView tvQcTb;
    @BindView(R.id.tv_hxd)
    TextView tvHxd;
    /**
     * 是否为呼吸灯
     */
    private boolean isHxD;

    /**
     * 设备信息
     */
    private DeviceInfo deviceInfo;
    /**
     * 网关
     */
    private String iotMac;
    private byte mRed;
    private byte mGreen;
    private byte mBlue;
    /**
     * 命令数据
     */
    private ColorLightOrderModel mColorLightOrderModel;
    /**
     * 0：未选中其他操作
     */
    private int defaultColor;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_layout_color_light_scene, container, false);
        ButterKnife.bind(this, mRootView);
        Bundle bundle = getArguments();
        deviceInfo = bundle.getParcelable("deviceInfo");
        colorPicker.setOnColorChangedListener(new ColorChangedListener());
        UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        if (userInfo != null) {
            iotMac = userInfo.gateway;
        }
        mColorLightOrderModel = new ColorLightOrderModel();
        mColorLightOrderModel.setMac_address(deviceInfo.getMac_address());
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



    @Override
    public void onBackPressed() {
        EventOfColorLight event = new EventOfColorLight();
        event.setColorLightOrderModel(mColorLightOrderModel);
        GjjEventBus.getInstance().post(event);
        getActivity().onBackPressed();
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

    @OnClick({R.id.tv_mode_qc_jm, R.id.tv_qc_tb, R.id.tv_hxd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_mode_qc_jm:
                isHxD = false;
                checkRightQCNow();
                mColorLightOrderModel.setOrderArray(RestRequestApi.getColorLightQc(iotMac, deviceInfo.getMac_address(), 1));
                defaultColor = 1;
                break;
            case R.id.tv_qc_tb:
                isHxD = false;
                checkQCGradually();
                mColorLightOrderModel.setOrderArray(RestRequestApi.getColorLightQc(iotMac, deviceInfo.getMac_address(), 2));
                defaultColor = 2;
                break;
            case R.id.tv_hxd:
                checkQCDelay();
                isHxD = true;
                mColorLightOrderModel.setOrderArray(RestRequestApi.getColorLightHXD(iotMac, deviceInfo.getMac_address(), (byte)0xff, (byte)0xff, (byte)0xff));
                defaultColor = 3;
                break;
        }
    }


    private class ColorChangedListener implements ColorPicker.OnColorChangedListener {

        @Override
        public void onColorChanged(int value) {
            int color = value;
            mRed = (byte) Color.red(color);
            mGreen = (byte) Color.green(color);
            mBlue = (byte) Color.blue(color);
            if(isHxD) {
                mColorLightOrderModel.setOrderArray(RestRequestApi.getColorLightHXD(iotMac, deviceInfo.getMac_address(), mRed, mGreen, mBlue));
            }else if(defaultColor == 0){
                mColorLightOrderModel.setOrderArray(RestRequestApi.getColorLightColor(iotMac,deviceInfo.getMac_address(),(byte)0x01,(byte)0x00,mRed,mGreen,mBlue));
            }
        }
    }
}
