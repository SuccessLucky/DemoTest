package com.kzksmarthome.SmartHouseYCT.biz.smart.setting;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kzksmarthome.common.event.EventOfEasyLinkSearchEnd;
import com.kzksmarthome.common.event.EventOfIOTSettingState;
import com.kzksmarthome.common.lib.easylink.data.TempData;
import com.mxchip.easylink.EasyLinkAPI;
import com.mxchip.wifiman.EasyLinkWifiManager;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultSelectGw;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfSelectIOT;
import com.kzksmarthome.SmartHouseYCT.biz.main.MainActivity;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.GatewayInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.AddGwResponse;
import com.kzksmarthome.SmartHouseYCT.util.BundleKeyConstant;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.biz.widget.CustomProgressDialog;
import com.kzksmarthome.common.event.EventOfTcpResult;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;
import com.kzksmarthome.common.lib.tcp.TCPMgr;
import com.kzksmarthome.common.lib.tools.DeviceState;
import com.kzksmarthome.common.lib.tools.IOTConfig;
import com.kzksmarthome.common.lib.tools.Tools;
import com.kzksmarthome.common.lib.util.AndroidUtil;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.net.ApiHost;
import com.kzksmarthome.common.module.user.UserInfo;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;
import com.squareup.okhttp.Request;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置网关界面
 */
public class SettingIOTActivity extends Activity implements RequestCallback ,IOTSelectOperationDialog.OnIOTSelectClickListener{
    @BindView(R.id.iot_setting_ssid)
    EditText wifissid;
    @BindView(R.id.iot_setting_pwd)
    EditText wifipsw;
    @BindView(R.id.commit_iot_setting)
    Button startsearch;
    @BindView(R.id.title_activity_tv)
    TextView titleActivityTv;
    @BindView(R.id.iot_setting_port)
    EditText iot_setting_port;

    public EasyLinkAPI elapi;
    private Context ctx = null;
    private EasyLinkWifiManager mWifiManager = null;
    private CustomProgressDialog mLoginDialog;
    private boolean isAddGw;
    /**
     * 选择Iot
     */
    private IOTSelectOperationDialog mIOTSelectOperationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_setting_iot_fragment_layout);
        ButterKnife.bind(this);
        titleActivityTv.setText(R.string.iot_setting_title_str);
        Intent intent = getIntent();
        if(intent != null){
            intent = getIntent();
            isAddGw =  intent.getBooleanExtra(BundleKeyConstant.KEY_GW_IS_ADD,false);
        }
        initSetting();
        GjjEventBus.getInstance().register(readMacEvent);
    }

    private void initSetting() {
        ctx = this;
        elapi = new EasyLinkAPI(ctx);

        mWifiManager = new EasyLinkWifiManager(ctx);
        wifissid.setText(mWifiManager.getCurrentSSID());

        startsearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String wifiPwd = wifipsw.getText().toString().trim().replaceAll(" ", "");
                String wifiSSID = wifissid.getText().toString().trim().replaceAll(" ", "");
                String port = iot_setting_port.getText().toString().trim().replaceAll(" ", "");
                if(!TextUtils.isEmpty(port)){
                    TempData.PORT = Integer.valueOf(port);
                }
                if (TextUtils.isEmpty(wifiSSID)) {
                    SmartHomeApp.showToast(R.string.no_wifi_ssid);
                    return;
                } else if (TextUtils.isEmpty(wifiPwd)) {
                    SmartHomeApp.showToast(R.string.no_wifi_pwd);
                    return;
                }
                //启动easylink配置地址
                elapi.startEasyLink(ctx, wifiSSID, wifiPwd);
                MainTaskExecutor.scheduleTaskOnUiThread(30000, runnable);
                showLoading(getString(R.string.start_seting_iot));
                ApiHost.NETWORK_ISREMOTE = false;
                TCPMgr.getInstance().closeConnect();
                //SmartHomeApp.getInstance().getIOTInfo(true);
                SmartHomeApp.getInstance().getIOTInfoNew(true,10);
            }
        });
    }
    /**
     * 执行10秒检测
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            dismissProgressDialog();
            SmartHomeApp.showToast(R.string.setting_iot_time_out_string);
        }
    };


    private void showLoading(String msg) {
        CustomProgressDialog loginDialog = mLoginDialog;
        if (null == loginDialog) {
            loginDialog = new CustomProgressDialog(SettingIOTActivity.this);
            mLoginDialog = loginDialog;
            loginDialog.setCancelable(false);
            loginDialog.setCanceledOnTouchOutside(true);
            loginDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override

                public void onCancel(DialogInterface dialog) {
                    onBackPressed();
                }
            });
        }
        loginDialog.setTipText(msg);
        if(!loginDialog.isShowing()) {
            loginDialog.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SmartHomeApp.getInstance().setmCurrentActivity(this);
    }

    //初始化网络连接
    Object readMacEvent = new Object() {
        public void onEventMainThread(EventOfTcpResult eventOfTcpResult) {
            if (eventOfTcpResult != null && eventOfTcpResult.deviceState != null) {
                DeviceState deviceState = eventOfTcpResult.deviceState;
                try {
                    byte[] srcAddr = deviceState.srcAddr;
                    if (srcAddr != null && srcAddr.length > 0) {
                        IOTConfig.SRCADDR = deviceState.srcAddr;
                        //保存网关Mac地址(把byte转成了字符串，使用是记得转回来，方法在tools中)
                        String mac = Tools.byte2HexStr(srcAddr);
                        String name = "网关-"+mac;
                        SmartHomeAppLib.getInstance().getPreferences().edit()
                                .putString(SharePrefConstant.PREFS_KEY_IOT_MAC, mac)
                                .commit();
                        String wifi_mac_address = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_IOT_WIFI_MAC,null);
                        RestRequestApi.addGateway(SettingIOTActivity.this, name, mac,wifi_mac_address, SettingIOTActivity.this);
                        SmartHomeApp.showToast(R.string.get_gw_mac_succ);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        public void onEventMainThread(EventOfSelectIOT event) {
            dismissProgressDialog();
            if(isFinishing()){
                return;
            }
            if(event != null && !Util.isListEmpty(event.getIotSelectInfoArrayList())){
                selectIOT(event.getIotSelectInfoArrayList());
            }
        }

        public void onEventMainThread(EventOfIOTSettingState eventOfIOTSettingState) {
            dismissProgressDialog();
            SmartHomeApp.showToast(R.string.setting_iot_fail);
        }

        public void onEventMainThread(EventOfEasyLinkSearchEnd eventOfEasyLinkSearchEnd) {
            MainTaskExecutor.cancelTaskOnUiThread(runnable);
            //网关配置结束
            elapi.stopEasyLink();
        }
    };

    /**
     * 设置红外类型
     */
    public void selectIOT(ArrayList<IOTSelectInfo> dataList) {
        if(mIOTSelectOperationDialog == null) {
            mIOTSelectOperationDialog = null;
        }else if(mIOTSelectOperationDialog.isShowing()){
            return;
        }
        mIOTSelectOperationDialog = new IOTSelectOperationDialog(this, dataList);
        mIOTSelectOperationDialog.setOnRedDeviceTypeClickListener(this);
        mIOTSelectOperationDialog.setCancelable(false);
        mIOTSelectOperationDialog.setCanceledOnTouchOutside(true);
        mIOTSelectOperationDialog.show();
    }

    @Override
    public void onIOTSelectClick(int position) {
        try {
            if(mIOTSelectOperationDialog != null){
                IOTSelectInfo iotSelectInfo = mIOTSelectOperationDialog.mAdapter.mDataList.get(position);
                SmartHomeAppLib.getInstance().getPreferences().edit().putString(SharePrefConstant.PREFS_KEY_IOT_WIFI_MAC, iotSelectInfo.getIot_wifi_mac()).commit();
                SmartHomeApp.getInstance().connectServerAddress(iotSelectInfo.getHostIp(),iotSelectInfo.getHostPort(),false);
                mIOTSelectOperationDialog.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 关闭登录提示框
     */
    private void dismissProgressDialog() {
        if (null != mLoginDialog) {
            mLoginDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GjjEventBus.getInstance().unregister(readMacEvent);
    }

    @OnClick(R.id.title_activity_back_im)
    public void onClick() {
        this.finish();
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {
        if (this.isFinishing()) {
            return;
        }
        dismissProgressDialog();
        SmartHomeApp.showToast("请检查网络");
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        if (this.isFinishing()) {
            return;
        }
        dismissProgressDialog();
        if (ApiHost.ADD_GW_URL.equals(url)) {
            if (response != null) {
                AddGwResponse param = (AddGwResponse) response.body;
                if (param != null) {
                    if(param.isSuccess()){
                        if(null != param.getResult().getUser_gateways() && param.getResult().getUser_gateways().size() > 0){
                            GatewayInfo gwInfo = param.getResult().getUser_gateways().get(0);
                            UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
                            userInfo.gateway = gwInfo.getMac_address();
                            userInfo.role = gwInfo.getMember_type();
                            userInfo.uuid = AndroidUtil.getDeviceIMEI(getApplication());
                            SmartHomeAppLib.getUserMgr().saveUser(userInfo);
                            EventOfResultSelectGw event = new EventOfResultSelectGw();
                            event.gwInfo = gwInfo;
                            event.isAddGW = true;
                            GjjEventBus.getInstance().post(event);
                            Intent intent = new Intent(SettingIOTActivity.this, MainActivity.class);
                            startActivity(intent);
                            this.finish();
                        }
                    }else{
                        if(null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())){
                            SmartHomeApp.showToast(param.getError().getMessage());
                            setGwErrorInfo();
                        }else{
                            setGwErrorInfo();
                            SmartHomeApp.showToast("网关添加失败");
                        }
                    }
                }else{
                    setGwErrorInfo();
                    SmartHomeApp.showToast("网关添加失败");
                }
            }
        }
    }

    /**
     * 设置失败后的信息
     */
    private void setGwErrorInfo(){
        SmartHomeAppLib.getInstance().getPreferences().edit()
                .putString(SharePrefConstant.PREFS_KEY_IOT_MAC, null)
                .commit();
        SmartHomeAppLib.getInstance().getPreferences().edit().putString(SharePrefConstant.PREFS_KEY_IOT_WIFI_MAC,null).commit();
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        if (this.isFinishing()) {
            return;
        }
        dismissProgressDialog();
        SmartHomeApp.showToast("网关添加失败");
    }
}
