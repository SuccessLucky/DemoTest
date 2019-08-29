package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 上传成功事件
 */
public class EventOfCheckUpgrade implements Parcelable {

    public boolean hasNew;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(hasNew ? (byte) 1 : (byte) 0);
    }

    public EventOfCheckUpgrade() {
    }

    private EventOfCheckUpgrade(Parcel in) {
        this.hasNew = in.readByte() != 0;
    }

    public static final Creator<EventOfCheckUpgrade> CREATOR = new Creator<EventOfCheckUpgrade>() {
        public EventOfCheckUpgrade createFromParcel(Parcel source) {
            return new EventOfCheckUpgrade(source);
        }

        public EventOfCheckUpgrade[] newArray(int size) {
            return new EventOfCheckUpgrade[size];
        }
    };
}