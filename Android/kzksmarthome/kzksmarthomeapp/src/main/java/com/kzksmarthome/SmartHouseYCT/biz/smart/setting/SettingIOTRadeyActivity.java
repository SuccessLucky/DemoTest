package com.kzksmarthome.SmartHouseYCT.biz.smart.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.util.BundleKeyConstant;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 网关设置准备
 * Created by jack on 2016/9/20.
 */
public class SettingIOTRadeyActivity extends Activity {
    @BindView(R.id.title_activity_tv)
    TextView titleActivityTv;
    private boolean isAddGw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_iot_ready);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if(intent != null){
            intent = getIntent();
           isAddGw = intent.getBooleanExtra(BundleKeyConstant.KEY_GW_IS_ADD,false);
        }
        titleActivityTv.setText(R.string.iot_setting_hint);
    }

    @OnClick(R.id.setting_iot_ready_next)
    public void onClick() {
        Intent intent = new Intent(this, SettingIOTActivity.class);
        intent.putExtra(BundleKeyConstant.KEY_GW_IS_ADD,isAddGw);
        startActivity(intent);
        this.finish();
    }

    @OnClick(R.id.title_activity_back_im)
    public void onClickBack() {
        this.finish();
    }
}

