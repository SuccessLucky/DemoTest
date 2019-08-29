package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.kzksmarthome.SmartHouseYCT.biz.smart.scene.SceneDetailInfo;

import java.util.List;

public class EventOfResultDeviceScene implements Parcelable {
    /**
     * 选中的设备
     */
    public List<SceneDetailInfo> deviceList;
    /**
     * 是否是联动设备
     */
    public boolean isLD = false;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.deviceList);
        dest.writeByte(this.isLD ? (byte) 1 : (byte) 0);
    }

    public EventOfResultDeviceScene() {
    }

    protected EventOfResultDeviceScene(Parcel in) {
        this.deviceList = in.createTypedArrayList(SceneDetailInfo.CREATOR);
        this.isLD = in.readByte() != 0;
    }

    public static final Creator<EventOfResultDeviceScene> CREATOR = new Creator<EventOfResultDeviceScene>() {
        @Override
        public EventOfResultDeviceScene createFromParcel(Parcel source) {
            return new EventOfResultDeviceScene(source);
        }

        @Override
        public EventOfResultDeviceScene[] newArray(int size) {
            return new EventOfResultDeviceScene[size];
        }
    };
}
