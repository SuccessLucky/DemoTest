package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FloorInfo;

public class EventOfResultUpdateFloor implements Parcelable {
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

    public EventOfResultUpdateFloor() {
    }

    protected EventOfResultUpdateFloor(Parcel in) {
        this.floorInfo = in.readParcelable(FloorInfo.class.getClassLoader());
    }

    public static final Creator<EventOfResultUpdateFloor> CREATOR = new Creator<EventOfResultUpdateFloor>() {
        @Override
        public EventOfResultUpdateFloor createFromParcel(Parcel source) {
            return new EventOfResultUpdateFloor(source);
        }

        @Override
        public EventOfResultUpdateFloor[] newArray(int size) {
            return new EventOfResultUpdateFloor[size];
        }
    };
}
