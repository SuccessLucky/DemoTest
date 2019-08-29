package com.kzksmarthome.common.event;


import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.common.lib.tools.DeviceState;

/**
 * TCP请求返回
 * Created by jack on 2016/9/17.
 */
public class EventOfTcpResult implements Parcelable {
  public DeviceState deviceState;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.deviceState);
    }

    public EventOfTcpResult() {
    }

    protected EventOfTcpResult(Parcel in) {
        this.deviceState = (DeviceState) in.readSerializable();
    }

    public static final Creator<EventOfTcpResult> CREATOR = new Creator<EventOfTcpResult>() {
        @Override
        public EventOfTcpResult createFromParcel(Parcel source) {
            return new EventOfTcpResult(source);
        }

        @Override
        public EventOfTcpResult[] newArray(int size) {
            return new EventOfTcpResult[size];
        }
    };
}
