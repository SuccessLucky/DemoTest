package com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultRingtoneSelect;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.squareup.okhttp.Request;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author laixj
 * @version V1.0
 * @Title: RingtoneSetFragment
 * @Description: 铃声设置界面
 * @date 2016/9/15 16:31
 */
public class RingtoneSetFragment extends BaseRequestFragment implements RequestCallback {

    @BindView(R.id.setting_ringtone_keypad_tv)
    TextView settingRingtoneKeypadTv;
    @BindView(R.id.setting_ringtone_keypad_cb)
    ImageButton settingRingtoneKeypadCb;
    @BindView(R.id.setting_ringtone_keypad_rl)
    RelativeLayout settingRingtoneKeypadRl;
    @BindView(R.id.setting_ringtone_alarm_tv)
    TextView settingRingtoneAlarmTv;
    @BindView(R.id.setting_ringtone_alarm_cb)
    ImageButton settingRingtoneAlarmCb;
    @BindView(R.id.setting_vibrate_rl)
    RelativeLayout settingVibrateRl;
    @BindView(R.id.setting_ringtone_offline_tv)
    TextView settingRingtoneOfflineTv;
    @BindView(R.id.setting_ringtone_offline_cb)
    ImageButton settingRingtoneOfflineCb;
    @BindView(R.id.setting_ringtone_select_rl)
    RelativeLayout settingRingtoneSelectRl;

    private boolean isKeypadSound;
    private boolean isAlarmSound;
    private boolean isOfflineSound;
    /**
     * 现有按键音
     */
    private RingtoneInfo originKeypadRingtone;
    /**
     * 现有报警提示音
     */
    private RingtoneInfo originAlarmRingtone;
    /**
     * 现有掉线提示音
     */
    private RingtoneInfo originOfflineRingtone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_ringtone_set_fragment_layout, container, false);
        ButterKnife.bind(this, mRootView);
        initData();
        initView();
        Log.d("laixj", "设置铃声onCreateView");
        GjjEventBus.getInstance().register(mRingtoneSelectEvent);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData(){
        // TODO: 2016/9/15
    }

    private void initView(){
        // TODO: 2016/9/15
        Log.d("laixj", "设置铃声initView");
        if(null == originKeypadRingtone){
            settingRingtoneKeypadTv.setText(R.string.setting_ringtone_default_str);
        }else{
            settingRingtoneKeypadTv.setText(originKeypadRingtone.getRingtoneName());
        }
        if(null == originAlarmRingtone){
            settingRingtoneAlarmTv.setText(R.string.setting_ringtone_default_str);
        }else{
            settingRingtoneAlarmTv.setText(originAlarmRingtone.getRingtoneName());
        }
        if(null == originOfflineRingtone){
            settingRingtoneOfflineTv.setText(R.string.setting_ringtone_default_str);
        }else{
            settingRingtoneOfflineTv.setText(originOfflineRingtone.getRingtoneName());
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
        GjjEventBus.getInstance().unregister(mRingtoneSelectEvent);
    }

    @OnClick({R.id.setting_ringtone_keypad_tv, R.id.setting_ringtone_keypad_cb, R.id.setting_ringtone_alarm_tv, R.id.setting_ringtone_alarm_cb, R.id.setting_ringtone_offline_tv, R.id.setting_ringtone_offline_cb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_ringtone_keypad_tv:{
                Bundle bundle = new Bundle();
                bundle.putSerializable("ringtone", (Serializable)originKeypadRingtone);
                bundle.putString("type", "keypad");
                PageSwitcher.switchToTopNavPage(getActivity(), RingtoneSelectFragment.class, bundle, "", "铃声选择", "确定", -1, "");
                break;}
            case R.id.setting_ringtone_keypad_cb:
                isKeypadSound = !isKeypadSound;
                if (isKeypadSound) {
                    settingRingtoneKeypadCb.setImageResource(R.drawable.smart_check_on);
                } else {
                    settingRingtoneKeypadCb.setImageResource(R.drawable.smart_check_off);
                }
                // TODO: 2016/9/15
                break;
            case R.id.setting_ringtone_alarm_tv:{
                Bundle bundle = new Bundle();
                bundle.putSerializable("ringtone", (Serializable)originAlarmRingtone);
                bundle.putString("type", "alarm");
                PageSwitcher.switchToTopNavPage(getActivity(), RingtoneSelectFragment.class, bundle, "", "铃声选择", "确定", -1, "");
                break;}
            case R.id.setting_ringtone_alarm_cb:
                isAlarmSound = !isAlarmSound;
                if (isAlarmSound) {
                    settingRingtoneAlarmCb.setImageResource(R.drawable.smart_check_on);
                } else {
                    settingRingtoneAlarmCb.setImageResource(R.drawable.smart_check_off);
                }
                // TODO: 2016/9/15
                break;
            case R.id.setting_ringtone_offline_tv:{
                Bundle bundle = new Bundle();
                bundle.putSerializable("ringtone", (Serializable)originOfflineRingtone);
                bundle.putString("type", "offline");
                PageSwitcher.switchToTopNavPage(getActivity(), RingtoneSelectFragment.class, bundle, "", "铃声选择", "确定", -1, "");
                break;}
            case R.id.setting_ringtone_offline_cb:
                isOfflineSound = !isOfflineSound;
                if (isOfflineSound) {
                    settingRingtoneOfflineCb.setImageResource(R.drawable.smart_check_on);
                } else {
                    settingRingtoneOfflineCb.setImageResource(R.drawable.smart_check_off);
                }
                // TODO: 2016/9/15
                break;
        }
    }

    /**
     * 请求回调
     */
    public Object mRingtoneSelectEvent = new Object() {
        public void onEventMainThread(EventOfResultRingtoneSelect data) {
            RingtoneInfo selected = data.ringtoneInfo;
            String type = data.type;
            if(null == type || null == selected){
                return;
            }
            Log.d("laixj", "设置铃声mRingtoneSelectEvent--"+selected.toString());
            Log.d("laixj", "设置铃声mRingtoneSelectEvent--"+type);
            if("keypad".equals(type)){
                if(originKeypadRingtone == null){
                    originKeypadRingtone = selected;
                }else{
                    if(!originKeypadRingtone.equals(selected)){
                        originKeypadRingtone = selected;
                        showToast("选择按键铃声"+selected.toString());
                    }
                }
                if(null == originKeypadRingtone){
                    settingRingtoneKeypadTv.setText(R.string.setting_ringtone_default_str);
                }else{
                    settingRingtoneKeypadTv.setText(originKeypadRingtone.getRingtoneName());
                }
                // TODO: 2016/9/15
            }else if("alarm".equals(type)){
                if(originAlarmRingtone == null){
                    originAlarmRingtone = selected;
                }else{
                    if(!originAlarmRingtone.equals(selected)){
                        originAlarmRingtone = selected;
                        showToast("选择报警铃声"+selected.toString());
                    }
                }
                if(null == originAlarmRingtone){
                    settingRingtoneAlarmTv.setText(R.string.setting_ringtone_default_str);
                }else{
                    settingRingtoneAlarmTv.setText(originAlarmRingtone.getRingtoneName());
                }
                // TODO: 2016/9/15
            }if("offline".equals(type)){
                if(originOfflineRingtone == null){
                    originOfflineRingtone = selected;
                }else{
                    if(!originOfflineRingtone.equals(selected)){
                        originOfflineRingtone = selected;
                        showToast("选择掉线铃声"+selected.toString());
                    }
                }
                if(null == originOfflineRingtone){
                    settingRingtoneOfflineTv.setText(R.string.setting_ringtone_default_str);
                }else{
                    settingRingtoneOfflineTv.setText(originOfflineRingtone.getRingtoneName());
                }
                // TODO: 2016/9/15
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            showToast("选择铃声返回");
            if (requestCode == RingtoneSelectFragment.REQUEST_CODE_RINGTONE_SELECT) {
                // TODO: 2016/9/15
                RingtoneInfo selected = data.getParcelableExtra("ringtoneInfo");
                String type = data.getStringExtra("type");
                if(null == type || null == selected){
                    return;
                }
                if("keypad".equals(type)){
                    if(originKeypadRingtone == null){
                        originKeypadRingtone = selected;
                    }else{
                        if(!originKeypadRingtone.equals(selected)){
                            originKeypadRingtone = selected;
                            showToast("选择按键铃声"+selected.toString());
                        }
                    }
                    if(null == originKeypadRingtone){
                        settingRingtoneKeypadTv.setText(R.string.setting_ringtone_default_str);
                    }else{
                        settingRingtoneKeypadTv.setText(originKeypadRingtone.getRingtoneName());
                    }
                    // TODO: 2016/9/15
                }else if("alarm".equals(type)){
                    if(originAlarmRingtone == null){
                        originAlarmRingtone = selected;
                    }else{
                        if(!originAlarmRingtone.equals(selected)){
                            originAlarmRingtone = selected;
                            showToast("选择报警铃声"+selected.toString());
                        }
                    }
                    if(null == originAlarmRingtone){
                        settingRingtoneAlarmTv.setText(R.string.setting_ringtone_default_str);
                    }else{
                        settingRingtoneAlarmTv.setText(originAlarmRingtone.getRingtoneName());
                    }
                    // TODO: 2016/9/15
                }if("offline".equals(type)){
                    if(originOfflineRingtone == null){
                        originOfflineRingtone = selected;
                    }else{
                        if(!originOfflineRingtone.equals(selected)){
                            originOfflineRingtone = selected;
                            showToast("选择掉线铃声"+selected.toString());
                        }
                    }
                    if(null == originOfflineRingtone){
                        settingRingtoneOfflineTv.setText(R.string.setting_ringtone_default_str);
                    }else{
                        settingRingtoneOfflineTv.setText(originOfflineRingtone.getRingtoneName());
                    }
                    // TODO: 2016/9/15
                }
            }
        }
    }
}
