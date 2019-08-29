package com.kzksmarthome.common.module.stat;

import java.util.ArrayList;
import java.util.List;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.task.BackgroundTaskExecutor;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;

public class StatService extends Thread {
    private static boolean mRunning = false;

    private final static int SLEEP_TIMEOUT = 10 * 1000; // 60 seconds
    private final int mHighPrioritySendInterval;
    private boolean mSending;

    private int mMaxSendCount;
    private int mSendCounter = 0;

    private static StatService mInstance;

    private final List<AbstractStat> mStatWorkerList = new ArrayList<AbstractStat>(3);

    public synchronized static StatService getInstance() {
        if (mInstance == null) {
            mInstance = new StatService();
        }
        return mInstance;
    }
    
    public void startStat() {
        mStatWorkerList.add(TraceStat.getInstance());
        mStatWorkerList.add(BusinessStat.getInstance());
        start();
    }

    public void stopStat() {
        if (mInstance != null) {
            mInstance.interrupt();
        }
    }
    
    public void stopImmediately() {
        for (AbstractStat stat : mStatWorkerList) {
            stat.flush();
        }
    }

    private StatService() {
        this.setName(this.getClass().getSimpleName());
        mHighPrioritySendInterval = 20 * 1000;// TODO set interval
        int lowPrioritySendInterval = 40 * 1000;
        mMaxSendCount = lowPrioritySendInterval / mHighPrioritySendInterval;
    }

    @Override
    public void run() {
        if (mRunning)
            return;

        mRunning = true;

        while (true) {
            try {
                sleep(SLEEP_TIMEOUT);
            } catch (InterruptedException e) {
            }

            for (AbstractStat stat : mStatWorkerList) {
                stat.flush();
            }

            if (mSending || !SmartHomeAppLib.getInstance().isBackProcess())
                continue;

            long currentTime = System.currentTimeMillis();
            long lastSentTime = SmartHomeAppLib.getInstance().getPreferences()
                    .getLong(SharePrefConstant.PREFS_KEY_STAT_LAST_SENT_TIME, 0);

            if (lastSentTime == 0 || lastSentTime + mHighPrioritySendInterval < currentTime
                    || lastSentTime > currentTime) {
                if (++mSendCounter % mMaxSendCount == 0) {
                    sendStat(PriorityConstants.STAT_PRIORITY_ALL);
                    mSendCounter = 0;
                } else {
                    sendStat(PriorityConstants.STAT_PRIORITY_HIGH);
                }
                try {
                    SmartHomeAppLib.getInstance()
                            .getPreferences()
                            .edit()
                            .putLong(SharePrefConstant.PREFS_KEY_STAT_LAST_SENT_TIME,
                                    System.currentTimeMillis()).commit();
                } catch (Exception e) {
                    L.w(e);
                }
            }
        }
    }

    private void sendStat(final int priority) {
        mSending = true;
        L.d("upload stat, priority %s", priority);
        // TODO 上传日志
        BackgroundTaskExecutor.executeTask(new Runnable() {

            @Override
            public void run() {

                try {
                    for (AbstractStat stat : mStatWorkerList) {
                        //stat.send(priority);
                    }

                } catch (Exception e) {
                    L.w(e);

                } finally {
                    mSending = false;
                }
            }
        });

    }

}
