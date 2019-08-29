package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FloorInfo;

public class EventOfResultDeleteFloor implements Parcelable {
    /**
     * 楼层信息
     */
    public FloorInfo floorInfo;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.floorInfo, flags);
    }

    public EventOfResultDeleteFloor() {
    }

    protected EventOfResultDeleteFloor(Parcel in) {
        this.floorInfo = in.readParcelable(FloorInfo.class.getClassLoader());
    }

    public static final Creator<EventOfResultDeleteFloor> CREATOR = new Creator<EventOfResultDeleteFloor>() {
        @Override
        public EventOfResultDeleteFloor createFromParcel(Parcel source) {
            return new EventOfResultDeleteFloor(source);
        }

        @Override
        public EventOfResultDeleteFloor[] newArray(int size) {
            return new EventOfResultDeleteFloor[size];
        }
    };
}
