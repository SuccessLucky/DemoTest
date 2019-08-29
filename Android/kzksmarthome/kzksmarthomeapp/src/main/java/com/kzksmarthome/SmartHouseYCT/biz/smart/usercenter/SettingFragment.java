package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;
import com.squareup.okhttp.Request;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: SettingFragment
 * @Description: 设置界面
 * @date 2016/9/15 15:10
 */
public class SettingFragment extends BaseRequestFragment implements RequestCallback {

    @BindView(R.id.setting_push_switch_iv)
    ImageView settingPushSwitchIv;
    @BindView(R.id.setting_push_switch_tv)
    TextView settingPushSwitchTv;
    @BindView(R.id.setting_push_switch_cb)
    ImageButton settingPushSwitchCb;
    @BindView(R.id.setting_push_switch_rl)
    RelativeLayout settingPushSwitchRl;
    @BindView(R.id.setting_vibrate_iv)
    ImageView settingVibrateIv;
    @BindView(R.id.setting_vibrate_tv)
    TextView settingVibrateTv;
    @BindView(R.id.setting_vibrate_cb)
    ImageButton settingVibrateCb;
    @BindView(R.id.setting_vibrate_rl)
    RelativeLayout settingVibrateRl;
    @BindView(R.id.setting_ringtone_select_iv)
    ImageView settingRingtoneSelectIv;
    @BindView(R.id.setting_ringtone_select_tv)
    TextView settingRingtoneSelectTv;
    @BindView(R.id.setting_ringtone_select_rl)
    RelativeLayout settingRingtoneSelectRl;

    private boolean isReceivePush;
    private boolean isVibrate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_layout_setting_fragment, container, false);
        ButterKnife.bind(this, mRootView);
        initData();
        initView();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData(){
        // TODO: 2016/9/15
    }

    private void initView(){
         isVibrate =  SmartHomeAppLib.getInstance().getPreferences().getBoolean(SharePrefConstant.KEY_VIBRATE_FLAG,false);
        if(isReceivePush){
            settingPushSwitchCb.setImageResource(R.drawable.smart_check_on);
        }else{
            settingPushSwitchCb.setImageResource(R.drawable.smart_check_off);
        }
        if(isVibrate){
            settingVibrateCb.setImageResource(R.drawable.smart_check_on);
        }else{
            settingVibrateCb.setImageResource(R.drawable.smart_check_off);
        }
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

    @OnClick({R.id.setting_push_switch_cb, R.id.setting_vibrate_cb, R.id.setting_ringtone_select_rl})
    public void onClick(View view) {
        Log.d("laixj", "点击-->"+view.getId());
        switch (view.getId()) {
            case R.id.setting_push_switch_cb:
                isReceivePush = !isReceivePush;
                if(isReceivePush){
                    settingPushSwitchCb.setImageResource(R.drawable.smart_check_on);
                }else{
                    settingPushSwitchCb.setImageResource(R.drawable.smart_check_off);
                }
                // TODO: 2016/9/15
                break;
            case R.id.setting_vibrate_cb:
                if(!isVibrate){
                    settingVibrateCb.setImageResource(R.drawable.smart_check_on);
                    SmartHomeAppLib.getInstance().getPreferences().edit().putBoolean(SharePrefConstant.KEY_VIBRATE_FLAG,true).commit();
                    isVibrate = true;
                }else{
                    settingVibrateCb.setImageResource(R.drawable.smart_check_off);
                    SmartHomeAppLib.getInstance().getPreferences().edit().putBoolean(SharePrefConstant.KEY_VIBRATE_FLAG,false).commit();
                    isVibrate = false;
                }
                break;
            case R.id.setting_ringtone_select_rl:
                PageSwitcher.switchToTopNavPage(getActivity(), RingtoneSetFragment.class, null, "", getString(R.string.setting_ringtone_select_str), "");
                break;
        }
    }
}
