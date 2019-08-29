package com.kzksmarthome.common.lib.easylink.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public abstract class VersionUtil {
    //版本号获取
    public static String[] initVersion(Context context) {
        try {
            String pkName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(pkName, 0).versionName;
            int versionCode = context.getPackageManager().getPackageInfo(pkName, 0).versionCode;
            return new String[]{versionName,""+versionCode};
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
