package com.kzksmarthome.common.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.common.lib.easylink.bean.GatewayWifi;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索结束
 * Created by lizhi on 2017/11/10.
 */

public class EventOfEasyLinkSearchEnd implements Parcelable {
    /**
     * 网关列表
     */
    private List<GatewayWifi> gatewayWifiList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.gatewayWifiList);
    }

    public EventOfEasyLinkSearchEnd() {
    }

    protected EventOfEasyLinkSearchEnd(Parcel in) {
        this.gatewayWifiList = new ArrayList<GatewayWifi>();
        in.readList(this.gatewayWifiList, GatewayWifi.class.getClassLoader());
    }

    public static final Creator<EventOfEasyLinkSearchEnd> CREATOR = new Creator<EventOfEasyLinkSearchEnd>() {
        @Override
        public EventOfEasyLinkSearchEnd createFromParcel(Parcel source) {
            return new EventOfEasyLinkSearchEnd(source);
        }

        @Override
        public EventOfEasyLinkSearchEnd[] newArray(int size) {
            return new EventOfEasyLinkSearchEnd[size];
        }
    };

    public List<GatewayWifi> getGatewayWifiList() {
        return gatewayWifiList;
    }

    public void setGatewayWifiList(List<GatewayWifi> gatewayWifiList) {
        this.gatewayWifiList = gatewayWifiList;
    }
}
