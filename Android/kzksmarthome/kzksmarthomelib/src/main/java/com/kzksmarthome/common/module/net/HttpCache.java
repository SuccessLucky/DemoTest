package com.kzksmarthome.common.module.net;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.util.MD5Util;

/**
 * 网络缓存
 * Created by lizhi on 2018/1/6.
 */

public class HttpCache {
    /**
     * 缓存文件名称
     */
    private static final String CACHE_FILE_NAME = "CACHE_FILE_NAME";
    /**
     * 缓存share
     */
    @SuppressLint("WrongConstant")
    private static SharedPreferences mCacheShare = null;


    /**
     * 获取缓存key
     *
     * @param url            请求url
     * @param requestBodyStr 参数
     */
    public static String getCacheKey(String url, String requestBodyStr) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String params = "";
        if (requestBodyStr != null) {
            params = requestBodyStr;
        }
        return MD5Util.md5Hex(url + params);
    }

    /**
     * 获取缓存
     *
     * @param cacheKey
     * @return
     */
    public static String getCache(String cacheKey) {
        if (TextUtils.isEmpty(cacheKey)) {
            return null;
        }
        return getMCacheShare().getString(cacheKey, null);
    }

    /**
     * 保存缓存
     *
     * @param cacheKey   缓存key
     * @param jsonString 缓存数据
     */
    public static void saveCache(String cacheKey, String jsonString) {
        if (TextUtils.isEmpty(cacheKey)) {
            return;
        }
        getMCacheShareEditor().putString(cacheKey, jsonString);
        getMCacheShareEditor().commit();
    }

    /**
     * 移除缓存
     *
     * @param cacheKey 缓存key
     */
    public static void removeCache(String cacheKey) {
        if (TextUtils.isEmpty(cacheKey)) {
            return;
        }
        getMCacheShareEditor().remove(cacheKey);
        getMCacheShareEditor().commit();
    }

    /**
     * 清空缓存
     */
    public static void clearCache() {
        getMCacheShareEditor().clear();
    }


    /**
     * 获取 Editor
     *
     * @return
     */
    private static SharedPreferences.Editor getMCacheShareEditor() {
        SharedPreferences.Editor editor = getMCacheShare().edit();
        return editor;
    }


    /**
     * 获取缓存的share
     *
     * @return
     */
    @SuppressLint("WrongConstant")
    private static SharedPreferences getMCacheShare() {
        if (mCacheShare == null) {
            mCacheShare = SmartHomeAppLib.getInstance().getContext().getSharedPreferences(CACHE_FILE_NAME, Context.MODE_APPEND);
        }
        return mCacheShare;
    }
}
