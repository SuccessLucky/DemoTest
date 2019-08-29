package com.kzksmarthome.common.lib.easylink;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.event.EventOfEasyLinkSearchEnd;
import com.kzksmarthome.common.lib.easylink.bean.GatewayWifi;
import com.kzksmarthome.common.lib.easylink.data.MessageFlag;
import com.kzksmarthome.common.lib.easylink.data.TempData;
import com.kzksmarthome.common.lib.easylink.utils.CommandDataUtil;
import com.kzksmarthome.common.lib.easylink.utils.news.DataTypeUtil;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.task.ForegroundTaskExecutor;
import com.kzksmarthome.common.lib.task.MainTaskExecutor;
import com.kzksmarthome.common.module.log.L;
import com.kzksmarthome.lib.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * easyLink搜索类
 * Created by lizhi on 2017/11/10.
 */

public class EasyLinkSearchTool {

    private boolean flag; //按钮的状态
    private boolean TaskIsStart; //网关搜索是否开启的标志
    private FirstTimeConfig firstConfig;  //easylink首次设置
    private EasyLinkWifiManager mWifiManager;
    private MulticastSocket multicastSocket; //ip组播端口
    private InetAddress serverAddress; //ip组播地址
    private List<GatewayWifi> gatewayWifiList;

    private int sum = 0;  //累计搜索到相同网关的次数
    private int max = 12;  //最大次数

    private static EasyLinkSearchTool easyLinkSearchTool = new EasyLinkSearchTool();

    private EasyLinkSearchTool() {
    }

    public static EasyLinkSearchTool getInstance() {
        return easyLinkSearchTool;
    }

    public Runnable sendRunnable = null;
    public Runnable receiveRunnable = null;

    private Handler handler = null;


    /**
     * 发送广播数据
     */
    public void sendMultiCastData() {
        sendRunnable = new Runnable() {
            @Override
            public void run() {
                byte[] data = DataTypeUtil.hexStringToBytes(TempData.GetGatewayData);
                while (TaskIsStart) {
                    if(multicastSocket == null || multicastSocket.isClosed()){
                        return;
                    }
                    DatagramPacket dp = new DatagramPacket(data, 0, data.length, serverAddress, TempData.GatewayBoardcastPort);
                    try {
                        multicastSocket.send(dp);
                        Thread.sleep(2500);
                    } catch (Exception e) {
                        closeMultiCastSocket();
                        e.printStackTrace();
                    }
                }
            }
        };
        ForegroundTaskExecutor.executeTask(sendRunnable);
    }

    /**
     * 接收广播数据
     */
    private void receiverMultiCastData() {
        receiveRunnable = new Runnable() {
            @Override
            public void run() {
                L.w("网关max-----------------------" + max);
                byte[] data = new byte[1024];
                while (TaskIsStart) {
                    DatagramPacket dp = new DatagramPacket(data, data.length);
                    try {
                        if(multicastSocket == null || multicastSocket.isClosed()){
                            return;
                        }
                        multicastSocket.receive(dp);
                        Log.e("hkf", DataTypeUtil.bytes2HexString(dp.getData(), dp.getData().length));
                        if (!DataTypeUtil.bytes2HexString(dp.getData(), dp.getData().length).contains(TempData.GetGatewayData)) {
                            String s = DataTypeUtil.bytes2HexString(dp.getData(), dp.getData().length);
                            GatewayWifi gateway = CommandDataUtil.getIntance().getGatewayByString(s);
                            Log.e("hkf", gateway == null ? "" : gateway.toString());
                            boolean f = true;
                            if (gateway == null) {
                                if (sum >= max) {
                                    Message msg = new Message();
                                    msg.what = MessageFlag.WAITOVER;
                                    handler.sendMessage(msg);
                                } else {
                                    sum++;
                                }
                            } else {
                                for (int i = 0; i < gatewayWifiList.size(); i++) {
                                    if (gateway != null && gatewayWifiList.get(i).getIp().equals(gateway.getIp())) {
                                        f = false;
                                        if (sum >= max) {
                                            Message msg = new Message();
                                            msg.what = MessageFlag.WAITOVER;
                                            handler.sendMessage(msg);
                                        } else {
                                            sum++;
                                        }
                                        break;
                                    }
                                }
                                if (f) {
                                    Message msg = new Message();
                                    //gateway = setZbMac(gateway);
                                    msg.obj = gateway;
                                    msg.what = MessageFlag.ADD_GATEWAY;
                                    handler.sendMessage(msg);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        closeMultiCastSocket();
                    }
                }
            }
        };

        ForegroundTaskExecutor.executeTask(receiveRunnable);
    }

    /**
     * 关闭发送线程
     */
    public void closeSendThread() {
        if (sendRunnable != null) {
            sendRunnable = null;
        }
    }

    /**
     * 关闭接收线程
     */
    public void closeReceiverThread() {
        if (receiveRunnable != null) {
            receiveRunnable = null;
        }
    }


    /**
     * 接收数据
     */
    public void createHandler() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MessageFlag.ADD_GATEWAY:
                        GatewayWifi gateway = (GatewayWifi) msg.obj;
                        gatewayWifiList.add(gateway);
                        break;
                    case MessageFlag.WAITOVER:
                        Log.e("FirstActivity", "WAITOVER");
                        stopSearch();
                        break;
                }
            }
        };
    }

    /**
     * 启动搜索
     */
    public void startSearch(int count) {
        this.max = count;
        MainTaskExecutor.scheduleTaskOnUiThread(5000, new Runnable() {
            @Override
            public void run() {
                SmartHomeAppLib.showToastLongTime(R.string.search_iot_str);
                closeMultiCastSocket();
                try {
                    multicastSocket = new MulticastSocket(TempData.GatewayBoardcastPort);
                    serverAddress = InetAddress.getByName(TempData.GatewayBoardcastIp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (gatewayWifiList != null) {
                    gatewayWifiList.clear();
                    gatewayWifiList = null;
                }
                gatewayWifiList = new ArrayList<GatewayWifi>();
                TaskIsStart = true;
//        ForegroundTaskExecutor.executeTask(sendRunnable);
//        ForegroundTaskExecutor.executeTask(receiveRunnable);
                createHandler();
                sendMultiCastData();
                receiverMultiCastData();
            }
        });
    }

    /**
     * 停止搜索
     */
    public void stopSearch() {
        sum = 0;
        TaskIsStart = false;
        try {
            closeMultiCastSocket();
            closeSendThread();
            closeReceiverThread();
            if(gatewayWifiList != null){
                L.w("网关列表-----------------------数量："+gatewayWifiList.size()+"----"+gatewayWifiList.toString());
            }
            EventOfEasyLinkSearchEnd eventOfEasyLinkSearchEnd = new EventOfEasyLinkSearchEnd();
            eventOfEasyLinkSearchEnd.setGatewayWifiList(gatewayWifiList);
            GjjEventBus.getInstance().post(eventOfEasyLinkSearchEnd);
            handler = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭广播连
     */
    private void closeMultiCastSocket() {
        if (multicastSocket != null) {
            multicastSocket.close();
            multicastSocket = null;
        }
    }

}
