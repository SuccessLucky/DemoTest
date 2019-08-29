package com.kzksmarthome.common.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * 跳转到注册页面事件
 * 
 * @author panrq
 * @createDate 2015-4-29
 *
 */
public class EventOfLaunchLogin implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public EventOfLaunchLogin() {
    }

    private EventOfLaunchLogin(Parcel in) {
    }

    public static final Parcelable.Creator<EventOfLaunchLogin> CREATOR = new Parcelable.Creator<EventOfLaunchLogin>() {
        public EventOfLaunchLogin createFromParcel(Parcel source) {
            return new EventOfLaunchLogin(source);
        }

        public EventOfLaunchLogin[] newArray(int size) {
            return new EventOfLaunchLogin[size];
        }
    };
}
