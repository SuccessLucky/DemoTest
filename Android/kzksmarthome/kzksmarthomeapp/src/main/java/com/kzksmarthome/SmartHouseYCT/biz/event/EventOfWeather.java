package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 天气event
 * Created by jack on 2016/9/24.
 */
public class EventOfWeather implements Parcelable {
    public  String responseStr;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.responseStr);
    }

    public EventOfWeather() {
    }

    protected EventOfWeather(Parcel in) {
        this.responseStr = in.readString();
    }

    public static final Creator<EventOfWeather> CREATOR = new Creator<EventOfWeather>() {
        @Override
        public EventOfWeather createFromParcel(Parcel source) {
            return new EventOfWeather(source);
        }

        @Override
        public EventOfWeather[] newArray(int size) {
            return new EventOfWeather[size];
        }
    };
}
