package com.kzksmarthome.SmartHouseYCT.biz.smart.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseRequestFragment;
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultSelectGw;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfSceneUpdate;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfWeather;
import com.kzksmarthome.SmartHouseYCT.biz.main.MainActivity;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceButtonInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.GatewayInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetDeviceByIdResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetGatewayInfoResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetSceneListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.UpdateGwSecurityResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.SceneInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.weather.WeatherModel;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.biz.widget.discretescrollview.DiscreteScrollView;
import com.kzksmarthome.common.biz.widget.discretescrollview.transform.ScaleTransformer;
import com.kzksmarthome.common.event.EventOfOpenCamera;
import com.kzksmarthome.common.event.EventOfTcpResult;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;
import com.kzksmarthome.common.lib.tools.DeviceState;
import com.kzksmarthome.common.lib.tools.Tools;
import com.kzksmarthome.common.module.net.ApiHost;
import com.kzksmarthome.common.module.user.UserInfo;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.videogo.openapi.EZOpenSDK;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.util.CLog;

/**
 * 主界面
 * Created by jack on 2016/8/24.
 */
public class IndexFragment extends BaseRequestFragment implements RequestCallback,CommonSceneAdapter.OnSceneItemClick {
    public byte[] one_way_kg_dst;

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.index_city_tv)
    TextView indexCityTv;
    @BindView(R.id.index_weather_im)
    ImageView indexWeatherIm;
    @BindView(R.id.index_weather_temperature_tv)
    TextView indexWeatherTemperatureTv;
    @BindView(R.id.index_cbf_tv)
    TextView indexCBFTv;
    @BindView(R.id.index_warning_tv)
    TextView indexWarningTv;
    @BindView(R.id.index_warning_layout)
    RelativeLayout indexWarningLayout;
    @BindView(R.id.index_camera_layout)
    RelativeLayout indexCameraLayout;
    @BindView(R.id.index_scene_recycle)
    RecyclerView indexSceneRecycle;
    @BindView(R.id.index_time_tv)
    TextView index_time_tv;

    private CommonSceneAdapter mAdapter;

    private EZOpenSDK mEZOpenSDK = null;

    private List<SceneInfo> sceneList = new ArrayList<SceneInfo>();
    private String baseSceneUrl = "";
    //private ImageLoader imageLoader = ImageLoader.getInstance();
    //private ImageLoader mImageLoader;

    private GatewayInfo gwInfo = null;

    private String originGwMac = null;
    /**
     * 布放1、撤防0
     */
    private int  intBFCF = 0;

    private Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.smart_layout_index_fragment, container, false);
        ButterKnife.bind(this, mRootView);
        GjjEventBus.getInstance().register(mIndexEvent);
        mEZOpenSDK = EZOpenSDK.getInstance();
        Log.d("laixj", "首页请求数据-onCreateView->");
        initData();
        initView();
        baseSceneUrl = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_SCENE_BASEURL, "");
        //mImageLoader = SmartHomeApp.getInstance().getImageLoader((IComponentContainer) getActivity(), R.drawable.translucent);
        UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        if(null != userInfo){
            originGwMac = userInfo.gateway;
        }
        GjjEventBus.getInstance().register(deviceEvent);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(hidden){
            if(timer != null) {
                timer.cancel();
                timer = null;
            }
        }else{
            startTime();
        }
        super.onHiddenChanged(hidden);
    }

    private void initData() {
        Log.d("laixj", "首页请求数据->");
        RestRequestApi.getSceneList(getActivity(), 1, this);
        RestRequestApi.getGatewayInfo(getActivity(), this);
    }



    private void initView() {
        initSecurity();
        mAdapter = new CommonSceneAdapter(getActivity());
        indexSceneRecycle.setLayoutManager(new GridLayoutManager(SmartHomeApp.getInstance().getmCurrentActivity(),3));
        mAdapter.setSceneList(sceneList);
        mAdapter.setOnSceneItemClickListener(this);
        indexSceneRecycle.setAdapter(mAdapter);
        index_time_tv.setText(getTime());
        startTime();
    }


    /**
     * 获取时间
     * @return
     */
    public SpannableString getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String dataTime = simpleDateFormat.format(new Date());
        GregorianCalendar ca = new GregorianCalendar();
        String tm = dataTime + "上午";
        if(ca.get(GregorianCalendar.AM_PM) > 0){
            tm = dataTime + "下午";
        }
        SpannableString spannableString = new SpannableString(tm);
        SuperscriptSpan superscriptSpan = new SuperscriptSpan();
        int textLength = tm.length();
        spannableString.setSpan(superscriptSpan, textLength -2, textLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.4f);
        spannableString.setSpan(sizeSpan, textLength -2, textLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 启动时间
     */
    private void startTime(){
            if(timer == null) {
                timer = new Timer();
            }
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    MainTaskExecutor.runTaskOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SpannableString time = getTime();
                            if(time != null && index_time_tv != null) {
                                index_time_tv.setText(time);
                            }
                        }
                    });
                    startTime();
                }
            }, 60 * 1000);
    }




    private void initSecurity() {
        indexCBFTv.setBackgroundResource(R.drawable.bfcf_bg);
    }

    private void judgeSecurity() {
        if (null == gwInfo) {
            initSecurity();
        } else {
            if (1 == gwInfo.getSecurity_status()) {
                indexCBFTv.setBackgroundResource(R.drawable.bf_bg);
            } else {
                indexCBFTv.setBackgroundResource(R.drawable.bfcf_bg);
            }
        }
    }

    /**
     * 请求回调
     */
    public Object mIndexEvent = new Object() {
        public void onEventBackgroundThread(EventOfTcpResult eventOfTcpResult) {
            if (eventOfTcpResult != null) {
                DeviceState deviceState = eventOfTcpResult.deviceState;
                if (deviceState != null) {
                    if (deviceState.deviceType == 0x05) {
                        if (one_way_kg_dst == null) {
                            one_way_kg_dst = deviceState.dstAddr;
                        }
                    }
                }
            }
        }

        public void onEventBackgroundThread(EventOfWeather eventOfWeather) {
            if (eventOfWeather != null) {
                CLog.i("laixj","--->"+eventOfWeather.responseStr);
                String data = eventOfWeather.responseStr;
                try {
                    initWeatherData(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 切换网关之后需重新请求数据
         * @param data
         */
        public void onEventMainThread(EventOfResultSelectGw data) {
            if (null == data.gwInfo) {
                return;
            }
            Log.d("switchgw", "首页切换网关-data.gwInfo->"+data.gwInfo.getGateway_name());
            GatewayInfo newGw = data.gwInfo;
            if (!data.isAddGW && newGw.getMac_address().equals(originGwMac)) {
                return;
            }
            Log.d("switchgw", "首页切换网关-originGwMac->"+originGwMac);
            originGwMac = newGw.getMac_address();
            Log.d("laixj", "切换网关重新请求数据->");
            emptyScene();
            initData();
        }

        public void onEventMainThread(EventOfSceneUpdate event){//情景模式数据更新了重新请求
            if(getActivity() == null){
                return;
            }
            RestRequestApi.getSceneList(getActivity(), 1, IndexFragment.this);
        }
    };


    @Override
    public void onFailure(Request request, String url, Exception e) {
        if (getActivity() == null) {
            return;
        }
        dismissLoadingDialog();
        SmartHomeApp.showToast("请检查网络");
        if (url.equals(ApiHost.GET_SCENE_LIST_URL + "?type=1")) {
            emptyScene();
        }
    }

    private void emptyScene() {
        mAdapter.setSceneList(new ArrayList<SceneInfo>());
        mAdapter.notifyDataSetChanged();
    }

    private void initScene() {
        mAdapter.setSceneList(sceneList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSceneItemClick(int position) {
        SceneInfo sceneInfo = sceneList.get(position);
        if (null != sceneInfo) {
            Log.d("laixj", "点击常用场景->" + sceneInfo.toString());
            doLoading();
            sceneCtrl(sceneInfo);
        }
    }


    /**
     * 显示loading
     */
    private void doLoading(){
        showLoadingDialog(R.string.send_str,true);
        MainTaskExecutor.scheduleTaskOnUiThread(2000, new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
            }
        });
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        try {
            if (getActivity() == null) {
                return;
            }
            dismissLoadingDialog();
            if (url.equals(ApiHost.GET_SCENE_LIST_URL + "?type=1")) {
                if (response != null) {
                    GetSceneListResponse param = (GetSceneListResponse) response.body;
                    if (param != null) {
                        if (param.isSuccess()) {
                            if (null != param.getResult()) {
                                sceneList = param.getResult();
                                initScene();
                            } else {
                                emptyScene();
                            }
                        } else {
                            if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                                SmartHomeApp.showToast(param.getError().getMessage());
                            } else {
                                SmartHomeApp.showToast("请求失败");
                            }
                            emptyScene();
                        }
                    } else {
                        SmartHomeApp.showToast("请求失败");
                        emptyScene();
                    }
                }
            } else if (url.startsWith(ApiHost.GET_DEVICE_BY_ID_URL.substring(0, ApiHost.GET_DEVICE_BY_ID_URL.lastIndexOf("/")))) {
                if (response != null) {
                    GetDeviceByIdResponse param = (GetDeviceByIdResponse) response.body;
                    if (param != null) {
                        if (param.isSuccess()) {
                            if (null != param.getResult()) {
                                DeviceInfo deviceInfo = param.getResult();
                                if (null == deviceInfo.getDevice_buttons()) {
                                    for (DeviceButtonInfo button : deviceInfo.getDevice_buttons()) {
                                        if (getString(R.string.device_switch).equals(button)) {
                                            UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
                                            String iotMac = userInfo.gateway;
                                            try {
                                                RestRequestApi.sendRedOrder(iotMac, deviceInfo.getMac_address(), Tools.hexStr2Byte(button.getInstruction_code()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (url.startsWith(ApiHost.GET_GW_INFO_URL)) {
                if (response != null) {
                    GetGatewayInfoResponse param = (GetGatewayInfoResponse) response.body;
                    if (param != null) {
                        if (param.isSuccess()) {
                            if (null != param.getResult()) {
                                gwInfo = param.getResult();
                                judgeSecurity();
                            }
                        } else {
                            if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                                SmartHomeApp.showToast(param.getError().getMessage());
                            } else {
                                SmartHomeApp.showToast("请求失败");
                            }
                            emptyScene();
                        }
                    } else {
                        SmartHomeApp.showToast("请求失败");
                        emptyScene();
                    }
                }
            } else if (url.equals(ApiHost.UPDATE_GW_SECURITY_URL)) {
                if (response != null) {
                    UpdateGwSecurityResponse param = (UpdateGwSecurityResponse) response.body;
                    if (param != null) {
                        if (param.isSuccess()) {
                            if(intBFCF == 1) {
                                SmartHomeApp.showToast(R.string.bf_succ_hint_str);
                            }else if(intBFCF == 0){
                                SmartHomeApp.showToast(R.string.cf_succ_hint_str);
                            }
                        } else {
                            if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                                SmartHomeApp.showToast(param.getError().getMessage());
                            } else {
                                SmartHomeApp.showToast("请求失败");
                            }
                            emptyScene();
                        }
                    } else {
                        SmartHomeApp.showToast("请求失败");
                        emptyScene();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        if (getActivity().isFinishing()) {
            return;
        }
        dismissLoadingDialog();
        if (url.equals(ApiHost.GET_SCENE_LIST_URL + "?type=1")) {
            emptyScene();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GjjEventBus.getInstance().unregister(deviceEvent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    @OnClick({ R.id.index_cbf_tv, R.id.index_warning_layout, R.id.index_camera_layout})
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.index_cbf_tv:
                    if (null != gwInfo && 1 == gwInfo.getSecurity_status()) {
                        intBFCF = 0;
                        gwInfo.setSecurity_status(0);
                        RestRequestApi.contorCF(getActivity(), this,originGwMac);
                    }else if (null != gwInfo ) {
                        intBFCF = 1;
                        gwInfo.setSecurity_status(1);
                        RestRequestApi.contorBF(getActivity(), this,originGwMac);
                    }
                    break;
                case R.id.index_warning_layout:
                    PageSwitcher.switchToTopNavPage(getActivity(), WarningFragment.class, null, "", getString(R.string.warning_title), null);
                    break;
                case R.id.index_camera_layout:
                    mEZOpenSDK.openLoginPage();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解除告警
     */
    public void  liftAlarm(){
        MainTaskExecutor.scheduleTaskOnUiThread(300,new Runnable(){
            @Override
            public void run() {
                RestRequestApi.liftAlarm(getActivity(),originGwMac);
            }
        });
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
                    if (deviceState != null) {
                        if (deviceState.dstAddr == null) {
                            return;
                        }
                        byte[] od = deviceState.deviceOD;
                        if (od[0] == 0x03 && od[1] == (byte) 0x05) {//布放撤防
                            if(deviceState.result_data_01 == 0x00){
                                RestRequestApi.updateGwSecurity(getActivity(), intBFCF, IndexFragment.this);
                                if(intBFCF == 0) {
                                    liftAlarm();
                                }
                                judgeSecurity();
                            }else{
                                if(intBFCF == 0){
                                    showToast(R.string.cf_faill_str);
                                }if(intBFCF == 1){
                                    showToast(R.string.bf_faill_str);
                                }
                            }
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onEventMainThread(EventOfOpenCamera eventOfOpenCamera) {
            mEZOpenSDK.openLoginPage();
        }
    };



    private void sceneCtrl(SceneInfo sceneInfo) {
        try {
            if(sceneInfo != null && sceneInfo.getSerial_number() != null){
                UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
                String iotMac = userInfo.gateway;
                RestRequestApi.sendSendSceneOrder(iotMac, Tools.hexStr2Byte(sceneInfo.getSerial_number()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isSuccess;

    /**
     * 初始化天气数据
     *
     * @param responseStr
     */
    public void initWeatherData(String responseStr) throws Exception {
        isSuccess = false;
        WeatherModel weatherModel = null;
        Gson gson = new Gson();
        if (!TextUtils.isEmpty(responseStr)) {
            weatherModel = gson.fromJson(responseStr, WeatherModel.class);
            if (weatherModel != null) {
                if ("200".equals(weatherModel.getRetCode()) && "success".equals(weatherModel.getMsg())) {//获取数据成功
                    isSuccess = true;
                }
            }
        }
        final WeatherModel finalWeatherModel = weatherModel;
        CLog.i("laixj","model--->"+finalWeatherModel.toString());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null) {
                    return;
                }
                if (isSuccess) {
                    try {
                        String temperature = finalWeatherModel.getResult().get(0).getFuture().get(0).getTemperature().replaceAll("C", "");
                        String city = finalWeatherModel.getResult().get(0).getCity();
                        if (!TextUtils.isEmpty(city)) {
                            indexCityTv.setText(city);
                        }
                        if (!TextUtils.isEmpty(temperature)) {
                            indexWeatherTemperatureTv.setText(temperature);
                        }
                        String weather = finalWeatherModel.getResult().get(0).getWeather();
                        if (!TextUtils.isEmpty(weather)) {
                            //"晴,多云,少云,阴,小雨,雨,雷阵雨,中雨,阵雨,零散阵雨,零散雷雨,小雪,雨夹雪,阵雪,霾"
                            String future = "";
                            if ("晴".equals(weather)) {
                                future = "00";
                            } else if ("多云".equals(weather)) {
                                future = "01";
                            } else if ("阴".equals(weather)) {
                                future = "02";
                            } else if ("小雨".equals(weather)) {
                                future = "07";
                            } else if ("雨".equals(weather)) {
                                future = "08";
                            } else if ("雷阵雨".equals(weather)) {
                                future = "04";
                            } else if ("中雨".equals(weather)) {
                                future = "22";
                            } else if ("阵雨".equals(weather)) {
                                future = "11";
                            } else if ("零散阵雨".equals(weather)) {
                                future = "11";
                            } else if ("零散雷雨".equals(weather)) {
                                future = "04";
                            } else if ("小雪".equals(weather)) {
                                future = "14";
                            } else if ("雨夹雪".equals(weather)) {
                                future = "06";
                            } else if ("阵雪".equals(weather)) {
                                future = "10";
                            } else if ("霾".equals(weather)) {
                                future = "29";
                            }
                            String fileName = "a_" + future;
                            int resId = getResources().getIdentifier(fileName, "drawable", getActivity().getPackageName());
                            if (resId != 0) {
                                indexWeatherIm.setImageResource(resId);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    SmartHomeApp.showToast(R.string.get_weather_fail_str);
                }
            }
        });
    }
}
