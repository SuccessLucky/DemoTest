package com.kzksmarthome.common.lib.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 后台线程池
 * 类/接口注释
 * 
 * @author panrq
 * @createDate Dec 29, 2014
 *
 */
// an asynchronous task executor(thread pool)
public class BackgroundTaskExecutor {
    private static ExecutorService sThreadPoolExecutor = null;
    private static ScheduledThreadPoolExecutor sScheduledThreadPoolExecutor = null;

    public static void ensurePriority() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }
    
    public static void executeTask(Runnable task) {
        ensureThreadPoolExecutor();
        sThreadPoolExecutor.execute(task);
    }

    public static <T> Future<T> submitTask(Callable <T> task) {
        ensureThreadPoolExecutor();
        return sThreadPoolExecutor.submit(task);
    }

    public static ScheduledFuture<?> scheduleTask(long delay, Runnable task) {
        ensureScheduledThreadPoolExecutor();
        return sScheduledThreadPoolExecutor.schedule(task, delay, TimeUnit.MILLISECONDS);
    }
    
    public static void removeTask(Runnable task) {
        if (sScheduledThreadPoolExecutor != null) {
            sScheduledThreadPoolExecutor.remove(task);
        }
    }

    private synchronized static void ensureThreadPoolExecutor() {
        if (sThreadPoolExecutor == null) {
            sThreadPoolExecutor = Executors.newFixedThreadPool(3);
//            sThreadPoolExecutor = new ThreadPoolExecutor(5, 5,
//                    60L, TimeUnit.SECONDS,
//                    new LinkedBlockingQueue<Runnable>(),
//                    Executors.defaultThreadFactory());
        }
    }

    private synchronized static void ensureScheduledThreadPoolExecutor() {
        if (sScheduledThreadPoolExecutor == null) {
            sScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
        }
    }

    public static void shutdown() {
        if (sThreadPoolExecutor != null) {
            sThreadPoolExecutor.shutdown();
            sThreadPoolExecutor = null;
        }

        if (sScheduledThreadPoolExecutor != null) {
            sScheduledThreadPoolExecutor.shutdown();
            sScheduledThreadPoolExecutor = null;
        }
    }
}
