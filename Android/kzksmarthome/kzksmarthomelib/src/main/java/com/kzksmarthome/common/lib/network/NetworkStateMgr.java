package com.kzksmarthome.common.lib.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.telephony.TelephonyManager;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.module.log.L;

/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate Dec 29, 2014
 *
 */
public class NetworkStateMgr {

    private static NetworkStateMgr mInstance;
    
    private NetworkState mNetworkState;
    private NetworkStateBroadcastReceiver mNetworkBroadcastReceiver;

    public synchronized static NetworkStateMgr getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkStateMgr();
        }
        return mInstance;
    }

    private NetworkStateMgr() {
        Context app = SmartHomeAppLib.getInstance().getContext();
        if (SmartHomeAppLib.getInstance().isForeProcess()) {
            //前台进程注册EventBus接收广播
            GjjEventBus.getInstance().register(this);
//            GjjApp.getInstance().getMessagePump().register(Message.Type.NETWORK_STATE_CHANGED, this);
        } else if (SmartHomeAppLib.getInstance().isBackProcess()) {
            //后台进程注册广播监听网络变化，如果收到变化，则用EventBus广播给前台
            mNetworkBroadcastReceiver = new NetworkStateBroadcastReceiver();
            app.registerReceiver(mNetworkBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }
    
    public void onEventBackgroundThread(NetworkState event) {
        mNetworkState = event;
    }

    class NetworkStateBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mNetworkState = getNetworkStateForce(SmartHomeAppLib.getInstance().getContext());
            //send this both to foreground and background
//            BackProcMessenger.getInstance().send(Message.Type.NETWORK_STATE_CHANGED.ordinal(), mNetworkState);
//            GjjApp.getInstance().getMessagePump().broadcastMessage(Message.Type.NETWORK_STATE_CHANGED, mNetworkState, Message.PRIORITY_HIGH);
            GjjEventBus.getInstance().post((Parcelable)mNetworkState, true);
        }
    }

    public NetworkState getNetworkState() {
        if (mNetworkState == null) {
            mNetworkState = getNetworkStateForce(SmartHomeAppLib.getInstance().getContext());
        }
        return mNetworkState;
    }

    public boolean isNetworkAvailable() {
        return getNetworkState().ordinal() != NetworkState.UNAVAILABLE.ordinal();
    }
    
    /**
     * 注意，因为EventBus注册有先后，故无法保证接受顺序；如果有注册EventBus监听NetworkState，
     * 这时收到事件，想判断网络是否可用，需调用此接口
     * @param networkState
     */
    public static boolean isThisNetworkAvailable(NetworkState networkState) {
        return networkState.ordinal() != NetworkState.UNAVAILABLE.ordinal();
    }

    private NetworkState getNetworkStateForce(Context context) {
        NetworkState state = null;
        NetworkInfo info = null;

        try {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            info = manager.getActiveNetworkInfo();

            if (info == null) {// android2.2 sometimes getActiveNetworkInfo always return null, but network is available
                NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if (wifiInfo != null && wifiInfo.isAvailable()) {
                    info = wifiInfo;
                } else if (mobileInfo != null && mobileInfo.isAvailable()) {
                    info = mobileInfo;
                }
            }

            if (info != null && (info.isConnectedOrConnecting() || info.isRoaming())) {
                if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                    state = NetworkState.WIFI;
                }

                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    if (info.getSubtype() <= 4) {
                        state = isWapConnection(info.getExtraInfo()) ? NetworkState.NET_2G_WAP
                                : NetworkState.NET_2G; // ~ 50-100 kbps

                    } else {
                        state = NetworkState.NET_3G;
                    }
                }
            }

        } catch (Exception e) {
            L.w(e);
        }

        if (state == null) {
            state = NetworkState.UNAVAILABLE;
        }

        if (state == NetworkState.WIFI) {
            state.setExtra("wifi");
        } else if (info != null) {
            state.setExtra(getExtra(info));
        }

        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String operator = tm.getNetworkOperatorName();

        if (operator == null || operator.length() == 0) {
            state.setOperator("unknown");
        } else {
            state.setOperator(operator);
        }

        return state;
    }

    private static String getExtra(NetworkInfo info) {
        String extra = info.getExtraInfo();

        if (extra == null) {
            extra = info.getSubtypeName();
        }

        return extra;
    }

    private static boolean isWapConnection(String extraInfo) {
        return extraInfo != null && extraInfo.contains("wap");
    }
}
