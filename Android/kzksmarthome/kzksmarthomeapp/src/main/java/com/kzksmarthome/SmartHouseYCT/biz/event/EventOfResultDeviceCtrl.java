package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;

public class EventOfResultDeviceCtrl implements Parcelable {
    /**
     * 控制设备后的设备信息
     */
    public DeviceInfo deviceInfo;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.deviceInfo, flags);
    }

    public EventOfResultDeviceCtrl() {
    }

    protected EventOfResultDeviceCtrl(Parcel in) {
        this.deviceInfo = in.readParcelable(DeviceInfo.class.getClassLoader());
    }

    public static final Creator<EventOfResultDeviceCtrl> CREATOR = new Creator<EventOfResultDeviceCtrl>() {
        @Override
        public EventOfResultDeviceCtrl createFromParcel(Parcel source) {
            return new EventOfResultDeviceCtrl(source);
        }

        @Override
        public EventOfResultDeviceCtrl[] newArray(int size) {
            return new EventOfResultDeviceCtrl[size];
        }
    };
}
