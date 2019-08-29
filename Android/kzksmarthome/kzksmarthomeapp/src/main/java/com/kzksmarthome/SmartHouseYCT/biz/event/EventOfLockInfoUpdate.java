package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.common.lib.tools.DeviceState;

/**
 * 锁上报
 * Created by jack on 2016/11/3.
 */

public class EventOfLockInfoUpdate implements Parcelable {
    public DeviceState deviceState;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.deviceState);
    }

    public EventOfLockInfoUpdate() {
    }

    protected EventOfLockInfoUpdate(Parcel in) {
        this.deviceState = (DeviceState) in.readSerializable();
    }

    public static final Creator<EventOfLockInfoUpdate> CREATOR = new Creator<EventOfLockInfoUpdate>() {
        @Override
        public EventOfLockInfoUpdate createFromParcel(Parcel source) {
            return new EventOfLockInfoUpdate(source);
        }

        @Override
        public EventOfLockInfoUpdate[] newArray(int size) {
            return new EventOfLockInfoUpdate[size];
        }
    };
}
