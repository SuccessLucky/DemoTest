package com.kzksmarthome.common.module.stat;

import java.util.HashMap;
import java.util.Map;

public class StatPriorityMapping {

    /** 统计日志优先级别-所有*/
    public static final int STAT_PRIORITY_ALL = 1;
    /** 统计日志优先级别-高*/
    public static final int STAT_PRIORITY_HIGH = 2;
    /** 统计日志优先级别-低*/
    public static final int STAT_PRIORITY_LOW = 3;

    /** 将高优先级到action映射为STAT_PRIORITY_HIGHT */
    private static final Map<String, Integer> map = new HashMap<String, Integer>();
    static {
        map.put("login", STAT_PRIORITY_HIGH);
        map.put("register", STAT_PRIORITY_HIGH);
        map.put("launch", STAT_PRIORITY_HIGH);
    }
    
    /**
     * 根据action查询日志的优先级，如果在映射表没找到则是低优先级
     * @param action
     * @return
     */
    public static int getStatPriorityByAction(String action) {
        Integer priority = map.get(action);
        if (priority == null) {
            return PriorityConstants.STAT_PRIORITY_LOW;
        } else {
            return priority;
        }
    }
    
    /**
     * 根据action判断日志是否是高优先级，如果在映射表没找到则是低优先级
     * @param action
     * @return
     */
    public static boolean isHighPriorityAction(String action) {
        Integer priority = map.get(action);
        if (priority == null) {
            return false;
        } else {
            return true;
        }
    }
}
