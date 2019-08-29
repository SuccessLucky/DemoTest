package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.GatewayInfo;

public class EventOfResultSelectGw implements Parcelable {
    /**
     * 选中网关
     */
    public GatewayInfo gwInfo;
    /**
     * 是否是添加网关
     */
    public boolean isAddGW = false;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.gwInfo, flags);
        dest.writeByte(this.isAddGW ? (byte) 1 : (byte) 0);
    }

    public EventOfResultSelectGw() {
    }

    protected EventOfResultSelectGw(Parcel in) {
        this.gwInfo = in.readParcelable(GatewayInfo.class.getClassLoader());
        this.isAddGW = in.readByte() != 0;
    }

    public static final Creator<EventOfResultSelectGw> CREATOR = new Creator<EventOfResultSelectGw>() {
        @Override
        public EventOfResultSelectGw createFromParcel(Parcel source) {
            return new EventOfResultSelectGw(source);
        }

        @Override
        public EventOfResultSelectGw[] newArray(int size) {
            return new EventOfResultSelectGw[size];
        }
    };
}
