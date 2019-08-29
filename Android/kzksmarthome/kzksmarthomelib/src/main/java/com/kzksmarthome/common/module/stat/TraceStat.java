package com.kzksmarthome.common.module.stat;

import android.content.Context;
import android.text.TextUtils;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.sharedpreference.SharePrefConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TraceStat extends AbstractStat {

    private static TraceStat mInstance;
    private final Object lock = new Object();
    private ConcurrentLinkedQueue<JSONObject> mTraceData = new ConcurrentLinkedQueue<JSONObject>();
    private int mClientTrace = 0; // 采集开关，0为默认不打开， 1为打开

    private TraceStat(Context context) {
        super(context);
        long lastClearTime = SmartHomeAppLib.getInstance().getPreferences()
                .getLong(SharePrefConstant.PREFS_KEY_TRACE_STAT_NUM_LAST_UPDATE_TIME, 0);
        // 上次清零时间距离现在超过1天，要清零
        if (lastClearTime != 0 && System.currentTimeMillis() - lastClearTime > Util.TIME_DAY_MILLIS) {
            SmartHomeAppLib.getInstance().getPreferences().edit()
                    .putInt(SharePrefConstant.PREFS_KEY_TRACE_INFO_CURRENT_NUM, 0).commit();
        }
    }

    public synchronized static TraceStat getInstance() {
        if (mInstance == null) {
            mInstance = new TraceStat(SmartHomeAppLib.getInstance().getContext());
        }
        return mInstance;
    }

    /**
     * 开关是否开启，是否数据正确
     * 
     * @return
     */
    public boolean canTrace(String action) {
        if (mClientTrace == 1 && checkContent(action)) {
            return true;
        }
        return false;
    }

    /**
     * 添加采集数据
     * 
     * @param jsonObject
     */
    public void addStat(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        synchronized (lock) {
            int mTracesNum = SmartHomeAppLib.getInstance().getPreferences()
                    .getInt(SharePrefConstant.PREFS_KEY_TRACE_INFO_CURRENT_NUM, 0);
            // 是否超过100条
            if (mTracesNum < 100) {
                L.d("TraceStat# mTracesNum:%s  action:%s  Content: %s", mTracesNum,
                        jsonObject.optString("action"), jsonObject);
                mTraceData.add(jsonObject);
                mTracesNum++;
                SmartHomeAppLib.getInstance().getPreferences().edit()
                        .putInt(SharePrefConstant.PREFS_KEY_TRACE_INFO_CURRENT_NUM, mTracesNum)
                        .commit();
            }
        }
    }

    private boolean checkContent(String action) {
        if (!TextUtils.isEmpty(action)) {
            return true;
        }
        return false;
    }

    /**
     * 写数据库
     */
    @Override
    protected void flush() {
        if (mTraceData.isEmpty()) {
            return;
        }
        try {
            JSONArray data = new JSONArray();
            synchronized (lock) {
                JSONObject value;
                while ((value = mTraceData.poll()) != null) {
                    data.put(value);
                }
                mTraceData.clear();
            }
            byte[] encryptedData = Util.stringToBytes(data.toString());
            saveTraceStat2DB(encryptedData);
            L.d("TraceStat# flush");
        } catch (Exception e) {
            L.e(e);
        }
    }

    /**
     * 上传数据库里的统计数据
     */
    @Override
    protected void send(int priority) {
        if (priority != PriorityConstants.STAT_PRIORITY_ALL) {
            return;
        }
        try {
            clearExpiredData();
            List<TraceInfo> list = getTraceInfoFromDB();
            if (list != null && list.size() > 0) {
                final JSONArray data = traceInfoList2JSONArray(list);
                if (data.length() > 0) {
                    String ids = genTraceInfoListIds(list);
                    send2Server(data.toString(), ids);
                    L.d("TraceStat# send:%s", data);
                }
            }
        } catch (OutOfMemoryError e) {
            // 数据量过多，抛弃这批日志

        } catch (Exception e) {
            L.e(e);
        }
    }

    /**
     * 从数据库中读取统计数据
     * 
     * @return 统计数据对象列表
     */
    private List<TraceInfo> getTraceInfoFromDB() {
       return null;
    }

    /**
     * 保存统计数据到数据库
     * 
     * @param encryptedData 统计数据
     */
    private void saveTraceStat2DB(byte[] encryptedData) {

    }

    /**
     * 删除过期
     */
    private void clearExpiredData() {

    }

    /**
     * 将数据库中统计信息对象列表转换为JSONArray
     * 
     * @param list 统计信息对象列
     * @return 转换后的List
     * @throws JSONException OutOfMemoryError
     * @throws IOException
     */
    private JSONArray traceInfoList2JSONArray(List<TraceInfo> list) throws JSONException,
            OutOfMemoryError, IOException {
        final JSONArray resultArray = new JSONArray();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (TraceInfo info : list) {
            baos.write(info.data);
            String data = baos.toString();
            baos.reset();
            JSONArray tempArray = new JSONArray(data);
            int length = tempArray.length();
            for (int i = 0; i < length; i++) {
                resultArray.put(tempArray.getJSONObject(i));
            }
        }
        return resultArray;
    }

    /**
     * 删除指定的统计数据列表
     * 
     * @param ids 统计数据对象列表
     */
    private void deleteTraceInfoFromDB(String ids) {

    }

    private String genTraceInfoListIds(List<TraceInfo> list) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');

        for (TraceInfo info : list) {
            sb.append(info._id);
            sb.append(',');
        }

        sb.deleteCharAt(sb.length() - 1).append(')');
        return sb.toString();
    }

    /**
     * 将统计数据对应的字节数组发送给服务器
     * 
     * @param data 要发给服务器的统计数据
     */
    private void send2Server(String data, String ids) {
        // TODO 要发给服务器的统计数据， 发送成功后删除相应db数据
    }
}
