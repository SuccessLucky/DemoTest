package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.RoomInfo;

public class EventOfResultAddRoom implements Parcelable {
    /**
     * 新增房间信息
     */
    public RoomInfo roomInfo;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.roomInfo, flags);
    }

    public EventOfResultAddRoom() {
    }

    protected EventOfResultAddRoom(Parcel in) {
        this.roomInfo = in.readParcelable(RoomInfo.class.getClassLoader());
    }

    public static final Creator<EventOfResultAddRoom> CREATOR = new Creator<EventOfResultAddRoom>() {
        @Override
        public EventOfResultAddRoom createFromParcel(Parcel source) {
            return new EventOfResultAddRoom(source);
        }

        @Override
        public EventOfResultAddRoom[] newArray(int size) {
            return new EventOfResultAddRoom[size];
        }
    };
}
