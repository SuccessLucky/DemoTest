package com.kzksmarthome.SmartHouseYCT.biz.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;
import com.igexin.sdk.PushManager;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.app.SmartHomeApp;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseMainActivity;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfCheckUpgrade;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfLockInfoUpdate;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfNewError;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfSelectIOT;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfSpeech;
import com.kzksmarthome.SmartHouseYCT.biz.login.LoginActivity;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceMainFragment;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DoorAccessInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.ImageInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.home.IndexFragment;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetAllDeviceListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetDoorLockUserListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.responsebody.GetImageListResponse;
import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.SceneMainFragment;
import com.kzksmarthome.SmartHouseYCT.biz.smart.setting.IOTSelectInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.setting.IOTSelectOperationDialog;
import com.kzksmarthome.SmartHouseYCT.biz.smart.usercenter.UserCenterMainFragment;
import com.kzksmarthome.SmartHouseYCT.biz.smart.weather.LocationService;
import com.kzksmarthome.SmartHouseYCT.biz.smart.weather.LocationUtil;
import com.kzksmarthome.SmartHouseYCT.biz.widget.NestRadioGroup;
import com.kzksmarthome.SmartHouseYCT.util.IatSettings;
import com.kzksmarthome.SmartHouseYCT.util.ImageTypeEnums;
import com.kzksmarthome.SmartHouseYCT.util.JsonParser;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.event.EventOfNeedLogin;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.task.ForegroundTaskExecutor;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;
import com.kzksmarthome.common.lib.tcp.TCPMgr;
import com.kzksmarthome.common.lib.tools.DeviceState;
import com.kzksmarthome.common.lib.tools.Tools;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.log.LogService;
import com.kzksmarthome.common.module.net.ApiHost;
import com.kzksmarthome.common.module.stat.BusinessStat;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.util.CLog;

/**
 * @author jack
 * @version V1.0
 * @Title:
 * @Description: 主界面
 * @date 2015年7月19日 下午6:17:01
 */
public class MainActivity extends BaseMainActivity implements RequestCallback, IOTSelectOperationDialog.OnIOTSelectClickListener, DialogInterface.OnDismissListener {
    @BindView(R.id.voice_tab)
    ImageView voiceTab;
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI`
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    int ret = 0; // 函数调用返回值
    private static final String SAVE_INSTANCE_STATE_KEY_TAB_ID = "tabId";
    private SharedPreferences mSharedPreferences;
    private static MainActivity sMainActivity;
    @BindView(R.id.control_tab)
    RadioButton controlTab;
    @BindView(R.id.device_tab)
    RadioButton deviceTab;
    @BindView(R.id.msg_tab)
    RadioButton msgTab;
    @BindView(R.id.seting_tab)
    RadioButton setingTab;

    public RadioButton getControlTab() {
        return controlTab;
    }

    /**
     * 设备列表
     */
    public List<DeviceInfo> mDeviceInfoList;
    @BindView(R.id.network_hint_tv)
    TextView networkHintTv;

    public static MainActivity getMainActivity() {
        return sMainActivity;
    }

    @BindView(R.id.group_tab)
    NestRadioGroup mRadioGroup;

    @BindView(R.id.redTip)
    ImageView mRedTip;

    // private RedPointView mImgPoint = null;
    private boolean mIsBackPressed = false;
    //	private boolean mNeedRefreshNotify = false;
    private Animation mAnimationFirst;
    private Animation mAnimationSecond;
    /**
     * 是否重复播放动画标志
     */
    private boolean mRunFlag;
    /**
     * 是否有新版本
     */
    private boolean mNewVersion = false;
    /**
     * 开锁指纹或密码ID
     */
    private int lockId;

    public boolean hasNewVersion() {
        return mNewVersion;
    }

    /**
     * 是否选择了IOT;
     */
    public boolean mIsSelectIOT;
    /**
     * 选择Iot
     */
    private IOTSelectOperationDialog mIOTSelectOperationDialog;
    protected LocationService locationService;

    /**
     * 首页
     */
    // @OnClick(R.id.news_tab)
    void showIndexTab() {
        replaceFragment(IndexFragment.class);
    }

    /**
     * 设备
     */
    // @OnClick(R.id.progress_tab)
    void showDeviceTab() {
        replaceFragment(DeviceMainFragment.class);
    }

    /**
     * 场景
     */
    // @OnClick(R.id.personal_tab)
    void showSceneTab() {
        replaceFragment(SceneMainFragment.class);
    }

    /**
     * 我的
     */
    // @OnClick(R.id.personal_tab)
    void showSettingTab() {
        replaceFragment(UserCenterMainFragment.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationService.unregisterListener(mListener);
        GjjEventBus.getInstance().unregister(mEventReceiver);
    }

    private Object mEventReceiver = new Object() {
//
//        public void onEventBackgroundThread(EventOfMarkMsgRead event) {
//            mNeedRefreshNotify = true;
//        }
//
//        public void onEventBackgroundThread(EventOfDesignPhotoRead event) {
//            mNeedRefreshNotify = true;
//        }

        @SuppressWarnings("unused")
        public void onEventMainThread(EventOfCheckUpgrade event) {
            mNewVersion = event.hasNew;
        }

        public void onEventBackgroundThread(EventOfNeedLogin event) {
            SmartHomeAppLib.getUserMgr().logOut();
            SmartHomeAppLib.getInstance().getPreferences().edit()
                    .putString(SharePrefConstant.PREFS_KEY_IOT_MAC, null)
                    .commit();
            SmartHomeAppLib.getInstance().getPreferences().edit().putString(SharePrefConstant.PREFS_KEY_IOT_WIFI_MAC, null).commit();
            ApiHost.NETWORK_ISREMOTE = false;
            //退出登录断开网络连接
            TCPMgr.getInstance().closeConnect();
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        public void onEventMainThread(EventOfSelectIOT event) {
            if (isFinishing()) {
                return;
            }
            if (event != null && !Util.isListEmpty(event.getIotSelectInfoArrayList())) {
                selectIOT(event.getIotSelectInfoArrayList());
            }
        }

        public void onEventMainThread(EventOfLockInfoUpdate event) {
            try {
                if (event != null) {
                    DeviceState deviceState = event.deviceState;
                    switch (deviceState.lockOperateType) {
                        case 0x50://指纹开锁上报
                            lockId = deviceState.lockState;
                            RestRequestApi.getDoorUserList(MainActivity.this, 0, Tools.byte2HexStr(deviceState.dstAddr), MainActivity.this);
                            break;
                        case 0x51://密码开锁上报
                            lockId = deviceState.lockState;
                            RestRequestApi.getDoorUserList(MainActivity.this, 0, Tools.byte2HexStr(deviceState.dstAddr), MainActivity.this);
                            break;
                        case 0x60://远程开锁上报
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onEventMainThread(EventOfNewError event) {
            if (event.netError) {
                networkHintTv.setVisibility(View.VISIBLE);
            } else {
                networkHintTv.setVisibility(View.GONE);
            }
        }

    };


    /**
     * 设置红外类型
     */
    public void selectIOT(ArrayList<IOTSelectInfo> dataList) {
        if (mIOTSelectOperationDialog == null) {
            mIOTSelectOperationDialog = new IOTSelectOperationDialog(this, dataList);
        } else if (mIOTSelectOperationDialog.isShowing()) {
            return;
        }
        mIOTSelectOperationDialog.setOnRedDeviceTypeClickListener(this);
        mIOTSelectOperationDialog.setCancelable(false);
        mIOTSelectOperationDialog.setCanceledOnTouchOutside(true);
        mIOTSelectOperationDialog.setOnDismissListener(this);
        mIOTSelectOperationDialog.show();
    }

    @Override
    public void onIOTSelectClick(int position) {
        try {
            mIsSelectIOT = true;
            if (mIOTSelectOperationDialog != null) {
                IOTSelectInfo iotSelectInfo = mIOTSelectOperationDialog.mAdapter.mDataList.get(position);
                SmartHomeAppLib.getInstance().getPreferences().edit().putString(SharePrefConstant.PREFS_KEY_IOT_WIFI_MAC, iotSelectInfo.getIot_wifi_mac()).commit();
                SmartHomeApp.getInstance().connectServerAddress(iotSelectInfo.getHostIp(), iotSelectInfo.getHostPort(), false);
                mIOTSelectOperationDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sMainActivity = this;
        if (isFinishing()) {
            return;
        }
        //获取网关信息
        RestRequestApi.getAllDeviceList(this, this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        GjjEventBus.getInstance().register(mEventReceiver);
        mRadioGroup.setOnCheckedChangeListener(new NestRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestRadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.control_tab:
                        showIndexTab();
                        break;
                    case R.id.device_tab:
                        showDeviceTab();
                        break;
                    case R.id.msg_tab:
                        showSceneTab();
                        break;
                    case R.id.seting_tab:
                        showSettingTab();
                        break;
                }
            }
        });
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(this, mInitListener);

        mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME,
                Activity.MODE_PRIVATE);
        int tabId = 0;
        if (null != savedInstanceState) {
            tabId = savedInstanceState.getInt(SAVE_INSTANCE_STATE_KEY_TAB_ID);
        }
        BusinessStat.getInstance().addStat("launch", String.valueOf(System.currentTimeMillis() / 1000), "tab_" + tabId, null);
        switch (tabId) {
            case R.id.msg_tab:
                msgTab.setChecked(true);
                //mRadioGroup.check(R.id.msg_tab);
                break;
            case R.id.device_tab:
                deviceTab.setChecked(true);
                //mRadioGroup.check(R.id.device_tab);
                break;
            case R.id.seting_tab:
                setingTab.setChecked(true);
                //mRadioGroup.check(R.id.seting_tab);
                break;
            default:
                controlTab.setChecked(true);
                //mRadioGroup.check(R.id.control_tab);
                break;
        }
        Log.d("GetuiPushReceiver", "个推SDK初始化11");
        //个推SDK初始化
        PushManager.getInstance().initialize(this.getApplicationContext());
        Log.d("GetuiPushReceiver", "个推SDK初始化22");
        //TODO:拉取红点
//		requestNoticeSet();
        //TODO:检查更新
//		Request request = ArrowRequestFactory.checkUpgrade();
//		GjjRequestManager.getInstance().execute(request, this);

//		handleIntent(getIntent());
        RestRequestApi.getImageList(this, ImageTypeEnums.DEVICE.getCode(), this);
        RestRequestApi.getImageList(this, ImageTypeEnums.ROOM.getCode(), this);
        RestRequestApi.getImageList(this, ImageTypeEnums.SCENE.getCode(), this);
        locationService = SmartHomeApp.getInstance().locationService;
        locationService.registerListener(mListener);
        startLocation();
        networkHintTv.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


//	public void requestNoticeSet() {
//
//	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		if (mNeedRefreshNotify) {
//			requestNoticeSet();
//		}
//		mNeedRefreshNotify = false;
//	}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_INSTANCE_STATE_KEY_TAB_ID, mRadioGroup.getCheckedRadioButtonId());
    }

    @Override
    public void onBackPressed() {
        if (mIsBackPressed) {
            mIsBackPressed = false;
            finish();
            //SmartHomeApp.getInstance().killApp();
        } else {
            SmartHomeApp.showToast(R.string.quit_toast);
            mIsBackPressed = true;
            MainTaskExecutor.scheduleTaskOnUiThread(2000, new Runnable() {

                @Override
                public void run() {
                    mIsBackPressed = false;
                }
            });
        }
    }

    private void cleanAnimAndGoneView(View view) {

        view.setVisibility(View.GONE);

        if (mAnimationFirst != null) {
            mAnimationFirst.cancel();
            mAnimationFirst = null;
        }

        if (mAnimationSecond != null) {
            mAnimationSecond.cancel();
            mAnimationSecond = null;
        }

        if (null != view.getAnimation()) {
            view.getAnimation().cancel();
            view.clearAnimation();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SmartHomeApp.getInstance().setmCurrentActivity(this);
        //LocationUtil.getInstance().locationService.start();
        ForegroundTaskExecutor.executeTask(new Runnable() {

            @Override
            public void run() {
                L.d("LogService start");
                // 启动日志拉取
                LogService.getInstance().start();
            }
        });
    }

    /**
     * 开始获取位置信息
     */
    public void startLocation() {
        CLog.i("laixj", "开始定位");
        locationService.start();
    }

    /**
     * 停止获取位置信息
     */
    public void stopLocation() {
        CLog.i("laixj", "停止定位");
        //locationService.unregisterListener(mListener);
        locationService.stop();
    }

    /**
     * 定位结果回调，重写onReceiveLocation方法
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                CLog.i("laixj", "定位返回latitude=" + location.getLatitude() + ";longitude=" + location.getLongitude() + ";province=" + location.getProvince() + ";city=" + location.getCity());
                if (!TextUtils.isEmpty(location.getProvince()) && !TextUtils.isEmpty(location.getCity())) {
                    stopLocation();
                    LocationUtil.getInstance().mLatitude = location.getLatitude();
                    LocationUtil.getInstance().mLongitude = location.getLongitude();
                 MainTaskExecutor.runTaskOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         LocationUtil.getInstance().getWeatherData(location.getProvince(), location.getCity());
                     }
                 });
                }
            }
        }

    };

    @Override
    protected void onStop() {
        super.onStop();
        LocationUtil.getInstance().locationService.stop();//关闭定位
        ForegroundTaskExecutor.executeTask(new Runnable() {

            @Override
            public void run() {
                // 延迟三秒后关闭logcat
                L.d("LogService stop");
                LogService.getInstance().delaykill();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    public void handleIntent(Intent intent) {
        /*if (intent == null
                || (intent.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0) {
			return;
		}

		 * String actionType =
		 * intent.getStringExtra(UserPushService.KEY_PUSH_ACTION_TYPE); if
		 * (UserPushService
		 * .VALUE_PUSH_ACTION_VIEW_MSG_DETAIL.equals(actionType)) { Bundle
		 * bundle = new Bundle();
		 * bundle.putBoolean(BaseFragment.FLAT_CLEAR_TOP_FRAGMENT, true);
		 * PageSwitcher.switchToTopNavPage(this, NewsListFragment.class, null,
		 * getString(R.string.tab_feed), getString(R.string.msg_center), null);
		 * 
		 * bundle = new Bundle(); final ArrowPushMsg data =
		 * intent.getParcelableExtra
		 * (UserPushService.KEY_PUSH_DATA_GJJ_PUSH_MSG);
		 * bundle.putParcelable(BundleKeyConstant.KEY_PUSH_MESSAGE, data);
		 * PageSwitcher.switchToTopNavPage(this, NewsDetailFragment.class,
		 * bundle, R.string.back_btn, R.string.msg_detail, 0); }
		 */
    }

    private void saveImgBaseUrl(int category, String baseUrl) {
        SharedPreferences.Editor editor = SmartHomeAppLib.getInstance().getPreferences().edit();
        if (category == ImageTypeEnums.DEVICE.getCode()) {
            editor.putString(SharePrefConstant.PREFS_KEY_DEVICE_BASEURL, baseUrl);
        } else if (category == ImageTypeEnums.ROOM.getCode()) {
            editor.putString(SharePrefConstant.PREFS_KEY_ROOM_BASEURL, baseUrl);
        } else if (category == ImageTypeEnums.SCENE.getCode()) {
            editor.putString(SharePrefConstant.PREFS_KEY_SCENE_BASEURL, baseUrl);
        }
        editor.commit();
    }

    @Override
    public void onFailure(Request request, String url, Exception e) {
        // TODO: 2016/10/13
    }

    @Override
    public void onBizSuccess(ResponseParam response, String url, int from) {
        try {
            if (url.startsWith(ApiHost.GET_IMG_URL.substring(0, ApiHost.GET_IMG_URL.lastIndexOf("=")))) {
                if (response != null) {
                    GetImageListResponse param = (GetImageListResponse) response.body;
                    if (param != null) {
                        if (param.isSuccess()) {
                            if (null != param.getResult()) {
                                Log.d("laixj", "房间图片->" + param.getResult().toString());
                                if (param.getResult().getImages().size() > 0) {
                                    ImageInfo imageInfo = param.getResult().getImages().get(0);
                                    if (url.equals(String.format(ApiHost.GET_IMG_URL, ImageTypeEnums.DEVICE.getCode()))) {
                                        saveImgBaseUrl(ImageTypeEnums.DEVICE.getCode(), imageInfo.getBase_url());
                                    } else if (url.equals(String.format(ApiHost.GET_IMG_URL, ImageTypeEnums.ROOM.getCode()))) {
                                        saveImgBaseUrl(ImageTypeEnums.ROOM.getCode(), imageInfo.getBase_url());
                                    } else if (url.equals(String.format(ApiHost.GET_IMG_URL, ImageTypeEnums.SCENE.getCode()))) {
                                        saveImgBaseUrl(ImageTypeEnums.SCENE.getCode(), imageInfo.getBase_url());
                                    }
                                }
                            }
                        } else {
                            if (null != param.getError() && !TextUtils.isEmpty(param.getError().getMessage())) {
                                SmartHomeApp.showToast(param.getError().getMessage());
                            } else {
                                Log.d("laixj", "房间图片->" + "fail");
                                SmartHomeApp.showToast("拉取图片失败");
                            }
                        }
                    } else {
                        Log.d("laixj", "房间图片->" + "(param == null)");
                        SmartHomeApp.showToast("拉取图片失败");
                    }
                }
            } else if (url.startsWith(ApiHost.GET_LOCK_USER_URL) || url.startsWith(ApiHost.GET_LOCK_USER_MAC_URL)) {
                if (lockId != 0 && response != null) {
                    GetDoorLockUserListResponse rsp = (GetDoorLockUserListResponse) response.body;
                    if (rsp != null && rsp.getResult() != null) {
                        for (DoorAccessInfo doorAccessInfo : rsp.getResult()) {
                            if (lockId == doorAccessInfo.getLock_id()) {
                                //提示用户那个人开锁了
                                if (!TextUtils.isEmpty(doorAccessInfo.getUser_name())) {
                                    SmartHomeApp.showToast(doorAccessInfo.getUser_name());
                                }
                                break;
                            }
                        }
                    }
                }
            } else if (url.startsWith(ApiHost.GET_ALL_DEVICE_URL)) {//获取网关列表
                GetAllDeviceListResponse rsp = (GetAllDeviceListResponse) response.body;
                if (rsp != null) {
                    mDeviceInfoList = rsp.getResult();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBizFail(ResponseParam response, String url, int from) {
        // TODO: 2016/10/13
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (!mIsSelectIOT) {
            SmartHomeApp.showToast("连接远程服务器！");
            String iot_mac = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_IOT_WIFI_MAC, null);
            if (!TextUtils.isEmpty(iot_mac)) {
                try {
                    SmartHomeApp.getInstance().connectServerAddress(ApiHost.FORWARD_SERVER_HOST, ApiHost.FORWARD_SERVER_PORT, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        mIsSelectIOT = false;
    }

    /**
     * 开始听写
     */
    public void startSpeech() {
        // 移动数据分析，收集开始听写事件
        FlowerCollector.onEvent(this, "iat_recognize");
        mIatResults.clear();
        // 设置参数
        setParam();
        boolean isShowDialog = mSharedPreferences.getBoolean(
                getString(R.string.pref_key_iat_show), true);
        if (isShowDialog) {
            // 显示听写对话框
            mIatDialog.setListener(mRecognizerDialogListener);
            mIatDialog.show();
            showTip(getString(R.string.text_begin));
        } else {
            // 不显示听写对话框
            ret = mIat.startListening(mRecognizerListener);
            if (ret != ErrorCode.SUCCESS) {
                showTip("听写失败,错误码：" + ret);
            } else {
                showTip(getString(R.string.text_begin));
            }
        }
    }


    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }


    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }

    };


    private String TAG = "MainActivity";
    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            printResult(results);

            if (isLast) {
                // TODO 最后的结果
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据：" + data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 显示弹窗
     *
     * @param str
     */
    private void showTip(final String str) {
        SmartHomeApp.showToast(str);
    }

    /**
     * 解析数据
     *
     * @param results
     */
    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        EventOfSpeech event = new EventOfSpeech();
        event.setSpeechStr(resultBuffer.toString());
        GjjEventBus.getInstance().post(event);
    }

    @OnClick(R.id.voice_tab)
    public void onViewClicked() {
        startSpeech();
        msgTab.setChecked(true);
    }
}
