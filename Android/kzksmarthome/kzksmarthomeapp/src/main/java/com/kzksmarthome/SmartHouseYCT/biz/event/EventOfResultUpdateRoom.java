package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

public class EventOfResultUpdateRoom implements Parcelable {
    /**
     * 楼层id
     */
    public int floorId;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.floorId);
    }

    public EventOfResultUpdateRoom() {
    }

    protected EventOfResultUpdateRoom(Parcel in) {
        this.floorId = in.readInt();
    }

    public static final Creator<EventOfResultUpdateRoom> CREATOR = new Creator<EventOfResultUpdateRoom>() {
        @Override
        public EventOfResultUpdateRoom createFromParcel(Parcel source) {
            return new EventOfResultUpdateRoom(source);
        }

        @Override
        public EventOfResultUpdateRoom[] newArray(int size) {
            return new EventOfResultUpdateRoom[size];
        }
    };
}
