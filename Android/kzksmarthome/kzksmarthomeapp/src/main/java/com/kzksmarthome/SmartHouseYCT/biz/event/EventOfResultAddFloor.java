package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.device.FloorInfo;

public class EventOfResultAddFloor implements Parcelable {
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

    public EventOfResultAddFloor() {
    }

    protected EventOfResultAddFloor(Parcel in) {
        this.floorInfo = in.readParcelable(FloorInfo.class.getClassLoader());
    }

    public static final Creator<EventOfResultAddFloor> CREATOR = new Creator<EventOfResultAddFloor>() {
        @Override
        public EventOfResultAddFloor createFromParcel(Parcel source) {
            return new EventOfResultAddFloor(source);
        }

        @Override
        public EventOfResultAddFloor[] newArray(int size) {
            return new EventOfResultAddFloor[size];
        }
    };
}
