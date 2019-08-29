package com.kzksmarthome.SmartHouseYCT.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.SpeechUtility;
import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.base.PageSwitcher;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfActivityStatus;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfLockInfoUpdate;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfNewError;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfSelectIOT;
import com.kzksmarthome.SmartHouseYCT.biz.login.LoginActivity;
import com.kzksmarthome.SmartHouseYCT.biz.main.MainActivity;
import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.home.BrandType;
import com.kzksmarthome.SmartHouseYCT.biz.smart.http.RestRequestApi;
import com.kzksmarthome.SmartHouseYCT.biz.smart.setting.IOTSelectInfo;
import com.kzksmarthome.SmartHouseYCT.biz.smart.setting.SettingIOTActivity;
import com.kzksmarthome.SmartHouseYCT.biz.smart.setting.SettingIOTRadeyActivity;
import com.kzksmarthome.SmartHouseYCT.biz.smart.weather.LocationService;
import com.kzksmarthome.SmartHouseYCT.biz.smart.weather.LocationUtil;
import com.kzksmarthome.SmartHouseYCT.biz.splash.SplashActivity;
import com.kzksmarthome.SmartHouseYCT.util.ShortCutUtil;
import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.biz.widget.ConfirmDialog;
import com.kzksmarthome.common.event.EventOfEasyLinkSearchEnd;
import com.kzksmarthome.common.event.EventOfIOTSettingState;
import com.kzksmarthome.common.event.EventOfLaunchLogin;
import com.kzksmarthome.common.event.EventOfRegisterRotme;
import com.kzksmarthome.common.event.EventOfSocket;
import com.kzksmarthome.common.event.EventOfTcpResult;
import com.kzksmarthome.common.lib.easylink.EasyLinkSearchTool;
import com.kzksmarthome.common.lib.easylink.bean.GatewayWifi;
import com.kzksmarthome.common.lib.easylink.data.TempData;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.ipc.ForeProcMessenger;
import com.kzksmarthome.common.lib.network.NetworkState;
import com.kzksmarthome.common.lib.network.NetworkStateMgr;
import com.kzksmarthome.common.lib.task.BackgroundTaskExecutor;
import com.kzksmarthome.common.lib.task.ForegroundTaskExecutor;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;
import com.kzksmarthome.common.lib.tcp.SocketRequest;
import com.kzksmarthome.common.lib.tcp.TCPMgr;
import com.kzksmarthome.common.lib.tools.BusinessTool;
import com.kzksmarthome.common.lib.tools.DeviceState;
import com.kzksmarthome.common.lib.tools.IOTConfig;
import com.kzksmarthome.common.lib.tools.Tools;
import com.kzksmarthome.common.lib.util.AndroidUtil;
import com.kzksmarthome.common.lib.util.FileUtil;
import com.kzksmarthome.common.lib.util.LooperHook;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.log.LogService;
import com.kzksmarthome.common.module.net.ApiHost;
import com.kzksmarthome.common.module.stat.StatService;
import com.kzksmarthome.common.module.user.UserMgr;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.bugly.crashreport.CrashReport.UserStrategy;
import com.videogo.openapi.EZOpenSDK;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import in.srain.cube.Cube;
import in.srain.cube.app.lifecycle.IComponentContainer;
import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;
import in.srain.cube.image.ImageProvider;
import in.srain.cube.image.iface.ImageLoadHandler;
import in.srain.cube.image.iface.ImageTaskExecutor;
import in.srain.cube.image.impl.DefaultImageLoadHandler;
import in.srain.cube.image.impl.DefaultImageResizer;
import in.srain.cube.util.Debug;

/**
 * 类/接口注释
 *
 * @author panrq
 * @createDate Dec 29, 2014
 */
@SuppressLint("NewApi")
public class SmartHomeApp extends Application {
    private static final String TAG = "SmartHomeApp";
    private static SmartHomeApp mApp;

    private boolean mIsInitialized = false;
    private Toast mToast;
    private int toastY = 0;// toast默认显示高度
    private BroadcastReceiver mScreenReceiver;

    /**
     * 确认对话框
     */
    private ConfirmDialog mConfirmDialog;

    public static SmartHomeApp getInstance() {
        return mApp;
    }

    private boolean mIsForeProcess = false;
    private boolean mIsBackProcess = false;
    public static String APP_KEY = "e3cd95f361cc4f3ea2965a5c0007e411";
    // private LockPatternUtils mLockPatternUtils;
    public int screenWidth;
    public int screenHeight;
    public LocationService locationService;
    /**
     * 当前Activity
     */
    private Activity mCurrentActivity;

    /**
     * 发送网数据获取数据
     */
    private SocketRequest socketRequest;


    public Activity getmCurrentActivity() {
        return mCurrentActivity;
    }

    public void setmCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }

    /**
     * 创建全局变量
     * 全局变量一般都比较倾向于创建一个单独的数据类文件，并使用static静态变量
     *
     * 这里使用了在Application中添加数据的方法实现全局变量
     * 注意在AndroidManifest.xml中的Application节点添加android:name=".MyApplication"属性
     *
     */
    private WindowManager.LayoutParams wmParams=new WindowManager.LayoutParams();


    public WindowManager.LayoutParams getMywmParams(){
        return wmParams;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        checkMainProcess();
        SmartHomeAppLib.onCreate(mApp, mIsForeProcess, mIsBackProcess);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        if (mIsForeProcess || mIsBackProcess) {
            SmartHomeAppLib.attachUserMgr(new UserMgr());
        }

        if (mIsBackProcess) {
            String versionNow = AndroidUtil.getVersionName(this);
            String versionOld = SmartHomeAppLib.getInstance().getPreferences()
                    .getString(SharePrefConstant.PREFS_KEY_APP_VERSION, null);
            if (!versionNow.equals(versionOld)) {
                SmartHomeAppLib.getInstance().getPreferences().edit()
                        .putString(SharePrefConstant.PREFS_KEY_APP_VERSION, versionNow).commit();
            }
        }
        if (mIsForeProcess) {
            GjjEventBus.getInstance().register(smartAppEvent);
            asyncForeProcInitializations();
            foreProcInitializations();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                ActivityLifecycleCallbacks activityLifeCallback = getActivityLifeCallbacks();
                if (activityLifeCallback != null) {
                    registerActivityLifecycleCallbacks(activityLifeCallback);
                }
            }
        } else if (mIsBackProcess) {
            asyncBackProcInitializations();
            backProcInitializations();
        }
        initBaiduLocation();
        //Activity生命周期监听
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
    }

    private HashSet<Activity> activityArrayList = new HashSet<Activity>();

    /**
     * 使用这个来监听activity生命周期，判断app是否进入前后台，只针对4.0及以上手机系统
     */
    private Application.ActivityLifecycleCallbacks getActivityLifeCallbacks() {
        Application.ActivityLifecycleCallbacks activityLifeCallback = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            activityLifeCallback = new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                }

                @Override
                public void onActivityStarted(Activity activity) {
                    activityArrayList.add(activity);
                }

                @Override
                public void onActivityResumed(Activity activity) {
                    //检测网络
                    if (!(activity instanceof LoginActivity) && !(activity instanceof SplashActivity) && !(activity instanceof SettingIOTRadeyActivity) && !(activity instanceof SettingIOTActivity)) {
                        if(SmartHomeAppLib.getUserMgr().isLogin()) {
                            if (activityArrayList.size() == 1) {
                                GjjEventBus.getInstance().post(NetworkStateMgr.getInstance().getNetworkState(), true);
                            } else if (!TCPMgr.getInstance().isConnect()) {
                                EventOfNewError eventOfNewError = new EventOfNewError();
                                eventOfNewError.netError = true;
                                GjjEventBus.getInstance().post(eventOfNewError);
                            }
                        }
                    }
                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {
                    activityArrayList.remove(activity);
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {

                }
            };
        }
        return activityLifeCallback;
    }

    /**
     * 初始化定位
     */
    private void initBaiduLocation() {
        locationService = new LocationService(getApplicationContext());
        //SDKInitializer.initialize(getApplicationContext());
    }

    // public LockPatternUtils getLockPatternUtils() {
    // if (mLockPatternUtils == null) {
    // mLockPatternUtils = new LockPatternUtils(this);
    // }
    // return mLockPatternUtils;
    // }

    public boolean isInitialized() {
        return mIsInitialized;
    }

    /**
     * 启动严苛模式
     */
    private void initStrictMode() {
        if (L.isDebugMode() && VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            builder.detectLeakedSqlLiteObjects();
            if (VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                builder.detectAll();
            }
            builder.penaltyLog();

            StrictMode.VmPolicy vmp = builder.build();
            StrictMode.setVmPolicy(vmp);
        }
    }

    /**
     * 后台经常启动闹钟、push服务
     */
    private void backProcInitializations() {
        // GjjAlarmMgr.getInstance().startAlarm();
        // PushService.startService(this);
    }

    /**
     * 后台进程初始化错误日志等
     */
    private void asyncBackProcInitializations() {
        ForegroundTaskExecutor.executeTask(new Runnable() {

            @Override
            public void run() {
                initStrictMode();
                initBugly();
                NetworkStateMgr.getInstance();
                StatService.getInstance().startStat();

                LooperHook lh = LooperHook.getInstance();
                lh.startHook();

                mIsInitialized = true;
            }
        });
    }

    /**
     * 初始化错误统计、网络状态监控、打开严苛模式、开启进程拉取等
     */
    private void asyncForeProcInitializations() {
        ForegroundTaskExecutor.executeTask(new Runnable() {

            @Override
            public void run() {
                String srcAddr = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_IOT_MAC, null);
                try {
                    IOTConfig.SRCADDR = srcAddr == null ? null : Tools.hexStr2Bytes(srcAddr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // getLockPatternUtils();
                mIsInitialized = true;

                initBugly();
                StatService.getInstance().startStat();
                NetworkStateMgr.getInstance();
                initStrictMode();
                if (L.isDebugMode()) {
                    FragmentManager.enableDebugLogging(true);
                }

                LooperHook lh = LooperHook.getInstance();
                lh.startHook();
                // 创建桌面图标
                boolean created = SmartHomeAppLib.getInstance().getPreferences()
                        .getBoolean(SharePrefConstant.PREFS_KEY_CREATED_SHORT_CUT, false);
                if (!created
                        && ShortCutUtil.checkShortcutIsExist(SmartHomeApp.this, getString(R.string.app_name),
                        getPackageName()) == 0) {
                    ShortCutUtil.createLauncherShortCut(SmartHomeApp.this, getString(R.string.app_name),
                            R.drawable.ic_launcher);
                    SmartHomeAppLib.getInstance().getPreferences().edit()
                            .putBoolean(SharePrefConstant.PREFS_KEY_CREATED_SHORT_CUT, true).commit();
                }


                mScreenReceiver = new BroadcastReceiver() {
                    private String action = null;

                    @Override
                    public void onReceive(Context context, Intent intent) {
                        action = intent.getAction();
                        if (Intent.ACTION_SCREEN_ON.equals(action)) {

                        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {

                        }
                    }
                };
                registerReceiver(mScreenReceiver, createScreenFilter());
                GjjEventBus.getInstance().register(readMacEvent);
                //getIOTInfo(false);
                initBrandType();
            }
        });
    }

    //初始化网络连接
    public Object readMacEvent = new Object() {
        public void onEventBackgroundThread(EventOfTcpResult eventOfTcpResult) {
            try {
                if (eventOfTcpResult != null) {
                    DeviceState deviceState = eventOfTcpResult.deviceState;
                    if (deviceState != null) {
                        byte[] srcAddr = deviceState.srcAddr;
                        if (!deviceState.remoteAddFlag) {
                            MainTaskExecutor.runTaskOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showToast("远程连接失败！");
                                }
                            });
                        }
                        if (srcAddr != null && srcAddr.length > 0) {
                            IOTConfig.SRCADDR = deviceState.srcAddr;
                            //保存网关Mac地址(把byte转成了字符串，使用是记得转回来，方法在tools中)
                            SmartHomeAppLib.getInstance().getPreferences().edit()
                                    .putString(SharePrefConstant.PREFS_KEY_IOT_MAC, Tools.byte2HexStr(srcAddr))
                                    .commit();
                        } else {
                            if (!checkIsAddDevice(deviceState.dstAddr)) {
                                return;
                            }
                            byte[] od = deviceState.deviceOD;
                            if (od == null) {
                                return;
                            }
                            if (od[0] == 0x0f && od[1] == (byte) 0xbe) {// 多用途休眠设备控制器4030
                                switch (deviceState.deviceType) {
                                    case 0x02:
                                        if (deviceState.deviceProduct == 0x02) {//指纹锁
                                            EventOfLockInfoUpdate eventOfLockInfoUpdate = new EventOfLockInfoUpdate();
                                            eventOfLockInfoUpdate.deviceState = deviceState;
                                            GjjEventBus.getInstance().post(eventOfLockInfoUpdate);
                                        }
                                        break;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 判断设备是否添加到网关了
         * @param address
         */
        public boolean checkIsAddDevice(byte[] address) throws Exception {
            if (address != null && mCurrentActivity != null && mCurrentActivity instanceof MainActivity) {
                String deviceAddress = Tools.byte2HexStr(address);
                List<DeviceInfo> deviceInfoList = ((MainActivity) mCurrentActivity).mDeviceInfoList;
                if (deviceInfoList != null) {
                    for (DeviceInfo deviceInfo : deviceInfoList) {
                        if (deviceAddress.equals(deviceInfo.getMac_address())) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public void onEventBackgroundThread(EventOfRegisterRotme event) {
            try {
                L.i("Request# connect to register rotme on smarthomeapp");
                SocketRequest socketRequest = new SocketRequest();
                final String iot_mac = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_IOT_MAC, null);
                if (iot_mac != null) {
                    socketRequest.requestData = BusinessTool.getInstance().sendRemoteOrder(Tools.hexStr2Bytes(iot_mac));
                } else {
                    return;
                }
                socketRequest.callbackId = System.currentTimeMillis();
                TCPMgr.getInstance().sendRequest(socketRequest);
                BackgroundTaskExecutor.scheduleTask(1000, new Runnable() {
                    @Override
                    public void run() {
                        RestRequestApi.setIOTTime(iot_mac);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 弹通知
     *
     * @param msg
     */
    public void showNotic(String msg) {
        ///// 第一步：获取NotificationManager
        NotificationManager nm = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        ///// 第二步：定义Notification
        Intent intent = new Intent(this, MainActivity.class);
        //PendingIntent是待执行的Intent
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("告警信息")
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_launcher).setContentIntent(pi)
                .build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        //   //设置在通知发出的时候的音频
        //Uri soundUri = Uri.fromFile(new File("/system/media/audio/ringtones/Basic_tone.ogg"));
        //notification.sound = soundUri;

        //设置手机震动
        //第一个，0表示手机静止的时长，第二个，1000表示手机震动的时长
        //第三个，1000表示手机震动的时长，第四个，1000表示手机震动的时长
        //此处表示手机先震动1秒，然后静止1秒，然后再震动1秒
        //if (SmartHomeAppLib.getInstance().getPreferences().getBoolean(SharePrefConstant.KEY_VIBRATE_FLAG, false)) {
        /*long[] vibrates = {0, 1000, 1000, 1000};
        notification.vibrate = vibrates;*/
        //}
        //设置LED指示灯的闪烁
        //ledARGB设置颜色
        //ledOnMS指定LED灯亮起的时间
        //ledOffMS指定LED灯暗去的时间
        //flags用于指定通知的行为
//   notification.ledARGB = Color.GREEN;
//   notification.ledOnMS = 1000;
//   notification.ledOffMS = 1000;
//   notification.flags = Notification.FLAG_SHOW_LIGHTS;

        //如果不想进行那么多繁杂的这只，可以直接使用通知的默认效果
        //默认设置了声音，震动和灯光
        notification.defaults = Notification.DEFAULT_ALL;

        /////第三步：启动通知栏，第一个参数是一个通知的唯一标识
        nm.notify(0, notification);

        //关闭通知
        //nm.cancel(0);
    }


    public static IntentFilter createScreenFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        return filter;
    }



    private void initImageLoader() {
        // Fresco.initialize(this);
        // cube lib init
        Debug.DEBUG_CACHE = Debug.DEBUG_IMAGE = Debug.DEBUG_REQUEST = L.isDebugMode();
        File imageCache = FileUtil.getCacheDirectory(this, true);
        /*
         * DefaultImageLoadHandler loadHandler = new
		 * DefaultImageLoadHandler(mApp);
		 * loadHandler.setErrorResources(R.drawable.default_loading);
		 * loadHandler.setLoadingResources(R.drawable.default_loading);
		 * ImageLoaderFactory.setDefaultImageLoadHandler(loadHandler);
		 */
        ImageLoaderFactory.customizeCache(this, ImageLoaderFactory.getDefaultMemoryCacheSizeInKB(),
                imageCache.getAbsolutePath(), ImageLoaderFactory.DEFAULT_FILE_CACHE_SIZE_IN_KB);
        ImageLoaderFactory.setDefaultImageResizer(DefaultImageResizer.getInstance());
        ImageLoaderFactory.create(this);
        Cube.onCreate(this);
    }

    public ImageLoader getImageLoader(IComponentContainer attachTo, int defaultImgResId) {
        return getImageLoader(attachTo, defaultImgResId, null, null);
    }

    public ImageLoader getImageLoader(IComponentContainer attachTo, int defaultImgResId, boolean isCircle) {
        return getImageLoader(attachTo, defaultImgResId, null, null, isCircle);
    }

    public ImageLoader getImageLoader(IComponentContainer attachTo, int defaultImgResId,
                                      ImageTaskExecutor imageTaskExecutor) {
        return getImageLoader(attachTo, defaultImgResId, null, imageTaskExecutor);
    }

    public ImageLoader getImageLoader(IComponentContainer attachTo, int defaultImgResId, ImageProvider imageProvider,
                                      ImageTaskExecutor imageTaskExecutor) {
        return getImageLoader(attachTo, defaultImgResId, imageProvider, imageTaskExecutor, false);
    }

    public ImageLoader getImageLoader(IComponentContainer attachTo, int defaultImgResId, ImageProvider imageProvider,
                                      ImageTaskExecutor imageTaskExecutor, boolean isCircle) {
        ImageLoader loader = ImageLoaderFactory.create(SmartHomeApp.getInstance(), imageTaskExecutor);
        if (attachTo != null) {
            loader.tryToAttachToContainer(attachTo);
        }
        if (defaultImgResId != 0) {
            ImageLoadHandler handler = loader.getImageLoadHandler();
            DefaultImageLoadHandler defaultHandler = null;
            if (handler != null && handler instanceof DefaultImageLoadHandler) {
                defaultHandler = (DefaultImageLoadHandler) handler;
            } else {
                defaultHandler = new DefaultImageLoadHandler(this);
            }
            defaultHandler.setErrorResources(defaultImgResId);
            defaultHandler.setLoadingResources(defaultImgResId);
            defaultHandler.setCircleImage(isCircle);
        }
        return loader;
    }

    /**
     * 初始化ImageLoader、？
     */
    private void foreProcInitializations() {
        ForeProcMessenger.getInstance();
        initImageLoader();
        LocationUtil.getInstance().init();
        EZOpenSDK.initLib(this, APP_KEY, "");
    }

    public boolean isMainProcess() {
        return mIsForeProcess;
    }

    private void checkMainProcess() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infoList = activityManager.getRunningAppProcesses();

        mIsForeProcess = false;
        mIsBackProcess = false;
        String pkgName = getPackageName();
        String backProcessName = pkgName + ":core";
        for (ActivityManager.RunningAppProcessInfo appProcess : infoList) {
            if (appProcess.processName.equals(pkgName)) {
                if (appProcess.pid == android.os.Process.myPid()) {
                    mIsForeProcess = true;
                    break;
                }
            } else if (appProcess.processName.equals(backProcessName)) {
                if (appProcess.pid == android.os.Process.myPid()) {
                    mIsBackProcess = true;
                    break;
                }
            }
        }
    }

    /**
     * bugly异常报告初始化
     */
    private void initBugly() {
        UserStrategy strategy = new UserStrategy(this); // App的策略Bean
        strategy.setAppChannel(getString(R.string.ch)); // 设置渠道
        strategy.setAppReportDelay(5000);// 设置SDK处理延时，毫秒
        StringBuilder sb = Util.getThreadSafeStringBuilder();
        Context ctx = SmartHomeApp.getInstance();
        sb.append("kzksmarthome").append('_').append(AndroidUtil.getVersionName(ctx)).append('_')
                .append(AndroidUtil.getVersionCode(ctx));
        sb.append('_').append(AndroidUtil.getBuild(ctx));
        strategy.setAppVersion(sb.toString()); // appName_versionName_versionCode_build
        String appId;
        if (L.isDebugMode()) {
            appId = "73c993283e";
        } else {
            appId = "73c993283e";
        } // Bugly AppId
        CrashReport.initCrashReport(this, appId, L.isDebugMode(), strategy);
    }

    /**
     * app响应事件
     */
    public Object smartAppEvent = new Object() {

        public void onEventMainThread(EventOfLaunchLogin event) {
            L.d("SmartHomeApp userInfo change event %s", event);
            PageSwitcher.switchToLoginActivity(null);
        }


        public void onEventBackgroundThread(final EventOfActivityStatus event) {
            L.d("EventOfActivityStatus %s", event);
            if (event.getStatus() == EventOfActivityStatus.STATUS_RESUME) {

            } else if (event.getStatus() == EventOfActivityStatus.STATUS_PAUSE) {

            }
        }

        public void onEventBackgroundThread(NetworkState networkState) {
            //提示网络切换重连
            EventOfNewError eventOfNewError = new EventOfNewError();
            eventOfNewError.netError = true;
            GjjEventBus.getInstance().post(eventOfNewError);
            if (networkState != null && !networkState.equals(NetworkState.WIFI)) {//判断为非wifi链接远程
                connectRemote();
            } else {//链接本地
                connectLocal();
            }
        }

        public void onEventMainThread(EventOfSocket event) {
            if (event != null) {//是否连接网关失败，弹出对话框提示用户
                if (event.ismIsConnectFail()) {
                    if (NetworkStateMgr.getInstance().isNetworkAvailable()) {//判断问了是否可用
                        showConfirDialog();
                    } else {
                        showToast(R.string.connect_net_work_str);
                    }
                } else {
                    //连接成功，发送读取网关命令或远程注册信息
                    TCPMgr.getInstance().sendRequest(socketRequest);
                    L.e("网关连接成功发送数据------------");
                    EventOfNewError eventOfNewError = new EventOfNewError();
                    eventOfNewError.netError = false;
                    GjjEventBus.getInstance().post(eventOfNewError);
                    String iot_mac = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_IOT_MAC, null);
                    if(iot_mac != null) {
                        RestRequestApi.setIOTTime(iot_mac);
                    }
                }
            }
        }
        public void onEventMainThread(EventOfEasyLinkSearchEnd eventOfEasyLinkSearchEnd){
            if(eventOfEasyLinkSearchEnd != null){
                try {
                    getIOTInfoBase(eventOfEasyLinkSearchEnd.getGatewayWifiList());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 显示确定对话框
     */
    private void showConfirDialog() {
        if (!(mCurrentActivity != null && mCurrentActivity.isFinishing())) {
            return;
        }
        if (mConfirmDialog == null) {
            mConfirmDialog = new ConfirmDialog(mCurrentActivity);
        } else if (mConfirmDialog.isShowing()) {//判断弹出框是否存在
            return;
        }
        mConfirmDialog.setContent(getResources().getString(R.string.socket_fial_go_remote));

        mConfirmDialog.setConfirmClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectRemote();
                mConfirmDialog.dismiss();
            }
        });
        mConfirmDialog.setCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mConfirmDialog != null) {
                    mConfirmDialog.dismiss();
                }
            }
        });
        mConfirmDialog.show();
    }

    /**
     * 链接远程
     */
    private void connectRemote() {
        TCPMgr.getInstance().closeConnect();
        ApiHost.NETWORK_ISREMOTE = true;
        ApiHost.TCP_IOT_HOST = ApiHost.FORWARD_SERVER_HOST;
        ApiHost.TCP_IOT_PORT = Integer.valueOf(ApiHost.FORWARD_SERVER_PORT);
        TCPMgr.getInstance().retryConnectOpen();
    }

    /**
     * 链接局域网
     */
    private void connectLocal() {
        TCPMgr.getInstance().closeConnect();
        ApiHost.NETWORK_ISREMOTE = false;
        //获取IOT信息
        if (SmartHomeApp.getInstance().chekcGWINFO()) {
            SmartHomeApp.getInstance().getIOTInfoNew(false, 5);
        }
    }

    /**
     * toast 文字
     *
     * @param textResId
     */
    public static void showToast(int textResId) {
        showToast(mApp.getString(textResId), 0);
    }

    /**
     * toast 文字带图片
     *
     * @param textResId
     * @param iconResId
     */
    public static void showToast(int textResId, int iconResId) {
        showToast(mApp.getString(textResId), iconResId);
    }

    /**
     * toast 文字
     *
     * @param text
     */
    public static void showToast(String text) {
        showToast(text, 0);
    }

    /**
     * toast 文字带图片
     *
     * @param text
     * @param iconResId
     */
    public static void showToast(String text, int iconResId) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        TextView tv;
        if (null == mApp.mToast) {
            mApp.mToast = Toast.makeText(SmartHomeApp.getInstance(), text, Toast.LENGTH_SHORT);
            LayoutInflater inflate = (LayoutInflater) mApp.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            tv = (TextView) inflate.inflate(R.layout.dialog_toast, null);
            mApp.mToast.setView(tv);
            mApp.toastY = mApp.mToast.getYOffset();
        }
        tv = (TextView) mApp.mToast.getView();
        tv.setText(text);
        Drawable icon = null;
        if (iconResId > 0) {
            try {
                icon = mApp.getResources().getDrawable(iconResId);
            } catch (Exception e) {
                L.e(e);
            }
            mApp.mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mApp.mToast.setGravity(Gravity.BOTTOM, 0, mApp.toastY);
        }
        tv.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
        mApp.mToast.show();
    }

    /**
     * 取消toast
     */
    public static void cancelToast() {
        if (null != mApp.mToast) {
            mApp.mToast.cancel();
        }
    }

    /**
     * 获取包名
     *
     * @return
     */
    public static String getGjjPackageName() {
        return mApp.getPackageName();
    }

    /**
     * 杀死APP
     */
    public void killApp() {
        cancelToast();
        unregisterReceiver(mScreenReceiver);
        ForegroundTaskExecutor.executeTask(new Runnable() {
            @Override
            public void run() {
                LogService.getInstance().killLogcatProc();
                StatService.getInstance().stopImmediately();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                }
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            L.d(topActivity.getPackageName());
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public int searchCount = 0;

    /**
     * 检测是否要检索网关
     *
     * @return
     */
    public boolean chekcGWINFO() {
        com.kzksmarthome.common.module.user.UserInfo userInfo = SmartHomeAppLib.getUserMgr().getUser();
        if (userInfo != null && userInfo.gateway != null) {
            return true;
        }
        return false;
    }

    /**
     * 获取网关信息
     *
     * @param isRegister 添加网关
     * @param count
     */
    public void getIOTInfoNew(Boolean isRegister, int count) {
        if (!isRegister) {
            if (!SmartHomeAppLib.getUserMgr().isLogin() || TCPMgr.getInstance().isConnect()) {//登录成功和连接失败的时候才获取网关信息
                return;
            }
        }
        EasyLinkSearchTool.getInstance().startSearch(count);
    }


    /**
     * 网关信息处理
     *
     * @throws Exception
     */
    private void getIOTInfoBase(List<GatewayWifi> gatewayWifiList) throws Exception {
        EventOfIOTSettingState eventOfIOTSettingState = new EventOfIOTSettingState();
        if (gatewayWifiList != null && gatewayWifiList.size() > 0) {//找到
            //把有效的网关信息解析出来
            ArrayList<IOTSelectInfo> iotSelectInfoArrayList = new ArrayList<IOTSelectInfo>();
            for (GatewayWifi gatewayWifi : gatewayWifiList) {
                IOTSelectInfo iotSelectInfo = new IOTSelectInfo();
                iotSelectInfo.setIot_wifi_mac(gatewayWifi.getMac());
                String hostIp = gatewayWifi.getIp();
                String hostPort = TempData.PORT + "";
                iotSelectInfo.setHostIp(hostIp);
                iotSelectInfo.setHostPort(hostPort);
                iotSelectInfoArrayList.add(iotSelectInfo);
            }
            String iot_wifi_mac = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_IOT_WIFI_MAC, null);
            if (iot_wifi_mac == null && iotSelectInfoArrayList.size() == 1) {
                IOTSelectInfo iotSelectInfo = iotSelectInfoArrayList.get(0);
                SmartHomeAppLib.getInstance().getPreferences().edit().putString(SharePrefConstant.PREFS_KEY_IOT_WIFI_MAC, iotSelectInfo.getIot_wifi_mac()).commit();
                connectServerAddress(iotSelectInfo.getHostIp(), iotSelectInfo.getHostPort(), false);
            } else {
                EventOfSelectIOT eventOfSelectIOT = new EventOfSelectIOT();
                eventOfSelectIOT.setIotSelectInfoArrayList(iotSelectInfoArrayList);
                if (iot_wifi_mac == null) {
                    GjjEventBus.getInstance().post(eventOfSelectIOT);
                } else {
                    boolean isCheckIOT = false; //是否检测到对应的网关
                    for (IOTSelectInfo iotSelectInfo : iotSelectInfoArrayList) {
                        if (iot_wifi_mac.equals(iotSelectInfo.getIot_wifi_mac())) {
                            isCheckIOT = true;
                            connectServerAddress(iotSelectInfo.getHostIp(), iotSelectInfo.getHostPort(), false);
                            break;
                        }
                    }
                    if (!isCheckIOT) {
                        GjjEventBus.getInstance().post(eventOfSelectIOT);
                    }
                }
            }
        } else {
            String iot_wifi_mac = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_IOT_WIFI_MAC, null);
            if (iot_wifi_mac != null) {
                connectServerAddress(ApiHost.FORWARD_SERVER_HOST, ApiHost.FORWARD_SERVER_PORT, true);
            }else{
                GjjEventBus.getInstance().post(eventOfIOTSettingState);
            }
        }
    }


    /**
     * 保存网关地址
     *
     * @param hostIp
     * @param hostPort
     * @param isRemote 是否为连接远程服务器
     * @throws Exception
     */
    public void connectServerAddress(String hostIp, String hostPort, boolean isRemote) throws Exception {
        ApiHost.NETWORK_ISREMOTE = isRemote;
        ApiHost.TCP_IOT_HOST = hostIp;
        ApiHost.TCP_IOT_PORT = Integer.valueOf(hostPort);
        //开始建立TCP链接并读取Mac地址
        socketRequest = new SocketRequest();
        if (!isRemote) {
            socketRequest.requestData = BusinessTool.getInstance().readMAC();
        } else {
            String iot_mac = SmartHomeAppLib.getInstance().getPreferences().getString(SharePrefConstant.PREFS_KEY_IOT_MAC, null);
            if (iot_mac != null) {
                socketRequest.requestData = BusinessTool.getInstance().sendRemoteOrder(Tools.hexStr2Bytes(iot_mac));
            } else {
                return;
            }
        }
        socketRequest.callbackId = System.currentTimeMillis();
        TCPMgr.getInstance().retryConnectOpen();
    }

    /**
     * 空调类型列表
     */
    public static ArrayList<BrandType> mBrandTypeArrayList;

    /**
     * 初始化空调类型表
     */
    public void initBrandType() {
        mBrandTypeArrayList = new ArrayList<BrandType>(100);
        InputStream is = null;
        BufferedReader bufferedReader = null;
        try {
            is = getAssets().open("brandtype");
            if (is != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(is));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line != null) {
                        BrandType brandType = new BrandType();
                        String[] brandType_array_M = line.split("---");
                        if (brandType_array_M != null && brandType_array_M.length > 1) {
                            brandType.brandName = brandType_array_M[0];
                            int index_dh = brandType_array_M[1].indexOf(",");
                            int index_sg = brandType_array_M[1].indexOf("-");
                            if (index_dh == -1 && index_sg == -1) {
                                brandType.brandTypeArray = new Integer[]{Integer.valueOf(brandType_array_M[1])};
                            } else if (index_dh == -1) {
                                String[] brandType_array_C = brandType_array_M[1].split("-");
                                if (brandType_array_C != null && brandType_array_C.length > 1) {
                                    brandType.startNum = Integer.valueOf(brandType_array_C[0]);
                                    brandType.endNum = Integer.valueOf(brandType_array_C[1]);
                                }
                            } else if (index_sg == -1) {
                                String[] brandType_array_C1 = brandType_array_M[1].split(",");
                                if (brandType_array_C1 != null && brandType_array_C1.length > 0) {
                                    brandType.brandTypeArray = new Integer[brandType_array_C1.length];
                                    for (int i = 0; i < brandType_array_C1.length; i++) {
                                        brandType.brandTypeArray[i] = Integer.valueOf(brandType_array_C1[i]);
                                    }
                                }
                            } else {
                                String[] brandType_array_C1 = brandType_array_M[1].split(",");
                                if (brandType_array_C1 != null && brandType_array_C1.length > 0) {
                                    brandType.brandTypeArray = new Integer[brandType_array_C1.length];
                                    ArrayList<Integer> saveType = new ArrayList<Integer>(10);
                                    for (int i = 0; i < brandType_array_C1.length; i++) {
                                        if (brandType_array_C1[i].indexOf("-") == -1) {
                                            saveType.add(Integer.valueOf(brandType_array_C1[i]));
                                        } else {
                                            String[] brandType_array_C = brandType_array_C1[i].split("-");
                                            if (brandType_array_C != null && brandType_array_C.length > 1) {
                                                brandType.startNum = Integer.valueOf(brandType_array_C[0]);
                                                brandType.endNum = Integer.valueOf(brandType_array_C[1]);
                                            }
                                        }
                                    }
                                    brandType.brandTypeArray = (Integer[]) saveType.toArray(new Integer[0]);
                                }
                            }
                        }
                        mBrandTypeArrayList.add(brandType);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

