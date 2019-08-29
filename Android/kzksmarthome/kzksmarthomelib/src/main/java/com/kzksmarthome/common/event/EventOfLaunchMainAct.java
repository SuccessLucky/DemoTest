package com.kzksmarthome.common.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * 跳转到首页事件
 * 
 * @author panrq
 * @createDate 2015-4-29
 *
 */
public class EventOfLaunchMainAct implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public EventOfLaunchMainAct() {
    }

    private EventOfLaunchMainAct(Parcel in) {
    }

    public static final Parcelable.Creator<EventOfLaunchMainAct> CREATOR = new Parcelable.Creator<EventOfLaunchMainAct>() {
        public EventOfLaunchMainAct createFromParcel(Parcel source) {
            return new EventOfLaunchMainAct(source);
        }

        public EventOfLaunchMainAct[] newArray(int size) {
            return new EventOfLaunchMainAct[size];
        }
    };
}
