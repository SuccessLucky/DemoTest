package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;

import java.util.List;

public class EventOfResultDeviceRights implements Parcelable {
    /**
     * 选中的设备权限
     */
    public List<DeviceInfo> deviceList;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.deviceList);
    }

    public EventOfResultDeviceRights() {
    }

    protected EventOfResultDeviceRights(Parcel in) {
        this.deviceList = in.createTypedArrayList(DeviceInfo.CREATOR);
    }

    public static final Creator<EventOfResultDeviceRights> CREATOR = new Creator<EventOfResultDeviceRights>() {
        @Override
        public EventOfResultDeviceRights createFromParcel(Parcel source) {
            return new EventOfResultDeviceRights(source);
        }

        @Override
        public EventOfResultDeviceRights[] newArray(int size) {
            return new EventOfResultDeviceRights[size];
        }
    };
}
