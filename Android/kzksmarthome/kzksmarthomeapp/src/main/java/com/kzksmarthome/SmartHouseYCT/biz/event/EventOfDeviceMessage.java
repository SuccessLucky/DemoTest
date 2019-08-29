package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.DeviceInfo;

/**
 * 设备数据本地更新
 * Created by jack on 2016/10/21.
 */
public class EventOfDeviceMessage implements Parcelable {
    public int position;
    public DeviceInfo deviceInfo;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.position);
        dest.writeParcelable(this.deviceInfo, flags);
    }

    public EventOfDeviceMessage() {
    }

    protected EventOfDeviceMessage(Parcel in) {
        this.position = in.readInt();
        this.deviceInfo = in.readParcelable(DeviceInfo.class.getClassLoader());
    }

    public static final Creator<EventOfDeviceMessage> CREATOR = new Creator<EventOfDeviceMessage>() {
        @Override
        public EventOfDeviceMessage createFromParcel(Parcel source) {
            return new EventOfDeviceMessage(source);
        }

        @Override
        public EventOfDeviceMessage[] newArray(int size) {
            return new EventOfDeviceMessage[size];
        }
    };
}
