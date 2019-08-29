package com.kzksmarthome.common.module.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.module.log.L;

/**
 * 实现功能：统一闹钟管理者
 */
public class GjjAlarmMgr {
    public final static String ALARM_ACTION = SmartHomeAppLib.getInstance().getContext().getPackageName()
            + ".module.alarm.AlarmReceiver.action"; // 闹钟action
    public final static int ALARM_FREQUENCY = 1000 * 60 * 1; // 闹钟周期默认1分钟，考虑参数下发
    public final static int ALARM_DEVIATION = (int) (ALARM_FREQUENCY * 0.1); // 闹钟误差范围

    public final static int ALARM_TYPE_CHECK_PUSH_CONNECTION = 1;

    public final static int DEFAULT_FREQUENCY_CHECK_PUSH_CONNECTION = ALARM_FREQUENCY * 2;// 心跳周期2分钟

    private static GjjAlarmMgr mInstance = null; // 单例闹钟管理者
    public SparseArray<IAlarmEvent> sAlarmEvents; // 闹钟事件

    /**
     * 获得闹钟管理者实例（单例）
     * 
     * @return
     */
    public synchronized static GjjAlarmMgr getInstance() {
        if (mInstance == null) {
            mInstance = new GjjAlarmMgr();
        }
        return mInstance;
    }

    /**
     * 创建闹钟，创建闹钟事件列表
     */
    private GjjAlarmMgr() {
        L.i("alarm#start set alarm");
        sAlarmEvents = new SparseArray<IAlarmEvent>();
    }

    public void startAlarm() {
        Context context = SmartHomeAppLib.getInstance().getContext();
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.cancel(pi);
        am.setRepeating(AlarmManager.RTC_WAKEUP, ALARM_FREQUENCY, ALARM_FREQUENCY, pi);
        L.i("alarm#end set alarm");
    }

    /**
     * 取消闹钟
     */
    public void cancelAlarm() {
        Context context = SmartHomeAppLib.getInstance().getContext();
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(ALARM_ACTION);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.cancel(pi);
    }

    /**
     * 重置闹钟，设置新周期
     * 
     * @param frequency
     */
    public void resetAlarm(long frequency) {
        Context context = SmartHomeAppLib.getInstance().getContext();
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(ALARM_ACTION);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.cancel(pi);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), frequency, pi);
    }

    /**
     * 默认旧的闹钟频率
     */
    public void resetAlarm() {
        resetAlarm(ALARM_FREQUENCY);
    }

    /**
     * 注册闹钟事件
     * 
     * @param type 闹钟事件类型
     * @param alarmInterface 闹钟事件
     */
    public void registerAlarmEvent(int type, IAlarmEvent alarmInterface) {
        if ((sAlarmEvents != null) && sAlarmEvents.get(type) == null) { // 避免重复插入
            sAlarmEvents.put(type, alarmInterface);
        }
        // Log.d("headDebug", "sAlarmEvents--size:" + sAlarmEvents.size());
    }

    /**
     * 根据类型取消闹钟时间注册
     * 
     * @param type 闹钟事件类型
     */
    public void unRegisterAlarmEvent(int type) {
        if (sAlarmEvents != null) {
            sAlarmEvents.remove(type);
        }
    }

    /**
     * 获得闹钟事件列表
     * 
     * @return
     */
    public SparseArray<IAlarmEvent> getAlarmEvents() {
        return sAlarmEvents;
    }

}
