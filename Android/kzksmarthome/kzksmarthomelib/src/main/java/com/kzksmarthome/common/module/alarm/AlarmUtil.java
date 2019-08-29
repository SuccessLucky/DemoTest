package com.kzksmarthome.common.module.alarm;

import android.os.SystemClock;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.module.log.L;

/**
 * 实现功能：单位统一用毫秒。
 */
public class AlarmUtil {

    /**
     * 上次请求时间和请求频率的值都写在xml的情况
    *@param lastKey  上次发起请求时间在SharePreferences中对应的key
    *@param lastDefaultTime 上次发起请求时间的默认值 一般为0
    *@param frequencyKey 闹钟事件频率在SharePreferences中对应的key
    *@param frequencyDefaultTime 闹钟事件频率的默认值
    *@return 
     */
    public static boolean checkTime(String lastKey, long lastDefaultTime, String frequencyKey, long frequencyDefaultTime) {
        long lastTime = SmartHomeAppLib.getInstance().getPreferences().getLong(lastKey, lastDefaultTime);
        long frequencyTime = SmartHomeAppLib.getInstance().getPreferences().getLong(frequencyKey, frequencyDefaultTime);
        return checkTime(lastTime, frequencyTime);
    }

    /**
     * 仅仅上次请求时间写在xml的情况
    *@param lastKey 上次发起请求时间在SharePreferences中对应的key
    *@param lastDefaultTime 上次发起请求时间的默认值
    *@param frequencyTime  闹钟事件频率值
    *@return
     */
    public static boolean checkTime(String lastKey, long lastDefaultTime, long frequencyTime) {
        long lastTime = SmartHomeAppLib.getInstance().getPreferences().getLong(lastKey, lastDefaultTime);
        return checkTime(lastTime, frequencyTime);
    }

    /**
     *  两个值都写在xml中，而用全局变量
    *@param lastTime 上次发起请求的时间
    *@param frequencyTime 闹钟事件的频率
    *@return
     */
    public static boolean checkTime(long lastTime, long frequencyTime) {
        L.d("Request# TCPMgr checkTime elapsedRealtime: %s, lastTime: %s, frequencyTime: %s", SystemClock.elapsedRealtime(), lastTime, frequencyTime);
        // 当前时间-上次请求时间，由于闹钟触发遍历闹钟事件，使得闹钟事件触发时间点存在微小误差，导致要等到下轮才能触发，为了避免这个发生，而将闹钟事件频率-误差
        if (SystemClock.elapsedRealtime() - lastTime >= frequencyTime - GjjAlarmMgr.ALARM_DEVIATION) {
            return true;
        } else {
            return false;
        }
    }

    /** 周期低于一小时的建议不用此函数。
     * 重置下次请求的时间，适用于长周期闹钟请求失败，避免再次请求需要过很长时间  例子：比如周期为1天，发起请求时失败，想再6小时后发起请求。  resetLastRequestTime("", 3600*1000*6, 3600 * 1000 *24)
    *@param key 
     */
    public static void resetNextRequestTime(String key, long afterTime, long frequencyTime) {
        // 通过把上次请求时间往前退后(frequencyTime-afterTime)，使得下次请求周期变为afterTime。
        long lastTime = SmartHomeAppLib.getInstance().getPreferences().getLong(key, afterTime);
        SmartHomeAppLib.getInstance().getPreferences().edit().putLong(key, lastTime - (frequencyTime - afterTime)).commit();
    }

}
