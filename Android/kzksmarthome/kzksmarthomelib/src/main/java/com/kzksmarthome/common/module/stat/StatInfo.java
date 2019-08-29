package com.kzksmarthome.common.module.stat;

public class StatInfo {

    /** 主键 */
    public int _id;

    /** 统计数据 */
    public String data;

    /** 日志优先级 */
    public int priority;

    /** 数据插入时间戳 */
    public long insertTime;

    /** 扩展字段1 */
    public int extend1;

    /** 扩展字段2 */
    public String extend2;

    public StatInfo(int _id, String data, int priority, long insertTime) {
        this._id = _id;
        this.data = data;
        this.priority = priority;
        this.insertTime = insertTime;
    }

    public StatInfo(int _id, String data, int priority, long insertTime, int extend1, String extend2) {
        this(_id, data, priority, insertTime);
        this.extend1 = extend1;
        this.extend2 = extend2;
    }
}
