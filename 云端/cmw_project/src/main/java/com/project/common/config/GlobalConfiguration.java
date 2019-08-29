package com.project.common.config;

import com.google.common.base.Strings;

/**
 * Created by xieyanhao on 17/7/18.
 */
public class GlobalConfiguration {

    private static String GTAppId;
    private static String GTAppkey;
    private static String GTMaster;
    private static String SMSAlert;

    private static String yunpianApiKey;
    private static long yunpianTplVertify;
    private static long yunpianTplAlarm;

    public static String SMS_ON = "ON";
    public static String SMS_OFF = "OFF";

    public static String getGTAppId() {

        if (GTAppId == null) {
            GTAppId = ConfigUtils.shared().getGlobalConfig("GT_APP_ID");
        }
        return GTAppId;
    }

    public static String getGTAppkey() {

        if (GTAppkey == null) {
            GTAppkey = ConfigUtils.shared().getGlobalConfig("GT_APP_KEY");
        }
        return GTAppkey;
    }

    public static String getGTMaster() {

        if (GTMaster == null) {
            GTMaster = ConfigUtils.shared().getGlobalConfig("GT_MASTER");
        }
        return GTMaster;
    }

    public static String getYunpianApiKey() {

        if (yunpianApiKey == null) {
            yunpianApiKey = ConfigUtils.shared().getGlobalConfig("YUNPIAN_API_KEY");
        }
        return yunpianApiKey;
    }

    public static String getSMSAlert() {

        if (SMSAlert == null) {
            SMSAlert = ConfigUtils.shared().getGlobalConfig("SMS_ALERT");
        }
        return SMSAlert;
    }

    public static long getYunpianTplVertify() {

        if (yunpianTplVertify == 0) {
            String yunpianTplVertifyStr = ConfigUtils.shared().getGlobalConfig("YUNPIAN_TPL_VERTIFY_CODE");
            if (Strings.isNullOrEmpty(yunpianTplVertifyStr)) {
                yunpianTplVertify = 0;
            } else {
                yunpianTplVertify = Long.valueOf(yunpianTplVertifyStr);
            }
        }
        return yunpianTplVertify;
    }

    public static long getYunpianTplAlarm() {

        if (yunpianTplAlarm == 0) {
            String yunpianTplAlarmStr = ConfigUtils.shared().getGlobalConfig("YUNPIAN_TPL_ALARM");
            if (Strings.isNullOrEmpty(yunpianTplAlarmStr)) {
                yunpianTplAlarm = 0;
            } else {
                yunpianTplAlarm = Long.valueOf(yunpianTplAlarmStr);
            }
        }
        return yunpianTplAlarm;
    }
}
