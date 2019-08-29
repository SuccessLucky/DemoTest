package com.kzksmarthome.common.module.alarm;

/**
 * 实现功能：
 */
public interface IAlarmEvent {

    /**
     * 检测是否需要触发该闹钟事件
    *@param type 闹钟事件类型
    *@return
     */
    boolean checkTime(int type);
    
    /**
     * 处理闹钟事件
    *@param type 闹钟事件类型
     */
    void handleAlarmEvent(int type);
    
}
