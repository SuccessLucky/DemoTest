package com.kzksmarthome.SmartHouseYCT.biz.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 场景更新了
 * Created by jack on 2016/11/12.
 */

public class EventOfSceneUpdate implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public EventOfSceneUpdate() {
    }

    protected EventOfSceneUpdate(Parcel in) {
    }

    public static final Creator<EventOfSceneUpdate> CREATOR = new Creator<EventOfSceneUpdate>() {
        @Override
        public EventOfSceneUpdate createFromParcel(Parcel source) {
            return new EventOfSceneUpdate(source);
        }

        @Override
        public EventOfSceneUpdate[] newArray(int size) {
            return new EventOfSceneUpdate[size];
        }
    };
}
