package com.kzksmarthome.common.module.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;

import com.kzksmarthome.common.lib.task.BackgroundTaskExecutor;

/**
 * 实现功能：
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        BackgroundTaskExecutor.executeTask(new Runnable() {
            @Override
            public void run() {
                SparseArray<IAlarmEvent> sparseArray = GjjAlarmMgr.getInstance().getAlarmEvents();
                int size = sparseArray.size();
/*                Log.d("headDebug", "AlarmReceiver--size:" + size
                        + "----GjjAlarmMgr.getInstance()---" + GjjAlarmMgr.getInstance()
                        + "--- sAlarmEvents --" + GjjAlarmMgr.getInstance().sAlarmEvents + "---"
                        + GjjAlarmMgr.getInstance().sAlarmEvents.hashCode());*/
               
                for (int i = 0; i < size; i++) {
                    int type = sparseArray.keyAt(i);
                    IAlarmEvent alarmInterface = sparseArray.get(type);
                    if (alarmInterface != null && alarmInterface.checkTime(type)) {
                        alarmInterface.handleAlarmEvent(type);
                    } else {
                        // sparseArray.remove(type);
                    }
                }

            }
        });
    }

}
