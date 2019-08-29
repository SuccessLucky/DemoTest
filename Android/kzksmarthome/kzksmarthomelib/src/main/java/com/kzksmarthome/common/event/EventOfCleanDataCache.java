package com.kzksmarthome.common.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * 清除数据事件
 * 
 * @author panrq
 * @createDate 2015-4-29
 *
 */
public class EventOfCleanDataCache implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public EventOfCleanDataCache() {
    }

    private EventOfCleanDataCache(Parcel in) {
    }

    public static final Parcelable.Creator<EventOfCleanDataCache> CREATOR = new Parcelable.Creator<EventOfCleanDataCache>() {
        public EventOfCleanDataCache createFromParcel(Parcel source) {
            return new EventOfCleanDataCache(source);
        }

        public EventOfCleanDataCache[] newArray(int size) {
            return new EventOfCleanDataCache[size];
        }
    };
}
