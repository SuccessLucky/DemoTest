package com.kzksmarthome.common.lib.util;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Printer;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.stat.TraceInfo;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;

/**
 * Created by Administrator on 2014/11/10.
 */
public class LooperHook {

    private static final String STATS_MESSAGE_LOGGING_FLAG_DEFAULT = "0011"; // 默认开关
    private static int[] LOOPER_HOOK_INTERVAL_DEFAULT = { 51, 101, 201, 501, 1501, 3001 };
    private static final String TAG = "LooperHook";
    private static LooperHook sInstance;

    private Looper sLooper;
    private MessageLogging sMessageLogging;
    private boolean mIsFpsOpen = false; // 是否开启帧率监测
    private boolean mIsH5Open = false; // 是否开启H5监测
    private LogcatPrinter mLogcatPrinter;
    private AnrPrinter mAnrPrinter;

    public synchronized static LooperHook getInstance() {
        if (sInstance == null) {
            sInstance = new LooperHook(Looper.getMainLooper());
        }
        return sInstance;
    }

    private LooperHook(Looper looper) {
        sLooper = looper;
        sMessageLogging = new MessageLogging();
    }

    private void setIsFpsOpen(boolean mIsFpsOpen) {
        this.mIsFpsOpen = mIsFpsOpen;
    }

    private boolean getIsFpsOpen() {
        return mIsFpsOpen;
    }

    private void setIsH5Open(boolean mIsH5Open) {
        this.mIsH5Open = mIsH5Open;
    }

    private boolean getIsH5Open() {
        return mIsH5Open;
    }

    /**
     * 设置监听开关
     *
     */
    private void initFlag() {
        String flagString = SmartHomeAppLib.getInstance().getPreferences()
                .getString(SharePrefConstant.PREFS_KEY_LOGGING_FLAG, STATS_MESSAGE_LOGGING_FLAG_DEFAULT);
        int flag = 0;
        try {
            flag = Integer.parseInt(flagString, 2);
        } catch (NumberFormatException e) {
            L.e(e);
        }

        if ((flag >> 0 & 1) == 1) { // 右边第一位为1，则注册监听消息处理情况
            mLogcatPrinter = new LogcatPrinter();
            sMessageLogging.registerMsgFinishedListener(mLogcatPrinter);
        }

        if ((flag >> 1 & 1) == 1) { // 右边第二位为1，则注册监听 ANR卡顿情况
            mAnrPrinter = new AnrPrinter();
            sMessageLogging.registerMsgStartedListener(mAnrPrinter);
            sMessageLogging.registerMsgFinishedListener(mAnrPrinter);
        }

        if ((flag >> 2 & 1) == 1) { // 右边第三位为1，则打开监听帧率开关
            setIsFpsOpen(true);
        } else {
            setIsFpsOpen(false);
        }

        if ((flag >> 3 & 1) == 1) { // 右边第四位为1，则打开监听H5特定页面方法执行时间开关
            setIsH5Open(true);
        } else {
            setIsH5Open(false);
        }
    }

    /**
     * 开始监听
     */
    public void startHook() {
        initFlag();
        sLooper.setMessageLogging(sMessageLogging);
    }

    /**
     * 停止监听
     */
    public void stopHook() {
        if (mLogcatPrinter != null) {
            sMessageLogging.unregisterMsgFinishedListener(mLogcatPrinter);
        }
        if (mAnrPrinter != null) {
            sMessageLogging.unregisterMsgStartedListener(mAnrPrinter);
            sMessageLogging.unregisterMsgFinishedListener(mAnrPrinter);
        }
        sLooper.setMessageLogging(null);
    }

    /**
     * 内部类：MessageLogging 实现了Printer接口
     */
    private static class MessageLogging implements Printer {

        private static final String START_TAG = ">"; // 开始标志
        private static final String FINISH_TAG = "<"; // 结束标注
        private boolean mHasStartLog = false; // 是否开始log
        private String mStartLog = null; // log内容
        private long mStartRtime = -1; // start Rtime
        private long mStartCtime = -1; // start Ctime

        // IMsgStartedListener 接口集合
        private Vector<IMsgStartedListener> mStartedListeners = new Vector<IMsgStartedListener>();
        // IMsgFinishedListener 接口集合
        private Vector<IMsgFinishedListener> mFinishedListeners = new Vector<IMsgFinishedListener>();

        /**
         * 定义接口：IMsgStartedListener
         */
        interface IMsgStartedListener {
            void onMsgStarted(String log, long srtime, long sctime);
        }

        /**
         * 定义接口：IMsgFinishedListener
         */
        interface IMsgFinishedListener {
            void onMsgFinished(String log, long srtime, long sctime, long rtime, long ctime);
        }

        /**
         * 注册IMsgStartedListener接口
         * 
         * @param startedListener
         */
        void registerMsgStartedListener(IMsgStartedListener startedListener) {
            mStartedListeners.add(startedListener);
        }

        /**
         * 注销IMsgStartedListener接口
         * 
         * @param startedListener
         */
        void unregisterMsgStartedListener(IMsgStartedListener startedListener) {
            mStartedListeners.remove(startedListener);
        }

        /**
         * 注册IMsgFinishedListener接口
         * 
         * @param finishedListener
         */
        void registerMsgFinishedListener(IMsgFinishedListener finishedListener) {
            mFinishedListeners.add(finishedListener);
        }

        /**
         * 注销IMsgFinishedListener接口
         * 
         * @param finishedListener
         */
        void unregisterMsgFinishedListener(IMsgFinishedListener finishedListener) {
            mFinishedListeners.remove(finishedListener);
        }

        /**
         * 重新println方法，在Looper机制的源码中，最后会回调这个方法
         * 
         * @param log
         */
        @Override
        public void println(String log) {

            if (log.startsWith(START_TAG)) { // log的开始，是否为自定的开始标志，即">"
                mStartRtime = System.currentTimeMillis(); // log开始，系统时间
                mStartCtime = SystemClock.currentThreadTimeMillis(); // log开始，在当前线程中已运行的时间
                mStartLog = log; // 被打印 被解析的log
                mHasStartLog = true; // 设置 是否开始log 的标记值 为：true，表示开始log

                for (IMsgStartedListener listener : mStartedListeners) {
                    listener.onMsgStarted(mStartLog, mStartRtime, mStartCtime);
                }

            } else if (mHasStartLog && log.startsWith(FINISH_TAG)) { // log的开始，是否为自定义的结束标记，即"<"
                mHasStartLog = false; // 设置 是否开始log 的标记值 为：false，表示结束log
                long finishRtime = System.currentTimeMillis(); // log结束，系统时间
                long rtime = finishRtime - mStartRtime; // log持续，系统时间

                for (IMsgFinishedListener listener : mFinishedListeners) {
                    listener.onMsgFinished(mStartLog, mStartRtime, mStartCtime, rtime, 0);
                }
            }
        }

    }

    /**
     * 内部类LogcatPrinter 实现了接口IMsgFinishedListener
     */
    class LogcatPrinter implements MessageLogging.IMsgFinishedListener {

        @Override
        public void onMsgFinished(String log, long srtime, long sctime, long rtime, long ctime) {
            int minterval = 0;
            for (int per_interval : LOOPER_HOOK_INTERVAL_DEFAULT) {

                if (rtime >= per_interval) {
                    minterval = per_interval;
                    // long finishCtime = SystemClock.currentThreadTimeMillis(); //log结束，线程时间
                    ctime = SystemClock.currentThreadTimeMillis() - sctime; // log持续，处理此消息，线程花费时间
                } else {
                    break;
                }
            }
            if (minterval != 0) {
                String[] mclass = log.split("arrow.");
                String simpleClass = sLooper.getThread().getName() + "|";
                if (mclass != null) {
                    if (mclass.length > 0) {
                        simpleClass += mclass[mclass.length - 1];
                    } else {
                        simpleClass += mclass[0];
                    }
                }
                JSONObject result = TraceInfo.buildHandleMsgTimeInfo(rtime, ctime, minterval,
                        simpleClass);
//                TraceStat.getInstance().addStat(result);
                L.d("%s, onMsgFinished result : %s", TAG, result);
            }

        }
    }

    /**
     * 内部类AnrPrinter 实现了接口IMsgStartedListener IMsgFinishedListener
     */
    class AnrPrinter implements MessageLogging.IMsgStartedListener,
            MessageLogging.IMsgFinishedListener {

        private static final long mAnrTime = 800;
        private HandlerThread mAnrHandlerThread;
        private Handler mAnrHandler;
        private boolean mMsgFinished = false;

        private Runnable mAnrRunnable = new Runnable() {
            @Override
            public void run() {
                dumpStackTraces();
            }
        };

        public AnrPrinter() {
            mAnrHandlerThread = new HandlerThread("ANR HANDLER THREAD",
                    android.os.Process.THREAD_PRIORITY_BACKGROUND);
            mAnrHandlerThread.start();
            mAnrHandler = new Handler(mAnrHandlerThread.getLooper());
        }

        /**
         * 开始处理消息时，回调这个方法
         * 
         * @param log 系统的log信息
         * @param srtime 消息处理开始时，系统开始时间
         * @param sctime 消息处理开始时，线程开始时间
         */
        @Override
        public void onMsgStarted(String log, long srtime, long sctime) {
            mMsgFinished = false;
            mAnrHandler.removeCallbacks(mAnrRunnable);
            mAnrHandler.postDelayed(mAnrRunnable, mAnrTime);
        }

        /**
         * 结束处理消息时，回调这个方法
         * 
         * @param log 系统的log信息
         * @param srtime 消息处理开始时，系统开始时间
         * @param sctime 消息处理开始时，线程开始时间
         * @param rtime 处理消息过程中，系统花费的时间 （卡顿时间 + rtime）
         * @param ctime 处理消息过程中，线程花费的时间
         */
        @Override
        public void onMsgFinished(String log, long srtime, long sctime, long rtime, long ctime) {
            mMsgFinished = true;
            mAnrHandler.removeCallbacks(mAnrRunnable);
        }

        /**
         * 备份当前进程所有线程的StackTraces数据
         */
        public void dumpStackTraces() {
            StringBuffer buffer = new StringBuffer();

            buffer.append(getFreeMemory() + "\n\n");
            Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
            Set<Thread> set = map.keySet();
            Iterator<Thread> i = set.iterator();
            boolean validMsg = true;
            while (i.hasNext()) {
                Thread thread = i.next();
                buffer.append(thread.getName() + " " + thread.getPriority() + " "
                        + thread.getState() + "\n");
                int index = 0;
                StackTraceElement[] ste = map.get(thread);
                for (StackTraceElement e : ste) {
                    String line = e.toString();
                    if (index == 0) {
                        if ("main".equals(thread.getName())
                                && "android.os.MessageQueue.nativePollOnce(Native Method)"
                                        .equals(line)) {
                            validMsg = false;
                            break;
                        }
                    }
                    buffer.append(line + "\n");
                    ++index;
                }
                buffer.append("\n");
            }

            if (!mMsgFinished && validMsg) {
                // TODO 保存pb文件
                File dir = FileUtil.getANRThreadTraceDir(SmartHomeAppLib.getInstance().getContext());
                if (null != dir && dir.exists()) {
                    String anrString = buffer.toString();
                    Util.writeFile(dir.getPath() + "/anr_trace.stacktrace", anrString);
                    L.d("%s dumpStackTraces: %s", TAG, anrString);
                }
            }
        }

        /**
         * 取得系统空闲内存
         * 
         * @return
         */
        private String getFreeMemory() {
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            ActivityManager activityManager = (ActivityManager) SmartHomeAppLib.getInstance().getContext().getSystemService(Activity.ACTIVITY_SERVICE);
            activityManager.getMemoryInfo(mi);
            long availableMegs = mi.availMem;
            StringBuilder sb = new StringBuilder();
            sb.append('\n').append('\n');
            sb.append("Available RAM: ").append(availableMegs).append('\n');
            sb.append("Low MEM: ").append(mi.lowMemory).append('\n');

            return sb.toString();
        }
    }

}
