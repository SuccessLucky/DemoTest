package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 更新设备列表的通知
 * Created by jack on 2016/11/6.
 */

public class EventOfUpdateDeviceList implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public EventOfUpdateDeviceList() {
    }

    protected EventOfUpdateDeviceList(Parcel in) {
    }

    public static final Creator<EventOfUpdateDeviceList> CREATOR = new Creator<EventOfUpdateDeviceList>() {
        @Override
        public EventOfUpdateDeviceList createFromParcel(Parcel source) {
            return new EventOfUpdateDeviceList(source);
        }

        @Override
        public EventOfUpdateDeviceList[] newArray(int size) {
            return new EventOfUpdateDeviceList[size];
        }
    };
}
