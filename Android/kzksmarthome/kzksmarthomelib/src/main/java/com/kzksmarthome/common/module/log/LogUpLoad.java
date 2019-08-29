package com.kzksmarthome.common.module.log;

import java.io.File;
import java.io.FileNotFoundException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;

import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.AccessControlList;
import com.alibaba.sdk.android.oss.model.ClientConfiguration;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.alibaba.sdk.android.oss.model.TokenGenerator;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSFile;
import com.alibaba.sdk.android.oss.util.OSSToolKit;
import com.kzksmarthome.common.event.EventOfUpLoadLog;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.task.ForegroundTaskExecutor;
/**
 *
 * @Description: 日志上传
 * @Copyright: Copyright (c) 2015
 * @version: 1.0.0.0
 * @author: jack
 * @createDate 2015-5-20
 *
 */
public class LogUpLoad {
    private static String TAG = "LogUpLoad";
    static final String password_key ="ab72f0e880ce64da96af3e99f7109fcb";

    private static final String accessKey = GjjRc4.decry_RC4("fd15f53c24848ad303b8661f6f9c2735",
            password_key);
    private static final String secretKey = GjjRc4.decry_RC4("96188f1333a480fd3aa33f0b6c8a001f47cb4255a88a6d52b82569fff3b4",
            password_key);

    public LogUpLoad(Context context){
        initOSSS(context);
    }

    /**
     * 初始化OSSS
     * @param context
     */
    @SuppressLint("NewApi")
    private void initOSSS(Context context) {
        if (L.isDebugMode()) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        OSSService ossService = OSSServiceProvider.getService();
        // 初始化设置
        ossService.setApplicationContext(context.getApplicationContext());
        ossService.setGlobalDefaultTokenGenerator(new TokenGenerator() { // 设置全局默认加签器
            @Override
            public String generateToken(String httpMethod, String md5, String type,
                                        String date, String ossHeaders, String resource) {

                String content = httpMethod + "\n" + md5 + "\n" + type + "\n" + date + "\n"
                        + ossHeaders + resource;

                return OSSToolKit.generateToken(accessKey, secretKey, content);
            }
        });
        ossService.setGlobalDefaultHostId("oss-cn-shenzhen.aliyuncs.com");
        ossService.setCustomStandardTimeWithEpochSec(System.currentTimeMillis() / 1000);
        ossService.setGlobalDefaultACL(AccessControlList.PRIVATE); // 默认为private

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectTimeout(15 * 1000); // 设置全局网络连接超时时间，默认30s
        conf.setSocketTimeout(15 * 1000); // 设置全局socket超时时间，默认30s
        conf.setMaxConnections(50); // 设置全局最大并发网络链接数, 默认50
        ossService.setClientConfiguration(conf);

    }

    /**
     * 日志上传
     * @param filePath 文件路径
     * @param fileName 文件名加后缀
     */
    @SuppressLint("SdCardPath")
    public void resumableUpload(final String filePath,String fileName) {
        OSSService ossService = OSSServiceProvider.getService();
        OSSBucket bucket = ossService.getOssBucket("logsave");

        OSSFile bigfFile = ossService.getOssFile(bucket, fileName);
        try {
            bigfFile.setUploadFilePath(filePath, "raw/binary");
            bigfFile.ResumableUploadInBackground(new SaveCallback() {

                @Override
                public void onSuccess(String objectKey) {
                    deleFile(filePath);
                    EventOfUpLoadLog event = new EventOfUpLoadLog();
                    event.upLoadState = true;
                    GjjEventBus.getInstance().post(event);
                    L.d("%s [onSuccess] - " + objectKey + " upload success!", TAG);
                    //上传成功后杀死日志进程
                    LogService.getInstance().killLogcatProc();
                    ForegroundTaskExecutor.scheduleTask(1000, new Runnable() {
                        
                        @Override
                        public void run() {
                            LogService.getInstance().start();                          
                        }
                    });
                }

                @Override
                public void onProgress(String objectKey, int byteCount, int totalSize) {
                    L.d("%s [onProgress] - current upload " + objectKey + " bytes: " + byteCount + " in total: " + totalSize, TAG);
                }

                @Override
                public void onFailure(String objectKey, OSSException ossException) {
                    EventOfUpLoadLog event=new EventOfUpLoadLog();
                    event.upLoadState = false;
                    GjjEventBus.getInstance().post(event);
                    L.e(ossException);
                }
            });
        } catch (FileNotFoundException e) {
            L.e(e);
        }
    }
    /**
     * 删除zip文件和前天的txt日志文件
     * @param filePath
     */
    public void deleFile(String filePath){
        //首先删除压缩文件
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }


}
