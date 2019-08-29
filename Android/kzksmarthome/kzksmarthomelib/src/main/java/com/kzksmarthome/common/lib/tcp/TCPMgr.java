package com.kzksmarthome.common.lib.tcp;

import android.content.Context;
import android.util.Log;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.event.EventOfChangeApiHost;
import com.kzksmarthome.common.event.EventOfCleanDataCache;
import com.kzksmarthome.common.event.EventOfTcpResult;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.network.NetworkState;
import com.kzksmarthome.common.lib.tools.BusinessTool;
import com.kzksmarthome.common.lib.tools.DeviceState;
import com.kzksmarthome.common.lib.tools.Tools;
import com.kzksmarthome.common.lib.util.DataCleanMgr;
import com.kzksmarthome.common.module.log.L;

import java.io.InputStream;
import java.util.ArrayList;

public class TCPMgr extends BaseSocketMgr {
    /**
     * 单例实现，参考
     * http://www.iteye.com/topic/344876
     * http://www.iteye.com/topic/260515
     * http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
     * http://blog.csdn.net/lufeng20/article/details/24314381
     */
    private static volatile TCPMgr mInstance;

    public static TCPMgr getInstance() {
        if (mInstance == null) {
            synchronized (TCPMgr.class) {
                if (mInstance == null) {
                    mInstance = new TCPMgr();
                    mInstance.init();
                }
            }
        }
        return mInstance;
    }

    private void init() {
        GjjEventBus.getInstance().register(this);
    }
    public void onEventBackgroundThread(NetworkState networkState) {
        L.d("Request# TCPMgr onEventBackgroundThread networkState: %s ", networkState);
        checkAndConnect(false, false);
    }

    public void onEventBackgroundThread(EventOfChangeApiHost event) {
        L.d("Request# TCPMgr onEventBackgroundThread event: %s ", event);

        //切换服务器后清除用户信息和缓存信息
//        GjjAppLib.getUserMgr().logOut();
        Context app = SmartHomeAppLib.getInstance().getContext();
        DataCleanMgr.cleanExternalCache(app);
        DataCleanMgr.cleanExternalFiles(app);
//        GjjAppLib.getInstance().getPreferences().edit().clear().commit();
        GjjEventBus.getInstance().post(new EventOfCleanDataCache(), false);
        retryConnect();
    }

    /**
     * 对接收的数据分包
     */
    @Override
    protected void splitPackage(InputStream is) {
        try {
            byte[] bytes = new byte[1024];
            int result = 0;
            if ((result = is.read(bytes)) > 0) {
                System.out.println(new String(bytes));
                byte[] resultBytes = new byte[result];
                System.arraycopy(bytes, 0, resultBytes, 0, result);
                Log.d("AddDevice","返回--原始--数据："+Tools.byte2HexStr(resultBytes));
                if (!Tools.isNeedSpilt(resultBytes)) {
                    Log.d("AddDevice","返回数据："+Tools.byte2HexStr(resultBytes));
                    DeviceState deviceState = BusinessTool.getInstance().resolveData(resultBytes);
                    EventOfTcpResult eventOfTcpResult = new EventOfTcpResult();
                    eventOfTcpResult.deviceState = deviceState;
                    GjjEventBus.getInstance().post(eventOfTcpResult,true);
                } else {
                    ArrayList<byte[]> splitDatas = Tools.splitData(resultBytes);
                    if (splitDatas != null) {
                        for (byte[] datas : splitDatas) {
                            Log.d("AddDevice","返回数据："+Tools.byte2HexStr(datas));
                            DeviceState deviceState = BusinessTool.getInstance().resolveData(datas);
                            EventOfTcpResult eventOfTcpResult = new EventOfTcpResult();
                            eventOfTcpResult.deviceState = deviceState;
                            GjjEventBus.getInstance().post(eventOfTcpResult,true);
                        }
                    }
                }
            }
        } catch (Exception e) {
            L.e(e);
        } catch (OutOfMemoryError e) {
            L.e(e);
        }
//        callback.onSplitFinishBackground(null, null);
    }



}
