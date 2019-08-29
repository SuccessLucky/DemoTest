package com.kzksmarthome.common.module.stat;

import org.json.JSONObject;

import com.kzksmarthome.common.module.log.L;

public class TraceInfo {

    /** 主键 */
    public long _id;

    /** 统计数据 */
    public byte[] data;

    /** 日志优先级 */
    public int priority;

    /** 数据插入时间戳 */
    public long insertTime;

    /** 扩展字段1 */
    public int extend1;

    /** 扩展字段2 */
    public String extend2;

    public TraceInfo(long _id, byte[] data, int priority, long insertTime) {
        this._id = _id;
        this.data = data;
        this.priority = priority;
        this.insertTime = insertTime;
    }

    public TraceInfo(long _id, byte[] data, int priority, long insertTime, int extend1,
            String extend2) {
        this(_id, data, priority, insertTime);
        this.extend1 = extend1;
        this.extend2 = extend2;
    }

    public static JSONObject buildHandleMsgTimeInfo(long rtime, long ctime, int minterval,
            String simpleClass) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rtime", rtime);
            jsonObject.put("ctime", ctime);
            jsonObject.put("minterval", minterval);
            jsonObject.put("simpleClass", simpleClass);
        } catch (Exception e) {
            L.w(e);
            return null;
        }
        return jsonObject;
    }
}
