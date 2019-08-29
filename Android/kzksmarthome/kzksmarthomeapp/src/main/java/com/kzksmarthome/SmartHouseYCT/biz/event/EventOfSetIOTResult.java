package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 网关配置成功
 * Created by jack on 2016/9/17.
 */
public class EventOfSetIOTResult implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public EventOfSetIOTResult() {
    }

    protected EventOfSetIOTResult(Parcel in) {
    }

    public static final Creator<EventOfSetIOTResult> CREATOR = new Creator<EventOfSetIOTResult>() {
        @Override
        public EventOfSetIOTResult createFromParcel(Parcel source) {
            return new EventOfSetIOTResult(source);
        }

        @Override
        public EventOfSetIOTResult[] newArray(int size) {
            return new EventOfSetIOTResult[size];
        }
    };
}
