package com.kzksmarthome.common.lib.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.lib.R;

public class AndroidUtil {

    private static String IMEI = "";
    private static String IMSI = "";
    private static String VERSION_NAME = "";
    private static int VERSION_CODE = 0;
    private static String MAC_ADDRESS = "";
    private static String BUILD = "";

    public static String getVersionName(Context ctx) {
        if (TextUtils.isEmpty(VERSION_NAME)) {

            try {
                PackageManager pm = ctx.getApplicationContext().getPackageManager();
                PackageInfo pInfo = pm.getPackageInfo(
                        SmartHomeAppLib.getInstance().getContext().getPackageName(),
                        PackageManager.GET_CONFIGURATIONS);
                VERSION_NAME = pInfo.versionName;

            } catch (NameNotFoundException e) {
                L.w(e);
            }
        }
        if (TextUtils.isEmpty(VERSION_NAME)) {
            VERSION_NAME = "";
        }
        return VERSION_NAME;
    }

    public static int getVersionCode(Context ctx) {
        if (VERSION_CODE == 0) {

            try {
                PackageManager pm = ctx.getApplicationContext().getPackageManager();
                PackageInfo pInfo = pm.getPackageInfo(
                        SmartHomeAppLib.getInstance().getContext().getPackageName(),
                        PackageManager.GET_CONFIGURATIONS);
                VERSION_CODE = pInfo.versionCode;

            } catch (NameNotFoundException e) {
                L.w(e);
            }
        }

        return VERSION_CODE;
    }

    /**
     * 获取设备IMEI码
     * 
     * @return
     */
    public static String getDeviceIMEI(Context ctx) {
        if (TextUtils.isEmpty(IMEI)) {

            try {

                TelephonyManager telephonyManager = (TelephonyManager) ctx.getApplicationContext().getSystemService(
                        Context.TELEPHONY_SERVICE);
                IMEI = telephonyManager.getDeviceId();
            } catch (java.lang.SecurityException e) {
                L.w(e);
            }
        }

        if (TextUtils.isEmpty(IMEI)) {
            IMEI = "";
        }

        return IMEI;
    }

    /**
     * 获取设备mac
     * 
     * @return
     */
    public static String getWifiMacAddress(Context ctx) {
        if (TextUtils.isEmpty(MAC_ADDRESS)) {
            try {
                WifiManager wm = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                if (wm != null && wm.getConnectionInfo() != null) {
                    MAC_ADDRESS = wm.getConnectionInfo().getMacAddress();
                }
            } catch (Exception e) {
                L.w(e);
            }
        }

        if (MAC_ADDRESS == null) {
            MAC_ADDRESS = "";
        }

        return MAC_ADDRESS;
    }

    /**
     * 获取设备IMSI
     * 
     * @return
     */
    public static String getDeviceIMSI(Context ctx) {
        if (TextUtils.isEmpty(IMSI)) {

            try {

                TelephonyManager telephonyManager = (TelephonyManager) ctx.getApplicationContext().getSystemService(
                        Context.TELEPHONY_SERVICE);
                IMSI = telephonyManager.getSubscriberId();
            } catch (java.lang.SecurityException e) {
                L.w(e);
            }
        }

        if (IMSI == null) {
            IMSI = "";
        }
        return IMSI;
    }

    /**
     * 获取build号
     * 
     * @return
     */
    public static String getBuild(Context ctx) {
        if (TextUtils.isEmpty(BUILD)) {
            BUILD = ctx.getApplicationContext().getString(R.string.build);
        }

        if (BUILD == null) {
            BUILD = "0";
        }
        return BUILD;
    }

    public static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static int getStatusBarHeight() {
        return Resources.getSystem().getDimensionPixelSize(Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
    }

    /**
     * 启动应用，传递需要启动的包名
     * 
     * @param pkgName
     */
    public static boolean launchApp(Context context, String pkgName) {
        if (pkgName.equals(context.getPackageName())) {
            return true;
        }
        try {
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(pkgName);

            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                context.startActivity(context.getPackageManager().getLaunchIntentForPackage(pkgName));

                // 添加启动游戏任务完成
                return true;
            } else {
                SmartHomeAppLib.showToast(R.string.start_fail);
            }

        } catch (Exception e) {
            L.w(e);
        }
        return false;
    }

    /**
     * 获取当前进程占用内存
     * 
     * @param context
     * @return
     */
    public static float getProcessMemoryInfo(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int pid = android.os.Process.myPid();
        android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(new int[] { pid });
        return (float) memoryInfoArray[0].getTotalPrivateDirty() / 1024;
    }
}
