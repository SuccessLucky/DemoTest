package com.kzksmarthome.SmartHouseYCT.model.download;

import java.io.File;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import com.kzksmarthome.common.module.log.L;

public class FileUtil {
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static File DOWNLOAD_DIR;

    private static File getExternalFilesDir(Context ctx, String typeString) {
        File baseDir = ctx.getExternalFilesDir(typeString);
        if (baseDir == null) {
            baseDir = new File("/sdcard/Android/data/" + ctx.getPackageName() + "/files/"
                    + typeString);
            baseDir.mkdirs();
        }
        if (!baseDir.exists()) {
            return null;
        }
        return baseDir;
    }

    /**
     * 获取下载目录
     * 
     * @param ctx Context
     * @return 目录File对象
     * **/
    public static File getDownloadFilesDir(Context ctx) {
        if (DOWNLOAD_DIR == null) {
            DOWNLOAD_DIR = getExternalFilesDir(ctx, Environment.DIRECTORY_DOWNLOADS);

            if (DOWNLOAD_DIR == null) {
                DOWNLOAD_DIR = new File(ctx.getFilesDir(), Environment.DIRECTORY_DOWNLOADS);
            }

        }
        if (!DOWNLOAD_DIR.exists()) {
            DOWNLOAD_DIR.mkdirs();
        }

        return DOWNLOAD_DIR;
    }

    public static boolean fileExist(String fileStr) {
        if (!TextUtils.isEmpty(fileStr)) {
            File file = new File(fileStr);
            return file.exists();
        }
        return false;
    }

    /**
     * 获取图片缓存目录
     * 
     * @param context
     * @param preferExternal
     * @return
     */
    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;
        if (preferExternal && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalFilesDir(context, "cache");
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            L.d("Can't define system cache directory! use: " + cacheDirPath);
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * ANR文件备份目录
     * 
     * @param context
     * @return
     */
    public static File getANRThreadTraceDir(Context context) {
        if (hasExternalStoragePermission(context)) {
            return getExternalFilesDir(context, "anr");
        }
        return null;
    }
}
