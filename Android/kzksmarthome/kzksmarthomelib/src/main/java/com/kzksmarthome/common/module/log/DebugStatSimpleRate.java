package com.kzksmarthome.common.module.log;

import java.util.HashMap;
import java.util.Random;

import android.text.TextUtils;

/**
 * 通过L.s统计的采样频率
 * @author panrq
 *
 */
public class DebugStatSimpleRate {
    private static final HashMap<String, Integer> DebugStatSimpleRateMap = new HashMap<String, Integer>(2);
    
    /**
     * 日志采用随机数
     */
    private static final Random mStatRandom = new Random();
    
    static {
        DebugStatSimpleRateMap.put(LogModuleName.DOWNLOAD, 1);
    }
    
    /**
     * 根据message获取日志的采样频率，1表示1%，100表示100%，如果在映射表没找到则返回0
     * @param message
     * @return
     */
    private static int getSimpleRate(String message) {
        if (!TextUtils.isEmpty(message)) {
            int index = message.indexOf('#');
            if (index > 0) {
                String action = message.substring(0, index) + "#";
                Integer priority = DebugStatSimpleRateMap.get(action);
                if (priority == null) {
                    return 0;
                } else {
                    return priority.intValue();
                }
            }
        }
        return 0;
    }
    
    /**
     * 根据采用频率，返回是否采样
     * @param message
     * @return
     */
    public static boolean canStat(String message) {
        return mStatRandom.nextInt(100) < DebugStatSimpleRate.getSimpleRate(message);
    }
}
