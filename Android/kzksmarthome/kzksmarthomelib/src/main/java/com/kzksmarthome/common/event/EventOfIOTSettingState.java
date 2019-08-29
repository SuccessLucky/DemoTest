package com.kzksmarthome.common.event;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 网关设置返回
 * Created by jack on 2016/9/17.
 */
public class EventOfIOTSettingState implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public EventOfIOTSettingState() {
    }

    protected EventOfIOTSettingState(Parcel in) {
    }

    public static final Creator<EventOfIOTSettingState> CREATOR = new Creator<EventOfIOTSettingState>() {
        @Override
        public EventOfIOTSettingState createFromParcel(Parcel source) {
            return new EventOfIOTSettingState(source);
        }

        @Override
        public EventOfIOTSettingState[] newArray(int size) {
            return new EventOfIOTSettingState[size];
        }
    };
}
