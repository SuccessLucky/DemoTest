package com.kzksmarthome.common.lib.okhttp;

public class CacheControlMode {
    /**
     * 只使用本地缓存，不请求远程
     */
    public static final int CACHE_LOCAL_ONLY = 1;
    
    /**
     * 只请求远程，不使用本地缓存
     */
    public static final int CACHE_REMOTE_NO_CACHE = 2;
    
    /**
     * 只请求远程，不使用本地缓存，保持到缓存
     */
    public static final int CACHE_REMOTE_AND_CACHE = 3;
    
    /**
     * 先返回本地缓存，再强制远程请求，并更新本地缓存,这时会有两次onRequestFinished回调
     */
    public static final int CACHE_LOCAL_FIRST_REMOTE_CACHE = 4;
    
    /**
     * 如果本地未过期，返回本地缓存，否则请求远程，并更新本地缓存
     */
    public static final int CACHE_LOCAL_FIRST_REMOTE_IF_EXPIRE_CACHE = 5;
    
    /**
     * 默认缓存过期时间，永不过期
     */
    public static final int DEFAULT_LOCAL_CACHE_TIME_FOREVER = Integer.MAX_VALUE;
    
    /**
     * 默认缓存过期时间，一周, 单位秒
     */
    public static final int DEFAULT_LOCAL_CACHE_TIME_WEEK = 60 * 60 * 24 * 7;
    
//    public static final String RESPONSE_FROM_KEY = "response_from";
//    public static final int RESPONSE_FROM_VALUE_LOCAL = 1;
//    public static final int RESPONSE_FROM_VALUE_REMOTE = 2;

    public int mCacheMode;
    public int mCacheDuration;

    public CacheControlMode(int cacheMode, int cacheDuration) {
        mCacheMode = cacheMode;
        mCacheDuration = cacheDuration;
    }
}
