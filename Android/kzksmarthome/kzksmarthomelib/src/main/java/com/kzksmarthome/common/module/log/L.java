package com.kzksmarthome.common.module.log;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.ConsoleMessage.MessageLevel;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.util.Util;

/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate Dec 29, 2014
 *
 */
public class L {
    // webview打log的各种等级
    private static final int WEB_LEVEL_DEBUG = MessageLevel.DEBUG.ordinal();
    private static final int WEB_LEVEL_LOG = MessageLevel.LOG.ordinal();
    private static final int WEB_LEVEL_TIP = MessageLevel.TIP.ordinal();
    private static final int WEB_LEVEL_WARNING = MessageLevel.WARNING.ordinal();
    private static final int WEB_LEVEL_ERROR = MessageLevel.ERROR.ordinal();
    
    public static void d(String message, Object...args) {
        boolean canPrint = LogManager.canPrint(LogManager.LOG_LEVEL_DEBUG);
        boolean canSave = LogManager.canSave(LogManager.LOG_LEVEL_DEBUG);
        if (canPrint || canSave) {
            message = formatMessage(message, args);
            if (canPrint) {
                Log.d(getTag(), message);
            }
            if (canSave) {
                save(LogManager.LOG_LEVEL_DEBUG, getTag(), message);
            }
        }
    }

    public static void i(String message, Object...args) {
        boolean canPrint = LogManager.canPrint(LogManager.LOG_LEVEL_INFO);
        boolean canSave = LogManager.canSave(LogManager.LOG_LEVEL_INFO);
        if (canPrint || canSave) {
            message = formatMessage(message, args);
            if (canPrint) {
                Log.i(getTag(), message);
            }
            if (canSave) {
                save(LogManager.LOG_LEVEL_INFO, getTag(), message);
            }
        }
        
    }

    public static void w(String message, Object...args) {
        boolean canPrint = LogManager.canPrint(LogManager.LOG_LEVEL_WARN);
        boolean canSave = LogManager.canSave(LogManager.LOG_LEVEL_WARN);
        if (canPrint || canSave) {
            message = formatMessage(message, args);
            if (canPrint) {
                Log.w(getTag(), message);
            }
            if (canSave) {
                save(LogManager.LOG_LEVEL_WARN, getTag(), message);
            }
        }
    }
    
    public static void w(Throwable e) {
        if (LogManager.canPrint(LogManager.LOG_LEVEL_WARN)) {
            e.printStackTrace();
        }
        if (LogManager.canSave(LogManager.LOG_LEVEL_WARN)) {
            save(LogManager.LOG_LEVEL_WARN, getTag(), e.toString());
        }
    }

    public static void e(String message, Object...args) {
        boolean canPrint = LogManager.canPrint(LogManager.LOG_LEVEL_ERROR);
        boolean canSave = LogManager.canSave(LogManager.LOG_LEVEL_ERROR);
        if (canPrint || canSave) {
            message = formatMessage(message, args);
            if (canPrint) {
                Log.e(getTag(), message);
            }
            if (canSave) {
                save(LogManager.LOG_LEVEL_ERROR, getTag(), message);
            }
        }
    }
    
    public static void e(Throwable e) {
        if (LogManager.canPrint(LogManager.LOG_LEVEL_ERROR)) {
            e.printStackTrace();
        }
        if (LogManager.canSave(LogManager.LOG_LEVEL_ERROR)) {
            save(LogManager.LOG_LEVEL_ERROR, getTag(), e.toString());
        }
    }

    public static void v(String message, Object...args) {
        boolean canPrint = LogManager.canPrint(LogManager.LOG_LEVEL_TRACE);
        boolean canSave = LogManager.canSave(LogManager.LOG_LEVEL_TRACE);
        if (canPrint || canSave) {
            message = formatMessage(message, args);
            if (canPrint) {
                Log.v(getTag(), message);
            }
            if (canSave) {
                save(LogManager.LOG_LEVEL_TRACE, getTag(), message);
            }
        }
    }
    
    public static void t(String message, Object...args) {
        v(message, args);
    }
    
    public static void f(String message, Object...args) {
        boolean canPrint = LogManager.canPrint(LogManager.LOG_LEVEL_FATAL);
        boolean canSave = LogManager.canSave(LogManager.LOG_LEVEL_FATAL);
        if (canPrint || canSave) {
            message = formatMessage(message, args);
            if (canPrint) {
                Log.e(getTag(), message);
            }
            if (canSave) {
                save(LogManager.LOG_LEVEL_FATAL, getTag(), message);
            }
        }
    }
    
    public static void s(String message, Object...args) {
        boolean canPrint = LogManager.canPrint(LogManager.LOG_LEVEL_STAT);
        boolean canSave = LogManager.canSave(LogManager.LOG_LEVEL_STAT);
        if (canPrint || canSave) {
            message = formatMessage(message, args);
            if (canPrint) {
                Log.i(getTag(), message);
            }
            if (canSave && DebugStatSimpleRate.canStat(message)) {
                save(LogManager.LOG_LEVEL_STAT, getTag(), message);
            }
        }
        
    }
    
    private static String formatMessage(String message, Object...args) {
        if (message == null) {
            return "";
        }
        if (args != null && args.length > 0) {
            try {
                return String.format(message, args);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return message;
    }
    
    /**
     * 是否处于调试级别
     * @return
     */
    public static boolean isDebugMode() {
        return LogManager.isDebugMode();
    }
    /**
     * 是否为Money调试阶段
     * @return
     */
    public static boolean isMonkeyMode(){
        return LogManager.isMonkeyMode();
    }
    
    /**
     * 是否上传测试
     * 
     * @return
     */
    public static boolean isUploadTest(){
        return LogManager.isUploadTest();
    }
    
    /**
     * 处理h5日志
     * @param consoleMessage
     */
    public static void handleConsoleMessage(ConsoleMessage consoleMessage) {
        int level = webLevel2LogLevel(consoleMessage.messageLevel().ordinal());
        if (LogManager.canPrint(level)) {
            print(level, getH5Tag(consoleMessage), consoleMessage.message());
        }
        // H5日志暂时不上传
        /*if (LogManager.canSave(level)) {
            save(level, getH5Tag(consoleMessage), consoleMessage.message());
        }*/
    }
    
    /**
     * 获取native日志tag
     * @return
     */
    private static String getTag() {
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[4];

        String className = stackTrace.getClassName();
        StringBuilder builder = Util.getThreadSafeStringBuilder();
        builder.append(className.substring(className.lastIndexOf('.') + 1)).append('.')
                .append(stackTrace.getMethodName()).append('#').append(stackTrace.getLineNumber());
        return builder.toString();
    }
    
    /**
     * 获取h5日志的tag
     * @param consoleMessage
     * @return
     */
    private static String getH5Tag(ConsoleMessage consoleMessage) {
        String tag = consoleMessage.sourceId();
        if(!TextUtils.isEmpty(tag)){
            try {
                tag = tag.replace("/data/data/" + SmartHomeAppLib.getInstance().getContext().getPackageName() + "/", "");
                tag = String.format("H5[%s](%d)", tag, consoleMessage.lineNumber());
            } catch (Exception e) {
                L.w("getH5Tag Exception " + e);
                tag = "H5";
            }
        } else{
            L.w("H5Page# tag is null");
            tag = "H5";
        }

        return tag;
    }
    
    /**
     * 打印到std
     * @param level
     * @param tag
     * @param message
     */
    private static void print(int level, String tag, String message) {
        if (message == null) {
            message = "";
        }
        switch (level) {
        case Log.VERBOSE:
            Log.v(tag, message);
            break;

        case Log.DEBUG:
            Log.d(tag, message);
            break;

        case Log.INFO:
            Log.i(tag, message);
            break;

        case Log.WARN:
            Log.w(tag, message);
            break;

        case Log.ERROR:
            Log.e(tag, message);
            break;

        }
    }
    
    /**
     * 获取level对应的文字描述
     * @param level
     * @return
     */
    public static String getLevelString(int level) {
        switch (level) {
        case LogManager.LOG_LEVEL_TRACE:
            return "TRACE";

        case LogManager.LOG_LEVEL_DEBUG:
            return "DEBUG";

        case LogManager.LOG_LEVEL_INFO:
            return "INFO";

        case LogManager.LOG_LEVEL_WARN:
            return "WARN";

        case LogManager.LOG_LEVEL_ERROR:
            return "ERROR";
            
        case LogManager.LOG_LEVEL_FATAL:
            return "FATAL";
            
        case LogManager.LOG_LEVEL_STAT:
            return "STAT";
            
        default:
            return "";
        }
    }
    
    /**
     * 保存到发送队列
     * @param level
     * @param tag
     * @param message
     */
    private static void save(int level, String tag, String message) {
//        DebugStat.getInstance().addStat(System.currentTimeMillis(), level, tag, message);
    }
    
    /**
     * 将h5日志级别转换为Android日志级别
     * @param webLevel
     * @return
     */
    private static int webLevel2LogLevel(int webLevel) {
        if (webLevel == WEB_LEVEL_LOG) {
            return LogManager.LOG_LEVEL_INFO;
        } else if (webLevel == WEB_LEVEL_WARNING) {
            return LogManager.LOG_LEVEL_WARN;
        } else if (webLevel == WEB_LEVEL_ERROR) {
            return LogManager.LOG_LEVEL_ERROR;
        } else if (webLevel == WEB_LEVEL_TIP || webLevel == WEB_LEVEL_DEBUG) {
            return LogManager.LOG_LEVEL_DEBUG;
        } else {
            return 0;
        }
    }
    
    public static boolean canUploadServer(int level) {
        return LogManager.canUpload(level);
    }
}
