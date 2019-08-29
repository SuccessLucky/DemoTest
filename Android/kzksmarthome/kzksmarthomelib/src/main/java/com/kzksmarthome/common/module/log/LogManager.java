package com.kzksmarthome.common.module.log;

import android.annotation.TargetApi;
import android.content.pm.ApplicationInfo;
import android.os.Build;


import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.event.EventOfChangeDebugMode;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;

import butterknife.ButterKnife;

/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate Dec 29, 2014
 *
 */
class LogManager {

    static final int LOG_LEVEL_NO    = 0x00;
    static final int LOG_LEVEL_TRACE = 0x01;
    static final int LOG_LEVEL_DEBUG = 0x02;
    static final int LOG_LEVEL_INFO  = 0x04;
    static final int LOG_LEVEL_WARN  = 0x08;
    static final int LOG_LEVEL_ERROR = 0x10;
    static final int LOG_LEVEL_FATAL = 0x20;
    static final int LOG_LEVEL_STAT = 0x40;
    static final int LOG_LEVEL_ALL   = 0xFF;
    
    /**
     * 当前日志打印级别
     * 打包测试包时为LOG_LEVEL_ALL，即所有级别都会打印，
     * 打包release包时为LOG_LEVEL_NO，即所有级别都不会打印
     */
    private int printLevel = LOG_LEVEL_ALL;
    /**
     * 当前日志上传级别
     * 打包测试包时为LOG_LEVEL_NO，即所有级别都不会上传，
     * 打包release包时为LOG_LEVEL_ERROR|LOG_LEVEL_FATAL，即LOG_LEVEL_ERROR和LOG_LEVEL_FATAL级别都会打印
     */
    private int uploadLevel = LOG_LEVEL_NO;
    /**
     * 是否是Monkey包
     */
    private boolean isMonkeyRun = false;
    
    /**
     * 是否上传测试
     */
    private boolean isUploadTest = true;
    
    private final static int DEFAULT_SAVE_LEVEL = LOG_LEVEL_INFO | LOG_LEVEL_WARN | LOG_LEVEL_ERROR | LOG_LEVEL_FATAL | LOG_LEVEL_STAT;
    
    private static LogManager mLogManager;
    
    private synchronized static LogManager getInstance() {
        if (mLogManager == null) {
            mLogManager = new LogManager();
            mLogManager.init();
        }
        
        return mLogManager;
    }
    
    /**
     * 重新加载控制参数
     */
    private void init() {
        initDebugConfig();
    }
    
    private void initDebugConfig() {
        if (printLevel == LOG_LEVEL_ALL && SmartHomeAppLib.getInstance().isForeProcess()) {
            ButterKnife.setDebug(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && 0 != (SmartHomeAppLib.getInstance().getContext().getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
                MainTaskExecutor.runTaskOnUiThread(new Runnable() {

                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                       // WebView.setWebContentsDebuggingEnabled(true);
                    }
                    
                });
            }
        }
        GjjEventBus.getInstance().register(this);
    }

    public void onEventBackgroundThread(EventOfChangeDebugMode event) {
        if (event.mOpen) {
            printLevel = LOG_LEVEL_ALL;
            uploadLevel = LOG_LEVEL_NO;
        } else {
            printLevel = LOG_LEVEL_NO;
            uploadLevel = LOG_LEVEL_ALL;
        }
    }

    LogManager() {
    }

    /**
     * 指定日志级别是否达到打印级别
     * @param level
     * @return
     */
    static boolean canPrint(int level) {
        return (level & getInstance().printLevel) > 0;
    }
    
    /**
     * 指定日志级别是否达到上传级别
     * @param level
     * @return
     */
    static boolean canUpload(int level) {
        return (level & getInstance().uploadLevel) > 0;
    }

    static boolean canSave(int level) {
        if ((level & DEFAULT_SAVE_LEVEL) > 0) {
            return true;
        } else {
            return canUpload(level);
        }
    }
    /**
     * 是否处于调试级别
     * @return
     */
    static boolean isDebugMode() {
        return getInstance().printLevel == LOG_LEVEL_ALL;
    }
    /**
     * 是否处于Monkey测试阶段
     * @return
     */
    static boolean isMonkeyMode(){
        return getInstance().isMonkeyRun;
    }
    
    static boolean isUploadTest() {
        return isDebugMode() && getInstance().isUploadTest;
    }
}
