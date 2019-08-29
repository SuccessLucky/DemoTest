package com.kzksmarthome.SmartHouseYCT.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.splash.SplashActivity;
import com.kzksmarthome.common.module.log.L;

public class ShortCutUtil {
    
    /**
     * 根据系统权限(permission)查询Authority
     * @param ctx
     *        上下文对象
     * @param permission
     *        系统权限
     * @return
     *        Authority
     */
    private static String getAuthorityFromPermission(Context ctx, String permission) {
        if (permission == null) {
            return null;
        }

        List<PackageInfo> packs = ctx.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);

        if (packs != null) {
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;

                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (provider.readPermission != null && provider.readPermission.contains(permission)) {
                            if (provider.readPermission.contains("htc")) //针对htc机器做特殊处理
                                return "com.htc.launcher.settings";

                            else
                                return provider.authority;
                        }

                        /*if (provider.writePermission != null && provider.writePermission.contains(permission)) {
                            return provider.authority;
                        }*/
                    }
                }
            }
        }

        return null;
    }
    /**
     * 检查应用程序快捷方式是否已经创建
     * @param ctx
     *        上下文对象
     * @param name
     *        快捷方式名称
     * @param pkgName
     *        意图对象
     * @return
     *        快捷方式数量
     */
    public static int checkShortcutIsExist(Context ctx, String name, String pkgName) {
//      String permission = "com.android.launcher.permission.READ_SETTINGS";
//      String permission = "android.launcher.permission.READ_SETTINGS";

        /**注:Android原生系统遵循标准的接口
         permission 是Android系统的相关权限,由于存在各种不同的ROM中应用之间共享数据的权限或URI路径不同(一样的程序逻辑操作)。
         下面列举 读取快捷方式应用的launcher.db中favoirtes表(该表保存桌面快捷方式的数据)权限和URI。
         例如MIUI系统:permission:com.android.launcher.permission.READ_SETTINGS
         authority:com.miui.home.launcher.settings
         例如HTC系统:permission:com.htc.launcher.permission.READ_SETTINGS
         authority:com.htc.launcher.settings
         例如华为系统:permission:com.huawei.android.launcher.permission.READ_SETTINGS
         authority:com.huawei.launcher.settings
         所以针对以上各种定制的Android系统，要采用权限大范围的匹配方式去获取相应的authority，即只是根据
         permission为launcher.permission.READ_SETTINGS去匹配。
         */
        try {
            String permission = "launcher.permission.READ_SETTINGS";
            String authority = getAuthorityFromPermission(ctx, permission);

            if (authority == null) {
                return 0;
            }

            String url = "content://" + authority + "/favorites?notify=true";
            //根据快捷方式的名称判断是否已经创建过
            Cursor cursor = ctx.getContentResolver().query(Uri.parse(url), null, "title=?", new String[] {name}, null);

            if (cursor == null) {
                return 0;
            }

            int count = cursor.getCount();

            if (count > 0) {
                boolean find = false;

                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        String intentText = cursor.getString(cursor.getColumnIndex("intent"));

                        if (intentText.contains(pkgName)) {
                            find = true;
                            break;
                        }

                        cursor.moveToNext();
                    }
                }

                if (!find) {
                    count = 0;
                }
            }

            cursor.close();
            return count;

        } catch (Exception e) {
            L.e(e);
        }

        return 0;
    }
    
    /**
     * 创建应用程序快捷方式
     * @param ctx
     * @param name
     * @param iconRes
     *
     */
    public static void createLauncherShortCut(Context ctx, String name, int iconRes) {
        try {
            Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            // 不允许重复创建
            shortcutIntent.putExtra("duplicate", false);
            // 需要现实的名称
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
            // 快捷图片
            Parcelable icon = Intent.ShortcutIconResource.fromContext(ctx, iconRes);
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
            Intent launchIntent = new Intent(ctx, SplashActivity.class);
            //卸载应用后系统自动清除快捷方式
            launchIntent.setAction("android.intent.action.MAIN");
            launchIntent.addCategory("android.intent.category.LAUNCHER");
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
            // 发送广播。OK
            ctx.sendBroadcast(shortcutIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteLauncherShortCut(Context ctx, String name, int iconRes) {
        try {
            Intent shortcutIntent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
            // 不允许重复创建
            shortcutIntent.putExtra("duplicate", false);
            // 需要现实的名称
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
            // 快捷图片
            Parcelable icon = Intent.ShortcutIconResource.fromContext(ctx, iconRes);
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
            Intent launchIntent = new Intent();
            launchIntent.setClass(ctx, SplashActivity.class);
            //卸载应用后系统自动清除快捷方式
            launchIntent.setAction("android.intent.action.MAIN");
            launchIntent.addCategory("android.intent.category.LAUNCHER");
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
            // 发送广播。OK

        } catch (Exception e) {
            L.e(e);
        }
    }
}
