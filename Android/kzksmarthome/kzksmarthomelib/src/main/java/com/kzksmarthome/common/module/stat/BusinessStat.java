package com.kzksmarthome.common.module.stat;

import android.content.Context;
import android.text.TextUtils;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.okhttp.CacheControlMode;
import com.kzksmarthome.common.lib.okhttp.RequestCallback;
import com.kzksmarthome.common.lib.okhttp.RequestMgr;
import com.kzksmarthome.common.lib.okhttp.ResponseParam;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.common.module.log.LogModuleName;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Kop on 2015/7/22.
 */
public class BusinessStat extends AbstractStat {

    private BusinessStat(Context context) {
        super(context);
    }

    private static BusinessStat mInstance;

    public synchronized static BusinessStat getInstance() {
        if (mInstance == null) {
            mInstance = new BusinessStat(SmartHomeAppLib.getInstance().getContext());
        }
        return mInstance;
    }


    /** 缓存统计日志 */
    private final ConcurrentHashMap<String, Integer> mStatMap = new ConcurrentHashMap<String, Integer>(16, 0.9f, 1);

    /** 访问mStatMap同步锁 */
    private final Object lock = new Object();

    public void addStat(String key, boolean flush) {
        addStat(key);

        if (flush) {
            flush();
        }
    }

    public void addStat(String action, String a1, String a2, String a3) {
        StringBuilder sb = new StringBuilder();
        if (TextUtils.isEmpty(a1)) {
            a1 = "";
        }

        if (TextUtils.isEmpty(a2)) {
            a2 = "";
        }
        if (TextUtils.isEmpty(a3)) {
            a3 = "";
        }
        sb.append(action).append('`').append(a1).append('`').append(a2).append('`').append(a3);
        addStat(sb.toString());
    }

    /**
     * 将一条统计日志保存到内存中
     * @param key
     */
    public void addStat(String key) {
        if (key == null) {
            return;
        }
        synchronized (lock) {
            Integer count = mStatMap.get(key);

            if (count == null) {
                mStatMap.put(key, 1);
                L.d("%s key: %s count: 1", LogModuleName.STAT, key);

            } else {
                mStatMap.put(key, count + 1);
                L.d("%s key: %s count: %d", LogModuleName.STAT, key, count + 1);
            }
        }
    }


    /**
     * 将内存中的统计数据持久化
     */
    protected void flush() {
        if (mStatMap.size() == 0) {
            return;
        }
        try {
            JSONObject highPriorityData = new JSONObject();
            JSONObject lowPriorityData = new JSONObject();
            synchronized (lock) {
                Iterator<String> it = mStatMap.keySet().iterator();

                while (it.hasNext()) {
                    String key = it.next();
                    int count = mStatMap.remove(key);
                    String action = key.substring(0, key.indexOf('`'));
                    if (StatPriorityMapping.isHighPriorityAction(action)) {
                        highPriorityData.put(key, count);
                    } else {
                        lowPriorityData.put(key, count);
                    }

                }
            }

            if (highPriorityData.length() > 0) {
                L.d("%s flush highPriorityData: %s", LogModuleName.STAT, highPriorityData.toString());
            }
            if (lowPriorityData.length() > 0) {
                L.d("%s flush lowPriorityData: %s", LogModuleName.STAT, lowPriorityData.toString());
            }


        } catch (Exception e) {
            L.w(e);
        }
    }

    @Override
    protected void send(int priority) {

    }


    /**
     * 将数据库中统计信息对象列表转换为HashMap，相同key的统计将合并数量
     * @param list 统计信息对象列
     * @return 转换后的HashMap
     * @throws JSONException
     */
    private HashMap<String, Integer> statInfoList2Map(List<StatInfo> list) throws JSONException {
        final HashMap<String, Integer> tempMap = new HashMap<String, Integer>();

        for (StatInfo info : list) {
            JSONObject obj = new JSONObject(info.data);
            L.d("%s send data: %s", LogModuleName.STAT, obj);
            Iterator<?> iterator = obj.keys();

            while (iterator.hasNext()) {
                String key = iterator.next().toString();
                int count = obj.getInt(key);
                Integer savedCount = tempMap.get(key);

                if (savedCount == null) {
                    tempMap.put(key, count);

                } else {
                    tempMap.put(key, count + savedCount.intValue());
                }
            }
        }
        return tempMap;
    }

    /**
     * 将合并后的HashMap用GZIP压缩并用M9加密
     * @param map
     * @return 压缩和加密后的byte数组
     * @throws IOException
     */
    private List<String> map2String(HashMap<String, Integer> map) throws IOException {
        StringBuilder sb = Util.getThreadSafeStringBuilder();

        Set<Map.Entry<String, Integer>> entrySet = map.entrySet();

        List<String> list = new ArrayList<String>(entrySet.size());
        for (Map.Entry<String, Integer> entry : entrySet) {
            sb.append(entry.getKey()).append('`').append(entry.getValue());
            list.add(sb.toString());
            sb.delete(0, sb.length());
        }
        return list;
    }

    /**
     * 将统计数据对应的字节数组发送给服务器
     * @param data 要发给服务器的统计数据，未压缩未加密
     */
    private void send2Server(List<String> data, final String ids) {
        if (data.size() == 0) {
            L.e("%s send no data", LogModuleName.STAT);
            return;
        }

        StatReqBody reqBody = new StatReqBody();
        reqBody.list = data;
        CacheControlMode mode = new CacheControlMode(CacheControlMode.CACHE_REMOTE_NO_CACHE, 0);
        RequestMgr.execute(RequestMgr.Method.POST, "appStat", mode, reqBody,
                StatReqBody.class, Object.class, new RequestCallback() {

                    @Override
                    public void onFailure(Request request, String url, Exception e) {
                    }

                    @Override
                    public void onBizSuccess(ResponseParam response, String url, int from) {

                    }

                    @Override
                    public void onBizFail(ResponseParam response, String url, int from) {
                    }
                });
        //TODO
    }

    class StatReqBody {
        List<String> list;
    }








    protected String genStatInfoListIds(List<StatInfo> list) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');

        for (StatInfo info : list) {
            sb.append(info._id);
            sb.append(',');
        }

        sb.deleteCharAt(sb.length() - 1).append(')');
        return sb.toString();
    }
}
