package com.kzksmarthome.common.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 远程连接登记
 * @author lizhid
 * @version V1.0
 * @description:
 * @date 2016/11/2
 */
public class EventOfRegisterRotme implements Parcelable {


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public EventOfRegisterRotme() {
    }

    protected EventOfRegisterRotme(Parcel in) {
    }

    public static final Parcelable.Creator<EventOfRegisterRotme> CREATOR = new Parcelable.Creator<EventOfRegisterRotme>() {
        @Override
        public EventOfRegisterRotme createFromParcel(Parcel source) {
            return new EventOfRegisterRotme(source);
        }

        @Override
        public EventOfRegisterRotme[] newArray(int size) {
            return new EventOfRegisterRotme[size];
        }
    };
}
