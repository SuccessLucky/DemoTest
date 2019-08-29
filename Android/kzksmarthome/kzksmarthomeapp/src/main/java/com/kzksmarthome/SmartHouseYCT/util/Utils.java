package com.kzksmarthome.SmartHouseYCT.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.kzksmarthome.common.module.log.L;

public class Utils {
    public static final String TAG = "PushDemoActivity";
    public static final String RESPONSE_METHOD = "method";
    public static final String RESPONSE_CONTENT = "content";
    public static final String RESPONSE_ERRCODE = "errcode";
    protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
    public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
    public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
    public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
    protected static final String EXTRA_ACCESS_TOKEN = "access_token";
    public static final String EXTRA_MESSAGE = "message";

    public static String logStringCache = "";

    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {
            L.e(e);
        }
        return apiKey;
    }

    public static List<String> getTagsList(String originalText) {
        if (originalText == null || originalText.equals("")) {
            return null;
        }
        List<String> tags = new ArrayList<String>();
        int indexOfComma = originalText.indexOf(',');
        String tag;
        while (indexOfComma != -1) {
            tag = originalText.substring(0, indexOfComma);
            tags.add(tag);

            originalText = originalText.substring(indexOfComma + 1);
            indexOfComma = originalText.indexOf(',');
        }

        tags.add(originalText);
        return tags;
    }

    public static String getLogText(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("log_text", "");
    }

    public static void setLogText(Context context, String text) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("log_text", text);
        editor.commit();
    }
    /**
     * QQ分享
     * @param context
     * @param content
     */
   /* public static void shareQQ(UMSocialService mController,Context context,String content,String title,String url,String iconUrl) {
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(content);
        qqShareContent.setTitle(title);
        if(iconUrl == null){
            qqShareContent.setShareMedia(new UMImage(context, R.drawable.ic_launcher));
        } else {
            qqShareContent.setShareMedia(new UMImage(context, iconUrl));
        }
        qqShareContent.setTargetUrl(url);
        mController.setShareMedia(qqShareContent);
    //参数1为当前Activity，参数2为开发者在QQ互联申请的APP id，参数3为开发者在QQ互联申请的APP kEY.
      UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity)context, "100424468","c7394704798a158208a74ab60104f0ba");
      qqSsoHandler.addToSocialSDK(); 
      
 
    }*/
    /**
     * 微信分享
     */
    /*public static void shareWeiXin(UMSocialService mController,Context context,String content,String title,String url,String iconUrl) {
       WeiXinShareContent weixinContent = new WeiXinShareContent();
       weixinContent.setShareContent(content);
       weixinContent.setTitle(title);
       weixinContent.setTargetUrl(url);
       if(iconUrl == null){
            weixinContent.setShareMedia(new UMImage(context, R.drawable.ic_launcher));
        } else {
            weixinContent.setShareMedia(new UMImage(context, iconUrl));
        }
       mController.setShareMedia(weixinContent);
        String appID = "wx88db59467cefb8ee";
        String appSecret = "d6e0088adab733ec00d0bed0b363e385";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context,appID,appSecret);
        wxHandler.addToSocialSDK();
    }*/
     /**
      * 微信朋友圈
      * @param context
      * @param content
      */
   /* public static void shareWeiXinPY(UMSocialService mController,Context context,String content,String title,String url,String iconUrl) {
        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia
                .setShareContent(content);
        circleMedia.setTitle(title);
        if(iconUrl == null){
            circleMedia.setShareMedia(new UMImage(context, R.drawable.ic_launcher));
        } else {
            circleMedia.setShareMedia(new UMImage(context, iconUrl));
        }
        circleMedia.setTargetUrl(url);
        mController.setShareMedia(circleMedia);      
        String appID = "wx88db59467cefb8ee";
        String appSecret = "d6e0088adab733ec00d0bed0b363e385";
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context,appID,appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }*/
    /**
     * 新浪分享
     * @param mController
     * @param context
     * @param content
     * @param url
     */
   /* public static void shareSina(UMSocialService mController, Context context,String content,String url) {
       SinaShareContent sinaContent = new SinaShareContent();
       sinaContent
               .setShareContent(content);
       sinaContent.setShareImage( new UMImage(context, R.drawable.ic_launcher));
       mController.setShareMedia(sinaContent);      
      //设置新浪SSO handler
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
    }*/
}
